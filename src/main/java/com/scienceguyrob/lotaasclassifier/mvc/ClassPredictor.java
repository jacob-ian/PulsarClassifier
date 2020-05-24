package com.scienceguyrob.lotaasclassifier.mvc;

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
 * File name:   ClassPredictor.java
 * Created:     24/10/2016
 * Author:      Rob Lyon
 * <p/>
 * Contact:	rob@scienceguyrob.com
 * Web:		www.scienceguyrob.com
 */

import com.scienceguyrob.lotaasclassifier.classifiers.Classifiers;
import com.scienceguyrob.lotaasclassifier.classifiers.offline.MLPTester;
import com.scienceguyrob.lotaasclassifier.classifiers.offline.NaiveBayesTester;
import com.scienceguyrob.lotaasclassifier.classifiers.offline.SVMTester;
import com.scienceguyrob.lotaasclassifier.utils.Base;
import com.scienceguyrob.lotaasclassifier.utils.BasicLogger;
import com.scienceguyrob.lotaasclassifier.wekawrappers.I_WekaTest;
import com.scienceguyrob.lotaasclassifier.classifiers.offline.J48Tester;

/**
 * The class ClassPredictor runs a desired classifier on a suppied data set.
 *
 * @author Rob Lyon
 *
 * @version 1.0, 04/09/14
 */
public class ClassPredictor extends Base
{
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
    public ClassPredictor(BasicLogger l, String n){ super(l,n);}

    /**
     * Default constructor that initialises this class with a logger
     * set not to log in verbose mode.
     * @param n the name of the class.
     */
    public ClassPredictor(String n){ super(n);}

    //*****************************************
    //*****************************************
    //              Methods
    //*****************************************
    //*****************************************

    /**
     * Builds the classifier specified by the algorithm variable.
     * @return true if the classifier has been successfully tested and trained.
     */
    public boolean predict(int algorithm,String predictPath,String modelPath)
    {
        switch (algorithm)
        {
            case Classifiers.J48:
                return makePredictions(new J48Tester(log,"J48Tester"), predictPath, modelPath);
            case Classifiers.MLP:
                return makePredictions(new MLPTester(log,"MLPTester"), predictPath, modelPath);
            case Classifiers.NB:
                return makePredictions(new NaiveBayesTester(log,"NaiveBayesTester"), predictPath, modelPath);
            case Classifiers.SVM:
                return makePredictions(new SVMTester(log,"SVMTester"), predictPath, modelPath);
            default:
                return false;
        }
    }

    /**
     * Makes predictions using the supplied classifier.
     * @param classifier the classifier to make predictions.
     * @return true if the predictions are made successfully, else false.
     */
    private boolean makePredictions(I_WekaTest classifier,String predictPath,String modelPath)
    {
        String prefix = predictPath.substring(0, predictPath.lastIndexOf('.'));
        boolean loaded=classifier.loadModel(modelPath);
        boolean madePredictions=classifier.predict(predictPath,prefix);

        if(loaded & madePredictions)
            return true;
        else
        {
            if(!loaded)
            {
                log.sout("Could not load the classifier model",true);
                return false;
            }
            else if(!madePredictions)
            {
                log.sout("Could not make predictions using classifier model",true);
                return false;
            }
            else
            {
                log.sout("Could not predict using chosen classifier model",true);
                return false;
            }
        }
    }
}
