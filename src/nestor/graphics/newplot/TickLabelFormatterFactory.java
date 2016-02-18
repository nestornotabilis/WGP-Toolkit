// GraphTickLabelFormatterFactory.java created on 04 February 2004 at 13:56

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
 Factory for ITickLabelFormatter objects.
 @author  Kevin Ashelford.
 @version __VERSION__.
 */
class TickLabelFormatterFactory {
	
	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS
	
	//##########################################################################
	
	// CONSTRUCTOR
	
	private TickLabelFormatterFactory() {
		
		} // End of constructor.
	
	//##########################################################################
	
	// PROPERTY ACCESSOR METHODS
	
	//##########################################################################
	
	// METHODS
	
	/**
	 Returns a new ITickLabelFormatter object.
	 @return ITickLabelFormatter object.
	 */
	public static ITickLabelFormatter createTickLabelFormatter() {
		return new DefaultTickLabelFormatter();
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Returns a new ITickLabelFormatter object for <b>major</b> ticks.
	 @return ITickLabelFormatter object.
	 */
	public static ITickLabelFormatter createMajorTickLabelFormatter() {
		return new DefaultTickLabelFormatter();
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Returns a new ITickLabelFormatter object for <b>minor</b> ticks.
	 @return ITickLabelFormatter object.
	 */
	public static ITickLabelFormatter createMinorTickLabelFormatter() {
		ITickLabelFormatter f = new DefaultTickLabelFormatter();
		f.setVisible(false);
		return f;
		} // End of method.
	
	//##########################################################################
	
	} // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
