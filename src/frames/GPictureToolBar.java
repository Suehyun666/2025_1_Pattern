//package frames;
//
//import java.awt.BorderLayout;
//import java.awt.Color;
//import java.awt.FlowLayout;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.io.File;
//
//import javax.swing.JButton;
//import javax.swing.JLabel;
//import javax.swing.JOptionPane;
//import javax.swing.JPanel;
//import javax.swing.JTextField;
//import javax.swing.JToolBar;
//import javax.swing.SwingWorker;
//
//import generator.ImageGenerator;
//import generator.StabilityAIImageGenerator;
////import shapes.GPicture;
//
//public class GPictureToolBar extends JToolBar {
//    private static final long serialVersionUID = 1L;
//
//    private ImageGenerator generator;
//    private JTextField promptField;
//    private JButton generateButton;
//    private JButton advancedButton;
//    private JLabel statusLabel;
//    private GMainFrame mainFrame;
//
//    public GPictureToolBar(GMainFrame mainFrame) {
//        this.mainFrame = mainFrame;
//        this.generator=new StabilityAIImageGenerator(); //생성 위치 바꾸기
//        // 툴바 설정
//        setFloatable(false);
//        setBorderPainted(true);
//        setBackground(Color.LIGHT_GRAY);
//        setLayout(new BorderLayout(5, 0));
//
//        // 프롬프트 입력 패널
//        JPanel inputPanel = new JPanel(new BorderLayout(5, 0));
//        inputPanel.setBackground(Color.LIGHT_GRAY);
//
//        JLabel promptLabel = new JLabel("Prompt: ");
//        promptField = new JTextField(20);
//
//        inputPanel.add(promptLabel, BorderLayout.WEST);
//        inputPanel.add(promptField, BorderLayout.CENTER);
//
//        // 버튼 패널
//        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
//        buttonPanel.setBackground(Color.LIGHT_GRAY);
//
//        generateButton = new JButton("Generate");
//        advancedButton = new JButton("Advanced...");
//
//        generateButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                generateImage(promptField.getText());
//            }
//        });
//
//        advancedButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                showAdvancedDialog();
//            }
//        });
//
//        buttonPanel.add(generateButton);
//        buttonPanel.add(advancedButton);
//
//        // 상태 패널
//        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        statusPanel.setBackground(Color.LIGHT_GRAY);
//
//        statusLabel = new JLabel("Ready");
//        statusPanel.add(statusLabel);
//
//        // 툴바에 패널 추가
//        add(inputPanel, BorderLayout.CENTER);
//        add(buttonPanel, BorderLayout.EAST);
//        add(statusPanel, BorderLayout.WEST);
//
//        // 기본적으로 숨김 상태로 시작
//        setVisible(false);
//    }
//
//    private void generateImage(String prompt) {
//        if (prompt == null || prompt.trim().isEmpty()) {
//            statusLabel.setText("Please enter a prompt");
//            return;
//        }
//
//        statusLabel.setText("Generating image...");
//        generateButton.setEnabled(false);
//
//        // 백그라운드 작업으로 API 호출 수행
//        new SwingWorker<File, Void>() {
//            @Override
//            protected File doInBackground() throws Exception {
//                try {
//                    // Stability AI API 호출
//                    return generator.generateImage(prompt);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    return null;
//                }
//            }
//
//            @Override
//            protected void done() {
//                try {
//                    File imageFile = get();
//                    if (imageFile != null && imageFile.exists()) {
//                        // 메인 패널에 이미지 추가
//                        GPicture picture = new GPicture(imageFile);
//
//                        // 이미지 위치 설정 (화면 중앙에 배치 또는 원하는 위치)
//                        GMainPanel mainPanel = mainFrame.getMainPanel();
//                        int centerX = mainPanel.getWidth() / 2 - picture.getBounds().width / 2;
//                        int centerY = mainPanel.getHeight() / 2 - picture.getBounds().height / 2;
//                        picture.setLocation(centerX, centerY);
//
//                        // 메인 패널의 shapes 벡터에 추가
//                        // 참고: GMainPanel이 이 기능을 지원하도록 메소드를 추가해야 함
//                        addPictureToMainPanel(picture);
//
//                        statusLabel.setText("Image created successfully");
//                    } else {
//                        statusLabel.setText("Failed to generate image");
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    statusLabel.setText("Error: " + e.getMessage());
//                } finally {
//                    generateButton.setEnabled(true);
//                }
//            }
//        }.execute();
//    }
//
//    // 이미지를 메인 패널에 추가하는 메소드
//    private void addPictureToMainPanel(GPicture picture) {
//        GMainPanel mainPanel = mainFrame.getMainPanel();
//
//        // 이 부분은 GMainPanel에 적절한 메소드가 필요함
//        // 가정: mainPanel에 다음과 같은 메소드가 있거나 추가될 예정
//        // mainPanel.addShape(picture);
//
//        // 대안으로, 리플렉션이나 직접 액세스를 사용할 수 있지만 권장하지 않음
//        // 임시로 예외 발생
//        JOptionPane.showMessageDialog(mainFrame,
//                "Generated image at: " + picture.getBounds().x + ", " + picture.getBounds().y,
//                "Image Generated",
//                JOptionPane.INFORMATION_MESSAGE);
//
//        // 메인 패널 리페인트
//        mainPanel.repaint();
//    }
//
//    private void showAdvancedDialog() {
//        // 고급 옵션 다이얼로그 표시
//        // 예: API 키 변경, 이미지 크기, 스타일, 기타 API 파라미터 설정
//        String apiKey = JOptionPane.showInputDialog(mainFrame,
//                "Enter your Stability AI API Key:",
//                generator.getApiKey());
//
//        if (apiKey != null && !apiKey.trim().isEmpty()) {
//            generator.setApiKey(apiKey);
//        }
//    }
//
//    public void showToolBar(boolean show) {
//        setVisible(show);
//    }
//
//    public void setStatus(String status) {
//        statusLabel.setText(status);
//    }
//
//    public void initialize() {
//        promptField.setText("");
//        statusLabel.setText("Ready");
//        generateButton.setEnabled(true);
//    }
//}