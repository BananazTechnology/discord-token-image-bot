package tech.bananaz.discordnftbot.utils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

import javax.imageio.ImageIO;
import static java.util.Objects.nonNull;

public class ImageUtils {
	
	public static BufferedImage getImage(String url, Optional<Integer> retryCount, Optional<Integer> retryDelay, Optional<Integer> timeout) throws IOException {
	    int maxRetries = (nonNull(retryCount)) ? retryCount.get() : 3;
	    int retryDelayMillis = (nonNull(retryDelay)) ? retryDelay.get() : 1000; // 1 second
	    int connectionTimeout = (nonNull(timeout)) ? timeout.get() : 5000; // 5 second timeout

	    for (int retry = 0; retry < maxRetries; retry++) {
	        try {
	            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
	            connection.setConnectTimeout(connectionTimeout);
	            connection.connect();
	            BufferedImage image = ImageIO.read(connection.getInputStream());
	            connection.disconnect();
	            return image;
	        } catch (IOException e) {
	            e.printStackTrace();
	            if (retry < maxRetries - 1) {
	                try {
	                    Thread.sleep(retryDelayMillis);
	                } catch (InterruptedException ignored) {
	                }
	            }
	        }
	    }
	    throw new IOException("Failed to fetch image after " + maxRetries + " retries.");
	}

}
