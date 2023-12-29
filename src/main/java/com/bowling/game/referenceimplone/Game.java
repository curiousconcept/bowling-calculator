package com.bowling.game.referenceimplone;

// NOT MINE IMPLEMENTATION

public class Game implements com.bowling.game.Game
{
    public int score()
    {
        return scoreForFrame(itsCurrentFrame);
    }
    public void roll(int pins)
    {
        itsScorer.addThrow(pins);
        adjustCurrentFrame(pins);
    }
    private void adjustCurrentFrame(int pins)
    {
        if (firstThrowInFrame)
        {
            if (!adjustFrameForStrike(pins))
                firstThrowInFrame = false;
        }
        else
        {
            firstThrowInFrame=true;
            advanceFrame();
        }
    }
    private boolean adjustFrameForStrike(int pins)
    {
        if (pins == 10)
        {
            advanceFrame();
            return true;


        }
        return false;
    }
    private void advanceFrame()
    {
        itsCurrentFrame = Math.min(10, itsCurrentFrame + 1);
    }
    public int scoreForFrame(int theFrame)
    {
        return itsScorer.scoreForFrame(theFrame);
    }
    private int itsCurrentFrame = 0;
    private boolean firstThrowInFrame = true;
    private Scorer itsScorer = new Scorer();
}
