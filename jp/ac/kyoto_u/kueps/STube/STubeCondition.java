package jp.ac.kyoto_u.kueps.STube;

import java.io.*;

import javax.swing.*;

/**
 * <p>Title: S-Tube</p>
 * <p>Description: Controlling settling tube for analyzing grain-size</p>
 * <p>Copyright: Copyright (C) 2003 NARUSE, Hajime All rights reserved.<br><br>  This program is free software; you can redistribute it and/or modify<br> it under the terms of the GNU General Public License as published by<br> the Free Software Foundation; either version 2 of the License, or<br> (at your option) any later version.<br><br>  This program is distributed in the hope that it will be useful,<br> but WITHOUT ANY WARRANTY; without even the implied warranty of<br> MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the<br> GNU General Public License for more details.<br><br>  You should have received a copy of the GNU General Public License<br> along with this program; if not, write to the Free Software<br> Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA<br></p>
 * <p>Company: Kyoto University</p>
 * @author NARUSE, Hajime
 * @version 1.0
 */

public class STubeCondition
    implements Serializable {
  /**粒子比重*/
  protected double rhos = 2.65;

  /**計測する最小のPhi値*/
  protected double phimin = -1.0;

  /**計測する最大のPhi値*/
  protected double phimax = 2.5;

  /**計測するPhi値の間隔*/
  protected double dphi = 0.1;

  /**計測時間(sec)*/
  protected double minterval = 500.0;

  /**計測時間間隔*/
  protected double dm = 1.0;

  /**水温(degree)*/
  protected double wtemp = 20.0;

  /**沈降距離(cm)*/
  protected double wdepth = 150.0;

  /**重力加速度(cm/s^2)*/
  static protected final double G = 980.665;

  /**水の密度*/
  protected double rhof;

  /**水の粘性*/
  protected double mu;

  /**計測時刻*/
  protected double[] mtime;

  /**一定時間間隔で計測を行うか否か*/
  protected boolean intervalmode = false;

  /**沈降速度の計算にGibbs et al. (1971)の式を用いるか*/
  protected boolean gibbs = false;

  /**統計値の算出にphiスケールを用いるか否か*/
  protected boolean phiscale = true;

  /**デフォルトコンストラクタ*/
  public STubeCondition() {
    calcCondition();
  }

  /**
   * 従属変数を算出する
   */
  public void calcCondition() {
    rhof = 1.0001576 + 8.257e-6 * wtemp - 0.000005306 * wtemp * wtemp;
    mu = (0.00171 - 0.00004547 * wtemp + 0.0000005028 * wtemp * wtemp) * 10;
    if (!intervalmode) {
      mtime = new double[ (int) Math.floor( (phimax - phimin) / dphi) + 1];

      //沈降速度を別スレッドで計算
      Thread calc = new Thread() {
        double phi = phimin;
        public void run() {

          //進行状況を表示する
          ProgressMonitor pmon = new ProgressMonitor(null, "沈降速度を計算中...", "", 0,
              mtime.length);

          //計算処理
          for (int i = 0; i < mtime.length; i++) {
            mtime[i] = getSettlingTime(phi);
            phi += dphi;
            pmon.setProgress(i);
          }

          //進行状況ダイアログを閉じる
          pmon.close();

          //計算結果を標準出力に表示
          for (int i = 0; i < mtime.length; i++) {
            System.out.println(mtime[i]);
          }
        }
      };
      calc.start();

    }
    else {
      mtime = new double[ (int) Math.floor(minterval / dm) + 1];
      for (int i = 0; i < mtime.length; i++) {
        mtime[i] = (double) i * dm;
      }
    }

  }

  /**
   * Gibbs et al. (1971) の経験式を用いて沈降速度の計算を行う
   * @param d double
   * @return double
   */
  protected double getGibbsTVelocity(double d) {
    double r = d / 2.0;
    double nu = mu / rhof;
    double tv = ( -3.0 * nu +
                 Math.pow(9.0 * Math.pow(nu, 2.0) +
                          G * Math.pow(r, 2.0) * rhof * (rhos - rhof) *
                          (0.015476 + 0.19841 * r), 0.5)) /
        (rhof * (0.011607 + 0.14881 * r));
    return tv;
  }

  /**
   * 粒径（phi）から沈降時間を計算する
   * @param phi double
   * @return double
   */
  protected double getSettlingTime(double phi) {
    /**粒径（cm）*/
    double d = Math.pow(2.0, -phi) / 10;

    /**Gibbsの終端速度で求める*/
    if (gibbs) {
      double tvel;
      tvel = getGibbsTVelocity(d);
      return wdepth / tvel;
    }

    //台形差分法で沈降時間を計算
    double dt = 0.000001;
    double time = 0.0;
    double v = 0.000001;
    double v_old = v * 0.1;
    double y = 0.0;
    double re = 0.0;
    double cd = 0.0;
    double dv = 0.0;
    //加速過程を計算．速度が0.0001%しか変わっていなければ終端速度に達したと判定
    while (y < wdepth && (v - v_old) / v_old > 0.000001) {
      v_old = v;
      re = (rhof * v * d) / mu;
      cd = getCd(re);
      dv = ( (rhos - rhof) * G / rhos -
                   (3 * rhof * cd * Math.pow(v, 2.)) / (4 * rhos * d));
      y = y + (v + 1. / 2. * dv * dt) * dt;
      v = v + dv * dt;
      time = time + dt;
    }
    //終端速度に達してからの沈降時間を計算
    if (y < wdepth) {
      time += (wdepth - y) / v;
    }
    return time;

  }

  /**
   * 抵抗係数Cdをレイノルズ数reから算出する
   * @param re double
   * @return double
   */
  private final double getCd(double re) {
    double cd = 0;

    //ストークス則
    if (re < 0.9) {
      cd = 24. / re;
    }
    //シラーとナウマンの式
    else if (re < 800.) {
      cd = 24 / re * (1. + 0.15 * Math.pow(re, 0.687));
    }
    else {
      cd = 0.4;
    }

    return cd;
  }

  /**
   * 測定Conditionを文字列化する
   * @return String
   */
  public String toString() {
    StringWriter condition = new StringWriter();
    PrintWriter pr = new PrintWriter(condition);

    /**粒子比重*/
    pr.println("粒子比重: " + "\t" + Double.toString(rhos));

    /**計測する最小のPhi値*/
    pr.println("計測する最小のPhi値: " + "\t" + Double.toString(phimin));

    /**計測する最大のPhi値*/
    pr.println("計測する最大のPhi値: " + "\t" + Double.toString(phimax));

    /**計測するPhi値の間隔*/
    pr.println("計測するPhi値の間隔: " + "\t" + Double.toString(dphi));

    /**計測時間(sec)*/
    pr.println("計測時間(sec): " + "\t" + Double.toString(minterval));

    /**計測時間間隔*/
    pr.println("計測時間間隔: " + "\t" + Double.toString(dm));

    /**水温(degree)*/
    pr.println("水温: " + "\t" + Double.toString(wtemp));

    /**沈降距離(cm)*/
    pr.println("沈降距離(cm): " + "\t" + Double.toString(wdepth));

    /**重力加速度(cm/s^2)*/
    pr.println("重力加速度(cm/s^2): " + "\t" + Double.toString(G));

    /**計測時刻*/
    pr.print("計測時刻: ");
    for (int i = 0; i < mtime.length; i++) {
      pr.print(Double.toString(mtime[i]) + "\t");
    }
    pr.println();

    /**一定時間間隔で計測を行うか否か*/
    pr.println("一定時間間隔で計測を行うか否か: " + "\t" + Boolean.toString(intervalmode));

    /**沈降速度の計算にGibbs et al. (1971)の式を用いるか*/
    pr.println("沈降速度の計算にGibbs(1971)の式を用いるか: " + "\t" + Boolean.toString(gibbs));

    pr.println("統計値の計算にPhiScaleを用いるか: " + "\t" + Boolean.toString(phiscale));
    return condition.toString();
  }

}
