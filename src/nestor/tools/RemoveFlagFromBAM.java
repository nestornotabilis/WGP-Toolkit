//==============================================================================
//
//   RemoveFlagFromBAM.java
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

package nestor.tools;

import nestor.util.ProcessTimer;
import joptsimple.*;
import java.util.*;
import java.io.*;
import nestor.util.JoptsimpleUtil;
import net.sf.samtools.*;


////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
/**
 * 
 * @author  Kevin Ashelford.
 */
public class RemoveFlagFromBAM {

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


			// Outfile
			//------------------------------------------------------------------
			final ArrayList<String> outfileSynonyms = new ArrayList<String>();
			outfileSynonyms.add("o");
			outfileSynonyms.add("outfile");
			optionParser.acceptsAll(
				outfileSynonyms,
				"Outfile in same format as input (required)."
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

			
			// Flag 
			//------------------------------------------------------------------
			final ArrayList<String> flagSynonyms = new ArrayList<String>();
			flagSynonyms.add("f");
			flagSynonyms.add("flag");

			optionParser.acceptsAll(
				flagSynonyms,
				"Flag to remove from BAM."
				).withRequiredArg().ofType(Integer.class);
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

			
			// Flag must always be provided.
			Integer flag = null;
			if (options.has("flag")) {
				flag = (Integer)options.valueOf("flag");
				}
			else {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}
			
			// SAM/BAM outfile must always be provided.
			File outfile = null;
			if (options.has("outfile")) {
				outfile = new File((String)options.valueOf("outfile"));
//				JoptsimpleUtil.checkFileDoesNotExist(outfile);
				}
			else {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}


			// Run program.
			new RemoveFlagFromBAM(
				bamfile,
				outfile,
				flag
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
	// CONSTRUCTOR
	/**
	 * Creates a new instance of <code>RelabelBAMReads</code>.
	 */
	public RemoveFlagFromBAM(final File infile, final File outfile, final Integer flagToRemove) {
		
		
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
			
			int bitflag = record.getFlags();
			
			if ((bitflag & flagToRemove) == flagToRemove) {
				record.setFlags(bitflag - flagToRemove);
				}
			
			
			// Save to outfile.
			bamOut.addAlignment(record);
			
			}
	           
		
		// Close file streams.
		bamOut.close();
		bamIn.close();		
		
		} // End of constructor.
	
	//##########################################################################
	// METHODS
	//##########################################################################
	// INNER CLASSES
	//##########################################################################
} // End of class.
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
