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
 * File name:   ICLParameter.java
 * Created:     06/04/2016
 * Author:      Rob Lyon
 * <p>
 * Contact:	rob@scienceguyrob.com
 * Web:		www.scienceguyrob.com
 */
package com.scienceguyrob.lotaasclassifier.cli;

/**
 * An interface defining the expected functionality of command line parameter objects.
 */
public interface ICLParameter
{
	/**
	 *
	 *	Variables
	 *
	 */

	// Possible Parameter data types.
	int INT_PARAM_TYPE       = 1;
	int FLOAT_PARAM_TYPE     = 2;
	int DOUBLE_PARAM_TYPE    = 3;
	int BOOL_PARAM_TYPE      = 4;
	int STRING_PARAM_TYPE    = 5;
	int NUMERICAL_PARAM_TYPE = 6;
	int NOMINAL_PARAM_TYPE   = 7;
	int FILEPATH_PARAM_TYPE  = 8;
	int DIRPATH_PARAM_TYPE   = 9;

	/**
	 *
	 *	Getters & Setters
	 *
	 */

	String getValue();
	String getDescription();
	int getType();

	void setValue(String v);
	void setDescription(String d);
	void setType(int t);

	/**
	 *
	 *	METHODS
	 *
	 */

	/**
	 * @return the parameter as an integer, else null if there is an error.
	 */
	Integer toInt();

	/**
	 * @return the parameter as a float, else null if there is an error.
	 */
	Float toFloat();

	/**
	 * @return the parameter as a double, else null if there is an error.
	 */
	Double toDouble();

	/**
	 * @return the parameter as a boolean, else null if there is an error.
	 */
	Boolean toBool();

	/**
	 * @return the parameter as a string, else null if there is an error.
	 */
	String toString();

	/**
	 * Automatically finds the type of the parameter, i.e. integer, string boolean etc,
	 * and returns an integer that represents this type. The possible return values are:
	 *
	 * Integer   - 1
	 * Float     - 2
	 * Double    - 3
	 * Boolean   - 4
	 * String    - 5
	 * Numerical - 6 (i.e. unknown numerical)
	 * Nominal   - 7 (Other unknown)
	 *
	 * @return the type of the parameter as an integer.
	 */
	int findType();

	/**
	 * @param type the parameter type as an integer.
	 * @return the string representation of the parameter type.
	 */
	String typeToString(int type);

}
