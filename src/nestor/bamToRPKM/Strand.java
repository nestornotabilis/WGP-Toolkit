//==============================================================================
//
//   Strand.java
//   Created: Mar 14, 2012 at 3:53:13 PM
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

package nestor.bamToRPKM;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
/**
 * 
 * @author  Kevin Ashelford.
 */
public enum Strand {

	//##########################################################################

	Plus('+'), 
	Minus('-'), 
	Ignore('.'), 
	Unknown('.');
	
	
	private final char _symbol;

	Strand(char symbol) {_symbol = symbol;}

	public char getChar() {return _symbol;}
	
	public static Strand getStrand(char symbol) {
		
		switch (symbol) {
			case '+': return Plus;
			case '-': return Minus;
			case '.': return Unknown;
			default: throw new RuntimeException("Unexpected strand character: " + symbol); 	
			}
		
		}
	
	//##########################################################################
	
	} // End of enum.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

