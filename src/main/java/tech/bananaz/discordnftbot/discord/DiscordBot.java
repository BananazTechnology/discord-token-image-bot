package tech.bananaz.discordnftbot.discord;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.Permissions;
import org.javacord.api.entity.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import tech.bananaz.discordnftbot.models.CustomCommand;
import tech.bananaz.discordnftbot.models.EventMessage;
import tech.bananaz.discordnftbot.utils.DiscordProperties;

import static tech.bananaz.discordnftbot.utils.ImageUtils.*;

@Component
public class DiscordBot {
	
	@Autowired
	Commands commands;
	
	// Final
	public static final String NEWLINE  = "\n";
	public static final Color MSG_COLOR = Color.ORANGE;
	private static final Logger LOGGER  = LoggerFactory.getLogger(DiscordBot.class);
	
	/** Required */
	private DiscordApi disc;
	
	public void build(DiscordProperties config, ArrayList<CustomCommand> customInfo) {
		try {
			// Start Discord connection
	        this.disc = new DiscordApiBuilder()
	        					.setToken(config.getToken())
	        					.login()
	        					.join();
	       
	        startupLogger();
	        // Obtain critical information about the Discord surhvur!
			Collection<Server> server = this.disc.getServers();
			if(!(server.size() > 0)) throw new RuntimeException("Bot not in any servers!");
	        this.disc.addMessageCreateListener(this.commands.build(config, customInfo));
		} catch (Exception e) {
			LOGGER.error("Discord Error: Failed starting bot! Exception: " + e.getMessage());
        	throw new RuntimeException("Discord Error: Failed starting bot! Exception: " + e.getMessage());
		}
	}
	
	public void sendEntry(EventMessage msg) {
		String fullImageUrl = buildImageUrl(
									msg.getCommandInfo().getBaseUrl(),
									msg.getSelection(),
									msg.getCommandInfo().getFileFormat());
		
		try {
			buildMessage(fullImageUrl).send(msg.getChannel());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void startupLogger() throws JsonProcessingException {
        LOGGER.info("--------");
		LOGGER.debug("Discord bot config: "+ new ObjectMapper().writeValueAsString(disc.getApplicationInfo().join()));
        LOGGER.debug("Discord connection started!");
        LOGGER.info("Need this bot in your server? " + disc.createBotInvite(Permissions.fromBitmask(0x0000000008)));
        LOGGER.info("--------");
	}
	
	private MessageBuilder buildMessage(String imageUrl) throws IOException {
		MessageBuilder newMsg = new MessageBuilder();
		EmbedBuilder   newEmbed = new EmbedBuilder();
		if(imageUrl.contains(".gif")) {
			newEmbed.setImage(imageUrl);
		} else {
			newEmbed.setImage(getImage(imageUrl));
		}
		newEmbed.setColor(MSG_COLOR);
		newMsg.setEmbed(newEmbed);
		return newMsg;
	}
	
	private String buildImageUrl(String baseUrl, int selectiom, String fileFormat) {
		return String.format("%s%s%s", baseUrl, selectiom, fileFormat);
	}

}