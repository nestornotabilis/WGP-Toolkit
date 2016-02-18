//==============================================================================
//
//   AddColourspaceToBAM.java
//   Created: Nov 17, 2011 at 9:12:26 AM
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

import nestor.util.ProcessTimer;
import joptsimple.*;
import java.util.*;
import java.io.*;
import nestor.util.JoptsimpleUtil;
import net.sf.samtools.*;
import java.util.regex.*;
import nestor.util.SequenceUtil;
import nestor.util.QVEncoding;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
/**
 * 
 * @author  Kevin Ashelford.
 */
public class AddColourspaceToBAM {

	//##########################################################################
	// CLASS FIELDS
	////////////////////////////////////////////////////////////////////////////
	// MEMBER FIELDS
	
	private final Matcher _commentRegex = Pattern.compile("^#.*$").matcher("");
	private final Matcher _headerRegex = Pattern.compile("^>(\\S+)$").matcher("");
	private final Matcher _readRegex = Pattern.compile("^(\\w[\\.\\d]+)$").matcher(""); 
	private final Matcher _qualRegex = Pattern.compile("^([-\\s\\d]+)$").matcher("");
	
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
			

			// csfasta infile
			//------------------------------------------------------------------
			final ArrayList<String> csfastafileSynonyms = new ArrayList<String>();
			csfastafileSynonyms.add("c");
			csfastafileSynonyms.add("csfastafile");

			optionParser.acceptsAll(
				csfastafileSynonyms,
				"csfasta infile (required)."
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------


			// qual infile
			//------------------------------------------------------------------
			final ArrayList<String> qualfileSynonyms = new ArrayList<String>();
			qualfileSynonyms.add("q");
			qualfileSynonyms.add("qualfile");

			optionParser.acceptsAll(
				qualfileSynonyms,
				"accompanying qual infile (required)."
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------
			
			
			// BAM infile
			//------------------------------------------------------------------
			final ArrayList<String> bamfileSynonyms = new ArrayList<String>();
			bamfileSynonyms.add("b");
			bamfileSynonyms.add("bamfile");

			optionParser.acceptsAll(
				bamfileSynonyms,
				"SAM or SAM infile (required and MUST be sorted with respect to csfasta and qual files)."
				).withRequiredArg().ofType(String.class);
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
			new AddColourspaceToBAM(
				csfastafile,
				qualfile,
				bamfile,
				outfile
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
	 * Creates a new instance of <code>AddColourspaceToBAM</code>.
	 */
	public AddColourspaceToBAM(
		final File csfastafile,
		final File qualfile,
		final File bamfile,
		final File outfile
		) throws IOException, Exception {
		
		// open csfasta and qual filestreams.
		final BufferedReader csfastaIn = new BufferedReader(new FileReader(csfastafile));
		final BufferedReader qualIn = new BufferedReader(new FileReader(qualfile));
		
		
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
			
			// Get respective read info from csfasta and qual files.
			final Read csfastaRead = getNextRead(csfastaIn);
			final Read qualRead = getNextRead(qualIn);
			
			// sanity check - code assumes sam/bam, csfasta and qual files are
			// all in the same order - check this is so.
			if (!coincide(record, csfastaRead, qualRead)) {
				throw new Exception(
					"Infiles do not appear to follow the same order: SAM='" + 
					record.getReadName() + "', csfasta='" + 
					csfastaRead.getId() + "', qual='" + 
					qualRead.getId() + "'"
					);
				}
			
			// Modify SAM record.
			record.setAttribute("CS", csfastaRead.getSeq());
			record.setAttribute(
				"CQ", 
				SequenceUtil.encodeQualString(
					qualRead.getSeq(), 
					false, 
					QVEncoding.Illumina33
					)
				);
			
			// Save to outfile.
			bamOut.addAlignment(record);
			
			}
	           
		
		// Close file streams.
		bamOut.close();
		bamIn.close();
		csfastaIn.close();
		qualIn.close();
	
		} // End of constructor.
	
	//##########################################################################
	
	// METHODS
	
	private boolean coincide(
		final SAMRecord record,
		final Read read1,
		final Read read2
		) {
		
		final String s1 = record.getReadName();
		final String s2 = read1.getId();
		final String s3 = read2.getId();
		
		if (!s1.equals(s2)) return false;
		if (!s1.equals(s3)) return false;
		
		return true;
		
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	private Read getNextRead(final BufferedReader in) throws IOException, Exception {
		
		// TODO WARNING: fragile code! Need better checks esp. for comments
		String headerLine = in.readLine();
		String seqLine = in.readLine();
		
		if (_commentRegex.reset(headerLine).matches())
			throw new Exception("Unexpected comment line found in input stream");
		
		String id;
		if (_headerRegex.reset(headerLine).matches()) {
			id = _headerRegex.group(1);
			}
		else {
			throw new Exception("Expecting '" + headerLine + "' to be a header line");
			}
		
		String seq;
		if (_readRegex.reset(seqLine).matches()) {
			seq = seqLine;
			}
		else if (_qualRegex.reset(seqLine).matches()) {
			seq = seqLine;
			}
		else {
			throw new Exception("Expecting '" + seqLine + "' to be a valid sequence line");
			}
		
		return new Read(id, seq);
		
		} // End of method.
	
	//##########################################################################
	// INNER CLASSES
	
	private class Read {
		
		private final String id;
		private final String seq;
		
		public Read(final String id, final String seq) {
			this.id = id;
			this.seq = seq;
			}
		
		public String getId() {return id;}
		
		public String getSeq() {return seq;}
		
		} // End of inner class.
	
	//##########################################################################

} // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
