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
                    Spain 0 - Brazil 0
                    Mexico 0 - Canada 0""";
        assertEquals(expectedResult,manager.summaryOfMatchesByTotalScore());

    }



    @Test
    void testTwoTeamsPlayingTwoGamesDifferentPositions() {
        manager.startGame("Mexico","Canada");
        manager.startGame("Canada","Mexico");

        String expectedResult = """
                    Canada 0 - Mexico 0
                    Mexico 0 - Canada 0""";
        assertEquals(expectedResult,manager.summaryOfMatchesByTotalScore());

    }


    @org.junit.jupiter.api.Test
    void updateScore() {
        manager.startGame("Mexico","Canada");
        manager.updateScore("Mexico - Canada: 0 – 5");

        assertEquals("Mexico 0 - Canada 5",manager.summaryOfMatchesByTotalScore());
    }

    @org.junit.jupiter.api.Test
    void updateScoreInputIsOdd() {
        manager.startGame("Mexico","Canada");

        assertThrows(IllegalArgumentException.class,()->manager.updateScore(" \n: 0 – 5"));
    }


    @org.junit.jupiter.api.Test
    void updateScoreManyTimes() {
        manager.startGame("Mexico","Canada");
        manager.updateScore("Mexico - Canada: 0 – 1");
        manager.updateScore("Mexico - Canada: 0 – 2");
        manager.updateScore("Mexico - Canada: 0 – 3");
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
    void finishNotExistinghGameWithNullNames() {
        assertThrows(IllegalStateException.class,()->manager.finishGame(null,null));
    }

    @org.junit.jupiter.api.Test
    void finishNotExistinghGameWithBlankNames() {
        assertThrows(IllegalStateException.class,()->manager.finishGame("\n",""));
    }

    @org.junit.jupiter.api.Test
    void startAlreadyExistinghGame() {
        manager.startGame("Germany","France");
        assertThrows(IllegalStateException.class,()->manager.startGame("Germany","France"));
    }



    @org.junit.jupiter.api.Test
    void startTwoGamesUpdateInDifferentOrder() {
        manager.startGame("Germany","France");
        manager.startGame("Uruguay","Italy");

        manager.updateScore("Uruguay - Italy: 6 – 6");
        manager.updateScore("Germany - France: 2 – 2");


        String expectedResult = """
                    Uruguay 6 - Italy 6
                    Germany 2 - France 2""";
        assertEquals(expectedResult,manager.summaryOfMatchesByTotalScore());
    }

    @org.junit.jupiter.api.Test
    void startTwoGamesSameNames() {
        manager.startGame("Germany","Germany");

        manager.updateScore("Germany - Germany: 6 – 2");


        String expectedResult = """
                    Germany 6 - Germany 2""";
        assertEquals(expectedResult,manager.summaryOfMatchesByTotalScore());
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
    void startAndUpdateScoreAndFinishGame() {
        manager.startGame("Mexico","Canada");
        manager.updateScore("Mexico - Canada: 0 – 5");
        manager.finishGame("Mexico","Canada");

        assertEquals("",manager.summaryOfMatchesByTotalScore());
    }

    @org.junit.jupiter.api.Test
    void startGameWithEmptyNames() {
        assertThrows(IllegalArgumentException.class,()->manager.startGame("",""));
    }

    @org.junit.jupiter.api.Test
    void startGameWithNullNames() {
        assertThrows(IllegalArgumentException.class,()->manager.startGame(null,null));
    }


    @org.junit.jupiter.api.Test
    void startScoreAndUpdateManyGames() {
        manager.startGame("Mexico","Canada");
        manager.updateScore("Mexico - Canada: 0 - 5");
        manager.startGame("Spain","Brazil");
        manager.updateScore("Spain - Brazil: 1 – 0");
        manager.updateScore("Spain - Brazil: 2 – 0");
        manager.updateScore("Spain - Brazil: 5 – 2");
        manager.updateScore("Spain - Brazil: 10 – 2");
        manager.startGame("Germany","France");
        manager.updateScore("Germany - France: 2 – 0");
        manager.updateScore("Germany - France: 2 – 1");
        manager.updateScore("Germany - France: 2 – 2");
        manager.startGame("Uruguay","Italy");
        manager.updateScore("Uruguay - Italy: 0 – 0");
        manager.updateScore("Uruguay - Italy: 2 – 2");
        manager.updateScore("Uruguay - Italy: 6 – 6");
        manager.startGame("Argentina","Australia");
        manager.updateScore("Argentina - Australia: 1 – 1");
        manager.updateScore("Argentina - Australia: 1 – 1");
        manager.updateScore("Argentina - Australia: 3 – 1");

        String expectedResult = """
                Uruguay 6 - Italy 6
                Spain 10 - Brazil 2
                Mexico 0 - Canada 5
                Argentina 3 - Australia 1
                Germany 2 - France 2""";

        assertEquals(expectedResult,manager.summaryOfMatchesByTotalScore());
    }



}