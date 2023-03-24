package tech.bananaz.discordnftbot.discord;

import java.util.ArrayList;
import java.util.Optional;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tech.bananaz.discordnftbot.models.CustomCommand;
import tech.bananaz.discordnftbot.models.EventMessage;
import tech.bananaz.discordnftbot.utils.DiscordProperties;

@Component
public class Commands implements MessageCreateListener {
	
	@Autowired
	DiscordBot bot;
	
	// Custom
	private static final Logger LOGGER  = LoggerFactory.getLogger(Commands.class);
	private DiscordProperties config;
	private ArrayList<CustomCommand> commands;
	
	public Commands() { }
	
	public Commands build(DiscordProperties config, ArrayList<CustomCommand> customInfo) {
		this.config   = config;
		this.commands = customInfo;
		return this;
	}
	
	@Override
	public void onMessageCreate(MessageCreateEvent event) {
		// Grab individual data from message event
		Message message = event.getMessage();
		String parsedMsg = message.getContent().trim().strip().toLowerCase();
		Optional<User> sender = message.getUserAuthor();
		Optional<ServerTextChannel> outputChannel = message.getServerTextChannel();
		String cmdPrefix = this.config.getCommandPrefix();
		// Don't even bother if message does not chart with char
		if(outputChannel.get().getIdAsString().equalsIgnoreCase(this.config.getChannelId()) && !sender.get().isYourself()) {
			// Build internal message
			EventMessage e = new EventMessage();
			e.setChannel(outputChannel.get());
			e.setUser(sender.get());
			
			LOGGER.info("User {} triggered the bot EventMessage with [{}]!", sender.get().getDiscriminatedName(), parsedMsg);
			
			// Run command checks
			for(CustomCommand c : this.commands) {
				String[] splitCommand = parsedMsg.split(" ");
				if(splitCommand[0].equals( cmdPrefix + c.getCommand().toLowerCase() )) {
					try {
						int id = Integer.valueOf(splitCommand[1]);
						if(id >= 1 && c.getMax() >= id) {
							e.setSelection(id);
							e.setCommandInfo(c);
							this.bot.sendEntry(e);
							LOGGER.info("User {} requested an image!", sender.get().getDiscriminatedName());
						}
					} catch (Exception ex) {
						LOGGER.info("User {} failed parse of token id [{}]!", sender.get().getDiscriminatedName(), parsedMsg);
					}
				}
			}
		}
	}
}
