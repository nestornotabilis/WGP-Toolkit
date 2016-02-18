// DefaultAxis.java created on 03 February 2004 at 14:26

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
 Default implementation of the IAxis interface.
 @author  Kevin Ashelford.
 */
class DefaultAxis implements IAxis {
	
	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS
	
	private float		_from;
	private float		_to;
	private ScaleType	_scaleType;
	private int			_majorTicks;
	private int			_minorTicks;
	private float		_increment;
	private String		_title;
	private AxisType	_type;
	
	//##########################################################################
	
	// CONSTRUCTOR
	
	/**
	 Creates a new instance of DefaultAxis.
	 */
	public DefaultAxis() {
		
		_from = 0f;
		_to = 10f;
		_scaleType = ScaleType.Linear;
		_majorTicks = 11;
		
		_minorTicks = 4;
		_title = "";
		_type = AxisType.Unknown;
		
		} // End of constructor.
	
	//##########################################################################
	
	// PROPERTY ACCESSOR METHODS
	
	
	/**
	 Sets range for axis.
	 @param from IAxis start value.
	 @param to IAxis end value.
	 */
	public void setRange(float from, float to) {
		_from = from;
		_to = to;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Sets axis start value.
	 @param value IAxis start value.
	 */
	public void setFrom(float value) {
		_from = value;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Gets axis start value.
	 @return Start value.
	 */
	public float getFrom() {
		return _from;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Sets axis end value.
	 @param value IAxis end value.
	 */
	public void setTo(float value) {
		_to = value;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Gets axis end value.
	 @return IAxis end value.
	 */
	public float getTo() {
		return _to;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Sets type of scale to display.
	 @param type ScaleType enum.
	 */
	public void setScaleType(ScaleType type) {
		_scaleType = type;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Gets scale type of axis.
	 @return ScaleType enum.
	 */
	public ScaleType getScaleType() {
		return _scaleType;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Sets scale increment.
	 @param value Increment value.
	 */
	public void setIncrement(float value) {
		_increment = value;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Gets scale increment.
	 @return Increment value.
	 */
	public float getIncrement() {
		return _increment;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Sets number of major ticks to display.
	 @param number Number of major ticks.
	 */
	public void setMajorTicks(int number) {
		throw new UnsupportedOperationException();
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Gets the number of major ticks to display.
	 @return Number of major ticks.
	 */
	public int getMajorTicks() {
		throw new UnsupportedOperationException();
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Sets number of minor ticks to display (between major ticks).
	 @param number Number of minor ticks.
	 */
	public void setMinorTicks(int number) {
		_minorTicks = number;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Gets number of minor ticks to display (between major ticks).
	 @return Number of minor ticks.
	 */
	public int getMinorTicks() {
		return _minorTicks;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Sets axis title.
	 @param title IAxis title.
	 */
	public void setTitle(String title) {
		_title = title;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Gets axis title.
	 @return IAxis title.
	 */
	public String getTitle() {
		return _title;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Sets axis type.
	 @param type AxisType enumerator.
	 */
	public void setType(AxisType type) {
		_type = type;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Gets axis type.
	 @return AxisType enumerator.
	 */
	public AxisType getType() {
		return _type;
		} // End of property.
	
	//##########################################################################
	
	// METHODS
	
	
	
	//##########################################################################
	
	} // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
