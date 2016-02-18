// IPlotLineFormatter.java created on 03 February 2004 at 14:48

//=============================================================================
//
//    Copyright (C) 2004 Kevin Ashelford
//    Contact details:
//    Email: ashelford@cardiff.ac.uk
//    Address:  Cardiff School of Bioscience, Cardiff University, PO. Box 915,
//    Cardiff, UK. CF10 3TL
//
//    This program is free software; you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation; either version 2 of the License, or
//    any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program; if not, write to the Free Software
//    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
//=============================================================================

package nestor.graphics.newplot;

import java.awt.*;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 Interface for encapsulating formating information for plot-lines.
 @author  Kevin Ashelford.
 */
public interface IPlotLineFormatter extends ILineFormatter {
	
	//##########################################################################
	
	/**
	 Sets plot symbol.
	 @param symbol Symbol enumerator.
	 */
	public void setSymbol(Symbol symbol);

	/**
	 Gets plot symbol.
	 @return Symbol enumerator.
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
	 Determines whether there is a gap between plot line and symbol.
	 @param b True if there is to be a gap.
	 */
	public void setGapToSymbol(boolean b);
	
	/**
	 Returns true if there is to be a gap between plot line and symbol.
	 @return True if there is to be a gap.
	 */
	public boolean isGapToSymbol();
	
	/**
	 Sets plot style for line.
	 @param plotStyle PlotStyle enumerator.
	 */
	public void setPlotStyle(PlotStyle plotStyle);
	
	/**
	 Gets plot style for line.
	 @return PlotStyle enumerator.
	 */
	public PlotStyle getPlotStyle();
	
	/**
	 Gets ICoordinateCollection object registered with formatter.
	 @return ICoordinateCollection object.
	 */
	public ICoordinateCollection getCoordinateCollection();
	
	//##########################################################################
	
	} // End of interface.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
