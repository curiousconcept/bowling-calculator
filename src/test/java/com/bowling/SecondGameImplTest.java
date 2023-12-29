package com.bowling;

import com.bowling.game.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SecondGameImplTest  {

    private Game game;

    @BeforeEach
    void setUp() {
        game = new com.bowling.game.second.SecondGameImpl();
    }

    @Test
    void checkTwoScores() {

        game.roll(4);
        game.roll(2);

        assertEquals(6, game.score());
    }

    @Test
    void checkTwoScoresAcrossTwoFrames() {

        game.roll(4);
        game.roll(5);
        game.roll(2);
        game.roll(3);

        assertEquals(14, game.score());
    }


    @Test
    void checkSpareGetsBonusOnlyForASingleNextRoll() {

        game.roll(4);
        game.roll(6);

        game.roll(2);
        game.roll(1);

        assertEquals(15, game.score());
    }

    @Test
    void checkSingleStrike() {
        game.roll(10);
        assertEquals(10, game.score());
    }

    @Test
    @DisplayName("given I roll 'X' 9 times and for the last frame: strike and strike and third attempt is a miss")
    void checkLastWindowWithTwoStrikesAndMissFollowingStrikes() {

        rollStrikesUpToLastFrame();

        game.roll(10);
        game.roll(10);
        game.roll(0);

        assertEquals(290, game.score());
    }


    @Test
    @DisplayName("given I roll 'X' 9 times and for the last frame: spare of 5 and 5 and third attempt is a miss")
    void checkLastWindowWithSpareAndMissFollowingStrikes() {

        rollStrikesUpToLastFrame();
        game.roll(5);
        game.roll(5);
        game.roll(0);

        assertEquals(265, game.score());
    }

    @Test
    void checkNinthFailBowlsFrameCancelsOutBonusesForLast() {
        rollSamePinsNumberOfTimes(10, 8);

        game.roll(0);
        game.roll(0);

        game.roll(10);
        game.roll(10);
        game.roll(10);

        assertEquals(240, game.score());
    }

    @Test
    void checkPerfectScore() {

        rollSamePinsNumberOfTimes(10, 12);

        assertEquals(300, game.score());
    }

    @Test
    @DisplayName("given I roll X and X and 4 and 2 then I expect total score of 46")
    void checkDoubleStrikesFollowedByTwoScores_calculateCorrectBonus() {

        game.roll(10);
        game.roll(10);
        game.roll(4);
        game.roll(2);

        assertEquals(46, game.score());
    }

    @Test
    void checkDoubleStrikes() {

        game.roll(10);
        game.roll(10);

        assertEquals(30, game.score());
    }

    @Test
    void checkTripleStrikes() {

        game.roll(10);
        game.roll(10);
        game.roll(10);

        assertEquals(60, game.score());
    }

    @Test
    @DisplayName("given I roll X and 1 and 3 then I expect total score of 18")
    void checkStrikeFollowedByTwoRegularScores_calculatesCorrectBonusUsingBothRegularScores() {

        game.roll(10);
        game.roll(1);
        game.roll(3);

        assertEquals(18, game.score());
    }

    @Test
    @DisplayName("given I roll zero bowls up to last frame, when I hit 4 and 5 the game is over")
    void checkThirdShotIsForbiddenWhenIDontSpareInTheLastFrame() {

        rollZeroesUpToTheLastFrame();
        game.roll(4);
        game.roll(5);

        IllegalStateException thrown = assertThrows(
                IllegalStateException.class,
                () -> game.roll(1),
                "Expected game to be over"
        );

        assertTrue(thrown.getMessage().contains("finished"));
        assertTrue(thrown.getMessage().contains("9"));


        assertEquals(9, game.score());
    }

    @Test
    @DisplayName("given I roll zero bowls up to last frame, when I hit 5 and a spare I am allowed third frame shot")
    void checkThirdShotIsAllowedWhenISpareInTheLastFrame() {

        rollZeroesUpToTheLastFrame();

        game.roll(4);
        game.roll(6);
        game.roll(1);

        assertEquals(11, game.score());
    }


    @Test
    @DisplayName("given I roll zero bowls up to last frame, when I hit 5 and a spare I am allowed third frame shot but not fourth")
    void checkFourthShotIsForbiddenWhenISpareInTheLastFrame() {

        checkThirdShotIsAllowedWhenISpareInTheLastFrame();


        IllegalStateException thrown = assertThrows(
                IllegalStateException.class,
                () -> game.roll(1),
                "Expected game to be over"
        );

        assertTrue(thrown.getMessage().contains("finished"));
        assertTrue(thrown.getMessage().contains("11"));

        assertEquals(11, game.score());
    }

    @Test
    @DisplayName("given I roll zero bowls up to last frame, when I hit three strikes then there are no bonuses")
    void checkNoBonusesForLastFrameWhenFrameBeforeLastHasZeroes() {

        rollZeroesUpToTheLastFrame();

        game.roll(10);
        game.roll(10);
        game.roll(10);


        assertEquals(30, game.score());
    }

    @Test
    void checkMegaGameContainingDifferentCombos(){
        game.roll(10);
        game.roll(10);
        game.roll(10);

        game.roll(6);
        game.roll(4);

        game.roll(0);
        game.roll(4);

        assertEquals(90, game.score());
    }


    private void rollZeroesUpToTheLastFrame() {
        rollSamePinsNumberOfTimes(0, 18);
    }

    private void rollStrikesUpToLastFrame() {
        rollSamePinsNumberOfTimes(10, 9);
    }

    private void rollSamePinsNumberOfTimes(int pins, int timesToRoll) {
       int counter = 0;

        for (int i = 0; i < timesToRoll; i++) {
            System.out.println("CALLING ROLL " + ++counter +" with value " + pins);
            game.roll(pins);
        }
    }

}