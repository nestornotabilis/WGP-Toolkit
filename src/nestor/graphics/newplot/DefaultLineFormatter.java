// DefaultLineFormatter.java created on 06 February 2004 at 08:46

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
 Default implementation of the ILineFormatter interface.
 @author  Kevin Ashelford.
 @version __VERSION__.
 */
public class DefaultLineFormatter implements ILineFormatter {
	
	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS
	
	private Color _lineColor;
	private Color _symbolColor;
	private float _lineWidth;
	private LineStyle _lineStyle;
	
	//##########################################################################
	
	// CONSTRUCTOR
	
	/**
	 Creates a new instance of class.
	 */
	public DefaultLineFormatter() {
		
		_lineColor = Color.BLUE;
		_symbolColor = Color.BLUE;
		_lineWidth = 1;
		_lineStyle = LineStyle.Solid;
		
		}// End of constructor.
	
	//##########################################################################
	
	// PROPERTY ACCESSOR METHODS
	
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
	
	//##########################################################################
	
	// METHODS
	
	//##########################################################################
	
	} // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
