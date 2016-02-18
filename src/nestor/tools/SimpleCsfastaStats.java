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
import nestor.util.MutableLong;
import nestor.util.QVEncoding;
import java.text.NumberFormat;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
/**
 * 
 * @author  Kevin Ashelford.
 */
public class SimpleCsfastaStats implements Constants {

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
			

			// csfasta infile
			//------------------------------------------------------------------
			final ArrayList<String> csfastaFileSynonyms = new ArrayList<String>();
			csfastaFileSynonyms.add("f");
			csfastaFileSynonyms.add("csfasta");

			optionParser.acceptsAll(
				csfastaFileSynonyms,
				"one of more csFasta infiles (required. Comma separated list for multiple files)."
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------


			// qual infile
			//------------------------------------------------------------------
			final ArrayList<String> qualFileSynonyms = new ArrayList<String>();
			qualFileSynonyms.add("q");
			qualFileSynonyms.add("qual");

			optionParser.acceptsAll(
				qualFileSynonyms,
				"one or more qual infiles (optional. Comma separated list for multiple files)."
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------
			
			
			// Results outfile
			//------------------------------------------------------------------
			final ArrayList<String> resultsFileSynonyms = new ArrayList<String>();
			resultsFileSynonyms.add("r");
			resultsFileSynonyms.add("results");
			optionParser.acceptsAll(
				resultsFileSynonyms,
				"Results file as a properties file (optional: will write to STDOUT"
				+ "if not provided)."
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------
			
			
			// Bad quals cutoff
			//------------------------------------------------------------------
			final ArrayList<String> cutoffSynonyms = new ArrayList<String>();
			cutoffSynonyms.add("c");
			cutoffSynonyms.add("cutoff");

			optionParser.acceptsAll(
				cutoffSynonyms,
				"Bad QV cut-off value (optional)."
				).withRequiredArg().ofType(Integer.class);
			//------------------------------------------------------------------
			
			
			// Mean quals outfile
			//------------------------------------------------------------------
			final ArrayList<String> meanQualsFileSynonyms = new ArrayList<String>();
			meanQualsFileSynonyms.add("m");
			meanQualsFileSynonyms.add("meanquals");

			optionParser.acceptsAll(
				meanQualsFileSynonyms,
				"Mean QVs histogram outfile (optional)."
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------

			
			// Bad quals outfile
			//------------------------------------------------------------------
			final ArrayList<String> badQualsFileSynonyms = new ArrayList<String>();
			badQualsFileSynonyms.add("b");
			badQualsFileSynonyms.add("badquals");

			optionParser.acceptsAll(
				badQualsFileSynonyms,
				"Bad QVs histogram outfile (optional)."
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------
			
			
			
			
			// Obtain and process arguments
			OptionSet options = optionParser.parse(args);

			// Action infile arguments.
			if (options.has("help")) {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}

			
			
			
			// csfasta infile must always be provided.
			File[] csfastaFiles = null;
			if (options.has("csfasta")) {
				
				String[] filenames = ((String)options.valueOf("csfasta")).split(",");
				
				csfastaFiles = new File[filenames.length];
				
				for (int i = 0; i < filenames.length; i++) {
					csfastaFiles[i] = new File(filenames[i]);
					JoptsimpleUtil.checkFileExists(csfastaFiles[i]);
					}
				
				
				}
			else {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}

			
			
			
			// qual infile may be provided.
			File[] qualFiles = null;
			if (options.has("qual")) {
				
				String[] filenames = ((String)options.valueOf("qual")).split(",");
				
				qualFiles = new File[filenames.length];
				
				for (int i = 0;i < filenames.length; i++) {
					qualFiles[i] = new File(filenames[i]);
					JoptsimpleUtil.checkFileExists(qualFiles[i]);
					}
				
				
				}
			
			
			
			// Results outfile must always be provided.
			File resultsFile = null;
			if (options.has("results")) {
				resultsFile = new File((String)options.valueOf("results"));
				}
			//else {
			//	JoptsimpleUtil.displayHelp(optionParser);
			//	System.exit(0);
			//	}
			
			
			// Mean quals outfile.
			File meanQualsFile = null;
			if (options.has("meanquals")) {
				meanQualsFile = new File((String)options.valueOf("meanquals"));
				//JoptsimpleUtil.checkFileDoesNotExist(meanQualsFile);
				}
			
			
			
			// Bad quals outfile.
			File badQualsFile = null;
			if (options.has("badquals")) {
				badQualsFile = new File((String)options.valueOf("badquals"));
				//JoptsimpleUtil.checkFileDoesNotExist(badQualsFile);
				}
		
			
			
			// Cutoff.
			Integer cutoff = null;
			if (options.has("cutoff")) {
				cutoff = (Integer)options.valueOf("cutoff");
				}
			

			// Run program.
			new SimpleCsfastaStats(
				csfastaFiles,
				qualFiles,
				resultsFile,
				meanQualsFile,
				badQualsFile,
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
	 * Creates a new instance of <code>SimpleFastqStats</code>.
	 */
	public SimpleCsfastaStats(
		final File[] csfastaFiles,
		final File[] qualFiles,
		final File resultsFile,
		final File meanQualsFile,
		final File badQualsFile,
		final Integer cutoff, 
		final QVEncoding encoding
		) throws IOException, Exception {
		
		
		if (qualFiles != null) {
			
			// Fail if following parameters not set:
			if (meanQualsFile == null) throw new Exception("Mean quals outfile not set");
			if (badQualsFile == null) throw new Exception("Bad quals outfile not set"); 
			if (cutoff == null) throw new Exception("Qual cutoff not set");
		
			}
			
		
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
	
		
		final MutableLong sum = new MutableLong(0);
		final MutableLong n = new MutableLong(0);
		final TreeMap<Integer, MutableInteger> meanQuals = new TreeMap<Integer, MutableInteger>();
		final TreeMap<Integer, MutableInteger> badQuals = new TreeMap<Integer, MutableInteger>();
		
		for (File infile : csfastaFiles) {
			processCsfastaFile(infile, sum, n);
			}
		
		//--------------------------------------------------------------------
		final double meanReadLength = (double)sum.value()/(double)n.value();
		
		
		// Either write to outfile or STDOUT 
		PrintStream out = null;
		// No outfile specified, so write to STDOUT.
		if (resultsFile == null) {out = System.out;}
		// Outfile specified.
		else {out = new PrintStream(resultsFile);}
		
		
		// Record mean read length and raw data yield.
		//PrintWriter out = new PrintWriter(new FileWriter(resultsFile, true));
		out.println(READ_COUNT + "=" + n.value());
		out.println(MEAN_READ_LENGTH + "=" + meanReadLength);
		out.println(RAW_DATA_YIELD + "=" + sum.value());
		out.close();
		//--------------------------------------------------------------------
		
		
		
		// Only process in qual data provided.
		if (qualFiles == null) return;
		
		for (File infile : qualFiles) {
			processQualFile(infile, meanQuals, badQuals, cutoff, encoding);
			}
		
		//--------------------------------------------------------------------
		createHistogram(meanQuals, meanQualsFile, n.value());
		createGnuplotScript(
			meanQualsFile,
			"Variation in mean QV per read",
			"Mean QV per read",
			"Percentage of reads"
			);
		runGnuplot(meanQualsFile);
		
		createHistogram(badQuals, badQualsFile, n.value());
		createGnuplotScript(
			badQualsFile,
			"Variation in number of poor quality bases per read",
			"Number of poor quality bases in read",
			"Percentage of reads"
			);
		runGnuplot(badQualsFile);
		//--------------------------------------------------------------------
		
		} // End of constructor.
	
	//##########################################################################
	
	// METHODS
	
	private void processCsfastaFile(
		final File csfastaFile, 
		final MutableLong sum, 
		final MutableLong n
		) throws IOException, Exception {
		
		BufferedReader csfastaIn = new BufferedReader(new FileReader(csfastaFile));
		
		String headerLine = null;
		while ((headerLine = csfastaIn.readLine()) != null) {
			
			// Ignore comments.
			if (headerLine.matches("^#.*$")) continue;
			
			// Sanity check - assumes first line encountered is header 
			if (!headerLine.matches("^>.+$")) 
				throw new Exception("Expecting header line - found '" + headerLine + "'"); 
			
			
			final String seqLine = csfastaIn.readLine();
			
			sum.incrementBy(seqLine.length() - 1);
			n.increment();
			
			
			} // End of while loop.
		
		csfastaIn.close();
		
		} // End of method. 
	
	////////////////////////////////////////////////////////////////////////////
	
	private void processQualFile(
		final File qualFile,
		final TreeMap<Integer, MutableInteger> meanQuals,
		final TreeMap<Integer, MutableInteger> badQuals,
		final Integer cutoff,
		final QVEncoding encoding
		) throws IOException, Exception {
		
		BufferedReader qualIn = new BufferedReader(new FileReader(qualFile));
		
		String headerLine = null;
		while ((headerLine = qualIn.readLine()) != null) {
			
			// Ignore comments.
			if (headerLine.matches("^#.*$")) continue;
			
			// Sanity check - assumes first line encountered is header 
			if (!headerLine.matches("^>.+$")) 
				throw new Exception("Expecting header line - found '" + headerLine + "'"); 
			
			
			final String qualLine	= qualIn.readLine();
			
			
			processQual(qualLine, meanQuals, badQuals, cutoff, encoding);
			
			} // End of while loop.
		
		qualIn.close();
		
		
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
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
		out.println("set logscale y");
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
		final File meanQualsFile,
		final long n
		) throws IOException {
		
		PrintWriter out = new PrintWriter(new FileWriter(meanQualsFile));
		
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
	
	private void processQual(
		final String qualLine, 
		final TreeMap<Integer, MutableInteger> meanQuals,
		final TreeMap<Integer, MutableInteger> badQuals,
		final Integer cutoff,
		final QVEncoding encoding
		) {
		
		int sum = 0;
		int noOfBadQuals = 0;
		
		// Process qual line.
		final String[] qvStrings = qualLine.split("\\s");
		for (String qvString : qvStrings) {
			
			int qv = Integer.parseInt(qvString);
			
			sum += qv;
			if (qv <= cutoff) noOfBadQuals++;
			
			}
		
		
		// Calculate mean qual for read.
		int mean = (int)(Math.ceil((double)sum/(double)qvStrings.length ));
		
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
		
		if (!encoding.equals(QVEncoding.Illumina33)) 
			throw new UnsupportedOperationException(
				"Only Illumina33 currently supported"
				);
		
		return (int)ascii - 33;
	
		} // End of method.
	
	//##########################################################################
	
	// INNER CLASSES
	
	//##########################################################################

	} // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
