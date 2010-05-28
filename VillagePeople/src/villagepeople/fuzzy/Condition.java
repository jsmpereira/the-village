package villagepeople.fuzzy;

/**
 * Represents a condition in a fuzzy rule, like "distance is far".
 */
public class Condition {

    /**
     * Creates a new condition where the variable with the name given by the
     * first parameter has the value given the second parameter.
     *
     * @param variableName The name of the variable in the condition
     * @param term The value of the variable
     */
    public Condition(String variableName, Term term) {
        this.variableName = variableName;
        this.term = term;
    }


    /**
     * @return The name of the variable in the condition
     */
    public String getVariableName() {
        return variableName;
    }


    /**
     * @return The value of the variable
     */
    public Term getTerm() {
        return term;
    }


    /**
     * Fuzzy variable name.
     */
    private String variableName;

    /**
     * The value for the given variable.
     */
    private Term term;
}
