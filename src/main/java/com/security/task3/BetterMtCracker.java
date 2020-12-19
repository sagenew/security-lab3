package com.security.task3;

import com.security.CasinoClient;
import com.security.task2.MTRandom;

import java.io.IOException;
import java.util.Random;

public class BetterMtCracker {
    private final static String MODE_BetterMT = "BetterMt";

    public static void main(String[] args) throws IOException {
        CasinoClient client = new CasinoClient();
        long playerId = new Random().nextLong();
        client.createAccount(playerId);

        int [] mtState = new int[624];
        long actualNum;
        for (int i = 0; i < 624; i++) {
            actualNum = client.makeBet(MODE_BetterMT, playerId, 1,1,false);
            mtState[i] = MTRandom.unTemp((int) actualNum);
        }

        MTRandom crackedMt = new MTRandom(mtState);
        long predictedNum;
        boolean printResponse = false;
        for (int i = 0; i <= 10; i++) {
            predictedNum = crackedMt.next();
            if(i == 10) printResponse = true;
            actualNum = client.makeBet(MODE_BetterMT, playerId, 100, predictedNum, printResponse);
            System.out.println("Predicted number :\t" + predictedNum + ",\tactual bet :\t" + actualNum +
                    ".\tBets are equal : " + (predictedNum == actualNum) + ", money = " + client.getMoney());
        }
    }
}
