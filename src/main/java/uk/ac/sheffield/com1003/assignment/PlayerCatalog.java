package uk.ac.sheffield.com1003.assignment;

import uk.ac.sheffield.com1003.assignment.codeprovided.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * SKELETON IMPLEMENTATION
 */
public class PlayerCatalog extends AbstractPlayerCatalog
{
    /**
     * Constructor
     */
    public PlayerCatalog(String eplFilename, String ligaFilename) {
        super(eplFilename, ligaFilename);
    }

    @Override
    public PlayerPropertyMap parsePlayerEntryLine(String line) throws IllegalArgumentException
    {



        // Split the line using comma as the delimiter
        String[] parts = line.split(",");

        // Check if the line has enough values for the PlayerDetail, PlayerProperty enums, and line number


        // Create a new PlayerPropertyMap
        PlayerPropertyMap playerPropertyMap = new PlayerPropertyMap();

        // Skip the line number (index 0), start from index 1


        if (parts.length != PlayerDetail.values().length + PlayerProperty.values().length ) {
            throw new IllegalArgumentException(line +"is malformed, does not include every property for a single player");
        }
        // Iterate through PlayerDetail enum and set the details in the map using int i
        PlayerDetail[] playerDetails = PlayerDetail.values();
        PlayerProperty[] playerProperties = PlayerProperty.values();
        for (int i = 0; i < playerDetails.length; i++) {
            playerPropertyMap.putDetail(playerDetails[i], parts[i]);
        }

        for( int i = 0; i< playerProperties.length;i++){
            playerPropertyMap.put(PlayerProperty.values()[i],Double.valueOf(parts[i+4]));
        }

        // Iterate through PlayerProperty enum and set the properties in the map

        for (int i = 0; i < playerProperties.length; i++) {
            try {
                playerPropertyMap.put(playerProperties[i], Double.valueOf(parts[i + 4]));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Line contains undefined properties or invalid data: " + e.getMessage());
            }
        }
        return playerPropertyMap;

    }

    @Override
    public void updatePlayerCatalog() {
        // Create a new ArrayList to store all player entries
        ArrayList<PlayerEntry> allPlayerEntries = new ArrayList<>();
        // Add Premier League player entries to the list
        allPlayerEntries.addAll(getPlayerEntriesList(League.EPL));
        // Add La Liga player entries to the list
        allPlayerEntries.addAll(getPlayerEntriesList(League.LIGA));
        // Update the playerEntriesMap with the combined list of player entries
        playerEntriesMap.put(League.ALL, allPlayerEntries);
    }

    @Override


    public double getMinimumValue(PlayerProperty playerProperty, List<PlayerEntry> playerEntryList)
            throws NoSuchElementException {
        return playerEntryList.stream()
                .mapToDouble(playerEntry -> playerEntry.getProperty(playerProperty))
                .min()
                .orElseThrow(() -> new NoSuchElementException("No value present"));

    }





    @Override
    public double getMaximumValue(PlayerProperty playerProperty, List<PlayerEntry> playerEntryList) {

        return playerEntryList.stream()
                .mapToDouble(playerEntry -> playerEntry.getProperty(playerProperty))
                .max()
                .orElseThrow(() -> new NoSuchElementException("No value present"));
        }











    @Override
    public double getMeanAverageValue(PlayerProperty playerProperty, List<PlayerEntry> playerEntryList)
            throws NoSuchElementException {
        // Use Java 8 streams to find the sum of the player property values and the count of non-null property values
        double sum = 0.0;
        int count = 0;

        for (PlayerEntry entry : playerEntryList) {
            try {
                sum += entry.getProperty(playerProperty);
                count++;
            } catch (NullPointerException e) {
                // Ignore the entry with a missing property
                return -1;
            }
        }

        if (count == 0) {
            throw new NoSuchElementException("No elements found for the specified player property.");
        }

        return sum / count;

    }

    @Override
    public List<PlayerEntry> getFirstFivePlayerEntries(League type)
    {
        // Use Java 8 streams to get the first five player entries for the given league type
        return getPlayerEntriesList(type).stream()
                .limit(5)
                .collect(Collectors.toList());

    }

}
