package Blizzard1238562.simpleTPA.platform.paper;

import Blizzard1238562.simpleTPA.platform.Messenger;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

public final class PaperMessenger implements Messenger {

    @Override
    public void send(CommandSender recipient, Component message) {
        recipient.sendMessage(message);
    }
}
