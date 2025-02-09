package model.base;

import model.statistics.Player;

import java.util.Objects;

public class Game {
    private final int gameId;
    private Player firstPlayer;
    private Player secondPlayer;
    private int firstPlayerScore;
    private int secondPlayerScore;

    public Game(int gameId) {
        this.gameId = gameId;
    }

    public Player firstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(Player firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public Player secondPlayer() {
        return secondPlayer;
    }

    public void setSecondPlayer(Player secondPlayer) {
        this.secondPlayer = secondPlayer;
    }

    public int firstPlayerScore() {
        return firstPlayerScore;
    }

    public void setFirstPlayerScore(int firstPlayerScore) {
        this.firstPlayerScore = firstPlayerScore;
    }

    public int secondPlayerScore() {
        return secondPlayerScore;
    }

    public void setSecondPlayerScore(int secondPlayerScore) {
        this.secondPlayerScore = secondPlayerScore;
    }

    public boolean isParticipant(Player player) {
        return player.equals(firstPlayer) || player.equals(secondPlayer);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return gameId == game.gameId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(gameId);
    }
}
