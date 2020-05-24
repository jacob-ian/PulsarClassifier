package com.jacobianmatthews.pulsarclassifier;

import com.jacobianmatthews.pulsarclassifier.utils.Models;
import com.jacobianmatthews.pulsarclassifier.utils.Classifiers;
import com.scienceguyrob.lotaasclassifier.classifiers.offline.J48Tester;
import com.scienceguyrob.lotaasclassifier.classifiers.offline.MLPTester;
import com.scienceguyrob.lotaasclassifier.classifiers.offline.NaiveBayesTester;
import com.scienceguyrob.lotaasclassifier.classifiers.offline.SVMTester;
import com.scienceguyrob.lotaasclassifier.utils.BasicLogger;


/**
 * This file is intended to build a Classification Model in four different Machine Learning algorithms
 * from a training data set provided by the user. It can be completed in ensemble (all classifiers) by using
 * algorithm=-1, or individually by using their respective integers.
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

        // Check if user requested the ensemble classifier
        if (algorithm == -1) {

            // Create success count variable
            int successCount = 0;

            // All algorithms selected therefore loop through training and testing them all
            for (int classifier : Classifiers.classifiers) {
                // Check if the result of building the selected classifier returns true
                if (buildClassifier(classifier, trainingSetPath, modelPath)) {
                    // Add to the successCount as it was successful
                    successCount++;
                }
            }

            // Check to see if all classifiers have built successfully
            if (successCount == Classifiers.classifiers.length) {
                // All classifiers have been built successfully, therefore return true
                return true;

            } else {

                // Not all classifiers were successful, therefore return false
                return false;
            }

        } else {

            // Not using the ensemble classifier, build the individual classifier and return the result
            return buildClassifier(algorithm, trainingSetPath, modelPath);

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
    private boolean buildClassifier(int algorithm, String trainingSetPath, String modelDir) {
        switch (algorithm) {
            case Classifiers.J48:
                return trainAndTest(new J48Tester(log, "J48Tester"), trainingSetPath,
                        Models.getModelFilePath(algorithm, modelDir));
            case Classifiers.MLP:
                return trainAndTest(new MLPTester(log, "MLPTester"), trainingSetPath,
                        Models.getModelFilePath(algorithm, modelDir));
            case Classifiers.NB:
                return trainAndTest(new NaiveBayesTester(log, "NaiveBayesTester"), trainingSetPath,
                        Models.getModelFilePath(algorithm, modelDir));
            case Classifiers.SVM:
                return trainAndTest(new SVMTester(log, "SVMTester"), trainingSetPath,
                        Models.getModelFilePath(algorithm, modelDir));
            default:
                return false;
        }
    }
    
}