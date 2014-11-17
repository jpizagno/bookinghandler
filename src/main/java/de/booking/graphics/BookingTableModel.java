package de.booking.graphics;


import java.lang.reflect.Field;
import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import de.booking.model.Booking;

public class BookingTableModel extends AbstractTableModel implements TableModel {

	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<Booking> coffeesRowSet; // The ResultSet to interpret
	  int numcols, numrows; // How many rows and columns in the table

	  public List<Booking> getCoffeesRowSet() {
	    return coffeesRowSet;
	  }


	  public BookingTableModel(List<Booking> myBookingList) {
	    coffeesRowSet = myBookingList;
	    Booking myBooking = new Booking();
	    numcols = myBooking.getClass().getDeclaredFields().length;
	    //numcols = coffeesRowSet.size();
	  }
	 

	  /** Method from interface TableModel; returns the number of columns */

	  public int getColumnCount() {
	    return numcols;
	  }

	    /** Method from interface TableModel; returns the number of rows */

	  public int getRowCount() {
	    return numrows;
	  }

	  /** Method from interface TableModel; returns the column name at columnIndex
	   *  based on information from ResultSetMetaData
	   */

	  public String getColumnName(int column) {
		 Booking myBooking = new Booking();
		 return myBooking.getClass().getDeclaredFields()[column].getName();
	  }

	  /** Method from interface TableModel; returns the most specific superclass for
	   *  all cell values in the specified column. To keep things simple, all data
	   *  in the table are converted to String objects; hence, this method returns
	   *  the String class.
	   */

	  @SuppressWarnings({  "rawtypes", "unchecked" })
	  public Class getColumnClass(int column) {
		  return String.class;
	  }

	  /** Method from interface TableModel; returns the value for the cell specified
	   *  by columnIndex and rowIndex. TableModel uses this method to populate
	   *  itself with data from the row set. SQL starts numbering its rows and
	   *  columns at 1, but TableModel starts at 0.
	   */
	  public Object getValueAt(int rowIndex, int columnIndex) {

		  Field[] fields = this.coffeesRowSet.get(rowIndex).getClass().getDeclaredFields();
		  return fields[columnIndex];
	  }

	    /** Method from interface TableModel; returns true if the specified cell
	     *  is editable. This sample does not allow users to edit any cells from
	     *  the TableModel (rows are added by another window control). Thus,
	     *  this method returns false.
	     */

	  public boolean isCellEditable(int rowIndex, int columnIndex) {
		  if (columnIndex==10) {
			  // this is the booking number
			  return false;
		  } else {
			  return true;
		  }
	  }
	  
	  public Booking getBookingAtRow(int row){
		  return this.coffeesRowSet.get(row);
	  }

	  public void addTableModelListener(TableModelListener l) {
	  }

	  public void removeTableModelListener(TableModelListener l) {
	  }

}
