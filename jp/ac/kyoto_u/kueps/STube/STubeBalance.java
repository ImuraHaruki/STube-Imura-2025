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

  /**
   * A frame read immediately after opening/flushing can begin mid-message in
   * continuous-output mode.  Discard that one frame, then only parse complete
   * newline-delimited messages from the BufferedReader.
   */
  boolean discardNextLine = true;

  /** A balance reading must include at least one digit before its decimal. */
  private static final Pattern WEIGHT_PATTERN = Pattern.compile(
      "(?<![0-9.])([-+]?\\s*\\d+\\.\\d+)(?:\\s*)[gG]\\b");

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
   * TARE コマンドを送り、短時間待って次の行から受信を同期する。
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
      // InputStream と BufferedReader を混在させると、行の途中から
      // readLine() して先頭桁を失うことがある。次の getValue() で一行だけ
      // 捨て、必ず改行境界の次のフレームを読む。
      discardNextLine = true;
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
      if (Double.isNaN(readings[i])) {
        throw new IOException("Invalid balance reading while calibrating zero");
      }
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
    
    int attempts = 0;
    while (attempts < 3) {
      String line = readCompleteLine();
      value = line;
      System.out.println("[BALANCE] line=[" + value + "]");

      try {
        String s = (value == null) ? "" : value.trim();
        if (s.isEmpty()) throw new NumberFormatException("empty line");

        double v = parseWeightFrame(s);
        double result = v - offset;
        if (v < 0) {
          System.out.println("[BALANCE] negative raw value retained: parsed=" + v + " offset=" + offset + " result=" + result + " (from '" + s + "')");
        } else {
          System.out.println("[BALANCE] parsed=" + v + " offset=" + offset + " result=" + result + " (from '" + s + "')");
        }
        return result;

      } catch (Exception ex) {
        attempts++;
        System.out.println("[BALANCE] parse failed (attempt " + attempts + "): " + ex);
        try {
          Thread.sleep(50);
        } catch (InterruptedException ie) {
          Thread.currentThread().interrupt();
        }
      }
    }

    System.out.println("[BALANCE] giving up after invalid reads; returning NaN");
    return Double.NaN; 
  }

  /**
   * Discards exactly one potentially partial frame after open/tare, then reads
   * the next complete newline-delimited frame.  Do not read the underlying
   * InputStream directly after the BufferedReader has been created.
   */
  private String readCompleteLine() throws IOException {
    if (discardNextLine) {
      String discarded = readString();
      discardNextLine = false;
      System.out.println("[BALANCE] discarded synchronization frame=[" + discarded + "]");
    }
    return readString();
  }

  /**
   * Parses a complete balance frame.  In particular, values such as ".289 g"
   * are rejected rather than silently accepted as 0.289 g.
   */
  static double parseWeightFrame(String frame) {
    Matcher matcher = WEIGHT_PATTERN.matcher(frame);
    if (!matcher.find()) {
      throw new NumberFormatException("invalid balance frame: " + frame);
    }
    return Double.parseDouble(matcher.group(1).replaceAll("\\s+", ""));
  }

  public void open() throws NumberFormatException, PortInUseException,
      UnsupportedCommOperationException, IOException, TooManyListenersException {

    sport = (SerialPort) com.open("Kyoto Univ. STube", (int) timeout * 3);
    sport.setSerialPortParams(option.com_rate, option.com_databits,
                              option.com_stopbits, option.com_parity);

    System.out.println("[BALANCE] setSerialPortParams() done");

    port_reader = new BufferedReader(new InputStreamReader(sport.getInputStream()));
    port_writer = new PrintStream(sport.getOutputStream(), true);
    discardNextLine = true;

    System.out.println("[BALANCE] open()");
    System.out.println("[BALANCE] port=" + com.getName());
    System.out.println("[BALANCE] rate=" + option.com_rate
      + " databits=" + option.com_databits
      + " stopbits=" + option.com_stopbits
      + " parity=" + option.com_parity);

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
