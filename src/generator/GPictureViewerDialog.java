package generator;

import frames.GMainPanel;
import layers.GPicture;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;

public class GPictureViewerDialog extends JDialog {
    private BufferedImage image;
    private GMainPanel mainPanel;
    private File imageFile;

    public GPictureViewerDialog(JFrame owner, String title, File imageFile, GMainPanel mainPanel) {
        super(owner, title, true);
        this.mainPanel = mainPanel;
        this.imageFile = imageFile;

        // 디버깅 정보 출력
        System.out.println("=== Image Loading Debugging ===");
        System.out.println("File road: " + imageFile.getAbsolutePath());
        System.out.println("File Exist: " + imageFile.exists());
        System.out.println("File Size: " + imageFile.length() + " bytes");
        System.out.println("File Read Possible: " + imageFile.canRead());

        // 파일 타입 확인
        try {
            String contentType = Files.probeContentType(imageFile.toPath());
            System.out.println("Contents type: " + contentType);
        } catch (IOException e) {
            System.out.println("Contents type Check Failed: " + e.getMessage());
        }

        // ImageIO가 지원하는 포맷 확인
        String[] readerFormatNames = ImageIO.getReaderFormatNames();
        System.out.println("ImageIO Supported Format: " + String.join(", ", readerFormatNames));

        try {
            // 파일이 완전히 쓰일 때까지 잠시 대기
            Thread.sleep(500);

            // 이미지 로드 시도
            this.image = ImageIO.read(imageFile);

            if (this.image == null) {
                System.out.println("ImageIO.read() returned null");

                // 대체 방법 시도 - Toolkit 사용
                Image toolkitImage = Toolkit.getDefaultToolkit().getImage(imageFile.getAbsolutePath());
                MediaTracker tracker = new MediaTracker(this);
                tracker.addImage(toolkitImage, 0);
                tracker.waitForAll();

                if (toolkitImage.getWidth(null) > 0) {
                    // Toolkit으로 로드된 이미지를 BufferedImage로 변환
                    this.image = new BufferedImage(
                            toolkitImage.getWidth(null),
                            toolkitImage.getHeight(null),
                            BufferedImage.TYPE_INT_ARGB
                    );
                    Graphics2D g2d = this.image.createGraphics();
                    g2d.drawImage(toolkitImage, 0, 0, null);
                    g2d.dispose();
                    System.out.println("Image Load Success");
                }
            } else {
                System.out.println("ImageIO.read() Success");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Image Load Failed : " + e.getMessage() + "\nFile: " + imageFile.getAbsolutePath(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (this.image == null) {
            JOptionPane.showMessageDialog(this,
                    "Can not load the fole .\nfile: " + imageFile.getAbsolutePath(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        initComponents();
        setSize(new Dimension(Math.min(image.getWidth() + 40, 800),
                Math.min(image.getHeight() + 120, 600)));
        setLocationRelativeTo(owner);
        setVisible(true);
    }

    private void initComponents() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());

        // 이미지를 스크롤 가능한 패널에 표시
        JLabel imageLabel = new JLabel(new ImageIcon(image));
        JScrollPane scrollPane = new JScrollPane(imageLabel);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // 버튼 패널
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        JButton addToPanelButton = new JButton("Add");
        JButton closeButton = new JButton("Close");

        buttonPanel.add(saveButton);
        buttonPanel.add(addToPanelButton);
        buttonPanel.add(closeButton);

        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        // 버튼 액션
        saveButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File("generated_image.png"));

            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File saveFile = fileChooser.getSelectedFile();
                try {
                    // 원본 파일 복사
                    Files.copy(imageFile.toPath(), saveFile.toPath(),
                            java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    JOptionPane.showMessageDialog(this, "Image Saved",
                            "Save Completed", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Save Failed: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        addToPanelButton.addActionListener(e -> {
            // 실제 이미지 파일을 사용하여 GPicture 생성
            GPicture picture = new GPicture(imageFile);

            // 이미지 위치 설정 (화면 중앙)
            int centerX = mainPanel.getWidth() / 2 - picture.getWidth() / 2;
            int centerY = mainPanel.getHeight() / 2 - picture.getHeight() / 2;
            picture.setLocation(centerX, centerY);

            // 메인 패널에 추가
            mainPanel.addShape(picture);
            mainPanel.repaint();

            JOptionPane.showMessageDialog(this, "Image added Successfully.",
                    "Add Complete", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });

        closeButton.addActionListener(e -> dispose());

        add(contentPanel);
    }
}