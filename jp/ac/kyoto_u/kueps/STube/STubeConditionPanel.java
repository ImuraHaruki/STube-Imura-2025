package jp.ac.kyoto_u.kueps.STube;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * <p>Title: S-Tube</p>
 * <p>Description: Controlling settling tube for analyzing grain-size</p>
 * <p>Copyright: Copyright (C) 2003 NARUSE, Hajime All rights reserved.<br><br>  This program is free software; you can redistribute it and/or modify<br> it under the terms of the GNU General Public License as published by<br> the Free Software Foundation; either version 2 of the License, or<br> (at your option) any later version.<br><br>  This program is distributed in the hope that it will be useful,<br> but WITHOUT ANY WARRANTY; without even the implied warranty of<br> MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the<br> GNU General Public License for more details.<br><br>  You should have received a copy of the GNU General Public License<br> along with this program; if not, write to the Free Software<br> Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA<br></p>
 * <p>Company: Kyoto University</p>
 * @author NARUSE, Hajime
 * @version 1.0
 */

public class STubeConditionPanel
    extends JPanel {

  STubeCondition condition;

  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JLabel label_rhos = new JLabel();
  JLabel label_wtemp = new JLabel();
  JLabel label_wdepth = new JLabel();
  JSpinner jspinner_rhos = new JSpinner(new SpinnerNumberModel(2.65, 0.01, 9.99,
      0.01));
  JSpinner jspinner_wtemp = new JSpinner(new SpinnerNumberModel(20.0, 0.0, 99.9,
      0.1));
  JSpinner jspinner_wdepth = new JSpinner(new SpinnerNumberModel(180.0, 0.1,
      999.0, 0.1));
  JLabel label_phi = new JLabel();
  JLabel label_dphi = new JLabel();
  JLabel label_minterval = new JLabel();
  JSpinner jspinner_phimin = new JSpinner(new SpinnerNumberModel(0.0, -4.0,
      10.0, 0.1));
  JSpinner jspinner_dphi = new JSpinner(new SpinnerNumberModel(0.1, 0.01, 10.0,
      0.01));
  JSpinner jspinner_minterval = new JSpinner(new SpinnerNumberModel(120, 1,
      144000, 1));
  JLabel label_wline = new JLabel();
  JLabel label_dm = new JLabel();
  JSpinner jspinner_phimax = new JSpinner(new SpinnerNumberModel(0.0, -4.0,
      10.0, 0.1));
  JSpinner jspinner_dm = new JSpinner(new SpinnerNumberModel(1.0, 0.5, 100.0,
      1.0));
  JCheckBox jcheckbox_intervalmode = new JCheckBox();
  JCheckBox jcheckbox_gibbs = new JCheckBox();
  JCheckBox jcheckbox_phiscale = new JCheckBox();

  /**
   * コンストラクタ
   */

  public STubeConditionPanel() {
    super();
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    setLayout(gridBagLayout1);
    label_rhos.setText("粒子の密度(g/cm3)：");
    label_wtemp.setText("水温(℃)：");
    label_wdepth.setText("沈降距離 (cm) ：");
    label_phi.setText("計測区間 (phi) ：");
    label_dphi.setText("計測間隔 (phi) ：");
    label_minterval.setText("計測時間 (sec.) ：");
    label_wline.setFont(new java.awt.Font("Dialog", 0, 16));
    label_wline.setText("~");
    label_dm.setText("計測間隔 (sec.) ：");
    jcheckbox_intervalmode.setToolTipText("一定時間間隔で計測を行う");
    jcheckbox_intervalmode.setText("Interval-Mode");
    jcheckbox_intervalmode.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        jcheckbox_intervalmode_itemStateChanged(e);
      }
    });
    jcheckbox_gibbs.setText("Use Gibbs\' Formula");
    jspinner_phimin.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        jspinner_phimin_stateChanged(e);
      }
    });
    jspinner_phimax.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        jspinner_phimax_stateChanged(e);
      }
    });
    jcheckbox_phiscale.setToolTipText("統計値の算出などにphiスケールを用いる");
    jcheckbox_phiscale.setText("Use phi-scale");
    add(label_rhos, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                                           , GridBagConstraints.EAST,
                                           GridBagConstraints.NONE,
                                           new Insets(10, 10, 5, 0), 0, 0));
    add(label_wtemp, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.EAST,
                                            GridBagConstraints.NONE,
                                            new Insets(5, 10, 5, 0), 0, 0));
    add(label_wdepth, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
                                             , GridBagConstraints.EAST,
                                             GridBagConstraints.NONE,
                                             new Insets(5, 10, 5, 0), 0, 0));
    add(jspinner_rhos, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
                                              , GridBagConstraints.WEST,
                                              GridBagConstraints.NONE,
                                              new Insets(10, 0, 5, 10), 0, 0));
    add(jspinner_wtemp, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
                                               , GridBagConstraints.WEST,
                                               GridBagConstraints.NONE,
                                               new Insets(5, 0, 5, 10), 0, 0));
    add(jspinner_wdepth, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
                                                , GridBagConstraints.WEST,
                                                GridBagConstraints.NONE,
                                                new Insets(5, 0, 5, 10), 0, 0));
    add(label_phi, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
                                          , GridBagConstraints.EAST,
                                          GridBagConstraints.NONE,
                                          new Insets(10, 10, 5, 0), 0, 0));
    add(label_dphi, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
                                           , GridBagConstraints.EAST,
                                           GridBagConstraints.NONE,
                                           new Insets(5, 10, 5, 0), 0, 0));
    add(label_minterval, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
                                                , GridBagConstraints.EAST,
                                                GridBagConstraints.NONE,
                                                new Insets(5, 10, 5, 0), 0, 0));
    add(jspinner_phimin, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
                                                , GridBagConstraints.EAST,
                                                GridBagConstraints.NONE,
                                                new Insets(10, 0, 5, 0), 0, 0));
    add(jspinner_dphi, new GridBagConstraints(3, 1, 3, 1, 0.0, 0.0
                                              , GridBagConstraints.WEST,
                                              GridBagConstraints.NONE,
                                              new Insets(5, 0, 5, 10), 0, 0));
    add(jspinner_minterval, new GridBagConstraints(3, 2, 3, 1, 0.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.NONE,
        new Insets(5, 0, 5, 10), 0, 0));
    add(label_wline, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
                                            , GridBagConstraints.CENTER,
                                            GridBagConstraints.NONE,
                                            new Insets(10, 0, 5, 0), 0, 0));
    add(jspinner_phimax, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0
                                                , GridBagConstraints.WEST,
                                                GridBagConstraints.NONE,
                                                new Insets(10, 0, 5, 10), 0, 0));
    add(label_dm, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
                                         , GridBagConstraints.EAST,
                                         GridBagConstraints.NONE,
                                         new Insets(5, 10, 5, 0), 0, 0));
    add(jspinner_dm, new GridBagConstraints(3, 3, 3, 1, 0.0, 0.0
                                            , GridBagConstraints.WEST,
                                            GridBagConstraints.NONE,
                                            new Insets(5, 0, 5, 10), 0, 0));
    add(jcheckbox_gibbs, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
                                                , GridBagConstraints.WEST,
                                                GridBagConstraints.NONE,
                                                new Insets(0, 20, 10, 5), 0, 0));
    this.add(jcheckbox_intervalmode,
             new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0
                                    , GridBagConstraints.CENTER,
                                    GridBagConstraints.NONE,
                                    new Insets(5, 10, 10, 5), 0, 0));
    this.add(jcheckbox_phiscale, new GridBagConstraints(3, 4, 3, 1, 0.0, 0.0
        , GridBagConstraints.WEST, GridBagConstraints.NONE,
        new Insets(5, 5, 10, 10), 0, 0));
  }

  /**
   * 初期条件設定
   *
   * @param cond STubeCondition
   */
  public void setCondition(STubeCondition cond) {
    condition = cond;
    jspinner_rhos.setValue(new Double(cond.rhos));
    jspinner_wtemp.setValue(new Double(cond.wtemp));
    jspinner_wdepth.setValue(new Double(cond.wdepth));
    jspinner_phimin.setValue(new Double(cond.phimin));
    jspinner_dphi.setValue(new Double(cond.dphi));
    jspinner_minterval.setValue(new Double(cond.minterval));
    jspinner_phimax.setValue(new Double(cond.phimax));
    jspinner_dm.setValue(new Double(cond.dm));
    jcheckbox_intervalmode.setSelected(cond.intervalmode);
    jcheckbox_gibbs.setSelected(cond.gibbs);
    jcheckbox_phiscale.setSelected(cond.phiscale);

    boolean intervalmode = jcheckbox_intervalmode.isSelected();
    label_phi.setEnabled(!intervalmode);
    jspinner_phimin.setEnabled(!intervalmode);
    jspinner_phimax.setEnabled(!intervalmode);
    label_dphi.setEnabled(!intervalmode);
    jspinner_dphi.setEnabled(!intervalmode);
    label_minterval.setEnabled(intervalmode);
    jspinner_minterval.setEnabled(intervalmode);
    label_dm.setEnabled(intervalmode);
    jspinner_dm.setEnabled(intervalmode);

  }

  /**
   * 沈降管状態設定の取得
   * @return STubeCondition
   */
  public STubeCondition getCondition() {

    condition.rhos = ( (Double) jspinner_rhos.getValue()).doubleValue();
    condition.wtemp = ( (Double) jspinner_wtemp.getValue()).doubleValue();
    condition.wdepth = ( (Double) jspinner_wdepth.getValue()).doubleValue();
    condition.phimin = ( (Double) jspinner_phimin.getValue()).doubleValue();
    condition.dphi = ( (Double) jspinner_dphi.getValue()).doubleValue();
    condition.minterval = ( (Double) jspinner_minterval.getValue()).doubleValue();
    condition.phimax = ( (Double) jspinner_phimax.getValue()).doubleValue();
    condition.dm = ( (Double) jspinner_dm.getValue()).doubleValue();
    condition.intervalmode = jcheckbox_intervalmode.isSelected();
    condition.gibbs = jcheckbox_gibbs.isSelected();
    condition.phiscale = jcheckbox_phiscale.isSelected();

    condition.calcCondition();

    return condition;
  }

  /**
   * phi-modeの選択状態に合わせてダイアログを変化させる
   * @param e ItemEvent
   */
  void jcheckbox_intervalmode_itemStateChanged(ItemEvent e) {
    boolean intervalmode = jcheckbox_intervalmode.isSelected();
    label_phi.setEnabled(!intervalmode);
    jspinner_phimin.setEnabled(!intervalmode);
    jspinner_phimax.setEnabled(!intervalmode);
    label_dphi.setEnabled(!intervalmode);
    jspinner_dphi.setEnabled(!intervalmode);
    label_minterval.setEnabled(intervalmode);
    jspinner_minterval.setEnabled(intervalmode);
    label_dm.setEnabled(intervalmode);
    jspinner_dm.setEnabled(intervalmode);
  }

  void jspinner_phimin_stateChanged(ChangeEvent e) {
    setSpinnerMinMax();
  }

  void setSpinnerMinMax() {
    ( (SpinnerNumberModel) jspinner_phimax.getModel()).setMinimum( (Double)
        jspinner_phimin.getValue());
    ( (SpinnerNumberModel) jspinner_phimin.getModel()).setMaximum( (Double)
        jspinner_phimax.getValue());
  }

  void jspinner_phimax_stateChanged(ChangeEvent e) {
    setSpinnerMinMax();
  }

}
