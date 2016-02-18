//==============================================================================
//
//   _BoxAndWhiskerPlot.java
//   Created: Dec 19, 2010 at 5:56:46 PM
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

import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import java.awt.image.*;
import java.awt.print.*;
import nestor.graphics.newplot.boxAndWhisker.*;
import nestor.graphics.newplot.boxAndWhisker.BoxAndWhiskerModelFactory;
import nestor.graphics.newplot.boxAndWhisker.IBoxAndWhiskerModel;
import nestor.stats.Stats;
import java.util.*;
import javax.swing.*;
import java.text.NumberFormat;
import javax.swing.border.*;
import nestor.tools.bamstats.IBAMStatsConstants;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 * 
 * @author  Kevin Ashelford.
 */
class _HorizontalBoxAndWhiskerPlot extends Plot implements IBAMStatsConstants {

	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS

	private IBoxAndWhiskerModel				_boxAndWhiskerModel;
	private IBoxAndWhiskerRenderer			_renderer;
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

	private final BoxAndWhiskerPositions _positions = new BoxAndWhiskerPositions();
	private Stats _highlightedStats = null;

	private final JToolTip _toolTip = new MyToolTip();

	private final NumberFormat _numberFormat = NumberFormat.getInstance();

	//##########################################################################
	
	// CONSTRUCTOR
	
    /**
	 * Creates a new instance of <code>_BoxAndWhiskerPlot</code>.
	 */


	public _HorizontalBoxAndWhiskerPlot() {
		this(
			AxisFactory.createAxis(
				"X Axis",
				AxisType.X,
				0f,
				10f,
				1,
				1,
				ScaleType.Linear
				)
			);

		} // End of constructor.

	////////////////////////////////////////////////////////////////////////////

    public _HorizontalBoxAndWhiskerPlot(IAxis xAxis) {

		_numberFormat.setMinimumIntegerDigits(1);
        _numberFormat.setMinimumFractionDigits(0);
        _numberFormat.setMaximumFractionDigits(2);

		ToolTipManager.sharedInstance().setInitialDelay(0);
		ToolTipManager.sharedInstance().setReshowDelay(0);

		_xAxis = xAxis;

		_yAxis = AxisFactory.createAxis(
			"",
			AxisType.Y,
			0,
			10,
			1f,
			0,
			ScaleType.Linear
			);

		_boxAndWhiskerModel	= BoxAndWhiskerModelFactory.createGraphModel();
		_axisRenderer		= AxisRendererFactory.createAxisRenderer();
		_renderer			= BoxAndWhiskerRendererFactory.createBoxAndWhiskerRenderer(null);

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

		// For detecting movement over box and whiskers with mouse.
		BoxAndWhiskerMouseListener l2 = new BoxAndWhiskerMouseListener();
		addMouseMotionListener(l2);



		} // End of constructor.

	//##########################################################################
		
	// METHODS

	public JToolTip createToolTip() {
		
		return _toolTip;
		}

	////////////////////////////////////////////////////////////////////////////

	public Point getToolTipLocation(final MouseEvent me) {
		
		return new Point(me.getX()+15, me.getY()+15);
		}

	////////////////////////////////////////////////////////////////////////////

	private String truncate(final String s) {

		if (s.length() > MAX_LABEL_SIZE) {
			return s.substring(0, MAX_LABEL_SIZE) + "...";
			}
		else {
			return s;
			}

		} // End of method.

	////////////////////////////////////////////////////////////////////////////

	public void insideBoxAndWhisker() {

		if (_toolTip.isVisible()) return;

		// Display summary of stats object

		String refId = _highlightedStats.getId();
		
		//String sourceFileName = _highlightedStats.getMetadata().getSourceFile().getName();
		String mean = _numberFormat.format(_highlightedStats.getArithmeticMean());
		String median = _numberFormat.format(_highlightedStats.getMedian());
		String mode = _numberFormat.format(_highlightedStats.getMode());
		String q1 = _numberFormat.format(_highlightedStats.getLowerQuartile());
		String q3 = _numberFormat.format(_highlightedStats.getUpperQuartile());
		String p2_5 = _numberFormat.format(_highlightedStats.getTwoPointFivePercentile());
		String p97_5 = _numberFormat.format(_highlightedStats.getNinetySevenPointFivePercentile());
		String min = _numberFormat.format(_highlightedStats.getMinimum());
		String max = _numberFormat.format(_highlightedStats.getMaximum());
		String range = _numberFormat.format(_highlightedStats.getRange());

		String featureText = "";
	//	if (_highlightedStats.getMetadata().hasFeatureId()) {
	//		String featureId = _highlightedStats.getMetadata().getFeatureId();
	//		if (!featureId.equals("null")) {
	//			featureText = "<tr>" +
	//						"<td>Feature:</td>" +
	//						"<td><b>" + featureId + "</b></td>" +
	//					"</tr>";
	//			}
	//		}


		setToolTipText(
			"<html>" +
			
			"<center><h2>" + refId + "</h2></center><hr>" +

/*
			"<table border=\"0\">" +
				"<tbody>" +

					featureText +

					"<tr>" +
						"<td>File:</td>" +
						"<td><b>" + sourceFileName + "</b></td>" +
					"</tr>" +
				"</tbody>" +
			"</table><hr>" +
*/

		//	"Feature: <b>" + featureId + "</b><br>" +
		//	"File: <b>" + sourceFileName + "</b><br><hr>" +

			"<table border=\"0\">" +
				"<tbody>" +
					"<tr>" +
						"<td>Mean:</td>" +
						"<td>" + mean + "</td>" +
					"</tr>" +
					"<tr>" +
						"<td>Median:</td>" +
						"<td>" + median + "</td>" +
					"</tr>" +
					"<tr>" +
						"<td>Mode:</td>" +
						"<td>" + mode + "</td>" +
					"</tr>" +
					"<tr>" +
						"<td>q1:</td>" +
						"<td>" + q1 + "</td>" +
					"</tr>" +
					"<tr>" +
						"<td>q3:</td>" +
						"<td>" + q3 + "</td>" +
					"</tr>" +
					"<tr>" +
						"<td>2.5%:</td>" +
						"<td>" + p2_5 + "</td>" +
					"</tr>" +
					"<tr>" +
						"<td>97.5%:</td>" +
						"<td>" + p97_5 + "</td>" +
					"</tr>" +

					"<tr>" +
						"<td>Min:</td>" +
						"<td>" + min + "</td>" +
					"</tr>" +
					"<tr>" +
						"<td>Max:</td>" +
						"<td>" + max + "</td>" +
					"</tr>" +
					"<tr>" +
						"<td>Range:</td>" +
						"<td>" + range + "</td>" +
					"</tr>" +

				"</tbody>" +
			"</table>" +

	//		"Mean: " + mean + "<br>" +
	//		"Median: " + median + "<br>" +
	//		"Mode: " + mode + "<br>" +
	//		"q1: " + q1 + "<br>" +
	//		"q3: " + q3 + "<br>" +
	//		"2.5%: " + p2_5 + "<br>" +
	//		"97.5%: " + p97_5 + "<br>" +
			"<hr></html>"
			);
		_toolTip.setVisible(true);

		}

	////////////////////////////////////////////////////////////////////////////

	public void outsideBoxAndWhisker() {
		_toolTip.setVisible(false);
		}

	////////////////////////////////////////////////////////////////////////////

	public void clear() {
		_boxAndWhiskerModel.setStatsCollection(null);
		_positions.clear();
		_highlightedStats = null;
		plot();
		} // End of method.

	////////////////////////////////////////////////////////////////////////////

	// TODO - MOVE TO PARENT CLASS?
	public void plot() {
		repaint();
		} // End of method.

	////////////////////////////////////////////////////////////////////////////

	// TODO - MOVE TO PARENT CLASS?
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

	// TODO - MOVE TO PARENT CLASS?
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
		if (_boxAndWhiskerModel.hasData()) {
			doPage(g2);
			}

		} // End of method.

	////////////////////////////////////////////////////////////////////////////

	// TODO - MOVE TO PARENT CLASS?
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

	// TODO - to complete
	private void doPage(Graphics2D g) {

		// White background.
		g.setPaint(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());


		// Switch on if requested.
		if (_antiAliasing) {
			g.setRenderingHint(
				RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
				);
			}


		// Determine number of box-and-whiskers to draw.
		int numberOfBoxAndWhiskers = _boxAndWhiskerModel.getStatsCollectionCount();

		// Estimate box width in order to assess y axis padding.
		// TODO - Better estimate of y axis padding based on number of box and whisker plots.  <======================================
		float padding = 0.5f;

		// Set y axis on basis of number of box and whisker plots.
		_yAxis.setFrom(0); // Add padding?
		_yAxis.setTo(numberOfBoxAndWhiskers + padding); 

		// Plot graph axis.
		drawAxis(g);

		// Do not proceed further if data model is empty.
		if (_boxAndWhiskerModel.getStatsCollection() == null) {return;}


		// PLOT DATA NOW
		//==============

		// Data object.
		Stats stats;

		// Defines appearance of line.
		IBoxAndWhiskerFormatter boxAndWhiskerFormatter;

		// First clear existing positin store - contents (if any) to be replaced
		// by following data.
		_positions.clear();

		// Work through each box and whisker in turn.
		for (int i = 0; i < numberOfBoxAndWhiskers; i++) {

			// Get data for current box and whisker.
			stats = _boxAndWhiskerModel.getStats(i);

			// Get the Plot-line formatting object for current plot-line data.
			boxAndWhiskerFormatter = _renderer.getBoxAndWhiskerFormatter(stats);

			// Draw box and whisker.
			drawHorizontalBoxAndWhisker(
				numberOfBoxAndWhiskers - i,
				stats,
				boxAndWhiskerFormatter,
				g
				);

			} // End of for loop - no box and whisker objects remaining.

		displayKey(g);

		// Finally, draw masking box to hide data drawn over edge of x axis.

		// Background
		//=============

		int width = this.getRightMargin();
		int x = getWidth() - width + 2;
		int y = 0;
		int height = getHeight() - this.getBottomMargin();

		g.setPaint(Color.white);
		g.fillRect(
			x,
			y,
			width,
			height
			);




		} // End of method.


	////////////////////////////////////////////////////////////////////////////

	private void drawHorizontalBoxAndWhisker(
		final int yPosition,
		final Stats stats,
		final IBoxAndWhiskerFormatter formatter,
		final Graphics2D g
		) {

		// IMPORTANT - here's where stats values converted to absolute positions
		// on graphic.
		//============================================
		double xOrigin = _leftMargin + convert(_xAxisFormatter.getStartMargin(), AxisType.X);
		double yOrigin = getHeight() - (_bottomMargin + convert(_yAxisFormatter.getStartMargin(), AxisType.Y));

		double trueXOrigin = xOrigin - (_xAxis.getFrom() * convert(_xAxis.getIncrement(), AxisType.X) / _xAxis.getIncrement() );
		double trueYOrigin = yOrigin + (_yAxis.getFrom() * convert(_yAxis.getIncrement(), AxisType.Y) / _yAxis.getIncrement() );

		double min		= trueXOrigin + convert(stats.getMinimum(), AxisType.X);
		double max		= trueXOrigin + convert(stats.getMaximum(), AxisType.X);

		double p2_5		= trueXOrigin + convert(stats.getTwoPointFivePercentile(), AxisType.X);
		double p97_5	= trueXOrigin + convert(stats.getNinetySevenPointFivePercentile(), AxisType.X);

		double mean		= trueXOrigin + convert(stats.getArithmeticMean(), AxisType.X);
		double median	= trueXOrigin + convert(stats.getMedian(), AxisType.X);
		double q1		= trueXOrigin + convert(stats.getLowerQuartile(), AxisType.X);
		double q3		= trueXOrigin + convert(stats.getUpperQuartile(), AxisType.X);

		double yPos = trueYOrigin - convert(yPosition, AxisType.Y);
		//============================================

		// TODO - estimate box width based on number of box and whisker objects.
		// Estimate box width - depends on number of box and whisker objects. <---------------------------------------------------------
		float yAxisLength = (float)getHeight() - (float)(_topMargin + _bottomMargin);
		int numberOfBoxAndWhiskers = _boxAndWhiskerModel.getStatsCollectionCount();

		// accommodate padding at end of y axis (see y axis initialisation)
		float x =	(yAxisLength - (float)convert(0.5, AxisType.Y) )
					/
					(float)(numberOfBoxAndWhiskers);

		// Allow small gap between boxes.
		float boxWidth = x - 8;


		boolean isSelected = false;
		if (_highlightedStats == stats) isSelected = true;
	
		// Draw whiskers line.
		drawLine(
			g,
			formatter,
			(int)Math.round(p2_5),
			(int)Math.round(yPos),
			(int)Math.round(p97_5),
			(int)Math.round(yPos),
			isSelected
			);

		// Draw min and max lines
		drawLine(
			g,
			formatter,
			(int)Math.round(p2_5),
			(int)Math.round(yPos) - (int)(boxWidth/2f),
			(int)Math.round(p2_5),
			(int)Math.round(yPos) + (int)(boxWidth/2f),
			isSelected
			);

		drawLine(
			g,
			formatter,
			(int)Math.round(p97_5),
			(int)Math.round(yPos) - (int)(boxWidth/2f),
			(int)Math.round(p97_5),
			(int)Math.round(yPos) + (int)(boxWidth/2f),
			isSelected
			);

		// Draw Box.
		double width = q3 - q1;
		drawBox(
			g, 
			formatter,
			(int)Math.round(q1),
			(int)Math.round(yPos) - (int)(boxWidth/2f),
			(int)width,
			(int)boxWidth,
			isSelected
			);

		// Draw median.
		drawLine(
			g,
			formatter,
			(int)Math.round(median),
			(int)Math.round(yPos) - (int)(boxWidth/2f),
			(int)Math.round(median),
			(int)Math.round(yPos) + (int)(boxWidth/2f),
			isSelected
			);

		// Draw mean.
		drawSymbol(
			g,
			formatter,
			(int)Math.round(mean),
			(int)Math.round(yPos),
			isSelected
			);

		// Finally store location of box-and-whisker for use by mouse listener.
		_positions.addStats(
			new Rectangle(
				(int)Math.round(p2_5),
				(int)Math.round(yPos) - (int)(boxWidth/2f),
				(int)Math.round(p97_5),
				(int)Math.round(yPos) + (int)(boxWidth/2f),
				(int)Math.round(mean), // mean X
				(int)Math.round(yPos) // mean Y
				),
			stats
			);


		} // End of method.

	////////////////////////////////////////////////////////////////////////////

	// MOVE TO PARENT CLASS?
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

	// TODO - MOVE TO PARENT CLASS?
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

	// TODO - MOVE TO PARENT CLASS?
	// Converts axis distances to screen distances
	private double convert(double in, AxisType axisType) {

		float axisLength; // Actual length of axis on screen.
		float range; // Range of values that the axis will display.

		// X axis:
		//===============================================================
		if (axisType == AxisType.X) {

			axisLength =
				(float)getWidth() - (float)(_leftMargin + _rightMargin);

			range =
				(_xAxis.getTo() + _xAxisFormatter.getEndMargin()) -
				(_xAxis.getFrom() - _xAxisFormatter.getStartMargin());

			}

		// Y IAxis:
		//===============================================================
		else if (axisType == AxisType.Y) {

			axisLength =
				(float)getHeight() - (float)(_topMargin + _bottomMargin);

			range =
				(_yAxis.getTo() + _yAxisFormatter.getEndMargin()) -
				(_yAxis.getFrom() - _yAxisFormatter.getStartMargin());

			}

		//===============================================================

		else {
			// IAxis type currently unsupported.
			throw new IllegalArgumentException(
				"AxisType '" + axisType.toString() + "' not currently supported."
				);
			}

		return axisLength / range * in;

		} // End of method.

	////////////////////////////////////////////////////////////////////////////

	// TODO - MOVE TO PARENT CLASS?
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

	////////////////////////////////////////////////////////////////////////////

	// TODO - MOVE TO PARENT CLASS?
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

	// TODO - MOVE TO PARENT CLASS?
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

	// TODO - MOVE TO PARENT CLASS?
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

	private String decideLabel(final Stats stats) {

		return stats.getId();
				
		} // End of method.

	////////////////////////////////////////////////////////////////////////////

	// TODO - MOVE TO PARENT CLASS?
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

		// TODO - need to deal better with too small an increment due to excess number of box and whiskers being plotted.
		if (increment < 1) increment = 1;

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


		ArrayList<Stats> statsArray = _boxAndWhiskerModel.getStatsCollection();
		int n = statsArray.size();


		double newTickYPosition;
		//float scaleValue = _yAxis.getFrom();
		float scaleValue = 0;

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

					//String label = tickLabelToString(n - scaleValue + 1, AxisType.Y); //=======================================================
					//String label = statsArray.get(n - (int)scaleValue).getLabel();

					String label = "";
					if (scaleValue != 0) {
						// Uncomment for numbers
						//label = tickLabelToString(n - scaleValue + 1, AxisType.Y);
						label = truncate(decideLabel(statsArray.get(n - (int)scaleValue)));
						}

					// Ignore scale value of zero.
					//if (scaleValue == 0) continue;

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
				//scaleValue = scaleValue + _yAxis.getIncrement();
				scaleValue++;

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

	// TODO - PERHAPS ABSTRACT IN PARENT CLASS?
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
		for (int i = 0; i < _boxAndWhiskerModel.getStatsCollectionCount(); i++) {

			double length = _keyFont.getStringBounds(
				_boxAndWhiskerModel.getStats(i).getId(),
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
			fontHeight * _boxAndWhiskerModel.getStatsCollectionCount(); // text height.

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
		for (int i = 0; i < _boxAndWhiskerModel.getStatsCollectionCount(); i++) {

			// Get current plot data.
			Stats stats = _boxAndWhiskerModel.getStats(i);

			// Get associated line formatter.
			IBoxAndWhiskerFormatter f = _renderer.getBoxAndWhiskerFormatter(stats);

			// Draw illustrative line.
			drawLine(
				g, f,
				currentX,
				currentY - fontAscent/2 + fontDescent,
				currentX + imageLength,
				currentY - fontAscent/2 + fontDescent,
				false
				);

			// Write text.
			g.setPaint(_keyForeground);
			g.setFont(_keyFont);
			g.drawString(
				_boxAndWhiskerModel.getStats(i).getId(),
				currentX + imageLength + imageToTextGap,
				currentY
				);

			currentY = currentY + fontHeight;
			}

		// Lastly, record new key dimension for mouse etc.
		_keyDimension.setSize(width, height);

		} // End of method.

	////////////////////////////////////////////////////////////////////////////

	private void drawBox(
		final Graphics2D g,
		final IBoxAndWhiskerFormatter f,
		final int x,
		final int y,
		final int width,
		final int height,
		final boolean isSelected
		) {


		// Background
		//=============
	//	g.setPaint(BOX_AND_WHISKER_COLOUR);
		g.setPaint(f.getBoxColor());
	//	if (isSelected) g.setPaint(Color.DARK_GRAY);
		g.fillRect(x, y, width, height);


		// Border
		//=======
		//g.setPaint(f.getLineColor());
		g.setPaint(BOX_AND_WHISKER_COLOUR);
		if (isSelected) g.setPaint(HIGHLIGHTED_BOX_AND_WHISKER_COLOUR);
		g.setStroke(new BasicStroke(f.getLineWidth()));
		g.drawRect(x, y, width, height);



		} // End of method.

	////////////////////////////////////////////////////////////////////////////

	// TODO - MOVE TO PARENT CLASS? NOTE CHANGE TO MORE GENERIC ILineFormatter.
	private void drawLine(
		final Graphics2D g,
		final ILineFormatter f,
		final int x1,
		final int y1,
		final int x2,
		final int y2,
		final boolean isSelected
		) {

		//g.setColor(f.getLineColor());
		g.setPaint(BOX_AND_WHISKER_COLOUR);
		if (isSelected) g.setPaint(HIGHLIGHTED_BOX_AND_WHISKER_COLOUR);
		g.setStroke(
			getStroke(
				f.getLineStyle(),
				f.getLineWidth()
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

	// TODO - MOVE TO PARENT CLASS? - can't because of IBoxAndWhiskerFormatter - worth rewriting?
	private void drawSymbol(
		final Graphics2D g,
		final IBoxAndWhiskerFormatter f,
		final int x,
		final int y,
		final boolean isSelected
		) {

		int w = f.getSymbolSize();
		//Color c = f.getSymbolColor();

		Color c = BOX_AND_WHISKER_COLOUR;
		if (isSelected) c = HIGHLIGHTED_BOX_AND_WHISKER_COLOUR;


		int x1 = x - Math.round((float)w/2f);
		int y1 = y - Math.round((float)w/2f);


		// Closed circle.
		if (f.getSymbol() == Symbol.ClosedCircle) {
			g.setColor(c);
			g.fillOval(x1, y1, w, w);
			}

		// Closed square.
		else if (f.getSymbol() == Symbol.ClosedSquare) {
			g.setColor(c);
			g.fillRect(x1, y1, w, w);
			}

		// Open circle.
		else if (f.getSymbol() == Symbol.OpenCircle) {
			g.setColor(Color.WHITE);
			g.fillOval(x1, y1, w, w);
			g.setColor(c);
			g.drawOval(x1, y1, w, w);
			}

		// Open square.
		else if (f.getSymbol() == Symbol.OpenSquare) {
			g.setColor(Color.WHITE);
			g.fillRect(x1, y1, w, w);
			g.setColor(c);
			g.drawRect(x1, y1, w, w);
			}

		// Not supported.
		else {
			throw new IllegalArgumentException(
				"Symbol '" +
				f.getSymbol().toString() +
				"' is not currently supported"
				);
			}


		} // End of method.

	////////////////////////////////////////////////////////////////////////////

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
	public IBoxAndWhiskerModel getDataModel() {
		return _boxAndWhiskerModel;
		} // End of property.

	////////////////////////////////////////////////////////////////////////////

	
	public void setDataRenderer(
		IDataRenderer renderer
		) {
		_renderer = (IBoxAndWhiskerRenderer)renderer;
		} // End of method.

	////////////////////////////////////////////////////////////////////////////

	/**
	 Gets the ICoordinateCollectionRenderer object.
	 @return ICoordinateCollectionRenderer object.
	 */
	public IDataRenderer getDataRenderer() {
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
	
	// INNER CLASSES

	private class BoxAndWhiskerPositions {

		private HashMap<Rectangle, Stats> map = new HashMap<Rectangle, Stats>();


		public void clear() {
			map.clear();
			}


		public Stats getStats(int x, int y) {
			return map.get(getRectangle(x, y));
			}


		public Rectangle getRectangle(int x, int y) {

			Iterator<Rectangle> it = map.keySet().iterator();
			while (it.hasNext()) {
				Rectangle r = it.next();
				if (r.encompasses(x, y)) {return r;}
				}
			return null;

			}


		public void addStats(Rectangle r, Stats s) {
			map.put(r, s);
			}


		public boolean isEmpty() {
			return map.isEmpty();
			}


		} // End of inner class.

	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	private class Rectangle {

		private final int x1;
		private final int x2;
		private final int y1;
		private final int y2;
		private final int meanX;
		private final int meanY;

		public Rectangle(int x1, int y1, int x2, int y2, int meanX, int meanY) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
			this.meanX = meanX;
			this.meanY = meanY;
			} // end of constructor.

		public boolean encompasses(int x, int y) {

			if (x >= x1 && x <= x2 && y >= y1 && y <= y2) return true;
			return false;

			} // end of method.

		public int getMeanX() {return meanX;}
		public int getMeanY() {return meanY;}

		} // End of inner class.

	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	private class BoxAndWhiskerMouseListener extends MouseMotionAdapter {

		public void mouseMoved(final MouseEvent me) {

			int x = me.getX();
			int y = me.getY();

			// Do nothing if no stats plotted.
			if (_positions.isEmpty()) return;

			// Get stats object under mouse.
			_highlightedStats = _positions.getStats(x, y);
			repaint();

			if (_highlightedStats != null) {
				insideBoxAndWhisker();
				}
			else {
				outsideBoxAndWhisker();
				}


			

			
			
			

			} // end of method.

		} // End of inner class.

	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

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

	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	private class MyToolTip extends JToolTip {

		public MyToolTip() {
			this.setBackground(nestor.tools.bamstats.IBAMStatsConstants.ODD_ROW_COLOUR);
			this.setBorder(
				new CompoundBorder(
					new LineBorder(new Color(100,100,100)),
					new EmptyBorder(5,5,5,5)
					)
				);

			}

		} // End of method.
	
	//##########################################################################
	
    } // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////





