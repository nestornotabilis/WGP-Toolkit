//==============================================================================
//
//   DefaultBoxAndWhiskerFormatter.java
//   Created: Jan 6, 2011 at 11:17:30 AM
//   Copyright (C) 2011, University of Liverpool.
//   Author: Kevin Ashelford.
//
//   Contact details:
//   Email:   k.ashelford@liv.ac.uk
//   Address: School of Biological Sciences, University of Liverpool, 
//            Biosciences Building, Crown Street, Liverpool, UK. L69 7ZB
//
//   This program is free software; you can redistribute it and/or modify
//   it under the terms of the GNU General Public License as published by
//   the Free Software Foundation; either version 2 of the License, or
//   any later version.
//
//    This program is distributed in the hope that it will be useful,
//   but WITHOUT ANY WARRANTY; without even the implied warranty of
//   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//   GNU General Public License for more details.
//
//   You should have received a copy of the GNU General Public License
//   along with this program; if not, write to the Free Software
//   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
//==============================================================================

package nestor.graphics.newplot.boxAndWhisker;

import nestor.stats.Stats;
import java.awt.*;
import nestor.graphics.newplot.*;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 * Default implementation of a box-and-whisker formatter interface.
 * @author  Kevin Ashelford.
 */
public class DefaultBoxAndWhiskerFormatter implements IBoxAndWhiskerFormatter {

	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS

	private Color		_lineColor;
	private float		_lineWidth;
	private LineStyle	_lineStyle;
	private Stats		_stats;

	private Color		_symbolColor;
	private Symbol		_symbol;
	private int			_symbolSize;

	private Color		_boxColor;
	
	//##########################################################################
	
	// CONSTRUCTOR
	
    /**
	 * Creates a new instance of <code>DefaultBoxAndWhiskerFormatter</code>.
	 @param stats Stats object encapsulating data to be formatted.
	 */
    public DefaultBoxAndWhiskerFormatter(final Stats stats) {

		_lineColor		= Color.blue;
		_lineWidth		= 1;
		_lineStyle		= LineStyle.Solid;
		_stats			= stats;

		_symbolColor	= Color.black;
		_symbol			= Symbol.ClosedCircle;
		_symbolSize		= 8;

		_boxColor		= Color.blue;


		} // End of constructor.

	//##########################################################################
		
	// METHODS

	public void setSymbol(Symbol symbol) {
		_symbol = symbol;
		} // End of property.

	////////////////////////////////////////////////////////////////////////////

	public Symbol getSymbol() {
		return _symbol;
		} // End of property.

	////////////////////////////////////////////////////////////////////////////

	public void setSymbolSize(int size) {
		_symbolSize = size;
		} // End of property.

	////////////////////////////////////////////////////////////////////////////

	public int getSymbolSize() {
		return _symbolSize;
		} // End of property.

	////////////////////////////////////////////////////////////////////////////

	public void setSymbolColor(Color color) {
		_symbolColor = color;
		} // End of property.

	////////////////////////////////////////////////////////////////////////////

	public Color getSymbolColor() {
		return _symbolColor;
		} // End of property.

	////////////////////////////////////////////////////////////////////////////

	public Color getBoxColor() {
		return _boxColor;
		} // End of property.

	public void setBoxColor(final Color color) {
		_boxColor = color;
		} // End of method.

	////////////////////////////////////////////////////////////////////////////

	public void setLineColor(Color color) {
		_lineColor = color;
		} // End of property.

	////////////////////////////////////////////////////////////////////////////

	public Color getLineColor() {
		return _lineColor;
		} // End of property.

	////////////////////////////////////////////////////////////////////////////


	public void setLineWidth(float width) {
		_lineWidth = width;
		} // End of property.

	////////////////////////////////////////////////////////////////////////////

	public float getLineWidth() {
		return _lineWidth;
		} // End of property.

	////////////////////////////////////////////////////////////////////////////

	public void setLineStyle(LineStyle style) {
		_lineStyle = style;
		} // End of property.

	////////////////////////////////////////////////////////////////////////////

	public LineStyle getLineStyle() {
		return _lineStyle;
		} // End of property.

	////////////////////////////////////////////////////////////////////////////

	public Stats getStats() {
		return _stats;
		} // End of property.
	
	//##########################################################################
	
	// INNER CLASSES
	
	//##########################################################################
	
    } // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////





