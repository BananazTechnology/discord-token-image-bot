package tech.bananaz.discordnftbot.utils;

import java.util.ArrayList;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import static java.util.Objects.isNull;
import lombok.Data;
import lombok.ToString.Exclude;
import tech.bananaz.discordnftbot.discord.DiscordBot;
import tech.bananaz.discordnftbot.models.CustomCommand;

@Data
@Component
public class DiscordProperties {
	
	@Exclude
	@Autowired
	DiscordBot discordBot;
	
	// Easy access to K/V pairs
	private final String PREFIX     = "prefix";
	private final String TOKEN_PAIR = "token";
	private final String CHANNEL_ID = "channelId";
	
	// Filled with data at Runtime
	private String  token;
	private String  commandPrefix;
	private String  channelId;

	public void configureDiscordProperties(Map<Object, Object> discordInfo, ArrayList<CustomCommand> customInfo) throws RuntimeException {
		// Store variable or use in the Discord interface
		this.commandPrefix      = (String) discordInfo.get(PREFIX);
		this.token          	= (String) discordInfo.get(TOKEN_PAIR);
		this.channelId          = (String) discordInfo.get(CHANNEL_ID);
		if(isNull(this.commandPrefix) || isNull(this.token) || isNull(this.channelId)) throw new RuntimeException("No 'prefix' and/or 'token' and/or 'channelId' for discord");
		
		this.discordBot.build(this, customInfo);
	}

}
