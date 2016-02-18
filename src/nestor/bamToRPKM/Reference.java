//==============================================================================
//
//   Reference.java
//   Created: Mar 1, 2011 at 10:13:06 AM
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

package nestor.bamToRPKM;

import nestor.bamToRPKM.annotation.*;
import java.util.*;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 * 
 * @author  Kevin Ashelford.
 */
public class Reference {

	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS
	
	final String _refId;
	final int _size;

	final HashMap<Integer, ArrayList<Exon>> _exonsByReferencePosition =
		new HashMap<Integer, ArrayList<Exon>>();

	final HashMap<Integer, ArrayList<CDS>> _cdsByReferencePosition =
		new HashMap<Integer, ArrayList<CDS>>();

	final HashMap<Exon, Boolean> _exons = new HashMap<Exon, Boolean>();

	final HashMap<CDS, Boolean> _CDSs = new HashMap<CDS, Boolean>();

	final HashMap<String, Transcript> _transcripts =
		new HashMap<String, Transcript>();

	//##########################################################################
	
	// CONSTRUCTOR
	
    /**
	 * Creates a new instance of <code>Reference</code>.
	 */
    public Reference(final String refId, final int length) {
		_refId = refId;
		_size = length;
		} // End of constructor.

	//##########################################################################
		
	// METHODS

	public String getId() {return _refId;} // end of method.

	////////////////////////////////////////////////////////////////////////////

	public int getSize() {return _size;} // End of method.

	////////////////////////////////////////////////////////////////////////////

	public void addCDS(final CDS cds, final int start, final int end) {

		// Should be a novel cds.
		if (_CDSs.containsKey(cds))
			throw new RuntimeException(
				"CDS already associated with reference:" + cds
				);

		// Sanity check: ensure ref id matches with that stored in cds.
		if (!cds.getRefId().equalsIgnoreCase(_refId))
			throw new RuntimeException(
				"Ref ids do not match: " + cds.getRefId() + " vs " + _refId
				);

		// Store cds in master list.
		_CDSs.put(cds, true);

		// For each ref position between start and end (inclusive) record cds.
		for (int i = start; i <= end; i++) {
				addCDSsAt(cds, i);
				}

		} // end of method.

	////////////////////////////////////////////////////////////////////////////

	public void addExon(final Exon exon, final int start, final int end) {

		// Should be a novel exon.
		if (_exons.containsKey(exon))
			throw new RuntimeException(
				"Exon already associated with reference:" + exon
				);

		// Sanity check: ensure ref id matches with that stored in exon.
		if (!exon.getRefId().equalsIgnoreCase(_refId))
			throw new RuntimeException(
				"Ref ids do not match: " + exon.getRefId() + " vs " + _refId
				);


		// Store exon in master list.
		_exons.put(exon, true);

		// Create transcript record if not yet existing.
		if (!_transcripts.containsKey(exon.getTranscriptId()))
			_transcripts.put(
				exon.getTranscriptId(),
				new Transcript(
					exon.getRefId(),
					0, // initial start (ignored by transcript class)
					0, // initial end (ignored by transcript class)
					exon.getStrand(),
					'.', // frame,
					exon.getGeneId(),
					exon.getTranscriptId()
					)
				);

		// Add to transcript list.
		_transcripts.get(exon.getTranscriptId()).addExon(exon);

		// For each ref position between start and end (inclusive) record exon.
		for (int i = start; i <= end; i++) {
				addExonAt(exon, i);
				}

		} // End of method.

	////////////////////////////////////////////////////////////////////////////

	private void addCDSsAt(final CDS cds, final int position) {

		// Create map entry if not already existing.
		if (!_cdsByReferencePosition.containsKey(position))
			_cdsByReferencePosition.put(position, new ArrayList<CDS>());

		// Add exon.
		_cdsByReferencePosition.get(position).add(cds);

		} // End of method.

	////////////////////////////////////////////////////////////////////////////

	private void addExonAt(final Exon exon, final int position) {

		// Create map entry if not already existing.
		if (!_exonsByReferencePosition.containsKey(position))
			_exonsByReferencePosition.put(position, new ArrayList<Exon>());

		// Add exon.
		_exonsByReferencePosition.get(position).add(exon);

		} // End of method.

	////////////////////////////////////////////////////////////////////////////

	public boolean hasCDSsAt(final int position) {
		if (_cdsByReferencePosition.containsKey(position))
			{return true;} else {return false;}
		} // End of method.

	////////////////////////////////////////////////////////////////////////////

	public boolean hasExonsAt(final int position) {
		if (_exonsByReferencePosition.containsKey(position))
			{return true;} else {return false;}
		} // End of method.

	////////////////////////////////////////////////////////////////////////////

	public ArrayList<CDS> getCDSsAt(final int position) {
		return _cdsByReferencePosition.get(position);
		} // End of method.

	////////////////////////////////////////////////////////////////////////////

	public ArrayList<Exon> getExonsAt(final int position) {
		return _exonsByReferencePosition.get(position);
		} // End of method.

	////////////////////////////////////////////////////////////////////////////

	public boolean hasCDSsBetween(final int start, final int end) {

		//for (int i : new int[]{start, end}) { // TODO - this simplified method is faster but excludes CDS straddled by larger reads.
		for (int i = start; i <= end; i++) {

			// Return true if cds found at position.
			if (_cdsByReferencePosition.containsKey(i)) return true;
			}

		// No cds found - return false.
		return false;

		} // End of method.

	////////////////////////////////////////////////////////////////////////////

	public boolean hasExonsBetween(final int start, final int end) {

		//for (int i : new int[]{start, end}) { // TODO - this simplified method is faster but excludes exons straddled by larger reads.
		for (int i = start; i <= end; i++) {

			// Return true if exon found at position.
			if (_exonsByReferencePosition.containsKey(i)) return true;
			}

		// No exons found - return false.
		return false;

		} // End of method.

	////////////////////////////////////////////////////////////////////////////

	public CDS[] getCDSsBetween(final int start, final int end) {

		HashMap<CDS, Boolean> buffer = new HashMap<CDS, Boolean>();

		// Work through reference positions from start to end, inclusive.
		//for (int i : new int[]{start, end}) { // TODO - this simplified method is faster but excludes cds straddled by larger reads.
		for (int i = start; i <= end; i++) {
			// Do nothing if no cds stored at this position.
			if (!_cdsByReferencePosition.containsKey(i)) continue;
			// Store any novel cds.
			for (CDS cds : _cdsByReferencePosition.get(i))
				if (!buffer.containsKey(cds)) buffer.put(cds, true);
			}

		// Convert buffer to array.
		CDS[] cds = new CDS[buffer.size()];
		buffer.keySet().toArray(cds);
		return cds;

		} // end of method.

	////////////////////////////////////////////////////////////////////////////

	public Exon[] getExonsBetween(final int start, final int end) {

		HashMap<Exon, Boolean> buffer = new HashMap<Exon, Boolean>();

		// Work through reference positions from start to end, inclusive.
		//for (int i : new int[]{start, end}) { // TODO - this simplified method is faster but excludes exons straddled by larger reads.
		for (int i = start; i <= end; i++) {
			// Do nothing if no exons stored at this position.
			if (!_exonsByReferencePosition.containsKey(i)) continue;
			// Store any novel exon.
			for (Exon exon : _exonsByReferencePosition.get(i))
				if (!buffer.containsKey(exon)) buffer.put(exon, true);
			}

		// Convert buffer to array.
		Exon[] exons = new Exon[buffer.size()];
		buffer.keySet().toArray(exons);
		return exons;

		} // end of method.

	////////////////////////////////////////////////////////////////////////////

	public Annotation[] getAnnotationsBetween(
		final int start,
		final int end,
		final ReportType type
		) {

		switch (type) {
			case Exon		: return getExonsBetween(start, end);
			case CDS		: return getCDSsBetween(start, end);
			case All		: throw new RuntimeException("Operation not appropriate for '" + type + "'");
			case Transcript	: throw new RuntimeException("Operation not appropriate for '" + type + "'");
			default			: throw new RuntimeException("Unexpected type '" + type + "'");
			}

		} // end of method.

	////////////////////////////////////////////////////////////////////////////

	public CDS[] getCDSs() {
		CDS[] CDSs = new CDS[_CDSs.size()];
		_CDSs.keySet().toArray(CDSs);
		return CDSs;
		} // End of method.

	////////////////////////////////////////////////////////////////////////////

	public Exon[] getExons() {
		Exon[] exons = new Exon[_exons.size()];
		_exons.keySet().toArray(exons);
		return exons;
		} // End of method.

	////////////////////////////////////////////////////////////////////////////

	public Transcript[] getTranscripts() {
		Transcript[] transcripts = new Transcript[_transcripts.size()];
		_transcripts.values().toArray(transcripts);
		return transcripts;
		} // End of method.

	////////////////////////////////////////////////////////////////////////////

	public Annotation[] getAnnotations(final ReportType type) {

		switch (type) {
			case Exon		: return getExons();
			case CDS		: return getCDSs();
			case Transcript	: return getTranscripts();
			case All		: throw new RuntimeException("Operation not appropriate for '" + type + "'");
			default			: throw new RuntimeException("Unexpected type '" + type + "'");
			}

		} // end of method.

	//##########################################################################
	
	// INNER CLASSES
	
	//##########################################################################
	
    } // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////




