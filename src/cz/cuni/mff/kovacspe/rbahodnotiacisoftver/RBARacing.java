/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.kovacspe.rbahodnotiacisoftver;

import java.awt.Container;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javafx.scene.control.ComboBox;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTable;

/**
 *
 * @author Peter
 */
public class RBARacing extends javax.swing.JFrame {

    public List<TeamRC> RCTeams;
    public List<RacingGroup> Groups;
    private JPanel BTNcont;
    JComboBox chooseGroupComboBox;
    private JTable table;
    Overview RacingTable;

    private void SortTeamsByQualificationTime() {
        Comparator<TeamRC> cmprtr = new Comparator<TeamRC>() {
            @Override
            public int compare(TeamRC t, TeamRC t1) {
                try {
                    return t.getBestQualificationTime() - t1.getBestQualificationTime();
                } catch (TimeNotSetException ex) {
                    if (!t.isTimeInitialized()) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            }

        };
        RCTeams.sort(cmprtr);
    }

    private void SortTeamsByID() {
        Comparator<TeamRC> cmprtr = new Comparator<TeamRC>() {
            @Override
            public int compare(TeamRC t, TeamRC t1) {
                return t.getTeamID() - t1.getTeamID();
            }

        };
        RCTeams.sort(cmprtr);
    }

    private JMenuBar CreateRCMenuBar() {
        JMenuBar mb = new JMenuBar();
        JMenu teamManager = new JMenu("Správa súťaže");

        JMenuItem qualification = new JMenuItem("Hodnotenie kvalifikácie");
        qualification.addActionListener(e -> OpenQualificationTable());
        teamManager.add(qualification);

        JMenuItem makeGroups = new JMenuItem("Vytvoriť skupiny na pretek");
        makeGroups.addActionListener(e -> CreateRacingGroups(2));
        teamManager.add(makeGroups);

        JMenuItem loadGroups = new JMenuItem("Načítať skupiny na pretek");
        loadGroups.addActionListener(e -> CreateAndShowRacingGUI());
        teamManager.add(loadGroups);

        mb.add(teamManager);
        mb.add(RBAHodnotiaciSoftver.CreateContextSwitchingMenu());
        return mb;
    }

    private void CreateRacingGroups(int groupSize) {
        SortTeamsByQualificationTime();
        int i = 0;
        RacingGroup betterGroup = null;
        List<TeamRC> tmp = new ArrayList<>();
        List<RacingGroup> groups = new ArrayList<>();
        while (i < RCTeams.size()) {
            RCTeams.get(i).RaceNumber = i % groupSize;
            tmp.add(RCTeams.get(i));
            if (i % groupSize == groupSize - 1) {
                RacingGroup rg = new RacingGroup("Group" + ((char) (65 + (i / groupSize))), tmp, 5);
                rg.betterGroup = betterGroup;

                betterGroup = rg;
                groups.add(rg);
                tmp = new ArrayList<>();
            }
            i++;
        }
        if ((i - 1) % groupSize != groupSize - 1) {
            RacingGroup rg = new RacingGroup("Group" + ((char) (65 + (i / groupSize))), tmp, 5);
            rg.betterGroup = betterGroup;

            betterGroup = rg;
            groups.add(rg);
            tmp = new ArrayList<>();
        }
        RBAHodnotiaciSoftver.SaveTeams(groups, "RacingGroups.dat");
    }

    private void LoadRacingGroups() {
        Groups = RBAHodnotiaciSoftver.LoadTeams("RacingGroups.dat");
    }

    public void refreshTable() {
        RacingGroup currRacingGroup = ((RacingGroup) chooseGroupComboBox.getSelectedItem());
        RacingTable.UpdateTable(currRacingGroup.WrapGroupData());
    }

    private void setGroup(RacingGroup g) {
        BTNcont.removeAll();

        BTNcont.setLayout(new BoxLayout(BTNcont, BoxLayout.Y_AXIS));
        for (TeamRC t : g.readTeams()) {
            TeamRCBTN b = new TeamRCBTN(t.toString(), g, t, this);
            BTNcont.add(b);
        }
        BTNcont.repaint();
        this.pack();
    }

    private void StartRace() {
        
        RacingGroup currRacingGroup = ((RacingGroup) chooseGroupComboBox.getSelectedItem());
        
        if (!currRacingGroup.isRunning){
            currRacingGroup.ResetResults();
        RacingTable = new Overview();        
        String[] header = {"Pozícia", "Názov tímu", "Kolo", "Stav"};
        RacingTable.InitTable(currRacingGroup.WrapGroupData(), header);
        RacingTable.setVisible(true);
        } else {
            BTNcont.removeAll();
            BTNcont.repaint();
            currRacingGroup.SaveFinalResults();
        }
    }

    public void CreateAndShowRacingGUI() {
        //this.CreateRCMenuBar();
        Container cont = this.getContentPane();
        cont.removeAll();
        LoadRacingGroups();
        cont.setLayout(new GridLayout(2, 2));

        chooseGroupComboBox = new JComboBox();
        Groups.forEach(g -> chooseGroupComboBox.addItem(g));
        chooseGroupComboBox.addActionListener(e -> setGroup((RacingGroup) chooseGroupComboBox.getSelectedItem()));
        cont.add(chooseGroupComboBox);
        JButton BTNStart = new JButton("Start");
        BTNStart.addActionListener(e -> {
            StartRace();
        });
        cont.add(BTNStart);
        BTNcont = new JPanel();
        cont.add(BTNcont);
        table = new JTable();
        cont.add(table);
        this.pack();
    }

    private String[][] WrapRCQualificationData() {
        SortTeamsByID();
        String[][] table = new String[RCTeams.size()][4];
        for (TeamRC t : RCTeams) {
            int i = RCTeams.indexOf(t);
            table[i][0] = Integer.toString(t.getTeamID());
            table[i][1] = t.Name;
            table[i][2] = t.getTimeConvertedToString();
            table[i][3] = t.note;
        }
        return table;
    }

    private void OpenQualificationTable() {
        Overview over = new Overview();
        over.setTitle("Racing - kvalifikácia");
        String[] header = {"Team ID", "Názov tímu", "Najlepšia jazda", "Poznámka"};
        int[] discols = {0, 1};
        over.InitTable(WrapRCQualificationData(), header, discols);
        over.InitSaveBTN();
        over.setVisible(true);
        System.out.println("Tabulka hodnotenia bz mala byt otvorena");
    }

    void LoadTeams() {
        RCTeams = RBAHodnotiaciSoftver.LoadTeams("RC.res");

    }

    void SaveTeams() {
        RBAHodnotiaciSoftver.SaveTeams(RCTeams, "RC.res");
    }

    /**
     * Creates new form NewJFrame
     */
    public RBARacing() {
        initComponents();
        this.setJMenuBar(CreateRCMenuBar());

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
        setTitle("Racing");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
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
            java.util.logging.Logger.getLogger(RBARacing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RBARacing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RBARacing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RBARacing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RBARacing().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
