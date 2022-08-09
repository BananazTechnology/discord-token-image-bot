package tech.bananaz.discordnftbot.models;

import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.user.User;

import lombok.Data;

@Data
public class EventMessage {

	ServerTextChannel channel;
	User     		  user;
	CustomCommand     commandInfo;
	int 			  selection;
	
	public EventMessage() { }
	
	public EventMessage(ServerTextChannel channel, User user, int selection, CustomCommand customInfo) {
		this.channel     = channel;
		this.user        = user;
		this.commandInfo = customInfo;
		this.selection   = selection;
	}
}
