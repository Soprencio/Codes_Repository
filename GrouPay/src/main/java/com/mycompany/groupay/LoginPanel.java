package com.mycompany.groupay;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

public class LoginPanel extends JPanel {

    private GrouPayApp parentFrame;
    private JTextField emailField;
    private JPasswordField passwordField;

    public LoginPanel(GrouPayApp parentFrame) {
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
        card.setBorder(new EmptyBorder(20, 40, 20, 40));

        JLabel welcomeLabel = new JLabel("¿Cómo estás? ¡Estuvimos esperandote!");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel logoLabel = new JLabel("<html><font color='#8C7BE4'>Grou</font><font color='#EEB006'>Pay</font></html>", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 36));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sloganLabel = new JLabel("Finanzas familiares simples", SwingConstants.CENTER);
        sloganLabel.setForeground(GrouPayApp.COLOR_TEXT_SECONDARY);
        sloganLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        sloganLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        emailField = createStyledTextField("Correo electronico*");
        passwordField = createStyledPasswordField("Contraseña*");

        JButton loginBtn = createStyledButton("Iniciar sesión", GrouPayApp.COLOR_BUTTON);
        loginBtn.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            if (isPlaceholder(email, "Correo electronico*") || isPlaceholder(password, "Contraseña*")) {
                JOptionPane.showMessageDialog(parentFrame, "Por favor complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (parentFrame.authenticateUser(email, password)) {
                JOptionPane.showMessageDialog(parentFrame, "¡Bienvenido!");
            } else {
                JOptionPane.showMessageDialog(parentFrame, "Correo electrónico o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JLabel registerPrompt = new JLabel("Todavía no tenes una cuenta?");
        registerPrompt.setForeground(GrouPayApp.COLOR_BUTTON);
        registerPrompt.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel registerLink = new JLabel("Registrate acá");
        registerLink.setForeground(GrouPayApp.COLOR_BUTTON);
        registerLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerLink.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton createBtn = createStyledButton("Crear cuenta", GrouPayApp.COLOR_BUTTON);
        createBtn.addActionListener(e -> parentFrame.showRegisterPanel());

        card.add(Box.createVerticalStrut(10));
        card.add(logoLabel);
        card.add(sloganLabel);
        card.add(Box.createVerticalStrut(20));
        card.add(emailField);
        card.add(Box.createVerticalStrut(10));
        card.add(passwordField);
        card.add(Box.createVerticalStrut(20));
        card.add(loginBtn);
        card.add(Box.createVerticalStrut(15));
        
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
        card.add(registerPrompt);
        card.add(registerLink);
        card.add(Box.createVerticalStrut(10));
        card.add(createBtn);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.insets = new Insets(0, 0, 20, 0);
        add(welcomeLabel, gbc);

        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 0, 0);
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
}
