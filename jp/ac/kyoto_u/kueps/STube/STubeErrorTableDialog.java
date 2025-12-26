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

public class STubeErrorTableDialog
    extends JDialog {
  STubeErrorTableModel error_tmodel;

  JPanel main_panel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JScrollPane jscrollpane = new JScrollPane();
  JTable jtable_error = new JTable();

  public STubeErrorTableDialog(Frame frame) {
    this(frame, "測定結果（平均値および標準誤差）", false);
  }

  public STubeErrorTableDialog(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try {
      jbInit();
      pack();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public STubeErrorTableDialog() {
    this(null, "", false);
  }

  private void jbInit() throws Exception {
    main_panel.setLayout(borderLayout1);
    jtable_error.setPreferredScrollableViewportSize(new Dimension(640, 200));
    jtable_error.setFont(new java.awt.Font("SansSerif", 0, 12));
    jtable_error.getTableHeader().setFont(new java.awt.Font("SansSerif", 1, 12));
    jtable_error.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    getContentPane().add(main_panel);
    main_panel.add(jscrollpane, BorderLayout.CENTER);
    jscrollpane.getViewport().add(jtable_error, null);
  }

  public void setModel(STubeErrorTableModel tmodel) {
    error_tmodel = tmodel;
    error_tmodel.updateModel();
    jtable_error.setModel(error_tmodel);
    updateTable();
  }

  public void updateTable() {
    error_tmodel.updateModel();
    main_panel.remove(jscrollpane);
    error_tmodel.updateModel();
    jtable_error = new JTable();
    jtable_error.setModel(error_tmodel);
    jtable_error.setFont(new java.awt.Font("SansSerif", 0, 12));
    jtable_error.getTableHeader().setFont(new java.awt.Font("SansSerif", 1, 12));
    jtable_error.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    jtable_error.setCellSelectionEnabled(true);
    jtable_error.setColumnSelectionAllowed(false);
    jscrollpane = new JScrollPane();
    jscrollpane.getViewport().add(jtable_error,null);
    main_panel.add(jscrollpane);
    repaint();

  }
}
