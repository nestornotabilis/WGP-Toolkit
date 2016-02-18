// DefaultAxisFormatter.java created on 04 February 2004 at 13:09

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
 Default implementation of the IAxisFormatter interface.
 @author  Kevin Ashelford.
 @version __VERSION__.
 */
public class DefaultAxisFormatter implements IAxisFormatter {
	
	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS
	
	private Color				_lineColor;
	private float				_lineWidth;
	private ILabelFormatter		_titleFormatter;
	private ITickLabelFormatter	_majorTickLabelFormatter;
	private ITickLabelFormatter	_minorTickLabelFormatter;
	private int					_majorTickLength;
	private int					_minorTickLength;
	private TickLocation		_majorTickLocation;
	private TickLocation		_minorTickLocation;
	private Position			_axisPosition;
	private float				_startMargin;
	private float				_endMargin;
	private ILineFormatter		_majorGridLineFormatter;
	private ILineFormatter		_minorGridLineFormatter;
	private boolean				_majorGridLinesVisible;
	private boolean				_minorGridLinesVisible;
	
	//##########################################################################
	
	// CONSTRUCTOR
	
	/**
	 Creates a new instance of DefaultAxisFormatter.
	 */
	public DefaultAxisFormatter() {

		// Presets formatting for axis.

		_lineColor = Color.BLACK;
		_lineWidth = 1;
		_titleFormatter = LabelFormatterFactory.createLabelFormatter();
		
		_majorTickLabelFormatter = 
			TickLabelFormatterFactory.createMajorTickLabelFormatter();
		
		_minorTickLabelFormatter = 
			TickLabelFormatterFactory.createMinorTickLabelFormatter();
		
		_majorTickLength = 10;
		_minorTickLength = 5;
		
		_majorTickLocation = TickLocation.Out;
		_minorTickLocation = TickLocation.Out;
		
		_axisPosition = Position.Unknown;
		
		_startMargin = 0f;
		_endMargin = 0f;
		
		_majorGridLineFormatter = LineFormatterFactory.createLineFormatter();
		_minorGridLineFormatter = LineFormatterFactory.createLineFormatter();
		
		_majorGridLinesVisible = true;
		_minorGridLinesVisible = true;
		
		} // End of constructor.
	
	//##########################################################################
	
	// PROPERTY ACCESSOR METHODS
	
	/**
	 Sets whether grid lines are visible.
	 @param b True if lines are visible.
	 */
	public void setMajorGridLinesVisible(boolean b) {
		_majorGridLinesVisible = b;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Determines whether grid lines are visible.
	 @return True if lines are visible.
	 */
	public boolean isMajorGridLinesVisible() {
		return _majorGridLinesVisible;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Sets whether grid lines are visible.
	 @param b True if lines are visible.
	 */
	public void setMinorGridLinesVisible(boolean b) {
		_minorGridLinesVisible = b;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Determines whether grid lines are visible.
	 @return True if lines are visible.
	 */
	public boolean isMinorGridLinesVisible() {
		return _minorGridLinesVisible;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Sets ILineFormatter object for major grid lines.
	 @param formatter ILineFormatter object.
	 */
	public void setMajorGridLineFormatter(ILineFormatter formatter) {
		_majorGridLineFormatter = formatter;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Gets ILineFormatter object for major grid lines.
	 @return formatter ILineFormatter object.
	 */
	public ILineFormatter getMajorGridLineFormatter() {
		return _majorGridLineFormatter;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Sets ILineFormatter object for minor grid lines.
	 @param formatter ILineFormatter object.
	 */
	public void setMinorGridLineFormatter(ILineFormatter formatter) {
		_minorGridLineFormatter = formatter;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Gets ILineFormatter object for minor grid lines.
	 @return formatter ILineFormatter object.
	 */
	public ILineFormatter getMinorGridLineFormatter() {
		return _minorGridLineFormatter;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Sets the gap between the displayed start of the axis and the true start.
	 @param value Gap size.
	 */
	public void setStartMargin(float value) {
		_startMargin = value;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Gets the gap between the displayed start of the axis and the true start.
	 @return Gap size.
	 */
	public float getStartMargin() {
		return _startMargin;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Sets the gap between the true axis end and the displayed axis end.
	 @param value Gap size.
	 */
	public void setEndMargin(float value) {
		_endMargin = value;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Gets the gap between the true axis end and the displayed axis end.
	 @return Gap size.
	 */
	public float getEndMargin() {
		return _endMargin;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Sets colour of axis line.
	 @param color Axis line colour.
	 */
	public void setLineColor(Color color) {
		_lineColor = color;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Gets colour of axis line.
	 @return Axis line colour.
	 */
	public Color getLineColor() {
		return _lineColor;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Sets width of axis line.
	 @param width Axis line width.
	 */
	public void setLineWidth(float width) {
		_lineWidth = width;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Get width of axis line.
	 @return Axis line width.
	 */
	public float getLineWidth() {
		return _lineWidth;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Sets formatter object for axis title.
	 @param formatter ILabelFormatter object.
	 */
	public void setTitleFormatter(ILabelFormatter formatter) {
		_titleFormatter = formatter;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Gets formatter object for axis title.
	 @return ILabelFormatter object.
	 */
	public ILabelFormatter getTitleFormatter() {
		return _titleFormatter;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Sets formatter object for major tick labels.
	 @param formatter ITickLabelFormatter object.
	 */
	public void setMajorTickLabelFormatter(ITickLabelFormatter formatter) {
		_majorTickLabelFormatter = formatter;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Gets formatter object for major tick labels.
	 @return ITickLabelFormatter object.
	 */
	public ITickLabelFormatter getMajorTickLabelFormatter() {
		return _majorTickLabelFormatter;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Sets formatter object for minor tick labels.
	 @param formatter ITickLabelFormatter object.
	 */
	public void setMinorTickLabelFormatter(ITickLabelFormatter formatter) {
		_minorTickLabelFormatter = formatter;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Gets formatter object for minor tick labels.
	 @return ITickLabelFormatter object.
	 */
	public ITickLabelFormatter getMinorTickLabelFormatter() {
		return _minorTickLabelFormatter;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Sets major tick length.
	 @param length Length of tick.
	 */
	public void setMajorTickLength(int length) {
		_majorTickLength = length;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Gets major tick length.
	 @return Length of tick.
	 */
	public int getMajorTickLength() {
		return _majorTickLength;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Sets minor tick length.
	 @param length Length of tick.
	 */
	public void setMinorTickLength(int length) {
		_minorTickLength = length;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Gets minor tick length.
	 @return Length of tick.
	 */
	public int getMinorTickLength() {
		return _minorTickLength;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Sets location of tick relative to axis.
	 @param location TickLocation enum.
	 */
	public void setMajorTickLocation(TickLocation location) {
		_majorTickLocation = location;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Gets location of tick relative to axis.
	 @return TickLocation enum.
	 */
	public TickLocation getMajorTickLocation() {
		return _majorTickLocation;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Sets location of tick relative to axis.
	 @param location TickLocation enum.
	 */
	public void setMinorTickLocation(TickLocation location) {
		_minorTickLocation = location;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Gets location of tick relative to axis.
	 @return TickLocation enum.
	 */
	public TickLocation getMinorTickLocation() {
		return _minorTickLocation;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Sets location of axis in context of plot.
	 @param position Position enum.
	 */
	public void setAxisPosition(Position position) {
		_axisPosition = position;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Gets location of axis in context of plot.
	 @return Position enum.
	 */
	public Position getAxisPosition() {
		return _axisPosition;
		} // End of property.
	
	//##########################################################################
	
	// METHODS
	
	//##########################################################################
	
	} // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
