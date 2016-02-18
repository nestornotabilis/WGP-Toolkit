//==============================================================================
//
//   SimplePileup.java
//   Created: Nov 22, 2011 at 9:58:21 AM
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
import java.util.regex.*;


////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 * 
 * @author  Kevin Ashelford.
 */
public class SimplePileup {

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


			// Simple pileup outfile
			//------------------------------------------------------------------
			final ArrayList<String> outfileSynonyms = new ArrayList<String>();
			outfileSynonyms.add("o");
			outfileSynonyms.add("out");
			optionParser.acceptsAll(
				outfileSynonyms,
				"Simple pileup outfile or directory as appropriate (optional: "
				+ " will write to STDOUT if not provided)."
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------
			
			// BED infile
			//------------------------------------------------------------------
			final ArrayList<String> bedFileSynonyms = new ArrayList<String>();
			bedFileSynonyms.add("f");
			bedFileSynonyms.add("features");
			optionParser.acceptsAll(
				bedFileSynonyms,
				"Features file in BED format (optional)."
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------
			
		
			// Ignore zero coverage?
			//------------------------------------------------------------------
			final ArrayList<String> minCovSynonyms = new ArrayList<String>();
			minCovSynonyms.add("m");
			minCovSynonyms.add("minimum");
			optionParser.acceptsAll(
				minCovSynonyms,
				"Minimum coverage to be reported (optional: default zero)."
				).withRequiredArg().ofType(Integer.class);
			//------------------------------------------------------------------
			
			
			// Split my chromosome
			//------------------------------------------------------------------
			final ArrayList<String> splitSynonyms = new ArrayList<String>();
			splitSynonyms.add("s");
			splitSynonyms.add("split");
			optionParser.acceptsAll(
				splitSynonyms,
				"Split output by chromosome (optional: detault false)"
				).withRequiredArg().ofType(Boolean.class);
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
			
			
			// simple pileup outfile is required.
			File outfile = null;
			if (options.has("out")) {
				outfile = new File((String)options.valueOf("out"));
				JoptsimpleUtil.checkFileDoesNotExist(outfile);
				}
			
			
			// bed infile.
			File bedfile = null;
			if (options.has("features")) {
				bedfile = new File((String)options.valueOf("features"));
				JoptsimpleUtil.checkFileExists(bedfile);
				}
			
			
			// minimum coverage to be reported.
			int minCoverage = 0;
			if (options.has("minimum")) {
				minCoverage = (Integer)options.valueOf("minimum");
				}
			
			
			// Split output by chromosome.
			boolean split = false;
			if (options.has("split")) {
				split = (Boolean)options.valueOf("split");
				}
			
			if (split == true && outfile == null) {
				outfile = new File("split_out");
				}
			
			
			// Run program.
			new SimplePileup(
				bamfile,
				outfile,
				minCoverage,
				split,
				bedfile
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
	 * Creates a new instance of <code>SimplePileup</code>.
	 */
	public SimplePileup(
		final File bamfile,
		final File outfile,
		final int minCoverage,
		final boolean split,
		final File bedfile
		) throws IOException, Exception {
		
		
		if (split) {
			generateMultiFileOutput(bamfile, outfile, minCoverage);
			}
		else {
			generateSingleFileOutput(bamfile, outfile, minCoverage, bedfile);
			}
		
		
		} // End of constructor.
	
	//##########################################################################
	
	// METHODS
	
	private void generateSingleFileOutput(
		final File bamfile,
		final File outfile,
		final int minCoverage,
		final File bedFile
		) throws IOException, Exception {
		
		
		
		// PROCESS BED FILE if exsists.
		//----------------------------------------------------------------------
		final BedData bedData = new BedData();
		
		if (bedFile != null) {
			Matcher bedRegex = Pattern.compile("^(\\S+)\\t(\\d+)\\t(\\d+).*$").matcher("");
			Matcher ignoreRegex = Pattern.compile("^track.*$").matcher("");
		
			BufferedReader bedIn = new BufferedReader(new FileReader(bedFile));
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
						//bedData.increment(chr, i);
						bedData.create(chr, i, 0);
						}
				
					} 
				else {
					throw new IOException(
						"Unexpected line encountered '" + line + "'"
						);
					}
			
			
				}
			bedIn.close();
			}
		//----------------------------------------------------------------------
		
		
		
		
		
		PileupHandler handler;
		
		// scenario A - writing to STDOUT.
		if (outfile == null) {
		
		
			handler = new PileupHandler(){
				public void process(int[] coverage, String chr) throws IOException {
				
				// PROBLEM: coverage array represents ENTIRE chromosome regardless
				// of what bedfile, if used, might describe. 	
				for (int i = 0; i < coverage.length; i++) {
					int pos = i + 1;
					int cov = coverage[i];
					
					// IF bed file data exists
					// Do nothing if not covered by bed file - 
					// TODO - needs rewriting - poor design. See above comment.
					if (bedFile != null && !bedData.exists(chr, pos)) continue;
					
					
					if (cov >= minCoverage)
						System.out.println(chr + "\t" + pos + "\t" + "N\t" + cov);
						}
				}};
		
			
			}
		
		// Scenario B - writing to outfile	
		else {
				
			
			
			
			
			handler = new PileupHandler(){
				public void process(int[] coverage, String chr) throws IOException {
							
					
				
				// appending to outfile! 	
				final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outfile, true)));
				
				// PROBLEM: coverage array represents ENTIRE chromosome regardless
				// of what bedfile, if used, might describe. 	
				for (int i = 0; i < coverage.length; i++) {
					int pos = i + 1;
					int cov = coverage[i];
					
					// IF bed file data exists
					// Do nothing if not covered by bed file - 
					// TODO - needs rewriting - poor design. See above comment.
					if (bedFile != null && !bedData.exists(chr, pos)) continue;
					
					if (cov >= minCoverage)
						out.println(chr + "\t" + pos + "\t" + "N\t" + cov);
						}
				
				out.close();
				}};
		
			
			}
			
		
		
		Pileup pileup = new Pileup();
		
		if (bedData.getSize() > 0 ) {
			pileup.run(bamfile, bedData, handler);
			}
		else {
			pileup.run(bamfile, handler);
			}
		
		
		
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	private void generateMultiFileOutput(
		final File bamfile,
		final File outdir,
		final int minCoverage
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
						".pileup"
						);
				
				final PrintWriter out = new PrintWriter(new FileWriter(outfile));
				
				
				for (int i = 0; i < coverage.length; i++) {
					int pos = i + 1;
					int cov = coverage[i];
					if (cov >= minCoverage) 
						out.println(chr + "\t" + pos + "\t" + "N\t" + cov);
					}
				
				out.close();
				
			}};
		
		
		Pileup pileup = new Pileup();
		pileup.run(bamfile, handler);
		
		} // End of method.
	
	//##########################################################################
	
	// INNER CLASSES
	
	//##########################################################################

	} // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
