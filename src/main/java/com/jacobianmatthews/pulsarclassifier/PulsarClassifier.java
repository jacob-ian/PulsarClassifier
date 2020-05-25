package com.jacobianmatthews.pulsarclassifier;

import com.scienceguyrob.lotaasclassifier.classifiers.Classifiers;
import com.scienceguyrob.lotaasclassifier.cli.CLI;
import com.scienceguyrob.lotaasclassifier.cli.CLParameter;
import com.scienceguyrob.lotaasclassifier.cli.ICLI;
import com.scienceguyrob.lotaasclassifier.utils.BasicLogger;
import com.scienceguyrob.lotaasclassifier.utils.Common;

import java.net.URL;


/**
 * This class takes in command-line arguments and starts the PulsarClassifier
 * program.
 * 
 * It is a derivative of the com.scienceguyrob.lotaasclassifier package.
 *
 * @author Jacob Ian Matthews & Rob Lyon
 *
 * @version 1.0, 22/5/2020
 */
public class PulsarClassifier {
    /**
     *
     *	VARIABLES
     *
     */

    /**
     * Full path to the file containing training data.
     */
    private static String training_path = "";

    /**
     * Full path to the file (ARFF file) to be assigned classifier predictions.
     */
    private static String predict_path = "";

    /**
     * Full path to the classifier validation file (ARFF file).
     */
    private static String validate_path = "";

    /**
     * Full path to the file containing the classification model to use.
     */
    private static String model_path = "";

    /**
     * The algorithm to train/use to make predictions.
     */
    private static int algorithm = -1;

    /**
     * Logging flag, if true, verbose logging outputs will be written to standard out.
     */
    private static boolean verbose = false;

    /**
     * Training flag. If true the system will attempt to build a new classifier.
     */
    private static boolean train = false;

    /**
     * Prediction flag. If true the system will attempt to classify data.
     */
    private static boolean predict = false;

    /**
     * Validation flag. If true, the system will attempt to validate the classifiers performance.
     */
    private static boolean validate = false;

    /**
     * The working directory for this code.
     */
    private static URL workingDir = PulsarClassifier.class.getProtectionDomain().getCodeSource().getLocation();

    /**
     * The object used to output debug/logging information.
     */
    private static BasicLogger log = new BasicLogger(verbose,workingDir.getFile().replace(".jar",".log"));

    //*****************************************
    //*****************************************
    //             Main Method
    //*****************************************
    //*****************************************

    /**
     * The main entry point to the application.
     * @param args the command line arguments.
     */
    public static void main(String[] args)
    {
        processCommandLine(args);

        /**
         * Run desired commands...
         */

        if(!train & !predict & !validate)
        {
            log.sout("Unable to train classifier model/make predictions - inputs invalid", true);
            safeExit();
        }
        else if(train & predict)
        {
            log.sout("Valid training and classification inputs provided - system unsure what to do", true);
            safeExit();
        }
        else if(train)
        {
            log.sout("Attempting to build a new classifier", true);
            ClassifierBuilder cb = new ClassifierBuilder(log,"ClassifierBuilder");

            // Actually build the ensemble classification system
            boolean result = cb.build(algorithm,training_path,model_path);

            // Check the results of the classifier build
            if( algorithm == -1 )
            {
                if (result)
                {
                    log.sout("Ensemble classifier built successfully", true);

                } else {

                    log.sout("Ensemble classifier couldn't be built.", true);
                }

            } else {
                if (result)
                {
                    log.sout(Classifiers.getClassifierName(algorithm)+" classifier built successfully", true);

                } else {
                    log.sout(Classifiers.getClassifierName(algorithm)+" classifier couldn't be built.", true);
                }
            }

        }
        else if(predict)
        {
            log.sout("Attempting to apply predictions using existing classifier", true);

            ClassPredictor cp = new ClassPredictor(log,"ClassPredictor");

            // Actually build the classification system
            boolean result = cp.predict(algorithm,predict_path,model_path);

            // Log the results back to the user
            if ( algorithm == -1 )
            {
                if ( result )
                {
                    log.sout("Ensemble classifier made predictions successfully.", true);
                } else {
                    log.sout("Ensemble classifier was unsuccessful in applying predictions.", true);
                }
            } else {

                if( result )
                {
                    log.sout(Classifiers.getClassifierName(algorithm)+ " classifier made predictions successfully", true);

                } else {
                    log.sout(Classifiers.getClassifierName(algorithm)+ " classifier unsuccessful in applying predictions", true);
                }

            }

            
        }
        else if(validate)
        {
            log.sout("Attempting to validate existing classifier performance", true);

            ClassifierValidator cv = new ClassifierValidator(log,"ClassifierValidator");

            // Validate the classifier chosen
            boolean result = cv.validate(algorithm,validate_path,model_path);

            // Output the results for the ensemble classifier
            if(algorithm == -1) {
                if(result) {
                    log.sout("Ensemble classifier validated successfully.", true);
                } else {
                    log.sout("Ensemble classifier couldn't be valdated successfully.", true);
                }

            // Output the results for the individual classifiers
            } else {
                if(result) {
                    log.sout(Classifiers.getClassifierName(algorithm)+ " classifier validated successfully", true);
                } else {
                    log.sout(Classifiers.getClassifierName(algorithm)+ " classifier validation unsuccessful", true);
                }
            }
            
        }

        /**
         * When done...
         */
        safeExit();
    }

    /**
     * Command line processing methods.
     */

    /**
     * Processes the command line parameters.
     * @param args the command line arguments
     */
    public static void processCommandLine(String[] args)
    {
        // Print some details, help etc to command line.
        printApplicationDetails();

        // Always make sure to write an app start message to the log file.
        printLogFileHeader();

        // Set logging to false before processing user input
        verbose = false;

        System.out.println( "\nReading Terminal Parameters...\n" );

        ICLI cli = getCommandlineOptions();
        cli.processArguments(args);

        // Update local variables based on user parameters.
        updateVariables(cli);
        printParameters();
    }

    /**
     * Updates class variables with user input parameters, if provided.
     * @param cli the command line parameters to use to obtain user input.
     */
    private static void updateVariables(ICLI cli)
    {
        /**
         * There are two main modes for the application. Either build a classifier, or
         * classify new data.
         *
         * Option one requires the full path to a training set file, the integer identifier
         * of the classifier to build, and the path to write the classification model to.
         *
         * Option two requires the full path to a file requiring predictions, the integer
         * identifier of the classifier used to make the predictions, and the path to the
         * classification model.
         *
         * Below we make sure this logic is correct.
         */

        // First deal with general simulation wide variables.
        if(cli.hasParameter(FLAG_VERBOSE))
            verbose = true;
        else
            verbose = false;

        // Check the algorithm supplied is valid, if not return since there's
        // no point checking the other variables.
        if (cli.hasParameter(FLAG_ALGORITHM))
            if (cli.getParameter(FLAG_ALGORITHM).toInt() > -2 && cli.getParameter(FLAG_ALGORITHM).toInt() < 6)
                algorithm = cli.getParameter(FLAG_ALGORITHM).toInt();
            else
            {
                log.sout("Algorithm value supplied via -a flag invalid (must be -1 to 5).", true);
                return;
            }

        if(cli.hasParameter(FLAG_TRAINING)) // if a training set has been provided.
        {
            // Try to load training data path (used for ML classification).
            if (cli.hasParameter(FLAG_TRAINING))
            {
                training_path = cli.getParameter(FLAG_TRAINING).getValue();

                if (Common.fileExist(training_path)) // If the training set is valid.
                {
                    if (cli.hasParameter(FLAG_MODEL))
                    {
                        model_path = cli.getParameter(FLAG_MODEL).getValue();

                        if (Common.isPathValid(model_path)) // If the training set is valid.
                            train = true;
                        else
                            log.sout("Model output path specified via -m flag invalid",true);
                    }
                    else
                        log.sout("No output model path supplied with -m flag",true);
                }
                else
                    log.sout("No machine learning training data supplied via -t flag",true);
            }
        }
        else if(cli.hasParameter(FLAG_PREDICT)) // if a training set has been provided.
        {
            // Try to load training data path (used for ML classification).
            if (cli.hasParameter(FLAG_PREDICT))
            {
                predict_path = cli.getParameter(FLAG_PREDICT).getValue();

                if (Common.fileExist(predict_path)) // If the file to classify is valid.
                {
                    if (cli.hasParameter(FLAG_MODEL))
                    {
                        model_path = cli.getParameter(FLAG_MODEL).getValue();

                        if (Common.isPathValid(model_path)) // If the model path is valid.
                            predict = true;
                        else
                            log.sout("Cannot load the classifier model via the -m flag",true);
                    }
                    else
                        log.sout("Cannot load the classifier model via the -m flag",true);
                }
                else
                    log.sout("No data to be classified supplied via -s flag",true);
            }
        }
        else if(cli.hasParameter(FLAG_VALIDATE)) // if a validation set has been provided.
        {
            // Try to load validation data path.
            if (cli.hasParameter(FLAG_VALIDATE))
            {
                validate_path = cli.getParameter(FLAG_VALIDATE).getValue();

                if (Common.fileExist(validate_path)) // If the file to classify is valid.
                {
                    if (cli.hasParameter(FLAG_MODEL))
                    {
                        model_path = cli.getParameter(FLAG_MODEL).getValue();

                        if (Common.isPathValid(model_path)) // If the model path is valid.
                            validate = true;
                        else
                            log.sout("Cannot load the classifier model via the -m flag",true);
                    }
                    else
                        log.sout("Cannot load the classifier model via the -m flag",true);
                }
                else
                    log.sout("No data to be used for validation supplied via -validate flag",true);
            }
        }
    }

    /**
     * @return the command line options for this application.
     */
    private static ICLI getCommandlineOptions()
    {
        ICLI cli = new CLI();

        cli.addParameter(FLAG_VERBOSE,
                "Verbose logging flag (optional, logging off by default)",
                CLParameter.BOOL_PARAM_TYPE);

        cli.addParameter(FLAG_VALIDATE,
                "The path to the validation data to use to build a classifier (required).",
                CLParameter.FILEPATH_PARAM_TYPE);

        cli.addParameter(FLAG_TRAINING,
                "The path to the training data to use to build a classifier (required).",
                CLParameter.FILEPATH_PARAM_TYPE);

        cli.addParameter(FLAG_MODEL,
                "The path to the classification model to load/create (required).",
                CLParameter.FILEPATH_PARAM_TYPE);

        cli.addParameter(FLAG_PREDICT,
                "The path to the observational data to label (required).",
                CLParameter.FILEPATH_PARAM_TYPE);

        cli.addParameter(FLAG_ALGORITHM,
                "The algorithm to use (required).",
                CLParameter.INT_PARAM_TYPE);

        return cli;
    }

    /**
     * Prints input parameters to the command line.
     */
    private static void printParameters()
    {
        String details = "\nAPPLICATION PARAMETERS\n";
        details += "Verbose logging    : " + verbose            + "\n";
        details += "Training set path  : " + training_path      + "\n";
        details += "Prediction path    : " + predict_path       + "\n";
        details += "Validation set path: " + validate_path      + "\n";
        details += "Model path         : " + model_path         + "\n";
        details += "Algorithm          : " + algorithm          + "\n";
        System.out.println(details);

    }

    /**
     * Prints application details when beginning execution.
     */
    private static void printApplicationDetails()
    {
        String details = "\n";
        details += "**************************************************************************\n";
        details += "|                                                                        |\n";
        details += "|                          PULSAR CLASSIFIER v1.0                        |\n";
        details += "|                                                                        |\n";
        details += "**************************************************************************\n";
        details += "| Description:                                                           |\n";
        details += "|                                                                        |\n";
        details += "| A machine learning pulsar classification program derived from Rob.     |\n";
        details += "| Lyon's LOTAASClassifier v1.0. Can be used in ensemble classification   |\n";
        details += "| or individual classification modes. This software can create           |\n";
        details += "| classification models and make predictions on data using those models. |\n";
        details += "| Requires Java 1.6 or later to run.                                     |\n";
        details += "|                                                                        |\n";
        details += "**************************************************************************\n";
        details += "| Author: Jacob Ian Matthews & Rob Lyon                                  |\n";
        details += "| Email : jacob@jacobian.com.au                                          |\n";
        details += "| web   : jacobianmatthews.com                                           |\n";
        details += "**************************************************************************\n";
        details += "| Required Command Line Arguments:                                       |\n";
        details += "|                                                                        |\n";
        details += "| Training mode (builds a new classifier model):                         |\n";
        details += "|                                                                        |\n";
        details += "| -t (path) path to a file containing training data in ARFF format.      |\n";
        details += "|             This is used to train the machine learning classifier that |\n";
        details += "|             assigns predicted candidate labels.                        |\n";
        details += "|                                                                        |\n";
        details += "| -m (path) output directory for the created model.                      |\n";
        details += "|                                                                        |\n";
        details += "| -a (int) the learning algorithm to build a model for. There are some   |\n";
        details += "|          possible choices listed below:                                |\n";
        details += "|                                                                        |\n";
        details += "|         -1 = Ensemble Classifier (builds all algorithms)               |\n";
        details += "|          1 = J48 decision tree                                         |\n";
        details += "|          2 = Multilayer perceptron (neural network)                    |\n";
        details += "|          3 = Naive Bayes                                               |\n";
        details += "|          4 = Support vector machine                                    |\n";
        details += "|                                                                        |\n";
        details += "| Prediction mode (applies the classifier to new data):                  |\n";
        details += "|                                                                        |\n";
        details += "| -m (path) path to the models directory, describing the pre-built       |\n";
        details += "|           classifier to use. The model must have been built using      |\n";
        details += "|           this tool or WEKA.                                           |\n";
        details += "|                                                                        |\n";
        details += "| -p (string) path to a file containing unlabelled data in ARFF format.  |\n";
        details += "|             The model loaded in via the -m flag will apply predicted   |\n";
        details += "|             labels to the data in this file.                           |\n";
        details += "|                                                                        |\n";
        details += "| -a (int) the learning algorithm stored in the model.                   |\n";
        details += "|          possible choices listed below:                                |\n";
        details += "|                                                                        |\n";
        details += "|         -1 = Ensemble Classifier                                       |\n";
        details += "|          1 = J48 decision tree                                         |\n";
        details += "|          2 = Multilayer perceptron (neural network)                    |\n";
        details += "|          3 = Naive Bayes                                               |\n";
        details += "|          4 = Support vector machine                                    |\n";
        details += "|                                                                        |\n";
        details += "| Validation mode (checks a new classifier model):                       |\n";
        details += "|                                                                        |\n";
        details += "| -m (path) path to the models directory, describing the pre-built       |\n";
        details += "|           classifier to use. The models must have been built using     |\n";
        details += "|           this tool or WEKA.                                           |\n";
        details += "|                                                                        |\n";
        details += "| -v        (string) path to a file containing labelled data in ARFF     |\n";
        details += "|             format. The model loaded in via the -m flag will then be   |\n";
        details += "|             tested against the labels in the file.                     |\n";
        details += "|                                                                        |\n";
        details += "| -a (int) the learning algorithm stored in the model.                   |\n";
        details += "|          possible choices listed below:                                |\n";
        details += "|                                                                        |\n";
        details += "|         -1 = Ensemble Classifier                                       |\n";
        details += "|          1 = J48 decision tree                                         |\n";
        details += "|          2 = Multilayer perceptron (neural network)                    |\n";
        details += "|          3 = Naive Bayes                                               |\n";
        details += "|          4 = Support vector machine                                    |\n";
        details += "|                                                                        |\n";
        details += "**************************************************************************\n";
        details += "| Optional Command Line Arguments:                                       |\n";
        details += "|                                                                        |\n";
        details += "| -d (boolean) verbose debugging flag.                                   |\n";
        details += "|                                                                        |\n";
        details += "**************************************************************************\n";
        details += "|                                                                        |\n";
        details += "| EXAMPLE USAGE:                                                         |\n";
        details += "|                                                                        |\n";
        details += "| java -jar LotaasClassifier.jar -a -1 -t /my/file.arff -m /my/models/   |\n";
        details += "|                                                                        |\n";
        details += "| This would build an ensemble classifier using the supplied training    |\n";
        details += "| set with the 'learned' models written to /models/                      |\n";
        details += "|                                                                        |\n";
        details += "**************************************************************************\n";
        details += "| License:                                                               |\n";
        details += "|                                                                        |\n";
        details += "| Code made available under the GPLv3 (GNU General Public License), that |\n";
        details += "| allows you to copy, modify and redistribute the code as you see fit    |\n";
        details += "| (http://www.gnu.org/copyleft/gpl.html). Though a mention to the        |\n";
        details += "| original author using the citation above in derivative works, would be |\n";
        details += "| very much appreciated.                                                 |\n";
        details += "**************************************************************************\n";

        System.out.println(details);
    }

    /**
     * Prints application details to the log file.
     */
    private static void printLogFileHeader()
    {
        log.setVerbose(true);

        if(log != null)
            log.sout("Welcome to PULSAR CLASSIFIER 1.0",true);
        else
        {
            System.out.println("Log file cannot be initialised, exiting...");
            safeExit();
        }
    }

    /**
     * Safely exits the application and updates the log.
     */
    private static void safeExit()
    {
        log.setVerbose(true);
        log.sout("Exiting PULSAR CLASSIFIER 1.0 correctly",true);
        System.exit(0);
    }

    /**
     * The command line flags...
     */

    private static String FLAG_VERBOSE      = "-d";
    private static String FLAG_VALIDATE     = "-v";
    private static String FLAG_TRAINING     = "-t";
    private static String FLAG_PREDICT      = "-p";
    private static String FLAG_MODEL        = "-m";
    private static String FLAG_ALGORITHM    = "-a";
}