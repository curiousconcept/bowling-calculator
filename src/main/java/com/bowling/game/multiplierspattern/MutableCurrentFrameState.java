package com.bowling.game.multiplierspattern;

public class MutableCurrentFrameState {
    private int frame = 1;
    private int attempt = 1;

    public boolean isFirstAttempt(){
        return attempt==1;
    }

    public boolean isNotFirstAttempt(){
        return !isFirstAttempt();
    }

    public void moveToNextFrame() {
        frame++;
        attempt = 1;
    }

    public void nextAttempt(){
        attempt++;
    }

    public boolean isLastFrame(){
        return frame>=10;
    }

    public boolean isNotLastFrame(){
        return !isLastFrame();
    }

    public int getFrame() {
        return frame;
    }

    public int getAttempt() {
        return attempt;
    }
}
