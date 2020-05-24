package com.scienceguyrob.lotaasclassifier.wekawrappers;

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
 * File name:   WekaClassifier.java
 * Created:     23/10/2016
 * Author:      Rob Lyon
 * <p/>
 * Contact:	rob@scienceguyrob.com
 * Web:		www.scienceguyrob.com
 */

import com.scienceguyrob.lotaasclassifier.io.Writer;
import com.scienceguyrob.lotaasclassifier.stats.ClassifierStats;
import com.scienceguyrob.lotaasclassifier.stats.I_ClassifierStats;
import com.scienceguyrob.lotaasclassifier.utils.Base;
import com.scienceguyrob.lotaasclassifier.utils.BasicLogger;
import com.scienceguyrob.lotaasclassifier.utils.Common;
import weka.core.Instance;

/**
 * The class defines the basic type of Weka classifier.
 *
 * @author Rob Lyon
 *
 * @version 1.0, 05/02/13
 */
public class WekaClassifier extends Base
{
    //*****************************************
    //*****************************************
    //              Variables
    //*****************************************
    //*****************************************

    /**
     * The path to the ARFF file containing a training set.
     */
    protected String trainingSetFilePath = "";

    /**
     * The path to the ARFF file containing a test set.
     */
    protected String testSetFilePath = "";

    /**
     * The path to the ARFF file containing data to be classified.
     */
    protected String classificationFilePath = "";

    /**
     * Stores the statisitics describing the performance of the classifier.
     */
    protected I_ClassifierStats stats = new ClassifierStats();

    /**
     * The index of the class labels in the ARFF training and test files.
     */
    protected int classIndex = -1;

    /**
     * The class labels.
     */
    protected String[] classLabels = null;

    //*****************************************
    //*****************************************
    //             Constructor
    //*****************************************
    //*****************************************

    /**
     * Default constructor.
     * @param l the logger to be used by this class.
     * @param n the name of the class.
     */
    public WekaClassifier(BasicLogger l, String n){ super(l,n);}

    /**
     * Default constructor that initialises this class with a logger
     * set not to log in verbose mode.
     * @param n the name of the class.
     */
    public WekaClassifier(String n){ super(n);}

    //*****************************************
    //*****************************************
    //           Getter & Setters
    //*****************************************
    //*****************************************

    /**
     * @return the training set file path.
     */
    public String getTrainingSetFilePath() { return trainingSetFilePath; }

    /**
     * @param trainingSetFilePath the training set file path to set.
     */
    public void setTrainingSetFilePath(String trainingSetFilePath) { this.trainingSetFilePath = trainingSetFilePath; }

    /**
     * @return the test set file path.
     */
    public String getTestSetFilePath() { return testSetFilePath; }

    /**
     * @param testSetFilePath the test set file path to set.
     */
    public void setTestSetFilePath(String testSetFilePath) { this.testSetFilePath = testSetFilePath; }

    /**
     * @return the classification set file path.
     */
    public String getClassificationFilePath() { return classificationFilePath; }

    /**
     * @param classificationFilePath the classification set file path to set.
     */
    public void setClassificationFilePath(String classificationFilePath) { this.classificationFilePath = classificationFilePath; }

    /**
     * @return the statisitics describing the performance of the classifier.
     */
    public I_ClassifierStats getClassifierStatistics() { return this.stats; }

    /**
     * @return the index of the class labels in the ARFF training and test files.
     */
    public int getClassIndex(){ return this.classIndex; }

    /**
     * Sets the index of the class labels in the ARFF training and test files.
     *
     * @param index the index of the class labels in the ARFF files.
     */
    public void setClassIndex(int index){ this.classIndex = index; }

    /**
     * Sets the class labels to be expected by the classifier.
     * @param l the labels to expect.
     */
    public void setClasslabels(String[] l){ this.classLabels = l;}

    /**
     * @return Gets the class labels to be expected by the classifier.
     */
    public String[] getClasslabels(){ return this.classLabels; }

    //*****************************************
    //*****************************************
    //              Methods
    //*****************************************
    //*****************************************

    /**
     * Obtains the scores belonging to a instance within the ARFF
     * file being classified. Returns these scores in a comma delimited
     * string.
     * @param instance the instance whose score values should be obtained.
     * @return a comma delimited string containing the scores.
     */
    public String getScores(Instance instance, int attributes)
    {
        String data="";
        for(int i=0;i<attributes;i++)
            data+=instance.value(i)+",";

        return data;
    }

    /**
     * Records misclassified instances in an ARFF file.
     * @param path the path to the file to store missclassified instances.
     * @param missclassifications a string containing the misclassified instances (assumed separated by newlines).
     * @param attributes the number of attributes possessed by the data.
     */
    public void missclassificationsToARFF(String path,String missclassifications,int attributes)
    {
        String header="@relation Missclassifications_" + Common.getCondensedTime("_") + "_" + Common.getDateWithSeperator("_")+"\n\n";

        for(int i=1; i<attributes;i++)
            header+="@attribute Score"+i+" numeric\n";

        header+="@attribute class {0,1}\n";
        header+="@attribute reason {FP,FN}\n";
        header+="@data\n";

        Writer.append(path, header);
        Writer.append(path, missclassifications);
    }

    /**
     * Records predictions in an ARFF file.
     * @param path the path to the file to store predictions.
     * @param predictions a string containing the predictions (assumed separated by newlines).
     * @param attributes the number of attributes possessed by the data.
     */
    public void predictionsToARFF(String path,String predictions,int attributes)
    {
        String header="@relation Predictions_"+Common.getCondensedTime("_")+"_"+Common.getDateWithSeperator("_")+"\n\n";

        for(int i=1; i<attributes;i++)
            header+="@attribute Score"+i+" numeric\n";

        header+="@attribute class {0,1}\n";
        header+="@data\n";

        Writer.append(path, header);
        Writer.append(path, predictions);
    }
}
