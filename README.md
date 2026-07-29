# SimpleTPA

A lightweight Paper plugin for `/tpa`-style teleport requests, with cooldowns, an opt-out toggle, and clickable accept/deny chat buttons.

**Modrinth:** [simpletpaplugin](https://modrinth.com/plugin/simpletpaplugin)

## Features

- `/tpa <player>` to request a teleport
- `/tpaccept` / `/tpdeny` to respond to the latest incoming request
- `/tpacancel` to cancel your own pending request
- `/tpatoggle` to opt out of receiving requests
- `/tpreload` to reload `config.yml`
- `/tpa version` to check for updates
- Per-player cooldowns, fully customizable messages and sounds
- Built-in Modrinth update checker

See [`config.yml`](src/main/resources/config.yml) for all messages, sounds, and settings, and [`plugin.yml`](src/main/resources/plugin.yml) for the full permission list.

## Project structure

```
src/main/java/Blizzard1238562/simpleTPA/
├── SimpleTPA.java          # Plugin entry point, wires everything together
├── command/                # One CommandExecutor per command
├── config/                 # Config loading, migration, and message lookup
├── listener/                # Bukkit event listeners
├── manager/                # In-memory state (requests, cooldowns, opt-outs)
├── task/                   # Scheduled BukkitRunnables
├── update/                 # Modrinth update checker
└── util/                   # Sound playback and chat component helpers
```

## Building from source

**Requirements:**
- JDK 21+
- Maven 3.9+

```
mvn clean package
```

The compiled and shaded jar will be at `target/SimpleTPA-<version>.jar`. Just drop it into your `plugins/` folder.

The build shades `com.google.code.gson:gson` into the final jar; `paper-api` stays `provided` and is supplied by the server at runtime.

## Contributing

Issues and pull requests are welcome. Please keep changes to `pom.xml` (API versioning) and the update checker minimal and well-explained, since both are easy to break in ways that only show up at runtime.

## License

MIT — see [LICENSE](LICENSE).
