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
 * File name:   BasicLogger.java
 * Created:     21/01/2016
 * Author:      Rob Lyon
 * <p>
 * Contact:	rob@scienceguyrob.com
 * Web:		www.scienceguyrob.com
 */
package com.scienceguyrob.lotaasclassifier.utils;

import com.scienceguyrob.lotaasclassifier.io.Writer;

/**
 * Simple logging utilities.
 *
 * @author Rob Lyon
 */
public class BasicLogger extends SerializableBaseObject
{
    /**
     *
     *	VARIABLES
     *

     */

    /**
     * Logging flag, if true, verbose logging outputs will be written to standard out.
     */
    public boolean verbose = false;

    /**
     * The log file name.
     */
    public String logfile = "Application.log";

    /**
     *
     *	CONSTRUCTOR
     *
     */

    /**
     * The main constructor to use.
     * @param loggingFlag the verbose logging flag, when true detailed log outputs will be produced.
     * @param filename the log file filename.
     */
    public BasicLogger(boolean loggingFlag, String filename)
    {
        this.verbose = loggingFlag;
        this.logfile = filename;
    }

    /**
     * Default constructor, verbose logging off by default.
     */
    public BasicLogger() { this.verbose = false; }

    /**
     *
     *	METHODS
     *
     */

    /**
     * Simple wrapper for standard out.
     * @param msg the message to output.
     * @param writeToFile the flag that when true tries to write the text to the log file.
     */
    public synchronized void sout(String msg, boolean writeToFile)
    {
        if(!this.verbose)
            return;

        String text = debugFormat(msg);

        // Output to the terminal
        System.out.println(text);

        // Output to the file?
        if(writeToFile)
            Writer.append(this.logfile,text + "\n");
    }

    /**
     * Simple wrapper for standard out.
     * @param msg the message to output.
     * @param var a variable to write out next to the message, separated by a colon.
     * @param writeToFile the flag that when true tries to write the text to the log file.
     */
    public synchronized void sout(String msg,String var, boolean writeToFile)
    {
        if(!this.verbose)
            return;

        System.out.println(debugFormat(msg + " : " +var));

        String text = debugFormat(msg + " : " +var);

        // Output to the terminal
        System.out.println(text);

        // Output to the file?
        if(writeToFile)
            Writer.append(this.logfile,text+ "\n");
    }

    /**
     * Simple wrapper for standard out.
     * @param msg the message to output.
     * @param var a variable to write out next to the message, separated by a colon.
     */
    public synchronized void eout(String msg, String var)
    {
        String text = errorFormat(msg + " : " +var);

        // Output to the terminal
        System.out.println(text);

        // Output to the file?
        Writer.append(this.logfile,text + "\n");
    }

    /**
     * Simple wrapper for standard out.
     * @param msg the message to output.
     */
    public synchronized void eout(String msg,Exception e)
    {
        String text = errorFormat(msg,e);

        // Output to the terminal
        System.out.println(text);

        // Output to the file?
        Writer.append(this.logfile,text + "\n");
    }

    /**
     * Writes out a line of text to standard out that separates output text.
     *
     * @param writeToFile the flag that when true tries to write the text to the log file.
     */
    public synchronized void breakLine(boolean writeToFile)
    {
        if(!this.verbose)
            return;

        String text = "\n**************************************************\n";
        System.out.println(text);

        // Output to the file?
        if(writeToFile)
            Writer.append(this.logfile,text);
    }

    /**
     * Formats a debug message into a suitable log format.
     * @param text the output type.
     * @return the formatted string.
     */
    private synchronized static String debugFormat(String text)
    {
        String DATE = Common.getDate();
        String TIME = Common.getTime();

        //Get Tick count
        long TICKS = System.currentTimeMillis();

        String MESSAGE =  "INFO,"+DATE + "," + TIME + "," + TICKS + "," + text;

        //Return String Formatted
        return MESSAGE;
    }

    /**
     * Formats an error message into a suitable error log format.
     * @param text the output type.
     * @param e the exception that.
     * @return the formatted string describing how the error occurred and of course the error itself.
     */
    private synchronized static String errorFormat(String text,Exception e)
    {
        String DATE = Common.getDate();
        String TIME = Common.getTime();

        //Get Tick count
        long TICKS = System.currentTimeMillis();

        //Form a string describing the exception
        String EXCEPTION = e.toString();

        String MESSAGE =  "ERROR," + DATE + "," + TIME + "," + TICKS + "," + text + ","+ EXCEPTION;

        //Return String Formatted
        return MESSAGE;
    }

    /**
     * Formats an error message into a suitable error log format.
     * @param text the output type.
     * @return the formatted string describing how the error occurred and of course the error itself.
     */
    private synchronized static String errorFormat(String text)
    {
        String DATE = Common.getDate();
        String TIME = Common.getTime();

        //Get Tick count
        long TICKS = System.currentTimeMillis();

        String MESSAGE =  "ERROR," + DATE + "," + TIME + "," + TICKS + "," + text ;

        //Return String Formatted
        return MESSAGE;
    }

    /**
     * Turns basic logging on.
     */
    public synchronized void LoggingOn() { this.verbose = true; }

    /**
     * Turns basic logging off.
     */
    public synchronized void LggingOff() { this.verbose = false; }

    /**
     *
     *	Getter and setters
     *
     */

    /**
     * @return the verbose logging flag.
     */
    public boolean getVerbose() {
        return this.verbose;
    }

    /**
     * Sets the verbose logging flag.
     * @param verbose the flag value, true or false.
     */
    public void setVerbose(boolean verbose) { this.verbose = verbose; }
}
