//==============================================================================
//
//   SimpleExomePileup.java
//   Created: Nov 23, 2011 at 1:17:09 PM
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
import nestor.io.*;
import nestor.io.bed.*;
import nestor.sam.feature.*;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 * 
 * @author  Kevin Ashelford.
 */
public class SimpleExomePileup {

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


			// bed infile
			//------------------------------------------------------------------
			final ArrayList<String> featuresSynonyms = new ArrayList<String>();
			featuresSynonyms.add("f");
			featuresSynonyms.add("features");
			optionParser.acceptsAll(
				featuresSynonyms,
				"Feature file in bed format (required)."
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------
			
			
			// Simple pileup outfile
			//------------------------------------------------------------------
			final ArrayList<String> outfileSynonyms = new ArrayList<String>();
			outfileSynonyms.add("o");
			outfileSynonyms.add("out");
			optionParser.acceptsAll(
				outfileSynonyms,
				"Simple pileup outfile or directory as appropriate (required)."
				).withRequiredArg().ofType(String.class);
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
			
			
			// bed infile is required.
			File bedfile = null;
			if (options.has("features")) {
				bedfile = new File((String)options.valueOf("features"));
				JoptsimpleUtil.checkFileExists(bedfile);
				}
			else {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}
			
			
			// simple pileup outfile is required.
			File outfile = null;
			if (options.has("out")) {
				outfile = new File((String)options.valueOf("out"));
				}
			else {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}
			
			
			
			
			// Run program.
			new SimpleExomePileup(
				bamfile,
				bedfile,
				outfile
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
	 * Creates a new instance of <code>SimpleExomePileup</code>.
	 */
	public SimpleExomePileup(
		final File bamfile,
		final File bedfile,
		final File outfile
		) throws IOException, Exception {
		
		
		final HashMap<String, ArrayList<IFeature>> features = getFeatures(bedfile);
		

		// Open output stream.
		final PrintWriter out = new PrintWriter(new FileWriter(outfile));
		
		PileupHandler handler = new PileupHandler(){
			public void process(int[] coverage, String chr){
				
				
				// Do nothing if chr not represented in feature collection.
				if (!features.containsKey(chr)) return;
				
				
				
				// Next for each feature, identify relevant region and calculate stats
				// on that region only.
				ArrayList<IFeature> relevantFeatures = features.get(chr);
				for (IFeature feature : relevantFeatures) {

					final String featureId	= feature.getAttribute(AttributeType.TranscriptId);
					final int start			= feature.getStart()	- 1; // i-indexed feature -
					final int end			= feature.getEnd()		   ; // convert to zero index for array

					int [] coverageSlice = null;
					coverageSlice = Arrays.copyOfRange(coverage, start, end + 1);
					
					for (int i = 0; i < coverageSlice.length; i++) {
						int pos = i + 1;
						int cov = coverageSlice[i];
						out.println(chr + "\t" + featureId + "\t" + pos + "\t" + cov);
						}
					
					
			
					} // End of foreach loop - no more features
				
				
								
			}};
		
		Pileup pileup = new Pileup();
		pileup.run(bamfile, handler);
		
		out.close();
		
		} // End of constructor.
	
	//##########################################################################
	
	// METHODS

	private HashMap<String, ArrayList<IFeature>> getFeatures(File featureFile) 
		throws IOException {

		BufferedReader featureIn = 
			new BufferedReader(new FileReader(featureFile));

		DefaultBedReaderHandler handler = new DefaultBedReaderHandler();
		IReader reader = BedReaderFactory.createReader();
		reader.read(featureIn, handler);
		featureIn.close();

		// Get list of features to process.
		return handler.getFeatures();
		
		} // End of method.

	
	//##########################################################################
	
	// INNER CLASSES
	
	//##########################################################################

	} // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
