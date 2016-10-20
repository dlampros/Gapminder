package resultManager;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import GUI.Chart;
import GUI.panel;


public class createTable extends panel {
	private static final long serialVersionUID = 1L;
	private DefaultTableModel model;
	private JTable table;
	private ArrayList<Integer> updateRows = new ArrayList<Integer>();
	private ArrayList<String> oldValues = new ArrayList<String>();
	private Chart chart;
	
	public createTable(String tablenm, final app myApp, ArrayList<String> selCountries) throws SQLException {
		super();
	
		String q = "SELECT C_DESCR," + tablenm + "_Y_ID," + tablenm + "_DESCR FROM countries," + tablenm + 
						" WHERE C_ID=" + tablenm + "_C_ID";

		showResults(q, myApp);
		
		JTableHeader header = table.getTableHeader();
	    header.setBackground(Color.magenta.darker().darker());
	    header.setForeground(Color.white);
	    
		table.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
				if(oldValues.size() == updateRows.size() && !oldValues.isEmpty())
					oldValues.add(table.getValueAt(table.getSelectedRow(), 2).toString());
				
				if(table.getSelectedRow() > -1){
					if(oldValues.isEmpty())
						oldValues.add(table.getValueAt(table.getSelectedRow(), 2).toString());
					
					int selectedRow = table.getSelectedRow();
					updateRows.add(selectedRow);
				}
			}
		});
		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(table);
		splitPane.setLeftComponent(scrollPane);
		
		createChart(tablenm,selCountries);
	}
	
	public ArrayList<String> getSelectedRows() {
		int nRow = model.getRowCount();
		//int nCol = model.getColumnCount();
		ArrayList<String> selectedCountries = new ArrayList<String>();
		
		for(int i=0; i<nRow; i++)
			if(table.isRowSelected(i) && !selectedCountries.contains(model.getValueAt(i, 0).toString()))
				selectedCountries.add(model.getValueAt(i, 0).toString());
	
		return selectedCountries;
	}

	private void createChart(String tablenm, ArrayList<String> selCountries) {
		int nRow = model.getRowCount();
		//int nCol = model.getColumnCount();

		String countrNm = " ";
		int nYears = 0, nCountries = 0;

		for(int i=0; i<nRow; i++) {
			if(!selCountries.isEmpty()) {
				if(!model.getValueAt(i, 0).toString().matches(countrNm) && selCountries.contains(model.getValueAt(i, 0).toString())) {
					countrNm = model.getValueAt(i, 0).toString();
					nCountries++;
				}
				
				if(selCountries.contains(model.getValueAt(i, 0).toString()) && nCountries == 1) {
					nYears++;
				}
			}
			else {
				if(!model.getValueAt(i, 0).toString().matches(countrNm)) {
					if(nCountries > 4) break;
					countrNm = model.getValueAt(i, 0).toString();
					nCountries++;
				}
				
				if(nCountries == 1) nYears++;
			}
		}		
		
        double data[][] = new double[nCountries][nYears]; 
        String years[] = new String[nYears];
        String countries[] = new String[nCountries];

        countrNm = " ";
		int j = -1;
        
		for(int i=0; i<nRow; i++) {
			if(!selCountries.isEmpty()) {
				
				if(!model.getValueAt(i, 0).toString().matches(countrNm) && selCountries.contains(model.getValueAt(i, 0).toString())){
					//if(j>3) break;
					countrNm = model.getValueAt(i, 0).toString();
					j++;
					countries[j] = countrNm;
				}
				
				if(j == 0 && selCountries.contains(model.getValueAt(i, 0).toString())){
					years[i%nYears] = model.getValueAt(i, 1).toString();
				}				
				
				if(j > -1 && selCountries.contains(model.getValueAt(i, 0).toString())){
					data[j][i%nYears] = Double.parseDouble(model.getValueAt(i, 2).toString());
				}
			}
			else {
				if(!model.getValueAt(i, 0).toString().matches(countrNm)) {
					if(j > 3) break;
					countrNm = model.getValueAt(i, 0).toString();
					j++;
					countries[j] = countrNm;
				}
				
				if(j == 0) years[i] = model.getValueAt(i, 1).toString();
				
				data[j][i%nYears] = Double.parseDouble(model.getValueAt(i, 2).toString());
			}
		}
		
        chart =  new Chart(tablenm, data, countries, years);
        chart.setVisible(true);
		splitPane.setRightComponent(chart);
 	}
	
	protected Vector<String> getNextRow(ResultSet rs, ResultSetMetaData rsmd) throws SQLException {
		Vector<String> currentRow = new Vector<>();
		
		for (int i = 1; i <= rsmd.getColumnCount(); ++i) {
			String str = rs.getString(i);
			
			if (str == null)
				str = "NULL";
			
			currentRow.addElement(str);
		}
		
		return currentRow;
	}
	
	public void insert(String tablenm, final app myApp) {
		insertForm inform = new insertForm();
		inform.setVisible(true);
		
		String cntr = inform.getCountry();
		String yr = inform.getYear();
		String dscr = inform.getDescr();
		
		if(!yr.isEmpty()) {
			String qi = "INSERT INTO " + tablenm + "(" + tablenm + "_C_ID," + tablenm + "_Y_ID," + tablenm + "_DESCR) " + 
						"SELECT C_ID," + yr + "," + dscr + " " +
						"FROM countries WHERE C_DESCR = \"" + cntr + "\""; 
			
			myApp.executeUpdate(qi);
			
			Vector<String> row = new Vector<>();
			row.addElement(cntr);
			row.addElement(yr);
			row.addElement(dscr);
			model.addRow(row);
		}
	}
	
	public void delete(String tablenm, final app myApp) {
		String r = "";
		
		if(table.getSelectedRow() > -1) {
			int selectedRow = table.getSelectedRow();
			
			r = table.getValueAt(selectedRow, 0).toString() + " ";
			r += table.getValueAt(selectedRow, 1).toString() + " ";
		
			String a[] = r.split(" ", 2);
			String qd = "DELETE FROM " + tablenm + 
						" WHERE " + tablenm + "_Y_ID=" + a[1] + " AND " + tablenm + "_C_ID  " + 
						"IN (SELECT C_ID FROM countries WHERE C_DESCR=\"" + a[0] + "\")";
			
			myApp.executeUpdate(qd);
			model.removeRow(selectedRow);
		}
	}
	
	public void delByYear(String tablenm, final app myApp) {
		String r = "";
		
		if(table.getSelectedRow() > -1) {
			int selectedRow = table.getSelectedRow();
			r = table.getValueAt(selectedRow, 1).toString() + " ";
		
			String qd = "DELETE FROM " + tablenm + " WHERE " + tablenm + "_Y_ID=" + r ;
			myApp.executeUpdate(qd);
		}
		
		int rows = table.getRowCount();
		for(int i=0; i<rows; i++) {
			
			if(table.getValueAt(i, 1).toString().matches(r.trim())){
				model.removeRow(i);
				rows--;
			}
		}	
	}
	
	public void selByYear(String tablenm, final app myApp) {		
		String r = "";
		
		if(table.getSelectedRow() > -1) {
			int selectedRow = table.getSelectedRow();
			r = table.getValueAt(selectedRow, 1).toString()+" ";
		
			String q = "SELECT C_descr, " + tablenm + "_Y_ID, " + tablenm + "_DESCR " +
						"FROM " + tablenm + ",countries " +
						"WHERE C_ID= " + tablenm + "_C_ID and " + tablenm + "_Y_ID= " + r ;

			showResults(q, myApp);
		}		
	}
	
	public void delByCountry(String tablenm, final app myApp) {
		String r = "";
		
		if(table.getSelectedRow() > -1){
			int selectedRow = table.getSelectedRow();
		
			r = table.getValueAt(selectedRow, 0).toString() + " ";
			JOptionPane.showMessageDialog(null, r);
		
			String qd = "DELETE FROM " + tablenm + 
						" WHERE " + tablenm + "_C_ID=(SELECT C_ID FROM countries WHERE C_DESCR = \"" + r.trim() + "\")";
			myApp.executeUpdate(qd);
		}
		
		int rows = table.getRowCount();
		for(int i=0; i<rows; i++) {
			if(table.getValueAt(i, 0).toString().matches(r.trim())) {
				model.removeRow(i);
				rows--;
				i--;
			}
		}
	}
	
	public void selByCountry(String tablenm, final app myApp) {
		String r = "";
		if(table.getSelectedRow() > -1){
			int selectedRow = table.getSelectedRow();
			
			r = table.getValueAt(selectedRow, 0).toString() + " ";
			
			String q = "SELECT C_descr, " + tablenm + "_Y_ID, " + tablenm + "_DESCR  FROM " + tablenm + ",countries " + 
						"WHERE C_ID = " + tablenm + "_C_ID AND C_DESCR = \"" + r.trim() + "\"";
			
			showResults(q, myApp);
		}		
	}
	
	public void update(String tablenm, final app myApp) {
		for(int i=0;i<updateRows.size();i++) {
			String qu = "UPDATE " + tablenm + " SET " + tablenm + "_DESCR =" + table.getValueAt(updateRows.get(i), 2) +
					" WHERE "+  tablenm + "_Y_ID =" + table.getValueAt(updateRows.get(i), 1) +
					" AND " + tablenm + "_C_ID  IN (SELECT C_ID FROM countries WHERE C_DESCR=\"" + table.getValueAt(updateRows.get(i), 0) + "\")";
			
			if(!myApp.executeUpdate(qu)) {
				model.setValueAt(oldValues.get(i), updateRows.get(i),2);
				table.setModel(model);
				table.repaint();
			}
		}		
		
		for(int i=0; i<updateRows.size(); i++) {
			updateRows.remove(i);
			--i;
		}
	}
	
	public void avgForOrgByCountry(String organization, String tablenm, final app myApp) {
		String query = "CREATE VIEW view1 AS " +
						"SELECT CO_C_ID FROM cou_org " + 
						"WHERE CO_O_ID = (SELECT O_ID FROM organizations WHERE O_ACRO = \"" + organization + "\")";
		
		String query1 = "CREATE VIEW view2 AS " +
						"SELECT CO_C_ID," + tablenm + "_DESCR," + tablenm + "_Y_ID, C_DESCR " +
						"FROM view1," + tablenm + ", countries " +
						"WHERE CO_C_ID = " + tablenm + "_C_ID and CO_C_ID=C_ID";
		
		String q = "SELECT C_DESCR,AVG(" + tablenm + "_DESCR) " +
					"FROM view2 " +
					"GROUP BY C_DESCR";
		
		String qd1 = "DROP VIEW view1";
		String qd2 = "DROP VIEW view2";
		
		myApp.executeUpdate(query);
		myApp.executeUpdate(query1);
		
		showResults(q, myApp);

		myApp.executeUpdate(qd1);
		myApp.executeUpdate(qd2);
	}
	
	public void cntrGreaterThanAvgOfCont(String continent, String tablenm, final app myApp) {
		String query = "CREATE VIEW view1 AS SELECT C_ID, C_DESCR " +
						"FROM countries, continents " + 
						"WHERE C_CNT_ID = (SELECT CNT_ID FROM continents WHERE CNT_DESCR= \"" + continent + "\")";
	
		String query1 = "CREATE VIEW view2 AS " +
						"SELECT C_ID, C_DESCR, " + tablenm + "_DESCR,AVG(" + tablenm + "_DESCR) AS average " +
						"FROM view1," + tablenm + 
						" WHERE C_ID = " + tablenm + "_C_ID GROUP BY C_DESCR";
		
		String q = "SELECT C_DESCR, average " +
					"FROM view2 " +
					"WHERE average >ALL(SELECT AVG(average) FROM view2)";
		
//		String q = "SELECT  C_DESCR,AVG("+tablenm+"_DESCR"+") "+
//					"FROM view2 "+
//					"where " + tablenm +"_DESCR >all(select AVG("+ tablenm +"_DESCR) from view2)"+
//					"group by C_DESCR";
		
		
		String qd1 = "DROP VIEW view1";
		String qd2 = "DROP VIEW view2";
		
		myApp.executeUpdate(query);
		myApp.executeUpdate(query1);
		
		showResults(q, myApp);

		myApp.executeUpdate(qd1);
		myApp.executeUpdate(qd2);
	}
	
	public void findMinMax(String yr1, String yr2, String tablenm, final app myApp) {
		String q = "SELECT C_DESCR, MIN(" + tablenm + "_DESCR) AS min,MAX(" + tablenm + "_DESCR) AS max" +
					" FROM " + tablenm + ",countries" +
					" WHERE " + tablenm + "_Y_ID>=" + yr1 + " AND " + tablenm + "_Y_ID<=" + yr2 + " AND " + tablenm + "_C_ID = C_ID " +
					"GROUP BY "+ tablenm +"_C_ID";
		
		showResults(q, myApp);
	}
	
	public void getCountriesByOr(String organization, String tabTitle, final app myApp) {
		String q = "SELECT C_DESCR,CNT_DESCR " + 
					"FROM countries,continents,cou_org,organizations " + 
					"WHERE C_CNT_ID=CNT_ID AND CO_C_ID=C_ID AND O_ID=CO_O_ID AND O_ACRO = \"" + organization.trim() + "\" " + 
					"GROUP BY CNT_ID,C_DESCR";
	
		showResults(q, myApp);
	}

	private void showResults(String query, final app myApp) {
		ResultSet res =  myApp.executeQuery(query);

		boolean moreRecords = false;
		try {
			moreRecords = res.next();
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(this, "Sth has gone wrong");
			e.printStackTrace();
		}
		
		if (!moreRecords) JOptionPane.showMessageDialog(this, "No records");
		
		Vector<String> columnHeads = new Vector<>();
		Vector<Vector<String>> rows = new Vector<>();
		try {
			ResultSetMetaData rsmd = res.getMetaData();
			// get column heads
			for (int i = 1; i <= rsmd.getColumnCount(); ++i)
				columnHeads.addElement(rsmd.getColumnName(i));
			// get row data
			do {
				rows.addElement(getNextRow(res, rsmd));
			} while (res.next());
		}
		catch (SQLException sqlex) {
				sqlex.printStackTrace();
		}
		
		model = new DefaultTableModel(rows, columnHeads);
		table.setModel(model);
		table.repaint();
	}
	
}