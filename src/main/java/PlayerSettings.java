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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Set;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

public class PlayerSettings {

	private static final int NUM_PLAYERS = 4;

	private final int playerNum;
	private final int playerIdx;

	public PlayerSettings(final int playerIdx) {

		this.playerIdx = playerIdx;
		this.playerNum = playerIdx + 1;
	}

	private static <AVT> AVT[] removeIndices(final AVT[] orig, final Set<Integer> indices) {

		final AVT[] smaller = (AVT[]) Array.newInstance(orig.getClass().getComponentType(), orig.length - indices.size());
		int iSmaller = 0;
		for (int i = 0; i < orig.length; i++) {
			if (!indices.contains(i)) {
				smaller[iSmaller++] = orig[i];
			}
		}
		return smaller;
	}

	/**
	 * @param f
	 * @param game
	 * @wbp.parser.entryPoint
	 */
	public void show(final JFrame f, final Game game) {

		final boolean firstPlayer = (playerIdx == 0);
		final boolean humanPlayer = firstPlayer;
		final boolean lastPlayer = (playerIdx == (NUM_PLAYERS - 1));

		final PlayerSettings gs3;
		final MainMap mmap;
		if (lastPlayer) {
			gs3 = null;
			mmap = new MainMap(game);
		} else {
			gs3 = new PlayerSettings(playerIdx + 1);
			mmap = null;
		}

		final ImageIcon[] PLAYER_ICONS = new ImageIcon[] {
			game.image1,
			game.image2,
			game.image3,
			game.image4,
			game.image5,
			game.image6,
			//game.image7,
			game.image8
		};
		final ImageIcon[] PLAYER_FIGURES = new ImageIcon[] {
			game.imagep1,
			game.imagep2,
			game.imagep3,
			game.imagep4,
			game.imagep5,
			game.imagep6,
			//game.imagep7,
			game.imagep8
		};
		final Integer[] INDICES = new Integer[PLAYER_ICONS.length];
		for (int i = 0; i < INDICES.length; i++) {
			INDICES[i] = i;
		}

		final Set<Integer> toRemoveInds = new HashSet<>(playerIdx);
		for (int i = 0; i < playerIdx; i++) {
			toRemoveInds.add(game.p_icon[i]);
		}

		final ImageIcon[] filteredIcons = removeIndices(PLAYER_ICONS, toRemoveInds);
		final Integer[] filteredIndices = removeIndices(INDICES, toRemoveInds);

		JFrame playerSettingsFrame = new JFrame("Player" + playerNum + " Setting");
		playerSettingsFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		playerSettingsFrame.getContentPane().removeAll();
		playerSettingsFrame.setSize(450, 320);
		playerSettingsFrame.getContentPane().setLayout(null);

		final JLabel lblName = new JLabel("Name");
		lblName.setBounds(122, 40, 46, 15);
		playerSettingsFrame.getContentPane().add(lblName);

		final JTextField name = new JTextField();
		name.setText("Player" + playerNum);
		name.setBounds(205, 34, 106, 21);
		playerSettingsFrame.getContentPane().add(name);
		lblName.setLabelFor(name);

		final JLabel lblAi = new JLabel("AI");
		lblAi.setBounds(122, 79, 68, 15);
		playerSettingsFrame.getContentPane().add(lblAi);

		final JCheckBox ai = new JCheckBox();
		ai.setSelected(!humanPlayer);
		ai.setBounds(205, 76, 106, 21);
		playerSettingsFrame.getContentPane().add(ai);
		lblAi.setLabelFor(ai);

		final JLabel lblIcon = new JLabel("Icon");
		lblIcon.setBounds(122, 124, 46, 15);
		playerSettingsFrame.getContentPane().add(lblIcon);

		final JComboBox<ImageIcon> icon = new JComboBox<>();
		icon.setBounds(205, 118, 106, 21);
		icon.setModel(new DefaultComboBoxModel<>(filteredIcons));
		icon.setSelectedIndex(0);
		playerSettingsFrame.getContentPane().add(icon);
		lblIcon.setLabelFor(icon);

		final JLabel lblStartingMoney = new JLabel("Starting money");
		lblStartingMoney.setBounds(122, 165, 68, 15);
		playerSettingsFrame.getContentPane().add(lblStartingMoney);

		final JSpinner startingMoney = new JSpinner();
		startingMoney.setValue(30000);
		startingMoney.setBounds(205, 159, 106, 21);
		playerSettingsFrame.getContentPane().add(startingMoney);
		lblStartingMoney.setLabelFor(startingMoney);

		final JButton btnCancel = new JButton("Previous");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				playerSettingsFrame.setVisible(false);
				f.setVisible(true);
			}
		});
		btnCancel.setBounds(85, 214, 155, 23);
		playerSettingsFrame.getContentPane().add(btnCancel);

		final JLabel lblErrorValue = new JLabel("(error value)");
		lblErrorValue.setForeground(Color.RED);
		lblErrorValue.setBounds(321, 40, 103, 15);
		lblErrorValue.setVisible(false);
		playerSettingsFrame.getContentPane().add(lblErrorValue);

		final JButton btnNext = new JButton("Next");
		if (lastPlayer) {
			btnNext.setText("Finish");
		}
		btnNext.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					game.p_name[playerIdx] = name.getText();
				} catch(final Exception e1) {
					lblErrorValue.setVisible(true);
					return;
				}
				lblErrorValue.setVisible(false);

				game.p_money[playerIdx] = (Integer) startingMoney.getValue();
				game.p_type[playerIdx] = ai.isSelected() ? 1 : 0;
				game.p_icon[playerIdx] = filteredIndices[icon.getSelectedIndex()];
				game.p_ic[playerIdx] = PLAYER_ICONS[game.p_icon[playerIdx]];
				game.p_pawn[playerIdx] = PLAYER_FIGURES[game.p_icon[playerIdx]];

				playerSettingsFrame.setVisible(false);
				if (lastPlayer) {
					mmap.generate_map(game);
				} else {
					gs3.show(playerSettingsFrame, game);
				}
			}
		});
		btnNext.setBounds(250, 214, 94, 23);
		playerSettingsFrame.getContentPane().add(btnNext);
		playerSettingsFrame.setVisible(true);
	}
}
