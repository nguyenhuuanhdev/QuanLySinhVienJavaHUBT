import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginForm extends JFrame {
    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnLogin, btnClear;
    private JLabel lblStatus;

    public LoginForm() {
        setTitle("Login System 2025");
        setSize(420, 360);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(false);

        // ======= NỀN GRADIENT =======
        JPanel background = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(83, 105, 118),
                        0, getHeight(), new Color(41, 46, 73));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        background.setLayout(new BorderLayout());
        add(background);

        JLabel title = new JLabel("Welcome Back!", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        background.add(title, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(255, 255, 255, 230));
        panel.setPreferredSize(new Dimension(340, 220));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ======= ICON USER & PASSWORD =======
        ImageIcon userIcon = new ImageIcon(getClass().getResource("/img/user.png"));
        Image userImg = userIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        userIcon = new ImageIcon(userImg);

        ImageIcon passIcon = new ImageIcon(getClass().getResource("/img/password.png"));
        Image passImg = passIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        passIcon = new ImageIcon(passImg);

        // ======= USERNAME =======
        JLabel lblUserIcon = new JLabel(userIcon);
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(lblUserIcon, gbc);

        txtUser = new JTextField(15);
        txtUser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUser.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        gbc.gridx = 1;
        panel.add(txtUser, gbc);

        // ======= PASSWORD + ICON EYE =======
        JLabel lblPassIcon = new JLabel(passIcon);
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(lblPassIcon, gbc);

        txtPass = new JPasswordField(15);
        txtPass.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPass.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        // Panel chứa password + icon eye
        JPanel passPanel = new JPanel(new BorderLayout());
        passPanel.setBackground(new Color(255, 255, 255, 230));
        passPanel.add(txtPass, BorderLayout.CENTER);

        // Icon con mắt
        ImageIcon eyeOpen = new ImageIcon(getClass().getResource("/img/eye.png"));
        Image eyeOpenImg = eyeOpen.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        eyeOpen = new ImageIcon(eyeOpenImg);

        ImageIcon eyeClose = new ImageIcon(getClass().getResource("/img/eye.png"));
        Image eyeCloseImg = eyeClose.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        eyeClose = new ImageIcon(eyeCloseImg);

        JLabel lblEye = new JLabel(eyeClose);
        lblEye.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblEye.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        passPanel.add(lblEye, BorderLayout.EAST);

        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(passPanel, gbc);

        // ======= SỰ KIỆN CLICK ICON EYE =======
        lblEye.addMouseListener(new MouseAdapter() {
            private boolean showing = false;
            @Override
            public void mouseClicked(MouseEvent e) {
                showing = !showing;
                txtPass.setEchoChar(showing ? (char) 0 : '•');

            }
        });

        // ======= BUTTONS =======
        btnLogin = new JButton("Login");
        styleButton(btnLogin, new Color(52, 152, 219));

        btnClear = new JButton("Clear");
        styleButton(btnClear, new Color(231, 76, 60));

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        btnPanel.setBackground(new Color(255, 255, 255, 0));
        btnPanel.add(btnLogin);
        btnPanel.add(btnClear);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(btnPanel, gbc);

        // ======= STATUS LABEL =======
        lblStatus = new JLabel("", SwingConstants.CENTER);
        lblStatus.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblStatus.setForeground(new Color(189, 195, 199));

        background.add(panel, BorderLayout.CENTER);
        background.add(lblStatus, BorderLayout.SOUTH);

        // ======= EVENTS =======
        btnLogin.addActionListener(e -> checkLogin());
        btnClear.addActionListener(e -> {
            txtUser.setText("");
            txtPass.setText("");
            lblStatus.setText("");
        });

        setVisible(true);
    }

    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(color.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(color);
            }
        });
    }

    private void checkLogin() {
        String username = txtUser.getText();
        String password = new String(txtPass.getPassword());

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE username=? AND password=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                lblStatus.setForeground(new Color(39, 174, 96));
                lblStatus.setText("Login successful!");
                JOptionPane.showMessageDialog(this, "Welcome " + username + "!");
                dispose();
                new StudentForm();
            } else {
                lblStatus.setForeground(new Color(192, 57, 43));
                lblStatus.setText("Invalid username or password!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            lblStatus.setForeground(Color.RED);
            lblStatus.setText("Database error!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginForm::new);
    }
}
