//==============================================================================
//
//   SubsampleBAMWithReadNames.java
//   Created: Oct 31, 2012 at 3:03:48 PM
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
import java.util.regex.*;
import net.sf.samtools.*;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
/**
 * 
 * @author  Kevin Ashelford.
 */
public class SubsampleBAMWithReadNames {

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


			// list infile
			//------------------------------------------------------------------
			final ArrayList<String> listFileSynonyms = new ArrayList<String>();
			listFileSynonyms.add("l");
			listFileSynonyms.add("list");
			optionParser.acceptsAll(
				listFileSynonyms,
				"List of read names to either include or exclude."
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------
		
			
			// include or exclude?
			//------------------------------------------------------------------
			final ArrayList<String> excludeSynonyms = new ArrayList<String>();
			excludeSynonyms.add("e");
			excludeSynonyms.add("exclude");
			optionParser.acceptsAll(
				excludeSynonyms,
				"If true exclude records in list, otherwise exclude those"
				+ "not on list (default true)"
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------
			
	
			
			// optional label tag
			//------------------------------------------------------------------
			final ArrayList<String> tagSynonyms = new ArrayList<String>();
			tagSynonyms.add("t");
			tagSynonyms.add("tag");

			optionParser.acceptsAll(
				tagSynonyms,
				"String to append to read id (optional)."
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

			
			// Tag string must always be provided.
			String tag = null;
			if (options.has("tag")) {
				tag = (String)options.valueOf("tag");
				}
			
			
			// List infile must always be provided.
			File listfile = null;
			if (options.has("list")) {
				listfile = new File((String)options.valueOf("list"));
				JoptsimpleUtil.checkFileExists(listfile);
				}
			else {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}
			
			// Exclusion decision.
			boolean exclude = true;
			if (options.has("exclude")) {
				exclude = new Boolean((String)options.valueOf("exclude"));
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
			new SubsampleBAMWithReadNames(
				bamfile,
				listfile,
				exclude,
				outfile,
				tag
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
	 * Creates a new instance of <code>SubsampleBAMWithReadNames</code>.
	 */
	public SubsampleBAMWithReadNames(
		final File bamfile,
		final File listfile,
		final boolean exclude,
		final File outfile,
		final String tag
		) throws IOException, Exception {
		
		// PROCESS LIST FILE.
		final HashMap<String, Boolean> listData = new HashMap<String, Boolean>();
	//	final ArrayList<String> listData = new ArrayList<String>();
		
				
		Matcher listRegex = Pattern.compile("^(\\S+)$").matcher("");
		
		BufferedReader listIn = new BufferedReader(new FileReader(listfile));
		String line = null;
		while ((line = listIn.readLine()) != null) {
			
			if (listRegex.reset(line).matches()) {
				final String name = listRegex.group(1);
	
				listData.put(name, true);
				//listData.add(name);
				
				} 
			else {
				throw new Exception("Unexpected line encountered '" + line + "'");
				}
			
			
			}
		listIn.close();
		
		//======================================================================
		
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
			if (isValidLine(record, listData, exclude)) {
				
				// Modify read name with supplied tag if requested
				if (tag != null) { 
					record.setReadName(tag + "_" + record.getReadName());
					}
					
					
				// add to outfile.
				bamOut.addAlignment(record);
				
					
				}
			
			}
		
		bamIn.close();
		bamOut.close();
		
		
		} // End of constructor.
	
	//##########################################################################
	// METHODS
	
	private boolean isValidLine(
		final SAMRecord record, 
		final HashMap<String, Boolean> listData,
		//final ArrayList<String> listData,
		final boolean exclude
		) {
		
		
		final String name = record.getReadName();
		
		// Scenario 1: list is an exclusion list
		if (exclude) {
			
			// List contains name, therefore not a valid line for output.
			if (listData.containsKey(name)) {
			//if (listData.contains(name)) {
				return false;
				}
			// List does not contain name, therefore line is valid for output.	
			else {
				return true;
				}
			
			}
		
		// Scenario 2: list is an inclusion list	
		else {
			
			// List contains name, therefore a valid line for output.
			if (listData.containsKey(name)) {
			//if (listData.contains(name)) {	
				return true;
				}
			// List does not contain name, therefore line is not valid for output.	
			else {
				return false;
				}
			
			}
		
		
		
		} // end of method.
	
	//##########################################################################
	// INNER CLASSES
	//##########################################################################
} // End of class.
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
