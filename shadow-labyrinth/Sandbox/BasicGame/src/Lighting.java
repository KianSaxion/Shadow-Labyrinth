import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nl.saxion.app.SaxionApp;

public class Lighting {

    private final Player player;
    private final int screenWidth, screenHeight;
    private int circleSize;
    private List<Torch> torches;
    private File tempImageFile; // Temporary image file for the light filter
    private boolean enabled = true; // Toggle visibility

    public Lighting(Player player, int screenWidth, int screenHeight, int circleSize) {
        this.player = player;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.circleSize = circleSize;
        this.torches = new ArrayList<>();
    }

    // Update circle size and re-generate the filter
    public void update(Player player, int circleSize) {
        this.circleSize = circleSize;
        try {
            createDarknessFilter(); // Recreate the darkness filter with the updated circle size
        } catch (IOException e) {
            throw new RuntimeException("Failed to update darkness filter image.", e);
        }
    }

    public void addTorch(Torch torch) {
        torches.add(torch);
    }

    private void createDarknessFilter() throws IOException {
        // Creates the darkness filter with a light gradient around the player.
        BufferedImage darknessFilter = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) darknessFilter.getGraphics();

        // Get the center coordinates of the light circle
        int centerX = player.screenX;
        int centerY = player.screenY;

        // Define gradient colors and fractions
        Color[] colors = {
                new Color(0, 0, 0, 0f),
                new Color(0, 0, 0, 0.32f),
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
        g2.fillRect(0, 0, screenWidth, screenHeight);

        // Add torches' light effect
        for (Torch torch : torches) {
            int torchX = torch.getWorldX();
            int torchY = torch.getWorldY();

            int torchScreenX = torchX - player.worldX + player.screenX;
            int torchScreenY = torchY - player.worldY + player.screenY;

            g2.setComposite(AlphaComposite.Clear); // Make it clear
            g2.fillOval(torchScreenX - torch.getLightRadius() / 2, torchScreenY - torch.getLightRadius() / 2,
                    torch.getLightRadius(), torch.getLightRadius());
        }

        // Dispose of the Graphics2D object to free resources
        g2.dispose();

        // Save to a temporary image file for SaxionApp
        tempImageFile = File.createTempFile("darknessFilter", ".png");
        ImageIO.write(darknessFilter, "png", tempImageFile);
        tempImageFile.deleteOnExit(); // Delete temp file when closing the game
    }

    // Easy adjustment of circleSize
    public void setCircleSize(int newCircleSize) {
        this.circleSize = newCircleSize; // Update the circle size to the new provided value
        try {
            createDarknessFilter(); // Re-generate the darkness filter based on new circle size
        } catch (IOException e) {
            throw new RuntimeException("Failed to resize darkness filter image.", e);
        }
    }

    // Toggles visibility
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    // Draws the darkness filter image onto the screen
    public void draw() {
        if (enabled && tempImageFile != null) {
            SaxionApp.drawImage(tempImageFile.getAbsolutePath(), 0, 0);
        }
    }
}