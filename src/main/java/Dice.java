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

import javafx.scene.image.Image;

public class Dice {

	public static final Image idice1_2 = Game.loadImage("/Image/die-1+2.gif");
	public static final Image idice1_3 = Game.loadImage("/Image/die-1+3.gif");
	public static final Image idice1_4 = Game.loadImage("/Image/die-1+4.gif");
	public static final Image idice1_5 = Game.loadImage("/Image/die-1+5.gif");
	public static final Image idice2_1 = Game.loadImage("/Image/die-2+1.gif");
	public static final Image idice2_3 = Game.loadImage("/Image/die-2+3.gif");
	public static final Image idice2_4 = Game.loadImage("/Image/die-2+4.gif");
	public static final Image idice2_6 = Game.loadImage("/Image/die-2+6.gif");
	public static final Image idice3_1 = Game.loadImage("/Image/die-3+1.gif");
	public static final Image idice3_2 = Game.loadImage("/Image/die-3+2.gif");
	public static final Image idice3_5 = Game.loadImage("/Image/die-3+5.gif");
	public static final Image idice3_6 = Game.loadImage("/Image/die-3+6.gif");
	public static final Image idice4_1 = Game.loadImage("/Image/die-4+1.gif");
	public static final Image idice4_2 = Game.loadImage("/Image/die-4+2.gif");
	public static final Image idice4_5 = Game.loadImage("/Image/die-4+5.gif");
	public static final Image idice4_6 = Game.loadImage("/Image/die-4+6.gif");
	public static final Image idice5_1 = Game.loadImage("/Image/die-5+1.gif");
	public static final Image idice5_3 = Game.loadImage("/Image/die-5+3.gif");
	public static final Image idice5_4 = Game.loadImage("/Image/die-5+4.gif");
	public static final Image idice5_6 = Game.loadImage("/Image/die-5+6.gif");
	public static final Image idice6_2 = Game.loadImage("/Image/die-6+2.gif");
	public static final Image idice6_3 = Game.loadImage("/Image/die-6+3.gif");
	public static final Image idice6_4 = Game.loadImage("/Image/die-6+4.gif");
	public static final Image idice6_5 = Game.loadImage("/Image/die-6+5.gif");
	public static final Image[] ddice = {
		idice1_2,
		idice1_3,
		idice1_4,
		idice1_5,
		idice2_1,
		idice2_3,
		idice2_4,
		idice2_6,
		idice3_1,
		idice3_2,
		idice3_5,
		idice3_6,
		idice4_1,
		idice4_2,
		idice4_5,
		idice4_6,
		idice5_1,
		idice5_3,
		idice5_4,
		idice5_6,
		idice6_2,
		idice6_3,
		idice6_4,
		idice6_5
	};
	public Image idice1;
	public Image idice2;

	public int dice1;
	public int dice1Raw = 0;
	public int dice2;
	public int dice2Raw = 4;
	public int count;

	public int idice1X = 480;
	public int idice1Y = 520;
	public int idice2X = 540;
	public int idice2Y = 520;

	private final Game game;

	Dice(final Game game) {
		this.idice1 = ddice[dice1Raw];
		this.idice2 = ddice[dice2Raw];
		this.dice1 = dice1Raw / 4 + 1;
		this.dice2 = dice2Raw / 4 + 1;
		this.count = dice1 + dice2;
		this.game = game;
	}

	public int generateRandomNumber() {
		return game.getRandom().nextInt(ddice.length);
	}

	public void rollDice() {
		dice1Raw = generateRandomNumber();
		dice2Raw = generateRandomNumber();
		idice1 = ddice[dice1Raw];
		idice2 = ddice[dice2Raw];
		dice1 = dice1Raw / 4 + 1;
		dice2 = dice2Raw / 4 + 1;
		count = dice1 + dice2;
	}
}
