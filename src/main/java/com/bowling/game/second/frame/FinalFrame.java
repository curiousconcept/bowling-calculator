package com.bowling.game.second.frame;

import java.util.Optional;

public class FinalFrame extends LinkedFrame {

    private Integer thirdAttempt;


    @Override
    boolean roll(int pins) {

        if(getFirstAttempt().isEmpty()){

            setFirstAttempt(pins);
            return false;
        }

        if(getSecondAttempt().isEmpty()){
            setSecondAttempt(pins);

            if(super.isSpare(false) || super.isStrike(pins))
                return false;
        }

        else if(getThirdAttempt().isEmpty()){
            setThirdAttempt(pins);
        }

        setTotalScore(getTotalScore() + getFrameTotal());

        return true;
    }

    @Override
    public void defaultActionOnAccessingCompleteFrameIllegally() {
        throw new IllegalStateException("Game is already finished, final score is: " + getTotalScore());
    }

    @Override
    int getFrameTotal() {
        return getFirstAttempt().orElse(0)+getSecondAttempt().orElse(0)+getThirdAttempt().orElse(0);
    }

    public Optional<Integer> getThirdAttempt() {
        return Optional.ofNullable(thirdAttempt);
    }

    public void setThirdAttempt(Integer thirdAttempt) {
        this.thirdAttempt = thirdAttempt;
    }

    public static FinalFrame appendNewTail(LinkedFrame linkedFrame) {
        FinalFrame finalFrame = new FinalFrame();

        linkedFrame.setFrame(finalFrame, true);

        return finalFrame;
    }
}
