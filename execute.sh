#!/bin/sh
cd archive
javaw -classpath MHA.jar:../MHA/napkinlaf.jar:../MHA/liquidlnf.jar: -Dhttp.nonProxyHosts=www.yahoo.com -Dswing.systemlaf=javax.swing.plaf.metal.MetalLookAndFeel -Dmha.port=4444 mha.ui.SimpleGUI.MHAGUI --lang=fr
