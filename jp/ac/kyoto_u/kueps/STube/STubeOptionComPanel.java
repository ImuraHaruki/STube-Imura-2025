package jp.ac.kyoto_u.kueps.STube;

import java.util.*;

import java.awt.*;
import javax.swing.*;

import gnu.io.*;

/**
 * <p>Title: STubeOptionComPanel</p>
 * <p>Description: Controlling settling tube for analyzing grain-size</p>
 * <p>Copyright: Copyright (C) 2003 NARUSE, Hajime All rights reserved.<br><br>  This program is free software; you can redistribute it and/or modify<br> it under the terms of the GNU General Public License as published by<br> the Free Software Foundation; either version 2 of the License, or<br> (at your option) any later version.<br><br>  This program is distributed in the hope that it will be useful,<br> but WITHOUT ANY WARRANTY; without even the implied warranty of<br> MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the<br> GNU General Public License for more details.<br><br>  You should have received a copy of the GNU General Public License<br> along with this program; if not, write to the Free Software<br> Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA<br></p>
 * <p>Company: Kyoto University</p>
 * @author NARUSE, Hajime
 * @version 1.0
 */

public class STubeOptionComPanel
    extends JPanel {
  HashMap port = new HashMap();
  Integer[] rate = {
      new Integer(300), new Integer(600), new Integer(900), new Integer(1200),
      new Integer(2400), new Integer(4800), new Integer(9600),
      new Integer(19200), new Integer(38400), new Integer(57600)};
  HashMap databits = new HashMap();
  HashMap databits_inv = new HashMap();
  HashMap stopbits = new HashMap();
  HashMap stopbits_inv = new HashMap();
  HashMap parity = new HashMap();
  HashMap parity_inv = new HashMap();
  STubeOption option;

  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JLabel label_comport = new JLabel();
  JLabel label_rate = new JLabel();
  JLabel label_databits = new JLabel();
  JComboBox jcombobox_port = new JComboBox();
  JComboBox jcombobox_rate = new JComboBox();
  JComboBox jcombobox_databits = new JComboBox();
  JLabel jlabel_stopbits = new JLabel();
  JComboBox jcombobox_stopbits = new JComboBox();
  JLabel label_parity = new JLabel();
  JComboBox jcombobox_parity = new JComboBox();

  public STubeOptionComPanel() {
    try {
      Iterator it;
      TreeSet ss;

      /**利用できるポートの一覧を問い合わせる*/
      Enumeration plist = CommPortIdentifier.getPortIdentifiers();
      /**ポートの一覧を処理して，マップに追加する*/
      while (plist.hasMoreElements()) {
        CommPortIdentifier cpi = (CommPortIdentifier) plist.nextElement();
        if (cpi.getPortType() == CommPortIdentifier.PORT_SERIAL) {
          jcombobox_port.addItem(cpi.getName());
        }
      }

      jcombobox_rate = new JComboBox(rate);

      databits.put("5", new Integer(SerialPort.DATABITS_5));
      databits.put("6", new Integer(SerialPort.DATABITS_6));
      databits.put("7", new Integer(SerialPort.DATABITS_7));
      databits.put("8", new Integer(SerialPort.DATABITS_8));
      it = databits.keySet().iterator();
      while (it.hasNext()) {
        Object key = it.next();
        databits_inv.put(databits.get(key), key);
      }
      ss = new TreeSet(databits.keySet());
      jcombobox_databits = new JComboBox(ss.toArray());

      stopbits.put("1", new Integer(SerialPort.STOPBITS_1));
      stopbits.put("2", new Integer(SerialPort.STOPBITS_2));
      it = stopbits.keySet().iterator();
      while (it.hasNext()) {
        Object key = it.next();
        stopbits_inv.put(stopbits.get(key), key);
      }
      ss = new TreeSet(stopbits.keySet());
      jcombobox_stopbits = new JComboBox(ss.toArray());

      parity.put("None", new Integer(SerialPort.PARITY_NONE));
      parity.put("Even", new Integer(SerialPort.PARITY_EVEN));
      parity.put("Odd", new Integer(SerialPort.PARITY_ODD));
      parity.put("Mark", new Integer(SerialPort.PARITY_MARK));
      parity.put("Space", new Integer(SerialPort.PARITY_SPACE));
      it = parity.keySet().iterator();
      while (it.hasNext()) {
        Object key = it.next();
        parity_inv.put(parity.get(key), key);
      }
      ss = new TreeSet(parity.keySet());
      jcombobox_parity = new JComboBox(ss.toArray());

      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  void jbInit() throws Exception {
    label_comport.setText("通信ポート：");
    this.setLayout(gridBagLayout1);
    label_rate.setText("通信速度：");
    label_databits.setText("データビット：");
    jlabel_stopbits.setText("ストップビット：");
    label_parity.setText("パリティ：");
    this.add(label_comport, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
        , GridBagConstraints.EAST, GridBagConstraints.NONE,
        new Insets(10, 10, 5, 0), 0, 0));
    this.add(label_rate, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
                                                , GridBagConstraints.EAST,
                                                GridBagConstraints.NONE,
                                                new Insets(5, 10, 5, 0), 0, 0));
    this.add(jcombobox_port, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.NONE,
        new Insets(10, 0, 5, 10), 0, 0));
    this.add(jcombobox_rate, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.NONE,
        new Insets(5, 0, 5, 10), 0, 0));
    this.add(label_parity, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.EAST,
                                                  GridBagConstraints.NONE,
                                                  new Insets(5, 10, 10, 0), 0,
                                                  0));
    this.add(jcombobox_parity, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.NONE,
        new Insets(5, 0, 10, 10), 0, 0));
    this.add(jlabel_stopbits, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
        , GridBagConstraints.EAST, GridBagConstraints.NONE,
        new Insets(5, 5, 10, 0), 0, 0));
    this.add(jcombobox_stopbits, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.NONE,
        new Insets(5, 0, 10, 10), 0, 0));
    this.add(label_databits, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
        , GridBagConstraints.EAST, GridBagConstraints.NONE,
        new Insets(5, 10, 5, 0), 0, 0));
    this.add(jcombobox_databits, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.NONE,
        new Insets(5, 0, 5, 10), 0, 0));
  }

  void setOption(STubeOption opt) {
    option = opt;
    jcombobox_port.setSelectedItem(option.com_name);
    jcombobox_rate.setSelectedItem(new Integer(option.com_rate));
    jcombobox_databits.setSelectedItem(databits_inv.get(new Integer(option.
        com_databits)));
    jcombobox_stopbits.setSelectedItem(stopbits_inv.get(new Integer(option.
        com_stopbits)));
    jcombobox_parity.setSelectedItem(parity_inv.get(new Integer(option.
        com_parity)));
  }

  STubeOption getOption() {
    option.com_name = (String) jcombobox_port.getSelectedItem();
    option.com_rate = ( (Integer) jcombobox_rate.getSelectedItem()).intValue();
    option.com_databits = ( (Integer) databits.get( (Object) jcombobox_databits.
        getSelectedItem())).intValue();
    option.com_stopbits = ( (Integer) stopbits.get( (Object) jcombobox_stopbits.
        getSelectedItem())).intValue();
    option.com_parity = ( (Integer) parity.get( (Object) jcombobox_parity.
                                               getSelectedItem())).intValue();
    return option;
  }

}
