// DefaultGraphTickLabelFormatter.java created on 04 February 2004 at 13:32

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
 Default implementation of GraphTickLabelFormatter interface.
 @author  Kevin Ashelford.
 */
public class DefaultTickLabelFormatter 
	extends DefaultLabelFormatter 
	implements ITickLabelFormatter
	{
	
	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS
	
	private LabelType _type;
	private NumberFormat _numberFormat;
	private int _decimalPlaces;
	
	//##########################################################################
	
	// CONSTRUCTOR
	
	/**
	 Creates a new instance of DefaultTickLabelFormatter.
	 */
	public DefaultTickLabelFormatter() {
		
		_font = new Font("Dialog", Font.PLAIN, 12);
		_type = LabelType.Numeric;
		_numberFormat = NumberFormat.Decimal;
		_decimalPlaces = 1;
		
		} // End of constructor.
	
	//##########################################################################
	
	// PROPERTY ACCESSOR METHODS
	
	/**
	 Sets type of tick label.
	 @param type LabelType enum.
	 */
	public void setType(LabelType type) {
		_type = type;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Gets the type of tick label.
	 @return LabelType enum.
	 */
	public LabelType getType() {
		return _type;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Sets number format of label (label must be of numeric type).
	 @param format NumberFormat enum.
	 @throws IllegalArgumentException Exception thrown if label is not of a
	 numeric type.
	 */
	public void setNumberFormat(NumberFormat format) throws IllegalArgumentException {
		
		if (_type == LabelType.Numeric) {
			_numberFormat = format;
			}
		else {
			throw new IllegalArgumentException(
				"GraphTickLabelFormatter object has incorrect LabelType. " + 
				"The NumberFormat " + format.toString() + 
				" cannot be set when LabelType is " + _type.toString() + "."
				);
			}
		
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Gets number format of label (label must be of numeric type).
	 @return NumberType enum.
	 @throws IllegalArgumentException Exception thrown if label is not of a
	 numeric type.
	 */
	public NumberFormat getNumberFormat() {
		
		if (_type == LabelType.Numeric) {
			return _numberFormat;
			}
		else {
			throw new IllegalArgumentException(
				"GraphTickLabelFormatter object has incorrect LabelType. " + 
				"The NumberFormat " + _numberFormat.toString() + 
				" is invalid when LabelType is " + _type.toString() + "."
				);
			}
		
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Sets number of decimal places for numeric label (label must be of numeric 
	 type).
	 @param number Number of decimal places.
	 @throws IllegalArgumentException Exception thrown if label is not of a
	 numeric type.
	 */
	public void setDecimalPlaces(int number) {
		
		if (_type == LabelType.Numeric) {
			_decimalPlaces = number;
			}
		else {
			throw new IllegalArgumentException(
				"GraphTickLabelFormatter object has incorrect LabelType. " + 
				"The DecimalPlaces property cannot be set with " + number + 
				" when LabelType is " + _type.toString() + "."
				);
			}
		
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Gets number of decimal places for numeric label (label must be of numeric
	 type).
	 @return Number of decimal places.
	 @throws IllegalArgumentException Exception thrown if label is not of a
	 numeric type.
	 */
	public int getDecimalPlaces() {
		
		if (_type == LabelType.Numeric) {
			return _decimalPlaces;
			}
		else {
			throw new IllegalArgumentException(
				"GraphTickLabelFormatter object has incorrect LabelType. " + 
				"The DecimalPlaces property is invalid when LabelType is " + 
				_type.toString() + "."
				);
			}
		
		} // End of property.
	
	//##########################################################################
	
	// METHODS
	
	//##########################################################################
	
	} // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
