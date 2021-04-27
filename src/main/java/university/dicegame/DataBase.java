/*
 * This class is used to connect/disconnect to database
 * and to run operations
 */
package university.dicegame;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author Stanislav Stoychev
 */
public class DataBase {
     // init database constants
    private static final String DATABASE_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/dices";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private static final String MAX_POOL = "250";

    // init connection object
    private Connection connection;
    private String query;
    // init properties object
    private PreparedStatement preparedStmt;
    private PreparedStatement selectStmt;
    private Statement stmt;
    
    public DataBase() {
        this.connect();
    }

    // connect database
    public Connection connect() {
        if (connection == null) {
            try {
                Class.forName(DATABASE_DRIVER);
                this.connection = DriverManager.getConnection(DATABASE_URL,USERNAME,PASSWORD);
            } catch (ClassNotFoundException | SQLException e) {
                System.err.println("Got an exception while connecting!");
                e.printStackTrace();
            }
        }
        return connection;
    }

    // disconnect database
    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                System.err.println("Got an exception while disconnecting!");
                e.printStackTrace();
            }
        }
    }
    
    public void prepareStatementQuery(){
        try {
            query = "INSERT INTO `rolls` "
                    + "("
                    + "`two_value`, `three_value`, `four_value`, `five_value`, `six_value`, `seven_value`, "
                    + "`eight_value`, `nine_value`, `ten_value`, `eleven_value`, `twelve_value`, `date_rolled`"
                    + ") "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
            this.preparedStmt = this.connection.prepareStatement(query);
        } catch(SQLException e) {
            System.err.println("Got an exception while preparing statement!");
            System.err.println(e.getMessage());
        }
    }
    
    public void prepareStatemntQueryValue(int key,int value) {
        try {
            preparedStmt.setInt(key, value);
        } catch(SQLException e) {
            System.err.println("Got an exception while preparing query values!");
            System.err.println(e.getMessage());
        }
    }
    
    public ResultSet getRollsByDate(java.sql.Date date) {
        try {
            String query = "Select * from `rolls` where `date_rolled` = ?";
            selectStmt = connection.prepareStatement(query);
            selectStmt.setDate(1, date);
            ResultSet rs = selectStmt.executeQuery();
            return rs;
        } catch(SQLException e) {
            System.err.println("Got an exception while Selecting!");
            System.err.println(e.getMessage());
        }
        return null;
    }
    
    public ResultSet getDatesAvailable() {
         try {
            String query = "Select DISTINCT(`date_rolled`) from `rolls`";
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            return rs;
        } catch(SQLException e) {
            System.err.println("Got an exception while Selecting!");
            System.err.println(e.getMessage());
        }
        return null;
    }
    
    public void InsertQuery(){
        try{
            java.util.Date utilDate = new java.util.Date();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            preparedStmt.setDate(12,sqlDate);
            preparedStmt.execute();
//            this.disconnect();
        } catch(Exception e) {
            System.err.println("Got an exception while inserting!");
            System.err.println(e.getMessage());
        }
    }
}
