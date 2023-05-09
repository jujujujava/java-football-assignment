package uk.ac.sheffield.com1003.assignment.gui;

import uk.ac.sheffield.com1003.assignment.codeprovided.*;
import uk.ac.sheffield.com1003.assignment.codeprovided.gui.AbstractPlayerDashboardPanel;
import uk.ac.sheffield.com1003.assignment.codeprovided.gui.PlayerEntryDashboard;

import javax.swing.*;

import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SKELETON IMPLEMENTATION
 */

public class PlayerDashboardPanel extends AbstractPlayerDashboardPanel

{

    // Constructor
    public PlayerDashboardPanel(AbstractPlayerCatalog playerCatalog) {
        super(playerCatalog);

    }

    @Override
    public void populatePlayerDetailsComboBoxes() {
        // Get unique player names, nations, positions, and teams from the dataset
        for (PlayerDetail playerDetail: PlayerDetail.values()) {
            JComboBox < String > comboBox;
            switch (playerDetail) {
                case PLAYER:
                    comboBox = comboPlayerNames;
                    comboBox.addItem(" ");
                    break;
                case NATION:
                    comboBox = comboNations;
                    comboBox.addItem(" ");
                    break;
                case POSITION:
                    comboBox = comboPositions;
                    comboBox.addItem(" ");
                    break;
                case TEAM:
                    comboBox = comboTeams;
                    comboBox.addItem(" ");
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + playerDetail);
            }

            List < String > uniqueDetailValues = playerCatalog.getPlayerEntriesList(League.ALL).stream()
                    .map(entry -> entry.getDetail(playerDetail))
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());

            for (String detailValue: uniqueDetailValues) {
                comboBox.addItem(detailValue);
            }

        }
    }

    /**
     * addListeners method - adds relevant actionListeners to the GUI components
     */
    @SuppressWarnings("unused")
    @Override
    public void addListeners() {

        // Add action listener for buttonAddFilter
        buttonAddFilter.addActionListener(e -> {
            // Call the method to add a new filter
            addFilter();

            // Call the method to execute the query
            executeQuery();
        });

        // Add action listener for buttonClearFilters
        buttonClearFilters.addActionListener(e -> {
            // Call the method to clear all filters
            clearFilters();

        });

        // Add action listeners for combo boxes
        ActionListener comboBoxListener = e -> {
            // Call the method to filter player entries based on combo boxes selections
            updatePlayerCatalogDetailsBox();
        };

        comboLeagueTypes.addActionListener(comboBoxListener);
        comboPlayerNames.addActionListener(comboBoxListener);
        comboNations.addActionListener(comboBoxListener);
        comboPositions.addActionListener(comboBoxListener);
        comboTeams.addActionListener(comboBoxListener);
        comboQueryProperties.addActionListener(comboBoxListener);
        comboLeagueTypes.addActionListener(e -> {
            // Call the method to execute the query
            executeQuery();
        });
        comboPlayerNames.addActionListener(e -> {
            // Call the method to execute the query
            executeQuery();
        });
        comboNations.addActionListener(e -> {
            // Call the method to execute the query
            executeQuery();
        });
        comboPositions.addActionListener(e -> {
            // Call the method to execute the query
            executeQuery();
        });
        comboTeams.addActionListener(e -> {
            // Call the method to execute the query
            executeQuery();
        });
        comboQueryProperties.addActionListener(e -> {
            // Call the method to execute the query
            executeQuery();
        });

        // Add action listener for comboRadarChartCategories
        comboRadarChartCategories.addActionListener(e -> {
                    // Call the method to update the properties that the radar chart should display
                    updateRadarChart();
                }
                //
        );
    }

    /**
     * clearFilters method - clears all filters from the subQueryList ArrayList and updates
     * the relevant GUI components
     */
    @Override
    public void clearFilters() {
        // Clear the subQueryList ArrayList
        subQueryList.clear();

        // Reset all combo boxes to their default values (i.e., no filter)

        // Call the method to update the player details panel with no filters applied

        // Clear the JTextArea displaying the list of active filters
        filteredPlayerEntriesTextArea.setText("");
        subQueriesTextArea.setText("");
        statisticsTextArea.setText("");
        subQueriesTextArea.setText("");

    }

    @Override
    public void updateRadarChart() {
        // TODO implement

    }

    /**
     * updateStats method - updates the table with statistics after any changes which may
     * affect the JTable which holds the statistics
     */
    @Override
    public void updateStatistics() {
        statisticsTextArea.setText("");
        statisticsTextArea.append("Filtered Entry Number: " + filteredPlayerEntriesList.size() + "\n");

        // Table header with PlayerProperty names
        statisticsTextArea.append(String.format("%-10s", ""));
        for (PlayerProperty property: PlayerProperty.values()) {
            statisticsTextArea.append(String.format("%-20s", property.getName()));
        }
        statisticsTextArea.append("\n");

        // Row for minimum values
        statisticsTextArea.append(String.format("%-10s", "Min"));
        for (PlayerProperty property: PlayerProperty.values()) {
            double minValue = playerCatalog.getMinimumValue(property, filteredPlayerEntriesList);
            statisticsTextArea.append(String.format("%-20.2f", minValue));
        }
        statisticsTextArea.append("\n");

        // Row for maximum values
        statisticsTextArea.append(String.format("%-10s", "Max"));
        for (PlayerProperty property: PlayerProperty.values()) {
            double maxValue = playerCatalog.getMaximumValue(property, filteredPlayerEntriesList);
            statisticsTextArea.append(String.format("%-20.2f", maxValue));
        }
        statisticsTextArea.append("\n");

        // Row for average values
        statisticsTextArea.append(String.format("%-10s", "Avg"));
        for (PlayerProperty property: PlayerProperty.values()) {
            double avgValue = playerCatalog.getMeanAverageValue(property, filteredPlayerEntriesList);
            statisticsTextArea.append(String.format("%-20.2f", avgValue));
        }
        statisticsTextArea.append("\n");
    }

    /**
     * updatePlayerCatalogDetailsBox method - updates the list of players when changes are made
     */
    @Override
    public void updatePlayerCatalogDetailsBox() {
        StringBuilder sb = new StringBuilder();
        sb.append("League Type      ID      Player's Name" +
                "      Player's Nation      Position in the pitch      Team's name\n");
        for (PlayerEntry entry: filteredPlayerEntriesList) {
            sb.append(entry.getLeagueType()).append("                  ")
                    .append(entry.getId()).append("   ")
                    .append(entry.getDetail(PlayerDetail.PLAYER)).append("   ")
                    .append(entry.getDetail(PlayerDetail.NATION)).append("   ")
                    .append(entry.getDetail(PlayerDetail.POSITION)).append("   ")
                    .append(entry.getDetail(PlayerDetail.TEAM)).append("\n");
        }
        filteredPlayerEntriesTextArea.setText(sb.toString());

    }

    /**
     * executeQuery method - applies chosen query to the relevant list
     */
    @Override
    public void executeQuery() {
        // Get the list of players for the selected league type
        String leagueName = (String) comboLeagueTypes.getSelectedItem();
        League leagueType = League.fromName(leagueName);
        List < PlayerEntry > playerEntries = playerCatalog.getPlayerEntriesList(leagueType);

        // Get the selected values from the combo boxes
        String playerName = (String) comboPlayerNames.getSelectedItem();
        String nation = (String) comboNations.getSelectedItem();
        String position = (String) comboPositions.getSelectedItem();
        String team = (String) comboTeams.getSelectedItem();

        // Filter the player entries based on the selected values
        if (!playerName.equals(" ")) {
            playerEntries = playerCatalog.getPlayerEntriesList(playerEntries, PlayerDetail.PLAYER, playerName);
        }
        if (!nation.equals(" ")) {
            playerEntries = playerCatalog.getPlayerEntriesList(playerEntries, PlayerDetail.NATION, nation);
        }
        if (!position.equals(" ")) {
            playerEntries = playerCatalog.getPlayerEntriesList(playerEntries, PlayerDetail.POSITION, position);
        }
        if (!team.equals(" ")) {
            playerEntries = playerCatalog.getPlayerEntriesList(playerEntries, PlayerDetail.TEAM, team);
        }

        List < PlayerEntry > filteredPlayerEntries = new ArrayList < > ();
        for (PlayerEntry entry: playerEntries) {
            boolean matchesAllSubQueries = true;
            for (SubQuery subQuery: subQueryList) {
                if (!entryMatchesSubQuery(entry, subQuery)) {
                    matchesAllSubQueries = false;
                    break;
                }
            }
            if (matchesAllSubQueries) {
                filteredPlayerEntries.add(entry);
            }

        }

        // Update the filteredPlayerEntriesTextArea with the resulting list of players

        filteredPlayerEntriesList = filteredPlayerEntries;
        updateStatistics();
        updatePlayerCatalogDetailsBox();
    }

    /**
     * addFilters method - adds filters input into GUI to subQueryList ArrayList
     */
    @Override
    public void addFilter() {

        PlayerProperty property = PlayerProperty.fromPropertyName((String) comboQueryProperties.getSelectedItem());
        String operator = (String) comboOperators.getSelectedItem();
        String valueText = value.getText();
        double value;
        try {
            value = Double.parseDouble(valueText);
        } catch (NumberFormatException e) {
            // Show an error message if the value cannot be converted to a double
            JOptionPane.showMessageDialog(this, "Invalid value entered. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Create a new SubQuery object
        SubQuery subQuery = new SubQuery(property, operator, value);

        // Add the new SubQuery object to the subQueryList
        subQueryList.add(subQuery);

        // Update the subquerytextArea to display the new list of filters
        StringBuilder sb = new StringBuilder();
        for (SubQuery query: subQueryList) {
            sb.append(query.toString()).append("; ");
        }
        subQueriesTextArea.setText(sb.toString());

        // Call executeQuery to apply the filters and update Areas 3 and 4
        executeQuery();

    }
    //Create helper private method to check whether PlayerProperty is meeting the specified subQuery
    private boolean entryMatchesSubQuery(PlayerEntry entry, SubQuery subQuery) {
        PlayerProperty playerProperty = subQuery.getPlayerProperty();
        double propertyValue = entry.getProperty(playerProperty);

        switch (subQuery.getOperator()) {
            case ">":
                return propertyValue > subQuery.getValue();
            case ">=":
                return propertyValue >= subQuery.getValue();
            case "<":
                return propertyValue < subQuery.getValue();
            case "<=":
                return propertyValue <= subQuery.getValue();
            case "=":
                return propertyValue == subQuery.getValue();
            case "!=":
                return propertyValue != subQuery.getValue();
            default:
                return false;
        }
    }
    @Override
    public boolean isMinCheckBoxSelected() {
        // TODO implement
        return false;
    }

    @Override
    public boolean isMaxCheckBoxSelected() {
        // TODO implement
        return false;
    }

    @Override
    public boolean isAverageCheckBoxSelected() {
        // TODO implement
        return false;
    }

}
