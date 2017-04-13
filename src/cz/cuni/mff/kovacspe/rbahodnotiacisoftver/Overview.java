/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.kovacspe.rbahodnotiacisoftver;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Peter
 */
public class Overview extends javax.swing.JFrame {

    /**
     * Creates new form Overview
     */
    JTable table;
    Container cont;

    public Overview() {
        initComponents();
        cont = this.getContentPane();
        cont.setLayout(new BorderLayout());

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    }


    /**
     * Vloží tabuľku s popísaným obsahom a hlavičkou
     *
     * @param data Obsah tabuľky v dvojrozmernom poli
     * @param ColumnNames Hlavička tabuľky
     */
    public void InitTable(String[][] data, Object[] ColumnNames) {
        JFrame frame = this;

        table = new JTable(data, ColumnNames);

        cont.add(table.getTableHeader(), BorderLayout.PAGE_START);
        cont.add(table, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);
    }
    
    public void UpdateTable(String[][] data){
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                table.setValueAt(data[i][j], i, j);
            }
        }
        
    }

    public void InitTable(String[][] data, Object[] ColumnNames, int[] discols) {
        JFrame frame = this;

        table = new JTable(data, ColumnNames);
        DefaultTableModel model = new DefaultTableModel(data, ColumnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                for (int i = 0; i < discols.length; i++) {
                    if (discols[i] == column) {
                        return false;
                    }
                }
                return true;
            }
        };
        table.setModel(model);

        //cont.add(table.getTableHeader(), BorderLayout.PAGE_START);
        cont.add(table, BorderLayout.CENTER);
        
        //frame.pack();
        //frame.setVisible(true);
    }

    /**
     * Nastaví tlačidlá používané pri kategórií Vlastný model
     */
    public void InitSaveBTN() {
        JButton btnSave = new JButton("Ulož zmeny");
        if (RBAHodnotiaciSoftver.currMode==Modes.VLASTNY_MODEL)
        btnSave.addActionListener(e -> WrapVMResults());
        if (RBAHodnotiaciSoftver.currMode==Modes.RACING);
        btnSave.addActionListener(e -> WrapRCQualificationResults());

        cont.add(btnSave, BorderLayout.PAGE_END);
    }

    /**
     * Uloží výsledky z tabuľky v prípade, že sa jedná o výsledky Vlastného
     * modelu
     */
    void WrapVMResults() {
        if (RBAHodnotiaciSoftver.currMode == Modes.VLASTNY_MODEL) {
            for (int i = 0; i < table.getRowCount(); i++) {
                if (RBAHodnotiaciSoftver.vm.VMTeams.get(i).getTeamID() == Integer.parseInt((String) table.getValueAt(i, 0))) {
                    RBAHodnotiaciSoftver.vm.VMTeams.get(i).SoftwarePoints = Integer.parseInt((String) table.getValueAt(i, 2));
                    RBAHodnotiaciSoftver.vm.VMTeams.get(i).ConstructionPoints = Integer.parseInt((String) table.getValueAt(i, 3));
                    RBAHodnotiaciSoftver.vm.VMTeams.get(i).CreativityPoints = Integer.parseInt((String) table.getValueAt(i, 4));
                    RBAHodnotiaciSoftver.vm.VMTeams.get(i).PresentationPoints = Integer.parseInt((String) table.getValueAt(i, 5));
                    RBAHodnotiaciSoftver.vm.VMTeams.get(i).BonusPoints = Integer.parseInt((String) table.getValueAt(i, 6));
                    RBAHodnotiaciSoftver.vm.VMTeams.get(i).Tier = Integer.parseInt((String) table.getValueAt(i, 8));
                } else {
                    JOptionPane.showMessageDialog(null, "Chyba v synchronizácií údajov. Čísla tímov alebo mená sa nezhodujú. Vykonané zmeny nie je možné uložiť.", "RBA", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
        RBAHodnotiaciSoftver.vm.SerializeTeams();
    }

    void WrapRCQualificationResults() {
        if (RBAHodnotiaciSoftver.currMode == Modes.RACING) {
            for (int i = 0; i < table.getRowCount(); i++) {
                if (RBAHodnotiaciSoftver.rc.RCTeams.get(i).getTeamID() == Integer.parseInt((String) table.getValueAt(i, 0))) {
                    
                    RBAHodnotiaciSoftver.rc.RCTeams.get(i).setBestQualificationTime(TeamRC.ConvertTimeToSeconds((String)table.getValueAt(i, 2)));
                    RBAHodnotiaciSoftver.rc.RCTeams.get(i).note = (String) table.getValueAt(i, 3);

                } else {
                    JOptionPane.showMessageDialog(null, "Chyba v synchronizácií údajov. Čísla tímov alebo mená sa nezhodujú. Vykonané zmeny nie je možné uložiť.", "RBA", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
        RBAHodnotiaciSoftver.rc.SaveTeams();
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1091, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 550, Short.MAX_VALUE)
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
            java.util.logging.Logger.getLogger(Overview.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Overview.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Overview.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Overview.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Overview().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
