package jp.ac.kyoto_u.kueps.STube;

import java.io.*;
import java.util.*;

import java.awt.*;
import javax.swing.*;

/**
 * <p>Title: S-Tube</p>
 *
 * <p>Description: Controlling settling tube for analyzing grain-size</p>
 * <p>Copyright: Copyright (C) 2003 NARUSE, Hajime All rights reserved.<br><br>  This program is free software; you can redistribute it and/or modify<br> it under the terms of the GNU General Public License as published by<br> the Free Software Foundation; either version 2 of the License, or<br> (at your option) any later version.<br><br>  This program is distributed in the hope that it will be useful,<br> but WITHOUT ANY WARRANTY; without even the implied warranty of<br> MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the<br> GNU General Public License for more details.<br><br>  You should have received a copy of the GNU General Public License<br> along with this program; if not, write to the Free Software<br> Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA<br></p>
 * <p>Company: Kyoto University</p>
 * @author NARUSE, Hajime
 * @version 1.0
 */

public class STube {

  /**沈降管の状態を表すオブジェクト*/
  STubeCondition condition;

  /**サンプルのリスト*/
  ArrayList samples = new ArrayList();

  /**このアプリケーションのオプション*/
  STubeOption option;

  /**計測結果を出力するテーブルモデル*/
  STubeResultTableModel result_tmodel = new STubeResultTableModel();

  /**平均と誤差を出力するテーブルモデル*/
  STubeErrorTableModel error_tmodel = new STubeErrorTableModel();

  /**このアプリケーションのメインフレーム*/
  STubeFrame frame = new STubeFrame();

  /**測定処理を行うスレッド*/
  STubeMeasure measure = new STubeMeasure();

  /**データが修正されているか*/
  boolean modified = false;

  boolean packFrame = false;

  //Construct the application
  public STube() {

    try {
      frame.setParentApp(this);//このオブジェクトのポインタをフレームに与える
      result_tmodel.setSamples(samples);
      error_tmodel.setSamples(samples);
      loadOptionOnInit();


      setModified(false);
      frame.updateFrame();

      //Validate frames that have preset sizes
      //Pack frames that have useful preferred size info, e.g. from their layout
      if (packFrame) {
        frame.pack();
      }
      else {
        frame.validate();
      }

      //Center the window
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension frameSize = frame.getSize();
      if (frameSize.height > screenSize.height) {
        frameSize.height = screenSize.height;
      }
      if (frameSize.width > screenSize.width) {
        frameSize.width = screenSize.width;
      }
      frame.setLocation( (screenSize.width - frameSize.width) / 2,
                        (screenSize.height - frameSize.height) / 2);
      frame.setVisible(true);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  //Main method
  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (Exception e) {
      e.printStackTrace();
      JOptionPane.showConfirmDialog(null, e.toString());
    }
    new STube();
  }

  /**
   * 沈降管の状態を取得する
   * @return STubeCondition
   */
  public STubeCondition getCondition() {
    return condition;
  }

  /**沈降管の状態を設定する
   *
   * @param cond STubeCondition
   */
  public void setCondition(STubeCondition cond) {
    condition = cond;
  }

  /**サンプルの配列を取得する
   *
   * @return STubeSample[]
   */
  public STubeSample[] getSamples() {
    STubeSample[] s = new STubeSample[samples.size()];
    samples.toArray(s);
    return s;
  }

  /**サンプルを追加する
   *
   * @param sample STubeSample
   */
  public void addSample(STubeSample sample) {
    samples.add(sample);
    error_tmodel.setSamples(samples);
    frame.updateFrame();
    setModified(true);
  }

  /**
   * サンプルをセットする
   * @param slist ArrayList
   */
  public void setSamples(ArrayList slist) {
    try {
      Iterator it = slist.iterator();
      while (it.hasNext()) {
        if (! (it.next() instanceof STubeSample)) {
          throw new IllegalArgumentException();
        }
      }
      this.samples = slist;
      result_tmodel.setSamples(samples);
      error_tmodel.setSamples(samples);
      frame.updateFrame();
      setModified(false);
    }
    catch (IllegalArgumentException ex) {
      JOptionPane.showMessageDialog(frame, "不正なファイル形式です", "エラー",
                                    JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * オプションをセットする
   * @param opt STubeOption
   */
  public void setOption(STubeOption opt) {
    this.option = opt;
    result_tmodel.setOption(option);
    error_tmodel.setOption(option);
    frame.updateFrame();
  }

  /**
   * 終了時にオプションを保存
   */
  void saveOptionOnExit() {
    try {
      String home = System.getProperty("user.home");
      String sep = System.getProperty("file.separator");
      File stubedir = new File(home + sep + ".STube");
      if (!stubedir.exists()) {
        stubedir.mkdir();
      }
      File optfile = new File(stubedir.getPath() + sep + "STubeOption.sop");
      File condfile = new File(stubedir.getPath() + sep + "STubeCondition.scd");
      ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
          optfile));
      ObjectOutputStream cos = new ObjectOutputStream(new FileOutputStream(
          condfile));
      oos.writeObject(option);
      cos.writeObject(condition);
      System.out.println("[SAVE] Saved option and condition - wdepth=" + condition.wdepth + ", wtemp=" + condition.wtemp + ", rhos=" + condition.rhos);
      oos.close();
      cos.close();
    }
    catch (FileNotFoundException ex) {
      ex.printStackTrace();
    }
    catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  /**
   * 開始時にオプションを読み込む
   */
  public void loadOptionOnInit() {
    try {
      String home = System.getProperty("user.home");
      String sep = System.getProperty("file.separator");
      File stubedir = new File(home + sep + ".STube");
      File optfile = new File(stubedir.getPath() + sep + "STubeOption.sop");
      System.out.println("[OPTFILE] " + optfile.getAbsolutePath());
      File condfile = new File(stubedir.getPath() + sep + "STubeCondition.scd");
      if (optfile.exists()) {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
            optfile));
        setOption( (STubeOption) ois.readObject());
        System.out.println("[OPT] option=" + option);
        System.out.println("[OPT] rate=" + option.com_rate);
        ois.close();
      }
      else {
        setOption(new STubeOption());
      }
      if (condfile.exists()) {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
            condfile));
        setCondition( (STubeCondition) ois.readObject());
        System.out.println("[COND] Loaded condition - wdepth=" + condition.wdepth + ", wtemp=" + condition.wtemp + ", rhos=" + condition.rhos);
        ois.close();
      }
      else {
        setCondition(new STubeCondition());
        System.out.println("[COND] Created new default condition");
      }
    }
    catch (ClassNotFoundException ex) {
      setOption(new STubeOption());
      condition = new STubeCondition();
      ex.printStackTrace();
    }
    catch (IOException ex) {
      setOption(new STubeOption());
      condition = new STubeCondition();
      ex.printStackTrace();
    }

  }

  /**
   * サンプルデータが変更されているか否かを設定する
   *
   * @param modified boolean
   */
  public void setModified(boolean modified) {
    this.modified = modified;
    frame.jmenu_file_savesample.setEnabled(modified);
  }

  /**
   * サンプルを削除する
   * @param indexes int[]
   */
  public void removeSamples(int[] indexes) {
    TreeMap map = new TreeMap();
    for (int i = 0; i < samples.size(); i++) {
      map.put(new Integer(i), samples.get(i));
    }
    for (int i = 0; i < indexes.length; i++) {
      if (indexes[i] < samples.size()) {
        map.remove(new Integer(indexes[i]));
      }
    }
    samples.clear();
    samples.addAll(map.values());

    frame.updateFrame();
    setModified(true);
  }

}
