/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rbahodnotiacisoftver;

/**
 *
 * @author Peter
 */
public class TeamRV {

    private int TeamID;
    public String Name;
    public Integer PointsRound1;
    public Integer PointsRound2;
    public Integer PointsRound3;
    private Integer Tier;
    
    public void setPoints(int p){
        this.Points=p;
    }
    
    public boolean havePoints(){
        return !(Points==null);
    }
    
    public int getPoints() throws PointsNotSetException{
        if (Points!=null)
        return Points;
        else throw new PointsNotSetException();
    }
    

    public int getTeamID() {
        return TeamID;
    }

    public TeamRV(int TeamID, String Name) {
        this.TeamID = TeamID;
        this.Name = Name;
        this.Points=null;
        this.Tier=null;
    }
}

class PointsNotSetException extends Exception{
    
}
