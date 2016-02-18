// ICoordinateCollection.java created on 03 February 2004 at 11:03

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
 Interface for Coordinate collections.
 @author  Kevin Ashelford.
 */
public interface ICoordinateCollection {
	
	//##########################################################################
	
	/**
	 Returns title associated with coordinate collection.
	 @return Title.
	 */
	public String getTitle();
	
	/**
	 Returns label for ICoordinateCollection object.
	 @return CoordinateCollectonLabel enumerator.
	 */
	public CoordinateCollectionType getType();
	
	/**
	 Returns coordinate corresponding to index number.
	 @param index Index number of coordinate.
	 @return Coordinate object.
	 */
	public Coordinate getCoordinate(int index);
	
	/**
	 Returns number of coordinate objects stored within ICoordinateCollection
	 object.
	 @return Size of collection.
	 */
	public int getSize();
	
	/**
	 Returns the X coordinates as an array of primitive variables.
	 @return Array of primitive variables.
	 */
	public double[] getX();
	
	/**
	 Returns the Y coordinates as an array of primitive variables.
	 @return Array of primitive variables.
	 */
	public double[] getY();
	
	/**
	 Returns the X and Y coordinates as a matrix of primitive variables.
	 @return Array two arrays; array 1 contains X values, array 2 contains Y.
	 */
	public double[][] getXandY();

	/**
	 Returns highest X value in collection.
	 @return Maximum value.
	 */
	public double getMaximumX();

	/**
	 Returns highest Y value in collection.
	 @return Maximum value.
	 */
	public double getMaximumY();
	
	//##########################################################################
	
	} // End of interface.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
