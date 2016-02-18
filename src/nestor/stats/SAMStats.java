//==============================================================================
//
//   SAMStats.java
//   Created: Nov 28, 2011 at 4:39:39 PM
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

package nestor.stats;

import net.sf.samtools.*;
import java.util.*;
import nestor.util.MutableInteger;
import nestor.util.MutableLong;
import nestor.graphics.newplot.*;
import nestor.tools.BedData;
import nestor.util.QVEncoding;
	
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 * 
 * @author  Kevin Ashelford.
 */
public class SAMStats {

	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS
	
	private String _id		= "";
	private long _n			= 0;
	private long _unmapped	= 0;
	private long _unique	= 0;
	private long _secondary = 0;
	
	private final TreeMap<Integer, MutableInteger> _mapqValues = 
			new TreeMap<Integer, MutableInteger>();
	
	private final ArrayList<Integer> _readLengths = new ArrayList<Integer>();
	private final HashMap<Integer, MutableLong> _nByLength = new HashMap<Integer, MutableLong>();
	
	
	final TreeMap<Integer, MutableInteger> _meanQuals = new TreeMap<Integer, MutableInteger>();
	final TreeMap<Integer, MutableInteger> _badQuals = new TreeMap<Integer, MutableInteger>();
	
	//##########################################################################
	
	// CONSTRUCTOR
	
	/**
	 * Creates a new instance of <code>SAMStats</code>.
	 */
	public SAMStats(final SAMFileReader bamIn, final String id) {
		this(bamIn);
		_id = id;
		} // End of constructor.
	
	////////////////////////////////////////////////////////////////////////////
	
	public SAMStats(final SAMFileReader bamIn) {
		this(bamIn, new BedData());
		
		} // End of constructor.
	
	////////////////////////////////////////////////////////////////////////////
	
	public SAMStats(
		final SAMFileReader bamIn,
		final Integer cutoff, 
		final QVEncoding encoding
		) {
		this(bamIn, new BedData(), cutoff, encoding);
		
		} // End of constructor.
	
	////////////////////////////////////////////////////////////////////////////
	
	public SAMStats(
		final SAMFileReader bamIn, 
		final BedData bedData
		) {
		
		this(bamIn, bedData, null, null);
		
		} // End of constructor.
	
	////////////////////////////////////////////////////////////////////////////
	
	public SAMStats(
		final SAMFileReader bamIn, 
		final BedData bedData,
		final Integer cutoff, 
		final QVEncoding encoding
		) {
		
		// Silence over-strict validation errors.
		bamIn.setValidationStringency(SAMFileReader.ValidationStringency.SILENT);
		
		// Process each record in sam/bam input stream.		
		for (final SAMRecord record : bamIn) {
			
			// Will ignore IF bed file exists AND not covered by that bed file.
			if (bedData.getSize() > 0 && !isValidLine(record, bedData)) continue;
			
			// Will ignore read if not primary alignment.
			// Do this BEFORE counting reads - otherwise some reads will be counted
			// more than once.
			if ((record.getFlags() & 256) == 256) {
				_secondary++;
				continue;
				}
			
			// Get length of current read.
			Integer readLength = record.getReadLength();
			
			// If not already noted, add to list of read lengths encountered.
			if (!_readLengths.contains(readLength)) {
				_readLengths.add(readLength);
				}
			 
			// Increment count for this readLength.
			if (!_nByLength.containsKey(readLength)) {
				_nByLength.put(readLength, new MutableLong(0));
				}
			
			_nByLength.get(readLength).increment();
			
			// Read count (duplicates data but left for backwards compatability).
			_n++;
			
			
			
			// NOTE THIS ONLY WORKS WITH BAMS THAT IMPLEMENT THIS ATTRIBUTE
			// SUCH AS BWA GENERATED BAMS.
			Character xt = (Character)record.getAttribute("XT");
			if (xt != null && xt.equals('U')) _unique++; 
			
			
			// Will ignore read if unmapped - use bitwise flag as most reliable
			// means of assessing mapped status.
			if ((record.getFlags() & 4) == 4) {
				_unmapped++;
				continue;
				}
			
			
			
			// Record mapq value
			Integer mapq = record.getMappingQuality();
			if (_mapqValues.containsKey(mapq)) {
				_mapqValues.get(mapq).increment();	
				}
			else {
				_mapqValues.put(mapq, new MutableInteger(1));	
				}
			
			// If wanting to collect QV info (assume evidence of encoding 
			// signifies this) ... 
			if (encoding != null) {
				final String qualLine = record.getBaseQualityString();
				
				// Process QV line
				process(
					qualLine, 
					_meanQuals, 
					_badQuals, 
					cutoff, 
					encoding
					);
				
				
				}
			
			
			

			}
	
		bamIn.close();
		
		
		} // End of constructor.
	
	//##########################################################################
	
	// METHODS
	
	public long getReadCount() {
		return _n;
		} // End of method.
	
	public long getUniqueReadCount() {
		return _unique;
		} // End of method. 
	
	public long getSecondaryAlignmentCount() {
		return _secondary;
		}
	
	public long getUnmappedReadCount() {
		return _unmapped;
		} // End of method.
	
	public long getMappedReadCount() {
		return _n - _unmapped;
		} // End of method.
	
	public long getReadCountWithMAPQOfAtLeast(final int minMAPQ) {
		return getMAPQCount(minMAPQ);
		}
	
	public int[] getReadLengths() {
		
		int[] lengths = new int[_readLengths.size()];
		
		for (int i = 0; i < _readLengths.size(); i++) {
			lengths[i] = _readLengths.get(i);
			}
		
		Arrays.sort(lengths);
		
		return lengths;
		}
	
	public long getReadCount(int length) {
		
		return _nByLength.get(length).value();
		
		}
	
	public TreeMap<Integer, MutableInteger> getMeanQuals() {
		
		if (_meanQuals.isEmpty()) {
			throw new RuntimeException("No mean QVs - perhaps using the wrong constructor?");
			}
		else {
			return _meanQuals;
			}
		
		} // End of method.
	
	
	public TreeMap<Integer, MutableInteger> getBadQuals() {
		
		if (_badQuals.isEmpty()) {
			throw new RuntimeException("No bad QVs - perhaps using the wrong constructor?");
			}
		else {
			return _badQuals;
			}
		
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	private long getMAPQCount(final int minMAPQ) {
		
		Iterator<Integer> it = _mapqValues.keySet().iterator();
		
		long count = 0;
		while (it.hasNext()) {
			
			int mapq = it.next();
			
			if (mapq >= minMAPQ) {
				count += _mapqValues.get(mapq).value();
					}
			
			}
		
		return count;
		
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	public IHistogram getMapqHistogram() {
		
		Coordinate[] c = new Coordinate[_mapqValues.size()];
		
		
		Iterator<Integer> it = _mapqValues.keySet().iterator();
		int i = 0;
		while (it.hasNext()) {
			
			Integer key = it.next();
			
			double percentage = 
				(double)(_mapqValues.get(key).value()) /(double)_n * 100d;
			
			c[i++] = new Coordinate(key, percentage);
			
			}
		
		
		
		ICoordinateCollection cc =
				CoordinateCollectionFactory.createCoordinateCollection(
					c,
					_id
					);
		
		return HistogramFactory.createHistogram(cc);
		
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	private boolean isValidLine(final SAMRecord record, final BedData bedData) {
		
		
		final int flag		= record.getFlags();
		final String chr	= record.getReferenceName();
		final int start		= record.getAlignmentStart();
		final int end		= record.getAlignmentEnd();
		
		

        // Not valid if unmapped.
		if ((flag & 4) == 4) return false;
		
		// Is Valid
		if (bedData.exists(chr, start) || bedData.exists(chr, end)) return true;
		
		// Is invalid 
		return false;
		
		} // end of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	private void process(
		final String qualLine, 
		final TreeMap<Integer, MutableInteger> meanQuals,
		final TreeMap<Integer, MutableInteger> badQuals,
		final Integer cutoff,
		final QVEncoding encoding
		) {
		
		int sum = 0;
		int noOfBadQuals = 0;
		
		// Process qual line.
		final char[] asciiArray = qualLine.toCharArray();
		for (char ascii : asciiArray) {
			
			int qv = convertToNumber(ascii, encoding);
			
			sum += qv;
			if (qv <= cutoff) noOfBadQuals++;
			
			}
		
		
		// Calculate mean qual for read.
		int mean = (int)(Math.ceil((double)sum/(double)qualLine.length()));
		
		// Increment mean qual.
		if (meanQuals.containsKey(mean)) {
			meanQuals.get(mean).increment();
			}
		else {
			meanQuals.put(mean, new MutableInteger(1));
			}
		
		
		// Increment bad qual.
		if (badQuals.containsKey(noOfBadQuals)) {
			badQuals.get(noOfBadQuals).increment();
			} 
		else {
			badQuals.put(noOfBadQuals, new MutableInteger(1));
			}
		
		
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	private int convertToNumber(final char ascii, final QVEncoding encoding) {
		
		if (!encoding.equals(QVEncoding.Illumina33)) 
			throw new UnsupportedOperationException(
				"Only Illumina33 currently supported"
				);
		
		return (int)ascii - 33;
	
		} // End of method.
	
	//##########################################################################
	
	// INNER CLASSES
	
	//##########################################################################

	} // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

