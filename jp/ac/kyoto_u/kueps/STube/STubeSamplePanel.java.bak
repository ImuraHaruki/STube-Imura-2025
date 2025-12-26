package jp.ac.kyoto_u.kueps.STube;

import java.awt.*;
import javax.swing.*;

/**
 * <p>Title: STubeSamplePanel</p>
 * <p>Description: サンプルの状態を設定するためのパネル</p>
 * <p>Copyright: Copyright (C) 2003 NARUSE, Hajime All rights reserved.<br><br>  This program is free software; you can redistribute it and/or modify<br> it under the terms of the GNU General Public License as published by<br> the Free Software Foundation; either version 2 of the License, or<br> (at your option) any later version.<br><br>  This program is distributed in the hope that it will be useful,<br> but WITHOUT ANY WARRANTY; without even the implied warranty of<br> MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the<br> GNU General Public License for more details.<br><br>  You should have received a copy of the GNU General Public License<br> along with this program; if not, write to the Free Software<br> Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA<br></p>
 * <p>Company: Kyoto University</p>
 * @author NARUSE, Hajime
 * @version 1.0
 */

public class STubeSamplePanel
    extends JPanel {

  STubeSample sample;

  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JLabel label_sample = new JLabel();
  JTextField jtextfield_samplename = new JTextField();

  /**
   * コンストラクタ
   */
  public STubeSamplePanel() {
    super();
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    setLayout(gridBagLayout1);
    label_sample.setText("サンプル名：");
    jtextfield_samplename.setMinimumSize(new Dimension(80, 20));
    jtextfield_samplename.setPreferredSize(new Dimension(80, 20));
    jtextfield_samplename.setText("sample");
    add(label_sample, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                                             , GridBagConstraints.EAST,
                                             GridBagConstraints.NONE,
                                             new Insets(10, 10, 5, 0), 0, 0));
    add(jtextfield_samplename,
        new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
                               , GridBagConstraints.WEST,
                               GridBagConstraints.NONE,
                               new Insets(10, 0, 5, 10), 0, 0));
  }

  /**
   * サンプルの初期条件の入力
   * @param sample STubeSample
   */
  public void setSample(STubeSample sample) {
    this.sample = sample;
    jtextfield_samplename.setMinimumSize(new Dimension(80, 20));
    jtextfield_samplename.setPreferredSize(new Dimension(80, 20));
    jtextfield_samplename.setText(sample.name);
  }

  /**
   * 設定されたサンプルの取得
   * @return STubeSample
   */
  public STubeSample getSample() {
    sample.name = jtextfield_samplename.getText();
    return sample;
  }

}
