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


package mha.engine;

import java.util.Vector;


public class MHAController {

    private Vector obs;

    public MHAController() {
	obs = new Vector();
    }

    public synchronized void addListener(MHAListener o) {
        if (o == null)
            throw new NullPointerException();
	if (!obs.contains(o)) {
	    obs.addElement(o);
	}
    }

    public synchronized void deleteListener(MHAListener o) {
        obs.removeElement(o);
    }


    public void sendMessage(String output, boolean a, boolean b) {

        Object[] arrLocal;

	synchronized (this) {
            arrLocal = obs.toArray();
        }

        for (int i = arrLocal.length-1; i>=0; i--)
            ((MHAListener)arrLocal[i]).sendMessage(output,a,b);
    }

    public void needInput(int s) {

        Object[] arrLocal;

	synchronized (this) {
            arrLocal = obs.toArray();
        }

        for (int i = arrLocal.length-1; i>=0; i--)
            ((MHAListener)arrLocal[i]).needInput(s);

    }

    public void noInput() {

        Object[] arrLocal;

	synchronized (this) {
            arrLocal = obs.toArray();
        }

        for (int i = arrLocal.length-1; i>=0; i--)
            ((MHAListener)arrLocal[i]).noInput();

    }

    public void setGameStatus(String state) {

        Object[] arrLocal;

	synchronized (this) {
            arrLocal = obs.toArray();
        }

        for (int i = arrLocal.length-1; i>=0; i--)
            ((MHAListener)arrLocal[i]).setGameStatus(state);

    }

    public void newGame(boolean t) {

        Object[] arrLocal;

	synchronized (this) {
            arrLocal = obs.toArray();
        }

        for (int i = arrLocal.length-1; i>=0; i--)
            ((MHAListener)arrLocal[i]).newGame(t);

    }

    public void startGame(boolean s) {

        Object[] arrLocal;

	synchronized (this) {
            arrLocal = obs.toArray();
        }

        for (int i = arrLocal.length-1; i>=0; i--)
            ((MHAListener)arrLocal[i]).startGame(s);

    }

    public void closeGame() {

        Object[] arrLocal;

	synchronized (this) {
            arrLocal = obs.toArray();
        }

        for (int i = arrLocal.length-1; i>=0; i--)
            ((MHAListener)arrLocal[i]).closeGame();

    }

    public void setSlider(int min, int c1num, int c2num) {

        Object[] arrLocal;

	synchronized (this) {
            arrLocal = obs.toArray();
        }

        for (int i = arrLocal.length-1; i>=0; i--)
            ((MHAListener)arrLocal[i]).setSlider(min,c1num,c2num);

    }

    public void armiesLeft(int l, boolean s) {

        Object[] arrLocal;

	synchronized (this) {
            arrLocal = obs.toArray();
        }

        for (int i = arrLocal.length-1; i>=0; i--)
            ((MHAListener)arrLocal[i]).armiesLeft(l,s);

    }

    public void showDice(int n, boolean w) {

        Object[] arrLocal;

	synchronized (this) {
            arrLocal = obs.toArray();
        }

        for (int i = arrLocal.length-1; i>=0; i--)
            ((MHAListener)arrLocal[i]).showDice(n,w);

    }

    public void showMapPic(java.awt.Image p) {

        Object[] arrLocal;

	synchronized (this) {
            arrLocal = obs.toArray();
        }

        for (int i = arrLocal.length-1; i>=0; i--)
            ((MHAListener)arrLocal[i]).showMapPic(p);

    }

    public void showCardsFile(String c, boolean m) {

        Object[] arrLocal;

	synchronized (this) {
            arrLocal = obs.toArray();
        }

        for (int i = arrLocal.length-1; i>=0; i--)
            ((MHAListener)arrLocal[i]).showCardsFile(c, m);

    }

    public void serverState(boolean s) {

        Object[] arrLocal;

	synchronized (this) {
            arrLocal = obs.toArray();
        }

        for (int i = arrLocal.length-1; i>=0; i--)
            ((MHAListener)arrLocal[i]).serverState(s);

    }

    public void openBattle(int c1num, int c2num) {

        Object[] arrLocal;

	synchronized (this) {
            arrLocal = obs.toArray();
        }

        for (int i = arrLocal.length-1; i>=0; i--)
            ((MHAListener)arrLocal[i]).openBattle(c1num,c2num);

    }

    public void closeBattle() {

        Object[] arrLocal;

	synchronized (this) {
            arrLocal = obs.toArray();
        }

        for (int i = arrLocal.length-1; i>=0; i--)
            ((MHAListener)arrLocal[i]).closeBattle();

    }

    public void addPlayer(int type, String name, java.awt.Color color, String ip) {

        Object[] arrLocal;

	synchronized (this) {
            arrLocal = obs.toArray();
        }

        for (int i = arrLocal.length-1; i>=0; i--)
            ((MHAListener)arrLocal[i]).addPlayer(type, name, color, ip);

    }

    public void delPlayer(String name) {

        Object[] arrLocal;

	synchronized (this) {
            arrLocal = obs.toArray();
        }

        for (int i = arrLocal.length-1; i>=0; i--)
            ((MHAListener)arrLocal[i]).delPlayer(name);

    }

    public void showDiceResults(int[] att, int[] def) {

        Object[] arrLocal;

	synchronized (this) {
            arrLocal = obs.toArray();
        }

        for (int i = arrLocal.length-1; i>=0; i--)
            ((MHAListener)arrLocal[i]).showDiceResults(att,def);

    }

    public void setNODAttacker(int n) {

        Object[] arrLocal;

	synchronized (this) {
            arrLocal = obs.toArray();
        }

        for (int i = arrLocal.length-1; i>=0; i--)
            ((MHAListener)arrLocal[i]).setNODAttacker(n);

    }

    public void setNODDefender(int n) {

        Object[] arrLocal;

	synchronized (this) {
            arrLocal = obs.toArray();
        }

        for (int i = arrLocal.length-1; i>=0; i--)
            ((MHAListener)arrLocal[i]).setNODDefender(n);

    }

    public void sendDebug(String a) {

        Object[] arrLocal;

	synchronized (this) {
            arrLocal = obs.toArray();
        }

        for (int i = arrLocal.length-1; i>=0; i--)
            ((MHAListener)arrLocal[i]).sendDebug(a);

    }

}
