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
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainMap {

	private static boolean crit = false;

	private final Game game;
	private final GameMap game_data;
	private GameMap ini_map;
	private GameLoop game_loop;
	private final Stage stage;
	private final Pane root;
	private final AtomicBoolean refreshScheduled;
	private MapCanvas canvas;

	public MainMap(final Game game) {
		this.game = game;
		this.game_data = new GameMap();
		this.ini_map = new GameMap();
		this.stage = new Stage();
		this.root = new Pane();
		this.refreshScheduled = new AtomicBoolean(false);

		for (int i = 0; i < game.maxPSize; i++) {
			game.p_id[i] = 0;
			game.p_dest_id[i] = 0;
			game.p_status[i] = "0";
			game.p_in_jail[i] = 0;
			game.p_stop[i] = 0;
			game.pshow_sqmark[i] = false;
		}

		stage.setTitle("瑞德大富翁 · RICHMAN");
		Theme.decorate(stage);
		stage.setResizable(true);
		stage.setMinWidth(620);
		stage.setMinHeight(640);
		stage.setOnCloseRequest(event -> Platform.exit());

		final Scene scene = Theme.boardScene(root);
		stage.setScene(scene);

		game.btnPropertyButton.setOnAction(event -> {
			if (crit || ini_map == null) {
				return;
			}
			crit = true;
			try {
				game.property.show(ini_map);
			} finally {
				crit = false;
			}
		});
		game.btnPropertyButton.setLayoutX(200);
		game.btnPropertyButton.setLayoutY(795);
		game.btnPropertyButton.setPrefWidth(230);
		root.getChildren().add(game.btnPropertyButton);

		game.btnNewButton.setOnAction(event -> {
			if (!game.move_start && game.p_type[game.turn] == 0) {
				game.dice.rollDice();
				final int turnId = game.turn;
				game.p_dest_id[turnId] = (game.p_id[turnId] + game.dice.count) % ini_map.size;
				// move_start is volatile: set it last so destination is visible to game loop.
				game.move_start = true;
				game.setRollButtonDisabled(true);
			}
			refresh();
		});
		game.btnNewButton.setLayoutX(570);
		game.btnNewButton.setLayoutY(795);
		game.btnNewButton.setPrefWidth(230);
		root.getChildren().add(game.btnNewButton);
	}

    public void refresh() {
        refreshScheduled.set(true);
    }

	public GameMap ini_gameMap(final GameMap gdata) {
		final GameMap initializedMap = new GameMap();
		final Color[] land_color = new Color[]{
			Color.RED,
			Color.CYAN,
			Color.BLUE,
			Color.GREEN,
			Color.ORANGE,
			Color.PINK,
			Color.YELLOW,
			Color.GRAY
		};
		int index;

		for (int i = 0; i < gdata.size; i++) {
			gdata.id[i] = i;
			if (i <= 23) {
				gdata.type[i] = 0;
			} else if (i <= 27) {
				gdata.type[i] = 1;
			} else if (i <= 35) {
				gdata.type[i] = 2;
			} else if (i <= 39) {
				gdata.type[i] = 3;
			}
		}

		gdata.value[0] = 2000;
		gdata.name[0] = "Jingan";
		gdata.value[1] = 2100;
		gdata.name[1] = "Yongan";
		gdata.value[2] = 2200;
		gdata.name[2] = "Dingxi";
		gdata.value[3] = 2300;
		gdata.name[3] = "Xindian";
		gdata.value[4] = 2400;
		gdata.name[4] = "Qizhang";
		gdata.value[5] = 2500;
		gdata.name[5] = "Luzhou";
		gdata.value[6] = 2600;
		gdata.name[6] = "Jingmei";
		gdata.value[7] = 2700;
		gdata.name[7] = "Wanlong";
		gdata.value[8] = 2800;
		gdata.name[8] = "Huilong";
		gdata.value[9] = 2900;
		gdata.name[9] = "Guting";
		gdata.value[10] = 3000;
		gdata.name[10] = "Muzha";
		gdata.value[11] = 3000;
		gdata.name[11] = "Wanfang";
		gdata.value[12] = 3200;
		gdata.name[12] = "Xinhai";
		gdata.value[13] = 3200;
		gdata.name[13] = "Tamsui";
		gdata.value[14] = 3400;
		gdata.name[14] = "Daan";
		gdata.value[15] = 3400;
		gdata.name[15] = "Xinyi";
		gdata.value[16] = 3600;
		gdata.name[16] = "Wende";
		gdata.value[17] = 3800;
		gdata.name[17] = "Nangang";
		gdata.value[18] = 4000;
		gdata.name[18] = "Shandao";
		gdata.value[19] = 4200;
		gdata.name[19] = "Beimen";
		gdata.value[20] = 4400;
		gdata.name[20] = "Ximen";
		gdata.value[21] = 4600;
		gdata.name[21] = "Beitou";
		gdata.value[22] = 4800;
		gdata.name[22] = "Nanjing";
		gdata.value[23] = 5000;
		gdata.name[23] = "Xinhu";

		for (int i = 0; i < initializedMap.size; i++) {
			initializedMap.type[i] = 9;
		}

		initializedMap.id[0] = 24;
		initializedMap.type[0] = 1;

		Integer[] array = {25, 26, 27};
		List<Integer> list = new ArrayList<>(Arrays.asList(array));
		final Random random = game.getRandom();

		Integer temp;
		Integer temp_data;
		temp = Util.getRandomItem(list, random);
		initializedMap.id[10] = temp;
		initializedMap.type[10] = 1;
		list.remove(temp);

		temp = Util.getRandomItem(list, random);
		initializedMap.id[20] = temp;
		initializedMap.type[20] = 1;
		list.remove(temp);

		temp = Util.getRandomItem(list, random);
		initializedMap.id[30] = temp;
		initializedMap.type[30] = 1;

		for (int i = 10; i <= 30; i += 10) {
			switch (initializedMap.id[i]) {
				case 25:
					initializedMap.jailId = i;
					break;
				case 26:
					initializedMap.ckshallId = i;
					break;
				case 27:
					initializedMap.hospitalId = i;
					break;
				default:
					break;
			}
		}

		final Integer[] array1 = {28, 29, 30, 31, 32, 33, 34, 35};
		final Integer[] array2 = {36, 37, 38, 39};
		final Integer[] array3 = {
			0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
			12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23
		};

		final List<Integer> list1 = new ArrayList<>(Arrays.asList(array1));
		final List<Integer> list2 = new ArrayList<>(Arrays.asList(array2));
		final List<Integer> list3 = new ArrayList<>(Arrays.asList(array3));

		for (int i = 0; i < 4; i++) {
			switch (i) {
				case 0:
					array = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
					break;
				case 1:
					array = new Integer[]{11, 12, 13, 14, 15, 16, 17, 18, 19};
					break;
				case 2:
					array = new Integer[]{21, 22, 23, 24, 25, 26, 27, 28, 29};
					break;
				case 3:
					array = new Integer[]{31, 32, 33, 34, 35, 36, 37, 38, 39};
					break;
				default:
					array = new Integer[0];
					break;
			}

			list = new ArrayList<>(Arrays.asList(array));

			temp = Util.getRandomItem(list, random);
			temp_data = Util.getRandomItem(list1, random);
			initializedMap.id[temp] = temp_data;
			initializedMap.type[temp] = 2;
			list.remove(temp);
			list1.remove(temp_data);

			temp = Util.getRandomItem(list, random);
			temp_data = Util.getRandomItem(list1, random);
			initializedMap.id[temp] = temp_data;
			initializedMap.type[temp] = 2;
			list.remove(temp);
			list1.remove(temp_data);

			temp = Util.getRandomItem(list, random);
			temp_data = Util.getRandomItem(list2, random);
			initializedMap.id[temp] = temp_data;
			initializedMap.type[temp] = 3;
			list.remove(temp);
			list2.remove(temp_data);

			for (int j = 0; j < 6; j++) {
				temp = Util.getRandomItem(list, random);
				temp_data = Util.getRandomItem(list3, random);
				initializedMap.id[temp] = temp_data;
				initializedMap.type[temp] = 0;
				list.remove(temp);
				list3.remove(temp_data);
			}
		}

		int j = 0;
		for (int i = 0; i < 40; i++) {
			if (initializedMap.type[i] == 0) {
				index = initializedMap.id[i];
				initializedMap.name[i] = gdata.name[index];
				initializedMap.value[i] = gdata.value[index];
				initializedMap.color[i] = land_color[j / 3];
				initializedMap.sameColor[j / 3][j % 3] = i;
				++j;
			}
		}

		return initializedMap;
	}

	public void generate_map(final Game game) {
		ini_map = ini_gameMap(game_data);

		if (canvas != null) {
			root.getChildren().remove(canvas);
		}

		canvas = new MapCanvas(ini_map, game);
		canvas.setLayoutX(0);
		canvas.setLayoutY(0);
		root.getChildren().add(0, canvas);

        for (int i=0; i<40; i++) {
            for (int p=0; p<Game.maxPSize; p++) {
                var slot=MapCanvas.pawnSlot(i,p);
                ini_map.pX[p][i]=(int)slot.getMinX();
                ini_map.pY[p][i]=(int)slot.getMinY();
            }
        }

		game.p_x_now[0] = ini_map.pX[0][game.p_id[0]];
		game.p_y_now[0] = ini_map.pY[0][game.p_id[0]];
		game.p_x_now[1] = ini_map.pX[1][game.p_id[1]];
		game.p_y_now[1] = ini_map.pY[1][game.p_id[1]];
		game.p_x_now[2] = ini_map.pX[2][game.p_id[2]];
		game.p_y_now[2] = ini_map.pY[2][game.p_id[2]];
		game.p_x_now[3] = ini_map.pX[3][game.p_id[3]];
		game.p_y_now[3] = ini_map.pY[3][game.p_id[3]];

		game.updateSqMarkPosition(0);
		game.updateSqMarkPosition(1);
		game.updateSqMarkPosition(2);
		game.updateSqMarkPosition(3);

		for (int i = 0; i < 40; i++) {
			ini_map.level[i] = 0;
			ini_map.owner[i] = 0;
		}

        javafx.animation.AnimationTimer renderer=new javafx.animation.AnimationTimer() {
            private long lastFrame;
            @Override public void handle(long now) {
                if(now-lastFrame<33_000_000L) return;
                if(refreshScheduled.getAndSet(false) || canvas.hasAnimations(now)) canvas.draw();
                lastFrame=now;
            }
        };
        stage.setOnShown(e -> renderer.start());
        stage.setOnHidden(e -> renderer.stop());
        canvas.draw();
        stage.show();
		stage.toFront();

		game_loop = new GameLoop(game, canvas, ini_map, this);
		game_loop.t_game.setDaemon(true);
		game_loop.t_game.start();
	}
}
