package jp.ac.kyoto_u.kueps.STube;

import java.awt.*;
import javax.swing.*;

/**
 * <p>Title: S-Tube</p>
 * <p>Description: Controlling settling tube for analyzing grain-size</p>
 * <p>Copyright: Copyright (C) 2003 NARUSE, Hajime All rights reserved.<br><br>  This program is free software; you can redistribute it and/or modify<br> it under the terms of the GNU General Public License as published by<br> the Free Software Foundation; either version 2 of the License, or<br> (at your option) any later version.<br><br>  This program is distributed in the hope that it will be useful,<br> but WITHOUT ANY WARRANTY; without even the implied warranty of<br> MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the<br> GNU General Public License for more details.<br><br>  You should have received a copy of the GNU General Public License<br> along with this program; if not, write to the Free Software<br> Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA<br></p>
 * <p>Company: Kyoto University</p>
 * @author NARUSE, Hajime
 * @version 1.0
 */

public class STubeInfoPanel
    extends JPanel {
  /**サンプル情報を表示する領域*/
  STubeInfoRegion info = new STubeInfoRegion();

  /**オプション*/
  STubeOption option = new STubeOption();

  BorderLayout borderLayout1 = new BorderLayout();

  public STubeInfoPanel() {
    try {
      jbInit();
      this.add(info, BorderLayout.CENTER);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  void jbInit() throws Exception {
    this.setBackground(Color.white);
    this.setLayout(borderLayout1);
  }

  /**
   * サンプル情報を表示する
   * @param sample STubeSample
   */
  public void setSample(STubeSample sample) {
    info.setSample(sample);
    repaint();
  }

  /**
   * 情報をクリアーする
   */
  public void clear() {
    info.clear();
    repaint();
  }

  /**
   * オプションを設定する
   * @param opt STubeOption
   */
  public void setOption(STubeOption opt) {
    option = opt;
    //パネルのフォントの設定
    Component[] components = info.getComponents();
    for (int i = 0; i < components.length; i++) {
      components[i].setFont(option.font);
    }

    //再描画
    repaint();

  }

}
