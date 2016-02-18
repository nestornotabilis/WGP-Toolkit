//==============================================================================
//
//   IBoxAndWhiskerFormatter.java
//   Created: Jan 6, 2011 at 11:13:35 AM
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

package nestor.graphics.newplot.boxAndWhisker;

import nestor.graphics.newplot.ILineFormatter;
import nestor.stats.Stats;
import java.awt.Color;
import nestor.graphics.newplot.Symbol;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 * Interface for formatter objects associated with box-and-whisker plots.
 * @author  Kevin Ashelford.
 */
public interface IBoxAndWhiskerFormatter extends ILineFormatter {

	//##########################################################################


	/**
	 Sets plot symbol.
	 @param symbol Symbol enum.
	 */
	public void setSymbol(Symbol symbol);

	/**
	 Gets plot symbol.
	 @return Symbol enum.
	 */
	public Symbol getSymbol();

	/**
	 Sets symbol size.
	 @param size Symbol size.
	 */
	public void setSymbolSize(int size);

	/**
	 Gets symbol size.
	 @return Symbol size.
	 */
	public int getSymbolSize();

	/**
	 Sets symbol colour.
	 @param color Symbol colour.
	 */
	public void setSymbolColor(Color color);

	/**
	 Gets symbol colour.
	 @return Symbol colour.
	 */
	public Color getSymbolColor();

	/**
	 Returns colour of box-and-whisker box.
	 @return Box colour
	 */
	public Color getBoxColor();

	/**
	 Sets colour of box-and-whisker box.
	 @param color Box colour.
	 */
	public void setBoxColor(Color color);


	/**
	 Returns stats object encapsulating data being formatted.
	 @return Stats object.
	 */
	public Stats getStats();

	
	//##########################################################################
	
    } // End of interface.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////