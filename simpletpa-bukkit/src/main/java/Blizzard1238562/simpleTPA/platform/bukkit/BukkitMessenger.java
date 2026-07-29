package Blizzard1238562.simpleTPA.platform.bukkit;

import Blizzard1238562.simpleTPA.platform.Messenger;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

public final class BukkitMessenger implements Messenger {

    private final BukkitAudiences audiences;

    public BukkitMessenger(BukkitAudiences audiences) {
        this.audiences = audiences;
    }

    @Override
    public void send(CommandSender recipient, Component message) {
        audiences.sender(recipient).sendMessage(message);
    }
}
