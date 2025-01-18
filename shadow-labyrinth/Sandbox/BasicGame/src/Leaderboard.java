import nl.saxion.app.CsvReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Leaderboard {
    public static final String FILE_NAME = "leaderboard.csv";

    public static void saveTime(long totalTime) {
        // Write the total time (in seconds) in the leaderboard.csv file
        try (FileWriter writer = new FileWriter(FILE_NAME, true)) {
            writer.append(String.valueOf(totalTime / 1000.0))
                    .append("\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Double> getTop10Times() {
        ArrayList<Double> allTimes = new ArrayList<>();

        CsvReader reader = new CsvReader("leaderboard.csv");
        reader.setSeparator(',');

        // Read all the times in the csv file and add them to the arraylist
        while (reader.loadRow()){
            double time = reader.getDouble(0);
                allTimes.add(time);
        }

        // Sort the times using the Collections method
        Collections.sort(allTimes);

        // Create a sublist with the first 10 sorted times in the arraylist and return the list
        if (allTimes.size() > 10) {
            allTimes = new ArrayList<>(allTimes.subList(0, 10));
        }
        return allTimes;
    }
}
