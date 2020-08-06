#!/bin/sh
cd ../archive
java -classpath MHA.jar:../MHA/napkinlaf.jar:../MHA/liquidlnf.jar: -Dhttp.nonProxyHosts=www.yahoo.com -Dswing.systemlaf=javax.swing.plaf.metal.MetalLookAndFeel -Dmha.port=4444 mha.ui.SimpleGUI.MHAGUI --lang=toto
