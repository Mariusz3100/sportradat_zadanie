package mariusz.ambroziak;

import java.util.*;
import java.util.regex.Pattern;

public class MatchManager {
    public static final String TEAMS_AND_SCORE_SPLITER_IN_UPDATE = ":";
    public static final String TEAMS_SPLITTER_IN_UPDATE = "[–|-]";
    public static final String SCORES_SPLITTER_IN_UPDATE = "[–|-]";
    public static final String FORMAT_OF_MATCH_FOR_SUMMARY = "%s %d - %s %d";
    public static final String PATTERN_OF_UPDATE = "[\\w ]+ [–|-] [\\w ]+: \\d+ [–|-] \\d+";

    private static int matchesCounter=0;

    Map<Match,MatchResult> currentMatches=new HashMap<>();
    Map<Match,Integer> matchesCreationOrder=new HashMap<>();

    public void  startGame(String homeTeam,String awayTeam){
        if(homeTeam==null||awayTeam==null
                ||homeTeam.isBlank()||awayTeam.isBlank())
            throw new IllegalArgumentException("Team names must not be null or empty");

        Match current=new Match(homeTeam,awayTeam);

        if(currentMatches.containsKey(current)) {
            throw createGameAlreadyStartedException();
        }else{
            currentMatches.put(current,new MatchResult(0,0));
            matchesCreationOrder.put(current,matchesCounter++);
        }


    }

    private static IllegalStateException createGameAlreadyStartedException() {
        return new IllegalStateException("Game already started!");
    }

    public void finishGame(String homeTeam,String awayTeam){
        Match match=new Match(homeTeam,awayTeam);

        if(!currentMatches.containsKey(match))
            throw createGameNotStartedException();

        currentMatches.remove(match);

        matchesCreationOrder.remove(match);
    }

    private static IllegalStateException createGameNotStartedException() {
        return new IllegalStateException("Game not started!");
    }

    public void updateScore(String update) {
        if(!Pattern.matches(PATTERN_OF_UPDATE,update)){
            throw new IllegalArgumentException("Update argument is in incorrect format.");
        }else {
            parseProperlyFormattedUpdateString(update);
        }

    }

    private void parseProperlyFormattedUpdateString(String update) {
        String[] updateSplit = update.split(TEAMS_AND_SCORE_SPLITER_IN_UPDATE);
        Match teams = parseTeamsPart(updateSplit[0]);

        if (!currentMatches.containsKey(teams))
            throw createGameNotStartedException();

        MatchResult updatedResult = parseScorePart(updateSplit[1]);
        currentMatches.put(teams, updatedResult);
    }

    private static Match parseTeamsPart(String updateSplit) {
        String[] teamsSplit=updateSplit.split(TEAMS_SPLITTER_IN_UPDATE);
        return new Match(teamsSplit[0].trim(),teamsSplit[1].trim());
    }

    private static MatchResult parseScorePart(String scorePart) {
        String[] scoreSplit= scorePart.split(SCORES_SPLITTER_IN_UPDATE);
        int homeTeamScore = Integer.parseInt(scoreSplit[0].trim());
        int awayTeamScore = Integer.parseInt(scoreSplit[1].trim());
        return new MatchResult(homeTeamScore, awayTeamScore);
    }

    public String summaryOfMatchesByTotalScore(){
        Map<Integer, Map<Integer, String>> totalScoreToOrderedMatches = parseFieldMapsToSummariesOrdereByTotalScoreAndCreation();

        return concatenateSummaries(totalScoreToOrderedMatches);
    }

    private static String concatenateSummaries(Map<Integer, Map<Integer, String>> totalScoreToOrderedMatches) {
        StringBuilder retValue= new StringBuilder();
        for(Map.Entry<Integer,Map<Integer,String>> entry: totalScoreToOrderedMatches.entrySet()){
            retValue.append(String.join("\n", entry.getValue().values())).append("\n");
        }
        return retValue.toString().trim();
    }

    private Map<Integer, Map<Integer, String>> parseFieldMapsToSummariesOrdereByTotalScoreAndCreation() {
        Map<Integer,Map<Integer,String>> totalScoreToOrderedMatches=new TreeMap<>(Comparator.reverseOrder());

        for(Map.Entry<Match, Integer> creationOrderEntry: matchesCreationOrder.entrySet()){
            Match match=creationOrderEntry.getKey();
            MatchResult result=currentMatches.get(match);

            if(!totalScoreToOrderedMatches.containsKey(calculateTotalScore(result))) {
                TreeMap<Integer, String> newTotalScoreEntry = createNewTotalScoreEntry(creationOrderEntry, match, result);
                totalScoreToOrderedMatches.put(calculateTotalScore(result), newTotalScoreEntry);
            }else{
                Map<Integer, String> existingTotalScoreEntry = totalScoreToOrderedMatches.get(calculateTotalScore(result));
                existingTotalScoreEntry.put(creationOrderEntry.getValue(), summaryOfOneMatch(match, result));
            }
        }
        return totalScoreToOrderedMatches;
    }

    private static int calculateTotalScore(MatchResult result) {
        return result.homeTeamScore() + result.awayTeamScore();
    }

    private static TreeMap<Integer, String> createNewTotalScoreEntry(Map.Entry<Match, Integer> creationOrderEntry, Match match, MatchResult result) {
        TreeMap<Integer, String> newTotalScoreEntry = new TreeMap<>(Comparator.reverseOrder());
        newTotalScoreEntry.put(creationOrderEntry.getValue(), summaryOfOneMatch(match, result));
        return newTotalScoreEntry;
    }



    private static String summaryOfOneMatch(Match match, MatchResult result) {
        return String.format(FORMAT_OF_MATCH_FOR_SUMMARY,
                match.homeTeamName(), result.homeTeamScore(),
                match.awayTeamName(), result.awayTeamScore());
    }

}

record Match(String homeTeamName, String awayTeamName){}
record MatchResult(int homeTeamScore,int awayTeamScore){}
