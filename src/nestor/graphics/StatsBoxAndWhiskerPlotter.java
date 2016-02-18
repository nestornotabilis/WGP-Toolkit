//==============================================================================
//
//   StatsBoxAndWhiskerPlot.java
//   Created: Jan 6, 2011 at 3:54:24 PM
//   Copyright (C) 2011, University of Liverpool.
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

import java.util.*;
import java.io.*;
import nestor.stats.Stats;
import nestor.graphics.newplot.*;
import nestor.graphics.newplot.boxAndWhisker.*;
import java.awt.*;
import java.awt.image.RenderedImage;
import javax.imageio.ImageIO;
import nestor.tools.Constants;
import nestor.stats.IHistogram;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 * Box and whisker plotter widget to be used with PlotPanel.
 * @author  Kevin Ashelford.
 */
public class StatsBoxAndWhiskerPlotter implements IPlotter, Constants {

	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS

	private final Plot _plot = PlotFactory.createHorizontalBoxAndWhiskerPlot();

	//##########################################################################
	
	// CONSTRUCTOR
	
    /**
	 * Creates a new instance of <code>StatsBoxAndWhiskerPlot</code>.
	 */
    public StatsBoxAndWhiskerPlotter() {
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


	public void plot(boolean isCumulative) {
		throw new RuntimeException("Operation not supported.");
		}

	////////////////////////////////////////////////////////////////////////////

	public void createPng(
		final Stats stats,
		final IHistogram UNUSED,  // UNUSED 
		final File outfile,
		final ScaleType type,
		final String xAxisLabel,
		final String yAxisLabel,
		final float minX,
		final float maxX,
		final int width,
		final int height,
		final float increment,
		final int xMinorTick,
		final boolean isCumulative
		) throws IOException {

		_plot.setSize(width, height);

		updatePlot(
			stats,
			UNUSED,
			type,
			xAxisLabel,
			yAxisLabel,
			minX,
			maxX,
			increment,
			xMinorTick,
			isCumulative
			);

		RenderedImage image = _plot.createImage();
		ImageIO.write(image, "png", outfile);

		}


	////////////////////////////////////////////////////////////////////////////

	public void createPng(
		final ArrayList<Stats> statsArray,
		final ArrayList<IHistogram> UNUSED, // UNUSED 
		final File outfile,
		final ScaleType type,
		final String xAxisLabel,
		final String yAxisLabel,
		final float minX,
		final float maxX,
		final int width,
		final int height,
		final float increment,
		final int xMinorTick,
		final boolean isCumulative
		) throws IOException {

		_plot.setSize(width, height);

		updatePlot(
			statsArray,
			UNUSED,
			type,
			xAxisLabel,
			yAxisLabel,
			minX,
			maxX,
			increment,
			xMinorTick,
			isCumulative
			);

		RenderedImage image = _plot.createImage();
		ImageIO.write(image, "png", outfile);

		}

	////////////////////////////////////////////////////////////////////////////

	public void updatePlot(
		final Stats stats,
		final IHistogram UNUSED, // UNUSED 
		final ScaleType type,
		final String xAxisLabel,
		final String yAxisLabel,
		final float minX,
		final float maxX,
		final float increment,
		final int xMinorTick,
		final boolean isCumulative
		) {

		ArrayList<Stats> array = new ArrayList<Stats>();
		array.add(stats);

		updatePlot(
			array,
			null, // UNUSED
			type,
			xAxisLabel,
			yAxisLabel,
			minX,
			maxX,
			increment,
			xMinorTick,
			isCumulative
			);

		} // End of method.


	////////////////////////////////////////////////////////////////////////////

	public void updatePlot(
		final float minX,
		final float maxX,
		final float increment,
		final int xMinorTick
		) {

		_plot.getXAxis().setFrom(minX);
		_plot.getXAxis().setTo(maxX);

		//float increment = (float)maxX/10f;

		_plot.getXAxis().setIncrement(increment);

		_plot.getXAxis().setMinorTicks(xMinorTick);

		_plot.plot();

		} // End of method.

	////////////////////////////////////////////////////////////////////////////

	public void updatePlot(
		final ArrayList<Stats> originalStatsArray,
		final ArrayList<IHistogram> UNUSED,
		final ScaleType type,
		final String xAxisLabel,
		final String yAxisLabel,
		final float minX,
		final float maxX,
		final float increment,
		final int xMinorTick,
		final boolean isCumulative
		) {

		// TODO - messy - needs tidying up - once decided how to deal with log data.
		ArrayList<Stats> statsArray = new ArrayList<Stats>();

		ArrayList<IBoxAndWhiskerFormatter> formatterArray = new ArrayList<IBoxAndWhiskerFormatter>();
		int colorNumber = 0;
		int styleNumber= 0;
		int i = 0;

		for (Stats oldStats : originalStatsArray) {

			Stats newStats;

			if (type == ScaleType.Log10) {
				newStats = createLoggedStats(oldStats);
				statsArray.add(newStats);
				}
			else if (type == ScaleType.Linear) {
				newStats = oldStats;
				statsArray.add(newStats);
				}
			else {
				throw new RuntimeException("Shouldn't reach this point!");
				}

			IBoxAndWhiskerFormatter formatter = new DefaultBoxAndWhiskerFormatter(newStats);
			formatterArray.add(formatter);
			formatter.setLineColor(PLOT_COLOURS[colorNumber]); // TODO - Not currently used
			formatter.setLineStyle(LINE_STYLES[styleNumber]);
			formatter.setLineWidth(2);
			formatter.setSymbol(Symbol.ClosedCircle);
			formatter.setSymbolColor(PLOT_COLOURS[colorNumber]);
			formatter.setSymbolSize(12);
			formatter.setBoxColor(PLOT_COLOURS[colorNumber]);
			i++;
			colorNumber++;
			if (colorNumber == 12) {colorNumber = 0; styleNumber++;}
			if (styleNumber == 5) styleNumber = 0;
			}


		IBoxAndWhiskerModel dataModel = (IBoxAndWhiskerModel)_plot.getDataModel();
		dataModel.setStatsCollection(statsArray);

		IBoxAndWhiskerRenderer renderer = (IBoxAndWhiskerRenderer)_plot.getDataRenderer();
		renderer.setBoxAndWhiskerFormatters(formatterArray);

		// Set plot dimensions - note height of final image dependent on number
		// of box and whisker objects.
		int height = 55 * statsArray.size();
		if (height < 180) height = 180;
		
		//int minorTicks = 1;

		// Define axes.
		IAxis xAxis = AxisFactory.createAxis(
			xAxisLabel,
			AxisType.X,
			(float)minX, // from
			(float)maxX, // to
			increment,	//(int)Math.ceil(increment), // Increment
			xMinorTick,  // Minor ticks.
			ScaleType.Linear
			);

		_plot.setXAxis(xAxis);
		_plot.getYAxis().setTitle(yAxisLabel);

		// Not strictly necessary, but left for illustrative purposes.
		_plot.setBackground(Color.WHITE);
		_plot.setOpaque(true);

		// Define dimensions of graph object.
		_plot.setBottomMargin(70);
		_plot.setTopMargin(20);
		_plot.setLeftMargin(100);
		_plot.setRightMargin(50);
		_plot.setTickToLabelGap(5);
		_plot.setXAxisLabelToBorderGap(20);
		_plot.setYAxisLabelToBorderGap(25);

		// Switch on antialiasing.
		_plot.setAntiAliasing(true);

		// Specify characteristics of Key.
		_plot.setKeyVisible(false);
		_plot.setKeyBorderVisible(true);
		_plot.setKeyBackground(Color.WHITE);
		_plot.setKeyForeground(Color.BLACK);
		_plot.setKeyMargin(10);
		_plot.setKeyFont(new Font("Dialog", Font.PLAIN, 14));
		_plot.setKeyBorderWidth(2);
		_plot.setKeyBorderColor(Color.BLACK);
		_plot.setKeyOpaque(true);
		_plot.setKeyDefaultLocation(false);
		_plot.setKeyLocation(new Point(580,60));


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
		yAxisFormatter.setLineColor(Color.white);
		yAxisFormatter.setLineWidth(2);
		yAxisFormatter.setMajorTickLength(10);
		yAxisFormatter.setMinorTickLength(5);

		yAxisFormatter.getMajorTickLabelFormatter().setColor(Color.black);
		yAxisFormatter.getMajorTickLabelFormatter().setFont(new Font("Dialog", Font.PLAIN, 14));
		yAxisFormatter.getMajorTickLabelFormatter().setVisible(true);

		yAxisFormatter.setMajorGridLinesVisible(false);
		yAxisFormatter.getMajorGridLineFormatter().setLineColor(MAJOR_GRID_LINE_COLOUR);
		yAxisFormatter.getMajorGridLineFormatter().setLineStyle(LineStyle.Solid);
		yAxisFormatter.getMajorGridLineFormatter().setLineWidth(1);

		yAxisFormatter.setMinorGridLinesVisible(false);
		yAxisFormatter.getMinorGridLineFormatter().setLineColor(MAJOR_GRID_LINE_COLOUR);
		yAxisFormatter.getMinorGridLineFormatter().setLineStyle(LineStyle.Dash);
		yAxisFormatter.getMinorGridLineFormatter().setLineWidth(1);


		// AXIS LABELS
		xAxisFormatter.getTitleFormatter().setColor(Color.black);
		xAxisFormatter.getTitleFormatter().setFont(new Font("Dialog", Font.PLAIN, 18));
		xAxisFormatter.getTitleFormatter().setVisible(true);

		yAxisFormatter.getTitleFormatter().setColor(Color.black);
		yAxisFormatter.getTitleFormatter().setFont(new Font("Dialog", Font.PLAIN, 18));
		yAxisFormatter.getTitleFormatter().setVisible(true);

		
		_plot.plot();

		} // End of method.

	////////////////////////////////////////////////////////////////////////////

	// Logging only those metrics relevant to box and whisker plot - messy!
	private Stats createLoggedStats(final Stats stats) {

		throw new RuntimeException("Not currently supported");
	/*	
		Stats loggedStats = new Stats();

		loggedStats.setMinimum(			Math.log10(stats.getMinimum() + LOG_CORRECTION) );
		loggedStats.setMaximum(			Math.log10(stats.getMaximum() + LOG_CORRECTION) );
		loggedStats.setMedian(			Math.log10(stats.getMedian() + LOG_CORRECTION) );
		loggedStats.setArithmeticMean(	Math.log10(stats.getArithmeticMean() + LOG_CORRECTION) );
		loggedStats.setUpperQuartile(	Math.log10(stats.getUpperQuartile() + LOG_CORRECTION) );
		loggedStats.setLowerQuartile(	Math.log10(stats.getLowerQuartile() + LOG_CORRECTION) );
		loggedStats.setTwoPointFivePercentile(	Math.log10(stats.getTwoPointFivePercentile() + LOG_CORRECTION) );
		loggedStats.setNinetySevenPointFivePercentile(	Math.log10(stats.getNinetySevenPointFivePercentile() + LOG_CORRECTION) );

		return loggedStats;

		//return stats;
	
	 */
		} // End of method.

	//##########################################################################
	
	// INNER CLASSES
	
	//##########################################################################
	
    } // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////





