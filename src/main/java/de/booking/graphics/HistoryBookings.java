package de.booking.graphics;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.booking.database.DatabaseHandler;
import de.booking.model.Booking;
import de.booking.model.Percentages;
import de.booking.service.BookingService;
import de.booking.service.PercentagesService;
import de.booking.toolbox.DataIngest;
import de.booking.toolbox.JTablePdf;
import de.booking.toolbox.OSDetector;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.List;

public class HistoryBookings extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static ConfigurableApplicationContext context;

	private JTable bookingtable;
	private JTextField myTotalAllBookings ;
	private JTextField ehoiTotalAllBookings ;
	private JTextField monthTextField;
	private JTextField yearTextField;
	private float totalAllMonth;
	private Button deleteBooking;
	private Float totalEhoiMonth;

	private DatabaseHandler myDB;

	public HistoryBookings() {

		myDB = new DatabaseHandler();
		context = new ClassPathXmlApplicationContext("applicationContext.xml");

		// across top:  current percentages
		PercentagesService percentagesService = (PercentagesService) context.getBean("percentagesService");

		//Hashtable<String,Float> percentages = myDB.getPercentages();
		Percentages percentages = percentagesService.readPercentages(1);

		//Enumeration<String> keys = percentages.keys();
		JLabel[] columns = new JLabel[percentages.getClass().getDeclaredFields().length];
		JLabel[] values = new JLabel[percentages.getClass().getDeclaredFields().length];

		int i=0;
		for (Field field : percentages.getClass().getDeclaredFields()) {
			field.setAccessible(true); // You might want to set modifier to public first.
			Object value;
			try {
				value = field.get(percentages);
				if (value != null) {
					System.out.println(field.getName() + "=" + value);      
					columns[i] = new JLabel(field.getName());
					values[i] = new JLabel(value.toString());
					i++;
				}
			} catch (IllegalArgumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 

		}

		//Lay out the text controls and the labels.
		JPanel percentages_panel = new JPanel();
		GridBagLayout gridbag = new GridBagLayout();
		//GridBagConstraints c = new GridBagConstraints();
		percentages_panel.setLayout(gridbag);
		addLabelTextRows(columns, values, gridbag, percentages_panel);


		// two edit text boxes for month/year
		JPanel month_panel = new JPanel();
		JLabel monthLabel = new JLabel("Month: ");
		monthLabel.setDisplayedMnemonic(KeyEvent.VK_N);
		monthTextField = new JTextField();
		monthTextField.setColumns(2);
		monthLabel.setLabelFor(monthTextField);
		month_panel.add(monthLabel, BorderLayout.WEST);
		month_panel.add(monthTextField, BorderLayout.CENTER);
		// year label 
		JLabel yearLabel = new JLabel("Year: ");
		monthLabel.setDisplayedMnemonic(KeyEvent.VK_N);
		yearTextField = new JTextField();
		yearTextField.setColumns(4);
		yearLabel.setLabelFor(yearTextField);
		month_panel.add(yearLabel, BorderLayout.WEST);
		month_panel.add(yearTextField, BorderLayout.CENTER);
		// total:
		myTotalAllBookings = new JTextField("  Current Total = 0Eu ");
		myTotalAllBookings.setColumns(15);
		month_panel.add(myTotalAllBookings);
		// next few lines make the JTextField look more like a  JPanel
		myTotalAllBookings.setEditable(false);
		myTotalAllBookings.setOpaque(true);
		myTotalAllBookings.setBorder(null);
		myTotalAllBookings.getDocument().addDocumentListener(new DocumentListener() {
			// this is used to register a litener when the text is updated
			public void removeUpdate(DocumentEvent e) {
				System.out.println("remove");
			}
			public void insertUpdate(DocumentEvent e) {
				System.out.println("insert");
			}
			public void changedUpdate(DocumentEvent e) {
				System.out.println("changed");
			}
		});


		// e-HOI total:
		ehoiTotalAllBookings = new JTextField("  Current Total = 0Eu ");
		ehoiTotalAllBookings.setColumns(15);
		month_panel.add(ehoiTotalAllBookings);
		ehoiTotalAllBookings.setEditable(false);
		ehoiTotalAllBookings.setOpaque(true);
		ehoiTotalAllBookings.setBorder(null);

		// button to calculate total, print results
		Button button1 = new Button();
		button1.setLabel("total");
		button1.addActionListener(new TotalButtonListener());
		month_panel.add(button1);
		Button button2 = new Button();
		button2.setLabel("save to file");
		button2.addActionListener(new SendTable2PDF());
		month_panel.add(button2);
		//Button button3 = new Button();
		//button3.setLabel("ingest CSV file");
		//button3.addActionListener(new IngestCSVaction());
		//month_panel.add(button3);

		// add button to cancel/storno selected booking:
		Button stornoBooking = new Button();
		stornoBooking.setLabel("Storno Selected Booking");
		stornoBooking.addActionListener(new StornoSelectedBookingListener());
		month_panel.add(stornoBooking);

		// create a button to "UN"storno selected Booking
		Button unstornoBooking = new Button();
		unstornoBooking.setLabel("UN storno Selected Booking");
		unstornoBooking.addActionListener(new unStornoSelectedBookingListener());
		month_panel.add(unstornoBooking);

		// create button to delete 
		deleteBooking = new Button();
		deleteBooking.setLabel("Delete Selected Booking");
		deleteBooking.addActionListener(new DeleteSelectedBookingListener());
		month_panel.add(deleteBooking);

		// total display, number of bookings

		// Jtable with bookings for that month/year
		bookingtable = new JTable(); // Displays the table
		bookingtable.setShowGrid(true);
		bookingtable.setShowVerticalLines(true);
		bookingtable.setShowHorizontalLines(true);
		bookingtable.setGridColor(Color.gray);

		setModelinTable(); //myDB.getBookingsEhoiByMonthYear(6, 2012));

		// setup layout
		this.setLayout(new BorderLayout());
		// add components		
		JScrollPane myScrollPane = new JScrollPane(percentages_panel);
		myScrollPane.setPreferredSize(new Dimension(100, 100));
		add(myScrollPane,BorderLayout.NORTH);//,c1);

		this.add(month_panel,BorderLayout.CENTER);

		JScrollPane myScrollPanetable = new JScrollPane(bookingtable);
		myScrollPanetable.setPreferredSize(new Dimension(800, 200) );
		add(myScrollPanetable,BorderLayout.SOUTH);//,c1);
	}

	private void setModelinTable() {
		bookingtable.clearSelection();
		BookingService bookingService = (BookingService) context.getBean("bookingService");
		List<Booking> myBookingList = bookingService.getTopNRows(20);
		BookingTableModel myBookingTableModel;
		myBookingTableModel = new BookingTableModel(myBookingList);
		bookingtable.setModel(myBookingTableModel);	
	}

	@SuppressWarnings("unused")
	private List<Booking> getContentsofBookingTableByMonthYear(int month, int year) {	
		return myDB.getBookingsEhoiByMonthYear(month,year);
	}

	private void addLabelTextRows(JLabel[] keys, JLabel[] values,GridBagLayout gridbag,
			Container container) {
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.FIRST_LINE_START;

		for (int i = 0; i < keys.length; i++) {
			c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last
			c.fill = GridBagConstraints.NONE;      //reset to default
			c.weightx = 0.0;                       //reset to default
			container.add(keys[i], c);

			c.gridwidth = GridBagConstraints.REMAINDER;     //end row
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 1.0;
			container.add(values[i], c);
		}
	}

	class unStornoSelectedBookingListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			// get the selected booking and UN storno it.
			int selectedRow = bookingtable.getSelectedRow();			   

			if (selectedRow != -1) {
				//String booking_number = (String) ((BookingTableModel) 
				//		bookingtable.getModel()).getValueAt(selectedRow, Booking.returnColumnIntGivenName("booking_number"));
				//myDB.UNstornoBooking(booking_number);

				Booking bookingUnstorno = ((BookingTableModel) bookingtable.getModel()).getBookingAtRow(selectedRow);
				// storno :  cancel/storno=1  valid=0
				bookingUnstorno.setStorno(0);
				BookingService bookingService = (BookingService) context.getBean("bookingService");
				bookingService.updateBooking(bookingUnstorno);

				// remake the table
				setModelinTable();
			} else {
				String st="No Booking Selected";
				JOptionPane.showMessageDialog(null,st);
			}

		}
	}

	class StornoSelectedBookingListener implements ActionListener {
		// this class is called by stornoBooking, and cancels/storno selected cruise
		public void actionPerformed(ActionEvent e) {
			int selectedRow = bookingtable.getSelectedRow();

			if (selectedRow != -1) {
				//String booking_number = (String) ((BookingTableModel) 
				//		bookingtable.getModel()).getValueAt(selectedRow, Booking.returnColumnIntGivenName("booking_number"));
				//myDB.stornoBooking(booking_number);

				Booking bookingUnstorno = ((BookingTableModel) bookingtable.getModel()).getBookingAtRow(selectedRow);
				// storno :  cancel/storno=1  valid=0
				bookingUnstorno.setStorno(1);
				BookingService bookingService = (BookingService) context.getBean("bookingService");
				bookingService.updateBooking(bookingUnstorno);

				// remake the table
				setModelinTable();
			} else {
				String st="No Booking Selected";
				JOptionPane.showMessageDialog(null,st);
			}
		}
	}


	class SendTable2PDF implements ActionListener {
		// called when "save to file" button is clicked.  Should get month/year, query the DB , set result
		public void actionPerformed(ActionEvent e) {
			//JtablePdf.createPdf("testoutfile.pdf", bookingtable);
			JTablePdf.createPDF2("BookingMonth.pdf", bookingtable,
					monthTextField.getText().trim(), yearTextField.getText().trim(), 
					totalAllMonth);
		}
	}

	class IngestCSVaction implements ActionListener {
		// when button is clicked ingests a CSV file
		public void actionPerformed(ActionEvent e) {
			//old Mac:  String filename = "/Users/jpizagno/Desktop/JuliWork.csv";
			//windows:  String filename = "C:/Users/juliapizagno/bookings/JuliWork.csv";
			//new Mac:  String filename = "/Users/jim/Desktop/JuliWork.csv";

			String filename = "C:/Users/juliapizagno/bookings/JuliWork.csv"; // default to Windows
			if (OSDetector.isWindows())
			{
				filename = "C:/Users/juliapizagno/bookings/JuliWork.csv";
			}
			if (OSDetector.isMac()){
				filename = "/Users/jpizagno/Desktop/JuliWork.csv";
			}
			DataIngest.IngestCSV(filename);
		}
	}

	public void  updateTable() {
		// get month/year:
		String monthstr = "nothing entered ";
		String yearstr = "nothing entered ";
		Integer month = 0;
		Integer year = 0;
		try {
			// get STring try to convert to int
			monthstr = monthTextField.getText().trim();
			yearstr = yearTextField.getText().trim();
			month = Integer.valueOf(monthstr);
			year = Integer.valueOf(yearstr);
		} catch (ClassCastException myException) {
			String st="could not convert month="+monthstr+" year="+yearstr+" into strings";
			JOptionPane.showMessageDialog(null,st);
			myException.printStackTrace();
		}
		if (myDB.isConnected()) {
			// get these bookings from the DB by month,year
			List<Booking> monthYearResults = myDB.getBookingsEhoiByMonthYear(month, year);
			//getResultSet that is columns: kreuzhaft,flug,hotl,versicherung
			List<Booking> ehoiMonthYearResults = myDB.getBookingsEhoiByMonthYear(month, year);
			setModelinTable(); //monthYearResults);


			// update the total text Field
			totalAllMonth = getTotalAllMonths(monthYearResults);
			totalEhoiMonth = getTotalEHoiMonth(ehoiMonthYearResults);
			String newText = "  Current Total = "+String.valueOf(totalAllMonth)+" ";
			String ehoiNewText = "  Current Total = "+String.valueOf(totalEhoiMonth )+" ";
			myTotalAllBookings.setText(newText);
			ehoiTotalAllBookings.setText(ehoiNewText);
			System.out.println(myTotalAllBookings.getText());

		}
	}

	class DeleteSelectedBookingListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// gets the current booking that is selected and deletes it
			int selectedRow = bookingtable.getSelectedRow();
			if (selectedRow != -1) {

				//String booking_number = (String) ((BookingTableModel) 
				//		bookingtable.getModel()).getValueAt(selectedRow, Booking.returnColumnIntGivenName("booking_number"));
				//myDB.deleteBooking(booking_number);

				Booking bookingUnstorno = ((BookingTableModel) bookingtable.getModel()).getBookingAtRow(selectedRow);
				// storno :  cancel/storno=1  valid=0
				BookingService bookingService = (BookingService) context.getBean("bookingService");
				bookingService.deleteBooking(bookingUnstorno);

				// remake the table
				setModelinTable();
			} else {
				String st="No Row Selected";
				JOptionPane.showMessageDialog(null,st);
			}
		}
	}

	public Float getTotalEHoiMonth(List<Booking> ehoiMonthYearResults){
		// given a result set, sum the kreuzfahrt,flug,hotel,versicherung
		// used to give the ehoi result
		float sum_total = 0;
		float total_thisbooking = 0;

		for (Booking myBooking : ehoiMonthYearResults) {
			total_thisbooking = myBooking.getKreuzfahrt();
			total_thisbooking += myBooking.getFlug();
			total_thisbooking += myBooking.getHotel();
			total_thisbooking += myBooking.getVersicherung();
			sum_total += total_thisbooking;
		}
		return sum_total;

		/*
		try {
			while (ehoiMonthYearResults.next()) {
			    total_thisbooking = ehoiMonthYearResults.getFloat("kreuzfahrt");
			    total_thisbooking = total_thisbooking + ehoiMonthYearResults.getFloat("flug");
			    total_thisbooking = total_thisbooking + ehoiMonthYearResults.getFloat("hotel");
			    total_thisbooking = total_thisbooking + ehoiMonthYearResults.getFloat("versicherung");
			    sum_total += total_thisbooking;
			}
		} catch (SQLException e) {
			System.out.println("HistoryBookings.ehoiMonthYearResults iteration through results of month/year qeuer bombed");
			e.printStackTrace();
		}
		System.out.println("sum_total ehoiMonthYearResults = "+String.valueOf(sum_total));
		try {
			ehoiMonthYearResults.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("HistoryBooking getTotalEHoiMonth ehoiMonthYearResults ERROR");
			e.printStackTrace();
		}
		return sum_total;	
		 */ 
	}

	public float getTotalAllMonths(List<Booking> monthYearResults) {
		// given a result set, sum all of the 'total' column
		float sum_total = 0;
		
		for (Booking myBooking : monthYearResults) {
			sum_total += myBooking.getTotal();
		}
		return sum_total;
		/*
		try {
			while (monthYearResults.next()) {
				float total_thisbooking = monthYearResults.getFloat("total");
				sum_total += total_thisbooking;
			}
		} catch (SQLException e) {
			System.out.println("HistoryBookings.getTotalAllMonths iteration through results of month/year qeuer bombed");
			e.printStackTrace();
		}
		System.out.println("sum_total = "+String.valueOf(sum_total));
		return sum_total;	 
		*/
	}


	class TotalButtonListener implements ActionListener {
		// called when "total" button is clicked.  Should get month/year, query the DB , set result
		public void actionPerformed(ActionEvent e) {
			updateTable();
		}
	}
}