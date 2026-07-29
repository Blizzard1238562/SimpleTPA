# SimpleTPA

A Paper/Folia and Bukkit/Spigot plugin for `/tpa`-style teleport requests, with cooldowns, an opt-out toggle, clickable accept/deny chat buttons, and optional teleport warmups.

**Modrinth:** [simpletpaplugin](https://modrinth.com/plugin/simpletpaplugin)

## Features

- `/tpa <player>` to request a teleport, with support for multiple simultaneous incoming/outgoing requests
- `/tpaccept` / `/tpdeny` / `/tpacancel`, with a player argument to disambiguate when you have several pending requests
- `/tpatoggle` to opt out of receiving requests
- `/tpa help` and `/tpa version` (update checking against Modrinth)
- `/tpreload` to reload `config.yml` without a restart
- Per-player cooldowns and an optional stand-still teleport warmup
- Messages support legacy `§` codes and MiniMessage tags in any mix
- Optional PlaceholderAPI/LuckPerms prefix formatting in front of player names, with graceful fallback if not installed
- Full Folia support in the Paper build (regional schedulers, async teleports)
- Built-in Modrinth update checker with console and in-game (op) notifications

See [`config.yml`](simpletpa-paper/src/main/resources/config.yml) for all settings, sounds, and messages, and [`plugin.yml`](simpletpa-paper/src/main/resources/plugin.yml) for the full permission list.

## Project structure

This is a Maven multi-module build. `simpletpa-common` holds all shared logic behind small platform interfaces (`Messenger`, `SoundPlayer`, `TeleportService`, `DelayedTaskScheduler`, `AsyncTaskScheduler`); each platform module provides its own implementation of those interfaces.

```
simpletpa-common/    # Shared business logic — commands, config, request handling (not distributed on its own)
simpletpa-paper/     # Paper/Folia implementation (regional schedulers, async teleports)
simpletpa-bukkit/    # Bukkit/Spigot implementation (shades & relocates Adventure)
```

Only `simpletpa-paper` and `simpletpa-bukkit` produce a distributable jar; `simpletpa-common` is a build-time dependency of both.

## Building from source

**Requirements:**
- JDK 21+
- Maven 3.9+

```bash
mvn clean package
```

This produces two standalone jars:
- `simpletpa-paper/target/SimpleTPA-Paper-<version>.jar` — for Paper and Folia servers
- `simpletpa-bukkit/target/SimpleTPA-Bukkit-<version>.jar` — for Bukkit/Spigot servers

Install whichever matches your server software into `plugins/`; don't run both at once. Both jars shade `com.google.code.gson:gson`; the Bukkit jar additionally shades and relocates the Adventure libraries. `paper-api` / `spigot-api` stay `provided` and are supplied by the server at runtime.

## Contributing

Issues and pull requests are welcome. Please keep changes to the module `pom.xml` files (API versioning) and the update checker minimal and well-explained, since both are easy to break in ways that only show up at runtime.

## License

Apache License 2.0 — see [LICENSE](LICENSE). Commercial use, modification, and redistribution (including selling) are permitted; if you redistribute this project or a derivative of it, you must retain the copyright/attribution notices and the [NOTICE](NOTICE) file crediting the original SimpleTPA project.
