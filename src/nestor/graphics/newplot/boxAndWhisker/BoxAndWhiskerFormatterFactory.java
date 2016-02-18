//==============================================================================
//
//   BoxAndWhiskerFormatterFactory.java
//   Created: Jan 6, 2011 at 11:16:00 AM
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

package nestor.graphics.newplot.boxAndWhisker;

import nestor.stats.Stats;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 * Factory for creating box-and-whisker plot formatter objects.
 * @author  Kevin Ashelford.
 */
public class BoxAndWhiskerFormatterFactory {

	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS
	
	//##########################################################################
	
	// CONSTRUCTOR
	
    private BoxAndWhiskerFormatterFactory() {
		
		} // End of constructor.

	//##########################################################################
		
	// METHODS

	/**
	 Create new formatter object based on supplied stats object.
	 @param stats Stats object.
	 @return Formatter.
	 */
	public static IBoxAndWhiskerFormatter createFormatter(final Stats stats) {
		return new DefaultBoxAndWhiskerFormatter(stats);
		} // End of method.
	
	//##########################################################################
	
	// INNER CLASSES
	
	//##########################################################################
	
    } // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////





