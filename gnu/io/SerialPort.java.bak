package gnu.io;

import java.io.InputStream;
import java.io.OutputStream;

public class SerialPort extends CommPort {

    // gnu.io 互換の定数（STubeOption 等が参照している）
    public static final int DATABITS_5 = 5;
    public static final int DATABITS_6 = 6;
    public static final int DATABITS_7 = 7;
    public static final int DATABITS_8 = 8;

    public static final int STOPBITS_1 = 1;
    public static final int STOPBITS_2 = 2;

    public static final int PARITY_NONE  = 0;
    public static final int PARITY_ODD   = 1;
    public static final int PARITY_EVEN  = 2;
    public static final int PARITY_MARK  = 3;
    public static final int PARITY_SPACE = 4;

    private final com.fazecast.jSerialComm.SerialPort sp;

    SerialPort(com.fazecast.jSerialComm.SerialPort sp) {
        this.sp = sp;
    }

    @Override
    public String getName() {
        return sp.getSystemPortName();
    }

    @Override
    public void close() {
        sp.closePort();
    }

    public InputStream getInputStream() {
        return sp.getInputStream();
    }

    public OutputStream getOutputStream() {
        return sp.getOutputStream();
    }

    public void setSerialPortParams(int baudRate, int dataBits, int stopBits, int parity)
            throws UnsupportedCommOperationException {

        int jsStopBits = (stopBits == STOPBITS_2)
                ? com.fazecast.jSerialComm.SerialPort.TWO_STOP_BITS
                : com.fazecast.jSerialComm.SerialPort.ONE_STOP_BIT;

        int jsParity;
        switch (parity) {
            case PARITY_ODD:   jsParity = com.fazecast.jSerialComm.SerialPort.ODD_PARITY; break;
            case PARITY_EVEN:  jsParity = com.fazecast.jSerialComm.SerialPort.EVEN_PARITY; break;
            case PARITY_MARK:  jsParity = com.fazecast.jSerialComm.SerialPort.MARK_PARITY; break;
            case PARITY_SPACE: jsParity = com.fazecast.jSerialComm.SerialPort.SPACE_PARITY; break;
            case PARITY_NONE:
            default:           jsParity = com.fazecast.jSerialComm.SerialPort.NO_PARITY; break;
        }

        boolean ok = sp.setComPortParameters(baudRate, dataBits, jsStopBits, jsParity);
        if (!ok) {
            throw new UnsupportedCommOperationException("Failed to set serial parameters.");
        }
    }

    // 必要なら STube 側が呼ぶかもしれないので用意（未使用なら放置でOK）
    public boolean isOpen() {
        return sp.isOpen();
    }

    com.fazecast.jSerialComm.SerialPort unwrap() {
        return sp;
    }
}
