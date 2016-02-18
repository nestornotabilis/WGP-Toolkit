//==============================================================================
//
//   SumBedRegions.java
//   Created: Nov 23, 2011 at 5:08:43 PM
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
import nestor.io.simplebed.*;
import java.util.regex.*;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 * 
 * @author  Kevin Ashelford.
 */
public class SumBedRegions implements Constants {

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
			
			
			// bed infile
			//------------------------------------------------------------------
			final ArrayList<String> featuresSynonyms = new ArrayList<String>();
			featuresSynonyms.add("f");
			featuresSynonyms.add("features");

			optionParser.acceptsAll(
				featuresSynonyms,
				"Features infile in BED format (required)."
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------


			// Results outfile
			//------------------------------------------------------------------
			final ArrayList<String> resultsFileSynonyms = new ArrayList<String>();
			resultsFileSynonyms.add("r");
			resultsFileSynonyms.add("results");
			optionParser.acceptsAll(
				resultsFileSynonyms,
				"Results file as a properties file (required)."
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------
					
			
			
			// Obtain and process arguments
			OptionSet options = optionParser.parse(args);

			// Action infile arguments.
			if (options.has("help")) {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}

			
			// Results outfile must always be provided.
			File resultsFile = null;
			if (options.has("results")) {
				resultsFile = new File((String)options.valueOf("results"));
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
			
			
			
			// Run program.
			new SumBedRegions(
				bedfile,
				resultsFile
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
	 * Creates a new instance of <code>SumBedRegions</code>.
	 */
	public SumBedRegions(
		final File bedfile,
		final File resultsFile
		) throws IOException, Exception {
		
		BufferedReader in = new BufferedReader(new FileReader(bedfile));
		SimpleBedReaderHandler handler = new SimpleBedReaderHandler();
		IReader reader = BedReaderFactory.createReader();
		reader.read(in, handler);
		in.close();
		
		PrintWriter out = new PrintWriter(new FileWriter(resultsFile, true));
		out.println(TARGET_SIZE + "=" + handler.getBedData().getSize());
		out.close();
		
		} // End of constructor.
	
	//##########################################################################
	
	// METHODS
	
	//##########################################################################
	
	// INNER CLASSES
	
	//##########################################################################

	} // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
