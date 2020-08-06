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

import java.io.File;
import javax.swing.filechooser.FileFilter;


public class MHAFileFilter extends FileFilter {

	private String extension = "";
	private String description = "";

	public MHAFileFilter(String ext,String descr) {
		extension = ext;
		description = descr;
	}

	public boolean accept(File f) {

		if (f.isDirectory()) {
			return true;
		}

		String ext = getExtension(f);

		if (ext != null) {
			if (ext.equals( extension )) {
				return true;
			}
			else {
				return false;
			}
		}

		return false;

	}

	public String getDescription()
	{
		return description+" (*."+extension+")";
//		return "Fiche de perso pour Mountyhall Arena (*.mha)";
	}

	public static String getExtension(File f) {
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');

		if (i > 0 &&  i < s.length() - 1) {
			ext = s.substring(i+1).toLowerCase();
		}
		return ext;
	}


}
