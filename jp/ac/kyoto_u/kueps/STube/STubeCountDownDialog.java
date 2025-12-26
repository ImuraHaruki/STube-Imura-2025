package jp.ac.kyoto_u.kueps.STube;

import java.applet.*;
import java.io.*;
import java.net.*;
import java.util.*;

import java.awt.*;
import javax.swing.*;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.*;
import javax.sound.sampled.*;

/**
 * <p>Title: S-Tube</p>
 * <p>Description: Controlling settling tube for analyzing grain-size</p>
 * <p>Copyright: Copyright (C) 2003 NARUSE, Hajime All rights reserved.<br><br>  This program is free software; you can redistribute it and/or modify<br> it under the terms of the GNU General Public License as published by<br> the Free Software Foundation; either version 2 of the License, or<br> (at your option) any later version.<br><br>  This program is distributed in the hope that it will be useful,<br> but WITHOUT ANY WARRANTY; without even the implied warranty of<br> MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the<br> GNU General Public License for more details.<br><br>  You should have received a copy of the GNU General Public License<br> along with this program; if not, write to the Free Software<br> Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA<br></p>
 * <p>Company: Kyoto University</p>
 * @author NARUSE, Hajime
 * @version 1.0
 */

public class STubeCountDownDialog
    extends JDialog {
  /**カウントする数*/
  int count = 10;

  /**カウント音*/
  Clip count_clip = null;

  /**最終カウント音*/
  Clip lastcount_clip = null;

  /**測定開始音*/
  Clip start_clip = null;

  /**デフォルトのカウント音*/
  URL count_sound = jp.ac.kyoto_u.kueps.STube.
      STubeCountDownDialog.class.
      getResource("count.wav");

  /**デフォルトの最終カウント音*/
  URL lastcount_sound = jp.ac.kyoto_u.kueps.STube.
      STubeCountDownDialog.class.
      getResource("count_final.wav");

  /**デフォルトの測定開始音*/
  URL start_sound = jp.ac.kyoto_u.kueps.STube.
      STubeCountDownDialog.class.
      getResource("start.wav");

  JPanel panel1 = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JLabel label_explanation = new JLabel();
  JLabel count_text = new JLabel();

  public STubeCountDownDialog(Frame frame, int count, String count_wav,
                              String lastcount_wav, String start_wav) {

    this(frame, count);

    try {
      File c_wav = new File(count_wav);
      if (c_wav.exists()) {
        count_sound = c_wav.toURL();
      }
      File l_wav = new File(lastcount_wav);
      if (l_wav.exists()) {
        lastcount_sound = l_wav.toURL();
      }
      File s_wav = new File(start_wav);
      if (s_wav.exists()) {
        start_sound = s_wav.toURL();
      }
    }
    catch (Exception ex) {
    }

  }

  public STubeCountDownDialog(Frame frame, int count) {
    this(frame, "計測カウントダウン", true);
    this.count = count;
    count_text.setText(Integer.toString(this.count));
  }

  public STubeCountDownDialog(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try {
      jbInit();
      pack();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public STubeCountDownDialog() {
    this(null, "", false);
  }

  private void jbInit() throws Exception {
    panel1.setLayout(borderLayout1);
    label_explanation.setFont(new java.awt.Font("Dialog", 0, 18));
    label_explanation.setHorizontalAlignment(SwingConstants.CENTER);
    label_explanation.setText("計測開始まであと");
    count_text.setFont(new java.awt.Font("Dialog", 1, 200));
    count_text.setForeground(Color.blue);
    count_text.setHorizontalAlignment(SwingConstants.CENTER);
    count_text.setText("10");
    panel1.setMaximumSize(new Dimension(300, 200));
    panel1.setMinimumSize(new Dimension(300, 200));
    panel1.setPreferredSize(new Dimension(300, 300));
    getContentPane().add(panel1);
    panel1.add(label_explanation, BorderLayout.NORTH);
    panel1.add(count_text, BorderLayout.CENTER);
  }

  /**カウントダウンを開始する*/
  public void startCountDown() {
    /**ウィンドウを中央に配置
     *
     */
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension dialogsize = this.getSize();
    if (dialogsize.height > screenSize.height) {
      dialogsize.height = screenSize.height;
    }
    if (dialogsize.width > screenSize.width) {
      dialogsize.width = screenSize.width;
    }
    this.setLocation( (screenSize.width - dialogsize.width) / 2,
                     (screenSize.height - dialogsize.height) / 2);

    /**タイマーを準備する*/
    java.util.Timer timer = new java.util.Timer();
    int final_count = count;

    /**Clipを用意する*/
    if (start_clip != null) {
      start_clip.close();
    }
    if (lastcount_clip != null) {
      lastcount_clip.close();
    }
    if (count_clip != null) {
      count_clip.close();
    }
    start_clip = openClip(start_sound);
    lastcount_clip = openClip(lastcount_sound);
    count_clip = openClip(count_sound);

    //カウント音を鳴らす
    for (int i = 1; i < final_count - 3; i++) {
      TimerTask count_task = new TimerTask() {
        public void run() {
          count--;
          count_text.setText(Integer.toString(count));
          count_clip.setFramePosition(0);
          count_clip.start();
          repaint();
        }
      };
      timer.schedule(count_task, 1000 * i);
    }

    //最終カウント音を鳴らす
    for (int i = final_count - 3; i < final_count; i++) {
      TimerTask lastcount_task = new TimerTask() {
        public void run() {
          count--;
          count_text.setForeground(Color.red);
          count_text.setText(Integer.toString(count));
          lastcount_clip.setFramePosition(0);
          lastcount_clip.start();
          repaint();
        }
      };
      timer.schedule(lastcount_task, 1000 * i);
    }

    //計測スタート音を鳴らす
    TimerTask start_task = new TimerTask() {
      public void run() {
        start_clip.setFramePosition(0);
        start_clip.start();
        setVisible(false);
      }
    };
    timer.schedule(start_task, 1000 * final_count);

    setVisible(true);
  }

  /**
   * Clipを準備する
   * @param file File
   * @return Clip
   */
  private Clip openClip(URL file) {

    Clip clip = null;

    try {
      AudioFormat format = AudioSystem.getAudioInputStream(file).getFormat();
      Info info = new Info(Clip.class, format);
      clip = (Clip) AudioSystem.getLine(info);
      clip.open(AudioSystem.getAudioInputStream(file));
    }
    catch (LineUnavailableException ex) {
      ex.printStackTrace();
    }
    catch (IOException ex) {
      ex.printStackTrace();
    }
    catch (UnsupportedAudioFileException ex) {
      ex.printStackTrace();
    }

    return clip;
  }
}
