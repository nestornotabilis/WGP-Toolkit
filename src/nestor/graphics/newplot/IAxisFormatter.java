// IAxisFormatter.java created on 04 February 2004 at 11:15

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

import java.awt.Color;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 Interface for encapsulating formating information for axes.
 @author  Kevin Ashelford.
 */
public interface IAxisFormatter {
	
	//##########################################################################
	
	/**
	 Sets colour of axis line.
	 @param color Axis line colour.
	 */
	public void setLineColor(Color color);
	
	/**
	 Gets colour of axis line.
	 @return Axis line colour.
	 */
	public Color getLineColor();
	
	/**
	 Sets width of axis line.
	 @param width Axis line width.
	 */
	public void setLineWidth(float width);
	
	/**
	 Get width of axis line.
	 @return Axis line width.
	 */
	public float getLineWidth();
	
	/**
	 Sets formatter object for axis title.
	 @param formatter GraphLabelFormatter object.
	 */
	public void setTitleFormatter(ILabelFormatter formatter);
	
	/**
	 Gets formatter object for axis title.
	 @return GraphLabelFormatter object.
	 */
	public ILabelFormatter getTitleFormatter();
	
	/**
	 Sets formatter object for major tick labels.
	 @param formatter GraphTickLabelFormatter object.
	 */
	public void setMajorTickLabelFormatter(ITickLabelFormatter formatter);
	
	/**
	 Gets formatter object for major tick labels.
	 @return GraphTickLabelFormatter object.
	 */
	public ITickLabelFormatter getMajorTickLabelFormatter();
	
	/**
	 Sets formatter object for minor tick labels.
	 @param formatter GraphTickLabelFormatter object.
	 */
	public void setMinorTickLabelFormatter(ITickLabelFormatter formatter);
	
	/**
	 Gets formatter object for minor tick labels.
	 @return GraphTickLabelFormatter object.
	 */
	public ITickLabelFormatter getMinorTickLabelFormatter();
	
	/**
	 Sets major tick length.
	 @param length Length of tick.
	 */
	public void setMajorTickLength(int length);
	
	/**
	 Gets major tick length.
	 @return Length of tick.
	 */
	public int getMajorTickLength();
	
	/**
	 Sets minor tick length.
	 @param length Length of tick.
	 */
	public void setMinorTickLength(int length);
	
	/**
	 Gets minor tick length.
	 @return Length of tick.
	 */
	public int getMinorTickLength();
	
	/**
	 Sets location of tick relative to axis.
	 @param location TickLocation enum.
	 */
	public void setMajorTickLocation(TickLocation location);
	
	/**
	 Gets location of tick relative to axis.
	 @return TickLocation enum.
	 */
	public TickLocation getMajorTickLocation();
	
	/**
	 Sets location of tick relative to axis.
	 @param location TickLocation enum.
	 */
	public void setMinorTickLocation(TickLocation location);
	
	/**
	 Gets location of tick relative to axis.
	 @return TickLocation enum.
	 */
	public TickLocation getMinorTickLocation();
	
	/**
	 Sets location of axis in context of plot.
	 @param position Position enum.
	 */
	public void setAxisPosition(Position position);
	
	/**
	 Gets location of axis in context of plot.
	 @return Position enum.
	 */
	public Position getAxisPosition();
	
	/**
	 Sets the gap between the displayed start of the axis and the true start.
	 @param value Gap size.
	 */
	public void setStartMargin(float value);
	
	/**
	 Gets the gap between the displayed start of the axis and the true start.
	 @return Gap size.
	 */
	public float getStartMargin();
	
	/**
	 Sets the gap between the true axis end and the displayed axis end.
	 @param value Gap size.
	 */
	public void setEndMargin(float value);
	
	/**
	 Gets the gap between the true axis end and the displayed axis end.
	 @return Gap size.
	 */
	public float getEndMargin();
	
	/**
	 Sets ILineFormatter object for major grid lines.
	 @param formatter ILineFormatter object.
	 */
	public void setMajorGridLineFormatter(ILineFormatter formatter);
	
	/**
	 Gets ILineFormatter object for major grid lines.
	 @return formatter ILineFormatter object.
	 */
	public ILineFormatter getMajorGridLineFormatter();
	
	/**
	 Sets ILineFormatter object for minor grid lines.
	 @param formatter ILineFormatter object.
	 */
	public void setMinorGridLineFormatter(ILineFormatter formatter);
	
	/**
	 Gets ILineFormatter object for minor grid lines.
	 @return formatter ILineFormatter object.
	 */
	public ILineFormatter getMinorGridLineFormatter();
	
	/**
	 Sets whether grid lines are visible.
	 @param b True if lines are visible.
	 */
	public void setMajorGridLinesVisible(boolean b);
	
	/**
	 Determines whether grid lines are visible.
	 @return True if lines are visible.
	 */
	public boolean isMajorGridLinesVisible();
	
	/**
	 Sets whether grid lines are visible.
	 @param b True if lines are visible.
	 */
	public void setMinorGridLinesVisible(boolean b);
	
	/**
	 Determines whether grid lines are visible.
	 @return True if lines are visible.
	 */
	public boolean isMinorGridLinesVisible();
	
	//##########################################################################
	
	} // End of interface.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
