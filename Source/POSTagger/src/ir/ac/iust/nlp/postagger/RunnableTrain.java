package ir.ac.iust.nlp.postagger;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintStream;

/**
 *
 * @author Mojtaba Khallash
 */
public class RunnableTrain implements Runnable {
    
    POSTaggerForm target = null;
    public static PrintStream out = System.out;
    
    String model;
    String trainFile;
    int maxIters;
    
    public RunnableTrain(POSTaggerForm target, String model, String train, int maxItres) {
        this.target = target;
        this.model = model;
        this.trainFile = train;
        this.maxIters = maxItres;
    }
    
    @Override
    public void run() {
        try {
            Process p;
            BufferedReader stdError;
            String s;

            p = Runtime.getRuntime().exec("java -Dfile.encoding=UTF8 -cp lib" + File.separator +
                    "mxpost.jar tagger.TrainTagger " + this.model + " " + 
                    this.trainFile);
            /* Equal to */
            // tagger.TrainTagger.main(new String[] {this.model, this.trainFile});
            /*          */
            stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            while ((s = stdError.readLine()) != null) {
                out.println(s);
            }

            String eventsPath = this.model + File.separator + "events";
            String featurePath = this.model + File.separator + "tagfeatures.fmap";
            String modelPath = this.model + File.separator + "model";
            String maxIterations = String.valueOf(this.maxIters);
            p = Runtime.getRuntime().exec("java -Dfile.encoding=UTF8 -cp lib" + File.separator +
                    "mxpost.jar maxent.GIS " + eventsPath + " " + 
                    featurePath + " " + modelPath + " " + maxIterations);
            /* Equal to */
            // maxent.GIS.main(new String[] {eventsPath, 
            //    featurePath, 
            //    modelPath, 
            //    maxIterations});
            /*          */
            stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            while ((s = stdError.readLine()) != null) {
                out.println(s);
            }
        } catch (Exception ex) {
        } finally {
            if (target != null) {
                target.trainThreadFinished();
            }
        }
    }
}