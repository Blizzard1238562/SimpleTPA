package Blizzard1238562.simpleTPA.listener;

import Blizzard1238562.simpleTPA.event.TpaRequestSendEvent;
import Blizzard1238562.simpleTPA.event.TpaRequestAcceptedEvent;
import Blizzard1238562.simpleTPA.event.TpaRequestDeniedEvent;
import Blizzard1238562.simpleTPA.event.TpaRequestCancelledEvent;
import Blizzard1238562.simpleTPA.event.TpaRequestExpiredEvent;
import Blizzard1238562.simpleTPA.event.TpaRequestOutcomeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class TpaDebugListener implements Listener {

    private final JavaPlugin plugin;
    private volatile boolean enabled = false;

    public TpaDebugListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean toggle() {
        enabled = !enabled;
        return enabled;
    }

    @EventHandler
    public void onSend(TpaRequestSendEvent event) {
        if (!enabled) return;
        plugin.getLogger().info("[TPA Debug] SEND " + event.getRequestType()
                + " sender=" + event.getSender().getName()
                + " target=" + event.getTarget().getName()
                + " cancelled=" + event.isCancelled());
    }

    // We cannot listen directly to the abstract OutcomeEvent; 
    // listen indirectly via the event classes which extend it
    @EventHandler public void onAccepted(TpaRequestAcceptedEvent e)   { logOutcome(e); }
    @EventHandler public void onDenied(TpaRequestDeniedEvent e)       { logOutcome(e); }
    @EventHandler public void onCancelled(TpaRequestCancelledEvent e) { logOutcome(e); }
    @EventHandler public void onExpired(TpaRequestExpiredEvent e)     { logOutcome(e); }

    private void logOutcome(TpaRequestOutcomeEvent event) {
        if (!enabled) return;
        plugin.getLogger().info("[TPA Debug] " + event.getOutcome() + " " + event.getRequestType()
                + " sender=" + event.getSender().getName()
                + " target=" + event.getTarget().getName());
    }
}