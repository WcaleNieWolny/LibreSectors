# How the plugin is going to work?

#### Let's imagine that you join the server what happens?

You will get assigned a sector and respective spigot server will load your data from database or create one (if data is not present in the database)

#### What happens if your sector is not responsive?

Well... for the time being you will not be able to join the server but in the future you will be spawned on the main (spawn) sector

#### What happens if sector lost connection to the database?

If database is unbelievable then the whole system is on fire. I do not think that there is any reasonable way to detect lost connection.
To do it I would need to use some wierd internal API. I can't do anything about your shitty hosting/lack of skill.