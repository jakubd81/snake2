package com.example.jakub.snake2;
import android.content.SharedPreferences;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Jakub on 02.12.2015.
 */
public class ScoresTable
{
    public static int MAX_ENTRIES_SIZE = 10;
    private static final short NO_PLAYER_ENTRY = -1;
    private final int m_maxEntriesSize;

    public static LinkedHashMap<String, Integer> retrieveSortedEntries()
    {
        SharedPreferences sharedPreferences = ResourceManager.getInstance().getSharedPreferences();
        Map<String, ?> sharedTable = sharedPreferences.getAll();
        List<Map.Entry> highScoresList = new LinkedList<Map.Entry>(sharedTable.entrySet());
        Collections.sort(highScoresList, new Comparator<Map.Entry>() {

            public int compare(Map.Entry firstEntry, Map.Entry secondEntry)
            {
                int firstValue = (int)firstEntry.getValue();
                int secondValue = (int)secondEntry.getValue();

                return firstValue < secondValue ? 1 : firstValue > secondValue ? -1 : 0;
            }
        });

        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
        Iterator<Map.Entry> it = highScoresList.iterator();

        //put sorted entries into the Map
        while (it.hasNext())
        {
            Map.Entry entry = it.next();
            String playerName = (String)entry.getKey();
            int score = (Integer)entry.getValue();
            sortedMap.put(playerName, score);
        }

        return sortedMap;
    }

    public ScoresTable(int maxEntriesSize)
    {
        m_maxEntriesSize = maxEntriesSize;
    }

    public boolean checkIfAddNewScore(int finalScore)
    {
        boolean addScore = false;
        int tableSize = ResourceManager.getInstance().getPreferencesSize();

        if (tableSize < m_maxEntriesSize)
        {
            addScore = true;
        }
        else
        {
            // we have ten slots in table
            // check if there's entry with lower score
            LinkedHashMap<String, Integer> sortedHsTable = retrieveSortedEntries();
            Iterator it = sortedHsTable.entrySet().iterator();

            while (it.hasNext())
            {
                Map.Entry entry = (Map.Entry)it.next();
                int entryScore = (int)entry.getValue();

                if (entryScore < finalScore)
                {
                    addScore = true;
                    break;
                }
            }
        }

        return addScore;
    }

    public void addPlayerScore(String playerName, int currentScore)
    {
        SharedPreferences sharedPreferences = ResourceManager.getInstance().getSharedPreferences();
        int playerScore = sharedPreferences.getInt(playerName, NO_PLAYER_ENTRY);

        // player name already exists in the table
        if (NO_PLAYER_ENTRY != playerScore)
        {
            // change score to higher
            if (currentScore > playerScore)
            {
                addHighScore(playerName, currentScore);
            }
        }
        else
        {
            // new player
            int entriesInTable = ResourceManager.getInstance().getPreferencesSize();

            if (m_maxEntriesSize == entriesInTable)
            {
                removeLastPlayer();
            }

            addHighScore(playerName, currentScore);
        }
    }

    private void removeLastPlayer()
    {
        Map.Entry lastEntry = getLastEntry();

        if (null != lastEntry)
        {
            String playerToDelete = (String)lastEntry.getKey();
            SharedPreferences sharedPreferences = ResourceManager.getInstance().getSharedPreferences();
            SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
            preferencesEditor.remove(playerToDelete);
            preferencesEditor.apply();
        }
        else
        {
            System.out.println("ERROR: ScoresTable::manageNewScore()");
        }
    }

    private Map.Entry getLastEntry()
    {
        LinkedHashMap<String, Integer> sortedHsTable = retrieveSortedEntries();
        Iterator it = sortedHsTable.entrySet().iterator();
        Map.Entry lastElement = null;

        while (it.hasNext())
        {
            lastElement = (Map.Entry)it.next();
        }

        return lastElement;
    }

    private void addHighScore(String playerName, int finalScore)
    {
        SharedPreferences sharedPreferences = ResourceManager.getInstance().getSharedPreferences();
        SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
        preferencesEditor.putInt(playerName, finalScore);
        preferencesEditor.apply();
    }
}
