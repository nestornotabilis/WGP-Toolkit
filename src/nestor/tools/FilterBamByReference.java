//==============================================================================
//
//   FilterBamByReference.java
//   Created: Jun 14, 2012 at 14:39:15 AM
//   Copyright (C) 2012, Cardiff University.
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
/*
package nestor.tools;

import nestor.util.ProcessTimer;
import joptsimple.*;
import java.util.*;
import java.io.*;
import nestor.util.JoptsimpleUtil;
import net.sf.samtools.*;


////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

public class FilterBamByReference {

	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS
	
	//##########################################################################
	
	// ENTRY POINT
	

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


			// Outfile
			//------------------------------------------------------------------
			final ArrayList<String> outfileSynonyms = new ArrayList<String>();
			outfileSynonyms.add("o");
			outfileSynonyms.add("outfile");
			optionParser.acceptsAll(
				outfileSynonyms,
				"Outfile in same format as input (optional)."
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------
			
			
			// BAM infile
			//------------------------------------------------------------------
			final ArrayList<String> bamfileSynonyms = new ArrayList<String>();
			bamfileSynonyms.add("b");
			bamfileSynonyms.add("bamfile");

			optionParser.acceptsAll(
				bamfileSynonyms,
				"BAM or SAM infile (required)."
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------

			// List of chr to filter for.
			//------------------------------------------------------------------
			final ArrayList<String> refSynonyms = new ArrayList<String>();
			refSynonyms.add("r");
			refSynonyms.add("ref");

			optionParser.acceptsAll(
				refSynonyms,
				"Reference ids to filter for (comma separated list)."
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------
			
			

			// Obtain and process arguments
			OptionSet options = optionParser.parse(args);

			// Action infile arguments.
			if (options.has("help")) {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}

			
			// BAM infile must always be provided.
			File bamfile = null;
			if (options.has("bamfile")) {
				bamfile = new File((String)options.valueOf("bamfile"));
				JoptsimpleUtil.checkFileExists(bamfile);
				}
			else {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}

			
		
			// Outfile may be provided.
			File outfile = null;
			if (options.has("outfile")) {
				outfile = new File( (String)options.valueOf("outfile") );
				}

			
			// List must be provided.
			String refList = null;
			if (options.has("ref")) {
				refList = (String)options.valueOf("ref");
				}
			else {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}

			
			
			// Run program.
			new FilterBamByReference(
				bamfile,
				outfile,
				refList
				);

			} 
		
		// Catch io exceptions.
		catch (IOException ioe) {
			System.err.println(
				"ERROR: " + ioe.getMessage()
				);
			ioe.printStackTrace();
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

	public FilterBamByReference(
		final File infile, 
		final File outfile,
		final String refList
		) throws IOException {
		
		
		// Outfile
		//===============================================================
		PrintStream out = null;
		// No outfile specified, so write to STDOUT.
		if (outfile == null) {out = System.out;}
		// Outfile specified.
		else {out = new PrintStream(outfile);}
		//===============================================================
		
		
		String[] validRefs = refList.split(","); 
		
		System.out.println(validRefs[0]);
		System.out.println(validRefs[1]);
		
		
		// Create SAM/BAM file input stream
		final SAMFileReader bamIn = new SAMFileReader(infile);
		
		// Silence over-strict validation errors.
		bamIn.setValidationStringency(SAMFileReader.ValidationStringency.SILENT);
		
		
		// Create SAM/BAM output stream.
		final SAMFileWriter bamOut = 
			new SAMFileWriterFactory().makeSAMOrBAMWriter(
				bamIn.getFileHeader(),
				true, 
				outfile
				);
		
		
		
		// Process each record in sam/bam input stream.		
		for (final SAMRecord record : bamIn) {
			
			
			String[] refs = new String[2];
			refs[0] = record.getReferenceName();
			refs[1] = record.getMateReferenceName();
			
			if (isValidPair(refs, validRefs)) {
				bamOut.addAlignment(record);
				}
			
			}
	           
		// Close file streams.
		bamOut.close();
		bamIn.close();		
		
		} // End of constructor.
	
	//##########################################################################
	// METHODS
	
	private boolean isValidPair(final String[] refs, final String[] validRefs) {
		
		boolean ref1Ok = false;
		boolean ref2Ok = false;
		
		for (String validRef : validRefs) {
			if (refs[0] == validRef) return false;
				
			
			
			}
		
		return true;
		
		} // end of method.
	
	//##########################################################################
	// INNER CLASSES
	//##########################################################################
} // End of class.
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
*/