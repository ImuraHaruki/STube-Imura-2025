package jp.ac.kyoto_u.kueps.STube;

import java.io.*;
import java.text.*;
import java.util.*;

import javax.swing.table.*;

/**
 * <p>Title: STubeErrorTableModel</p>
 * <p>Description: 誤差解析の結果を表示するためのテーブル</p>
 * <p>Copyright: Copyright (C) 2003 NARUSE, Hajime All rights reserved.<br><br>  This program is free software; you can redistribute it and/or modify<br> it under the terms of the GNU General Public License as published by<br> the Free Software Foundation; either version 2 of the License, or<br> (at your option) any later version.<br><br>  This program is distributed in the hope that it will be useful,<br> but WITHOUT ANY WARRANTY; without even the implied warranty of<br> MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the<br> GNU General Public License for more details.<br><br>  You should have received a copy of the GNU General Public License<br> along with this program; if not, write to the Free Software<br> Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA<br></p>
 * <p>Company: Kyoto University</p>
 * @author NARUSE, Hajime
 * @version 1.0
 */

public class STubeErrorTableModel
    extends AbstractTableModel {
  /**表示するサンプルのマップ*/
  STubeSample[] samples = new STubeSample[0];

  /**表示するテーブルのデータ*/
  ArrayList tabledata = new ArrayList();

  /**表示するテーブルのヘッダ*/
  ArrayList columnname = new ArrayList();

  /**表示にかかわるオプション*/
  STubeOption option = new STubeOption();

  /**最大の階級数*/
  int maxclassnum = 0;

  /**ナンバーフォーマット*/
  NumberFormat nf = NumberFormat.getInstance();

  public STubeErrorTableModel() {
  }

  public int getRowCount() {
    return tabledata.size();
  }

  public int getColumnCount() {
    return columnname.size();
  }

  public Object getValueAt(int rowIndex, int columnIndex) {
    return ( (ArrayList) tabledata.get(rowIndex)).get(columnIndex);
  }

  public void updateModel() {
    tabledata.clear();

    updateColumnName();

    //階級数をカウントしておく
    if (samples.length > 0) {
      maxclassnum = samples[0].mdata.length;
    }

    for (int i = 0; i < samples.length; i++) {

      //同じ名前で階級数も同じサンプルを取り出す
      String samplename = samples[i].name;
      int classnum = samples[i].mdata.length;
      ArrayList target = new ArrayList();
      target.add(samples[i]);
      while (i < samples.length - 1 && samplename.equals(samples[i + 1].name) &&
             classnum == samples[i + 1].mdata.length) {
        i++;
        target.add(samples[i]);
      }

      double[] values = new double[target.size()];
      ArrayList row = new ArrayList();
      row.add(samplename);
      row.add(Integer.toString(target.size()));

      for (int j = 0; j < values.length; j++) {
        values[j] = ( (STubeSample) target.get(j)).total_weight;
      }
      row.add(nf.format(getMean(values)));
      row.add(nf.format(getError(values)));

      for (int j = 0; j < values.length; j++) {
        values[j] = ( (STubeSample) target.get(j)).mean;
      }
      row.add(nf.format(getMean(values)));
      row.add(nf.format(getError(values)));

      for (int j = 0; j < values.length; j++) {
        values[j] = ( (STubeSample) target.get(j)).sorting;
      }
      row.add(nf.format(getMean(values)));
      row.add(nf.format(getError(values)));

      for (int j = 0; j < values.length; j++) {
        values[j] = ( (STubeSample) target.get(j)).skewness;
      }
      row.add(nf.format(getMean(values)));
      row.add(nf.format(getError(values)));

      for (int j = 0; j < values.length; j++) {
        values[j] = ( (STubeSample) target.get(j)).kurtosis;
      }
      row.add(nf.format(getMean(values)));
      row.add(nf.format(getError(values)));

      for (int j = 0; j < values.length; j++) {
        values[j] = ( (STubeSample) target.get(j)).mode;
      }
      row.add(nf.format(getMean(values)));
      row.add(nf.format(getError(values)));

      for (int j = 0; j < values.length; j++) {
        values[j] = ( (STubeSample) target.get(j)).fivep;
      }
      row.add(nf.format(getMean(values)));
      row.add(nf.format(getError(values)));

      for (int j = 0; j < values.length; j++) {
        values[j] = ( (STubeSample) target.get(j)).twentyfivep;
      }
      row.add(nf.format(getMean(values)));
      row.add(nf.format(getError(values)));

      for (int j = 0; j < values.length; j++) {
        values[j] = ( (STubeSample) target.get(j)).median;
      }
      row.add(nf.format(getMean(values)));
      row.add(nf.format(getError(values)));

      for (int j = 0; j < values.length; j++) {
        values[j] = ( (STubeSample) target.get(j)).seventyfivep;
      }
      row.add(nf.format(getMean(values)));
      row.add(nf.format(getError(values)));

      for (int j = 0; j < values.length; j++) {
        values[j] = ( (STubeSample) target.get(j)).ninetyfivep;
      }
      row.add(nf.format(getMean(values)));
      row.add(nf.format(getError(values)));

      for (int j = 0; j < values.length; j++) {
        values[j] = ( (STubeSample) target.get(j)).geoqtiledev;
      }
      row.add(nf.format(getMean(values)));
      row.add(nf.format(getError(values)));

      double[][] classvalues = new double[classnum][target.size()];
      for (int k = 0; k < classnum; k++) {
        for (int j = 0; j < target.size(); j++) {
          STubeSample s = ( (STubeSample) target.get(j));
          classvalues[k][j] = s.mdata[k].dweight / s.total_weight * 100;
        }
      }
      for (int j = 0; j < classnum; j++) {
        row.add(nf.format(getMean(classvalues[j])));
      }
      for (int j = 0; j < classnum; j++) {
        row.add(nf.format(getError(classvalues[j])));
      }

      tabledata.add(row);
    }
  }

  public void setSamples(ArrayList samplelist) {
    ArrayList sorted_samples = new ArrayList(samplelist);
    Collections.sort(sorted_samples);
    samples = new STubeSample[samplelist.size()];
    samples = (STubeSample[]) sorted_samples.toArray(samples);
    updateModel();
  }

  public void setOption(STubeOption opt) {
    option = opt;
    updateModel();
  }

  /**
   * toString()のオーバーライド
   * @return String
   */
  public String toString() {
    StringWriter sw = new StringWriter();
    PrintWriter pr = new PrintWriter(sw);

    StringBuffer header = new StringBuffer();
    for (int i = 0; i < getColumnCount(); i++) {
      header.append(getColumnName(i) + "\t");
    }
    pr.println(header);

    Iterator it = tabledata.iterator();
    while (it.hasNext()) {
      StringBuffer line = new StringBuffer();
      ArrayList row = (ArrayList) it.next();
      Iterator rit = row.iterator();
      while (rit.hasNext()) {
        line.append(rit.next().toString() + "\t");
      }
      pr.println(line);
    }
    return sw.toString();
  }

  public String getColumnName(int cindex) {
    return columnname.get(cindex).toString();
  }

  void updateColumnName() {
    columnname.clear();
    columnname.add("サンプル名");
    columnname.add("測定数");

    columnname.add("総重量");
    columnname.add("総重量(Err.)");
    columnname.add("Mean");
    columnname.add("Mean(Err.)");
    columnname.add("Sorting");
    columnname.add("Sorting(Err.)");
    columnname.add("Skewness");
    columnname.add("Skewness(Err.)");
    columnname.add("Kurtosis");
    columnname.add("Kurtosis(Err.)");
    columnname.add("Mode");
    columnname.add("Mode(Err.)");
    columnname.add("5%");
    columnname.add("5%(Err.)");
    columnname.add("25%");
    columnname.add("25%(Err.)");
    columnname.add("Median");
    columnname.add("Median");
    columnname.add("75%");
    columnname.add("75%(Err.)");
    columnname.add("95%");
    columnname.add("95%(Err.)");
    columnname.add("GeoQtDev.");
    columnname.add("GeoQtDev.(Err.)");

    if (option.output_class && samples.length > 0) { //階級の名前を表示する
      STubeSample sample = (STubeSample) samples[0];
      if (sample.condition.intervalmode) {
        for (int i = 0; i < maxclassnum; i++) {
          columnname.add("Class" + Integer.toString(i));
        }
        for (int i = 0; i < maxclassnum; i++) {
          columnname.add("Class(Err.)" + Integer.toString(i));
        }
      }
      else {
        double classnum = sample.condition.phimin;
        for (int i = 0; i < maxclassnum; i++) {
          columnname.add(nf.format(classnum) + "phi");
          classnum += sample.condition.dphi;
        }
        for (int i = 0; i < maxclassnum; i++) {
          columnname.add(nf.format(classnum) + "phi(Err.)");
        }
      }
    }

  }

  /**
   * dataの平均値を計算する
   * @param data double[]
   * @return double
   */
  protected double getMean(double[] data) {
    double mean = 0.0;
    for (int i = 0; i < data.length; i++) {
      mean += data[i];
    }
    mean /= data.length;
    return mean;
  }

  /**
   * 誤差を計算する
   * @param data double[]
   * @return double
   */
  protected double getError(double[] data) {
    if (data.length < 3) {
      return 0;
    }
    else {
      //平均値を計算
      double mean = getMean(data);
      //標本標準偏差を計算
      double sstdev = 0.0;
      for (int i = 0; i < data.length; i++) {
        sstdev = Math.pow(data[i] - mean, 2.0);
      }
      sstdev /= (data.length - 1);
      sstdev = Math.sqrt(sstdev);

      return sstdev;
    }
  }

}
