package com.bowling.game.second.frame.bonus;

public interface BonusScoreCompleteFrameProvider {


    void notifySubscribersOfFrameComplete();

    void acceptSubscriber(BonusScoreCompleteFramesListener bonusScoreCompleteFramesListener);


    BonusScoreCompleteFrame provideBowlBonusContainer();
}
