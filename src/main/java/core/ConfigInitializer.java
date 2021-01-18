package core;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.log4testng.Logger;
import utils.db_utils.DBUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigInitializer {
    public static String activeEnv;
    public static Properties properties = new Properties();
    private static Logger logger = Logger.getLogger(ConfigInitializer.class);

    public static Properties initializePropertiesFiles(String filePath) {
        Properties pr = new Properties();
        try {
            logger.info("Loading properties from path : " + filePath);
            FileInputStream fis = new FileInputStream(filePath);
            pr.load(fis);
            logger.info("Properties initialised");
        } catch (Exception e) {
            logger.error("Unable to initialise property file!\n " + e.getStackTrace());
        }
        return pr;
    }

    private void loadEnvProperties() throws IOException {
        Properties env = new Properties();
        env.load(getClass().getResourceAsStream("/env.properties"));
        activeEnv = env.getProperty("env_to_use");
        logger.info("Active env: " + activeEnv);
        properties =
                initializePropertiesFiles(new File(System.getProperty("user.dir")).getAbsolutePath()
                        + "/configurations/environment/"
                        + activeEnv
                        + ".properties");
        logger.info("Properties loaded");
    }

    @BeforeSuite(alwaysRun = true)
    public void initialize() throws Exception {
        loadEnvProperties();
    }

    @AfterSuite(alwaysRun = true)
    public void deInitAll() {
        DBUtils.closeDbConnections();
        logger.info("Suite tearDown Successful");
    }
}
