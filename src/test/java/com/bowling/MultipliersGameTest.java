package com.bowling;

import com.bowling.game.Game;
import com.bowling.game.multiplierspattern.MultipliersPatternGame;

class MultipliersGameTest extends AbstractGameTest {

    private Game game;

    @Override
    Game createGame() {
        return new com.bowling.game.referenceimpltwo.Game();
    }
}