//==============================================================================
//
//   BedData.java
//   Created: Nov 21, 2011 at 2:59:33 PM
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

import java.util.*;
import nestor.util.MutableInteger;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
/**
 * 
 * @author  Kevin Ashelford.
 */
public class BedData {

	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS
	
	private final HashMap<String, HashMap<Integer, MutableInteger>> _data = 
		new HashMap<String, HashMap<Integer, MutableInteger>>();
	
	//##########################################################################
	
	// CONSTRUCTOR
	
	/**
	 * Creates a new instance of <code>BedData</code>.
	 */
	public BedData() {
		
		} // End of constructor.
	
	//##########################################################################
	
	// METHODS
	
	
	public void create(String chr, int pos, int value) {
		
		
		
		// Chr already exists in data store.
		if (_data.containsKey(chr)) {
				
			// Pos already exists
			if (_data.get(chr).containsKey(pos)) {
				// DO NOTHING
				}
			// First time pos encountered for this chr.	
			else {
				_data.get(chr).put(pos, new MutableInteger(value));
				}
			
			}
			
		// Chr previously unencountered.	
		else {
			HashMap<Integer, MutableInteger> posData = 
				new HashMap<Integer, MutableInteger>();
			
			posData.put(pos, new MutableInteger(value));
				
			_data.put(chr, posData);
			
			}
		
		
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	public void setValue(String chr, int pos, int value) {
		
		if (!this.exists(chr, pos)) 
			throw new RuntimeException("Cannot find chr=" + chr + ", pos=" + pos);
		
		_data.get(chr).get(pos).setValue(value);
		
		
		} // end of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	public void increment(String chr, int pos) {
			
		// Chr already exists in data store.
		if (_data.containsKey(chr)) {
				
			// Pos already exists
			if (_data.get(chr).containsKey(pos)) {
				_data.get(chr).get(pos).increment();
				}
			// First time pos encountered for this chr.	
			else {
				_data.get(chr).put(pos, new MutableInteger(1));
				}
			
			}
			
		// Chr previously unencountered.	
		else {
			HashMap<Integer, MutableInteger> posData = 
				new HashMap<Integer, MutableInteger>();
			
			posData.put(pos, new MutableInteger(1));
				
			_data.put(chr, posData);
			
			}
		
		
		} // End of method.
		
	////////////////////////////////////////////////////////////////////////////
		
	public boolean exists(String chr, int pos) {
			
		// If contains chr.
		if (_data.containsKey(chr)) {
			
			// If also contains position.
			if (_data.get(chr).containsKey(pos)) {
				return true;
				}
			// No such position stored against this chr.	
			else {
				return false;
				}
			}
			
		// Chr not stored.	
		else {
			return false;
			}
			
			
			
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////

	public ArrayList<String> getChrList() {
		return new ArrayList<String>(_data.keySet());	
		} // End of method. 
	
	////////////////////////////////////////////////////////////////////////////
	
	public int getSize(final String chr) {
		return _data.get(chr).size();
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	public boolean contains(final String chr) {
		return _data.containsKey(chr);
		}
	
	////////////////////////////////////////////////////////////////////////////
	
	public int getSize() {
		
		int size = 0;
		ArrayList<String> chrs = this.getChrList();
		for(String chr : chrs) {
			size += this.getSize(chr);
			}
		
		return size;
		
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	public HashMap<String, HashMap<Integer, MutableInteger>> getUnderlyingDataStructure() {
		
		return _data;
		
		} // End of method.
	
	//##########################################################################
	
	// INNER CLASSES
	
	//##########################################################################

	} // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

