package jp.ac.kyoto_u.kueps.STube;

import java.text.*;
import java.util.*;

import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.print.*;
import javax.swing.*;

/**
 * <p>Title: STubeMainPanel</p>
 * <p>Description: データを表示するグラフを描画するパネル</p>
 * <p>Copyright: Copyright (C) 2003 NARUSE, Hajime All rights reserved.<br><br>  This program is free software; you can redistribute it and/or modify<br> it under the terms of the GNU General Public License as published by<br> the Free Software Foundation; either version 2 of the License, or<br> (at your option) any later version.<br><br>  This program is distributed in the hope that it will be useful,<br> but WITHOUT ANY WARRANTY; without even the implied warranty of<br> MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the<br> GNU General Public License for more details.<br><br>  You should have received a copy of the GNU General Public License<br> along with this program; if not, write to the Free Software<br> Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA<br></p>
 * <p>Company: Kyoto University</p>
 * @author NARUSE, Hajime
 * @version 1.0
 */

public class STubeGraph
    extends JComponent
    implements Printable {

  /**X座標の最大値*/
  protected double xmax = 100.0;

  /**X座標の最小値*/
  protected double xmin = 0.0;

  /**Y座標最大値*/
  protected double ymax = 100.0;

  /**Y座標最小値*/
  protected double ymin = 0.0;

  /**x軸の目盛り間隔*/
  double xt_interval = 10;

  /**y軸の目盛り間隔*/
  double yt_interval = 10;

  /**x軸にログスケールを用いる場合の底*/
  int x_logbase = 0;

  /**y軸にログスケールを用いる場合の底*/
  int y_logbase = 0;

  /**表示するデータの配列．Point2D.Doubleオブジェクトが格納される*/
  protected ArrayList datapoints = new ArrayList();

  /**グラフ描画オプション*/
  STubeOption option = new STubeOption();

  /**サンプル名*/
  String samplename = " ";

  BorderLayout borderLayout1 = new BorderLayout();

  /**デフォルトコンストラクタ*/
  public STubeGraph() {
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public STubeGraph(STubeOption option) {
    this();
    setOption(option);
  }

  void jbInit() throws Exception {
  }

  /**
   * オプションをセットする
   * @param opt STubeOption
   */
  public void setOption(STubeOption opt) {
    option = opt;
    repaint();
  }

  /**
   * 画面描画メソッド
   * @param g グラフィックデバイスコンテクスト
   */
  public void paint(Graphics g) {
    paintGraph(g, (double) getWidth(), (double) getHeight(), getX(), getY());
  }

  /**
   * paint, print 共通描画メソッド
   * @param g1 Graphics
   * @param width double
   * @param height double
   * @param init_x double
   * @param init_y double
   */
  public void paintGraph(Graphics g1, double width, double height,
                         double init_x, double init_y) {

    /**グラフィックスの初期化・初期状態の保存・設定*/
    Graphics2D g = (Graphics2D) g1;
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                       RenderingHints.VALUE_ANTIALIAS_ON);
    AffineTransform initial_trans = g.getTransform();
    g.translate(init_x, init_y);

    /**
     * データが存在する場合，オリジナルカーブ・修正カーブ・外枠・目盛りを作成し，
     * 描画する
     */
    GeneralPath curve;
    if (datapoints.size() > 1) {
      /**
       * カーブを作成する．
       * outer_boundary：グラフの外枠
       */
      curve = getCurve();

    }
    else {
      curve = null;
    }

    /**
     * x,y軸の最大・最小値にあわせて外枠を作成する
     */
    GeneralPath outer_boundary = new GeneralPath();
    outer_boundary.append(new Rectangle2D.Double(xmin, ymin, xmax - xmin,
                                                 ymax - ymin), false);

    /**
     * x,y軸の最大・最小値および目盛り間隔に応じて，必要な個数だけ目盛りオブジェクトを作成する
     */
    ArrayList ticks = new ArrayList();
    if (xt_interval != 0) {
      for (double i = Math.ceil(xmin / xt_interval) * xt_interval;
           i <= xmax; i += xt_interval) {
        ticks.add(new Tick(i, ymin, Tick.X_AXIS, x_logbase, 7.0, option.font));
      }
    }
    if (yt_interval != 0) {
      for (double i = Math.ceil(ymin / yt_interval) * yt_interval;
           i <= ymax; i += yt_interval) {
        ticks.add(new Tick(i, xmin, Tick.Y_AXIS, 7.0, option.font));
      }
    }

    /**
     * グラフの数値座標をデバイス座標へと変換するためのアフィン変換行列を作成する
     * まず，グラフの原点をデバイス座標の原点へ移動させ，上下反転させながらデバイス
     * の大きさに合わせて拡大縮小させる
     * scale_x: 左右方向の拡大係数
     * scale_y：上下方向の拡大係数
     */
    double scale_x = 0.8 * width / (xmax - xmin);
    double scale_y = 0.9 * height / (ymax - ymin);
    AffineTransform aT = new AffineTransform();
    aT.setToTranslation( -xmin * scale_x, -ymin * -scale_y);
    aT.scale(scale_x, -scale_y);

    /**
     * 外枠・目盛りをそれぞれデバイス座標へと変換する
     */
    outer_boundary.transform(aT);
    for (int i = 0; i < ticks.size(); i++) {
      ( (Tick) (ticks.get(i))).transform(aT);
    }

    /**
     * ユーザー座標の原点を移動させ，グラフが画面の中央に写るようにする
     */
    g.translate(0.1 * width, 0.95 * height);

    /**外枠・目盛りを描画する*/
    g.setPaint(Color.black);
    g.draw(outer_boundary);
    for (int i = 0; i < ticks.size(); i++) {
      ( (Tick) (ticks.get(i))).draw(g);
    }

    /**凡例を表示する*/
    //外枠の各座標と，文字の大きさを取得
    double boundary_minx = outer_boundary.getBounds2D().getMinX();
    double boundary_maxx = outer_boundary.getBounds2D().getMaxX();
    double boundary_miny = outer_boundary.getBounds2D().getMinY();
    double boundary_maxy = outer_boundary.getBounds2D().getMaxY();
    Rectangle2D letter = (new TextLayout("O", option.font,
                                         g.getFontRenderContext())).getBounds();
    //軸のラベル
    String xlabel_text;
    if (option.view_xaxis == STubeOption.VIEW_PHI) {
      xlabel_text = "(PHI)";
    }
    else if (option.view_xaxis == STubeOption.VIEW_MM) {
      xlabel_text = "(mm)";
    }
    else {

      xlabel_text = "(Sec.)";
    }
    TextLayout xlabel = new TextLayout(xlabel_text, option.font,
                                       g.getFontRenderContext());
    Point2D xlabel_pos = new Point2D.Double(boundary_maxx +
                                            letter.getWidth() * 2,
                                            boundary_maxy);
    String ylabel_text = option.view_yaxis == option.VIEW_PERCENT ? "(%)" :
        "(g)";
    TextLayout ylabel = new TextLayout(ylabel_text, option.font,
                                       g.getFontRenderContext());
    Point2D ylabel_pos = new Point2D.Double(boundary_minx -
                                            letter.getWidth() * 7,
                                            boundary_miny);
    xlabel.draw(g, (float) xlabel_pos.getX(), (float) xlabel_pos.getY());
    ylabel.draw(g, (float) ylabel_pos.getX(), (float) ylabel_pos.getY());
    //グラフタイトルの表示
    TextLayout title = new TextLayout(samplename, option.font,
                                      g.getFontRenderContext());
    float x = (float) (boundary_minx + (boundary_maxx - boundary_minx) / 2. -
                       title.getBounds().getWidth());
    float y = (float) (boundary_miny - letter.getHeight() / 2.);
    title.draw(g, x, y);

    if (curve != null) {
      /**外枠でクリップして，カーブを描画する*/
      curve.transform(aT);
      Shape orig_clip = g.getClip();
      Stroke orig_stroke = g.getStroke();
      g.setStroke(new BasicStroke(option.line_width));
      g.setClip(outer_boundary);
      Paint orig_paint = g.getPaint();
      g.setPaint(option.line_color);
      g.draw(curve);
      g.setClip(orig_clip);
      g.setStroke(orig_stroke);
      g.setPaint(orig_paint);
    }

    //グラフィック描画位置を元に戻す
    g.setTransform(initial_trans);
  }

  /**
   * x座標・y座標の配列から折れ線を作成する
   * @return 入力されたx,y座標に対応するGenericPathの折れ線を返す
   */
  private GeneralPath getCurve() {
    GeneralPath curve = new GeneralPath();
    if (!datapoints.isEmpty()) {
      curve.moveTo( (float) ( (Point2D.Double) datapoints.get(0)).x,
                   (float) ( (Point2D.Double) datapoints.get(0)).y);
      for (int i = 1; i < datapoints.size(); i++) {
        curve.lineTo( (float) ( (Point2D.Double) datapoints.get(i)).x,
                     (float) ( (Point2D.Double) datapoints.get(i)).y);
      }
    }
    return curve;

  }

  /**
   * プリントアウト用描画メソッド
   * @param graphics グラフィックデバイスコンテキスト
   * @param pageFormat ページフォーマット
   * @param pagenum ページ番号
   * @return ページ番号が0のときはPAGE_EXISTS，それ以外のときはNO_SUCH_PAGEを返す
   */
  public int print(Graphics graphics, PageFormat pageFormat, int pagenum) {
    if (pagenum != 0) {
      return Printable.NO_SUCH_PAGE;
    }

    //グラフの描画
    paintGraph(graphics, pageFormat.getImageableWidth(),
               pageFormat.getImageableHeight(), pageFormat.getImageableX(),
               pageFormat.getImageableY());

    return Printable.PAGE_EXISTS;
  }

  /**
   * データポイントを追加する
   * @param x double
   * @param y double
   */
  public void addData(double x, double y) {
    datapoints.add(new Point2D.Double(x, y));
    this.repaint();
  }

  /**
   * データをセットする
   * @param x double[]
   * @param y double[]
   */
  public void setData(double x[], double y[]) {
    if (x.length == y.length) {
      datapoints.clear();
      for (int i = 0; i < x.length; i++) {
        datapoints.add(new Point2D.Double(x[i], y[i]));
      }
    }
    repaint();
  }

  /**
   * データを消去する
   */
  public void clearData() {
    samplename = " ";
    datapoints.clear();
    this.repaint();
  }

  /**
   * X軸の範囲を設定する
   * @param xmin double
   * @param xmax double
   * @param tick_interval double
   */
  public void setXRange(double xmin, double xmax, double tick_interval) {
    if (xmin != xmax && !Double.isInfinite(ymax)) {
      this.xmin = xmin;
      this.xmax = xmax;
      this.xt_interval = tick_interval;
    }
    this.repaint();
  }

  /**
   * Y軸の範囲を設定する
   * @param ymin double
   * @param ymax double
   * @param tick_interval double
   */
  public void setYRange(double ymin, double ymax, double tick_interval) {
    if (ymin < ymax && !Double.isInfinite(ymax)) {
      this.ymin = ymin;
      this.ymax = ymax;
      this.yt_interval = tick_interval;
    }
    this.repaint();
  }

  public void showSample(STubeSample sample, STubeOption option) {
    //グラフ初期化
    clearData();

    //グラフのデータを読み込む
    samplename = sample.name;
    double x;
    double y;
    double maxx = 0;
    double maxy = 0;
    double total_weight = sample.total_weight;
    double current_dweight;
    double current_weight;
    double current_phi;
    double current_time;
    for (int i = 0; i < sample.mdata.length; i++) {
      current_dweight = sample.mdata[i].dweight;
      current_weight = sample.mdata[i].weight;
      current_phi = sample.mdata[i].phi;
      current_time = sample.mdata[i].time;
      if(option.view_xaxis == option.VIEW_PHI){
        x = current_phi;
        setXLogBase(0);
      }
      else if (option.view_xaxis == option.VIEW_MM){
        x = current_phi * Math.log(2.) / Math.log(10.) * -1;
        setXLogBase(10);
      }
      else{
        x = current_time;
        setXLogBase(0);
      }
      maxx = maxx < x ? x : maxx;
      y = option.view_histogram ?
          current_dweight : current_weight;
      y = option.view_yaxis == option.VIEW_PERCENT ? y / total_weight * 100 : y;
      maxy = maxy < y ? y : maxy;
      datapoints.add(new Point2D.Double(x, y));
    }

    //レンジの設定
    if (option.view_xaxis == option.VIEW_PHI) {
      setXRange(sample.condition.phimin, sample.condition.phimax, 0.5);
    }
    else if (option.view_xaxis == option.VIEW_MM) {
      setXRange(sample.condition.phimax * Math.log(2) / Math.log(10) * -1,
                sample.condition.phimin * Math.log(2) / Math.log(10) * -1, 0.2);
    }
    else {
      setXRange(0, maxx, 10);
    }
    if (option.view_yaxis == option.VIEW_PERCENT) {
      setYRange(0, maxy + 10., 10.);
    }
    else {
      setYRange(0, maxy + 0.2, 0.2);
    }

    this.repaint();

  }

  public void setXLogBase(int logbase){
    x_logbase = logbase;
    repaint();
  }

  public void setYLogBase(int logbase){
  y_logbase = logbase;
  repaint();
}


}

/**
 *
 * <p>タイトル: Tick</p>
 * <p>説明: ScheilGraphの目盛りを表現するクラス．座標変換しても目盛り文字が
 * ゆがまないようにしてある．</p>
 * <p>著作権:  Copyright (C) 2003 NARUSE, Hajime  This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.  You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA </p>
 * <p>会社名: Kyoto University</p>
 * @author NARUSE, Hajime
 * @version 1.0
 */
class Tick {
  /**
   * 目盛りの位置
   */
  private Point2D.Double tick_location = new Point2D.Double(0.0, 0.0);

  /**
   * 目盛りの長さ
   */
  private double tick_length = 8.0;

  /**
   * X軸用の目盛りであることを示す
   */
  static public int X_AXIS = 1;

  /**
   * Y軸用の目盛りであることを示す
   */
  static public int Y_AXIS = 2;

  /**
   * どちらの軸の目盛りであるかを示す．X_AXISもしくはY_AXIS
   */
  private int tick_axis = Tick.X_AXIS;

  /**
   * この目盛りが示す数値
   */
  double tick_number = 0.0;

  /**
   * 目盛りの文字
   */
  Font tick_font = new Font("SansSerif", Font.PLAIN, 11);

  /**ログスケールの底となる数*/
  int tick_logbase = 10;

  /**
   * デフォルトコンストラクタ
   */
  public Tick() {
  }

  /**
   * コンストラクタ
   * @param number double
   * @param static_number double
   * @param axis int
   * @param logbase int
   * @param length double
   * @param font Font
   */
  public Tick(double number, double static_number, int axis, int logbase,
              double length,
              Font font) {
    setTick(number, static_number, axis, logbase, length, font);
  }

  /**
   * 最初に数値・本来の軸以外の軸の最小値・軸の指定・目盛りの長さ・フォントを指定して
   * オブジェクトを構築するコンストラクタ
   * @param number 目盛りの数値
   * @param static_number 本来の軸でないほうの軸の最小値．要するに，
   * この座標の数値を変化させずに目盛りを移動させていく．
   * @param axis どちらの軸のための目盛りであるかを指定
   * @param length 目盛りの長さ
   * @param font 目盛りのフォント
   */
  public Tick(double number, double static_number, int axis, double length,
              Font font) {
    setTick(number, static_number, axis, 0, length, font);
  }

  /**
   * 目盛りの数値・本来の軸以外の軸の最小値・軸の指定・目盛りの長さ・フォントを指定する
   * @param number 目盛りの数値
   * @param static_number 本来の軸でないほうの軸の最小値．要するに，
   * この座標の数値を変化させずに目盛りを移動させていく．
   * @param axis どちらの軸のための目盛りであるかを指定
   * @param logbase ログスケールの場合の底となる数
   * @param length 目盛りの長さ
   * @param font 目盛りのフォント
   */
  public void setTick(double number, double static_number, int axis,
                      int logbase, double length, Font font) {
    tick_number = number;
    tick_axis = axis;
    if (tick_axis == X_AXIS) {
      tick_location = new Point2D.Double(tick_number, static_number);
    }
    else {
      tick_location = new Point2D.Double(static_number, tick_number);
    }
    tick_logbase = logbase;
    tick_length = length;
    tick_font = font;
  }

  /**
   * 目盛りの位置をアフィン座標変換で移動する
   * @param aft アフィン座標変換行列
   */
  public void transform(AffineTransform aft) {
    double[] location = new double[2];
    location[0] = tick_location.x;
    location[1] = tick_location.y;
    aft.transform(location, 0, location, 0, 1);
    tick_location.x = location[0];
    tick_location.y = location[1];
  }

  /**
   * 目盛りの描画
   * @param g グラフィックデバイスコンテキスト
   */
  public void draw(Graphics2D g) {
    /**
     * tick_line：目盛り線
     * nf：目盛りに表示する数値フォーマット
     * tick_text：目盛りに表示する数字
     * text_x,text_y：目盛りに表示する数字の表示位置座標
     */
    float text_x, text_y;
    TextLayout tick_text;
    Line2D.Double tick_line = new Line2D.Double();
    NumberFormat nf = NumberFormat.getNumberInstance();
    nf.setMaximumFractionDigits(1);
    if (tick_logbase == 0) {
      tick_text = new TextLayout(nf.format(tick_number), tick_font,
                                 g.getFontRenderContext());
    }
    else {
      String source_text = Integer.toString(tick_logbase) +
          nf.format(tick_number);
      AttributedString attributed_text = new AttributedString(source_text);
      attributed_text.addAttribute(TextAttribute.FAMILY, tick_font.getFamily());
      attributed_text.addAttribute(TextAttribute.SIZE, new Float( tick_font.getSize2D()));
      AffineTransform aft = new AffineTransform();
      aft.scale(2. / 3., 2. / 3.);
      aft.translate(0, -1. / 2. * (2. / 3. * tick_font.getSize2D()));
      attributed_text.addAttribute(TextAttribute.TRANSFORM,
                                   new TransformAttribute(aft),
                                   Integer.toString(tick_logbase).length(),
                                   source_text.length());
      tick_text = new TextLayout(attributed_text.getIterator(),
                                 g.getFontRenderContext());
    }

    /**
     * X軸用の目盛りだった場合
     */
    if (tick_axis == X_AXIS) {
      tick_line.setLine(tick_location.x, tick_location.y,
                        tick_location.x,
                        tick_location.y + tick_length);
      text_x = (float) (tick_location.x -
                        tick_text.getBounds().getWidth() / 2.0);
      text_y = (float) (tick_location.y + tick_length +
                        tick_text.getBounds().getHeight());
    }
    /**
     * Y軸用の目盛りだった場合
     */
    else {
      tick_line.setLine(tick_location.x - tick_length, tick_location.y,
                        tick_location.x, tick_location.y);
      text_x = (float) (tick_location.x - tick_length -
                        tick_text.getBounds().getWidth());
      text_y = (float) (tick_location.y +
                        tick_text.getBounds().getHeight() / 2.0);
    }

    /**
     *目盛りと数字の描画
     */
    g.draw(tick_line);
    tick_text.draw(g, text_x, text_y);
  }
}
