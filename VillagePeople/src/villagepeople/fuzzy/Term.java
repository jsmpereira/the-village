package villagepeople.fuzzy;

import nrc.fuzzy.FuzzySet;

/**
 * Represents a term in the fuzzy logic DSL.
 *
 * A term is a possible value for a variable. For instance, if the variable is
 * temperature, cold and hot are possible names for its terms.
 */
public class Term {

    /**
     * Creates a new term with the given name and fuzzy set.
     *
     * @param termName The name of the term.
     * @param fuzzySet The fuzzy set of the term.
     */
    public Term(String termName, FuzzySet fuzzySet) {
        this.name = termName;
        this.fuzzySet = fuzzySet;
    }


    /**
     * @return The name of the term.
     */
    public String getName() {
        return name;
    }


    /**
     * @return The fuzzy set of the term.
     */
    public FuzzySet getFuzzySet() {
        return fuzzySet;
    }


    /**
     * The name of the term.
     */
    private String name;

    /**
     * The fuzzy set of the term.
     */
    private FuzzySet fuzzySet;
}
