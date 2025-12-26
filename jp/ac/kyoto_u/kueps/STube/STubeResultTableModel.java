package jp.ac.kyoto_u.kueps.STube;

import java.io.*;
import java.text.*;
import java.util.*;

import javax.swing.table.*;

/**
 * <p>Title: STubeResultTableModel</p>
 * <p>Description: 結果を表示するテーブルモデル</p>
 * <p>Copyright: Copyright (C) 2003 NARUSE, Hajime All rights reserved.<br><br>  This program is free software; you can redistribute it and/or modify<br> it under the terms of the GNU General Public License as published by<br> the Free Software Foundation; either version 2 of the License, or<br> (at your option) any later version.<br><br>  This program is distributed in the hope that it will be useful,<br> but WITHOUT ANY WARRANTY; without even the implied warranty of<br> MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the<br> GNU General Public License for more details.<br><br>  You should have received a copy of the GNU General Public License<br> along with this program; if not, write to the Free Software<br> Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA<br></p>
 * <p>Company: Kyoto University</p>
 * @author NARUSE, Hajime
 * @version 1.0
 */

public class STubeResultTableModel
    extends AbstractTableModel {
  /**表示するサンプルのリスト*/
  ArrayList samples = new ArrayList();

  /**表示するテーブルのデータ*/
  ArrayList tabledata = new ArrayList();

  /**テーブルのヘッダ*/
  ArrayList columnname = new ArrayList();

  /**表示にかかわるオプション*/
  STubeOption option = new STubeOption();

  int maxclassnum = 0;

  NumberFormat nf = NumberFormat.getInstance();

  public STubeResultTableModel() {
    nf.setMaximumFractionDigits(4);
  }

  public int getRowCount() {
    int rowcount = tabledata.size();
    return rowcount;
  }

  public int getColumnCount() {
    return columnname.size();
  }

  public Object getValueAt(int rowIndex, int columnIndex) {
    ArrayList row = (ArrayList) tabledata.get(rowIndex);
    if (columnIndex < row.size()) {
      return row.get(columnIndex);
    }
    else {
      return "";
    }
  }

  public void setSamples(ArrayList samplelist) {
    samples = samplelist;
    updateModel();
  }

  /**
   * このテーブルの内容を更新する
   */
  public void updateModel() {
    tabledata.clear(); //これまでのデータを消去

    updateColumnName(); //カラムの名前を更新

    //階級数をカウントしておく
    if(samples.size()>0){
      maxclassnum = ( (STubeSample) samples.get(0)).mdata.length;
    }

    //それぞれのサンプルのデータを格納する
    Iterator it = samples.iterator();
    while (it.hasNext()) {
      STubeSample sample = (STubeSample) it.next();
      ArrayList row = new ArrayList(); //それぞれの行をあらわすArrayList
      row.add(sample.name);
      row.add(DateFormat.getDateInstance().format(sample.mdate));
      row.add(DateFormat.getTimeInstance().format(sample.mdate));
      if (option.output_condition) {
        row.add(nf.format(sample.condition.wdepth));
        row.add(nf.format(sample.condition.wtemp));
        row.add(nf.format(sample.condition.rhos));
        if (sample.condition.intervalmode) {
          row.add("Interval");
        }
        else {
          row.add("Normal");
        }
        row.add(nf.format(sample.condition.minterval));
        if (!sample.condition.intervalmode) {
          row.add("???");
        }
        else {
          row.add(nf.format(sample.condition.dm));
        }
        row.add(nf.format(sample.condition.phimin) + "~" +
                nf.format(sample.condition.phimax));
        row.add(nf.format(sample.condition.dphi));
      }

      if (option.output_stats) { //統計値を格納する
        row.add(nf.format(sample.total_weight));
        row.add(nf.format(sample.mean));
        row.add(nf.format(sample.sorting));
        row.add(nf.format(sample.skewness));
        row.add(nf.format(sample.kurtosis));
        row.add(nf.format(sample.mode));
        row.add(nf.format(sample.fivep));
        row.add(nf.format(sample.twentyfivep));
        row.add(nf.format(sample.median));
        row.add(nf.format(sample.seventyfivep));
        row.add(nf.format(sample.ninetyfivep));
        row.add(nf.format(sample.geoqtiledev));
      }

      if (option.output_class) { //各階級データを格納する
        for (int i = 0; i < sample.mdata.length; i++) {
          row.add(nf.format(sample.mdata[i].dweight));
        }
      }
      if (option.output_rawdata) { //生データを格納する
        for (int i = 0; i < sample.mdata.length; i++) {
          row.add(nf.format(sample.mdata[i].weight));
        }
      }

    //テーブルにこの行を付け加える
    tabledata.add(row);
  }

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

/**
 * Returns the name of the column at <code>columnIndex</code>.
 *
 * @param columnIndex the index of the column
 * @return the name of the column
 */
public String getColumnName(int columnIndex) {
  return columnname.get(columnIndex).toString();
}

public void updateColumnName() {
  columnname = new ArrayList();

  columnname.add("サンプル名");
  columnname.add("計測日");
  columnname.add("時刻");

  if (option.output_condition) {//測定条件を出力する場合
    columnname.add("沈降距離(cm)");
    columnname.add("水温(℃)");
    columnname.add("粒子密度(g/cm3)");
    columnname.add("測定モード");
    columnname.add("計測時間(sec.)");
    columnname.add("計測間隔(sec.)");
    columnname.add("計測区間(phi)");
    columnname.add("計測間隔(phi)");
  }
  if (option.output_stats) {
    columnname.add("総重量");
    columnname.add("Mean");
    columnname.add("Sorting");
    columnname.add("Skewness");
    columnname.add("Kurtosis");
    columnname.add("Mode");
    columnname.add("5%");
    columnname.add("25%");
    columnname.add("Median");
    columnname.add("75%");
    columnname.add("95%");
    columnname.add("GeoQtDev.");
  }
  if (option.output_class && samples.size() > 0) {//階級の名前を表示する
    STubeSample sample = (STubeSample) samples.get(0);
    if(sample.condition.intervalmode){
      for (int i = 0; i < maxclassnum; i++) {
        columnname.add("Class" + Integer.toString(i));
      }
    }
    else{
      double classnum = sample.condition.phimin;
      for(int i = 0; i < maxclassnum; i++){
        columnname.add(nf.format(classnum)+"phi");
        classnum += sample.condition.dphi;
      }
    }
  }
  if (option.output_rawdata) {
    for (int i = 0; i < maxclassnum; i++) {
      columnname.add("RawData" + Integer.toString(i));
    }
  }

}

public void setOption(STubeOption opt) {
  option = opt;
  updateModel();
}

}
