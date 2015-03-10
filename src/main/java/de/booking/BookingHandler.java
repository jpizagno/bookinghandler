package de.booking;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.booking.graphics.BookingEditor;
import de.booking.graphics.HistoryBookings;

/**
 * This package needs hibernate JAR files to run.
 * A MySQL database is needed.  
 * When running this code, the -classpath needs to point to the hibernate JAR files.
 * 
 * contains main() method.
 * 
 */
public class BookingHandler extends JFrame  {
	
	private static final long serialVersionUID = 1L;
	private static ClassPathXmlApplicationContext context;

	public static void main (String[] args) {		
		BookingHandler myApp = new BookingHandler();
		myApp.setup();
		myApp.go();
		myApp.shutdown();
	}

	/**
	 * Create Swing components
	 * 		create editors
	 * 		entry buttons
	 * 		queries
	 * 		views by month
	 * 		Tell the frame container that you want to use a flow layout.
	 * 		This makes components you add play nicely with one another.
	 * 
	 * */
    private void go() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        

		setLayout(new FlowLayout());

		//JPanel workBookings = new JPanel();
		BookingEditor workBookings = new BookingEditor();
		HistoryBookings viewBookings = new HistoryBookings();
		
		JTabbedPane tabs = new JTabbedPane();
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		
		JScrollPane myScrollPane = new JScrollPane(workBookings);
		Dimension preferredSizeScrollPane = new Dimension();
		preferredSizeScrollPane.height = (int) (height*0.60);
		preferredSizeScrollPane.width = (int) (width*0.60);
		myScrollPane.setPreferredSize(preferredSizeScrollPane );

		tabs.addTab("Current Bookings",  myScrollPane);
		tabs.addTab("History", viewBookings);
		
		/* set the size of the frame so as to ensure that all widgets are visible
		* workBookings.pack();
		* By default, the window is not visible. Make it visible.
		*/
		
		add(tabs);
		setSize((int) (width*0.90), (int) (height*0.75));
		setVisible(true);
	}

	public void setup() {
		this.getContentPane().setBackground( Color.getHSBColor(68, 235, 235) );
		context = new ClassPathXmlApplicationContext("classpath*:**/applicationContext.xml");
	}
	public void shutdown() {
		context.close();
	}

}
