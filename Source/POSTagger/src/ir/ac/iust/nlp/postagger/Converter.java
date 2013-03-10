package ir.ac.iust.nlp.postagger;

import java.io.*;

/**
 *
 * @author Mojtaba Khallash
 */
public class Converter {
    
    public enum POSType {
        FPOS,
        CPOS
    }
    
    public static void convertDependencyToTag(String input, String output,
            boolean addGoldTag, POSType type) throws Exception {
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(input), "UTF8"));
        String s1;
        StringBuilder sb = new StringBuilder();
        boolean start = true;
        while( (s1 = reader.readLine()) != null) {
            String[] parts1 = s1.split("\t");
            if (parts1.length >= 2)
            {
                if(start == false) {
                    sb.append(" ");
                }
                else
                    start = false;
                String word = parts1[1].replace(' ', '_');
                if (addGoldTag == true) {
                    String gold;
                    if (type == POSType.FPOS) {
                        gold = parts1[4];
                    }
                    else {
                        gold = parts1[3];
                    }
                    sb.append(word).append("_").append(gold);
                }
                else
                    sb.append(word);
            }
            else{
                start = true;
                sb.append("\n");
            }
        }
        try (Writer out = new BufferedWriter(new OutputStreamWriter(
                 new FileOutputStream(output), "UTF-8"))) {
            out.write(sb.toString());
        }
    }
    
    public static void convertTagToDependency(String inputTag, String inputConll, 
            String output, POSType type) throws Exception {
        StringBuilder sb;
        try (BufferedReader reader1 = new BufferedReader(new InputStreamReader(
                     new FileInputStream(inputTag), "UTF8"))) {
            BufferedReader reader2 = new BufferedReader(new InputStreamReader(
                    new FileInputStream(inputConll), "UTF8"));
            String s1;
            String s2;
            sb = new StringBuilder();
            boolean start = true;
            while( (s1 = reader1.readLine()) != null) {
                String[] parts1 = s1.split(" ");
                for (int i = 0; i < parts1.length; i++) {
                    String[] vals1 = parts1[i].split("_");
                    String predictTag = vals1[vals1.length - 1];

                    s2 = reader2.readLine();
                    String[] parts2 = s2.split("\t");
                    if (type == POSType.FPOS) {
                        parts2[4] = predictTag;
                    }
                    else {
                        parts2[3] = predictTag;
                    }
                    sb.append(parts2[0]);
                    for (int j = 1; j < parts2.length; j++) {
                        sb.append("\t").append(parts2[j]);
                    }
                    sb.append("\n");
                }
                reader2.readLine();
                sb.append("\n");
            }
        }
        
        try (Writer out = new BufferedWriter(new OutputStreamWriter(
                 new FileOutputStream(output), "UTF-8"))) {
            out.write(sb.toString());
        }
    }
}