//==============================================================================
//
//   Constants.java
//   Created: Nov 17, 2011 at 3:21:02 PM
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

package nestor.tools;

import java.awt.Color;
import nestor.graphics.newplot.LineStyle;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
/**
 * 
 * @author  Kevin Ashelford.
 */
public interface Constants {

	//##########################################################################

	public final String READ_COUNT				= "read.count";
	public final String MEAN_READ_LENGTH		= "mean.read.length";
	public final String MEAN_MAPPED_READ_LENGTH	= "mean.mapped.read.length";
	
	public final String RAW_DATA_YIELD		= "raw.data.yield";
	public final String MAPPED_DATA_YIELD	= "mapped.data.yield";
	
	public final String TOTAL_READS			= "sam.total.read.count";
	public final String MAPPED_READS		= "sam.mapped.read.count";
	public final String UNIQUE_READS		= "sam.unique.read.count";
	public final String SECONDARY_READS		= "sam.secondary.mapped.read.count";
	
	public final String TOTAL_READS_ONTARGET	= "sam.total.read.count.ontarget";
	public final String MAPPED_READS_ONTARGET	= "sam.mapped.read.count.ontarget";
	public final String UNIQUE_READS_ONTARGET	= "sam.unique.read.count.ontarget";
	
	public final String MEAN_COVERAGE			= "mean.coverage";
	public final String MEDIAN_COVERAGE			= "median.coverage";
	public final String Q1_COVERAGE				= "q1.coverage";
	public final String Q3_COVERAGE				= "q3.coverage";
	public final String REGION_COVERED			= "fraction.covered";
	public final String REGION_COVERED_4x		= "fraction.covered.4x";
	public final String REGION_COVERED_10x		= "fraction.covered.10x";
	public final String REGION_COVERED_20x		= "fraction.covered.20x";
	public final String REGION_COVERED_30x		= "fraction.covered.30x";
	public final String REGION_COVERED_40x		= "fraction.covered.40x";
	public final String REGION_COVERED_50x		= "fraction.covered.50x";
	public final String REGION_COVERED_100x		= "fraction.covered.100x";
	public final String REGION_COVERED_1000x	= "fraction.covered.1000x";
	public final String REGION_COVERED_2000x	= "fraction.covered.2000x";
	public final String REGION_COVERED_3000x	= "fraction.covered.3000x";
	
	
	
	public final String READS_WITH_MAPQ_OF_0_OR_HIGHER = "sam.mapped.read.count.mapq.0plus";
	public final String READS_WITH_MAPQ_OF_1_OR_HIGHER = "sam.mapped.read.count.mapq.1plus";
	public final String READS_WITH_MAPQ_OF_10_OR_HIGHER = "sam.mapped.read.count.mapq.10plus";
	public final String READS_WITH_MAPQ_OF_20_OR_HIGHER = "sam.mapped.read.count.mapq.20plus";
	public final String READS_WITH_MAPQ_OF_30_OR_HIGHER = "sam.mapped.read.count.mapq.30plus";
	
	
	public final String PERCENT_READS_WITH_MEAN_QV30_PLUS = "percent.reads.meanqv.30plus";
	public final String PERCENT_READS_WITH_MEAN_QV25_PLUS = "percent.reads.meanqv.25plus";
	public final String PERCENT_READS_WITH_MEAN_QV20_PLUS = "percent.reads.meanqv.20plus";
	public final String PERCENT_READS_WITH_MEAN_QV15_PLUS = "percent.reads.meanqv.15plus";
	public final String PERCENT_READS_WITH_10_BAD_QV_PLUS = "percent.reads.badqv.10plus";

	
	public final String PERCENT_MAPPED_READS_WITH_MEAN_QV30_PLUS = "percent.mapped.reads.meanqv.30plus";
	public final String PERCENT_MAPPED_READS_WITH_MEAN_QV25_PLUS = "percent.mapped.reads.meanqv.25plus";
	public final String PERCENT_MAPPED_READS_WITH_MEAN_QV20_PLUS = "percent.mapped.reads.meanqv.20plus";
	public final String PERCENT_MAPPED_READS_WITH_MEAN_QV15_PLUS = "percent.mapped.reads.meanqv.15plus";
	public final String PERCENT_MAPPED_READS_WITH_10_BAD_QV_PLUS = "percent.mapped.reads.badqv.10plus";
	
	public final String TARGET_SIZE			= "target.size";
	
	
	public boolean CUMULATIVE_PLOT_SELECTED = false;
	public int HTML_PLOT_HEIGHT				= 300;
	public int HTML_PLOT_WIDTH				= 700;
	public int HTML_BOX_AND_WHISKER_HEIGHT	= 180;
	
	public static final Color[] PLOT_COLOURS = new Color[]{
		new Color(255, 150, 150), //	Color.red,
		new Color(150, 150, 255), //	Color.blue,
		new Color(150, 255, 100), //	Color.green,
		new Color(150, 150, 150), //	Color.black,
		new Color(150, 255, 255), //	Color.cyan,
		new Color(255, 255, 150), //	Color.magenta,
		new Color(255, 150, 255), //	Color.yellow,
		new Color(255, 200, 255), //	Color.orange,
		new Color(255, 200, 200), //	Color.pink,
		new Color(200, 255, 255), //	Color.gray,
		new Color(200, 150, 150), //	Color.lightGray,
		new Color(150, 150, 200)	//	Color.darkGray
		};
	
	public static final LineStyle[] LINE_STYLES = new LineStyle[]{
		LineStyle.Solid,
		LineStyle.Dash,
		LineStyle.ShortDash,
		LineStyle.Dot,
		LineStyle.DashDot
		};
	
	public static final Color MAJOR_GRID_LINE_COLOUR = new Color(230,230,255);
	
	//##########################################################################
	
	} // End of interface.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

