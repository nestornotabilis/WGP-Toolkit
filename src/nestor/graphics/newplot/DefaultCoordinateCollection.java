// DefaultCoordinateCollection.java created on 03 February 2004 at 11:04

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

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 Default implementation of the CooordinateCollection interface.  Object of this 
 class represents a collection of (x, y) coordinates to display in a Graph 
 object.
 @author  Kevin Ashelford.
 */
class DefaultCoordinateCollection implements ICoordinateCollection {
	
	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS
	
	private final Coordinate[] _coordinates;
	private final String _title;
	private final CoordinateCollectionType _type;
	
	//##########################################################################
	
	// CONSTRUCTOR
	
	/**
	 Creates a new instance of DefaultCoordinateCollection.
	 @param coordinates Array of coordinates.
	 @param title Title for display purposes.
	 @param type Type of ICoordinateCollection.
	 */
	public DefaultCoordinateCollection(
		Coordinate[] coordinates,
		String title,
		CoordinateCollectionType type
		) {
		
		_coordinates = coordinates;
		_title = title;
		_type = type;
		
		} // End of constructor.
	
	//##########################################################################
	
	// METHODS
	
	public String getTitle() {
		return _title;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	public CoordinateCollectionType getType() {
		return _type;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	public Coordinate getCoordinate(int index) {
		return _coordinates[index];
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	
	public int size() {
		return _coordinates.length;
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	public int getSize() {
		return _coordinates.length;
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	public double[] getX() {
		return getXandY()[0];
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	public double[][] getXandY() {
		
		final double[][] coordinates = new double[2][getSize()];
		
		for (int i = 0; i < getSize(); i++) {
			Coordinate c = _coordinates[i];
			coordinates[0][i] = c.getX();
			coordinates[1][i] = c.getY();
			}
		
		
		return coordinates;
		
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	public double[] getY() {
		return getXandY()[1];
		} // End of method.

	////////////////////////////////////////////////////////////////////////////

	public double getMaximumX() {

		double max = 0;
		for (Coordinate coordinate : _coordinates) {
			if (coordinate.getX() > max) max = coordinate.getX();
			}

		return max;

		} // End of method.

	////////////////////////////////////////////////////////////////////////////

	public double getMaximumY() {

		double max = 0;
		for (Coordinate coordinate : _coordinates) {
			if (coordinate.getY() > max) max = coordinate.getY();
			}

		return max;

		} // End of method.

	//##########################################################################
	
	} // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
