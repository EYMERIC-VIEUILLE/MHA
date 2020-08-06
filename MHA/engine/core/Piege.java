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

package mha.engine.core;

import java.io.*;

public class Piege extends Lieu {

    private Troll createur;
    private int degat;
    private int bmmDegat=0;

    public Piege(int x,int y, int n,Troll c, int d) 
    {
	super("Piège",x,y,n);
	createur=c;
	degat=d;
    }

    public Piege(int x,int y, int n,Troll c, int d,int dm) 
    {
	this(x,y,n,c,d);
	bmmDegat=dm;
    }
    
    public int getPosX()
    {
    	return posx;
    }
    
    public int getPosY()
    {
    	return posy;
    }
        
    public int getPosN()
    {
    	return posn;
    }
    
    public String getPos()
    {
    	return posx+" "+posy+" "+posn;
    }
        
    public String getName()
    {
    	return name;
    }
    
    public Troll getCreateur()
    {
    	return createur;
    }
    
    public int getDegat()
    {
    	return degat;
    }

    public int getBMMDegat()
    {
    	return bmmDegat;
    }
    
    public String getInfos()
    {
    	return "Vous vous trouvez sur la même case qu'un piège\nIl fait pas très bon de rester proche d'un lieu comme cela.";
    }
}
