
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StudentRegistrationForm extends JFrame implements ActionListener {
    JTextField nameField, ageField, emailField;
    JButton submitBtn, resetBtn;

    final String DB_URL = "jdbc:mysql://localhost:3306/studentdb";
    final String USER = "root";        // your MySQL username
    final String PASS = "your_password";  // your MySQL password

    public StudentRegistrationForm() {
        setTitle("Student Registration Form");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 2, 10, 10));

        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Age:"));
        ageField = new JTextField();
        add(ageField);

        add(new JLabel("Email:"));
        emailField = new JTextField();
        add(emailField);

        submitBtn = new JButton("Submit");
        resetBtn = new JButton("Reset");
        submitBtn.addActionListener(this);
        resetBtn.addActionListener(this);

        add(submitBtn);
        add(resetBtn);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitBtn) {
            String name = nameField.getText();
            String email = emailField.getText();
            String ageText = ageField.getText();

            if (name.isEmpty() || email.isEmpty() || ageText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!");
                return;
            }

            try {
                int age = Integer.parseInt(ageText);
                if (age <= 0) {
                    JOptionPane.showMessageDialog(this, "Invalid age!");
                    return;
                }

                // Save to DB
                Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                String query = "INSERT INTO students (name, age, email) VALUES (?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, name);
                ps.setInt(2, age);
                ps.setString(3, email);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Student registered successfully!");
                conn.close();

                // Clear fields
                nameField.setText("");
                ageField.setText("");
                emailField.setText("");

            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for age!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            }

        } else if (e.getSource() == resetBtn) {
            nameField.setText("");
            ageField.setText("");
            emailField.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentRegistrationForm::new);
    }
}
