//==============================================================================
//
//   ProcessTimer.java
//   Created: Dec 17, 2010 at 9:49:57 AM
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

package nestor.util;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 * Simple class for monitoring the duration of processes.
 * @author  Kevin Ashelford.
 */
public class ProcessTimer {

	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS

	private long startTime = -1;
	private long stopTime = -1;
	
	//##########################################################################
	
	// CONSTRUCTOR
	
    /**
	 * Creates a new instance of <code>ProcessTimer</code>.
	 */
    public ProcessTimer() {
		
		} // End of constructor.

	//##########################################################################
		
	// METHODS

	/**
	 * Start process timer (e.g., call before starting a process).
	 */
	public void start() {
		startTime = System.currentTimeMillis();
		} // End of method.

	////////////////////////////////////////////////////////////////////////////

	/**
	 * Stop process timer (e.g., call once process has ended).
	 */
	public void stop() {

		if (startTime == -1) {
			throw new RuntimeException("Stop called before start!");
			}

		stopTime = System.currentTimeMillis();
		}

	////////////////////////////////////////////////////////////////////////////

	/**
	 * Get summary string detailing duration of process that's ended.
	 * @return String detailing total duration elapsed.
	 */
	public String getDuration() {

		if (startTime == -1 || stopTime == -1) {
			throw new RuntimeException(
				"Method can only be called once both start() and stop() have"
				+ "been called.");
			}

		final double elapsed = (stopTime - startTime) / 1000d;

		int minutes =  (int) Math.floor(elapsed / 60d);
		int seconds = (int) Math.round(elapsed - 60*minutes);

		String text;
		if (minutes == 0) {

			if (seconds == 1) {
				text = "Total time: 1 second";
				}
			else {
				text = "Total time: " + seconds + " seconds";
				}
			
			}
		else if (minutes == 1) {
			text = "Total time: 1 minute " + seconds + " seconds";
			}
		else  {
			text = "Total time: " + minutes + "  minutes " + seconds + " seconds";
			}

		return text;

		}

	//##########################################################################
	
	// INNER CLASSES
	
	//##########################################################################
	
    } // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////





