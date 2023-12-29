package com.bowling.game.second.frame.bonus;

import java.util.Optional;

public class BonusCompleteFrameImpl implements BonusScoreCompleteFrame {

    private final int first;
    private final Integer second;

    public BonusCompleteFrameImpl(int first, Integer second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public int getFirst() {
        return first;
    }

    @Override
    public Optional<Integer> getSecond() {
        return Optional.ofNullable(second);
    }

    @Override
    public String toString() {
        return "{" +
               "first=" + first +
               ", second=" + second +
               '}';
    }
}
