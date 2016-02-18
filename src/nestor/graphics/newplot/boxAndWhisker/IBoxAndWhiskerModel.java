//==============================================================================
//
//   IBoxAndWhiskerModel.java
//   Created: Jan 5, 2011 at 5:03:16 PM
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

import java.util.*;
import nestor.stats.Stats;
import nestor.graphics.newplot.*;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 * Interface for data models for box and whisker plots.
 * @author  Kevin Ashelford.
 */
public interface IBoxAndWhiskerModel extends IDataModel {

	//##########################################################################

	/**
	 Associates data with data model.
	 @param statsArray Array of stats objects representing the box and whiskers
	 to plot.
	 */
	public void setStatsCollection(ArrayList<Stats> statsArray);

	/**
	 Returns data associated with data model.
	 @return Array of stats objects representing the box and whiskers to plot.
	 */
	public ArrayList<Stats> getStatsCollection();

	/**
	 Returns number of box-and-whiskers currently associated with data model.
	 @return Count.
	 */
	public int getStatsCollectionCount();

	/**
	 Returns stats object for specific box and whisker as specified by index.
	 @param index Index of box-and-whisker stats object.
	 @return Stats object.
	 */
	public Stats getStats(int index);
	
	//##########################################################################
	
    } // End of interface.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////