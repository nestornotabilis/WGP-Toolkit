//==============================================================================
//
//   TrimFastq.java
//   Created: Dec 13, 2012 at 3:31:36 PM
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

import java.io.*;
import java.util.*;
import java.util.regex.*;
import joptsimple.*;
import nestor.util.ProcessTimer;
import nestor.util.JoptsimpleUtil;
import nestor.util.SequenceUtil;
import nestor.util.QVEncoding;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
/**
 * 
 * @author  Kevin Ashelford.
 */
public class TrimFastq {

	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS
	
	private final Matcher _headerRegex	= Pattern.compile("^@(\\S+).*$").matcher("");
	private final Matcher _seqRegex		= Pattern.compile("^[a-zA-Z0-9]+$").matcher(""); 
	private final Matcher _spacerRegex	= Pattern.compile("^\\+$").matcher("");
	private final Matcher _qualRegex	= Pattern.compile("^\\S+$").matcher("");
	private final Matcher _trimRegex	= Pattern.compile("^(\\S+)\\t(\\d+)\\t(\\d+)\\t(\\S+)$").matcher(""); 
	
	//##########################################################################
	
	// ENTRY POINT
	/**
	 * @param args the command line arguments
	 */

	public static void main(final String[] args) {
		
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
				"Outfile in fastq format (optional)."
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------
			

			// infile
			//------------------------------------------------------------------
			final ArrayList<String> infileSynonyms = new ArrayList<String>();
			infileSynonyms.add("i");
			infileSynonyms.add("infile");

			optionParser.acceptsAll(
				infileSynonyms,
				"fastq infile."
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------


			// optional length of 5' to trim
			//------------------------------------------------------------------
			final ArrayList<String> fivePrimeSynonyms = new ArrayList<String>();
			fivePrimeSynonyms.add("5");
			fivePrimeSynonyms.add("5prime");

			optionParser.acceptsAll(
				fivePrimeSynonyms,
				"Length to trim from 5' end (default, 0)."
				).withRequiredArg().ofType(Integer.class);
			//------------------------------------------------------------------
			
			
			// optional length of 3' to trim
			//------------------------------------------------------------------
			final ArrayList<String> threePrimeSynonyms = new ArrayList<String>();
			threePrimeSynonyms.add("3");
			threePrimeSynonyms.add("3prime");

			optionParser.acceptsAll(
				threePrimeSynonyms,
				"Length to trim from 3' end (default, 0)."
				).withRequiredArg().ofType(Integer.class);
			//------------------------------------------------------------------
	
			
			// optional file listing positions to trim
			//------------------------------------------------------------------
			final ArrayList<String> trimFileSynonyms = new ArrayList<String>();
			trimFileSynonyms.add("t");
			trimFileSynonyms.add("trimfile");

			optionParser.acceptsAll(
				trimFileSynonyms,
				"Optional file containing positions to trim from and two (overrides any 5' and 3' lengths provided)"
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------

			
			
			// Obtain and process arguments
			OptionSet options = optionParser.parse(args);

			// Action infile arguments.
			if (options.has("help")) {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}

			// fastq infile must always be provided.
			File infile = null;
			if (options.has("infile")) {
				infile = new File((String)options.valueOf("infile"));
				JoptsimpleUtil.checkFileExists(infile);
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

		
			// Five prime trim length may be provided.
			Integer fivePrimeLength = 0;
			if (options.has("5prime")) {
				fivePrimeLength = (Integer)options.valueOf("5prime");
				}
			
			// Three prime trim length may be provided.
			Integer threePrimeLength = 0;
			if (options.has("3prime")) {
				threePrimeLength = (Integer)options.valueOf("3prime");
				}
			
			// Three prime trim length may be provided.
			File trimFile = null;
			if (options.has("trimfile")) {
				trimFile = new File((String)options.valueOf("trimfile"));
				JoptsimpleUtil.checkFileExists(trimFile);
				}
			

			// Run program.
			new TrimFastq(
				infile,
				outfile,
				fivePrimeLength,
				threePrimeLength,
				trimFile
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

		} // End of main.
	
	
	//##########################################################################
	
	// CONSTRUCTOR
	
	/**
	 * Creates a new instance of <code>SubsampleFastq</code>.
	 */
	public TrimFastq(
		final File infile,
		final File outfile,
		final int fivePrimeLength,
		final int threePrimeLength,
		final File trimFile
		) throws IOException {
		
		// Outfile
		//===============================================================
		PrintStream out = null;
		// No outfile specified, so write to STDOUT.
		if (outfile == null) {out = System.out;}
		// Outfile specified.
		else {out = new PrintStream(outfile);}
		//===============================================================
		
		HashMap<String, Integer[]> trimData = new HashMap<String, Integer[]>();
		if (trimFile != null) {
			
			BufferedReader trimIn = new BufferedReader(new FileReader(trimFile));
			
			String line;
			while ( (line = trimIn.readLine()) != null ) {
				
				if (_trimRegex.reset(line).matches()) {
					
					String id = _trimRegex.group(1);
					int start = Integer.parseInt(_trimRegex.group(2));
					int end =	Integer.parseInt(_trimRegex.group(3));
					
					Integer[] positions = new Integer[]{start, end};
					
					trimData.put(id, positions);
					
					} 
				else {
					throw new IOException("Unexpected line '" + line + "'.");
					}
				
				}
			
			trimIn.close();
			
			}
			
			
			
		
		
		// Open infile
		BufferedReader in = new BufferedReader(new FileReader(infile));
		
		int i = 0;
		String header;
		String headerTrimmed;
		String seq;
		String spacer;
		String qual;
		while ((header	= in.readLine()) != null) {
				seq		= in.readLine();
				spacer	= in.readLine();
				qual	= in.readLine();
				
			if (_headerRegex.reset(header).matches()) {
				headerTrimmed = _headerRegex.group(1);
				}
			else {	
				throw new IOException("Unexpected header: " + header);
				}
				
			if (!_seqRegex.reset(seq).matches()) 
				throw new IOException("Unexpected sequence: " + seq);
			
			if (!_spacerRegex.reset(spacer).matches()) 
				throw new IOException("Unexpected spacer: " + spacer);
			
			if (!_qualRegex.reset(qual).matches()) 
				throw new IOException("Unexpected qual line: " + qual);
			
			
			if (trimFile == null) {
				out.println(header);
				out.println(trim(seq, fivePrimeLength, seq.length() - threePrimeLength));
				out.println(spacer);
				out.println(trim(qual, fivePrimeLength, seq.length() - threePrimeLength));
				}
				
			// Assume trimfile is to provide info	
			else {
				
				// trim according to trimfile info
				if (trimData.containsKey(headerTrimmed)) {
					
					int start = trimData.get(headerTrimmed)[0];
					int end   = trimData.get(headerTrimmed)[1];
					
					out.println(header);
					out.println(trim(seq, start, end));
					out.println(spacer);
					out.println(trim(qual, start, end));
					
					} 
					
				// Do nothing, not in trim file	
				else {
					
					out.println(header);
					out.println(seq);
					out.println(spacer);
					out.println(qual);
					
					
					}
				
				
				}
			

			
			
			}
		
		out.close();
		in.close();
		
		} // End of constructor.
	
	//##########################################################################
	
	// METHODS
	
	private String trim(
		final String seq,
		final int start,
		int end
		) {
		
		if (end > seq.length()) 
			end = seq.length();
		
		return seq.substring(start, end);
		
		} // End of method.
	
	//##########################################################################
	
	// INNER CLASSES
	
	//##########################################################################

	} // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
