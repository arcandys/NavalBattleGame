package com.example.battleships_client;

import io.micrometer.common.lang.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class GameController {

    private List<History> historyList = new ArrayList<>();
    private boolean isGameFinished = false;
    private boolean isTileHit = false;
    private boolean isShipSunk = false;
    private final BattleshipsGameService battleshipsGameService;

    @Autowired
    public GameController(BattleshipsGameService battleshipsGameService) {
        this.battleshipsGameService = battleshipsGameService;
    }
    @Autowired
    private static RestTemplate restTemplate;
    static {
        restTemplate = new RestTemplate();
    }

    @GetMapping("/game/start")
    public String battleship(Model model) {
        System.out.println("Starting battleship");

        // Start POST request
        ResponseEntity<char[][]> responseEntity = restTemplate.postForEntity("http://localhost:8080/game/start", null, char[][].class);

        // Check if the game started
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            // Retrieve grid
            char[][] grid = responseEntity.getBody();

            // Create instance
            BattleshipsClient battleshipsClient = new BattleshipsClient();
            battleshipsClient.setGridUpdate(grid);

            // Add grid and BattleshipsClient object to model
            model.addAttribute("grid", grid);
            model.addAttribute("battleshipsClient", battleshipsClient);

            System.out.println("Return battleships");

            return "battleships";
        } else {
            System.out.println("Return error");
            return "error";
        }


    }
    @PostMapping("/game/fire")
    public String fire(@RequestParam int x, @RequestParam int y, RedirectAttributes redirectAttributes, Model model) {
        // GET request
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:8080/game/fire?x=" + x + "&y=" + y, null, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            this.battleshipsGameService.addShots();
            String response = responseEntity.getBody();
            System.out.println("Fire Response: " + response);
            redirectAttributes.addAttribute("response", response);

            if (response.contains("isTileHit: true")) {
                isTileHit = true;
            } else {
                isTileHit = false;
            }
            if (response.contains("isShipSunk: true")) {
                isShipSunk = true;
            } else {
                isShipSunk = false;
            }
            if (response.contains("isGameFinished: true")) {
                isGameFinished = true;
            } else {
                isGameFinished = false;
            }

            History history = new History(x, y, isTileHit, isShipSunk);
            historyList.add(history);
            model.addAttribute("historyList", historyList);

            redirectAttributes.addAttribute("isTileHit", isTileHit);
            redirectAttributes.addAttribute("isShipSunk", isShipSunk);
            redirectAttributes.addAttribute("isGameFinished", isGameFinished);

            if(isGameFinished){
                System.out.println("Ended Games");
                this.battleshipsGameService.endGame();
                return "redirect:/game/stop?teamName=TeamA&gameId=" + battleshipsGameService.getGameId();
            }
            else{
                return "redirect:/game/start";
            }
        } else {
            System.out.println("Fire Error");
            return "errorPage";
        }
    }

    @GetMapping("/game/stop")
    public String stopGame(@RequestParam String teamName, @RequestParam String gameId, RedirectAttributes redirectAttributes) {
        if(isGameFinished){this.battleshipsGameService.createRecord("Crousti_poulet","master_chicken","scores");}
        // Reset game using POST request
        ResponseEntity<String> resetResponseEntity = restTemplate.postForEntity("http://localhost:8080/game/stop?teamName=" + teamName + "&gameId=" + gameId, null, String.class);

        // Check if reset was successful
        if (resetResponseEntity.getStatusCode() == HttpStatus.OK) {
            String resetResponse = resetResponseEntity.getBody();
            System.out.println("Reset Response: " + resetResponse);

            redirectAttributes.addAttribute("resetResponse", resetResponse);
            isGameFinished = false;
            historyList.clear();
            return "ending";
        } else {
            System.out.println("Game Reset Error");
            return "errorPage";
        }
    }

    @ModelAttribute("historyList")
    public List<History> getHistoryList() {
        return historyList;
    }
}
