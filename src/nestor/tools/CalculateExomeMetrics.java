//==============================================================================
//
//   CalculateExomeMetrics.java
//   Created: Nov 28, 2011 at 4:21:17 PM
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
import net.sf.samtools.*;
import java.util.regex.*;
import nestor.stats.*;
import nestor.graphics.newplot.*;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 * 
 * @author  Kevin Ashelford.
 */
public class CalculateExomeMetrics implements Constants {

	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS
	
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
			final ArrayList<String> featureFileSynonyms = new ArrayList<String>();
			featureFileSynonyms.add("f");
			featureFileSynonyms.add("features");
			optionParser.acceptsAll(
				featureFileSynonyms,
				"Features file in BED format (required)."
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------
		
			
			// MAPQ histogram outfile
			//------------------------------------------------------------------
			final ArrayList<String> mapqFileSynonyms = new ArrayList<String>();
			mapqFileSynonyms.add("m");
			mapqFileSynonyms.add("mapq");

			optionParser.acceptsAll(
				mapqFileSynonyms,
				"MAPQ histogram outfile (required)."
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------

			
	
			// Results outfile
			//------------------------------------------------------------------
			final ArrayList<String> resultsFileSynonyms = new ArrayList<String>();
			resultsFileSynonyms.add("r");
			resultsFileSynonyms.add("results");
			optionParser.acceptsAll(
				resultsFileSynonyms,
				"Results file as a properties file (required)."
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------
			
			
			
			// Obtain and process arguments
			OptionSet options = optionParser.parse(args);

			// Action infile arguments.
			if (options.has("help")) {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}

			
			// bam infile must always be provided.
			File bamfile = null;
			if (options.has("bam")) {
				bamfile = new File((String)options.valueOf("bam"));
				JoptsimpleUtil.checkFileExists(bamfile);
				}
			else {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}

			
			// bed infile must always be provided.
			File bedfile = null;
			if (options.has("features")) {
				bedfile = new File((String)options.valueOf("features"));
				JoptsimpleUtil.checkFileExists(bedfile);
				}
			else {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}
			
			
			// Results outfile must always be provided.
			File resultsfile = null;
			if (options.has("results")) {
				resultsfile = new File((String)options.valueOf("results"));
				}
			else {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}
			

			// MAPQ outfile must always be provided.
			File mapqFile = null;
			if (options.has("mapq")) {
				mapqFile = new File((String)options.valueOf("mapq"));
				}
			else {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}
			

			// Run program.
			new CalculateExomeMetrics(
				bamfile,
				bedfile,
				resultsfile,
				mapqFile
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
	 * Creates a new instance of <code>CalculateExomeMetrics</code>.
	 */
	public CalculateExomeMetrics(
		final File bamfile,
		final File bedfile,
		final File resultsfile,
		final File mapqFile
		) throws IOException, Exception {
		
		// Subsample BAM according to BED, and generate simple metrics.
		subsampleBAM(bamfile, bedfile, resultsfile, mapqFile);

		// assess coverage metrics for target and flanking regions.
		
		
		// Assessing target region coverage metrics
		// run("date; samtools mpileup $bamFile > $mpileupFile");


		// Assessing target and flanking region coverage metrics.
		// run("java -jar -Xmx20g /share/apps/WGP-Toolkit/AssessingTargetRegionCoverage.jar -p $mpileupFile -r $resultsFile -f $bedFile -e 200");

		
		} // End of constructor.
	
	//##########################################################################
	
	// METHODS
	
	private void subsampleBAM(final File bamfile, final File bedfile, final File resultsfile, final File mapqFile) throws IOException, Exception {
		
		// PROCESS BED FILE.
		final BedData bedData = new BedData();
		
		Matcher bedRegex = Pattern.compile("^(\\S+)\\t(\\d+)\\t(\\d+)\\t.*$").matcher("");
		Matcher ignoreRegex = Pattern.compile("^track.*$").matcher("");
		
		BufferedReader bedIn = new BufferedReader(new FileReader(bedfile));
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
				throw new Exception("Unexpected line encountered '" + line + "'");
				}
			
			
			}
		bedIn.close();
		
		

		// PROCESS BAM FILE.
		
		// Create SAM/BAM file input stream
		final SAMFileReader bamIn = new SAMFileReader(bamfile);
		SAMStats samStats = new SAMStats(bamIn, bedData);
		bamIn.close();
		
		
		// Record read counts
		final PrintWriter out = new PrintWriter(new FileWriter(resultsfile, true));
		out.println(TOTAL_READS_ONTARGET + "=" + samStats.getReadCount());
		out.println(MAPPED_READS_ONTARGET + "=" + samStats.getMappedReadCount());
		out.println(UNIQUE_READS_ONTARGET + "=" + samStats.getUniqueReadCount());
		out.close();
		
		// Get Histogram.
		IHistogram histogram = samStats.getMapqHistogram();
		
		createHistogram(histogram, mapqFile);
		
		createGnuplotScript(
			mapqFile, 
			"Variation in MAPQ score",
			"MAPQ score",
			"Percentage of reads"
			);
		runGnuplot(mapqFile);

	
		
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////	
	
	private void createHistogram(
		final IHistogram data,
		final File meanQualsFile
		) throws IOException {
		
		PrintWriter out = new PrintWriter(new FileWriter(meanQualsFile));
		
		ICoordinateCollection cc = data.getCoordinateCollection();
		
		for (int i = 0; i < cc.getSize(); i++) {
			Coordinate c = cc.getCoordinate(i);
			out.println(c.getX() + "\t" + c.getY());
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
		out.println("set logscale y");
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
