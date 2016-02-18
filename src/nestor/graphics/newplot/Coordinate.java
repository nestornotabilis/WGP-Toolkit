// Coordinate.java created on 03 February 2004 at 10:47

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
 This class holds the x and y coordinates for plotting within a Graph object.
 Coordinate is an immutable object.
 @author  Kevin Ashelford.
 */
public final class Coordinate {
	
	private double _x;
	private double _y;
	
	//##########################################################################
	
	// CONSTRUCTOR
	
	/**
	 Creates new instance of Coordinate class.
	 @param x X coordinate.
	 @param y Y coordinate.
	 */
	public Coordinate(final double x, final double y) {
		_x = x;
		_y = y;
		} // End of constructor.
	
	//##########################################################################
	
	// METHODS
	
	/**
	 Returns X coordinate.
	 @return X coordinate.
	 */
	public double getX() {
		return _x;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Returns Y coordinate.
	 @return Y coordinate.
	 */
	public double getY() {
		return _y;
		} // End of property.

	////////////////////////////////////////////////////////////////////////////

	/**
	 Sets X coordinate.
	 @param x Value.
	 */
	public void setX(final double x) {_x = x;} // End of method.

	/**
	 Sets Y coordinate.
	 @param y Value.
	 */
	public void setY(final double y) {_y = y;} // End of method.

	////////////////////////////////////////////////////////////////////////////
	
	/**
	 String representation of coordinate.
	 @return String representation of coordinate.
	 */
	@Override
	public String toString() {
		return "x=" + _x + ", y=" + _y;
		} // End of method.
	
	//##########################################################################
	
	} // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
