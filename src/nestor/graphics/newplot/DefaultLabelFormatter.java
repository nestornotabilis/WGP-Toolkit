// DefaultLabelFormatter.java created on 04 February 2004 at 13:20

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
 Default implementation of ILabelFormatter interface.
 @author  Kevin Ashelford.
 */
class DefaultLabelFormatter implements ILabelFormatter {
	
	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS
	
	private Color _color;
	private boolean _visible;
	/** Label font. */
	protected Font _font;
	
	//##########################################################################
	
	// CONSTRUCTOR
	
	/**
	 Creates a new instance of DefaultGraphLabelFormatter.
	 */
	public DefaultLabelFormatter() {
		
		_color = Color.BLACK;
		_visible = true;
		_font = new Font("Dialog", Font.PLAIN, 12);
		
		} // End of constructor.
	
	//##########################################################################
	
	// PROPERTY ACCESSOR METHODS
	
	/**
	 Sets colour of label.
	 @param color Label colour.
	 */
	public void setColor(Color color) {
		_color = color;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Gets colour of label.
	 @return Label colour.
	 */
	public Color getColor() {
		return _color;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Sets whether label is visible.
	 @param b True if visible.
	 */
	public void setVisible(boolean b) {
		_visible = b;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Returns true if label is visible.
	 @return True if label is visible.
	 */
	public boolean isVisible() {
		return _visible;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Sets font for label.
	 @param font Label Font object.
	 */
	public void setFont(Font font) {
		_font = font;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Gets label font.
	 @return Font object.
	 */
	public Font getFont() {
		return _font;
		} // End of property.
	
	//##########################################################################
	
	// METHODS
	
	//##########################################################################
	
	} // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
