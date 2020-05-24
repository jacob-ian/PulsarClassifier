package com.jacobianmatthews.pulsarclassifier;

import com.jacobianmatthews.pulsarclassifier.utils.Classifiers;
import com.jacobianmatthews.pulsarclassifier.utils.Models;
import com.scienceguyrob.lotaasclassifier.classifiers.offline.J48Tester;
import com.scienceguyrob.lotaasclassifier.classifiers.offline.MLPTester;
import com.scienceguyrob.lotaasclassifier.classifiers.offline.NaiveBayesTester;
import com.scienceguyrob.lotaasclassifier.classifiers.offline.SVMTester;
import com.scienceguyrob.lotaasclassifier.utils.BasicLogger;
import com.scienceguyrob.lotaasclassifier.wekawrappers.I_WekaTest;
/**
 * This class validates the ensemble and individual classifiers on a 
 * provided data set.
 * 
 * @author Jacob Ian Matthews
 * @version 1.0, 22/05/2020
 */
public class ClassifierValidator extends com.scienceguyrob.lotaasclassifier.mvc.ClassifierValidator {

    // CONSTRUCTORS
    public ClassifierValidator(BasicLogger l, String n) {
        super(l, n);
    }
    
    public ClassifierValidator(String n){ super(n);}

    /**
     * 
     * @param algorithm integer corresponding to the algorithm
     * @param validationPath String with the filepath of the data to test the classifier on
     * @param modelsDir String with the path of the directory containing the classifier models
     * @return true if the classifier(s) have been successfully tested
     */
    public boolean validate(int algorithm, String validationPath, String modelsDir)
    {
        // Check if using the ensemble classifier
        if( algorithm == -1 ){

            // Create a counter to keep track of the number of successful validations
            int successCount = 0;

            // Loop through the array of classifiers to validate them all
            for(int classifier: Classifiers.classifiers)
            {
                // Check for the result of the validation
                if( chooseClassifier(classifier, validationPath, modelsDir) )
                {
                    // Successful, therefore add to the counter
                    successCount++;
                }
            }

            // Check if all classifiers validated successfully
            if(successCount == Classifiers.classifiers.length)
            {
                // Return true, as validation was successful
                return true;
            } else {
                // One or validations were unsuccessful, return false
                return false;
            }

        } else {

            // Make predictions on the chosen classifier and return the result
            return chooseClassifier(algorithm, validationPath, modelsDir);

        }

    }

    /**
     * 
     * @param algorithm integer corresponding to the classifier.
     * @param validationPath String containing the filepath of the testing data
     * @param modelDir String containing the directory containing the classifier models
     * @return
     */
    private boolean chooseClassifier(int algorithm, String validationPath, String modelDir)
    {
        // Check which classifier needs to be validated and validate it
        switch (algorithm)
        {
            case Classifiers.J48:
                return performValidation(new J48Tester(log,"J48Tester"), algorithm, validationPath, Models.getModelFilePath(algorithm, modelDir));
            case Classifiers.MLP:
                return performValidation(new MLPTester(log,"MLPTester"), algorithm, validationPath, Models.getModelFilePath(algorithm, modelDir));
            case Classifiers.NB:
                return performValidation(new NaiveBayesTester(log,"NaiveBayesTester"), algorithm, validationPath, Models.getModelFilePath(algorithm, modelDir));
            case Classifiers.SVM:
                return performValidation(new SVMTester(log,"SVMTester"), algorithm, validationPath, Models.getModelFilePath(algorithm, modelDir));
            default:
                return false;
        }

    }

    /**
     * 
     * @param classifier a WEKA classifier
     * @param algorithm integer corresponding to the chosen algorithm
     * @param validationPath string containing the path to the data to test with
     * @param modelPath string containing the path to the classifier's model
     * @return
     */
    private boolean performValidation(I_WekaTest classifier, int algorithm, String validationPath, String modelPath)
    {
        // Check for successful loading and validation of the model
        boolean loaded = classifier.loadModel(modelPath);
        boolean validated = classifier.validate(validationPath);

        // Get the name of the classifier
        String classifierName = Classifiers.getClassifierName(algorithm);

        // Output the results of the validations
        if(loaded & validated)
            return true;
        else
        {
            if(!loaded)
            {
                log.sout("Could not load the "+classifierName+" classifier model",true);
                return false;
            }
            else if(!validated)
            {
                log.sout("Could not validate the "+classifierName+" model",true);
                return false;
            }
            else
            {
                log.sout("Could not perform validation on the "+classifierName+" classifier model",true);
                return false;
            }
        }
    }
    
}