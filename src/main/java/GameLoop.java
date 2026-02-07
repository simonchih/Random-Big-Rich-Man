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

import java.util.Random;

public class GameLoop implements Runnable {

	private static final double speedModifier = 1.0;

	public Game mygame;
	public MapCanvas mycanvas;
	public GameMap gameMap;
	public BuyLand buy_land;
	public BuildHouse build_house;
	public AI ai;
	public Thread t_game;
	public volatile boolean susp;

	private final MainMap mainMap;

	GameLoop(final Game game, final MapCanvas canvas, final GameMap gm, final MainMap mainMap) {
		mygame = game;
		mycanvas = canvas;
		gameMap = gm;
		this.mainMap = mainMap;
		t_game = new Thread(this);
		buy_land = new BuyLand(game, gm, this);
		build_house = new BuildHouse(game, gm, this);
		ai = new AI(game, gm, this);
	}

	private void refreshBoard() {
		mainMap.refresh();
	}

	@Override
	public void run() {
		int one_step;
		int n;
		int id;
		long cash;
		final boolean[] no_cross_cash = {true, true, true, true};

		mygame.setRollButtonDisabled(true);

		while (playerNumber(mygame) > 1) {
			for (int i = 0; i < Game.maxPSize; i++) {
				if (i == mygame.turn) {
					if (9 == mygame.p_type[i]) {
						mygame.pshow_sqmark[mygame.turn] = false;
						mygame.turn = (mygame.turn + 1) % Game.maxPSize;
					} else if (mygame.p_stop[i] > 0) {
						mygame.p_stop[i]--;
						if (1 == mygame.p_in_jail[i]) {
							mygame.p_status[i] = "In jail. Stop " + mygame.p_stop[mygame.turn] + " turn.";
						} else {
							mygame.p_status[i] = "Stop " + mygame.p_stop[mygame.turn] + " turn.";
						}
						mygame.pshow_sqmark[mygame.turn] = false;
						mygame.turn = (mygame.turn + 1) % Game.maxPSize;
					} else {
						mygame.p_in_jail[i] = 0;
						mygame.p_status[i] = "0";
						if (!mygame.move_start && 0 == mygame.p_type[i]) {
							mygame.setRollButtonDisabled(false);
							break;
						} else if (1 == mygame.p_type[i]) {
							mygame.dice.rollDice();
							mygame.p_dest_id[i] = (mygame.p_id[i] + mygame.dice.count) % gameMap.size;
							mygame.move_start = true;
							break;
						}
					}
				}
			}

			for (int i = 0; i < Game.maxPSize; i++) {
				if (9 != mygame.p_type[i] && mygame.p_money[i] < 0) {
					for (int j = 0; j < gameMap.size; j++) {
						if (i + 1 == gameMap.owner[j]) {
							gameMap.owner[j] = 0;
							gameMap.level[j] = 0;
						}
					}
					mygame.p_status[i] = "Broken";
					mygame.p_type[i] = 9;
				}
			}

			if (playerNumber(mygame) <= 1) {
				break;
			}

			refreshBoard();
			if (!mygame.move_start) {
				try {
					Thread.sleep(10L);
				} catch (final InterruptedException e) {
					e.printStackTrace();
					Thread.currentThread().interrupt();
					return;
				}
			}

			while (mygame.p_id[0] != mygame.p_dest_id[0]
					|| mygame.p_id[1] != mygame.p_dest_id[1]
					|| mygame.p_id[2] != mygame.p_dest_id[2]
					|| mygame.p_id[3] != mygame.p_dest_id[3]) {
				try {
					Thread.sleep((long) Math.max(1, speedModifier * 5));
				} catch (final InterruptedException e) {
					e.printStackTrace();
					Thread.currentThread().interrupt();
					return;
				}

				for (int i = 0; i < Game.maxPSize; i++) {
					if (mygame.p_id[i] != mygame.p_dest_id[i]) {
						if (!no_cross_cash[i] && 0 == mygame.p_id[i]) {
							no_cross_cash[i] = true;
							mygame.deal(mygame.cross_cash, i, "Get: ");
						}
						if (mygame.p_x_now[i] == gameMap.pX[i][(mygame.p_id[i] + 1) % gameMap.size]
								&& mygame.p_y_now[i] == gameMap.pY[i][(mygame.p_id[i] + 1) % gameMap.size]) {
							mygame.p_id[i] = (mygame.p_id[i] + 1) % gameMap.size;
						}
						if (mygame.p_x_now[i] != gameMap.pX[i][(mygame.p_id[i] + 1) % gameMap.size]) {
							one_step = (gameMap.pX[i][(mygame.p_id[i] + 1) % gameMap.size] > mygame.p_x_now[i]) ? 1 : -1;
							mygame.p_x_now[i] = mygame.p_x_now[i] + one_step;
							mygame.updateSqMarkPosition(i);
						}
						if (mygame.p_y_now[i] != gameMap.pY[i][(mygame.p_id[i] + 1) % gameMap.size]) {
							one_step = (gameMap.pY[i][(mygame.p_id[i] + 1) % gameMap.size] > mygame.p_y_now[i]) ? 1 : -1;
							mygame.p_y_now[i] = mygame.p_y_now[i] + one_step;
							mygame.updateSqMarkPosition(i);
						}
					}

					if (i == mygame.turn
							&& mygame.move_start
							&& mygame.p_id[i] == mygame.p_dest_id[i]) {
						mygame.move_start = false;
						id = mygame.p_dest_id[mygame.turn];
						no_cross_cash[i] = (0 == mygame.p_dest_id[i]);

						if (0 == gameMap.type[id] && 0 == mygame.p_type[mygame.turn]) {
							susp = true;
							if (0 == gameMap.owner[id]) {
								buy_land.show();
							} else if (mygame.turn + 1 == gameMap.owner[id]) {
								build_house.show();
							} else {
								payout();
								susp = false;
							}
							while (susp) {
								try {
									Thread.sleep((long) (speedModifier * 500));
								} catch (final InterruptedException e) {
									e.printStackTrace();
									Thread.currentThread().interrupt();
									return;
								}
							}
						} else if (0 == gameMap.type[id]) {
							if (0 == gameMap.owner[id]) {
								ai.buy_land();
							} else if (mygame.turn + 1 == gameMap.owner[id]) {
								ai.build_house();
							} else {
								payout();
							}
						} else if (2 == gameMap.type[id]) {
							mygame.pshow_sqmark[mygame.turn] = true;
							refreshBoard();

							final Random rand = mygame.getRandom();
							n = rand.nextInt(11);

							if (n < 3) {
								cash = 100 * (rand.nextInt(30) + 1);
								mygame.deal((-1) * cash, mygame.turn, "Lost: ");
							} else if (n < 6) {
								cash = 100 * (rand.nextInt(30) + 1);
								mygame.deal(cash, mygame.turn, "Get: ");
							} else if (6 == n) {
								cash = -1 * (mygame.p_money[mygame.turn] / 100) * 10;
								if (0 == cash) {
									mygame.p_status[mygame.turn] = "Nothing happen.";
								} else {
									mygame.deal(cash, mygame.turn, "Income Tax: ");
								}
							} else if (7 == n) {
								go_ckshall();
								continue;
							} else if (8 == n) {
								go_start();
								continue;
							} else if (9 == n) {
								forward();
								refreshBoard();
								try {
									Thread.sleep((long) (speedModifier * 2000));
								} catch (final InterruptedException e) {
									e.printStackTrace();
									Thread.currentThread().interrupt();
									return;
								}
								continue;
							} else if (10 == n) {
								stop(1);
							}
						} else if (3 == gameMap.type[id]) {
							switch (gameMap.id[id]) {
								case 36:
									go_jail();
									continue;
								case 37:
									go_hospital();
									continue;
								case 38:
									land_tax();
									break;
								case 39:
									house_tax();
									break;
								default:
									break;
							}
						}

						if (mygame.property.isVisible()) {
							mygame.property.show(gameMap);
						}

						if (1 == mygame.p_type[mygame.turn]) {
							refreshBoard();
							try {
								Thread.sleep((long) (speedModifier * 2000));
							} catch (final InterruptedException e) {
								e.printStackTrace();
								Thread.currentThread().interrupt();
								return;
							}
						}

						mygame.pshow_sqmark[mygame.turn] = false;
						mygame.turn = (mygame.turn + 1) % Game.maxPSize;
					}
				}

				refreshBoard();
			}
		}

		if (mygame.property.isVisible()) {
			mygame.property.show(gameMap);
		}
	}

	public static int playerNumber(final Game game) {
		int n = 0;
		final int numPlayers = 4;
		for (int pi = 0; pi < numPlayers; pi++) {
			if (game.p_type[pi] != 9) {
				n++;
			}
		}
		return n;
	}

	public void payout() {
		final int id = mygame.p_dest_id[mygame.turn];
		final int doub = mygame.double_fee(gameMap, id);
		final long fee = mygame.toll(gameMap, doub, id);
		final int owner = gameMap.owner[id] - 1;
		if (1 != mygame.p_in_jail[owner]) {
			mygame.deal(-1 * fee, mygame.turn, "Toll: ");
			mygame.deal(fee, owner, "Get ");
		}
	}

	public void land_tax() {
		final int turn_id = mygame.turn;
		int land_number = 0;
		final long tax = 400;
		for (int i = 0; i < gameMap.size; i++) {
			if (turn_id + 1 == gameMap.owner[i] && 0 == gameMap.type[i]) {
				land_number++;
			}
		}
		final long fee = tax * land_number;
		mygame.deal(-1 * fee, turn_id, "Land Tax: ");
	}

	public void house_tax() {
		final int turn_id = mygame.turn;
		int house_number = 0;
		final long tax = 200;
		for (int i = 0; i < gameMap.size; i++) {
			if (turn_id + 1 == gameMap.owner[i] && 0 == gameMap.type[i]) {
				house_number += gameMap.level[i];
			}
		}
		final long fee = tax * house_number;
		mygame.deal(-1 * fee, turn_id, "House Tax: ");
	}

	public void go_jail() {
		final int turn_id = mygame.turn;
		mygame.p_in_jail[turn_id] = 1;
		mygame.p_id[turn_id] = (gameMap.jailId - 1) % gameMap.size;
		mygame.p_dest_id[turn_id] = gameMap.jailId;
		mygame.move_start = true;
		stop(3);
	}

	public void go_hospital() {
		final int turn_id = mygame.turn;
		mygame.p_dest_id[turn_id] = gameMap.hospitalId;
		mygame.p_id[turn_id] = (gameMap.hospitalId - 1) % gameMap.size;
		mygame.move_start = true;
		mygame.deal(-1 * mygame.hospital_fee, turn_id, "Hospital Fee: ");
	}

	public void go_ckshall() {
		final int turn_id = mygame.turn;
		mygame.p_status[turn_id] = "Go to CKS Memorial Hall.";
		mygame.p_id[turn_id] = (gameMap.ckshallId - 1) % gameMap.size;
		mygame.p_dest_id[turn_id] = gameMap.ckshallId;
		mygame.move_start = true;
	}

	public void go_start() {
		final int turn_id = mygame.turn;
		mygame.p_status[turn_id] = "Go to start point.";
		mygame.p_id[turn_id] = gameMap.size - 1;
		mygame.p_dest_id[turn_id] = 0;
		mygame.move_start = true;
	}

	public void forward() {
		final int turn_id = mygame.turn;
		final Random rand = mygame.getRandom();
		final int move_step = rand.nextInt(12) + 1;
		mygame.p_status[turn_id] = "Forward " + move_step + " step(s).";
		mygame.p_dest_id[turn_id] = (mygame.p_id[turn_id] + move_step) % gameMap.size;
		mygame.move_start = true;
	}

	public void stop(final int stop_turn) {
		final int turn_id = mygame.turn;
		mygame.p_stop[turn_id] = stop_turn;
		if (1 == mygame.p_in_jail[turn_id]) {
			mygame.p_status[turn_id] = "In jail. Stop " + stop_turn + " turn.";
		} else {
			mygame.p_status[turn_id] = "Stop " + stop_turn + " turn.";
		}
	}
}
