package viglaglepeopgle.entities;

import viglaglepeopgle.display.Observer;
import viglaglepeopgle.game.Controller;
import viglaglepeopgle.partsys.Smoke;
import viglaglepeopgle.renderable.AnimatedRectangle;
import viglaglepeopgle.renderable.ModelRenderer;
import viglaglepeopgle.settings.Resources;
import villagepeople.weaponry.Rocket;
import explicitlib.event.Action;
import explicitlib.event.EventArgs;
import explicitlib.geometry.Point3D;
import explicitlib.geometry.Size;
import explicitlib.geometry.Vector3D;

/**
 * GLEntity for a rocket.
 *
 * Used to display a rocket in OpenGL.
 */
public class GLRocket extends GLProjectile {

    /**
     * Creates a new GLRocket for a given rocket in the game.
     *
     * @param controller The controller of the game where this rocket belongs.
     * @param rocket The corresponding rocket.
     */
    public GLRocket(final Controller controller, final Rocket rocket) {
        super(rocket);
        this.controller = controller;
        this.model = new ModelRenderer(Resources.getModel("rocket"),
                new Point3D(0, 0, 0));
        //this.smoke = new Smoke();

        // Create the explosion when the rocket hits something
        Action collisionAction = new Action() {
            @Override
            public void execute(Object source, EventArgs args) {
                if (explosion == null) {

                    // Turn the explosion to the observer.
                    Vector3D direction = controller.getViewer().getScene()
                        .getObserver().getHeading();
                    explosion = new AnimatedRectangle(new Size(3, 3),
                            model.getLocation(),
                            direction,
                            Resources.getTextureAnimation("explosion"));
                    explosion.setTextureRepetitions(new Size(1, 1));
                }
            }
        };

        rocket.onCollisionWithStaticEntity().attach(collisionAction);
        rocket.onHitBot().attach(collisionAction);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void render() {
        Rocket projectile = (Rocket) this.entity;

        Observer observer = controller.getViewer().getScene().getObserver();
        //smoke.render(observer);

        if (projectile.isExploding()) {
            this.explosion.render();
        } else {
            model.render();
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void update(long timeElapsed) {
        Rocket projectile = (Rocket) this.entity;
        Point3D location = new Point3D(
                projectile.getLocation().getX(),
                0.5,
                -projectile.getLocation().getY());

        if (!projectile.isExploding()) {
            model.setRotation((float) projectile.getRotation());
            model.setLocation(location);

            Vector3D heading = new Vector3D(
                    projectile.getHeadingVector().getX(),
                    0,
                    -projectile.getHeadingVector().getY());

            for (int i = 0; i < 30; i++) {
                double x = location.getX() + heading.getX() * (-i * 0.1);
                double y = location.getY() + heading.getY() * (-i * 0.1);
                double z = location.getZ() + heading.getZ() * (-i * 0.1);
                //this.smoke.add((float) x, (float) y, (float) z);
            }
        } else {
            // Turn the explosion to the observer.
            Vector3D direction = controller.getViewer().getScene()
                .getObserver().getHeading();
            this.explosion.setNormal(direction);
            this.explosion.step(timeElapsed);
        }
    }


    /**
     * @return the model that represents this rocket on screen.
     */
    public ModelRenderer getModel() {
        return this.model;
    }

    /** The model that represents this rocket on screen. */
    private ModelRenderer model;

    /** The animation of the explosion. */
    private AnimatedRectangle explosion;

    /** The controller of the game where this projectile belongs. */
    private Controller controller;

    /** Particle system for the smoke. */
    private Smoke smoke;
}
