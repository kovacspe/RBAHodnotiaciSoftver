/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.kovacspe.rbahodnotiacisoftver;

/**
 *
 * @author Peter
 */
public class TeamRC extends TeamBase {

    public String note;
    private Integer BestQualificationTime;
    private int RaceRound;
    private int RaceRoundEntry;
    public int RaceNumber;
    boolean finished;
    
    public int getRaceRound(){
        return RaceRound;
    }
    
    public int getRaceRoundEntry(){
        return RaceRoundEntry;
    }
    
    public void passFinishLine(int Entry){
        RaceRound++;
        RaceRoundEntry=Entry;
    }
    
    public void resetRaceStatus(){
        RaceRound=0;
        RaceRoundEntry=RaceNumber;
        finished=false;
    }

    public static Integer ConvertTimeToSeconds(String time) {
        String[] MinSec = time.split(":");
        try {
            return Integer.parseInt(MinSec[0]) * 60 + Integer.parseInt(MinSec[1]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
            return null;
        }
    }

    public String getTimeConvertedToString() {
        String s;
        try {
            s = Integer.toString(getBestQualificationTime() / 60) + ":" + Integer.toString(getBestQualificationTime() % 60);
        } catch (TimeNotSetException ex) {
            s = "NULL";
        }
        return s;
    }

    public boolean isTimeInitialized() {
        return BestQualificationTime != null;
    }

    public void setBestQualificationTime(Integer time) {
        BestQualificationTime = time;
    }

    public int getBestQualificationTime() throws TimeNotSetException {
        if (BestQualificationTime == null) {
            throw new TimeNotSetException();
        } else {
            return BestQualificationTime;
        }
    }

    public TeamRC(int TeamID, String Name) {
        super(TeamID, Name);
        BestQualificationTime = null;
        this.note = "";
        this.RaceNumber=0;
        resetRaceStatus();
    }
    
    @Override
    public String toString(){
        return Name+"("+RaceNumber+")";
    }

}

class TimeNotSetException extends Exception {

}
