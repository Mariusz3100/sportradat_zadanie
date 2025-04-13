package mariusz.ambroziak;

import java.util.HashMap;
import java.util.Map;

public class MatchManager {
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
        String[] updateSplit=update.split(":");
        String[] teamsSplit=updateSplit[0].split("-");
        String[] scoreSplit=updateSplit[1].split("–");
        Match teams=new Match(teamsSplit[0].trim(),teamsSplit[1].trim());
        MatchResult updatedResult=new MatchResult(Integer.parseInt(scoreSplit[0].trim()),Integer.parseInt(scoreSplit[1].trim()));
        currentMatches.put(teams,updatedResult);

    }

    public String summaryOfMatchesByTotalScore(){
        String pattern="%s %d - %s %d";
        for(Map.Entry<Match,MatchResult> entry:currentMatches.entrySet()){
            Match match=entry.getKey();
            MatchResult result=entry.getValue();
            return String.format(pattern,match.homeTeamName(),result.homeTeamScore(),match.awayTeamName(),result.awayTeamScore());
        }
        return "";
    }

}

record Match(String homeTeamName, String awayTeamName){}
record MatchResult(int homeTeamScore,int awayTeamScore){}
