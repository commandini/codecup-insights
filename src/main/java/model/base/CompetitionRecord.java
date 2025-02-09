package model.base;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import csv.converter.GameModeConverter;
import csv.converter.PlayerOrdinalConverter;

public class CompetitionRecord {
    @CsvBindByName(column = "Game")
    private int gameId;
    @CsvBindByName(column = "Round")
    private int round;
    @CsvCustomBindByName(column = "IsSwiss", converter = GameModeConverter.class)
    private GameMode gameMode;
    @CsvCustomBindByName(column = "Player", converter = PlayerOrdinalConverter.class)
    private PlayerOrdinal playerOrdinal;
    @CsvBindByName(column = "User")
    private String user;
    @CsvBindByName(column = "Score")
    private int score;
    @CsvBindByName(column = "Status")
    private GameStatus status;

    public int gameId() {
        return gameId;
    }

    public PlayerOrdinal playerOrdinal() {
        return playerOrdinal;
    }

    public String user() {
        return user;
    }

    public GameMode gameMode() {
        return gameMode;
    }

    public int score() {
        return score;
    }
}
