package frame;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class GNewDocumentDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    private JTextField widthField;
    private JTextField heightField;
    private JComboBox<String> resolutionCombo;
    private JComboBox<String> colorModeCombo;
    private boolean confirmed = false;
    private Dimension documentSize;

    public GNewDocumentDialog(Frame owner) {
        //attribute
        super(owner, "New Document", true);
        setSize(400, 300);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.GRAY);

        // �ʺ� �Է�
        JLabel widthLabel = new JLabel("Width:");
        widthLabel.setForeground(Color.WHITE);
        widthField = new JTextField("800");

        // ���� �Է�
        JLabel heightLabel = new JLabel("Height:");
        heightLabel.setForeground(Color.WHITE);
        heightField = new JTextField("600");

        // �ػ�
        JLabel resolutionLabel = new JLabel("Resolution:");
        resolutionLabel.setForeground(Color.WHITE);
        resolutionCombo = new JComboBox<>(new String[]{"72 ppi", "300 ppi", "600 ppi"});

        // �÷� ���
        JLabel colorModeLabel = new JLabel("Color Mode:");
        colorModeLabel.setForeground(Color.WHITE);
        colorModeCombo = new JComboBox<>(new String[]{"RGB Color", "CMYK Color", "Grayscale"});

        // ������Ʈ �߰�
        mainPanel.add(widthLabel);
        mainPanel.add(widthField);
        mainPanel.add(heightLabel);
        mainPanel.add(heightField);
        mainPanel.add(resolutionLabel);
        mainPanel.add(resolutionCombo);
        mainPanel.add(colorModeLabel);
        mainPanel.add(colorModeCombo);

        add(mainPanel, BorderLayout.CENTER);

        // ��ư �г�
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.GRAY);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            try {
                int width = Integer.parseInt(widthField.getText());
                int height = Integer.parseInt(heightField.getText());

                // ��ȿ�� �˻�
                if (width <= 0 || height <= 0) {
                    JOptionPane.showMessageDialog(this,
                            "Width and height must be positive numbers.",
                            "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                documentSize = new Dimension(width, height);
                confirmed = true;
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Please enter valid numbers for width and height.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Dimension getDocumentSize() {
        return documentSize;
    }
}