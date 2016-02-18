// _XYPlot.java created on 03 February 2004 at 10:36

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
import java.awt.event.*;
import java.awt.font.*;
import java.awt.image.*;
import java.awt.print.*;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 Swing component for plotting line graphs.
 @author  Kevin Ashelford.
 */
class _XYPlot extends Plot {
	
	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS
	
	private IGraphModel						_graphModel;
	private ICoordinateCollectionRenderer	_renderer;
	private IAxisRenderer					_axisRenderer;
	private IAxis							_xAxis;
	private IAxis							_yAxis;
	private IAxisFormatter					_xAxisFormatter;
	private IAxisFormatter					_yAxisFormatter;
	private int								_leftMargin;
	private int								_rightMargin;
	private int								_topMargin;
	private int								_bottomMargin;
	private int								_xAxisLabelToBorderGap;
	private int								_yAxisLabelToBorderGap;
	private int								_tickToLabelGap;
	private boolean							_keyVisible;
	private boolean							_keyBorderVisible;
	private Font							_keyFont;
	private Color							_keyBorderColor;
	private int								_keyMargin;
	private float							_keyBorderWidth;
	private Color							_keyBackground;
	private Color							_keyForeground;
	private boolean							_keyOpaque;
	private Point							_keyLocation;
	private Dimension						_keyDimension;
	private boolean							_keyDefaultLocation;
	private boolean							_antiAliasing;
	
	//##########################################################################
	
	// CONSTRUCTOR
	
	/**
	 Creates a new instance of _XYPlot.
	 */
	public _XYPlot() {

		this(

			AxisFactory.createAxis(
				"X Axis", 
				AxisType.X, 
				0f, 
				10f, 
				1, 
				1, 
				ScaleType.Linear
				),

			AxisFactory.createAxis(
				"Y Axis", 
				AxisType.Y, 
				0f, 
				10f, 
				1, 
				1, 
				ScaleType.Linear
				)

			);
		
		} // End of constructor.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Creates a new instance of _XYPlot.
	 @param xAxis X IAxis object.
	 @param yAxis Y IAxis object.
	 */
	public _XYPlot(IAxis xAxis, IAxis yAxis) {
		
		_xAxis = xAxis;
		_yAxis = yAxis;
		
		_graphModel = GraphModelFactory.createGraphModel();
		_axisRenderer = AxisRendererFactory.createAxisRenderer();
		_renderer = 
			CoordinateCollectionRendererFactory.
				createCoordinateCollectionRenderer(null);

		// Default settings - may be overridden
		_leftMargin				= 80;
		_rightMargin			= 50;
		_topMargin				= 50;
		_bottomMargin			= 70;
		_tickToLabelGap			= 4;
		_xAxisLabelToBorderGap	= 20;
		_yAxisLabelToBorderGap	= 20;
		_keyVisible				= false;
		_keyBorderVisible		= false;
		_keyFont				= new Font("Dialog", Font.PLAIN, 14);
		_keyBorderColor			= Color.BLACK;
		_keyMargin				= 5;
		_keyBorderWidth			= 1;
		_keyBackground			= Color.WHITE;
		_keyForeground			= Color.BLACK;
		_keyOpaque				= true;
		_keyLocation			= new Point(100, 10);
		_keyDimension			= new Dimension(0, 0);
		_keyDefaultLocation		= false;
		_antiAliasing			= false;
		
		// For moving key.
		GraphMouseListener l= new GraphMouseListener();
		addMouseListener(l);
		addMouseMotionListener(l);
		
		} // End of constructor.
	
	//##########################################################################
	
	// PROPERTY ACCESSOR METHODS
	
	/**
	 Switches on/off anti-aliasing.
	 @param b True if anti-aliasing is to be turned on.
	 */
	public void setAntiAliasing(boolean b) {
		_antiAliasing = b;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Returns true if anti-aliasing is turned on.
	 @return True if on.
	 */
	public boolean isAntiAliasing() {
		return _antiAliasing;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Sets gap between axis label and component border.
	 @param gap Gap value.
	 */
	public void setXAxisLabelToBorderGap(int gap) {
		_xAxisLabelToBorderGap = gap;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Gets gap between axis label and component border.
	 @return Gap value.
	 */
	public int getXAxisLAbelToBorderGap() {
		return _xAxisLabelToBorderGap;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Sets gap between axis label and component border.
	 @param gap Gap value.
	 */
	public void setYAxisLabelToBorderGap(int gap) {
		_yAxisLabelToBorderGap = gap;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Gets gap between axis label and component border.
	 @return Gap value.
	 */
	public int getYAxisLAbelToBorderGap() {
		return _yAxisLabelToBorderGap;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Gets the current IGraphModel object.
	 @return IGraphModel object.
	 */
	public IGraphModel getDataModel() {
		return _graphModel;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Sets the renderer for ICoordinateCollection objects associated with the
	 IGraphModel.
	 @param renderer ICoordinateCollection object.
	 */
	public void setDataRenderer(
		IDataRenderer renderer
		) {	
		_renderer = (ICoordinateCollectionRenderer)renderer;
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Gets the ICoordinateCollectionRenderer object.
	 @return ICoordinateCollectionRenderer object.
	 */
	public ICoordinateCollectionRenderer getDataRenderer() {
		return _renderer;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Sets the IAxisRenderer object.
	 @param renderer IAxisRenderer object.
	 */
	public void setAxisRenderer(IAxisRenderer renderer) {
		_axisRenderer = renderer;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Gets the IAxisRenderer object.
	 @return IAxisRenderer object.
	 */
	public IAxisRenderer getAxisRenderer() {
		return _axisRenderer;
		} //End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Sets the X IAxis object registered with _XYPlot.
	 @param xAxis X IAxis object.
	 */
	public void setXAxis(IAxis xAxis) {
		_xAxis = xAxis;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Gets the X IAxis object registered with _XYPlot.
	 @return X IAxis object.
	 */
	public IAxis getXAxis() {
		return _xAxis;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Sets the Y IAxis object registered with _XYPlot.
	 @param yAxis Y IAxis object.
	 */
	public void setYAxis(IAxis yAxis) {
		_yAxis = yAxis;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Gets the Y IAxis object registered with _XYPlot.
	 @return Y IAxis object.
	 */
	public IAxis getYAxis() {
		return _yAxis;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Sets margin to the left of plot.
	 @param margin Size of margin.
	 */
	public void setLeftMargin(int margin) {
		_leftMargin = margin;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Gets margin to the left of plot.
	 @return Size of margin.
	 */
	public int getLeftMargin() {
		return _leftMargin;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Sets margin to the right of plot.
	 @param margin Size of margin.
	 */
	public void setRightMargin(int margin) {
		_rightMargin = margin;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Gets margin to the right of plot.
	 @return Size of margin.
	 */
	public int getRightMargin() {
		return _rightMargin;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Sets margin at the top of the plot.
	 @param margin Size of margin.
	 */
	public void setTopMargin(int margin) {
		_topMargin = margin;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Gets margin at the top of the plot.
	 @return Size of margin.
	 */
	public int getTopMargin() {
		return _topMargin;
		} // End of property.
	
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Sets margin at the bottom of the plot.
	 @param margin Size of margin.
	 */
	public void setBottomMargin(int margin) {
		_bottomMargin = margin;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Gets margin at the bottom of the plot.
	 @return Size of margin.
	 */
	public int getBottomMargin() {
		return _bottomMargin;
		} // End of property.
	
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Sets gap between tick and its label.
	 @param gap Gap value.
	 */
	public void setTickToLabelGap(int gap) {
		_tickToLabelGap = gap;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Gets gap between tick and its label.
	 @return Gap value.
	 */
	public int getTickToLabelGap() {
		return _tickToLabelGap;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Returns true if plot key is to be shown.
	 @return True if visible.
	 */
	public boolean isKeyVisible() {
		return _keyVisible;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Determines whether key is visible.
	 @param b True if visible.
	 */
	public void setKeyVisible(boolean b) {
		_keyVisible = b;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Set key font.
	 @param font Key font.
	 */
	public void setKeyFont(Font font) {
		_keyFont = font;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Get key font.
	 @return Key font.
	 */
	public Font getKeyFont() {
		return _keyFont;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Set Key border-line colour.
	 @param color Color object.
	 */
	public void setKeyBorderColor(Color color) {
		_keyBorderColor = color;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Get Key border-line colour.
	 @return Color object.
	 */
	public Color getKeyBorderColor() {
		return _keyBorderColor;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Sets gap between key text and enclosing border.
	 @param margin Margin size.
	 */
	public void setKeyMargin(int margin) {
		_keyMargin = margin;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Gets gap between key text and enclosing border.
	 @return Margin size.
	 */
	public int getKeyMargin() {
		return _keyMargin;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Sets width of border line surrounding key.
	 @param width Border width.
	 */
	public void setKeyBorderWidth(float width) {
		_keyBorderWidth = width;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Gets width of border line surrounding key.
	 @return Border width.
	 */
	public float getKeyBorderWidth() {
		return _keyBorderWidth;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Sets key background colour.
	 @param color Background colour.
	 */
	public void setKeyBackground(Color color) {
		_keyBackground = color;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Gets key background colour.
	 @return Background colour.
	 */
	public Color getKeyBackground() {
		return _keyBackground;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Sets key foreground colour.
	 @param color Foreground colour.
	 */
	public void setKeyForeground(Color color) {
		_keyForeground = color;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Gets key foreground colour.
	 @return Foreground colour.
	 */
	public Color getKeyForeground() {
		return _keyForeground;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 If true selected background colour of key is visible.  Otherwise, the 
	 background colour of the underlying component is seen.
	 @param b True if opaque.
	 */
	public void setKeyOpaque(boolean b) {
		_keyOpaque = b;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Returns true if key background is painted.
	 @return True if opaque.
	 */
	public boolean isKeyOpaque() {
		return _keyOpaque;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Determines whether the border around the key is visible.
	 @param b True if border is visible.
	 */
	public void setKeyBorderVisible(boolean b) {
		_keyBorderVisible = b;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Returns true if key border is visible.
	 @return True if visible.
	 */
	public boolean isKeyBorderVisible() {
		return _keyBorderVisible;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Determines whether key is to take up its default location on the control. 
	 If false, key location is determined by the KeyLocation property.
	 @param b True if default is to be used.
	 */
	public void setKeyDefaultLocation(boolean b) {
		_keyDefaultLocation = b;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Returns true if key is to adopt its default location on the control.
	 If false, key location is determined by the KeyLocation property.
	 @return True if default.
	 */
	public boolean isKeyDefaultLocation() {
		return _keyDefaultLocation;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Sets location of key within component.  Ignored if KeyDefaultLocation 
	 property is set to true.
	 @param location Key location.
	 */
	public void setKeyLocation(Point location) {
		_keyLocation = location;
		} // End of property.
	
	////////////////////////////////////////////////////////////////////////////

	/**
	 Gets location of key within component.  Note that this information is 
	 ignored if KeyDefaultLocation property is set to true.
	 @return Key location.
	 */
	public Point getKeyLocation() {
		return _keyLocation; 
		} // End of property.
	
	//##########################################################################
	
	// METHODS
	
	/**
	 Clears graph of any plot lines.
	 */
	public void clear() {
		_graphModel.setCoordinateCollections(null);
		plot();
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Triggers component to draw plot.
	 */
	public void plot() {
		repaint();
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Override method to draw plot on component.
	 @param g Graphics object.
	 */
	protected void paintComponent(Graphics g) {
		
		// Must invoke base class method first.
		super.paintComponent(g);
		
		// Cast as Java 2D API Graphics object.
		Graphics2D g2 = (Graphics2D)g;
		
		// White background.
		g2.setPaint(Color.WHITE);
		g2.fillRect(0, 0, getWidth(), getHeight());

		// Pass to private method for generating plot (if data exists).
		if (_graphModel.hasData()) {
			doPage(g2);
			}
		
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Returns a RenderedImage object representing the graph.  From this, client
	 code can generate png or jpeg files using the ImageIO.write() method.
	 @return RenderedImage object.
	 */
	public RenderedImage createImage() {
		
		BufferedImage image = 
			new BufferedImage(
				getWidth(), 
				getHeight(), 
				BufferedImage.TYPE_INT_RGB
				);

		// Obtain graphics object associated with image object.
		Graphics2D g2 = (Graphics2D)image.getGraphics();

		// Pass to private method for generating plot.
		doPage(g2);

		// With plot generated, ok to return image object.
		return image;
		
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 Prints graph to printer.  As used by an instance of DocPrintJob.
	 @param g Graphics object.
	 @param format PageFormat object.
	 @param pageNumber Number of pages.
	 @return Printable tag.
	 */
	public int print(Graphics g, PageFormat format, int pageNumber) {
		
		// Notifies listening class whether or not page number is a legal number.
		// In this instance, should print only one page. 
		if (pageNumber > 0) {return Printable.NO_SUCH_PAGE;}
		
		// Cast as Java 2D API Graphics object.
		Graphics2D g2 = (Graphics2D)g;
		
		// Translate to accommodate the requested top and left margins.
		g2.translate(format.getImageableX(), format.getImageableY());
		
		// Ensure figure fits on page.
		Dimension size = this.getSize();
		double pageWidth = format.getImageableWidth(); 
		double pageHeight = format.getImageableHeight();
		
		// If the component is too wide or tall for the page, scale it down.
		if (size.width > pageWidth) {
			// Calculate how much to scale by.
			double factor = pageWidth/size.width;
			// Adjust coordinate system.
			g2.scale(factor, factor);
			// Adjust page size up.
			pageWidth /= factor;
			pageHeight /= factor;
			}
		
		if (size.height > pageHeight) {
			double factor = pageHeight/size.height;
			g2.scale(factor, factor);
			pageWidth /= factor;
			pageHeight /= factor;
			}
		
		// Now centre image on page.
		g2.translate((pageWidth - size.width)/2, (pageHeight - size.height)/2);
		
		// Give it a title.
		//g2.drawString("Test title", 0, -15);
		
		// Pass to method for generating plot.
		doPage(g2);
		
		return Printable.PAGE_EXISTS;
		
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////

	// Draws graph with supploed graphics object.
	private void doPage(Graphics2D g) {
		
		// Switch on if requested.
		if (_antiAliasing) {
			g.setRenderingHint(
				RenderingHints.KEY_ANTIALIASING,  
                RenderingHints.VALUE_ANTIALIAS_ON
				);
			}
		
		// White background.
		g.setPaint(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		// Plot graph axis.
		drawAxis(g);
	
		// Do not proceed further if IGraphModel is empty.
		if (_graphModel.getCoordinateCollections() == null) {return;} 
		
		
		// Data for plot-line.
		ICoordinateCollection coordinateCollection;
		// Stores data points.
		Coordinate previousCoordinate = null;
		Coordinate currentCoordinate = null;
		// Defines appearance of line.
		IPlotLineFormatter plotLineFormatter;
		
		// Determine number of lines to plot.
		int numberOfPlotLines = _graphModel.getCoordinateCollectionCount();

	

		
		// Work through each line in turn.
		for (int i = 0; i < numberOfPlotLines; i++) {
			
			// Get data for current plot-line.
			coordinateCollection = _graphModel.getCoordinateCollection(i);
			
			// Get the Plot-line formatting object for current plot-line data.
			plotLineFormatter = _renderer.getPlotLineFormatter(
				coordinateCollection
				);
			
			// Special case if only one coordinate.
			if (coordinateCollection.getSize() == 1) {
				
				Coordinate c = coordinateCollection.getCoordinate(0);
				
				String s = coordinateCollection.getTitle();
				
				// Duplicate coordinate so that following code works correctly.
				coordinateCollection = 
					CoordinateCollectionFactory.
						createCoordinateCollection(
							new Coordinate[]{c, c},
							s
							);
				
				}
			
			// Work through each data point of plot-line.
			previousCoordinate = null;
			for (int j = 0; j < coordinateCollection.getSize(); j++) {

				// TODO - remove debugging code
				//if (j > 500) {continue;}

				// Get new coordinate.
				currentCoordinate = coordinateCollection.getCoordinate(j);



				// Draw line between this and previous coordinate.
				drawLineAndSymbols(
					previousCoordinate, 
					currentCoordinate, 
					plotLineFormatter,
					g
					);
				
				// Store coordinate.
				previousCoordinate = currentCoordinate;
				
				} // End of for loop - no more coordinates for plot-line.
			
			} // End of for loop - no plot-lines remaining.
		
		
		displayKey(g);
			
		
		} // End of method.

	////////////////////////////////////////////////////////////////////////////
	
	private void displayKey(Graphics2D g) {
		
		FontRenderContext frc = g.getFontRenderContext();
		
		// Do nothing if key is not visible.
		//==================================
		if (!_keyVisible) {return;}
		
		
		// Determine width and height of key.
		//======================================================================
		
		// Determine font height.
		LineMetrics metrics = _keyFont.getLineMetrics("Test", frc);
		int fontHeight = Math.round(metrics.getHeight());
		int fontDescent = Math.round(metrics.getDescent());
		int fontAscent = Math.round(metrics.getAscent());
		
		// Determine max string length.
		int maxStringLength = 0; 
		for (int i = 0; i < _graphModel.getCoordinateCollectionCount(); i++) {
			
			double length = _keyFont.getStringBounds(
				_graphModel.getCoordinateCollectionTitle(i), 
				frc
				).getWidth();
			
			if ((int)length > maxStringLength) {maxStringLength = (int)length;}
			}
		
		// Determine length of key image.
		int imageLength = 40;
		int imageToTextGap = 5;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		// Calculate key width.
		int width = 
			_keyMargin +		// Left margin. 
			imageLength +		// Key image.
			imageToTextGap +	// Short spacer.
			maxStringLength +	// Text.
			_keyMargin;			// Right margin.
		
		// Calculate height.
		int heightMargin = _keyMargin - fontDescent; // Correct for descent.
		int height = 
			heightMargin * 2 +  // Top and bottom margins.
			fontHeight * _graphModel.getCoordinateCollectionCount(); // text height.
		
		//======================================================================
		
		
		// Determine x and y coordinates for key.
		//=======================================
		int x;
		int y;
		if (_keyDefaultLocation) {
			x = getWidth() / 2 - width/2;
			y = 10;
			}
		else {
			x = (int)_keyLocation.getX();
			y = (int)_keyLocation.getY();
			}
		
		// Background?
		//=============
		if (_keyOpaque) {
			g.setPaint(_keyBackground);
			g.fillRect(x, y, width, height);
			} 
		
		// Border?
		//========
		if (_keyBorderVisible) {
			g.setPaint(_keyBorderColor);
			g.setStroke(new BasicStroke(_keyBorderWidth));
			g.drawRect(x, y, width, height);
			}
		
		// Draw image and text.
		//====================
		
		int currentY = y + heightMargin + fontAscent;
		int currentX = x + _keyMargin;
		for (int i = 0; i < _graphModel.getCoordinateCollectionCount(); i++) {
			
			// Get current plot data.
			ICoordinateCollection cc = _graphModel.getCoordinateCollection(i);
			
			// Get associated line formatter.
			IPlotLineFormatter plf = _renderer.getPlotLineFormatter(cc);
			
			// Draw illustrative line if required.
			if (plf.getPlotStyle() == PlotStyle.Line || 
				plf.getPlotStyle() == PlotStyle.LineAndScatter) {
				
				drawLine(
					g, plf, 
					currentX, 
					currentY - fontAscent/2 + fontDescent, 
					currentX + imageLength, 
					currentY - fontAscent/2 + fontDescent
					);
				
				}
			
			// Draw illustrative symbol if required.
			if (plf.getPlotStyle() == PlotStyle.LineAndScatter ||
				plf.getPlotStyle() == PlotStyle.Scatter) {
				
				drawSymbol(
					g, plf, 
					currentX + imageLength/2, 
					currentY - fontAscent/2 + fontDescent
					);	
					
				}
			
			
			// Write text.
			g.setPaint(_keyForeground);
			g.setFont(_keyFont);
			g.drawString(
				_graphModel.getCoordinateCollectionTitle(i), 
				currentX + imageLength + imageToTextGap, 
				currentY
				);
			
			currentY = currentY + fontHeight;
			}
		
		// Lastly, record new key dimension for mouse etc.
		_keyDimension.setSize(width, height);
	
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	private void drawLineAndSymbols(
		Coordinate first,
		Coordinate second,
		IPlotLineFormatter plf,
		Graphics2D g
		) {
		
		if (first == null) {return;} // No first as yet.	

		// Calculate some values:
		//============================================
		double xOrigin = _leftMargin + convert(_xAxisFormatter.getStartMargin(), AxisType.X);
		double yOrigin = getHeight() - (_bottomMargin + convert(_yAxisFormatter.getStartMargin(), AxisType.Y));
		
		double trueXOrigin = xOrigin - (_xAxis.getFrom() * convert(_xAxis.getIncrement(), AxisType.X) / _xAxis.getIncrement() );
		double trueYOrigin = yOrigin + (_yAxis.getFrom() * convert(_yAxis.getIncrement(), AxisType.Y) / _yAxis.getIncrement() );
	
		double firstX = first.getX();
		double firstY = first.getY();
		double secondX = second.getX();
		double secondY = second.getY();

		// Prevent drawing over axes =============================

		// Don't draw if both data points are outside of x axis (ignore y axis for now...).
		// TODO - support for y axis also?
		if (firstX < _xAxis.getFrom() && secondX < _xAxis.getFrom()) {
			//System.out.println("x1=" + firstX + ", x2" + secondX);
			return;
			}
		if (firstX > _xAxis.getTo() && secondX > _xAxis.getTo()) {
			//System.out.println("x1=" + firstX + ", x2" + secondX);
			return;
			}

		if (firstX < _xAxis.getFrom())	firstX = _xAxis.getFrom();
		if (firstX > _xAxis.getTo())	firstX = _xAxis.getTo();
		if (firstY < _yAxis.getFrom())	firstY = _yAxis.getFrom();
		if (firstY > _yAxis.getTo())	firstY = _yAxis.getTo();

		if (secondX < _xAxis.getFrom())	secondX = _xAxis.getFrom();
		if (secondX > _xAxis.getTo())	secondX = _xAxis.getTo();
		if (secondY < _yAxis.getFrom())	secondY = _yAxis.getFrom();
		if (secondY > _yAxis.getTo())	secondY = _yAxis.getTo();
		//========================================================

		double x1 = trueXOrigin + convert(firstX, AxisType.X);
		double x2 = trueXOrigin + convert(secondX, AxisType.X);
		double y1 = trueYOrigin - convert(firstY, AxisType.Y);
		double y2 = trueYOrigin - convert(secondY, AxisType.Y);
		//============================================

		
		// Draw line if requested.
		if (plf.getPlotStyle() == PlotStyle.Line || plf.getPlotStyle() == PlotStyle.LineAndScatter) {
			
			drawLine(
				g, 
				plf, 
				(int)Math.round(x1),
				(int)Math.round(y1),
				(int)Math.round(x2),
				(int)Math.round(y2)
				);
		
			}
		
		// Draw Symbols if requested.
		if (plf.getPlotStyle() == PlotStyle.Scatter || plf.getPlotStyle() == PlotStyle.LineAndScatter) {
			
			drawSymbol(
				g, 
				plf, 
				(int)Math.round(x1),
				(int)Math.round(y1)
				);
			
			drawSymbol(
				g, 
				plf, 
				(int)Math.round(x2),
				(int)Math.round(y2)
				);
			}
		
			
		
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////

	private void drawLine(
		final Graphics2D g,
		final IPlotLineFormatter plf,
		final int x1,
		final int y1,
		final int x2,
		final int y2
		) {
		
		g.setColor(plf.getLineColor());
		g.setStroke(
			getStroke(
				plf.getLineStyle(), 
				plf.getLineWidth()
				)
			);
		
		g.drawLine(
			Math.round(x1), 
			Math.round(y1), 
			Math.round(x2), 
			Math.round(y2)
			);
		
	
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	private void drawSymbol(
		final Graphics2D g,
		final IPlotLineFormatter plf,
		final int x,
		final int y
		) {
		
		int w = plf.getSymbolSize();
		Color c = plf.getSymbolColor();
			
		int x1 = x - Math.round((float)w/2f);	
		int y1 = y - Math.round((float)w/2f);	

		
		// Closed circle.
		if (plf.getSymbol() == Symbol.ClosedCircle) {
			
			// Include gap if requested.
			if (plf.isGapToSymbol() && plf.getPlotStyle() == PlotStyle.LineAndScatter) {
				g.setColor(Color.WHITE);
				g.fillOval(x1-2, y1-2, w+4, w+4);
				}
			
			g.setColor(c);
			g.fillOval(x1, y1, w, w);
			}
		
		// Closed square.
		else if (plf.getSymbol() == Symbol.ClosedSquare) {
			
			// Include gap if requested.
			if (plf.isGapToSymbol() && plf.getPlotStyle() == PlotStyle.LineAndScatter) {
				g.setColor(Color.WHITE);
				g.fillRect(x1-2, y1-2, w+4, w+4);
				}
			
			g.setColor(c);
			g.fillRect(x1, y1, w, w);
			}
		
		// Open circle.
		else if (plf.getSymbol() == Symbol.OpenCircle) {
			
			// Include gap if requested.
			if (plf.isGapToSymbol() && plf.getPlotStyle() == PlotStyle.LineAndScatter) {
				g.setColor(Color.WHITE);
				g.fillOval(x1-3, y1-3, w+6, w+6);
				}
			
			g.setColor(Color.WHITE);
			g.fillOval(x1, y1, w, w);
			g.setColor(c);
			g.drawOval(x1, y1, w, w);
			}
		
		// Open square.
		else if (plf.getSymbol() == Symbol.OpenSquare) {
			
			// Include gap if requested.
			if (plf.isGapToSymbol() && plf.getPlotStyle() == PlotStyle.LineAndScatter) {
				g.setColor(Color.WHITE);
				g.fillRect(x1-3, y1-3, w+6, w+6);
				}
			
			g.setColor(Color.WHITE);
			g.fillRect(x1, y1, w, w);
			g.setColor(c);
			g.drawRect(x1, y1, w, w);
			}
		
		// Not supported.
		else {
			throw new IllegalArgumentException(
				"Symbol '" + 
				plf.getSymbol().toString() +
				"' is not currently supported"
				);
			}
		
		
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	private void drawAxis(Graphics2D g) {
		
		// Get Formatters.
		_xAxisFormatter = _axisRenderer.getAxisFormatter(_xAxis);
		_yAxisFormatter = _axisRenderer.getAxisFormatter(_yAxis);
		
		// X IAxis:
		//========
		
		// Draw ticks, grids and labels.
		drawXAxisArchitecture(
			g, 
			(float)_leftMargin, 
			(float)getHeight() - (float)_bottomMargin,
			(float)getWidth() - (float)(_leftMargin + _rightMargin)
			);
		
		// Draw axis label.
		drawXAxisLabel(
			g, 
			(float)getHeight() - (float)_bottomMargin,
			(float)getWidth() - (float)(_leftMargin + _rightMargin)
			); 
		
		// Y IAxis:
		//========
		
		// Draw ticks, grid lines and labels.
		drawYAxisArchitecture(
			g, 
			(float)_leftMargin, // X origin.
			(float)getHeight() - (float)_bottomMargin, // Y origin.
			(float)getHeight() - (float)(_topMargin + _bottomMargin) // IAxis length
			);
		
		// Draw axis label.
		drawYAxisLabel(
			g, 
			(float)getHeight() - (float)(_topMargin + _bottomMargin) // IAxis length
			);
		
		
		
			
		//DRAW LINES LAST:
		//================
		
		// Set colour of axis.
		g.setPaint(_yAxisFormatter.getLineColor());

		// Set line width.
		g.setStroke(getStroke(LineStyle.Solid, _yAxisFormatter.getLineWidth()));
		
		// Draw y axis.
		g.drawLine(
			_leftMargin, 
			_topMargin, 
			_leftMargin, 
			getHeight() - _bottomMargin
			);	

		// Set colour of axis.
		g.setPaint(_xAxisFormatter.getLineColor());

		// Set line width.
		g.setStroke(getStroke(LineStyle.Solid, _xAxisFormatter.getLineWidth()));

		// Draw x axis.
		g.drawLine(
			_leftMargin, 
			getHeight() - _bottomMargin, 
			getWidth() - _rightMargin, 
			getHeight() - _bottomMargin
			);
		
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
/*
	private int convert(int in, AxisType axisType) {
		return (int)convert((float)in, axisType);
		} // End of method.
*/
	////////////////////////////////////////////////////////////////////////////
	
	// Converts axis distances to screen distances
	private double convert(double in, AxisType axisType) {
		
		double axisLength; // Actual length of axis on screen.
		double range; // Range of values that the axis will display.
		
		// X axis:
		//===============================================================
		if (axisType == AxisType.X) {
			
			axisLength = getWidth() - (_leftMargin + _rightMargin);
			
			range = 
				(_xAxis.getTo() + _xAxisFormatter.getEndMargin()) - 
				(_xAxis.getFrom() - _xAxisFormatter.getStartMargin());	
			
			}
		
		// Y IAxis:
		//===============================================================
		else if (axisType == AxisType.Y) {
			
			axisLength = getHeight() - (_topMargin + _bottomMargin);



			range = 
				(_yAxis.getTo() + _yAxisFormatter.getEndMargin()) - 
				(_yAxis.getFrom() - _yAxisFormatter.getStartMargin());


			//System.out.println("axisLength=" + axisLength + "/ range=" + range + " * in=" + in);

			}
		
		//===============================================================
		
		else {
			// IAxis type currently unsupported.
			throw new IllegalArgumentException(
				"AxisType '" + axisType.toString() + "' not currently supported."
				);
			}


		double out =  axisLength / range * in;

		//System.out.println("out=" + out);

		return out;

		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	// Converts float to string with specified number of decimal points.
	private String tickLabelToString(double label, AxisType axisType) {
		
		int decimalPlaces; 
		
		if (axisType == AxisType.X) {
			decimalPlaces = 
				_xAxisFormatter.getMajorTickLabelFormatter().getDecimalPlaces();
			
			
			}
		else if (axisType == AxisType.Y) {
			decimalPlaces = 
				_yAxisFormatter.getMajorTickLabelFormatter().getDecimalPlaces();
			}
		
		else {
			throw new IllegalArgumentException(
				"AxisType '" + axisType.toString() + "' not currently supported."
				);
			}

		//decimalPlaces = 1;

		java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
		nf.setMaximumFractionDigits(decimalPlaces);
		nf.setMinimumFractionDigits(decimalPlaces);
		
		return nf.format(label);
		
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	private void drawYAxisArchitecture(
		Graphics2D g, 
		float xStart,
		float yStart,
		float axisLength
		) {	
		
		boolean showMajorTicks = true;	
			
		// Calculate some values.
		//==========================================================	
			
		// Calculate length of margins in terms of screen size.
		double startMargin = convert(_yAxisFormatter.getStartMargin(), AxisType.Y);
		double endMargin = convert(_yAxisFormatter.getEndMargin(), AxisType.Y);

		// Calculate increment length in terms of screen size.
		double increment = convert(_yAxis.getIncrement(), AxisType.Y);
		
		double minorIncrement = increment / ((float)_yAxis.getMinorTicks() + 1f);
		
		double tickYPosition = yStart - startMargin;
		double yFinish = yStart - (axisLength - endMargin);
		//===========================================================
		
		// DRAW TICKS:
		//============
		
		// Decide on major tick position relative to axis, and length.
	    //=========================================================
			
		// Default - assumes ticks are out.
		float tickXPosition1 = xStart;
		float tickXPosition2 = xStart - _yAxisFormatter.getMajorTickLength();
		
		// If ticks are in:
		if (_yAxisFormatter.getMajorTickLocation() == TickLocation.In) {
			tickXPosition2 = xStart + _yAxisFormatter.getMajorTickLength();
			}
		
		// If ticks are both in and out:
		if (_yAxisFormatter.getMajorTickLocation() == TickLocation.InAndOut) {
			tickXPosition1 = xStart + _yAxisFormatter.getMajorTickLength();
			}
		
		// If ticks are neither in nor out:
		if (_yAxisFormatter.getMajorTickLocation() == TickLocation.None) {
			showMajorTicks = false;
			}	
		//=========================================================	
		
		double newTickYPosition;
		float scaleValue = _yAxis.getFrom();
		
		FontRenderContext frc = g.getFontRenderContext();
		
		if (showMajorTicks) {
			
			Font font = _yAxisFormatter.getMajorTickLabelFormatter().getFont();
			
			
			// Draw major ticks
			//================================================
			newTickYPosition = tickYPosition;
			while (Math.round(newTickYPosition) >= Math.round(yFinish)) {  

				g.setPaint(_yAxisFormatter.getLineColor());
				g.setStroke(new BasicStroke(_yAxisFormatter.getLineWidth()));
				
				// Draw tick.
				g.drawLine(
					(int)Math.round(tickXPosition1),
					(int)Math.round(newTickYPosition),
					(int)Math.round(tickXPosition2),
					(int)Math.round(newTickYPosition)
					);
				
				// Draw grid line if required.
				if (_yAxisFormatter.isMajorGridLinesVisible()) {
					
					g.setPaint(_yAxisFormatter.getMajorGridLineFormatter().getLineColor());
					
					g.setStroke(
						getStroke(
							_yAxisFormatter.getMajorGridLineFormatter().getLineStyle(), 
							_yAxisFormatter.getMajorGridLineFormatter().getLineWidth()
							)
						);
	
					// Draw gridlines.
					g.drawLine(
						(int)Math.round(xStart),
						(int)Math.round(newTickYPosition),
						getWidth() - _rightMargin, 
						(int)Math.round(newTickYPosition)
						);
					
					} // End of if block - grid line drawn.
				
				// Draw label (if requested).
				if (_yAxisFormatter.getMajorTickLabelFormatter().isVisible()) {
					
					g.setFont(font);
					g.setPaint(
						_yAxisFormatter.getMajorTickLabelFormatter().getColor()
						);

					String label = tickLabelToString(scaleValue, AxisType.Y);

					LineMetrics metrics = font.getLineMetrics(label, frc);
					float fontHeight = metrics.getHeight();
					float stringLength = 
						(float)font.getStringBounds(label, frc).getWidth();

					g.drawString(
						label, 
						xStart - 
							_yAxisFormatter.getMajorTickLength() - 
							_tickToLabelGap - 
							stringLength, 
						(float)(newTickYPosition + fontHeight/4)
						);
					
					} // End of if block - label drawn(?)
				
				newTickYPosition = newTickYPosition - increment;
				scaleValue = scaleValue + _yAxis.getIncrement(); 
				
				}	

			//================================================
			
			}
		
		
		// Decide on minor tick position relative to axis, and length.
	    //=========================================================
			
		// Default - assumes ticks are out.
		float minorTickXPosition1 = xStart;
		float minorTickXPosition2 = xStart - _yAxisFormatter.getMinorTickLength();
		
		// If ticks are in:
		if (_yAxisFormatter.getMinorTickLocation() == TickLocation.In) {
			minorTickXPosition2 = xStart + _yAxisFormatter.getMinorTickLength();
			}
		
		// If ticks are both in and out:
		if (_yAxisFormatter.getMinorTickLocation() == TickLocation.InAndOut) {
			minorTickXPosition1 = xStart + _yAxisFormatter.getMinorTickLength();
			}
		
		// If ticks are neither in nor out:
		if (_yAxisFormatter.getMinorTickLocation() == TickLocation.None) {
			return; // Abort - only major ticks drawn.
			}	
		//=========================================================	
		
		// Draw minor ticks
		//================================================
		newTickYPosition = tickYPosition;
		
		// Records number of ticks to eliminate if necessary.
		int i= _yAxis.getMinorTicks();
		// Only eliminate if major ticks are present on same side.
		if (
			// Not the same ...
			_yAxisFormatter.getMajorTickLocation() != 
				_yAxisFormatter.getMinorTickLocation() && 
				// ...and major tick is not on both sides.
				_yAxisFormatter.getMajorTickLocation() != TickLocation.InAndOut		
				) {
			i++; // ensures 'if' argument, below, will never be true.
			}
		 
		while (Math.round(newTickYPosition) >= Math.round(yFinish)) {  
			
			//---------------------------------------------------------------
			// Occasionally minor ticks are not aligned exactly with major.
			// therefore only draw minor ticks that don't overlap with major.
			//---------------------------------------------------------------
			
			if (i == _yAxis.getMinorTicks()) {
				newTickYPosition = newTickYPosition - minorIncrement;
				i = 0;
				continue;
				}
			i++;
			//---------------------------------------------------------------
			
			g.setPaint(_yAxisFormatter.getLineColor());
			g.setStroke(new BasicStroke(_yAxisFormatter.getLineWidth()));
			
			g.drawLine(
				(int)Math.round(minorTickXPosition1),
				(int)Math.round(newTickYPosition),
				(int)Math.round(minorTickXPosition2),
				(int)Math.round(newTickYPosition)
				);
			
			// Draw grid line if required.
			if (_yAxisFormatter.isMinorGridLinesVisible()) {
					
				g.setPaint(_yAxisFormatter.getMinorGridLineFormatter().getLineColor());

				g.setStroke(
					getStroke(
						_yAxisFormatter.getMinorGridLineFormatter().getLineStyle(), 
						_yAxisFormatter.getMinorGridLineFormatter().getLineWidth()
						)
					);

				// Draw gridlines.
				g.drawLine(
					(int)Math.round(xStart),
					(int)Math.round(newTickYPosition),
					getWidth() - _rightMargin, 
					(int)Math.round(newTickYPosition)
					);

				} // End of if block - grid line drawn.
			
			newTickYPosition = newTickYPosition - minorIncrement;
			}	
		
		//================================================	
			
		
		
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	private void drawXAxisLabel(
		Graphics2D g, 
		float yStart,
		float axisLength
		) {
		
		// Do nothing if title is not to be visible. 
		if (!_xAxisFormatter.getTitleFormatter().isVisible()) {return;}	
			
		Font font = _xAxisFormatter.getTitleFormatter().getFont();
		Color color = _xAxisFormatter.getTitleFormatter().getColor();
		
		FontRenderContext frc = g.getFontRenderContext();
		
		String title = _xAxis.getTitle();
		
		LineMetrics metrics = font.getLineMetrics(title, frc);
		float fontHeight = metrics.getHeight();
		float stringLength = 
			(float)font.getStringBounds(title, frc).getWidth();
		
		
		Font font2 = _xAxisFormatter.getMajorTickLabelFormatter().getFont();
		LineMetrics metrics2 = font2.getLineMetrics("0", frc);
		float tickLabelFontHeight = metrics2.getHeight();
		
		
		float xPosition = _leftMargin + axisLength/2 - stringLength/2;
		float yPosition = getHeight() - _xAxisLabelToBorderGap;
			//yStart + 
			//fontHeight + 
			//_xAxisFormatter.getMajorTickLength() + 
			//_tickToLabelGap + 
			//tickLabelFontHeight/2;
		
		g.setPaint(color);
		g.setFont(font);
			
		g.drawString(
			title, 
			xPosition, 
			yPosition
			);	
			
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	private void drawYAxisLabel(
		Graphics2D g, 
		float axisLength
		) {
		
		// Do nothing if title is not to be visible. 
		if (!_yAxisFormatter.getTitleFormatter().isVisible()) {return;}	
			
		Font font = _yAxisFormatter.getTitleFormatter().getFont();
		Color color = _yAxisFormatter.getTitleFormatter().getColor();
		
		FontRenderContext frc = g.getFontRenderContext();
		
		String title = _yAxis.getTitle();
		
		LineMetrics metrics = font.getLineMetrics(title, frc);
		float fontHeight = metrics.getHeight();
		float stringLength = 
			(float)font.getStringBounds(title, frc).getWidth();
		
		
		Font font2 = _yAxisFormatter.getMajorTickLabelFormatter().getFont();
		float tickLabelStringLength = 
			(float)font2.getStringBounds("000", frc).getWidth();
		
		float xPosition = _yAxisLabelToBorderGap + fontHeight/2;
		float yPosition = _topMargin + axisLength /2 + stringLength/2;
		
		g.setPaint(color);
		g.setFont(font);
		
		// Rotate graphics object.
		g.rotate(Math.toRadians(270));
		
		// Draw string.
		g.drawString(
			title, 
			-yPosition, // y axis (after rotation).
			xPosition  // X axis (after rotation).
			);	
		
		// Rotate graphics object back again!
		g.rotate(Math.toRadians(-270)); 
		
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	private void drawXAxisArchitecture(
		Graphics2D g, 
		double xStart,
		double yStart,
		double axisLength
		) {		
		
		boolean showMajorTicks = true;	
			
		// Calculate some values.
		//===========================================================

		// Calculate length of margins in terms of screen size.
		double startMargin = convert(_xAxisFormatter.getStartMargin(), AxisType.X);
		double endMargin = convert(_xAxisFormatter.getEndMargin(), AxisType.X);

		// Calculate increment length in terms of screen size.
		double increment = convert(_xAxis.getIncrement(), AxisType.X);
		
		double minorIncrement = increment / ((float)_xAxis.getMinorTicks() + 1f);
		
		double tickXPosition = xStart + startMargin;
		double xFinish = xStart + (axisLength - endMargin);
		//===========================================================
		
		// DRAW TICKS:
		//============
		
		// Decide on major tick position relative to axis, and length.
	    //=========================================================
			
		// Default - assumes ticks are out.
		double tickYPosition1 = yStart;
		double tickYPosition2 = yStart + _xAxisFormatter.getMajorTickLength();
		
		// If ticks are in:
		if (_xAxisFormatter.getMajorTickLocation() == TickLocation.In) {
			tickYPosition2 = yStart - _xAxisFormatter.getMajorTickLength();
			}
		
		// If ticks are both in and out:
		if (_xAxisFormatter.getMajorTickLocation() == TickLocation.InAndOut) {
			tickYPosition1 = yStart - _xAxisFormatter.getMajorTickLength();
			}
		
		// If ticks are neither in nor out:
		if (_xAxisFormatter.getMajorTickLocation() == TickLocation.None) {
			showMajorTicks = false;
			}	
		//=========================================================	
		
		double newTickXPosition;
		double scaleValue = _xAxis.getFrom();
		
		FontRenderContext frc = g.getFontRenderContext();
		
		if (showMajorTicks) {
			
			Font font = _xAxisFormatter.getMajorTickLabelFormatter().getFont();
			
			// Draw major ticks
			//================================================
			newTickXPosition = tickXPosition;
			while (Math.round(newTickXPosition) <= Math.round(xFinish)) { 

				g.setPaint(_xAxisFormatter.getLineColor());
				g.setStroke(new BasicStroke(_xAxisFormatter.getLineWidth()));
				
				// Draw tick.
				g.drawLine(
					(int)Math.round(newTickXPosition),
					(int)Math.round(tickYPosition1),
					(int)Math.round(newTickXPosition),
					(int)Math.round(tickYPosition2)
					);
				
				// Draw grid line if required.
				if (_xAxisFormatter.isMajorGridLinesVisible()) {
					
					g.setPaint(_xAxisFormatter.getMajorGridLineFormatter().getLineColor());
					
					g.setStroke(
						getStroke(
							_xAxisFormatter.getMajorGridLineFormatter().getLineStyle(), 
							_xAxisFormatter.getMajorGridLineFormatter().getLineWidth()
							)
						);
	
					// Draw gridlines.
					g.drawLine(
						(int)Math.round(newTickXPosition),
						(int)Math.round(yStart),
						(int)Math.round(newTickXPosition),
						_topMargin
						);
					
					} // End of if block - grid line drawn.
				
				// Draw label (if requested).
				if (_xAxisFormatter.getMajorTickLabelFormatter().isVisible()) {
					
					g.setFont(font);
					g.setPaint(
						_xAxisFormatter.getMajorTickLabelFormatter().getColor()
						);
					
					String label = tickLabelToString(scaleValue, AxisType.X);
					
					LineMetrics metrics = font.getLineMetrics(label, frc);
					float fontHeight = metrics.getHeight();
					float fontDescender = metrics.getDescent();
					float fontAscender = metrics.getAscent();
					float stringLength = 
						(float)font.getStringBounds(label, frc).getWidth();
					
					g.drawString(
						label, 
						(float)(newTickXPosition - stringLength/2),
						(float)(
							yStart +
							_xAxisFormatter.getMajorTickLength() + 
							_tickToLabelGap + 
							(fontAscender - fontDescender)
							)
						);
					
					} // End of if block - label drawn (if requested).
				
				newTickXPosition = newTickXPosition + increment;
				scaleValue = scaleValue + _xAxis.getIncrement();
				}	

			//================================================
			
			}
		
		
		
		
		// Decide on minor tick position relative to axis, and length.
	    //=========================================================
			
		// Default - assumes ticks are out.
		double minorTickYPosition1 = yStart;
		double minorTickYPosition2 = yStart + _xAxisFormatter.getMinorTickLength();
		
		// If ticks are in:
		if (_xAxisFormatter.getMinorTickLocation() == TickLocation.In) {
			minorTickYPosition2 = yStart - _xAxisFormatter.getMinorTickLength();
			}
		
		// If ticks are both in and out:
		if (_xAxisFormatter.getMinorTickLocation() == TickLocation.InAndOut) {
			minorTickYPosition1 = yStart - _xAxisFormatter.getMinorTickLength();
			}
		
		// If ticks are neither in nor out:
		if (_xAxisFormatter.getMinorTickLocation() == TickLocation.None) {
			return; // Abort - only major ticks drawn.
			}	
		//=========================================================	
		
		// Draw minor ticks
		//================================================
		newTickXPosition = tickXPosition;
		
		// Records number of ticks to eliminate if necessary.
		int i= _xAxis.getMinorTicks();
		// Only eliminate if major ticks are present on same side.
		if (
			// Not the same ...
			_xAxisFormatter.getMajorTickLocation() != 
				_xAxisFormatter.getMinorTickLocation() && 
				// ...and major tick is not on both sides.
				_xAxisFormatter.getMajorTickLocation() != TickLocation.InAndOut		
				) {
			i++; // ensures 'if' argument, below, will never be true.
			}
		
		
		while (Math.round(newTickXPosition) <= Math.round(xFinish)) {  
			
			//---------------------------------------------------------------
			// Occasionally minor ticks are not aligned exactly with major.
			// therefore only draw minor ticks that don't overlap with major.
			//---------------------------------------------------------------
			
			if (i == _xAxis.getMinorTicks()) {
				newTickXPosition = newTickXPosition + minorIncrement;
				i = 0;
				continue;
				}
			i++;
			//---------------------------------------------------------------
			
			g.setPaint(_xAxisFormatter.getLineColor());
			g.setStroke(new BasicStroke(_xAxisFormatter.getLineWidth()));
			
			g.drawLine(
				(int)Math.round(newTickXPosition),
				(int)Math.round(minorTickYPosition1),
				(int)Math.round(newTickXPosition),
				(int)Math.round(minorTickYPosition2)
				);
			
			
			// Draw grid lines if required.
				if (_xAxisFormatter.isMinorGridLinesVisible()) {
					
					g.setPaint(_xAxisFormatter.getMinorGridLineFormatter().getLineColor());
					
					g.setStroke(
						getStroke(
							_xAxisFormatter.getMinorGridLineFormatter().getLineStyle(), 
							_xAxisFormatter.getMinorGridLineFormatter().getLineWidth()
							)
						);
	
					// Draw gridlines.
					g.drawLine(
						(int)Math.round(newTickXPosition),
						(int)Math.round(yStart),
						(int)Math.round(newTickXPosition),
						_topMargin
						);
					
					} // End of if block - grid line drawn.
			
			newTickXPosition = newTickXPosition + minorIncrement;
			}	
		
		//================================================
		
		
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	// Creates a stroke object with the correct line style and width.
	private Stroke getStroke(LineStyle style, float width) {
		
		float[] dashPattern = null;
		
		if (style == LineStyle.Solid) {
			dashPattern = new float[] {100f, 0f};
			}
		
		if (style == LineStyle.Dash) {
			dashPattern = new float[] {10.0f, 2.0f};
			}
		
		if (style == LineStyle.Dot) {
			dashPattern = new float[] {3.0f, 2.0f};
			}
		
		if (style == LineStyle.DashDot) {
			dashPattern = new float[] {10.0f, 2.0f, 3.0f, 2.0f};
			}
		
		if (style == LineStyle.DashDotDot) {
			dashPattern = new float[] {10.0f, 2.0f, 3.0f, 2.0f, 3.0f, 2.0f};
			}
		
		if (style == LineStyle.ShortDash) {
			dashPattern = new float[] {5.0f, 2.0f};
			}
		
		if (style == LineStyle.ShortDashDot) {
			dashPattern = new float[] {5.0f, 2.0f, 3.0f, 2.0f};
			}
		
		if (style == LineStyle.ShortDot) {
			dashPattern = new float[] {2.0f, 1.0f};
			}
		
		return new BasicStroke(
			width, 
			BasicStroke.CAP_BUTT, // end cap style. 
			BasicStroke.JOIN_BEVEL, // Join style. 
			10.0f, // miterlimit. 
			dashPattern, //Dash pattern. 
			0.0f //dash_phase.
			);			
			
		
		} // End of method.
	
	//##########################################################################
	
	// INNER CLASSES
	
	private class GraphMouseListener implements MouseListener, MouseMotionListener {
		
		Color originalBorderColor; 
		Color originalBackground;
		boolean originalBorderStatus;
		boolean originalBackgroundStatus;
		boolean move = false;
		
		public void mouseClicked(MouseEvent me) {
			
			}
		
		public void mouseEntered(MouseEvent me) {
			}
		
		public void mouseExited(MouseEvent me) {
			}
		
		public void mousePressed(MouseEvent me) {
			inKey(me);
			}
		
		public void mouseReleased(MouseEvent me) {
			defaultKeyAppearance();
			}
		
		public void mouseDragged(MouseEvent me) {
			moveKey(me);
			}		
		
		public void mouseMoved(MouseEvent me) {
			
			}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		private void inKey(MouseEvent me) {
			
		originalBorderColor = _keyBorderColor;
		originalBackground = _keyBackground;
		originalBorderStatus = _keyBorderVisible;
		originalBackgroundStatus = _keyOpaque;
			
			// Mouse is within key area.
			if (
				me.getX() >= _keyLocation.x && 
				me.getX() <= _keyLocation.x + _keyDimension.width &&
				me.getY() >= _keyLocation.y &&
				me.getY() <= _keyLocation.y + _keyDimension.height
				) {
				
				_keyBorderColor = Color.RED;
				//_keyBackground = Color.LIGHT_GRAY;
				_keyOpaque = true;
				_keyBorderVisible = true;
				move = true;
				repaint();
				
					
				}
			
			// Mouse outside Key area.
			else {
				defaultKeyAppearance();
				}
			
			}
		
		private void defaultKeyAppearance() {
			_keyBorderColor = originalBorderColor;
			_keyBorderVisible = originalBorderStatus;
			_keyBackground = originalBackground;
			_keyOpaque = originalBackgroundStatus;
			move = false;
			repaint();
			}
		
		private void moveKey(MouseEvent me) {
			if (move) {
				
				int newX = me.getX() - _keyDimension.width/2;
				int newY = me.getY() - _keyDimension.height/2;
				
				_keyLocation.setLocation(newX, newY);
				
				repaint();
				
				}
			} 
		
		} // End of inner class.
	
	//##########################################################################
	
	} // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////