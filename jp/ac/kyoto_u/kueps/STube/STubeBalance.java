package jp.ac.kyoto_u.kueps.STube;

import java.io.*;
import java.util.*;

import gnu.io.*;

public class STubeBalance {

  CommPortIdentifier com;
  SerialPort sport;

  BufferedReader port_reader;
  PrintStream port_writer;

  boolean waiting = false;
  long timeout = 2000;

  String value = "";

  STubeOption option = new STubeOption();

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

  synchronized public double getValue() throws IOException {

    System.out.println("[BALANCE] getValue()");
    readString();
    System.out.println("[BALANCE] line=[" + value + "]");

    try {
      String s = (value == null) ? "" : value.trim();
      if (s.isEmpty()) throw new NumberFormatException("empty line");

      String num = s.replaceAll("[^0-9+\\-.]", "");
      if (num.isEmpty()) throw new NumberFormatException("no number in line: " + s);

      double v = Double.parseDouble(num);
      System.out.println("[BALANCE] parsed=" + v);
      return v;

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
