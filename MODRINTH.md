# SimpleTPA
### Plugin
Its a SimpleTPA Plugin. With some Customization Options. **Default config is at the end of this page.**

### Please Message me if you encounter any Bugs!
**Discord: blizzard8562 _(for Bug-Reports, other Issues and Feedback)_**

**Available for Paper/Folia and Bukkit/Spigot** _(two separate downloads, see the Modrinth versions tab)_

**Features:**
- Customizable Messages _(supports MiniMessage tags and legacy color codes)_
- TPA-Cooldowns
- Custom Sounds
- Clickable Chat Message _(for /tpaccept or /tpdeny)_
- PlaceholderAPI Support
- Optional Teleport-Warmup _(stand still for a few seconds after being accepted, before you get teleported)_
- Optional support for multiple simultaneous TPA-Requests
- Folia Support

**How to use:**
- _**/tpa [Player]**_ To send a teleportation request to a specified Player
- _**/tpaccept [Player]**_ To accept a pending TPA-Request. Specify a Player if you have multiple pending requests.
- _**/tpdeny [Player]**_ To deny a pending TPA-Request. Specify a Player if you have multiple pending requests.
- _**/tpacancel [Player]**_ To cancel one of your pending outgoing TPA-Requests. Specify a Player if you have multiple.
- _**/tpreload**_ To reload the config.yml. _Permission Required: tpa.reload_
- _**/tpatoggle**_ Toggles whether you allow incoming TPA-Requests.
- _**/tpa version**_ Allows viewing plugin version info.
- _**/tpa help**_ Lists all available commands with a short description.

**Permissions:**
- _**tpa.command.tpa**_ You need this Permission to run _**/tpa**_.
- _**tpa.command.tpaccept**_ You need this Permission to run _**/tpaccept**_.
- _**tpa.command.tpdeny**_ You need this Permission to run _**/tpdeny**_.
- _**tpa.command.tpacancel**_ You need this Permission to run _**/tpacancel**_.
- _**tpa.command.tpatoggle**_ You need this Permission to run _**/tpatoggle**_.
- _**tpa.reload**_ You need this Permission to run _**/tpreload**_.
- _**tpa.command.version**_ You need this Permission to run _**/tpa version**_.
- _**tpa.command.help**_ You need this Permission to run _**/tpa help**_.

![SimpleTPA Logo](https://cdn.modrinth.com/data/cached_images/54c5a66eb1d5a78b720ed6ffa10d657fe28412d5.png)

_default config.yml_

```
settings:
  tpa_cooldown: 30 # Cooldown in Seconds, before a Player can send another TPA-Request.
  tpa_request_timeout: 60 # Time in Seconds, until a TPA-Request runs out.
  check_for_updates: true # Checks Modrinth every 12 hours for a new version.
  modrinth_project_slug: "simpletpaplugin" # Modrinth project slug used for update notifications.
  use_placeholderapi_formatting: true # Formats Player names in messages using PlaceholderAPI. Requires PlaceholderAPI to be installed.
  player_display_format: "§6%luckperms_prefix% §8| %player%" # Format for Player names in messages. %luckperms_prefix% requires PlaceholderAPI and LuckPerms to be installed.
  teleport_warmup_enabled: false # If enabled, the requester has to stand still for a configured amount of time before being teleported after their TPA-Request gets accepted.
  teleport_warmup_seconds: 5 # Time in Seconds the requester has to stand still before being teleported. Only used if teleport_warmup_enabled is true.
  allow_multiple_requests: true # If enabled, Players can send TPA-Requests to multiple different Players at the same time, and receive multiple incoming Requests at once.
sounds:
  tpa_request_sent: "entity.experience_orb.pickup"
  tpa_request_received: "entity.player.levelup"
  tpa_accept: "entity.enderman.teleport"
  tpa_deny: "entity.villager.no"
  tpa_expired: "entity.item.break"
  tpa_toggle_enabled: "block.note_block.bass"
  tpa_toggle_disabled: "block.note_block.pling"
  tpa_teleport_cancelled: "entity.villager.no"
messages: # Supports both legacy § color codes and MiniMessage tags (e.g. <green>, <gradient:blue:aqua>, <hover:show_text:'...'>).
  tpa_request_sent: "You sent a TPA-Request to %target% ."
  tpa_request_received: "%player% sent you a TPA-Request! /tpaccept or /tpdeny"
  tpa_accept_success: "You accepted the TPA-Request from %player% ."
  tpa_accept_teleport: "You got teleported by %player% ."
  tpa_teleport_warmup_started: "Teleporting to %player% in %seconds% seconds. Don't move!"
  tpa_teleport_warmup_cancelled: "Teleportation to %player% was cancelled because you moved."
  tpa_deny_success: "You denied the TPA-Request from %player% ."
  tpa_no_request: "You dont have any pending TPA-Requests."
  tpa_request_exists: "You already have a pending TPA-Request. Wait for it to be accepted, denied, or to expire before sending another."
  tpa_request_exists_target: "You already have a pending TPA-Request to this Player."
  tpa_no_request_from: "You don't have a pending TPA-Request from %player% ."
  tpa_no_request_to: "You don't have a pending TPA-Request to %player% ."
  tpa_multiple_requests: "You have multiple pending TPA-Requests: %players% . Use /tpaccept <player> or /tpdeny <player> to choose one."
  tpa_multiple_requests_outgoing: "You have multiple pending outgoing TPA-Requests: %players% . Use /tpacancel <player> to choose one."
  player_not_online: "This Player is currently not online."
  player_only_command: "This command can only be used by Players."
  tpa_self_request: "You cannot send a TPA-Request to yourself."
  wrong_usage: "Use: /%command% <Player>"
  tpa_cancel_success: "Your TPA-Request was cancelled."
  tpa_cancel_notify: "%player% cancelled their TPA-Request."
  tpa_request_expired_sender: "Your TPA-Request to %target% ran out."
  tpa_request_expired_receiver: "The TPA-Request by %player% ran out."
  tpa_cooldown: "§ePlease wait %seconds% Seconds, before sending a new TPA-Request."
  config_reloaded: "§aThe Configuration has succesfully been reloaded."
  no_permission: "§cYou dont have the required Permissions to use this Command.!"
  tpa_toggle_enabled: "§cYou will no longer receive TPA-Requests."
  tpa_toggle_disabled: "§aYou can now receive TPA-Requests again."
  tpa_target_not_accepting: "%target% is not currently accepting TPA-Requests."
  version_info: "SimpleTPA version info - Current: %current% | Latest: %latest% | %url%"
  update_available_console: "A new version (%version%) of SimpleTPA is available! Download: %url%"
  update_available_player: "§eA new version (%version%) of SimpleTPA is available! §fDownload: %url%"

clickable_messages:
  accept_text: "[✔ Accept]"
  accept_hover: "Click to accept the TPA!"
  accept_command: "/tpaccept"
  accept_color: "GREEN"

  deny_text: "[✖ Deny]"
  deny_hover: "Click to deny the TPA!"
  deny_command: "/tpdeny"
  deny_color: "RED"

help_messages:
  header: "§6--- SimpleTPA Commands ---"
  tpa: "§e/tpa <player> §7- Sends a TPA-Request to a Player."
  tpaccept: "§e/tpaccept [player] §7- Accepts a pending TPA-Request. Specify a Player if you have multiple."
  tpdeny: "§e/tpdeny [player] §7- Denies a pending TPA-Request. Specify a Player if you have multiple."
  tpacancel: "§e/tpacancel [player] §7- Cancels one of your pending outgoing TPA-Requests."
  tpatoggle: "§e/tpatoggle §7- Toggles whether you can receive TPA-Requests."
  version: "§e/tpa version §7- Shows the current plugin version and update status."
  help: "§e/tpa help §7- Shows this help message."
  tpreload: "§e/tpreload §7- Reloads the plugin configuration."
```


<details>
<summary>German Translation for config.yml(1.5)</summary>


```
settings:
  tpa_cooldown: 30 # Abklingzeit in Sekunden, bevor ein Spieler eine weitere TPA-Anfrage senden kann.
  tpa_request_timeout: 60 # Zeit in Sekunden, bis eine TPA-Anfrage abläuft.
  check_for_updates: true # Überprüft alle 12 Stunden auf Modrinth, ob eine neue Version verfügbar ist.
  modrinth_project_slug: "simpletpaplugin" # Modrinth-Projekt-Slug für Update-Benachrichtigungen.
  use_placeholderapi_formatting: true # Formatiert Spielernamen in Nachrichten mit PlaceholderAPI. Erfordert, dass PlaceholderAPI installiert ist.
  player_display_format: "§6%luckperms_prefix% §8| %player%" # Format für Spielernamen in Nachrichten. %luckperms_prefix% erfordert PlaceholderAPI und LuckPerms.
  teleport_warmup_enabled: false # Wenn aktiviert, muss der Anfragende nach Annahme der TPA-Anfrage für eine festgelegte Zeit stillstehen, bevor er teleportiert wird.
  teleport_warmup_seconds: 5 # Zeit in Sekunden, die der Anfragende stillstehen muss, bevor er teleportiert wird. Wird nur verwendet, wenn teleport_warmup_enabled aktiviert ist.
  allow_multiple_requests: true # Wenn aktiviert, können Spieler gleichzeitig TPA-Anfragen an mehrere verschiedene Spieler senden und mehrere eingehende Anfragen gleichzeitig erhalten.

sounds:
  tpa_request_sent: "entity.experience_orb.pickup"
  tpa_request_received: "entity.player.levelup"
  tpa_accept: "entity.enderman.teleport"
  tpa_deny: "entity.villager.no"
  tpa_expired: "entity.item.break"
  tpa_toggle_enabled: "block.note_block.bass"
  tpa_toggle_disabled: "block.note_block.pling"
  tpa_teleport_cancelled: "entity.villager.no"

messages:
  tpa_request_sent: "Du hast eine TPA-Anfrage an %target% gesendet."
  tpa_request_received: "%player% hat dir eine TPA-Anfrage gesendet! /tpaccept oder /tpdeny"
  tpa_accept_success: "Du hast die TPA-Anfrage von %player% akzeptiert."
  tpa_accept_teleport: "Du wurdest von %player% teleportiert."
  tpa_teleport_warmup_started: "Du wirst in %seconds% Sekunden zu %player% teleportiert. Beweg dich nicht!"
  tpa_teleport_warmup_cancelled: "Die Teleportation zu %player% wurde abgebrochen, da du dich bewegt hast."
  tpa_deny_success: "Du hast die TPA-Anfrage von %player% abgelehnt."
  tpa_no_request: "Du hast keine ausstehenden TPA-Anfragen."
  tpa_request_exists: "Du hast bereits eine ausstehende TPA-Anfrage. Warte, bis diese akzeptiert, abgelehnt wurde oder abgelaufen ist, bevor du eine weitere sendest."
  tpa_request_exists_target: "Du hast bereits eine ausstehende TPA-Anfrage an diesen Spieler."
  tpa_no_request_from: "Du hast keine ausstehende TPA-Anfrage von %player%."
  tpa_no_request_to: "Du hast keine ausstehende TPA-Anfrage an %player%."
  tpa_multiple_requests: "Du hast mehrere ausstehende TPA-Anfragen: %players% . Nutze /tpaccept <Spieler> oder /tpdeny <Spieler>, um eine auszuwählen."
  tpa_multiple_requests_outgoing: "Du hast mehrere ausstehende ausgehende TPA-Anfragen: %players% . Nutze /tpacancel <Spieler>, um eine auszuwählen."
  player_not_online: "Dieser Spieler ist derzeit nicht online."
  player_only_command: "Dieser Befehl kann nur von Spielern verwendet werden."
  tpa_self_request: "Du kannst dir selbst keine TPA-Anfrage senden."
  wrong_usage: "Verwendung: /%command% <Spieler>"
  tpa_cancel_success: "Deine TPA-Anfrage wurde abgebrochen."
  tpa_cancel_notify: "%player% hat seine TPA-Anfrage abgebrochen."
  tpa_request_expired_sender: "Deine TPA-Anfrage an %target% ist abgelaufen."
  tpa_request_expired_receiver: "Die TPA-Anfrage von %player% ist abgelaufen."
  tpa_cooldown: "§eBitte warte %seconds% Sekunden, bevor du eine neue TPA-Anfrage sendest."
  config_reloaded: "§aDie Konfiguration wurde erfolgreich neu geladen."
  no_permission: "§cDu hast nicht die benötigten Berechtigungen, um diesen Befehl zu verwenden!"
  tpa_toggle_enabled: "§cDu erhältst nun keine TPA-Anfragen mehr."
  tpa_toggle_disabled: "§aDu kannst jetzt wieder TPA-Anfragen erhalten."
  tpa_target_not_accepting: "%target% akzeptiert derzeit keine TPA-Anfragen."
  version_info: "SimpleTPA Versionsinfo - Aktuell: %current% | Neueste: %latest% | %url%"
  update_available_console: "Eine neue Version (%version%) von SimpleTPA ist verfügbar! Download: %url%"
  update_available_player: "§eEine neue Version (%version%) von SimpleTPA ist verfügbar! §fDownload: %url%"

clickable_messages:
  accept_text: "[✔ Akzeptieren]"
  accept_hover: "Klicke, um die TPA-Anfrage zu akzeptieren!"
  accept_command: "/tpaccept"
  accept_color: "GREEN"

  deny_text: "[✖ Ablehnen]"
  deny_hover: "Klicke, um die TPA-Anfrage abzulehnen!"
  deny_command: "/tpdeny"
  deny_color: "RED"

help_messages:
  header: "§6--- SimpleTPA Befehle ---"
  tpa: "§e/tpa <Spieler> §7- Sendet eine TPA-Anfrage an einen Spieler."
  tpaccept: "§e/tpaccept [Spieler] §7- Akzeptiert eine ausstehende TPA-Anfrage. Gib einen Spieler an, wenn du mehrere hast."
  tpdeny: "§e/tpdeny [Spieler] §7- Lehnt eine ausstehende TPA-Anfrage ab. Gib einen Spieler an, wenn du mehrere hast."
  tpacancel: "§e/tpacancel [Spieler] §7- Bricht eine deiner ausstehenden ausgehenden TPA-Anfragen ab."
  tpatoggle: "§e/tpatoggle §7- Schaltet um, ob du TPA-Anfragen erhalten kannst."
  version: "§e/tpa version §7- Zeigt die aktuelle Plugin-Version und den Update-Status an."
  help: "§e/tpa help §7- Zeigt diese Hilfenachricht an."
  tpreload: "§e/tpreload §7- Lädt die Plugin-Konfiguration neu."
```


</details>


Have fun :)
