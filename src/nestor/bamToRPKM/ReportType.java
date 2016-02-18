//==============================================================================
//
//   ReportType.java
//   Created: Mar 2, 2011 at 11:39:04 AM
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

package nestor.bamToRPKM;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 * 
 * @author  Kevin Ashelford.
 */
public enum ReportType {

	//##########################################################################

	Exon("exon"),

	CDS("CDS"),

	Transcript("transcript"),

	All("all");

	private final String _tag;

	ReportType(String tag) {_tag = tag;}

	public String getTag() {return _tag;}

	//##########################################################################
	
    } // End of enum.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
