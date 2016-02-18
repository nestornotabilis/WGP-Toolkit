// PlotLineFormatterFactory.java created on 04 February 2004 at 10:23

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

import java.awt.*;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 Factory class for generating IPlotLineFormatter objects.
 @author  Kevin Ashelford.
 @version __VERSION__.
 */
public class PlotLineFormatterFactory {
	
	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS
	
	//##########################################################################
	
	// CONSTRUCTOR
	
	private PlotLineFormatterFactory() {
		
		} // End of constructor.
	
	//##########################################################################
	
	// PROPERTY ACCESSOR METHODS
	
	//##########################################################################
	
	// METHODS
	
	/**
	 Returns new IPlotLineFormatter object.
	 @param collection ICoordinateCollection object to be registered with formatter.
	 @return IPlotLineFormatter object.
	 */
	public static IPlotLineFormatter createPlotLineFormatter(
		ICoordinateCollection collection
		) {
		return new DefaultPlotLineFormatter(collection);
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Returns new IPlotLineFormatter object for the <b>first</b> plot-line on a
	 graph.
	  @param collection ICoordinateCollection object to be registered with formatter.
	 @return IPlotLineFormatter object.
	 */
	public static IPlotLineFormatter createFirstPlotLineFormatter(
		ICoordinateCollection collection
		) {
		
		IPlotLineFormatter plf = new DefaultPlotLineFormatter(collection);
		
		plf.setLineColor(Color.BLUE);
		plf.setSymbolColor(Color.BLUE);
		plf.setGapToSymbol(false);
		plf.setLineStyle(LineStyle.Solid);
		plf.setSymbol(Symbol.ClosedCircle);
		plf.setSymbolSize(8);
		
		return plf;
		
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Returns new IPlotLineFormatter object for the <b>second</b> plot-line on a
	 graph.
	 @param collection ICoordinateCollection object to be registered with formatter.
	 @return IPlotLineFormatter object.
	 */
	public static IPlotLineFormatter createSecondPlotLineFormatter(
		ICoordinateCollection collection
		) {
		
		IPlotLineFormatter plf = new DefaultPlotLineFormatter(collection);
		
		plf.setLineColor(Color.RED);
		plf.setSymbolColor(Color.RED);
		plf.setGapToSymbol(false);
		plf.setLineStyle(LineStyle.Solid);
		plf.setSymbol(Symbol.ClosedCircle);
		plf.setSymbolSize(8);
		
		return plf;
		
		} // End of method.
	
	//##########################################################################
	
	} // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
