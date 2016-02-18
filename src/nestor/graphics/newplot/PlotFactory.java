//==============================================================================
//
//   PlotFactory.java
//   Created: Dec 19, 2010 at 5:24:50 PM
//   Copyright (C) 2008, University of Liverpool.
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

package nestor.graphics.newplot;

/**
 * 
 * @author  Kevin Ashelford.
 */
public class PlotFactory {

	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS
	
	//##########################################################################
	
	// CONSTRUCTOR
	
    private PlotFactory() {
		
		} // End of constructor.

	//##########################################################################
		
	// METHODS

	/**
	 Create XY plot.
	 @return Plot object.
	 */
	public static Plot createXYPlot() {
		return new _XYPlot();
		} // End of method.

	/**
	 Create XY plot.
	 @param xAxis X axis.
	 @param yAxis Y axis.
	 @return Plot object.
	 */
	public static Plot createXYPlot(IAxis xAxis, IAxis yAxis) {
		return new _XYPlot(xAxis, yAxis);
		} // End of method.

	/**
	 Create histogram plot.
	 @return Plot object.
	 */
	public static Plot createHistogramPlot() {
		return new _HistogramPlot();
		} // End of method.

	/**
	 Create histogram plot.
	 @param xAxis X axis.
	 @param yAxis Y axis.
	 @return Plot object.
	 */
	public static Plot createHistogramPlot(IAxis xAxis, IAxis yAxis) {
		return new _HistogramPlot(xAxis, yAxis);
		} // End of method.

	/**
	 Create box and whisker plot in horizontal orientation.
	 @return Plot object.
	 */
	public static Plot createHorizontalBoxAndWhiskerPlot() {
		return new _HorizontalBoxAndWhiskerPlot();
		} // End of method.

	/**
	 Create box and whisker plot in horizontal orientation.
	 @param xAxis X axis.
	 @return Plot object.
	 */
	public static Plot createHorizontalBoxAndWhiskerPlot(IAxis xAxis) {
		return new _HorizontalBoxAndWhiskerPlot(xAxis);
		} // End of method.


	//##########################################################################
	
	// INNER CLASSES
	
	//##########################################################################
	
    } // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////





