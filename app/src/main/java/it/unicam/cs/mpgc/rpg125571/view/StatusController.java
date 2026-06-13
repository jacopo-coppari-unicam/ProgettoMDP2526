package it.unicam.cs.mpgc.rpg125571.view;

import it.unicam.cs.mpgc.rpg125571.model.character.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class StatusController {

    @FXML private Label levelLabel;
    @FXML private Label goldLabel;

    private MainDashboardController mainContext;

    public void setMainContext(MainDashboardController mainContext) {
        this.mainContext = mainContext;
    }

    public void refresh() {
        Player player = mainContext.getPlayer();
        if (player == null) return;

        levelLabel.setText("Level: " + player.getLevel());
        goldLabel.setText("Gold: " + player.getGold());
    }
}