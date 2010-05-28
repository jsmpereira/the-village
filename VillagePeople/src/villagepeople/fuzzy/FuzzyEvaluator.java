package villagepeople.fuzzy;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import nrc.fuzzy.FuzzyRule;
import nrc.fuzzy.FuzzyValue;
import nrc.fuzzy.FuzzyValueVector;
import nrc.fuzzy.FuzzyVariable;
import nrc.fuzzy.TriangleFuzzySet;

/**
 * Fuzzy evaluator for weapon selection.
 *
 * Each weapon has its own fuzzy evaluator where it defines fuzzy variables like
 * distance or desirability and, of course, rules that, for a given set of
 * inputs returns an output indicating the desirability of using that weapon.
 */
public class FuzzyEvaluator {

    /**
     * Adds a variable to the fuzzy evaluator.
     *
     * @param variable The variable to add
     */
    public void addVariable(Variable variable) {
        try {
            FuzzyVariable fuzzyVar = new FuzzyVariable(variable.getName(),
                    variable.getMin(), variable.getMax(), "units");

            for (Term term : variable.getTerms()) {
                fuzzyVar.addTerm(term.getName(), term.getFuzzySet());
            }

            variables.put(fuzzyVar.getName(), fuzzyVar);
        } catch (Exception ex) {
            // Not supposed to happen
            ex.printStackTrace();
        }
    }


    /**
     * Adds a rule to the fuzzy evaluator.
     *
     * @param rule The rule to add
     */
    public void addRule(Rule rule) {
        try {
            // We have to convert from Rule to FuzzyJ's FuzzyRule

            // First, create an empty fuzzy rule
            FuzzyRule fuzzyRule = new FuzzyRule();

            // Then, add each antecedent to the fuzzy rule
            for (Condition condition : rule.getAntecedents()) {
                for (FuzzyVariable variable : this.variables.values()) {
                    if (variable.getName()
                            .equals(condition.getVariableName())) {
                        fuzzyRule.addAntecedent(
                                new FuzzyValue(variable,
                                        condition.getTerm().getName()));
                    }
                }
            }

            // Now add each conclusion to the fuzzy rule
            for (Condition condition : rule.getConclusions()) {
                for (FuzzyVariable variable : this.variables.values()) {
                    if (variable.getName()
                            .equals(condition.getVariableName())) {
                        fuzzyRule.addConclusion(
                                new FuzzyValue(variable,
                                        condition.getTerm().getName()));
                    }
                }
            }

            // Finally, add to the list of rules
            rules.add(fuzzyRule);
        } catch (Exception ex) {
            // Not supposed to happen
            ex.printStackTrace();
        }
    }


    /**
     * Evaluates the rules for some given input and returns the result.
     *
     * @param distance The distance to the target
     * @param ammo The remaining ammo for the weapon
     * @return The desirability to switch to the weapon
     */
    public double evaluate(double distance, int ammo) {
        try {
            final double delta = 0.05;
            FuzzyValue global = null;
            FuzzyValue inputDistance = new FuzzyValue(
                    variables.get("distance"),
                    new TriangleFuzzySet(distance - delta, distance,
                            distance + delta));

            FuzzyValue inputAmmo = new FuzzyValue(
                    variables.get("ammoStatus"),
                    new TriangleFuzzySet(ammo - delta, ammo, ammo + delta));

            // Call each rule and add the result to the global value
            for (FuzzyRule rule : this.rules) {
                rule.removeAllInputs();
                rule.addInput(inputDistance);
                rule.addInput(inputAmmo);

                if (rule.testRuleMatching()) {
                    FuzzyValueVector vector = rule.execute();

                    if (global == null) {
                        global = vector.fuzzyValueAt(0);
                    } else {
                        global = global.fuzzyUnion(vector.fuzzyValueAt(0));
                    }
                }
            }

            // Defuzzify the global result
            return global.momentDefuzzify();
        } catch (Exception ex) {
            // Not supposed to happen
            System.err.println(ammo);
            ex.printStackTrace();
        }

        return 0;
    }


    /**
     * Variables in this fuzzy system.
     */
    private Map<String, FuzzyVariable> variables =
        new HashMap<String, FuzzyVariable>();

    /**
     * Fuzzy rules.
     */
    private List<FuzzyRule> rules = new LinkedList<FuzzyRule>();
}
