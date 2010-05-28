package villagepeople.exceptions;

/**
 * Exception thrown when the parsing of some configuration file fails.
 */
public class ParseException extends Exception {

    /**
     * Creates a new ParseException with a specified reason.
     *
     * @param reason The reason that explains why the parsing failed.
     */
    public ParseException(String reason) {
        super(reason);
    }
}
