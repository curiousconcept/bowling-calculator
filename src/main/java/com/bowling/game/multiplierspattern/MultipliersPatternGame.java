package com.bowling.game.multiplierspattern;

import com.bowling.game.Game;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * This solution avoids backtracking to strike frames requiring bonus upgrade as well their retrospective subsequent frame downstream score propagation.
 * This leads to main functional downside it only returns the latest frame score correctly. It won't return historical frame score
 * correctly(even if we stored frame scores). The upside is that logic is very simple as it only follows the patterns below
 * (I don't guarantee that I identified 100% of them, each missed pattern would result in a bug), you can use:
 *
 * https://www.sportcalculators.com/bowling-score-calculator
 *
 * to check the patterns below:
 *
 *          CURRENT_FRAME_ROLL  NEXT_ROLL_BONUS MULTIPLIER
 *  [x],        [x]                   -> 3x
 *  [5,4],      [x]                   -> 2x
 *  [x]         [5,                   -> 2x
 *  ...         [5,/]                 -> 2x
 *
 * End frame patterns:
 *
 *  FRAME 9    CURRENT_FRAME_ROLL(LAST) NEXT_ROLL_BONUS MULTIPLIER
 *  [5,0]       [x,                   -> 1x
 *  [0,5]       [x,                   -> 2x
 *  [x]         [x,                   -> 2x
 *  [x]         [x,x,                 -> 1x
 *
 *
 *  The spare doesn't matter in last frame for the third attempt. Third attempt is never calculated with any bonus multiplier.
 *
 *  This denotes general idea, it's very mundane and test harness kept increasing as I discovered edge cases, until I switched to developing something more robust as
 *  I am still not sure if I caught all edge cases. But I would believe 99% of cases are captured, there one which could be missing
 *  is possibly near the last frame transition as it was very complex one as we can see some mid frames patterns are breaking, like consecutive strikes.
 *
 *  The code itself is rudimentary, but helped me immensely understand the game mechanics and gave a better
 *  idea how to handle it. I am now trying LinkedList of Frames and some partial use of Observer pattern which is what I am working on right now.
 *
 */
public class MultipliersPatternGame implements Game {

    private static final int DEFAULT_BONUS_MULTIPLIER = 1;
    private static final int DOUBLE_BONUS_MULTIPLIER = 2;
    private static final int TRIPLE_BONUS_MULTIPLIER = 3;

    private int totalScore;

    private int mutableBonusRollScoreMultiplier = DEFAULT_BONUS_MULTIPLIER;
    private final MutableCurrentFrameState currentFrameState = new MutableCurrentFrameState();

    private final List<Integer> rollTracker = new ArrayList<>();

    @Override
    public void roll(int pins) {

        if (gameIsFinished(currentFrameState, pins)) {
            throw new IllegalStateException("Game is already finished, final score is: " + totalScore);
        }

        validate(pins);

        totalScore += pins * mutableBonusRollScoreMultiplier;
        processWithinFrame(pins);
        rollTracker.add(pins);
    }

    private void processWithinFrame(int pins) {

        if (currentFrameState.isLastFrame()) {
            handleLastFrame(pins);
            return;
        }

        handleFrame(pins);
    }

    private void handleLastFrame(int pins) {
        if (currentFrameState.isFirstAttempt() && ((isStrike(pins) && isPreviousNotZero() || isPreviousStrike()) )) {
            mutableBonusRollScoreMultiplier = DOUBLE_BONUS_MULTIPLIER;
        } else
            mutableBonusRollScoreMultiplier = DEFAULT_BONUS_MULTIPLIER;

        currentFrameState.nextAttempt();
    }

    private void handleFrame(int pins) {
        if (currentFrameState.isNotFirstAttempt()) {
            mutableBonusRollScoreMultiplier = isSpare(pins) ? DOUBLE_BONUS_MULTIPLIER : DEFAULT_BONUS_MULTIPLIER;
            currentFrameState.moveToNextFrame();
            return;
        }

        if (isStrike(pins)) {
            mutableBonusRollScoreMultiplier = isPreviousStrike() ? TRIPLE_BONUS_MULTIPLIER : DOUBLE_BONUS_MULTIPLIER;
            currentFrameState.moveToNextFrame();
            return;
        }

        currentFrameState.nextAttempt();
        mutableBonusRollScoreMultiplier = isPreviousStrike() ? DOUBLE_BONUS_MULTIPLIER : DEFAULT_BONUS_MULTIPLIER;
    }

    @Override
    public int score() {
        return totalScore;
    }


    private void validate(int pins) {
        if (pins > 10)
            throw new IllegalArgumentException("A single bowl can only hit ten pins max, illegal supplied pins count: " + pins);

        if (currentFrameState.isNotLastFrame() && currentFrameState.isNotFirstAttempt() && !rollTracker.isEmpty()) {

            int previousRoll = rollTracker.get(rollTracker.size() - 1);

            int sumOfPreviousAndCurrent = previousRoll + pins;

            if (sumOfPreviousAndCurrent > 10) {
                throw new IllegalArgumentException(String.format("A second bowl attempt and first attempt cannot exceed sum of 10, " +
                                                                 "illegal supplied pins count: %s and previous bowl: %s, illegal sum is: %s",
                                                                 pins, previousRoll, sumOfPreviousAndCurrent));
            }
        }

        // TODO validate last frame

    }


    private boolean gameIsFinished(MutableCurrentFrameState currentFrameState, int pins) {

        if(currentFrameState.isNotLastFrame()) {
            return false;
        }

        if (currentFrameState.getAttempt()==4){
            return true;
        }

        if (!isPreviousStrike() && currentFrameState.getAttempt()==3 && !isPreviousSpare()){
            return true;
        }

        return false;
    }

    private static boolean isStrike(int pins) {
        return pins == 10;
    }

    private boolean isPreviousStrike() {
        return !rollTracker.isEmpty() && rollTracker.get(rollTracker.size() - 1) == 10;
    }

    private boolean isPreviousNotZero() {
        return !rollTracker.isEmpty() && rollTracker.get(rollTracker.size() - 1) > 0;
    }

    private boolean isSpare(int pins) {
        return !rollTracker.isEmpty() && rollTracker.get(rollTracker.size() - 1) + pins == 10;
    }

    private boolean isPreviousSpare() {
        return !rollTracker.isEmpty() && rollTracker.get(rollTracker.size() - 1) + rollTracker.get(rollTracker.size() - 2) == 10;
    }
}