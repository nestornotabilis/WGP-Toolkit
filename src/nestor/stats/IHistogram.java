//==============================================================================
//
//   IHistogram.java
//   Created: Dec 20, 2010 at 11:33:50 AM
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
////////////////////////////////////////////////////////////////////////////////

/**
 * Interface for histogram objects.
 * @author  Kevin Ashelford.
 */
public interface IHistogram {

	/**
	 * Returns binned data as a collection of coordinate objects, as used by
	 * plotting classes.
	 * @return Coordinate collection.
	 */
	public ICoordinateCollection getCoordinateCollection();

	/**
	 * Returns number of bins (and hence bars) in histogram.
	 * @return Number of bins.
	 */
	public int getNumberOfBins();

	/**
	 * Returns true if histogram object has coordinates, and hence contains data.
	 * @return True if coordinates exist.
	 */
	public boolean hasCoordinateCollection();
	

    } // End of interface.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////





