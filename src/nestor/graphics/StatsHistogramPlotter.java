//==============================================================================
//
//   StatsPlot.java
//   Created: Jan 5, 2011 at 10:32:52 AM
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

package nestor.graphics;

import java.io.*;
import nestor.graphics.newplot.*;
import java.awt.image.RenderedImage;
import javax.imageio.ImageIO;
import java.awt.*;
import java.util.*;
import nestor.stats.*;
import java.text.NumberFormat;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 * Histogram plotter widget to be used with PlotPanel.
 * @author  Kevin Ashelford.
 */
public class StatsHistogramPlotter implements IPlotter, nestor.tools.Constants {

	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS

	private final Plot _plot = PlotFactory.createHistogramPlot();

	//##########################################################################
	
	// CONSTRUCTOR
	
    /**
	 * Creates a new instance of <code>StatsPlot</code>.
	 */
    public StatsHistogramPlotter() {
		
		} // End of constructor.

	//##########################################################################
		
	// METHODS

	public Plot getPanel() {return _plot;}

	////////////////////////////////////////////////////////////////////////////

	public void clearPlot() {
		_plot.getDataModel().deleteData();
		_plot.clear();
		}

	public void plot() {_plot.plot();}

	////////////////////////////////////////////////////////////////////////////

	private float _maxY;

	public void plot(boolean isCumulative) {

		IGraphModel dataModel = (IGraphModel)_plot.getDataModel();
		dataModel.setCumulative(isCumulative);

		// TODO -messy - needs tidying
		if (isCumulative) {
			_maxY = _plot.getYAxis().getTo();
			_plot.getYAxis().setTo(100);
			_plot.getYAxis().setIncrement(10f);
			}
		else {
			_plot.getYAxis().setTo(_maxY);
			_plot.getYAxis().setIncrement((float)_maxY/10f);
			}


		
		_plot.plot();

		}

	////////////////////////////////////////////////////////////////////////////

	public void createPng(
		final Stats stats,
		final IHistogram histogram,
		final File outfile,
		final ScaleType type,
		final String xAxisLabel,
		final String yAxisLabel,
		final float minX,
		final float maxX,
		final int width,
		final int height,
		final float increment,
		final int xMinorTicks,
		final boolean isCumulative
		) throws IOException {

		_plot.setSize(width, height);

		updatePlot(
			stats,
			histogram,
			type,
			xAxisLabel,
			yAxisLabel,
			minX, maxX,
			increment,
			xMinorTicks,
			isCumulative
			);

		RenderedImage image = _plot.createImage();
		ImageIO.write(image, "png", outfile);

		}

	////////////////////////////////////////////////////////////////////////////

	public void createPng(
		final ArrayList<Stats> sArray,
		final ArrayList<IHistogram> hArray,
		final File outfile,
		final ScaleType type,
		final String xAxisLabel,
		final String yAxisLabel,
		final float minX,
		final float maxX,
		final int width,
		final int height,
		final float increment,
		final int xMinorTicks,
		final boolean isCumulative
		) throws IOException {

		_plot.setSize(width, height);

		updatePlot(
			sArray,
			hArray,
			type,
			xAxisLabel,
			yAxisLabel,
			minX, maxX,
			increment,
			xMinorTicks,
			isCumulative
			);

		RenderedImage image = _plot.createImage();
		ImageIO.write(image, "png", outfile);

		}

	////////////////////////////////////////////////////////////////////////////

	public void updatePlot(
		final float minX,
		final float maxX,
		final float increment,
		final int xMinorTick
		) {

		_plot.getXAxis().setFrom(minX);
		_plot.getXAxis().setTo(maxX);
		_plot.getXAxis().setIncrement(increment);
		_plot.getXAxis().setMinorTicks(xMinorTick);
		_plot.plot();

		}

	////////////////////////////////////////////////////////////////////////////

	public void updatePlot(
		final Stats stats,
		final IHistogram histogram,
		final ScaleType type,
		final String xAxisLabel,
		final String yAxisLabel,
		final float minX,
		final float maxX,
		final float increment,
		final int xMinorTicks,
		final boolean isCumulative
		) {
		
		ArrayList<Stats> sArray = new ArrayList<Stats>();
		sArray.add(stats);
		
		ArrayList<IHistogram> hArray = new ArrayList<IHistogram>();
		hArray.add(histogram);
		
		updatePlot(
			sArray,
			hArray,
			type,
			xAxisLabel,
			yAxisLabel,
			minX,
			maxX,
			increment,
			xMinorTicks,
			isCumulative
			);
		
		} // End of method.

	////////////////////////////////////////////////////////////////////////////

	public void updatePlot(
		final ArrayList<Stats> statsArray,
		final ArrayList<IHistogram> histogramArray,
		final ScaleType type,
		final String xAxisLabel,
		final String yAxisLabel,
		final float minX,
		final float maxX,
		final float increment,
		final int xMinorTicks,
		final boolean isCumulative
		) {

		//========================


		ICoordinateCollection[] ccs = new ICoordinateCollection[statsArray.size()];
		IPlotLineFormatter[] plfs = new IPlotLineFormatter[statsArray.size()];
		int colorNumber = 0;
		int styleNumber= 0;
		int i = 0;

		double minY = 0;
		double maxY = 0;

		
		for (int j = 0; j < statsArray.size(); j++) {
		
			Stats stats = statsArray.get(j);
			IHistogram histogram = histogramArray.get(j);
			
			
			double p2_5 = stats.getTwoPointFivePercentile();
			double p97_5 = stats.getNinetySevenPointFivePercentile();

			// log stored values if necessary.
			if (ScaleType.Log10 == type) {
				throw new RuntimeException("Not yet implemented");
				//logHistogram(histogram);
				//p2_5 = Math.log10(p2_5 + LOG_CORRECTION);
				//p97_5 = Math.log10(p97_5 + LOG_CORRECTION);
				}

			ccs[i] = histogram.getCoordinateCollection();

			if (ccs[i].getMaximumY() > maxY) maxY = ccs[i].getMaximumY();

			plfs[i] = new DefaultPlotLineFormatter(ccs[i]);

			plfs[i].setGapToSymbol(true);
			plfs[i].setLineColor(PLOT_COLOURS[colorNumber]);
			plfs[i].setLineStyle(LINE_STYLES[styleNumber]);

			i++;
			colorNumber++;
			if (colorNumber == 12) {colorNumber = 0; styleNumber++;}
			if (styleNumber == 5) styleNumber = 0;
			}



		//========================




		IGraphModel dataModel = (IGraphModel)_plot.getDataModel();
		dataModel.setCoordinateCollections(
			ccs,
			isCumulative
			);

		
		ICoordinateCollectionRenderer renderer = (ICoordinateCollectionRenderer)_plot.getDataRenderer();
		renderer.setPlotLineFormatters(
			plfs
			);



		// Define axes.
		IAxis xAxis = AxisFactory.createAxis(
			xAxisLabel,
			AxisType.X,
			minX,			// from
			maxX,			// to
			increment,		// Increment
			xMinorTicks,	// Minor ticks.
			ScaleType.Linear
			);
		_plot.setXAxis(xAxis);



		// Define y axis.
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumIntegerDigits(1);
        nf.setMinimumFractionDigits(1);
        nf.setMaximumFractionDigits(1);

		// Assuming maxY != 100, add spacer.
		maxY = maxY + 1f;
		if (maxY > 100  || isCumulative) maxY = 100;

		// TODO - temp measure - needs tidying up
		_maxY = (float)maxY;


		float yIncrement = (float)maxY/10f;
		
		
		int yMinorTicks = 1;

		IAxis yAxis = AxisFactory.createAxis(
			yAxisLabel,
			AxisType.Y,
			(float)minY,
			(float)maxY,
			yIncrement, // 10f
			yMinorTicks, // 4
			ScaleType.Linear
			);
		_plot.setYAxis(yAxis);

		// Not strictly necessary, but left for illustrative purposes.
		_plot.setBackground(Color.WHITE);
		_plot.setOpaque(true);

		// Define dimensions of graph object.
		_plot.setBottomMargin(70);
		_plot.setTopMargin(10);
		_plot.setLeftMargin(100);
		_plot.setRightMargin(50);
		_plot.setTickToLabelGap(5);
		_plot.setXAxisLabelToBorderGap(20);
		_plot.setYAxisLabelToBorderGap(25);

		// Switch on antialiasing.
		_plot.setAntiAliasing(true);

		// Specify characteristics of Key.
		_plot.setKeyVisible(false);
	//	_plot.setKeyBorderVisible(true);
	//	_plot.setKeyBackground(Color.WHITE);
	//	_plot.setKeyForeground(Color.BLACK);
	//	_plot.setKeyMargin(10);
	//	_plot.setKeyFont(new Font("Dialog", Font.PLAIN, 14));
	//	_plot.setKeyBorderWidth(2);
	//	_plot.setKeyBorderColor(Color.BLACK);
	//	_plot.setKeyOpaque(true);
	//	_plot.setKeyDefaultLocation(false);
	//	_plot.setKeyLocation(new Point(580,60));


		// Define characteristics of axes.
		DefaultAxisRenderer r = new DefaultAxisRenderer();
		_plot.setAxisRenderer(r);


		IAxisFormatter xAxisFormatter = r.getAxisFormatter(AxisType.X);
		xAxisFormatter.setEndMargin(1);
		xAxisFormatter.setStartMargin(1);
		xAxisFormatter.setLineColor(Color.black);
		xAxisFormatter.setLineWidth(2);
		xAxisFormatter.setMajorTickLength(10);
		xAxisFormatter.setMinorTickLength(5);
		
		xAxisFormatter.getMajorTickLabelFormatter().setColor(Color.black);
		xAxisFormatter.getMajorTickLabelFormatter().setFont(new Font("Dialog", Font.PLAIN, 14));
		xAxisFormatter.getMajorTickLabelFormatter().setVisible(true);
	
		xAxisFormatter.setMajorGridLinesVisible(true);
		xAxisFormatter.getMajorGridLineFormatter().setLineColor(MAJOR_GRID_LINE_COLOUR);
		xAxisFormatter.getMajorGridLineFormatter().setLineStyle(LineStyle.Solid);
		xAxisFormatter.getMajorGridLineFormatter().setLineWidth(1);

		xAxisFormatter.setMinorGridLinesVisible(true);
		xAxisFormatter.getMinorGridLineFormatter().setLineColor(MAJOR_GRID_LINE_COLOUR);
		xAxisFormatter.getMinorGridLineFormatter().setLineStyle(LineStyle.Dash);
		xAxisFormatter.getMinorGridLineFormatter().setLineWidth(1);

		xAxisFormatter.getMajorTickLabelFormatter().setDecimalPlaces(0);

		
		IAxisFormatter yAxisFormatter = r.getAxisFormatter(AxisType.Y);
		yAxisFormatter.setEndMargin(0);
		yAxisFormatter.setStartMargin(0);
		yAxisFormatter.setLineColor(Color.black);
		yAxisFormatter.setLineWidth(2);
		yAxisFormatter.setMajorTickLength(10);
		yAxisFormatter.setMinorTickLength(5);

		yAxisFormatter.getMajorTickLabelFormatter().setColor(Color.black);
		yAxisFormatter.getMajorTickLabelFormatter().setFont(new Font("Dialog", Font.PLAIN, 14));
		yAxisFormatter.getMajorTickLabelFormatter().setVisible(true);

		yAxisFormatter.setMajorGridLinesVisible(true);
		yAxisFormatter.getMajorGridLineFormatter().setLineColor(MAJOR_GRID_LINE_COLOUR);
		yAxisFormatter.getMajorGridLineFormatter().setLineStyle(LineStyle.Solid);
		yAxisFormatter.getMajorGridLineFormatter().setLineWidth(1);

		yAxisFormatter.setMinorGridLinesVisible(true);
		yAxisFormatter.getMinorGridLineFormatter().setLineColor(MAJOR_GRID_LINE_COLOUR);
		yAxisFormatter.getMinorGridLineFormatter().setLineStyle(LineStyle.Dash);
		yAxisFormatter.getMinorGridLineFormatter().setLineWidth(1);

		yAxisFormatter.getMajorTickLabelFormatter().setDecimalPlaces(1);

		xAxisFormatter.getTitleFormatter().setColor(Color.black);
		xAxisFormatter.getTitleFormatter().setFont(new Font("Dialog", Font.PLAIN, 18));
		xAxisFormatter.getTitleFormatter().setVisible(true);

		yAxisFormatter.getTitleFormatter().setColor(Color.black);
		yAxisFormatter.getTitleFormatter().setFont(new Font("Dialog", Font.PLAIN, 18));
		yAxisFormatter.getTitleFormatter().setVisible(true);

		_plot.plot();

		} // End of method.

	////////////////////////////////////////////////////////////////////////////

	private void logHistogram(final IHistogram histogram) {

		throw new RuntimeException("Not yet implemented");
		
/*		ICoordinateCollection cc = histogram.getCoordinateCollection();

		for (int i = 0; i < cc.getSize(); i++) {

			// Log x value of current bin.
			Coordinate c = cc.getCoordinate(i);
			c.setX(
				Math.log10(c.getX() + LOG_CORRECTION)
				);

			}
*/
		} // End of method.

	//##########################################################################
	
	// INNER CLASSES
	
	//##########################################################################
	
    } // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////





