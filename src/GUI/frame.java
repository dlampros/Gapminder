package GUI;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import resultManager.app;
import resultManager.createTable;

public class frame extends JFrame {
	private static final long serialVersionUID = 1L;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private String usrnm = "";
	private String passwd = "";
	private app myApp = new app();
	private String currTable = "";
	private ArrayList<createTable> tables = new ArrayList<createTable>();
	private ArrayList<String> tablenm = new ArrayList<String>();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame frame = new frame();
					frame.setVisible(true);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public frame() {
		setMinimumSize(new Dimension(600, 400));
		setPreferredSize(new Dimension(1100, 600));
		setName("frame1");
		setTitle("GapMinder");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 400);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnDatabase = new JMenu("Database");
		menuBar.add(mnDatabase);
		
		JMenuItem logIn = new JMenuItem("Log In");
		logIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JTextField userField = new JTextField();
				JPasswordField passField = new JPasswordField();
				String message = "Please enter your user name and password.";
				
				int result = JOptionPane.showOptionDialog(null, new Object[] { message,
						userField, passField }, "Login", JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, null, null);

				if (result == JOptionPane.OK_OPTION) {
					usrnm = new String(userField.getText());
					passwd = new String(passField.getPassword());
				}

				myApp.initConnect(usrnm, passwd);
			}
		});
		mnDatabase.add(logIn);
		
		JMenuItem logOut = new JMenuItem("Log Out");
		logOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Quit");
				myApp.closeStmt();
				System.exit(0);
			}
		});
		mnDatabase.add(logOut);
		
		JMenu mnNewMenu = new JMenu("Applications");
		menuBar.add(mnNewMenu);
		
		JMenuItem insert = new JMenuItem("Insert");
		mnNewMenu.add(insert);
		
		JMenuItem delete = new JMenuItem("Delete");
		mnNewMenu.add(delete);
		
		JMenuItem update = new JMenuItem("Update");
		mnNewMenu.add(update);
		
		final JMenuItem show = new JMenuItem("Show Results");
		mnNewMenu.add(show);
		
		JMenuItem showChart = new JMenuItem("Show Chart");
		mnNewMenu.add(showChart);
		
		JMenu mnStats = new JMenu("General Stats");
		menuBar.add(mnStats);
		
		JMenuItem avgOrganCountry = new JMenuItem("Average for Organization by Country");
		mnStats.add(avgOrganCountry);
		
		JMenuItem countriesBiggerAvgYear = new JMenuItem("Countries with stats greater than average of the continent");
		mnStats.add(countriesBiggerAvgYear);
		
		JMenuItem mntmGetCountriesFrom = new JMenuItem("Get countries by organization");
		mnStats.add(mntmGetCountriesFrom);
		
		JMenuItem findMinMax = new JMenuItem("Find min/max by year");
		mnStats.add(findMinMax);
		
		JMenuItem mntmSelectByYear = new JMenuItem("Select by year");
		mnStats.add(mntmSelectByYear);
		
		JMenuItem mntmSelectByCountry = new JMenuItem("Select by country");
		mnStats.add(mntmSelectByCountry);
		
		JMenuItem deleteByYear = new JMenuItem("Delete by year");
		mnStats.add(deleteByYear);
		
		JMenuItem deleteByCountry = new JMenuItem("Delete by country");
		mnStats.add(deleteByCountry);
		
		JMenu mnData = new JMenu("Data");
		menuBar.add(mnData);
		
		final JRadioButton gov_health_sp = new JRadioButton("Government health spending of total gov. spending");
		buttonGroup.add(gov_health_sp);
		gov_health_sp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				currTable = "gov_health_sp";
			}
		});
		mnData.add(gov_health_sp);
		
		JRadioButton gov_health_sp_per_p_inter = new JRadioButton("Government health spending per person (international $)");
		buttonGroup.add(gov_health_sp_per_p_inter);
		gov_health_sp_per_p_inter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currTable = "gov_health_sp_per_p_inter";
			}
		});
		mnData.add(gov_health_sp_per_p_inter);
		
		JRadioButton gov_share_total_health_sp = new JRadioButton("Government share of total health spending (%)");
		buttonGroup.add(gov_share_total_health_sp);
		gov_share_total_health_sp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currTable = "gov_share_total_health_sp";
			}
		});
		mnData.add(gov_share_total_health_sp);
		
		JRadioButton gov_health_sp_per_p_us = new JRadioButton("Government health spending per person (US$)");
		gov_health_sp_per_p_us.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currTable = "gov_health_sp_per_p_us";
			}
		});
		buttonGroup.add(gov_health_sp_per_p_us);
		mnData.add(gov_health_sp_per_p_us);
		
		JRadioButton medical_doctors = new JRadioButton("Medical Doctors (per 1,000 people)");
		medical_doctors.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currTable = "medical_doctors";
			}
		});
		buttonGroup.add(medical_doctors);
		mnData.add(medical_doctors);
		
		JRadioButton oop_share_total_health_sp = new JRadioButton("Out-of-pocket share of total health spending (%)");
		oop_share_total_health_sp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currTable = "oop_share_total_health_sp";
			}
		});
		buttonGroup.add(oop_share_total_health_sp);
		mnData.add(oop_share_total_health_sp);
		
		JRadioButton pri_share_of_total_health_sp = new JRadioButton("Private share of total health spending (%)");
		pri_share_of_total_health_sp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currTable ="pri_share_of_total_health_sp";
			}
		});
		buttonGroup.add(pri_share_of_total_health_sp);
		mnData.add(pri_share_of_total_health_sp);
		
		JRadioButton total_health_sp = new JRadioButton("Total health spending (% of GDP)");
		total_health_sp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currTable ="total_health_sp";
			}
		});
		buttonGroup.add(total_health_sp);
		mnData.add(total_health_sp);
		
		JRadioButton total_health_sp_per_p_inter = new JRadioButton("Total health spending per person (international $)");
		total_health_sp_per_p_inter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currTable ="total_health_sp_per_p_inter";
			}
		});
		buttonGroup.add(total_health_sp_per_p_inter);
		mnData.add(total_health_sp_per_p_inter);
		
		JRadioButton total_health_sp_per_p_us = new JRadioButton("Total health spending per person (US$)");
		total_health_sp_per_p_us.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currTable ="total_health_sp_per_p_us";
			}
		});
		buttonGroup.add(total_health_sp_per_p_us);
		mnData.add(total_health_sp_per_p_us);
		
		final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
		);
		
		findMinMax.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkLength(tabbedPane);
				
				int curTab = tabbedPane.getSelectedIndex();
				String tabTitle = tabbedPane.getTitleAt(curTab).toString();
	
				JTextField yearField = new JTextField();
				JTextField yearField1 = new JTextField();
				String message = "Enter the range of years";
				
				int result = JOptionPane.showOptionDialog(null, new Object[] { message,
						yearField, yearField1 }, "Input", JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, null, null);

				String yr1 = null;
				String yr2 = null;
				
				if (result == JOptionPane.OK_OPTION) {
					yr1 = new String(yearField.getText());
					yr2 = new String(yearField1.getText());
				}
				
				
				
				tables.get(curTab).findMinMax(yr1, yr2, tabTitle, myApp);
			}
		});
		getContentPane().setLayout(groupLayout);
		
		
		mntmGetCountriesFrom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				checkLength(tabbedPane);
				
				int curTab = tabbedPane.getSelectedIndex();
				String tabTitle = tabbedPane.getTitleAt(curTab).toString();
				
				
				String organization = JOptionPane.showInputDialog("Organization");
				
				tables.get(curTab).getCountriesByOr(organization, tabTitle, myApp);
			}
		});
		
		deleteByYear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				checkLength(tabbedPane);
				
				int curTab = tabbedPane.getSelectedIndex();
				String tabTitle = tabbedPane.getTitleAt(curTab).toString();
	
				tables.get(curTab).delByYear(tabTitle, myApp);
			
			}
		});
		
		mntmSelectByYear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkLength(tabbedPane);
				
				int curTab = tabbedPane.getSelectedIndex();
				String tabTitle = tabbedPane.getTitleAt(curTab).toString();
	
				tables.get(curTab).selByYear(tabTitle, myApp);
				
			}
		});
		
		deleteByCountry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkLength(tabbedPane);
				
				int curTab = tabbedPane.getSelectedIndex();
				String tabTitle = tabbedPane.getTitleAt(curTab).toString();
	
				tables.get(curTab).delByCountry(tabTitle, myApp);
			}
		});
		
		mntmSelectByCountry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkLength(tabbedPane);
				
				int curTab = tabbedPane.getSelectedIndex();
				String tabTitle = tabbedPane.getTitleAt(curTab).toString();
	
				tables.get(curTab).selByCountry(tabTitle, myApp);
			}
		});
		
		show.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				checkLength(tabbedPane);

				if(!tablenm.contains(currTable)){

					int curTab = tabbedPane.getTabCount();

					createTable ct = null;
					ArrayList<String> selCountries = new ArrayList<String>();
					
					try {
						ct = new createTable(currTable, myApp, selCountries);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					tables.add(ct);
					tablenm.add(currTable);
					tabbedPane.add(currTable, tables.get(curTab));
					tabbedPane.setTabComponentAt(curTab, new ButtonTabComponent(tabbedPane));
					tabbedPane.setSelectedIndex(curTab);
				}
			}
		});
		
		showChart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				checkLength(tabbedPane);
				
				int curTab = tabbedPane.getSelectedIndex();
				String tabTitle = tabbedPane.getTitleAt(curTab).toString();
				
				
				ArrayList<String> selCountries = tables.get(curTab).getSelectedRows();
				
				
				tabbedPane.remove(curTab);
				checkLength(tabbedPane);
				
				curTab = tabbedPane.getTabCount();

				createTable ct = null;
				try {
					ct = new createTable(tabTitle, myApp, selCountries);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				tables.add(ct);
				tablenm.add(tabTitle);
				tabbedPane.add(tabTitle, tables.get(curTab));
				tabbedPane.setTabComponentAt(curTab, new ButtonTabComponent(tabbedPane));
				tabbedPane.setSelectedIndex(curTab);

			}
		});
		
		insert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkLength(tabbedPane);
				
				int curTab = tabbedPane.getSelectedIndex();
				String tabTitle = tabbedPane.getTitleAt(curTab).toString();

				tables.get(curTab).insert(tabTitle, myApp);
				
				tabbedPane.setSelectedIndex(curTab);
			}
		});
		
		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkLength(tabbedPane);
				
				int curTab = tabbedPane.getSelectedIndex();
				String tabTitle = tabbedPane.getTitleAt(curTab).toString();
		
				tables.get(curTab).delete(tabTitle, myApp);
			
				tabbedPane.setSelectedIndex(curTab);
			}
		});
		
		update.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				checkLength(tabbedPane);
				
				int curTab = tabbedPane.getSelectedIndex();
				String tabTitle = tabbedPane.getTitleAt(curTab).toString();
				
				
				tables.get(curTab).update(tabTitle, myApp);
				
				ArrayList<String> selCountries = new ArrayList<String>();
				
				tabbedPane.remove(curTab);
				checkLength(tabbedPane);
				
				curTab = tabbedPane.getTabCount();
				//String query = "SELECT * FROM "+currTable;
				createTable ct = null;
				try {
					ct = new createTable(tabTitle, myApp, selCountries);
				}
				catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				tables.add(ct);
				tablenm.add(tabTitle);
				tabbedPane.add(tabTitle, tables.get(curTab));
				tabbedPane.setTabComponentAt(curTab, new ButtonTabComponent(tabbedPane));
				tabbedPane.setSelectedIndex(curTab);
			}
		});
		
		
		avgOrganCountry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				checkLength(tabbedPane);
				
				int curTab = tabbedPane.getSelectedIndex();
				String tabTitle = tabbedPane.getTitleAt(curTab).toString();
				
				String organ = JOptionPane.showInputDialog("Organization");
				
				tables.get(curTab).avgForOrgByCountry(organ, tabTitle, myApp);
				
			}
		});
		
		countriesBiggerAvgYear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkLength(tabbedPane);
				
				int curTab = tabbedPane.getSelectedIndex();
				String tabTitle = tabbedPane.getTitleAt(curTab).toString();

				String continent = JOptionPane.showInputDialog("Continent");
				tables.get(curTab).cntrGreaterThanAvgOfCont(continent, tabTitle, myApp);	
			}
		});
		
	}
	
	private void checkLength(JTabbedPane tabPane){
		if(tablenm.size()!=tabPane.getTabCount()) {
			
			for(int i=0; i<tabPane.getTabCount(); i++) {
				if(!tablenm.get(i).contains(tabPane.getTitleAt(i))) {
					tablenm.remove(i);
					tables.remove(i);
					i--;
				}
			}
			
			if(tablenm.size()!=tabPane.getTabCount()) {
				
				for(int i=0; i<tablenm.size(); i++){
					if(i>=tabPane.getTabCount()){
						tablenm.remove(i);
						tables.remove(i);
					}
				}
			}
		}
	}
	
}
