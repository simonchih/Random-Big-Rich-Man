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
import javafx.scene.layout.StackPane;
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

	public Image image1 = loadImage("/Image/gtkmonop-token1.png");
	public Image image2 = loadImage("/Image/gtkmonop-token8.png");
	public Image image3 = loadImage("/Image/gtkmonop-token7.png");
	public Image image4 = loadImage("/Image/gtkmonop-token6.png");
	public Image image5 = loadImage("/Image/gtkmonop-token5.png");
	public Image image6 = loadImage("/Image/gtkmonop-token4.png");
	public Image image7 = loadImage("/Image/gtkmonop-token3.png");
	public Image image8 = loadImage("/Image/gtkmonop-token2.png");

	public Image imagep1 = loadImage("/Image/pawn1.gif");
	public Image imagep2 = loadImage("/Image/pawn2.gif");
	public Image imagep3 = loadImage("/Image/pawn3.gif");
	public Image imagep4 = loadImage("/Image/pawn4.gif");
	public Image imagep5 = loadImage("/Image/pawn5.gif");
	public Image imagep6 = loadImage("/Image/pawn6.gif");
	public Image imagep7 = loadImage("/Image/pawn7.gif");
	public Image imagep8 = loadImage("/Image/pawn8.gif");

	public Image iarrow = loadImage("/Image/gtkmonop-go-0.png");
	public Image ijail = loadImage("/Image/jail.jpg");
	public Image ickshall = loadImage("/Image/CKS_Memorial_Hall.jpg");
	public Image ihospital = loadImage("/Image/Hospital.jpg");

	public Image ihouse = loadImage("/Image/house.png");
	public Image ihouse_left = loadImage("/Image/house_left.png");
	public Image ihouse_up = loadImage("/Image/house_up.png");
	public Image ihouse_right = loadImage("/Image/house_right.png");
	public Image ihotel = loadImage("/Image/hotel.png");
	public Image ihotel_left = loadImage("/Image/hotel_left.png");
	public Image ihotel_up = loadImage("/Image/hotel_up.png");
	public Image ihotel_right = loadImage("/Image/hotel_right.png");

	public Image iquestionmark = loadImage("/Image/questionmark_60x79.png");
	public Image iquestionmark_left = loadImage("/Image/questionmark_60x79_left.png");
	public Image iquestionmark_right = loadImage("/Image/questionmark_60x79_right.png");
	public Image iquestionmark_up = loadImage("/Image/questionmark_60x79_up.png");

	public Image isqmark = loadImage("/Image/sqmark_8x11.png");

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

	private static int intImageHeight(final Image image) {
		return (int) Math.round(image.getHeight());
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
		p_sqmark_y_now[playerIdx] = p_y_now[playerIdx] - intImageHeight(isqmark);
	}

	@Override
	public void start(final Stage primaryStage) {
		btnNewButton = new Button("Roll Dice");
		rollButtonDisabledState = btnNewButton.isDisable();
		btnPropertyButton = new Button("Player's Property");
		property = new Property(this);

		final Button btnANewGame = new Button("A New Game");
		btnANewGame.setPrefWidth(264);
		btnANewGame.setOnAction(event -> {
			primaryStage.hide();
			gs1.show(primaryStage, this);
		});

		final StackPane panel = new StackPane();
		panel.setAlignment(Pos.CENTER);
		panel.getChildren().add(btnANewGame);

		final Scene scene = new Scene(panel, 300, 300);
		primaryStage.setTitle("Random Big Rich Man");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	public static void main(final String[] args) {
		launch(args);
	}
}
