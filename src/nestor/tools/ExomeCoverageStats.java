//==============================================================================
//
//   ExomeCoverageStats.java
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
import nestor.stats.*;
import nestor.graphics.newplot.*;
import nestor.io.histogram.*;
import java.util.regex.*;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 * 
 * @author  Kevin Ashelford.
 */
public class ExomeCoverageStats {

	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS
	
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

			
			// BED infile
			//------------------------------------------------------------------
			final ArrayList<String> bedFileSynonyms = new ArrayList<String>();
			bedFileSynonyms.add("f");
			bedFileSynonyms.add("features");
			optionParser.acceptsAll(
				bedFileSynonyms,
				"Features file in BED format (required)."
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
			
			
			// bed infile must always be provided.
			File bedfile = null;
			if (options.has("features")) {
				bedfile = new File((String)options.valueOf("features"));
				JoptsimpleUtil.checkFileExists(bedfile);
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
			new ExomeCoverageStats(
				bamfile,
				bedfile,
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
	public ExomeCoverageStats(
		final File bamfile,
		final File bedfile,
		final File outdir,
		final int window
		) throws IOException, Exception {
		
		// First create parent output directory
		if (!outdir.mkdir()) 
			throw new IOException(
				"Could not create directory '" + outdir.getAbsolutePath() + "'"
				);
		
		
		// Starts metrics file with header
			final PrintStream out = new PrintStream(
				new BufferedOutputStream(
					new FileOutputStream(
						outdir.getAbsolutePath() + File.separator + "summary.dat", true
						)
					)
				);
			DisplayStats.printUnformattedRowHeader(out);
			out.close();
		
			
		//============
			
		// PROCESS BED FILE.
		final BedData bedData = new BedData();
		
		Matcher bedRegex = Pattern.compile("^(\\S+)\\t(\\d+)\\t(\\d+)\\t.*$").matcher("");
		Matcher ignoreRegex = Pattern.compile("^track.*$").matcher("");
		
		BufferedReader bedIn = new BufferedReader(new FileReader(bedfile));
		String line = null;
		while ((line = bedIn.readLine()) != null) {
			
			if (ignoreRegex.reset(line).matches()) {
				continue;
				}
			
			else if (bedRegex.reset(line).matches()) {
				final String chr = bedRegex.group(1);
				final int start = Integer.parseInt(bedRegex.group(2)) + 1;
				final int end = Integer.parseInt(bedRegex.group(3));
				
				for (int i = start; i <= end; i++) {
					bedData.create(chr, i, 0);
					}
				
				} 
			else {
				throw new Exception("Unexpected line encountered '" + line + "'");
				}
		
			}
		bedIn.close();	

		//============
			
			
		
			
		// For each chromosome generate coverage pileup data and process.
		MyPileupHandler handler = new MyPileupHandler(outdir, window, bedData);
		Pileup.run(bamfile, handler);
		
		} // End of constructor.
	
	//##########################################################################
	
	// METHODS
	
	//##########################################################################
	
	// INNER CLASSES
	
	private class MyPileupHandler implements PileupHandler {
		
		private final File outdir;
		private final int window;
		private final BedData bedData;
		
		
		public MyPileupHandler(
			final File outdir,
			final int window,
			final BedData bedData
			) {
			this.outdir = outdir;
			this.window = window;
			this.bedData = bedData;
			
			
			}
		
		
		////////////////////////////////////////////////////////////////////////
		
		public void process(int[] coverage, String chr) throws IOException {
			
			// Do nothing if chromosome does not exist in features file.
			if (!bedData.contains(chr)) return;
			
			int[] overallCoverage = new int[bedData.getSize(chr)];
			int N = 0;

			// Consider only those bases that fall within exome within current chr.
			for (int i = 0; i < coverage.length; i++) {
				if (bedData.exists(chr, i)) {
					overallCoverage[N++] = coverage[i];
					} 
				}
			
			
			// Create directory specifically for id.
			final File dir = new File(outdir.getAbsolutePath() + File.separator + chr);
			dir.mkdir(); 
			
			// Generate window metrics
			final File outfile1 = new File(dir.getAbsolutePath() + File.separator + "slidingWindow.dat");
			final PrintWriter out1 = new PrintWriter(new FileWriter(outfile1));
			out1.println("# " + chr);
			writeHeader(out1);
			slidingWindowMetrics(overallCoverage, out1);
			out1.close();
			
			// Bin coverage depths
			final File outfile2 = new File(dir.getAbsolutePath() + File.separator + "coverage.hist");
			final PrintStream out2 = new PrintStream(outfile2);
			IHistogram histogram = createHistogram(overallCoverage, chr, 1);
			HistogramWriterFactory.createHistogramWriter(histogram).write(out2);
			out2.close();

			// Generate overall metrics
			final PrintStream out3 = new PrintStream(
				new BufferedOutputStream(
					new FileOutputStream(
						outdir.getAbsolutePath() + File.separator + "summary.dat", true
						)
					)
				);
			DisplayStats.printAsUnformattedRow(new Stats(overallCoverage, chr), out3);
			out3.close();
			
			} // End of method.
		
		////////////////////////////////////////////////////////////////////////
		
		private void slidingWindowMetrics(final int[] coverage, final PrintWriter out) {

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
			for (i = 0; i < size; i++) {

				//----------------------------------------------------------
				if ((i % window) == 0 && i != 0) {

					n = 0;
					Stats stats = new Stats(buffer);

					writeToOutfile(previousEnd+1, i, stats, out);
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

				out.close();
			
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
