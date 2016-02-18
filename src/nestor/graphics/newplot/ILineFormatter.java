// ILineFormatter.java created on 06 February 2004 at 08:38

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
 Containing formatting information for general plot lines.
 @author  Kevin Ashelford.
 */
public interface ILineFormatter {
	
	//##########################################################################
	
	/**
	 Sets colour of line.
	 @param color Line Colour.
	 */
	public void setLineColor(Color color);
	
	/**
	 Gets colour of line.
	 @return Line colour.
	 */
	public Color getLineColor();
	
	/**
	 Sets width of line.
	 @param width Line width.
	 */
	public void setLineWidth(float width);
	
	/**
	 Get width of line.
	 @return Line width.
	 */
	public float getLineWidth();
	
	/**
	 Sets style of line.
	 @param style LineStyle enumerator.
	 */
	public void setLineStyle(LineStyle style);
	
	/**
	 Gets style of line.
	 @return LineStyle enumerator.
	 */
	public LineStyle getLineStyle();
	
	//##########################################################################
	
	} // End of interface.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
