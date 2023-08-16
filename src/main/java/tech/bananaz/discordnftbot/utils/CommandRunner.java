package tech.bananaz.discordnftbot.utils;

import lombok.Data;
import tech.bananaz.discordnftbot.discord.DiscordBot;
import tech.bananaz.discordnftbot.models.EventMessage;

@Data
public class CommandRunner implements Runnable {
	
	private DiscordBot bot;
	private EventMessage storage;
	
	public CommandRunner(DiscordBot bot, EventMessage storage) {
		this.bot = bot;
		this.storage = storage;
	}

	@Override
	public void run() {
		this.bot.sendEntry(this.storage);
	}

}
