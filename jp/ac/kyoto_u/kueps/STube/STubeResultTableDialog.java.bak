package jp.ac.kyoto_u.kueps.STube;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

/**
 * <p>Title: S-Tube</p>
 * <p>Description: Controlling settling tube for analyzing grain-size</p>
 * <p>Copyright: Copyright (C) 2003 NARUSE, Hajime All rights reserved.<br><br>  This program is free software; you can redistribute it and/or modify<br> it under the terms of the GNU General Public License as published by<br> the Free Software Foundation; either version 2 of the License, or<br> (at your option) any later version.<br><br>  This program is distributed in the hope that it will be useful,<br> but WITHOUT ANY WARRANTY; without even the implied warranty of<br> MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the<br> GNU General Public License for more details.<br><br>  You should have received a copy of the GNU General Public License<br> along with this program; if not, write to the Free Software<br> Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA<br></p>
 * <p>Company: Kyoto University</p>
 * @author NARUSE, Hajime
 * @version 1.0
 */

public class STubeResultTableDialog
    extends JDialog {
  /**結果を表示するためのテーブルモデル*/
  STubeResultTableModel result_model = new STubeResultTableModel();

  JPanel mainpanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JScrollPane jscrollpane = new JScrollPane();
  JTable jtable_result = new JTable();

  public STubeResultTableDialog(Frame frame) {
    this(frame, "Result", false);
  }

  public STubeResultTableDialog(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try {
      jtable_result.setPreferredScrollableViewportSize(new Dimension(640, 200));
      jbInit();
      pack();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public STubeResultTableDialog() {
    this(null, "", false);
  }

  private void jbInit() throws Exception {
    mainpanel.setLayout(borderLayout1);
    jtable_result.setFont(new java.awt.Font("SansSerif", 0, 12));
    jtable_result.getTableHeader().setFont(new java.awt.Font("SansSerif", 1, 12));
    jtable_result.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    jtable_result.setCellSelectionEnabled(true);
    jtable_result.setColumnSelectionAllowed(false);
    jtable_result.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        jtable_result_mouseClicked(e);
      }
    });
    jtable_result.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        jtable_result_keyPressed(e);
      }
    });
    getContentPane().add(mainpanel);
    mainpanel.add(jscrollpane, BorderLayout.CENTER);
    jscrollpane.getViewport().add(jtable_result, null);
  }

  public void setModel(STubeResultTableModel tmodel) {
    result_model = tmodel;
    result_model.updateModel();
    jtable_result.setModel(result_model);
    updateTable();
  }

  public void updateTable() {
    mainpanel.remove(jscrollpane);
    result_model.updateModel();
    jtable_result = new JTable();
    jtable_result.setModel(result_model);
    jtable_result.setFont(new java.awt.Font("SansSerif", 0, 12));
    jtable_result.getTableHeader().setFont(new java.awt.Font("SansSerif", 1, 12));
    jtable_result.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    jtable_result.setCellSelectionEnabled(true);
    jtable_result.setColumnSelectionAllowed(false);
    jtable_result.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        jtable_result_mouseClicked(e);
      }
    });
    jtable_result.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        jtable_result_keyPressed(e);
      }
    });
    jscrollpane = new JScrollPane();
    jscrollpane.getViewport().add(jtable_result,null);
    mainpanel.add(jscrollpane);
    repaint();
  }

  void jtable_result_keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_DELETE) {
      int rowcount = jtable_result.getSelectedRowCount();
      if (rowcount > 0) {
        String message;
        if (rowcount == 1) {
          message = "この測定結果を削除しますか？";
        }
        else {
          message = "これらの測定結果を削除しますか？";
        }
        if (JOptionPane.showConfirmDialog(this, message, "測定結果の削除",
                                          JOptionPane.YES_NO_OPTION) ==
            JOptionPane.YES_OPTION) {
          ( (STubeFrame) getParent()).papp.removeSamples(jtable_result.
              getSelectedRows());
        }
      }
    }
  }

  /**
   * テーブルがクリックされた場合の処理
   * シングルクリックならばサンプルの表示
   * ダブルクリックならばサンプル名の変更
   * @param e MouseEvent
   */
  void jtable_result_mouseClicked(MouseEvent e) {
    if (e.getClickCount() == 1) {
      ( (STubeFrame) getParent()).showSample(jtable_result.getSelectedRow());
    }
    if (e.getClickCount() == 2) {
      ( (STubeFrame) getParent()).rename_sample(jtable_result.getSelectedRow());
    }
  }
}
