/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cuni.mff.kovacspe.rbahodnotiacisoftver;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

/**
 *
 * @author Peter
 */
public class TeXConvertor {

    public static void ConvertVMResultsToTeX(List<TeamVM> teams) {
        try (PrintWriter out = new PrintWriter(new FileOutputStream("VMres.tex"))) {
            out.println("\\begin{tabular}{|l|l|l|l|}");
            int i = 1;
            for (TeamVM t : teams) {             
                    out.println(i + "& T" + t.getTeamID() + "&" + t.Name + "&" + t.getSum() + "\\\\");                
                i++;
            }
            out.println("\\end{tabular}");
        } catch (IOException ex) {
            System.err.println("Nepodarilo sa zapisat");
        }
    }

    public static void ConvertRCQualifiactionResultsToTeX(List<TeamRC> teams) {
        try (PrintWriter out = new PrintWriter(new FileOutputStream("RCQualif.tex"))) {

            out.println("\\begin{tabular}{|l|l|l|l|}");
            int i = 1;
            for (TeamRC t : teams) {
                try {
                    out.println(i + "& T" + t.getTeamID() + "&" + t.Name + "&" + t.getBestQualificationTime() + "\\\\");
                } catch (TimeNotSetException ex) {
                    out.println(i + "& T" + t.getTeamID() + "&" + t.Name + "&-\\\\");
                }
                i++;
            }
            out.println("\\end{tabular}");
        } catch (IOException ex) {
            System.err.println("Nepodarilo sa zapisat");
        }
    }
}
