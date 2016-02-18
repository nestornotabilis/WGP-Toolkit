//==============================================================================
//
//   CoverageStats.java
//   Created: Nov 25, 2011 at 4:38:24 PM
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

package nestor.tools;

import java.io.*;
import java.util.*;
import joptsimple.*;
import nestor.util.*;
import nestor.stats.DisplayCoverageStats;
import nestor.stats.Stats;
import nestor.stats.IHistogram;
import nestor.stats.HistogramFactory;
import nestor.graphics.newplot.*;
import nestor.io.histogram.*;
import java.text.NumberFormat;
import nestor.graphics.*;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 * 
 * @author  Kevin Ashelford.
 */
public class CoverageStats implements Constants {

	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS
	
	NumberFormat _formatter		= NumberFormat.getInstance();
	NumberFormat _intFormatter	= NumberFormat.getInstance();
	
	//##########################################################################
	
	// ENTRY POINT
	
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		
		ProcessTimer timer = new ProcessTimer();
		timer.start();
		
		try {

			final OptionParser optionParser = new OptionParser();


			// Help
			//------------------------------------------------------------------
			final ArrayList<String> helpSynonyms = new ArrayList<String>();
			helpSynonyms.add("h");
			helpSynonyms.add("help");
			optionParser.acceptsAll(helpSynonyms, "Generate help.");
			//------------------------------------------------------------------
			

			// sam/bam infile
			//------------------------------------------------------------------
			final ArrayList<String> bamFileSynonyms = new ArrayList<String>();
			bamFileSynonyms.add("b");
			bamFileSynonyms.add("bam");

			optionParser.acceptsAll(
				bamFileSynonyms,
				"Sorted SAM/BAM infile (required)."
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------


			// outdir
			//------------------------------------------------------------------
			final ArrayList<String> outSynonyms = new ArrayList<String>();
			outSynonyms.add("o");
			outSynonyms.add("out");
			optionParser.acceptsAll(
				outSynonyms,
				"Output directory (required)."
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------
			
			
			// window
			//------------------------------------------------------------------
			final ArrayList<String> windowSynonyms = new ArrayList<String>();
			windowSynonyms.add("w");
			windowSynonyms.add("window");
			optionParser.acceptsAll(
				windowSynonyms,
				"Sampling window (default 100000)."
				).withRequiredArg().ofType(Integer.class);
			//------------------------------------------------------------------
			
			
			
			// Obtain and process arguments
			OptionSet options = optionParser.parse(args);

			// Action infile arguments.
			if (options.has("help")) {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}

			
			// bam infile is required.
			File bamfile = null;
			if (options.has("bam")) {
				bamfile = new File((String)options.valueOf("bam"));
				JoptsimpleUtil.checkFileExists(bamfile);
				}
			else {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}
			
			
			// outdir is required.
			File outdir = null;
			if (options.has("out")) {
				outdir = new File((String)options.valueOf("out"));
				}
			else {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}
			
			
			// Sampling window.
			int window = 100000;
			if (options.has("window")) {
				window = (Integer)options.valueOf("window");
				}
			
			
			// Run program.
			new CoverageStats(
				bamfile,
				outdir,
				window
				);

			} 

	
			
		// Catch io exceptions.
		catch (IOException ioe) {
			System.err.println(
				"ERROR: " + ioe.getMessage()
				);
			}	
	
		// Catch all other more unexpected problems.
		catch (Exception e) {
			System.err.println(
				"ERROR: " + e.getMessage()
				);
			e.printStackTrace();
			}

			
			
			
		finally {
			
			// Report time elapsed.
			timer.stop();
			System.err.println(timer.getDuration());
			
			}
	
		
		
		} // End of main method.

	//##########################################################################
	
	// CONSTRUCTOR
	
	/**
	 * Creates a new instance of <code>CoverageStats</code>.
	 */
	public CoverageStats(
		final File bamfile,
		final File outdir,
		final int window
		) throws IOException {
		
		
		_formatter.setMinimumIntegerDigits(1);
        _formatter.setMinimumFractionDigits(2);
        _formatter.setMaximumFractionDigits(2);

		_intFormatter.setMinimumIntegerDigits(1);
        _intFormatter.setMinimumFractionDigits(0);
        _intFormatter.setMaximumFractionDigits(0);
		
		
		
		
		// First create parent output directory
		if (!outdir.mkdir()) 
			throw new IOException(
				"Could not create directory '" + outdir.getAbsolutePath() + "'"
				);
		
		
		// Start metrics file with header
		final File summaryFile = new File(outdir.getAbsolutePath() + File.separator + "summary.dat");
		final PrintStream out = new PrintStream(summaryFile);
		DisplayCoverageStats.printUnformattedRowHeader(out);
		out.close();
		
		// Start html index file.
		final File htmlFile = new File(outdir.getAbsolutePath() + File.separator + "index.html");
		PrintWriter htmlOut = new PrintWriter(new FileWriter(htmlFile));
		createHtmlHead(htmlOut, "");
		htmlOut.close();
		
		
		
		// For each chromosome generate coverage pileup data and process.
		MyPileupHandler handler = new MyPileupHandler(outdir, window, summaryFile, htmlFile);
		Pileup.run(bamfile, handler);

		// End html index file.
		htmlOut = new PrintWriter(new FileWriter(htmlFile, true));
		createHtmlTail(htmlOut);
		htmlOut.close();
		
		} // End of constructor.
	
	//##########################################################################
	
	// METHODS
	
	private void createHtmlHead(final PrintWriter out, final String title) {
		
		out.println("<html>");
		out.println("<body>");
		
		out.println("<h1>" + title + "</h1>");
		out.println("<table border=\"1\" cellspacing=\"1\" cellpadding=\"5\">");
			out.println("<thead>");
				out.println("<tr td style=\"vertical-align: top; background-color: rgb(204, 204, 204);\">");
					out.println("<th>Id</th>");
					out.println("<th>N</th>");
					out.println("<th>Mean</th>");
					out.println("<th>Median</th>");
					out.println("<th>sd</th>");
					out.println("<th>q1</th>");
					out.println("<th>q3</th>");
					out.println("<th>2.5%<br>percentile</th>");
					out.println("<th>97.5%<br>percentile</th>");
					out.println("<th>Min</th>");
					out.println("<th>Max</th>");
					out.println("<th>% covered</th>");
					
					out.println("<th>% covered<br>at 4x<br>or greater</th>");
					out.println("<th>% covered<br>at 10x<br>or greater</th>");
					out.println("<th>% covered<br>at 20x<br>or greater</th>");
					out.println("<th>% covered<br>at 30x<br>or greater</th>");
					
				out.println("</tr>");
			out.println("</thead>");
			out.println("<tbody>");
		
		
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	private void createHtmlTail(final PrintWriter out) {
		
		out.println("</tbody>");
		out.println("</table>");
		out.println("</body>");
		out.println("</html>");
		
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	private void createHtmlContent(final PrintWriter out, final Stats stats) {
		
		out.println("<tr>");
		out.println("<td><a href=\"" + stats.getId() + File.separator + "index.html\">" + stats.getId() + "</a></td>");
		out.println("<td>" + _intFormatter.format(stats.getN()) + "</td>");
		out.println("<td>" + _formatter.format(stats.getArithmeticMean()) + "</td>");
		out.println("<td>" + _formatter.format(stats.getMedian()) + "</td>");
		out.println("<td>" + _formatter.format(stats.getStandardDeviation()) + "</td>");
		out.println("<td>" + _formatter.format(stats.getLowerQuartile()) + "</td>");
		out.println("<td>" + _formatter.format(stats.getUpperQuartile()) + "</td>");
		out.println("<td>" + _formatter.format(stats.getTwoPointFivePercentile()) + "</td>");
		out.println("<td>" + _formatter.format(stats.getNinetySevenPointFivePercentile()) + "</td>");
		out.println("<td>" + _formatter.format(stats.getMinimum()) + "</td>");
		out.println("<td>" + _formatter.format(stats.getMaximum()) + "</td>");

		out.println("<td>" + _formatter.format(stats.getFractionCovered()*100) + "</td>");
		out.println("<td>" + _formatter.format(stats.getFractionCovered4x()*100) + "</td>");
		out.println("<td>" + _formatter.format(stats.getFractionCovered10x()*100) + "</td>");
		out.println("<td>" + _formatter.format(stats.getFractionCovered20x()*100) + "</td>");
		out.println("<td>" + _formatter.format(stats.getFractionCovered30x()*100) + "</td>");

		out.println("</tr>");

		} // End of method
	
	////////////////////////////////////////////////////////////////////////////
	
	private void createChrHtml(final PrintWriter out, final String chr) {
		
		out.println("<html>");
		out.println("<body>");
		
		out.println("<h1>" + chr + "</h1>");
		
		out.println("<img src=\"boxAndWhisker.png\" alt=\"Box and whisker plot\"/>");
		
		out.println("<img src=\"histogram.png\" alt=\"coverage histogram plot\"/>");
		
		out.println("<img src=\"cumulative_histogram.png\" alt=\"coverage histogram plot\"/>");
		
		out.println("<img src=\"coverage.png\" alt=\"coverage plot\"/>");
		
		out.println("</body>");
		out.println("</html>");
		
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	private int chooseXStartPosition(double x) {

		if (x - 1 < 0) {return 0;}

		else {
			return (int)x - 1;
			}

		} // end of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	private File createBoxAndWhisker(Stats stats, File outfile) throws IOException {

		String id = stats.getId();

		// Determine range of x axis.  This will depend on metric category.
		int from = chooseXStartPosition(stats.getTwoPointFivePercentile());
		int to = (int)stats.getNinetySevenPointFivePercentile();

		// Don't do anything if nothing worthwhile to plot.
		if (to == 0) {
			return null;
			}

		// Avoid instances where to and from are the same causing display to hang.
		if (from == to) to++;

		// This should vary according to size of graph widget.
		int plotWidth = HTML_PLOT_WIDTH;
		int plotHeight = HTML_BOX_AND_WHISKER_HEIGHT;
		int noOfTicks = (int)Math.ceil(plotWidth/78d);

		// Decide on increment - should be an integer.
		int increment = (int)Math.floor((double)(to-from)/(double)noOfTicks);

		int xMinorTicks = 1;

		// Modify if increment too small
		if (increment < 1) {
			to = from + (int)noOfTicks;
			increment = 1;
			xMinorTicks = 0;
			}

		StatsBoxAndWhiskerPlotter plotter = new StatsBoxAndWhiskerPlotter();
		plotter.createPng(
			stats,
			null,
			outfile,
			ScaleType.Linear,
			"Coverage depth",
			"",
			from,
			to,
			plotWidth,
			plotHeight,
			increment,
			xMinorTicks,
			false // TODO - unused - needs tidying
			);

		return outfile;

		} // End of method.

	////////////////////////////////////////////////////////////////////////////

	private File createHistogramPlot(
		final Stats stats,
		final IHistogram histogram,
		final boolean isCumulative,
		final File outfile
		) throws IOException {

		final String id = stats.getId();
		
		// Determine range of x axis.  This will depend on metric category.
		int from = chooseXStartPosition(stats.getTwoPointFivePercentile());
		int to = (int)stats.getNinetySevenPointFivePercentile();


		// Don't do anything if nothing worthwhile to plot.
		if (to == 0) {
			return null;
			}


		// Avoid instances where to and from are the same causing display to hang.
		if (from == to) to++;

		// This should vary according to size of graph widget.
		int plotWidth = HTML_PLOT_WIDTH;
		int plotHeight = HTML_PLOT_HEIGHT;
		int noOfTicks = (int)Math.ceil(plotWidth/78d);

		// Decide on increment - should be an integer.
		int increment = (int)Math.floor((double)(to-from)/(double)noOfTicks);

		int xMinorTicks = 1;

		// Modify if increment too small
		if (increment < 1) {
			to = from + (int)noOfTicks;
			increment = 1;
			xMinorTicks = 0;
			}

		StatsHistogramPlotter plotter = new StatsHistogramPlotter();
		plotter.createPng(
			stats,
			histogram,
			outfile,
			ScaleType.Linear,
			"Coverage depth",
			"% of reference",
			from,
			to,
			plotWidth,
			plotHeight,
			increment,
			xMinorTicks,
			isCumulative
			);

		return outfile;

		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	private File createLinePlot(
		final Stats stats,
		final IHistogram histogram,
		final boolean isCumulative,
		final File outfile
		) throws IOException {

		final String id = stats.getId();
		
		// Determine range of x axis.  This will depend on metric category.
		int from = 0;
		int to = (int)histogram.getCoordinateCollection().getMaximumX();



		// Avoid instances where to and from are the same causing display to hang.
		if (from == to) to++;

		// This should vary according to size of graph widget.
		int plotWidth = HTML_PLOT_WIDTH;
		int plotHeight = HTML_PLOT_HEIGHT;
		int noOfTicks = (int)Math.ceil(plotWidth/150d);

		// Decide on increment - should be an integer.
		int increment = (int)Math.floor((double)(to-from)/(double)noOfTicks);

		int xMinorTicks = 1;

		// Modify if increment too small
		if (increment < 1) {
			to = from + (int)noOfTicks;
			increment = 1;
			xMinorTicks = 0;
			}

		StatsLinePlotter plotter = new StatsLinePlotter();
		plotter.createPng(
			stats,
			histogram,
			outfile,
			ScaleType.Linear,
			"Position",
			"Coverage depth",
			from,
			to,
			plotWidth,
			plotHeight,
			increment,
			xMinorTicks,
			isCumulative
			);

		return outfile;

		} // End of method.
	
	//##########################################################################
	
	// INNER CLASSES
	
	private class MyPileupHandler implements PileupHandler {
		
		private final File outdir;
		private final int window;
		private final File summaryFile;
		private final File htmlFile; 
		
		public MyPileupHandler(
			final File outdir,
			final int window,
			final File summaryFile,
			final File htmlFile
			) {
			this.outdir = outdir;
			this.window = window;
			this.htmlFile = htmlFile;
			this.summaryFile = summaryFile;
			}
		
		
		////////////////////////////////////////////////////////////////////////
		
		public void process(int[] coverage, String chr) throws IOException {
			
			//-----------------------------------------------------------------
			// Create directory specifically for chr.
			final File chrDir = new File(outdir.getAbsolutePath() + File.separator + chr);
			if (!chrDir.mkdir()) 
			throw new IOException(
				"Could not create directory '" + chrDir.getAbsolutePath() + "'"
				);
			//------------------------------------------------------------------
			
			
			//------------------------------------------------------------------
			// Generate window metrics
			final File outfile1 = new File(chrDir.getAbsolutePath() + File.separator + "slidingWindow.dat");
			final PrintWriter out1 = new PrintWriter(new FileWriter(outfile1));
			out1.println("# " + chr);
			writeHeader(out1);
			IHistogram swHistogram = slidingWindowMetrics(coverage, out1);
			out1.close();
			//------------------------------------------------------------------
			
			
			//------------------------------------------------------------------
			// Bin coverage depths
			final File outfile2 = new File(chrDir.getAbsolutePath() + File.separator + "coverage.hist");
			final PrintStream out2 = new PrintStream(outfile2);
			IHistogram histogram = createHistogram(coverage, chr, 1);
			HistogramWriterFactory.createHistogramWriter(histogram).write(out2);
			out2.close();
			//------------------------------------------------------------------
			
			
			//------------------------------------------------------------------
			// Create an index html for specific chr.
			final File outfile5 = new File(chrDir.getAbsolutePath() + File.separator + "index.html");
			PrintWriter out5 = new PrintWriter(new FileWriter(outfile5));
			createChrHtml(out5, chr);
			out5.close();
			//------------------------------------------------------------------
			
			

			//------------------------------------------------------------------
			// Generate overall metrics
			final Stats stats = new Stats(coverage, chr);
			
			// Update summary text file
			final PrintStream out3 = new PrintStream(
				new BufferedOutputStream(new FileOutputStream(summaryFile, true))
				);
			DisplayCoverageStats.printAsUnformattedRow(stats, out3);
			out3.close();
			
			// Update summary HTML file
			final PrintWriter out4 = new PrintWriter(new FileWriter(htmlFile, true));
			createHtmlContent(out4, stats);
			out4.close();
			//------------------------------------------------------------------
			
			
			
			//------------------------------------------------------------------
			// Create box and whisker plot.
			final File outfile6 = new File(chrDir.getAbsolutePath() + File.separator + "boxAndWhisker.png");
			createBoxAndWhisker(stats, outfile6);
			
			
			// Generate histograms
			final File histOutfile = new File(chrDir.getAbsolutePath() + File.separator + "histogram.png");
			createHistogramPlot(stats, histogram, false, histOutfile);
	
			final File chistOutfile = new File(chrDir.getAbsolutePath() + File.separator + "cumulative_histogram.png");
			createHistogramPlot(stats, histogram, true, chistOutfile);
			
			// Create coverage line plot
			final File lineOutfile = new File(chrDir.getAbsolutePath() + File.separator + "coverage.png");
			createLinePlot(stats, swHistogram, false, lineOutfile);
			
			//------------------------------------------------------------------
			
			} // End of method.
		
		////////////////////////////////////////////////////////////////////////
		
		private IHistogram slidingWindowMetrics(final int[] coverage, final PrintWriter out) {
			
			int size = coverage.length;

			// Using primative array is ~50% faster than ArrayList.		
			long[] buffer;
			if (size < window) {
				buffer = new long[size];
				}
			else {
				buffer = new long[window];
				}

			int n = 0;
			int i;
			int previousEnd = 0;
			ArrayList<Coordinate> cArray = new ArrayList<Coordinate>();
			for (i = 0; i < size; i++) {

				//----------------------------------------------------------
				if ((i % window) == 0 && i != 0) {

					n = 0;
					Stats stats = new Stats(buffer);

					writeToOutfile(previousEnd+1, i, stats, out);
					
					// Add to histogram.
					cArray.add(new Coordinate(i, stats.getArithmeticMean()));
					
					
					previousEnd = i;

					// Account for last few bases.
					if (size - i+1 < window) {
						buffer = new long[size-i];
						}
					// Block of bases of size window to be processed. 	
					else {
						buffer = new long[window];
						}		
					}
				//----------------------------------------------------------

					buffer[n] = coverage[i];
					n++;

					
					
					} // End of for loop - no more bases.

				// Final emptying of buffer.
				Stats stats = new Stats(buffer);
				writeToOutfile(previousEnd+1, i, stats, out);
				cArray.add(new Coordinate(i, stats.getArithmeticMean()));
				
				
				out.close();
				
				Coordinate[] c = new Coordinate[cArray.size()];
				cArray.toArray(c);
				
				
				ICoordinateCollection cc =
				CoordinateCollectionFactory.createCoordinateCollection(
					c,
					"coverage"
					);

			return HistogramFactory.createHistogram(cc);
				
			
			} // End of method.
	
		////////////////////////////////////////////////////////////////////////
	
		private void writeHeader(
			final PrintWriter out
			) {
		
			out.println(
				
				"# " + 
				// 1
				"start\t" +
			
				// 2
				"end\t" + 

				// 3. N
				"N\t" +
				// 4. Sum
				"sum\t" +
				// 5. Mean
				"mean\t" +
				// 6. Median
				"median\t" +
				// 7. Mode
				"mode\t" +
				// 8. Variance
				"var\t" +
				// 9. SD
				"sd\t" +
				// 10. Coefficient of variation
				"C\t" +
				// 11. Lower quartile
				"q1\t" +
				// 12. Upper quartile
				"q3\t" +
				// 13. 2.5% percentile
				"2.5%\t" +
				// 14. 97.5% percentile
				"97.5%\t" +
				// 15. min
				"min\t" +
				// 16. max
				"max\t" +
				// 17. range
				"range"
				);
		
		
			} // End of method.
		
		////////////////////////////////////////////////////////////////////////
		
		private void writeToOutfile(
			final int start, 
			final int end,
			final Stats stats, 
			final PrintWriter out
			) {
		
			out.println(
				// 1
				start + "\t" +
			
				// 2
				end + "\t" + 

				// 3. N
				stats.getN() + "\t" +
				// 4. Sum
				stats.getSum() + "\t" +
				// 5. Mean
				stats.getArithmeticMean() + "\t" +
				// 6. Median
				stats.getMedian() + "\t" +
				// 7. Mode
				stats.getMode() + "\t" +
				// 8. Variance
				stats.getVariance() + "\t" +
				// 9. SD
				stats.getStandardDeviation() + "\t" +
				// 10. Coefficient of variation
				stats.getCoefficientOfVariation() + "\t" +
				// 11. Lower quartile
				stats.getLowerQuartile() + "\t" +
				// 12. Upper quartile
				stats.getUpperQuartile() + "\t" +
				// 13. 2.5% percentile
				stats.getTwoPointFivePercentile() + "\t" +
				// 14. 97.5% percentile
				stats.getNinetySevenPointFivePercentile() + "\t" +
				// 15. min
				stats.getMinimum() + "\t" +
				// 16. max
				stats.getMaximum() + "\t" +
				// 17. range
				stats.getRange()
				);
		
		
			} // End of method.
		
		////////////////////////////////////////////////////////////////////////
		
		private IHistogram createHistogram(
			final int[] data,
			final String label,
			final int binSize
			) {

			// Bin hashmap - key = bin-id, value = count.
			final HashMap<Integer, MutableInteger> bins =
				new HashMap<Integer, MutableInteger>();

			// Associate data values with appropriate bin number
			int largestBinNumber = 0;
			for (int x : data) {

				int binNumber = (int)Math.floor( (double)x / (double)binSize );
				if (binNumber > largestBinNumber) largestBinNumber = binNumber;

				MutableInteger value = bins.get(binNumber);
				if (value == null) {
					value = new MutableInteger();
					bins.put(binNumber, value);
					}
				value.increment();

				}

			// Store binned data in histogram object.
			Coordinate[] c = new Coordinate[largestBinNumber+1];
			for (int i = 0; i <= largestBinNumber; i++) {

				// Nothing in this bin - record as zero.
				if (bins.get(i) == null) {
					c[i] = new Coordinate(
						i*binSize,	
						0
						);
					}


				else {
					// Calculate frequency as a percentage of total ref size.
					c[i] =
						new Coordinate(
							(float)(i*binSize),		
							// % frequency.
							(float)(
								(float)(bins.get(i).value()) / (float)binSize
									/
								(float)data.length * 100f
								)
							);
					}

				}


			ICoordinateCollection cc =
				CoordinateCollectionFactory.createCoordinateCollection(
					c,
					label
					);

			return HistogramFactory.createHistogram(cc);

			} // End of method.
		
		} // End of inner class. 
	
	//##########################################################################
	
	} // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
