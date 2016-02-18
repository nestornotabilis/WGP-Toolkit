//==============================================================================
//
//   PileupPlots.java
//   Created: Nov 22, 2011 at 4:17:21 PM
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

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 * 
 * @author  Kevin Ashelford.
 */
public class PileupPlots {

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
			new PileupPlots(
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
	 * Creates a new instance of <code>PileupPlots</code>.
	 */
	public PileupPlots(
		final File bamfile,
		final File outdir,
		final int window
		) throws IOException {
		
		// Create outdirectory
		if (!outdir.mkdir()) 
			throw new IOException(
				"Could not create directory '" + outdir.getName() + "'"
				);
		
		
		PileupHandler handler = new PileupHandler(){
			public void process(int[] coverage, String chr) throws IOException {
					
				File outfile = 
					new File(
						outdir.getAbsolutePath() + 
						File.separator + 
						chr + 
						".dat"
						);
				
				final PrintWriter out = new PrintWriter(new FileWriter(outfile));
				
				
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
				
			}};
		
		Pileup.run(bamfile, handler);
		
		} // End of constructor.
	
	//##########################################################################
	
	// METHODS
	
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
	
	//##########################################################################
	
	// INNER CLASSES
	
	//##########################################################################
	
	} // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
