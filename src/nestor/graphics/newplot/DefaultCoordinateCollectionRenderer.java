// DefaultGraphCoordinateCollectionRenderer.java created on 04 February 2004 at 14:58

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
 Default implementation of GraphCoordinateCollectionRenderer interface.
 @author  Kevin Ashelford.
 */
public class DefaultCoordinateCollectionRenderer
	implements ICoordinateCollectionRenderer {
	
	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS
		
	private IPlotLineFormatter[] _plotLineFormatters;
	
	//##########################################################################
	
	// CONSTRUCTOR
	
	/**
	 Creates a new instance of DefaultGraphCoordinateCollectionRenderer.
	 @param formatters PlotLineFormatters to be handled by renderer.
	 */
	public DefaultCoordinateCollectionRenderer(
		IPlotLineFormatter[] formatters
		) {
		
		_plotLineFormatters = formatters;
		
		} // End of constructor.
	
	//##########################################################################
	
	// PROPERTY ACCESSOR METHODS
	
	/**
	 Sets IPlotLineFormatter objects for renderer.
	 @param formatters Array of IPlotLineFormatter objects.
	 */
	public void setPlotLineFormatters(IPlotLineFormatter[] formatters) {
		_plotLineFormatters = formatters;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Gets IPlotLineFormatter objects for renderer.
	 @return formatters Array of IPlotLineFormatter objects.
	 */
	public IPlotLineFormatter[] getPlotLineFormatters() {
		return _plotLineFormatters;	
		} // End of property.
	
	//##########################################################################
	
	// METHODS
	
	/**
	 Gets the plot line formatting object corresponding to the supplied 
	 ICoordinateCollection object.
	 @param coordinateCollection ICoordinateCollection object representing a
	 plot-line.
	 @return IPlotLineFormatter object.
	 */
	public IPlotLineFormatter getPlotLineFormatter(
		ICoordinateCollection coordinateCollection
		) {
		
		if (_plotLineFormatters == null) {
			throw new NullPointerException(
				"No PlotLineFormatter object has been registered with " + 
				"CoordinateCollectionRenderer."
				);
			}	
			
		for (int i = 0; i < _plotLineFormatters.length; i++) {

			IPlotLineFormatter plf = _plotLineFormatters[i];
			 
			// Error if no ICoordinateCollection has been registered with formatter.
			if (plf.getCoordinateCollection() == null) {
				throw new NullPointerException(
				"No CoordinateCollection object has been registered with " + 
				"PlotLineFormatter '" + plf.toString() + "'."
				);
				}
			
			if (plf.getCoordinateCollection() == coordinateCollection) {
				return plf;
				}
			
			} // End of for loop - no formatters left.
		
		// If reach this point, supplied ICoordinateCollection object does
		// not coincide with any registered with PlotLineFormatters.
		throw new IllegalArgumentException(
			"The CoordinateCollection '" + 
			coordinateCollection.getTitle() + 
			"' has no corresponding PlotLineFormatter."
			);
			
		
		} // End of method.
	
	//##########################################################################
	
	} // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
