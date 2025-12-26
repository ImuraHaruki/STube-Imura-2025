package gnu.io;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

public class CommPortIdentifier {

    public static final int PORT_SERIAL = 1;

    private final com.fazecast.jSerialComm.SerialPort port;

    private CommPortIdentifier(com.fazecast.jSerialComm.SerialPort port) {
        this.port = port;
    }

    public String getName() {
        return port.getSystemPortName();
    }

    public int getPortType() {
        return PORT_SERIAL;
    }

    public static Enumeration getPortIdentifiers() {
        com.fazecast.jSerialComm.SerialPort[] ports =
                com.fazecast.jSerialComm.SerialPort.getCommPorts(); // 公式API :contentReference[oaicite:3]{index=3}

        ArrayList list = new ArrayList();
        for (com.fazecast.jSerialComm.SerialPort p : ports) {
            list.add(new CommPortIdentifier(p));
        }
        return Collections.enumeration(list);
    }

    public static CommPortIdentifier getPortIdentifier(String name) throws NoSuchPortException {
        if (name == null) throw new NoSuchPortException("null");

        com.fazecast.jSerialComm.SerialPort[] ports =
                com.fazecast.jSerialComm.SerialPort.getCommPorts();

        for (com.fazecast.jSerialComm.SerialPort p : ports) {
            // Windowsだと "COM3" みたいなのが systemPortName に入ります
            if (name.equalsIgnoreCase(p.getSystemPortName())) {
                return new CommPortIdentifier(p);
            }
        }
        throw new NoSuchPortException("No such port: " + name);
    }

    public CommPort open(String owner, int timeoutMillis) throws PortInUseException {
        if (port.isOpen()) {
            throw new PortInUseException("Port already open: " + port.getSystemPortName());
        }

        // ざっくりタイムアウト設定（必要なら後で調整）
        port.setComPortTimeouts(
                com.fazecast.jSerialComm.SerialPort.TIMEOUT_READ_SEMI_BLOCKING,
                Math.max(timeoutMillis, 0),
                Math.max(timeoutMillis, 0)
        );

        boolean ok = port.openPort();
        if (!ok) {
            throw new PortInUseException("Failed to open: " + port.getSystemPortName());
        }

        return new SerialPort(port);
    }
}
