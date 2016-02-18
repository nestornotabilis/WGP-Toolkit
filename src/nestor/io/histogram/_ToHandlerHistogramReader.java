//==============================================================================
//
//   _ToHandlerHistogramReader.java
//   Created: Jan 11, 2011 at 5:38:37 PM
//   Copyright (C) 2011, University of Liverpool.
//   Author: Kevin Ashelford.
//
//   Contact details:
//   Email:   k.ashelford@liv.ac.uk
//   Address: School of Biological Sciences, University of Liverpool, 
//            Biosciences Building, Crown Street, Liverpool, UK. L69 7ZB
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

package nestor.io.histogram;

import nestor.io.*;
import java.io.*;
import java.util.regex.*;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 * 
 * @author  Kevin Ashelford.
 */
class _ToHandlerHistogramReader implements IReader {

	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS
	
	//##########################################################################
	
	// CONSTRUCTOR
	
    protected _ToHandlerHistogramReader() {
		
		} // End of constructor.

	//##########################################################################
		
	// METHODS

	public void read(
		final BufferedReader in,
		IReaderHandler handler
		) throws IOException {

		Matcher regex = Pattern.compile("^(\\S+)\\t+(\\S+)$").matcher("");
		Matcher labelRegex = Pattern.compile("^#\\s(\\S+)$").matcher("");

		String line = null;

		while ((line = in.readLine()) != null) {

			if (labelRegex.reset(line).matches()) {
				((IHistogramReaderHandler)handler)._nextLabel(labelRegex.group(1));
				}

			else if(regex.reset(line).matches()) {
				Double bin = Double.parseDouble(regex.group(1));
				Double value = Double.parseDouble(regex.group(2));

				((IHistogramReaderHandler)handler)._nextBin(bin, value);
				}

			else {
				throw new IOException(
					"Unexpected line encountered: '" +
					line +
					"'"
					);
				}

			}

		} // End of method.
	
	//##########################################################################
	
	// INNER CLASSES
	
	//##########################################################################
	
    } // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////





