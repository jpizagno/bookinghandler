package de.booking;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.booking.dao.BookingDAO;
import de.booking.graphics.BookingEditor;
import de.booking.graphics.HistoryBookings;
import de.booking.model.Booking;
import de.booking.service.BookingService;

/**
 * This overall package needs Connector/J and itext JAR files to run.
 * A MySQL database is needed, and hence Connector/J.  
 * See DBBackend/DatabaseHandler to see how MySQL tables and triggers are setup.
 * When running this code, the -classpath needs to point to the connector/J and itextpdf JAR files.
 * 
 * 
 */
public class BookingHandler extends JFrame  {
	
	private static ConfigurableApplicationContext context;
	
	public static void main (String[] args) {
		BookingHandler myApp = new BookingHandler();
		myApp.setup();
		myApp.go();
		myApp.shutdown();
	}
	
    private void go() {
        // start whole program
        String myDefaults = null;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        
        // Check to see if Database is up
        // if(DB up get old values and load)
        myDB = new DatabaseHandler();
        if(myDB.connect()) {
                // save database to disk
                myDB.exportOUTFILEonStartUp();
                
                // get defeaults
                myDefaults = myDB.getDefaults();
                String st="Connected to Database";
                JOptionPane.showMessageDialog(null,st);
                //String innodbStatus = myDB.getInnodbStatus();
                //JOptionPane.showMessageDialog(null,innodbStatus);
        } else {
            myDefaults = "Could not connect to MySQL database.  Booking Handler" +
                    " needs this to save data.";
            JOptionPane.showMessageDialog(null,myDefaults);
        }



		// Create Swing components
		// create editors
		// entry buttons
		// queries
		// views by month
		//Tell the frame container that you want to use a flow layout.
		//This makes components you add play nicely with one another.
		setLayout(new FlowLayout());

		//JPanel workBookings = new JPanel();
		BookingEditor workBookings = new BookingEditor(myDB);
		HistoryBookings viewBookings = new HistoryBookings(myDB);
		
		JTabbedPane tabs = new JTabbedPane();
		
		
		JScrollPane myScrollPane = new JScrollPane(workBookings);
		Dimension preferredSize = new Dimension();
		preferredSize.height = 500;
		preferredSize.width = 900;
		myScrollPane.setPreferredSize(preferredSize );

		tabs.addTab("Current Bookings",  myScrollPane);
		tabs.addTab("History", viewBookings);
		
		// set the size of the frame so as to ensure that all widgets are visible
		//workBookings.pack();
		//By default, the window is not visible. Make it visible.
		
		add(tabs);
		setSize(950, 600);
		setVisible(true);
	        
	}

	public void setup() {
		System.out.println("load context");
		context = new ClassPathXmlApplicationContext("applicationContext.xml");
	}
	public void shutdown() {
		context.close();
	}

}
