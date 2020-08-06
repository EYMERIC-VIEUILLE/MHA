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

import java.io.*;
import java.math.BigInteger;
import javax.swing.*;
import java.awt.Toolkit;
import java.util.Vector;


public class InfoTroll {
    
    private String name;
    private int id;
    private int race;
    private int niveau;
    private int team=-1;
    private ImageIcon icon;

    public InfoTroll(String n, int i,int l, int r) {
	name = n;
	id = i;
	race=r;
	niveau=l;
    }

    public String getRace()
    {
    	String r="Skrim";
	if(race==1)
		r="Durakuir";
	else if(race==2)
		r="Kastar";
	else if(race==3)
		r="Tomawak";
	return r;
    }

    public String getNom() {
        return name;
    }

    public int getId() {
        return id;
    }
    
    public int getNiveau()
    {
        return niveau;
    }

    public int getRaceById()
    {
        return race;
    }    
    
    public void setTeam(int i)
    {
    	team=i;
    }
    
    public int getTeam()
    {
    	return team;
    }

    public ImageIcon getIcon()
    {
    	return icon;
    }

    public void setIcon(String s)
    {
	BigInteger bi = new BigInteger(s, 16);
	byte[] data = bi.toByteArray();
	icon=new ImageIcon(Toolkit.getDefaultToolkit().createImage(data));
    }

}
