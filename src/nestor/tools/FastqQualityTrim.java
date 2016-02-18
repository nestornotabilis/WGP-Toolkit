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
public class FastqQualityTrim {

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


			
			
			
			// Minimum read length
			//------------------------------------------------------------------
			final ArrayList<String> minLengthSynonyms = new ArrayList<String>();
			minLengthSynonyms.add("l");
			minLengthSynonyms.add("length");

			optionParser.acceptsAll(
				minLengthSynonyms,
				"Minimum read length (default, 50)."
				).withRequiredArg().ofType(Integer.class);
			//------------------------------------------------------------------
	
			
			// Minimum base quality
			//------------------------------------------------------------------
			final ArrayList<String> minQualitySynonyms = new ArrayList<String>();
			minQualitySynonyms.add("q");
			minQualitySynonyms.add("quality");

			optionParser.acceptsAll(
				minQualitySynonyms,
				"Minimum base call quality (default, 10)."
				).withRequiredArg().ofType(Integer.class);
			//------------------------------------------------------------------
			
			
			// Max number of bad base qualities
			//------------------------------------------------------------------
			final ArrayList<String> maxBadBasesSynonyms = new ArrayList<String>();
			maxBadBasesSynonyms.add("b");
			maxBadBasesSynonyms.add("bad");

			optionParser.acceptsAll(
				maxBadBasesSynonyms,
				"Maximum number of bad base calls allowed (default, 10)."
				).withRequiredArg().ofType(Integer.class);
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

		
			// Minimum read length
			Integer minLength = 50;
			if (options.has("length")) {
				minLength = (Integer)options.valueOf("length");
				}
			
			// Minimum base quality
			Integer minQuality = 10;
			if (options.has("quality")) {
				minQuality = (Integer)options.valueOf("quality");
				}
			
			// Max number of bad base qualities.
			Integer maxBadBases = 10;
			if (options.has("bad")) {
				maxBadBases = (Integer)options.valueOf("bad");
				}
			

			// Run program.
			new FastqQualityTrim(
				infile,
				outfile,
				minLength,
				minQuality,
				maxBadBases
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
	public FastqQualityTrim(
		final File infile,
		final File outfile,
		final int minLength,
		final int minQuality,
		final int maxBadBases
		) throws IOException {
		
		// Outfile
		//===============================================================
		PrintStream out = null;
		// No outfile specified, so write to STDOUT.
		if (outfile == null) {out = System.out;}
		// Outfile specified.
		else {out = new PrintStream(outfile);}
		//===============================================================
		
				
		// Open infile
		BufferedReader in = new BufferedReader(new FileReader(infile));
		
		int i = 0;
		String header;
		String headerTrimmed;
		String seq;
		String spacer;
		String qual;
/*
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
*/		
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
