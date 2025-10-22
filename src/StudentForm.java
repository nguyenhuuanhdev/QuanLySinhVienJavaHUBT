import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StudentForm extends JFrame {
    private JTable table;
    private JTextField txtName, txtAge, txtAddress;
    private JComboBox<String> cbGender;
    private DefaultTableModel model;

    public StudentForm() {
        setTitle("Student Management 2025");
        setSize(850, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Nền gradient
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
        background.setLayout(new BorderLayout(10, 10));
        background.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        add(background);

        // Tiêu đề
        JLabel title = new JLabel("STUDENT MANAGEMENT SYSTEM", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        background.add(title, BorderLayout.NORTH);

        // Panel nhập liệu (trắng mờ, bo góc)
        JPanel inputPanel = new JPanel(new GridLayout(2, 4, 12, 12));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        inputPanel.setBackground(new Color(255, 255, 255, 220));
        inputPanel.setOpaque(true);

        txtName = new JTextField();
        cbGender = new JComboBox<>(new String[]{"Male", "Female"});
        txtAge = new JTextField();
        txtAddress = new JTextField();

        Font font = new Font("Segoe UI", Font.PLAIN, 14);
        txtName.setFont(font);
        txtAge.setFont(font);
        txtAddress.setFont(font);
        cbGender.setFont(font);

        // Các label có icon
        JLabel lblName = new JLabel(" Full Name:");
        lblName.setIcon(loadIcon("/img/user.png"));
        JLabel lblGender = new JLabel(" Gender:");
        lblGender.setIcon(loadIcon("/img/gender.png"));
        JLabel lblAge = new JLabel(" Age:");
        lblAge.setIcon(loadIcon("/img/age.png"));
        JLabel lblAddress = new JLabel(" Address:");
        lblAddress.setIcon(loadIcon("/img/address.png"));

        inputPanel.add(lblName);
        inputPanel.add(txtName);
        inputPanel.add(lblGender);
        inputPanel.add(cbGender);
        inputPanel.add(lblAge);
        inputPanel.add(txtAge);
        inputPanel.add(lblAddress);
        inputPanel.add(txtAddress);

        background.add(inputPanel, BorderLayout.NORTH);

        // Bảng dữ liệu
        model = new DefaultTableModel(new String[]{"ID", "Full Name", "Gender", "Age", "Address"}, 0);
        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(52, 152, 219));
        table.setSelectionForeground(Color.WHITE);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(52, 73, 94));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);

        // Hàng xen kẽ màu
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            private final Color evenColor = new Color(245, 245, 245);
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object val, boolean sel,
                                                           boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(tbl, val, sel, foc, row, col);
                if (!sel) c.setBackground(row % 2 == 0 ? evenColor : Color.WHITE);
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        background.add(scrollPane, BorderLayout.CENTER);

        // Panel nút
        JPanel buttonPanel = new JPanel(new GridLayout(1, 5, 15, 0));
        buttonPanel.setBackground(new Color(255, 255, 255, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));

        JButton btnAdd = createModernButton("Add", new Color(46, 204, 113));
        JButton btnEdit = createModernButton("Edit", new Color(241, 196, 15));
        JButton btnDelete = createModernButton("Delete", new Color(231, 76, 60));
        JButton btnRefresh = createModernButton("Refresh", new Color(52, 152, 219));
        JButton btnResetID = createModernButton("Reset ID", new Color(52, 73, 94));

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnResetID);

        background.add(buttonPanel, BorderLayout.SOUTH);

        // Load dữ liệu
        loadStudents();

        // Sự kiện
        btnAdd.addActionListener(e -> addStudent());
        btnEdit.addActionListener(e -> editStudent());
        btnDelete.addActionListener(e -> deleteStudent());
        btnRefresh.addActionListener(e -> {
            loadStudents();
            txtName.setText("");
            txtAge.setText("");
            txtAddress.setText("");
            cbGender.setSelectedIndex(0);
            table.clearSelection();
        });
        btnResetID.addActionListener(e -> resetTable());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtName.setText(model.getValueAt(row, 1).toString());
                    cbGender.setSelectedItem(model.getValueAt(row, 2).toString());
                    txtAge.setText(model.getValueAt(row, 3).toString());
                    txtAddress.setText(model.getValueAt(row, 4).toString());
                }
            }
        });

        setVisible(true);
    }

    // ======= Hàm load icon nhỏ (18x18) =======
    private ImageIcon loadIcon(String path) {
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(path));
            Image scaled = icon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (Exception e) {
            return null; // tránh crash nếu không tìm thấy ảnh
        }
    }

    private JButton createModernButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        // Hiệu ứng hover
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
        return btn;
    }

    private void loadStudents() {
        model.setRowCount(0);
        try (Connection conn = DBConnection.getConnection()) {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM students");
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("fullname"),
                        rs.getString("gender"),
                        rs.getInt("age"),
                        rs.getString("address")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Load error: " + e.getMessage());
        }
    }

    private void addStudent() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO students (fullname, gender, age, address) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, txtName.getText());
            pst.setString(2, cbGender.getSelectedItem().toString());
            pst.setInt(3, Integer.parseInt(txtAge.getText()));
            pst.setString(4, txtAddress.getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Added successfully!");
            loadStudents();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Add error: " + e.getMessage());
        }
    }

    private void editStudent() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to edit!");
            return;
        }
        int id = (int) model.getValueAt(row, 0);
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE students SET fullname=?, gender=?, age=?, address=? WHERE id=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, txtName.getText());
            pst.setString(2, cbGender.getSelectedItem().toString());
            pst.setInt(3, Integer.parseInt(txtAge.getText()));
            pst.setString(4, txtAddress.getText());
            pst.setInt(5, id);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Updated!");
            loadStudents();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Edit error: " + e.getMessage());
        }
    }

    private void deleteStudent() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to delete!");
            return;
        }
        int id = (int) model.getValueAt(row, 0);
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement pst = conn.prepareStatement("DELETE FROM students WHERE id=?");
            pst.setInt(1, id);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Deleted!");
            loadStudents();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Delete error: " + e.getMessage());
        }
    }

    private void resetTable() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "This will delete ALL students and reset ID to 1.\nAre you sure?",
                "Confirm Reset",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DBConnection.getConnection()) {
                Statement st = conn.createStatement();
                st.execute("TRUNCATE TABLE students");
                JOptionPane.showMessageDialog(this, "All records deleted and ID reset!");
                loadStudents();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Reset error: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentForm::new);
    }
}
