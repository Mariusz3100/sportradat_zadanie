package mariusz.ambroziak;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatchManagerTest {
    MatchManager manager;

    @BeforeEach
    void setUp() {
        manager=new MatchManager();
    }

    @Test
    void testNoGamePlaying() {
        String summary = manager.summaryOfMatchesByTotalScore();

        assertEquals("", summary);
    }


    @Test
    void testOneGamePlaying() {
        manager.startGame("Mexico","Canada");

        assertEquals("Mexico - Canada: 0 – 0",manager.summaryOfMatchesByTotalScore());

    }


    @org.junit.jupiter.api.Test
    void updateScore() {
        manager.startGame("Mexico","Canada");
        manager.updateScore("Mexico - Canada: 0 – 5");
        assertEquals("Mexico 0 - Canada 5",manager.summaryOfMatchesByTotalScore());
    }

    @org.junit.jupiter.api.Test
    void updateScoreAndFinishGame() {
        manager.startGame("Mexico","Canada");
        manager.updateScore("Mexico - Canada: 0 – 5");
        manager.finishGame("Mexico","Canada");

        assertEquals("Mexico 0 - Canada 5",manager.summaryOfMatchesByTotalScore());
    }



}