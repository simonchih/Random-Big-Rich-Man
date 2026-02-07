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
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Property {

	private static final class PropertyRow {
		private final Color color;
		private final String name;
		private final long value;
		private final int houseLevel;
		private final boolean doubled;
		private final long toll;

		private PropertyRow(
			final Color color,
			final String name,
			final long value,
			final int houseLevel,
			final boolean doubled,
			final long toll) {
			this.color = color;
			this.name = name;
			this.value = value;
			this.houseLevel = houseLevel;
			this.doubled = doubled;
			this.toll = toll;
		}

		public Color getColor() {
			return color;
		}

		public String getName() {
			return name;
		}

		public long getValue() {
			return value;
		}

		public int getHouseLevel() {
			return houseLevel;
		}

		public boolean isDoubled() {
			return doubled;
		}

		public long getToll() {
			return toll;
		}
	}

	private final Game game;
	private final Stage stage;
	private final TabPane tabbedPane;

	Property(final Game game) {
		this.game = game;
		this.tabbedPane = new TabPane();
		this.stage = new Stage();
		this.stage.setTitle("Property");
		this.stage.setScene(new Scene(tabbedPane, 650, 300));
	}

	private PropertyRow toRow(final GameMap gameMap, final int i) {
		final int doub = game.double_fee(gameMap, i);
		final boolean db = (doub == 2);
		final long fee = game.toll(gameMap, doub, i);
		return new PropertyRow(gameMap.color[i], gameMap.name[i], gameMap.value[i], gameMap.level[i], db, fee);
	}

	public boolean isVisible() {
		return stage.isShowing();
	}

	protected void show(final GameMap gameMap) {
		if (Platform.isFxApplicationThread()) {
			showInternal(gameMap);
		} else {
			Platform.runLater(() -> showInternal(gameMap));
		}
	}

	private void showInternal(final GameMap gameMap) {
		int selectedIndex = 0;
		if (stage.isShowing() && !tabbedPane.getTabs().isEmpty()) {
			selectedIndex = tabbedPane.getSelectionModel().getSelectedIndex();
		}

		final List<PropertyRow> data1 = new ArrayList<>();
		final List<PropertyRow> data2 = new ArrayList<>();
		final List<PropertyRow> data3 = new ArrayList<>();
		final List<PropertyRow> data4 = new ArrayList<>();

		for (int i = 0; i < gameMap.size; i++) {
			final int owner = gameMap.owner[i];
			switch (owner) {
				case 1:
					data1.add(toRow(gameMap, i));
					break;
				case 2:
					data2.add(toRow(gameMap, i));
					break;
				case 3:
					data3.add(toRow(gameMap, i));
					break;
				case 4:
					data4.add(toRow(gameMap, i));
					break;
				default:
					break;
			}
		}

		tabbedPane.getTabs().clear();
		tabbedPane.getTabs().add(new Tab(game.p_name[0] + "'s Property", createTable(data1)));
		tabbedPane.getTabs().add(new Tab(game.p_name[1] + "'s Property", createTable(data2)));
		tabbedPane.getTabs().add(new Tab(game.p_name[2] + "'s Property", createTable(data3)));
		tabbedPane.getTabs().add(new Tab(game.p_name[3] + "'s Property", createTable(data4)));

		for (final Tab tab : tabbedPane.getTabs()) {
			tab.setClosable(false);
		}

		if (!tabbedPane.getTabs().isEmpty()) {
			selectedIndex = Math.max(0, Math.min(selectedIndex, tabbedPane.getTabs().size() - 1));
			tabbedPane.getSelectionModel().select(selectedIndex);
		}

		stage.show();
		stage.toFront();
	}

	private TableView<PropertyRow> createTable(final List<PropertyRow> rows) {
		final ObservableList<PropertyRow> data = FXCollections.observableArrayList(rows);
		final TableView<PropertyRow> table = new TableView<>(data);
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		table.setEditable(false);
		table.setFocusTraversable(false);

		final TableColumn<PropertyRow, Color> colorCol = new TableColumn<>("Color");
		colorCol.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getColor()));
		colorCol.setCellFactory(col -> new TableCell<PropertyRow, Color>() {
			private final Rectangle rect = new Rectangle(24, 14);

			@Override
			protected void updateItem(final Color item, final boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setGraphic(null);
				} else {
					rect.setFill(item);
					rect.setStroke(Color.BLACK);
					setGraphic(rect);
				}
			}
		});

		final TableColumn<PropertyRow, String> nameCol = new TableColumn<>("Name");
		nameCol.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getName()));

		final TableColumn<PropertyRow, Long> valueCol = new TableColumn<>("Value");
		valueCol.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getValue()));

		final TableColumn<PropertyRow, Integer> levelCol = new TableColumn<>("House Level");
		levelCol.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getHouseLevel()));

		final TableColumn<PropertyRow, Boolean> doubledCol = new TableColumn<>("Double");
		doubledCol.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().isDoubled()));

		final TableColumn<PropertyRow, Long> tollCol = new TableColumn<>("Toll");
		tollCol.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getToll()));

		table.getColumns().add(colorCol);
		table.getColumns().add(nameCol);
		table.getColumns().add(valueCol);
		table.getColumns().add(levelCol);
		table.getColumns().add(doubledCol);
		table.getColumns().add(tollCol);

		return table;
	}
}
