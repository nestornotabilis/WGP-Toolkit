//==============================================================================
//
//   CDS.java
//   Created: Mar 2, 2011 at 1:27:42 PM
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

import nestor.bamToRPKM.*;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 * 
 * @author  Kevin Ashelford.
 */
public class CDS extends Annotation {

	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS
	
	//##########################################################################
	
	// CONSTRUCTOR
	
    /**
	 * Creates a new instance of <code>CDS</code>.
	 */
    public CDS(
		String refId,		// 1. seqname - The name of the sequence. Must be a chromosome or scaffold.
		//String feature,		// 3. feature - The name of this type of feature. Some examples of standard feature types are "CDS", "start_codon", "stop_codon", and "exon".
		int start,			// 4. start - The starting position of the feature in the sequence. The first base is numbered 1.
		int end,			// 5. end - The ending position of the feature (inclusive).
		Strand strand,		// 7. strand - Valid entries include '+', '-', or '.' (for don't know/don't care).
		char frame,			// 8. frame - If the feature is a coding exon, frame should be a number between 0-2 that represents the reading frame of the first base. If the feature is not a coding exon, the value should be '.'.
		String geneId,		// 9. gene_id - A globally unique identifier for the genomic source of the sequence.
		String transcriptId	// 10. transcript_id - A globally unique identifier for the predicted transcript
		) {
		super(refId, start, end, strand, frame, geneId, transcriptId);
		} // End of constructor.

	//##########################################################################
		
	// METHODS

	public ReportType getType() {return ReportType.CDS;}
	
	//##########################################################################
	
	// INNER CLASSES
	
	//##########################################################################
	
    } // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////





