package de.booking.graphics;


import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class BookingTableModel extends AbstractTableModel implements TableModel {

	  private ResultSet coffeesRowSet; // The ResultSet to interpret
	  private ResultSetMetaData metadata; // Additional information about the results
	  int numcols, numrows; // How many rows and columns in the table

	  public ResultSet getCoffeesRowSet() {
	    return this.coffeesRowSet;
	  }


	  public BookingTableModel(ResultSet rowSetArg) throws SQLException {

	    this.coffeesRowSet = rowSetArg;
	    this.metadata = this.coffeesRowSet.getMetaData();
	    numcols = metadata.getColumnCount();

	    // Retrieve the number of rows.
	    this.coffeesRowSet.beforeFirst();
	    this.numrows = 0;
	    while (this.coffeesRowSet.next()) {
	      this.numrows++;
	    }
	    this.coffeesRowSet.beforeFirst();
	  }

	  //public void addEventHandlersToRowSet(RowSetListener listener) {
	  //  this.coffeesRowSet.addRowSetListener(listener);
	  //   }

      /*
	  public void insertRow(String coffeeName, int supplierID, float price,
	                        int sales, int total) throws SQLException {

	    try {
	      this.coffeesRowSet.moveToInsertRow();
	      this.coffeesRowSet.updateString("COF_NAME", coffeeName);
	      this.coffeesRowSet.updateInt("SUP_ID", supplierID);
	      this.coffeesRowSet.updateFloat("PRICE", price);
	      this.coffeesRowSet.updateInt("SALES", sales);
	      this.coffeesRowSet.updateInt("TOTAL", total);
	      this.coffeesRowSet.insertRow();
	      this.coffeesRowSet.moveToCurrentRow();
	    } catch (SQLException e) {
	      System.out.println("BookingTableModel.insertRow bomb because:  ");
	      e.printStackTrace();
	    }
	  }
	  */

	  public void close() {
	    try {
	      coffeesRowSet.getStatement().close();
	    } catch (SQLException e) {
	    	System.out.println("BookingTableModel.close bomb because:  ");
		      e.printStackTrace();
	    }
	  }

	  /** Automatically close when we're garbage collected */
	  protected void finalize() {
	    close();
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
	    try {
	      return this.metadata.getColumnLabel(column + 1);
	    } catch (SQLException e) {
	      return e.toString();
	    }
	  }

	  /** Method from interface TableModel; returns the most specific superclass for
	   *  all cell values in the specified column. To keep things simple, all data
	   *  in the table are converted to String objects; hence, this method returns
	   *  the String class.
	   */

	  public Class getColumnClass(int column) {
	    return String.class;
	  }

	  /** Method from interface TableModel; returns the value for the cell specified
	   *  by columnIndex and rowIndex. TableModel uses this method to populate
	   *  itself with data from the row set. SQL starts numbering its rows and
	   *  columns at 1, but TableModel starts at 0.
	   */

	  public Object getValueAt(int rowIndex, int columnIndex) {

	    try {
	      this.coffeesRowSet.absolute(rowIndex + 1);
	      Object o = this.coffeesRowSet.getObject(columnIndex + 1);
	      if (o == null)
	        return null;
	      else
	        return o.toString();
	    } catch (SQLException e) {
	      return e.toString();
	    }
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

	  // Because the sample does not allow users to edit any cells from the
	  // TableModel, the following methods, setValueAt, addTableModelListener,
	  // and removeTableModelListener, do not need to be implemented.

	  public void setValueAt(Object value, int row, int column) {
		  try {
			// move the cursor to row
			this.coffeesRowSet.absolute(row+1);
			// get booking_number given "row"
			int booking_number_thisrow = this.coffeesRowSet.getInt("booking_number");
			System.out.println("booking_number_thisrow = "+booking_number_thisrow);
			// get column name given "column"
			String col_name = ((ResultSetMetaData) this.coffeesRowSet.getMetaData()).getColumnName(column+1);
			System.out.println("col_name = "+col_name);
			
			// set /row updatedrow/ update db
			this.coffeesRowSet.updateRow();
			
			System.out.println("Calling setValueAt row " + row + ", column " + column + " value = "+value);
			 
			fireTableCellUpdated(row, column);
			this.fireTableDataChanged();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }

	  public void addTableModelListener(TableModelListener l) {
	  }

	  public void removeTableModelListener(TableModelListener l) {
	  }

}
