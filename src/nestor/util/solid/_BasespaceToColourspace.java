//==============================================================================
//
//   _BasespaceToColourspace.java
//   Created: Oct 30, 2012 at 3:34:30 PM
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
package nestor.util.solid;

import java.io.*;
import java.util.regex.*;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
/**
 * 
 * @author  Kevin Ashelford.
 */
public class _BasespaceToColourspace implements IFileConvertor {

	//##########################################################################
	// CLASS FIELDS
	////////////////////////////////////////////////////////////////////////////
	// MEMBER FIELDS
	
	private final Matcher _seqRegex = Pattern.compile("^([ACGTMRWSYKBDHVN]+)$").matcher("");

        private final ColourSpaceFromStringGenerator _g = new ColourSpaceFromStringGenerator();
        private final Key _key = new Key();

		
	//##########################################################################
	// CONSTRUCTOR
	/**
	 * Creates a new instance of <code>_BasespaceToColourspace</code>.
	 */
	public _BasespaceToColourspace() {
		
	} // End of constructor.
	//##########################################################################
	// METHODS
	
	public void convert(final File infile, final File outfile) throws IOException {

                // Open input / output streams.
                BufferedReader in = new BufferedReader(new FileReader(infile));
                PrintWriter out = new PrintWriter(new FileWriter(outfile));


                Character base0 = Character.UNASSIGNED;
                String colourspaceSeq = null;

                String line= null;
                while ((line = in.readLine()) != null) {

                        // Header
                        if (line.startsWith(">")) {
                                out.println(line);
                                // Reset.
                                base0 = Character.UNASSIGNED;
                                }

                        // Whitespace only.
                        else if (line.matches("^\\s*$")) {
                                out.println(line);
                                }

                        // Sequence.
                        else if (_seqRegex.reset(line).matches()) {


                                // Get sequence string.
                                String seq = _seqRegex.group(1);

                                // Convert all degenerate bases to N.
                                seq = seq.replaceAll("[MRWSYKBDHV]", "N");

                                Character first = Character.UNASSIGNED;

                                if (base0 == Character.UNASSIGNED) {
                                        first = seq.charAt(0);
                                        }
                                else {
                                        seq = base0 + seq;
                                        }


                                _g.submitData(seq);
                                base0 = _g.getBase0();
                                colourspaceSeq = _g.getColourBaseSeq();




                                if (first != Character.UNASSIGNED) {
                                        colourspaceSeq = first + colourspaceSeq;
                                        first = Character.UNASSIGNED;
                                        }


                                out.println(colourspaceSeq);

                                } // End of seq regex if block.

                        else {
                                throw new RuntimeException("Unexpected line:\n" + line);
                                }


                        } // End of while loop - no more lines in infile.

                in.close();
                out.close();

                } // End of method.

	
	//##########################################################################
	// INNER CLASSES
	
	private class ColourSpaceFromStringGenerator {

                private StringBuilder colourspace;
                private char base0;

                public void submitData(final String seqString) {

                        char[] basespace = seqString.toCharArray();
                        colourspace = new StringBuilder();
                        base0 = basespace[basespace.length-1];

                        for (int i = 0; i < basespace.length - 1; i++) {
                                char firstbase = basespace[i];
                                char secondbase = basespace[i+1];


                                colourspace.append(_key.getValue(firstbase, secondbase));

                                }

                        }

                public char getBase0 () {
                        return base0;
                        }

                public String getColourBaseSeq () {
                        return colourspace.toString();
                        }


                } // End of inner class.

        ////////////////////////////////////////////////////////////////////////
	
	private class Key {

                public char getValue(final char firstbase, final char secondbase) {


                        if (firstbase == 'A') {
                                switch (secondbase) {
                                        case 'A': return '0';
                                        case 'C': return '1';
                                        case 'G': return '2';
                                        case 'T': return '3';
                                        case 'N': return '4';
                                        default: throw new RuntimeException("Opps!");
                                                }
                                } // End of A if block.


                        else if (firstbase == 'C') {
                                switch (secondbase) {
                                        case 'A': return '1';
                                        case 'C': return '0';
                                        case 'G': return '3';
                                        case 'T': return '2';
                                        case 'N': return '4';
                                        default: throw new RuntimeException("Opps!");
                                                }
                                } // End of C if block.


                        else if (firstbase == 'G') {
                                switch (secondbase) {
                                        case 'A': return '2';
                                        case 'C': return '3';
                                        case 'G': return '0';
                                        case 'T': return '1';
                                        case 'N': return '4';
                                        default: throw new RuntimeException("Opps!");
                                                }
                                } // End of G if block.


                        else if (firstbase == 'T') {
                                switch (secondbase) {
                                        case 'A': return '3';
                                        case 'C': return '2';
                                        case 'G': return '1';
                                        case 'T': return '0';
                                        case 'N': return '4';
                                        default: throw new RuntimeException("Opps!");
                                                }
                                } // End of T if block.


                        else if (firstbase == 'N') {
                                switch (secondbase) {
                                        case 'A': return '5';
                                        case 'C': return '5';
                                        case 'G': return '5';
                                        case 'T': return '5';
                                        case 'N': return '6';
                                        default: throw new RuntimeException("Opps!");
                                                }
                                } // End of T if block.


                        throw new RuntimeException("Unaccounted for: " + firstbase + ", " + secondbase);

                        }


                } // End of inner class.

	
	//##########################################################################
} // End of class.
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

