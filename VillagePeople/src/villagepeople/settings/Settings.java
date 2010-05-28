package villagepeople.settings;

import java.io.IOException;

import explicitlib.geometry.Size;
import explicitlib.resources.PropertiesReader;

/**
 * Utility class to manage VillagePeople settings.
 */
public class Settings {

    /** Name of the properties file where all the settings are stored. */
    private static final String PROPERTIES_FILE =
        "config/villagepeople.properties";


    // XXX Remove all this shit.
	public static final Size CELL_SIZE = new Size(32, 32);

	public static final String SPRITE_WALL_PATH = "sprites/wall.gif";
	public static final String SPRITE_TEAM_A_PATH = "sprites/anakin.png";
	public static final String SPRITE_TEAM_B_PATH = "sprites/obiwan.png";

	public static final String SPRITE_LASER_GUN_PATH = "sprites/lasergun.png";
	public static final String SPRITE_LASER_RAY_PATH = "sprites/laser_ray.png";

	public static final String SPRITE_HANDGUN_PATH = "sprites/handgun.png";
	public static final String SPRITE_BULLET_PATH = "sprites/bullet.png";

	public static final String SPRITE_ROCKET_LAUNCHER_PATH = "sprites/rocket.png";

	public static final String SPRITE_HEALTH_PATH = "sprites/health.png";

    /**
     * Reads the properties.
     * Aborts the program if an exception occurs.
     */
    static {
        try {
            reader = new PropertiesReader(PROPERTIES_FILE);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }


    /**
     * Reads an integer from the .properties, but aborts the application
     * if an exception occurs.
     *
     * @param propertyName The name of the property to read from.
     * @return The integer value.
     */
    public static int getInt(String propertyName) {
        try {
            return reader.getInt(propertyName);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }

        return 0;
    }
    
    
    /**
     * Reads a double from the .properties, but aborts the application
     * if an exception occurs.
     *
     * @param propertyName The name of the property to read from.
     * @return The double value.
     */
    public static double getDouble(String propertyName) {
        try {
            return reader.getDouble(propertyName);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }

        return 0;
    }


    /**
     * Reads a boolean from the .properties, but aborts the application
     * if an exception occurs.
     *
     * @param propertyName The name of the property to read from.
     * @return The boolean value.
     */
    public static boolean getBoolean(String propertyName) {
        try {
            return reader.getBoolean(propertyName);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }

        return false;
    }


    /**
     * Reads a string from the .properties, but aborts the application
     * if an exception occurs.
     *
     * @param propertyName The name of the property to read from.
     * @return The string value.
     */
    public static String getString(String propertyName) {
        try {
            return reader.getString(propertyName);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }

        return null;
    }


    /** The properties. */
    private static PropertiesReader reader;
}
