//==============================================================================
//
//   IPlotter.java
//   Created: Jan 10, 2011 at 5:17:42 PM
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

import nestor.graphics.newplot.Plot;
import java.io.*;
import java.util.*;
import nestor.graphics.newplot.ScaleType;
import nestor.stats.Stats;
import nestor.stats.IHistogram;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 * Interface for all plotter objects displayed by BAMStats.
 * @author  Kevin Ashelford.
 */
public interface IPlotter {

	//##########################################################################
	
	/**
	 Returns Plot object encapsulated by plotter.
	 @return PLot object.
	 */
	public Plot getPanel();

	/**
	 Creates a png outfile of the single-line plot.
	 @param stats Stats object encapsulating data to plot.
	 @param outfile Outfile to write to.
	 @param type Scale type for plot (e.g., linear).
	 @param xAxisLabel Label for x axis.
	 @param yAxisLabel Label for y axis.
	 @param minX Lower limit for x axis.
	 @param maxX Upper limit for x axis.
	 @param width Width of final image.
	 @param height Height of final image.
	 @param increment Number of incremental ticks to display on x-axis.
	 @param xMinorTicks Number of minor ticks to display for each major x-axis tick.
	 @param isCumulative If true, cumulative plot is displayed (if relevant).
	 @throws IOException Thrown if problem encountered whilst writing outfile.
	 */
	public void createPng(
		Stats stats,
		IHistogram histogram,
		File outfile,
		ScaleType type,
		String xAxisLabel,
		String yAxisLabel,
		float minX,
		float maxX,
		int width,
		int height,
		float increment,
		int xMinorTicks,
		boolean isCumulative
		) throws IOException;

	/**
	 Creates a png outfile of the multi-line plot.
	 @param statsArray Array of stats objects encapsulating all data to plot.
	 @param outfile Outfile to write to.
	 @param type Scale type for plot (e.g., linear).
	 @param xAxisLabel Label for x axis.
	 @param yAxisLabel Label for y axis.
	 @param minX Lower limit for x axis.
	 @param maxX Upper limit for x axis.
	 @param width Width of final image.
	 @param height Height of final image.
	 @param increment Number of incremental ticks to display on x-axis.
	 @param xMinorTicks Number of minor ticks to display for each major x-axis tick.
	 @param isCumulative If true, cumulative plot is displayed (if relevant).
	 @throws IOException Thrown if problem encountered whilst writing outfile.
	 */
	public void createPng(
		ArrayList<Stats> statsArray,
		ArrayList<IHistogram> histogramArray,
		File outfile,
		ScaleType type,
		String xAxisLabel,
		String yAxisLabel,
		float minX,
		float maxX,
		int width,
		int height,
		float increment,
		int xMinorTicks,
		boolean isCumulative
		) throws IOException;

	/**
	 Updates multi-line plot display.
	 @param statsArray Array of stats objects encapsulating all data to plot.
	 @param type Scale type for plot (e.g., linear).
	 @param xAxisLabel Label for x axis.
	 @param yAxisLabel Label for y axis.
	 @param minX Lower limit for x axis.
	 @param maxX Upper limit for x axis.
	 @param increment Number of incremental ticks to display on x-axis.
	 @param xMinorTicks Number of minor ticks to display for each major x-axis tick.
	 @param isCumulative If true, cumulative plot is displayed (if relevant).
	 */
	public void updatePlot(
		ArrayList<Stats> statsArray,
		ArrayList<IHistogram> histogramArray,
		ScaleType type,
		String xAxisLabel,
		String yAxisLabel,
		float minX,
		float maxX,
		float increment,
		int xMinorTicks,
		boolean isCumulative
		);

	/**
	 Updates single-line plot display.
	 @param stats Stats object encapsulating data to plot.
	 @param type Scale type for plot (e.g., linear).
	 @param xAxisLabel Label for x axis.
	 @param yAxisLabel Label for y axis.
	 @param minX Lower limit for x axis.
	 @param maxX Upper limit for x axis.
	 @param increment Number of incremental ticks to display on x-axis.
	 @param xMinorTicks Number of minor ticks to display for each major x-axis tick.
	 @param isCumulative If true, cumulative plot is displayed (if relevant).
	 */
	public void updatePlot(
		Stats stats,
		IHistogram histogram,
		ScaleType type,
		String xAxisLabel,
		String yAxisLabel,
		float minX,
		float maxX,
		float increment,
		int xMinorTicks,
		boolean isCumulative
		);

	/**
	 Update plot display.
	 @param minX New lower limit for x axis.
	 @param maxX New upper limit for y axis.
	 @param increment New number of incremental ticks to display on x-axis.
	 @param xMinorTicks New number of minor ticks to display for each major x-axis tick.
	 */
	public void updatePlot(
		float minX,
		float maxX,
		float increment,
		int xMinorTicks
		);

	/**
	 Clear widget.
	 */
	public void clearPlot();

	/**
	 Plot encapsulated data.
	 */
	public void plot();

	/**
	 If set to true, cumulative plot is created (if relevant).
	 @param isCumulative True if plot is cumulative.
	 */
	public void plot(boolean isCumulative);
	
	//##########################################################################
	
    } // End of interface.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////