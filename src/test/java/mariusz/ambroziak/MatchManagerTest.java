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

        assertEquals("Mexico 0 - Canada 0",manager.summaryOfMatchesByTotalScore());

    }

    @Test
    void testTwoGamesPlaying() {
        manager.startGame("Mexico","Canada");
        manager.startGame("Spain","Brazil");

        String expectedResult = """
                    Mexico 0 - Canada 0
                    Spain 0 - Brazil 0""";
        assertEquals(expectedResult,manager.summaryOfMatchesByTotalScore());

    }


    @org.junit.jupiter.api.Test
    void updateScore() {
        manager.startGame("Mexico","Canada");
        manager.updateScore("Mexico - Canada: 0 – 5");

        assertEquals("Mexico 0 - Canada 5",manager.summaryOfMatchesByTotalScore());
    }

    @org.junit.jupiter.api.Test
    void updateScoreForNotExistinghGame() {
        assertThrows(IllegalStateException.class,()->manager.updateScore("Mexico - Canada: 0 – 5"));
    }

    @org.junit.jupiter.api.Test
    void finishNotExistinghGame() {
        assertThrows(IllegalStateException.class,()->manager.finishGame("Mexico","Canada"));
    }

    @org.junit.jupiter.api.Test
    void startAlreadyExistinghGame() {
        manager.startGame("Germany","France:");
        assertThrows(IllegalStateException.class,()->manager.startGame("Germany","France:"));
    }

    @org.junit.jupiter.api.Test
    void startAndFinishGame() {
        manager.startGame("Mexico","Canada");
        manager.finishGame("Mexico","Canada");

        assertEquals("",manager.summaryOfMatchesByTotalScore());
    }


    @org.junit.jupiter.api.Test
    void updateScoreAndFinishGame() {
        manager.startGame("Mexico","Canada");
        manager.updateScore("Mexico - Canada: 0 – 5");
        manager.finishGame("Mexico","Canada");

        assertEquals("",manager.summaryOfMatchesByTotalScore());
    }


    @org.junit.jupiter.api.Test
    void startAndScoreupdateScoreAndFinishGame() {
        manager.startGame("Mexico","Canada");
        manager.updateScore("Mexico - Canada: 0 – 5");
        manager.finishGame("Mexico","Canada");

        assertEquals("",manager.summaryOfMatchesByTotalScore());
    }



}