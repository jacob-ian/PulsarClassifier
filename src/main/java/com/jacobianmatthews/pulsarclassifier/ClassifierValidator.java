package com.jacobianmatthews.pulsarclassifier;

import com.scienceguyrob.lotaasclassifier.utils.BasicLogger;
/**
 * This class validates the ensemble and individual classifiers on a 
 * provided data set.
 * 
 * @author Jacob Ian Matthews
 * @version 1.0, 22/05/2020
 */
public class ClassifierValidator extends com.scienceguyrob.lotaasclassifier.mvc.ClassifierValidator {

    public ClassifierValidator(BasicLogger l, String n) {
        super(l, n);
    }
    
    public ClassifierValidator(String n){ super(n);}
    
}