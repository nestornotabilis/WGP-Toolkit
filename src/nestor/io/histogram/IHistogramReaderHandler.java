//==============================================================================
//
//   IHistogramReaderHandler.java
//   Created: Jan 11, 2011 at 5:35:34 PM
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

import nestor.io.IReaderHandler;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 * Handler interface for histogram file readers.
 * @author  Kevin Ashelford.
 */
public interface IHistogramReaderHandler extends IReaderHandler{

	//##########################################################################

	/**
	 * Only to be called within reader object.
	 * @param label
	 */
	public void _nextLabel(String label);

	/**
	 * Only to be called within reader object.
	 * @param bin
	 * @param value
	 */
	public void _nextBin(Double bin, Double value);

	//##########################################################################
	
    } // End of interface.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////