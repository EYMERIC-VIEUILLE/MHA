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

import java.io.*;

public class SimplePrintStream extends OutputStream {

	private ByteArrayOutputStream outputStream = new ByteArrayOutputStream(256);
	private StringWriter sw = null;

	private SimplePrintStream(StringWriter a) {

		sw = a;
	}
 
	public void write(int b) {

		outputStream.write(b);
	}
 
	public void flush() throws IOException {

		super.flush();

		sw.write( outputStream.toString() );

		outputStream.reset();

	}

	public static PrintStream getSimplePrintStream(StringWriter a) {

		return new PrintStream(new SimplePrintStream(a), true);

	}

}
