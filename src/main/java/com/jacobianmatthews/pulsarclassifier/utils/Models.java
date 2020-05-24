package com.jacobianmatthews.pulsarclassifier.utils;

import com.scienceguyrob.lotaasclassifier.utils.Common;

/**
 * The class Models contains methods to create the path to each classifier's model.
 * 
 * @author Jacob Ian Matthews
 * @version 1.0, 23/05/20
 */
public class Models extends Common {

    /**
     * This method converts a directory modelDir of which to output the classifier models, into individual
     * model filepaths for each classifier.
     * 
     * @param algorithm an integer relating to the classifier algorithm
     * @param modelDir String containing the directory to place the classifier models
     * @return String with the filepath of the model of the classifier.
    */
    public static String getModelFilePath(int algorithm, String modelDir)
    {
        // Check if the provided path is a valid directory
        if(isDirectory(modelDir)) {

            // Return the model's filepath
            return createModelFilePath(algorithm, modelDir);

        } else {
            // Create the directory as it doesn't exist
            if (dirCreateRecursive(modelDir)) {
                // Return the model's filepath
                return createModelFilePath(algorithm, modelDir);

            } else {
                // Couldn't create the directory, return a null filepath
                return null;
            }
        }
    }

    /**
     * 
     * @param algorithm An integer corresponding to the chosen algorithm.
     * @param modelDir  A string containing the path of the directory of models
     * @return A string with a filepath to a classifier's model
     */
    private static String createModelFilePath(int algorithm, String modelDir)
    {
        // Create the classifier's model's file name
        String fileName = createModelFileName(algorithm);

        // Check if the directory path ends in a forward slash
        if (modelDir.endsWith("/")) {
            // Don't add an extra forward slash and return the full path
            return modelDir + fileName;
        } else {
            // Add a slash to the end of the path
            return modelDir + "/" + fileName;
        }

    }

    /**
     * 
     * @param algorithm An integer corresponding to the chosen algorithm
     * @return Returns a string with the filename for the chosen algorithm
     */
    private static String createModelFileName(int algorithm)
    {
        // Get the algorithm/classifier's name
        String classifierName = Classifiers.getClassifierName(algorithm);

        // Return a file name dependent of the classifier's name
        return classifierName+"_model.m";
    }


    
}