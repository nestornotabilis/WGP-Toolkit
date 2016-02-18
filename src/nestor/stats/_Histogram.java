//==============================================================================
//
//   _Histogram.java
//   Created: Dec 20, 2010 at 11:34:00 AM
//   Copyright (C) 2010, University of Liverpool.
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

package nestor.stats;

import nestor.graphics.newplot.*;

////////////////////////////////////////////////////////////////////////////////

/**
 * 
 * @author  Kevin Ashelford.
 */
class _Histogram implements IHistogram {

	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS

	private final ICoordinateCollection _cc;
	
	//##########################################################################
	
	// CONSTRUCTOR
	
    /**
	 * Creates a new instance of <code>_Histogram</code>.
	 */
    protected _Histogram(final ICoordinateCollection cc) {
		_cc = cc;
		} // End of constructor.

	//##########################################################################
		
	// METHODS

	public ICoordinateCollection getCoordinateCollection() {
		return _cc;
		}

	////////////////////////////////////////////////////////////////////////////

	public int getNumberOfBins() {
		return _cc.getSize();
		} 

	////////////////////////////////////////////////////////////////////////////

	public boolean hasCoordinateCollection() {
		if (_cc == null) {return false;} else {return true;}
		} // end of method.

	//##########################################################################
	
	// INNER CLASSES
	
	//##########################################################################
	
    } // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////





