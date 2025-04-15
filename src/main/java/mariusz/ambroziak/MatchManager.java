package mariusz.ambroziak;

import java.util.*;

public class MatchManager {
    public static final String TEAMS_AND_SCORE_SPLITER_IN_UPDATE = ":";
    public static final String TEAMS_SPLITTER_IN_UPDATE = "-";
    public static final String SCORES_SPLITTER_IN_UPDATE = "–";
    public static final String FORMAT_OF_MATCH_FOR_SUMMARY = "%s %d - %s %d";

    private static int matchesCounter=0;

    Map<Match,MatchResult> currentMatches=new HashMap<>();
    Map<Match,Integer> matchesCreationOrder=new HashMap<>();

    public void  startGame(String homeTeam,String awayTeam){
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
        String[] updateSplit=update.split(TEAMS_AND_SCORE_SPLITER_IN_UPDATE);
        Match teams = parseTeamsPart(updateSplit[0]);

        if(!currentMatches.containsKey(teams))
            throw createGameNotStartedException();

        MatchResult updatedResult = parseScorePart(updateSplit[1]);
        currentMatches.put(teams,updatedResult);

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
        Map<Integer,Map<Integer,String>> results=new TreeMap(Comparator.reverseOrder());


        Set<Map.Entry<Match, Integer>> keySet = matchesCreationOrder.entrySet();

        for(Map.Entry<Match, Integer> entry: keySet){
            Match match=entry.getKey();
            MatchResult result=currentMatches.get(match);

            if(!results.containsKey(result.homeTeamScore()+result.awayTeamScore())) {
                TreeMap<Integer, String> newTotalScoreEntry = new TreeMap<>(Comparator.reverseOrder());
                newTotalScoreEntry.put(entry.getValue(), summaryOfOneMatch(match, result));
//                newTotalScoreEntry.put(result.homeTeamScore()+result.awayTeamScore(), summaryOfOneMatch(match, result));
                results.put(result.homeTeamScore()+result.awayTeamScore(), newTotalScoreEntry);
            }else{

                results.get(result.homeTeamScore()+result.awayTeamScore())
                        .put(entry.getValue(), summaryOfOneMatch(match, result));
            }


        }
        String retValue="";
        for(Map.Entry<Integer,Map<Integer,String>> entry:results.entrySet()){
            retValue+=String.join("\n",entry.getValue().values())+"\n";
        }

            return retValue.trim();
    }

//    public String summaryOfMatchesByTotalScore(){
//        Map<Integer,String> results=new TreeMap<>();
//        for(Map.Entry<Match, Integer> entry:matchesCreationOrder.entrySet()){
//            Match match=entry.getKey();
//            MatchResult result=currentMatches.get(match);
//            results.put(entry.getValue(),summaryOfOneMatch(match, result));
//        }
//        return String.join("\n",results.values());
//    }

    private static String summaryOfOneMatch(Match match, MatchResult result) {
        return String.format(FORMAT_OF_MATCH_FOR_SUMMARY,
                match.homeTeamName(), result.homeTeamScore(),
                match.awayTeamName(), result.awayTeamScore());
    }

}

record Match(String homeTeamName, String awayTeamName){}
record MatchResult(int homeTeamScore,int awayTeamScore){}
