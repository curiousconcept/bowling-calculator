package com.bowling.game.second.frame;

import com.bowling.game.second.frame.bonus.BonusCompleteFrameImpl;
import com.bowling.game.second.frame.bonus.BonusScoreCompleteFrame;
import com.bowling.game.second.frame.bonus.BonusScoreCompleteFrameProvider;
import com.bowling.game.second.frame.bonus.BonusScoreCompleteFramesListener;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

public abstract class LinkedFrame implements DynamicScoreCalculator, BonusScoreCompleteFrameProvider {
    private int index=1;

    private int totalScore;

    private Integer firstAttempt;
    private Integer secondAttempt;

    private LinkedFrame frame;

    private boolean frameComplete;

    private final Queue<BonusScoreCompleteFramesListener> scoreSubscribers = new LinkedList<>();

    abstract boolean roll(int pins);

    abstract int getFrameTotal();

    public int getTotalScore(){
        return totalScore;
    }

    void setFrame(LinkedFrame frame, boolean autoIncrementIndex) {
        if(autoIncrementIndex)
            frame.setIndex(this.index+1);
        this.frame = frame;
    }

    private void setIndex(int index){
        this.index = index;
    }

    boolean isFrameComplete() {
        return frameComplete;
    }

    public Optional<LinkedFrame> nextFrame() {
        return Optional.ofNullable(frame);
    }

    public boolean rollAndNotify(int pins){

        if(frameComplete)
            defaultActionOnAccessingCompleteFrameIllegally();

        this.frameComplete = roll(pins);
        System.out.println(this.index + " " + this.getClass().getSimpleName() + " " + this.getFrameTotal() + " SEC_AT " +
        " First Attempt " + getFirstAttempt() + " Second Attempt "+ getSecondAttempt());

        if (frameComplete) {
            notifySubscribersOfFrameComplete();
        }

        return frameComplete;
    }

    public void defaultActionOnAccessingCompleteFrameIllegally(){
        throw new RuntimeException(new IllegalAccessException("Frame is already finished and result was already dispatched!"));
    }



    boolean isStrike(int pins) {
        return pins == 10;
    }

    boolean isSpare(boolean withValidation) {
        int sum =  this.getFirstAttempt().orElseThrow() + this.getSecondAttempt().orElseThrow();

        if(withValidation && sum>10)
            throw new IllegalArgumentException("Illegal pin number supplied, total across two attempts can't be more than 10");

        return sum == 10;
    }

    public int getIndex() {
        return index;
    }

    Queue<BonusScoreCompleteFramesListener> getScoreSubscribers() {
        return scoreSubscribers;
    }

    @Override
    public void notifySubscribersOfFrameComplete() {
        while (!scoreSubscribers.isEmpty()){
            scoreSubscribers.poll().acceptBonus(this);
        }
    }

    int recalculateTotal(int upstreamTotal) {

        totalScore = upstreamTotal + getFrameTotal();

        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    @Override
    public void acceptSubscriber(BonusScoreCompleteFramesListener bonusScoreCompleteFramesListener) {
        this.getScoreSubscribers().offer(bonusScoreCompleteFramesListener);
    }

    @Override
    public BonusScoreCompleteFrame provideBowlBonusContainer() {
        if(!this.isFrameComplete())
            throw new IllegalStateException(String.format("Asked to provide bowl bonus from an unfinished frame: %s " +
                                                          "first attempt: %s second attempt: %s",
                                                          this.getIndex(), firstAttempt, secondAttempt));


        return new BonusCompleteFrameImpl(this.firstAttempt, this.secondAttempt);

    }

    @Override
    public void updateCurrentAndDownstream(int updatedTotal) {

        int recalculatedTotal = recalculateTotal(updatedTotal);

        nextFrame().ifPresent(frame -> frame.updateCurrentAndDownstream(recalculatedTotal));
    }



    public Optional<Integer> getFirstAttempt() {
        return Optional.ofNullable(firstAttempt);
    }

    public Optional<Integer> getSecondAttempt() {
        return Optional.ofNullable(secondAttempt);
    }

    public void setFirstAttempt(int firstAttempt) {
        this.firstAttempt = firstAttempt;
    }

    public void setSecondAttempt(int secondAttempt) {
        this.secondAttempt = secondAttempt;
    }
}
