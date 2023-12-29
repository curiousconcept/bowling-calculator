package com.bowling.game.second;

import com.bowling.game.Game;
import com.bowling.game.second.frame.FinalFrame;
import com.bowling.game.second.frame.LinkedFrame;
import com.bowling.game.second.frame.StdFrame;
import com.bowling.game.second.frame.util.Pair;

public class SecondGameImpl implements Game {


    private LinkedFrame currentFrame;

    boolean gameOver;

    public SecondGameImpl() {
        Pair<StdFrame,StdFrame> frames = StdFrame.createLinkedNumberOfFrames(9);
        FinalFrame.appendNewTail(frames.getSecond());
        currentFrame = frames.getFirst();
    }

    @Override
    public void roll(int pins) {

        if(gameOver){
            System.out.println("Game over your final score is: " + currentFrame.getTotalScore());
        }

        boolean frameFinished = currentFrame.rollAndNotify(pins);


        if (!frameFinished)
            return;

        if(currentFrame.nextFrame().isEmpty()) {
            gameOver = true;
            return;
        }

        currentFrame = currentFrame.nextFrame().get();
    }

    @Override
    public int score() {
        return currentFrame.getTotalScore();
    }

    public static void main(String[] args) {




    }
}
