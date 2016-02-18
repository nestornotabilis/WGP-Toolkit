//==============================================================================
//
//   DisplayStats.java
//   Created: Nov 24, 2011 at 3:29:46 PM
//   Copyright (C) 2011, Cardiff University.
//   Author: Kevin Ashelford.
//
//   Contact details:
//   Email:   ashelfordKE@cardiff.ac.uk
//   Address: Institute of Medical Genetics, Cardiff University, 
//            Heath Park Campus, Cardiff, UK. CF14 4XN
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

package nestor.stats;

import java.io.*;
import java.text.NumberFormat;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 * 
 * @author  Kevin Ashelford.
 */
public class DisplayStats {

	//##########################################################################
	
	// CLASS FIELDS
	
	private static final NumberFormat _formatter	= NumberFormat.getInstance();
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS
	
	//##########################################################################
	
	// CONSTRUCTOR
	
	/**
	 * Creates a new instance of <code>DisplayStats</code>.
	 */
	public DisplayStats() {
		
		
		
		} // End of constructor.
	
	//##########################################################################
	
	// METHODS
	
	private static String format(final double value, final int n) {
		
		_formatter.setMinimumIntegerDigits(1);
		_formatter.setMinimumFractionDigits(n);
		_formatter.setMaximumFractionDigits(n);
		
		return _formatter.format(value);
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	public static void printAsTable(final Stats stats, final PrintStream out) {
	
		out.printf(
			"\n\t===============================\n" +
			"\t%-10s %20s\n"						+
			"\t===============================\n"	+
			"\t%-10s %20s\n" +
			"\t%-10s %20s\n" +
			"\t%-10s %20s\n" +
//			"\t%-10s %20s\n" +
			"\t%-10s %20s\n" +
			"\t%-10s %20s\n" +
			"\t%-10s %20s\n" +
			"\t%-10s %20s\n" +
//			"\t%-10s %20s\n" +
			"\t%-10s %20s\n" +
			"\t%-10s %20s\n" +
			"\t%-10s %20s\n" +
			"\t%-10s %20s\n" +
			"\t%-10s %20s\n" +
			"\t%-10s %20s\n" +
			"\t%-10s %20s\n" +
			"\t===============================\n",
			"Statistic",    "Value",
			"n",                    format(stats.getN(), 0),
			"sum",                  format(stats.getSum(),0),
			"mean",                 format(stats.getArithmeticMean(), 2),
			"sample sd",            format(stats.getStandardDeviation(), 2),
//			"variance",             format(stats.getVariance(), 2),
			"CoV",					format(stats.getCoefficientOfVariation(), 2),
			"median",               format(stats.getMedian(), 2),
			"mode",                 format(stats.getMode(), 2),
//			"N50",					format(stats.getN50(), 0),
			"q1 (R 6)",             format(stats.getLowerQuartile(),2),
			"q3 (R 6)",             format(stats.getUpperQuartile(),2),
			"2.5%",					format(stats.getTwoPointFivePercentile(),0),
			"97.5%",				format(stats.getNinetySevenPointFivePercentile(),0),
			"min",                  format(stats.getMinimum(),0),
			"max",                  format(stats.getMaximum(),0),
			"range",                format(stats.getRange(),0)

			);
		
		
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	public static void printAsRow(final Stats stats, final PrintStream out) {
	
		out.printf(
			
			"%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n",
			stats.getId(),
			format(stats.getN(), 0),
			format(stats.getSum(),0),
			format(stats.getArithmeticMean(), 2),
			format(stats.getStandardDeviation(), 2),
//			format(stats.getVariance(), 2),
			format(stats.getCoefficientOfVariation(), 2),
			format(stats.getMedian(), 2),
			format(stats.getMode(), 2),
//			format(stats.getN50(), 0),
			format(stats.getLowerQuartile(),2),
			format(stats.getUpperQuartile(),2),
			format(stats.getTwoPointFivePercentile(),0),
			format(stats.getNinetySevenPointFivePercentile(),0),
			format(stats.getMinimum(),0),
			format(stats.getMaximum(),0),
			format(stats.getRange(),0)

			);
		
		
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	public static void printUnformattedRowHeader(final PrintStream out) {
		
		out.printf(
			
			"# %s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n",
			"id",
			"N", 
			"sum",
			"mean",
			"sd",
//			"var",
			"C",
			"median",
			"mode",
//			"N50",
			"q1",
			"q3",
			"2.5%",
			"97.5%",
			"min",
			"max",
			"range"
			);
		
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	public static void printAsUnformattedRow(final Stats stats, final PrintStream out) {
	
		out.printf(
			
			"%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n",
			stats.getId(),
			stats.getN(), 
			stats.getSum(),
			stats.getArithmeticMean(),
			stats.getStandardDeviation(),
//			stats.getVariance(),
			stats.getCoefficientOfVariation(),
			stats.getMedian(),
			stats.getMode(),
//			stats.getN50(),
			stats.getLowerQuartile(),
			stats.getUpperQuartile(),
			stats.getTwoPointFivePercentile(),
			stats.getNinetySevenPointFivePercentile(),
			stats.getMinimum(),
			stats.getMaximum(),
			stats.getRange()
			);
		
		} // End of method.
	
	//##########################################################################
	
	// INNER CLASSES
	
	//##########################################################################

	} // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

