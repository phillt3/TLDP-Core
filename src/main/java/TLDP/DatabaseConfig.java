package src.main.java.TLDP;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DatabaseConfig {

    private static final String CONFIG_FILE_PATH = "config.properties";
    private static Properties properties = new Properties();

    static {
        try {
            // Load configuration from the file
            FileInputStream fileInputStream = new FileInputStream(CONFIG_FILE_PATH);
            properties.load(fileInputStream);
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getJDBCUrl() {
        return properties.getProperty("jdbc.url");
    }

}
