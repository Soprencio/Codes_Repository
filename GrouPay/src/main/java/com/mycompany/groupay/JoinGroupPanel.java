package com.mycompany.groupay;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class JoinGroupPanel extends JPanel {

    private GrouPayApp parentFrame;
    private JTextField nameField;
    private JPasswordField passField;

    public JoinGroupPanel(GrouPayApp parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout());
        setBackground(GrouPayApp.COLOR_BG);
    }

    public void init() {
        removeAll();
        initUI();
        revalidate();
        repaint();
    }

    private void initUI() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setBorder(new EmptyBorder(30, 40, 0, 40));

        JButton backBtn = new JButton("◀");
        backBtn.setBackground(GrouPayApp.COLOR_BUTTON);
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("Arial", Font.BOLD, 18));
        backBtn.setFocusPainted(false);
        backBtn.setBorder(new LineBorder(GrouPayApp.COLOR_ACCENT, 1));
        backBtn.setPreferredSize(new Dimension(50, 50));
        backBtn.addActionListener(e -> parentFrame.showNoFamilyPanel());
        
        JPanel backWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backWrapper.setOpaque(false);
        backWrapper.add(backBtn);
        topBar.add(backWrapper, BorderLayout.WEST);

        add(topBar, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Unirse a un grupo", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 32));
        gbc.gridy = 0; gbc.gridwidth = 2;
        centerPanel.add(title, gbc);

        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(GrouPayApp.COLOR_CARD);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
                g2.dispose();
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(40, 50, 40, 50));
        card.setPreferredSize(new Dimension(650, 350));

        JLabel subtitle = new JLabel("Introducí los datos del grupo");
        subtitle.setForeground(Color.WHITE);
        subtitle.setFont(new Font("Arial", Font.BOLD, 18));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(subtitle);
        card.add(Box.createVerticalStrut(30));

        passField = createStyledPasswordField("Contraseña del grupo*");
        nameField = createStyledTextField("Nombre de la familia*");

        JLabel passLabel = new JLabel("Contraseña");
        passLabel.setForeground(Color.WHITE);
        passLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(passLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(passField);

        card.add(Box.createVerticalStrut(20));

        JLabel nameLabel = new JLabel("Nombre");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(nameLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(nameField);

        gbc.gridy = 1;
        gbc.insets = new Insets(30, 0, 40, 0);
        centerPanel.add(card, gbc);

        JButton joinBtn = new JButton("Unirse al grupo");
        joinBtn.setPreferredSize(new Dimension(350, 50));
        joinBtn.setBackground(GrouPayApp.COLOR_BUTTON);
        joinBtn.setForeground(Color.WHITE);
        joinBtn.setFont(new Font("Arial", Font.BOLD, 18));
        joinBtn.setFocusPainted(false);
        joinBtn.setBorder(new LineBorder(GrouPayApp.COLOR_ACCENT, 1));
        joinBtn.addActionListener(e -> handleJoin());
        
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        centerPanel.add(joinBtn, gbc);

        add(centerPanel, BorderLayout.CENTER);
    }

    private void handleJoin() {
        String name = nameField.getText();
        String pass = new String(passField.getPassword());
        
        if (isPlaceholder(name, "Nombre de la familia*") || isPlaceholder(pass, "Contraseña del grupo*")) {
            JOptionPane.showMessageDialog(this, "Por favor complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int result = parentFrame.getUserDAO().joinFamily(parentFrame.getCurrentUser(), name, pass);
        
        if (result == 1) {
            JOptionPane.showMessageDialog(this, "¡Te has unido con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            parentFrame.showHomePanel();
        } else if (result == -1) {
            JOptionPane.showMessageDialog(this, "El grupo ya tiene el máximo de 6 integrantes.", "Grupo Lleno", JOptionPane.WARNING_MESSAGE);
        } else if (result == 0) {
            JOptionPane.showMessageDialog(this, "Nombre o contraseña incorrectos.", "Error de Datos", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Ocurrió un error técnico al intentar unirse.", "Error", JOptionPane.ERROR_MESSAGE);
        }
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

    private boolean isPlaceholder(String text, String placeholder) {
        return text.isEmpty() || text.equals(placeholder);
    }
}
