package com.jacobianmatthews.pulsarclassifier;

import com.scienceguyrob.lotaasclassifier.classifiers.Classifiers;
import com.scienceguyrob.lotaasclassifier.classifiers.offline.J48Tester;
import com.scienceguyrob.lotaasclassifier.classifiers.offline.MLPTester;
import com.scienceguyrob.lotaasclassifier.classifiers.offline.NaiveBayesTester;
import com.scienceguyrob.lotaasclassifier.classifiers.offline.SVMTester;
import com.scienceguyrob.lotaasclassifier.utils.BasicLogger;


/**
 * This file is intended to build a Classification Model in four different Machine Learning algorithms
 * from a training data set provided by the user.
 * 
 * @author Jacob Ian Matthews
 * Contact: jacob@jacobian.com.au
 * @version 1.0, 22/05/2020
 * 
 */

/* Create the Class */
public class ClassifierBuilder extends com.scienceguyrob.lotaasclassifier.mvc.ClassifierBuilder {

    // CONSTRUCTORS
    public ClassifierBuilder(BasicLogger l, String n) {
        super(l, n);
    }

    public ClassifierBuilder(String n) {
        super(n);
    }

    /**
     * Builds the classifier for all algorithms with the training data set.
     * 
     * @param algorithm set to -1 for all algorithms, otherwise use original
     *                  integers for individual algorithms.
     * @return true if all classifiers have been successfully tested and trained.
     */
    public boolean build(int algorithm, String trainingSetPath, String modelPath) {

        // Create array of classifier integers
        int[] classifiers = { Classifiers.J48, Classifiers.MLP, Classifiers.NB, Classifiers.SVM };

        // Determine the algorithm integer
        switch (algorithm) {
            // Case for the ensemble classifier
            case -1:
                // Create success count variable
                int successCount = 0;

                // All algorithms selected therefore loop through training and testing them all
                for (final int classifier : classifiers) {
                    // Check if the result of building the selected classifier returns true
                    if (buildClassifier(classifier, trainingSetPath, modelPath)) {
                        // Add to the successCount as it was successful
                        successCount++;
                    }
                }

                // Check to see if all classifiers have built successfully
                if (successCount == classifiers.length) {
                    // All classifiers have been built successfully, therefore return true
                    return true;
                } else {

                    // Not all classifiers were successful, therefore return false
                    return false;
                }

                // Cases for the individual classifiers
            case Classifiers.J48:
                return trainAndTest(new J48Tester(log, "J48Tester"), trainingSetPath,
                        setModelPath(algorithm, modelPath));
            case Classifiers.MLP:
                return trainAndTest(new MLPTester(log, "MLPTester"), trainingSetPath,
                        setModelPath(algorithm, modelPath));
            case Classifiers.NB:
                return trainAndTest(new NaiveBayesTester(log, "NaiveBayesTester"), trainingSetPath,
                        setModelPath(algorithm, modelPath));
            case Classifiers.SVM:
                return trainAndTest(new SVMTester(log, "SVMTester"), trainingSetPath,
                        setModelPath(algorithm, modelPath));
            default:
                return false;

        }

    }
    /**
     * This method will call the trainAndTest method on the algorithm/classifier desired.
     * 
     * @param algorithm integer corresponding to the classifier algorithm
     * @param trainingSetPath String corresponding to the filepath of the training data set
     * @param modelPath String corresponding to the directory of the classifier models to be outputted
     * @return true if the selected classifier to be built is built successfully
     */
    private boolean buildClassifier(int algorithm, String trainingSetPath, String modelPath) {
        switch (algorithm) {
            case Classifiers.J48:
                return trainAndTest(new J48Tester(log, "J48Tester"), trainingSetPath,
                        setModelPath(algorithm, modelPath));
            case Classifiers.MLP:
                return trainAndTest(new MLPTester(log, "MLPTester"), trainingSetPath,
                        setModelPath(algorithm, modelPath));
            case Classifiers.NB:
                return trainAndTest(new NaiveBayesTester(log, "NaiveBayesTester"), trainingSetPath,
                        setModelPath(algorithm, modelPath));
            case Classifiers.SVM:
                return trainAndTest(new SVMTester(log, "SVMTester"), trainingSetPath,
                        setModelPath(algorithm, modelPath));
            default:
                return false;
        }
    }

    /**
     * This method converts a directory modelPath of which to output the classifier models, into individual
     * model filepaths for each classifier.
     * 
     * @param algorithm an integer relating to the classifier algorithm
     * @param modelPath String containing the directory to place the classifier models
     * @return String with the path to the model of each classifier.
     */
    private String setModelPath(int algorithm, String modelPath) {
        // Get the name of the classifier
        final String classifierName = Classifiers.getClassifierName(algorithm);

        // Create a model filename based on the classifier name
        final String fileName = "model_" + classifierName;

        // Check if modelPath ends in a forward slash
        if(modelPath.endsWith("/")){
            // Return the path to the model for the chosen algorithm
            return modelPath+fileName;
        } else {
            // Add a forward slash and return the model path
            return modelPath+"/"+fileName;
        }

    }
    
}