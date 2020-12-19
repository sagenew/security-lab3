package com.security.task2;

import com.security.CasinoClient;

import java.io.IOException;
import java.time.Instant;
import java.util.Random;

public class MtCracker {
    private final static String MODE_MT = "Mt";
    public static void main(String[] args) throws IOException {
        CasinoClient client = new CasinoClient();
        long playerId = new Random().nextLong();
        client.createAccount(playerId);

        long start = Instant.now().getEpochSecond();
        long actualNum = client.makeBet(MODE_MT, playerId, 1,
                1, true);
        long end = Instant.now().getEpochSecond();

        MTRandom crackedMt = null;
        for (long timestamp = start - 1000; timestamp < end + 1000; timestamp++) {
            MTRandom mt = new MTRandom(timestamp);
            if (mt.next() == actualNum) {
                crackedMt = mt;
                break;
            }
        }

        long predictedNum;
        boolean printResponse = false;
        for (int i = 0; i <= 10; i++) {
            assert crackedMt != null;
            predictedNum = crackedMt.next();
            if(i == 10) printResponse = true;
            actualNum = client.makeBet(MODE_MT, playerId, 100, predictedNum, printResponse);
            System.out.println("Predicted number :\t" + predictedNum + ",\tactual bet :\t" + actualNum +
                    ".\tBets are equal : " + (predictedNum == actualNum) + ", money = " + client.getMoney());
        }
    }
}
