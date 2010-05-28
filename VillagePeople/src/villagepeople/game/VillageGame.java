package villagepeople.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import villagepeople.collisions.Collision;
import villagepeople.collisions.CollisionDetector;
import villagepeople.entities.Bot;
import villagepeople.entities.InventoryItem;
import villagepeople.entities.Team;
import villagepeople.entities.Player;
import villagepeople.events.BotCreatedEventArgs;
import villagepeople.events.UserCreatedEventArgs;
import villagepeople.navigation.Map;
import villagepeople.settings.Settings;
import villagepeople.weaponry.Projectile;

import com.golden.gamedev.Game;
import com.golden.gamedev.object.Background;
import com.golden.gamedev.object.background.TileBackground;
import com.golden.gamedev.util.ImageUtil;

import explicitlib.event.Event;
import explicitlib.geometry.Point;
import explicitlib.geometry.Size;

public class VillageGame extends Game {

    public VillageGame(Map map) {
        this.map = map;
        this.bots = new ArrayList<Bot>();
        this.teams = new LinkedList<Team>();
        this.projectiles = new ArrayList<Projectile>();
        this.projectilesToRemove = new HashSet<Projectile>();
        this.botsToRemove = new HashSet<Bot>();
        this.collisionDetector = new CollisionDetector(this);
    }

    @Override
    public void initResources() {
      //  this.background = createBackground();

        // Initialize inventory items' sprites
        for (InventoryItem item : map.getItems()) {
         //   item.loadSprite(this);
            // item.activate();
        }

        // Initialize the teams
        Team team = new Team(Team.GREEN);
        this.addUser(new Player(this, getRandomLocation(), team));
        this.teams.add(team);
        for (int i = 0; i < 4; i++) {
            this.addBot(new Bot(this, getRandomLocation(), team));
        }

        team = new Team(Team.BLUE);
        this.teams.add(team);

        for (int i = 0; i < 5; i++) {
            this.addBot(new Bot(this, getRandomLocation(), team));
        }                                      
    }


    public Point getRandomLocation() {
        Point point;
        Point center;

        do {
            double x = (Math.random() * this.getMap().getWidth());
            double y = (Math.random() * this.getMap().getHeight());

            point = new Point(x, y);
            center = map.cellAt(point).getCenter();
        } while (!map.cellAt(point).isFree() || previousLocations.contains(center));

        previousLocations.add(center);
        return center;
    }
    private Set<Point> previousLocations = new HashSet<Point>();


    @Override
    public void render(Graphics2D graphics) {
        /*
        background.render(graphics);

        this.map.draw(graphics);

        for (Bot bot : this.bots) {
            bot.render(graphics);
        }

        for (Projectile projectile : this.projectiles) {
            projectile.render(graphics);
        }

        graphics.drawString("Projectiles: " + this.projectiles.size(), 0, 10);
        */
    }


    @Override
    public void update(long dtime) {
    	if (dtime > 0) {
	        map.update(dtime);
	
	        // Clear the team's influence map
	        for (Team team : this.teams) {
	            // team.updateInfluenceMap(dtime);
	        }
	
	        // Update bots
	        for (Bot bot : this.bots) {
	            bot.update(dtime);
	        }
	
	        // Update projectiles
	        for (Projectile projectile : this.projectiles) {
	            projectile.update(dtime);
	        }
	
	        // Handle collisions
	        for (Collision collision : this.collisionDetector.detectCollisions()) {
	            collision.handle();
	        }
	
	        // Delete dead bots
	        this.removeDeadBots();
	
	        // Delete old projectiles
	        this.removeOldProjectiles();
    	}
    }


    public Map getMap() {
        return this.map;
    }


    /**
     * Add a new User to the game.
     *
     * @param bot The bot to add.
     */
    public void addUser(Player user) {
    	this.user = user;
    	this.bots.add(user);        
        onUserCreated().fire(new UserCreatedEventArgs(user));
    }
    
    /**
     * @return The "on user created" event.
     */
    public Event onUserCreated() {
		return this.onUserCreated;
	}


	/**
     * Add a new bot to the game.
     *
     * @param bot The bot to add.
     */
    public void addBot(Bot bot) {
        this.bots.add(bot);
        onBotCreated().fire(new BotCreatedEventArgs(bot));
    }


    /**
     * @return The list of existing bots
     */
    public List<Bot> getBots() {
        return bots;
    }


    /**
     * Returns the bots inside the given circle.
     *
     * @param location The center of the circle
     * @param radius The radius od the circle
     * @return The list of bots inside the circle
     */
    public List<Bot> getBotsInRange(Point location, double radius) {
        List<Bot> bots = new LinkedList<Bot>();
        for (Bot bot : this.bots) {
            if (location.distanceTo(bot.getLocation()) <= radius) {
                bots.add(bot);
            }
        }

        return bots;
    }


    /**
     * @return The list of active projectiles
     */
    public List<Projectile> getProjectiles() {
        return this.projectiles;
    }


    /**
     * Adds a new projectile to the list of existing projectiles.
     *
     * @param projectile The projectile to add
     */
    public void addProjectile(Projectile projectile) {
        this.projectiles.add(projectile);
    }


    /**
     * Removes a projectile from the world.
     *
     * @param projectile The projectile to remove
     */
    public void removeProjectile(Projectile projectile) {
        if (projectilesToRemove.add(projectile)) {
            projectile.onRemoved().fire();
        }
    }


    /**
     * Removes a bot from the world.
     *
     * @param bot The bot to remove
     */
    public void removeBot(Bot bot) {
        if (botsToRemove.add(bot)) {
            bot.onRemoved().fire();
        }
    }
    
    
    /**
     * @return The "on bot created" event.
     */
    public Event onBotCreated() {
        return this.onBotCreated;
    }


    /**
     * Create the background with the walls and buildings, etc.
     * 
     * @return The newly created background.
     */
    private Background createBackground() {
        // Create and fill the image array
        BufferedImage[] tileImages = new BufferedImage[2];
        tileImages[0] = createFloorImage();
        tileImages[1] = super.getImage(Settings.SPRITE_WALL_PATH);

        int[][] tiles = new int[map.getWidth()][map.getHeight()];

        // fill tiles with random value
        for (int i = 0; i < map.getHeight(); i++) {
            for (int j = 0; j < map.getWidth(); j++) {
                if (!map.cellAt(j, i).isFree()) {
                    tiles[j][map.getHeight() - i - 1] = 1;
                }
            }
        }

        // create the background
        return new TileBackground(tileImages, tiles);
    }


    /**
     * Remove the dead bots
     */
    private void removeDeadBots() {
        this.bots.removeAll(botsToRemove);
        botsToRemove.clear();
    }


    /**
     * Remove old projectiles
     */
    private void removeOldProjectiles() {       
        this.projectiles.removeAll(projectilesToRemove);
        projectilesToRemove.clear();
    }


    /**
     * Creates a simple cell-width x cell-height gray filled image, to be used
     * as the floor
     * 
     * @return The image created.
     */
    private BufferedImage createFloorImage() {
        Size cellSize = Settings.CELL_SIZE;
        BufferedImage image = new BufferedImage((int) cellSize.getWidth(), (int) cellSize.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // Draw on the image
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fill(new Rectangle((int) cellSize.getWidth(), (int) cellSize.getHeight()));
        g2d.dispose();
        return image;
    }


    /**
     * Loads a PNG with translucency support
     */
    @Override
    public BufferedImage getImage(String filename) {
        BufferedImage result;
        if ((result = bsLoader.getStoredImage(filename)) == null) {
            URL framesUrl = bsIO.getURL(filename);
            result = ImageUtil.getImage(framesUrl, Transparency.TRANSLUCENT);
            bsLoader.storeImage(filename, result);
        }

        return result;
    }

    /**
     * Get the user
     * 
     * @return
     */
    public Player getUser() {
		return user;
	}
    
    // XXX Remove
    // The game background, with all the walls, buildings, etc
    private Background background;

    /** The map used in this game. */
    private Map map;

    /** The collision detector. */
    private CollisionDetector collisionDetector;

    /** The list of bots present in the map. */
    private List<Bot> bots;

    /** The list of teams. */
    private List<Team> teams;

    /** The list of projectiles present. */
    private List<Projectile> projectiles;

    /**
     * List of projectiles to remove in the next iteration.
     * This necessary in order to avoid ConcurrentModificationExceptions
     */
    private Set<Projectile> projectilesToRemove;

    /**
     * List of bots to remove in the next iteration.
     * This necessary in order to avoid ConcurrentModificationExceptions
     */
    private Set<Bot> botsToRemove;

    /** Event fired when a new bot is created and added to the game. */
    private Event onBotCreated = new Event(this);

    /** Event fired when a new bot is created and added to the game. */
    private Event onUserCreated = new Event(this);
    
    /** User player */
    private Player user;
    
    public static enum MatchType {
    	TWO_TEAMS,
    	NO_TEAMS,    	
    }
        
}
