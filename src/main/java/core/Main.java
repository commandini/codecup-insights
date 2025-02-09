package core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import model.base.CompetitionRecord;
import model.base.Game;
import model.base.PlayerOrdinal;
import model.statistics.Competition;
import model.statistics.Player;
import service.CompetitionLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static model.base.GameMode.FINAL;
import static model.base.PlayerOrdinal.PLAYER_1;
import static model.base.PlayerOrdinal.PLAYER_2;
import static service.FileService.deleteFilesRecursively;
import static service.FileService.getResourceFile;

public class Main {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .enable(SerializationFeature.INDENT_OUTPUT);

    public static void main(String[] args) throws IOException {
        deleteFilesRecursively(new File("data"));
        File competitionRecordsFile = getResourceFile("codecup_2025_final.csv");
        List<CompetitionRecord> finalCompetitionRecords = new CompetitionLoader().load(competitionRecordsFile)
                .stream().filter(competitionRecord -> FINAL.equals(competitionRecord.gameMode())).toList();
        List<Player> players = constructPlayers(finalCompetitionRecords);
        Map<String, Player> playerLookupByName = playerLookupByName(players);
        List<Game> games = constructGames(finalCompetitionRecords, playerLookupByName);
        players.forEach(player -> games.forEach(player::registerGame));
        players.forEach(Player::computeStatistics);
        Competition competition = new Competition(games);
        OBJECT_MAPPER.writeValue(new File("data/competition.json"), competition);
        OBJECT_MAPPER.writeValue(new File("data/players.json"), players);
        dissectAndSerializeStatisticsPerPlayer(players, games);
    }

    private static void dissectAndSerializeStatisticsPerPlayer(List<Player> players, List<Game> games) throws IOException {
        for (Player referencePlayer : players) {
            List<Player> dissectionForReferencePlayer = new ArrayList<>();
            for (Player otherPlayer : players) {
                otherPlayer.reset();
                if (!otherPlayer.equals(referencePlayer)) {
                    games.stream().filter(game -> game.isParticipant(referencePlayer) && game.isParticipant(otherPlayer))
                            .forEach(otherPlayer::registerGame);
                    dissectionForReferencePlayer.add(otherPlayer);
                }
            }
            dissectionForReferencePlayer.forEach(Player::computeStatistics);
            OBJECT_MAPPER.writeValue(new File("data/dissection/" + referencePlayer.name() + ".json"), dissectionForReferencePlayer);
        }
    }

    private static List<Game> constructGames(List<CompetitionRecord> competitionRecords, Map<String, Player> playerLookupByName) {
        Map<Integer, Game> gameLookupById = new HashMap<>();
        for (CompetitionRecord competitionRecord : competitionRecords) {
            int gameId = competitionRecord.gameId();
            gameLookupById.computeIfAbsent(gameId, key -> new Game(gameId));
            Game game = gameLookupById.get(gameId);
            Player player = playerLookupByName.get(competitionRecord.user());
            PlayerOrdinal playerOrdinal = competitionRecord.playerOrdinal();
            int score = competitionRecord.score();
            if (PLAYER_1.equals(playerOrdinal)) {
                game.setFirstPlayer(player);
                game.setFirstPlayerScore(score);
            } else if (PLAYER_2.equals(playerOrdinal)) {
                game.setSecondPlayer(player);
                game.setSecondPlayerScore(score);
            }
        }
        return new ArrayList<>(gameLookupById.values());
    }

    private static List<Player> constructPlayers(List<CompetitionRecord> competitionRecords) {
        return competitionRecords.stream()
                .map(CompetitionRecord::user)
                .distinct()
                .map(Player::new)
                .toList();
    }

    private static Map<String, Player> playerLookupByName(List<Player> players) {
        return players.stream().collect(Collectors.toMap(Player::name, Function.identity()));
    }
}
