// CoordinateCollectionFactory.java created on 04 February 2004 at 14:40

//=============================================================================
//
//    Copyright (C) 2004 Kevin Ashelford
//    Contact details:
//    Email: ashelford@cardiff.ac.uk
//    Address:  Cardiff School of Bioscience, Cardiff University, PO. Box 915,
//    Cardiff, UK. CF10 3TL
//
//    This program is free software; you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation; either version 2 of the License, or
//    any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program; if not, write to the Free Software
//    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
//=============================================================================

package nestor.graphics.newplot;

import java.util.*;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 Factory for generating ICoordinateCollection objects.
 @author  Kevin Ashelford.
 */
public class CoordinateCollectionFactory {
	
	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS
	
	//##########################################################################
	
	// CONSTRUCTOR
	
	private CoordinateCollectionFactory() {
		
		} // End of constructor.
	
	//##########################################################################
	
	// PROPERTY ACCESSOR METHODS
	
	//##########################################################################
	
	// METHODS
	
	/**
	 Returns a new coordinate collection.
	 @param coordinates Array of coordinates.
	 @param title Title for display purposes.
	 @return Coordinate collection.
	 */
	public static ICoordinateCollection createCoordinateCollection(
		Coordinate[] coordinates,
		String title
		) {
		return new DefaultCoordinateCollection(coordinates, title, null);
		} // End of method.


	////////////////////////////////////////////////////////////////////////////

	/**
	 Returns new coordinate collection
	 @param coordinates Array of coordinates.
	 @param title Title for display purposes.
	 @return Coordinate collection.
	 */
	public static ICoordinateCollection createCoordinateCollection(
		ArrayList<Coordinate> coordinates,
		String title
		) {

		Coordinate[] c = new Coordinate[coordinates.size()];
		for (int i = 0; i < c.length; i++) {
			c[i] = coordinates.get(i);
			}

		return new DefaultCoordinateCollection(c, title, null);
		} // End of method.

	//##########################################################################
	
	} // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
