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

public abstract class MHAAdapter implements MHAListener {

    public void sendMessage(String output, boolean a, boolean b) {}
    public void needInput(int s) {}
    public void noInput() {}
    public void setGameStatus(String state) {}
    public void newGame(boolean t) {}
    public void startGame(boolean s) {}
    public void closeGame() {}
    public void setSlider(int min, int c1num, int c2num) {}
    public void armiesLeft(int l, boolean s) {}
    public void showDice(int n, boolean w) {}
    public void showMapPic(java.awt.Image p) {}
    public void showCardsFile(String c, boolean m) {}
    public void serverState(boolean s) {}
    public void openBattle(int c1num, int c2num) {}
    public void closeBattle() {}
    public void addPlayer(int type, String name, java.awt.Color color, String ip) {}
    public void delPlayer(String name) {}
    public void showDiceResults(int[] att, int[] def) {}
    public void setNODAttacker(int n) {}
    public void setNODDefender(int n) {}
    public void sendDebug(String a) {}

}
