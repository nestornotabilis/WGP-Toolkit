// DefaultGraphModel.java created on 03 February 2004 at 11:55

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
 Default implementation of the IGraphModel interface.  This class determines
 what data should be displayed by an instance of the Graph class.
 @author  Kevin Ashelford.
 */
class DefaultGraphModel implements IGraphModel {
	
	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS
	
	private ICoordinateCollection[] _coordinateCollections;
	private boolean _isCumulative = false;
	
	//##########################################################################
	
	// CONSTRUCTOR
	
	/**
	 Creates a new instance of DefaultGraphModel.
	 */
	public DefaultGraphModel() {
		_coordinateCollections = null;
		} // End of constructor.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Creates a new instance of DefaultGraphModel.
	 @param collection ICoordinateCollection representing the plot-line
	 to display within a Graph object.
	 */
	public DefaultGraphModel(ICoordinateCollection collection) {
		setCoordinateCollection(collection);
		} // End of constructor.
	
	////////////////////////////////////////////////////////////////////////////

	/**
	 Creates a new instance of DefaultGraphModel.
	 @param collections Array of ICoordinateCollection objects
	 representing the plot-lines to be displayed within a Graph object.
	 */
	public DefaultGraphModel(ICoordinateCollection[] collections) {
		_coordinateCollections = collections;
		} // End of constructor.
	
	//##########################################################################
	
	// METHODS

	public boolean hasData() {
		if (_coordinateCollections == null) {return false;} else {return true;}
		} // End of method.

	////////////////////////////////////////////////////////////////////////////

	public void deleteData() {
		_coordinateCollections = null;
		}

	////////////////////////////////////////////////////////////////////////////

	/**
	 Returns the number of coordinate collections (and hence number of 
	 plot-lines) associated with model.
	 @return Number of CoordinateCollections.
	 */
	public int getCoordinateCollectionCount() {
		return _coordinateCollections.length;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Registers a single ICoordinateCollection object with the model.
	 @param collection CoordinateColection object.
	 */
	public void setCoordinateCollection(ICoordinateCollection collection) {
		ICoordinateCollection[] collections = {collection};
		_coordinateCollections = collections;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Sets the ICoordinateCollection (plot-line) objects associated with the
	 IGraphModel.
	 @param collections Array of ICoordinateCollection objects.
	 */
	public void setCoordinateCollections(ICoordinateCollection[] collections) {
		_coordinateCollections = collections;
		} // End of property.

	public void setCoordinateCollections(ICoordinateCollection[] collections, boolean isCumulative) {
		_coordinateCollections = collections;
		_isCumulative = isCumulative;
		}

	////////////////////////////////////////////////////////////////////////////

	public boolean isCumulative() {
		return _isCumulative;
		} // End of method.

	public void setCumulative(boolean b) {_isCumulative = b;}

	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Returns the current ICoordinateCollection objects associated with model.
	 @return Array of ICoordinateCollection objects.
	 */
	public ICoordinateCollection[] getCoordinateCollections() {
		return _coordinateCollections;
		} // End of property.

	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Returns the ICoordinateCollection (plot-line) indicated by the supplied
	 index number.
	 @param index Index number of ICoordinateCollection object.
	 @return ICoordinateCollection object.
	 */
	public ICoordinateCollection getCoordinateCollection(int index) {
		return this._coordinateCollections[index];
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Returns the title of the ICoordinateCollection(plot-line) in question.
	 @param index Index number for ICoordinateCollection object (plot-line).
	 @return Title of ICoordinateCollection and hence plot-line.
	 */
	public String getCoordinateCollectionTitle(int index) {
		return _coordinateCollections[index].getTitle();
		} // End of method.
	
	//##########################################################################
	
	} // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
