package viglaglepeopgle.game;

import org.lwjgl.util.Timer;

import viglaglepeopgle.display.Scene;
import viglaglepeopgle.entities.GLBot;
import viglaglepeopgle.entities.GLFirstAidItem;
import viglaglepeopgle.entities.GLProjectile;
import viglaglepeopgle.entities.GLRocket;
import viglaglepeopgle.entities.GLPlayer;
import viglaglepeopgle.entities.GLWall;
import viglaglepeopgle.inputcontrollers.GLPlayerController;
import viglaglepeopgle.inputcontrollers.GLSceneController;
import viglaglepeopgle.renderable.Rectangle;
import viglaglepeopgle.settings.Resources;
import viglaglepeopgle.util.Texture;
import villagepeople.entities.Bot;
import villagepeople.entities.FirstAidItem;
import villagepeople.entities.StaticEntity;
import villagepeople.entities.Player;
import villagepeople.entities.Wall;
import villagepeople.events.BotCreatedEventArgs;
import villagepeople.events.FireEventArgs;
import villagepeople.events.UserCreatedEventArgs;
import villagepeople.game.VillageGame;
import villagepeople.weaponry.Projectile;
import villagepeople.weaponry.Rocket;
import explicitlib.event.Action;
import explicitlib.event.EventArgs;
import explicitlib.geometry.Point3D;
import explicitlib.geometry.Size;

/**
 * Provides a bridge between the village people library and the opengl
 * front-end.
 */
/**
 * @author Joel Cordeiro
 * 
 */
public class Controller {

	/**
	 * Creates a new controller for the given game.
	 * 
	 * @param viewer
	 *            The simulator front-end (i.e., the GUI).
	 * @param model
	 *            The game reference.
	 */
	public Controller(final Viewer viewer, final VillageGame model) {
		this.viewer = viewer;
		this.model = model;

		viewer.onUpdate().attach(new Action() {
			@Override
			public void execute(Object source, EventArgs args) {
				Controller.this.update();
			}
		});

		model.onBotCreated().attach(new Action() {
			@Override
			public void execute(Object source, EventArgs args) {
				Bot bot = ((BotCreatedEventArgs) args).getBot();
				final GLBot glBot = new GLBot(bot);
				viewer.getScene().add(glBot);

				bot.onKilled().attach(new Action() {

					@Override
					public void execute(Object source, EventArgs args) {
						viewer.getScene().remove(glBot);
					}

				});

				bot.onFire().attach(new Action() {
					@Override
					public void execute(Object source, EventArgs args) {
						Projectile projectile = ((FireEventArgs) args)
								.getProjectile();

						final GLProjectile glProjectile = GLProjectile
								.createFor(Controller.this, projectile);

						if (followRocket == null
								&& projectile instanceof Rocket) {
							followRocket = (GLRocket) glProjectile;
						}

						viewer.getScene().add(glProjectile);
						projectile.onRemoved().attach(new Action() {

							@Override
							public void execute(Object source, EventArgs args) {

								viewer.getScene().remove(glProjectile);
							}

						});
					}
				});

				// followBot = glBot;
			}
		});

		model.onUserCreated().attach(new Action() {

			@Override
			public void execute(Object source, EventArgs args) {
				final Player player = ((UserCreatedEventArgs) args).getUser();
				final GLPlayer glPlayer = new GLPlayer(player);
				/*glPlayer.setGlController(new GLSceneController(player,
						viewer.getScene()));*/
				glPlayer.setGlController(new GLPlayerController(player,
						viewer.getScene()));

				viewer.getScene().add(glPlayer);				
				
				//glPlayer.getGlController().onWideCamera().fire();
				glPlayer.getGlController().onUserCamera().fire();
				
				player.onKilled().attach(new Action() {

					@Override
					public void execute(Object source, EventArgs args) {
						viewer.getScene().remove(glPlayer);						
					}

				});

				player.onFire().attach(new Action() {
					@Override
					public void execute(Object source, EventArgs args) {
						Projectile projectile = ((FireEventArgs) args)
								.getProjectile();

						final GLProjectile glProjectile = GLProjectile
								.createFor(Controller.this, projectile);

						if (followRocket == null
								&& projectile instanceof Rocket) {
							followRocket = (GLRocket) glProjectile;
						}

						viewer.getScene().add(glProjectile);
						projectile.onRemoved().attach(new Action() {

							@Override
							public void execute(Object source, EventArgs args) {

								viewer.getScene().remove(glProjectile);
							}

						});
					}
				});

				// followBot = glBot;
			}

		});

		model.initResources();
		initMap();
	}

	// private GLBot followBot;
	private GLRocket followRocket;

	/**
	 * Performs a single iteration of the VillagePeople simulator.
	 */
	public void update() {
		float currentTime = updateTimer.getTime();

		// Update the game
		this.model.update((long) ((currentTime - lastTime) * 1000));
		this.lastTime = currentTime;

		/*
		 * if (followBot != null) { Vector3D obsHeading = new
		 * Vector3D(Math.cos(followBot.getModel().getRotation()), 0,
		 * -Math.sin(followBot.getModel().getRotation())); Point3D obsLocation =
		 * followBot.getModel().getLocation().plus(obsHeading.reverse().multiplyBy(3)).displace(0,
		 * 1.5, 0);
		 * this.viewer.getScene().getObserver().setLocation(obsLocation);
		 * this.viewer.getScene().getObserver().setHeading(obsHeading); }
		 */
		/*
		 * if (followRocket != null) { Vector3D obsHeading = new
		 * Vector3D(Math.cos(followRocket.getModel().getRotation()), 0,
		 * -Math.sin(followRocket.getModel().getRotation())); Point3D
		 * obsLocation =
		 * followRocket.getModel().getLocation().plus(obsHeading.reverse().multiplyBy(5)).displace(0,
		 * 0.2, 0);
		 * this.viewer.getScene().getObserver().setLocation(obsLocation); //
		 * this.followRocket = null;
		 * this.viewer.getScene().getObserver().setHeading(obsHeading); }
		 */
	}

	/**
	 * @return the simulator front-end (i.e., the GUI).
	 */
	public Viewer getViewer() {
		return this.viewer;
	}

	/**
	 * Initializes the map for rendering.
	 */
	private void initMap() {
		Scene scene = viewer.getScene();

		// Adds the floor to the scene
		Size mapSize = model.getMap().getSize();
		Texture floorTexture = Resources.getTexture("floor");
		Rectangle floor = new Rectangle(new Point3D(0, 0, 0), new Point3D(
				mapSize.getWidth(), 0, 0), new Point3D(mapSize.getWidth(), 0,
				-mapSize.getHeight()), new Point3D(0, 0, -mapSize.getHeight()),
				floorTexture);

		scene.add(floor);

		// Adds the ceiling to the scene
		final int ceilingY = 2;
		Rectangle ceiling = new Rectangle(new Point3D(0, ceilingY, 0),
				new Point3D(mapSize.getWidth(), ceilingY, 0), new Point3D(
						mapSize.getWidth(), ceilingY, -mapSize.getHeight()),
				new Point3D(0, ceilingY, -mapSize.getHeight()));

		// scene.add(ceiling);

		for (StaticEntity entity : model.getMap().getStaticEntities()) {
			GLWall wall = new GLWall((Wall) entity);
			scene.add(wall);
		}

		for (FirstAidItem item : model.getMap().getHealthItems()) {
			GLFirstAidItem glItem = new GLFirstAidItem(item);
			scene.add(glItem);
		}
	}

	/** The simulator front-end (i.e., the GUI). */
	private Viewer viewer;

	/** The game reference. */
	private VillageGame model;

	/** Update timer. */
	private Timer updateTimer = new Timer();

	/** Time of the last update() call. */
	private float lastTime;
}
