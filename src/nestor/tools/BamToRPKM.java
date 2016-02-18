//==============================================================================
//
//   BamToRPKM.java
//   Created: Mar 8, 2012 at 9:44:19 AM
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

import nestor.bamToRPKM.*;
import nestor.bamToRPKM.annotation.*;
import nestor.util.ProcessTimer;
import joptsimple.*;
import java.util.*;
import nestor.util.JoptsimpleUtil;
import java.io.*;
import net.sf.samtools.*;
import nestor.util.BAMUtil;
import nestor.io.*;
import nestor.io.gtf.*;
import nestor.sam.feature.*;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
/**
 * 
 * @author  Kevin Ashelford.
 */
public class BamToRPKM {

	//##########################################################################
	
	// CLASS FIELDS
	
	public static final int MIN_OVERLAP = 20;
	public static final int MIN_MAPQ = 0;
	public static final String PROGRAM_NAME = "BamToRPKM";
	public static final String LINE_BREAK = System.getProperty("line.separator");
	
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


			// Outfile
			//------------------------------------------------------------------
			final ArrayList<String> outfileSynonyms = new ArrayList<String>();
			outfileSynonyms.add("o");
			outfileSynonyms.add("outfile");
			optionParser.acceptsAll(
				outfileSynonyms,
				"Outfile in gtf format (optional)."
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------


			// Overlap
			//------------------------------------------------------------------
			final ArrayList<String> overlapSynonyms = new ArrayList<String>();
			overlapSynonyms.add("O");
			overlapSynonyms.add("overlap");
			optionParser.acceptsAll(
				overlapSynonyms,
				"Minimum overlap distance between read and feature "
				+ "(optional; default, " + MIN_OVERLAP + ")."
				).withRequiredArg().ofType(Integer.class);
			//------------------------------------------------------------------

			// Infile
			//------------------------------------------------------------------
			final ArrayList<String> infileSynonyms = new ArrayList<String>();
			infileSynonyms.add("i");
			infileSynonyms.add("infile");

			optionParser.acceptsAll(
				infileSynonyms,
				"SAM or BAM infile (must be sorted)."
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------


			// Report per exon or transcript.
			//------------------------------------------------------------------
			final ArrayList<String> reportSynonyms = new ArrayList<String>();
			reportSynonyms.add("r");
			reportSynonyms.add("report");

			optionParser.acceptsAll(
				reportSynonyms,
				"Category of output to report.  "
				+ "Accepts 'exon' (default), 'cds', 'transcript', or 'all')."
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------

			// Ignore strand.
			//------------------------------------------------------------------
			final ArrayList<String> strandSynonyms = new ArrayList<String>();
			strandSynonyms.add("s");
			strandSynonyms.add("stranded");

			optionParser.acceptsAll(
				strandSynonyms,
				"If true, strandedness is assumed (default, true)."
				).withRequiredArg().ofType(Boolean.class);
			//------------------------------------------------------------------
			
			
			// exclude duplicates.
			//------------------------------------------------------------------
			final ArrayList<String> dupSynonyms = new ArrayList<String>();
			dupSynonyms.add("d");
			dupSynonyms.add("duplicates");

			optionParser.acceptsAll(
				dupSynonyms,
				"If true, duplicates are allowed (default, true)."
				).withRequiredArg().ofType(Boolean.class);
			//------------------------------------------------------------------


			// Normalise by reference element or full reference.
			//------------------------------------------------------------------
			final ArrayList<String> refSynonyms = new ArrayList<String>();
			refSynonyms.add("R");

			optionParser.acceptsAll(
				refSynonyms,
				"If true, calculate RPKM with respect to total reads mapping to "
				+ "genome, otherwise RPKM calculated with respect to specific "
				+ "chromosome (default, true)."
				).withRequiredArg().ofType(Boolean.class);
			//------------------------------------------------------------------

			
			// Feature infile
			//------------------------------------------------------------------
			final ArrayList<String> featurefileSynonyms = new ArrayList<String>();
			featurefileSynonyms.add("f");
			featurefileSynonyms.add("features");

			optionParser.acceptsAll(
				featurefileSynonyms,
				"Feature file, in gtf format, specifying feature locations."
				).withRequiredArg().ofType(String.class);
			//------------------------------------------------------------------


			
			// Min mapping quality
			//------------------------------------------------------------------
			final ArrayList<String> mapqSynonyms = new ArrayList<String>();
			mapqSynonyms.add("m");
			mapqSynonyms.add("mapq");

			optionParser.acceptsAll(
				mapqSynonyms,
				"Minimum MAPQ score (default, " + MIN_MAPQ +  ")."
				).withRequiredArg().ofType(Integer.class);
			//------------------------------------------------------------------


			// Obtain and process arguments
			OptionSet options = optionParser.parse(args);
			
			// Action infile arguments.
			if (options.has("help")) {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}

			// Infile must always be provided.
			File infile = null;
			if (options.has("infile")) {
				infile = new File((String)options.valueOf("infile"));
				JoptsimpleUtil.checkFileExists(infile);
				}
			else {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}

			// Feature file must always be provided.
			File featurefile = null;
			if (options.has("features")) {
				featurefile = new File((String)options.valueOf("features"));
				JoptsimpleUtil.checkFileExists(featurefile);
				}
			else {
				JoptsimpleUtil.displayHelp(optionParser);
				System.exit(0);
				}

			// Outfile may be provided.
			File outfile = null;
			if (options.has("outfile")) {
				outfile = new File( (String)options.valueOf("outfile") );
				}

			
			// Assume strandedness.
			boolean isStranded = true;
			if (options.has("stranded")) {
				isStranded = (Boolean)options.valueOf("stranded");
				}
			
			// Allow duplicates.
			boolean allowDuplicates = true;
			if (options.has("duplicates")) {
				allowDuplicates = (Boolean)options.valueOf("duplicates");
				}
			
			// How to normalise.
			boolean normaliseWithFullRef = true;
			if (options.has("R")) {
				normaliseWithFullRef = (Boolean)options.valueOf("R");
				}

			// Overlap allowed.
			int overlap = MIN_OVERLAP;
			if (options.has("overlap")) {
				overlap = (Integer)options.valueOf("overlap");
				}

			
			// Overlap allowed.
			int minMapq = MIN_MAPQ;
			if (options.has("mapq")) {
				minMapq = (Integer)options.valueOf("mapq");
				}

			
			// Report category may be provided.
			ReportType reportType = ReportType.Exon;
			if (options.has("report")) {

				String r = (String)options.valueOf("report");

				if      (r.equalsIgnoreCase("exon")) {reportType = ReportType.Exon;}
				else if (r.equalsIgnoreCase("cds")) {reportType = ReportType.CDS;}
				else if (r.equalsIgnoreCase("transcript")) {reportType = ReportType.Transcript;}
				else if (r.equalsIgnoreCase("all")) {reportType = ReportType.All;}
				else {
					throw new Exception(
						"Do not recognise '" + r + "' as a valid report type."
						);
					}

				}


			// Run program with appropriate arguments.
			new BamToRPKM(
				infile,
				outfile,
				featurefile,
				reportType,
				overlap,
				normaliseWithFullRef,
				minMapq,
				allowDuplicates,
				isStranded
				);

			} 

		catch (IOException ioe) {
			System.err.println("ERROR: " + ioe.getMessage());
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
	 * Creates a new instance of <code>BamToRPKM</code>.
	 */
	public BamToRPKM (
		final File infile,
		final File outfile,
		final File featureFile,
		final ReportType reportType,
		final int overlap,
		final boolean normaliseWithFullRef,
		final int minMapq,
		final boolean allowDuplicates,
		final boolean isStranded
		) throws IOException {
		
		
		// Abort if outfile already exists.
		if (outfile != null && outfile.exists()) {
			throw new IOException(
				"Cannot continue '" + outfile + "' already exists!"
				);
			}

		// If to normalise with reads mapping to full reference, determine total
		// number.
		int overallTotalReads = 0;
		if (normaliseWithFullRef) {
			overallTotalReads = countMappedReads(infile);
			}



		// Create output stream object for writing rpkm values to.
		PrintStream out = null;
		// No outfile specified, so write to STDOUT.
		if (outfile == null) {out = System.out;}
		// Outfile specified.
		else {out = new PrintStream(outfile);}


		// Create SAM/BAM file input stream
		final SAMFileReader in = new SAMFileReader(infile);

		// Silence over-strict validation errors.
		in.setValidationStringency(SAMFileReader.ValidationStringency.SILENT);

		// Get ref sizes.
		HashMap<String, Integer> refSizes = BAMUtil.getRefSizes(in);

		// Stop if data not sorted.
//		if (in.getFileHeader().getSortOrder() == SortOrder.unsorted) {
//			throw new BAMToRPKMException(
//				"Sorry, cannot proceed as '" + infile.getName() +
//				"' appears to be unsorted. Please sort and retry."
//				);
//			}

		// Now process reads within bam file, one reference at a time.
		String currentRefId = null;
		Reference reference = null;
		int totalReads = 0;
		// Record represents a single read mapping.
		for (SAMRecord record : in) {

			// Ignore reads that do not map.
			if ((record.getFlags() & 4) == 4) continue;
			
			
			// Ignore duplicates if requested
			if (!allowDuplicates && (record.getFlags() & 1024) == 1024)	continue;

			// Ignore reads with mapq less than min.
			if (record.getMappingQuality() < minMapq) continue;
			
			// Increment read count for total number of reads mapped to reference.
			totalReads++;

			// Get relevant read info.
			final String refId	= record.getReferenceName();
		//	final int readStart	= record.getAlignmentStart();
		//	final int readEnd	= record.getAlignmentEnd();

			PrimerSet primerSet = determinePrimerSet(record.getReadName());



			Strand strand = Strand.Plus;
			if ((record.getFlags() & 16) == 16) strand = Strand.Minus;
	
			// Ignore strandedness if requested.
			if (!isStranded) strand = Strand.Ignore;
			

			// Sanity check (just in case...)
			if (!refSizes.containsKey(refId)) {
				throw new RuntimeException(
					"ERROR: Unexpected chromosome encountered: " + refId
					);
				}

			// New ref encountered, so process previous data before continuing.
			if (currentRefId!=null && !currentRefId.equalsIgnoreCase(refId)) {
				
				if (normaliseWithFullRef) {
					calculateAndWriteRPKM(out, reference, overallTotalReads, reportType);
					}
				else {
					calculateAndWriteRPKM(out, reference, totalReads, reportType);
					}

				totalReads = 0;
				reference = null;
				}

			// New ref encountered, therefore create new reference object.
			if(currentRefId==null || !currentRefId.equalsIgnoreCase(refId)) {
				currentRefId = refId;
				reference = readGtf(
					featureFile,
					currentRefId,
					refSizes.get(currentRefId)
					);
				}

			// If read maps to an exon, score against that exon.
			if (reference.hasExonsBetween(record.getAlignmentStart(), record.getAlignmentEnd())) {
				process(
					primerSet,
					reference,
					record,
					strand,
					reportType,
					overlap
					);
				}

			}

		if (normaliseWithFullRef) {
			calculateAndWriteRPKM(out, reference, overallTotalReads, reportType);
			}
		else {
			calculateAndWriteRPKM(out, reference, totalReads, reportType);
			}

		

		out.close();

		//net.cgr.util.MemoryUtility.print();
		
		
		
		} // End of constructor.
	
	//##########################################################################
	
	// METHODS
	
	private int countMappedReads (final File infile) {

		int totalReads = 0;

		// Create SAM/BAM file input stream
		final SAMFileReader in = new SAMFileReader(infile);

		// Silence over-strict validation errors.
		in.setValidationStringency(SAMFileReader.ValidationStringency.SILENT);

		for (SAMRecord record : in) {

			// Ignore reads that do not map.
			if ((record.getFlags() & 4) == 4) continue;

			// Increment read count for total number of reads mapped to reference.
			totalReads++;
			}

		in.close();

		return totalReads;

		} // End of method.

	////////////////////////////////////////////////////////////////////////////
	
	private PrimerSet determinePrimerSet(String readId) {

		if (readId.endsWith("F3")) {
			return PrimerSet.F3;
			}
		else if (readId.endsWith("F5-BC")) {
			return PrimerSet.F5;
			}
		else {
			return PrimerSet.Unknown;
			}

		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	private void calculateAndWriteRPKM(
		final PrintStream out,
		final Reference reference,
		final int N,
		final ReportType type
		) throws IOException {

		/*
		Calculating RPKM
		================

		http://www.nature.com/nmeth/journal/v5/n7/extref/nmeth.1226-S1.pdf

		reads per KB per million reads (RPKM), using the formula:

        R = 10^9C / NL

		Where
			C = number of mappable reads that fall onto the gene's exons.
			N = Total number of mappable reads in the experiment.
			L = Sum of the exons in base pairs.
		*/


		ArrayList<IFeature> features = new ArrayList<IFeature>();

		if (type == ReportType.Exon || type == ReportType.CDS || type == ReportType.Transcript) {
			features = calculateRPKMs(reference, N, type);
			}
		
		else if (type == ReportType.All) {
			features.addAll(calculateRPKMs(reference, N, ReportType.Exon));
			features.addAll(calculateRPKMs(reference, N, ReportType.CDS));
			features.addAll(calculateRPKMs(reference, N, ReportType.Transcript));
			}
		
		else {
			throw new RuntimeException(
				"Unsupported report type '" + type + "'."
				);
			}


		// Write to outfile.
		Collections.sort(features); 
		IWriter writer = GtfWriterFactory.createGtfWriter(features);
		writer.write(out);

		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	private ArrayList<IFeature> calculateRPKMs(
		final Reference reference,
		final int N,
		final ReportType type
		) {

		ArrayList<IFeature> features = new ArrayList<IFeature>();

//		NumberFormat rpkmFormatter	= NumberFormat.getInstance();
///		rpkmFormatter.setMinimumIntegerDigits(1);
//		rpkmFormatter.setMinimumFractionDigits(2);
//		rpkmFormatter.setMaximumFractionDigits(2);

//		NumberFormat readCountFormatter	= NumberFormat.getInstance();
//		readCountFormatter.setMinimumIntegerDigits(1);
//		readCountFormatter.setMinimumFractionDigits(0);
//		readCountFormatter.setMaximumFractionDigits(0);

		for (Annotation a : reference.getAnnotations(type)) {

			long C = a.getReadCount();
			double L = (double)a.getSize();
			double R = (1000000000d * (double)C) / (N*L);

			HashMap<AttributeType, String> attributes =
				new HashMap<AttributeType, String>();

			attributes.put(AttributeType.GeneId, a.getGeneId());
			attributes.put(AttributeType.TranscriptId, a.getTranscriptId());
			attributes.put(AttributeType.RPKM, String.valueOf(R));

			features.add(
				FeatureFactory.createFeature(
					a.getRefId(),	// seqname - The name of the sequence. Must be a chromosome or scaffold.
					PROGRAM_NAME,	// source - The program that generated this feature.
					type.getTag(),	// feature - The name of this type of feature. Some examples of standard feature types are "CDS", "start_codon", "stop_codon", and "exon".
					a.getStart(),	// start - The starting position of the feature in the sequence. The first base is numbered 1.
					a.getEnd(),		// end - The ending position of the feature (inclusive).
					String.valueOf(C),				// score - A score between 0 and 1000 (or dot).
					a.getStrand().getChar(),	// strand - Valid entries include '+', '-', or '.' (for don't know/don't care).
					'.', // frame - If the feature is a coding exon, frame should be a number between 0-2 that represents the reading frame of the first base. If the feature is not a coding exon, the value should be '.'.
					attributes
					)
				);

			}

		return features;

		} // end of method.

	////////////////////////////////////////////////////////////////////////////
	
	private Reference readGtf(
		final File infile,
		final String refId, 
		final int size
		) throws IOException {

		BufferedReader in = new BufferedReader(new FileReader(infile));
		MyGtfReaderHandler handler = new MyGtfReaderHandler(refId, size);
		IReader reader = GtfReaderFactory.createReader();
		reader.read(in, handler);
		in.close();

		return handler.getReference();

		} // End of method.

	////////////////////////////////////////////////////////////////////////////
	
		private void process(
		final PrimerSet primerSet,
		final Reference reference,
		final SAMRecord record,
		final Strand strand,
		final ReportType type,
		final int overlap
		) {

		
		final int readStart = record.getAlignmentStart();
		final int readEnd = record.getAlignmentEnd();
			
			
		// Note transcripts require exons to be processed.
		if (type == ReportType.Exon || type == ReportType.Transcript) {
			Exon[] exons = reference.getExonsBetween(readStart, readEnd);
			processAnnotation(primerSet, record, strand, exons, overlap);
			}

		else if (type == ReportType.CDS) {
			CDS[] CDSs = reference.getCDSsBetween(readStart, readEnd);
			processAnnotation(primerSet, record, strand, CDSs, overlap);
			}

		else if (type == ReportType.All) {
			// exons (and transcripts)
			Exon[] exons = reference.getExonsBetween(readStart, readEnd);
			processAnnotation(primerSet, record, strand, exons, overlap);
			// cds
			CDS[] CDSs = reference.getCDSsBetween(readStart, readEnd);
			processAnnotation(primerSet, record, strand, CDSs, overlap);
			}

		else {
			throw new RuntimeException(
				"Unsupported report type '" + type + "'."
				);
			}

		} // End of method.

	////////////////////////////////////////////////////////////////////////////
	
	private void processAnnotation(
		final PrimerSet primerSet,
		final SAMRecord record,
		final Strand strand,
		final Annotation[] annotations,
		final int overlap
		) {

		
		final int readStart = record.getAlignmentStart();
		final int readEnd = record.getAlignmentEnd();
		
		// Consider each annotation in turn.
		for (Annotation a : annotations) {

			
			// ACTION IF STANDEDNESS IS REQUIRED
			if (strand != Strand.Ignore) {
				
				// IGNORE IF:
				//------------------------------------------------------------------
			
				// if wrong strand for the primer set F3 (SOLiD)
				if (primerSet == PrimerSet.F3 && strand != a.getStrand()) continue;

				// if wrong strand for the primer set F5 (SOLiD)
				if (primerSet == PrimerSet.F5 && strand == a.getStrand()) continue;

				// If primer set is unknown assume Illumina hence equivalent to F3.
				if (primerSet == PrimerSet.Unknown && strand != a.getStrand()) continue;
			
				//------------------------------------------------------------------
				
				
				}
			
			
			
			
			
			// Deal with this scenario first: read straddles small annotation.
			if (readStart < a.getStart() && readEnd > a.getEnd()) {

				// Read map be split - if so, special treatment: 
				if (isSplit(record)) {
					// Increment if split regions cover feature.
					fallsBetweenGap(record, a, overlap);
					}
				
				// Read is unsplit - must completely cover feature - so increment normally.	
				else {
					a.incrementReadCount();
					}
				}
			
				
			// All other scenarios	
			else {
			
				// Specify specific scenarios under which count may be incremented.
				increment(readStart, readEnd, overlap, record, a);
			
				}

			} // No more exons to process.


		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	private void increment(
		final int readStart, 
		final int readEnd, 
		final int overlap, 
		final SAMRecord record,
		final Annotation a
		) {
	
			// Senario 1: read fully within exon.
			if (readStart >= a.getStart() && readEnd <= a.getEnd()) {
				a.incrementReadCount();
				}

			// Senario 2: read overlaps annotation end
			else if (readStart >= a.getStart() && readEnd > a.getEnd()) {

				if (readStart + overlap <= a.getEnd()) {
					a.incrementReadCount();
					}
				
				}

			// Scenario 3: read overlaps annotation beginning
			else if (readStart < a.getStart() && readEnd <= a.getEnd()) {

				if (readEnd - overlap >= a.getStart()) {
					a.incrementReadCount();
					}
				
				}

				
	
		} // End of method.
			
	////////////////////////////////////////////////////////////////////////////
	
	private boolean isSplit (final SAMRecord record) {
		
		if (record.getCigar().getReadLength() == record.getCigar().getReferenceLength()) {
			return false;
			} 
		else {
			return true;
			}
		
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////
	
	private void fallsBetweenGap (final SAMRecord record, final Annotation annotation, final int overlap) {
		
		// Get elements of CIGAR.
		List<CigarElement> elements = record.getCigar().getCigarElements();
		
		// Identify all start and end positions of matching (M) elements.
		int start = record.getAlignmentStart();
		int end;
		for (CigarElement element: elements) {
			
			if (element.getOperator() == element.getOperator().M) {
				
				end = start + element.getLength();
				
				// Assess whether or not to increment.
				increment(start, end, overlap, record, annotation);
				
				
				}
			
			}
		
		
		} // End of method.
	
	//##########################################################################
	
	// INNER CLASSES
		
	private class MyGtfReaderHandler implements IGtfReaderHandler {

		final Reference reference;

		public MyGtfReaderHandler(final String refId, final int size) {
			reference = new Reference(refId, size);
			}

		public void _nextLine(
			String refId,		// 1. seqname - The name of the sequence. Must be a chromosome or scaffold.
			String source,		// 2. source - The program that generated this feature.
			String feature,		// 3. feature - The name of this type of feature. Some examples of standard feature types are "CDS", "start_codon", "stop_codon", and "exon".
			int start,			// 4. start - The starting position of the feature in the sequence. The first base is numbered 1.
			int end,			// 5. end - The ending position of the feature (inclusive).
			String score,		// 6. score - A score between 0 and 1000 (or dot).
			char strand,		// 7. strand - Valid entries include '+', '-', or '.' (for don't know/don't care).
			char frame,		// 8. frame - If the feature is a coding exon, frame should be a number between 0-2 that represents the reading frame of the first base. If the feature is not a coding exon, the value should be '.'.
			HashMap<AttributeType, String> attributes
			//String geneId,		// 9. gene_id - A globally unique identifier for the genomic source of the sequence.
			//String transcriptId	// 10. transcript_id - A globally unique identifier for the predicted transcript.
			) {

			// Do nothing if not the right reference.
			if (!reference.getId().equalsIgnoreCase(refId)) return;

			// Process exons
			if (feature.equalsIgnoreCase("exon")) {
				
				// Create exon object.
				Exon exon = new Exon(
					refId,
					start,
					end,
					Strand.getStrand(strand),
					frame,
					attributes.get(AttributeType.GeneId),
					attributes.get(AttributeType.TranscriptId)
					);

				// Store.
				reference.addExon(exon, start, end);
				}


			// process CDS
			if (feature.equalsIgnoreCase("CDS")) {

				CDS cds = new CDS(
					refId,
					start,
					end,
					Strand.getStrand(strand),
					frame,
					attributes.get(AttributeType.GeneId),
					attributes.get(AttributeType.TranscriptId)
					);

				// Store.
				reference.addCDS(cds, start, end);
				}
			
			}

		public Reference getReference() {return reference;}

		} // end of inner class.	

	//##########################################################################

	} // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
