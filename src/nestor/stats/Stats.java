//==============================================================================
//
//   Stats.java
//   Created: Aug 28, 2009 at 6:50:43 PM
//   Copyright (C) 2008, University of Liverpool.
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

package nestor.stats;

import java.util.*;
import java.text.NumberFormat;

////////////////////////////////////////////////////////////////////////////////

/**
 * @author Kevin Ashelford
 */
public class Stats {

	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS

	private String	_id = null;
	
	private long	_N;
	
	private double	_sum;
	private double	_mean			= 0;
	private double	_mode			= 0;
	private double	_median			= 0;
	private double	_N50			= 0;
	private double	_sumOfSquares	= 0;
	private double	_variance		= Double.NaN;
	private double	_C				= Double.NaN;
	private double	_SD				= Double.NaN;
	private double	_q1				= Double.NaN;
	private double	_q3				= Double.NaN;
	private double	_p25			= Double.NaN;
	private double	_p975			= Double.NaN;
	private double	_min			= Double.NaN;
	private double	_max			= Double.NaN;
	private double	_range			= 0;
	
	private double _fractionCovered		= 0;
	private double _fractionCovered4x	= 0;
	private double _fractionCovered10x	= 0;
	private double _fractionCovered20x	= 0;
	private double _fractionCovered30x	= 0;
	
	private long	_numberBasesCovered;
	private long	_numberBasesCovered4x;
	private long	_numberBasesCovered10x;
	private long	_numberBasesCovered20x;
	private long	_numberBasesCovered30x;
	
	
	
	
	//##########################################################################
	
	// CONSTRUCTOR
	
	
	public Stats(final int[] data, final String id) {
		this(data);
		_id = id;
		}
    
	public Stats(final long[] data, final String id) {
		this(data);
		_id = id;
		}
	
	public Stats(final ArrayList<Long> data, final String id) {
		this(data);
		_id = id;
		}
	
	////////////////////////////////////////////////////////////////////////////
	
	public Stats (final String id) {
		_id = id;
		}
	
    public Stats(final int[] data) {
	
		Arrays.sort(data);
		
		// N
		_N = data.length;

		// Sum
		_sum = _Calc.getSum(data);

		// Mean
		_mean = _Calc.getArithmeticMean(data);

		// Median
		_median = _Calc.getMedian(data);

		// Mode
		_mode = _Calc.getMode(data);

		// N50
//		_N50 = _Calc.getN50(data);
		
		// Variance
		_variance = _Calc.getSampleVariance(data, _mean);

		// SD
		_SD = Math.sqrt(_variance);

		// Coefficient of variation
		_C = calculateC(_SD, _mean);

		// lower quartile
		_q1 = _Calc.getLowerQuartile(data);

		// upper quartile
		_q3 = _Calc.getUpperQuartile(data);

		// 2.5% percentile
		_p25 = _Calc.getQuantile(data, 0.025f);

		// 97.5% percentile
		_p975 = _Calc.getQuantile(data, 0.975f);

		// min
		_min = _Calc.getMinimum(data);

		// max
		_max = _Calc.getMaximum(data);

		// range
		_range = _Calc.getRange(data);
		
		long n = 0;
        long above0x = 0;
        long above4x = 0;
        long above10x = 0;
        long above20x = 0;
        long above30x = 0;

        for (int i = 0; i < data.length; i++)  {
                long cov = data[i];
                n++;
                if (cov > 0) above0x++;
                if (cov >= 4) above4x++;
                if (cov >= 10) above10x++;
                if (cov >= 20) above20x++;
                if (cov >= 30) above30x++;
                }
		
		this.setFractionCovered((double)above0x/(double)n);
		this.setFractionCovered4x((double)above4x/(double)n);
		this.setFractionCovered10x((double)above10x/(double)n);
		this.setFractionCovered20x((double)above20x/(double)n);
		this.setFractionCovered30x((double)above30x/(double)n);
		
		this.setNumberBasesCovered(above0x);
		this.setNumberBasesCovered4x(above4x);
		this.setNumberBasesCovered10x(above10x);
		this.setNumberBasesCovered20x(above20x);
		this.setNumberBasesCovered30x(above30x);
		
		
		} // End of constructor.
	
	////////////////////////////////////////////////////////////////////////////
		
	 public Stats(final long[] data) {
	
		Arrays.sort(data);

		// N
		_N = data.length;

		// Sum
		_sum = _Calc.getSum(data);

		// Mean
		_mean = _Calc.getArithmeticMean(data);

		// Median
		_median = _Calc.getMedian(data);

		// Mode
		_mode = _Calc.getMode(data);

		// N50
//		_N50 = _Calc.getN50(data);
		
		// Variance
		_variance = _Calc.getSampleVariance(data, _mean);

		// SD
		_SD = Math.sqrt(_variance);

		// Coefficient of variation
		_C = calculateC(_SD, _mean);

		// lower quartile
		_q1 = _Calc.getLowerQuartile(data);

		// upper quartile
		_q3 = _Calc.getUpperQuartile(data);

		// 2.5% percentile
		_p25 = _Calc.getQuantile(data, 0.025f);

		// 97.5% percentile
		_p975 = _Calc.getQuantile(data, 0.975f);

		// min
		_min = _Calc.getMinimum(data);

		// max
		_max = _Calc.getMaximum(data);

		// range
		_range = _Calc.getRange(data);
		
		long n = 0;
        long above0x = 0;
        long above4x = 0;
        long above10x = 0;
        long above20x = 0;
        long above30x = 0;

        for (int i = 0; i < data.length; i++)  {
                long cov = data[i];
                n++;
                if (cov > 0) above0x++;
                if (cov >= 4) above4x++;
                if (cov >= 10) above10x++;
                if (cov >= 20) above20x++;
                if (cov >= 30) above30x++;
                }
		
		this.setFractionCovered((double)above0x/(double)n);
		this.setFractionCovered4x((double)above4x/(double)n);
		this.setFractionCovered10x((double)above10x/(double)n);
		this.setFractionCovered20x((double)above20x/(double)n);
		this.setFractionCovered30x((double)above30x/(double)n);
		
		this.setNumberBasesCovered(above0x);
		this.setNumberBasesCovered4x(above4x);
		this.setNumberBasesCovered10x(above10x);
		this.setNumberBasesCovered20x(above20x);
		this.setNumberBasesCovered30x(above30x);
		
		} // End of constructor.

	 ///////////////////////////////////////////////////////////////////////////
	 
	 public Stats(final ArrayList<Long> data) {
	
		Collections.sort(data);

		// N
		_N = data.size();

		// Sum
		_sum = _Calc.getSum(data);

		// Mean
		_mean = _Calc.getArithmeticMean(data);

		// Median
		_median = _Calc.getMedian(data);

		// Mode
		_mode = _Calc.getMode(data);

		// N50
//		_N50 = _Calc.getN50(data);
		
		// Variance
		_variance = _Calc.getSampleVariance(data, _mean);

		// SD
		_SD = Math.sqrt(_variance);

		// Coefficient of variation
		_C = calculateC(_SD, _mean);

		// lower quartile
		_q1 = _Calc.getLowerQuartile(data);

		// upper quartile
		_q3 = _Calc.getUpperQuartile(data);

		// 2.5% percentile
		_p25 = _Calc.getQuantile(data, 0.025f);

		// 97.5% percentile
		_p975 = _Calc.getQuantile(data, 0.975f);

		// min
		_min = _Calc.getMinimum(data);

		// max
		_max = _Calc.getMaximum(data);

		// range
		_range = _Calc.getRange(data);
		
		long n = 0;
        long above0x = 0;
        long above4x = 0;
        long above10x = 0;
        long above20x = 0;
        long above30x = 0;

        for (int i = 0; i < data.size(); i++)  {
                long cov = data.get(i);
                n++;
                if (cov > 0) above0x++;
                if (cov >= 4) above4x++;
                if (cov >= 10) above10x++;
                if (cov >= 20) above20x++;
                if (cov >= 30) above30x++;
                }
		
		this.setFractionCovered((double)above0x/(double)n);
		this.setFractionCovered4x((double)above4x/(double)n);
		this.setFractionCovered10x((double)above10x/(double)n);
		this.setFractionCovered20x((double)above20x/(double)n);
		this.setFractionCovered30x((double)above30x/(double)n);
		
		this.setNumberBasesCovered(above0x);
		this.setNumberBasesCovered4x(above4x);
		this.setNumberBasesCovered10x(above10x);
		this.setNumberBasesCovered20x(above20x);
		this.setNumberBasesCovered30x(above30x);
		
		} // End of constructor.


	//##########################################################################
		
	// METHODS
	
	private double calculateC(double sd, double mean) {
		if (mean == 0) return Double.NaN;
		return sd * (100 / mean);
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////

	public String getId() {return _id;}
	public void setId(String id) {_id = id;}
	
	/**
	 * Returns total, N.
	 * @return Total.
	 */
	public long getN() {return _N;}

	/**
	 * Sets total.
	 * @param n Total.
	 */
	public void setN(final long n) {_N = n;}
	
	
	public long getNumberBasesCovered() {return _numberBasesCovered;}
	public long getNumberBasesCovered4x() {return _numberBasesCovered4x;}
	public long getNumberBasesCovered10x() {return _numberBasesCovered10x;}
	public long getNumberBasesCovered20x() {return _numberBasesCovered20x;}
	public long getNumberBasesCovered30x() {return _numberBasesCovered30x;}
	
	public void setNumberBasesCovered(final long n) {_numberBasesCovered = n;}
	public void setNumberBasesCovered4x(final long n) {_numberBasesCovered4x = n;}
	public void setNumberBasesCovered10x(final long n) {_numberBasesCovered10x = n;}
	public void setNumberBasesCovered20x(final long n) {_numberBasesCovered20x = n;}
	public void setNumberBasesCovered30x(final long n) {_numberBasesCovered30x = n;}
	
	/**
	 * Returns sum.
	 * @return Sum.
	 */
	public double getSum() {return _sum;}
	
	/**
	 * Sets sum.
	 * @param sum Sum.
	 */
	public void setSum(final double sum) {_sum = sum;}

	/**
	 * Returns arithmetic mean.
	 * @return Mean.
	 */
	public double getArithmeticMean() {return _mean;}
	
	/**
	 * Sets arithmetic mean.
	 * @param mean Mean.
	 */
	public void setArithmeticMean(final double mean) {_mean = mean;}

	/**
	 * Returns mode.
	 * @return Mode.
	 */
	public double getMode() {return _mode;}
	
	/**
	 * Sets mode.
	 * @param mode Mode.
	 */
	public void setMode(final double mode) {_mode = mode;}

	/**
	 * Returns median.
	 * @return Median.
	 */
	public double getMedian() {return _median;}

	/**
	 * Sets median.
	 * @param median Median.
	 */
	public void setMedian(final double median) {_median = median;}

	
	/**
	 * Returns N50.
	 * @return N50.
	 */
	public double getN50() {return _N50;}

	/**
	 * Sets median.
	 * @param median Median.
	 */
	public void setN50(final double N50) {_N50 = N50;}
	
	
	/**
	 * Returns standard deviation.
	 * @return Standard deviation.
	 */
	public double getStandardDeviation() {return _SD;}
	
	/**
	 * Sets standard deviation.
	 * @param sd Standard deviation.
	 */
	public void setStandardDeviation(final double sd) {_SD = sd;}

	/**
	 * returns coefficient of variation.
	 * @return Coefficient of variation.
	 */
	public double getCoefficientOfVariation() {return _C;}
	
	/**
	 * Sets coefficient of variation.
	 * @param C Coefficient of variation.
	 */
	public void setCoefficientOfVariation(final double C) {_C = C;}

	/**
	 * Returns variance.
	 * @return Variance.
	 */
	public double getVariance() {return _variance;}
	
	/**
	 * Sets variance.
	 * @param variance Variance.
	 */
	public void setVariance(final double variance) {_variance = variance;}

	/**
	 * Returns sum of squares.
	 * @return Sum of squares.
	 */
	public double getSumOfSquares() {return _sumOfSquares;}
	
	/**
	 * Sets sum of squares.
	 * @param sumOfSquares Sum of squares.
	 */
	public void setSumOfSquares(final double sumOfSquares) {_sumOfSquares = sumOfSquares;}

	/**
	 * Returns lower quartile (q1).
	 * @return Lower quartile.
	 */
	public double getLowerQuartile() {return _q1;}
	
	/**
	 * Sets lower quartile (q1).
	 * @param q1 Lower quartile.
	 */
	public void setLowerQuartile(final double q1) {_q1 = q1;}

	/**
	 * Returns upper quartile (q3).
	 * @return Upper quartile.
	 */
	public double getUpperQuartile() {return _q3;}
	
	/**
	 * Sets upper quartile (q3).
	 * @param q3 Upper quartile.
	 */
	public void setUpperQuartile(final double q3) {_q3 = q3;}

	/**
	 * Returns 2.5% percentile.
	 * @return Percentile.
	 */
	public double getTwoPointFivePercentile() {return _p25;}
	
	/**
	 * Sets 2.5% percentile.
	 * @param p Percentile.
	 */
	public void setTwoPointFivePercentile(final double p) {_p25 = p;}

	/**
	 * Returns 97.5% percentile.
	 * @return Percentile.
	 */
	public double getNinetySevenPointFivePercentile() {return _p975;}
	
	/**
	 * Sets 97.5% percentile.
	 * @param p Percentile.
	 */
	public void setNinetySevenPointFivePercentile(final double p) {_p975 = p;}

	/**
	 * Returns minimum.
	 * @return Minimum.
	 */
	public double getMinimum() {return _min;}
	
	/**
	 * Sets minimum.
	 * @param min Minimum.
	 */
	public void setMinimum(final double min) {_min = min;}

	/**
	 * Returns maximum.
	 * @return Maximum.
	 */
	public double getMaximum() {return _max;}
	
	/**
	 * Sets maximum.
	 * @param max Maximum.
	 */
	public void setMaximum(final double max) {_max = max;}

	/**
	 * Returns range.
	 * @return range.
	 */
	public double getRange() {return _range;}
	
	/**
	 * Sets range.
	 * @param range Range.
	 */
	public void setRange(final double range) {_range = range;}

	
	public void setFractionCovered(final double fraction) {_fractionCovered = fraction;}
	public double getFractionCovered() {return _fractionCovered;}
	
	public void setFractionCovered4x(final double fraction) {_fractionCovered4x = fraction;}
	public double getFractionCovered4x() {return _fractionCovered4x;}
	
	public void setFractionCovered10x(final double fraction) {_fractionCovered10x = fraction;}
	public double getFractionCovered10x() {return _fractionCovered10x;}
	
	public void setFractionCovered20x(final double fraction) {_fractionCovered20x = fraction;}
	public double getFractionCovered20x() {return _fractionCovered20x;}
	
	public void setFractionCovered30x(final double fraction) {_fractionCovered30x = fraction;}
	public double getFractionCovered30x() {return _fractionCovered30x;}
	

	/**
	 * Returns string representation of stats object.
	 * @return String summary.
	 */
	@Override
	public String toString() {

		NumberFormat nf = NumberFormat.getInstance();

		return 
			
			nf.format(this.getN()) + " " +
			preprocess(this.getArithmeticMean(), "%-9.2f") + " " +
			preprocess(this.getMedian(), "%-9.2f") + " " +
			preprocess(this.getStandardDeviation(), "%-9.2f") + " " +
			preprocess(this.getLowerQuartile(), "%-9.2f") + " " +
			preprocess(this.getUpperQuartile(), "%-9.2f") + " " +
			preprocess(this.getTwoPointFivePercentile(), "%-9.2f") + " " +
			preprocess(this.getNinetySevenPointFivePercentile(), "%-9.2f") + " " +
			preprocess(this.getMinimum(), "%-9.0f") + " " +
			preprocess(this.getMaximum(), "%-9.0f");


		} // End of method.

	////////////////////////////////////////////////////////////////////////////

	private String preprocess(final double value, final String formatting) {

		if (Double.isNaN(value)) {
			return "-";
			}
		else {
			return String.format(formatting, value);
			}

		} // End of method.

	//##########################################################################
	
	// INNER CLASSES
	
	//##########################################################################
	
    } // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////





