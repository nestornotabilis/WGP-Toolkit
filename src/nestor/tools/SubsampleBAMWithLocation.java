//==============================================================================
//
//   SubsampleBAMWithBED.java
//   Created: Nov 21, 2011 at 10:32:08 AM
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
import net.sf.samtools.*;
import java.util.regex.*;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
/**
 * 
 * @author  Kevin Ashelford.
 */
public class SubsampleBAMWithLocation {

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
			

			// BAM infile
			//------------------------------------------------------------------
			final ArrayList<String> bamFileSynonyms = new ArrayList<String>();
			bamFileSynonyms.add("b");
			bamFileSynonyms.add("bam");

			optionParser.acceptsAll(
				bamFileSynonyms,
				"SAM or BAM infile (required)."
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------


			// Location
			//------------------------------------------------------------------
			final ArrayList<String> locationSynonyms = new ArrayList<String>();
			locationSynonyms.add("l");
			locationSynonyms.add("location");
			optionParser.acceptsAll(
				locationSynonyms,
				"Location (required, e.g., chr9:10-100)."
				).withRequiredArg().ofType(String.class);
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
			
			
			
			// Obtain and process arguments
			OptionSet options = optionParser.parse(args);

			// Action infile arguments.
			if (options.has("help")) {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}

			
			// bam infile must always be provided.
			File bamfile = null;
			if (options.has("bam")) {
				bamfile = new File((String)options.valueOf("bam"));
				JoptsimpleUtil.checkFileExists(bamfile);
				}
			else {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}

			
			// location must always be provided.
			String location = null;
			if (options.has("location")) {
				location = (String)options.valueOf("location");
				}
			else {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}
			
			
			// sam/bam outfile must always be provided.
			File outfile = null;
			if (options.has("outfile")) {
				outfile = new File((String)options.valueOf("outfile"));
				}
			else {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}
			


			// Run program.
			new SubsampleBAMWithLocation(
				bamfile,
				location,
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
	 * Creates a new instance of <code>SubsampleBAMWithBED</code>.
	 */
	public SubsampleBAMWithLocation(
		final File bamfile,
		final String location,
		final File outfile
		) throws IOException, Exception {
		
		
		
		// PROCESS BED FILE.
		final BedData bedData = new BedData();
		
		Matcher regex = Pattern.compile("^(\\S+):(\\d+)-(\\d+)$").matcher("");
		
		
		if (regex.reset(location).matches()) {
				final String chr = regex.group(1);
				final int start = Integer.parseInt(regex.group(2));
				final int end = Integer.parseInt(regex.group(3));
				
				for (int i = start; i <= end; i++) {
					bedData.create(chr, i, 0);
					}
				
				} 
			else {
				throw new Exception("Unrecognised location: '" + location + "'");
				}
			
			
		

		// PROCESS BAM FILE.
		
		// Create SAM/BAM file input stream
		final SAMFileReader bamIn = new SAMFileReader(bamfile);
		
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
			if (isValidLine(record, bedData)) bamOut.addAlignment(record);
			}
		
		bamIn.close();
		bamOut.close();
		
		} // End of constructor.
	
	//##########################################################################
	
	// METHODS
	
	private boolean isValidLine(final SAMRecord record, final BedData bedData) {
		
		
		final int flag		= record.getFlags();
		final String chr	= record.getReferenceName();
		final int start		= record.getAlignmentStart();
		final int end		= record.getAlignmentEnd();
		

		// ABORTED ATTEMPT TO ALSO SCREEN MATE DETAILS.
		//final String chr2	= record.getMateReferenceName();
		//final int start2	= record.getMateAlignmentStart();
		//final int end2	= record.???

        // Not valid if unmapped.
		if ((flag & 4) == 4) return false;
		
		// Is Valid
		// THIS SCENARIO DOES NOT COVER (ALBEIT RARE) READ LARGER THAN BED 
		// FEATURE STRADDLING THAT FEATURE. COULD BE OVERCOME BY CHECKING EACH 
		// POSITION BETWEEN START AND END POINTS BUT WOULD THIS SLOW DOWN TOO 
		// MUCH?
		if (bedData.exists(chr, start) || bedData.exists(chr, end)) return true;
		
		// Is invalid 
		return false;
		
		} // end of method.
	
	//##########################################################################
	
	// INNER CLASSES

	//##########################################################################
	
	} // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
