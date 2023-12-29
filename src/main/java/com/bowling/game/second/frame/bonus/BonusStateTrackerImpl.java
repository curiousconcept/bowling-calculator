package com.bowling.game.second.frame.bonus;

import com.bowling.game.second.frame.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BonusStateTrackerImpl implements BonusStateTracker {

    private final BonusType bonusType;
    private final List<BonusScoreCompleteFrame> bonusBowlsFrames = new ArrayList<>();
    private boolean qualifiedForNextBonus = true;
    private int bonusPoints = 0;

    public BonusStateTrackerImpl(BonusType bonusType) {
        this.bonusType = bonusType;
    }

    public static BonusStateTracker ofSpare() {
        return new BonusStateTrackerImpl(BonusType.SPARE);
    }

    public static BonusStateTracker ofStrike() {
        return new BonusStateTrackerImpl(BonusType.STRIKE);
    }


    @Override
    public Optional<Integer> getBonusToApply(BonusScoreCompleteFrame bowlsContainer) {
        bonusBowlsFrames.add(bowlsContainer);

        wasteBonusIfFirstAttemptZero(bowlsContainer);

        if (!qualifiedForNextBonus) {
            return Optional.empty();
        }

        switch (bonusType) {
            case SPARE -> {
                return handleSpareBonus();
            }
            case STRIKE -> {
                return handleStrikeBonus();
            }
            default -> throw new IllegalStateException("Unknown bonus type: " + bonusType);
        }
    }

    private void wasteBonusIfFirstAttemptZero(BonusScoreCompleteFrame bowlsContainer) {
        if(bowlsContainer.getFirst() == 0){
            qualifiedForNextBonus = false;
        }
    }

    private Optional<Integer> handleSpareBonus() {
        bonusPoints = bonusBowlsFrames.get(0).getFirst();
        return Optional.of(bonusPoints);
    }

    private Optional<Integer> handleStrikeBonus() {


        if (bonusBowlsFrames.get(0).bowlsCount() == 2) {
            bonusPoints = bonusBowlsFrames.get(0).sum();
            qualifiedForNextBonus = false;
            return Optional.of(bonusPoints);
        }

        int firstBonusFrameFirstAttempt = bonusBowlsFrames.get(0).getFirst();

        if (bonusBowlsFrames.size() == 1) {
            bonusPoints = firstBonusFrameFirstAttempt;
            return Optional.of(Util.isStrike(bonusPoints)).filter(isStrike -> isStrike).map(strike -> bonusPoints);
        }

        // if previous strike, it's already calculated for a bonus, if not,
        // then we need compound bonus of first and second bonus frames
        if (bonusBowlsFrames.size() == 2) {
            var res = Optional.of(Util.isStrike(firstBonusFrameFirstAttempt))
                              .filter(isStrike -> isStrike)
                              .map(strike -> bonusBowlsFrames.get(1).getFirst())
                              .orElseGet(() -> bonusPoints += bonusBowlsFrames.get(1).getFirst());

            qualifiedForNextBonus = false;

            return Optional.of(res);
        }

        throw new IllegalStateException("Unexpected size of bonus tracker state, providing toString of the state:\n "
                                        + this);
    }


    @Override
    public int getTotalBonusCollectedSoFar() {
        return bonusPoints;
    }

    @Override
    public int getNumberOfBonusBowls() {
        return bonusType.ordinal();
    }

    public enum BonusType {
        NONE, SPARE, STRIKE
    }


    @Override
    public String toString() {
        return "BonusStateTrackerImpl{" +
               "bonusType=" + bonusType +
               ", bonusBowlsFrames=" + bonusBowlsFrames +
               ", allBonusesCollected=" + qualifiedForNextBonus +
               ", bonusPoints=" + bonusPoints +
               '}';
    }
}
