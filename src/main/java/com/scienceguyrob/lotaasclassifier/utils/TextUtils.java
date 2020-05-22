/**
 * This file is part of LotaasClassifier.
 * <p>
 * LotaasClassifier is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * LotaasClassifier is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with LotaasClassifier.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * File name:   StringUtils.java
 * Created:     25/01/2016
 * Author:      Rob Lyon
 * <p>
 * Contact:	rob@scienceguyrob.com
 * Web:		www.scienceguyrob.com
 */
package com.scienceguyrob.lotaasclassifier.utils;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class contains methods used to manipulate strings.
 *
 * @author Rob Lyon
 *
 * @version 1.0, 05/01/13
 */
@SuppressWarnings("unused")
public class TextUtils
{
    //*****************************************
    //*****************************************
    //               Methods
    //*****************************************
    //*****************************************
    /**
     * Checks if multiple keywords occur in a string.
     *
     * @param source the string to look at for keywords.
     * @param keywords the words to look for in the source string.
     * @return true if all the keywords are in the string, else false.
     */
    public static boolean areStringsInSource(String source, String[] keywords)
    {
        if(!Common.isNullOrEmpty(source))
        {
            if(!Common.isNullOrEmpty(keywords))
            {
                if(keywords.length==0)
                    return false;

                for(String s : keywords)
                {
                    if(isNullOrEmptyString(s))
                        continue;

                    String source_lower = source.toLowerCase();
                    String s_lower = s.toLowerCase();

                    if (!source_lower.contains(s_lower)){ return false; }
                }

                return true;
            }
            else{ return false; }
        }
        else{ return false; }
    }

    /**
     * Method that separates a string into sub-strings, broken up where each
     * specified character appears.
     * @param source the string to break up.
     * @param sepChar the character used to breakup the string.
     * @return the broken up string as an array, otherwise null.
     */
    public static String[] seperateString(String source, String sepChar)
    {
        if (!isNullOrEmptyString(source) && !source.equals("") && !sepChar.equals(""))
        {
            try
            {
                String[] subStrings = source.split(sepChar);

                if(subStrings.length < 1){ return null; }
                else if(subStrings.length == 1)
                {
                    // if the array has only one element, it may not have been
                    // possible to split the string, in which case the call to
                    // split will return an array containing the source string.
                    // As we don't want the source string to be returned in
                    // the array, we explicitly return null.
                    if(subStrings[0].equals(source))
                        return null;
                }

                return subStrings;
            }
            catch (Exception e){ return null; }
        }
        else{ return null; }
    }

    /**
     * Method that checks that a string actually contains a full integer.
     * @param source the string to check.
     * @return true if the string is representing a valid integer value, else false.
     */
    public static boolean doesStringContainInt(String source)
    {
        if(!isNullOrEmptyString(source))
        {
            String regex="([-+]?\\d+)";	// Integer Number 1

            Pattern p = Pattern.compile(regex,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            Matcher m = p.matcher(source);

            if (m.find())
            {
                try
                {
                    Integer.parseInt(source);
                    return true;
                }
                catch (NumberFormatException nfe){  return false; }
            }
            else {return false;}
        }
        else { return false; }
    }

    /**
     * Method that checks if a string is a string representation of a double or a float.
     * @param source the string to check.
     * @return true if the string is representing a float or double value.
     */
    public static boolean doesStringContainDoubleOrFloat(String source)
    {
        if(!isNullOrEmptyString(source))
        {
            String regex="([+-]?\\d*\\.\\d+)(?![-+0-9\\.])";	// Integer Number 1

            Pattern p = Pattern.compile(regex,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            Matcher m = p.matcher(source);

            if (m.find())
            {
                try
                {
                    Double.parseDouble(source);
                    return true;
                }
                catch (NumberFormatException nfe){  return false; }
            }
            else {return false;}
        }
        else { return false; }
    }

    /**
     * Gets the name of a file from a path.
     * @param path the full path to the file.
     * @return the name of the file with its extension.
     */
    public static String getFileNameFromPath(String path)
    {
        if(isNullOrEmptyString(path))
            return null;

        // If the path just consists of space
        if(path.replace(" ","").length()==0)
            return null;

        if(Common.isPathValid(path))
        {
            File file = new File(path);
            String fileName = file.getName();

            if(!isNullOrEmptyString(fileName))
            {
                // Get the path to the parent directory.
                return fileName;
            }else { return path; }
        }
        else { return path; }
    }

    /**
     * Checks if a string contains a file extension, but only if the extension
     * exists at the end of the file name, i.e. "file.txt" whereas "file.file.txt"
     * would be deemed invalid.
     * @param s the string to check.
     * @return true if the string contains a single file extension.
     */
    public static boolean containsFileExtension(String s)
    {
        if(!isNullOrEmptyString(s))
        {
            if(s.contains("."))
            {
                if(s.equals("."))
                    return false;

                // dot symbol at the start of string.
                if(s.indexOf(".") == 0)
                    return false;

                // count occurrences of dot character
                // if greater than one return false.
                //int count = 0;
                //for (int i=0; i < s.length(); i++)
                //{
                //    if (s.charAt(i) == '.')
                //        count++;
                //    if(count>1)
                //        return false;
                //}

                // dot symbol at the end of string.
                if(s.lastIndexOf(".") == s.length()-1)
                    return false;

                return true;
            }
            else
                return false;
        }
        else
            return false;
    }

    /**
     * Test if a string is null or empty.
     * @param s the string to check.
     * @return true if null or empty, else false.
     */
    public static boolean isNullOrEmptyString(String s)
    {
        if(s == null)
            return true;
        else if(s.equals(""))
            return true;
        else
            return false;
    }

    /**
     * Checks if a string is empty.
     * @param s the string to check.
     * @return true if the string is empty, else false.
     */
    public static boolean isEmptyString(String s)
    {
        if(s.equals(""))
            return true;
        else
            return false;
    }
}
