//==============================================================================
//
//   IBoxAndWhiskerRenderer.java
//   Created: Jan 5, 2011 at 5:44:55 PM
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

import nestor.graphics.newplot.*;
import nestor.stats.Stats;
import java.util.*;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 * 
 * @author  Kevin Ashelford.
 */
public interface IBoxAndWhiskerRenderer extends IDataRenderer {

	//##########################################################################
	
	/**
	 Sets formatter objects to be associated with renderer.
	 @param formatters Array of formatter objects.
	 */
	public void setBoxAndWhiskerFormatters(
		ArrayList<IBoxAndWhiskerFormatter> formatters
		);

	/**
	 Returns formatter objects associated with renderer.
	 @return Array of formatter objects.
	 */
	public ArrayList<IBoxAndWhiskerFormatter> getBoxAndWhiskerFormatters();

	/**
	 Returns specific formatter object corresponding to supplied box-and whisker,
	 as represented by stats object.
	 @param stats Stats object encapsulating box-and-whisker data.
	 @return Formatter.
	 */
	public IBoxAndWhiskerFormatter getBoxAndWhiskerFormatter(
		Stats stats
		);
	
	//##########################################################################
	
    } // End of interface.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////