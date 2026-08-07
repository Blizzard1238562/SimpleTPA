package Blizzard1238562.simpleTPA.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import Blizzard1238562.simpleTPA.manager.RequestType;

public final class TpaRequestSendEvent extends TpaEvent implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled;
    private String cancelReason;

    public TpaRequestSendEvent(Player sender, Player target, RequestType requestType) {
        super(sender, target, requestType);
    }

    @Override public boolean isCancelled() { return cancelled; }
    @Override public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
    public String getCancelReason() { return cancelReason; }
    public void setCancelReason(String reason) { this.cancelReason = reason; }

    @Override public HandlerList getHandlers() { return HANDLERS; }
    public static HandlerList getHandlerList() { return HANDLERS; }
}
