/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.kovacspe.rbahodnotiacisoftver;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author Peter
 */
public class RBARobotickaVyzva extends javax.swing.JFrame {

    List<JSpinner> Team1Spinners;
    List<JSpinner> Team2Spinners;
    JComboBox selectTournamentPair;
    JComboBox selectType;
    JComboBox team1Chooser;
    JComboBox team2Chooser;
    List<TeamRV> RVTeams;

    
    /**
     * Pridáva do container jedno prázne pole. Služi ako výplň GridLayoutu.
     * @param cont - container
     * @param count - počet prázdnych polí
     */
    private void AddEmptyCell(Container cont, int count) {
        for (int i = 0; i < count; i++) {
            cont.add(new JLabel());
        }
    }

    /**
     * Spočíta koľko bodov získal tím. Zohľadňuje aj mód hry.
     *
     * @param mySpinners
     * @param opponentSpinners
     * @return
     */
    private int CountResults(List<JSpinner> mySpinners, List<JSpinner> opponentSpinners) {
        int result = 0;
        for (JSpinner s : mySpinners) {
            result = result + ((Integer) s.getValue());
        }

        return result;
    }

    public void LoadTeams() {
        team1Chooser.removeAllItems();
        team2Chooser.removeAllItems();
        RVTeams = RBAHodnotiaciSoftver.LoadTeams("RV.res");
        RVTeams.forEach(t -> team1Chooser.addItem(t));
        RVTeams.forEach(t -> team2Chooser.addItem(t));

    }

    
    public void LoadTournaments(){
        
    }
    
    public void ClearSpinners(List<JSpinner> spinners){
        for(JSpinner s : spinners){
            s.setModel(new SpinnerNumberModel(0, 0, 5, 1));
        }
    }
    
    public void SaveTeamResults(List<JSpinner> spinners,TeamRV team){
        int[] Points = new int[6];
        
        for (int i = 0; i < 5; i++) {
            Points[i] = (int) spinners.get(i).getValue();
        }
 
        //team.BestRoundPoints = 
    }
    
    /**
     * Creates new form RBARobotickaVyzva
     */
    public RBARobotickaVyzva() {
        initComponents();

        CreateRVMenu();
        Container cont = this.getContentPane();
        cont.setLayout(new GridLayout(9, 5, 20, 20));

        // Combobox na vyberanie typu zapasu
        JLabel selectTypeLabel = new JLabel("Typ zápasu:");
        cont.add(selectTypeLabel);
        selectType = new JComboBox();
        selectType.addItem(TournamentMode.BASIC);
        selectType.addItem(TournamentMode.SPIDER);
        selectType.addActionListener(e -> ChangeTournamentMode((TournamentMode) selectType.getSelectedItem()));
        cont.add(selectType);

        // Combobox na vyberanie zapadsu ak sa hra pavuk
        selectTournamentPair = new JComboBox();
        selectTournamentPair.setVisible(false);
        cont.add(selectTournamentPair);

        AddEmptyCell(cont, 7);

        //Vyberace timov
        team1Chooser = new JComboBox();
        cont.add(team1Chooser);
        AddEmptyCell(cont, 2);
        team1Chooser.addActionListener(e -> {
            ClearSpinners(Team1Spinners);
        });
        team2Chooser = new JComboBox();
        cont.add(team2Chooser);
        AddEmptyCell(cont, 1);
        team2Chooser.addActionListener(e -> {
            ClearSpinners(Team2Spinners);
        });

        Team1Spinners = new LinkedList<>();
        Team2Spinners = new LinkedList<>();

        JLabel team1Score = new JLabel();
        JLabel team2Score = new JLabel();

        // Hodnotenia levelov
        for (int i = 1; i < 6; i++) {
            JLabel t1label = new JLabel("Level " + i);
            JSpinner t1spin = new JSpinner();
            SpinnerModel sm1 = new SpinnerNumberModel(0, 0, 5, 1);
            t1spin.setModel(sm1);
            cont.add(t1label);
            cont.add(t1spin);
            t1spin.addChangeListener(e -> {
                team1Score.setText("SPOLU: " + Integer.toString(CountResults(Team1Spinners, Team2Spinners)));
            });
            Team1Spinners.add(t1spin);

            AddEmptyCell(cont, 1);
            JLabel t2label = new JLabel("Level " + i);
            SpinnerModel sm2 = new SpinnerNumberModel(0, 0, 5, 1);
            JSpinner t2spin = new JSpinner();
            t2spin.setModel(sm2);
            cont.add(t2label);
            cont.add(t2spin);
            t2spin.addChangeListener(e -> {
                team2Score.setText("SPOLU: " + Integer.toString(CountResults(Team2Spinners, Team1Spinners)));
            });
            Team2Spinners.add(t2spin);
        }

        JButton saveTeam1 = new JButton("Ulož výsledky");
        cont.add(saveTeam1);
        cont.add(team1Score);
        AddEmptyCell(cont, 1);
        JButton saveTeam2 = new JButton("Ulož výsledky");
        cont.add(saveTeam2);
        cont.add(team2Score);

    }

    public void CreateRVMenu() {
        JMenuBar mb = new JMenuBar();
        mb.add(RBAHodnotiaciSoftver.CreateContextSwitchingMenu());
        this.setJMenuBar(mb);
    }

    private enum TournamentMode {
        BASIC, SPIDER

    }

    TournamentMode currTournamentMode = TournamentMode.BASIC;

    private void ChangeTournamentMode(TournamentMode newMode) {
        currTournamentMode = newMode;
        switch (newMode) {
            case BASIC:
                team1Chooser.setVisible(true);
                team2Chooser.setVisible(true);
                selectTournamentPair.setVisible(false);
                break;
            case SPIDER:
                team1Chooser.setVisible(false);
                team2Chooser.setVisible(false);
                selectTournamentPair.setVisible(true);
                break;

        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("RBA - Robotická výzva");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 818, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 406, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(RBARobotickaVyzva.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RBARobotickaVyzva.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RBARobotickaVyzva.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RBARobotickaVyzva.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RBARobotickaVyzva().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
