package villagepeople.fuzzy;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a fuzzy variable in the fuzzy rule DSL.
 *
 * A variable has a name, a domain, and a set of terms (cold, hot, etc)
 */
public class Variable {

    /**
     * Creates a new, empty variable.
     *
     * @param name The name of the variable
     * @param min The minimum value the variable can take
     * @param max The maximum value the variable can take
     */
    public Variable(String name, int min, int max) {
        this.name = name;
        this.min = min;
        this.max = max;

        terms = new LinkedList<Term>();
    }


    /**
     * Creates a new condition where the current variable instance takes the
     * value of the given term (i.e., temperature is hot).
     *
     * @param term The value that the variable assumes in the condition.
     * @return The newly created condition.
     */
    public Condition is(Term term) {
        return new Condition(this.name, term);
    }


    /**
     * Adds a new term to the list of terms of this variable.
     *
     * @param term The term to add.
     * @return The term itself.
     */
    public Term addTerm(Term term) {
        terms.add(term);
        return term;
    }


    /**
     * @return The name of the variable.
     */
    public String getName() {
        return name;
    }


    /**
     * @return The minimum value the variable can take.
     */
    public int getMin() {
        return min;
    }


    /**
     * @return The maximum value the variable can take.
     */
    public int getMax() {
        return max;
    }

    /**
     * @return The list of terms of this variable.
     */
    public List<Term> getTerms() {
        return terms;
    }


    /**
     * The name of the variable.
     */
    private String name;

    /**
     * The list of terms of this variable.
     */
    private List<Term> terms;

    /**
     * The minimum value the variable can take.
     */
    private int min;

    /**
     * The maximum value the variable can take.
     */
    private int max;
}
