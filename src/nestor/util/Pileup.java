//==============================================================================
//
//   Pileup.java
//   Created: Nov 22, 2011 at 12:49:21 PM
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

package nestor.util;

import java.io.*;
import net.sf.samtools.*;
import java.util.*;
import nestor.tools.BedData;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 * Should produce a simple pileup output identical to that produced by the 
 * histogram feature in IGV and samtools mpileup. Note with the latter in a 
 * few instances mpileup output quotes coverage lower than found by my script 
 * and by IGV.  When subsection of bam created, covering this region alone, 
 * samtools mpileup generates a figure that does coincide with that found by
 * IGV and my code, suggesting that there is a bug in the samtools mpileup.
 * 
 * 
 * @author  Kevin Ashelford.
 */
public class Pileup {

	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS
	
	//##########################################################################
	
	// CONSTRUCTOR
	
	/**
	 * Creates a new instance of <code>Pileup</code>.
	 */
	public Pileup() {
		
		} // End of constructor.
	
	//##########################################################################
	
	// METHODS
	
	public static void run(
		final File bamfile,
		final PileupHandler handler	
		) throws IOException {
		Pileup.run(bamfile, null, handler);
		}
	
	
	public static void run(
		final File bamfile,
		final BedData bedData,
		final PileupHandler handler
		) throws IOException {
		
		// Open sam/bam filestream.
		final SAMFileReader in = new SAMFileReader(bamfile);
		
		// Silence over-strict validation errors.
		in.setValidationStringency(SAMFileReader.ValidationStringency.SILENT);
		
		// get chromosome sizes from bam header.
		Map<String, Integer> refSizes = getRefSizes(in);
		
		// Instantiate coverage buffer with initial nominal size.
		// This will be used to store coverage for a single chromosome reference.
		// (initially do not know length of first chr, hence zero size)
		int[] coverage = null;

		// Work through each read in SAM file 
		// TODO - assume sorted - will need to check... .....................................................................
		String currentChr = null;
		for (SAMRecord record : in) {
			
			
			// Don't process record if not covered by bed file.		
			if (!isValidLine(record, bedData)) continue;
			
			
			// Get relevant info.
			final String chr	= record.getReferenceName();
			final int start		= record.getAlignmentStart();
			final int end		= record.getAlignmentEnd();
			
			
			// Will ignore read if unmapped - use bitwise flag as most reliable
			// means of assessing mapped status.
			if ((record.getFlags() & 4) == 4) continue;
			
			
			// Ignore if maqp is zero...
			//if (record.getMappingQuality() == 0) continue;
			
			
			// Sanity check chr (just in case...)
			if (!refSizes.containsKey(chr)) {
				throw new RuntimeException(
					"Unexpected chromosome encountered: " + chr
					);
				}
			
			
			// First read - special case.
			if (currentChr == null) {
				currentChr = chr;
				// now know size of first chromosome so can instantiate coverage
				// array properly.
				
				// PROBLEM - this does not take into account scenario if 
				// bedfile used. Results in entire chromosome represented in 
				// array even though only a section is covered by bedfile.
				// Can be accommodated by suitable implementation of PileupHandler
				// but poor design.
				coverage = new int[refSizes.get(chr)];
				}
			
			
			// New chromosome encountered - process collected coverage data for
			// previous chr and then start afresh.
			if (!currentChr.equals(chr)) {

				// First process existing chr.
				handler.process(coverage, currentChr);

				// Reset storage before processing current read.
				currentChr = chr;
				//mappedCount = 0;
				coverage = new int[refSizes.get(chr)];
				}
			
			
			// Process current read.------------------------------
			for (int i = start-1; i < end; i++) {
			coverage[i]++;
				}
			//----------------------------------------------------
			
			
			} // End of for loop - no more records. 
		
		
		
		// Process final chromosome
		handler.process(coverage, currentChr);
			
		// Probably unnecessary - but might encourage quicker garbage collection.
		coverage = null;
	
		// Close filestream.
		in.close();
			
		
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	private static boolean isValidLine(final SAMRecord record, final BedData bedData) {
		
		// Return true regardless if not bedfile is provided.
		if (bedData == null) {
			return true;	
			}
		
		
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
		//if (bedData.exists(chr, start) || bedData.exists(chr, end)) return true;
		
		if (bedData.exists(chr, start) || bedData.exists(chr, end)) {
			
		//	System.out.println("chr: " + chr);
		//	System.out.println("start: " + start);
		//	System.out.println("end: " + end);
		//	System.out.println(">>>" + bedData.toString());
			
			return true;
			}
		
		// Is invalid 
		return false;
		
		} // end of method.
	
	
	////////////////////////////////////////////////////////////////////////////
	
	private static Map<String, Integer> getRefSizes(final SAMFileReader in) {

		HashMap<String, Integer> chrSizes = new HashMap<String, Integer>();

		// Determine how many chromosomes form the reference.
		int numberOfChr = in.getFileHeader().getSequenceDictionary().size();

		// Determine label and size of each chromosome.
		ArrayList<String> chrNames = new ArrayList<String>();
		for (int i = 0; i < numberOfChr; i++) {
			final SAMSequenceRecord sequenceRecord = in.getFileHeader().getSequence(i);

			// Suspect null sequenceRecord might be encountered - let's see:
			if (sequenceRecord == null)
				throw new RuntimeException(
					"Unexpected null sequenceRecord"
					);

			final String chrName = sequenceRecord.getSequenceName();
			final int chrSize = sequenceRecord.getSequenceLength();

			chrSizes.put(chrName, chrSize);
			chrNames.add(chrName);
			}

		return chrSizes;

		} // End of method.
	
	//##########################################################################
	
	// INNER CLASSES
	
	//##########################################################################

	} // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

