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

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Random;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game {
	public static PlayerSettings gs1 = new PlayerSettings(0);
	public ImageIcon image1 = new ImageIcon(Game.class.getResource("/Image/gtkmonop-token1.png"));
	public ImageIcon image2 = new ImageIcon(Game.class.getResource("/Image/gtkmonop-token8.png"));
	public ImageIcon image3 = new ImageIcon(Game.class.getResource("/Image/gtkmonop-token7.png"));
	public ImageIcon image4 = new ImageIcon(Game.class.getResource("/Image/gtkmonop-token6.png"));
	public ImageIcon image5 = new ImageIcon(Game.class.getResource("/Image/gtkmonop-token5.png"));
	public ImageIcon image6 = new ImageIcon(Game.class.getResource("/Image/gtkmonop-token4.png"));
	public ImageIcon image7 = new ImageIcon(Game.class.getResource("/Image/gtkmonop-token3.png"));
	public ImageIcon image8 = new ImageIcon(Game.class.getResource("/Image/gtkmonop-token2.png"));

	public ImageIcon imagep1 = new ImageIcon(Game.class.getResource("/Image/pawn1.gif"));
	public ImageIcon imagep2 = new ImageIcon(Game.class.getResource("/Image/pawn2.gif"));
	public ImageIcon imagep3 = new ImageIcon(Game.class.getResource("/Image/pawn3.gif"));
	public ImageIcon imagep4 = new ImageIcon(Game.class.getResource("/Image/pawn4.gif"));
	public ImageIcon imagep5 = new ImageIcon(Game.class.getResource("/Image/pawn5.gif"));
	public ImageIcon imagep6 = new ImageIcon(Game.class.getResource("/Image/pawn6.gif"));
	public ImageIcon imagep7 = new ImageIcon(Game.class.getResource("/Image/pawn7.gif"));
	public ImageIcon imagep8 = new ImageIcon(Game.class.getResource("/Image/pawn8.gif"));

	public ImageIcon iarrow = new ImageIcon(Game.class.getResource("/Image/gtkmonop-go-0.png"));
	public ImageIcon ijail = new ImageIcon(Game.class.getResource("/Image/jail.jpg"));
	public ImageIcon ickshall = new ImageIcon(Game.class.getResource("/Image/CKS_Memorial_Hall.jpg"));
	public ImageIcon ihospital = new ImageIcon(Game.class.getResource("/Image/Hospital.jpg"));

	public ImageIcon ihouse = new ImageIcon(Game.class.getResource("/Image/house.png"));
	public ImageIcon ihouse_left = new ImageIcon(Game.class.getResource("/Image/house_left.png"));
	public ImageIcon ihouse_up = new ImageIcon(Game.class.getResource("/Image/house_up.png"));
	public ImageIcon ihouse_right = new ImageIcon(Game.class.getResource("/Image/house_right.png"));
	public ImageIcon ihotel = new ImageIcon(Game.class.getResource("/Image/hotel.png"));
	public ImageIcon ihotel_left = new ImageIcon(Game.class.getResource("/Image/hotel_left.png"));
	public ImageIcon ihotel_up = new ImageIcon(Game.class.getResource("/Image/hotel_up.png"));
	public ImageIcon ihotel_right = new ImageIcon(Game.class.getResource("/Image/hotel_right.png"));

	public ImageIcon iquestionmark = new ImageIcon(Game.class.getResource("/Image/questionmark_60x79.png"));
	public ImageIcon iquestionmark_left = new ImageIcon(Game.class.getResource("/Image/questionmark_60x79_left.png"));
	public ImageIcon iquestionmark_right = new ImageIcon(Game.class.getResource("/Image/questionmark_60x79_right.png"));
	public ImageIcon iquestionmark_up = new ImageIcon(Game.class.getResource("/Image/questionmark_60x79_up.png"));
    
    public ImageIcon isqmark = new ImageIcon(Game.class.getResource("/Image/sqmark_8x11.png"));

	public static final String s36_1 = "Go to";
	public static final String s36_2 = "Jail";
	public static final String s37_1 = "Go to";
	public static final String s37_2 = "Hospital";
	public static final String s38_1 = "Land";
	public static final String s38_2 = "Tax";
	public static final String s39_1 = "House";
	public static final String s39_2 = "Tax";

	public String skaching;

	public JButton btnNewButton;
	public Dice dice;

	public JButton btnPropertyButton;
	public Property property;

	public static int maxPSize = 4;
	/** 0: player1 */
	public int turn;
	public boolean move_start;
    /** true: show small question mark */
    public boolean[] pshow_sqmark;
	/** 0: player 1, 3: player 4 */
	public String[] p_name;
	/** 0:human, 1:AI, 9:out */
	public int[] p_type;
	public int[] p_icon;
	public ImageIcon[] p_ic;
	public ImageIcon[] p_pawn;
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

	public Game(final long seed) {
		this.skaching = "/Sound/kaching.wav";
		this.btnNewButton = new JButton("Roll Dice");
		this.dice = new Dice(this);
		this.btnPropertyButton = new JButton("Player's Property");
		this.property = new Property(this);
		this.turn = 0;
		this.move_start = false;
		this.hospital_fee = 1000;
		this.cross_cash = 2000;
		this.p_pawn = new ImageIcon[maxPSize];
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
		this.p_ic = new ImageIcon[maxPSize];
		this.p_icon = new int[maxPSize];
		this.p_type = new int[maxPSize];
		this.p_name = new String[maxPSize];
		this.random = new Random(seed);
		System.out.println("random seed: " + seed);
	}

	public Game() {
		this(System.currentTimeMillis());
	}

	public Random getRandom() {
		return random;
	}

	//playSound modified from http://stackoverflow.com/questions/26305/how-can-i-play-sound-in-java
	//I personally made this code that works fine. I think it only works with .wav format.
	public static synchronized void playSound(final String url) {
		new Thread(new Runnable() {
			// The wrapper thread is unnecessary, unless it blocks on the
			// Clip finishing
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
		int doub, owner1, owner2, owner3;

		owner1 = gameMap.owner[gameMap.sameColor[GameMap.colorIndex(gameMap.color[i])][0]];
		owner2 = gameMap.owner[gameMap.sameColor[GameMap.colorIndex(gameMap.color[i])][1]];
		owner3 = gameMap.owner[gameMap.sameColor[GameMap.colorIndex(gameMap.color[i])][2]];
		if (owner1 == owner2 && owner2 == owner3) {
			doub = 2;
		} else {
			doub = 1;
		}

		return doub;
	}

	public long toll(final GameMap gameMap, final int doub, final int i) {
		long fee, basicMoney;

		basicMoney = (long)(0.2 * gameMap.value[i]);
		fee = (long) (doub * Math.pow(2, gameMap.level[i]) * basicMoney);

		return fee;
	}

	public static void main(final String[] args) {
		final Game game = new Game();
		final JFrame frame = new JFrame("Random Big Rich Man");
		frame.setSize(300, 300); // won't show New Game button in macOS 15.6
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		final JButton btnANewGame = new JButton("A New Game");
		btnANewGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				frame.setVisible(false);
				gs1.show(frame, game);
			}
		});
		btnANewGame.setBounds(10, 112, 264, 23);
		panel.add(btnANewGame);
		frame.setVisible(true);
	}
}
