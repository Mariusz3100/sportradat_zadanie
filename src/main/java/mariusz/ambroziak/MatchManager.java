package mariusz.ambroziak;

import java.util.HashMap;
import java.util.Map;

public class MatchManager {
    public static final String TEAMS_AND_SCORE_SPLITER_IN_UPDATE = ":";
    public static final String TEAMS_SPLITTER_IN_UPDATE = "-";
    public static final String SCORES_SPLITTER_IN_UPDATE = "–";
    public static final String FORMAT_OF_MATCH_FOR_SUMMARY = "%s %d - %s %d";

    Map<Match,MatchResult> currentMatches=new HashMap<>();

    public void startGame(String homeTeam,String awayTeam){
        Match current=new Match(homeTeam,awayTeam);
        currentMatches.put(current,new MatchResult(0,0));
    }

    public void finishGame(String homeTeam,String awayTeam){
        Match match=new Match(homeTeam,awayTeam);
        currentMatches.remove(match);
    }

    public void updateScore(String update) {
        String[] updateSplit=update.split(TEAMS_AND_SCORE_SPLITER_IN_UPDATE);
        Match teams = parseTeamsPart(updateSplit[0]);
        MatchResult updatedResult = parseScorePart(updateSplit[1]);
        currentMatches.put(teams,updatedResult);

    }

    private static Match parseTeamsPart(String updateSplit) {
        String[] teamsSplit= updateSplit.split(TEAMS_SPLITTER_IN_UPDATE);
        Match teams=new Match(teamsSplit[0].trim(),teamsSplit[1].trim());
        return teams;
    }

    private static MatchResult parseScorePart(String scorePart) {
        String[] scoreSplit= scorePart.split(SCORES_SPLITTER_IN_UPDATE);
        int homeTeamScore = Integer.parseInt(scoreSplit[0].trim());
        int awayTeamScore = Integer.parseInt(scoreSplit[1].trim());
        return new MatchResult(homeTeamScore, awayTeamScore);
    }

    public String summaryOfMatchesByTotalScore(){
        for(Map.Entry<Match,MatchResult> entry:currentMatches.entrySet()){
            Match match=entry.getKey();
            MatchResult result=entry.getValue();
            return summaryOfOneMatch(match, result);
        }
        return "";
    }

    private static String summaryOfOneMatch(Match match, MatchResult result) {
        return String.format(FORMAT_OF_MATCH_FOR_SUMMARY,
                match.homeTeamName(), result.homeTeamScore(),
                match.awayTeamName(), result.awayTeamScore());
    }

}

record Match(String homeTeamName, String awayTeamName){}
record MatchResult(int homeTeamScore,int awayTeamScore){}
