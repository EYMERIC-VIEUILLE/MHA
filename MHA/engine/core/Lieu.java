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

public class Lieu {

    protected String name;
    protected  int posx;
    protected  int posy;
    protected  int posn;
    protected  int id;
    private static int nblieu=0;

    public Lieu(String na,int x,int y, int n) 
    {
	name=na;
	id=nblieu++;
	posx=x;
	posy=y;
	posn=n;
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
    
    public String getInfos()
    {
    	return toString();
    }
 
    
    public String toString()
    {
    	return id+" "+name+" "+getPos();
    }
}
