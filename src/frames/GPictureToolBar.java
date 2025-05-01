package frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import shapes.GPicture;

public class GPictureToolBar extends JToolBar {
    private static final long serialVersionUID = 1L;

    private JTextField promptField;
    private JButton generateButton;
    private JButton advancedButton;
    private JLabel statusLabel;
    private GMainFrame mainFrame;

    public GPictureToolBar(GMainFrame mainFrame) {
        this.mainFrame = mainFrame;

        // 툴바 설정
        setFloatable(false);
        setBorderPainted(true);
        setBackground(Color.LIGHT_GRAY);
        setLayout(new BorderLayout(5, 0));

        // 프롬프트 입력 패널
        JPanel inputPanel = new JPanel(new BorderLayout(5, 0));
        inputPanel.setBackground(Color.LIGHT_GRAY);

        JLabel promptLabel = new JLabel("Prompt: ");
        promptField = new JTextField(20);

        inputPanel.add(promptLabel, BorderLayout.WEST);
        inputPanel.add(promptField, BorderLayout.CENTER);

        // 버튼 패널
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        buttonPanel.setBackground(Color.LIGHT_GRAY);

        generateButton = new JButton("Generate");
        advancedButton = new JButton("Advanced...");

        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateImage(promptField.getText());
            }
        });

        advancedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAdvancedDialog();
            }
        });

        buttonPanel.add(generateButton);
        buttonPanel.add(advancedButton);

        // 상태 패널
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        statusPanel.setBackground(Color.LIGHT_GRAY);

        statusLabel = new JLabel("Ready");
        statusPanel.add(statusLabel);

        // 툴바에 패널 추가
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.EAST);
        add(statusPanel, BorderLayout.WEST);

        // 기본적으로 숨김 상태로 시작
        setVisible(false);
    }

    private void generateImage(String prompt) {
        if (prompt == null || prompt.trim().isEmpty()) {
            statusLabel.setText("Please enter a prompt");
            return;
        }

        statusLabel.setText("Generating image...");
        generateButton.setEnabled(false);

        // 메인 패널에서 이미지 생성 요청
        GMainPanel mainPanel = mainFrame.getMainPanel();

        // 여기서 실제 이미지 생성 로직을 호출
        // 예: mainPanel.createPictureFromPrompt(prompt);

        // 예시 목적으로 타이머 사용 (실제로는 API 호출 후 콜백으로 처리)
        new Thread(() -> {
            try {
                Thread.sleep(2000); // API 호출 시뮬레이션

                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("Image created");
                    generateButton.setEnabled(true);
                    promptField.setText("");
                });
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    private void showAdvancedDialog() {
        // 고급 옵션 다이얼로그 표시
        // 예: 이미지 크기, 스타일, 기타 API 파라미터 설정

        // GPictureAdvancedDialog dialog = new GPictureAdvancedDialog(mainFrame);
        // dialog.showDialog();
    }

    public void showToolBar(boolean show) {
        setVisible(show);
    }

    public void setStatus(String status) {
        statusLabel.setText(status);
    }

    public void initialize() {
        promptField.setText("");
        statusLabel.setText("Ready");
        generateButton.setEnabled(true);
    }
}