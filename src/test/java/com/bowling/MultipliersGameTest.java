package com.bowling;

import com.bowling.game.Game;
import com.bowling.game.bidirectionallinkedliststrategy.GameStrategy;
import com.bowling.game.multiplierspattern.MultipliersPatternGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MultipliersGameTest extends AbstractGameTest {

    private Game game;

    @Override
    Game createGame() {
        return new MultipliersPatternGame();
    }
}