//==============================================================================
//
//   DefaultBoxAndWhiskerModel.java
//   Created: Jan 5, 2011 at 5:23:02 PM
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

import java.util.*;
import nestor.stats.Stats;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 * Default implementation of box-and-whisker data model.
 * @author  Kevin Ashelford.
 */
public class DefaultBoxAndWhiskerModel implements IBoxAndWhiskerModel {

	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS

	private ArrayList<Stats> _statsArray = null;

	//##########################################################################
	
	// CONSTRUCTOR
	
    /**
	 * Creates a new instance of <code>DefaultBoxAndWhiskerModel</code>.
	 */
    public DefaultBoxAndWhiskerModel() {
		
		} // End of constructor.

	//##########################################################################
		
	// METHODS

	public boolean hasData() {
		if (_statsArray == null) {return false;} else {return true;}
		} // End of method.

	////////////////////////////////////////////////////////////////////////////

	public void deleteData() {
		_statsArray = null;
		}

	////////////////////////////////////////////////////////////////////////////

	public void setStatsCollection(ArrayList<Stats> statsArray) {
		_statsArray = statsArray;
		} // End of method.

	////////////////////////////////////////////////////////////////////////////

	public ArrayList<Stats> getStatsCollection() {
		return _statsArray;
		} // End of method.

	////////////////////////////////////////////////////////////////////////////

	public int getStatsCollectionCount() {
		return _statsArray.size();
		} // End of method.

	////////////////////////////////////////////////////////////////////////////

	public Stats getStats(int index) {
		return _statsArray.get(index);
		} // End of method.

	//##########################################################################
	
	// INNER CLASSES
	
	//##########################################################################
	
    } // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////





