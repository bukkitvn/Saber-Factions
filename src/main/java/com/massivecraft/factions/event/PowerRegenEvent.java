package com.massivecraft.factions.event;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import org.bukkit.event.Cancellable;

/**
 * Event called when a player regenerate power.
 */
public class PowerRegenEvent extends FactionPlayerEvent implements Cancellable {

    /**
     * @author Illyria Team
     */

    private boolean cancelled = false;
    private double modified = 0;

    public PowerRegenEvent(Faction f, FPlayer p) {
        super(f, p);
    }

    /**
     * Get the amount of power this player will regen by default
     *
     * @return power amount gained as a Double.
     */
    public double getDefaultPowerGained() {
        return fPlayer.getMillisPassed() * Conf.powerPerMinute / 60000;
    }

    /**
     * Get the amount of custom power this player will gain. Ignored if less than or equal to 0.
     *
     * @return Custom power as a double
     */
    public double getCustomPower() {
        return modified;
    }

    /**
     * Set the custom power gain for this event.
     *
     * @param gain Amount of power to be added to player.
     */
    public void setCustomPower(Double gain) {
        modified = gain;
    }

    /**
     * Get if we will be using the custom power gain instead of default.
     *
     * @return If we will process the event custom returned as a Boolean.
     */
    public boolean usingCustomPower() {
        return modified > 0;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean c) {
        this.cancelled = c;
    }

}
