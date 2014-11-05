package de.booking.toolbox;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.JTable;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.Chunk;

import de.booking.model.Booking;



public class JTablePdf {
	
	public static void createPDF2(String fileOutName, JTable jTable, String month_str, String year_str, float total4monthNyear) {
		// writes to formatted columns
		
		float title_padding = 10.0f;
		
		//Document document = new Document();
		// Landscape mode:
		Document document = new Document(PageSize.LETTER.rotate());
		
		String[] oneLessColumn = Booking.returnFieldnames().split(":");
		int num_columns = oneLessColumn.length + 1; // "+1" for total at end of:  kreuzfahrt:flug:hotel:versicherung:day_departure:month_departure:year_departure:surname:first_name:booking_number:booking_date:storno:total"
		String[] columnTitles = new String[num_columns];
		for (int col_i=0; col_i<num_columns-1;col_i++){
			columnTitles[col_i] = oneLessColumn[col_i];
		}
		columnTitles[columnTitles.length-1] = "total";
		
		try {
			PdfWriter.getInstance(document, new FileOutputStream(fileOutName));
			document.open();
			PdfPTable table = new PdfPTable(num_columns);
			
			// set universal FONT:
			Font myFont = new Font(); // name & point size 
			myFont.setSize(6);
			
			// create title cell:
			PdfPCell cell_title = new PdfPCell(new Paragraph("Julia's bookings for month "+month_str+" year "+year_str));
			cell_title.setColspan(num_columns);
			cell_title.setHorizontalAlignment(Element.ALIGN_CENTER);
			//cell_title.setBackgroundColor(new Color(128,200,128));
			cell_title.setPadding(title_padding);
			table.addCell(cell_title);
			
			// create total:
			PdfPCell cell_total = new PdfPCell(new Paragraph("Julia's total:  "+String.valueOf(total4monthNyear)) );
			cell_total.setColspan(num_columns);
			cell_total.setHorizontalAlignment(Element.ALIGN_CENTER);
			//cell_total.setBackgroundColor(new Color(128,200,128));
			cell_total.setPadding(title_padding);
			table.addCell(cell_total);
			
			// add column titles:
			for (int col_i=0; col_i < columnTitles.length; col_i++) {
				Paragraph myP = new Paragraph();
				Chunk bar = new Chunk(columnTitles[col_i], myFont ); 
				myP.add( bar ); 
				table.addCell(myP);
			}
			
			// add columns
			// for each item just add cell:
			ResultSet myCachedRowSet = ((BookingTableModel) jTable.getModel()).getCoffeesRowSet();
			try {
				myCachedRowSet.beforeFirst();
				while (myCachedRowSet.next()) {
					// at this row, get each column:
					for (int col_i=0; col_i < columnTitles.length; col_i++) {
						// get each column at this row:
						ResultSetMetaData rsmd = (ResultSetMetaData)myCachedRowSet.getMetaData();
						String column_atthis_row = myCachedRowSet.getString(columnTitles[col_i]);
						Paragraph myP = new Paragraph();
						if (column_atthis_row==null) {
							column_atthis_row = "";
						}
						Chunk bar = new Chunk(column_atthis_row, myFont ); 
						myP.add( bar ); 
						table.addCell(myP);
					}
				}
			} catch (SQLException e) {
				System.out.println("*** JtablePdf.createPDF2: SQLException myCachedRowSet.next() BOMBING!");
				e.printStackTrace();
			}
			
			// close all:
			document.add(table);
			document.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("*** JtablePdf.createPDF2:  FileNotFoundException");
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			System.out.println("*** JtablePdf.createPDF2:  DocumentException");
			e.printStackTrace();
		}
	}
	
}
