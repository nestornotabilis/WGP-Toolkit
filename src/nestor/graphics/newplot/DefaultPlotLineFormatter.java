// DefaultPlotLineFormatter.java created on 03 February 2004 at 15:32

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
 Default implementation of the IPlotLineFormatter interface.
 @author  Kevin Ashelford.
 @version __VERSION__.
 */
public class DefaultPlotLineFormatter implements IPlotLineFormatter {
	
	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS
	
	private Color _lineColor;
	private Color _symbolColor;
	private float _lineWidth;
	private LineStyle _lineStyle;
	private Symbol _symbol;
	private int _symbolSize;
	private boolean _gapToSymbol;
	private PlotStyle _plotStyle;
	private ICoordinateCollection _coordinateCollection;
	
	//##########################################################################
	
	// CONSTRUCTOR
	
	/**
	 Creates a new instance of DefaultPlotLineFormatter.
	 @param collection ICoordinateCollection object corresponding to formatter.
	 */
	public DefaultPlotLineFormatter(ICoordinateCollection collection) {
		
		_coordinateCollection = collection;
		
		_lineColor = Color.BLUE;
		_symbolColor = Color.BLUE;
		_lineWidth = 1;
		_lineStyle = LineStyle.Solid;
		_symbol = Symbol.ClosedCircle;
		_symbolSize = 8;
		_gapToSymbol = false;
		_plotStyle = PlotStyle.LineAndScatter;
		
		
		}// End of constructor.
	
	//##########################################################################
	
	// PROPERTY ACCESSOR METHODS
	
	/**
	 Gets ICoordinateCollection object registered with formatter.
	 @return ICoordinateCollection object.
	 */
	public ICoordinateCollection getCoordinateCollection() {
		return _coordinateCollection;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Sets colour of plot line.
	 @param color Line Colour.
	 */
	public void setLineColor(Color color) {
		_lineColor = color;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Gets colour of plot line.
	 @return Line colour.
	 */
	public Color getLineColor() {
		return _lineColor;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	
	/**
	 Sets width of plot line.
	 @param width Line width.
	 */
	public void setLineWidth(float width) {
		_lineWidth = width;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	
	/**
	 Get width of plot line.
	 @return Line width.
	 */
	public float getLineWidth() {
		return _lineWidth;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	
	/**
	 Sets style of plot line.
	 @param style LineStyle enum.
	 */
	public void setLineStyle(LineStyle style) {
		_lineStyle = style;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	
	/**
	 Gets style of plot line.
	 @return LineStyle enum.
	 */
	public LineStyle getLineStyle() {
		return _lineStyle;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	
	/**
	 Sets plot symbol.
	 @param symbol Symbol enum.
	 */
	public void setSymbol(Symbol symbol) {
		_symbol = symbol;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	

	/**
	 Gets plot symbol.
	 @return Symbol enum.
	 */
	public Symbol getSymbol() {
		return _symbol;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	
	/**
	 Sets symbol size.
	 @param size Symbol size.
	 */
	public void setSymbolSize(int size) {
		_symbolSize = size;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	
	/**
	 Gets symbol size.
	 @return Symbol size.
	 */
	public int getSymbolSize() {
		return _symbolSize;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	
	/**
	 Sets symbol colour.
	 @param color Symbol colour.
	 */
	public void setSymbolColor(Color color) {
		_symbolColor = color;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	
	/**
	 Gets symbol colour.
	 @return Symbol colour.
	 */
	public Color getSymbolColor() {
		return _symbolColor;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	
	/**
	 Determines whether there is a gap between plot line and symbol.
	 @param b True if there is to be a gap.
	 */
	public void setGapToSymbol(boolean b) {
		_gapToSymbol = b;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	
	/**
	 Returns true if there is to be a gap between plot line and symbol.
	 @return True if there is to be a gap.
	 */
	public boolean isGapToSymbol() {
		return _gapToSymbol;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	
	/**
	 Sets plot style for line.
	 @param plotStyle PlotStyle enum.
	 */
	public void setPlotStyle(PlotStyle plotStyle) {
		_plotStyle = plotStyle;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	
	/**
	 Gets plot style for line.
	 @return PlotStyle enum.
	 */
	public PlotStyle getPlotStyle() {
		return _plotStyle;
		} // End of property.
	
	
	//##########################################################################
	
	// METHODS
	
	//##########################################################################
	
	} // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
