package viglaglepeopgle.game;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.lwjgl.LWJGLException;

import villagepeople.entities.Player;
import villagepeople.game.VillageGame;
import villagepeople.navigation.Map;
import villagepeople.navigation.Parser;


/**
 * The startup class.
 *
 * This is also the place where the command line arguments are parsed.
 */
public final class Main {

    /**
     * The main!
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        PropertyConfigurator.configure("log4j.properties");

        Main main = new Main();

        if (!main.parseOptions(args)) {
            System.exit(1);
        }

        Map map = null;
        try {
            map = new Parser().parse(main.mapFile);
        } catch (Exception e) {
            logger.error("Error parsing the Map file. Exiting.");
            e.printStackTrace();
            System.exit(1);
        }

        VillageGame game = new VillageGame(map);
        try {
            Viewer viewer = new Viewer();
            Controller controller = new Controller(viewer, game);
            viewer.run();
        } catch (LWJGLException e) {
            logger.error("Error initializing the display: " + e);
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Empty constructor.
     */
    private Main() {

    }

    /**
     * Parses the command line options using the args4j library.
     * Prints the usage in case of error.
     *
     * @param args The command line arguments
     * @return True if the parsing was successful, false otherwise.
     */
    private boolean parseOptions(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);

        // Console width = 80 characters
        parser.setUsageWidth(80);

        try {
            // Parse the arguments.
            parser.parseArgument(args);

            // Map file is required!
            if (mapFile.isEmpty()) {
                throw new CmdLineException("No map specified");
            }
        } catch (CmdLineException e) {
            // Print usage
            System.err.println(e.getMessage());
            System.err.println("java VillagePeople.Game [options...] "
                    + "arguments...");
            parser.printUsage(System.err);
            System.err.println();

            return false;
        }

        return true;
    }


    /** Path to the Map file to use. */
    @Option(name = "-m", usage = "Path of the map file to use")
    private String mapFile = "";


    /** Class logger. */
    private static Logger logger = Logger.getLogger(Parser.class.getName());
}

