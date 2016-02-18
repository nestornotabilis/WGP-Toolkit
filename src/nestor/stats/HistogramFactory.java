//==============================================================================
//
//   HistogramFactory.java
//   Created: Dec 20, 2010 at 11:34:11 AM
//   Copyright (C) 2010, University of Liverpool.
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

import nestor.graphics.newplot.*;
import java.util.*;
import nestor.util.MutableInteger;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 * Factory class for producing histogram objects. All histogram objects should
 * be created via this factory.
 * @author  Kevin Ashelford.
 */
public class HistogramFactory {

	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS
	
	//##########################################################################
	
	// CONSTRUCTOR
	
    private HistogramFactory() {
		
		} // End of constructor.

	//##########################################################################
		
	// METHODS

	/**
	 * Creates a histogram object from bin and value data, held in separate
	 * arrays.
	 * @param bins Array of bins.
	 * @param values Array of values corresponding to bins.
	 * @param label Label string associated with histogram.
	 * @return New histogram object.
	 */
	public static IHistogram convertHistogramDataToHistogramObject(
		final ArrayList<Double> bins,
		final ArrayList<Double> values,
		final String label
		) {

		// Store binned data in histogram object.
		ArrayList<Coordinate> c = new ArrayList<Coordinate>();

		for (int i = 0; i < bins.size(); i++) {

			c.add(
				new Coordinate(
					bins.get(i),
					values.get(i)
					)
				);

			}

		ICoordinateCollection cc =
			CoordinateCollectionFactory.createCoordinateCollection(
				c,
				label
				);

		return new _Histogram(cc);

		} // End of method.

	////////////////////////////////////////////////////////////////////////////

	/**
	 * Creates a new histogram object, duplicating contents of original.
	 * @param histogram Histogram object to clone.
	 * @return Histogram clone.
	 */
	public static IHistogram cloneHistogram(IHistogram histogram) {

		ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();

		for (int i = 0; i < histogram.getNumberOfBins(); i++) {

			Coordinate c = histogram.getCoordinateCollection().getCoordinate(i);

			coordinates.add(
				new Coordinate(
					c.getX(),
					c.getY()
					)
				);

			}

		return new _Histogram(
			CoordinateCollectionFactory.createCoordinateCollection(
				coordinates,
				histogram.getCoordinateCollection().getTitle()
				)
			);

		} // End of method.

	////////////////////////////////////////////////////////////////////////////

	/**
	 * Creates a histogram object from raw (i.e., unbinned) data held in array.
	 * Method bins data into bins of size specified.
	 * @param data Raw data to bin.
	 * @param label Label string for histogram.
	 * @param binSize Size of bins into which data will be distributed.
	 * @return Final histogram object.
	 */
	public static IHistogram createHistogramObject(
		final int[] data,
		final String label,
		final int binSize
		) {

		// Bin hashmap - key = bin-id, value = count.
		final HashMap<Integer, MutableInteger> bins =
			new HashMap<Integer, MutableInteger>();

		// Associate data values with appropriate bin-id
		int largestBin = 0;
		for (int x : data) {

			int bin = (int)Math.floor((double)x / (double)binSize);
			if (bin > largestBin) largestBin = bin;

			MutableInteger value = bins.get(bin);
			if (value == null) {
				value = new MutableInteger();
				bins.put(bin, value);
				}
			value.increment();

			}

		// Store binned data in histogram object.
		Coordinate[] c = new Coordinate[largestBin+1];
		for (int i = 0; i <= largestBin; i++) {

			if (bins.get(i) == null) {
				c[i] = new Coordinate(i*binSize, 0);
				}
			else {
				c[i] = new Coordinate(i*binSize, bins.get(i).value());
				}

			}


		ICoordinateCollection cc =
			CoordinateCollectionFactory.createCoordinateCollection(
				c,
				label
				);

		return new _Histogram(cc);




		} // End of method.

	////////////////////////////////////////////////////////////////////////////

	/**
	 * Creates histogram object from supplied coordinate collection.
	 * @param cc Coordinate collection.
	 * @return Histogram object.
	 */
	public static IHistogram  createHistogram(final ICoordinateCollection cc) {
		return new _Histogram(cc);
		} // End of method.
	
	//##########################################################################
	
	// INNER CLASSES
	
	//##########################################################################
	
    } // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////






