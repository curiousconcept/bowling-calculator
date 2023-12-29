package com.bowling.game.second.frame.bonus;

public interface BonusScoreCompleteFramesListener {

    void subscribe(int bonusBowls);

    void acceptBonus(BonusScoreCompleteFrameProvider bonusScoreCompleteFrameProvider);
}
