//==============================================================================
//
//   SimpleFastqStats.java
//   Created: Nov 17, 2011 at 9:09:44 AM
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
import nestor.util.QVEncoding;
import java.text.NumberFormat;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 * 
 * @author  Kevin Ashelford.
 */
public class SimpleFastqStats implements Constants {

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
			

			// fastq infile
			//------------------------------------------------------------------
			final ArrayList<String> fastqFileSynonyms = new ArrayList<String>();
			fastqFileSynonyms.add("f");
			fastqFileSynonyms.add("fastq");

			optionParser.acceptsAll(
				fastqFileSynonyms,
				"Fastq infile (required)."
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
			
			
			// Bad quals cutoff - default 10
			//------------------------------------------------------------------
			final ArrayList<String> cutoffSynonyms = new ArrayList<String>();
			cutoffSynonyms.add("c");
			cutoffSynonyms.add("cutoff");

			optionParser.acceptsAll(
				cutoffSynonyms,
				"Bad QV cut-off value (optional; default 10)."
				).withRequiredArg().ofType(Integer.class);
			//------------------------------------------------------------------
			
			
			// Outfile tag
			//------------------------------------------------------------------
			final ArrayList<String> tagSynonyms = new ArrayList<String>();
			tagSynonyms.add("t");
			tagSynonyms.add("tag");

			optionParser.acceptsAll(
				tagSynonyms,
				"Outfile prefix (required)."
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------

			
			// Encoding
			//------------------------------------------------------------------
			final ArrayList<String> encodingSynonyms = new ArrayList<String>();
			encodingSynonyms.add("e");
			encodingSynonyms.add("encoding");

			optionParser.acceptsAll(
				encodingSynonyms,
				"Encoding (default Sanger/Illumina 1.3 and 1.8+ encoding, 33)."
				).withRequiredArg().ofType(Integer.class);
			//------------------------------------------------------------------
			
			
			

			// Optional prefix for parameter names
			//------------------------------------------------------------------
			final ArrayList<String> prefixSynonymns = new ArrayList<String>();
			prefixSynonymns.add("p");
			prefixSynonymns.add("prefix");
			
			optionParser.acceptsAll(
				prefixSynonymns,
				"Optional parameter prefix (e.g., F3)"
				).withRequiredArg().ofType(String.class); 
			//------------------------------------------------------------------
			
			
			// Obtain and process arguments
			OptionSet options = optionParser.parse(args);

			// Action infile arguments.
			if (options.has("help")) {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}

			
			
			
			// fastq infile must always be provided.
			File fastqFile = null;
			if (options.has("fastq")) {
				fastqFile = new File((String)options.valueOf("fastq"));
				JoptsimpleUtil.checkFileExists(fastqFile);
				}
			else {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}

			
			// Results outfile.
			File resultsFile = null;
			if (options.has("results")) {
				resultsFile = new File((String)options.valueOf("results"));
				}
			
			// Optional prefix string.
			String prefix = "";
			if (options.has("prefix")) {
				prefix = (String)options.valueOf("prefix") + ".";
				}
			
			
			// Mean quals outfile must always be provided.
			String tag = null;
			if (options.has("tag")) {
				tag = (String)options.valueOf("tag");
				}
			else {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}
			
			
			
			// Cutoff.
			Integer cutoff = 10;
			if (options.has("cutoff")) {
				cutoff = (Integer)options.valueOf("cutoff");
				}
			
			
			// Encoding.
			QVEncoding encoding = QVEncoding.Illumina33;
			if (options.has("encoding")) {
				int correction = (Integer)options.valueOf("encoding");
				
				if (correction == 33) {
					encoding = QVEncoding.Illumina33;
					}
				else if (correction == 64) {
					encoding = QVEncoding.Illumina64;
					}
				
				}


			// Run program.
			new SimpleFastqStats(
				fastqFile,
				resultsFile,
				tag,
				cutoff,
				encoding, 
				prefix
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
	 * Creates a new instance of <code>SimpleFastqStats</code>.
	 */
	public SimpleFastqStats(
		final File fastqFile,
		final File resultsFile,
		final String tag,
		final Integer cutoff, 
		final QVEncoding encoding,
		final String prefix
		) throws IOException, Exception {
		
		// Goals:
		// - Mean read length.
		// - Raw data yield.
		// - mean quals as histogram and box and whisker plot.
		// - Bad quals as a histogram and box and whisker plot.
		
		// Initialise formatter.
		_formatter = NumberFormat.getInstance();
		_formatter.setMinimumIntegerDigits(1);
		_formatter.setMinimumFractionDigits(1);
		_formatter.setMaximumFractionDigits(1);
	
		
		long sum = 0;
		long n = 0;
		
		final TreeMap<Integer, MutableInteger> meanQuals = new TreeMap<Integer, MutableInteger>();
		final TreeMap<Integer, MutableInteger> badQuals = new TreeMap<Integer, MutableInteger>();

		
		
		BufferedReader in = new BufferedReader(new FileReader(fastqFile));
		
		String headerLine = null;
		while ((headerLine = in.readLine()) != null) {
			
			// Ignore comments.
			if (headerLine.matches("^#.*$")) continue;
			
			// Sanity check 
			// TODO - fragile: assumes very particular fastq layout - needs recoding.
			if (!headerLine.matches("^@.*$")) 
				throw new Exception("Expecting header line - found '" + headerLine + "'"); 
			
			
			final String seqLine	= in.readLine();
			final String junk		= in.readLine();
			final String qualLine	= in.readLine();
			
			sum += seqLine.length();
			n++;
			
			process(
				qualLine, 
				meanQuals, 
				badQuals, 
				cutoff, 
				encoding
				);
			
			} // End of while loop.
		
		in.close();
		
		
		final double meanReadLength = (double)sum/(double)n;
		
		// Record mean read length and raw data yield.
		
		
		PrintStream out = null;
		// No outfile specified, so write to STDOUT.
		if (resultsFile == null) {out = System.out;}
		// Outfile specified.
		else {out = new PrintStream(resultsFile);}
		
		//PrintWriter out = new PrintWriter(new FileWriter(resultsFile, true));
		out.println(prefix + READ_COUNT + "=" + n);
		out.println(prefix + MEAN_READ_LENGTH + "=" + meanReadLength);
		out.println(prefix + RAW_DATA_YIELD + "=" + sum);
		
		out.println(prefix + PERCENT_READS_WITH_MEAN_QV15_PLUS + "=" + _formatter.format(getPercentWithKey(meanQuals, 15, n)));
		out.println(prefix + PERCENT_READS_WITH_MEAN_QV20_PLUS + "=" + _formatter.format(getPercentWithKey(meanQuals, 20, n)));
		out.println(prefix + PERCENT_READS_WITH_MEAN_QV25_PLUS + "=" + _formatter.format(getPercentWithKey(meanQuals, 25, n)));
		out.println(prefix + PERCENT_READS_WITH_MEAN_QV30_PLUS + "=" + _formatter.format(getPercentWithKey(meanQuals, 30, n)));
		out.println(prefix + PERCENT_READS_WITH_10_BAD_QV_PLUS + "=" + _formatter.format(getPercentWithKey(badQuals, 10, n)));
		
	

		
		out.close();
		
		
		
		final File meanQualsFile = new File(tag + ".mean.hist");
		final File badQualsFile = new File(tag + ".bad.hist");
		
		final File cumMeanQualsFile = new File(tag + ".cumulative.mean.hist");
		final File cumBadQualsFile = new File(tag + ".cumulative.bad.hist");
		
		
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
		
		} // End of constructor.
	
	//##########################################################################
	
	// METHODS
	
	private void runGnuplot(
		final File histFile
		) throws IOException {
		
		Process p = 
			Runtime.getRuntime().exec("gnuplot " + histFile.getAbsolutePath() + ".gb");
    
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
	
	private void process(
		final String qualLine, 
		final TreeMap<Integer, MutableInteger> meanQuals,
		final TreeMap<Integer, MutableInteger> badQuals,
		final Integer cutoff,
		final QVEncoding encoding
		) {
		
		int sum = 0;
		int noOfBadQuals = 0;
		
		// Process qual line.
		final char[] asciiArray = qualLine.toCharArray();
		for (char ascii : asciiArray) {
			
			int qv = convertToNumber(ascii, encoding);
			
			sum += qv;
			if (qv <= cutoff) noOfBadQuals++;
			
			}
		
		
		// Calculate mean qual for read.
		int mean = (int)(Math.ceil((double)sum/(double)qualLine.length()));
		
		// Increment mean qual.
		if (meanQuals.containsKey(mean)) {
			meanQuals.get(mean).increment();
			}
		else {
			meanQuals.put(mean, new MutableInteger(1));
			}
		
		
		// Increment bad qual.
		if (badQuals.containsKey(noOfBadQuals)) {
			badQuals.get(noOfBadQuals).increment();
			} 
		else {
			badQuals.put(noOfBadQuals, new MutableInteger(1));
			}
		
		
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	private int convertToNumber(final char ascii, final QVEncoding encoding) {
		
		if (encoding.equals(QVEncoding.Illumina33)) {
			return (int)ascii - 33;	
			}
		else if (encoding.equals(QVEncoding.Illumina64)) {
			return (int)ascii - 64;
			}
		else {
			throw new UnsupportedOperationException(
				"Only Illumina33 & 64 currently supported"
				);
			}
	
		} // End of method.
	
	//##########################################################################
	
	// INNER CLASSES
	
	//##########################################################################

	} // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
