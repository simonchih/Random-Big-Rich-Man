/*
 * Copyright (C) 2017 Simon <ficstudio@yahoo.com.tw>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class BuyLand {

	private final Game game;
	private final GameMap gameMap;
	private final GameLoop gameLoop;

	BuyLand(final Game game, final GameMap gameMap, final GameLoop gameLoop) {
		this.game = game;
		this.gameMap = gameMap;
		this.gameLoop = gameLoop;
	}

	public void show() {
		if (Platform.isFxApplicationThread()) {
			showDialog();
		} else {
			Platform.runLater(this::showDialog);
		}
	}

	private void showDialog() {
		final int turn_id = game.turn;

		final Stage bl = new Stage();
		bl.setTitle("Buy Land");
		bl.setResizable(false);
		bl.setOnCloseRequest(event -> gameLoop.susp = false);

		final Pane root = new Pane();
		root.setPrefSize(450, 300);
		bl.setScene(new Scene(root, 450, 300));

		final Label lblNewLabel = new Label("Hi, " + game.p_name[turn_id] + ":");
		lblNewLabel.setLayoutX(10);
		lblNewLabel.setLayoutY(10);
		root.getChildren().add(lblNewLabel);

		final Button btnCancel = new Button("Cancel");
		btnCancel.setLayoutX(55);
		btnCancel.setLayoutY(214);
		btnCancel.setOnAction(event -> {
			bl.close();
			gameLoop.susp = false;
		});
		root.getChildren().add(btnCancel);

		final Button btnBuy = new Button("Buy Land");
		btnBuy.setLayoutX(198);
		btnBuy.setLayoutY(214);
		btnBuy.setPrefWidth(188);
		btnBuy.setOnAction(event -> {
			game.deal((-1) * gameMap.value[game.p_dest_id[turn_id]], turn_id, "Buy Land: ");
			gameMap.owner[game.p_dest_id[turn_id]] = turn_id + 1;
			bl.close();
			gameLoop.susp = false;
		});
		root.getChildren().add(btnBuy);

		final Label question = new Label(
			"Do you want to buy "
				+ gameMap.name[game.p_dest_id[turn_id]]
				+ " with $"
				+ gameMap.value[game.p_dest_id[turn_id]]
				+ "?"
		);
		question.setLayoutX(20);
		question.setLayoutY(26);
		root.getChildren().add(question);

		if (gameMap.value[game.p_dest_id[turn_id]] > game.p_money[turn_id]) {
			final Label error = new Label("You have NOT enough money!");
			error.setLayoutX(20);
			error.setLayoutY(189);
			error.setTextFill(Color.RED);
			root.getChildren().add(error);
			btnBuy.setDisable(true);
		}

		bl.show();
		bl.toFront();
		bl.requestFocus();
	}
}
