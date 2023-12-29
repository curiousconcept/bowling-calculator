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
                this.bonusStateTracker = BonusStateTrackerImpl.ofStrike();
                this.getBonusStateTracker().map(BonusStateTracker::getNumberOfBonusBowls).ifPresent(this::subscribe);
            } else {
                return false;
            }
        } else if (getSecondAttempt().isEmpty()) {
            setSecondAttempt(pins);

            if (isSpare(true)) {
                this.bonusStateTracker = BonusStateTrackerImpl.ofSpare();
                this.getBonusStateTracker().map(BonusStateTracker::getNumberOfBonusBowls).ifPresent(this::subscribe);
            }
        }

        // check if this is a problem for long running dry '0' game, if it is we'd need to use frame index
        if (getTotalScore() == 0)
            setTotalScore(getFrameTotal());
        else
            setTotalScore(getTotalScore() + getFrameTotal());

        this.nextFrame().ifPresent(stdFrame -> stdFrame.updateCurrentAndDownstream(getTotalScore()));

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
        int i = 0;

        while (i < numberOfBonusBowls && currentFrame.nextFrame().isPresent()) {
            currentFrame = currentFrame.nextFrame().get();
            currentFrame.acceptSubscriber(this);
            i++;
        }
    }

    @Override
    public void acceptBonus(BonusScoreCompleteFrameProvider bonusScoreCompleteFrameProvider) {

        BonusScoreCompleteFrame bowlBonusContainer = bonusScoreCompleteFrameProvider.provideBowlBonusContainer();


        this.nextFrame().ifPresent(stdFrame -> getBonusStateTracker().flatMap(tracker -> tracker.getBonusToApply(bowlBonusContainer))
                                                                     .map(bonus-> {
                                                                         setTotalScore(getTotalScore()+bonus);
                                                                         return getTotalScore();})
                                                                     .ifPresent(stdFrame::updateCurrentAndDownstream));
    }

    Optional<BonusStateTracker> getBonusStateTracker() {
        return Optional.ofNullable(bonusStateTracker);
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

}
