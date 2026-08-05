package Blizzard1238562.simpleTPA.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import Blizzard1238562.simpleTPA.manager.RequestType;

public abstract class TpaEvent extends Event {

    // Abstract class to simplify implementation of specific events

    private final Player sender;
    private final Player target;
    private final RequestType requestType;

    protected TpaEvent(Player sender, Player target, RequestType requestType) {
        this.sender = sender;
        this.target = target;
        this.requestType = requestType;
    }

    public Player getSender() { return sender; }
    public Player getTarget() { return target; }
    public RequestType getRequestType() { return requestType; }
}