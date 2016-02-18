// GraphTickLabelFormatter.java created on 04 February 2004 at 11:41

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
 Interface for Graph tick label formatters.
 @author  Kevin Ashelford.
 */
public interface ITickLabelFormatter extends ILabelFormatter {
	
	//##########################################################################
	
	/**
	 Sets type of tick label.
	 @param type LabelType enumerator.
	 */
	public void setType(LabelType type);
	
	/**
	 Gets the type of tick label.
	 @return LabelType enumerator.
	 */
	public LabelType getType();
	
	/**
	 Sets number format of label (label must be of numeric type).
	 @param format NumberFormat enumerator.
	 @throws IllegalArgumentException Exception thrown if label is not of a
	 numeric type.
	 */
	public void setNumberFormat(NumberFormat format) throws IllegalArgumentException;
	
	/**
	 Gets number format of label (label must be of numeric type).
	 @return NumberType enumerator.
	 @throws IllegalArgumentException Exception thrown if label is not of a
	 numeric type.
	 */
	public NumberFormat getNumberFormat() throws IllegalArgumentException;
	
	/**
	 Sets number of decimal places for numeric label (label must be of numeric 
	 type).
	 @param number Number of decimal places.
	 @throws IllegalArgumentException Exception thrown if label is not of a
	 numeric type.
	 */
	public void setDecimalPlaces(int number) throws IllegalArgumentException;
	
	/**
	 Gets number of decimal places for numeric label (label must be of numeric
	 type).
	 @return Number of decimal places.
	 @throws IllegalArgumentException Exception thrown if label is not of a
	 numeric type.
	 */
	public int getDecimalPlaces() throws IllegalArgumentException;
	
	//##########################################################################
	
	} // End of interface.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
