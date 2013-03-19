package ir.ac.iust.nlp.postagger;

/*
* Copyright (C) 2013 Iran University of Science and Technology
*
* This file is part of "POS Tagger" Project, as available 
* from http://nlp.iust.ac.ir This file is free software;
* you can redistribute it and/or modify it under the terms of the GNU General 
* Public License (GPL) as published by the Free Software Foundation, in 
* version 2 as it comes in the "COPYING" file of the VirtualBox OSE 
* distribution. VirtualBox OSE is distributed in the hope that it will be 
* useful, but WITHOUT ANY WARRANTY of any kind.
*
* You may elect to license modified versions of this file under the terms 
* and conditions of either the GPL.
*/

import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Mojtaba Khallash
 */
public class POSTagger {

    // -v 0 -mode <operation-mode(D2T|T2D|TR|TG)>
    public static void main(String[] args) {
        boolean visisble = true;
        String mode = "";
        String input = "";
        String inputConll = "";
        String output = "";
        String model = "";
        boolean hasGold = true;
        String goldPath  = null;
        Converter.POSType type = Converter.POSType.FPOS;
        int maxIter = 100;
        
        showIntroduction();

        boolean exception = false;
        try {
            for (int i = 0; i< args.length; i++) {
                switch (args[i]) {
                    case "-v":
                        i++;
                        String val = args[i];
                        if (!(val.equals("0") || val.equals("1"))) {
                            throw new Exception();
                        }
                        visisble = val.equals("1");
                        break;
                    case "-mode":
                        i++;
                        mode = args[i].toUpperCase();
                        break;
                    case "-i":
                        i++;
                        input = args[i];
                        break;
                    case "-c":
                        i++;
                        inputConll = args[i];
                        break;
                    case "-o":
                        i++;
                        output = args[i];
                        break;
                    case "-m":
                        i++;
                        model = args[i];
                        break;
                    case "-t":
                        i++;
                        switch (args[i].toLowerCase()) {
                            case "fpos":
                                type = Converter.POSType.FPOS;
                                break;
                            case "cpos":
                                type = Converter.POSType.CPOS;
                                break;
                        }
                        break;
                    case "-g":
                        i++;
                        switch (mode) {
                            case "D2T":
                                hasGold = ("1".equals(args[i]));
                                break;
                            case "TG":
                                goldPath = args[i];
                                break;
                        }
                        break;
                    case "-iter":
                        i++;
                        maxIter = Integer.parseInt(args[i]);
                        break;
                }
            }
            
            if (visisble == false && 
                (input.length() == 0 ||
                 (!mode.equals("TR") && output.length() == 0))) {
                throw new Exception();
            }
        } catch (Exception e) {
            exception = true;
            visisble = false;
        }
        finally {
            if (visisble == false) {
                if (exception == true) {
                    showHelp();
                    System.exit(1);
                }
                else {
                    try {
                        File in = new File(input);
                        if (!in.exists()) {
                            throw new FileNotFoundException("\t- Input file \"" + input + "\" not found.");
                        }
                        switch(mode) {
                            case "D2T":
                                if (output.indexOf(".") == -1) {
                                    output += "pos.lbl";
                                }
                                System.out.println("Start Converting Dependency to Tag.");
                                Converter.convertDependencyToTag(
                                        input, 
                                        output, 
                                        hasGold, 
                                        type);
                                System.out.println("'" + output + "' file created successfully.");
                                break;
                            case "T2D":
                                if (output.indexOf(".") == -1) {
                                    output += "predict_pos.conll";
                                }
                                in = new File(inputConll);
                                if (!in.exists()) {
                                    throw new FileNotFoundException("\t- Input conll file \"" + inputConll + "\" not found.");
                                }
                                System.out.println("Start Converting Tag to Dependency.");
                                Converter.convertTagToDependency(
                                        input, 
                                        inputConll, 
                                        output, 
                                        type);
                                System.out.println("'" + output + "' file created successfully.");
                                break;
                            case "TR":
                                File md = new File(model);
                                if (!md.exists()) {
                                    md.mkdirs();
                                }
                                RunnableTrain train = new RunnableTrain(
                                        null, 
                                        model,
                                        input, 
                                        maxIter);
                                train.run();
                                break;
                            case "TG":
                                if (output.indexOf(".") == -1) {
                                    output += "tagged_output.lbl";
                                }
                                if (goldPath != null) {
                                    File g = new File(goldPath);
                                    if (!g.exists()) {
                                        throw new FileNotFoundException("\t- Gold file \"" + g + "\" not found.");
                                    }
                                }
                                RunnableTagging tag = new RunnableTagging(
                                        null, 
                                        model,
                                        input, 
                                        output,
                                        goldPath);
                                tag.run();
                                break;
                        }
                        System.exit(0);
                    } catch(FileNotFoundException ex) {
                        System.out.println(ex.getMessage());
                        System.exit(1);
                    } catch(Exception ex) {
                        System.exit(1);
                    }
                }
            }
            else {
                showHelp();
            }
        }
        
        POSTaggerForm application = new POSTaggerForm();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(application);
            application.pack();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
        }
        application.setVisible(visisble);
    }
    
    private static void showIntroduction() {
        System.out.println("-----------------------------------------------------------------------------");
        System.out.println("                          POS Tagger 1.0");
        System.out.println("-----------------------------------------------------------------------------");
        System.out.println("                            Mojtaba Khallash");
        System.out.println();
        System.out.println("             Iran University of Science and Technology (IUST)");
        System.out.println("                                 Iran");
        System.out.println("-----------------------------------------------------------------------------");
        System.out.println();        
    }
    
    private static void showHelp() {
        System.out.println("Required Arguments:");
        System.out.println("        -v <visibility (0|1)>");
        System.out.println("        -mode <operation-mode(D2T|T2D|TR|TG)>\n");
        System.out.println("        D2T: Convert Dependency To Tagger");
        System.out.println("        -i <input conll file>");
        System.out.println("        -o <output tag path>");
        System.out.println("        -g <adding gold tag (0|1) [default: 1]>");
        System.out.println("        -t <type of tag (fpod|cpos) [default: fpos]>\n");
        System.out.println("        T2D: Convert Tag To Dependency");
        System.out.println("        -i <input tag file>");
        System.out.println("        -c <input conll file>");
        System.out.println("        -o <output dependency path>");
        System.out.println("        -t <type of tag (fpod|cpos) [default: fpos]>\n");
        System.out.println("        TR: Train Tagger");
        System.out.println("        -i <input tag file for train>");
        System.out.println("        -m <output model path>");
        System.out.println("        -iter <maximim number of iteration [default: 100]>\n");
        System.out.println("        TG: Tagging");
        System.out.println("        -i <input tag file for tagging>");
        System.out.println("        -m <trained model path>");
        System.out.println("        -o <output tag path>");
        System.out.println("        -g <gold tag file [optional]>\n");
    }
}