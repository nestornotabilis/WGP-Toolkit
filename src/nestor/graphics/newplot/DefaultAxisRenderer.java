// DefaultAxisRenderer.java created on 04 February 2004 at 14:28

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
 Default implementation of IAxisRenderer interface.
 @author  Kevin Ashelford.
 */
public class DefaultAxisRenderer implements IAxisRenderer {
	
	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS
	
	private IAxisFormatter _xAxisFormatter;
	private IAxisFormatter _yAxisFormatter;

	//##########################################################################
	
	// CONSTRUCTOR
	
	/**
	 Creates a new instance of DefaultAxisRenderer.
	 */
	public DefaultAxisRenderer() {
		
		_xAxisFormatter = AxisFormatterFactory.createXAxisFormatter();
		_yAxisFormatter = AxisFormatterFactory.createYAxisFormatter();


		} // End of constructor.
	
	//##########################################################################
	
	// METHODS

	/**
	 Gets the IAxisFormatter object corresponding to the IAxis object supplied.
	 @param axis IAxis object.
	 @return IAxisFormatter object.
	 */
	public IAxisFormatter getAxisFormatter(IAxis axis) {
		
		if (axis.getType() == AxisType.X) {
			return _xAxisFormatter;
			}
		else if (axis.getType() == AxisType.Y) {
			return _yAxisFormatter;
			}
		
		throw new IllegalArgumentException(
			"An axis with AxisType " + axis.getType().toString() + 
			"is not currently catered for."
			);
			
		
		} // End of method.



	/**
	 Returns formatter for the specified axis.
	 @param axis Type of axis.
	 @return Formatter object.
	 */
	public IAxisFormatter getAxisFormatter(AxisType axis) {

		if (axis == AxisType.X) {
			return _xAxisFormatter;
			}
		else if (axis == AxisType.Y) {
			return _yAxisFormatter;
			}

		throw new IllegalArgumentException(
			"An axis with AxisType " + axis.toString() +
			"is not currently catered for."
			);


		} // End of method.
	
	//##########################################################################
	
	} // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
