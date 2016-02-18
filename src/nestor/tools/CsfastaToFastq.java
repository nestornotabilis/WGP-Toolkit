//==============================================================================
//
//   CsfastaToFastq.java
//   Created: Nov 15, 2011 at 4:55:18 PM
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
//import java.util.regex.*;
import java.util.*;
import joptsimple.*;
import nestor.util.ProcessTimer;
import nestor.util.JoptsimpleUtil;
import nestor.util.SequenceUtil;
import nestor.util.QVEncoding;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

public class CsfastaToFastq {

	//private final Matcher _headerRegex = Pattern.compile("^>(\\S+).*$").matcher("");

	////////////////////////////////////////////////////////////////////////////

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
			

			// csfasta infile
			//------------------------------------------------------------------
			final ArrayList<String> csfastafileSynonyms = new ArrayList<String>();
			csfastafileSynonyms.add("c");
			csfastafileSynonyms.add("csfastafile");

			optionParser.acceptsAll(
				csfastafileSynonyms,
				"csfasta infile."
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------


			// qual infile
			//------------------------------------------------------------------
			final ArrayList<String> qualfileSynonyms = new ArrayList<String>();
			qualfileSynonyms.add("q");
			qualfileSynonyms.add("qualfile");

			optionParser.acceptsAll(
				qualfileSynonyms,
				"qual infile."
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------

			// fraction of data to output
			//------------------------------------------------------------------
			final ArrayList<String> fractionSynonyms = new ArrayList<String>();
			fractionSynonyms.add("f");
			fractionSynonyms.add("fraction");

			optionParser.acceptsAll(
				fractionSynonyms,
				"fraction of data to output (default, 1)."
				).withRequiredArg().ofType(Float.class);
			//------------------------------------------------------------------
			

			// Obtain and process arguments
			OptionSet options = optionParser.parse(args);

			// Action infile arguments.
			if (options.has("help")) {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}

			// Csfasta infile must always be provided.
			File csfastafile = null;
			if (options.has("csfastafile")) {
				csfastafile = new File((String)options.valueOf("csfastafile"));
				JoptsimpleUtil.checkFileExists(csfastafile);
				}
			else {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}

			// Qual infile must always be provided.
			File qualfile = null;
			if (options.has("qualfile")) {
				qualfile = new File((String)options.valueOf("qualfile"));
				JoptsimpleUtil.checkFileExists(qualfile);
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

			// Fraction may be provided.
			float fraction = 1;
			if (options.has("fraction")) {
				fraction = (Float)options.valueOf("fraction");
				}


			// Run program.
			new CsfastaToFastq(
				csfastafile,
				qualfile,
				outfile,
				fraction
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
	
	////////////////////////////////////////////////////////////////////////////

	public CsfastaToFastq (
		final File seqFile, 
		final File qualFile, 
		final File outfile,
		final float fraction
		) throws Exception {
	

		// Outfile
		//===============================================================
		// Abort if outfile already exists.
		//if (outfile != null && outfile.exists()) {
		//	throw new IOException(
		//		"Cannot continue '" + outfile + "' already exists!"
		//		);
		//	}

		// Create output stream object for writing output to.
		PrintStream out = null;
		// No outfile specified, so write to STDOUT.
		if (outfile == null) {out = System.out;}
		// Outfile specified.
		else {out = new PrintStream(outfile);}
		//===============================================================
		
		// Determine how many records to print. i.e., print every x records.
		int x = Math.round(1f / fraction);
		
		// Open seq and qual files
		BufferedReader seqIn = new BufferedReader(new FileReader(seqFile));
		BufferedReader qualIn = new BufferedReader(new FileReader(qualFile));

		// for each seq/qual convert and print to outfile.

		String seqLine;
		String qualLine;
		
		// Monitor read number.
		int i = 0;
		while ((seqLine = seqIn.readLine()) != null && (qualLine = qualIn.readLine()) != null) {

			
			
			if (seqLine.startsWith("#")) {
				processComment(seqLine, qualLine);
				}

			else if (seqLine.startsWith(">")) {
				
				// Increment read count
				i++;
				// process header if identified as fraction of data to print.
				if ((i%x) == 0) 
					processHeader(seqLine, qualLine, out);
				}

			else if (seqLine.matches("^[ACGT][\\.0123456]+$")) {
				// process header if identified as fraction of data to print.
				if ((i%x) == 0) 
					processRead(seqLine, qualLine, out);
				}

			else {
				throw new Exception(
					"Unexpected lines:\nSeq: " +
					seqLine +
					"\nQual:" +
					qualLine + "\n"
					);
				}


			} // No more lines to read.

		

		// Close all file streams.
		seqIn.close();
		qualIn.close();
		out.close();

		} // End of constructor.
	
	////////////////////////////////////////////////////////////////////////////
	
	// Check that both lines are comments - no further action.
	private void processComment(final String line1, final String line2) throws Exception {

		if (line1.startsWith("#") && line2.startsWith("#")) {return;}

		throw new Exception(
			"Unmatching comment lines:\n" +
			"" + line1 + "\n" + line2 + "\n"
			);

		} // End of method.

	////////////////////////////////////////////////////////////////////////////

	// Check that lines are identical - if so, write to outfile.
	private void processHeader (final String line1, final String line2, final PrintStream out) throws Exception {

		if (line1.equals(line2)) {
			out.println(line1.replaceFirst(">", "@"));
			return;
			}

		throw new Exception(
			"Headers don't match:\n" +
			line1 + "\n" + line2 + "\n"
			);

		} // End of method.

	////////////////////////////////////////////////////////////////////////////

	// Check that both lines are same length and that seq is colourspace and
	// qual is int array.
	private void processRead (final String line1, final String line2, final PrintStream out) throws Exception {

		String fsSeq = SequenceUtil.toFakespace(line1, true);
		String qualString = SequenceUtil.encodeQualString(line2, true, QVEncoding.Illumina33);

		if (fsSeq.length() == qualString.length()) {
			out.println(fsSeq);
			out.println("+");
			out.println(qualString);
			return;
			}

		throw new Exception(
			"Sequence doesn't match qualities:\n" +
			fsSeq + "\n" + qualString + "\n"
			);

		} // End of method.

	//##########################################################################

	} // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////