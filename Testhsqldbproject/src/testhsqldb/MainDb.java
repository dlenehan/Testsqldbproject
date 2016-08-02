package testhsqldb;

import java.sql.SQLException;

public class MainDb {
	
	public static void main(String[] args) {

        TestDb db = null;

        try {
            db = new TestDb("db_file");
        } catch (Exception ex1) {
            ex1.printStackTrace();    // could not start db

            return;                   // bye bye
        }

        try {

            //make an empty table
            //
            // by declaring the id column IDENTITY, the db will automatically
            // generate unique values for new rows- useful for row keys
        	
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

            System.out.println("Exception !!!");
        }

        try {

            // add some rows - will create duplicates if run more then once
            // the id column is automatically generated
        	db.update(
                "INSERT INTO addresses(contact_id, firstname, surname, address_line_1, address_line_2, address_line_3, mobile)"
                + " VALUES('1','Joe','Bloggs','3 the main street','newtown','co dublin','0877601406')");
        	    
        	db.update(
                    "INSERT INTO addresses(contact_id, firstname, surname, address_line_1, address_line_2, address_line_3, mobile)"
                    + " VALUES('2','Pete','Quinn','4 the main street','newtown','co dublin','0877601406')");
                
            

            // do a query
            db.query("SELECT * FROM addresses");

            // at end of program
            db.shutdown();
        } catch (SQLException ex3) {
            ex3.printStackTrace();
        }}
    }    // main()