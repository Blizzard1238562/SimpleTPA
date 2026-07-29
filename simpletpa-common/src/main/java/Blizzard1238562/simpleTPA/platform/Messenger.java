package Blizzard1238562.simpleTPA.platform;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

public interface Messenger {

    void send(CommandSender recipient, Component message);
}
