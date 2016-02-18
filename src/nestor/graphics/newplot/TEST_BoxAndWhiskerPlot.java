
package nestor.graphics.newplot;

import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;

import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;

import java.awt.image.RenderedImage;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.AbstractAction;
import javax.swing.Action;

import javax.imageio.ImageIO;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.ServiceUI;
import javax.print.Doc;
import javax.print.DocPrintJob;
import javax.print.SimpleDoc;
import javax.print.PrintException;
import javax.print.StreamPrintServiceFactory;
import javax.print.StreamPrintService;

import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;

import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;

import javax.print.attribute.standard.OrientationRequested;

import nestor.stats.Stats;
import java.util.*;
import nestor.graphics.newplot.boxAndWhisker.*;
import nestor.sam.statistics.samRecord.Metadata;

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 @author  Kevin Ashelford
 */
class TEST_BoxAndWhiskerPlot extends JFrame {
	
	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS
	
	private Plot _boxAndWhiskerPlot;
	
	//##########################################################################
	
	// ENTRY POINT
	
	/**
	 @param args the command line arguments
	 */
	public static void main(String[] args) {
		TEST_BoxAndWhiskerPlot bwp = new TEST_BoxAndWhiskerPlot();
		bwp.initialise();
		} // End of main.
	
	//##########################################################################

	// METHODS

	private void initialise() {

		// ensure window properly closed when requested by user.
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// Set size of window.
		setSize(800, 650);

		// Set layout style.
		getContentPane().setLayout(new FlowLayout());
		
		// Instantiate graph object and add to frame.
		_boxAndWhiskerPlot = PlotFactory.createHorizontalBoxAndWhiskerPlot();
		getContentPane().add(_boxAndWhiskerPlot);

		// Create buttons and associate appropriate actions.
		JButton button = new JButton(new PrintAction());
		getContentPane().add(button);
		
		JButton button2 = new JButton(new SaveAction());
		getContentPane().add(button2);
		
		JButton button3 = new JButton(new ImageAction());
		getContentPane().add(button3);
		
		// Specify size of graphs display wihin window.
		_boxAndWhiskerPlot.setPreferredSize(new Dimension(800, 550));

		// Define dimensions of graph object.
		_boxAndWhiskerPlot.setBottomMargin(70);
		_boxAndWhiskerPlot.setTopMargin(50);
		_boxAndWhiskerPlot.setLeftMargin(150);
		_boxAndWhiskerPlot.setRightMargin(50);
		_boxAndWhiskerPlot.setTickToLabelGap(5);
		_boxAndWhiskerPlot.setXAxisLabelToBorderGap(20);
		_boxAndWhiskerPlot.setYAxisLabelToBorderGap(25);

		// Switch on antialiasing.
		_boxAndWhiskerPlot.setAntiAliasing(true);
		
		// Specify characteristics of Key.
		_boxAndWhiskerPlot.setKeyVisible(true);
		_boxAndWhiskerPlot.setKeyBorderVisible(true);
		_boxAndWhiskerPlot.setKeyBackground(Color.WHITE);
		_boxAndWhiskerPlot.setKeyForeground(Color.BLACK);
		_boxAndWhiskerPlot.setKeyMargin(10);
		_boxAndWhiskerPlot.setKeyFont(new Font("Dialog", Font.PLAIN, 14));
		_boxAndWhiskerPlot.setKeyBorderWidth(2);
		_boxAndWhiskerPlot.setKeyBorderColor(Color.BLACK);
		_boxAndWhiskerPlot.setKeyOpaque(true);
		_boxAndWhiskerPlot.setKeyDefaultLocation(false);

		// Define x axis.
		_boxAndWhiskerPlot.setXAxis(
			AxisFactory.createAxis(
				"Coverage depth (Log10 + 1)",
				AxisType.X, 
				0f, // from
				300f, // to
				20f, // Increment
				0,  // Minor ticks.
				ScaleType.Linear
				)
			);

		_boxAndWhiskerPlot.getYAxis().setTitle("Chromosomes");

		// Define characteristics of axes.
		DefaultAxisRenderer r = new DefaultAxisRenderer();
		_boxAndWhiskerPlot.setAxisRenderer(r);



		IAxisFormatter xAxisFormatter = r.getAxisFormatter(AxisType.X);
		xAxisFormatter.setEndMargin(0);
		xAxisFormatter.setStartMargin(0);
		xAxisFormatter.setLineColor(Color.black);
		xAxisFormatter.setLineWidth(2);
		xAxisFormatter.setMajorTickLength(10);
		xAxisFormatter.setMinorTickLength(5);

		xAxisFormatter.getMajorTickLabelFormatter().setColor(Color.black);
		xAxisFormatter.getMajorTickLabelFormatter().setFont(new Font("Dialog", Font.PLAIN, 14));
		xAxisFormatter.getMajorTickLabelFormatter().setVisible(true);

		xAxisFormatter.setMajorGridLinesVisible(true);
		xAxisFormatter.getMajorGridLineFormatter().setLineColor(Color.GREEN);
		xAxisFormatter.getMajorGridLineFormatter().setLineStyle(LineStyle.Dash);
		xAxisFormatter.getMajorGridLineFormatter().setLineWidth(1);

		xAxisFormatter.setMinorGridLinesVisible(true);
		xAxisFormatter.getMinorGridLineFormatter().setLineColor(Color.red);
		xAxisFormatter.getMinorGridLineFormatter().setLineStyle(LineStyle.Dot);
		xAxisFormatter.getMinorGridLineFormatter().setLineWidth(1);

		xAxisFormatter.getMajorTickLabelFormatter().setDecimalPlaces(2);


		IAxisFormatter yAxisFormatter = r.getAxisFormatter(AxisType.Y);
		yAxisFormatter.setEndMargin(0);
		yAxisFormatter.setStartMargin(0);
		yAxisFormatter.setLineColor(Color.black);
		yAxisFormatter.setLineWidth(2);
		yAxisFormatter.setMajorTickLength(10);
		yAxisFormatter.setMinorTickLength(5);

		yAxisFormatter.getMajorTickLabelFormatter().setColor(Color.black);
		yAxisFormatter.getMajorTickLabelFormatter().setFont(new Font("Dialog", Font.PLAIN, 14));
		yAxisFormatter.getMajorTickLabelFormatter().setVisible(true);

		yAxisFormatter.setMajorGridLinesVisible(true);
		yAxisFormatter.getMajorGridLineFormatter().setLineColor(Color.GREEN);
		yAxisFormatter.getMajorGridLineFormatter().setLineStyle(LineStyle.Dash);
		yAxisFormatter.getMajorGridLineFormatter().setLineWidth(1);

		yAxisFormatter.setMinorGridLinesVisible(true);
		yAxisFormatter.getMinorGridLineFormatter().setLineColor(Color.red);
		yAxisFormatter.getMinorGridLineFormatter().setLineStyle(LineStyle.Dot);
		yAxisFormatter.getMinorGridLineFormatter().setLineWidth(1);

		yAxisFormatter.getMajorTickLabelFormatter().setDecimalPlaces(2);

		xAxisFormatter.getTitleFormatter().setColor(Color.black);
		xAxisFormatter.getTitleFormatter().setFont(new Font("Dialog", Font.PLAIN, 18));
		xAxisFormatter.getTitleFormatter().setVisible(true);

		yAxisFormatter.getTitleFormatter().setColor(Color.black);
		yAxisFormatter.getTitleFormatter().setFont(new Font("Dialog", Font.PLAIN, 18));
		yAxisFormatter.getTitleFormatter().setVisible(true);


		// Create datasets for display.
		Stats stats1 = new Stats("stats1");
		stats1.setArithmeticMean(100);
		stats1.setMedian(120);
		stats1.setUpperQuartile(140);
		stats1.setLowerQuartile(70);
		stats1.setMinimum(2);
		stats1.setMaximum(250);

		Stats stats2 = new Stats("stats2");
		stats2.setArithmeticMean(120);
		stats2.setMedian(140);
		stats2.setUpperQuartile(160);
		stats2.setLowerQuartile(80);
		stats2.setMinimum(10);
		stats2.setMaximum(240);

		Stats stats3 = new Stats("stats3");
		stats3.setArithmeticMean(120);
		stats3.setMedian(140);
		stats3.setUpperQuartile(160);
		stats3.setLowerQuartile(80);
		stats3.setMinimum(50);
		stats3.setMaximum(240);

		Stats stats4 = new Stats("stats4");
		stats4.setArithmeticMean(120);
		stats4.setMedian(140);
		stats4.setUpperQuartile(160);
		stats4.setLowerQuartile(80);
		stats4.setMinimum(50);
		stats4.setMaximum(240);


		ArrayList<Stats> statsCollection = new ArrayList<Stats>();
		statsCollection.add(stats1);
		statsCollection.add(stats2);
		statsCollection.add(stats3);
		statsCollection.add(stats4);

		// Associate data with graph object.
		IBoxAndWhiskerModel dataModel = (IBoxAndWhiskerModel)_boxAndWhiskerPlot.getDataModel();
		dataModel.setStatsCollection(statsCollection);

		// Specify box and whisker style.
		IBoxAndWhiskerFormatter f1 = new DefaultBoxAndWhiskerFormatter(stats1);
		f1.setLineColor(Color.blue);
		f1.setLineStyle(LineStyle.Solid); 
		f1.setLineWidth(2);
		f1.setSymbol(Symbol.ClosedSquare);
		f1.setSymbolColor(Color.blue);
		f1.setSymbolSize(14);
		f1.setBoxColor(Color.lightGray);
		
		IBoxAndWhiskerFormatter f2 = new DefaultBoxAndWhiskerFormatter(stats2);
		f2.setLineColor(Color.red);
		f2.setLineStyle(LineStyle.Solid); 
		f2.setLineWidth(2);
		f2.setSymbol(Symbol.ClosedCircle);
		f2.setSymbolColor(Color.black);
		f2.setSymbolSize(18);
		f2.setBoxColor(Color.pink);

		IBoxAndWhiskerFormatter f3 = new DefaultBoxAndWhiskerFormatter(stats3);
		f3.setLineColor(Color.green);
		f3.setLineStyle(LineStyle.Solid);
		f3.setLineWidth(2);
		f3.setSymbol(Symbol.ClosedCircle);
		f3.setSymbolColor(Color.black);
		f3.setSymbolSize(18);
		f3.setBoxColor(Color.lightGray);

		IBoxAndWhiskerFormatter f4 = new DefaultBoxAndWhiskerFormatter(stats4);
		f4.setLineColor(Color.black);
		f4.setLineStyle(LineStyle.Solid);
		f4.setLineWidth(2);
		f4.setSymbol(Symbol.ClosedCircle);
		f4.setSymbolColor(Color.black);
		f4.setSymbolSize(18);
		f4.setBoxColor(Color.lightGray);

		IBoxAndWhiskerRenderer renderer = (IBoxAndWhiskerRenderer)_boxAndWhiskerPlot.getDataRenderer();
		ArrayList<IBoxAndWhiskerFormatter> formatters = new ArrayList<IBoxAndWhiskerFormatter>();
		formatters.add(f1);
		formatters.add(f2);
		formatters.add(f3);
		formatters.add(f4);
		renderer.setBoxAndWhiskerFormatters(
			formatters
			);

		// Ensure window is visible.
        setVisible(true);
			
		// Display box and whisker plot.
		_boxAndWhiskerPlot.plot();
		

		// Uncomment to demonstrate clearing of graph after 5 seconds.
		/*
		try {
		Thread.sleep(5000);
		} catch(Exception e) {}
		_boxAndWhiskerPlot.clear();
		*/

		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////

	// Called by print button action.
	private void print() {
		
		// Get list of all printers that can handle Printable objects.
		DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
		PrintService[] services = 
			PrintServiceLookup.lookupPrintServices(flavor, null);
		
		// Set some printing attributes.
		PrintRequestAttributeSet printAttributes = 
			new HashPrintRequestAttributeSet();
		printAttributes.add(OrientationRequested.LANDSCAPE);
		
		// Display a dialog that allows the user to select one of the available
		// printers and to edit te default attributes.
		PrintService service = ServiceUI.printDialog(
			null, 
			100, 
			100, 
			services, 
			null, 
			null, 
			printAttributes
			);
		
		// If the user cancelled, don't do anything.
		if (service == null) return;
		
		// Now call a method to finish the printing.
		printToService(service, printAttributes);
		
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////

	// Called by print method.
	private void printToService(
		PrintService service, 
		PrintRequestAttributeSet printAttributes
		) {
		
		DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
		Doc doc = new SimpleDoc(_boxAndWhiskerPlot, flavor, null);
			
		DocPrintJob job = service.createPrintJob();
		
		// Set up a dialog box to monitor printing status.
		final JOptionPane pane = new JOptionPane(
			"Printing...",
			JOptionPane.PLAIN_MESSAGE
			);
			
		JDialog dialog = pane.createDialog(this, "Print Status");
		
		// This listener object updates the dialog as the status changes.
		job.addPrintJobListener(new PrintJobAdapter() {
			public void printJobCompleted(PrintJobEvent pje) {
				pane.setMessage("Printing complete!");
				}
			public void printDataTransferCompleted(PrintJobEvent pje) {
				pane.setMessage("Document transferred to printer.");
				}
			public void printJobRequiresAttention(PrintJobEvent pje) {
				pane.setMessage("Check printer: out of paper?");	
				}
			public void printJobFailed(PrintJobEvent pje) {
				pane.setMessage("Print job failed.");
				} 
			});
		
		// Show the dialog. Non-modal.
		dialog.setModal(false);
		//dialog.show();
        dialog.setVisible(true);
		
		// Now print the Doc to the DocPrintJob.
		try {
			job.print(doc, printAttributes);
			} 
		
		catch (PrintException pe) {
			pane.setMessage(pe.toString());
			}
			
		} // End of method.
	
	////////////////////////////////////////////////////////////////////////////

	// Called by save button action - saves display in postscript format.
	private void save() throws IOException {
		
		DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
		String format = "application/postscript";
		
		StreamPrintServiceFactory factory = 
			StreamPrintServiceFactory.
				lookupStreamPrintServiceFactories(flavor, format)[0];
		
		// Ask the user to select a file and open the selected file.
		JFileChooser c = new JFileChooser();
		if (c.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {return;}
		File f = c.getSelectedFile();
		FileOutputStream out = new FileOutputStream(f);
		
		// Obtain a PrintService that prints to that file.
		StreamPrintService service = factory.getPrintService(out);
		
		// Do printing.
		printToService(service, null);
		
		out.close();
		
		}
	
	//##########################################################################
	
	// INNER CLASSES

	// Print button action.
	private class PrintAction extends AbstractAction {
		
		public PrintAction() {
			this.putValue(Action.NAME, "Print");
			} 
		
		public void actionPerformed(java.awt.event.ActionEvent e) {
			print();
			}		
		
		} // End of inner class.
	
	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	// Save button action.
	private class SaveAction extends AbstractAction {
		
		public SaveAction() {
			this.putValue(Action.NAME, "Save");
			} 
		
		public void actionPerformed(java.awt.event.ActionEvent e) {
			try {
				save();
				} 
			catch (IOException ioe) {
				ioe.printStackTrace();
				}
			}		
		
		} // End of inner class.
	
	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	// Image button action.
	private class ImageAction extends AbstractAction {
			
		public ImageAction() {
			putValue(Action.NAME, "Save Image");
			}
		
		public void actionPerformed(java.awt.event.ActionEvent e) {
			RenderedImage image = _boxAndWhiskerPlot.createImage();
			File file = new File("plot_from_TestGraph.png");
			
			try {
				ImageIO.write(image, "png", file);
				} 
			
			catch (IOException ioe) {
				ioe.printStackTrace();
				}
			}
	
		} // End of inner class.
	
	//##########################################################################
	
	} // End of class.

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////