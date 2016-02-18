//==============================================================================
//
//   SequenceUtil.java
//   Created: Nov 17, 2011 at 12:32:27 PM
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

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
/**
 * 
 * @author  Kevin Ashelford.
 */
public class SequenceUtil {

	//##########################################################################
	// CLASS FIELDS
	////////////////////////////////////////////////////////////////////////////
	// MEMBER FIELDS
	//##########################################################################
	// CONSTRUCTOR
	/**
	 * Creates a new instance of <code>SequenceUtil</code>.
	 */
	public SequenceUtil() {
		
		} // End of constructor.
	
	//##########################################################################

	// METHODS
	
	public static String toFakespace(
		final String csSeq,
		final boolean ignoreFirstBase
		) throws Exception {

		StringBuilder buffer = new StringBuilder();

		// Ignore first basespace base and first colourspace base.
		
		if (!ignoreFirstBase) 
			throw new RuntimeException("Allowing for first bases to be converted is yet to be coded.");
		
		char[] bases = csSeq.toCharArray();
		for (int i = 2; i < bases.length; i++) {

			switch (bases[i]) {
				case '.' : buffer.append("N"); continue;
				case '0' : buffer.append("A"); continue;
				case '1' : buffer.append("C"); continue;
				case '2' : buffer.append("G"); continue;
				case '3' : buffer.append("T"); continue;
				case '4' : buffer.append("N"); continue;
				case '5' : buffer.append("N"); continue;
				case '6' : buffer.append("N"); continue;
				default : throw new Exception("Unexpected base: " + bases[i]);
				}

			}

		return buffer.toString();

		} // End of method.

	////////////////////////////////////////////////////////////////////////////

	public static String encodeQualString(
		final String qualArray,
		final boolean ignoreFirstBase,
		final QVEncoding encoding
		) throws Exception {

		StringBuilder buffer = new StringBuilder();

		String[] values = qualArray.split("\\s");

		for (int i = 0; i < values.length; i++) {

			// Start at i = 1 if ignore first base (Ignore first QV if first 
			// colourspace value is ignored)
			if (i == 0 && ignoreFirstBase) continue; 
			
			
			int n = Integer.parseInt(values[i]);

			if (n == -1) {n = 0;}


			int x = 33;
			if (encoding != QVEncoding.Illumina33) 
				throw new Exception("Alternative QV encodings not yet supported.");
			
			buffer.append(
				// Solexa/Illumina format.
				//(char)(n+64)
				// Phred format (assumes postive integers only)
				(char)(n+x)  
				);

			}

		return buffer.toString();

		} // End of method.
	
	//##########################################################################
	// INNER CLASSES
	//##########################################################################

} // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

