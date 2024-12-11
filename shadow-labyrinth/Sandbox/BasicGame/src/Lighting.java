import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import nl.saxion.app.SaxionApp;

public class Lighting {

    private final Player player;
    private final int screenWidth, screenHeight;
    private int circleSize;
    private File tempImageFile; // Temporary image file that stores the light/darkness effect that will be drawn on the screen

    public Lighting(Player player, int screenWidth, int screenHeight, int circleSize) {
        this.player = player;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.circleSize = circleSize;

        try {
            createDarknessFilter();
        } catch (IOException e) {
            throw new RuntimeException("Failed to create darkness filter image.", e);
        }
    }

    // For resizing the visible circle area
    public void update(Player player, int circleSize) {
        this.circleSize = circleSize; // Update size
        try {
            createDarknessFilter(); // Re-generate the light filter with the new player position
        } catch (IOException e) {
            throw new RuntimeException("Failed to update darkness filter image.", e);
        }
    }

    private void createDarknessFilter() throws IOException {
        BufferedImage darknessFilter = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) darknessFilter.getGraphics();

        Area screenArea = new Area(new Rectangle2D.Double(0, 0, screenWidth, screenHeight));

        int centerX = player.screenX;
        int centerY = player.screenY;

        double x = centerX - (circleSize / 2.0);
        double y = centerY - (circleSize / 2.0);

        Shape circleShape = new Ellipse2D.Double(x, y, circleSize, circleSize);
        Area lightArea = new Area(circleShape);

        screenArea.subtract(lightArea);

        Color[] colors = {
                new Color(0, 0, 0, 0.1f),
                new Color(0, 0, 0, 0.42f),
                new Color(0, 0, 0, 0.76f),
                new Color(0, 0, 0, 0.98f)
        };
        float[] fractions = {0f, 0.5f, 0.8f, 1f};

        RadialGradientPaint gradientPaint = new RadialGradientPaint(
                centerX, centerY, circleSize / 2.0f, fractions, colors
        );

        g2.setPaint(gradientPaint);
        g2.fill(lightArea);

        g2.setColor(new Color(0, 0, 0, 0.95f));
        g2.fill(screenArea);

        g2.dispose();

        tempImageFile = File.createTempFile("darknessFilter", ".png");
        ImageIO.write(darknessFilter, "png", tempImageFile);
        tempImageFile.deleteOnExit();
    }

    public void setCircleSize(int newCircleSize) {
        this.circleSize = newCircleSize; // Update the circle size to the new provided value
        try {
            createDarknessFilter(); // Re-generate the light effect (darkness filter) based on the new circle size
        } catch (IOException e) {
            throw new RuntimeException("Failed to resize darkness filter image.", e);
        }
    }

    private boolean enabled = true; //activate or deactivate vision impairment

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void draw() {
        if (enabled && tempImageFile != null) {
            SaxionApp.drawImage(tempImageFile.getAbsolutePath(), 0, 0);
        }
    }
}


