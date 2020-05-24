package com.scienceguyrob.lotaasclassifier.classifiers.offline;

/**
 * This file is part of LotaasClassifier.
 * <p/>
 * LotaasClassifier is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * LotaasClassifier is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with LotaasClassifier.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * File name:   NaiveBayesTester.java
 * Created:     23/10/2016
 * Author:      Rob Lyon
 * <p/>
 * Contact:	rob@scienceguyrob.com
 * Web:		www.scienceguyrob.com
 */

import com.scienceguyrob.lotaasclassifier.io.*;
import com.scienceguyrob.lotaasclassifier.io.Writer;
import com.scienceguyrob.lotaasclassifier.stats.ClassifierStats;
import com.scienceguyrob.lotaasclassifier.utils.BasicLogger;
import com.scienceguyrob.lotaasclassifier.utils.Common;
import com.scienceguyrob.lotaasclassifier.wekawrappers.I_WekaTest;
import com.scienceguyrob.lotaasclassifier.wekawrappers.WekaClassifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;

import java.io.*;


/**
 * The class NaiveBayesTester tests the NaiveBayes implementation of a decision tree from Weka.
 *
 * @author Rob Lyon
 *
 * @version 1.0, 05/03/13
 */
public class NaiveBayesTester extends WekaClassifier implements I_WekaTest
{
    //*****************************************
    //*****************************************
    //              Variables
    //*****************************************
    //*****************************************

    /**
     * The decision tree classifier.
     */
    private NaiveBayes learner = new NaiveBayes();

    /**
     * The name of the classifier.
     */
    private String name = "NaiveBayes";

    /**
     * A string builder object used to store positive classifications.
     */
    private StringBuilder pb = new StringBuilder();

    /**
     * A string builder object used to store negative classifications.
     */
    private StringBuilder nb = new StringBuilder();

    /**
     *
     *	CONSTRUCTOR
     *
     */

    /**
     * Default constructor.
     * @param l the logger to be used by this class.
     * @param n the name of the class.
     */
    public NaiveBayesTester(BasicLogger l, String n){ super(l,n); }

    /**
     * Default constructor that initialises this class with a logger
     * set not to log in verbose mode.
     * @param n the name of the class.
     */
    public NaiveBayesTester(String n){ super(n);}

    /**
     *
     *	GETTERS & SETTERS
     *
     */

    public NaiveBayes getClassifier(){ return this.learner; }

    /**
     *
     *	METHODS
     *
     */

    /* (non-Javadoc)
     * @see cs.man.ac.uk.moawrappers.I_WekaTest#train()
     */
    @Override
    public boolean train(String trainingSet)
    {
        // Store training set file path.
        this.trainingSetFilePath = trainingSet;

        log.sout("Training a " + name + " classifier", true);
        log.sout("Training a " + name + " classifier on training data in: " + this.trainingSetFilePath, true);

        long startTime = System.nanoTime();
        try
        {
            BufferedReader reader = new BufferedReader( new FileReader(this.trainingSetFilePath));
            Instances data = new Instances(reader);
            data.setClassIndex(data.numAttributes() - 1);

            log.sout("Training instances being used to train the classifier: " + data.numInstances(), true);
            learner.buildClassifier(data);

            long endTime = System.nanoTime();
            long nanoseconds = endTime - startTime;
            double seconds = (double) nanoseconds / 1000000000.0;
            log.sout("Training a "+name+" classifier completed in "+nanoseconds+" (ns) or "+seconds+" (s)", true);
            return true;
        }
        catch (IOException e)
        {
            log.eout("Could not train a " + name + " classifier: IOException on training data file", e);
            return false;
        }
        catch (Exception e)
        {
            log.eout("Could not train a " + name + " classifier: Exception building model", e);
            return false;
        }
    }

    /* (non-Javadoc)
	 * @see cs.man.ac.uk.moawrappers.I_WekaTest#validate(java.lang.String, java.lang.String)
	 */
    @SuppressWarnings("unused")
    @Override
    public boolean validate(String validationSet)
    {
        log.sout("Validating the " + name + "Classifier", true);

        try
        {
            // Test meta information and important variables.
            int correctPositiveClassifications = 0;
            int correctNegativeClassifications = 0;
            int instanceNumber=0;

            ClassifierStats stats = new ClassifierStats();

            //Used to store and append output information.
            StringBuilder misclassifiedLabelledInstances = new StringBuilder();

            // Prepare data for testing
            BufferedReader reader = new BufferedReader( new FileReader(validationSet));
            Instances data = new Instances(reader);
            data.setClassIndex(data.numAttributes() - 1);

            log.sout("The "+ name + " classifier is ready.", true);
            log.sout("Validating the " + name + " classifier on all instances available.", true);
            log.sout("Validation set: "+ validationSet, true);
            log.sout("Validation set instances: " + data.numInstances(), true);

            long startTime = System.nanoTime();

            // label instances
            for (int i = 0; i < data.numInstances(); i++)
            {
                instanceNumber+=1;

                double classification = learner.classifyInstance(data.instance(i));
                String instanceClass= Double.toString(data.instance(i).classValue());

                // LABELLED TEST DATA - Weka knows the correct class.
                if(classification==1 && instanceClass.startsWith("0"))// Predicted positive, actually negative
                {
                    stats.incrementFP();
                    misclassifiedLabelledInstances.append(getScores(data.instance(i),data.numAttributes()-1)+"0,FP\n");
                }
                else if(classification==1 && instanceClass.startsWith("1"))// Predicted positive, actually positive
                {
                    correctPositiveClassifications+=1;
                    stats.incrementTP();
                }
                else if(classification==0 && instanceClass.startsWith("1"))// Predicted negative, actually positive
                {
                    stats.incrementFN();
                    misclassifiedLabelledInstances.append(getScores(data.instance(i),data.numAttributes()-1)+"1,FN\n");
                }
                else if(classification==0 && instanceClass.startsWith("0"))// Predicted negative, actually negative
                {
                    correctNegativeClassifications+=1;
                    stats.incrementTN();
                }
            }

            long endTime = System.nanoTime();
            long duration = endTime - startTime;
            double seconds = (double) duration / 1000000000.0;

            stats.calculate();
            int memoryInBytes = 0;

            log.sout("Validation of " + name + " classifier completed in "+duration+" (ns) or "+seconds+" (s)", true);
            System.out.println("Performance of " + name + " classifier");
            System.out.println(stats.toString());

            System.out.println("Recording " + name + " classifier errors");

            // Build path to write misclassified data in ARFF format to.
            String extension = "";

            int i = validationSet.lastIndexOf('.');
            if (i > 0)
            {
                extension = validationSet.substring(i,validationSet.length());
                String missclassifiedARRFPath = validationSet.replace(extension, "_Missclassified_"+Common.getCondensedTime("_")+"_"+Common.getDateWithSeperator("_")+".arff");

                System.out.println("Recording classifier errors to: "+ missclassifiedARRFPath);

                missclassificationsToARFF(missclassifiedARRFPath, misclassifiedLabelledInstances.toString(), data.numAttributes());
            }

            return true;
        }
        catch (Exception e)
        {
            log.eout("Could not validate " + name + " classifier due to an error",e);
            return false;
        }
    }

    /**
     * Make predictions on never before seen instances.
     * @param newData the data to make predictions on.
     */
    public boolean predict(String newData,String outputPath)
    {
        // Store training set file path.
        this.testSetFilePath = newData;

        log.sout("Attempting to classify data using the " + name + " classifier", true);

        try
        {
            // Test meta information and important variables.
            int positiveClassifications = 0;
            int negativeClassifications = 0;
            @SuppressWarnings("unused")
            int instanceNumber=0;

            //Used to store and append output information.
            //StringBuilder predictions = new StringBuilder();

            // Prepare data for testing
            BufferedReader reader = new BufferedReader( new FileReader(this.testSetFilePath));
            Instances data = new Instances(reader);
            data.setClassIndex(data.numAttributes() - 1);

            log.sout("The " + name + " classifier is ready.", true);
            log.sout("The " + name+" classify will process all instances available.", true);
            log.sout("Total examples to be classified: " + data.numInstances(), true);

            long startTime = System.nanoTime();

			/*
			 * This next part is required, so that we can tell which
			 * candidate we are classifying.
			 */

            String line = "";

            // Read the file and display it line by line.
            BufferedReader in = null;

            try
            {
                //open stream to file
                in = new BufferedReader(new FileReader(newData));

                try
                {
                    while ((line = in.readLine()) != null)
                        if(line.contains("@data"))
                            break;
                }
                catch(IOException e){ log.eout("Error reading file containing data to be classified",e); }
            }
            catch (FileNotFoundException e)
            {
                log.eout("Error file containing data to be classified cannot be found",e);
                return false;
            }

            // Here labels are applied to the data.
            for (int i = 0; i < data.numInstances(); i++)
            {
                line = in.readLine();// get next instance from data file.
                instanceNumber+=1;

                if(i%10000==0)
                    log.sout("The " + name + " model is classifying instance: "+i, true);

                double classification = learner.classifyInstance(data.instance(i));

                if(classification == 1)// Predicted positive.
                {
                    positiveClassifications++;
                    processPositivePrediction(outputPath,line);
                }
                else// Predicted negative
                {
                    negativeClassifications++;
                    processNegativePrediction(outputPath,line);
                }
            }

            // Close file after it has been read.
            in.close();

            if(pb.length()>0)
            {
                com.scienceguyrob.lotaasclassifier.io.Writer.append(outputPath+".positive", pb.toString());
                pb.setLength(0);
            }

            if(nb.length()>0)
            {
                com.scienceguyrob.lotaasclassifier.io.Writer.append(outputPath+".negative", nb.toString());
                nb.setLength(0);
            }

            // Report details.
            long endTime = System.nanoTime();
            long duration = endTime - startTime;
            double seconds = (double) duration / 1000000000.0;

            log.sout("Classification using "+name+" completed in " + duration + " (ns) or " + seconds + " (s)", true);
            log.sout("Predicted positive: " + positiveClassifications + " predicted negative " + negativeClassifications,true);

            return true;
        }
        catch (Exception e)
        {
            log.eout("Could not make predictions using " +name+ " classifier due to an error",e);
            return false;
        }
    }

    /**
     * Processes data items given a positive label.
     * @param data given a positive class label
     */
    public void processPositivePrediction(String outputPath, String data)
    {
        String candidatePath = data.substring(data.lastIndexOf("%")+1, data.length());
        //String dataComponent = data.replace(",?%" + candidatePath, "");

        //String prediction = candidatePath + "," + dataComponent + "\n";
        String prediction = candidatePath + "\n";

        pb.append(prediction);

        if(pb.length()>10000)
        {
            Writer.append(outputPath+".positive", pb.toString());
            pb.setLength(0);
        }
    }

    /**
     * Processes data items given a negative label.
     * @param data given a negative class label
     */
    public void processNegativePrediction(String outputPath,String data)
    {
        String candidatePath = data.substring(data.lastIndexOf("%")+1, data.length());
        //String dataComponent = data.replace(",?%" + candidatePath, "");

        //String prediction = candidatePath + "," + dataComponent + "\n";
        String prediction = candidatePath + "\n";

        nb.append(prediction);

        if(nb.length()>5000)
        {
            Writer.append(outputPath+".negative", nb.toString());
            nb.setLength(0);
        }
    }

    /**
     * Saves the classifier model to the path specified.
     * @param path the path to save the classifier model to.
     */
    public boolean saveModel(String path)
    {
        try
        {
            Common.fileDelete(path);// Delete existing model if it exists.
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
            oos.writeObject(learner);
            oos.flush();
            oos.close();

            return Common.fileExist(path);
        }
        catch(Exception e)
        {
            log.eout("Could not save "+name+" model",e);
            return false;
        }
    }

    /**
     * Loads the classifier model at the path specified.
     * @param path to the model to load.
     */
    public boolean loadModel(String path)
    {
        try
        {
            // Deserialize model
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
            NaiveBayes cls = (NaiveBayes) ois.readObject();
            ois.close();

            learner = cls;

            if(learner!=null)
                return true;
            else
                return false;
        }
        catch(Exception e)
        {
            log.eout("Could not load the "+name+" classification model",e);
            return false;
        }
    }

    /* (non-Javadoc)
     * @see cs.man.ac.uk.classifiers.I_WekaTest#reset()
     */
    @Override
    public boolean reset()
    {
        log.sout("Resetting the classification model of the " + name + " classifier", true);
        try
        {
            stats.reset();
            learner = null;
            learner = new NaiveBayes();
            log.sout("The " + name + " classifier was reset successfully", true);
            return true;
        }
        catch (Exception e)
        {
            log.eout("Caught Exception resetting the "+name+" classifier",e);
            return false;
        }
    }
}

