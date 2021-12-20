package telran.bc.dto;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeMap;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    TreeMap<LocalDateTime, Game> games;
    private String name;
    private long id;
    private long nonce = 0L;

    public User(long userId, String name) {
        this.name = name;
        this.games = new TreeMap<>();
        this.id = userId;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public List<Game> getGames(LocalDateTime from, LocalDateTime to) {
        return games.subMap(from, true, to.withSecond(59).withNano(999999999), true).entrySet().stream().map(e -> e.getValue()).toList();
    }

    public Game startGame(String gamePath) {
        System.out.println("new game created");
        return new Game(++nonce, gamePath);
    }

    public void saveGame(Game currentGame, String path) throws IOException {
        if (!currentGame.isActive()) {
            LocalDateTime time = currentGame.getTimeEnd();
            games.put(time, currentGame);
            saveGameToDataFormat(currentGame, path, time);
            saveGameToTxtFormat(currentGame, path, time);
        }
    }

    private void saveGameToTxtFormat(Game currentGame, String path, LocalDateTime time) throws FileNotFoundException, IOException {
        String filePath = String.format("%s/%d_%s_%d_%d_%d_%d_%d.txt", path, getId(), getName(), time.getYear(),
                time.getMonth().getValue(), time.getDayOfMonth(), time.getHour(), time.getMinute());
        try (PrintWriter writer = new PrintWriter(filePath)) {
            currentGame.getMoves().stream()
                    .forEach(m -> writer.println(m.toString()));
            System.out.println("Game has been saved to: " + filePath);
        }
    }

    private void saveGameToDataFormat(Game currentGame, String path, LocalDateTime time)
            throws IOException, FileNotFoundException {
        String filePath = String.format("%s/%d_%s_%d_%d_%d_%d_%d.data", path, getId(), getName(), time.getYear(),
                time.getMonth().getValue(), time.getDayOfMonth(), time.getHour(), time.getMinute());
        try (ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(filePath))) {
            writer.writeObject(currentGame);
            System.out.println("Game has been saved to: " + filePath);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (id != other.id)
            return false;
        return true;
    }
}
