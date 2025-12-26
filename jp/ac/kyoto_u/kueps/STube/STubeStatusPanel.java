package jp.ac.kyoto_u.kueps.STube;

import java.text.*;
import java.util.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * <p>Title: STubeStatusPanel</p>
 * <p>Description: Controlling settling tube for analyzing grain-size</p>
 * <p>Copyright: Copyright (C) 2003 NARUSE, Hajime All rights reserved.<br><br>  This program is free software; you can redistribute it and/or modify<br> it under the terms of the GNU General Public License as published by<br> the Free Software Foundation; either version 2 of the License, or<br> (at your option) any later version.<br><br>  This program is distributed in the hope that it will be useful,<br> but WITHOUT ANY WARRANTY; without even the implied warranty of<br> MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the<br> GNU General Public License for more details.<br><br>  You should have received a copy of the GNU General Public License<br> along with this program; if not, write to the Free Software<br> Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA<br></p>
 * <p>Company: Kyoto University</p>
 * @author NARUSE, Hajime
 * @version 1.0
 */

public class STubeStatusPanel
    extends JPanel {
  /**カウントダウンを行うための変数*/
  int total_count = 0;
  int count = 0;

  /**サンプル名*/
  String samplename;

  JLabel label_status = new JLabel();
  TitledBorder titledBorder1;
  JProgressBar jprogressbar = new JProgressBar();
  JLabel label_com = new JLabel();
  JLabel label_gram = new JLabel();
  JLabel label_second = new JLabel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();

  public STubeStatusPanel() {
    try {
      jbInit();
      startCount();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  void jbInit() throws Exception {
    titledBorder1 = new TitledBorder("");
    label_status.setFont(new java.awt.Font("Dialog", 0, 12));
    label_status.setBorder(BorderFactory.createLoweredBevelBorder());
    label_status.setDoubleBuffered(false);
    label_status.setMaximumSize(new Dimension(120, 19));
    label_status.setMinimumSize(new Dimension(120, 19));
    label_status.setPreferredSize(new Dimension(120, 19));
    label_status.setRequestFocusEnabled(true);
    label_status.setHorizontalAlignment(SwingConstants.LEADING);
    label_status.setText("データがありません");
    this.setLayout(gridBagLayout1);
    label_com.setFont(new java.awt.Font("Dialog", 0, 12));
    label_com.setBorder(BorderFactory.createLoweredBevelBorder());
    label_com.setMaximumSize(new Dimension(45, 19));
    label_com.setMinimumSize(new Dimension(45, 19));
    label_com.setPreferredSize(new Dimension(45, 19));
    label_com.setHorizontalAlignment(SwingConstants.CENTER);
    label_com.setText("");
    label_gram.setEnabled(true);
    label_gram.setFont(new java.awt.Font("Dialog", 0, 12));
    label_gram.setBorder(BorderFactory.createLoweredBevelBorder());
    label_gram.setMaximumSize(new Dimension(60, 19));
    label_gram.setMinimumSize(new Dimension(60, 19));
    label_gram.setPreferredSize(new Dimension(60, 19));
    label_gram.setHorizontalAlignment(SwingConstants.CENTER);
    label_gram.setText("0.000g");
    label_second.setFont(new java.awt.Font("Dialog", 1, 12));
    label_second.setForeground(Color.blue);
    label_second.setBorder(BorderFactory.createLoweredBevelBorder());
    label_second.setMaximumSize(new Dimension(90, 19));
    label_second.setMinimumSize(new Dimension(90, 19));
    label_second.setPreferredSize(new Dimension(90, 19));
    label_second.setHorizontalAlignment(SwingConstants.CENTER);
    label_second.setText("計測終了");
    this.setFont(new java.awt.Font("SansSerif", 0, 11));
    jprogressbar.setMinimumSize(new Dimension(100, 18));
    jprogressbar.setPreferredSize(new Dimension(150, 18));
    this.add(label_status, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.WEST,
                                                  GridBagConstraints.NONE,
                                                  new Insets(2, 2, 2, 2), 0, 0));
    this.add(label_com, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.WEST,
                                               GridBagConstraints.NONE,
                                               new Insets(2, 2, 2, 2), 0, 0));
    this.add(label_gram, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
                                                , GridBagConstraints.WEST,
                                                GridBagConstraints.NONE,
                                                new Insets(2, 2, 2, 2), 0, 0));
    this.add(label_second, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
                                                  , GridBagConstraints.WEST,
                                                  GridBagConstraints.NONE,
                                                  new Insets(2, 2, 2, 2), 0, 0));
    this.add(jprogressbar, new GridBagConstraints(4, 0, 1, 1, 1.0, 0.0
                                                  , GridBagConstraints.WEST,
                                                  GridBagConstraints.NONE,
                                                  new Insets(2, 2, 2, 2), 0, 0));
  }

  /**
   * カウントダウンを開始する
   *
   * @param second int
   */
  public void startCount() {
    label_second.setText("計測中　残り" + Integer.toString(count) + "秒");
    this.repaint();
    java.util.Timer timer = new java.util.Timer();

    TimerTask task = new TimerTask() {
      /**
       * run
       */
      public void run() {
        count--;
        if (count > 0) {
          label_second.setForeground(Color.red);
          label_second.setText("残り" + Integer.toString(count) + "秒");
          label_status.setForeground(Color.red);
          label_status.setText("計測中：" + samplename);
          jprogressbar.setMaximum(total_count);
          jprogressbar.setValue(total_count - count);
        }
        else {
          label_second.setForeground(Color.blue);
          label_second.setText("計測終了");
          label_status.setForeground(Color.black);
          label_status.setText(samplename);
          jprogressbar.setValue(0);
        }
        repaint();
      }
    };
    timer.schedule(task, 0, 1000);
  }

  /**
   * カウント数を設定する
   * @param count int
   */
  public void setCount(int count) {
    this.total_count = count;
    this.count = count;
  }

  /**
   * サンプル名をセットする
   *
   * @param name String
   */
  public void setSampleName(String name) {
    samplename = name;
    label_status.setText(samplename);
  }

  /**
   * ポート名をセットする
   * @param pname String
   */
  public void setPortName(String pname) {
    label_com.setText(pname);
  }

  /**
   * サンプル重量をセットする
   *
   * @param gram double
   */
  public void setWeight(double gram) {
    NumberFormat nf = NumberFormat.getInstance();
    nf.setMaximumFractionDigits(3);
    label_gram.setText(nf.format(gram) + "g");
  }

  /**
   * 情報をクリアする
   */
  public void clearStatus() {
    count = 0;
    total_count = 0;
    label_com.setText("");
    label_gram.setText("");
    label_status.setText("データがありません");
    samplename = "";
  }
}
