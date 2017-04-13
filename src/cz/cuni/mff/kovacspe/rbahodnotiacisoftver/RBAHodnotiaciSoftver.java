/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.kovacspe.rbahodnotiacisoftver;

import java.io.*;
import java.nio.*;
import java.util.*;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 *
 * @author Peter
 */
public class RBAHodnotiaciSoftver {

    /**
     *
     */
    static public Modes currMode;
    public static List<Team> AllTeams = new ArrayList<Team>();

    /**
     * Načítava tímy z registrácie
     */
    public static void LoadAndCreateTeams() {
        AllTeams = new ArrayList<Team>();
        AllTeams.add(new Team(1, "Noobs", "Alejova 1", "jozko", null, null, true, true, false));
        AllTeams.add(new Team(2, "Noobs2", "Alejova 1", "fero", "duro", "baci", true, false, false));
        AllTeams.add(new Team(3, "Noobs3", "Alejova 1", "jozko", "pista", null, false, true, true));
        AllTeams.add(new Team(4, "Loosers", "Postova", "Lacko", "pista", null, true, true, true));
        SaveTeams(AllTeams,"Teams.res");
    }

    /**
     * Serializuje tímy do súboru Teams.res, kde si o nich uchováva všetky dáta.
     */
    
    static <T> void SaveTeams(List<T> teams,String fileName){
               try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(teams);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Chyba pri ukladaní tímov do súboru "+fileName, "RBA", JOptionPane.WARNING_MESSAGE);
        } 
    }
    

    /**
     * Načítava zoserializovaný list tímov zo súboru.
     *
     * @param <T> Typ timov ktore nacitava
     * @param FileName Meno súboru, z ktorého sa načítava
     * @return
     */
    static <T> List<T> LoadTeams(String FileName) {
        List<T> dest = new ArrayList<T>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FileName))) {
            dest = (List<T>) in.readObject();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Súbor " + FileName + " sa nedá čítať skontrolujte, \nči súbor existuje a či je v dobrom adresári ", "RBA", JOptionPane.WARNING_MESSAGE);
            RBAHodnotiaciSoftver.ChangeMode(Modes.ADMINISTRACIA);
        } catch (ClassCastException|ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Súbor " + FileName + " bol poškodený,\nvygenerujte nový", "RBA", JOptionPane.WARNING_MESSAGE);
            RBAHodnotiaciSoftver.ChangeMode(Modes.ADMINISTRACIA);
        } 
        return dest;
    }

    /**
     * Načítava zoserializované tímy zo súboru Teams.res.
     */
    static void LoadTeams() {
        AllTeams = LoadTeams("Teams.res");
        f.WriteTeams(AllTeams);

    }

    /**
     * Vytvorí list čistých, neobodovaných profilov tímov, prihlásených na
     * vlastný model
     *
     * @return list tímov na vlastný model
     */
    private static List<TeamVM> MakeVMList() {
        List<TeamVM> TeamsID = new ArrayList<>();
        for (Team t : AllTeams) {
            if (t.VM) {
                TeamsID.add(new TeamVM(t.getTeamID(), t.Name));
            }
        }
        return TeamsID;
    }

    /**
     * Vytvorí list čistých, neobodovaných profilov tímov, prihlásených na
     * Racing
     *
     * @return list tímov na Racing
     */
    private static List<TeamRC> MakeRCList() {
        List<TeamRC> TeamsID = new ArrayList<>();
        for (Team t : AllTeams) {
            if (t.RC) {
                TeamsID.add(new TeamRC(t.getTeamID(), t.Name));
            }
        }
        return TeamsID;
    }

    /**
     * Vytvorí list čistých, neobodovaných profilov tímov, prihlásených na
     * Robotickú výzvu
     *
     * @return list tímov na Robotickú výzvu
     */
    public static List<TeamRV> MakeRVList() {
        List<TeamRV> TeamsID = new ArrayList<>();
        for (Team t : AllTeams) {
            if (t.RV) {
                TeamsID.add(new TeamRV(t.getTeamID(), t.Name));
            }
        }
        return TeamsID;
    }

    /**
     * Vytvorí súbory zoserializovaných čistých, neobodovaných profilov tímov,
     * podľa príslušných kategórií.
     */
    public static void MakeEmptyResultFiles() {
        try (ObjectOutputStream VMout = new ObjectOutputStream(new FileOutputStream("VM.res"));
                ObjectOutputStream RCout = new ObjectOutputStream(new FileOutputStream("RC.res"));
                ObjectOutputStream RVout = new ObjectOutputStream(new FileOutputStream("RV.res"));) {

            try {
                VMout.writeObject(MakeVMList());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Chyba pri ukladaní profilov tímov na Vlastný model", "RBA", JOptionPane.WARNING_MESSAGE);
            }

            
                try {
                    RCout.writeObject(MakeRCList());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Chyba pri ukladaní profilov tímov na Racing", "RBA", JOptionPane.WARNING_MESSAGE);
                }
            
            try {
                RVout.writeObject(MakeRVList());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Chyba pri ukladaní profilov tímov na Robotickú výzvu", "RBA", JOptionPane.WARNING_MESSAGE);
                System.err.println(ex);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Chyba pri ukladaní profilov tímov.", "RBA", JOptionPane.WARNING_MESSAGE);
        }

    }

    /**
     * Vytvára položku v menu na zmenu hodnotiaceho prostredia.
     *
     * @return
     */
    static JMenu CreateContextSwitchingMenu() {
        JMenu kat = new JMenu("Prepnúť hodnotiace prostredie");
        JMenuItem rac = new JMenuItem("Racing");
        JMenuItem rv = new JMenuItem("Robotická výzva");
        JMenuItem vm = new JMenuItem("Vlastný model");
        JMenuItem adm = new JMenuItem("Administrácia");
        rac.addActionListener(e -> RBAHodnotiaciSoftver.ChangeMode(Modes.RACING));
        rv.addActionListener(e -> RBAHodnotiaciSoftver.ChangeMode(Modes.ROBOTICKA_VYZVA));
        vm.addActionListener(e -> RBAHodnotiaciSoftver.ChangeMode(Modes.VLASTNY_MODEL));
        adm.addActionListener(e -> RBAHodnotiaciSoftver.ChangeMode(Modes.ADMINISTRACIA));
        kat.add(rac);
        kat.add(rv);
        kat.add(vm);
        kat.add(adm);
        return kat;
    }

    /**
     * Vytvára menu pre časť Administrácia.
     *
     * @return
     */
    private static JMenuBar CreateMenu() {
        JMenu menu = new JMenu("RBA");
        JMenuBar menubar = new JMenuBar();
        menubar.add(menu);
        JMenuItem mi = new JMenuItem("Nacitaj timy");
        mi.addActionListener(e -> {
        });
        menu.add(mi);
        menubar.add(CreateContextSwitchingMenu());
        return menubar;
    }

    /**
     * Prepína medzi jednotlivými prostrediami kategórií a administráciou.
     *
     * @param mode Mód, na ktorý chce užívateľ prestaviť prostredie.
     */
    static void ChangeMode(Modes mode) {
        switch (currMode) {
            case ADMINISTRACIA:
                f.setVisible(false);
                break;
            case RACING:
                rc.setVisible(false);
                break;
            case ROBOTICKA_VYZVA:
                rv.setVisible(false);
                break;
            case VLASTNY_MODEL:
                vm.setVisible(false);
        }
        currMode = mode;
        switch (currMode) {
            case ADMINISTRACIA:
                f.setVisible(true);
                break;
            case RACING:
                rc.setVisible(true);
                rc.LoadTeams();
                
                break;
            case ROBOTICKA_VYZVA:
                rv.setVisible(true);
                rv.LoadTeams();
                break;
            case VLASTNY_MODEL:
                vm.setVisible(true);
                vm.LoadTeams();

        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        currMode = Modes.ADMINISTRACIA;
        f.setJMenuBar(CreateMenu());
        f.setVisible(true);
        vm.setVisible(false);
        rv.setVisible(false);
        rc.setVisible(false);
        //ChangeMode(Modes.ROBOTICKA_VYZVA);
    }

    static RBAForm f = new RBAForm();
    static RBAVlastnyModel vm = new RBAVlastnyModel();
    static RBARobotickaVyzva rv = new RBARobotickaVyzva();
    static RBARacing rc = new RBARacing();

}
