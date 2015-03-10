package de.booking.graphics;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.booking.model.Booking;
import de.booking.service.BookingService;

public class BookingEditor extends JPanel  {

	private static final long serialVersionUID = 1L;

	private static ConfigurableApplicationContext context;

	protected static final String textFieldString = "Booking name";
	protected static final String buttonString = "JButton";
	protected Button bookButton ;

	private JLabel[] labels;
	private JTextField[] textFields;
	private JTable bookingTable = null;
	private Button deleteBooking;
	private Button stornoBooking;
	private Button clearFields; // clears text fields
	private Button unstornoBooking;

	@Autowired
	private BookingService bookingService;

	public BookingEditor() {
		// connect to DB
		context = new ClassPathXmlApplicationContext("classpath*:**/applicationContext.xml");
		JOptionPane.showMessageDialog(null,"starting Booking Editor");

		Booking myBooking = new Booking();
		Field[] fields = myBooking.getClass().getDeclaredFields();

		// here we want to exclude id(0) and updated_time(15) from being displayed
		// remove id,comment, updated_time from fields
		// better to recreate fields without these
		Field[] fields_tmp = new Field[fields.length - 3];
		fields_tmp[0] = fields[1];
		Field[] tmp = Arrays.copyOfRange(fields, 2, 13);
		for (int i=0;i<tmp.length;i++){
			fields_tmp[i+1] = tmp[i];
		}
		fields_tmp[12] = fields[14];
		fields = fields_tmp;


		labels = new JLabel[fields.length];
		textFields = new JTextField[fields.length];
		for (int fieldint = 0 ; fieldint<fields.length; fieldint++) {
			JTextField textField = new JTextField(10);
			JLabel textFieldLabel = null;
			//Create some labels for the fields.
			if (fields[fieldint].getName().equalsIgnoreCase("booking_date") ) {
				textFieldLabel = new JLabel(fields[fieldint].getName() + " (dd/mm/yyyy): ");
			} else {
				textFieldLabel = new JLabel(fields[fieldint].getName() + ": ");
			}
			if (fields[fieldint].getName().equalsIgnoreCase("total")==true){
				// then make this field  not editable, and says "automatically:
				textField.setEditable(false);
				textField.setText("done automatically");
				textFieldLabel.setLabelFor(textField);
			} else {
				textField.setActionCommand(fields[fieldint].getName().replace("_", ""));
				textFieldLabel.setLabelFor(textField);
			}
			labels[fieldint] = textFieldLabel;
			textFields[fieldint] = textField;

			//myBookingFieldsList.add(textField);
		}
		//Lay out the text controls and the labels.
		JPanel textControlsPane = new JPanel();
		GridBagLayout gridbag = new GridBagLayout();
		//GridBagConstraints c = new GridBagConstraints();
		textControlsPane.setLayout(gridbag);
		addLabelTextRows(labels, textFields, gridbag, textControlsPane);


		////Create the button to process entered booking
		bookButton = new Button("book this cruise");
		bookButton.addActionListener(new FieldsButtonListener());
		bookButton.setForeground( Color.BLUE );

		// create button to delete 
		deleteBooking = new Button();
		deleteBooking.setLabel("Delete Selected Booking");
		deleteBooking.addActionListener(new DeleteSelectedBookingListener());
		deleteBooking.setForeground( Color.red );

		// add button to cancel/storno selected booking:
		stornoBooking = new Button();
		stornoBooking.setLabel("Storno Selected Booking");
		stornoBooking.addActionListener(new StornoSelectedBookingListener());
		stornoBooking.setForeground( Color.red );

		// create a button to clear the fields
		clearFields = new Button();
		clearFields.setLabel("clear fields");
		clearFields.addActionListener(new ClearFieldsListener());
		clearFields.setForeground( Color.gray );

		// create a button to "UN"storno selected Booking
		unstornoBooking = new Button();
		unstornoBooking.setLabel("UN storno Selected Booking");
		unstornoBooking.addActionListener(new unStornoSelectedBookingListener());

		//c.gridwidth = GridBagConstraints.REMAINDER; //last
		//c.anchor = GridBagConstraints.NORTH;
		//c.weightx = 1.0;
		textControlsPane.add(bookButton);//, c); //actionLabel, c);
		textControlsPane.add(deleteBooking);
		textControlsPane.add(stornoBooking);
		textControlsPane.add(clearFields);
		textControlsPane.add(unstornoBooking);
		textControlsPane.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createTitledBorder("Text Fields"),
						BorderFactory.createEmptyBorder(5,5,5,5)));


		//Create a text area.
		// make this a table later
		JTextArea textArea = new JTextArea(
				"Hey, Julia, enter any information here, and it will be saved with the booking."
				);
		textArea.setFont(new Font("Serif", Font.ITALIC, 16));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		JScrollPane areaScrollPane = new JScrollPane(textArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		areaScrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		areaScrollPane.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createCompoundBorder(
								BorderFactory.createTitledBorder("Comment on this booking"),
								BorderFactory.createEmptyBorder(5,5,5,5)),
								areaScrollPane.getBorder()));


		// create the BookingTableModel, enter data, set listeneers  
		bookingTable = new JTable(); // Displays the table

		// calling this function fills the table with recent columsn
		setModelinTable();

		this.setLayout(new BorderLayout());
		add(textControlsPane,BorderLayout.WEST); //,c1);
		add(areaScrollPane,BorderLayout.CENTER); //,c1);

		JScrollPane myScrollPane = new JScrollPane(bookingTable);
		myScrollPane.setPreferredSize(new Dimension(800, 200) );
		add(myScrollPane,BorderLayout.SOUTH);//,c1);
	}

	/**
	 * 
	 */
	private void setModelinTable()  {
		bookingTable.clearSelection();
		List<Booking> myBookingList = getRecentBookings();
		BookingTableModel myBookingTableModel;
		myBookingTableModel = new BookingTableModel(myBookingList);
		bookingTable.setModel(myBookingTableModel);
	}

	/**
	 *  Gets most recent (By updated_time DESC) bookings
	 * @return  List<Booking>
	 */
	private List<Booking> getRecentBookings() {		
		return ((BookingService) context.getBean("bookingService")).getTopNRows(20);
	}

	/**
	 * Adds the labels, given textFields, and Swing gridbag and Container
	 * 
	 * @param labels
	 * @param textFields
	 * @param gridbag
	 * @param container
	 */
	private void addLabelTextRows(JLabel[] labels, JTextField[] textFields,GridBagLayout gridbag,
			Container container) {
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.FIRST_LINE_START;

		for (int i = 0; i < labels.length; i++) {
			c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last
			c.fill = GridBagConstraints.NONE;      //reset to default
			c.weightx = 0.0;                       //reset to default
			container.add(labels[i], c);

			c.gridwidth = GridBagConstraints.REMAINDER;     //end row
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 1.0;
			container.add(textFields[i], c);
		}
	}

	class unStornoSelectedBookingListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			// get the selected booking and UN storno it.
			int selectedRow = bookingTable.getSelectedRow();			   

			if (selectedRow != -1) {
				Booking bookingUnstorno = ((BookingTableModel) bookingTable.getModel()).getBookingAtRow(selectedRow);
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
			int selectedRow = bookingTable.getSelectedRow();

			if (selectedRow != -1) {
				Booking bookingUnstorno = ((BookingTableModel) bookingTable.getModel()).getBookingAtRow(selectedRow);
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

	class DeleteSelectedBookingListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// gets the current booking that is selected and deletes it
			int selectedRow = bookingTable.getSelectedRow();
			if (selectedRow != -1) {
				Booking bookingUnstorno = ((BookingTableModel) bookingTable.getModel()).getBookingAtRow(selectedRow);
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


	class ClearFieldsListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			// clear the fields
			for (int i=0; i<textFields.length; i++) {
				textFields[i].setText("");
			}
		}

	}

	class FieldsButtonListener implements ActionListener {
		Booking booking2Insert;
		FieldsButtonListener() {
			// create the booking to be inserted
			booking2Insert = new Booking();
		}

		public void actionPerformed(ActionEvent e) {
			int result = booking2Insert.setAllUserFields(labels,textFields);
			// send Booking to Database
			if (result==0){
				//myDB.insertNewBooking(booking2Insert);
				BookingService bookingService = (BookingService) context.getBean("bookingService");
				bookingService.insertNewBookingCalcTotal(booking2Insert);

				setModelinTable();
			}
		}
	}
}
