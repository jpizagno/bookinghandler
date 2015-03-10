package de.booking.graphics;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import de.booking.model.Booking;

public class BookingTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private List<Booking> li = new ArrayList<Booking>();
	private Booking myBooking = new Booking();
	private String[] columnNames = new String[myBooking.getClass().getDeclaredFields().length];
	
	public BookingTableModel(List<Booking> list){
		this.li = list;
		Booking myBooking = new Booking();
		Field[] fields = myBooking.getClass().getDeclaredFields();
		for (int fieldint = 0 ; fieldint<fields.length; fieldint++) {
			columnNames[fieldint] = fields[fieldint].getName();
		}
	}

	@Override
	public String getColumnName(int columnIndex){
		return columnNames[columnIndex];
	}

	/**
	 * get the value at rowIndex and columnIndex
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		Booking myBooking = li.get(rowIndex);
		Field[] fields = myBooking.getClass().getDeclaredFields();
		if (columnIndex < 16) {
			return runGetter(fields[columnIndex], myBooking);
		} else {
			return null;
		}
	}
	
	/**
	 * Uses reflection to call getting method
	 * 
	 * @param field that will be get'd:  get{field}
	 * @param booking Booking object
	 * @return
	 */
	public static Object runGetter(Field field, Booking booking)
	{
		@SuppressWarnings("rawtypes")
		Class aClass = booking.getClass();
	    // MZ: Find the correct method
	    for (Method method : aClass.getMethods())
	    {
	        if ((method.getName().startsWith("get")) && (method.getName().length() == (field.getName().length() + 3)))
	        {
	            if (method.getName().toLowerCase().endsWith(field.getName().toLowerCase()))
	            {
	                // MZ: Method found, run it
	                try
	                {
	                    return method.invoke(booking);
	                }
	                catch (IllegalAccessException e)
	                {
	                	e.printStackTrace();
	                    //Logger.fatal("Could not determine method: " + method.getName());
	                }
	                catch (InvocationTargetException e)
	                {
	                	e.printStackTrace();
	                    //Logger.fatal("Could not determine method: " + method.getName());
	                }

	            }
	        }
	    }
	    return null;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex){
		return String.class;
	}

	/**
	 * get Booking at row selectedRow
	 * @param selectedRow
	 * @return
	 */
	public Booking getBookingAtRow(int selectedRow) {
		return (Booking) li.get(selectedRow);
	}
	
	/**
	 * returns number of rows
	 */
	public int getRowCount() {
		return li.size();
	}
        
	/**
	 * returns number of column Names
	 */
	public int getColumnCount() {
		return columnNames.length; 
	}
	
	/**
	 * returns the input list 
	 * @return
	 */
	public List<Booking> getInputList(){
		return li;
	}
}