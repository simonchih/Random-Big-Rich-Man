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

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

public class Game extends Application {

	public static PlayerSettings gs1 = new PlayerSettings(0);

	public Image image1 = Art.sprite(0), image2 = Art.sprite(1), image3 = Art.sprite(2), image4 = Art.sprite(3);
	public Image image5 = Art.sprite(4), image6 = Art.sprite(5), image7 = Art.sprite(6), image8 = Art.sprite(7);
	public Image imagep1 = image1, imagep2 = image2, imagep3 = image3, imagep4 = image4;
	public Image imagep5 = image5, imagep6 = image6, imagep7 = image7, imagep8 = image8;
	public Image iarrow = Art.sprite(13), ijail = Art.sprite(12), ickshall = Art.sprite(10), ihospital = Art.sprite(11);
	public Image ihouse = Art.sprite(8), ihotel = Art.sprite(9);
	public Image iquestionmark = Art.sprite(14), isqmark = iquestionmark;

	public static final String s36_1 = "Go to";
	public static final String s36_2 = "Jail";
	public static final String s37_1 = "Go to";
	public static final String s37_2 = "Hospital";
	public static final String s38_1 = "Land";
	public static final String s38_2 = "Tax";
	public static final String s39_1 = "House";
	public static final String s39_2 = "Tax";

	public String skaching;

	public Button btnNewButton;
	public Dice dice;

	public Button btnPropertyButton;
	public Property property;

	public static int maxPSize = 4;
	/** 0: player1 */
	public volatile int turn;
	public volatile boolean move_start;
	/** true: show small question mark */
	public boolean[] pshow_sqmark;
	/** 0: player 1, 3: player 4 */
	public String[] p_name;
	/** 0:human, 1:AI, 9:out */
	public int[] p_type;
	public int[] p_icon;
	public Image[] p_ic;
	public Image[] p_pawn;
	public long[] p_money;
	public int[] p_sqmark_x_now;
	public int[] p_sqmark_y_now;
	public int[] p_x_now;
	public int[] p_y_now;
	public final java.util.concurrent.atomic.AtomicReferenceArray<PawnJump> pawnJumps =
		new java.util.concurrent.atomic.AtomicReferenceArray<>(maxPSize);
	public int[] p_id;
	public int[] p_dest_id;
	public String[] p_status;
	public int[] sp_x;
	public int[] sp_y;
	/** 0:out of jail, 1: in jail */
	public int[] p_in_jail;
	/** 0:continue, 1: stop 1 turn ... */
	public int[] p_stop;

	public long cross_cash;
	public long hospital_fee;

	public Random random;
	private volatile Boolean rollButtonDisabledState;

	public Game(final long seed) {
		this.skaching = "/Sound/kaching.wav";
		this.dice = new Dice(this);
		this.turn = 0;
		this.move_start = false;
		this.hospital_fee = 1000;
		this.cross_cash = 2000;
		this.p_pawn = new Image[maxPSize];
		this.pshow_sqmark = new boolean[maxPSize];
		this.p_money = new long[maxPSize];
		this.p_x_now = new int[maxPSize];
		this.p_sqmark_x_now = new int[maxPSize];
		this.p_id = new int[maxPSize];
		this.p_dest_id = new int[maxPSize];
		this.sp_x = new int[maxPSize];
		this.p_stop = new int[maxPSize];
		this.p_in_jail = new int[maxPSize];
		this.sp_y = new int[maxPSize];
		this.p_status = new String[maxPSize];
		this.p_y_now = new int[maxPSize];
		this.p_sqmark_y_now = new int[maxPSize];
		this.p_ic = new Image[maxPSize];
		this.p_icon = new int[maxPSize];
		this.p_type = new int[maxPSize];
		this.p_name = new String[maxPSize];
		this.random = new Random(seed);
		this.rollButtonDisabledState = null;
		System.out.println("random seed: " + seed);
	}

	public Game() {
		this(System.currentTimeMillis());
	}

	public static Image loadImage(final String path) {
		final URL resource = Game.class.getResource(path);
		if (resource == null) {
			throw new IllegalArgumentException("Missing image resource: " + path);
		}
		return new Image(resource.toExternalForm());
	}


	public Random getRandom() {
		return random;
	}

	// playSound modified from http://stackoverflow.com/questions/26305/how-can-i-play-sound-in-java
	// I personally made this code that works fine. I think it only works with .wav format.
	public static synchronized void playSound(final String url) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					final Clip clip = AudioSystem.getClip();
					final AudioInputStream inputStream = AudioSystem.getAudioInputStream(
							Game.class.getResource(url));
					clip.open(inputStream);
					clip.start();
				} catch (final IOException | LineUnavailableException | UnsupportedAudioFileException e) {
					System.err.println(e.getMessage());
				}
			}
		}).start();
	}

	public void deal(final long cash, final int turn_id, final String event) {
		p_money[turn_id] += cash;
		p_status[turn_id] = event + cash;
		if (cash < 0) {
			playSound(skaching);
		}
	}

	public int double_fee(final GameMap gameMap, final int i) {
		int doub;
		final int owner1 = gameMap.owner[gameMap.sameColor[GameMap.colorIndex(gameMap.color[i])][0]];
		final int owner2 = gameMap.owner[gameMap.sameColor[GameMap.colorIndex(gameMap.color[i])][1]];
		final int owner3 = gameMap.owner[gameMap.sameColor[GameMap.colorIndex(gameMap.color[i])][2]];
		if (owner1 == owner2 && owner2 == owner3) {
			doub = 2;
		} else {
			doub = 1;
		}
		return doub;
	}

	public long toll(final GameMap gameMap, final int doub, final int i) {
		final long basicMoney = (long) (0.2 * gameMap.value[i]);
		return (long) (doub * Math.pow(2, gameMap.level[i]) * basicMoney);
	}

	public void setRollButtonDisabled(final boolean disabled) {
		if (btnNewButton == null) {
			return;
		}
		final Boolean previous = rollButtonDisabledState;
		if (previous != null && previous.booleanValue() == disabled) {
			return;
		}
		rollButtonDisabledState = disabled;

		if (Platform.isFxApplicationThread()) {
			btnNewButton.setDisable(disabled);
		} else {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					btnNewButton.setDisable(disabled);
				}
			});
		}
	}

	public void updateSqMarkPosition(final int playerIdx) {
		p_sqmark_x_now[playerIdx] = p_x_now[playerIdx] + 1;
		p_sqmark_y_now[playerIdx] = Math.max(20, p_y_now[playerIdx] - 16);
	}

	@Override
	public void start(final Stage primaryStage) {
		btnNewButton = Theme.button("擲骰子  /  ROLL", true);
		rollButtonDisabledState = btnNewButton.isDisable();
		btnPropertyButton = Theme.button("我的資產  /  PROPERTY", false);
		property = new Property(this);

		final Button btnANewGame = Theme.button("開始新遊戲   →", true);
		btnANewGame.setPrefWidth(260);
		btnANewGame.setOnAction(event -> {
			primaryStage.hide();
			gs1.show(primaryStage, this);
		});

		final javafx.scene.layout.VBox copy = new javafx.scene.layout.VBox(18,
			Theme.label("TAIPEI  ·  FORTUNE EDITION", "eyebrow"),
			Theme.label("瑞德大富翁", "title"), Theme.label("RICHMAN", "heading"),
			Theme.label("一座城市，無限可能。\n選擇你的角色，展開致富旅程。", "subtitle"), btnANewGame);
		copy.setAlignment(Pos.CENTER_LEFT);
		copy.setPadding(new javafx.geometry.Insets(32));
		final javafx.scene.layout.HBox panel = new javafx.scene.layout.HBox(16, copy,
			Art.view(Art.load("taipei"), 510, 340));
		panel.setAlignment(Pos.CENTER);
		panel.setPadding(new javafx.geometry.Insets(24));
		final Scene scene = Theme.scene(panel, 940, 410);
		primaryStage.setTitle("瑞德大富翁 · RICHMAN");
		Theme.decorate(primaryStage);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
		Theme.reveal(panel);
	}

	public static void main(final String[] args) {
		launch(args);
	}
}
