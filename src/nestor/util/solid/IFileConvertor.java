//==============================================================================
//
//   IFileConvertor.java
//   Created: Oct 30, 2012 at 3:27:44 PM
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
public interface IFileConvertor {

	//##########################################################################

	/**
         * Converts infile and writes to outfile.
         * @param infile File to convert.
         * @param outfile File to write to.
         * @throws IOException thrown.
         */
        public void convert(
                final File infile,
                final File outfile
                ) throws IOException;

	//##########################################################################
	
} // End of interface.
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

