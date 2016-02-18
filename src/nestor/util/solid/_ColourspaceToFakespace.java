//==============================================================================
//
//   _ColourspaceToFakespace.java
//   Created: Oct 30, 2012 at 3:31:15 PM
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

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
/**
 * 
 * @author  Kevin Ashelford.
 */
public class _ColourspaceToFakespace implements IFileConvertor {

	//##########################################################################
	// CLASS FIELDS
	////////////////////////////////////////////////////////////////////////////
	// MEMBER FIELDS
	//##########################################################################
	// CONSTRUCTOR
	/**
	 * Creates a new instance of <code>_ColourspaceToFakespace</code>.
	 */
	public _ColourspaceToFakespace() {
		
	} // End of constructor.
	//##########################################################################
	// METHODS
	
	public void convert(
                final File infile,
                final File outfile
                ) throws IOException {

                BufferedReader in = new BufferedReader(new FileReader(infile));
                PrintWriter out = new PrintWriter(new FileWriter(outfile));

                String line = null;
                while ((line = in.readLine()) != null) {

                        // Comment line.
                        if (line.startsWith("#")) {
                                out.println(line);
                                }

                        // Whitespace only.
                        else if (line.matches("^\\s*$")) {
                                out.println(line);
                                }

                        // Header
                        else if (line.startsWith(">")) {
                                out.println(line);
                                }

                        // sequence
                        else if (line.matches("^[ACGT]{0,1}[\\.0123456]+$")) {
                                out.println(process(line));
                                }

                        // Something unexpected.

                        }

                in.close();
                out.close();

                } // End of method.

	////////////////////////////////////////////////////////////////////////

        private String process(final String csSeq) {

                StringBuilder fsSeq = new StringBuilder();

                // Remove first first basespace base?  Note if base left in 
                // place able translate back into basespace.  AB consistently 
                // removes first base though...
                char[] csBases = csSeq.toCharArray();

                for (int i = 0; i < csBases.length; i++) {

                        switch(csBases[i]) {
                                case 'A' : fsSeq.append("A"); continue; // Leaving first base
                                case 'C' : fsSeq.append("C"); continue; //
                                case 'G' : fsSeq.append("G"); continue; //
                                case 'T' : fsSeq.append("T"); continue; //
                                case '.' : fsSeq.append("."); continue;
                                case '0' : fsSeq.append("A"); continue;
                                case '1' : fsSeq.append("C"); continue;
                                case '2' : fsSeq.append("G"); continue;
                                case '3' : fsSeq.append("T"); continue;
                                case '4' : fsSeq.append("N"); continue;
                                case '5' : fsSeq.append("N"); continue;
                                case '6' : fsSeq.append("N"); continue;
                                default :
                                        throw new RuntimeException(
                                                "Unexpected cs base: '" + csBases[i] + "'"
                                                );
                                }

                        }


                return fsSeq.toString();

                } // End of method.

	
	//##########################################################################
	// INNER CLASSES
	//##########################################################################
} // End of class.
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

