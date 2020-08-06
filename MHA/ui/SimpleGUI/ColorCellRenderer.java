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

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.*;
import java.awt.*;
import java.util.*;
 
public class ColorCellRenderer extends DefaultTableCellRenderer
{
  private Vector <Color> couleurs = null;
	
  public ColorCellRenderer(Vector <Color> vc)
  {
    super();
    couleurs = vc;
    setHorizontalAlignment( CENTER );
  }
  
  public void setCouleurs(Vector <Color> c)
  {
  	couleurs=c;
  }
	
  public Component getTableCellRendererComponent(JTable table, Object value,
    boolean isSelected, boolean hasFocus, int row, int column)
  {
    if(row<couleurs.size() && row>=0 && couleurs.elementAt(row)!=null)
    {
    	this.setBackground(couleurs.elementAt(row));  // All columns verified were empty - set our background RED
	setOpaque(true);
    }
    else
    {
    	setOpaque(false);
    	this.setBackground(null);
    }
    super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    return this;
  }
}
 
