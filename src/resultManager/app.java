package resultManager;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class app {
	 // Connect to the database test, at server localhost 
    static String url="jdbc:mysql://localhost/referencegapminder";
    
    // The connection object. This holds all connection information. Observe: it is static
    public static Connection con;
    public Statement stmt;
    
	public app() {
		con = null;
		stmt = null;
	}

	// The connection creator method
	public void initConnect(String login, String passwd) {
		// Load the Driver
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// Class.forName("org.gjt.mm.mysql.Driver");
		}
		catch (java.lang.ClassNotFoundException e) {
			System.err.print("ClassNotFoundException: ");
		}

		// Connect to the Database
		try {
			con = DriverManager.getConnection(url, login, passwd);
			DatabaseMetaData md = con.getMetaData();
			if (md == null) {
				System.out.println("No Database Meta Data");
			}
			else {
				System.out.println("Database Product Name   : " + md.getDatabaseProductName());
				System.out.println("Allowable active connections: " + md.getMaxConnections());
			}
		}
		catch (SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}
	}
	
    public ResultSet executeQuery(String query) {
        ResultSet res = null;
        try {
            stmt = con.createStatement();
            res = stmt.executeQuery(query);
        }
        catch(SQLException ex) {
            //System.err.println("SQLException: " + ex.getMessage());
        	JOptionPane.showMessageDialog(null, "SQLException: " + ex.getMessage());
        }
        return res;
    }
    
    public boolean executeUpdate(String query) {
        try {
            stmt = con.createStatement();
            stmt.executeUpdate(query);
        }
        catch(SQLException ex) {
            //System.err.println("SQLException: " + ex.getMessage());
            JOptionPane.showMessageDialog(null, "SQLException: " + ex.getMessage());
            return (false);
        }
        return(true);
    }

    public void closeStmt() {
    	try {
			stmt.close();
		}
    	catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
		}
    }
    
}