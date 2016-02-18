//==============================================================================
//
//   ColourspaceToFakespace.java
//   Created: Oct 30, 2012 at 3:46:42 PM
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
import nestor.util.solid.*;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
/**
 * 
 * @author  Kevin Ashelford.
 */
public class ColourspaceToFakespace implements Constants {

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
			

			// fasta infile
			//------------------------------------------------------------------
			final ArrayList<String> fastaFileSynonyms = new ArrayList<String>();
			fastaFileSynonyms.add("i");
			fastaFileSynonyms.add("infile");

			optionParser.acceptsAll(
				fastaFileSynonyms,
				"fasta infile."
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------
			
			
			// Results outfile
			//------------------------------------------------------------------
			final ArrayList<String> resultsFileSynonyms = new ArrayList<String>();
			resultsFileSynonyms.add("o");
			resultsFileSynonyms.add("outfile");
			optionParser.acceptsAll(
				resultsFileSynonyms,
				"Csfasta outfile."
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------
			
			
			
			
			// Obtain and process arguments
			OptionSet options = optionParser.parse(args);

			// Action infile arguments.
			if (options.has("help")) {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}

			
			
			
			// fasta infile must always be provided.
			File infile = null;
			if (options.has("infile")) {
				infile = new File((String)options.valueOf("infile"));
				JoptsimpleUtil.checkFileExists(infile);
				}
			else {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}

			
			// outfile may be provided.
			File outfile = null;
			if (options.has("outfile")) {
				outfile = new File((String)options.valueOf("outfile"));
				}
			else {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}
			
			

			// Run program.
			new ColourspaceToFakespace(
				infile,
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
	 * Creates a new instance of <code>ColourspaceToFakespace</code>.
	 */
	public ColourspaceToFakespace(
		final File infile,
		final File outfile
		) throws IOException {
		
		 IFileConvertor fc =
                        FileConvertorFactory.getFileConvertor(
                                FileConversion.ColourspaceToFakespace
                                );

                fc.convert(infile, outfile);
		
		
	} // End of constructor.
	//##########################################################################
	// METHODS
	//##########################################################################
	// INNER CLASSES
	//##########################################################################
} // End of class.
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
