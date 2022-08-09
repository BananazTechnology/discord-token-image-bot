package tech.bananaz.discordnftbot.utils;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.Data;
import tech.bananaz.discordnftbot.models.CustomCommand;

@Data
public class CustomProperties {
	
	// Easy access to K/V pairs
	private static final String COMMAND		 = "command";
	private static final String BASE_URL     = "baseUrl";
	private static final String FILE_FORMAT  = "fileFormat";
	private static final String MAX  		 = "max";
	private static final String[] FIELDS = {COMMAND, BASE_URL, FILE_FORMAT, MAX};
	private static final Logger LOGGER  = LoggerFactory.getLogger(CustomProperties.class);

	public static ArrayList<CustomCommand> configureCustomProperties(Set<Map<String, String>> customInfo) throws RuntimeException {
		ArrayList<CustomCommand> commands = new ArrayList<>();
		if(customInfo != null && customInfo.size() > 0) {
			for(Map<?,?> contract : customInfo) {
				if(contract.size() >= FIELDS.length) {
					CustomCommand newCmd = new CustomCommand();
					newCmd.setCommand((String) contract.get(COMMAND));
					newCmd.setBaseUrl((String) contract.get(BASE_URL));
					newCmd.setFileFormat((String) contract.get(FILE_FORMAT));
					newCmd.setMax(Integer.parseInt((String) contract.get(MAX)));
					LOGGER.info("Registered new command: {}", newCmd);
					commands.add(newCmd);
				}
			}
		}
		return commands;
	}
}
