package com.security;

import com.security.task1.LcgCracker;
import com.security.task2.MtCracker;
import com.security.task3.BetterMtCracker;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        LcgCracker.main(new String[]{});
        MtCracker.main(new String[]{});
        BetterMtCracker.main(new String[]{});
    }
}
