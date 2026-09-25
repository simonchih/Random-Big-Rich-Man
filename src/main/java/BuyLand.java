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

public class BuyLand {

	private final Game game;
	private final GameMap gameMap;
	private final GameLoop gameLoop;

	BuyLand(final Game game, final GameMap gameMap, final GameLoop gameLoop) {
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
        final int player=game.turn, tile=game.p_dest_id[player];
        DealDialog.show(game,gameLoop,"購入土地",game.p_name[player]+"，要購入 "+gameMap.name[tile]+" 嗎？",
            gameMap.value[tile],8,() -> {
                game.deal(-gameMap.value[tile],player,"Buy Land: ");
                gameMap.owner[tile]=player+1;
            });
    }
}
