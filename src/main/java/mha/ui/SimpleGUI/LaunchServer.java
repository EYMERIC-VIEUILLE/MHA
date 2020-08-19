/*********************************************************************************
 *    This file is part of Mountyhall Arena                                       *
 *                                                                                *
 *    Mountyhall Arena is free software; you can redistribute it and/or modify    *
 *    it under the terms of the GNU General Public License as published by        *
 *    the Free Software Foundation; either version 2 of the License, or           *
 *    (at your option) any later version.                                         *
 *                                                                                *
 *    Mountyhall Arena is distributed in the hope that it will be useful,         *
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of              *
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the               *
 *    GNU General Public License for more details.                                *
 *                                                                                *
 *    You should have received a copy of the GNU General Public License           *
 *    along with Mountyzilla; if not, write to the Free Software                  *
 *    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA  *
 *********************************************************************************/

package mha.ui.SimpleGUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import mha.engine.*;
import mha.engine.core.*;
import java.net.*;

public class LaunchServer extends JPanel implements ActionListener {
	final JButton cancelButton, launchButton;

	final JTextField joueursMax, teamMax, respawnMax, tempsMax, tailleArene;

	final JRadioButton dm, tdm;

	final JCheckBox invi, tp, camou, regroup;

	final JDialog dialog;

	static final long serialVersionUID = 1231321531;

	MHA mha;

	JFrame gui;

	public LaunchServer(JFrame g, MHA m) {
		super();
		gui = g;
		mha = m;
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new java.awt.Insets(7, 3, 3, 3);
		setLayout(gridbag);
		c.fill = GridBagConstraints.BOTH;

		JPanel jpg = new JPanel();
		jpg.setLayout(new BoxLayout(jpg, BoxLayout.PAGE_AXIS));
		dm = new JRadioButton("Deathmatch");
		dm.setSelected(true);
		dm.addActionListener(this);
		tdm = new JRadioButton("Team Deathmatch");
		tdm.addActionListener(this);
		ButtonGroup group = new ButtonGroup();
		group.add(dm);
		group.add(tdm);
		jpg.add(dm);
		jpg.add(tdm);
		jpg.setBorder(BorderFactory.createTitledBorder("Type de partie"));

		c.gridx = 0; // col
		c.gridy = 0; // row
		c.gridwidth = 3; // width
		c.gridheight = 1; // height
		JLabel jl = new JLabel("Lancer une partie", null, SwingConstants.CENTER);
		jl.setFont(jl.getFont().deriveFont(jl.getFont().getSize2D() + 10));
		add(jl, c);

		c.gridx = 0; // col
		c.gridy = 1; // row
		c.gridwidth = 1; // width
		c.gridheight = 9; // height
		add(jpg, c);

		c.gridx = 0; // col
		c.gridy = 10; // row
		c.gridwidth = 1; // width
		c.gridheight = 1; // height
		cancelButton = new JButton("Annuler");
		cancelButton.addActionListener(this);
		add(cancelButton, c);

		c.gridx = 1; // col
		c.gridy = 1; // row
		add(new JLabel("Nombre max de joueurs : ", SwingConstants.RIGHT), c);
		c.gridy++;
		add(
				new JLabel("Nombre max d'équipes (8 Max.) : ",
						SwingConstants.RIGHT), c);
		c.gridy++;
		add(new JLabel("Nombre de résurection : ", SwingConstants.RIGHT), c);
		c.gridy++;
		add(new JLabel(
				"Nombre de secondes max pour jouer sa DLA (0=infini) : ",
				SwingConstants.RIGHT), c);
		c.gridy++;
		add(new JLabel("Taille de l'arène (0=auto) : ", SwingConstants.RIGHT),
				c);
		c.gridy++;
		add(new JLabel("Interdire l'utilisation de invisibilité : ",
				SwingConstants.RIGHT), c);
		c.gridy++;
		add(new JLabel("Interdire l'utilisation de téléportation : ",
				SwingConstants.RIGHT), c);
		c.gridy++;
		add(new JLabel("Les Tomawaks commencent camouflés ",
				SwingConstants.RIGHT), c);
		c.gridy++;
		add(new JLabel("Les équipes commencent regroupées ",
				SwingConstants.RIGHT), c);

		c.gridx = 2; // col
		c.gridy = 1; // row
		joueursMax = new JTextField("5", 5);
		add(joueursMax, c);
		c.gridy++;
		teamMax = new JTextField("5", 5);
		teamMax.setEnabled(false);
		add(teamMax, c);
		c.gridy++;
		respawnMax = new JTextField("0", 5);
		add(respawnMax, c);
		c.gridy++;
		tempsMax = new JTextField("150", 5);
		add(tempsMax, c);
		c.gridy++;
		tailleArene = new JTextField("0", 5);
		add(tailleArene, c);
		c.gridy++;
		invi = new JCheckBox();
		add(invi, c);
		c.gridy++;
		tp = new JCheckBox();
		add(tp, c);
		c.gridy++;
		camou = new JCheckBox();
		add(camou, c);
		c.gridy++;
		regroup = new JCheckBox();
		regroup.setEnabled(false);
		add(regroup, c);
		c.gridy++;
		launchButton = new JButton("Lancer");
		launchButton.addActionListener(this);
		add(launchButton, c);

		dialog = new JDialog(gui, "Démarrer une partie MHA");
		dialog.setContentPane(this);
		// dialog.setSize(new Dimension(500,280));
		dialog.pack();
		dialog.setSize(dialog.getWidth() + 10, dialog.getHeight() + 10);
		dialog.setLocationRelativeTo(g);
		dialog.setResizable(false);
		dialog.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cancelButton) {
			dialog.setVisible(false);
			dialog.dispose();
		} else if (e.getSource() == launchButton) {
			int nbJ, nbT, nbR, tmpM, tA;
			if (joueursMax.getText().length() == 0
					|| teamMax.getText().length() == 0
					|| respawnMax.getText().length() == 0
					|| tempsMax.getText().length() == 0) {
				JOptionPane.showMessageDialog(dialog,
						"Merci de remplir tous les champs", "Erreur",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (mha.getChatArea() != null) {
				JOptionPane.showMessageDialog(dialog,
						"Un serveur a déja été démarré", "Erreur",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			try {
				nbJ = Integer.parseInt(joueursMax.getText());
				nbT = Integer.parseInt(teamMax.getText());
				tA = Integer.parseInt(tailleArene.getText());
				nbR = Integer.parseInt(respawnMax.getText());
				tmpM = Integer.parseInt(tempsMax.getText());
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(dialog,
						"Les champs doivent être des chiffres", "Erreur",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (nbJ < 2) {
				JOptionPane.showMessageDialog(dialog,
						"Il doit y avoir au moins 2 joueurs.", "Erreur",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (tdm.isSelected() && nbT < 2) {
				JOptionPane.showMessageDialog(dialog,
						"Il doit y avoir au moins 2 équipes.", "Erreur",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (nbT > 8) {
				JOptionPane.showMessageDialog(dialog,
						"Il ne peut y avoir qu'au maximum 8 équipes.",
						"Erreur", JOptionPane.ERROR_MESSAGE);
				return;
			}
			String s = "Vous voulez commencer une partie :\n";
			if (dm.isSelected())
				s += "Royal rumble\n";
			else if (tdm.isSelected())
				s += "Par guilde\n";
			s += "Il y aura un maximum " + nbJ + " joueurs\n" + nbT
					+ " équipes\nChaque joueur pourra réapparaitre " + nbR
					+ " fois";
			if (invi.isSelected())
				s += "\nL'utilisation d'invisibilité a été interdite";
			if (tp.isSelected())
				s += "\nL'utilisation de téléportation a été interdite";
			if (camou.isSelected())
				s += "\nLes Tomawaks commencent camouflés";
			// JOptionPane.showMessageDialog(dialog, s, "Infos",
			// JOptionPane.PLAIN_MESSAGE);

			try {
				ChatArea ca = new ChatArea(mha.getControler(), mha, nbJ);
				mha.setChatArea(ca);
				mha.getControler().serverState(true);
				dialog.setVisible(false);
				dialog.dispose();

				MHAGame.setTPPossible(!tp.isSelected());
				MHAGame.setInviPossible(!invi.isSelected());
				MHAGame.setRegroupe(regroup.isSelected());
				MHAGame.setTomCamouflés(camou.isSelected());
				if (tmpM > 0)
					ca.getServer().time_blitz = tmpM * 1000;
				// System.out.println(ca.getServer().time_blitz);
				MHAGame.setNbrResu(nbR);
				if (tdm.isSelected()) {
					MHAGame.setMode(MHAGame.MODE_TEAM_DEATHMATCH);
					MHAGame.setNbrTeam(nbT);
					MHAGame.setSizeArena(tA);
				} else
					MHAGame.setMode(MHAGame.MODE_DEATHMATCH);

				mha.getControler().sendMessage("Le serveur a démarré", false,
						true);

			} catch (BindException ex1) {
				JOptionPane.showMessageDialog(dialog,
						"Le port est déja utilisé par une autre application",
						"Erreur", JOptionPane.ERROR_MESSAGE);
				return;
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			return;
		} else if (e.getSource() == tdm) {
			teamMax.setEnabled(true);
			regroup.setEnabled(true);
		} else if (e.getSource() == dm) {
			teamMax.setEnabled(false);
			regroup.setEnabled(false);
		}
	}

	public static void main(String[] s) {
		new LaunchServer(null, null);
	}
}
