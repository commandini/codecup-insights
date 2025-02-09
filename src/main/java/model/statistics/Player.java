package model.statistics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.math.Stats;
import model.base.Game;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Player {
    private static final int MINIMUM_WIN_COUNT_DIFFERENCE_REQUIRED_FOR_STRENGTH = 4;
    private static final int SQUEAKER_THRESHOLD = 2;
    private static final int SQUEAKER_WIN_POINTS = 200 + SQUEAKER_THRESHOLD;
    private static final int SQUEAKER_LOSE_POINTS = 100 - SQUEAKER_THRESHOLD;

    @JsonIgnore
    private final Map<Player, Set<Game>> gameHistory;

    // unique identifier
    @JsonProperty
    private final String name;

    // Points related statistics
    @JsonProperty
    private long points;
    @JsonProperty
    private int maxPoints;
    @JsonProperty
    private int minPoints;
    @JsonProperty
    private double averagePoints;
    @JsonProperty
    private double standardDeviationOfPoints;

    // Game status related statistics
    @JsonProperty
    private int wins;
    @JsonProperty
    private int draws;
    @JsonProperty
    private int losses;
    @JsonProperty
    private int fails;
    @JsonProperty
    private int squeakerWins;
    @JsonProperty
    private int squeakerLosses;

    @JsonProperty
    private SubjectivePlayerType subjectivePlayerType;

    public Player() {
        this("");
    }

    public Player(String name) {
        this.name = name;
        this.gameHistory = new HashMap<>();
    }

    public void reset() {
        gameHistory.clear();
        points = 0;
        maxPoints = 0;
        minPoints = 0;
        averagePoints = 0;
        standardDeviationOfPoints = 0;
        wins = 0;
        draws = 0;
        losses = 0;
        fails = 0;
        squeakerWins = 0;
        squeakerLosses = 0;
    }

    public void registerGame(Game game) {
        Player firstPlayer = game.firstPlayer();
        Player secondPlayer = game.secondPlayer();
        if (firstPlayer.equals(this)) {
            gameHistory.computeIfAbsent(secondPlayer, key -> new HashSet<>());
            gameHistory.get(secondPlayer).add(game);
        } else if (secondPlayer.equals(this)) {
            gameHistory.computeIfAbsent(firstPlayer, key -> new HashSet<>());
            gameHistory.get(firstPlayer).add(game);
        }
    }

    public void computeStatistics() {
        computePointsRelatedStatistics();
        computeGameStatusRelatedStatistics();
    }

    private void computeGameStatusRelatedStatistics() {
        for (Set<Game> games : gameHistory.values()) {
            for (Game game : games) {
                if (game.firstPlayer().equals(this)) {
                    if (game.firstPlayerScore() == 0) {
                        fails++;
                    }
                    if (game.firstPlayerScore() > game.secondPlayerScore()) {
                        wins++;
                        if (game.firstPlayerScore() <= SQUEAKER_WIN_POINTS && game.secondPlayerScore() >= SQUEAKER_LOSE_POINTS) {
                            squeakerWins++;
                        }
                    }
                    if (game.firstPlayerScore() == game.secondPlayerScore()) {
                        draws++;
                    }
                    if (game.firstPlayerScore() < game.secondPlayerScore()) {
                        losses++;
                        if (game.firstPlayerScore() >= SQUEAKER_LOSE_POINTS && game.secondPlayerScore() <= SQUEAKER_WIN_POINTS) {
                            squeakerLosses++;
                        }
                    }
                } else {
                    if (game.secondPlayerScore() == 0) {
                        fails++;
                    }
                    if (game.secondPlayerScore() > game.firstPlayerScore()) {
                        wins++;
                        if (game.secondPlayerScore() <= SQUEAKER_WIN_POINTS && game.firstPlayerScore() >= SQUEAKER_LOSE_POINTS) {
                            squeakerWins++;
                        }
                    }
                    if (game.secondPlayerScore() == game.firstPlayerScore()) {
                        draws++;
                    }
                    if (game.secondPlayerScore() < game.firstPlayerScore()) {
                        losses++;
                        if (game.secondPlayerScore() >= SQUEAKER_LOSE_POINTS && game.firstPlayerScore() <= SQUEAKER_WIN_POINTS) {
                            squeakerLosses++;
                        }
                    }
                }
            }
            subjectivePlayerType = determinePlayerLeague(wins - losses);
        }
    }

    private SubjectivePlayerType determinePlayerLeague(int delta) {
        if (delta >= MINIMUM_WIN_COUNT_DIFFERENCE_REQUIRED_FOR_STRENGTH) {
            return SubjectivePlayerType.Stronger;
        } else if (delta <= -MINIMUM_WIN_COUNT_DIFFERENCE_REQUIRED_FOR_STRENGTH) {
            return SubjectivePlayerType.Weaker;
        }
        return SubjectivePlayerType.Challenger;
    }

    private void computePointsRelatedStatistics() {
        int[] pointsPerGame = pointsPerGame();
        points = Arrays.stream(pointsPerGame).asLongStream().sum();
        averagePoints = format(Arrays.stream(pointsPerGame).average().orElse(1.0));
        maxPoints = Arrays.stream(pointsPerGame).max().orElse(0);
        minPoints = Arrays.stream(pointsPerGame).min().orElse(0);
        standardDeviationOfPoints = format(Stats.of(Arrays.stream(pointsPerGame).boxed().toList()).populationStandardDeviation());
    }

    private int[] pointsPerGame() {
        int gameCount = gameHistory.values().stream().mapToInt(Set::size).sum();
        int[] result = new int[gameCount];
        int gameIndex = 0;
        for (Set<Game> games : gameHistory.values()) {
            for (Game game : games) {
                if (game.firstPlayer().equals(this)) {
                    result[gameIndex++] = game.firstPlayerScore();
                } else {
                    result[gameIndex++] = game.secondPlayerScore();
                }
            }
        }
        return result;
    }

    private double format(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public String name() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
