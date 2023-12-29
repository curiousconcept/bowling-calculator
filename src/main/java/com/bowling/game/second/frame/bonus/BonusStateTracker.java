package com.bowling.game.second.frame.bonus;

import java.util.Optional;

// (restrict access of outer class to private members)
public interface BonusStateTracker {
    Optional<Integer> getBonusToApply(BonusScoreCompleteFrame bowlsContainer);

    int getTotalBonusCollectedSoFar();

    int getNumberOfBonusBowls();
}
