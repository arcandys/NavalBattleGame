package com.example.battleships_client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.FileWriter;
import java.io.IOException;

@Service
public class BattleshipsGameService {

    private static RestTemplate restTemplate;
    private char[][] gameGrid;

    private String gameId = "123";
    private static String teamName = "crousti_poulet";
    private static int shipsLeft = 20;

    public static int totalShotsFired = 0;

    public BattleshipsGameService() {
        this.restTemplate = new RestTemplate();
        this.gameGrid = initializeGrid();
        this.shipsLeft = shipsLeft;
    }

    static {
        restTemplate = new RestTemplate();
    }

    public <T> ResponseEntity<T> sendRequest(String url, HttpMethod method, HttpEntity<?> req, Class<T> res) {
        return restTemplate.exchange(url, method, req, res);
    }

    public String getGameId() {
        return this.gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public char[][] getGameGrid() {
        return gameGrid;
    }

    public void setGameGrid(char[][] gameGrid) {
        this.gameGrid = gameGrid;
    }

    private char[][] initializeGrid() {
        int rows = 10;
        int cols = 10;

        char[][] grid = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = '~';
            }
        }
        return grid;
    }

    public void createRecord(String clientName, String serverName, String suffix) {
        try (FileWriter writer = new FileWriter(clientName + "-" + serverName + "-" + suffix + ".txt", true)) {
            writer.write(System.lineSeparator());
            writer.write("Total Shots Fired: " + totalShotsFired);
            writer.flush();
            System.out.println("Shots fired recorded successfully.");
        } catch (IOException e) {
            System.err.println("Error recording shots fired: " + e.getMessage());
        }
    }


    public void addShots() {
        totalShotsFired++;
    }

    public void shipSunk() {
        shipsLeft--;
        System.out.println("ship -1");
        System.out.println("total = " + shipsLeft);
        if (shipsLeft == 0) {
            endGame();
        }
    }

    public void endGame() {
        System.out.println("Game over - All ships sunk!");
        System.out.println("Sending termination request to /game/stop with gameId: " + gameId);
        String stopGameUrl = "http://localhost:8081/game/stop?teamName=" + teamName + "&gameId=" + gameId;
        createRecord(teamName, "server", gameId);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(stopGameUrl, null, String.class);
        shipsLeft = 20;
    }
}
