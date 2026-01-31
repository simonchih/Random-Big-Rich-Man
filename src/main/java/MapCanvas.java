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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

public class MapCanvas extends JComponent {

	private static final long serialVersionUID = 1;

	private static final Font FONT_WGY_12 = new Font("wqy - zenhei", Font.PLAIN, 12);
	private static final Font FONT_WGY_24 = new Font("wqy - zenhei", Font.PLAIN, 24);
	private static final Font FONT_TIMES_12 = new Font("TimesRoman", Font.PLAIN, 12);

	int max_size = 760;
	int block_size = 60;
	int left_x = 110;
	int right_x = 650;
	int up_y = 110;
	int down_y = 650;
	int color_long = 59;
	int color_small = 20;
	int down_string_x_start = 594;
	int left_string_x_start = 20;
	int up_string_x_start = 114;
	int right_string_x_start = 690;
	int up_down_d = 20;
	int right_left_d = 15;
	int right_left_ic_d = 20;
	int left_string_y = 609;
	int right_string_y = 129;
	int string_player_d = 40;

	int p_gap = 5;

	public GameMap game_data;
	public Game mygame;
	private Image offscreenConst;
	private Image offscreen;

	public MapCanvas(final GameMap gm, final Game game) {
		this.game_data = gm;
		this.mygame = game;
		this.offscreenConst = null;
		this.offscreen = null;
	}

	@Override
	public void paintComponent(final Graphics g) {

		final Dimension d = getSize();

		// create the offscreen buffer and associated Graphics
		offscreen = createImage(d.width, d.height);
		final Graphics offGc = offscreen.getGraphics();
		// clear the exposed area
		offGc.setColor(getBackground());
		offGc.fillRect(0, 0, d.width, d.height);
		offGc.setColor(getForeground());
		// do normal redraw ...

		super.paintComponent(offGc);

//		if (offscreenConst == null) {
		if (1 == 1) { // XXX As long as fixed an dconstant drawing is not yet propperly separated, repaint all constant stuff each time
			// create the offscreen buffer and associated Graphics
			offscreenConst = createImage(d.width, d.height);
			final Graphics offConstGc = offscreenConst.getGraphics();
			// clear the exposed area
			offConstGc.setColor(getBackground());
			offConstGc.fillRect(0, 0, d.width, d.height);
			offConstGc.setColor(getForeground());
			// do normal redraw
			paintConstantField(offConstGc);
		}
		// transfer offscreenConst to offscreen
		offGc.drawImage(offscreenConst, 0, 0, this);

		paintVariableThings(offGc);

		// transfer offscreen to window
		g.drawImage(offscreen, 0, 0, this);
	}

	public void paintConstantField(final Graphics g) {

		final int numPlayers = 4;

		g.setFont(FONT_TIMES_12);

		// ini sp_x, sp_y
		for (int playerIdx = 0; playerIdx < numPlayers; playerIdx++) {
			mygame.sp_x[playerIdx] = left_x + string_player_d;
			mygame.sp_y[playerIdx] = up_y + (playerIdx + 1) * string_player_d;
		}

		g.drawLine(left_x, 0, left_x, max_size);
		g.drawLine(right_x, 0, right_x, max_size);
		g.drawLine(0, up_y, max_size, up_y);
		g.drawLine(0, down_y, max_size, down_y);

		// Up
		for (int i = left_x + block_size; i < right_x; i += block_size) {
			g.drawLine(i, 0, i, up_y);
		}

		// Down
		for (int i = left_x + block_size; i < right_x; i += block_size) {
			g.drawLine(i, down_y, i, max_size);
		}

		// Left
		for (int i = up_y + block_size; i < down_y; i += block_size) {
			g.drawLine(0, i, left_x, i);
		}

		// Right
		for (int i = up_y + block_size; i < down_y; i += block_size) {
			g.drawLine(right_x, i, max_size, i);
		}

		// Game Map id=0
		g.drawImage(
				mygame.iarrow.getImage(),
				right_x + (max_size - right_x-mygame.iarrow.getIconWidth()) / 2,
				down_y + (max_size - down_y - mygame.iarrow.getIconHeight()) / 2,
				mygame.iarrow.getIconWidth(),
				mygame.iarrow.getIconHeight(),
				null);

		for (int i = 1; i < game_data.size; i++) {
			switch (game_data.type[i]) {
				case 0:
					if (i >= 1 && i <= 9) {
						// Down
						g.setColor(Color.black);
						g.drawLine(right_x-block_size * (i - 1), down_y + color_small+1, right_x-block_size * i, down_y + color_small+1);
						g.drawString(game_data.name[i], down_string_x_start-block_size * (i - 1), down_y + color_small+up_down_d);
						g.drawString("$"+game_data.value[i], down_string_x_start-block_size * (i - 1), down_y + color_small + 2 * up_down_d);
						g.setColor(game_data.color[i]);
						g.fillRect(right_x-block_size * i + 1, down_y + 1, color_long, color_small);
						if (4 == game_data.level[i]) {
							g.drawImage(
									mygame.ihotel.getImage(),
									right_x-block_size * i + 1,
									down_y + 1,
									mygame.ihotel.getIconWidth(),
									mygame.ihotel.getIconHeight(),
									null);
						} else if (game_data.level[i] > 0) {
							g.drawImage(
									mygame.ihouse.getImage(),
									right_x-block_size * i + 1,
									down_y + 1,
									mygame.ihouse.getIconWidth(),
									mygame.ihouse.getIconHeight(),
									null);

							if (game_data.level[i] >= 2) {
								g.drawImage(
										mygame.ihouse.getImage(),
										right_x-block_size * i+mygame.ihouse.getIconWidth() + 1,
										down_y + 1,
										mygame.ihouse.getIconWidth(),
										mygame.ihouse.getIconHeight(),
										null);
							}
							if (3 == game_data.level[i]) {
								g.drawImage(
										mygame.ihouse.getImage(),
										right_x-block_size * i + 2 * mygame.ihouse.getIconWidth() + 1,
										down_y + 1,
										mygame.ihouse.getIconWidth(),
										mygame.ihouse.getIconHeight(),
										null);
							}
						}
						if (game_data.owner[i] > 0) {
							g.drawImage(
									mygame.p_ic[game_data.owner[i]-1].getImage(),
									right_x-block_size * i + (block_size-mygame.p_ic[game_data.owner[i]-1].getIconWidth()) / 2, down_y + color_small+3*up_down_d, mygame.p_ic[game_data.owner[i]-1].getIconWidth(), mygame.p_ic[game_data.owner[i]-1].getIconHeight(), null);
						}
					} else if (i >= 11 && i <= 19) {
						// Left
						g.setColor(Color.black);
						g.drawLine(left_x-color_small-1, down_y - block_size * (i - 11), left_x-color_small-1, down_y - block_size * (i - 10));
						g.drawString(game_data.name[i], left_string_x_start, left_string_y - block_size * (i - 11));
						g.drawString("$"+game_data.value[i], left_string_x_start, left_string_y + right_left_d-block_size * (i - 11));
						g.setColor(game_data.color[i]);
						g.fillRect(left_x-color_small, down_y - block_size * (i - 10) + 1, color_small, color_long);
						if (4 == game_data.level[i]) {
							g.drawImage(
									mygame.ihotel_left.getImage(),
									left_x-color_small,
									down_y - block_size * (i - 10) + 1,
									mygame.ihotel_left.getIconWidth(),
									mygame.ihotel_left.getIconHeight(),
									null);
						} else if (game_data.level[i] > 0) {
							g.drawImage(
									mygame.ihouse_left.getImage(),
									left_x-color_small,
									down_y - block_size * (i - 10) + 1,
									mygame.ihouse_left.getIconWidth(),
									mygame.ihouse_left.getIconHeight(),
									null);

							if (game_data.level[i] >= 2) {
								g.drawImage(
										mygame.ihouse_left.getImage(),
										left_x-color_small,
										down_y - block_size * (i - 10) + mygame.ihouse_left.getIconHeight() + 1,
										mygame.ihouse_left.getIconWidth(),
										mygame.ihouse_left.getIconHeight(),
										null);
							}
							if (3 == game_data.level[i]) {
								g.drawImage(
										mygame.ihouse_left.getImage(),
										left_x-color_small,
										down_y - block_size * (i - 10) + 2 * mygame.ihouse_left.getIconHeight() + 1,
										mygame.ihouse_left.getIconWidth(),
										mygame.ihouse_left.getIconHeight(),
										null);
							}
						}
						if (game_data.owner[i] > 0) {
							g.drawImage(
									mygame.p_ic[game_data.owner[i]-1].getImage(),
									(left_x - color_small - mygame.p_ic[game_data.owner[i]-1].getIconWidth()) / 2,
									left_string_y + right_left_ic_d-block_size * (i - 11),
									mygame.p_ic[game_data.owner[i]-1].getIconWidth(),
									mygame.p_ic[game_data.owner[i]-1].getIconHeight(),
									null);
						}
					} else if (i >= 21 && i <= 29) {
						// Up
						g.setColor(Color.black);
						g.drawLine(left_x + block_size * (i - 21), up_y - color_small-1, left_x + block_size * (i - 20), up_y - color_small - 1);
						g.drawString(game_data.name[i], up_string_x_start + block_size * (i - 21), up_y - color_small - up_down_d);
						g.drawString("$" + game_data.value[i], up_string_x_start + block_size * (i - 21), up_y - color_small - 2 * up_down_d);
						g.setColor(game_data.color[i]);
						g.fillRect(left_x + block_size * (i - 21) + 1, up_y - color_small, color_long, color_small);
						if (4 == game_data.level[i]) {
							g.drawImage(mygame.ihotel_up.getImage(),
									left_x + block_size * (i - 21) + 1,
									up_y - color_small,
									mygame.ihotel_up.getIconWidth(),
									mygame.ihotel_up.getIconHeight(),
									null);
						}
						else if (game_data.level[i] > 0) {
							g.drawImage(
									mygame.ihouse_up.getImage(),
									left_x + block_size * (i - 21) + 1,
									up_y - color_small,
									mygame.ihouse_up.getIconWidth(),
									mygame.ihouse_up.getIconHeight(),
									null);

							if (game_data.level[i] >= 2) {
								g.drawImage(
										mygame.ihouse_up.getImage(),
										left_x + block_size * (i - 21) + mygame.ihouse_up.getIconWidth() + 1,
										up_y - color_small,
										mygame.ihouse_up.getIconWidth(),
										mygame.ihouse_up.getIconHeight(),
										null);
							}
							if (3 == game_data.level[i]) {
								g.drawImage(
										mygame.ihouse_up.getImage(),
										left_x + block_size * (i - 21) + 2 * mygame.ihouse_up.getIconWidth() + 1,
										up_y - color_small,
										mygame.ihouse_up.getIconWidth(),
										mygame.ihouse_up.getIconHeight(),
										null);
							}
						}
						if (game_data.owner[i] > 0) {
							g.drawImage(
									mygame.p_ic[game_data.owner[i]-1].getImage(),
									left_x + block_size * (i - 21) + (block_size-mygame.p_ic[game_data.owner[i]-1].getIconWidth()) / 2,
									up_y - color_small-3*up_down_d-mygame.p_ic[game_data.owner[i]-1].getIconHeight(),
									mygame.p_ic[game_data.owner[i]-1].getIconWidth(),
									mygame.p_ic[game_data.owner[i]-1].getIconHeight(),
									null);
						}
					} else if (i >= 31 && i <= 39) {
						// Right
						g.setColor(Color.black);
						g.drawLine(right_x + color_small+1, up_y + block_size * (i - 31), right_x + color_small+1, up_y + block_size * (i - 30));
						g.drawString(game_data.name[i], right_string_x_start, right_string_y + block_size * (i - 31));
						g.drawString("$"+game_data.value[i], right_string_x_start, right_string_y + right_left_d + block_size * (i - 31));
						g.setColor(game_data.color[i]);
						g.fillRect(right_x + 1, up_y + block_size * (i - 31) + 1, color_small, color_long);
						if (4 == game_data.level[i]) {
							g.drawImage(
									mygame.ihotel_right.getImage(),
									right_x + 1,
									up_y + block_size * (i - 31) + 1,
									mygame.ihotel_right.getIconWidth(),
									mygame.ihotel_right.getIconHeight(),
									null);
						} else if (game_data.level[i] > 0) {
							g.drawImage(
									mygame.ihouse_right.getImage(),
									right_x + 1,
									up_y + block_size * (i - 31) + 1,
									mygame.ihouse_right.getIconWidth(),
									mygame.ihouse_right.getIconHeight(),
									null);

							if (game_data.level[i] >= 2) {
								g.drawImage(
										mygame.ihouse_right.getImage(),
										right_x + 1,
										up_y + block_size * (i - 31) + mygame.ihouse_right.getIconHeight() + 1,
										mygame.ihouse_right.getIconWidth(),
										mygame.ihouse_right.getIconHeight(),
										null);
							}
							if (3 == game_data.level[i]) {
								g.drawImage(
										mygame.ihouse_right.getImage(),
										right_x + 1,
										up_y + block_size * (i - 31) + 2 * mygame.ihouse_right.getIconHeight() + 1,
										mygame.ihouse_right.getIconWidth(),
										mygame.ihouse_right.getIconHeight(),
										null);
							}
						}
						if (game_data.owner[i] > 0) {
							g.drawImage(
									mygame.p_ic[game_data.owner[i]-1].getImage(),
									right_x + color_small + (block_size- mygame.p_ic[game_data.owner[i]-1].getIconWidth()) / 2,
									right_string_y + right_left_ic_d + block_size * (i - 31),
									mygame.p_ic[game_data.owner[i]-1].getIconWidth(),
									mygame.p_ic[game_data.owner[i]-1].getIconHeight(),
									null);
						}
					}	break;
				case 1:
					final ImageIcon iblock;
					switch (game_data.id[i]) {
						case 25:
							// Jail
							iblock = mygame.ijail;
							break;
						case 26:
							// Nothing
							iblock = mygame.ickshall;
							break;
						case 27:
							// Hospital
							iblock = mygame.ihospital;
							break;
						default:
							iblock = null;
							break;
					}

					switch (i) {
						case 10:
							g.drawImage(
									iblock.getImage(),
									(left_x - iblock.getIconWidth()) / 2,
									down_y + (max_size-down_y - iblock.getIconHeight()) / 2,
									iblock.getIconWidth(),
									iblock.getIconHeight(),
									null);
							break;
						case 20:
							g.drawImage(
									iblock.getImage(),
									(left_x - iblock.getIconWidth()) / 2,
									(up_y - iblock.getIconHeight()) / 2,
									iblock.getIconWidth(),
									iblock.getIconHeight(),
									null);
							break;
						case 30:
							g.drawImage(
									iblock.getImage(),
									right_x + (max_size-right_x - iblock.getIconWidth()) / 2,
									(up_y - iblock.getIconHeight()) / 2,
									iblock.getIconWidth(),
									iblock.getIconHeight(),
									null);
							break;
						default:
							break;
					}
					break;
				case 2:
					if (i >= 1 && i <= 9) {
						g.drawImage(
								mygame.iquestionmark.getImage(),
								right_x-block_size * i,
								down_y + color_small+1,
								mygame.iquestionmark.getIconWidth(),
								mygame.iquestionmark.getIconHeight(),
								null);
					} else if (i >= 11 && i <= 19) {
						g.drawImage(
								mygame.iquestionmark_left.getImage(),
								left_x-color_small-mygame.iquestionmark_left.getIconWidth()-1,
								down_y - block_size * (i - 10),
								mygame.iquestionmark_left.getIconWidth(),
								mygame.iquestionmark_left.getIconHeight(),
								null);
					} else if (i >= 21 && i <= 29) {
						g.drawImage(
								mygame.iquestionmark_up.getImage(),
								left_x + block_size * (i - 21),
								up_y - color_small-mygame.iquestionmark_up.getIconHeight()-1,
								mygame.iquestionmark_up.getIconWidth(),
								mygame.iquestionmark_up.getIconHeight(),
								null);
					} else if (i >= 31 && i <= 39) {
						g.drawImage(
								mygame.iquestionmark_right.getImage(),
								right_x + color_small+1,
								up_y + block_size * (i - 31),
								mygame.iquestionmark_right.getIconWidth(),
								mygame.iquestionmark_right.getIconHeight(),
								null);
					}	break;
				case 3:
					String s1 = null, s2 = null;
					switch (game_data.id[i]) {
						case 36:
							// go jail
							s1 = mygame.s36_1;
							s2 = mygame.s36_2;
							break;
						case 37:
							// go hospital
							s1 = mygame.s37_1;
							s2 = mygame.s37_2;
							break;
						case 38:
							// land tax
							s1 = mygame.s38_1;
							s2 = mygame.s38_2;
							break;
						case 39:
							// house tax
							s1 = mygame.s39_1;
							s2 = mygame.s39_2;
							break;
						default:
							break;
					}	if (i >= 1 && i <= 9) {
						g.setColor(Color.black);
						g.drawString(s1, down_string_x_start-block_size * (i - 1), down_y + color_small+up_down_d);
						g.drawString(s2, down_string_x_start-block_size * (i - 1), down_y + color_small + 2 * up_down_d);
					} else if (i >= 11 && i <= 19) {
						g.setColor(Color.black);
						g.drawString(s1, left_string_x_start, left_string_y - block_size * (i - 11));
						g.drawString(s2, left_string_x_start, left_string_y + right_left_d-block_size * (i - 11));
					} else if (i >= 21 && i <= 29) {
						g.setColor(Color.black);
						g.drawString(s2, up_string_x_start + block_size * (i - 21), up_y - color_small-up_down_d);
						g.drawString(s1, up_string_x_start + block_size * (i - 21), up_y - color_small-2 * up_down_d);
					} else if (i >= 31 && i <= 39) {
						g.setColor(Color.black);
						g.drawString(s1, right_string_x_start, right_string_y + block_size * (i - 31));
						g.drawString(s2, right_string_x_start, right_string_y + right_left_d + block_size * (i - 31));
					}	break;
				default:
					break;
			}
		}
	}

	public void paintVariableThings(final Graphics g) {

		final int numPlayers = 4;
		final List<String> sps = new ArrayList<>(numPlayers);

		for (int playerIdx = 0; playerIdx < numPlayers; playerIdx++) {
			sps.add(String.format("%s: %d (%s)", mygame.p_name[playerIdx], mygame.p_money[playerIdx], mygame.p_status[playerIdx]));
		}

		// draw player total money
		for (int playerIdx = 0; playerIdx < numPlayers; playerIdx++) {
			if (playerIdx == mygame.turn) {
				g.setFont(FONT_WGY_24);
				g.setColor(Color.blue);
			} else {
				g.setColor(Color.black);
				g.setFont(FONT_WGY_12);
			}
			g.drawString(sps.get(playerIdx), mygame.sp_x[playerIdx], mygame.sp_y[playerIdx]);
		}
		g.setFont(FONT_WGY_12);
		g.setColor(Color.black);

		// draw dice
		g.drawImage(mygame.dice.idice1.getImage(),
				mygame.dice.idice1X,
				mygame.dice.idice1Y,
				mygame.dice.idice1.getIconWidth(),
				mygame.dice.idice1.getIconHeight(),
				this);
		g.drawImage(mygame.dice.idice2.getImage(),
				mygame.dice.idice2X,
				mygame.dice.idice2Y,
				mygame.dice.idice2.getIconWidth(),
				mygame.dice.idice2.getIconHeight(),
				this);

		// draw pawn at pX, pY;
		for (int playerIdx = 0; playerIdx < numPlayers; playerIdx++) {
			if (mygame.p_type[playerIdx] != 9) {
				g.drawImage(
						mygame.p_pawn[playerIdx].getImage(),
						mygame.p_x_now[playerIdx],
						mygame.p_y_now[playerIdx],
						mygame.p_pawn[playerIdx].getIconWidth(),
						mygame.p_pawn[playerIdx].getIconHeight(),
						null);
                //draw small question mark at top of pawn
                if (true == mygame.pshow_sqmark[playerIdx]){
                    g.drawImage(
						mygame.isqmark.getImage(),
						mygame.p_sqmark_x_now[playerIdx],
						mygame.p_sqmark_y_now[playerIdx],
						mygame.isqmark.getIconWidth(),
						mygame.isqmark.getIconHeight(),
						null);
                }
			}            
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(max_size, max_size);
	}

	@Override
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}

	@Override
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}
}
