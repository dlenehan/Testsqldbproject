package testhsqldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;


public class TestDb {

    Connection conn;                                                

    
    public TestDb(String db_file_name_prefix) throws Exception {    
        
        Class.forName("org.hsqldb.jdbcDriver");

        conn = DriverManager.getConnection("jdbc:hsqldb:"
                                           + db_file_name_prefix,    // filenames
                                           "sa",                     // username
                                           "");                      // password
    }

    public void shutdown() throws SQLException {

        Statement st = conn.createStatement();

        
        st.execute("SHUTDOWN");
        conn.close();    
    }


    public synchronized void query(String expression) throws SQLException {

        Statement st = null;
        ResultSet rs = null;

        st = conn.createStatement();         
        
        
        
        rs = st.executeQuery(expression);   
       
        dump(rs);
        st.close();    
        
        
    }


    public synchronized void update(String expression) throws SQLException {

        Statement st = null;

        st = conn.createStatement();    // statements

        int i = st.executeUpdate(expression);    // run the query

        if (i == -1) {
            System.out.println("db error : " + expression);
        }

        st.close();
    }    // void update()

    public static void dump(ResultSet rs) throws SQLException {

        
        ResultSetMetaData meta   = rs.getMetaData();
        int               colmax = meta.getColumnCount();
        int               i;
        Object            o = null;

        
        for (; rs.next(); ) {
            for (i = 0; i < colmax; ++i) {
                o = rs.getObject(i + 1);    // Is SQL the first column is indexed

                // with 1 not 0
                System.out.print(o.toString() + " ");
            }

            System.out.println(" ");
        }
    }                                       //void dump( ResultSet rs )

    public static void main(String[] args) {

        TestDb db = null;

        try {
            db = new TestDb("db_file");  
        } catch (Exception ex1) {
            ex1.printStackTrace();    // could not start db

            return;                   // bye bye
        }

        try {

           
        	
            db.update("DROP TABLE addresses");
            db.update(
                "CREATE TABLE addresses (contact_id "
                + "VARCHAR(5) PRIMARY KEY,"
                + "firstname VARCHAR(10),"
                + " surname VARCHAR(10),"
                + " address_line_1 VARCHAR(20),"
     + "address_line_2 VARCHAR(20),"
     + "address_line_3 VARCHAR(20),"
     + "mobile VARCHAR (10))");	
        } catch (SQLException ex2) {

            //ignore
            //ex2.printStackTrace();  // second time we run program
            //  should throw execption since table
            // already there
            //
            // this will have no effect on the db
        }

        try {

            // add some rows - will create duplicates if run more then once
            // the id column is automatically generated
        	db.update(
                "INSERT INTO addresses(contact_id, firstname, surname, address_line_1, address_line_2, address_line_3, mobile)"
                + " VALUES('1','Deirdre','lenehan','1 the main street','newtown','co dublin','0877601406')");
        	    
        	db.update(
                    "INSERT INTO addresses(contact_id, firstname, surname, address_line_1, address_line_2, address_line_3, mobile)"
                    + " VALUES('2','Deirdre','lenehan','1 the main street','newtown','co dublin','0877601406')");
                
            

            // do a query
            db.query("SELECT * FROM addresses");
	
            // at end of program
            db.shutdown();
        } catch (SQLException ex3) {
            ex3.printStackTrace();
        }
    }    // main()
}    // class Testdb

