package Blizzard1238562.simpleTPA.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import Blizzard1238562.simpleTPA.manager.RequestType;

public final class TpaRequestExpiredEvent extends TpaRequestOutcomeEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public TpaRequestExpiredEvent(Player sender, Player target, RequestType requestType) {
        super(sender, target, requestType);
    }

    @Override
    public HandlerList getHandlers() { return HANDLERS; }

    public static HandlerList getHandlerList() { return HANDLERS; }

    @Override
    public Outcome getOutcome() {
        return Outcome.EXPIRED;
    }
}
