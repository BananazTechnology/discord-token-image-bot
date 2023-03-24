package tech.bananaz.discordnftbot.utils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.imageio.ImageIO;

public class ImageUtils {
	
	public static BufferedImage getImage(String url) throws IOException {
		HttpURLConnection connection = null;
		connection = (HttpURLConnection) new URL(url).openConnection();
	    connection.connect();
	    BufferedImage image = ImageIO.read(connection.getInputStream());
	    connection.disconnect();
	    return image;
	}

}
