package villagepeople.fuzzy;

import java.util.LinkedList;
import java.util.List;


/**
 * Represents a fuzzy rule.
 *
 * This class includes a small set of methods that allows the creation of rules
 * using a simple DSL (domain specific language), like the following example
 * shows:
 *
 * Rule.when(distance.is(far))
 *     .and(ammoStatus.is(low))
 *     .then(desirability.is(verydesirable)));
 */
public final class Rule {

    /**
     * Creates a new rule with a single antecedent. One should then call and()
     * and then() in order to add more antecedents or conclusions.
     *
     * @param condition The initial antecedent of the rule.
     * @return The newly created rule.
     */
    public static Rule when(Condition condition) {
        Rule rule = new Rule();
        rule.antecedents.add(condition);

        return rule;
    }


    /**
     * Creates a new, empty rule.
     */
    private Rule() {
        this.antecedents = new LinkedList<Condition>();
        this.conclusions = new LinkedList<Condition>();
    }


    /**
     * Appends a new condition to the set of antecedents or conclusions.
     *
     * @param condition The condition to add.
     * @return The modified rule (to allow subsequent calls to and() or then()).
     */
    public Rule and(Condition condition) {
        if (this.conclusions.size() == 0) {
            this.antecedents.add(condition);
        } else {
            this.conclusions.add(condition);
        }

        return this;
    }


    /**
     * Adds a new condition to the conclusions of the rule.
     *
     * @param condition The condition to add.
     * @return The modified rule (to allow subsequent calls to and()).
     */
    public Rule then(Condition condition) {
        this.conclusions.add(condition);
        return this;
    }


    /**
     * @return The list of antecedents for this rule.
     */
    public List<Condition> getAntecedents() {
        return antecedents;
    }


    /**
     * @return The list of conclusions for this rule.
     */
    public List<Condition> getConclusions() {
        return conclusions;
    }


    /**
     * Rule's antecedents.
     */
    private List<Condition> antecedents;

    /**
     * Rule's conclusions.
     */
    private List<Condition> conclusions;
}
