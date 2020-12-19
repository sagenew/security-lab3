package com.security.task2;

import com.security.CasinoClient;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

public class MtCracker {
    private final static String MODE_MT = "Mt";
    public static void main(String[] args) throws IOException {
        CasinoClient client = new CasinoClient();
        long playerId = new Random().nextLong();
        client.createAccount(playerId);

        long creationTime = new Date().getTime() / 1000;
        long actualNum = client.makeBet(MODE_MT, playerId, 1,
                1, true);

        MTRandom mt = null;
        for (long timestamp = creationTime - 100; timestamp < creationTime + 100; timestamp++) {
            mt = new MTRandom(timestamp);
            if (mt.next() == actualNum) break;
        }

        long predictedNum;
        boolean printResponse = false;
        for (int i = 0; i <= 10; i++) {
            assert mt != null;
            predictedNum = mt.next();
            if(i == 10) printResponse = true;
            actualNum = client.makeBet(MODE_MT, playerId, 100, predictedNum, printResponse);
            System.out.println("Predicted number :\t" + predictedNum + ",\tactual bet :\t" + actualNum +
                    ".\tBets are equal : " + (predictedNum == actualNum) + ", money = " + client.getMoney());
        }
    }
}
