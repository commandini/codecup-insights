package model.base;

public enum PlayerOrdinal {
    PLAYER_1("Player 1"), PLAYER_2("Player 2");
    private final String value;

    PlayerOrdinal(String value) {
        this.value = value;
    }

    public static PlayerOrdinal fromValue(String value) {
        for (PlayerOrdinal playerOrdinal : values()) {
            if (playerOrdinal.value.equalsIgnoreCase(value)) {
                return playerOrdinal;
            }
        }
        return null;
    }
}
