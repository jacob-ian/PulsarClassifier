package com.scienceguyrob.lotaasclassifier.utils;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

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
 * File name:   Base.java
 * Created:     23/10/2016
 * Author:      Rob Lyon
 * <p/>
 * Contact:	rob@scienceguyrob.com
 * Web:		www.scienceguyrob.com
 */
public class Base
{
    /**
     *
     *	VARIABLES
     *
     */

    /**
     * The logger used to write out debug information.
     */
    public BasicLogger log;

    /**
     * The name of the class.
     */
    public String name = "";

    /**
     * A string used to prefix logging messages produced by this class.
     */
    public String logPrefix = " - ";

    /**
     * The unique identifier of this class.
     */
    public String ID = "";

    /**
     * Default constructor.
     * @param l the logger to be used by this class.
     * @param n the name of the class.
     */
    public Base(BasicLogger l,String n)
    {
        log = l;
        this.name=n;
    }

    /**
     * Default constructor that initialises this class with a logger
     * set not to log in verbose mode.
     * @param n the name of the class.
     */
    public Base(String n)
    {
        log = new BasicLogger();
    }

    //*****************************************
    //*****************************************
    //               Methods
    //*****************************************
    //*****************************************


    /**
     * Writes an object to an XML file.
     * @param obj the object to write to XML.
     * @param path the path to the file the object should be written to.
     * @throws Exception
     * @return true if the object is written to XML correctly, else false.
     */
    public static boolean write(Object obj, String path)
    {
        try
        {
            XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream( new FileOutputStream(path)));
            encoder.writeObject(obj);
            encoder.close();
            return true;
        }
        catch(Exception e){System.out.println(e.toString());return false;}
    }

    /**
     * Reads the state of an object in from XML.
     * @param path the path to the XML containing the state of the object.
     * @return the Object if successful, else null.
     */
    public static Object read(String path)
    {
        try
        {
            XMLDecoder decoder = new XMLDecoder(new BufferedInputStream( new FileInputStream(path)));
            Object o = (Object)decoder.readObject();
            decoder.close();
            return o;
        }
        catch(Exception e){System.out.println(e.toString());return null;}
    }
}
