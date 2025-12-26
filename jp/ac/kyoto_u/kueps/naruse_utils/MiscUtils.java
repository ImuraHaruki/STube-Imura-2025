package jp.ac.kyoto_u.kueps.naruse_utils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Component;
import java.io.File;

public class MiscUtils {

    private static String normalizeExt(String extWithDot) {
        if (extWithDot == null || extWithDot.isEmpty()) return "";
        return extWithDot.startsWith(".") ? extWithDot.substring(1) : extWithDot;
    }

    private static File ensureExtension(File f, String extWithDot) {
        if (f == null) return null;
        if (extWithDot == null || extWithDot.isEmpty()) return f;

        String name = f.getName();
        if (name.toLowerCase().endsWith(extWithDot.toLowerCase())) {
            return f;
        }
        return new File(f.getParentFile(), name + extWithDot);
    }

    public static File showSaveDialog(Component parent, String extWithDot) {
        return showSaveDialog(parent, extWithDot, null);
    }

    public static File showSaveDialog(Component parent, String extWithDot, String description) {
        JFileChooser fc = new JFileChooser();
        String ext = normalizeExt(extWithDot);
        if (!ext.isEmpty()) {
            fc.setFileFilter(new FileNameExtensionFilter(
                    (description != null ? description : ("*." + ext)),
                    ext
            ));
        }

        int result = fc.showSaveDialog(parent);
        if (result != JFileChooser.APPROVE_OPTION) return null;

        File f = fc.getSelectedFile();
        return ensureExtension(f, extWithDot);
    }

    public static File showOpenDialog(Component parent, String extWithDot) {
        return showOpenDialog(parent, extWithDot, null);
    }

    public static File showOpenDialog(Component parent, String extWithDot, String description) {
        JFileChooser fc = new JFileChooser();
        String ext = normalizeExt(extWithDot);
        if (!ext.isEmpty()) {
            fc.setFileFilter(new FileNameExtensionFilter(
                    (description != null ? description : ("*." + ext)),
                    ext
            ));
        }

        int result = fc.showOpenDialog(parent);
        if (result != JFileChooser.APPROVE_OPTION) return null;

        return fc.getSelectedFile();
    }
}
