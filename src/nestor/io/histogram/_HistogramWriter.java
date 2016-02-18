//==============================================================================
//
//   _HistogramWriter.java
//   Created: Jan 11, 2011 at 11:22:43 AM
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

import java.io.*;
import nestor.io.IWriter;
import nestor.stats.IHistogram;
import nestor.graphics.newplot.ICoordinateCollection;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 * 
 * @author  Kevin Ashelford.
 */
class _HistogramWriter implements IWriter {

	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS

	private final IHistogram _histogram;
	
	//##########################################################################
	
	// CONSTRUCTOR
	
    protected _HistogramWriter(final IHistogram histogram) {
		_histogram = histogram;
		} // End of constructor.

	//##########################################################################
		
	// METHODS

	public void write(PrintStream out) {

		// Do nothing if no data to save.
		if (!_histogram.hasCoordinateCollection()) return;


		ICoordinateCollection cc = _histogram.getCoordinateCollection();

		out.println("# " + cc.getTitle());
		out.println("# depth\t% of reference");
		
		for (int i = 0; i < cc.getSize(); i++) {
			double x = cc.getCoordinate(i).getX();
			double y = cc.getCoordinate(i).getY();
			out.println(x + "\t" + y);
			}

		} // End of method.

	//##########################################################################
	
	// INNER CLASSES
	
	//##########################################################################
	
    } // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////





