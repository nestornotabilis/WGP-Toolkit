// TEST_XYPlot.java created on 03 February 2004 at 12:49

//=============================================================================
//
//    Copyright (C) 2004 Kevin Ashelford
//    Contact details:
//    Email: ashelford@cardiff.ac.uk
//    Address:  Cardiff School of Bioscience, Cardiff University, PO. Box 915,
//    Cardiff, UK. CF10 3TL
//
//    This program is free software; you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation; either version 2 of the License, or
//    any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program; if not, write to the Free Software
//    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
//=============================================================================

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

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

/**
 @author  Kevin Ashelford
 */
class TEST_XYPlot extends JFrame {
	
	//##########################################################################
	
	// CLASS FIELDS
	
	////////////////////////////////////////////////////////////////////////////
	
	// MEMBER FIELDS
	
	private Plot _xyplot;
	
	//##########################################################################
	
	// ENTRY POINT
	
	/**
	 @param args the command line arguments
	 */
	public static void main(String[] args) {
		TEST_XYPlot tg = new TEST_XYPlot();
		tg.initialise();
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
		_xyplot = PlotFactory.createXYPlot();
		getContentPane().add(_xyplot);

		// Create buttons and associate appropriate actions.
		JButton button = new JButton(new PrintAction());
		getContentPane().add(button);
		
		JButton button2 = new JButton(new SaveAction());
		getContentPane().add(button2);
		
		JButton button3 = new JButton(new ImageAction());
		getContentPane().add(button3);
		
		// Specify size of graphs display wihin window.
		_xyplot.setPreferredSize(new Dimension(800, 550));
		
		// Not strictly necessary, but left for illustrative purposes.
		_xyplot.setBackground(Color.WHITE);
		_xyplot.setOpaque(true);

		// Define dimensions of graph object.
		_xyplot.setBottomMargin(70);
		_xyplot.setTopMargin(50);
		_xyplot.setLeftMargin(80);
		_xyplot.setRightMargin(50);
		_xyplot.setTickToLabelGap(5);
		_xyplot.setXAxisLabelToBorderGap(20);
		_xyplot.setYAxisLabelToBorderGap(25);

		// Switch on antialiasing.
		_xyplot.setAntiAliasing(true);
		
		// Specify characteristics of Key.
		_xyplot.setKeyVisible(true);
		_xyplot.setKeyBorderVisible(true);
		_xyplot.setKeyBackground(Color.WHITE);
		_xyplot.setKeyForeground(Color.BLACK);
		_xyplot.setKeyMargin(10);
		_xyplot.setKeyFont(new Font("Dialog", Font.PLAIN, 14));
		_xyplot.setKeyBorderWidth(2);
		_xyplot.setKeyBorderColor(Color.BLACK);
		_xyplot.setKeyOpaque(true);
		_xyplot.setKeyDefaultLocation(false);

		// Define x axis.
		_xyplot.setXAxis(
			AxisFactory.createAxis(
				"Number of mismatches", 
				AxisType.X, 
				0f, // from
				7f, // to
				1f, // Increment
				0,  // Minor ticks.
				ScaleType.Linear
				)
			);

		// Define y axis.
		_xyplot.setYAxis(
			AxisFactory.createAxis(
				"% of records", 
				AxisType.Y, 
				0f, 
				100f, 
				20f, 
				4, 
				ScaleType.Linear
				)
			);

		// Define characteristics of axes.
		DefaultAxisRenderer r = new DefaultAxisRenderer();
		_xyplot.setAxisRenderer(r);




		IAxisFormatter xAxisFormatter = r.getAxisFormatter(AxisType.X);
		xAxisFormatter.setEndMargin(0);
		xAxisFormatter.setStartMargin(0.1f);
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
		Coordinate[] c1 = new Coordinate[8];
		c1[0] = new Coordinate(0, 0);
		c1[1] = new Coordinate(1, 30);
		c1[2] = new Coordinate(2, 50);
		c1[3] = new Coordinate(3, 50);
		c1[4] = new Coordinate(4, 70);
		c1[5] = new Coordinate(5, 90);
		c1[6] = new Coordinate(6, 93);
		c1[7] = new Coordinate(7, 100);
		
		Coordinate[] c2 = new Coordinate[8];
		c2[0] = new Coordinate(0, 0);
		c2[1] = new Coordinate(1, 50);
		c2[2] = new Coordinate(2, 60);
		c2[3] = new Coordinate(3, 80);
		c2[4] = new Coordinate(4, 86);
		c2[5] = new Coordinate(5, 88);
		c2[6] = new Coordinate(6, 90);
		c2[7] = new Coordinate(7, 90);
		
		ICoordinateCollection cc1 =
			CoordinateCollectionFactory.createCoordinateCollection(
				c1, "Test 1"
				);
		
		ICoordinateCollection cc2 =
			CoordinateCollectionFactory.createCoordinateCollection(
				c2, "another_Test_2"
				);

		// Associate data with graph object.
		IGraphModel dataModel = (IGraphModel)_xyplot.getDataModel();

		dataModel.setCoordinateCollections(
			new ICoordinateCollection[]{cc1, cc2}
			);

		// Specify plot style.
		IPlotLineFormatter plf1 = new DefaultPlotLineFormatter(cc1);
		plf1.setGapToSymbol(true); 
		plf1.setLineColor(Color.BLUE);
		plf1.setLineStyle(LineStyle.Solid); 
		plf1.setLineWidth(2); 
		plf1.setPlotStyle(PlotStyle.LineAndScatter); 
		plf1.setSymbol(Symbol.ClosedSquare); 
		plf1.setSymbolColor(Color.BLUE); 
		plf1.setSymbolSize(14);
		
		IPlotLineFormatter plf2 = new DefaultPlotLineFormatter(cc2);
		plf2.setGapToSymbol(true); // OK
		plf2.setLineColor(Color.RED); // OK
		plf2.setLineStyle(LineStyle.Solid); //OK
		plf2.setLineWidth(2); //OK
		plf2.setPlotStyle(PlotStyle.LineAndScatter); // OK
		plf2.setSymbol(Symbol.ClosedSquare); // OK
		plf2.setSymbolColor(Color.RED); // OK
		plf2.setSymbolSize(14); //OK
		
		ICoordinateCollectionRenderer renderer = (ICoordinateCollectionRenderer)_xyplot.getDataRenderer();

		renderer.setPlotLineFormatters(
			new IPlotLineFormatter[]{plf1, plf2}
			);	

		// Ensure window is visible.
        setVisible(true);
			
		// Display plt.
		_xyplot.plot();
		

		// Uncomment to demonstrate clearing of graph after 5 seconds.
		//try {
		//Thread.sleep(5000);
		//} catch(Exception e) {}
		//_xyplot.clear();
		
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
		Doc doc = new SimpleDoc(_xyplot, flavor, null);
			
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
			RenderedImage image = _xyplot.createImage();
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