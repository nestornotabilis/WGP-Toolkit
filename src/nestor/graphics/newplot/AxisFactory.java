// AxisFactory.java created on 03 February 2004 at 16:30

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
 Factory for creating IAxis objects.
 @author  Kevin Ashelford.
 */
public class AxisFactory {
	
	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS
	
	//##########################################################################
	
	// CONSTRUCTOR
	
	
	private AxisFactory() {
		
		} // End of constructor.
	
	//##########################################################################
	
	// PROPERTY ACCESSOR METHODS
	
	//##########################################################################
	
	// METHODS
	
	/**
	 Creates a new instance of an IAxis object.
	 @param title IAxis title.
	 @param axisType AxisType enum.
	 @param from Start value for axis.
	 @param to End value for axis.
	 @param increment Scale increment - determines number of major ticks.
	 @param minorTicks Number of minor ticks between each major tick.
	 @param scaleType ScaleType enum.
	 @return IAxis object.
	 */
	public static IAxis createAxis(
		String title,
		AxisType axisType,
		float from, 
		float to,
		float increment,
		int minorTicks,
		ScaleType scaleType
		) {
		
		IAxis axis = new DefaultAxis();
		axis.setRange(from, to);
		axis.setIncrement(increment);
		axis.setScaleType(scaleType);
		axis.setTitle(title);
		axis.setType(axisType);
		axis.setMinorTicks(minorTicks);
		
		return axis;
		
		} // End of method.
	
	//##########################################################################
	
	} // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
