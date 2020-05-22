package com.scienceguyrob.lotaasclassifier.cli;

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
 * File name:   ICLI.java
 * Created:     06/04/2016
 * Author:      Rob Lyon
 * <p>
 * Contact:	rob@scienceguyrob.com
 * Web:		www.scienceguyrob.com
 */
public interface ICLI
{
	/**
	 * Adds a new command line parameter to be searched for by this class.
	 * @param flag the flag to use for the parameter.
	 * @param desc the parameter description.
	 * @param type the data type, either:
	 *
	 * Integer 		= 1
	 * Float		= 2
	 * Double		= 3
	 * Boolean 		= 4
	 * String		= 5
	 * Numerical 	= 6
	 * Nominal		= 7
	 * File path	= 8
	 * Dir path		= 9.
	 *
	 * @return true if the parameter was added correctly, else false.
	 */
	boolean addParameter(String flag, String desc, int type);

	/**
	 * Gets the parameter specified by the flag.
	 * @param flg the flag of the parameter to return.
	 * @return the specified parameter, or null if there is an error or no such parameter.
	 */
	ICLParameter getParameter(String flg);

	/**
	 * Checks if the parameter specified by the flag has been input.
	 * @param flg the flag of the parameter to return.
	 * @return true if the parameter has been observed, else false.
	 */
	boolean hasParameter(String flg);

	/**
	 * Processes the command line arguments one by one.
	 * @param arg the command line arguments.
	 */
	void processArguments(String[] arg);

	/**
	 * Processes the individual arguments passed at the command line.
	 * @param arg the argument to process.
	 * @return true if the parameter passed in is as expected, else false.
	 */
	boolean processArgument(String arg);

	/**
	 * @param type the parameter type as an integer.
	 * @return the string representation of the parameter type.
	 */
	String typeToString(int type);
}
