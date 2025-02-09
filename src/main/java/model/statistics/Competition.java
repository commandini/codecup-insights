package model.statistics;

import com.fasterxml.jackson.annotation.JsonProperty;
import model.base.Game;

import java.util.List;

public class Competition {
    @JsonProperty
    private int firstPlayerWinCount;
    @JsonProperty
    private int secondPlayerWinCount;
    @JsonProperty
    private int drawCount;
    @JsonProperty
    private int firstPlayerPoints;
    @JsonProperty
    private int secondPlayerPoints;

    public Competition(List<Game> games){
        computeStatistics(games);
    }

    private void computeStatistics(List<Game> games) {
        for (Game game : games) {
            if (game.firstPlayerScore() > game.secondPlayerScore()) {
                firstPlayerWinCount++;
            } else if (game.secondPlayerScore() > game.firstPlayerScore()) {
                secondPlayerWinCount++;
            } else {
                drawCount++;
            }
            firstPlayerPoints += game.firstPlayerScore();
            secondPlayerPoints += game.secondPlayerScore();
        }
    }
}
