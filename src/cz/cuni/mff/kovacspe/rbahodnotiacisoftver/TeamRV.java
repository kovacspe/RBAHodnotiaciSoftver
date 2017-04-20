/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.kovacspe.rbahodnotiacisoftver;

import java.io.Serializable;

/**
 *
 * @author Peter
 */
public class TeamRV extends TeamBase {


    public int[] BestRoundPoints; // null ak nesúťažil v danom kole
                                  // vo formáte level1, ... , level5, odnesene zavazie
    public int[] tournamentRoundPoints;
    public int ActualRound;
    private Integer Tier;
   

    public TeamRV(int TeamID, String Name) {
        super(TeamID,Name);
        ActualRound = 1;
    }
    
    public int getWeight(){
        int weight=0;
        try{
            weight=BestRoundPoints[5];
        } catch(ArrayIndexOutOfBoundsException|NullPointerException ex){
            
        }
        return weight;
    }
    
    public int getSum(){
        int sum=0;
        try{
        
            for (int i = 0; i < 5; i++) {
            sum = sum + BestRoundPoints[i];    
            }
        } catch(NullPointerException ex){
            
        } catch(ArrayIndexOutOfBoundsException ex){
            
        }
        return sum;
    }
    

}

class PointsNotSetException extends Exception{
    
}
