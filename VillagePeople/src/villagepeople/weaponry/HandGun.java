package villagepeople.weaponry;

import nrc.fuzzy.TrapezoidFuzzySet;
import nrc.fuzzy.TriangleFuzzySet;
import villagepeople.entities.Bot;
import villagepeople.fuzzy.Rule;
import villagepeople.fuzzy.Term;
import villagepeople.fuzzy.Variable;
import villagepeople.settings.Settings;

/**
 * A handgun is a weapon that fires bullets.
 */
public class HandGun extends Weapon {


    /** Maximum ammo. */
    private static final int MAX_AMMO =
        Settings.getInt("weapons.handgun.maxAmmo");

    /** Initial ammo. */
    private static final int INITIAL_AMMO =
        Settings.getInt("weapons.handgun.initialAmmo");

    /** Fire rate. */
    private static final int FIRE_RATE =
        Settings.getInt("weapons.handgun.fireRate");


    /**
     * {@inheritDoc}
     *
     * @param agent {@inheritDoc}
     */
    public HandGun(Bot agent) {
        super(agent);

        try {
            // Build rules for the desirability calculation
            Variable distance = new Variable("distance", 0, 50);
            Term close = distance.addTerm(
                    new Term("close", new TrapezoidFuzzySet(0, 0, 1, 4)));
            Term medium = distance.addTerm(
                    new Term("medium", new TriangleFuzzySet(1, 4, 8)));
            Term far = distance.addTerm(
                    new Term("far", new TrapezoidFuzzySet(4, 8, 50, 50)));

            Variable ammoStatus = new Variable("ammoStatus", -1, 121);
            Term low = ammoStatus.addTerm(
                    new Term("low", new TrapezoidFuzzySet(0, 0, 0, 30)));
            Term ok = ammoStatus.addTerm(
                    new Term("ok", new TriangleFuzzySet(0, 30, 90)));
            Term loads = ammoStatus.addTerm(
                    new Term("loads", new TrapezoidFuzzySet(30, 90, 121, 121)));

            Variable desirability = new Variable("desirability", 0, 100);
            Term undesirable = desirability.addTerm(
                    new Term("undesirable",
                            new TrapezoidFuzzySet(0, 0, 25, 50)));
            Term desirable = desirability.addTerm(
                    new Term("desirable", new TriangleFuzzySet(25, 50, 75)));
            Term verydesirable = desirability.addTerm(
                    new Term("verydesirable",
                            new TrapezoidFuzzySet(50, 75, 100, 100)));

            this.fuzzyEvaluator.addVariable(distance);
            this.fuzzyEvaluator.addVariable(ammoStatus);
            this.fuzzyEvaluator.addVariable(desirability);

            this.fuzzyEvaluator.addRule(
                    Rule.when(distance.is(far))
                        .and(ammoStatus.is(loads))
                        .then(desirability.is(verydesirable)));

            this.fuzzyEvaluator.addRule(
                    Rule.when(distance.is(far))
                        .and(ammoStatus.is(ok))
                        .then(desirability.is(verydesirable)));

            this.fuzzyEvaluator.addRule(
                    Rule.when(distance.is(far))
                        .and(ammoStatus.is(low))
                        .then(desirability.is(verydesirable)));

            this.fuzzyEvaluator.addRule(
                    Rule.when(distance.is(medium))
                        .and(ammoStatus.is(loads))
                        .then(desirability.is(verydesirable)));

            this.fuzzyEvaluator.addRule(
                    Rule.when(distance.is(medium))
                        .and(ammoStatus.is(ok))
                        .then(desirability.is(desirable)));

            this.fuzzyEvaluator.addRule(
                    Rule.when(distance.is(medium))
                        .and(ammoStatus.is(low))
                        .then(desirability.is(undesirable)));

            this.fuzzyEvaluator.addRule(
                    Rule.when(distance.is(close))
                        .and(ammoStatus.is(loads))
                        .then(desirability.is(desirable)));

            this.fuzzyEvaluator.addRule(
                    Rule.when(distance.is(close))
                        .and(ammoStatus.is(ok))
                        .then(desirability.is(undesirable)));

            this.fuzzyEvaluator.addRule(
                    Rule.when(distance.is(close))
                    .and(ammoStatus.is(low))
                    .then(desirability.is(undesirable)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public int getMaxAmmo() {
        return MAX_AMMO;
    }


    /**
     * {@inheritDoc}
     *
     * @param distanceToTarget {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public double desirability(double distanceToTarget) {
        return this.fuzzyEvaluator.evaluate(distanceToTarget,
                this.getRemainingAmmo());
    }


    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    protected Projectile createProjectile() {
        return new Bullet(agent.getGame(), agent, agent.getLocation(),
                agent.getRotation() + this.noise());
    }


    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    protected long getFireRate() {
        return FIRE_RATE;
    }


    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    protected int getInitialAmmo() {
        return INITIAL_AMMO;
    }
}
