// IGraphModel.java created on 03 February 2004 at 10:40

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
 Classes that implement this interface determine what data should be displayed 
 by an instance of the Graph class.
 @author  Kevin Ashelford.
 */
public interface IGraphModel extends IDataModel {
	
	//##########################################################################
	
	/**
	 Registers a single ICoordinateCollection object with the model.
	 @param collection CoordinateColection object.
	 */
	public void setCoordinateCollection(ICoordinateCollection collection);

	/**
	 Registers multiple ICoordinateCollection objects with the model.
	 @param collections Array of ICoordinateCollection objects.
	 */
	public void setCoordinateCollections(ICoordinateCollection[] collections);


	/**
	 Registers a list of coordinate collections with data model.
	 @param collections List of coordinate collections.
	 @param isCumulative If true, plot is cumulative.
	 */
	public void setCoordinateCollections(ICoordinateCollection[] collections, boolean isCumulative);

	/**
	 Returns true if plot is cumulative.
	 @return True if cumulative.
	 */
	public boolean isCumulative();

	/**
	 If true, sets plot to be cumulative.
	 @param b If true, plot is cumulative.
	 */
	public void setCumulative(boolean b);
	
	/**
	 Returns the current ICoordinateCollection objects associated with model.
	 @return Array of ICoordinateCollection objects.
	 */
	public ICoordinateCollection[] getCoordinateCollections();
	
	/**
	 Returns the number of coordinate collections (and hence number of 
	 plot-lines) associated with model.
	 @return Number of CoordinateCollections.
	 */
	public int getCoordinateCollectionCount();
	
	/**
	 Returns the ICoordinateCollection (plot-line) indicated by the supplied
	 index number.
	 @param index Index number of ICoordinateCollection object.
	 @return ICoordinateCollection object.
	 */
	public ICoordinateCollection getCoordinateCollection(int index);
	
	/**
	 Returns the title of the ICoordinateCollection(plot-line) in question.
	 @param index Index number for ICoordinateCollection
	 object (plot-line).
	 @return Title of ICoordinateCollection and hence plot-line.
	 */
	public String getCoordinateCollectionTitle(int index);
	
	//##########################################################################
	
	} // End of interface.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
