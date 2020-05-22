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
 * File name:   Common.java
 * Created:     21/01/2016
 * Author:      Rob Lyon
 * <p>
 * Contact:	rob@scienceguyrob.com
 * Web:		www.scienceguyrob.com
 */
package com.scienceguyrob.lotaasclassifier.utils;

import java.io.*;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class contains a number of utility methods for the application.
 *
 * @author Rob Lyon
 *
 * @version 1.0, 05/01/13
 */
public class Common
{

	//*****************************************
	//*****************************************
	//            Comparisons
	//*****************************************
	//*****************************************

	/**
	 * Checks if a double value is within a specified interval.
	 * @param lowerBound the lower bound for the interval.
	 * @param upperBound the upper bound for the interval.
	 * @param value the value to check.
     * @return true if the value is in the interval, else false.
     */
	public static boolean inInterval(double lowerBound,double upperBound, double value)
	{
		int lowerResult = Double.compare(value,lowerBound);
		int upperResult = Double.compare(value,upperBound);

		if(lowerResult == 0 | upperResult == 0) // Value equal to either of the bounds.
			return true;

		if(lowerResult > 0 & upperResult < 0) // value greater than lower bound, lower than upper bound.
			return true;
		else
			return false; // value lower than lower bound, or greater than upper bound.

	}

	//*****************************************
	//*****************************************
	//            Check OS
	//*****************************************
	//*****************************************

	/**
	 * @return true if the executing application is running under windows, else false.
	 */
	public static boolean isWindows()
	{
		String os = System.getProperty("os.name").toLowerCase();
		//windows
		return (os.indexOf( "win" ) >= 0); 
	}

	/**
	 * @return true if the executing application is running under OSX (Mac), else false.
	 */
	public static boolean isMac()
	{
		String os = System.getProperty("os.name").toLowerCase();
		return (os.indexOf( "mac" ) >= 0); 
	}

	/**
	 * @return true if the executing application is running under Linux/Unix, else false.
	 */
	public static boolean isUnix()
	{
		String os = System.getProperty("os.name").toLowerCase();
		return (os.indexOf( "nix") >=0 || os.indexOf( "nux") >=0);
	}

	//*****************************************
	//*****************************************
	//           Time and Date
	//*****************************************
	//*****************************************

	/**
	 * Gets the current date in the form dd/mm/yyyy.
	 * @return the current date in the form dd/mm/yyyy.
	 */
	public static String getDate()
	{
		//Get Time firstly
		Calendar cal = Calendar.getInstance();

		String day = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
		String month = Integer.toString(cal.get(Calendar.MONTH)+1);
		String year = Integer.toString(cal.get(Calendar.YEAR));

		if(day.length() == 1)
			day = "0"+day;

		if(month.length() == 1)
			month = "0"+month;

		return day+"/"+month+"/"+year;
	}

	/**
	 * Gets the current date in the form dd[separator]mm[separator]yyyy.
	 * @param separator the string to use to separate the date components.
	 * @return the current date in the form dd[separator]mm[separator]yyyy.
	 */
	public static String getDateWithSeperator(String separator)
	{
		//Get Time firstly
		Calendar cal = Calendar.getInstance();

		String day = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
		String month = Integer.toString(cal.get(Calendar.MONTH)+1);
		String year = Integer.toString(cal.get(Calendar.YEAR));

		if(day.length() == 1)
			day = "0"+day;

		if(month.length() == 1)
			month = "0"+month;

		return day+separator+month+separator+year;
	}

	/**
	 * Gets the current time in the format hh:mm:ss.ms.
	 * @return the current time in the format hh:mm:ss.ms.
	 */
	public static String getTime()
	{
		//Get Time firstly
		Calendar cal = Calendar.getInstance();

		// Get the components of the time
		String hour24 = Integer.toString(cal.get(Calendar.HOUR_OF_DAY));     // 0..23
		String min = Integer.toString(cal.get(Calendar.MINUTE));             // 0..59
		String sec = Integer.toString(cal.get(Calendar.SECOND));             // 0..59
		String ms = Integer.toString(cal.get(Calendar.MILLISECOND));         // 0..999

		return hour24+":"+min+":"+sec+"."+ms;
	}

	//*****************************************
	//*****************************************
	//        Simple Object Methods
	//*****************************************
	//*****************************************

	/**
	 * Tests weather an object is null, and if the object is an array,
	 * checks that the array is non-empty.
	 * @param obj the empty to check.
	 * @return true if the object is null and/or empty, else false.
	 */
	public static boolean isNullOrEmpty(Object obj)
	{
		if(obj == null){ return true; }
		else if(isArray(obj))
		{
			Object[] array = (Object[]) obj;
			if(array.length  < 1)
				return true;
		}

		return false; 
	}

	/**
	 * Checks if the given object is an array (primitive or native).
	 *
	 * @param obj  Object to test.
	 * @return true of the object is an array, else false.
	 */
	public static boolean isArray(final Object obj) 
	{
		if (obj != null)
			return obj.getClass().isArray();
		else
			return false;
	}

	//*****************************************
	//*****************************************
	//            File Methods
	//*****************************************
	//*****************************************

	/**
	 * Gets the Canonical path of a file.
	 * @param f the file to get the Canonical path from.
	 * @return the Canonical path of the file, else null.
	 */
	public static String getFileCanonicalPath(File f)
	{
		try {  return f.getCanonicalPath(); }
		catch (IOException e){return null;}
	}

	/**
	 * Checks that the file at the specified path exists.
	 * @param path the file path to check.
	 * @return true if the file exists, else false.
	 */
	public static boolean fileExist(String path)
	{
		if(isPathValid(path))
		{
			File f = new File(path);

			try
			{
				if(!f.getCanonicalPath().equals(path))
					return false;

				if ( f.exists() && !dirExist(path)  ){return true;}
				else{return false;}
			}
			catch(Exception e){return false;}
		}
		else
			return false;
	}

	/**
	 * Creates a file at the specified path location, if the path is valid.
	 * @param path the location to create the file at.
	 * @return true if the file was created, else false.
	 */
	public static boolean fileCreate(String path)
	{
		if(isPathValid(path))
		{
			try 
			{
				File file = new File(path);

				if(!file.isDirectory())
				{
					// Create file if it does not exist
					boolean success = file.createNewFile();
					if (success) {return true;}
					else {return true;}//file already exists
				}else{ return false; }//It's a directory
			} 
			catch (IOException e) {return false;}
		}
		else
			return false;
	}

	/**
	 * Creates a file, and creates the directory structure for the file
	 * if it does not already exist.
	 * @param path the path to the file to create.
	 * @return true if the file was created successfully, else false.
	 */
	public static boolean fileCreateRecursive(String path)
	{
		if(isPathValid(path))
		{
			if(fileExist(path))
			{
				String pDir = getCanonicalPathToParent(path);

				boolean dirCreated = dirCreateRecursive(pDir);

				if(dirCreated){ return fileCreate(path); }
				else{ return false; }
			}
			else{ return true; }
		}
		else{ return false; }
	}

	/**
	 * Extracts the path to the parent directory of the specified file.
	 * @param path the path to the file whose parent directory we are to extract.
	 * @return the path to the parent directory of the specified file.
	 */
	public static String getCanonicalPathToParent(String path)
	{
		if(!Common.isNullOrEmpty(path) && Common.isPathValid(path))
		{
			if(Common.fileExist(path))
			{
				File file = new File(path);
				String fileName = file.getName();

				if(!Common.isNullOrEmpty(fileName))
				{
					// Get the path to the parent directory.
					return path.replace(File.separatorChar+fileName, "");
				}else { return null; }
			}
			else
			{
				int indexOfLastSeperator = path.lastIndexOf(File.separatorChar);
				return path.substring(0,indexOfLastSeperator);
			}
		}
		else { return null; }
	}

	/**
	 * Deletes a file at the specified path location.
	 * @param path the location to delete the file at.
	 * @return true if the file was deleted, else false.
	 */
	public static boolean fileDelete(String path)
	{
		if(isPathValid(path))
		{
			File file = new File(path);


			if(fileExist(path))
			{
				// Create file if it does not exist
				return file.delete();
			}
			else { return true; }
		}
		else
			return false;
	}

	/**
	 * Checks that the object at the specified path is a file.
	 * @param path the path to check.
	 * @return true if the object is a file.
	 */
	public static boolean isFile(String path)
	{
		if(!Common.isNullOrEmpty(path))
		{
			if(isPathValid(path))
			{
				File file = new File(path);
				if ( file .isFile() ){return true;}
				else{return false;}
			}
			else
				return false;
		}
		else { return false;}
	}

	/**
	 * Checks that the object at the specified path is a file.
	 * @param file the file to check.
	 * @return true if the object is a file.
	 */
	public static boolean isExisitingFile(File file)
	{
		if ( file .isFile() ){return true;}
		else{return false;}
	}

	/**
	 * Tests whether a path is valid for the system the application is executing upon.
	 * @param path the path to check.
	 * @return true if the file name is valid, else false.
	 */
	public static boolean isPathValid(String path) 
	{
		File f;

		if(path != null)
			f = new File(path);
		else
			return false;
		try 
		{
			@SuppressWarnings("unused")
			String canonicalPath = f.getCanonicalPath();

			// Check for invalid characters
			if(isWindows()){return isValidWinPath(path);}
			else if(isMac()){return isValidUnixPath(path);}
			else if(isUnix()){return isValidUnixPath(path);}
			else return true;

		}
		catch (IOException e) { return false; }
	}

	/**
	 * Checks if a path is valid for the Windows OS.
	 * @param path the path to test.
	 */
	public static boolean isValidWinPath(String path)
	{
		// If user inputs only a drive letter.
		if (path.length() == 3){ return false; }
		else if (path.endsWith("\\")){ return false; }
		else
		{
			String regex="([a-z]:\\\\(?:[-\\w\\.\\d]+\\\\)*(?:[-\\w\\.\\d]+)?)";

			Pattern p = Pattern.compile(regex,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
			Matcher m = p.matcher(path);

			// Invalid path characters to explicitly check for.
			String[] invalidChars = {"\"","/","*","?","<",">","|"};

			for(int i = 0; i < invalidChars.length;i++)
			{
				if(path.contains(invalidChars[i]))
					return false;
			}

			// Check the only colon appears at start of path, i.e. C:\
			if(path.indexOf(":")>1)
				return false;

			return (m.find());
		}
	}

	/**
	 * UNTESTED.
	 * Checks if a path is valid for the Unix/Linux OS.
	 * @param path the path to test.
	 */
	public static boolean isValidUnixPath(String path)
	{
		String regex="((?:\\/[\\w\\.\\-]+)+)";

		Pattern p = Pattern.compile(regex,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher m = p.matcher(path);
		return m.find();
	}

	//*****************************************
	//*****************************************
	//          Directory Methods
	//*****************************************
	//*****************************************

	/**
	 * Checks that the object at the specified path is a directory.
	 * @param path the path to check.
	 * @return true if the object is a directory.
	 */
	public static boolean isDirectory(String path)
	{
		if(isPathValid(path))
		{
			File dir = new File(path);
			if ( dir.isDirectory() ){return true;}
			else{return false;}
		}
		else
			return false;
	}

	/**
	 * Checks that the object at the specified path is a directory.
	 * @param dir the directory to check.
	 * @return true if the object is a directory.
	 */
	public static boolean isDirectory(File dir)
	{
		if ( dir.isDirectory() ){return true;}
		else{return false;}
	}

	/**
	 * Tests if a directory is empty.
	 * @param path the path to the directory to test.
	 * @return true if the directory is empty, else false.
	 */
	public static boolean isDirEmpty(String path)
	{
		if(isPathValid(path))
		{
			if(isDirectory(path) & dirExist(path))
			{
				File dir = new File(path);

				// Get list of file in the directory. If the length is zero
				// then the folder is empty.

				String[] files = dir.list();

				if (files.length == 0) 
					return true;
				else
					return false;
			}
			else { return false; }
		}
		else
			return false;
	} 

	/**
	 * Tests if a directory is empty.
	 * @param dir the directory to check.
	 * @return true if the directory is empty, else false.
	 */
	public static boolean isDirEmptyReadFile(File dir)
	{
		String[] files = dir.list();

		if (files.length == 0) 
			return true;
		else
			return false;
	} 

	/**
	 * Checks that the directory at the specified path exists.
	 * @param path the path to check.
	 * @return true if the directory exists
	 */
	public static boolean dirExist(String path)
	{
		if(isPathValid(path))
		{
			File dir = new File(path);

			try
			{
				if(!dir.getCanonicalPath().equals(path))
					return false;

				if ( dir.exists() && dir.isDirectory() ){return true;}
				else{return false;}
			}
			catch (IOException e){return false;}
		}
		else
			return false;
	}

	/**
	 * Creates a directory at the specified path location.
	 * @param path the location to create the directory at.
	 * @return true if the directory was created, else false.
	 */
	public static boolean dirCreate(String path)
	{
		if(!dirExist(path))
		{
			if(isPathValid(path))
			{
				File dir = new File(path);

				// Create the directory, all ancestor directories must exist.
				return  dir.mkdir();
			}
			else
				return false;
		}
		else{ return true; }
	}

	/**
	 * Creates a directory at the specified path location.
	 * @param path the location to create the directory at.
	 * @return true if the directory was created, else false.
	 */
	public static boolean dirCreateRecursive(String path)
	{
		if(!dirExist(path))
		{
			if(isPathValid(path))
			{
				File dir = new File(path);

				// Create the directory, all ancestor directories must exist.
				return  dir.mkdirs();
			}
			else
				return false;
		}
		else{ return true; }
	}

	/**
	 * Deletes a directory at the specified path location,
	 * and all the files contained within it.
	 * @param path the location of the directory to delete.
	 * @return true if the directory was deleted, else false.
	 */
	public static boolean dirDelete(String path)
	{
		if(isPathValid(path))
		{
			if(dirExist(path))
			{
				File dir = new File(path);

				// Deletes all files and sub directories under the directory.
				// Returns true if all deletions were successful.
				// If a deletion fails, the method stops attempting to delete and returns false.
				if (dir.isDirectory()) 
				{
					String[] children = dir.list();

					for (int i=0; i < children.length; i++) 
					{
						boolean success = deleteDir(new File(dir, children[i]));
						if (!success) { return false; }
					}
				}

				// The directory is now empty so delete it
				return dir.delete();
			}
			else{ return true; }
		}
		else
			return false;
	}

	/**
	 * Deletes all files and sub directories under a directory.
	 * Returns true if all deletions were successful. This method is
	 * only called within the dirDelete(String path) method, and
	 * should only be called from there.
	 * 
	 * If a deletion fails, the method stops attempting to delete and returns false.
	 * @param dir the directory to delete.
	 * @return true if the directory was deleted, else false.
	 */
	private static boolean deleteDir(File dir) 
	{
		if (dir.isDirectory()) 
		{
			String[] children = dir.list();

			for (int i=0; i < children.length; i++) 
			{
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {return false;}
			}
		}

		// The directory is now empty so delete it
		return dir.delete();
	}

	/**
	 * Returns the size of the specified file in bytes.
	 * @param path the path to the file.
	 * @return the size of the file in bytes.
	 */
	public static long getFileSize(String path)
	{
		try
		{
			File file = new File(path);
			long fileSize = file.length();
			return fileSize;
		}
		catch (Exception e)
		{
			System.out.println("Could not obtain file size:\n"+e.toString());
			return -1;
		}
	}

	/**
	 * Gets the current time in hours, minutes and seconds, condensed
	 * into a single string separated by a specified character.
	 * @param separator the separation character
	 * @return the time condense into a string with no spaces or separation characters.
	 */
	public static String getCondensedTime(String separator)
	{
		//Get Time firstly
		Calendar cal = Calendar.getInstance();

		String hour = Integer.toString(cal.get(Calendar.HOUR));
		String minute = Integer.toString(cal.get(Calendar.MINUTE));
		String second = Integer.toString(cal.get(Calendar.SECOND));
		return hour + separator + minute + separator +second;
	}
}
