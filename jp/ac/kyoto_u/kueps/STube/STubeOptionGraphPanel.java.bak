package jp.ac.kyoto_u.kueps.STube;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import jp.ac.kyoto_u.kueps.naruse_utils.*;

/**
 * <p>Title: STubeOptionGraphPanel</p>
 * <p>Description: Controlling settling tube for analyzing grain-size</p>
 * <p>Copyright: Copyright (C) 2003 NARUSE, Hajime All rights reserved.<br><br>  This program is free software; you can redistribute it and/or modify<br> it under the terms of the GNU General Public License as published by<br> the Free Software Foundation; either version 2 of the License, or<br> (at your option) any later version.<br><br>  This program is distributed in the hope that it will be useful,<br> but WITHOUT ANY WARRANTY; without even the implied warranty of<br> MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the<br> GNU General Public License for more details.<br><br>  You should have received a copy of the GNU General Public License<br> along with this program; if not, write to the Free Software<br> Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA<br></p>
 * <p>Company: Kyoto University</p>
 * @author NARUSE, Hajime
 * @version 1.0
 */

public class STubeOptionGraphPanel
    extends JPanel {
  Font selected_font;
  Color selected_color;
  SpinnerNumberModel line_width = new SpinnerNumberModel(2.0, 0.1, 9.9, 0.1);
  STubeOption option;

  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JLabel label_linewidth = new JLabel();
  JSpinner jspinner_linewidth = new JSpinner(line_width);
  JLabel label_pt = new JLabel();
  JButton jbutton_color = new JButton();
  JButton jbutton_font = new JButton();

  public STubeOptionGraphPanel() {
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  void jbInit() throws Exception {
    label_linewidth.setText("線幅：");
    this.setLayout(gridBagLayout1);
    label_pt.setText("pt.");
    jbutton_color.setText("線の色...");
    jbutton_color.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbutton_color_actionPerformed(e);
      }
    });
    jbutton_font.setText("フォント...");
    jbutton_font.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbutton_font_actionPerformed(e);
      }
    });
    this.add(label_linewidth, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
        , GridBagConstraints.EAST, GridBagConstraints.NONE,
        new Insets(10, 5, 5, 0), 0, 0));
    this.add(jspinner_linewidth, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.NONE,
        new Insets(10, 0, 5, 0), 0, 0));
    this.add(label_pt, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
                                              , GridBagConstraints.WEST,
                                              GridBagConstraints.NONE,
                                              new Insets(10, 0, 5, 5), 0, 0));
    this.add(jbutton_color, new GridBagConstraints(0, 1, 3, 1, 0.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.NONE,
        new Insets(5, 10, 10, 5), 0, 0));
    this.add(jbutton_font, new GridBagConstraints(3, 1, 3, 1, 0.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.NONE,
                                                  new Insets(5, 5, 10, 10), 0,
                                                  0));
  }

  public void setOption(STubeOption opt) {
    option = opt;
    line_width.setValue(new Float(option.line_width));
    selected_color = option.line_color;
    selected_font = option.font;
  }

  public STubeOption getOption() {
    option.line_width = line_width.getNumber().floatValue();
    option.line_color = selected_color;
    option.font = selected_font;
    return option;
  }

  void jbutton_font_actionPerformed(ActionEvent e) {
      NFontChooser font_chooser = new NFontChooser();
      font_chooser.setTitle("Select screen font");
    font_chooser.setSelectedFont(selected_font);
    if (font_chooser.showDialog() == NFontChooser.APPROVE_OPTION) {
      selected_font = font_chooser.getSelectedFont();
    }
  }

  void jbutton_color_actionPerformed(ActionEvent e) {
    JColorChooser color_chooser = new JColorChooser();
    Color temp_color = color_chooser.showDialog(this, "線の色", selected_color);
    if (temp_color != null) {
      selected_color = temp_color;
    }
  }

}
