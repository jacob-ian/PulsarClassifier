package com.jacobianmatthews.pulsarclassifier;

import java.util.ArrayList;
import java.util.List;
import com.jacobianmatthews.pulsarclassifier.utils.Classification;
import com.jacobianmatthews.pulsarclassifier.utils.ClassificationList;
import com.jacobianmatthews.pulsarclassifier.utils.Classifiers;
import com.jacobianmatthews.pulsarclassifier.utils.Models;
import com.scienceguyrob.lotaasclassifier.classifiers.offline.J48Tester;
import com.scienceguyrob.lotaasclassifier.classifiers.offline.MLPTester;
import com.scienceguyrob.lotaasclassifier.classifiers.offline.NaiveBayesTester;
import com.scienceguyrob.lotaasclassifier.classifiers.offline.SVMTester;
import com.scienceguyrob.lotaasclassifier.io.Writer;
import com.scienceguyrob.lotaasclassifier.utils.BasicLogger;
import com.scienceguyrob.lotaasclassifier.wekawrappers.I_WekaTest;

/**
 * This class contains the methods and properties required to make predictions on data 
 * to classify pulsars.
 * 
 * @author Jacob Ian Matthews
 * @version 1.0, 24/05/20
 */
public class ClassPredictor extends com.scienceguyrob.lotaasclassifier.mvc.ClassPredictor {

    // CONSTRUCTORS
    public ClassPredictor(BasicLogger l, String n) { super(l, n); }
    public ClassPredictor(String n){ super(n); }


    public boolean predict(int algorithm, String predictPath, String modelsDir)
    {
        // Check if the ensemble classifier is being used
        if ( algorithm == -1 )
        {
            // Get the prefix of the prediction output files (input file path without extension)
            String fileName = predictPath.substring(0, predictPath.lastIndexOf("."));

            // Create an array of the [output].positive and [output].negative files
            List<String> positiveFiles = new ArrayList<String>();
            List<String> negativeFiles = new ArrayList<String>();

            // Create an index to count the number of classifiers successfully completing predictions
            int predictCount = 0;

            // Loop through the array of classifiers to make all predictions
            for(int classifier: Classifiers.classifiers)
            {
                // Check the status of the classifier's predictions
                if ( chooseClassifier(classifier, predictPath, modelsDir) )
                {
                    // Returned true, therefore add to prediction count
                    predictCount++;
                }
                
                // Get the name of the classifier
                String classifierName = Classifiers.getClassifierName(classifier);

                // Re-create the output file's name
                String outputName = fileName+"_"+classifierName;

                // Add the output file's name to the array of file names
                positiveFiles.add(outputName+".positive");
                negativeFiles.add(outputName+".negative");

            }

            // Check that all classifiers completed predictions
            if ( predictCount == Classifiers.classifiers.length )
            {  
                // All classifiers have finished making predictions, now we can create the final
                // classification output file by combining the positive pulsar predictions of all classifiers.
                // According to Tan et al. (2017), having positive classifications in 3 separate classifiers
                // indicates a more accurate classification.

                // Create the ensemble classifier's output file names
                String ensemblePositive = fileName+"_ensemble.positive";
                String ensembleNegative = fileName+"_ensemble.negative";

                // Create some flags to denote the ensemble classification process completed successfully
                boolean positiveSuccess = false;
                boolean negativeSuccess = false;

                // Create a list containing the postitive classifications and a count of their occurrences
                ClassificationList positiveList = new ClassificationList();

                // Create a list containing the postitive classifications and a count of their occurrences
                ClassificationList negativeList = new ClassificationList();

                // Build the list of positive classifications using the outputs from all the classifiers
                if( positiveList.buildList(positiveFiles) ){

                    // We now have a list of positive candidate classifications and we can apply
                    // the cutoff of 3 separate positive classifications for the ensemble classifier.

                    // Loop through the list of positive classifications
                    for( Classification positive: positiveList.getList() )
                    {
                        // Check the classification key-value pair for a value >=3
                        if( positive.getValue() > 2 )
                        {
                            // Try add it to the ensemble positive output file
                            if ( !Writer.append(ensemblePositive, positive.getKey()+"\n") )
                            {
                                log.sout("Couldn't add "+positive.getKey()+" to positive ensemble classifier output file.", true);
                            }
                        } else {

                            // Add it to the negative classification list as it didn't survive the cutoff
                            negativeList.add(positive.getKey(), positive.getValue());
                        }
                        
                    }
                    // Flag that the process was successful
                    positiveSuccess = true;
 
                } else {

                    // Return false and log the error
                    log.sout("Ensemble classifier positive candidates list couldn't be compiled.", true);
                    return false;
                }

                // Build a list of negative classifications from all classifiers
                if ( negativeList.buildList(negativeFiles) )
                {
                    // Loop through the list to produce the negative output file
                    for (Classification negative: negativeList.getList() )
                    {
                        // Get the key of the classification
                        String key = negative.getKey();

                        // Append it to the output file
                        if( !Writer.append(ensembleNegative, key+"\n") )
                        {
                            // Log the error 
                            log.sout("Couldn't add "+key+" to the negative ensemble classifier output file.", true);

                        }
                    }

                    // Flag that the process was successful
                    negativeSuccess = true;

                } else {
                    // Log the error and return false
                    log.sout("Ensemble classifier negative candidates list couldn't be compiled.", true);
                }

                // Check to see if both processes were successful
                if( positiveSuccess && negativeSuccess )
                {
                    // Return true to denote that it worked
                    return true;
                } else {

                    return false;
                }


            } else {

                // Return false as the ensemble classifier didn't complete
                log.sout("Not all classifiers completed predictions in ensemble classifier. Cannot produce final classifications file.", true);
                return false;
            }

        } else {
            
            // The ensemble classifier isn't being used, choose individual classifier
            return chooseClassifier(algorithm, predictPath, modelsDir);
        }

    }

    private boolean chooseClassifier(int algorithm, String predictPath, String modelDir)
    {
        // Determine which classifier to make predictions with
        switch (algorithm) 
        {
            case Classifiers.J48:
                return makePredictions(new J48Tester(log,"J48Tester"), algorithm, predictPath, Models.getModelFilePath(algorithm, modelDir));
            case Classifiers.MLP:
                return makePredictions(new MLPTester(log,"MLPTester"), algorithm, predictPath, Models.getModelFilePath(algorithm, modelDir));
            case Classifiers.NB:
                return makePredictions(new NaiveBayesTester(log,"NaiveBayesTester"), algorithm, predictPath, Models.getModelFilePath(algorithm, modelDir));
            case Classifiers.SVM:
                return makePredictions(new SVMTester(log,"SVMTester"), algorithm, predictPath, Models.getModelFilePath(algorithm, modelDir));
            default:
                return false;
        }
    }

    private boolean makePredictions(I_WekaTest classifier, int algorithm, String predictPath, String modelPath)
    {
        // Get the Input Data's file name without the extension
        final String file = predictPath.substring(0, predictPath.lastIndexOf("."));

        // Get the name of the classifier
        final String classifierName = Classifiers.getClassifierName(algorithm);

        // Create the name of the Classifier's output file
        final String outputName = file+"_"+classifierName;

        // Check if the classifier's model loaded
        final boolean loaded = classifier.loadModel(modelPath);

        // Check if the predictions were made
        final boolean predicted = classifier.predict(predictPath, outputName);

        // Check the results of the booleans
        if(loaded & predicted)
            return true;
        else
        {
            if(!loaded)
            {
                log.sout("Could not load the "+classifierName+" classifier model",true);
                return false;
            }
            else if(!predicted)
            {
                log.sout("Could not make predictions using the "+classifierName+" classifier model",true);
                return false;
            }
            else
            {
                log.sout("Could not predict using the "+classifierName+" classifier model",true);
                return false;
            }
        }

    }

}