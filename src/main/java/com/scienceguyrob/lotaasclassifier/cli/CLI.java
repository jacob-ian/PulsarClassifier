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
 * File name:   CLI.java
 * Created:     25/01/2016
 * Author:      Rob Lyon
 * Contact:	rob@scienceguyrob.com
 * Web:		www.scienceguyrob.com
 */
package com.scienceguyrob.lotaasclassifier.cli;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.scienceguyrob.lotaasclassifier.utils.TextUtils;

/**
 * This class is used to process and store the command line
 * input to the application. It is simply a wrapper that tries
 * to simplify the process of passing these in, without using 
 * external API's. It is a little hacky in places, but works
 * for our purposes.
 * 
 * @author Rob Lyon
 *
 * @version 1.0, 05/01/13
 */
@SuppressWarnings({"unused", "Duplicates"})
public class CLI implements ICLI
{
	//*****************************************
	//*****************************************
	//                Variables
	//*****************************************
	//*****************************************

	/**
	 * The command line parameters to expect.
	 */
	private Map<String,Object[]> expected_parameters = new HashMap<String, Object[]>();

	/**
	 * The command line parameters.
	 */
	private Map<String,ICLParameter> parameters = new HashMap<String, ICLParameter>();

	/**
	 * The current flag seen through command line processing.
	 */
	private String currentFlag = "";

	//*****************************************
	//*****************************************
	//             Constructor
	//*****************************************
	//*****************************************

	/**
	 * Default constructor.
	 */
	public CLI(){}

	//*****************************************
	//*****************************************
	//               Methods
	//*****************************************
	//*****************************************

	/**
	 * Adds a new command line parameter.
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
	 * @return true if the parameter was added, else false.
	 */
	public boolean addParameter(String flag,String desc, int type)
	{
		if(!expected_parameters.containsKey(flag))
		{
			//System.out.println("Adding expected parameter: flag= "+flag + " Type= "+type);
			expected_parameters.put(flag, new Object[]{type,desc});

			if(expected_parameters.containsKey(flag))
				return true;
			else return false;
		}
		else
		{
			//System.out.println("Parameter with flag: " + flag + " already added!");
			return true;
		}

	}

	/**
	 * Gets the parameter specified by the flag.
	 * @param flg the flag of the parameter to return.
	 * @return the specified parameter.
	 */
	public ICLParameter getParameter(String flg)
	{
		Iterator<Entry<String, ICLParameter>> iterator = parameters.entrySet().iterator();

		while (iterator.hasNext()) 
		{
			Entry<String, ICLParameter> pairs = (Entry<String, ICLParameter>)iterator.next();

			String flag =  pairs.getKey();
			ICLParameter value = pairs.getValue();

			if(flag.equals(flg))
				return value;

		}

		// If the flag hasn't been set by the user, then return false
		// if it is a boolean.
		if(isBooleanFlag(flg))
		{
			ICLParameter parameter = new CLParameter(flg,"",ICLParameter.BOOL_PARAM_TYPE);
			parameter.setValue("false");
			this.parameters.put(flg,parameter);

			return parameter;
		}

		System.out.println("Parameter with flag: "+ flg + " not provided.");
		return null;
	}

	/**
	 * Checks if the parameter specified by the flag has been input.
	 * @param flg the flag of the parameter to return.
	 * @return true if the parameter has been observed, else false.
	 */
	public boolean hasParameter(String flg)
	{
		Iterator<Entry<String, ICLParameter>> iterator = parameters.entrySet().iterator();

		while (iterator.hasNext())
		{
			Entry<String, ICLParameter> pairs = (Entry<String, ICLParameter>)iterator.next();

			String flag =  pairs.getKey();
			ICLParameter value = pairs.getValue();

			if(flag.equals(flg))
				return true;

		}

		return false;
	}

	/**
	 * Processes the command line arguments one by one.
	 * @param arg the command line arguments.
	 */
	public void processArguments(String[] arg) 
	{
		for(int i = 0; i < arg.length; i++)
		{
			// If string empty.
			if(TextUtils.isEmptyString(arg[i].trim()))
				continue;

			// else process the string.
			processArgument(arg[i]);
		}
	}

	/**
	 * Processes the individual arguments passed at the command line.
	 * @param arg the argument to process.
	 * @return true if the parameter passed in is as expected, else false.
	 */
	public boolean processArgument(String arg)
	{
		// If the provided argument is a expected flag.
		if(isValidFlag(arg))
		{
			// Get the flag.
			this.currentFlag = arg.trim();

			// If the flag is used to set a boolean variable to true...
			if(isBooleanFlag(arg))
			{
				this.currentFlag = "";
				// Add the user specified parameter.
				return processBooleanFlag(arg);
			}

			return true; // As its a valid flag.
		}
		else // The argument is not a flag - it must be the value associated with a flag.
		{
			// If the last observed flag is not empty...
			if(this.currentFlag.length()>0)
			{
				// Get the data type we expect to be input for this flag.
				int type = getParameterTypeForFlag(this.currentFlag);

				// If the parameter type is valid
				if (type > -1)
				{
					// Reset the currently observed flag to empty, as it
					// has now been processed.
					String flag = new String(currentFlag);
					this.currentFlag = "";

					// Try to add the parameter.
					return addUserParameter(type, arg, flag);
				}
				else
				{
					this.currentFlag = "";// Reset...
					return false;
				}
			}
			else // An argument has been supplied, but its not known which flag it corresponds to.
			{
				System.out.println("Parameter: " + arg + " supplied but for unknown flag");
				return false;
			}
		}
	}

	/**
	 * Attempts to add a non-boolean user supplied parameter read from the command line.
	 * @param type the expected type for the parameter.
	 * @param arg the parameter value.
	 * @param flag the flag associated with the parameter.
     */
	private boolean addUserParameter(int type,String arg,String flag)
	{
		if(type == ICLParameter.INT_PARAM_TYPE)
		{
			ICLParameter parameter = new CLParameter(arg.trim(),"",ICLParameter.INT_PARAM_TYPE);

			if(parameter.toInt() != null)
			{
				this.parameters.put(flag, parameter);

				// Check parameter added weakly...
				if(this.parameters.containsKey(flag))
					return true;
				else
					return false;
			}
			else
			{
				System.out.println("Invalid value supplied for flag " + flag + " int expected.");
				System.out.println(arg);
				System.out.println(flag);
				System.out.println(type);
				return false;
			}
		}
		else if(type == ICLParameter.FLOAT_PARAM_TYPE)
		{
			ICLParameter parameter = new CLParameter(arg.trim(),"",ICLParameter.FLOAT_PARAM_TYPE);

			if(parameter.toFloat() != null)
			{
				this.parameters.put(flag, parameter);

				// Check parameter added weakly...
				if(this.parameters.containsKey(flag))
					return true;
				else
					return false;
			}
			else
			{
				System.out.println("Invalid value supplied for flag " + flag + " float expected.");
				return false;
			}
		}
		else if(type == ICLParameter.DOUBLE_PARAM_TYPE)
		{
			ICLParameter parameter = new CLParameter(arg.trim(),"",ICLParameter.DOUBLE_PARAM_TYPE);

			if(parameter.toDouble() != null)
			{
				this.parameters.put(flag, parameter);

				// Check parameter added weakly...
				if(this.parameters.containsKey(flag))
					return true;
				else
					return false;
			}
			else
			{
				System.out.println("Invalid value supplied for flag " + flag + " double expected.");
				return false;
			}
		}
		else if(type == ICLParameter.STRING_PARAM_TYPE )
		{
			ICLParameter parameter = new CLParameter(arg.trim(),"",ICLParameter.STRING_PARAM_TYPE);

			this.parameters.put(flag, parameter);

			// Check parameter added weakly...
			if(this.parameters.containsKey(flag))
				return true;
			else
				return false;
		}
		else if(type == ICLParameter.FILEPATH_PARAM_TYPE)
		{
			ICLParameter parameter = new CLParameter(arg.trim(),"",ICLParameter.FILEPATH_PARAM_TYPE);

			this.parameters.put(flag, parameter);

			// Check parameter added weakly...
			if(this.parameters.containsKey(flag))
				return true;
			else
				return false;
		}
		else if(type == ICLParameter.DIRPATH_PARAM_TYPE )
		{
			ICLParameter parameter = new CLParameter(arg.trim(),"",ICLParameter.DIRPATH_PARAM_TYPE);

			this.parameters.put(flag, parameter);

			// Check parameter added weakly...
			if(this.parameters.containsKey(flag))
				return true;
			else
				return false;
		}
		else
		{
			System.out.println("Parameter " + flag + " " + arg + " invalid "+ typeToString(type) + "!");

			return false;
		}
	}
	/**
	 * Process a boolean flag supplied via the command line.
	 * @param arg the command line argument containing the flag.
     */
	private boolean processBooleanFlag(String arg)
	{
		String p = arg.trim();
		//System.out.println("Flag: "+ flag + " Type:" + type +" Parameter = "+ p);
		ICLParameter parameter = new CLParameter(p,"",ICLParameter.BOOL_PARAM_TYPE);
		parameter.setValue("true");
		this.parameters.put(p,parameter);

		if(this.parameters.containsKey(p))
			return true;
		else
			return false;
	}

	/**
	 * @return true if the user supplied flag is valid, else false.
     */
	private boolean isValidFlag(String arg)
	{
		Iterator<Entry<String, Object[]>> iterator = expected_parameters.entrySet().iterator();

		while (iterator.hasNext())
		{
			Map.Entry<String, Object[]> pairs = (Map.Entry<String, Object[]>) iterator.next();

			String flag = pairs.getKey();
			Object[] details = pairs.getValue();
			int type = (Integer) details[0];
			String description = (String) details[1];

			if (arg.startsWith(flag) || arg.startsWith(flag.toUpperCase()))
				return true;
		}

		return false;
	}

	/**
	 * @return true if the user supplied flag is a boolean flag, else false.
	 */
	private boolean isBooleanFlag(String arg)
	{
		Iterator<Entry<String, Object[]>> iterator = expected_parameters.entrySet().iterator();

		while (iterator.hasNext())
		{
			Map.Entry<String, Object[]> pairs = (Map.Entry<String, Object[]>) iterator.next();

			String flag = pairs.getKey();
			Object[] details = pairs.getValue();
			int type = (Integer) details[0];
			String description = (String) details[1];

			if (arg.startsWith(flag) || arg.startsWith(flag.toUpperCase()))
			{
				if(type == ICLParameter.BOOL_PARAM_TYPE)
					return true;
			}
		}

		return false;
	}

	/**
	 * Gets the expected parameter type for a flag. For example if the application is expecting
	 * a float to be associated with the flag -ratio, this would return the integer code for
	 * representing a floating point input parameter. The integer codes are as follows:
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
	 * @param arg the argument read in from the command line.
	 * @return the integer code representing the data type of the parameter.
     */
	private int getParameterTypeForFlag(String arg)
	{
		Iterator<Entry<String, Object[]>> iterator = expected_parameters.entrySet().iterator();

		while (iterator.hasNext())
		{
			Map.Entry<String, Object[]> pairs = (Map.Entry<String, Object[]>) iterator.next();

			String flag = pairs.getKey();
			Object[] details = pairs.getValue();
			int type = (Integer) details[0];
			String description = (String) details[1];

			if (arg.startsWith(flag) || arg.startsWith(flag.toUpperCase()))
				return type;
		}

		return -1;
	}

	/**
	 * Over-ridden toString method. Outputs command line options.
	 */   
	public String toString()
	{
		// First part outputs command line options.
		String representation = "\nCommand line options:\n";

		Iterator<Entry<String,Object[]>> iterator_1 = expected_parameters.entrySet().iterator();

		int count = 1;

		while (iterator_1.hasNext()) 
		{
			Entry<String,Object[]> pairs = (Entry<String,Object[]>)iterator_1.next();

			String flag =  pairs.getKey();
			Object[] details = pairs.getValue();
			int type = (Integer)details[0];
			String description = (String)details[1];
			
			representation += (count + ".\t" + flag + "\t"+ description+ "\tType:"+ typeToString(type)+"\n");
			count += 1;
		}
		
		// If parameters have been passed in, print them out.
		if(parameters.size()>0)
		{
			representation += "\nProvided command line parameters:\n";

			Iterator<Entry<String, ICLParameter>> iterator_2 = parameters.entrySet().iterator();

			count = 1;

			while (iterator_2.hasNext()) 
			{
				Entry<String, ICLParameter> pairs = (Entry<String, ICLParameter>)iterator_2.next();

				String flag =  pairs.getKey();
				ICLParameter value = pairs.getValue();


				representation += (count + ".\t" + flag + "\t" + value.toString()+"\n");
				count += 1;
			}
		}
		
		return representation;
	}

	/**
	 * @param type the parameter type as an integer.
	 * @return the string representation of the parameter type.
	 */
	public String typeToString(int type)
	{
		switch (type)
		{
			case ICLParameter.INT_PARAM_TYPE:
				return "Integer";
			case ICLParameter.FLOAT_PARAM_TYPE:
				return "Float";
			case ICLParameter.DOUBLE_PARAM_TYPE:
				return "Double";
			case ICLParameter.BOOL_PARAM_TYPE:
				return "Boolean";
			case ICLParameter.STRING_PARAM_TYPE:
				return "String";
			case ICLParameter.NUMERICAL_PARAM_TYPE:
				return "Numerical";
			case ICLParameter.NOMINAL_PARAM_TYPE:
				return "Nominal";
			case ICLParameter.FILEPATH_PARAM_TYPE:
				return "File Path";
			case ICLParameter.DIRPATH_PARAM_TYPE:
				return "Directory Path";
			default:
				return "Unknown";
		}
	}
}
