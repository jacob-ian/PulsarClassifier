package com.jacobianmatthews.pulsarclassifier.utils;

import java.util.ArrayList;
import java.util.List;

import com.scienceguyrob.lotaasclassifier.io.Reader;

/**
 * This class creates a list with a key-value pairing system to use inside a
 * list. It is used as a part of ClassPredictor.java to keep track of the number
 * of occurrences of positive and negative pulsar classifications.
 * 
 * @author Jacob Ian Matthews
 * @version 1.0, 24/05/20
 * 
 */
public class ClassificationList 
{
    
    /**
     * Variables
     */
    public List<Classification> list;

    /**
     * Constructor
     */
    public ClassificationList()
    {
        // Create the list
        this.list = new ArrayList<Classification>();
    }

    /**
     * 
     * @param index index in the list of the item to retrieve
     * @return the Key-Value pair at the list index
     */
    public Classification get(int index)
    {
        // Return the list item 
        return list.get(index);
    }

    public List<Classification> getList()
    {
        return this.list;
    }

    /**
     * 
     * @return the number of items in the list
     */
    public int size()
    {
        // Get the size of the list
        return list.size();
    }

   
    public void add(String key, int value)
    {
        // Create a new entry and add it to the list
        list.add(new Classification(key, value));

    }

    /**
     * Get the value of a key-value pair inside the list, found by its key.
     * @param key String with the key of the pair
     * @return Integer value of the pair or 0 if it doesn't exist.
     */
    public int getValueByKey(String key)
    {

        // Loop through the list until the pair is found
        for(Classification item: this.list)
        {
            // Check the string against the provided string
            if( item.getKey().equals(key) )
            {
                // Found the classification item, get its value
                return item.getValue();
            }
        }

        // Return 0 since it wasn't found
        return 0;

    }

    /**
     * Sets the value of a key-value pair inside the list, by its key.
     * @param key String key of the pair.
     */
    public void setValueByKey(String key, int value)
    {
        // Loop through the list until the key-value pair is found
        for(Classification item: this.list)
        {
            // Compare the string keys
            if ( item.getKey().equals(key) )
            {
                // Found the pair, updated the value
                item.setValue(value);
            }

        }
    }

    /**
     * Builds an ensemble list of positive or negative classification instances from all classifiers.
     * @param files a List of filepaths to the positive or negative classifier outputs
     * @return true if build is successful
     */
    public boolean buildList(List<String> files)
    {
        // Loop through the classifiers' output files to count instances of candidate classifications
        for(String file: files) 
        {
            // Get the number of lines in the file
            int lineCount = Reader.getLineCount(file);

            // Get the contents of each line and handle them individually
            for(int i=1; i<=lineCount; i++)
            {
                // Read the line of the file
                String line = Reader.readLine(file, i);

                // Check if ensemble candidates list is empty
                if ( this.size() == 0 ){

                    // List is empty, no point in checking for previous ocurrences of this line
                    this.add(line, 1);

                } else {

                    // List isn't empty, check if this classification is already in the list
                    int occurrences = this.getValueByKey(line);

                    // If it isn't 0, then it has already showed up and therefore we can add to its total count
                    if (occurrences > 0) {

                        // Add to the value
                        this.setValueByKey(line, occurrences+1);

                    } else {

                        // Occurrences are 0, therefore we can add this key-value pair to the list
                        this.add(line, 1);

                    }
                }
            }
            
        }
        return true;

    }

    /**
     * 
     * @return a string containing lines of all key value classification pairs
     */
    public String printList()
        {
            // Create an empty string to print to the command line
            String output = "";

            // Loop through the list items and print their key and value
            for(Classification item: this.list)
            {  
                // Get the key and value of the classification
                String key = item.getKey();
                int value = item.getValue();

                // Add to the output string
                output+=key+", "+value+"\n";
            }

            // Return the output string
            return output;

        }

}