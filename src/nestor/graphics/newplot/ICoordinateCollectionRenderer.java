// GraphDataPointRenderer.java created on 03 February 2004 at 10:41

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
 Graph object data point renderer interface.  Classes implementing this 
 interface define how points in a plot should be rendered in a Graph object.
 @author  Kevin Ashelford.
 */
public interface ICoordinateCollectionRenderer extends IDataRenderer {
	
	//##########################################################################
	
	/**
	 Sets IPlotLineFormatter objects for renderer.
	 @param formatters Array of IPlotLineFormatter objects.
	 */
	public void setPlotLineFormatters(IPlotLineFormatter[] formatters);
	
	/**
	 Gets IPlotLineFormatter objects for renderer.
	 @return formatters Array of IPlotLineFormatter objects.
	 */
	public IPlotLineFormatter[] getPlotLineFormatters();
	
	/**
	 Gets the plot line formatting object corresponding to the supplied 
	 ICoordinateCollection object.
	 @param coordinateCollection ICoordinateCollection object representing a
	 plot-line.
	 @return IPlotLineFormatter object.
	 */
	public IPlotLineFormatter getPlotLineFormatter(
		ICoordinateCollection coordinateCollection
		);
	
	//##########################################################################
	
	} // End of interface.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
