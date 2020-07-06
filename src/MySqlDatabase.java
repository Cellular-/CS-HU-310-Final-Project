import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlDatabase {
    public static Connection getDatabaseConnection() throws SQLException {
    	File envFile = new File(System.getProperty("user.dir") + "\\dev.env");
    	
    	String sw = "";
    	if(envFile.exists()) {
    		sw = "_LOCAL";
    	}
    	 	
        Connection databaseConnection = null;
        int databasePort = Integer.parseInt(System.getenv("MYSQL_PORT" + sw));
        String databaseHost = System.getenv("MYSQL_HOST" + sw);
        String databaseUsername = System.getenv("MYSQL_USERNAME" + sw);
        String databasePassword = System.getenv("MYSQL_PASSWORD" + sw);
        String databaseName = System.getenv("MYSQL_DATABASE" + sw);

        return getDatabaseConnection(databaseUsername, databasePassword, databaseHost, databasePort, databaseName);
    }

    public static Connection getDatabaseConnection(
            String username, String password, String host, int port, String databaseName) throws SQLException{
        String databaseURL = String.format(
                "jdbc:mysql://%s:%s/%s?verifyServerCertificate=false&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true", host, port, databaseName);
        try{
            return DriverManager.getConnection(databaseURL, username, password);
        } catch (SQLException sqlException){
            System.out.println(String.format(
                    "SQLException was thrown while trying to connection to database: %s", databaseURL));
            System.out.println(sqlException.getMessage());
            throw sqlException;
        }
    }
}
