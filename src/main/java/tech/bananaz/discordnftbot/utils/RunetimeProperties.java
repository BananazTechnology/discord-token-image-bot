package tech.bananaz.discordnftbot.utils;

import static tech.bananaz.discordnftbot.utils.CustomProperties.configureCustomProperties;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;

import tech.bananaz.discordnftbot.models.CustomCommand;

@ConfigurationProperties(prefix = "nft-bot")
public class RunetimeProperties {
	
	@Autowired
	DiscordProperties discordProperties;
	
	private ArrayList<CustomCommand> commands = new ArrayList<>();
	
	public void setCustom(Set<Map<String, String>> customInfo) throws RuntimeException {
		this.commands = configureCustomProperties(customInfo);
	}
	/**
	 * Sets the discord object!
	 * @param token
	 * @throws RuntimeException
	 */
	public void setDiscord(Map<Object, Object> discordInfo) throws RuntimeException {
		this.discordProperties.configureDiscordProperties(discordInfo, this.commands);
	}
}