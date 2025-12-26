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

public class STubeOptionOutputPanel
    extends JPanel {
  /**ê›íËÇ∑ÇÈÉIÉvÉVÉáÉì*/
  STubeOption option;

  JCheckBox jcheckbox_condition = new JCheckBox();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JCheckBox jcheckbox_stats = new JCheckBox();
  JCheckBox jcheckbox_dweight = new JCheckBox();
  JCheckBox jcheckbox_rawdata = new JCheckBox();

  public STubeOptionOutputPanel() {
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  void jbInit() throws Exception {
    jcheckbox_condition.setText("Measurement Condition");
    this.setLayout(gridBagLayout1);
    jcheckbox_stats.setText("Statistic Values");
    jcheckbox_dweight.setText("Class");
    jcheckbox_rawdata.setText("Raw Data");
    this.add(jcheckbox_condition, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.NONE,
        new Insets(10, 10, 5, 10), 0, 0));
    this.add(jcheckbox_stats, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.NONE,
        new Insets(5, 10, 5, 10), 0, 0));
    this.add(jcheckbox_dweight, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.NONE,
        new Insets(5, 10, 5, 10), 0, 0));
    this.add(jcheckbox_rawdata, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.NONE,
        new Insets(5, 10, 10, 10), 0, 0));
  }

  public void setOption(STubeOption opt) {
    option = opt;
    jcheckbox_condition.setSelected(option.output_condition);
    jcheckbox_dweight.setSelected(option.output_class);
    jcheckbox_rawdata.setSelected(option.output_rawdata);
    jcheckbox_stats.setSelected(option.output_stats);
  }

  public STubeOption getOption() {
    option.output_condition = jcheckbox_condition.isSelected();
    option.output_class = jcheckbox_dweight.isSelected();
    option.output_rawdata = jcheckbox_rawdata.isSelected();
    option.output_stats = jcheckbox_stats.isSelected();
    return option;
  }
}
