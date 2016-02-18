//==============================================================================
//
//   DescriptiveStats.java
//   Created: Nov 23, 2011 at 3:25:54 PM
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
import java.util.regex.*;
import nestor.stats.*;
import java.text.NumberFormat;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 * 
 * @author  Kevin Ashelford.
 */
public class DescriptiveStats {

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
			
			
			// Infile
			//------------------------------------------------------------------
			final ArrayList<String> infileSynonyms = new ArrayList<String>();
			infileSynonyms.add("i");
			infileSynonyms.add("infile");

			optionParser.acceptsAll(
				infileSynonyms,
				"Infile containing INTEGER data to analyse in a single column."
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------


			
			
			
			// Obtain and process arguments
			OptionSet options = optionParser.parse(args);

			// Action infile arguments.
			if (options.has("help")) {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}

			
			// bed infile is required.
			File infile = null;
			if (options.has("infile")) {
				infile = new File((String)options.valueOf("infile"));
				JoptsimpleUtil.checkFileExists(infile);
				}
			else {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}
			
			
			
			
			// Run program.
			new DescriptiveStats(
				infile
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
	 * Creates a new instance of <code>DescriptiveStats</code>.
	 */
	public DescriptiveStats(final File infile) throws IOException {
		
		Matcher regex = Pattern.compile("^(\\d+)$").matcher("");  
		
		// Read data into array.
		ArrayList<Long> data = new ArrayList<Long>();
		
		BufferedReader in = new BufferedReader(new FileReader(infile));
		String line = null;
		while ((line = in.readLine()) != null) {
			
			if (regex.reset(line).matches()) {
				data.add(Long.parseLong(line));
				}
			else {
				throw new IOException(
					"Regex failed - expecting single column of integers"
					);
				}
			
			}
		in.close();
		
		// Generate table.
		DisplayStats.printAsTable(
			new Stats(data), 
			System.out
			);
		
		} // End of constructor.
	
	//##########################################################################
	
	// METHODS
	
	//##########################################################################
	
	// INNER CLASSES
	
	//##########################################################################

	} // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
