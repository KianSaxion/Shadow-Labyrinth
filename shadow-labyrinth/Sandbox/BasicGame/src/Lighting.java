import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import nl.saxion.app.SaxionApp;

public class Lighting {

    private  final Player player;
    private static final int screenWidth = 768;
    private static final int screenHeight = 576;
    private static int circleSize;
    public static File tempImageFile; // Temporary image file for the light filter
    private boolean enabled = false; // Toggle visibility

    public Lighting(Player player, int circleSize) {
        this.player = player;
        this.circleSize = circleSize;
    }

    // Update circle size and re-generate the filter
    public static void update() {
        try {
            createDarknessFilter(); // Recreate the darkness filter with the updated circle size
        } catch (IOException e) {
            throw new RuntimeException("Failed to update darkness filter image.", e);
        }
    }

    private static void createDarknessFilter() throws IOException {
        // Creates the darkness filter with a light gradient around the player.
        BufferedImage darknessFilter = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = getGraphics2D(darknessFilter);
        g2.fillRect(0, 0, screenWidth, screenHeight);

        // Dispose of the Graphics2D object to free resources
//        g2.dispose();


        // Save to a temporary image file for SaxionApp
        tempImageFile = File.createTempFile("darknessFilter", ".png");
        ImageIO.write(darknessFilter, "png", tempImageFile);
        tempImageFile.deleteOnExit(); // Delete temp file when closing the game
    }

    private static Graphics2D getGraphics2D(BufferedImage darknessFilter) {
        Graphics2D g2 = (Graphics2D) darknessFilter.getGraphics();

        // Get the center coordinates of the light circle
        int centerX = 768 / 2;
        int centerY = 576 / 2;

        // Define gradient colors and fractions
        Color[] colors = {
                new Color(0, 0, 0, 0.1f),
                new Color(0, 0, 0, 0.42f),
                new Color(0, 0, 0, 0.76f),
                new Color(0, 0, 0, 0.98f)
        };
        float[] fractions = {0f, 0.5f, 0.8f, 1f}; // Fractions for gradient progression

        // Create a radial gradient paint centered at the light circle
        RadialGradientPaint gradientPaint = new RadialGradientPaint(
                centerX, centerY, circleSize / 2.0f, fractions, colors
        );

        // Apply the gradient paint to the entire screen
        g2.setPaint(gradientPaint);
        return g2;
    }


    // Draws the darkness filter image onto the screen
    public void draw() {
        if (enabled && tempImageFile != null) {
            SaxionApp.drawImage(tempImageFile.getAbsolutePath(), 0, 0);
        }
    }
}