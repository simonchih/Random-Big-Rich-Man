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

public class GameMap {

	final int size = 40;
	final int colorSize = 8;
	final int sameColorLandSize = 3;
	// type 0, land
	// type 1, big block
	// type 2, chance
	// type 3, others
	// type 9, NOT assign
	int[] type = new int[size];
	int[] id = new int[size];
	int [][] pX = new int[Game.maxPSize][size];
	int [][] pY = new int[Game.maxPSize][size];

	// owner 0, No one
	// owner 1, player 1...
	int[] owner = new int[size];
	// level 0, No house
	// level 1, one house
	// level 4, hotel (highest)
	int[] level = new int[size];
	String[] name = new String[size];
	long[] value = new long[size];
	Color[] color = new Color[size];

	//Color landColor[] = new Color[]{Color.red, Color.cyan, Color.blue, Color.green, Color.orange, Color.pink, Color.yellow, Color.gray};
	int[][] sameColor = new int[colorSize][sameColorLandSize];

	int hospitalId;
	int jailId;
	int ckshallId;
	//int startId = 0;

	public static int colorIndex(Color c) {
		if (Color.red == c) return 0;
		else if (Color.cyan == c) return 1;
		else if (Color.blue == c) return 2;
		else if (Color.green == c) return 3;
		else if (Color.orange == c) return 4;
		else if (Color.pink == c) return 5;
		else if (Color.yellow == c) return 6;
		else if (Color.gray == c) return 7;
		else
			// impossible
			return -1;
	}
}
