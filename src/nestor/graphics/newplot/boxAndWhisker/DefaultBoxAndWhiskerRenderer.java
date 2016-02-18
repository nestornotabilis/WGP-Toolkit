//==============================================================================
//
//   DefaultBoxAndWhiskerRenderer.java
//   Created: Jan 5, 2011 at 6:02:43 PM
//   Copyright (C) 2008, University of Liverpool.
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
import java.util.*;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 * Default implementation of box-and-whisker renderer.
 * @author  Kevin Ashelford.
 */
public class DefaultBoxAndWhiskerRenderer implements IBoxAndWhiskerRenderer {

	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS

	private ArrayList<IBoxAndWhiskerFormatter> _boxAndWhiskerFormatters;
	
	//##########################################################################
	
	// CONSTRUCTOR
	
    /**
	 Creates a new instance of <code>DefaultBoxAndWhiskerRenderer</code>.
	 @param formatters Array of formatter objects.
	 */
    public DefaultBoxAndWhiskerRenderer(ArrayList<IBoxAndWhiskerFormatter> formatters) {
		_boxAndWhiskerFormatters = formatters;
		} // End of constructor.

	//##########################################################################
		
	// METHODS

	public void setBoxAndWhiskerFormatters(ArrayList<IBoxAndWhiskerFormatter> formatters) {
		_boxAndWhiskerFormatters = formatters;
		} // End of property.

	////////////////////////////////////////////////////////////////////////////

	public ArrayList<IBoxAndWhiskerFormatter> getBoxAndWhiskerFormatters() {
		return _boxAndWhiskerFormatters;
		} // End of property.

	////////////////////////////////////////////////////////////////////////////

	public IBoxAndWhiskerFormatter getBoxAndWhiskerFormatter(
		Stats stats
		) {

		if (_boxAndWhiskerFormatters == null) {
			throw new NullPointerException(
				"No formatter object has been registered with renderer."
				);
			}


		// Just in case...
		StringBuilder sb = new StringBuilder();

		for (IBoxAndWhiskerFormatter f : _boxAndWhiskerFormatters) {

			// Error if no Stats object has been registered with formatter.
			if (f.getStats() == null) {
				throw new NullPointerException(
				"No stats object has been registered with " +
				"formatter '" + f.toString() + "'."
				);
				}

			if (f.getStats().getId().equals(stats.getId())) {
				return f;
				}
			else {
				sb.append("[" + f.getStats().getId() + "]");
				}

			} // End of for loop - no formatters left.

		// If reach this point, supplied ICoordinateCollection object does
		// not coincide with any registered with PlotLineFormatters.
		throw new IllegalArgumentException(
			"The stats object '" +
			stats.getId() +
			"' has no corresponding formatter (" + sb.toString() + ")."
			);

		} // End of method.
	
	//##########################################################################
	
	// INNER CLASSES
	
	//##########################################################################
	
    } // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////





