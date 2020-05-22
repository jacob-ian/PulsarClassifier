/**
 * This file is part of SKAStormSim.
 * <p>
 * SKAStormSim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * SKAStormSim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with SKAStormSim.  If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * File name:   CLParameter.java
 * Created:     25/01/2016
 * Author:      Rob Lyon
 * <p>
 * Contact:	rob@scienceguyrob.com
 * Web:		www.scienceguyrob.com
 */
package com.scienceguyrob.lotaasclassifier.cli;

import com.scienceguyrob.lotaasclassifier.utils.Common;
import com.scienceguyrob.lotaasclassifier.utils.TextUtils;

/**
 * The class CLParameter represents an individual command line
 * parameter passed to the application.
 *
 * @author Rob Lyon
 *
 * @version 1.0, 05/01/13
 */
@SuppressWarnings("unused")
public class CLParameter implements ICLParameter
{
	/**
	 *
	 *	VARIABLES
	 *
	 */

	/**
	 * The parameter.
	 */
	String value = null;

	/**
	 * The description of this parameter.
	 */
	String description ="";
	
	/**
	 * The data type of this parameter.
	 */
	int type = -1;

	// Possible Parameter data types.
	public static final int INT_PARAM_TYPE       = 1;
	public static final int FLOAT_PARAM_TYPE     = 2;
	public static final int DOUBLE_PARAM_TYPE    = 3;
	public static final int BOOL_PARAM_TYPE      = 4;
	public static final int STRING_PARAM_TYPE    = 5;
	public static final int NUMERICAL_PARAM_TYPE = 6;
	public static final int NOMINAL_PARAM_TYPE   = 7;
	public static final int FILEPATH_PARAM_TYPE  = 8;
	public static final int DIRPATH_PARAM_TYPE   = 9;

	//*****************************************
	//*****************************************
	//            Constructor
	//*****************************************
	//*****************************************

	/**
	 * Default constructor.
	 */
	public CLParameter(){}

	/**
	 * Primary constructor. To be used when the data
	 * type of the parameter is not known a priori.
	 * 
	 * @param v the value of the parameter.
	 */
	public CLParameter(String v)
	{
		this.value = v;
		this.findType();
	}

	/**
	 * Primary constructor.
	 * 
	 * @param v the value of the parameter.
	 * @param t the data type of the parameter.
	 */
	public CLParameter(String v,int t)
	{
		this.value = v;
		this.type = t;
	}
	
	/**
	 * Secondary constructor.
	 * 
	 * @param v the value of the parameter.
	 * @param d the parameter description.
	 * @param t the data type of the parameter.
	 */
	public CLParameter(String v,String d,int t)
	{
		this.value = v;
		this.description = d;
		this.type = t;
	}

	/**
	 *
	 *	GETTERS & SETTERS
	 *
	 */

	public String getValue(){ return this.value; }
	public String getDescription(){ return this.description; }
	public int getType(){ return this.type; }

	public void setValue(String v){ this.value = v; }
	public void setDescription(String d){ this.description = d; }
	public void setType(int t){ this.type = t; }

	/**
	 *
	 *	METHODS
	 *
	 */

	/**
	 * @return the parameter as an integer, else null if there is an error.
	 */
	public Integer toInt() 
	{
		try
		{
			return Integer.parseInt(this.value);
		}
		catch(NumberFormatException nfe){ return null;}
		catch(Exception e){return null;}
	}

	/**
	 * @return the parameter as a float, else null if there is an error.
	 */
	public Float toFloat() 
	{
		try
		{
			return Float.parseFloat(this.value);
		}
		catch(NumberFormatException nfe){ return null;}
		catch(Exception e){return null;}
	}

	/**
	 * @return the parameter as a double, else null if there is an error.
	 */
	public Double toDouble() 
	{
		try
		{
			return Double.parseDouble(this.value);
		}
		catch(NumberFormatException nfe){ return null;}
		catch(Exception e){return null;}
	}

	/**
	 * @return the parameter as a boolean, else null if there is an error.
	 */
	public Boolean toBool() 
	{
		try
		{
			return  Boolean.valueOf(this.value);
		}
		catch(Exception e){return null;}
	}

	/**
	 * @return the parameter as a string, else null if there is an error.
	 */
	public String toString() { return this.value; }

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
	public int findType()
	{	
		if(Common.fileExist(this.value))
			this.type = FILEPATH_PARAM_TYPE;
		else if(Common.dirExist(this.value))
			this.type = DIRPATH_PARAM_TYPE;
		else if(TextUtils.doesStringContainInt(this.value))
		{
			Object tempValue = this.toInt();

			if(tempValue != null)
				this.type = INT_PARAM_TYPE;
		}
		else if(this.value.contains("f") && TextUtils.doesStringContainDoubleOrFloat(this.value.replace("f","")))
		{
			String temp = this.value.replace("f","");

			Object tempValue = this.toFloat();

			if(tempValue != null)
				this.type = FLOAT_PARAM_TYPE;
		}
		else if(TextUtils.doesStringContainDoubleOrFloat(this.value))
		{
			Object tempValue = this.toDouble();

			if(tempValue != null)
				this.type = DOUBLE_PARAM_TYPE;
		}
		else if( this.value.equals("true") | this.value.equals("false") )
			this.type = BOOL_PARAM_TYPE;
		else 
			this.type = STRING_PARAM_TYPE;

		return this.type;
	}
	
	/**
	 * @param type the parameter type as an integer.
	 * @return the string representation of the parameter type.
	 */
	public String typeToString(int type)
	{
		switch (type)
		{
			case INT_PARAM_TYPE:
				return "Integer";
			case FLOAT_PARAM_TYPE:
				return "Float";
			case DOUBLE_PARAM_TYPE:
				return "Double";
			case BOOL_PARAM_TYPE:
				return "Boolean";
			case STRING_PARAM_TYPE:
				return "String";
			case NUMERICAL_PARAM_TYPE:
				return "Numerical";
			case NOMINAL_PARAM_TYPE:
				return "Nominal";
			case FILEPATH_PARAM_TYPE:
				return "File Path";
			case DIRPATH_PARAM_TYPE:
				return "Directory Path";
			default:
				return "Unknown";
		}
	}
}
