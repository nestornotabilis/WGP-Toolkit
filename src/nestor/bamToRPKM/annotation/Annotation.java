//==============================================================================
//
//   Annotation.java
//   Created: Mar 2, 2011 at 1:30:18 PM
//   Copyright (C) 2008, University of Liverpool.
//   Author: Kevin Ashelford.
//
//   Contact details:
//   Email:   k.ashelford@liv.ac.uk
//   Address: School of Biological Sciences, University of Liverpool, 
//            Biosciences Building, Crown Street, Liverpool, UK. L69 7ZB
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

package nestor.bamToRPKM.annotation;

import nestor.bamToRPKM.ReportType;
import nestor.bamToRPKM.Strand;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 *
 * @author  Kevin Ashelford.
 */
public abstract class Annotation {

	//##########################################################################

	// CLASS FIELDS

	////////////////////////////////////////////////////////////////////////////

	// MEMBER FIELDS

	private final int		_start;
	private final int		_end;
	private final String	_transcriptId;
	private final String	_geneId;
	private final String	_refId;
	private final Strand	_strand;
	private int				_readCount = 0;
	private final char 		_frame;

	//##########################################################################

	// CONSTRUCTOR

    /**
	 * Creates a new instance of <code>Annotation</code>.
	 */
    public Annotation(
		String refId,		// 1. seqname - The name of the sequence. Must be a chromosome or scaffold.
		//String feature,		// 3. feature - The name of this type of feature. Some examples of standard feature types are "CDS", "start_codon", "stop_codon", and "exon".
		int start,			// 4. start - The starting position of the feature in the sequence. The first base is numbered 1.
		int end,			// 5. end - The ending position of the feature (inclusive).
		//String readCount,		// 6. score - A score between 0 and 1000 (or dot).
		Strand strand,		// 7. strand - Valid entries include '+', '-', or '.' (for don't know/don't care).
		char frame,			// 8. frame - If the feature is a coding exon, frame should be a number between 0-2 that represents the reading frame of the first base. If the feature is not a coding exon, the value should be '.'.
		String geneId,		// 9. gene_id - A globally unique identifier for the genomic source of the sequence.
		String transcriptId	// 10. transcript_id - A globally unique identifier for the predicted transcript.
		) {
		_start = start;
		_end = end;
		_frame = frame;

		// Sanity check.
		if (_end < _start)
			throw new RuntimeException(
				"End point (" + _end +
				") is less than start point (" + _start + ") for transcript " + transcriptId
				);

		_transcriptId = transcriptId;
		_geneId = geneId;
		_refId = refId;
		_strand = strand;
		} // End of constructor.

	//##########################################################################

	// METHODS

	public void incrementReadCount() {
		_readCount++;
		}

	////////////////////////////////////////////////////////////////////////////

	public int getReadCount() {return _readCount;}

	public int getStart() {return _start;}

	public int getEnd() {return _end;}

	public int getSize() {return (_end - _start + 1);}

	public String getTranscriptId() {return _transcriptId;}

	public String getGeneId() {return _geneId;}

	public String getRefId() {return _refId;}

	public Strand getStrand() {return _strand;}

	public char getFrame() {return _frame;}

	public abstract ReportType getType();

	////////////////////////////////////////////////////////////////////////////

	@Override
	public String toString() {

		return String.format(
			"%s\t%s\t%s\t%s\t%s\t%s\t%s",
			_refId,
			_start,
			_end,
			_strand.getChar(),
			_geneId,
			_transcriptId,
			_readCount
			);

		} // End of method.

	//##########################################################################

	// INNER CLASSES

	//##########################################################################

    } // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////