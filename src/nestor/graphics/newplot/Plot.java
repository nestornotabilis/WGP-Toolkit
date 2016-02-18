//==============================================================================
//
//   Plot.java
//   Created: Dec 19, 2010 at 5:25:17 PM
//   Copyright (C) 2010, University of Liverpool.
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

import java.awt.Color;
import java.awt.Point;
import java.awt.Font;
import javax.swing.JPanel;
import java.awt.print.Printable;
import java.awt.image.RenderedImage;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 * 
 * @author  Kevin Ashelford.
 */
public abstract class Plot extends JPanel  implements Printable {

	/**
	 Clears graph of any plot lines.
	 */
	public abstract void clear();

	/**
	 Triggers component to draw plot.
	 */
	public abstract void plot();

	
	/**
	 Returns a RenderedImage object representing the graph.  From this, client
	 code can generate png or jpeg files using the ImageIO.write() method.
	 @return RenderedImage object.
	 */
	public abstract RenderedImage createImage();



	/**
	 Switches on/off anti-aliasing.
	 @param b True if anti-aliasing is to be turned on.
	 */
	public abstract void setAntiAliasing(boolean b);

	/**
	 Returns true if anti-aliasing is turned on.
	 @return True if on.
	 */
	public abstract boolean isAntiAliasing();

	/**
	 Sets gap between axis label and component border.
	 @param gap Gap value.
	 */
	public abstract void setXAxisLabelToBorderGap(int gap);

	/**
	 Gets gap between axis label and component border.
	 @return Gap value.
	 */
	public abstract int getXAxisLAbelToBorderGap();

	/**
	 Sets gap between axis label and component border.
	 @param gap Gap value.
	 */
	public abstract void setYAxisLabelToBorderGap(int gap);

	/**
	 Gets gap between axis label and component border.
	 @return Gap value.
	 */
	public abstract int getYAxisLAbelToBorderGap();

	/**
	 Gets the current IDataModel object.
	 @return IDataModel object.
	 */
	public abstract IDataModel getDataModel();

	/**
	 Sets the renderer for ICoordinateCollection objects associated with the
	 IGraphModel.
	 @param renderer ICoordinateCollection object.
	 */
	public abstract void setDataRenderer(
		IDataRenderer renderer
		);

	/**
	 Gets the ICoordinateCollectionRenderer object.
	 @return ICoordinateCollectionRenderer object.
	 */
	public abstract IDataRenderer getDataRenderer();

	/**
	 Sets the IAxisRenderer object.
	 @param renderer IAxisRenderer object.
	 */
	public abstract void setAxisRenderer(IAxisRenderer renderer);

	/**
	 Gets the IAxisRenderer object.
	 @return IAxisRenderer object.
	 */
	public abstract IAxisRenderer getAxisRenderer();

	/**
	 Sets the X IAxis object registered with _XYPlot.
	 @param xAxis X IAxis object.
	 */
	public abstract void setXAxis(IAxis xAxis);

	/**
	 Gets the X IAxis object registered with _XYPlot.
	 @return X IAxis object.
	 */
	public abstract IAxis getXAxis();

	/**
	 Sets the Y IAxis object registered with _XYPlot.
	 @param yAxis Y IAxis object.
	 */
	public abstract void setYAxis(IAxis yAxis);

	/**
	 Gets the Y IAxis object registered with _XYPlot.
	 @return Y IAxis object.
	 */
	public abstract IAxis getYAxis();

	/**
	 Sets margin to the left of plot.
	 @param margin Size of margin.
	 */
	public abstract void setLeftMargin(int margin);

	/**
	 Gets margin to the left of plot.
	 @return Size of margin.
	 */
	public abstract int getLeftMargin();

	/**
	 Sets margin to the right of plot.
	 @param margin Size of margin.
	 */
	public abstract void setRightMargin(int margin);

	/**
	 Gets margin to the right of plot.
	 @return Size of margin.
	 */
	public abstract int getRightMargin();

	/**
	 Sets margin at the top of the plot.
	 @param margin Size of margin.
	 */
	public abstract void setTopMargin(int margin);

	/**
	 Gets margin at the top of the plot.
	 @return Size of margin.
	 */
	public abstract int getTopMargin();

	/**
	 Sets margin at the bottom of the plot.
	 @param margin Size of margin.
	 */
	public abstract void setBottomMargin(int margin);

	/**
	 Gets margin at the bottom of the plot.
	 @return Size of margin.
	 */
	public abstract int getBottomMargin();

	/**
	 Sets gap between tick and its label.
	 @param gap Gap value.
	 */
	public abstract void setTickToLabelGap(int gap);

	/**
	 Gets gap between tick and its label.
	 @return Gap value.
	 */
	public abstract int getTickToLabelGap();

	/**
	 Returns true if plot key is to be shown.
	 @return True if visible.
	 */
	public abstract boolean isKeyVisible();

	/**
	 Determines whether key is visible.
	 @param b True if visible.
	 */
	public abstract void setKeyVisible(boolean b);

	/**
	 Set key font.
	 @param font Key font.
	 */
	public abstract void setKeyFont(Font font);

	/**
	 Get key font.
	 @return Key font.
	 */
	public abstract Font getKeyFont();

	/**
	 Set Key border-line colour.
	 @param color Color object.
	 */
	public abstract void setKeyBorderColor(Color color);

	/**
	 Get Key border-line colour.
	 @return Color object.
	 */
	public abstract Color getKeyBorderColor();

	/**
	 Sets gap between key text and enclosing border.
	 @param margin Margin size.
	 */
	public abstract void setKeyMargin(int margin);

	/**
	 Gets gap between key text and enclosing border.
	 @return Margin size.
	 */
	public abstract int getKeyMargin();

	/**
	 Sets width of border line surrounding key.
	 @param width Border width.
	 */
	public abstract void setKeyBorderWidth(float width);

	/**
	 Gets width of border line surrounding key.
	 @return Border width.
	 */
	public abstract float getKeyBorderWidth();

	/**
	 Sets key background colour.
	 @param color Background colour.
	 */
	public abstract void setKeyBackground(Color color);

	/**
	 Gets key background colour.
	 @return Background colour.
	 */
	public abstract Color getKeyBackground();

	/**
	 Sets key foreground colour.
	 @param color Foreground colour.
	 */
	public abstract void setKeyForeground(Color color);

	/**
	 Gets key foreground colour.
	 @return Foreground colour.
	 */
	public abstract Color getKeyForeground();

	/**
	 If true selected background colour of key is visible.  Otherwise, the
	 background colour of the underlying component is seen.
	 @param b True if opaque.
	 */
	public abstract void setKeyOpaque(boolean b);

	/**
	 Returns true if key background is painted.
	 @return True if opaque.
	 */
	public abstract boolean isKeyOpaque();

	/**
	 Determines whether the border around the key is visible.
	 @param b True if border is visible.
	 */
	public abstract void setKeyBorderVisible(boolean b);

	/**
	 Returns true if key border is visible.
	 @return True if visible.
	 */
	public abstract boolean isKeyBorderVisible();

	/**
	 Determines whether key is to take up its default location on the control.
	 If false, key location is determined by the KeyLocation property.
	 @param b True if default is to be used.
	 */
	public abstract void setKeyDefaultLocation(boolean b);

	/**
	 Returns true if key is to adopt its default location on the control.
	 If false, key location is determined by the KeyLocation property.
	 @return True if default.
	 */
	public abstract boolean isKeyDefaultLocation();

	/**
	 Sets location of key within component.  Ignored if KeyDefaultLocation
	 property is set to true.
	 @param location Key location.
	 */
	public abstract void setKeyLocation(Point location);

	/**
	 Gets location of key within component.  Note that this information is
	 ignored if KeyDefaultLocation property is set to true.
	 @return Key location.
	 */
	public abstract Point getKeyLocation();

	//##########################################################################
	
    } // End of interface.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////