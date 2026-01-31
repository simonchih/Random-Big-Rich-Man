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

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class MainMap extends JFrame {

	private static final long serialVersionUID = 1;
	private static boolean crit = false;

	private Game game;
	private GameMap game_data;
	private GameMap ini_map;
	private GameLoop game_loop;

	public MainMap(final Game game) {
		this.game = game;
		this.game_data = new GameMap();
		this.ini_map = new GameMap();

		//ini p_id and p_dest_id, p_status, p_in_jail, p_stop, pshow_sqmark
		for (int i = 0; i < game.maxPSize; i++) {
			game.p_id[i] = 0;
			game.p_dest_id[i] = 0;
			game.p_status[i] = "0";
			game.p_in_jail[i] = 0;
			game.p_stop[i] = 0;
            game.pshow_sqmark[i] = false;
		}

		this.setTitle("Random Big Rich Man");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(767, 790);
		getContentPane().setLayout(null);

		game.btnPropertyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (true == crit) {
					return;
				}
				crit = true;
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						game.property.show(ini_map);
					}
				});
				crit = false;
			}
		});
		game.btnPropertyButton.setBounds(210, 601, 200, 23);
		getContentPane().add(game.btnPropertyButton);

		game.btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game.dice.rollDice();
				game.move_start = true;
				switch (game.turn) {
					case 0:
						game.p_dest_id[0] = (game.p_id[0] + game.dice.count) % ini_map.size;
						break;
					case 1:
						game.p_dest_id[1] = (game.p_id[1] + game.dice.count) % ini_map.size;
						break;
					case 2:
						game.p_dest_id[2] = (game.p_id[2] + game.dice.count) % ini_map.size;
						break;
					case 3:
						game.p_dest_id[3] = (game.p_id[3] + game.dice.count) % ini_map.size;
						break;
					default:
						break;
				}
				game.btnNewButton.setEnabled(false);
			}
		});
		game.btnNewButton.setBounds(460, 601, 150, 23);
		getContentPane().add(game.btnNewButton);
	}

	public GameMap ini_gameMap(final GameMap gdata) {
		this.setResizable(false);
		final GameMap ini_map = new GameMap();
		Color[] land_color = new Color[]{Color.red, Color.cyan, Color.blue, Color.green, Color.orange, Color.pink, Color.yellow, Color.gray};
		int index;
		// ini gdata
		// 0~23: land
		// 24~27: big block
		// 24: over this will give player $2000
		// 25: Jail
		// 26: Nothing
		// 27: Hospital
		// 28~35: chance
		// 36~39: others
		// 36: go jail
		// 37: go hospital
		// 38: land tax
		// 39: house tax
		for (int i = 0; i < gdata.size; i++) {
			gdata.id[i] = i;
			if (i >= 0 && i <= 23) {
				gdata.type[i] = 0;
			} else if (i <= 27) {
				gdata.type[i] = 1;
			} else if (i <= 35) {
				gdata.type[i] = 2;
			} else if ( i <= 39) {
				gdata.type[i] = 3;
			}
		}

		// 0~23
		gdata.value[0]  = 2000;
		gdata.name[0]   = "Jingan";
		gdata.value[1]  = 2100;
		gdata.name[1]   = "Yongan";
		gdata.value[2]  = 2200;
		gdata.name[2]   = "Dingxi";
		gdata.value[3]  = 2300;
		gdata.name[3]   = "Xindian";
		gdata.value[4]  = 2400;
		gdata.name[4]   = "Qizhang";
		gdata.value[5]  = 2500;
		gdata.name[5]   = "Luzhou";
		gdata.value[6]  = 2600;
		gdata.name[6]   = "Jingmei";
		gdata.value[7]  = 2700;
		gdata.name[7]   = "Wanlong";
		gdata.value[8]  = 2800;
		gdata.name[8]   = "Huilong";
		gdata.value[9]  = 2900;
		gdata.name[9]   = "Guting";
		gdata.value[10] = 3000;
		gdata.name[10]  = "Muzha";
		gdata.value[11] = 3000;
		gdata.name[11]  = "Wanfang";
		gdata.value[12] = 3200;
		gdata.name[12]  = "Xinhai";
		gdata.value[13] = 3200;
		gdata.name[13]  = "Tamsui";
		gdata.value[14] = 3400;
		gdata.name[14]  = "Daan";
		gdata.value[15] = 3400;
		gdata.name[15]  = "Xinyi";
		gdata.value[16] = 3600;
		gdata.name[16]  = "Wende";
		gdata.value[17] = 3800;
		gdata.name[17]  = "Nangang";
		gdata.value[18] = 4000;
		gdata.name[18]  = "Shandao";
		gdata.value[19] = 4200;
		gdata.name[19]  = "Beimen";
		gdata.value[20] = 4400;
		gdata.name[20]  = "Ximen";
		gdata.value[21] = 4600;
		gdata.name[21]  = "Beitou";
		gdata.value[22] = 4800;
		gdata.name[22]  = "Nanjing";
		gdata.value[23] = 5000;
		gdata.name[23]  = "Xinhu";

		// ini map
		for (int i = 0; i < ini_map.size; i++) {
			ini_map.type[i] = 9;
		}

		ini_map.id[0] = 24;
		ini_map.type[0] = 1;

		// 25: Jail
		// 26: Nothing/CKS Memorial Hall
		// 27: Hospital
		Integer[] array = {25, 26, 27};
		List<Integer> list = new ArrayList<>(Arrays.asList(array));
		final Random random = game.getRandom();

		Integer temp, temp_data;
		temp = Util.getRandomItem(list, random);
		ini_map.id[10] = temp;
		ini_map.type[10] = 1;
		list.remove(temp);

		temp = Util.getRandomItem(list, random);
		ini_map.id[20] = temp;
		ini_map.type[20] = 1;
		list.remove(temp);
		array = list.toArray(new Integer[0]);

		temp = Util.getRandomItem(list, random);
		ini_map.id[30] = temp;
		ini_map.type[30] = 1;

		for (int i = 10; i <= 30; i += 10) {
			switch (ini_map.id[i]) {
				case 25:
					ini_map.jailId = i;
					break;
				case 26:
					ini_map.ckshallId = i;
					break;
				case 27:
					ini_map.hospitalId = i;
					break;
				default:
					break;
			}
		}

		// 28~35: chance
		// 36~39: others
		Integer[] array1 = {28, 29, 30, 31, 32, 33, 34, 35};
		Integer[] array2 = {36, 37, 38, 39};
		// 0~23 land
		Integer[] array3 = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23};


		List<Integer> list1 = new ArrayList<>(Arrays.asList(array1));
		List<Integer> list2 = new ArrayList<>(Arrays.asList(array2));
		List<Integer> list3 = new ArrayList<>(Arrays.asList(array3));

		for (int i = 0; i < 4; i++) {
			switch (i) {
				case 0:
					array = new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 9};
					break;
				case 1:
					array = new Integer[] {11, 12, 13, 14, 15, 16, 17, 18, 19};
					break;
				case 2:
					array = new Integer[] {21, 22, 23, 24, 25, 26, 27, 28, 29};
					break;
				case 3:
					array = new Integer[] {31, 32, 33, 34, 35, 36, 37, 38, 39};
					break;
				default:
					break;
			}
			list = new ArrayList<>(Arrays.asList(array));

			temp = Util.getRandomItem(list, random);
			temp_data = Util.getRandomItem(list1, random);
			ini_map.id[temp] = temp_data;
			ini_map.type[temp] = 2;
			list.remove(temp);
//			array = list.toArray(new Integer[0]);
			list1.remove(temp_data);
//			array1 = list1.toArray(new Integer[0]);

			temp = Util.getRandomItem(list, random);
			temp_data = Util.getRandomItem(list1, random);
			ini_map.id[temp] = temp_data;
			ini_map.type[temp] = 2;
			list.remove(temp);
//			array = list.toArray(new Integer[0]);
			list1.remove(temp_data);
//			array1 = list1.toArray(new Integer[0]);

			temp = Util.getRandomItem(list, random);
			temp_data = Util.getRandomItem(list2, random);
			ini_map.id[temp] = temp_data;
			ini_map.type[temp] = 3;
			list.remove(temp);
			array = list.toArray(new Integer[0]);
			list2.remove(temp_data);

			for (int j = 0; j < 6; j++) {
				temp = Util.getRandomItem(list, random);
				temp_data = Util.getRandomItem(list3, random);
				ini_map.id[temp] = temp_data;
				ini_map.type[temp] = 0;
				list.remove(temp);
				array = list.toArray(new Integer[0]);
				list3.remove(temp_data);
			}
		}

		//ini color
		int j = 0;
		for (int i = 0; i < 40; i++) {
			if (0 == ini_map.type[i]) {
				index = ini_map.id[i];
				ini_map.name[i] = game_data.name[index];
				ini_map.value[i] = game_data.value[index];
				ini_map.color[i] = land_color[j / 3];
				ini_map.sameColor[j / 3][j % 3] = i;
				++j;
			}
		}

		return ini_map;
	}

	public void generate_map(Game game) {

		this.getContentPane().setLayout(null);
		this.setResizable(false);

		ini_map = ini_gameMap(game_data);

		for (int i = 0; i < Game.maxPSize; i++) {
			game.p_x_now[i] = ini_map.pX[i][0];
			game.p_y_now[i] = ini_map.pY[i][0];
            game.p_sqmark_x_now[i] = game.p_x_now[i] + 1;
            game.p_sqmark_y_now[i] = game.p_y_now[i] - game.isqmark.getIconHeight();
		}

		MapCanvas canvas = new MapCanvas(ini_map, game);
		canvas.setBounds(0, 0, canvas.max_size, canvas.max_size);

		// ini_map pX[0], pY[0]
		ini_map.pX[0][0] = canvas.right_x + canvas.color_small + canvas.p_gap;
		ini_map.pY[0][0] = canvas.down_y + canvas.color_small + canvas.p_gap;

		ini_map.pX[1][0] = canvas.right_x + canvas.color_small + canvas.p_gap + game.p_pawn[0].getIconWidth();
		ini_map.pY[1][0] = canvas.down_y + canvas.color_small + canvas.p_gap + game.p_pawn[0].getIconHeight();

		ini_map.pX[2][0] = canvas.right_x + canvas.color_small + canvas.p_gap + 2 * game.p_pawn[0].getIconWidth();
		ini_map.pY[2][0] = canvas.down_y + canvas.color_small + canvas.p_gap + 2 * game.p_pawn[0].getIconHeight();

		ini_map.pX[3][0] = canvas.right_x + canvas.color_small + canvas.p_gap + 3 * game.p_pawn[0].getIconWidth();
		ini_map.pY[3][0] = canvas.down_y + canvas.color_small + canvas.p_gap + 3 * game.p_pawn[0].getIconHeight();

		// ini_map pX[1], pY[1]
		ini_map.pX[0][1] = canvas.right_x - canvas.block_size / 2 - game.p_pawn[0].getIconWidth() / 2;
		ini_map.pY[0][1] = canvas.down_y + canvas.color_small + canvas.p_gap;
		ini_map.pX[1][1] = canvas.right_x - canvas.block_size / 2 - game.p_pawn[1].getIconWidth() / 2;
		ini_map.pY[1][1] = canvas.down_y + canvas.color_small + canvas.p_gap + game.p_pawn[0].getIconHeight();
		ini_map.pX[2][1] = canvas.right_x - canvas.block_size / 2 - game.p_pawn[2].getIconWidth() / 2;
		ini_map.pY[2][1] = canvas.down_y + canvas.color_small + canvas.p_gap + 2 * game.p_pawn[0].getIconHeight();
		ini_map.pX[3][1] = canvas.right_x - canvas.block_size / 2 - game.p_pawn[3].getIconWidth() / 2;
		ini_map.pY[3][1] = canvas.down_y + canvas.color_small + canvas.p_gap + 3 * game.p_pawn[0].getIconHeight();

		// ini_map pX[2~9], pY[2~9]
		for (int i = 2; i < 10; i++) {
			ini_map.pX[0][i] = ini_map.pX[0][i - 1] - canvas.block_size;
			ini_map.pY[0][i] = ini_map.pY[0][i - 1];
			ini_map.pX[1][i] = ini_map.pX[1][i - 1] - canvas.block_size;
			ini_map.pY[1][i] = ini_map.pY[1][i - 1];
			ini_map.pX[2][i] = ini_map.pX[2][i - 1] - canvas.block_size;
			ini_map.pY[2][i] = ini_map.pY[2][i - 1];
			ini_map.pX[3][i] = ini_map.pX[3][i - 1] - canvas.block_size;
			ini_map.pY[3][i] = ini_map.pY[3][i - 1];
		}

		// ini_map pX[10], pY[10]
		ini_map.pX[0][10] = canvas.left_x - canvas.color_small - canvas.p_gap - game.p_pawn[0].getIconWidth();
		ini_map.pY[0][10] = ini_map.pY[0][9];
		ini_map.pX[1][10] = canvas.left_x - canvas.color_small - canvas.p_gap - 2 * game.p_pawn[0].getIconWidth();
		ini_map.pY[1][10] = ini_map.pY[1][9];
		ini_map.pX[2][10] = canvas.left_x - canvas.color_small - canvas.p_gap - 3 * game.p_pawn[0].getIconWidth();
		ini_map.pY[2][10] = ini_map.pY[2][9];
		ini_map.pX[3][10] = canvas.left_x - canvas.color_small - canvas.p_gap - 4 * game.p_pawn[0].getIconWidth();
		ini_map.pY[3][10] = ini_map.pY[3][9];

		// ini_map pX[11], pY[11]
		ini_map.pX[0][11] = ini_map.pX[0][10];
		ini_map.pY[0][11] = canvas.down_y - canvas.block_size / 2 - game.p_pawn[0].getIconHeight() / 2;
		ini_map.pX[1][11] = ini_map.pX[1][10];
		ini_map.pY[1][11] = canvas.down_y - canvas.block_size / 2 - game.p_pawn[1].getIconHeight() / 2;
		ini_map.pX[2][11] = ini_map.pX[2][10];
		ini_map.pY[2][11] = canvas.down_y - canvas.block_size / 2 - game.p_pawn[2].getIconHeight() / 2;
		ini_map.pX[3][11] = ini_map.pX[3][10];
		ini_map.pY[3][11] = canvas.down_y - canvas.block_size / 2 - game.p_pawn[3].getIconHeight() / 2;

		// ini_map pX[12~19], pY[12~19]
		for (int i = 12; i < 20; i++) {
			ini_map.pX[0][i] = ini_map.pX[0][i - 1];
			ini_map.pY[0][i] = ini_map.pY[0][i - 1] - canvas.block_size;
			ini_map.pX[1][i] = ini_map.pX[1][i - 1];
			ini_map.pY[1][i] = ini_map.pY[1][i - 1] - canvas.block_size;
			ini_map.pX[2][i] = ini_map.pX[2][i - 1];
			ini_map.pY[2][i] = ini_map.pY[2][i - 1] - canvas.block_size;
			ini_map.pX[3][i] = ini_map.pX[3][i - 1];
			ini_map.pY[3][i] = ini_map.pY[3][i - 1] - canvas.block_size;
		}

		// ini_map pX[20], pY[20]
		ini_map.pX[0][20] = ini_map.pX[0][19];
		ini_map.pY[0][20] = canvas.up_y - canvas.color_small - canvas.p_gap - game.p_pawn[0].getIconHeight();
		ini_map.pX[1][20] = ini_map.pX[1][19];
		ini_map.pY[1][20] = canvas.up_y - canvas.color_small - canvas.p_gap - 2 * game.p_pawn[0].getIconHeight();
		ini_map.pX[2][20] = ini_map.pX[2][19];
		ini_map.pY[2][20] = canvas.up_y - canvas.color_small - canvas.p_gap - 3 * game.p_pawn[0].getIconHeight();
		ini_map.pX[3][20] = ini_map.pX[3][19];
		ini_map.pY[3][20] = canvas.up_y - canvas.color_small - canvas.p_gap - 4 * game.p_pawn[0].getIconHeight();

		// ini_map pX[21], pY[21]
		ini_map.pX[0][21] = canvas.left_x + canvas.block_size / 2 - game.p_pawn[0].getIconWidth() / 2;
		ini_map.pY[0][21] = ini_map.pY[0][20];
		ini_map.pX[1][21] = canvas.left_x + canvas.block_size / 2 - game.p_pawn[1].getIconWidth() / 2;
		ini_map.pY[1][21] = ini_map.pY[1][20];
		ini_map.pX[2][21] = canvas.left_x + canvas.block_size / 2 - game.p_pawn[2].getIconWidth() / 2;
		ini_map.pY[2][21] = ini_map.pY[2][20];
		ini_map.pX[3][21] = canvas.left_x + canvas.block_size / 2 - game.p_pawn[3].getIconWidth() / 2;
		ini_map.pY[3][21] = ini_map.pY[3][20];

		// ini_map pX[22~29], pY[22~29]
		for (int i = 22; i < 30; i++) {
			ini_map.pX[0][i] = ini_map.pX[0][i - 1] + canvas.block_size;
			ini_map.pY[0][i] = ini_map.pY[0][i - 1];
			ini_map.pX[1][i] = ini_map.pX[1][i - 1] + canvas.block_size;
			ini_map.pY[1][i] = ini_map.pY[1][i - 1];
			ini_map.pX[2][i] = ini_map.pX[2][i - 1] + canvas.block_size;
			ini_map.pY[2][i] = ini_map.pY[2][i - 1];
			ini_map.pX[3][i] = ini_map.pX[3][i - 1] + canvas.block_size;
			ini_map.pY[3][i] = ini_map.pY[3][i - 1];
		}

		// ini_map pX[30], pY[30]
		ini_map.pX[0][30] = canvas.right_x + canvas.color_small + canvas.p_gap;
		ini_map.pY[0][30] = ini_map.pY[0][29];
		ini_map.pX[1][30] = canvas.right_x + canvas.color_small + canvas.p_gap + game.p_pawn[0].getIconWidth();
		ini_map.pY[1][30] = ini_map.pY[1][29];
		ini_map.pX[2][30] = canvas.right_x + canvas.color_small + canvas.p_gap + 2 * game.p_pawn[0].getIconWidth();
		ini_map.pY[2][30] = ini_map.pY[2][29];
		ini_map.pX[3][30] = canvas.right_x + canvas.color_small + canvas.p_gap + 3 * game.p_pawn[0].getIconWidth();
		ini_map.pY[3][30] = ini_map.pY[3][29];

		// ini_map pX[31], pY[31]
		ini_map.pX[0][31] = ini_map.pX[0][30];
		ini_map.pY[0][31] = canvas.up_y + canvas.block_size / 2 - game.p_pawn[0].getIconHeight() / 2;
		ini_map.pX[1][31] = ini_map.pX[1][30];
		ini_map.pY[1][31] = canvas.up_y + canvas.block_size / 2 - game.p_pawn[1].getIconHeight() / 2;
		ini_map.pX[2][31] = ini_map.pX[2][30];
		ini_map.pY[2][31] = canvas.up_y + canvas.block_size / 2 - game.p_pawn[2].getIconHeight() / 2;
		ini_map.pX[3][31] = ini_map.pX[3][30];
		ini_map.pY[3][31] = canvas.up_y + canvas.block_size / 2 - game.p_pawn[3].getIconHeight() / 2;

		// ini_map pX[32~39], pY[32~39]
		for (int i = 32; i < 40; i++) {
			ini_map.pX[0][i] = ini_map.pX[0][i - 1];
			ini_map.pY[0][i] = ini_map.pY[0][i - 1] + canvas.block_size;
			ini_map.pX[1][i] = ini_map.pX[1][i - 1];
			ini_map.pY[1][i] = ini_map.pY[1][i - 1] + canvas.block_size;
			ini_map.pX[2][i] = ini_map.pX[2][i - 1];
			ini_map.pY[2][i] = ini_map.pY[2][i - 1] + canvas.block_size;
			ini_map.pX[3][i] = ini_map.pX[3][i - 1];
			ini_map.pY[3][i] = ini_map.pY[3][i - 1] + canvas.block_size;
		}

		//set player pX, pY
		game.p_x_now[0] = ini_map.pX[0][game.p_id[0]];
		game.p_y_now[0] = ini_map.pY[0][game.p_id[0]];
		game.p_x_now[1] = ini_map.pX[1][game.p_id[1]];
		game.p_y_now[1] = ini_map.pY[1][game.p_id[1]];
		game.p_x_now[2] = ini_map.pX[2][game.p_id[2]];
		game.p_y_now[2] = ini_map.pY[2][game.p_id[2]];
		game.p_x_now[3] = ini_map.pX[3][game.p_id[3]];
		game.p_y_now[3] = ini_map.pY[3][game.p_id[3]];
        
        game.p_sqmark_x_now[0] = game.p_x_now[0] + 1;
        game.p_sqmark_y_now[0] = game.p_y_now[0] - game.isqmark.getIconHeight();
        game.p_sqmark_x_now[1] = game.p_x_now[1] + 1;
        game.p_sqmark_y_now[1] = game.p_y_now[1] - game.isqmark.getIconHeight();
        game.p_sqmark_x_now[2] = game.p_x_now[2] + 1;
        game.p_sqmark_y_now[2] = game.p_y_now[2] - game.isqmark.getIconHeight();
        game.p_sqmark_x_now[3] = game.p_x_now[3] + 1;
        game.p_sqmark_y_now[3] = game.p_y_now[3] - game.isqmark.getIconHeight();

		//Set all level 0: No house
		//Set all owner to None
		for (int i = 0; i < 40; i++) {
			ini_map.level[i] = 0;
			ini_map.owner[i] = 0;
		}

		this.getContentPane().add(canvas);
		this.getContentPane().setBackground(new Color(233, 234, 205));
		this.setVisible(true);
		game_loop = new GameLoop(game, canvas, ini_map, this);
		//game_loop.t_game.setPriority(Thread.NORM_PRIORITY);
		game_loop.t_game.start();
	}
}
