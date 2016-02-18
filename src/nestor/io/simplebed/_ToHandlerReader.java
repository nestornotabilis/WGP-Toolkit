//==============================================================================
//
//   _ToHandlerReader.java
//   Created: Jan 20, 2011 at 4:11:08 PM
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

package nestor.io.simplebed;

import nestor.io.IReader;
import nestor.io.IReaderHandler;
import java.io.*;
import java.util.regex.*;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 * 
 * @author  Kevin Ashelford.
 */
class _ToHandlerReader implements IReader {

	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS

	private final Matcher _ignoreRegex =
		Pattern.compile("(^\\s*$)|(^#)|(^track)").matcher("");

	private final Matcher _lineRegex =
		Pattern.compile("^(\\S+)\\s+(\\d+)\\s+(\\d+)\\s+(\\S+)").matcher("");
	
	//##########################################################################
	
	// CONSTRUCTOR
	
    /**
	 * Creates a new instance of <code>_ToHandlerReader</code>.
	 */
    public _ToHandlerReader() {
		
		} // End of constructor.

	//##########################################################################
		
	// METHODS

	public void read(
		final BufferedReader in,
		final IReaderHandler handler
		) throws IOException {

		String line = null;
		while ((line = in.readLine()) != null) {
			
			if (_lineRegex.reset(line).find()) {
				String chromosomeId	= _lineRegex.group(1);
				String featureId	= _lineRegex.group(4);
				String start		= _lineRegex.group(2);
				String end			= _lineRegex.group(3);

				((IBedReaderHandler)handler)._nextLine(
					chromosomeId,
					featureId,
					// note that BED files are zero indexed, with feature lengths
					// exclusive of end value.  Code assumes features conform
					//to 1-indexing with end position inclusive.
					Integer.parseInt(start) + 1,
					Integer.parseInt(end)
					);

				}
			else if (_ignoreRegex.reset(line).find()) {
				continue;
				}

			else {
				throw new IOException(
					"Problem encountered whilst reading feature file. " +
					"Could not interpret line '" + line + "'"
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





