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
 * File name:   ClassifierBuilder.java
 * Created:     23/10/2016
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
 * This class is used to control the building of new classifiers.
 *
 * @author Rob Lyon
 *
 * @version 1.0, 04/09/14
 */
public class ClassifierBuilder extends Base
{
    /**
     * Default constructor.
     * @param l the logger to be used by this class.
     * @param n the name of the class.
     */
    public ClassifierBuilder(BasicLogger l, String n){ super(l,n); }

    /**
     * Default constructor that initialises this class with a logger
     * set not to log in verbose mode.
     * @param n the name of the class.
     */
    public ClassifierBuilder(String n){ super(n);}


    /**
     *
     *	CONSTRUCTOR
     *
     */

    /**
     * Builds the classifier specified by the algorithm variable.
     * @return true if the classifier has been successfully tested and trained.
     */
    public boolean build(int algorithm,String trainingSetPath,String modelPath)
    {
        switch (algorithm)
        {
            case Classifiers.J48:
                return trainAndTest(new J48Tester(log,"J48Tester"),trainingSetPath,modelPath);
            case Classifiers.MLP:
                return trainAndTest(new MLPTester(log,"MLPTester"),trainingSetPath,modelPath);
            case Classifiers.NB:
                return trainAndTest(new NaiveBayesTester(log,"NaiveBayesTester"),trainingSetPath,modelPath);
            case Classifiers.SVM:
                return trainAndTest(new SVMTester(log,"SVMTester"),trainingSetPath,modelPath);
            default:
                return false;
        }
    }

    /**
     * Trains and tests the supplied classifier.
     * @param classifier the classifier to train and test.
     * @return true if trained and tested successfully, else false.
     */
    private boolean trainAndTest(I_WekaTest classifier,String trainingSetPath,String modelPath)
    {
        //Common.fileDelete(this.outputFile);
        boolean trained=classifier.train(trainingSetPath);
        boolean saved=classifier.saveModel(modelPath);

        if(trained & saved)
            return true;
        else
        {
            if(!trained)
            {
                log.sout("could not train classifier", true);
                return false;
            }
            else
            {
                log.sout("could not save classifier model",true);
                return false;
            }
        }
    }
}
