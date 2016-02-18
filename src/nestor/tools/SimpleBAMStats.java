//==============================================================================
//
//   SimpleBAMStats.java
//   Created: Nov 18, 2011 at 4:16:53 PM
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

import java.io.*;
import java.util.*;
import joptsimple.*;
import nestor.util.ProcessTimer;
import nestor.util.JoptsimpleUtil;
import nestor.util.MutableInteger;
import net.sf.samtools.*;
import nestor.stats.*;
import nestor.graphics.newplot.*;
import nestor.util.QVEncoding;
import java.text.NumberFormat;
import java.util.regex.*;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
/**
 * 
 * @author  Kevin Ashelford.
 */
public class SimpleBAMStats implements Constants {

	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS
	
	private final NumberFormat _formatter;
	
	//##########################################################################
	
	// ENTRY POINT
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		
		ProcessTimer timer = new ProcessTimer();
		timer.start();
		
		try {

			final OptionParser optionParser = new OptionParser();


			// Help
			//------------------------------------------------------------------
			final ArrayList<String> helpSynonyms = new ArrayList<String>();
			helpSynonyms.add("h");
			helpSynonyms.add("help");
			optionParser.acceptsAll(helpSynonyms, "Generate help.");
			//------------------------------------------------------------------
			

			// BAM infile
			//------------------------------------------------------------------
			final ArrayList<String> bamFileSynonyms = new ArrayList<String>();
			bamFileSynonyms.add("b");
			bamFileSynonyms.add("bam");

			optionParser.acceptsAll(
				bamFileSynonyms,
				"SAM or BAM infile (required)."
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------


			// BED infile
			//------------------------------------------------------------------
			final ArrayList<String> bedFileSynonyms = new ArrayList<String>();
			bedFileSynonyms.add("f");
			bedFileSynonyms.add("features");
			optionParser.acceptsAll(
				bedFileSynonyms,
				"Features file in BED format (optional)."
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------
			
			
			// Results outfile
			//------------------------------------------------------------------
			final ArrayList<String> resultsFileSynonyms = new ArrayList<String>();
			resultsFileSynonyms.add("r");
			resultsFileSynonyms.add("results");
			optionParser.acceptsAll(
				resultsFileSynonyms,
				"Results file as a properties file (optional: will write to "
				+ "STDOUT if not provided)."
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------
			
							
			
			// Quality scores outfile prefix (optional)
			//------------------------------------------------------------------
			final ArrayList<String> prefixSynonyms = new ArrayList<String>();
			prefixSynonyms.add("p");
			prefixSynonyms.add("prefix");

			optionParser.acceptsAll(
				prefixSynonyms,
				"Outfile prefix for MAPQ and QV outfiles (optional)."
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------

			
			
			
			
			// Optional tag
			//------------------------------------------------------------------
			final ArrayList<String> tagSynonyms = new ArrayList<String>();
			tagSynonyms.add("t");
			tagSynonyms.add("tag");

			optionParser.acceptsAll(
				tagSynonyms,
				"Tag to append to outfile parameter keys (optional)."
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------
		
	
			// Bad quals cutoff - default 10
			//------------------------------------------------------------------
			final ArrayList<String> cutoffSynonyms = new ArrayList<String>();
			cutoffSynonyms.add("c");
			cutoffSynonyms.add("cutoff");

			optionParser.acceptsAll(
				cutoffSynonyms,
				"Bad QV cut-off value if provided outfile prefix for QV metrics (optional; default 10)."
				).withRequiredArg().ofType(Integer.class);
			//------------------------------------------------------------------
			
			
			
			// Obtain and process arguments
			OptionSet options = optionParser.parse(args);

			// Action infile arguments.
			if (options.has("help")) {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}

			
			// bam infile must always be provided.
			File bamFile = null;
			if (options.has("bam")) {
				bamFile = new File((String)options.valueOf("bam"));
				JoptsimpleUtil.checkFileExists(bamFile);
				}
			else {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}

			
			// Results outfile may be provided.
			File resultsFile = null;
			if (options.has("results")) {
				resultsFile = new File((String)options.valueOf("results"));
				}
			
			
			
			// outfile prefix of quality files may be provided.
			String prefix = null;
			if (options.has("prefix")) {
				prefix = (String)options.valueOf("prefix");
				}
			
			
			
			
			// tag MAY be provided.
			String tag = "";
			if (options.has("tag")) {
				tag = "." + (String)options.valueOf("tag");
				}
			

			// Cutoff.
			Integer cutoff = 10;
			if (options.has("cutoff")) {
				cutoff = (Integer)options.valueOf("cutoff");
				}
			
			// bed infile may be provided.
			File bedFile = null;
			if (options.has("features")) {
				bedFile = new File((String)options.valueOf("features"));
				JoptsimpleUtil.checkFileExists(bedFile);
				}
			
			
			
			// Run program.
			new SimpleBAMStats(
				bamFile,
				bedFile,
				resultsFile,
				prefix,
				tag,
				cutoff,
				QVEncoding.Illumina33
				);

			} 

	
			
		// Catch io exceptions.
		catch (IOException ioe) {
			System.err.println(
				"ERROR: " + ioe.getMessage()
				);
			}	
	
		// Catch all other more unexpected problems.
		catch (Exception e) {
			System.err.println(
				"ERROR: " + e.getMessage()
				);
			e.printStackTrace();
			}

			
			
			
		finally {
			
			// Report time elapsed.
			timer.stop();
			System.err.println(timer.getDuration());
			
			}
		
		
		} // End of main method.

	//##########################################################################
	
	// CONSTRUCTOR
	/**
	 * Creates a new instance of <code>SimpleBAMStats</code>.
	 */
	public SimpleBAMStats(
		final File bamFile,
		final File bedFile,
		final File resultsFile,
		final String prefix,
		final String tag,
		final Integer cutoff, 
		final QVEncoding encoding
		) throws IOException {
		
		
		
		// PROCESS BED FILE if exsists.
		//----------------------------------------------------------------------
		final BedData bedData = new BedData();
		
		if (bedFile != null) {
			Matcher bedRegex = Pattern.compile("^(\\S+)\\t(\\d+)\\t(\\d+).*$").matcher("");
			Matcher ignoreRegex = Pattern.compile("^track.*$").matcher("");
		
			BufferedReader bedIn = new BufferedReader(new FileReader(bedFile));
			String line = null;
			while ((line = bedIn.readLine()) != null) {
			
				if (ignoreRegex.reset(line).matches()) {
					continue;
					}
			
				else if (bedRegex.reset(line).matches()) {
					final String chr = bedRegex.group(1);
					final int start = Integer.parseInt(bedRegex.group(2)) + 1;
					final int end = Integer.parseInt(bedRegex.group(3));
				
					for (int i = start; i <= end; i++) {
						//bedData.increment(chr, i);
						bedData.create(chr, i, 0);
						}
				
					} 
				else {
					throw new IOException(
						"Unexpected line encountered '" + line + "'"
						);
					}
			
			
				}
			bedIn.close();
			}
		//----------------------------------------------------------------------
			
		
		
		
		// Initialise formatter.
		_formatter = NumberFormat.getInstance();
		_formatter.setMinimumIntegerDigits(1);
		_formatter.setMinimumFractionDigits(1);
		_formatter.setMaximumFractionDigits(1);
				
		// Create SAM/BAM file input stream
		final SAMFileReader bamIn = new SAMFileReader(bamFile);	
		final SAMStats samStats = new SAMStats(bamIn, bedData, cutoff, encoding);
		bamIn.close();
		
		// Record read counts
		
		PrintStream out = null;
		// No outfile specified, so write to STDOUT.
		if (resultsFile == null) {out = System.out;}
		// Outfile specified.
		else {out = new PrintStream(resultsFile);}
		
		
		//final PrintWriter out = new PrintWriter(new FileWriter(resultsFile, true));
		out.println(TOTAL_READS + tag + "=" + samStats.getReadCount());
		out.println(MAPPED_READS + tag + "=" + samStats.getMappedReadCount());
		out.println(UNIQUE_READS + tag + "=" + samStats.getUniqueReadCount());
		out.println(SECONDARY_READS + tag + "=" + samStats.getSecondaryAlignmentCount());
		
		
		long sumLength = 0;
		for (Integer length : samStats.getReadLengths()) {
			out.println(TOTAL_READS + "." + length + tag + "=" + samStats.getReadCount(length));
			sumLength += length * samStats.getReadCount(length);
			}
		double meanLength = (double)sumLength / (double)samStats.getReadCount(); 
		
		out.println(MEAN_MAPPED_READ_LENGTH + tag + "=" + meanLength);	
		out.println(MAPPED_DATA_YIELD + tag + "=" + sumLength);
		out.println(READS_WITH_MAPQ_OF_0_OR_HIGHER + tag + "=" + samStats.getReadCountWithMAPQOfAtLeast(0));
		out.println(READS_WITH_MAPQ_OF_1_OR_HIGHER + tag + "=" + samStats.getReadCountWithMAPQOfAtLeast(1));
		out.println(READS_WITH_MAPQ_OF_10_OR_HIGHER + tag + "=" + samStats.getReadCountWithMAPQOfAtLeast(10));
		out.println(READS_WITH_MAPQ_OF_20_OR_HIGHER + tag + "=" + samStats.getReadCountWithMAPQOfAtLeast(20));
		out.println(READS_WITH_MAPQ_OF_30_OR_HIGHER + tag + "=" + samStats.getReadCountWithMAPQOfAtLeast(30));
		
		
		// If requested QV info
		if (prefix != null) {
			
			// For storing QV results
			final TreeMap<Integer, MutableInteger> meanQuals = samStats.getMeanQuals();
			final TreeMap<Integer, MutableInteger> badQuals = samStats.getBadQuals();
			
			// Write summary metrics to results properties file.
			long n = samStats.getMappedReadCount();
			out.println(PERCENT_MAPPED_READS_WITH_MEAN_QV15_PLUS + "=" + _formatter.format(getPercentWithKey(meanQuals, 15, n)));
			out.println(PERCENT_MAPPED_READS_WITH_MEAN_QV20_PLUS + "=" + _formatter.format(getPercentWithKey(meanQuals, 20, n)));
			out.println(PERCENT_MAPPED_READS_WITH_MEAN_QV25_PLUS + "=" + _formatter.format(getPercentWithKey(meanQuals, 25, n)));
			out.println(PERCENT_MAPPED_READS_WITH_MEAN_QV30_PLUS + "=" + _formatter.format(getPercentWithKey(meanQuals, 30, n)));
			out.println(PERCENT_MAPPED_READS_WITH_10_BAD_QV_PLUS + "=" + _formatter.format(getPercentWithKey(badQuals, 10, n)));
			
			// Create necessary histograms.
			IHistogram histogram = samStats.getMapqHistogram();
		
			final File mapqFile			= new File(prefix + ".mapq.hist");
			final File meanQualsFile	= new File(prefix + ".mean.hist");
			final File badQualsFile		= new File(prefix + ".bad.hist");
		
			final File cumMapqFile		= new File(prefix + ".cumulative.mapq.hist");
			final File cumMeanQualsFile = new File(prefix + ".cumulative.mean.hist");
			final File cumBadQualsFile	= new File(prefix + ".cumulative.bad.hist");
			
			// MAPQ plot
			createHistogram(histogram, mapqFile);
			createGnuplotScript(
				mapqFile, 
				"Variation in MAPQ score",
				"MAPQ score",
				"Percentage of reads"
				);
			runGnuplot(mapqFile);
			
			// Cumulative MAPQ plot
			createCumulativeHistogram(histogram, cumMapqFile);
			createGnuplotScript(
				cumMapqFile, 
				"Cumulative variation in MAPQ score",
				"MAPQ score",
				"Percentage of reads (cumulative)"
				);
			runGnuplot(cumMapqFile);
		
			// Mean QVs per read
			createHistogram(meanQuals, meanQualsFile, n);
			createGnuplotScript(
				meanQualsFile,
				"Variation in mean QV per read",
				"Mean QV per read",
				"Percentage of reads"
				);
			runGnuplot(meanQualsFile);

			// Cumulative mean QVs per read
			createCumulativeHistogram(meanQuals, cumMeanQualsFile, n);
			createGnuplotScript(
				cumMeanQualsFile,
				"Cumulative variation in mean QV per read",
				"Mean QV per read",
				"Percentage of reads (cumulative)"
				);
			runGnuplot(cumMeanQualsFile);


			// No. of bad poor quality bases per read
			createHistogram(badQuals, badQualsFile, n);
			createGnuplotScript(
				badQualsFile,
				"Variation in number of poor quality bases per read",
				"Number of poor quality bases in read",
				"Percentage of reads"
				);
			runGnuplot(badQualsFile);

			// Culumulative number of poor quality bases per read
			createCumulativeHistogram(badQuals, cumBadQualsFile, n);
			createGnuplotScript(
				cumBadQualsFile,
				"Cumulative variation in number of poor quality bases per read",
				"Number of poor quality bases in read",
				"Percentage of reads (cumulative)"
				);
			runGnuplot(cumBadQualsFile);
			
			
			
			}
		
		
		out.close();
		
		// Get Histograms for MAPQ and QV if requested
		if (prefix != null) {
		
			

			}
		
		} // End of constructor.
	
	//##########################################################################
	
	// METHODS
	
	private double getPercentWithKey(
		final TreeMap<Integer, MutableInteger> data,
		final int cutoff,
		final long n
		) {
		
		
		double total = 0;
		
		Iterator<Integer> it = data.keySet().iterator();
		while (it.hasNext()) {
			
			Integer key = it.next();
			
			double percentage = 
				(double)(data.get(key).value()) /(double)n * 100d;
		
			total += percentage;
			
			if (key >= cutoff) {
				return 100d - total;
				}
			
			}
		
		return 0d;
		
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	private void createHistogram(
		final IHistogram data,
		final File outfile
		) throws IOException {
		
		PrintWriter out = new PrintWriter(new FileWriter(outfile));
		
		ICoordinateCollection cc = data.getCoordinateCollection();
		
		for (int i = 0; i < cc.getSize(); i++) {
			Coordinate c = cc.getCoordinate(i);
			out.println(c.getX() + "\t" + c.getY());
			}

		out.close();
		
		} // End of method.
	
	
	////////////////////////////////////////////////////////////////////////////
	
	private void createHistogram(
		final TreeMap<Integer, MutableInteger> data,
		final File file,
		final long n
		) throws IOException {
		
		PrintWriter out = new PrintWriter(new FileWriter(file));
		
		Iterator<Integer> it = data.keySet().iterator();
		while (it.hasNext()) {
			
			Integer key = it.next();
			
			double percentage = 
				(double)(data.get(key).value()) /(double)n * 100d;
			
			out.println(key + "\t" + percentage);
			
			}
		
		out.close();
		
		} // End of method.	
	
	////////////////////////////////////////////////////////////////////////////
	
	private void createCumulativeHistogram(
		final IHistogram data,
		final File outfile
		) throws IOException {
		
		PrintWriter out = new PrintWriter(new FileWriter(outfile));
		
		double total = 0;
		
		ICoordinateCollection cc = data.getCoordinateCollection();
		
		for (int i = 0; i < cc.getSize(); i++) {
			Coordinate c = cc.getCoordinate(i);
			
			total += c.getY();
			
			out.println(c.getX() + "\t" + total);
			}

		out.close();
		
		} // End of method.
	
	
	////////////////////////////////////////////////////////////////////////////
	
	private void createCumulativeHistogram(
		final TreeMap<Integer, MutableInteger> data,
		final File file,
		final long n
		) throws IOException {
		
		PrintWriter out = new PrintWriter(new FileWriter(file));
		
		double total = 0;
		
		Iterator<Integer> it = data.keySet().iterator();
		while (it.hasNext()) {
			
			Integer key = it.next();
			
			double percentage = 
				(double)(data.get(key).value()) /(double)n * 100d;
		
			total += percentage;
			
			out.println(key + "\t" + total);
			
			}
		
		out.close();
		
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////

	private void createGnuplotScript(
		final File histFile,
		final String title,
		final String xLabel,
		final String yLabel
		) throws IOException {
			
		PrintWriter out = 
			new PrintWriter(new FileWriter(histFile.getAbsolutePath() + ".gb"));
		
		out.println("set terminal png");
		out.println("set output '" + histFile.getAbsolutePath() + ".png'");
		out.println("set style fill solid border -1");
		out.println("set boxwidth 1");
		out.println("set tics out");
		out.println("set xtics nomirror");
		out.println("set ytics nomirror");
		//out.println("set logscale y");
		out.println("unset key");
		out.println("set title '" + title  + "'");
		out.println("set ylabel '" + yLabel + "'");
		out.println("set xlabel '" + xLabel + "'");
		out.println("plot '" + histFile.getAbsolutePath() + "' with boxes");
		
		out.close();

		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	private void runGnuplot(
		final File histFile
		) throws IOException {
		
		Process p = 
			Runtime.getRuntime().exec("gnuplot " + histFile.getAbsolutePath() + ".gb");
    
		} // End of method.
	
	//##########################################################################
	
	// INNER CLASSES
	
	//##########################################################################

	} // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
