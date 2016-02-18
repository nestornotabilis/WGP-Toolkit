//==============================================================================
//
//   FileConvertorFactory.java
//   Created: Oct 30, 2012 at 3:29:08 PM
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

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
/**
 * 
 * @author  Kevin Ashelford.
 */
public class FileConvertorFactory {

	//##########################################################################
	// CLASS FIELDS
	
	private static final _BasespaceToColourspace _bsToCs =
                new _BasespaceToColourspace();

        private static final _BasespaceToFakespace _bsToFs =
                new _BasespaceToFakespace();

        private static final _ColourspaceToFakespace _csToFs =
                new _ColourspaceToFakespace();
	
	////////////////////////////////////////////////////////////////////////////
	// MEMBER FIELDS
	//##########################################################################
	// CONSTRUCTOR
	/**
	 * Creates a new instance of <code>FileConvertorFactory</code>.
	 */
	public FileConvertorFactory() {
		
	} // End of constructor.
	//##########################################################################
	// METHODS
	
	/**
         * Returns file convertor appropriate to the file conversion requested.
         * @param type Type of file conversion to perform.
         * @return File convertor object (singleton).
         */
        public static IFileConvertor getFileConvertor(FileConversion type) {

                if (type == FileConversion.BasespaceToColourspace) {
                        return _bsToCs;
                        }

                else if (type == FileConversion.BasespaceToFakespace) {
                        return _bsToFs;
                        }

                else if (type == FileConversion.ColourspaceToFakespace) {
                        return _csToFs;
                        }

                else {
                        throw
                                new IllegalArgumentException(
                                        type.toString() + " not supported."
                                        );
                        }

                } // End of method.

	
	//##########################################################################
	// INNER CLASSES
	//##########################################################################
} // End of class.
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

