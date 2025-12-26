package jp.ac.kyoto_u.kueps.STube;

import java.io.*;
import java.util.*;
import javax.print.attribute.standard.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import javax.swing.*;

import jp.ac.kyoto_u.kueps.naruse_utils.*;

/**
 * <p>Title: STubeFrame</p>
 * <p>Description: Controlling settling tube for analyzing grain-size</p>
 * <p>Copyright: Copyright (C) 2003 NARUSE, Hajime All rights reserved.<br><br>  This program is free software; you can redistribute it and/or modify<br> it under the terms of the GNU General Public License as published by<br> the Free Software Foundation; either version 2 of the License, or<br> (at your option) any later version.<br><br>  This program is distributed in the hope that it will be useful,<br> but WITHOUT ANY WARRANTY; without even the implied warranty of<br> MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the<br> GNU General Public License for more details.<br><br>  You should have received a copy of the GNU General Public License<br> along with this program; if not, write to the Free Software<br> Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA<br></p>
 * <p>Company: Kyoto University</p>
 * @author NARUSE, Hajime
 * @version 1.0
 */

public class STubeFrame
    extends JFrame {
  /**このフレームを所有するアプリケーション*/
  STube papp;

  /**グラフ表示するためのパネル*/
  STubeGraph graph = new STubeGraph();

  /**情報を表示するためのパネル*/
  STubeInfoPanel info_panel = new STubeInfoPanel();

  /**このフレームのアイコン*/
  ImageIcon stube_image;

  /**結果を表示するためのテーブルダイアログ*/
  STubeResultTableDialog result_table = new STubeResultTableDialog(this);

  /**平均と誤差を表示するためのテーブルダイアログ*/
  STubeErrorTableDialog error_table = new STubeErrorTableDialog(this);

  /**現在表示しているサンプルのインデックス番号*/
  int current_sample_index = 0;

  /**ステータス表示用の下部のパネル*/
  STubeStatusPanel status_panel = new STubeStatusPanel();

  JPanel contentPane;
  JMenuBar jmenubar = new JMenuBar();
  JMenu jmenu_file = new JMenu();
  JMenuItem jmenu_file_exit = new JMenuItem();
  JMenu jmenu_help = new JMenu();
  JMenuItem jmenu_help_about = new JMenuItem();
  JToolBar jtoolbar = new JToolBar();
  ImageIcon go;
  ImageIcon help;
  ImageIcon openfile;
  ImageIcon save;
  ImageIcon stop;
  ImageIcon stube;
  ImageIcon tube;
  ImageIcon option;
  ImageIcon com_icon;
  BorderLayout borderLayout1 = new BorderLayout();
  JButton jbutton_start = new JButton();
  JPanel center_panel = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  JButton jbutton_condition = new JButton();
  JMenuItem jmenu_file_savesample = new JMenuItem();
  JMenuItem jmenu_file_savetube = new JMenuItem();
  JMenuItem jmenu_file_saveoption = new JMenuItem();
  JMenuItem jmenu_file_loadsample = new JMenuItem();
  JMenuItem jmenu_file_loadtube = new JMenuItem();
  JMenuItem jmenu_file_loadoption = new JMenuItem();
  JButton jbutton_option = new JButton();
  JMenu jmenu_graph = new JMenu();
  JMenuItem jmenu_file_exportresult = new JMenuItem();
  JMenu jmenu_setting = new JMenu();
  JMenuItem jmenu_setting_tubecondition = new JMenuItem();
  JMenuItem jmenu_setting_option = new JMenuItem();
  JCheckBoxMenuItem jcmenu_graph_histogram = new JCheckBoxMenuItem();
  JButton jbutton_stop = new JButton();
  JMenuItem jmenu_file_exportgraph = new JMenuItem();
  JButton jbutton_open = new JButton();
  JButton jbutton_save = new JButton();
  JButton jbutton_help = new JButton();
  JMenu jmenu_result = new JMenu();
  JMenuItem jmenu_result_showresult = new JMenuItem();
  JMenu jmenu_result_viewsample = new JMenu();
  JMenuItem jmenu_result_deletesample = new JMenuItem();
  JMenuItem jmenu_result_rename = new JMenuItem();
  JMenuItem jmenu_result_showerror = new JMenuItem();
  JMenuItem jmenu_file_measure = new JMenuItem();
  JMenuItem jmenu_file_printgraph = new JMenuItem();
  JMenuItem jmenu_file_printall = new JMenuItem();
  JMenu jmenu_graph_xaxis = new JMenu();
  JCheckBoxMenuItem jcmenu_graph_xaxis_phi = new JCheckBoxMenuItem();
  JCheckBoxMenuItem jcmenu_graph_xaxis_mm = new JCheckBoxMenuItem();
  JCheckBoxMenuItem jcmenu_graph_xaxis_sec = new JCheckBoxMenuItem();
  JMenu jmenu_graph_yaxis = new JMenu();
  JCheckBoxMenuItem jcmenu_graph_yaxis_percent = new JCheckBoxMenuItem();
  JCheckBoxMenuItem jcmenu_graph_yaxis_g = new JCheckBoxMenuItem();
  JButton jbutton_com = new JButton();
  JMenuItem jmenu_file_pagesetup = new JMenuItem();
  JMenuItem jmenu_file_exportallgraphs = new JMenuItem();
  JMenu jmenu_file_export = new JMenu();
  JMenuItem jmenu_file_export_meanerror = new JMenuItem();
  JMenuItem jmenu_file_export_condition = new JMenuItem();

  //Construct the frame
  public STubeFrame() {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit();
      ButtonGroup jbg_xaxis = new ButtonGroup();
      ButtonGroup jbg_yaxis = new ButtonGroup();
      jbg_xaxis.add(jcmenu_graph_xaxis_phi);
      jbg_xaxis.add(jcmenu_graph_xaxis_mm);
      jbg_xaxis.add(jcmenu_graph_xaxis_sec);
      jbg_yaxis.add(jcmenu_graph_yaxis_percent);
      jbg_yaxis.add(jcmenu_graph_yaxis_g);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  //Component initialization
  private void jbInit() throws Exception {
    openfile = new ImageIcon(jp.ac.kyoto_u.kueps.STube.STubeFrame.class.
                             getResource("openFile.png"));
    go = new ImageIcon(jp.ac.kyoto_u.kueps.STube.STubeFrame.class.
                       getResource("Go.png"));
    help = new ImageIcon(jp.ac.kyoto_u.kueps.STube.STubeFrame.class.
                         getResource("help.png"));
    stube_image = new ImageIcon(jp.ac.kyoto_u.kueps.STube.STubeFrame.class.
                                getResource("STube.png"));
    save = new ImageIcon(jp.ac.kyoto_u.kueps.STube.STubeFrame.class.
                         getResource("save.png"));
    stop = new ImageIcon(jp.ac.kyoto_u.kueps.STube.STubeFrame.class.
                         getResource("Stop.png"));
    option = new ImageIcon(jp.ac.kyoto_u.kueps.STube.STubeFrame.class.
                           getResource("Option.png"));
    tube = new ImageIcon(jp.ac.kyoto_u.kueps.STube.STubeFrame.class.
                         getResource("tube.png"));
    com_icon = new ImageIcon(jp.ac.kyoto_u.kueps.STube.STubeFrame.class.
                             getResource("Com.png"));
    jbutton_start.setIcon(go);
    jbutton_stop.setIcon(stop);
    jbutton_condition.setIcon(tube);
    jbutton_option.setToolTipText("オプション設定");
    jbutton_option.setIcon(option);
    jbutton_option.setText("");

    contentPane = (JPanel)this.getContentPane();
    contentPane.setLayout(borderLayout1);
    this.setSize(new Dimension(800, 600));
    this.setTitle("STube");
    this.setIconImage(stube_image.getImage());
    jmenu_file.setContentAreaFilled(true);
    jmenu_file.setText("File");
    jmenu_file_exit.setText("Exit");
    jmenu_file_exit.addActionListener(new
                                      STubeFrame_jmenu_file_exit_ActionAdapter(this));
    jmenu_help.setText("Help");
    jmenu_help_about.setText("About");
    jmenu_help_about.addActionListener(new
                                       STubeFrame_jmenu_help_about_ActionAdapter(this));
    jbutton_start.setToolTipText("測定開始");
    jbutton_start.setText("");
    jbutton_start.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbutton_start_actionPerformed(e);
      }
    });
    jtoolbar.setBackground(SystemColor.control);
    center_panel.setLayout(borderLayout2);
    jbutton_condition.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbutton_condition_actionPerformed(e);
      }
    });
    center_panel.setBackground(Color.white);
    jmenu_file_savesample.setText("Save Samples");
    jmenu_file_savesample.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmenu_file_savesample_actionPerformed(e);
      }
    });
    jmenu_file_savetube.setActionCommand("Save Tube Condition");
    jmenu_file_savetube.setText("Save Tube Condition");
    jmenu_file_savetube.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmenu_file_savetube_actionPerformed(e);
      }
    });
    jmenu_file_saveoption.setText("Save Option");
    jmenu_file_saveoption.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmenu_file_saveoption_actionPerformed(e);
      }
    });
    jmenu_file_loadsample.setText("Open Samples");
    jmenu_file_loadsample.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmenu_file_loadsample_actionPerformed(e);
      }
    });
    jmenu_file_loadtube.setText("Load Tube Condition");
    jmenu_file_loadtube.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmenu_file_loadtube_actionPerformed(e);
      }
    });
    jmenu_file_loadoption.setText("Load Option");
    jmenu_file_loadoption.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmenu_file_loadoption_actionPerformed(e);
      }
    });
    jbutton_option.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbutton_option_actionPerformed(e);
      }
    });
    jmenu_graph.setText("Graph");
    jmenu_file_exportresult.setText("Result");
    jmenu_file_exportresult.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmenu_file_exportresult_actionPerformed(e);
      }
    });
    jmenu_setting.setText("Setting");
    jmenu_setting_tubecondition.setText("Tube Condition");
    jmenu_setting_tubecondition.addActionListener(new java.awt.event.
                                                  ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmenu_setting_tubecondition_actionPerformed(e);
      }
    });
    jmenu_setting_option.setText("Option");
    jmenu_setting_option.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmenu_setting_option_actionPerformed(e);
      }
    });
    jcmenu_graph_histogram.setText("Histogram");
    jcmenu_graph_histogram.setSelected(true);
    jcmenu_graph_histogram.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jcmenu_graph_histogram_actionPerformed(e);
      }
    });

    jbutton_stop.setEnabled(false);
    jbutton_stop.setToolTipText("測定中止");
    jbutton_stop.setIcon(stop);
    jbutton_stop.setText("");
    jbutton_stop.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbutton_stop_actionPerformed(e);
      }
    });
    jmenu_file_exportgraph.setText("Graph");
    jmenu_file_exportgraph.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmenu_file_exportgraph_actionPerformed(e);
      }
    });
    jbutton_condition.setToolTipText("沈降管状態設定");
    jbutton_condition.setText("");
    jbutton_open.setToolTipText("測定結果を開く");
    jbutton_open.setIcon(openfile);
    jbutton_open.setText("");
    jbutton_open.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbutton_open_actionPerformed(e);
      }
    });
    jbutton_save.setToolTipText("測定結果を保存する");
    jbutton_save.setIcon(save);
    jbutton_save.setText("");
    jbutton_save.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbutton_save_actionPerformed(e);
      }
    });
    jbutton_help.setToolTipText("ヘルプ");
    jbutton_help.setIcon(help);
    jbutton_help.setText("");
    jbutton_help.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbutton_help_actionPerformed(e);
      }
    });
    jmenu_result.setText("Result");
    jmenu_result_showresult.setText("Show result");
    jmenu_result_showresult.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmenu_result_showresult_actionPerformed(e);
      }
    });
    jmenu_result_viewsample.setText("View sample graph");
    jmenu_result_deletesample.setText("Delete this result");
    jmenu_result_deletesample.addActionListener(new java.awt.event.
                                                ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmenu_result_deletesample_actionPerformed(e);
      }
    });
    jmenu_result_rename.setText("Rename this sample");
    jmenu_result_rename.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmenu_result_rename_actionPerformed(e);
      }
    });
    jmenu_result_showerror.setText("Show mean & error");
    jmenu_result_showerror.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmenu_result_showerror_actionPerformed(e);
      }
    });
    jmenu_file_measure.setText("Start measurement");
    jmenu_file_measure.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmenu_file_measure_actionPerformed(e);
      }
    });
    jmenu_file_printgraph.setText("Print this graph");
    jmenu_file_printgraph.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmenu_file_printgraph_actionPerformed(e);
      }
    });
    jmenu_file_printall.setText("Print all graphs");
    jmenu_file_printall.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmenu_file_printall_actionPerformed(e);
      }
    });
    jmenu_graph_xaxis.setText("X-Axis");
    jcmenu_graph_xaxis_phi.setText("Phi");
    jcmenu_graph_xaxis_phi.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jcmenu_graph_xaxis_phi_actionPerformed(e);
      }
    });
    jcmenu_graph_xaxis_mm.setText("mm");
    jcmenu_graph_xaxis_mm.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jcmenu_graph_xaxis_mm_actionPerformed(e);
      }
    });
    jcmenu_graph_xaxis_sec.setText("Sec.");
    jcmenu_graph_xaxis_sec.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jcmenu_graph_xaxis_sec_actionPerformed(e);
      }
    });
    jmenu_graph_yaxis.setText("Y-Axis");
    jcmenu_graph_yaxis_percent.setText("%");
    jcmenu_graph_yaxis_percent.addActionListener(new java.awt.event.
                                                 ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jcmenu_graph_yaxis_percent_actionPerformed(e);
      }
    });
    jcmenu_graph_yaxis_g.setText("g");
    jcmenu_graph_yaxis_g.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jcmenu_graph_yaxis_g_actionPerformed(e);
      }
    });
    jbutton_com.setToolTipText("通信条件設定");
    jbutton_com.setText("");
    jbutton_com.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jbutton_com_actionPerformed(e);
      }
    });
    jbutton_com.setIcon(com_icon);
    jmenu_file_pagesetup.setText("Page Setup...");
    jmenu_file_pagesetup.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmenu_file_pagesetup_actionPerformed(e);
      }
    });
    jmenu_file_exportallgraphs.setText("All Graphs");
    jmenu_file_exportallgraphs.addActionListener(new java.awt.event.
                                                 ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmenu_file_exportallgraphs_actionPerformed(e);
      }
    });
    jmenu_file_export.setText("Export");
    jmenu_file_export_meanerror.setText("Mean & Error");
    jmenu_file_export_meanerror.addActionListener(new java.awt.event.
                                                  ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmenu_file_export_meanerror_actionPerformed(e);
      }
    });
    jmenu_file_export_condition.setText("Condition");
    jmenu_file_export_condition.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jmenu_file_export_condition_actionPerformed(e);
      }
    });
    jmenu_file.add(jmenu_file_measure);
    jmenu_file.addSeparator();
    jmenu_file.add(jmenu_file_loadsample);
    jmenu_file.add(jmenu_file_loadtube);
    jmenu_file.add(jmenu_file_loadoption);
    jmenu_file.addSeparator();
    jmenu_file.add(jmenu_file_savesample);
    jmenu_file.add(jmenu_file_savetube);
    jmenu_file.add(jmenu_file_saveoption);
    jmenu_file.addSeparator();
    jmenu_file_export.add(jmenu_file_exportresult);
    jmenu_file_export.add(jmenu_file_export_meanerror);
    jmenu_file_export.add(jmenu_file_exportgraph);
    jmenu_file_export.add(jmenu_file_exportallgraphs);
    jmenu_file_export.add(jmenu_file_export_condition);
    jmenu_file.add(jmenu_file_export);
    jmenu_file.addSeparator();
    jmenu_file.add(jmenu_file_pagesetup);
    jmenu_file.add(jmenu_file_printgraph);
    jmenu_file.add(jmenu_file_printall);
    jmenu_file.addSeparator();
    jmenu_file.add(jmenu_file_exit);

    jmenu_help.add(jmenu_help_about);
    jmenubar.add(jmenu_file);
    jmenubar.add(jmenu_result);
    jmenubar.add(jmenu_graph);
    jmenubar.add(jmenu_setting);
    jmenubar.add(jmenu_help);
    this.setJMenuBar(jmenubar);
    contentPane.add(jtoolbar, BorderLayout.NORTH);
    contentPane.add(center_panel, BorderLayout.CENTER);
    center_panel.add(graph, BorderLayout.CENTER);
    center_panel.add(info_panel, BorderLayout.SOUTH);
    contentPane.add(status_panel, BorderLayout.SOUTH);
    jmenu_graph.add(jcmenu_graph_histogram);
    jmenu_graph.add(jmenu_graph_xaxis);
    jmenu_graph.add(jmenu_graph_yaxis);
    jmenu_graph.addSeparator();
    jmenu_setting.add(jmenu_setting_tubecondition);
    jmenu_setting.add(jmenu_setting_option);
    jtoolbar.add(jbutton_open, null);
    jtoolbar.add(jbutton_save, null);
    jtoolbar.addSeparator();
    jtoolbar.add(jbutton_start, null);
    jtoolbar.add(jbutton_stop, null);
    jtoolbar.addSeparator();
    jtoolbar.add(jbutton_condition, null);
    jtoolbar.add(jbutton_com, null);
    jtoolbar.add(jbutton_option, null);
    jtoolbar.addSeparator();
    jtoolbar.add(jbutton_help, null);
    jmenu_result.add(jmenu_result_showresult);
    jmenu_result.add(jmenu_result_showerror);
    jmenu_result.addSeparator();
    jmenu_result.add(jmenu_result_rename);
    jmenu_result.add(jmenu_result_deletesample);
    jmenu_graph_xaxis.add(jcmenu_graph_xaxis_phi);
    jmenu_graph_xaxis.add(jcmenu_graph_xaxis_mm);
    jmenu_graph_xaxis.add(jcmenu_graph_xaxis_sec);
    jmenu_graph_yaxis.add(jcmenu_graph_yaxis_percent);
    jmenu_graph_yaxis.add(jcmenu_graph_yaxis_g);
    jmenu_graph.add(jmenu_result_viewsample);
  }

  //File | Exit action performed
  public void jmenu_file_exit_actionPerformed(ActionEvent e) {
    if (papp.modified) {
      if (JOptionPane.showConfirmDialog(this, "測定結果が保存されていません．保存しますか？", "確認",
                                        JOptionPane.YES_NO_OPTION,
                                        JOptionPane.QUESTION_MESSAGE) ==
          JOptionPane.YES_OPTION) {
        saveSample();
      }
    }

    papp.saveOptionOnExit();
    System.exit(0);
  }

  //Help | About action performed
  public void jmenu_help_about_actionPerformed(ActionEvent e) {
    STubeFrame_AboutBox dlg = new STubeFrame_AboutBox(this);
    Dimension dlgSize = dlg.getPreferredSize();
    Dimension frmSize = getSize();
    Point loc = getLocation();
    dlg.setLocation( (frmSize.width - dlgSize.width) / 2 + loc.x,
                    (frmSize.height - dlgSize.height) / 2 + loc.y);
    dlg.setModal(true);
    dlg.pack();
    dlg.show();
  }

  //Overridden so we can exit when window is closed
  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      jmenu_file_exit_actionPerformed(null);
    }
  }

  /**
   * 計測を行うStartボタンが押されたときの処理
   * @param e ActionEvent
   */
  void jbutton_start_actionPerformed(ActionEvent e) {
    startMeasure();
  }

  /**
   * 測定を実行する
   */
  void startMeasure() {
    try {
      //新しいサンプルオブジェクトを生成
      STubeSample sample = new STubeSample();
      if (!papp.samples.isEmpty()) {//デフォルトでは前のサンプルと同じ名前をつける
        sample.name = ( (STubeSample) papp.samples.get(papp.samples.size() - 1)).
            name;
      }

      //サンプルの名前を設定させる
      STubeSamplePanel spanel = new STubeSamplePanel();
      spanel.setSample(sample);
      if (JOptionPane.showOptionDialog(this, spanel, "サンプル設定",
                                       JOptionPane.OK_CANCEL_OPTION,
                                       JOptionPane.PLAIN_MESSAGE, null, null, null)
          == JOptionPane.YES_OPTION) {
        sample = spanel.getSample();

        //測定用のオブジェクトを生成する
        papp.measure = new STubeMeasure(this, papp.option.com_name,
                                        papp.getCondition(), sample);
        //測定開始
        papp.measure.startMeasure();
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**測定を中止する*/
  void stopMeasure() {
    papp.measure.cancel();
  }

  /**
   * 沈降管のConditionを設定する
   * @param e ActionEvent
   */
  void jbutton_condition_actionPerformed(ActionEvent e) {
    showConditionDialog();
  }

  /**
   * 沈降管状態設定ダイアログを表示する
   */
  void showConditionDialog() {
    STubeConditionPanel cpanel = new STubeConditionPanel();
    cpanel.setCondition(papp.condition);
    if (JOptionPane.showOptionDialog(this, cpanel, "沈降管状態設定",
                                     JOptionPane.OK_CANCEL_OPTION,
                                     JOptionPane.PLAIN_MESSAGE, null, null, null)
        == JOptionPane.OK_OPTION) {
      //沈降管の状態が変更された場合，これまでの状態を保存するか否かを尋ねる
      if (JOptionPane.showConfirmDialog(this,
                                        "沈降管の測定条件が変更されました．これまでの測定条件を保存しますか？",
                                        "測定条件変更", JOptionPane.YES_NO_OPTION,
                                        JOptionPane.QUESTION_MESSAGE) ==
          JOptionPane.YES_OPTION) {
        saveTube();
      }
      //沈降管の状態が変更された場合，これまでの測定データを保存するか否かを尋ねる
      if (JOptionPane.showConfirmDialog(this,
                                        "これまでの測定結果はすべて消去されます．保存しますか？",
                                        "測定条件変更", JOptionPane.YES_NO_OPTION,
                                        JOptionPane.QUESTION_MESSAGE) ==
          JOptionPane.YES_OPTION) {
        saveSample();//サンプルを出力
      }
      papp.samples.clear();//サンプルをすべて削除
      updateFrame();
      papp.setModified(true);
      papp.setSamples(papp.samples);
      //状態を変更
      papp.setCondition(cpanel.getCondition());
    }
  }

  /**
   * 親アプリケーションをセットする
   * @param app STube
   */
  protected void setParentApp(STube app) {
    papp = app;
  }

  /**
   * フレームを初期化する
   */
  void updateFrame() {
    graph.setOption(papp.option);
    showSample();
    updateSampleList();
    status_panel.setPortName(papp.option.com_name);
    jcmenu_graph_histogram.setSelected(papp.option.view_histogram);
    if (papp.option.view_xaxis == papp.option.VIEW_PHI) {
      jcmenu_graph_xaxis_phi.setSelected(true);
    }
    else if (papp.option.view_xaxis == papp.option.VIEW_MM) {
      jcmenu_graph_xaxis_mm.setSelected(true);
    }
    else {
      jcmenu_graph_xaxis_sec.setSelected(true);
    }
    if (papp.option.view_yaxis == papp.option.VIEW_PERCENT) {
      jcmenu_graph_yaxis_percent.setSelected(true);
    }
    else if (papp.option.view_yaxis == papp.option.VIEW_GRAM) {
      jcmenu_graph_yaxis_g.setSelected(true);
    }
    info_panel.setOption(papp.option);
  }

  /**
   * サンプルを表示する
   * @param sample STubeSample
   */
  public void showSample(STubeSample sample) {

    //状態パネルを設定する
    status_panel.clearStatus();
    status_panel.setSampleName(sample.name);
    status_panel.setWeight(sample.mdata[sample.mdata.length - 1].weight);
    status_panel.setPortName(papp.option.com_name);
    info_panel.setSample(sample);

    //グラフにサンプルを表示する
    showSampleGraph(sample);
    repaint();
  }

  public void showSampleGraph(STubeSample sample) {
    //グラフにサンプルを表示する
    graph.showSample(sample, papp.option);
  }

  /**
   * 番号で指定してサンプルを表示する
   * @param id int
   */
  public void showSample(int id) {
    if (id > -1 && !papp.samples.isEmpty() && id < papp.samples.size()) {
      current_sample_index = id;
      showSample( (STubeSample) papp.samples.get(id));
    }
    else {
      graph.clearData();
      info_panel.clear();
      status_panel.clearStatus();
    }
  }

  /**
   * リストの最後にあるサンプルを表示する
   */
  public void showSample() {
    showSample(papp.samples.size() - 1);
  }

  /**
   * メニューおよびツールバーのボタンを使用可･不可にする
   * @param enable boolean
   */
  public void setButtonEnabled(boolean enable) {
    for (int i = 0; i < jmenubar.getMenuCount(); i++) {
      jmenubar.getMenu(i).setEnabled(enable);
    }
    jmenu_graph.setEnabled(true);
    jbutton_condition.setEnabled(enable);
    jbutton_start.setEnabled(enable);
    jbutton_option.setEnabled(enable);
    jbutton_stop.setEnabled(!enable);
    jbutton_open.setEnabled(enable);
    jbutton_save.setEnabled(enable);
    jbutton_com.setEnabled(enable);
  }

  /**
   * [File|Save Samples] サンプルデータの保存
   * @param e ActionEvent
   */
  void jmenu_file_savesample_actionPerformed(ActionEvent e) {
    saveSample();
  }

  /**
   * サンプルデータの保存
   */
  void saveSample() {
    try {
      File file = MiscUtils.showSaveDialog(this, ".ssa",
                                           "STube Sample Data (*.ssa)");
      if (file != null) {
        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(
            file));
        os.writeObject(papp.samples);
        os.close();
        papp.setModified(false);
      }
    }
    catch (Exception ex) {
      JOptionPane.showMessageDialog(this, "ファイルを保存できません", "エラー",
                                    JOptionPane.ERROR_MESSAGE);
      ex.printStackTrace();
    }
  }

  /**
   * [File|Save Tube Condition] 沈降管の状態保存
   * @param e ActionEvent
   */
  void jmenu_file_savetube_actionPerformed(ActionEvent e) {
    saveTube();
  }

  /**
   * 沈降管の状態保存
   */
  void saveTube() {
    try {
      File file = MiscUtils.showSaveDialog(this, ".scd",
                                           "STube Condition File (*.scd)");
      if (file != null) {
        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(
            file));
        os.writeObject(papp.condition);
        os.close();
      }
    }
    catch (Exception ex) {
      JOptionPane.showMessageDialog(this, "ファイルを保存できません", "エラー",
                                    JOptionPane.ERROR_MESSAGE);
      ex.printStackTrace();
    }
  }

  /**
   * [File|Save Option] オプションの保存
   * @param e ActionEvent
   */
  void jmenu_file_saveoption_actionPerformed(ActionEvent e) {
    saveOption();
  }

  /**
   * オプションの保存
   */
  void saveOption() {
    try {
      File savefile = MiscUtils.showSaveDialog(this, ".sop",
                                               "STube Option File (*.sop)");
      if (savefile != null) {
        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(
            savefile));
        os.writeObject(papp.option);
        os.close();
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
      JOptionPane.showMessageDialog(this, "ファイルを保存できません", "エラー",
                                    JOptionPane.ERROR_MESSAGE);
    }

  }

  /**
   * [File|Load Samples] サンプルデータを読み込む
   * @param e ActionEvent
   */
  void jmenu_file_loadsample_actionPerformed(ActionEvent e) {
    loadSample();
  }

  /**
   * サンプルを開く
   */
  void loadSample() {
    try {
      File file = MiscUtils.showOpenDialog(this, ".ssa",
                                           "STube Sample Data (*.ssa)");
      if (file != null) {
        ObjectInputStream out = new ObjectInputStream(new FileInputStream(
            file));
        ArrayList samples = (ArrayList) out.readObject();
        papp.setSamples(samples);
        this.showSample(papp.getSamples().length - 1);
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
      JOptionPane.showMessageDialog(this, "ファイルを開くことができません", "エラー",
                                    JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * [File|Load Tube Condition] 沈降管の状態をファイルから読み込む
   * @param e ActionEvent
   */
  void jmenu_file_loadtube_actionPerformed(ActionEvent e) {
    loadTube();
  }

  void loadTube() {
    try {
      File file = MiscUtils.showOpenDialog(this, ".scd",
                                           "STube Condition File (*.scd)");
      if (file != null) {
        ObjectInputStream out = new ObjectInputStream(new FileInputStream(
            file));
        papp.condition = (STubeCondition) out.readObject();
        out.close();
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
      JOptionPane.showMessageDialog(this, "ファイルを開くことができません", "エラー",
                                    JOptionPane.ERROR_MESSAGE);

    }
  }

  /**
   * [File | Load Option] オプションをファイルから読み込む
   * @param e ActionEvent
   */
  void jmenu_file_loadoption_actionPerformed(ActionEvent e) {
    loadOption();
  }

  /**
   * オプションをファイルから読み込む
   */
  void loadOption() {
    try {
      File file = MiscUtils.showOpenDialog(this, ".sop",
                                           "STube Option File (*.sop)");
      if (file != null) {
        ObjectInputStream out = new ObjectInputStream(new FileInputStream(
            file));
        papp.setOption( (STubeOption) out.readObject());
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
      JOptionPane.showMessageDialog(this, "ファイルを開くことができません", "エラー",
                                    JOptionPane.ERROR_MESSAGE);

    }
  }

  /**
   * オプション設定ボタンが押された場合の処理
   * @param e ActionEvent
   */
  void jbutton_option_actionPerformed(ActionEvent e) {
    showOptionDialog();
  }

  /**
   * 各種オプション設定用のダイアログを開く
   */
  void showOptionDialog() {
    JTabbedPane optiontab = new JTabbedPane();

    //それぞれのオプション設定パネルをセットアップし，optiontabに追加する
    STubeOptionMeasurePanel measure = new STubeOptionMeasurePanel();
    measure.setOption(papp.option);
    optiontab.add("測定", measure);

    STubeOptionComPanel com = new STubeOptionComPanel();
    com.setOption(papp.option);
    optiontab.add("通信設定", com);

    STubeOptionGraphPanel graph = new STubeOptionGraphPanel();
    graph.setOption(papp.option);
    optiontab.add("表示", graph);

    STubeOptionOutputPanel output = new STubeOptionOutputPanel();
    output.setOption(papp.option);
    optiontab.add("出力", output);

    if (JOptionPane.showOptionDialog(this, optiontab, "オプション",
                                     JOptionPane.OK_CANCEL_OPTION,
                                     JOptionPane.PLAIN_MESSAGE, null, null, null)
        == JOptionPane.OK_OPTION) {
      STubeOption option;
      option = measure.getOption();
      option = output.getOption();
      option = com.getOption();
      option = graph.getOption();
      papp.setOption(option);
    }
  }

  /**
   * [View|Show Result]
   * @param e ActionEvent
   */
  void jmenu_result_showresult_actionPerformed(ActionEvent e) {
    showResult();
  }

  /**
   * 結果を表すテーブルの表示
   */
  void showResult() {
    result_table.setModel(papp.result_tmodel);
    if (!papp.samples.isEmpty()) {
      result_table.jtable_result.setRowSelectionInterval(current_sample_index,
          current_sample_index);
    }
    result_table.setLocationRelativeTo(this);
    result_table.setVisible(true);
  }

  /**
   * [File|Export Result] 結果データ出力
   * @param e ActionEvent
   */
  void jmenu_file_exportresult_actionPerformed(ActionEvent e) {
    exportResult();
  }

  /**
   * 結果データをテキストに出力
   */
  void exportResult() {
    try {
      File file = MiscUtils.showSaveDialog(this, ".txt", "Text File (*.txt)");
      if (file != null) {
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        bw.write(papp.result_tmodel.toString());
        bw.close();
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
      JOptionPane.showMessageDialog(this, "ファイルを保存できません", "エラー",
                                    JOptionPane.ERROR_MESSAGE);
    }

  }

  /**
   * [Setting|Tube Condition]
   * @param e ActionEvent
   */
  void jmenu_setting_tubecondition_actionPerformed(ActionEvent e) {
    showConditionDialog();
  }

  /**
   * [Setting|Option]
   * @param e ActionEvent
   */
  void jmenu_setting_option_actionPerformed(ActionEvent e) {
    showOptionDialog();
  }

  /**
   * グラフの軸を変更する
   */
  void changeAxis() {
    int xaxis;
    int yaxis;
    if (jcmenu_graph_xaxis_phi.isSelected()) {
      xaxis = papp.option.VIEW_PHI;
    }
    else if (jcmenu_graph_xaxis_mm.isSelected()) {
      xaxis = papp.option.VIEW_MM;
    }
    else {
      xaxis = papp.option.VIEW_TIME;
    }
    if (jcmenu_graph_yaxis_percent.isSelected()) {
      yaxis = papp.option.VIEW_PERCENT;
    }
    else {
      yaxis = papp.option.VIEW_GRAM;
    }

    papp.option.view_xaxis = xaxis;
    papp.option.view_yaxis = yaxis;
    if (papp.measure.isFinished()) {
      showSample(current_sample_index);
    }
    else {
      graph.clearData();
    }
    repaint();
  }

  /**
   * [View|Histogram]
   * @param e ActionEvent
   */
  void jcmenu_graph_histogram_actionPerformed(ActionEvent e) {
    papp.option.view_histogram = jcmenu_graph_histogram.isSelected();
    showSample(current_sample_index);
    repaint();
  }

  /**
   * サンプルのリストを更新する
   */
  void updateSampleList() {
    jmenu_result_viewsample.removeAll();
    for (int i = 0; i < papp.samples.size(); i++) {
      JCheckBoxMenuItem item = new JCheckBoxMenuItem( ( (STubeSample) papp.
          samples.get(i)).name);
      item.setActionCommand(Integer.toString(i));
      if (i == current_sample_index) {
        item.setSelected(true);
      }
      item.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) {
          viewsample_actionPerformed(e);
        }
      });
      jmenu_result_viewsample.add(item);
    }
  }

  /**
   * [View|View Sample|XXX]
   * @param e ActionEvent
   */
  void viewsample_actionPerformed(ActionEvent e) {
    showSample(Integer.valueOf(e.getActionCommand()).intValue());
    updateSampleList();
  }

  /**
   * 測定を中止するボタンの処理
   * @param e ActionEvent
   */
  void jbutton_stop_actionPerformed(ActionEvent e) {
    stopMeasure();
  }

  /**
   * [File|Export Graph]
   * @param e ActionEvent
   */
  void jmenu_file_exportgraph_actionPerformed(ActionEvent e) {
    exportGraph();
  }

  /**
   * グラフの出力
   */
  void exportGraph() {
    try {
      File file = MiscUtils.showSaveDialog(this, ".ps",
                                           "PostScript File (*.ps)");
      if (file != null) {
        PSExporter psex = new PSExporter(graph, file);
        psex.setOrientation(OrientationRequested.LANDSCAPE);
        psex.exportGraphic();
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
      JOptionPane.showMessageDialog(this, "ファイルを保存できません", "エラー",
                                    JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * 測定結果を開く
   * @param e ActionEvent
   */
  void jbutton_open_actionPerformed(ActionEvent e) {
    loadSample();
  }

  /**
   * 測定結果を保存する
   * @param e ActionEvent
   */
  void jbutton_save_actionPerformed(ActionEvent e) {
    saveSample();
  }

  /**
   * ヘルプ
   * @param e ActionEvent
   */
  void jbutton_help_actionPerformed(ActionEvent e) {

  }

  /**
   * [Result|Delete this sample]
   * @param e ActionEvent
   */
  void jmenu_result_deletesample_actionPerformed(ActionEvent e) {
    delete_thissample();
  }

  /**
   * 現在表示中のサンプルを削除する
   */
  void delete_thissample() {
    int[] index = new int[1];
    index[0] = current_sample_index;
    papp.removeSamples(index);
  }

  /**
   * [Result|Rename this sample]
   * @param e ActionEvent
   */
  void jmenu_result_rename_actionPerformed(ActionEvent e) {
    rename_sample(current_sample_index);
  }

  /**
   * サンプル名を変更する
   * @param index int
   */
  void rename_sample(int index) {
    if (!papp.samples.isEmpty() && index < papp.samples.size()) {
      STubeSample sample = papp.getSamples()[index];
      STubeSamplePanel spanel = new STubeSamplePanel();
      spanel.setSample(sample);
      if (JOptionPane.showOptionDialog(this, spanel, "サンプル設定",
                                       JOptionPane.OK_CANCEL_OPTION,
                                       JOptionPane.PLAIN_MESSAGE, null, null, null) ==
          JOptionPane.OK_OPTION) {
        sample = spanel.getSample();
        result_table.updateTable();
        showSample(index);
      }
    }
  }

  void jmenu_result_showerror_actionPerformed(ActionEvent e) {
    showErrorTable();
  }

  void showErrorTable() {
    error_table.setModel(papp.error_tmodel);
    error_table.setLocationRelativeTo(this);
    error_table.setVisible(true);
  }

  /**
   * [File|Start measurement]
   * @param e ActionEvent
   */
  void jmenu_file_measure_actionPerformed(ActionEvent e) {
    startMeasure();
  }

  /**
   * [File|Print Graph]
   * @param e ActionEvent
   */
  void jmenu_file_printgraph_actionPerformed(ActionEvent e) {
    printGraph(graph);
  }

  /**
   * グラフをプリントアウトする
   * @param graph Printable
   */
  void printGraph(Printable graph) {
    try {
      PrinterJob pjob = PrinterJob.getPrinterJob();
      pjob.setJobName("STube Printout");
      pjob.setCopies(1);
      PageFormat pf;
      if (papp.option.pageformat != null) {
        pf = papp.option.pageformat;
      }
      else {
        pf = pjob.defaultPage();
      }
      pjob.setPrintable(graph, pf);
      if (pjob.printDialog() == true) {
        pjob.print();
      }
    }
    catch (PrinterException pex) {
      JOptionPane.showMessageDialog(this, pex.toString(), "Printer Error",
                                    JOptionPane.ERROR_MESSAGE);
      pex.printStackTrace();
    }
  }

  void jcmenu_graph_xaxis_phi_actionPerformed(ActionEvent e) {
    changeAxis();
  }

  void jcmenu_graph_xaxis_mm_actionPerformed(ActionEvent e) {
    changeAxis();
  }

  void jcmenu_graph_xaxis_sec_actionPerformed(ActionEvent e) {
    changeAxis();
  }

  void jcmenu_graph_yaxis_percent_actionPerformed(ActionEvent e) {
    changeAxis();
  }

  void jcmenu_graph_yaxis_g_actionPerformed(ActionEvent e) {
    changeAxis();
  }

  /**
   * ツールバー・通信設定
   * @param e ActionEvent
   */
  void jbutton_com_actionPerformed(ActionEvent e) {
    showComDialog();
  }

  /**
   * 通信設定ダイアログを表示
   */
  void showComDialog() {
    STubeOptionComPanel companel = new STubeOptionComPanel();
    companel.setOption(papp.option);
    if (JOptionPane.showOptionDialog(this, companel, "通信条件設定",
                                     JOptionPane.OK_CANCEL_OPTION,
                                     JOptionPane.PLAIN_MESSAGE, null, null, null) ==
        JOptionPane.OK_OPTION) {
      papp.setOption(companel.getOption());
    }
  }

  /**
   * [File|Page Setup...]
   * @param e ActionEvent
   */
  void jmenu_file_pagesetup_actionPerformed(ActionEvent e) {
    pageSetUp();
  }

  /**
   * 印刷用のページをセットアップする
   */
  void pageSetUp() {
    PrinterJob pj = PrinterJob.getPrinterJob();
    PageFormat pf = pj.defaultPage();
    pf.setOrientation(PageFormat.LANDSCAPE);
    if (papp.option.pageformat != null) {
      pf = pj.pageDialog(papp.option.pageformat);
    }
    else {
      pf = pj.pageDialog(pf);
    }
    if (pf != null) {
      papp.option.pageformat = pf;
    }
  }

  /**
   * [File|Print all graphs]
   * @param e ActionEvent
   */
  void jmenu_file_printall_actionPerformed(ActionEvent e) {
    printAllGraphs();
  }

  /**
   * すべてのグラフをプリントアウトする
   */
  void printAllGraphs() {
    STubeAllGraphs allgraphs = new STubeAllGraphs(papp.samples, papp.option);
    printGraph(allgraphs);
  }

  /**
   * [File|Export All Graphs]
   * @param e ActionEvent
   */
  void jmenu_file_exportallgraphs_actionPerformed(ActionEvent e) {
    exportAllGraphs();
  }

  /**
   * すべてのグラフの出力
   */
  void exportAllGraphs() {
    try {
      File file = MiscUtils.showSaveDialog(this, ".ps",
                                           "PostScript File (*.ps)");
      if (file != null) {
        STubeAllGraphs allgraphs = new STubeAllGraphs(papp.samples, papp.option);
        PSExporter psex = new PSExporter(allgraphs, file);
        psex.setOrientation(OrientationRequested.LANDSCAPE);
        psex.exportGraphic();
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
      JOptionPane.showMessageDialog(this, "ファイルを保存できません", "エラー",
                                    JOptionPane.ERROR_MESSAGE);
    }

  }

  /**
   * [File|Export|Mean & Error]
   * @param e ActionEvent
   */
  void jmenu_file_export_meanerror_actionPerformed(ActionEvent e) {
    exportMeanError();
  }

  /**
   * Mean & Error を出力する
   */
  void exportMeanError() {
    try {
      File file = MiscUtils.showSaveDialog(this, ".txt", "Text File (*.txt)");
      if (file != null) {
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        bw.write(papp.error_tmodel.toString());
        bw.close();
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
      JOptionPane.showMessageDialog(this, "ファイルを保存できません", "エラー",
                                    JOptionPane.ERROR_MESSAGE);
    }

  }

  /**
   * [File|Export|Condition]
   * @param e ActionEvent
   */
  public void jmenu_file_export_condition_actionPerformed(ActionEvent e) {
    exportCondition();
  }

  /**
   * 測定条件をテキストファイルで出力
   */
  void exportCondition() {
    try {
      File file = MiscUtils.showSaveDialog(this, ".txt", "Text File (*.txt)");
      if (file != null) {
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        bw.write(papp.condition.toString());
        bw.close();
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
      JOptionPane.showMessageDialog(this, "ファイルを保存できません", "エラー",
                                    JOptionPane.ERROR_MESSAGE);
    }

  }

}

/**
 * すべてのグラフをプリントアウトするためのクラス
 */
class STubeAllGraphs
    implements Printable {
  ArrayList samples;
  STubeOption option;

  public STubeAllGraphs(ArrayList samples, STubeOption option) {
    this.samples = samples;
    this.option = option;
  }

  /**
   * print
   *
   * @param graphics Graphics
   * @param pageFormat PageFormat
   * @param pageIndex int
   * @return int
   */
  public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
    //サンプルが無い場合はプリントアウトしない
    if (samples.isEmpty()) {
      return Printable.NO_SUCH_PAGE;
    }

    //1ページあたり4サンプルとして，何ページ存在するか調べる．
    int pagenum = (int) Math.ceil( (double) samples.size() / 4.);
    if (pagenum - 1 < pageIndex) {
      return Printable.NO_SUCH_PAGE;
    }

    //このページに表示するグラフを用意する
    STubeGraph[] allgraphs = new STubeGraph[4];
    for (int i = 0; i < 4; i++) {
      allgraphs[i] = new STubeGraph(option);
      if (pageIndex * 4 + i < samples.size()) {
        allgraphs[i].showSample( (STubeSample) samples.get(pageIndex * 4 + i),
                                option);
      }
      else {
        allgraphs[i] = null;
      }
    }

    //1ページあたり4サンプルを描画する
    for (int i = 0; i < 4; i++) {
      double blank_x = pageFormat.getImageableWidth() * 0.1;
      double blank_y = pageFormat.getImageableHeight() * 0.05;
      double width = pageFormat.getImageableWidth();
      double height = pageFormat.getImageableHeight() / 4.;
      double init_x = pageFormat.getImageableX() + blank_x / 2.;
      double init_y = pageFormat.getImageableY() + i * height + blank_y / 2.;
      width -= blank_x;
      height -= blank_y;
      if (allgraphs[i] != null) {
        allgraphs[i].paintGraph(graphics, width, height, init_x, init_y);
      }
    }

    return Printable.PAGE_EXISTS;

  }

}

class STubeFrame_jmenu_file_exit_ActionAdapter
    implements ActionListener {
  STubeFrame adaptee;

  STubeFrame_jmenu_file_exit_ActionAdapter(STubeFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jmenu_file_exit_actionPerformed(e);
  }
}

class STubeFrame_jmenu_help_about_ActionAdapter
    implements ActionListener {
  STubeFrame adaptee;

  STubeFrame_jmenu_help_about_ActionAdapter(STubeFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jmenu_help_about_actionPerformed(e);
  }
}
