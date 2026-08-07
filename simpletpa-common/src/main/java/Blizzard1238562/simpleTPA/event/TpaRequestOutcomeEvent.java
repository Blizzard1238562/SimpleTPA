package Blizzard1238562.simpleTPA.event;

import org.bukkit.entity.Player;

import Blizzard1238562.simpleTPA.manager.RequestType;

public abstract class TpaRequestOutcomeEvent extends TpaEvent {

    /**
     * The way a TPA request was resolved.
     */
    public enum Outcome {
        ACCEPTED,
        DENIED,
        CANCELLED,
        EXPIRED
    }

    protected TpaRequestOutcomeEvent(Player sender, Player target, RequestType requestType) {
        super(sender, target, requestType);
    }

    /**
     * How this request was resolved.
     *
     * @return the outcome corresponding to this event's concrete type
     */
    public abstract Outcome getOutcome();
}
