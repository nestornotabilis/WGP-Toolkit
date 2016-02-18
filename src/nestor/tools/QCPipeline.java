//==============================================================================
//
//   QCPipeline.java
//   Created: 11/15/2011 16:13:22
//   Copyright (C) 2011, University of Liverpool.
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

package nestor.tools;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 * Entry class.
 * @author Kevin Ashelford.
 */
public class QCPipeline {

	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS
	
	//##########################################################################
	
	// ENTRY POINT

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		
        new QCPipeline(args[0], args[1]);
		
		} // End of main method.

	//##########################################################################

	// CONSTRUCTOR
	
	/**
	 * Creates a new instance.
	 */
    	public QCPipeline(final String text1, final String text2) {
	
		System.out.println("text1=" + text1 + "; text2=" + text2);
	
		} // End of constructor.

	//##########################################################################
		
	// METHODS
	
	//##########################################################################
	
	// INNER CLASSES
	
	//##########################################################################
	
	} // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
