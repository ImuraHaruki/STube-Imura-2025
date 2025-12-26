package jp.ac.kyoto_u.kueps.STube;

import java.io.*;

import java.awt.*;
import java.awt.print.*;

import gnu.io.*;

/**
 * <p>Title: S-Tube</p>
 * <p>Description: Controlling settling tube for analyzing grain-size</p>
 * <p>Copyright: Copyright (C) 2003 NARUSE, Hajime All rights reserved.<br><br>  This program is free software; you can redistribute it and/or modify<br> it under the terms of the GNU General Public License as published by<br> the Free Software Foundation; either version 2 of the License, or<br> (at your option) any later version.<br><br>  This program is distributed in the hope that it will be useful,<br> but WITHOUT ANY WARRANTY; without even the implied warranty of<br> MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the<br> GNU General Public License for more details.<br><br>  You should have received a copy of the GNU General Public License<br> along with this program; if not, write to the Free Software<br> Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA<br></p>
 * <p>Company: Kyoto University</p>
 * @author NARUSE, Hajime
 * @version 1.0
 */

public class STubeOption
    implements Serializable, Cloneable {

  /**カウントダウン*/
  protected int cdoun = 20;

  /**通信ポート名*/
  protected String com_name = "COM1";

  /**通信ポートの速度*/
  int com_rate = 38400;

  /**データビット*/
  int com_databits = SerialPort.DATABITS_8;

  /**ストップビット*/
  int com_stopbits = SerialPort.STOPBITS_1;

  /**パリティ*/
  int com_parity = SerialPort.PARITY_NONE;

  /**表示モード(phi or time)*/
  int view_xaxis = VIEW_PHI;
  static int VIEW_PHI = 1;
  static int VIEW_TIME = 3;
  static int VIEW_MM = 2;

  /**表示モード (% or g) */
  int view_yaxis = VIEW_PERCENT;
  static int VIEW_PERCENT = 1;
  static int VIEW_GRAM = 2;

  /**表示モード (histogram or cumulative)*/
  boolean view_histogram = true;

  /**グラフの色*/
  protected Color line_color = Color.red;

  /**グラフのフォント*/
  protected Font font = new Font("SansSerif", Font.PLAIN, 11);

  /**線の太さ*/
  protected float line_width = 2.0f;

  /**計測遅延(m.sec.)*/
  int delay = 0;

  /**沈降管の状態を出力するか否か*/
  protected boolean output_condition = false;

  /**統計値を出力するか否か*/
  protected boolean output_stats = true;

  /**生データを出力するか否か*/
  protected boolean output_rawdata = false;

  /**重量階級頻度を出力するか否か*/
  protected boolean output_class = true;

  /**印刷するページのフォーマット*/
  transient protected PageFormat pageformat;

  /**カウントダウンするサウンドのパス*/
  String sound_count_path = "";

  /**ラスト3秒をカウントダウンするサウンドのパス*/
  String sound_lastcount_path = "";

  /**計測開始を告げるサウンドのパス*/
  String sound_start_path = "";

  public STubeOption() {
    pageformat = new PageFormat();
  }

}
