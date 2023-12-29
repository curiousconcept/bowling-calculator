package com.bowling.game.second.frame;

import com.bowling.game.second.frame.bonus.*;
import com.bowling.game.second.frame.util.Pair;

import java.util.Optional;

public class StdFrame extends LinkedFrame implements BonusScoreCompleteFramesListener {

    private BonusStateTracker bonusStateTracker;

    boolean roll(int pins) {

        if (getFirstAttempt().isEmpty()) {
            setFirstAttempt(pins);

            if (isStrike(getFirstAttempt().orElseThrow())) {
                initializeBonusStateTracker(BonusStateTrackerImpl.ofStrike());
            } else {
                return false;
            }
        } else if (getSecondAttempt().isEmpty()) {
            setSecondAttempt(pins);

            if (isSpare(true)) {
                initializeBonusStateTracker(BonusStateTrackerImpl.ofSpare());
            }
        }

        updateTotalScore();

        return true;
    }

    @Override
    int getFrameTotal() {
        return getBonus() + getFirstAttempt().orElse(0) + getSecondAttempt().orElse(0);
    }

    private int getBonus() {
        return getBonusStateTracker().map(BonusStateTracker::getTotalBonusCollectedSoFar).orElse(0);
    }

    @Override
    public void subscribe(int numberOfBonusBowls) {
        LinkedFrame currentFrame = this;

        for (int i = 0; i < numberOfBonusBowls && currentFrame.nextFrame().isPresent(); i++) {
            currentFrame = currentFrame.nextFrame().get();
            currentFrame.acceptSubscriber(this);
        }
    }

    @Override
    public void acceptBonus(BonusScoreCompleteFrameProvider bonusScoreCompleteFrameProvider) {

        BonusScoreCompleteFrame bowlBonusContainer = bonusScoreCompleteFrameProvider.provideBowlBonusContainer();


        this.nextFrame().ifPresent(stdFrame -> getBonusStateTracker().flatMap(tracker -> tracker.getBonusToApply(bowlBonusContainer))
                                                                     .map(bonus -> {
                                                                         setTotalScore(getTotalScore() + bonus);
                                                                         return getTotalScore();
                                                                     })
                                                                     .ifPresent(stdFrame::updateCurrentAndDownstream));
    }

    Optional<BonusStateTracker> getBonusStateTracker() {
        return Optional.ofNullable(bonusStateTracker);
    }

    private void initializeBonusStateTracker(BonusStateTracker tracker) {
        this.bonusStateTracker = tracker;
        getBonusStateTracker().map(BonusStateTracker::getNumberOfBonusBowls).ifPresent(this::subscribe);
    }


    /**
     * Returns head as first parameter and tail as second
     *
     * @param totalLinkedFrames
     * @return
     */
    public static Pair<StdFrame, StdFrame> createLinkedNumberOfFrames(int totalLinkedFrames) {
        StdFrame head = new StdFrame();
        StdFrame current = head;

        int excludingHeadLinkedFrames = totalLinkedFrames - 1;

        for (int i = 0; i < excludingHeadLinkedFrames; i++) {
            StdFrame nextObject = new StdFrame();
            current.setFrame(nextObject, true);
            current = nextObject;
        }
        return Pair.of(head, current);
    }

    private void updateTotalScore() {
        int totalScore = getTotalScore() + getFrameTotal();
        setTotalScore(totalScore);
        nextFrame().ifPresent(stdFrame -> stdFrame.updateCurrentAndDownstream(getTotalScore()));
    }

}
