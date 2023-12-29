package com.bowling.game.second.frame.bonus;

import com.bowling.game.second.frame.util.Util;

import java.util.Optional;

/**
 * This is not general ScoreFrame! There will be deviation to methods between intermediate frames and final frame.
 * Final frame should implement this interface only for the purpose of providing predictable bonus to previous frames.
 *
 * For this reason, the sum should not be validated here as we can have [x,x,1] which would result in sum = 20 > 10
 *
 * The interface contract also expects that the frame is complete
 */
public interface BonusScoreCompleteFrame {


    default boolean isStrike(){
        return Util.isStrike(getFirst());
    }

    default long bowlsCount(){

       return getSecond().isPresent() ? 2: 1;
    }

    int getFirst();
    Optional<Integer> getSecond();

    default int sum(){

        int second = getSecond().orElseThrow(() -> new IllegalStateException("Expected sum when second bowl is absent. "));

        return getFirst() + second;
    }
}
