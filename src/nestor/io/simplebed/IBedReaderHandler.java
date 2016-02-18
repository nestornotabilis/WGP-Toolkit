//==============================================================================
//
//   IBedReaderHandler.java
//   Created: Jan 20, 2011 at 4:07:05 PM
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

package nestor.io.simplebed;

import nestor.io.IReaderHandler;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 * Handler interface for bed file format.
 * @author  Kevin Ashelford.
 */
public interface IBedReaderHandler extends IReaderHandler {

	//##########################################################################

	/**
	 * Only called within the reader object.
	 * @param refId Id of reference from which feature derives.
	 * @param featureId Feature id.
	 * @param start One-indexed start position of feature within reference.
	 * @param end One-indexed end position of feature within reference.
	 */
	public void _nextLine(
		final String refId,
		final String featureId,
		final int start,
		final int end
		);
	
	
	//##########################################################################
	
    } // End of interface.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////