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

public class Portail extends Lieu {

    private int cibleX;
    private int cibleY;
    private int cibleN;
    private int dispersion;
    private int duree;
    
    public Portail(int x,int y, int n, int cx, int cy, int cn, int d) 
    {
	super("Portail de téléportation",x,y,n);
		cibleX=cx;
		cibleY=cy;
		cibleN=cn;
		duree=d;
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
    
    public int getCibleX()
    {
    	return cibleX;
    }
    
    public int getCibleY()
    {
    	return cibleY;
    }
    
    public int getCibleN()
    {
    	return cibleN;
    }
    
    public int getDispersion()
    {
    	return dispersion;
    }
    
    
    public int getDuree()
    {
    	return duree;
    }
    
    public String getInfos()
    {
    	return "Vous vous trouvez sur un portail de téléportation qui mène en X = "+cibleX+" | Y = "+cibleY+" | N = "+cibleN+".\nSi vous l'empruntez, vous serez désorienté pendant quelques temps!";
    }
    

}
