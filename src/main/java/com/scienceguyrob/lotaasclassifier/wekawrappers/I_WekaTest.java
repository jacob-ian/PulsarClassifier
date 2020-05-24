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
 * File name:   I_WekaTest.java
 * Created:     23/10/2016
 * Author:      Rob Lyon
 * <p/>
 * Contact:	rob@scienceguyrob.com
 * Web:		www.scienceguyrob.com
 */

/**
 * The class I_WekaTest defines an interface for testing WEKA classifiers.
 *
 * @author Rob Lyon
 *
 * @version 1.0, 05/02/13
 */
public interface I_WekaTest
{
    /**
     * Trains the classifier.
     * @param path the training set path.
     */
    public boolean train(String path);

    /**
     * Make predictions on never before seen instances.
     */
    public boolean predict(String data,String outputPath);

    /**
     * Saves the classifier model to the path specified.
     * @param path the path to save the classifier model to.
     */
    public boolean saveModel(String path);

    /**
     * Loads the classifier model at the path specified.
     * @param path to the model to load.
     */
    public boolean loadModel(String path);

    /**
     * Loads the classifier model at the path specified, and then tests it.
     * @param path to the validation file to use.
     */
    public boolean validate(String path);

    /**
     * Resets the classifier implementing this interface.
     */
    public boolean reset();

    /**
     * Processes data items given a positive label.
     * @param data given a positive class label
     */
    public void processPositivePrediction(String outputPath,String data);

    /**
     * Processes data items given a positive label.
     * @param data given a negative class label
     */
    public void processNegativePrediction(String outputPath,String data);

}
