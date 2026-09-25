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

public class BuildHouse {

	private final Game game;
	private final GameMap gameMap;
	private final GameLoop gameLoop;

	BuildHouse(final Game game, final GameMap gameMap, final GameLoop gameLoop) {
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
        if(gameMap.level[tile]>=4) { gameLoop.susp=false; return; }
        final boolean hotel=gameMap.level[tile]==3;
        final long price=(long)(gameMap.value[tile]*(hotel?0.4:0.2));
        DealDialog.show(game,gameLoop,hotel?"興建飯店":"升級房屋",game.p_name[player]+"，投資 "+gameMap.name[tile]+" 的下一階段。",
            price,hotel?9:8,() -> {
                game.deal(-price,player,"Spent: ");
                gameMap.level[tile]++;
            });
    }
}
