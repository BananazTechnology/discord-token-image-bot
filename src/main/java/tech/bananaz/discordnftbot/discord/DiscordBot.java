package tech.bananaz.discordnftbot.discord;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Random;

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
import static java.util.Objects.nonNull;

@Component
public class DiscordBot {
	
	@Autowired
	Commands commands;
	
	// Final
	public static final String NEWLINE  = "\n";
	public static final Color MSG_COLOR = Color.ORANGE;
	private static final Logger LOGGER  = LoggerFactory.getLogger(DiscordBot.class);
	private static final String IPFS_MAGIC_STRING = "%ipfs%";
	// "cf-ipfs.com", 
	private static final String[] IPFS_URLS = {"gateway.ipfs.io", "cloudflare-ipfs.com", "dweb.link", "gateway.pinata.cloud", "hardbin.com", "ipfs.runfission.com", "storry.tv"};
	
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
		try {
			buildMessage(msg, null, null).send(msg.getChannel());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.error(e.getMessage());
		}
	}
	
	private void startupLogger() throws JsonProcessingException {
        LOGGER.info("--------");
		LOGGER.debug("Discord bot config: "+ new ObjectMapper().writeValueAsString(disc.getApplicationInfo().join()));
        LOGGER.debug("Discord connection started!");
        LOGGER.info("Need this bot in your server? " + disc.createBotInvite(Permissions.fromBitmask(0x0000000008)));
        LOGGER.info("--------");
	}
	
	private MessageBuilder buildMessage(EventMessage msg, Optional<Integer> maxRetries, Optional<Integer> retryDelay) throws IOException {
	    int retries = (nonNull(maxRetries)) ? maxRetries.get() : 3;
	    int delayMs = (nonNull(retryDelay)) ? retryDelay.get() : 1000;
	    String fullImageUrl = null;
	    
	    int retryCount = 0;

	    while (retryCount <= retries) {
	    	try {
	    		fullImageUrl = buildImageUrl(
		                msg.getCommandInfo().getBaseUrl(),
		                msg.getSelection(),
		                msg.getCommandInfo().getFileFormat());
		        
		        BufferedImage image = getImage(fullImageUrl, Optional.of(1), null, null);
		        
		        // As long as getImage is set this should not exit if statement
		        if (image != null) {
		            MessageBuilder newMsg = new MessageBuilder();
		            EmbedBuilder newEmbed = new EmbedBuilder();
		            
		            if (fullImageUrl.contains(".gif")) {
		                newEmbed.setImage(fullImageUrl);
		            } else {
		                newEmbed.setImage(image);
		            }
		            
		            newEmbed.setColor(MSG_COLOR);
		            newMsg.setEmbed(newEmbed);
		            return newMsg;
		        }
		        
		        retryCount++;
		        
		        if (retryCount <= retries) {
		            try {
		                Thread.sleep(delayMs);
		            } catch (InterruptedException ignored) {
		            }
		        }
	    	} catch (Exception e) {
		        LOGGER.error("Failed getImage attempt {}/{} for {}", (retryCount + 1), retries, fullImageUrl);
			}
	    }

	    throw new IOException("Failed to fetch build message after " + retries + " retries.");
	}
	
	private String buildImageUrl(String baseUrl, int selectiom, String fileFormat) {
		baseUrl = baseUrl.toLowerCase();
		if(baseUrl.contains(IPFS_MAGIC_STRING)) baseUrl = baseUrl.replace(IPFS_MAGIC_STRING, IPFS_URLS[new Random().nextInt(IPFS_URLS.length)]);
		return String.format("%s%s%s", baseUrl, selectiom, fileFormat);
	}

}