package jp.ac.kyoto_u.kueps.STube;

import java.io.*;
import java.util.*;

import javax.swing.*;

import gnu.io.*;

/**
 * <p>Title: S-Tube</p>
 * <p>Description: Controlling settling tube for analyzing grain-size</p>
 * <p>Copyright: Copyright (C) 2003 NARUSE, Hajime All rights reserved.<br><br>  This program is free software; you can redistribute it and/or modify<br> it under the terms of the GNU General Public License as published by<br> the Free Software Foundation; either version 2 of the License, or<br> (at your option) any later version.<br><br>  This program is distributed in the hope that it will be useful,<br> but WITHOUT ANY WARRANTY; without even the implied warranty of<br> MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the<br> GNU General Public License for more details.<br><br>  You should have received a copy of the GNU General Public License<br> along with this program; if not, write to the Free Software<br> Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA<br></p>
 * <p>Company: Kyoto University</p>
 * @author NARUSE, Hajime
 * @version 1.0
 */

public class STubeMeasure {

  double max_dweight = 0.0;
  double phi;
  double dphi;

  /**天秤をあらわすオブジェクト*/
  STubeBalance balance;

  /**測定開始時刻を記録するための変数*/
  long start = 0;

  /**測定が中止されたか否かを示すフラグ*/
  boolean canceled = false;

  /**測定が終了したか否かを示すフラグ*/
  boolean finished = true;

  /**測定結果を表すグラフ*/
  STubeGraph graph;

  /**親フレームのポインタ*/
  STubeFrame pframe;

  /**各種設定*/
  STubeOption option;

  /**通信ポートの名前*/
  String com;

  /**沈降管の測定条件*/
  STubeCondition condition;

  /**サンプル*/
  STubeSample sample;

  /**測定結果を格納する配列*/
  Double[] weight;

  /**計測を行うためのタイマー*/
  java.util.Timer timer = new java.util.Timer();

  public STubeMeasure() {

  }

  public STubeMeasure(STubeFrame frm, String com,
                      STubeCondition condition, STubeSample sample) {
    try {
      //各種変数を初期化
      pframe = frm;
      graph = pframe.graph;
      option = pframe.papp.option;
      this.com = com;
      this.condition = condition;
      this.sample = sample;
      phi = condition.phimin;
      dphi = condition.dphi;
      pframe.setButtonEnabled(false);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * 測定開始
   */
  public void startMeasure() {

    try {
      //初期化処理
      finished = false;
      sample.condition = condition;
      balance = new STubeBalance(com); //天秤をあらわすオブジェクトを生成
      balance.setOption(option); // オプション（ボーレート等）を渡す
      balance.open(); //天秤との通信を開始する
      balance.tareAndFlush(1000); //天秤の目盛りを0にして受信バッファを捨てる（待機時間を更に増加）
      balance.calibrateZero(); //初回読み取り値をゼロ点として設定（値が安定するまで待つ）

      //グラフの初期化
      initGraph();

      //カウントダウン
      STubeCountDownDialog cddialog = new STubeCountDownDialog(pframe,
          option.cdoun, option.sound_count_path, option.sound_lastcount_path,
          option.sound_start_path);
      cddialog.startCountDown();

      //測定開始時刻を保存する
      start = System.currentTimeMillis();
      long current_time = 0;

      /**測定処理を行うタイマーをセットする*/
      for (int i = 0; i < condition.mtime.length; i++) {
        current_time = System.currentTimeMillis() - start;
        timer.schedule(new MeasureTask(this, sample, i),
                       (long) (condition.mtime[i] * 1000.) -
                       pframe.papp.option.delay - current_time);
      }

      /**測定状況表示*/
      pframe.status_panel.setSampleName(sample.name);
      pframe.status_panel.setCount( (int) (condition.mtime[condition.mtime.
                                           length -
                                           1]) + 1);

      /**終了処理を行うタイマーをセット*/
      timer.schedule(new EndTask(this),
                     (long) (condition.mtime[condition.mtime.length -
                             1] * 1000.) -
                     pframe.papp.option.delay + 100);
    }

    //例外処理
    catch (NoSuchPortException ex) {
      JOptionPane.showMessageDialog(pframe, "通信ポートが見つかりません", "エラー",
                                    JOptionPane.ERROR_MESSAGE);
      ex.printStackTrace();
      pframe.setButtonEnabled(true);
      finished = true;
    }
    catch (NumberFormatException ex) {
      JOptionPane.showMessageDialog(pframe, "対応していないデータ形式です", "エラー",
                                    JOptionPane.ERROR_MESSAGE);
      ex.printStackTrace();
      pframe.setButtonEnabled(true);
      finished = true;
    }
    catch (TooManyListenersException ex) {
      JOptionPane.showMessageDialog(pframe, ex.toString(), "エラー",
                                    JOptionPane.ERROR_MESSAGE);
      ex.printStackTrace();
      pframe.setButtonEnabled(true);
      finished = true;
    }
    catch (IOException ex) {
      JOptionPane.showMessageDialog(pframe, "電子天秤に接続できません", "エラー",
                                    JOptionPane.ERROR_MESSAGE);
      pframe.setButtonEnabled(true);
      ex.printStackTrace();
      finished = true;
    }
    catch (UnsupportedCommOperationException ex) {
      JOptionPane.showMessageDialog(pframe, ex.toString(), "エラー",
                                    JOptionPane.ERROR_MESSAGE);
      pframe.setButtonEnabled(true);
      ex.printStackTrace();
      finished = true;
    }
    catch (PortInUseException ex) {
      JOptionPane.showMessageDialog(pframe, ex.toString(), "エラー",
                                    JOptionPane.ERROR_MESSAGE);
      pframe.setButtonEnabled(true);
      ex.printStackTrace();
      finished = true;
    }
  }

  /**
   * グラフの初期化
   */
  protected void initGraph() {
    graph.clearData();
    pframe.info_panel.clear();
    if (option.view_xaxis == option.VIEW_PHI) {
      graph.setXRange(condition.phimin, condition.phimax, 0.5);
    }
    else {
      graph.setXRange(0, condition.minterval, 5);
    }
    if (option.view_yaxis == option.VIEW_PERCENT) {
      graph.setYRange(0.0, 100.0, 100.0);
    }
    else {
      graph.setYRange(0.0, 1.0, 0.5);
    }
  }

  /**測定動作がキャンセルされた場合*/
  public void cancel() {
    canceled = true;
    timer.cancel();
    balance.close();
    pframe.setButtonEnabled(true);
    pframe.showSample();
  }

  /**
   * 測定処理が終了したか問い合わせる
   * @return boolean
   */
  public boolean isFinished() {
    return finished;
  }

}

/**
 * 測定動作を行うタイマータスク
 */
class MeasureTask
    extends TimerTask {
  STubeMeasure measure;
  STubeSample sample;
  int index;

  public MeasureTask(STubeMeasure measure, STubeSample sample, int index) {
    this.measure = measure;
    this.sample = sample;
    this.index = index;
  }

  /**
   * 測定処理
   */
  public void run() {
    if (!measure.canceled) {
      try {
        //サンプルにデータを追加
        double time = (double) (System.currentTimeMillis() - measure.start) /
            1000.;
        double weight = measure.balance.getValue();
        double phi = measure.condition.phimin + index * measure.condition.dphi;
        sample.addData(weight, time, phi);
        double dweight = sample.mdata[sample.mdata.length - 1].dweight;
        measure.max_dweight = measure.max_dweight < dweight ? dweight :
            measure.max_dweight;

        //結果表示
        System.out.println(Double.toString(time) + "\t" +
                           Double.toString(weight));
        measure.pframe.status_panel.setWeight(weight);
        showSample(sample, measure.option);
      }

      catch (IOException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(measure.pframe, "電子天秤と通信できません", "エラー",
                                      JOptionPane.ERROR_MESSAGE);
      }
    }

  }

  /**
   * サンプルをグラフに表示する
   * @param sample STubeSample
   * @param option STubeOption
   */
  private final void showSample(STubeSample sample, STubeOption option) {

    //グラフ初期化
    STubeGraph graph = measure.graph;

    //グラフのデータを読み込む
    double[] x = new double[sample.mdata.length];
    double[] y = new double[sample.mdata.length];
    double maxx = 0;
    double maxy = 0;
    double weight = sample.mdata[sample.mdata.length - 1].weight;
    double currentdweight;
    double currentweight;
    for (int i = 0; i < x.length; i++) {
      currentdweight = sample.mdata[i].dweight;
      currentweight = sample.mdata[i].weight;
      if (measure.option.view_xaxis == STubeOption.VIEW_PHI) {
        x[i] = sample.mdata[i].phi;
      }
      else if (measure.option.view_xaxis == STubeOption.VIEW_MM) {
        x[i] = sample.mdata[i].phi * Math.log(2.) / Math.log(10.) * -1;
      }
      else {
        x[i] = sample.mdata[i].time;
      }
      maxx = maxx < x[i] ? x[i] : maxx;
      y[i] = measure.option.view_histogram ? currentdweight : currentweight;
      y[i] = measure.option.view_yaxis == STubeOption.VIEW_PERCENT ?
          y[i] / weight * 100 : y[i];
      maxy = maxy < y[i] ? y[i] : maxy;
    }
    graph.setData(x, y);

    //レンジの設定
    if (measure.option.view_xaxis == STubeOption.VIEW_PHI) {
      graph.setXRange(sample.condition.phimin, sample.condition.phimax, 0.5);
    }
    else if (measure.option.view_xaxis == STubeOption.VIEW_MM) {
      graph.setXLogBase(10);
      graph.setXRange(sample.condition.phimax * Math.log(2.) / Math.log(10.) *
                      -1,
                      sample.condition.phimin * Math.log(2.) / Math.log(10.) *
                      -1,
                      0.2);
    }
    else {
      graph.setXRange(0, sample.condition.minterval, 10);
    }
    if (measure.option.view_yaxis == STubeOption.VIEW_PERCENT) {
      graph.setYRange(0, maxy + 10., 10.);
    }
    else {
      graph.setYRange(0, maxy + 0.2, 0.2);
    }
  }
}

/**
 * 測定終了処理を行うタイマータスク
 */
class EndTask
    extends TimerTask {
  STubeMeasure measure;

  public EndTask(STubeMeasure measure) {
    this.measure = measure;
  }

  /**
   * run
   */
  public void run() {
    if (!measure.canceled) {
      try {
        measure.balance.tare();
        measure.balance.close(); //天秤との通信を終了する
        int question = JOptionPane.showConfirmDialog(measure.pframe,
            "この結果でよろしいですか？", "結果の確認", JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE); //結果を保存するか否かの確認
        measure.pframe.setButtonEnabled(true); //フレームのボタンを使用可能にする
        if (question == JOptionPane.YES_OPTION) {
          measure.sample.calcStats(); //統計値の計算
          measure.pframe.papp.addSample(measure.sample); //サンプルをリストに追加
        }
        else {
          measure.pframe.showSample(); //前のサンプルを表示
        }
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }
}
