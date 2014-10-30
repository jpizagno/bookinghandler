package de.booking.graphics;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import de.booking.database.DatabaseHandler;
import BookingModel.Booking;

public class BookingEditor extends JPanel  {
	 private String newline = "\n";
	   protected static final String textFieldString = "Booking name";
	   protected static final String buttonString = "JButton";
	   //protected JLabel actionLabel;
	   protected Button bookButton ;
	   
	   private JLabel[] labels;
	   private JTextField[] textFields;
	   private DatabaseHandler myDB = null;
	   private JTable bookingtable = null;
	   private Button deleteBooking;
	   private Button stornoBooking;
	   private Button clearFields; // clears text fields
	   private Button unstornoBooking;
	   
	public BookingEditor(DatabaseHandler myDBcopy) {
		// connect to DB
		myDB = myDBcopy;
		if (myDB.isConnected()==false){
			String st="BookingEditor NOT connected to Database";
			JOptionPane.showMessageDialog(null,st);
		}
		
        //Create a regular text fields.
        List<JTextField> myBookingFieldsList = new ArrayList<JTextField>();
        String[] myBookingFieldsNames = Booking.returnFieldnames().split(":");
        labels = new JLabel[myBookingFieldsNames.length];
        textFields = new JTextField[myBookingFieldsNames.length];
        for (int fieldint = 0 ; fieldint<myBookingFieldsNames.length; fieldint++) {
        	JTextField textField = new JTextField(10);
        	JLabel textFieldLabel = null;
        	//Create some labels for the fields.
	        if (myBookingFieldsNames[fieldint].equalsIgnoreCase("booking_date") ) {
	        	textFieldLabel = new JLabel(myBookingFieldsNames[fieldint] + " (dd/mm/yyyy): ");
	        } else {
	        	textFieldLabel = new JLabel(myBookingFieldsNames[fieldint] + ": ");
	        }
        	if (myBookingFieldsNames[fieldint].equalsIgnoreCase("total")==true){
        		// then make this field  not editable, and says "automatically:
        		textField.setEditable(false);
        		textField.setText("done automatically");
        		textFieldLabel.setLabelFor(textField);
        	} else {
		        textField.setActionCommand(myBookingFieldsNames[fieldint].replace("_", ""));
		        textFieldLabel.setLabelFor(textField);
        	}
		    labels[fieldint] = textFieldLabel;
		    textFields[fieldint] = textField;
        
		    myBookingFieldsList.add(textField);
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

        // create button to delete 
        deleteBooking = new Button();
        deleteBooking.setLabel("Delete Selected Booking");
        deleteBooking.addActionListener(new DeleteSelectedBookingListener());
        
        // add button to cancel/storno selected booking:
        stornoBooking = new Button();
        stornoBooking.setLabel("Storno Selected Booking");
        stornoBooking.addActionListener(new StornoSelectedBookingListener());
        
        // create a button to clear the fields
        clearFields = new Button();
        clearFields.setLabel("clear fields");
        clearFields.addActionListener(new ClearFieldsListener());
        
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
        bookingtable = new JTable(); // Displays the table
       
        // calling this function fills the table with recent columsn
        setModelinTable();
 
        this.setLayout(new BorderLayout());
        
    
        add(textControlsPane,BorderLayout.WEST); //,c1);

        add(areaScrollPane,BorderLayout.CENTER); //,c1);
        
        JScrollPane myScrollPane = new JScrollPane(bookingtable);
		myScrollPane.setPreferredSize(new Dimension(800, 200) );
        add(myScrollPane,BorderLayout.SOUTH);//,c1);
	}

	 private void setModelinTable() {
		 bookingtable.clearSelection();
		 ResultSet myCachedRowSet = getRecentBookings();
	        BookingTableModel myBookingTableModel;
			try {
				myBookingTableModel = new BookingTableModel(myCachedRowSet);
				bookingtable.setModel(myBookingTableModel);
			} catch (SQLException e) {
				System.out.println("BookingEditor. could not add myCachedRowSet to myBookingTableModel");
				e.printStackTrace();
			}
		
	}

	private ResultSet getRecentBookings() {		 
			 ResultSet rset = null;
			 if (myDB.isConnected()) {
				  //rset = myDB.get5topRows();
				  rset = myDB.getTopRows(20);
			 }
			 return rset;
		}

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
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// get the selected booking and UN storno it.
			int selectedRow = bookingtable.getSelectedRow();
			if (selectedRow != -1) {
				String booking_number = (String) ((BookingTableModel) 
						bookingtable.getModel()).getValueAt(selectedRow, Booking.returnColumnIntGivenName("booking_number"));
				myDB.UNstornoBooking(booking_number);
			
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
				String booking_number = (String) ((BookingTableModel) 
						bookingtable.getModel()).getValueAt(selectedRow, Booking.returnColumnIntGivenName("booking_number"));
				myDB.stornoBooking(booking_number);
			
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
			int selectedRow = bookingtable.getSelectedRow();
			if (selectedRow != -1) {
				
				String booking_number = (String) ((BookingTableModel) 
						bookingtable.getModel()).getValueAt(selectedRow, Booking.returnColumnIntGivenName("booking_number"));
				myDB.deleteBooking(booking_number);
				
				// remake the table
				setModelinTable();
			} else {
				String st="No Row Selected";
				JOptionPane.showMessageDialog(null,st);
			}
		}
	}
	
	
	class ClearFieldsListener implements ActionListener {
		@Override
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
				   if (myDB.isConnected()) {
					   myDB.insertNewBooking(booking2Insert);
					   setModelinTable();
					   //DefaultTableModel tableModel = (DefaultTableModel) bookingtable.getModel();
					   //tableModel.fireTableDataChanged();
					   //bookingtable.updateUI();
				   }
			   }
		  }
		}
}
