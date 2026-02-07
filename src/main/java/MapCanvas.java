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

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public class MapCanvas extends Canvas {

	private static final Font FONT_WGY_12 = Font.font("WenQuanYi Zen Hei", 12);
	private static final Font FONT_WGY_24 = Font.font("WenQuanYi Zen Hei", 24);
	private static final Font FONT_TIMES_12 = Font.font("Times New Roman", 12);

	public final int max_size = 760;
	public final int block_size = 60;
	public final int left_x = 110;
	public final int right_x = 650;
	public final int up_y = 110;
	public final int down_y = 650;
	public final int color_long = 59;
	public final int color_small = 20;
	public final int down_string_x_start = 594;
	public final int left_string_x_start = 20;
	public final int up_string_x_start = 114;
	public final int right_string_x_start = 690;
	public final int up_down_d = 20;
	public final int right_left_d = 15;
	public final int right_left_ic_d = 20;
	public final int left_string_y = 609;
	public final int right_string_y = 129;
	public final int string_player_d = 40;

	public final int p_gap = 5;

	public final GameMap game_data;
	public final Game mygame;

	public MapCanvas(final GameMap gm, final Game game) {
		this.game_data = gm;
		this.mygame = game;
		setWidth(max_size);
		setHeight(max_size);
	}

	private static int imageWidth(final Image image) {
		return (int) Math.round(image.getWidth());
	}

	private static int imageHeight(final Image image) {
		return (int) Math.round(image.getHeight());
	}

	public void draw() {
		final GraphicsContext gc = getGraphicsContext2D();
		gc.setFill(Color.rgb(233, 234, 205));
		gc.fillRect(0, 0, getWidth(), getHeight());

		paintConstantField(gc);
		paintVariableThings(gc);
	}

	public void paintConstantField(final GraphicsContext g) {
		final int numPlayers = 4;

		g.setFont(FONT_TIMES_12);
		g.setStroke(Color.BLACK);
		g.setFill(Color.BLACK);

		for (int playerIdx = 0; playerIdx < numPlayers; playerIdx++) {
			mygame.sp_x[playerIdx] = left_x + string_player_d;
			mygame.sp_y[playerIdx] = up_y + (playerIdx + 1) * string_player_d;
		}

		g.strokeLine(left_x, 0, left_x, max_size);
		g.strokeLine(right_x, 0, right_x, max_size);
		g.strokeLine(0, up_y, max_size, up_y);
		g.strokeLine(0, down_y, max_size, down_y);

		for (int i = left_x + block_size; i < right_x; i += block_size) {
			g.strokeLine(i, 0, i, up_y);
		}
		for (int i = left_x + block_size; i < right_x; i += block_size) {
			g.strokeLine(i, down_y, i, max_size);
		}
		for (int i = up_y + block_size; i < down_y; i += block_size) {
			g.strokeLine(0, i, left_x, i);
		}
		for (int i = up_y + block_size; i < down_y; i += block_size) {
			g.strokeLine(right_x, i, max_size, i);
		}

		g.drawImage(
			mygame.iarrow,
			right_x + (max_size - right_x - imageWidth(mygame.iarrow)) / 2.0,
			down_y + (max_size - down_y - imageHeight(mygame.iarrow)) / 2.0,
			imageWidth(mygame.iarrow),
			imageHeight(mygame.iarrow)
		);

		for (int i = 1; i < game_data.size; i++) {
			switch (game_data.type[i]) {
				case 0:
					paintLand(g, i);
					break;
				case 1:
					paintBigBlock(g, i);
					break;
				case 2:
					paintChance(g, i);
					break;
				case 3:
					paintOther(g, i);
					break;
				default:
					break;
			}
		}
	}

	private void paintLand(final GraphicsContext g, final int i) {
		if (i >= 1 && i <= 9) {
			g.setStroke(Color.BLACK);
			g.setFill(Color.BLACK);
			g.strokeLine(right_x - block_size * (i - 1), down_y + color_small + 1, right_x - block_size * i, down_y + color_small + 1);
			g.fillText(game_data.name[i], down_string_x_start - block_size * (i - 1), down_y + color_small + up_down_d);
			g.fillText("$" + game_data.value[i], down_string_x_start - block_size * (i - 1), down_y + color_small + 2 * up_down_d);
			g.setFill(game_data.color[i]);
			g.fillRect(right_x - block_size * i + 1, down_y + 1, color_long, color_small);

			if (game_data.level[i] == 4) {
				g.drawImage(mygame.ihotel, right_x - block_size * i + 1, down_y + 1, imageWidth(mygame.ihotel), imageHeight(mygame.ihotel));
			} else if (game_data.level[i] > 0) {
				g.drawImage(mygame.ihouse, right_x - block_size * i + 1, down_y + 1, imageWidth(mygame.ihouse), imageHeight(mygame.ihouse));
				if (game_data.level[i] >= 2) {
					g.drawImage(mygame.ihouse,
						right_x - block_size * i + imageWidth(mygame.ihouse) + 1,
						down_y + 1,
						imageWidth(mygame.ihouse),
						imageHeight(mygame.ihouse));
				}
				if (game_data.level[i] == 3) {
					g.drawImage(mygame.ihouse,
						right_x - block_size * i + 2 * imageWidth(mygame.ihouse) + 1,
						down_y + 1,
						imageWidth(mygame.ihouse),
						imageHeight(mygame.ihouse));
				}
			}

			if (game_data.owner[i] > 0) {
				final Image ownerIcon = mygame.p_ic[game_data.owner[i] - 1];
				g.drawImage(ownerIcon,
					right_x - block_size * i + (block_size - imageWidth(ownerIcon)) / 2.0,
					down_y + color_small + 3 * up_down_d,
					imageWidth(ownerIcon),
					imageHeight(ownerIcon));
			}
		} else if (i >= 11 && i <= 19) {
			g.setStroke(Color.BLACK);
			g.setFill(Color.BLACK);
			g.strokeLine(left_x - color_small - 1, down_y - block_size * (i - 11), left_x - color_small - 1, down_y - block_size * (i - 10));
			g.fillText(game_data.name[i], left_string_x_start, left_string_y - block_size * (i - 11));
			g.fillText("$" + game_data.value[i], left_string_x_start, left_string_y + right_left_d - block_size * (i - 11));
			g.setFill(game_data.color[i]);
			g.fillRect(left_x - color_small, down_y - block_size * (i - 10) + 1, color_small, color_long);

			if (game_data.level[i] == 4) {
				g.drawImage(mygame.ihotel_left,
					left_x - color_small,
					down_y - block_size * (i - 10) + 1,
					imageWidth(mygame.ihotel_left),
					imageHeight(mygame.ihotel_left));
			} else if (game_data.level[i] > 0) {
				g.drawImage(mygame.ihouse_left,
					left_x - color_small,
					down_y - block_size * (i - 10) + 1,
					imageWidth(mygame.ihouse_left),
					imageHeight(mygame.ihouse_left));
				if (game_data.level[i] >= 2) {
					g.drawImage(mygame.ihouse_left,
						left_x - color_small,
						down_y - block_size * (i - 10) + imageHeight(mygame.ihouse_left) + 1,
						imageWidth(mygame.ihouse_left),
						imageHeight(mygame.ihouse_left));
				}
				if (game_data.level[i] == 3) {
					g.drawImage(mygame.ihouse_left,
						left_x - color_small,
						down_y - block_size * (i - 10) + 2 * imageHeight(mygame.ihouse_left) + 1,
						imageWidth(mygame.ihouse_left),
						imageHeight(mygame.ihouse_left));
				}
			}
			if (game_data.owner[i] > 0) {
				final Image ownerIcon = mygame.p_ic[game_data.owner[i] - 1];
				g.drawImage(ownerIcon,
					(left_x - color_small - imageWidth(ownerIcon)) / 2.0,
					left_string_y + right_left_ic_d - block_size * (i - 11),
					imageWidth(ownerIcon),
					imageHeight(ownerIcon));
			}
		} else if (i >= 21 && i <= 29) {
			g.setStroke(Color.BLACK);
			g.setFill(Color.BLACK);
			g.strokeLine(left_x + block_size * (i - 21), up_y - color_small - 1, left_x + block_size * (i - 20), up_y - color_small - 1);
			g.fillText(game_data.name[i], up_string_x_start + block_size * (i - 21), up_y - color_small - up_down_d);
			g.fillText("$" + game_data.value[i], up_string_x_start + block_size * (i - 21), up_y - color_small - 2 * up_down_d);
			g.setFill(game_data.color[i]);
			g.fillRect(left_x + block_size * (i - 21) + 1, up_y - color_small, color_long, color_small);

			if (game_data.level[i] == 4) {
				g.drawImage(mygame.ihotel_up,
					left_x + block_size * (i - 21) + 1,
					up_y - color_small,
					imageWidth(mygame.ihotel_up),
					imageHeight(mygame.ihotel_up));
			} else if (game_data.level[i] > 0) {
				g.drawImage(mygame.ihouse_up,
					left_x + block_size * (i - 21) + 1,
					up_y - color_small,
					imageWidth(mygame.ihouse_up),
					imageHeight(mygame.ihouse_up));
				if (game_data.level[i] >= 2) {
					g.drawImage(mygame.ihouse_up,
						left_x + block_size * (i - 21) + imageWidth(mygame.ihouse_up) + 1,
						up_y - color_small,
						imageWidth(mygame.ihouse_up),
						imageHeight(mygame.ihouse_up));
				}
				if (game_data.level[i] == 3) {
					g.drawImage(mygame.ihouse_up,
						left_x + block_size * (i - 21) + 2 * imageWidth(mygame.ihouse_up) + 1,
						up_y - color_small,
						imageWidth(mygame.ihouse_up),
						imageHeight(mygame.ihouse_up));
				}
			}
			if (game_data.owner[i] > 0) {
				final Image ownerIcon = mygame.p_ic[game_data.owner[i] - 1];
				g.drawImage(ownerIcon,
					left_x + block_size * (i - 21) + (block_size - imageWidth(ownerIcon)) / 2.0,
					up_y - color_small - 3 * up_down_d - imageHeight(ownerIcon),
					imageWidth(ownerIcon),
					imageHeight(ownerIcon));
			}
		} else if (i >= 31 && i <= 39) {
			g.setStroke(Color.BLACK);
			g.setFill(Color.BLACK);
			g.strokeLine(right_x + color_small + 1, up_y + block_size * (i - 31), right_x + color_small + 1, up_y + block_size * (i - 30));
			g.fillText(game_data.name[i], right_string_x_start, right_string_y + block_size * (i - 31));
			g.fillText("$" + game_data.value[i], right_string_x_start, right_string_y + right_left_d + block_size * (i - 31));
			g.setFill(game_data.color[i]);
			g.fillRect(right_x + 1, up_y + block_size * (i - 31) + 1, color_small, color_long);

			if (game_data.level[i] == 4) {
				g.drawImage(mygame.ihotel_right,
					right_x + 1,
					up_y + block_size * (i - 31) + 1,
					imageWidth(mygame.ihotel_right),
					imageHeight(mygame.ihotel_right));
			} else if (game_data.level[i] > 0) {
				g.drawImage(mygame.ihouse_right,
					right_x + 1,
					up_y + block_size * (i - 31) + 1,
					imageWidth(mygame.ihouse_right),
					imageHeight(mygame.ihouse_right));
				if (game_data.level[i] >= 2) {
					g.drawImage(mygame.ihouse_right,
						right_x + 1,
						up_y + block_size * (i - 31) + imageHeight(mygame.ihouse_right) + 1,
						imageWidth(mygame.ihouse_right),
						imageHeight(mygame.ihouse_right));
				}
				if (game_data.level[i] == 3) {
					g.drawImage(mygame.ihouse_right,
						right_x + 1,
						up_y + block_size * (i - 31) + 2 * imageHeight(mygame.ihouse_right) + 1,
						imageWidth(mygame.ihouse_right),
						imageHeight(mygame.ihouse_right));
				}
			}
			if (game_data.owner[i] > 0) {
				final Image ownerIcon = mygame.p_ic[game_data.owner[i] - 1];
				g.drawImage(ownerIcon,
					right_x + color_small + (block_size - imageWidth(ownerIcon)) / 2.0,
					right_string_y + right_left_ic_d + block_size * (i - 31),
					imageWidth(ownerIcon),
					imageHeight(ownerIcon));
			}
		}
	}

	private void paintBigBlock(final GraphicsContext g, final int i) {
		final Image iblock;
		switch (game_data.id[i]) {
			case 25:
				iblock = mygame.ijail;
				break;
			case 26:
				iblock = mygame.ickshall;
				break;
			case 27:
				iblock = mygame.ihospital;
				break;
			default:
				iblock = null;
				break;
		}

		if (iblock == null) {
			return;
		}

		switch (i) {
			case 10:
				g.drawImage(
					iblock,
					(left_x - imageWidth(iblock)) / 2.0,
					down_y + (max_size - down_y - imageHeight(iblock)) / 2.0,
					imageWidth(iblock),
					imageHeight(iblock)
				);
				break;
			case 20:
				g.drawImage(
					iblock,
					(left_x - imageWidth(iblock)) / 2.0,
					(up_y - imageHeight(iblock)) / 2.0,
					imageWidth(iblock),
					imageHeight(iblock)
				);
				break;
			case 30:
				g.drawImage(
					iblock,
					right_x + (max_size - right_x - imageWidth(iblock)) / 2.0,
					(up_y - imageHeight(iblock)) / 2.0,
					imageWidth(iblock),
					imageHeight(iblock)
				);
				break;
			default:
				break;
		}
	}

	private void paintChance(final GraphicsContext g, final int i) {
		if (i >= 1 && i <= 9) {
			g.drawImage(
				mygame.iquestionmark,
				right_x - block_size * i,
				down_y + color_small + 1,
				imageWidth(mygame.iquestionmark),
				imageHeight(mygame.iquestionmark)
			);
		} else if (i >= 11 && i <= 19) {
			g.drawImage(
				mygame.iquestionmark_left,
				left_x - color_small - imageWidth(mygame.iquestionmark_left) - 1,
				down_y - block_size * (i - 10),
				imageWidth(mygame.iquestionmark_left),
				imageHeight(mygame.iquestionmark_left)
			);
		} else if (i >= 21 && i <= 29) {
			g.drawImage(
				mygame.iquestionmark_up,
				left_x + block_size * (i - 21),
				up_y - color_small - imageHeight(mygame.iquestionmark_up) - 1,
				imageWidth(mygame.iquestionmark_up),
				imageHeight(mygame.iquestionmark_up)
			);
		} else if (i >= 31 && i <= 39) {
			g.drawImage(
				mygame.iquestionmark_right,
				right_x + color_small + 1,
				up_y + block_size * (i - 31),
				imageWidth(mygame.iquestionmark_right),
				imageHeight(mygame.iquestionmark_right)
			);
		}
	}

	private void paintOther(final GraphicsContext g, final int i) {
		String s1 = null;
		String s2 = null;
		switch (game_data.id[i]) {
			case 36:
				s1 = mygame.s36_1;
				s2 = mygame.s36_2;
				break;
			case 37:
				s1 = mygame.s37_1;
				s2 = mygame.s37_2;
				break;
			case 38:
				s1 = mygame.s38_1;
				s2 = mygame.s38_2;
				break;
			case 39:
				s1 = mygame.s39_1;
				s2 = mygame.s39_2;
				break;
			default:
				break;
		}

		g.setFill(Color.BLACK);
		if (i >= 1 && i <= 9) {
			g.fillText(s1, down_string_x_start - block_size * (i - 1), down_y + color_small + up_down_d);
			g.fillText(s2, down_string_x_start - block_size * (i - 1), down_y + color_small + 2 * up_down_d);
		} else if (i >= 11 && i <= 19) {
			g.fillText(s1, left_string_x_start, left_string_y - block_size * (i - 11));
			g.fillText(s2, left_string_x_start, left_string_y + right_left_d - block_size * (i - 11));
		} else if (i >= 21 && i <= 29) {
			g.fillText(s2, up_string_x_start + block_size * (i - 21), up_y - color_small - up_down_d);
			g.fillText(s1, up_string_x_start + block_size * (i - 21), up_y - color_small - 2 * up_down_d);
		} else if (i >= 31 && i <= 39) {
			g.fillText(s1, right_string_x_start, right_string_y + block_size * (i - 31));
			g.fillText(s2, right_string_x_start, right_string_y + right_left_d + block_size * (i - 31));
		}
	}

	public void paintVariableThings(final GraphicsContext g) {
		final int numPlayers = 4;
		final List<String> sps = new ArrayList<>(numPlayers);

		for (int playerIdx = 0; playerIdx < numPlayers; playerIdx++) {
			sps.add(String.format("%s: %d (%s)", mygame.p_name[playerIdx], mygame.p_money[playerIdx], mygame.p_status[playerIdx]));
		}

		for (int playerIdx = 0; playerIdx < numPlayers; playerIdx++) {
			if (playerIdx == mygame.turn) {
				g.setFont(FONT_WGY_24);
				g.setFill(Color.BLUE);
			} else {
				g.setFill(Color.BLACK);
				g.setFont(FONT_WGY_12);
			}
			g.fillText(sps.get(playerIdx), mygame.sp_x[playerIdx], mygame.sp_y[playerIdx]);
		}
		g.setFont(FONT_WGY_12);
		g.setFill(Color.BLACK);

		for (int playerIdx = 0; playerIdx < numPlayers; playerIdx++) {
			if (mygame.p_type[playerIdx] != 9) {
				g.drawImage(
					mygame.p_pawn[playerIdx],
					mygame.p_x_now[playerIdx],
					mygame.p_y_now[playerIdx],
					imageWidth(mygame.p_pawn[playerIdx]),
					imageHeight(mygame.p_pawn[playerIdx])
				);

				if (mygame.pshow_sqmark[playerIdx]) {
					g.drawImage(
						mygame.isqmark,
						mygame.p_sqmark_x_now[playerIdx],
						mygame.p_sqmark_y_now[playerIdx],
						imageWidth(mygame.isqmark),
						imageHeight(mygame.isqmark)
					);
				}
			}
		}

		// Keep dice on top layer so it is never blocked by pawn/question marks.
		g.drawImage(
			mygame.dice.idice1,
			mygame.dice.idice1X,
			mygame.dice.idice1Y,
			imageWidth(mygame.dice.idice1),
			imageHeight(mygame.dice.idice1)
		);
		g.drawImage(
			mygame.dice.idice2,
			mygame.dice.idice2X,
			mygame.dice.idice2Y,
			imageWidth(mygame.dice.idice2),
			imageHeight(mygame.dice.idice2)
		);
	}
}
