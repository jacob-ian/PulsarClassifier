package com.jacobianmatthews.pulsarclassifier.utils;


/**
 * This class is a key-value pair datatype.
 * 
 * @author Jacob Ian Matthews
 * @version 1.0, 24/02/20
 * 
 */
public class Classification
{
    // Variables
    private String key;
    private int value;

    // Constructor
    public Classification(String key, int value)
    {
        // Get the values
        this.key = key;
        this.value = value;

    }

    public int getValue()
    {
        return this.value;
    }

    public String getKey()
    {
        return this.key;
    }

    public void setValue(int v)
    {
        this.value = v;
    }

    public void setKey(String k)
    {
        this.key = k;
    }
    
}
