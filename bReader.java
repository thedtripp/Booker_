import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
import java.sql.SQLException;

public class bReader {
    public static void main(String[] args) throws Exception {
        if (!checkArgs(args)) { return; }
        Connection conn = null;
        Statement stmt = null;
        PreparedStatement temp = null;
        try {
            SimpleDataSource.init(args[0]);
            conn = SimpleDataSource.getConnection();

            //SET UP:
            createDb(conn, args[0]);
            createTables(conn);
            showTables(conn);

            //NORMAL OPERATION:
            //MENU:
            //ENTER USER
            //ENTER BOOK
            //UPDATE PROGRESS
            //DISPLAY PROGRESS
            menu(conn, stmt, temp);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            //e.printStackTrace();
        }
    }

    //function 1
    public static void listBooks(Connection conn, Statement stmt) throws SQLException {
        stmt = conn.createStatement();
        String query = "SELECT book_id AS id, substr(book_title, 0, 32) AS title, chapters AS c, pages AS p FROM BOOKS";
        ResultSet rs = stmt.executeQuery(query);
        printResultSet(rs);
        rs.close();
    }

    //function 2
    public static void listUsers(Connection conn, Statement stmt) throws SQLException {
        stmt = conn.createStatement();
        String query = "SELECT * FROM Users ORDER BY username";
        ResultSet rs = stmt.executeQuery(query);
        printResultSet(rs);
        rs.close();
    }

    //function 3
    public static void listUBAssociations(Connection conn, Statement stmt) throws SQLException {
        stmt = conn.createStatement();
        String query = "select substr(username || '          ', 1, 10) AS name, substr(book_title, 1, 32) AS title, progress AS pro, current_chapter AS c from UBASSOCIATIONS natural join books natural join users";
        ResultSet rs = stmt.executeQuery(query);
        printResultSet(rs);
        rs.close();
    }

    //PRINT RESULT SET
    public static void printResultSet (ResultSet rs) throws SQLException {
        int i = 1;
        while (rs.next()) {
            //System.out.print(i + ": ");
            printTuple(rs);
            System.out.println();
            i++;
        }
    }

    //HELPER FUNCTION OF PRINT RESULT SET
    public static void printTuple (ResultSet rs) throws SQLException {
        for ( int i = 1; i <= rs.getMetaData().getColumnCount(); i++ ){
            System.out.print("[" + rs.getMetaData().getColumnName(i) + "]: " + rs.getString(rs.getMetaData().getColumnName(i)) + "\t" );
        }
    }

    public static void menu(Connection conn, Statement stmt, PreparedStatement temp) throws SQLException, Exception{
        int menuOption = 0;
        System.out.println(menuText());
        menuOption = getUserInt(1, 10, "Please select an option:");

        switch (menuOption) {
            case 1:
                //List books
                listBooks(conn, stmt);
                menu(conn, stmt, temp);
                break;
            case 2:
                //List users
                listUsers(conn, stmt);
                menu(conn, stmt, temp);
                break;
            case 3:
                //List ubassociations
                listUBAssociations(conn, stmt);
                menu(conn, stmt, temp);
                break;
            case 4:
                //Insert new book
                menu(conn, stmt, temp);
                break;
            case 5:
                //Insert new user
                menu(conn, stmt, temp);
                break;
            case 6:
                //Insert new ubassociation
                menu(conn, stmt, temp);
                break;
            case 7:
                //Update ubassociations
                menu(conn, stmt, temp);
                break;
            case 10:
                System.out.println("Exiting Menu.");
                break;
            default:
                System.out.println("Invalid option.");
                menu(conn, stmt, temp);        
        }
    }

    public static String menuText() {
        return "Menu:\n"
        + "Please select an option:\n"
        + "1. List all books.\n"
        + "2. List all users.\n"
        + "3. List all ubassociations.\n"
        + "4. Insert new book.\n"
        + "5. Insert new user.\n"
        + "6. Insert new ubassociation.\n"
        + "7. Update ubassociation.\n"
        + "8. List your books.\n"
        + "9. \n"
        + "10. Exit.";
    }

    // //doesn't work.
    // //todo use prepared statement?
    // public static void dropTable(Connection conn, String tableName) throws Exception {
    //     Statement stat = conn.createStatement();
    //     if (checkIfTableExists(conn, tableName)) {
    //         stat.execute("DROP TABLE " + tableName + ";");
    //         System.out.println("Dropped Table: " + tableName + ".");
    //     } else {
    //         System.out.println("Table: " + tableName + " NOT EXISTS. NOTHING TO DO.");
    //     }
    // }

    public static boolean checkIfTableExists(Connection conn, String tableName) throws Exception {
        DatabaseMetaData dbm = conn.getMetaData();
        // check if "employee" table is there
        ResultSet tables = dbm.getTables(null, null, tableName, null);
        if (tables.next()) {
            return true;
        }
        return false;
    }

    public static void showTables(Connection conn) throws Exception {
        ResultSet rs = conn.getMetaData().getTables(null, null, null, null);
        while (rs.next()) {
            System.out.println(rs.getString("TABLE_NAME"));
        } 
    }

    public static void createTables(Connection conn) throws Exception {
            System.out.println("Creating tables...");
            Statement stat = conn.createStatement();
            if (!checkIfTableExists(conn, "BOOKS")) {
                stat.execute("CREATE TABLE BOOKS (" +
                    "book_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "ISBN varchar(20) NOT NULL," +
                    "book_title varchar(255) NOT NULL," +
                    "book_author varchar(255) NOT NULL," +
                    "chapters int NOT NULL," +
                    "pages int NOT NULL" +
                ");");
            } else {System.out.println("Table: 'BOOKS' already exists.");}
            if (!checkIfTableExists(conn, "USERS")) {
                stat.execute("CREATE TABLE USERS (" +
                    "user_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username varchar(100) NOT NULL" +
                ");");
            }else {System.out.println("Table: 'USERS' already exists.");}
            if (!checkIfTableExists(conn, "UBASSOCIATIONS")) {
                stat.execute("CREATE TABLE UBASSOCIATIONS (" +
                    "book_id int NOT NULL," +
                    "user_id int NOT NULL," +
                    "progress int NOT NULL," +
                    "current_chapter int NOT NULL," +
                    "PRIMARY KEY (book_id, user_id)," +
                    "FOREIGN KEY (book_id) REFERENCES BOOKS(book_id)," +
                    "FOREIGN KEY (user_id) REFERENCES USERS(user_id)" +
                ");");
            }else {System.out.println("Table: 'UBASSOCIATIONS' already exists.");}

        
    }

    public static boolean checkArgs(String[] args) {
        if (args.length == 0) {
            System.out.println(
            "Usage: java -classpath driver_class_path"
            + File.pathSeparator
            + ". TestDB propertiesFile");
            return false;
        }
        return true;
    }

    public static void createDb(Connection conn, String s) throws Exception {
        
        Statement stat = conn.createStatement();
        stat.execute("CREATE TABLE Test (Name VARCHAR(20))");
        stat.execute("INSERT INTO Test VALUES ('Romeo')");
        ResultSet result = stat.executeQuery("SELECT * FROM Test");
        result.next();
        System.out.println(result.getString("Name"));
        stat.execute("DROP TABLE Test");
    }
    // PROMPT USER TO ENTER A NUMBER WITHIN A RANGE. LOOPS UNTIL USER GETS IT RIGHT (ENTERS ANY INT).
    public static int getUserInt(int lowBound, int upBound, String message) {
        System.out.println( message + " ( " + lowBound + " - " + upBound + " ).");
        Scanner input = new Scanner(System.in);
        String line = input.nextLine();
        int choice = 0;
        try {
            choice = Integer.parseInt(line);
            if (choice >= lowBound && choice <= upBound) { //choice is within acceptable range
                return choice;
            } else {
                System.out.println("Number: " + choice + " outside of range. Try again.");
                choice = getUserInt(lowBound, upBound, message);
            }
        } catch (NumberFormatException e) {
            System.out.println("You've caused a number format exception. Try again.");
            choice = getUserInt(lowBound, upBound, message);
        }
        return choice;
    }

    public static String getUserString(String message) throws SQLException{
        Scanner input = new Scanner(System.in);
        System.out.println( message );
        String str = input.nextLine();
        return str;
    }
}