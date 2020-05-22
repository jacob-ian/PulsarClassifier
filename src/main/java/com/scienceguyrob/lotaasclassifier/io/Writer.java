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
 * File name:   Writer.java
 * Created:     21/01/2016
 * Author:      Rob Lyon
 * <p>
 * Contact:	rob@scienceguyrob.com
 * Web:		www.scienceguyrob.com
 */
package com.scienceguyrob.lotaasclassifier.io;

import com.scienceguyrob.lotaasclassifier.utils.Common;
import com.scienceguyrob.lotaasclassifier.utils.TextUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


/**
 * This class contains methods used for writing to files.
 *
 * @author Rob Lyon
 *
 * @version 1.0, 05/01/13
 */
public class Writer
{
	//*****************************************
	//*****************************************
	//              Write to file
	//*****************************************
	//*****************************************

	/**
	 * Writes a string literal to a specified file. The literal is
	 * appended to the end of the file in question.
	 * @param path the path to the file to which the string literal will be appended.
	 * @param text the string text to append to the file.
	 * @return true if the write operation was successful, else false.
	 */
	public static boolean write(String path, String text)
	{
		if(text == null)
			return false;
		if(path == null)
			return false;

		if(TextUtils.isEmptyString(text))
			return false;

		if(TextUtils.isEmptyString(path))
			return false;

		if(!Common.fileExist(path))//Check file exists
			Common.fileCreate(path);//if not create it

		if(Common.fileExist(path))//make sure the file we created exists
		{
			try 
			{
				BufferedWriter out = new BufferedWriter(new FileWriter(path));//Non-append
				out.write(text);
				out.close();
				return true;
			} 
			catch (IOException e) {return false;}
		}
		else{ return false; }
	}

	/**
	 * Writes a string literal to a specified file. The literal is
	 * appended to the end of the file in question.
	 * @param path the path to the file to which the string literal will be appended.
	 * @param text the string text to append to the file.
	 * @return true if the write operation was successful, else false.
	 */
	public static boolean append(String path, String text)
	{
		if(text == null)
			return false;
		if(path == null)
			return false;

		if(TextUtils.isEmptyString(text))
			return false;

		if(TextUtils.isEmptyString(path))
			return false;

		if(!Common.fileExist(path))//Check file exists
			Common.fileCreate(path);//if not create it

		if(Common.fileExist(path))//make sure the file we created exists
		{
			try 
			{
				BufferedWriter out = new BufferedWriter(new FileWriter(path, true));//append
				out.write(text);
				out.close();
				return true;
			} 
			catch (IOException e) {return false;}
		}
		else{ return false; }
	}
	
	//*****************************************
	//*****************************************
	//              Clear File
	//*****************************************
	//*****************************************

	/**
	 * Clears a file of data.
	 * @param path the path to the text file to clear.
	 * @return true if the file was cleared successfully.
	 */
	public static boolean clear(String path)
	{
		if(path == null)
			return false;

		if(TextUtils.isEmptyString(path))
			return false;

		if (Common.fileExist(path))
		{
			boolean deleted = Common.fileDelete(path);

			if(deleted)
				return Common.fileCreate(path);
			else 
				return false;
		}
		else{ return false; }
	}
}
