import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import nl.saxion.app.SaxionApp;

public class Lighting {

    private static final int screenWidth = Variable.SCREEN_WIDTH;
    private static final int screenHeight = Variable.SCREEN_HEIGHT;
    private static int circleSize = 400; // Default light radius
    private static File filterDefault, filterLarge; // File objects for the two filter images
    private static File currentFilter; // The currently used filter (default or large)

    private static final boolean ENABLED = true; // Toggle visibility

    // Call this once to initialize filters (called in KeyHandler atm)
    public static void initializeFilters() {
        try {
            filterDefault = createDarknessFilter(400); // Default radius
            filterLarge = createDarknessFilter(600);   // Larger radius
            currentFilter = filterDefault;             // Start with the default filter
//            System.out.println("Filters initialized successfully!");
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize darkness filter images.", e);
        }
    }

    // Update the lighting filter
    public static void updateFilter(int newCircleSize) {
        // Only update if the circle size has changed
        if (circleSize != newCircleSize) {
            circleSize = newCircleSize;
            try {
                if (circleSize == 600) {
                    currentFilter = filterLarge;
                } else {
                    currentFilter = filterDefault;
                }
//                System.out.println("Filter updated to: " + (circleSize == 600 ? "Large" : "Default"));
            } catch (Exception e) {
                System.out.println("Error updating filter: " + e.getMessage());
            }
        }
    }

    private static File createDarknessFilter(int radius) throws IOException {
        BufferedImage darknessFilter = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = getGraphics2D(darknessFilter, radius);
        g2.fillRect(0, 0, screenWidth, screenHeight);
        g2.dispose();

        // Save the filter to a temporary file
        File tempFile = File.createTempFile("darknessFilter", ".png");
        ImageIO.write(darknessFilter, "png", tempFile);
        tempFile.deleteOnExit();
        return tempFile;
    }

    private static Graphics2D getGraphics2D(BufferedImage darknessFilter, int radius) {
        Graphics2D g2 = (Graphics2D) darknessFilter.getGraphics();

        int centerX = screenWidth / 2;
        int centerY = screenHeight / 2;

        Color[] colors = {
                new Color(0, 0, 0, 0.1f),
                new Color(0, 0, 0, 0.42f),
                new Color(0, 0, 0, 0.76f),
                new Color(0, 0, 0, 0.98f)
        };
        float[] fractions = {0f, 0.5f, 0.8f, 1f};

        RadialGradientPaint gradientPaint = new RadialGradientPaint(
                centerX, centerY, radius / 2.0f, fractions, colors
        );

        g2.setPaint(gradientPaint);
        return g2;
    }

    // Draw the current filter on the screen
    public static void draw() {
        if (ENABLED && currentFilter != null) {
            SaxionApp.drawImage(currentFilter.getAbsolutePath(), 0, 0);
        }
    }
}