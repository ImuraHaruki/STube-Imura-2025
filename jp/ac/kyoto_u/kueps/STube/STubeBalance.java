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
   * これにより、以降の getValue() は初回読み取り値との差分を返します。
   */
  public void calibrateZero() throws IOException {
    offset = 0.0; // 一時的にオフセットをリセット
    double initialValue = getValue(); // 生の値を取得
    offset = initialValue; // その値をオフセットとして保存
    System.out.println("[BALANCE] calibrateZero: offset set to " + offset);
  }

  synchronized public double getValue() throws IOException {

    System.out.println("[BALANCE] getValue()");
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
