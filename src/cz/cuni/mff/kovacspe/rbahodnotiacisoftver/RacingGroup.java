/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.kovacspe.rbahodnotiacisoftver;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Peter
 */
public class RacingGroup implements Serializable {

    private List<TeamRC> teams;
    boolean isRunning;
    String groupLabel;
    RacingGroup betterGroup; // null ak je to prva skupina
    private int maxRounds;
    private int counter;
    int[] roundEntryNumbers;

    public String[][] WrapGroupData() {
        SortTeamsByRace();
        String[][] data = new String[teams.size()][4];
        for (int i = 0; i < teams.size(); i++) {
            data[i][0] = (i + 1) + ".";
            data[i][1] = teams.get(i).toString();
            data[i][2] = teams.get(i).getRaceRound() + "";
            if (teams.get(i).finished) {
                data[i][3] = "FINISHED";
            } else {
                data[i][3] = "";
            }
        }
        return data;
    }

    public RacingGroup(String groupLabel, List<TeamRC> teams, int maxRounds) {
        this.groupLabel = groupLabel;
        this.maxRounds = maxRounds;
        this.teams = teams;
        isRunning = false;
        counter = teams.size() - 1;
    }

    private void SortTeamsByRace() {
        teams.sort(new Comparator<TeamRC>() {
            @Override
            public int compare(TeamRC t, TeamRC t1) {
                if (t.getRaceRound() > t1.getRaceRound()) {
                    return -1;
                } else if (t.getRaceRound() < t1.getRaceRound()) {
                    return 1;
                } else if (t.getRaceRoundEntry() > t1.getRaceRoundEntry()) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

    public void SaveFinalResults() {
        SortTeamsByRace();

        MoveWinnersToBetterGroup();
        RBAHodnotiaciSoftver.SaveTeams(teams, groupLabel + "_FINAL.res");
    }

    public void ResetResults() {
        for (int i = 0; i < teams.size(); i++) {
            teams.get(i).RaceNumber = i + 1;
            teams.get(i).resetRaceStatus();
        }
        roundEntryNumbers = new int[maxRounds];
        for (int i = 0; i < maxRounds; i++) {
            roundEntryNumbers[i] = 0;
        }
        isRunning = true;
    }

    public List<TeamRC> readTeams() {
        return teams;
    }

    public void addTeam(TeamRC team) {
        counter++;
        team.RaceNumber = counter;
        teams.add(team);
    }

    public void MoveWinnersToBetterGroup() {
        if (betterGroup != null) {
            SortTeamsByRace();
            if (teams.size() > 0) {

                betterGroup.addTeam(teams.get(0));
                teams.remove(0);
                if (teams.size() > 0) {
                    betterGroup.addTeam(teams.get(1));
                    teams.remove(0);
                }
            }
        }
    }

    private int getRoundEntryNumber(int round) {
        roundEntryNumbers[round]++;
        return roundEntryNumbers[round];
    }

    public void finishRound(TeamRC team) {

        if (teams.contains(team)) {
            if (!team.finished) {
                team.passFinishLine(getRoundEntryNumber(team.getRaceRound()));
            }
            if (team.getRaceRound() >= maxRounds) {
                team.finished = true;
            }

        }
    }

    @Override
    public String toString() {
        return groupLabel;
    }

    class TeamNotFoundException extends Exception {

    }

}
