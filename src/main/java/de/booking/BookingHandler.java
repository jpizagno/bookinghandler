package de.booking;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.booking.graphics.BookingEditor;
import de.booking.graphics.HistoryBookings;

/**
 * This overall package needs hibernate JAR JAR files to run.
 * A MySQL database is needed.  
 * When running this code, the -classpath needs to point to the hibernate JAR files.
 * 
 * 
 */
public class BookingHandler extends JFrame  {
	
	private static final long serialVersionUID = 1L;
	//private static ConfigurableApplicationContext context;
	private static ClassPathXmlApplicationContext context;
	
	/**
	 * 
	 */
	public static void main (String[] args) {
		BookingHandler myApp = new BookingHandler();
		myApp.setup();
		myApp.go();
		myApp.shutdown();
	}

    private void go() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        

		/*
		 *  Create Swing components
		 * create editors
		 * entry buttons
		 * queries
		 * views by month
		 * Tell the frame container that you want to use a flow layout.
		 * This makes components you add play nicely with one another.
		 * */
		setLayout(new FlowLayout());

		//JPanel workBookings = new JPanel();
		BookingEditor workBookings = new BookingEditor();
		HistoryBookings viewBookings = new HistoryBookings();
		
		JTabbedPane tabs = new JTabbedPane();
		
		JScrollPane myScrollPane = new JScrollPane(workBookings);
		Dimension preferredSize = new Dimension();
		preferredSize.height = 500;
		preferredSize.width = 900;
		myScrollPane.setPreferredSize(preferredSize );

		tabs.addTab("Current Bookings",  myScrollPane);
		tabs.addTab("History", viewBookings);
		
		/* set the size of the frame so as to ensure that all widgets are visible
		* workBookings.pack();
		* By default, the window is not visible. Make it visible.
		*/
		
		add(tabs);
		setSize(950, 600);
		setVisible(true);
	        
	}

	public void setup() {
		context = new ClassPathXmlApplicationContext("classpath*:**/applicationContext.xml");
	}
	public void shutdown() {
		context.close();
	}

}
