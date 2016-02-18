//==============================================================================
//
//   QC_BasicPipeline.java
//   Created: Nov 21, 2011 at 5:24:42 PM
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
import net.sf.samtools.*;
import java.util.regex.*;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 * 
 * @author  Kevin Ashelford.
 */
public class QC_BasicPipeline {

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
			

			// xsq infile
			//------------------------------------------------------------------
			final ArrayList<String> xsqFileSynonyms = new ArrayList<String>();
			xsqFileSynonyms.add("x");
			xsqFileSynonyms.add("xsq");

			optionParser.acceptsAll(
				xsqFileSynonyms,
				"Xsq infile (Optional)."
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------


			// Output directory
			//------------------------------------------------------------------
			final ArrayList<String> outdirSynonyms = new ArrayList<String>();
			outdirSynonyms.add("d");
			outdirSynonyms.add("directory");
			optionParser.acceptsAll(
				outdirSynonyms,
				"Output directory (required)."
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------
			
			
			
			// Reference
			//------------------------------------------------------------------
			final ArrayList<String> refSynonyms = new ArrayList<String>();
			refSynonyms.add("r");
			refSynonyms.add("reference");
			optionParser.acceptsAll(
				refSynonyms,
				"Reference - hg18 or hg19 (required)."
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------
			
			
							
			// BED infile
			//------------------------------------------------------------------
			final ArrayList<String> featureFileSynonyms = new ArrayList<String>();
			featureFileSynonyms.add("f");
			featureFileSynonyms.add("features");
			optionParser.acceptsAll(
				featureFileSynonyms,
				"Features file in BED format (optional)."
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------

		
			
			
			// Obtain and process arguments
			OptionSet options = optionParser.parse(args);

			// Action infile arguments.
			if (options.has("help")) {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}

			
			// xsq infile is optional.
			File xsqFile = null;
			if (options.has("xsq")) {
				xsqFile = new File((String)options.valueOf("xsq"));
				JoptsimpleUtil.checkFileExists(xsqFile);
				}
		
			
			// Output directory must always be provided.
			File outdir = null;
			if (options.has("directory")) {
				outdir = new File((String)options.valueOf("directory"));
				}
			else {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}
			
			
			// Reference must always be provided.
			String ref = null;
			if (options.has("reference")) {
				ref = (String)options.valueOf("reference");
				}
			else {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}
			
			
			
			// bed infile is optional.
			File bedFile = null;
			if (options.has("features")) {
				bedFile = new File((String)options.valueOf("features"));
				JoptsimpleUtil.checkFileExists(bedFile);
				}
			
			

			

			// Run program.
			new QC_BasicPipeline(
				xsqFile,
				outdir,
				ref,
				bedFile
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
	 * Creates a new instance of <code>QC_BasicPipeline</code>.
	 */
	public QC_BasicPipeline(
		final File xsqFile,
		final File outdir,
		final String ref,
		final File bedFile
		) throws IOException {
		
		if (xsqFile != null) {
			
			// make out directory.
			if (outdir.mkdir() == false) {
				throw new IOException("Could not create '" + outdir + "'");
				}

			// Extract data from XSQ file.
			processXSQ(xsqFile, outdir);
			}


		// Process each library in turn.
		//my @dirs = glob("$dir/Libraries/*/*/reads");
		//foreach my $libDir (@dirs) {
		//	print "Processing $libDir..\n";
		//	processDir($libDir);
		//	}

		
		} // End of constructor.
	
	//##########################################################################
	
	// METHODS
	
	private void processXSQ(final File xsqFile, final File outDir) {
		
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	private void processDir(final File dir) {
		
		} // End of method.
	
	//##########################################################################
	
	// INNER CLASSES
	
	//##########################################################################

	} // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
