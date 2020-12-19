package com.security.task1;

import com.security.CasinoClient;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;

public class LcgCracker {
    private final static String MODE_LCG = "Lcg";
    private static final long m = (long) Math.pow(2, 32);

    public static void main(String[] args) throws IOException {
        CasinoClient client = new CasinoClient();
        long playerId = new Random().nextLong();
        client.createAccount(playerId);

        long num1 = client.makeBet(MODE_LCG, playerId, 1, 1, false);
        long num2 = client.makeBet(MODE_LCG, playerId, 1, 1, false);
        long num3 = client.makeBet(MODE_LCG, playerId, 1, 1, false);

        /*  x_2 - x_1 = x1 * a - x0 * a    (mod m)
            x_2 - x_1 = a * (x1 - x0)      (mod m)
            a = (x_2 - x_1)/(x_1 - x_0)    (mod m)    */
        long a = ((num3 - num2) * modInverse(num2 - num1, m)) % m;

        /*  x1 = x0 * a  + c  (mod m)
            c  = x1 - x0 * a  (mod m) */
        long c = (num2 - num1 * a) % m;

        long predictedNum = num3, actualNum;
        for (int i = 0; i < 10; i++) {
            predictedNum = nextNumber(a,c,m, predictedNum);
            actualNum = client.makeBet(MODE_LCG, playerId, 10, predictedNum, false);
            System.out.println("Predicted bet :\t" + predictedNum + ",\tactual bet :\t" + actualNum +
                    ".\tBets are equal : " + (predictedNum == actualNum) + ", money = " + client.getMoney());
        }
    }

    private static long modInverse(long a, long b) {
        BigInteger aB = new BigInteger(String.valueOf(a));
        BigInteger bB = new BigInteger(String.valueOf(b));

        return aB.modInverse(bB).longValue();
    }

    public static long nextNumber(long a, long c, long m, long seed) {
        // x1 = x0 * a + c (mod m)
        return (int)((seed * a + c) % m);
    }
}
