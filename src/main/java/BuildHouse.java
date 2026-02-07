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

public class BuildHouse {

	private final Game game;
	private final GameMap gameMap;
	private final GameLoop gameLoop;

	BuildHouse(final Game game, final GameMap gameMap, final GameLoop gameLoop) {
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
		final long spent;
		final String buildingName;
		final String buttonName;

		switch (gameMap.level[game.p_dest_id[turn_id]]) {
			case 4:
				gameLoop.susp = false;
				return;
			case 3:
				buildingName = "hotel";
				buttonName = "Build Hotel";
				spent = (long) (gameMap.value[game.p_dest_id[turn_id]] * 0.4);
				break;
			default:
				buildingName = "house";
				buttonName = "Build House";
				spent = (long) (gameMap.value[game.p_dest_id[turn_id]] * 0.2);
				break;
		}

		final Stage bh = new Stage();
		bh.setTitle("Building");
		bh.setResizable(false);
		bh.setOnCloseRequest(event -> gameLoop.susp = false);

		final Pane root = new Pane();
		root.setPrefSize(450, 300);
		bh.setScene(new Scene(root, 450, 300));

		final Label hi = new Label("Hi, " + game.p_name[turn_id] + ":");
		hi.setLayoutX(10);
		hi.setLayoutY(10);
		root.getChildren().add(hi);

		final Button btnCancel = new Button("Cancel");
		btnCancel.setLayoutX(55);
		btnCancel.setLayoutY(214);
		btnCancel.setPrefWidth(124);
		btnCancel.setOnAction(event -> {
			bh.close();
			gameLoop.susp = false;
		});
		root.getChildren().add(btnCancel);

		final Button btnBuild = new Button(buttonName);
		btnBuild.setLayoutX(219);
		btnBuild.setLayoutY(214);
		btnBuild.setPrefWidth(188);
		btnBuild.setOnAction(event -> {
			game.deal(-1 * spent, turn_id, "Spent: ");
			gameMap.level[game.p_dest_id[turn_id]] += 1;
			bh.close();
			gameLoop.susp = false;
		});
		root.getChildren().add(btnBuild);

		final Label question = new Label("Do you want to spent $" + spent + " to build a " + buildingName + "?");
		question.setLayoutX(20);
		question.setLayoutY(26);
		root.getChildren().add(question);

		if (spent > game.p_money[turn_id]) {
			final Label error = new Label("You have NOT enough money!");
			error.setLayoutX(20);
			error.setLayoutY(189);
			error.setTextFill(Color.RED);
			root.getChildren().add(error);
			btnBuild.setDisable(true);
		}

		bh.show();
		bh.toFront();
		bh.requestFocus();
	}
}
