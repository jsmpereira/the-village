package villagepeople.events;

import explicitlib.event.EventArgs;


/**
 * Arguments for the "on update" event.
 */
public class UpdateEventArgs extends EventArgs {

    /**
     * Creates new EventArgs with the given parameters.
     *
     * @param timeElapsed the time passed since the last update.
     */
    public UpdateEventArgs(long timeElapsed) {
        this.timeElapsed = timeElapsed;
    }


    /**
     * @return the time passed since the last update.
     */
    public long getTimeElapsed() {
        return this.timeElapsed;
    }


    /** The time passed since the last update. */
    private long timeElapsed;
}
