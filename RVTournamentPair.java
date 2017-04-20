/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.kovacspe.rbahodnotiacisoftver;

/**
 *
 * @author kovacspe
 */
public class RVTournamentPair {
    TeamRV team1;
    TeamRV team2;
    private final int spiderLevel;
    private final RVTournamentPair groupForWinner; 
    
    public RVTournamentPair(TeamRV team1, TeamRV team2,RVTournamentPair groupforWinner, int spiderLevel){
        this.team1=team1;
        this.team2=team2;
        this.groupForWinner=groupforWinner;
        this.spiderLevel=spiderLevel;
    }
    
    public RVTournamentPair(RVTournamentPair groupForWinner, int spiderLevel){
        this(null,null,groupForWinner,spiderLevel);
    }
    
    public void chooseWinner() throws NoResultsFoundException{
        if (team1.tournamentRoundPoints[spiderLevel]!=-1 && team2.tournamentRoundPoints[spiderLevel]!=-1){
            if (team1.tournamentRoundPoints[spiderLevel]>team2.tournamentRoundPoints[spiderLevel]){
                
            } else if (team1.tournamentRoundPoints[spiderLevel]<team2.tournamentRoundPoints[spiderLevel]){
                
            }
        } else throw new NoResultsFoundException();
    }
    
    
    
    
            
            
}

class NoResultsFoundException extends Exception{

}
