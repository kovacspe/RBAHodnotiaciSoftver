/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rbahodnotiacisoftver;

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
    public static List<Team> AllTeams;
    public static List<Integer> VMTeamsID;

    /**
     * Načítava tímy z registrácie
     */
    public static void LoadAndCreateTeams() {
        AllTeams = new ArrayList<Team>();
        AllTeams.add(new Team(1, "Noobs", "Alejova 1", "jozko", null, null, true, true, false));
        AllTeams.add(new Team(2, "Noobs2", "Alejova 1", "fero", "duro", "baci", true, false, false));
        AllTeams.add(new Team(3, "Noobs3", "Alejova 1", "jozko", "pista", null, false, true, true));
        SaveTeams();
    }

    /**
     * Serializuje tímy do súboru Teams.res, kde si o nich uchováva všetky dáta.
     */
    public static void SaveTeams() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("Teams.res"))) {
            out.writeObject(AllTeams);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Chyba pri ukladaní tímov", "RBA", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Načítava zoserializované tímy zo súboru Teams.res. 
     */
    public static void LoadTeams() {
        AllTeams = new ArrayList<Team>();
        try (ObjectInputStream out = new ObjectInputStream(new FileInputStream("Teams.res"))) {
            AllTeams = (List<Team>) out.readObject();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Nepodarilo sa načítať tími.\n Súbor možno neexistuje vytvorte nový načítaním tímov z registrácie.", "RBA", JOptionPane.WARNING_MESSAGE);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Nepodarilo sa načítať tími. Súbor bol poškodený.\n Vytvorte nový súbor načítaním tímov z registrácie.", "RBA", JOptionPane.WARNING_MESSAGE);
        }
        f.WriteTeams(AllTeams);

    }

    /**
     * Vytvorí list čistých, neobodovaných profilov tímov, prihlásených na vlastný model
     * @return list tímov na vlastný model
     */
    public static List<TeamVM> MakeVMList() {
        List<TeamVM> TeamsID = new ArrayList<>();
        for (Team t : AllTeams) {
            if (t.VM) {
                TeamsID.add(new TeamVM(t.getTeamID(), t.Name));
            }
        }
        return TeamsID;
    }

    
    /**
     * Vytvorí list čistých, neobodovaných profilov tímov, prihlásených na Racing
     * @return list tímov na Racing
     */
    public static List<Integer> MakeRCList() {
        List<Integer> TeamsID = new ArrayList<>();
        for (Team t : AllTeams) {
            if (t.RC) {
                TeamsID.add(t.getTeamID());
            }
        }
        return TeamsID;
    }

    /**
     * Vytvorí list čistých, neobodovaných profilov tímov, prihlásených na Robotickú výzvu
     * @return list tímov na Robotickú výzvu
     */
    public static List<Integer> MakeRVList() {
        List<Integer> TeamsID = new ArrayList<>();
        for (Team t : AllTeams) {
            if (t.RV) {
                TeamsID.add(t.getTeamID());
            }
        }
        return TeamsID;
    }

    /**
     * Vytvorí súbory zoserializovaných čistých, neobodovaných profilov tímov, podľa príslušných kategórií.
     */
    public static void MakeEmptyResultFiles() {
        try (ObjectOutputStream VMout = new ObjectOutputStream(new FileOutputStream("VM.res"));
                ObjectOutputStream RCout = new ObjectOutputStream(new FileOutputStream("RC.res"));
                ObjectOutputStream RSout = new ObjectOutputStream(new FileOutputStream("RS.res"));) {

            try {
                VMout.writeObject(MakeVMList());
            } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Chyba pri ukladaní profilov tímov na Vlastný model", "RBA", JOptionPane.WARNING_MESSAGE);
            }

            MakeRCList().stream().forEach(e -> {
                try {
                    RCout.writeObject(e);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Chyba pri ukladaní profilov tímov na Racing", "RBA", JOptionPane.WARNING_MESSAGE);
                }
            });
            MakeRVList().stream().forEach(e -> {
                try {
                    RSout.writeObject(e);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Chyba pri ukladaní profilov tímov na Robotickú výzvu", "RBA", JOptionPane.WARNING_MESSAGE);
                }
            });
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Chyba pri ukladaní profilov tímov.", "RBA", JOptionPane.WARNING_MESSAGE);
        }

    }

    /**
     * Vytvára menu pre časť Administrácia.
     * @return 
     */
    public static JMenuBar CreateMenu() {
        JMenu menu = new JMenu("RBA");
        JMenuBar menubar = new JMenuBar();
        menubar.add(menu);
        JMenuItem mi = new JMenuItem("Nacitaj timy");
        mi.addActionListener(e -> {
        });
        menu.add(mi);
        JMenu kat = new JMenu("Prepnúť hodnotiace prostredie");
        JMenuItem rac = new JMenuItem("Racing");
        JMenuItem rv = new JMenuItem("Robotická výzva");
        JMenuItem vm = new JMenuItem("Vlastný model");
        rac.addActionListener(e->RBAHodnotiaciSoftver.ChangeMode(Modes.RACING));
        rv.addActionListener(e->RBAHodnotiaciSoftver.ChangeMode(Modes.ROBOTICKA_VYZVA));
        vm.addActionListener(e->RBAHodnotiaciSoftver.ChangeMode(Modes.VLASTNY_MODEL));
        kat.add(rac);
        kat.add(rv);
        kat.add(vm);
        menubar.add(kat);
        return menubar;
    }

    
    /**
     * Prepína medzi jednotlivými prostrediami kategórií a administráciou.
     * @param mode Mód, na ktorý chce užívateľ prestaviť prostredie. 
     */
    public static void ChangeMode(Modes mode) {
        switch (currMode) {
            case ADMINISTRACIA:
                f.setVisible(false);
                break;
            case RACING:
                break;
            case ROBOTICKA_VYZVA:
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
                break;
            case ROBOTICKA_VYZVA:
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

    }

    static RBAForm f = new RBAForm();
    static RBAVlastnyModel vm = new RBAVlastnyModel();
    

}
