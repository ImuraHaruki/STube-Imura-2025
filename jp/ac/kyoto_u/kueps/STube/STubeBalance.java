package jp.ac.kyoto_u.kueps.STube;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import gnu.io.*;

public class STubeBalance {

  CommPortIdentifier com;
  SerialPort sport;

  BufferedReader port_reader;
  PrintStream port_writer;

  boolean waiting = false;
  long timeout = 2000;

  String value = "";

  STubeOption option;
  
  /** 初回読み取り値をオフセットとして保存 */
  double offset = 0.0;

  public void setOption(STubeOption opt) {
    this.option = opt;
  }

  public STubeBalance() {
  }

  public STubeBalance(CommPortIdentifier com) {
    setCommPort(com);
  }

  public STubeBalance(String com_name) throws NoSuchPortException {
    setCommPort(com_name);
  }

  public void tare() {
    port_writer.print("TARE\r");
    port_writer.flush();
  }

  /**
   * TARE コマンドを送り、短時間待って受信バッファを捨てる。
   * open() 後に呼ぶことを想定しています。
   * @param waitMs 待ち時間（ミリ秒）
   */
  public void tareAndFlush(int waitMs) {
    try {
      tare();
      try {
        Thread.sleep(waitMs);
      } catch (InterruptedException ie) {
        Thread.currentThread().interrupt();
      }
      try {
        InputStream in = sport.getInputStream();
        while (in.available() > 0) {
          in.read();
        }
      } catch (Exception e) {
        // ignore flush errors
      }
    }
    catch (Exception ex) {
      // ignore
    }
  }

  /**
   * 現在の秤の値を読み取り、それをゼロ点（オフセット）として設定します。
   * 秤の値が安定するまで複数回読み取り、その平均値をオフセットにします。
   */
  public void calibrateZero() throws IOException {
    offset = 0.0; // 一時的にオフセットをリセット
    int numReadings = 5;
    double[] readings = new double[numReadings];
    
    System.out.println("[BALANCE] calibrateZero: waiting for stability...");
    for (int i = 0; i < numReadings; i++) {
      try {
        Thread.sleep(150); // 150ms 待機
      } catch (InterruptedException ie) {
        Thread.currentThread().interrupt();
      }
      readings[i] = getValue();
      System.out.println("[BALANCE] calibrateZero: read " + (i+1) + " = " + readings[i]);
    }
    
    // 平均値を計算
    double sum = 0;
    for (double r : readings) {
      sum += r;
    }
    offset = sum / numReadings;
    System.out.println("[BALANCE] calibrateZero: offset set to average " + offset);
  }

  synchronized public double getValue() throws IOException {

    System.out.println("[BALANCE] getValue()");
    
    // 連続送信モードに対応：読み取る直前にバッファの古いデータをクリア
    try {
      InputStream in = sport.getInputStream();
      while (in.available() > 0) {
        in.read();
      }
      // 少し待って最新データが届くのを待つ
      Thread.sleep(50);
    } catch (Exception e) {
      // ignore
    }
    
    readString();
    System.out.println("[BALANCE] line=[" + value + "]");

    try {
      String s = (value == null) ? "" : value.trim();
      if (s.isEmpty()) throw new NumberFormatException("empty line");

      // 数字部分を正規表現で抽出（符号、小数、指数表記に対応）
      Pattern p = Pattern.compile("[+-]?\\d*\\.?\\d+(?:[eE][+-]?\\d+)?");
      Matcher m = p.matcher(s);
      if (!m.find()) throw new NumberFormatException("no number in line: " + s);

      String num = m.group();
      double v = Double.parseDouble(num);
      // 秤から負の値が来た場合は0として扱う
      if (v < 0) {
        v = 0.0;
      }
      double result = v - offset;
      System.out.println("[BALANCE] parsed=" + v + " offset=" + offset + " result=" + result + " (from '" + s + "')");
      return result;

    } catch (Exception ex) {
      System.out.println("[BALANCE] parse failed: " + ex);
      return Double.NaN; 
    }
  }

  public void open() throws NumberFormatException, PortInUseException,
      UnsupportedCommOperationException, IOException, TooManyListenersException {

    sport = (SerialPort) com.open("Kyoto Univ. STube", (int) timeout * 3);
    sport.setSerialPortParams(option.com_rate, option.com_databits,
                              option.com_stopbits, option.com_parity);

    System.out.println("[BALANCE] setSerialPortParams() done");

    port_reader = new BufferedReader(new InputStreamReader(sport.getInputStream()));
    port_writer = new PrintStream(sport.getOutputStream(), true);

    System.out.println("[BALANCE] open()");
    System.out.println("[BALANCE] port=" + com.getName());
    System.out.println("[BALANCE] rate=" + option.com_rate
      + " databits=" + option.com_databits
      + " stopbits=" + option.com_stopbits
      + " parity=" + option.com_parity);

    debugDumpBytes(2000);
  }

  public void close() {
    try {
      if (port_reader != null) port_reader.close();
      if (port_writer != null) port_writer.close();
      if (sport != null) sport.close();
    }
    catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public void setCommPort(String name) throws NoSuchPortException {
    com = CommPortIdentifier.getPortIdentifier(name);
  }

  public void setCommPort(CommPortIdentifier comm_port) {
    com = comm_port;
  }

  String readString() throws IOException {
    waiting = true;
    value = null;

    Thread read = new Thread() {
      public void run() {
        try {
          value = port_reader.readLine(); 
        } catch (Exception ex) {
          ex.printStackTrace();
        } finally {
          waiting = false;
        }
      }
    };

    read.start();

    try {
      read.join(timeout);
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    if (waiting == true || value == null) {
      waiting = false;
      close();
      throw new IOException("Balance did not respond");
    }

    return value;
  }

  public void debugDumpBytes(int millis) {
    try {
      InputStream in = sport.getInputStream();
      long end = System.currentTimeMillis() + millis;
      System.out.println("[DUMP] start " + millis + "ms");

      int count = 0;
      while (System.currentTimeMillis() < end) {
        int n = in.available();
        if (n > 0) {
          int b = in.read();
          count++;

          if (b >= 32 && b <= 126) {
            System.out.print((char)b);
          } else {
            System.out.print(String.format("<%02X>", b));
          }
        } else {
          Thread.sleep(5);
        }
      }
      System.out.println("\n[DUMP] end. bytes=" + count);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
