package com.mycompany.groupay;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RegisterPanel extends JPanel {

    private GrouPayApp parentFrame;
    private JTextField nameField;
    private JTextField surnameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    public RegisterPanel(GrouPayApp parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new GridBagLayout());
        setBackground(GrouPayApp.COLOR_BG);

        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
                g2.dispose();
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(GrouPayApp.COLOR_CARD);
        card.setPreferredSize(new Dimension(650, 520));
        card.setMinimumSize(new Dimension(650, 520));
        card.setMaximumSize(new Dimension(650, 520));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(15, 40, 15, 40));

        JLabel titleLabel = new JLabel("<html>¡Bienvenido a <font color='#8C7BE4'>Grou</font><font color='#EEB006'>Pay</font>!</html>", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sloganLabel = new JLabel("Finanzas familiares simples", SwingConstants.CENTER);
        sloganLabel.setForeground(GrouPayApp.COLOR_TEXT_SECONDARY);
        sloganLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        sloganLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        nameField = createStyledTextField("Nombre de usuario*");
        surnameField = createStyledTextField("Apellido*");
        emailField = createStyledTextField("Correo electronico*");
        passwordField = createStyledPasswordField("Contraseña*");
        confirmPasswordField = createStyledPasswordField("Cofirmar Contraseña*");

        JButton registerBtn = createStyledButton("Crear cuenta", GrouPayApp.COLOR_BUTTON);
        registerBtn.addActionListener(e -> {
            String name = nameField.getText();
            String surname = surnameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String confirm = new String(confirmPasswordField.getPassword());

            if (isPlaceholder(name, "Nombre de usuario*") || isPlaceholder(surname, "Apellido*") || 
                isPlaceholder(email, "Correo electronico*") || isPlaceholder(password, "Contraseña*")) {
                JOptionPane.showMessageDialog(parentFrame, "Por favor complete todos los campos obligatorios.", "Error de Registro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(parentFrame, "Formato de email inválido.", "Error de Registro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!password.equals(confirm)) {
                JOptionPane.showMessageDialog(parentFrame, "Las contraseñas no coinciden", "Error de Registro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (parentFrame.registerUser(name, surname, email, password)) {
                JOptionPane.showMessageDialog(parentFrame, "¡Cuenta creada con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                parentFrame.showLoginPanel();
            } else {
                JOptionPane.showMessageDialog(parentFrame, "El email ya está registrado o hubo un error.", "Error de Registro", JOptionPane.ERROR_MESSAGE);
            }
        });

        JLabel loginPrompt = new JLabel("Ya tenés una cuenta?");
        loginPrompt.setForeground(GrouPayApp.COLOR_BUTTON);
        loginPrompt.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel loginLink = new JLabel("Iniciar sesión");
        loginLink.setForeground(GrouPayApp.COLOR_BUTTON);
        loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLink.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                parentFrame.showLoginPanel();
            }
        });

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(2));
        card.add(sloganLabel);
        card.add(Box.createVerticalStrut(15));
        card.add(nameField);
        card.add(Box.createVerticalStrut(8));
        card.add(surnameField);
        card.add(Box.createVerticalStrut(8));
        card.add(emailField);
        card.add(Box.createVerticalStrut(8));
        card.add(passwordField);
        card.add(Box.createVerticalStrut(8));
        card.add(confirmPasswordField);
        card.add(Box.createVerticalStrut(15));
        card.add(registerBtn);
        card.add(Box.createVerticalStrut(10));

        JPanel sepPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(GrouPayApp.COLOR_ACCENT);
                g.drawLine(0, getHeight()/2, getWidth()/2 - 10, getHeight()/2);
                g.drawLine(getWidth()/2 + 10, getHeight()/2, getWidth(), getHeight()/2);
                g.drawString("o", getWidth()/2 - 3, getHeight()/2 + 5);
            }
        };
        sepPanel.setMaximumSize(new Dimension(300, 20));
        sepPanel.setOpaque(false);
        card.add(sepPanel);

        card.add(Box.createVerticalStrut(5));
        card.add(loginPrompt);
        card.add(loginLink);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        add(card, gbc);
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField(placeholder);
        field.setMaximumSize(new Dimension(500, 45));
        field.setBackground(GrouPayApp.COLOR_INPUT_BG);
        field.setForeground(Color.GRAY);
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(GrouPayApp.COLOR_ACCENT, 2),
            new EmptyBorder(5, 15, 5, 15)
        ));

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
            }
        });
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        return field;
    }

    private JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField(placeholder);
        field.setEchoChar((char) 0); 
        field.setMaximumSize(new Dimension(500, 45));
        field.setBackground(GrouPayApp.COLOR_INPUT_BG);
        field.setForeground(Color.GRAY);
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(GrouPayApp.COLOR_ACCENT, 2),
            new EmptyBorder(5, 15, 5, 15)
        ));

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (new String(field.getPassword()).equals(placeholder)) {
                    field.setText("");
                    field.setEchoChar('•'); 
                    field.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getPassword().length == 0) {
                    field.setEchoChar((char) 0); 
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
            }
        });
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        return field;
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(250, 40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new LineBorder(GrouPayApp.COLOR_ACCENT, 1));
        return btn;
    }

    private boolean isPlaceholder(String text, String placeholder) {
        return text.isEmpty() || text.equals(placeholder);
    }

    private boolean isValidEmail(String email) {
        int atIndex = email.indexOf('@');
        int dotIndex = email.lastIndexOf('.');
        return atIndex > 0 && dotIndex > atIndex + 1 && dotIndex < email.length() - 1;
    }
}
