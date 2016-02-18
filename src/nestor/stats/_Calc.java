//==============================================================================
//
//   DescriptiveStats.java
//   Created: Dec 17, 2010 at 10:24:13 AM
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

import nestor.util.MutableLong;
import java.util.*;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 * Calculates descriptive statistics. <b>Note: only to be used with pre-sorted
 * data, in ascending order!</b> (To minimise overhead, no checks are made on
 * ordering of data.)
 * @author  Kevin Ashelford.
 */
public class _Calc {

	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS
	
	//##########################################################################
	
	// CONSTRUCTOR
	
    /**
	 * Creates a new instance.  <b>IMPORTANT: To reduce memory requirements,
	 class assumes data already sorted.  Do not use on unsorted data</b>.
	 */
    private _Calc() {
		
		} // End of constructor.

	//##########################################################################
		
	// METHODS

	public static long getMinimum(final List<Long> sorted) {
		return sorted.get(0);
		}

	public static long getMinimum(final long[] sorted) {
		return sorted[0];
		} 
	
	public static long getMinimum(final int[] sorted) {
		return sorted[0];
		} 

	public static long getMaximum(final List<Long> sorted) {
		return sorted.get(sorted.size() - 1);
		}

	public static long getMaximum(final long[] sorted) {
		return sorted[sorted.length - 1];
		}
	
	public static long getMaximum(final int[] sorted) {
		return sorted[sorted.length - 1];
		}

	public static long getRange(final List<Long> sorted) {
		return sorted.get(sorted.size() - 1) - sorted.get(0);
		}

	public static long getRange(final long[] sorted) {
		return sorted[sorted.length - 1] - sorted[0];
		}
	
	public static long getRange(final int[] sorted) {
		return sorted[sorted.length - 1] - sorted[0];
		}

	////////////////////////////////////////////////////////////////////////////

	public static double getSampleVariance(
		final long[] sorted,
		final double mean
		) {

		int n = sorted.length;

		// Deviates of mapped only.
		final double[] deviates = new double[n];
		for (int i = 0; i < n; i++) {
			deviates[i] = sorted[i] - (double)mean;
			}

		// Squared deviates of mapped only.
		final double[] squaredDeviates = new double[n];
		for (int i = 0; i < n; i++) {
			squaredDeviates[i] = deviates[i] * deviates[i];
			}

		// Sum of squares of mapped only.
		// NOTE: number will be LARGE therefore must be double!
		double sumOfSquares = 0;
		for (double squaredDeviate : squaredDeviates) {
			sumOfSquares += squaredDeviate;
			}

		// Variance .
		return (double)sumOfSquares / (double)(n - 1);

		} // End of method.

	////////////////////////////////////////////////////////////////////////////
	
	public static double getSampleVariance(
		final int[] sorted,
		final double mean
		) {

		int n = sorted.length;

		// Deviates of mapped only.
		final double[] deviates = new double[n];
		for (int i = 0; i < n; i++) {
			deviates[i] = sorted[i] - (double)mean;
			}

		// Squared deviates of mapped only.
		final double[] squaredDeviates = new double[n];
		for (int i = 0; i < n; i++) {
			squaredDeviates[i] = deviates[i] * deviates[i];
			}

		// Sum of squares of mapped only.
		// NOTE: number will be LARGE therefore must be double!
		double sumOfSquares = 0;
		for (double squaredDeviate : squaredDeviates) {
			sumOfSquares += squaredDeviate;
			}

		// Variance .
		return (double)sumOfSquares / (double)(n - 1);

		} // End of method.

	
	////////////////////////////////////////////////////////////////////////////

	public static double getSampleVariance(
		final List<Long> sorted,
		final double mean
		) {

		int n = sorted.size();

		// Deviates of mapped only.
		final double[] deviates = new double[n];
		for (int i = 0; i < n; i++) {
			deviates[i] = sorted.get(i) - (double)mean;
			}

		// Squared deviates of mapped only.
		final double[] squaredDeviates = new double[n];
		for (int i = 0; i < n; i++) {
			squaredDeviates[i] = deviates[i] * deviates[i];
			}

		// Sum of squares of mapped only.
		// NOTE: number will be LARGE therefore must be double!
		double sumOfSquares = 0;
		for (double squaredDeviate : squaredDeviates) {
			sumOfSquares += squaredDeviate;
			}

		// Variance .
		return (double)sumOfSquares / (double)(n - 1);

		} // End of method.

	////////////////////////////////////////////////////////////////////////////

	public static long getSum(final List<Long> sorted) {
		long sum = 0;
		for (long value : sorted) {
			sum += value;
			}
		return sum;
		}

	public static long getSum(final long[] sorted) {
		long sum = 0;
		for (long value : sorted) {
			sum += value;
			}
		return sum;
		}

	public static long getSum(final int[] sorted) {
		long sum = 0;
		for (long value : sorted) {
			sum += value;
			}
		return sum;
		}
	
	////////////////////////////////////////////////////////////////////////////

	public static double getArithmeticMean(final List<Long> sorted) {
		return (double)getSum(sorted) / (double)sorted.size();
		} // End of method.

	public static double getArithmeticMean(final long[] sorted) {
		return (double)getSum(sorted) / (double)sorted.length;
		} // End of method.
	
	public static double getArithmeticMean(final int[] sorted) {
		return (double)getSum(sorted) / (double)sorted.length;
		} // End of method.

	////////////////////////////////////////////////////////////////////////////

	public static double getMedian(final List<Long> sorted) {
		return getQuantile(sorted, 0.5d);
		}

	public static double getMedian(final long[] sorted) {
		return getQuantile(sorted, 0.5d);
		}
	
	public static double getMedian(final int[] sorted) {
		return getQuantile(sorted, 0.5d);
		}

	////////////////////////////////////////////////////////////////////////////

	public static double getLowerQuartile(final List<Long> sorted) {
		return getQuantile(sorted, 0.25d);
		}

	public static double getLowerQuartile(final long[] sorted) {
		return getQuantile(sorted, 0.25d);
		}
	
	public static double getLowerQuartile(final int[] sorted) {
		return getQuantile(sorted, 0.25d);
		}

	////////////////////////////////////////////////////////////////////////////

	public static double getUpperQuartile(final List<Long> sorted) {
		return getQuantile(sorted, 0.75d);
		}

	public static double getUpperQuartile(final long[] sorted) {
		return getQuantile(sorted, 0.75d);
		} 
	
	public static double getUpperQuartile(final int[] sorted) {
		return getQuantile(sorted, 0.75d);
		}

	////////////////////////////////////////////////////////////////////////////

	public static double getQuantile(
		final long[] sorted,
		final double quantile
		) {

		if (sorted == null)
			throw new RuntimeException("Data array is null!");

		if (sorted.length == 0) {
			throw new RuntimeException("Data array is empty!");
			}

	    // First check percentage ok.
		if (quantile < 0 || quantile > 1) {
			throw new IllegalArgumentException(
				quantile + " is not a valid quantile number"
				);
			}

		final int n = sorted.length;

		// Multiply quantile by number of observations plus 1.
		final double number = quantile * (n + 1);

		// Next step depends on whether number is a whole number or not.
		final double fraction = number%1f;
		if (fraction == 0) {
			// number is a whole number. Therefore our required value already
			// exists within the observation collection.
			return sorted[(int)number - 1];
			}
		else if (number < 1) { // Not 0 as previously
			// Number represents smallest number in collection.
			return sorted[0];
			}
		else if (number >= n) {
			// Number represents largest observation in collection.
			return sorted[(int)number - 1];
			}
		else {
			// number is a fraction number. Therefore our required value is the
			// weighted average of two values within the observation collection.
			final double firstObservation = sorted[(int)Math.floor(number) - 1];
			final double secondObservation = sorted[(int)Math.floor(number)];

			return (1-fraction) * firstObservation + (fraction) * secondObservation;
			}

        } // End of method.

	////////////////////////////////////////////////////////////////////////////

	public static double getQuantile(
		final int[] sorted,
		final double quantile
		) {

		if (sorted == null)
			throw new RuntimeException("Data array is null!");

		if (sorted.length == 0) {
			throw new RuntimeException("Data array is empty!");
			}

	    // First check percentage ok.
		if (quantile < 0 || quantile > 1) {
			throw new IllegalArgumentException(
				quantile + " is not a valid quantile number"
				);
			}

		final int n = sorted.length;

		// Multiply quantile by number of observations plus 1.
		final double number = quantile * (n + 1);

		// Next step depends on whether number is a whole number or not.
		final double fraction = number%1f;
		if (fraction == 0) {
			// number is a whole number. Therefore our required value already
			// exists within the observation collection.
			return sorted[(int)number - 1];
			}
		else if (number < 1) { // Not 0 as previously
			// Number represents smallest number in collection.
			return sorted[0];
			}
		else if (number >= n) {
			// Number represents largest observation in collection.
			return sorted[(int)number - 1];
			}
		else {
			// number is a fraction number. Therefore our required value is the
			// weighted average of two values within the observation collection.
			final double firstObservation = sorted[(int)Math.floor(number) - 1];
			final double secondObservation = sorted[(int)Math.floor(number)];

			return (1-fraction) * firstObservation + (fraction) * secondObservation;
			}

        } // End of method.

	////////////////////////////////////////////////////////////////////////////
	
	public static double getQuantile(
		final List<Long> sorted,  // should already be sorted in ascending order.
		final double quantile
		) {

		// TODO - for simplicity and performance issues, code assumes data already
		// sorted in ascending order - but would be good to introduce some
		// sanity check here to avoid later errors when code is expanded.

	    // First check percentage ok.
		if (quantile < 0 || quantile > 1) {
			throw new IllegalArgumentException(
				quantile + " is not a valid quantile number"
				);
			}

		final int n = sorted.size();

		// Multiply quantile by number of observations plus 1.
		final double number = quantile * (n + 1);

		// Next step depends on whether number is a whole number or not.
		final double fraction = number%1f;
		if (fraction == 0) {
			// number is a whole number. Therefore our required value already
			// exists within the observation collection.
			return sorted.get((int)number - 1);
			}
		else if (number >= n) {
			// Number represents largest observation in collection.
			return sorted.get((int)number - 1);
			}
		else if (number < 1) { // Not 0 as previously
			// Number represents smallest number in collection.
			return sorted.get(0);
			}
		else {
			// number is a fraction number. Therefore our required value is the
			// weighted average of two values within the observation collection.
			final double firstObservation = 
				sorted.get((int)Math.floor(number) - 1);
			final double secondObservation = 
				sorted.get((int)Math.floor(number));

			return (1-fraction) * firstObservation + (fraction) * secondObservation;
			}

        } // End of method.

	////////////////////////////////////////////////////////////////////////////

	public static double getMode(
		final long[] sorted
		) {

		// TODO - needs tidying up - this feature no longer used.
		boolean ignoreZeros = false;

		// Going to store coverage as keys in map, with values the number of
		// occurances of that particular coverage.
		HashMap<Long, MutableLong> map = new HashMap<Long, MutableLong>();

		// Fill map.
		for (long x : sorted) {

			// Ignore zeros if mapped only.
			if (x == 0 && ignoreZeros) {
				continue;
				}

			MutableLong value = map.get(x);
			if (value == null) {
				value = new MutableLong();
				map.put(x, value);
				}
			value.increment();

			}

		// Now find highest scoring coverage and hence mode.
		Long mode = null;
		Iterator<Long> i = map.keySet().iterator();
		while (i.hasNext()) {
			Long coverage = i.next();
			if (mode == null || map.get(coverage).value() > map.get(mode).value()) {
				mode = coverage;
				}
			}

		return mode;

        } // End of method.
	
	////////////////////////////////////////////////////////////////////////////

	public static double getMode(
		final int[] sorted
		) {

		// TODO - needs tidying up - this feature no longer used.
		boolean ignoreZeros = false;

		// Going to store coverage as keys in map, with values the number of
		// occurances of that particular coverage.
		HashMap<Long, MutableLong> map = new HashMap<Long, MutableLong>();

		// Fill map.
		for (long x : sorted) {

			// Ignore zeros if mapped only.
			if (x == 0 && ignoreZeros) {
				continue;
				}

			MutableLong value = map.get(x);
			if (value == null) {
				value = new MutableLong();
				map.put(x, value);
				}
			value.increment();

			}

		// Now find highest scoring coverage and hence mode.
		Long mode = null;
		Iterator<Long> i = map.keySet().iterator();
		while (i.hasNext()) {
			Long coverage = i.next();
			if (mode == null || map.get(coverage).value() > map.get(mode).value()) {
				mode = coverage;
				}
			}

		return mode;

        } // End of method.

	
	////////////////////////////////////////////////////////////////////////////

	public static double getMode(
		final List<Long> sorted
		) {

		// Going to store coverage as keys in map, with values the number of
		// occurances of that particular coverage.
		HashMap<Long, MutableLong> map =
			new HashMap<Long, MutableLong>();

		// Fill map.
		for (long x : sorted) {

			MutableLong value = map.get(x);
			if (value == null) {
				value = new MutableLong();
				map.put(x, value);
				}
			value.increment();

			}

		// Now find highest scoring coverage and hence mode.
		Long mode = null;
		Iterator<Long> i = map.keySet().iterator();
		while (i.hasNext()) {
			Long coverage = i.next();
			if (mode == null || map.get(coverage).value() > map.get(mode).value()) {
				mode = coverage;
				}
			}

		return mode;

        } // End of method.
	
	////////////////////////////////////////////////////////////////////////////
/*	
	public static long getN50(final long[] sorted) {
		
		long sum = getSum(sorted);

        double halfSum = (double)sum / 2d;

        for (int i = sorted.length-1; i >= 0; i--) {

                sum -= sorted[i];
                if (halfSum > sum) return sorted[i];
                }

        throw new RuntimeException("Failed to find N50");
		
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	public static long getN50(final int[] sorted) {
		
		long sum = getSum(sorted);

        double halfSum = (double)sum / 2d;

        for (int i = sorted.length-1; i >= 0; i--) {

                sum -= sorted[i];
                if (halfSum > sum) return sorted[i];
                }

        throw new RuntimeException("Failed to find N50");
		
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	public static long getN50(final List<Long> sorted) {
	
		long sum = getSum(sorted);

        double halfSum = (double)sum / 2d;

        for (int i = sorted.size()-1; i >= 0; i--) {

                sum -= sorted.get(i);
                if (halfSum > sum) return sorted.get(i);
                }

        throw new RuntimeException("Failed to find N50");
		
		} // End of method.
*/
	//##########################################################################
	
	// INNER CLASSES
	
	//##########################################################################
	
    } // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////





