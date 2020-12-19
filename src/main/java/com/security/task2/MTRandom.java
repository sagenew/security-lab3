package com.security.task2;

public class MTRandom {
    private static final int N = 624; // register size
    private static final int W = 32; // computer word size
    private static final int M = 397; // middle word, an offset used in the recurrence relation defining the series x, 1 ? m < n
    private static final int A = 0x9908B0DF; // coefficients of the rational normal form twist matrix
    private static final int U = 11; // additional Mersenne Twister tempering bit shift/mask
    private static final int L = 18; // additional Mersenne Twister tempering bit shift
    private static final int S = 7; // TGFSR(R) tempering bit shift
    private static final int B = 0x9D2C5680; // TGFSR(R) tempering bitmask
    private static final int T = 15; // TGFSR(R) tempering bit shift
    private static final int C = 0xEFC60000; // TGFSR(R) tempering bitmask
    private static final int F = 1812433253; // constant f forms another parameter to the generator

    private static final int LOWER_MASK = 0x80000000; // (1 << r) - 1
    private static final int UPPER_MASK = 0x7fffffff; // lowest w bits of (not lower_mask)

    private final int[] state;
    int index;

    //initialize the generator from a seed
    public MTRandom(long seed) {
        state = new int[N];
        index = N;
        state[0] = Long.valueOf(seed).intValue();
        for (int i = 1; i < N; i++) {
            state[i] = F * ( state[i - 1] ^ ( state[i - 1] >>> (W - 2) ) ) + i;
        }
    }

    //initialize the generator from a state
    public MTRandom(int[] state) {
        index = N;
        this.state = state;
    }

    // Extract a tempered value based on MT[index]
    // calling twist() every n numbers
    public long next() {
        if (index == N) twist();

        int x = state[index];

        x ^= ( x >>> U);
        x ^= ((x << S ) & B);
        x ^= ((x << T ) & C);
        x ^= ( x >>> L);

        index++;
        return Integer.toUnsignedLong(x);
    }

    // Generate the next n values from the series x_i
    private void twist() {
        for (int i = 0; i < N - M; i++) {
            int x = (state[i] & LOWER_MASK) | (state[i + 1] & UPPER_MASK);
            state[i] = state[i + M] ^ (x >>> 1) ^ ((x & 1) * A);
        }

        for (int i = N - M; i < N - 1; i++) {
            int x = (state[i] & LOWER_MASK) | (state[i + 1] & UPPER_MASK);
            state[i] = state[i - (N - M)] ^ (x >>> 1) ^ ((x & 1) * A);
        }

        int x = (state[N - 1] & LOWER_MASK) | (state[0] & UPPER_MASK);
        state[N - 1] = state[M - 1] ^ (x >>> 1) ^ ((x & 1) * A);
        index = 0;
    }

    public static int unTemp(int x) {
        x ^= x >>> L;
        x ^= (x << T) & C;

        int temp = x;
        for (int i = 0; i < 5; i++) {
            temp = x ^ ((temp << 7) & B);
        }

        int k = temp >>> U;
        int l = temp ^ k;
        int m = l >>> 11;

        return temp ^ m;
    }
}
