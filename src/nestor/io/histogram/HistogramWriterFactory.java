//==============================================================================
//
//   HistogramWriterFactory.java
//   Created: Jan 11, 2011 at 11:21:40 AM
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

import nestor.io.IWriter;
import nestor.stats.IHistogram;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 * Factory for creating histogram file writers.
 * @author  Kevin Ashelford.
 */
public class HistogramWriterFactory {

	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS
	
	//##########################################################################
	
	// CONSTRUCTOR
	
    private HistogramWriterFactory() {
		
		} // End of constructor.

	//##########################################################################
		
	// METHODS

	/**
	 * Create writer object.
	 * @param histogram Histogram object.
	 * @return Writer object.
	 */
	public static IWriter createHistogramWriter(final IHistogram histogram) {
		return new _HistogramWriter(histogram);
		} // End of method.
	
	//##########################################################################
	
	// INNER CLASSES
	
	//##########################################################################
	
    } // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////





