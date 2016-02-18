//==============================================================================
//
//   SubsampleFastq.java
//   Created: Nov 29, 2012 at 3:31:36 PM
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
public class FastqToFasta {

	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS
	
	private final Matcher _headerRegex	= Pattern.compile("^@(.+)$").matcher("");
	private final Matcher _seqRegex		= Pattern.compile("^[a-zA-Z0-9]+$").matcher(""); 
	private final Matcher _spacerRegex	= Pattern.compile("^\\+$").matcher("");
	private final Matcher _qualRegex	= Pattern.compile("^\\S+$").matcher("");
	
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
			outfileSynonyms.add("out");
			optionParser.acceptsAll(
				outfileSynonyms,
				"Prefix for outfiles."
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------
			

			// infile
			//------------------------------------------------------------------
			final ArrayList<String> infileSynonyms = new ArrayList<String>();
			infileSynonyms.add("i");
			infileSynonyms.add("infile");

			optionParser.acceptsAll(
				infileSynonyms,
				"fastq infile (NOTE: currently assumes illumina 64 encoding)."
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------


			// correction
			//------------------------------------------------------------------
			final ArrayList<String> correctionSynonyms = new ArrayList<String>();
			correctionSynonyms.add("c");
			correctionSynonyms.add("correction");

			optionParser.acceptsAll(
				correctionSynonyms,
				"ASCII correction - e,g, 64 for Phred+64 (default)."
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

			// Correction.
			int correction = 64;
			if (options.has("correction")) {
				correction = (Integer)options.valueOf("correction");
				}
			
			// Outfile may be provided.
			File seqOutfile = null;
			File qualOutfile = null;
			if (options.has("out")) {
				seqOutfile = new File( (String)options.valueOf("out") + ".fasta");
				JoptsimpleUtil.checkFileDoesNotExist(seqOutfile);
				qualOutfile = new File( (String)options.valueOf("out") + ".QV.fasta");
				JoptsimpleUtil.checkFileDoesNotExist(qualOutfile);
				}
			else {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}

		

			// Run program.
			new FastqToFasta(
				infile,
				seqOutfile,
				qualOutfile,
				correction
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
	public FastqToFasta(
		final File infile,
		final File seqOutfile,
		final File qualOutfile,
		final int correction
		) throws IOException {
		
		// Outfiles
		//===============================================================
		PrintStream seqOut =  new PrintStream(seqOutfile);
		PrintStream qualOut = new PrintStream(qualOutfile);
		//===============================================================
		
		
		// Open infile
		BufferedReader in = new BufferedReader(new FileReader(infile));
		
		String header;
		String seq;
		String spacer;
		String qual;
		while ((header	= in.readLine()) != null) {
				seq		= in.readLine();
				spacer	= in.readLine();
				qual	= in.readLine();
				
			if (_headerRegex.reset(header).matches()) {
				header = _headerRegex.group(1);
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
			
			
				seqOut.println(">" + header);
				seqOut.println(seq);
				
				qualOut.println(">" + header);
				qualOut.println(convertToIntegers(qual, correction));
			
			}
			
		seqOut.close();
		qualOut.close();
		
		} // End of constructor.
	
	//##########################################################################
	
	// METHODS
	
	private String convertToIntegers(
		final String qualString,
		final int correction
		) {
		
		ArrayList<Integer> buffer = new ArrayList<Integer>();
		for (char qual : qualString.toCharArray()) {
		
			buffer.add((int)qual - correction);
			
			}
		
		StringBuilder finalString = new StringBuilder();
		
		for (int i = 0; i < buffer.size() - 1; i++) {
			finalString.append(buffer.get(i) + " ");
			}
		finalString.append(buffer.get(buffer.size()-1));
		
		return finalString.toString();
		
		} // End of method.
	
	//##########################################################################
	
	// INNER CLASSES
	
	//##########################################################################

	} // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
