package jp.ac.kyoto_u.kueps.STube;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import jp.ac.kyoto_u.kueps.naruse_utils.*;
import java.io.*;

/**
 * <p>Title: STubeMeasurePanel</p>
 * <p>Description: 測定に関連したオプションを設定するパネル</p>
 * <p>Copyright: Copyright (C) 2003 NARUSE, Hajime All rights reserved.<br><br>  This program is free software; you can redistribute it and/or modify<br> it under the terms of the GNU General Public License as published by<br> the Free Software Foundation; either version 2 of the License, or<br> (at your option) any later version.<br><br>  This program is distributed in the hope that it will be useful,<br> but WITHOUT ANY WARRANTY; without even the implied warranty of<br> MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the<br> GNU General Public License for more details.<br><br>  You should have received a copy of the GNU General Public License<br> along with this program; if not, write to the Free Software<br> Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA<br></p>
 * <p>Company: Kyoto University</p>
 * @author NARUSE, Hajime
 * @version 1.0
 */

public class STubeOptionMeasurePanel
    extends JPanel {
  /**現在設定中のオプション*/
  STubeOption option;

  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JLabel label_countdown = new JLabel();
  JSpinner jspinner_countdown = new JSpinner(new SpinnerNumberModel(10, 3, 99,
      1));
  JLabel label_delay = new JLabel();
  JSpinner jspinner_delay = new JSpinner(new SpinnerNumberModel(10, 0, 9999, 1));
  JLabel label_sound_count = new JLabel();
  JTextField jtextfield_sound_count = new JTextField();
  JButton jbutton_sound_count = new JButton();
  JLabel label_sound_lastcount = new JLabel();
  JButton jbutton_sound_lastcount = new JButton();
  JTextField jtextfield_sound_lastcount = new JTextField();
  JLabel label_sound_start = new JLabel();
  JTextField jtextfield_sound_start = new JTextField();
  JButton jbutton_sound_start = new JButton();

  public STubeOptionMeasurePanel() {
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  void jbInit() throws Exception {
    this.setLayout(gridBagLayout1);
    label_countdown.setText("カウントダウン数：");
    label_delay.setText("計測遅延(m.sec.)：");
    label_sound_count.setText("カウント音：");
    jtextfield_sound_count.setPreferredSize(new Dimension(80, 20));
    jtextfield_sound_count.setText("");
    jbutton_sound_count.setMaximumSize(new Dimension(25, 15));
    jbutton_sound_count.setMinimumSize(new Dimension(25, 15));
    jbutton_sound_count.setText("...");
    jbutton_sound_count.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbutton_sound_count_actionPerformed(e);
      }
    });
    label_sound_lastcount.setText("最終カウント音：");
    jbutton_sound_lastcount.setText("...");
    jbutton_sound_lastcount.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbutton_sound_lastcount_actionPerformed(e);
      }
    });
    jtextfield_sound_lastcount.setText("");
    jtextfield_sound_lastcount.setPreferredSize(new Dimension(80, 20));
    label_sound_start.setText("計測開始音：");
    jtextfield_sound_start.setPreferredSize(new Dimension(80, 20));
    jtextfield_sound_start.setText("");
    jbutton_sound_start.setText("...");
    jbutton_sound_start.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbutton_sound_start_actionPerformed(e);
      }
    });
    this.add(label_countdown,              new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 5, 0), 0, 0));
    this.add(jspinner_countdown,
                          new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 0, 5, 10), 0, 0));
    this.add(label_delay,              new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 10, 5, 0), 0, 0));
    this.add(jspinner_delay,              new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 10), 0, 0));
    this.add(label_sound_count,                 new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 10, 5, 0), 0, 0));
    this.add(jtextfield_sound_count,                new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 0), 0, 0));
    this.add(jbutton_sound_count,               new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 5, 10), 0, 0));
    this.add(label_sound_lastcount,      new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 10, 5, 0), 0, 0));
    this.add(jbutton_sound_lastcount,     new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 5, 10), 0, 0));
    this.add(jtextfield_sound_lastcount,    new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 0), 0, 0));
    this.add(label_sound_start,    new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 10, 10, 0), 0, 0));
    this.add(jtextfield_sound_start,  new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 10, 0), 0, 0));
    this.add(jbutton_sound_start,  new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 10, 10), 0, 0));
  }

  /**
   * オプションの初期状態を設定する
   * @param opt STubeOption
   */
  public void setOption(STubeOption opt) {
    this.option = opt;
    jspinner_countdown.setValue(new Integer(option.cdoun));
    jspinner_delay.setValue(new Integer(option.delay));
    jtextfield_sound_count.setText(option.sound_count_path);
    jtextfield_sound_lastcount.setText(option.sound_lastcount_path);
    jtextfield_sound_start.setText(option.sound_start_path);
  }

  /**
   * オプションを取得する
   *
   * @return STubeOption
   */
  public STubeOption getOption() {
    option.cdoun = ( (Integer) jspinner_countdown.getValue()).intValue();
    option.delay = ( (Integer) jspinner_delay.getValue()).intValue();
    option.sound_count_path = jtextfield_sound_count.getText();
    option.sound_lastcount_path = jtextfield_sound_lastcount.getText();
    option.sound_start_path = jtextfield_sound_start.getText();
    return option;
  }

  void jbutton_sound_count_actionPerformed(ActionEvent e) {
    File result =  MiscUtils.showOpenDialog(this,".wav","Wave files (*.wav)");
    if(result != null){
      jtextfield_sound_count.setText(result.getPath());
    }
  }

  void jbutton_sound_lastcount_actionPerformed(ActionEvent e) {
    File result =  MiscUtils.showOpenDialog(this,".wav","Wave files (*.wav)");
    if(result != null){
      jtextfield_sound_lastcount.setText(result.getPath());
    }
  }

  void jbutton_sound_start_actionPerformed(ActionEvent e) {
    File result =  MiscUtils.showOpenDialog(this,".wav","Wave files (*.wav)");
    if(result != null){
      jtextfield_sound_start.setText(result.getPath());
    }

  }

}
