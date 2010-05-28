package villagepeople.events;

import villagepeople.entities.Player;
import explicitlib.event.EventArgs;

public class UserCreatedEventArgs extends EventArgs {

	public UserCreatedEventArgs(Player user) {
		this.user = user;
	}

	/**
    * @return The user that was created.
    */
    public Player getUser() {
        return this.user;
    }


    /** The user that was created. */
    private Player user;
}
