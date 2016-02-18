//==============================================================================
//
//   AssessingTargetRegionCoverage.java
//   Created: Nov 21, 2011 at 2:47:35 PM
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
import nestor.util.ProcessTimer;
import nestor.util.JoptsimpleUtil;
import nestor.util.MutableInteger;
import java.util.regex.*;
import nestor.stats._Calc;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 * 
 * @author  Kevin Ashelford.
 */
public class AssessingTargetRegionCoverage implements Constants {

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
			

			// Pileup infile
			//------------------------------------------------------------------
			final ArrayList<String> pileupFileSynonyms = new ArrayList<String>();
			pileupFileSynonyms.add("p");
			pileupFileSynonyms.add("pileup");

			optionParser.acceptsAll(
				pileupFileSynonyms,
				"pileup infile (required)."
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------


			// Results outfile
			//------------------------------------------------------------------
			final ArrayList<String> resultsFileSynonyms = new ArrayList<String>();
			resultsFileSynonyms.add("r");
			resultsFileSynonyms.add("results");
			optionParser.acceptsAll(
				resultsFileSynonyms,
				"Results file as a properties file (optional: will write to "
				+ "STDOUT if not provided)."
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

			
	
			// Flanking region
			//------------------------------------------------------------------
			final ArrayList<String> flankingRegionSynonyms = new ArrayList<String>();
			flankingRegionSynonyms.add("e");
			flankingRegionSynonyms.add("extra");

			optionParser.acceptsAll(
				flankingRegionSynonyms,
				"Extra flanking region to include in calculation."
				).withRequiredArg().ofType(Integer.class);
			//------------------------------------------------------------------
		
			
			// Is deep.
			//------------------------------------------------------------------
			final ArrayList<String> strandSynonyms = new ArrayList<String>();
			strandSynonyms.add("d");
			strandSynonyms.add("deep");

			optionParser.acceptsAll(
				strandSynonyms,
				"If true, ultra deep sequencing is assumed (default, false)."
				).withRequiredArg().ofType(Boolean.class);
			//------------------------------------------------------------------
			
			
			
			// Obtain and process arguments
			OptionSet options = optionParser.parse(args);

			// Action infile arguments.
			if (options.has("help")) {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}

			
			// bam infile must always be provided.
			File pileupFile = null;
			if (options.has("pileup")) {
				pileupFile = new File((String)options.valueOf("pileup"));
				JoptsimpleUtil.checkFileExists(pileupFile);
				}
			else {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}

			
			// Results outfile may be provided.
			File resultsFile = null;
			if (options.has("results")) {
				resultsFile = new File((String)options.valueOf("results"));
				}
			//else {
			//	JoptsimpleUtil.displayHelp(optionParser);
			//	System.exit(0);
			//	}
			
			
			// bed infile must always be provided.
			File bedFile = null;
			if (options.has("features")) {
				bedFile = new File((String)options.valueOf("features"));
				JoptsimpleUtil.checkFileExists(bedFile);
				}
			else {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}
			

			
			// Flanking region must always be provided.
			int flankingRegion = 0;
			if (options.has("extra")) {
				flankingRegion = (Integer)options.valueOf("extra");
				}
			else {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}
			
			
			// Assume normal exome capture.
			boolean isDeep = false;
			if (options.has("deep")) {
				isDeep = (Boolean)options.valueOf("deep");
				}
			

			// Run program.
			new AssessingTargetRegionCoverage(
				pileupFile,
				resultsFile,
				bedFile,
				flankingRegion,
				isDeep
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
	 * Creates a new instance of <code>AssessingTargetRegionCoverage</code>.
	 */
	public AssessingTargetRegionCoverage(
		final File pileupFile,
		final File resultsFile,
		final File bedFile,
		final int flankingRegion,
		final boolean isDeep
		) throws IOException, Exception {
		
		
		// PROCESS BED FILE.
		// TODO - wrap as separate class.
		final BedData ontarget = new BedData();
		final BedData flanking = new BedData();
		
		Matcher bedRegex = Pattern.compile("^(\\S+)\\t(\\d+)\\t(\\d+).*$").matcher("");
		Matcher ignoreRegex = Pattern.compile("^track.*$").matcher("");
		
		BufferedReader bedIn = new BufferedReader(new FileReader(bedFile));
		String line = null;
		while ((line = bedIn.readLine()) != null) {
			
			if (ignoreRegex.reset(line).matches()) {
				continue;
				}
			
			else if (bedRegex.reset(line).matches()) {
				final String chr	= bedRegex.group(1);
				final int start		= Integer.parseInt(bedRegex.group(2)) + 1;
				final int end		= Integer.parseInt(bedRegex.group(3));
				
				
				// On target region 
				for (int i = start; i <= end; i++) {
					ontarget.create(chr, i, 0);
					}
				
				
				if (flankingRegion > 0) { 
				
					// Flanking region before
					int a = start - flankingRegion;
					int b = start - 1;
					for (int i = a; i <= b; i++) {
						flanking.create(chr, i, 0);
						}
				

					// Flanking region after
					a = end + flankingRegion;
					b = end + 1;
					for (int i = a; i <= b; i++) {
						flanking.create(chr, i, 0);
						}
				
				}
				
	
				} 
			else {
				throw new Exception("Unexpected line encountered '" + line + "'");
				}
			
			
			}
		bedIn.close();
		
		
		// Read Pileup
		
		Matcher pileupRegex = Pattern.compile("^(\\S+)\\t(\\d+)\\t(\\w)\\t(\\d+)").matcher("");
		
		BufferedReader pileupIn = new BufferedReader(new FileReader(pileupFile));
		
		String pline = null;
		while ((pline = pileupIn.readLine()) != null) {
			
			if (pileupRegex.reset(pline).find()) {
				String chr = pileupRegex.group(1);
				int pos = Integer.parseInt(pileupRegex.group(2));
				int cov = Integer.parseInt(pileupRegex.group(4));
				
				if (ontarget.exists(chr, pos)) {
					ontarget.setValue(chr, pos, cov);
					}
				
				if (flanking.exists(chr, pos)) {
					flanking.setValue(chr, pos, cov);						
					}
				
				
				}
			else {
				throw new Exception("Regex failed with line '" + pline + "'");
				}
			
			}
		
		
		pileupIn.close();
		
		
		// Write to outfile or, if none provided, STDOUT.
		PrintStream out = null;
		// No outfile specified, so write to STDOUT.
		if (resultsFile == null) {out = System.out;}
		// Outfile specified.
		else {out = new PrintStream(resultsFile);}
		
		//final PrintWriter out = new PrintWriter(new FileWriter(resultsFile, true));
		
		if (isDeep) {
			processDeep(ontarget, out, ".ontarget");
			
			if (flankingRegion > 0) 			
				processDeep(flanking, out, ".flanking");
			} 
		else {
			process(ontarget, out, ".ontarget");
			
			if (flankingRegion > 0)
				process(flanking, out, ".flanking");
			}
		
		out.close();
		
		} // End of constructor.
	
	//##########################################################################
	
	// METHODS
	
	private void process(
			final BedData region, 
			final PrintStream out, 
			final String tag
			) {
		
		// Calculate:
		//		region mean coverage depth
		//		% of region covered
		//		Fraction of region covered >= 4x, 10x, 20x, 30x
		
		int n = 0;
		long sum = 0;
		long above0x = 0;
		long above4x = 0;
		long above10x = 0;
		long above20x = 0;
		long above30x = 0;
		long above40x = 0;
		long above50x = 0;
		
		HashMap<String, HashMap<Integer, MutableInteger>> data = 
			region.getUnderlyingDataStructure();
		
		
		// Assess number of loci covered by target region
		int lociCount = 0;
		Iterator<String> it = data.keySet().iterator();
		while (it.hasNext()) {
			String chr = it.next();
			lociCount += data.get(chr).size();
			}
		// Create array of this size.
		long[] covData = new long[lociCount];
		
		
		
		
		Iterator<String> chrIterator = data.keySet().iterator();
		while (chrIterator.hasNext()) {
			
			String chr = chrIterator.next();
			
			Iterator<Integer> posIterator = data.get(chr).keySet().iterator();
			while (posIterator.hasNext()) {
				
				Integer pos = posIterator.next();
				
				long cov = data.get(chr).get(pos).value();
				
				// Add to array.
				covData[n] = cov;
				
				n++;
				sum += cov;
				if (cov > 0)	above0x++;
				if (cov >= 4)	above4x++;
				if (cov >= 10)	above10x++;
				if (cov >= 20)	above20x++;
				if (cov >= 30)	above30x++;
				if (cov >= 40)  above40x++;
				if (cov >= 50)  above50x++; 
				
				}
			
			
			}
		
		
		// Sort Array		
		Arrays.sort(covData);
		
		// Calculate non-parametric metrics
		double median = _Calc.getMedian(covData);
		double q1 = _Calc.getLowerQuartile(covData);
		double q3 = _Calc.getUpperQuartile(covData); 
		
		// Write to outfile
		out.println(MEAN_COVERAGE		+ tag + "=" + (double)sum/(double)n);
		out.println(MEDIAN_COVERAGE			+ tag + "=" + median);
		out.println(Q1_COVERAGE				+ tag + "=" + q1);
		out.println(Q3_COVERAGE				+ tag + "=" + q3);
		out.println(REGION_COVERED		+ tag + "=" + (double)above0x/(double)n);
		out.println(REGION_COVERED_4x	+ tag + "=" + (double)above4x/(double)n);
		out.println(REGION_COVERED_10x	+ tag + "=" + (double)above10x/(double)n);
		out.println(REGION_COVERED_20x	+ tag + "=" + (double)above20x/(double)n);
		out.println(REGION_COVERED_30x	+ tag + "=" + (double)above30x/(double)n);
		out.println(REGION_COVERED_40x	+ tag + "=" + (double)above40x/(double)n);
		out.println(REGION_COVERED_50x	+ tag + "=" + (double)above50x/(double)n);
		
		
		
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	private void processDeep(
			final BedData region, 
			final PrintStream out, 
			final String tag
			) {
		
		
		int n = 0;
		long sum = 0;
		long above0x = 0;
		long above100x = 0;
		long above1000x = 0;
		long above2000x = 0;
		long above3000x = 0;
		
		
		HashMap<String, HashMap<Integer, MutableInteger>> data = 
			region.getUnderlyingDataStructure();
		
		
		
		// Assess number of loci covered by target region
		int lociCount = 0;
		Iterator<String> it = data.keySet().iterator();
		while (it.hasNext()) {
			String chr = it.next();
			lociCount += data.get(chr).size();
			}
		// Create array of this size.
		long[] covData = new long[lociCount];
		
		
		
		
		Iterator<String> chrIterator = data.keySet().iterator();
		while (chrIterator.hasNext()) {
			
			String chr = chrIterator.next();
			
			
			Iterator<Integer> posIterator = data.get(chr).keySet().iterator();
			
			while (posIterator.hasNext()) {
				
				
				Integer pos = posIterator.next();
				
				long cov = data.get(chr).get(pos).value();
				
				// Add to array.
				covData[n] = cov;
				
				
				n++;
				sum += cov;
				if (cov > 0)		above0x++;
				if (cov >= 100)		above100x++;
				if (cov >= 1000)	above1000x++;
				if (cov >= 2000)	above2000x++;
				if (cov >= 3000)	above3000x++;
				
				}
			
			
			}
		
		
		// Sort Array		
		Arrays.sort(covData);
		
		// Calculate non-parametric metrics
		double median = _Calc.getMedian(covData);
		double q1 = _Calc.getLowerQuartile(covData);
		double q3 = _Calc.getUpperQuartile(covData); 
		
		// Write to outfile
		out.println(MEAN_COVERAGE			+ tag + "=" + (double)sum/(double)n);
		out.println(MEDIAN_COVERAGE			+ tag + "=" + median);
		out.println(Q1_COVERAGE				+ tag + "=" + q1);
		out.println(Q3_COVERAGE				+ tag + "=" + q3);
		out.println(REGION_COVERED			+ tag + "=" + (double)above0x/(double)n);
		out.println(REGION_COVERED_100x		+ tag + "=" + (double)above100x/(double)n);
		out.println(REGION_COVERED_1000x	+ tag + "=" + (double)above1000x/(double)n);
		out.println(REGION_COVERED_2000x	+ tag + "=" + (double)above2000x/(double)n);
		out.println(REGION_COVERED_3000x	+ tag + "=" + (double)above3000x/(double)n);
		
		
		
		
		} // End of method.
	
	//##########################################################################
	
	// INNER CLASSES
	
	//##########################################################################

	} // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
