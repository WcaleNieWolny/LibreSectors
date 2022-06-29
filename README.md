# LibreSectors
A (hopefully) lightweight plugin for managing large minecraft servers


## Readme languages:
[PL](https://github.com/WcaleNieWolny/LibreSectors/README_PL.md)

[ANG](https://github.com/WcaleNieWolny/LibreSectors/README.md)

## Important note

We do not have a working demo yet. [we](https://github.com/WcaleNieWolny/LibreSectors/graphs/contributors) are actively working on creating one.


## FAQ

#### What are the priorities for the project?

Well... We need a working demo. That is the most important thing for now. The second priority is having fun developing this plugin :)

#### What technology are you going to start with?

* Project language: [Java](https://www.java.com/en/)
* Test language: [Kotlin](https://kotlinlang.org/)
* Database: [MonogDB](https://www.mongodb.com/), [MySQL](https://www.mysql.com/) (powered by [okaeri-persistence](https://github.com/OkaeriPoland/okaeri-persistence))
* Message broker: [Redis](https://redis.io/)
* Minecraft platform: [Spigot](https://www.spigotmc.org/) (for 1.8), [PaperMC](https://papermc.io/) (for 1.16+)
* Proxy: [Velocity](https://velocitypowered.com/)

> Note: MySQL will be implemented in the future. We also plan to add support for [BungeeCord](https://www.spigotmc.org/wiki/bungeecord/) before first stable release

#### Are you planning on adding support for XYZ?

We are planning to have a good abstraction layer so adding anything is not that hard. If you want you can [create issue](https://github.com/WcaleNieWolny/LibreSectors/issues), and we will look into it. All contributions are welcome!

#### Is there any way to contact you?

Well... the project does not have an official discord server. But you can join [Bookity](https://discord.gg/CYvyq3u) and ping `@WcaleNieWolny` for help.

## Roadmap

- create project (setup modules)

- create abstraction layer for message broker, database

- create spigot "one source of truth" (Database as a way of storing players equipment)

- split map into small areas (chunks) and add a way to send player between them

- send player equipment using message broker and receive it on the other end

- release first demo version


## License

[AGPL 3.0](https://choosealicense.com/licenses/agpl-3.0/#)

