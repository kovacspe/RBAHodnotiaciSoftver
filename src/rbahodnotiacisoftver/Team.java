/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rbahodnotiacisoftver;

import java.io.Serializable;

/**
 *
 * @author Peter
 */
public class Team implements Serializable {
    private int TeamID;
    public String Name;
    public String School;
    public String Student1;
    public String Student2;
    public String Student3;
    public boolean VM;
    public boolean RC;
    public boolean RV;
    
    public int getTeamID(){
        return TeamID;
    }
    
    public Team(int TeamID,String Name, String School,String Student1,String Student2, String Student3, boolean VM,boolean RC, boolean RV){
        this.Name = Name;
        this.RC = RC;
        this.RV = RV;
        this.School = School;
        this.Student1 = Student1;
        this.Student2 = Student2;
        this.Student3 = Student3;
        this.VM = VM;
        this.TeamID = TeamID;
    }

}
