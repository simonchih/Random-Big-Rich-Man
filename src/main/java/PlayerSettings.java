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

import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlayerSettings {

	private static final int NUM_PLAYERS = 4;

	private final int playerNum;
	private final int playerIdx;

	private static final class IconChoice {
		private final int sourceIndex;
		private final Image icon;

		private IconChoice(final int sourceIndex, final Image icon) {
			this.sourceIndex = sourceIndex;
			this.icon = icon;
		}
	}

	public PlayerSettings(final int playerIdx) {
		this.playerIdx = playerIdx;
		this.playerNum = playerIdx + 1;
	}

	public void show(final Stage previousStage, final Game game) {
		final boolean firstPlayer = (playerIdx == 0);
		final boolean humanPlayer = firstPlayer;
		final boolean lastPlayer = (playerIdx == (NUM_PLAYERS - 1));

		final PlayerSettings nextSettings;
		final MainMap mmap;
		if (lastPlayer) {
			nextSettings = null;
			mmap = new MainMap(game);
		} else {
			nextSettings = new PlayerSettings(playerIdx + 1);
			mmap = null;
		}

		final Image[] playerIcons = new Image[]{
			game.image1,
			game.image2,
			game.image3,
			game.image4,
			game.image5,
			game.image6,
			game.image7,
			game.image8
		};
		final Image[] playerFigures = new Image[]{
			game.imagep1,
			game.imagep2,
			game.imagep3,
			game.imagep4,
			game.imagep5,
			game.imagep6,
			game.imagep7,
			game.imagep8
		};

		final Set<Integer> usedIconIndices = new HashSet<>(playerIdx);
		for (int i = 0; i < playerIdx; i++) {
			usedIconIndices.add(game.p_icon[i]);
		}

		final List<IconChoice> filteredChoices = new ArrayList<>();
		for (int i = 0; i < playerIcons.length; i++) {
			if (!usedIconIndices.contains(i)) {
				filteredChoices.add(new IconChoice(i, playerIcons[i]));
			}
		}

		final Stage playerSettingsStage = new Stage();
		playerSettingsStage.setTitle("Player" + playerNum + " Setting");
		playerSettingsStage.setResizable(false);
		playerSettingsStage.setOnCloseRequest(event -> event.consume());
		if (previousStage != null) {
			previousStage.hide();
		}

		final Pane root = new Pane();
		final Scene scene = new Scene(root, 450, 320);
		playerSettingsStage.setScene(scene);

		final Label lblName = new Label("Name");
		lblName.setLayoutX(122);
		lblName.setLayoutY(40);
		root.getChildren().add(lblName);

		final TextField name = new TextField("Player" + playerNum);
		name.setLayoutX(205);
		name.setLayoutY(34);
		name.setPrefWidth(106);
		root.getChildren().add(name);

		final Label lblAi = new Label("AI");
		lblAi.setLayoutX(122);
		lblAi.setLayoutY(79);
		root.getChildren().add(lblAi);

		final CheckBox ai = new CheckBox();
		ai.setSelected(!humanPlayer);
		ai.setLayoutX(205);
		ai.setLayoutY(76);
		root.getChildren().add(ai);

		final Label lblIcon = new Label("Icon");
		lblIcon.setLayoutX(122);
		lblIcon.setLayoutY(124);
		root.getChildren().add(lblIcon);

		final ComboBox<IconChoice> icon = new ComboBox<>();
		icon.setItems(FXCollections.observableArrayList(filteredChoices));
		icon.setLayoutX(205);
		icon.setLayoutY(118);
		icon.setPrefWidth(106);

		icon.setCellFactory(param -> new ListCell<IconChoice>() {
			private final ImageView imageView = new ImageView();

			@Override
			protected void updateItem(final IconChoice item, final boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setGraphic(null);
				} else {
					imageView.setImage(item.icon);
					imageView.setPreserveRatio(true);
					imageView.setFitHeight(18);
					setGraphic(imageView);
				}
			}
		});
		icon.setButtonCell(new ListCell<IconChoice>() {
			private final ImageView imageView = new ImageView();

			@Override
			protected void updateItem(final IconChoice item, final boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setGraphic(null);
				} else {
					imageView.setImage(item.icon);
					imageView.setPreserveRatio(true);
					imageView.setFitHeight(18);
					setGraphic(imageView);
				}
			}
		});
		icon.getSelectionModel().selectFirst();
		root.getChildren().add(icon);

		final Label lblStartingMoney = new Label("Starting money");
		lblStartingMoney.setLayoutX(122);
		lblStartingMoney.setLayoutY(165);
		root.getChildren().add(lblStartingMoney);

		final Spinner<Integer> startingMoney = new Spinner<>();
		startingMoney.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 30000, 100));
		startingMoney.setEditable(true);
		startingMoney.setLayoutX(205);
		startingMoney.setLayoutY(159);
		startingMoney.setPrefWidth(106);
		root.getChildren().add(startingMoney);

		final Label lblErrorValue = new Label("(error value)");
		lblErrorValue.setTextFill(Color.RED);
		lblErrorValue.setLayoutX(321);
		lblErrorValue.setLayoutY(40);
		lblErrorValue.setVisible(false);
		root.getChildren().add(lblErrorValue);

		final Button btnCancel = new Button("Previous");
		btnCancel.setLayoutX(85);
		btnCancel.setLayoutY(214);
		btnCancel.setPrefWidth(155);
		btnCancel.setOnAction(event -> {
			playerSettingsStage.close();
			if (previousStage != null) {
				previousStage.show();
				previousStage.toFront();
			}
		});
		root.getChildren().add(btnCancel);

		final Button btnNext = new Button(lastPlayer ? "Finish" : "Next");
		btnNext.setLayoutX(250);
		btnNext.setLayoutY(214);
		btnNext.setPrefWidth(94);
		btnNext.setOnAction(event -> {
			final String playerName = name.getText();
			if (playerName == null || playerName.trim().isEmpty()) {
				lblErrorValue.setVisible(true);
				return;
			}
			lblErrorValue.setVisible(false);

			final IconChoice selected = icon.getSelectionModel().getSelectedItem();
			if (selected == null) {
				lblErrorValue.setVisible(true);
				return;
			}

			game.p_name[playerIdx] = playerName.trim();
			game.p_money[playerIdx] = startingMoney.getValue();
			game.p_type[playerIdx] = ai.isSelected() ? 1 : 0;
			game.p_icon[playerIdx] = selected.sourceIndex;
			game.p_ic[playerIdx] = playerIcons[selected.sourceIndex];
			game.p_pawn[playerIdx] = playerFigures[selected.sourceIndex];

			playerSettingsStage.hide();
			if (lastPlayer) {
				mmap.generate_map(game);
			} else {
				nextSettings.show(playerSettingsStage, game);
			}
		});
		root.getChildren().add(btnNext);

		playerSettingsStage.show();
	}
}
