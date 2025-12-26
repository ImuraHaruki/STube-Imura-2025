package jp.ac.kyoto_u.kueps.naruse_utils;

import javax.swing.*;
import java.awt.*;

public class NFontChooser {

    public static final int APPROVE_OPTION = 0;
    public static final int CANCEL_OPTION = 1;

    private Font selectedFont = new Font("SansSerif", Font.PLAIN, 12);

    // 追加：ダイアログタイトル
    private String title = "Choose Font";

    // 追加：タイトル設定
    public void setTitle(String t) {
        if (t != null && !t.isEmpty()) title = t;
    }

    public int showDialog() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] families = ge.getAvailableFontFamilyNames();

        JComboBox<String> familyBox = new JComboBox<>(families);
        familyBox.setSelectedItem(selectedFont.getFamily());

        JSpinner sizeSpinner = new JSpinner(new SpinnerNumberModel(selectedFont.getSize(), 6, 200, 1));

        JCheckBox bold = new JCheckBox("Bold", selectedFont.isBold());
        JCheckBox italic = new JCheckBox("Italic", selectedFont.isItalic());

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0; c.gridy = 0;
        panel.add(new JLabel("Font"), c);
        c.gridx = 1;
        panel.add(familyBox, c);

        c.gridx = 0; c.gridy = 1;
        panel.add(new JLabel("Size"), c);
        c.gridx = 1;
        panel.add(sizeSpinner, c);

        c.gridx = 0; c.gridy = 2;
        panel.add(bold, c);
        c.gridx = 1;
        panel.add(italic, c);

        int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                title,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String family = (String) familyBox.getSelectedItem();
            int size = (Integer) sizeSpinner.getValue();
            int style = Font.PLAIN;
            if (bold.isSelected()) style |= Font.BOLD;
            if (italic.isSelected()) style |= Font.ITALIC;

            if (family == null || family.isEmpty()) {
                family = "SansSerif";
            }
            selectedFont = new Font(family, style, size);
            return APPROVE_OPTION;
        }
        return CANCEL_OPTION;
    }

    public Font getSelectedFont() {
        return selectedFont;
    }

    public void setSelectedFont(Font f) {
        if (f != null) selectedFont = f;
    }
}
