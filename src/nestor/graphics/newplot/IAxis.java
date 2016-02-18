// IAxis.java created on 03 February 2004 at 10:43

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
 IAxis interface.  Classes implementing this interface are used to define axis
 for Graph objects.
 @author  Kevin Ashelford.
 */
public interface IAxis {
	
	//##########################################################################
	
	/**
	 Sets axis type.
	 @param type AxisType enum.
	 */
	public void setType(AxisType type);
	
	/**
	 Gets axis type.
	 @return AxisType enum.
	 */
	public AxisType getType();
	
	/**
	 Sets range for axis.
	 @param from IAxis start value.
	 @param to IAxis end value.
	 */
	public void setRange(float from, float to);
	
	/**
	 Sets axis start value.
	 @param value IAxis start value.
	 */
	public void setFrom(float value);
	
	/**
	 Gets axis start value.
	 @return Start value.
	 */
	public float getFrom();
	
	/**
	 Sets axis end value.
	 @param value IAxis end value.
	 */
	public void setTo(float value);
	
	/**
	 Gets axis end value.
	 @return IAxis end value.
	 */
	public float getTo();
	
	/**
	 Sets type of scale to display.
	 @param type ScaleType enum.
	 */
	public void setScaleType(ScaleType type);
	
	/**
	 Gets scale type of axis.
	 @return ScaleType enum.
	 */
	public ScaleType getScaleType();
	
	/**
	 Sets scale increment.
	 @param value Increment value.
	 */
	public void setIncrement(float value);
	
	/**
	 Gets scale increment.
	 @return Increment value.
	 */
	public float getIncrement();
	
	/**
	 Sets number of major ticks to display.
	 @param number Number of major ticks.
	 */
	public void setMajorTicks(int number);
	
	/**
	 Gets the number of major ticks to display.
	 @return Number of major ticks.
	 */
	public int getMajorTicks();
	
	/**
	 Sets number of minor ticks to display (between major ticks).
	 @param number Number of minor ticks.
	 */
	public void setMinorTicks(int number);
	
	/**
	 Gets number of minor ticks to display (between major ticks).
	 @return Number of minor ticks.
	 */
	public int getMinorTicks();
	
	/**
	 Sets axis title.
	 @param title IAxis title.
	 */
	public void setTitle(String title);
	
	/**
	 Gets axis title.
	 @return IAxis title.
	 */
	public String getTitle();
	
	//##########################################################################
	
	} // End of interface.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
