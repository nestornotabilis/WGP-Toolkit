//==============================================================================
//
//   SimpleBedReaderHandler.java
//   Created: Jan 20, 2011 at 4:06:47 PM
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

import java.util.*;
import nestor.tools.BedData;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 * Default implementation of handler interface for bed file format.
 * @author  Kevin Ashelford.
 */
public class SimpleBedReaderHandler implements IBedReaderHandler {

	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS
	
	// PROCESS BED FILE.
	final BedData _bedData = new BedData();
		
	
	//##########################################################################
	
	// CONSTRUCTOR
	
    /**
	 * Creates a new instance of <code>SimpleBedReaderHandler</code>.
	 */
    public SimpleBedReaderHandler() {
		
		} // End of constructor.

	//##########################################################################
		
	// METHODS

	public void _nextLine(
		final String refId,
		final String featureId,
		final int start,
		final int end
		) {

		for (int i = start; i <= end; i++) {
			_bedData.create(refId, i, 0);
			}

		} // End of method.

	////////////////////////////////////////////////////////////////////////////

	public BedData getBedData() {return _bedData;} // end of method. 
	
	//##########################################################################
	
	// INNER CLASSES
	
	//##########################################################################
	
    } // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////





