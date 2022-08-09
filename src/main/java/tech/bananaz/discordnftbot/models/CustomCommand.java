package tech.bananaz.discordnftbot.models;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(includeFieldNames = true)
public class CustomCommand {
	
	private String baseUrl;
	private String fileFormat;
	private String command;
	private int    max;
	
	public CustomCommand() {}

}
