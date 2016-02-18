//==============================================================================
//
//   JoptsimpleUtil.java
//   Created: Nov 17, 2011 at 9:17:12 AM
//   Copyright (C) 2011, Cardiff University.
//   Author: Kevin Ashelford.
//
//   Contact details:
//   Email:   ashelfordKE@cardiff.ac.uk
//   Address: Institute of Medical Genetics, Cardiff University, 
//            Heath Park Campus, Cardiff, UK. CF14 4XN
//
//   This program is free software; you can redistribute it and/or modify
//   it under the terms of the GNU General Public License as published by
//   the Free Software Foundation; either version 2 of the License, or
//   any later version.
//
//    This program is distributed in the hope that it will be useful,
//   but WITHOUT ANY WARRANTY; without even the implied warranty of
//   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//   GNU General Public License for more details.
//
//   You should have received a copy of the GNU General Public License
//   along with this program; if not, write to the Free Software
//   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
//==============================================================================
package nestor.util;

import joptsimple.*;
import java.io.*;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
/**
 * 
 * @author  Kevin Ashelford.
 */
public class JoptsimpleUtil {

	//##########################################################################
	// CLASS FIELDS
	
	public static final String LINE_BREAK = System.getProperty("line.separator");
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS
	
	//##########################################################################
	// CONSTRUCTOR
	/**
	 * Creates a new instance of <code>JoptsimpleUtil</code>.
	 */
	public JoptsimpleUtil() {
		
		} // End of constructor.
	
	//##########################################################################
	
	// METHODS
	
	public static void displayHelp(
		final OptionParser optionParser
		) throws IOException {

		System.out.println(
			LINE_BREAK + "USAGE:" + LINE_BREAK +"======" + LINE_BREAK
			);
		optionParser.printHelpOn( System.out );
		System.out.println();

		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	public static void checkFileExists(final File file) throws IOException {

		// Instantiate infile objects now - and explicitly check before
		// proceeding further (better control over error message).

		if (!file.exists())
				throw new IOException(
					"File '" + file.getName() + "' doesn't appear to exist"
					);

			if (!file.canRead())
				throw new IOException(
					"Found, but can't read, file '" + file.getName() +
					"'. Please check file's permissions and try again"
					);

		} // End of method.

	////////////////////////////////////////////////////////////////////////////
	
	public static void checkFileDoesNotExist(final File file) throws IOException {

		// Instantiate infile objects now - and explicitly check before
		// proceeding further (better control over error message).

		if (file.exists())
				throw new IOException(
					"File '" + file.getName() + "' already exists"
					);

		} // End of method.

	//##########################################################################
	
	// INNER CLASSES
	
	//##########################################################################
} // End of class.
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

