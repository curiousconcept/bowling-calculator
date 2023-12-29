package com.bowling.game.second.frame.util;

public class Util {

    private Util(){

    }

    public static boolean isSpare(int first, int second) {

        int sum = first + second;

        if (sum > 10)
            throw new IllegalArgumentException("Supplied: " + first + " and: " + second + " for spare check. Max 10 allowed");

        return sum == 10;
    }

    public static boolean isStrike(int first) {
        if (first > 10)
            throw new IllegalArgumentException("Supplied: " + first + " for strike check. Max 10 allowed");

        return first == 10;
    }
}
