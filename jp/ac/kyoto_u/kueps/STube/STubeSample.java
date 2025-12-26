package jp.ac.kyoto_u.kueps.STube;

import java.io.*;
import java.util.*;

/**
 * <p>Title: STubeSample</p>
 * <p>Description: Controlling settling tube for analyzing grain-size</p>
 * <p>Copyright: Copyright (C) 2003 NARUSE, Hajime All rights reserved.<br><br>  This program is free software; you can redistribute it and/or modify<br> it under the terms of the GNU General Public License as published by<br> the Free Software Foundation; either version 2 of the License, or<br> (at your option) any later version.<br><br>  This program is distributed in the hope that it will be useful,<br> but WITHOUT ANY WARRANTY; without even the implied warranty of<br> MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the<br> GNU General Public License for more details.<br><br>  You should have received a copy of the GNU General Public License<br> along with this program; if not, write to the Free Software<br> Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA<br></p>
 * <p>Company: Kyoto University</p>
 * @author NARUSE, Hajime
 * @version 1.0
 */

public class STubeSample
    implements Serializable, Comparable {
  protected STubeCondition condition;
  protected SampleMeasuredData[] mdata = new SampleMeasuredData[0];
  protected double total_weight = 0.0;
  protected double mean = 0.0;
  protected double sorting = 0.0;
  protected double skewness = 0.0;
  protected double kurtosis = 0.0;
  protected double mode = 0.0;
  protected double fivep = 0.0;
  protected double twentyfivep = 0.0;
  protected double median = 0.0;
  protected double seventyfivep = 0.0;
  protected double ninetyfivep = 0.0;
  protected double geoqtiledev = 0.0;

  protected String name = "Sample";
  protected Date mdate = new Date();

  public STubeSample() {
  }

  /**
   * 測定状況および結果を記録する
   *
   * @param weight double
   * @param time double
   * @param phi double
   */
  synchronized public void addData(double weight, double time, double phi) {
    SampleMeasuredData lastdatum = new SampleMeasuredData();
    lastdatum.weight = weight;
    lastdatum.time = time;
    lastdatum.phi = phi;
    lastdatum.dweight = mdata.length == 0 ? weight :
        weight - mdata[mdata.length - 1].weight;
    lastdatum.dweight = lastdatum.dweight < 0 ? 0 : lastdatum.dweight;

    ArrayList datalist = new ArrayList(Arrays.asList(mdata));
    datalist.add(lastdatum);
    mdata = (SampleMeasuredData[]) datalist.toArray(mdata);
  }

  /**
   * 各種統計値を算出する
   */
  protected void calcStats() {
    for (int i = 0; i < mdata.length; i++) {
      total_weight = total_weight < mdata[i].weight ? mdata[i].weight :
          total_weight;
    }
    if (!condition.intervalmode) {
      condition.minterval = mdata[mdata.length - 1].time;
      mean = getMean();
      sorting = getSorting();
      skewness = getSkewness();
      kurtosis = getKurtosis();
      mode = getMode();
      fivep = getPercentile(5.0);
      twentyfivep = getPercentile(25.0);
      median = getPercentile(50.0);
      seventyfivep = getPercentile(75.0);
      ninetyfivep = getPercentile(95.0);
      geoqtiledev = getGeoQtileDev(twentyfivep, seventyfivep);
    }
  }

  protected double getMean() {
    double mean = 0.0;
    for (int i = 0; i < mdata.length; i++) {
      mean += (mdata[i].phi - condition.dphi / 2.0) * mdata[i].dweight;
    }
    mean /= total_weight;
    if (condition.phiscale) {
      return mean;
    }
    else {
      return Math.pow(2.0, -mean);
    }
  }

  protected double getSorting() {
    double sorting = 0.0;
    for (int i = 0; i < mdata.length; i++) {
      sorting += Math.pow( (mdata[i].phi - condition.dphi / 2.0) - mean, 2.0) *
          mdata[i].dweight;
    }
    sorting /= total_weight;
    sorting = Math.pow(sorting, 0.5);
    if (condition.phiscale) {
      return sorting;
    }
    else {
      return Math.pow(2.0, -sorting);
    }
  }

  protected double getSkewness() {
    double skewness = 0.0;
    for (int i = 0; i < mdata.length; i++) {
      skewness += Math.pow( (mdata[i].phi - condition.dphi / 2.0) - mean, 3.0) *
          mdata[i].dweight;
    }
    skewness /= total_weight * Math.pow(sorting, 3.0);
    if (condition.phiscale) {
      return skewness;
    }
    else {
      return Math.pow(2.0, -skewness);
    }

  }

  protected double getKurtosis() {
    double kurtosis = 0.0;
    for (int i = 0; i < mdata.length; i++) {
      kurtosis += Math.pow( (mdata[i].phi - condition.dphi / 2.0) - mean, 4.0) *
          mdata[i].dweight;
    }
    kurtosis /= total_weight * Math.pow(sorting, 4.0);
    if (condition.phiscale) {
      return kurtosis;
    }
    else {
      return Math.pow(2.0, -kurtosis);
    }

  }

  /**
   * Mode値を取得する
   * @return double
   */
  protected double getMode() {
    double mode = 0.0;
    double next_class_weight = 0.0;
    double prev_class_weight = 0.0;
    double max_weight = 0.0;
    for (int i = 0; i < mdata.length; i++) {

      //これまでの最大値を今回の階級値が上回った場合，その階級値および前後の階級値を保存する
      if (max_weight < mdata[i].dweight) {
        max_weight = mdata[i].dweight;
        next_class_weight = i + 1 < mdata.length ? mdata[i + 1].dweight : 0.0;
        prev_class_weight = i > 0 ? mdata[i - 1].dweight : 0.0;
        mode = mdata[i].phi - condition.dphi / 2.0;
      }
    }

    //前後の階級値の割合からModeを計算する
    mode = mode - condition.dphi +
        (max_weight - prev_class_weight) /
        (2 * max_weight - prev_class_weight - next_class_weight) *
        condition.dphi;

    if (condition.phiscale) {
      return mode;
    }
    else {
      return Math.pow(2.0, -mode);
    }
  }

  /**
   * パーセンタイル点を求める
   * @param p double
   * @return double
   */
  protected double getPercentile(double p) {
    double ptile = 0.0;

    ptile = 0.0;
    double ptilevalue = total_weight * p / 100.;
    int ptile_index = 0;
    double prev_value = 0.0;
    double next_value = 0.0;
    if (ptilevalue < mdata[0].weight) {
      prev_value = 0.0;
      next_value = mdata[0].weight;
    }
    else {
      for (int i = 0; i < mdata.length; i++) {
        if (mdata[i].weight < ptilevalue) {
          ptile_index = i;
        }
      }

      prev_value = mdata[ptile_index].weight;
      next_value = ptile_index + 1 < mdata.length ?
          mdata[ptile_index + 1].weight : prev_value;

    }
    if (prev_value != next_value) {
      ptile = condition.dphi / (next_value - prev_value) *
          (ptilevalue - prev_value) + mdata[ptile_index].phi;
    }
    else {
      ptile = mdata[ptile_index].phi;
    }
    ptile -= condition.dphi / 2.0;

    if (condition.phiscale) {
      return ptile;
    }
    else {
      return Math.pow(2.0, -ptile);
    }
  }

  /**
   * 幾何四分位偏差を求める
   * @param tfive double
   * @param sfive double
   * @return double
   */
  protected double getGeoQtileDev(double tfive, double sfive) {
    double tfivevalue = Math.pow(2., -1. * tfive);
    double sfivevalue = Math.pow(2., -1 * sfive);
    double gqd = Math.sqrt(tfivevalue / sfivevalue);
    return gqd;
  }

  /**
   * サンプル名を設定する
   * @param name String
   */
  public void setSampleName(String name) {
    this.name = name;
  }

  /**
   * compareTo
   *
   * @param o Object
   * @return int
   */
  public int compareTo(Object o) {
    if (o instanceof STubeSample) {
      String samplename = ( (STubeSample) o).name;
      return name.compareTo(samplename);
    }
    else {
      return 0;
    }
  }

}

/**
 * サンプルの計測値の生データを記録するためのクラス
 */
class SampleMeasuredData
    implements Serializable {
  public double time = 0.0;
  public double weight = 0.0;
  public double phi = 0.0;
  public double dweight = 0.0;

  public SampleMeasuredData() {
  }
}
