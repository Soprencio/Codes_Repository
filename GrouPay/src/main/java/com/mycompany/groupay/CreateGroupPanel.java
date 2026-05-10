package com.mycompany.groupay;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class CreateGroupPanel extends JPanel {

    private GrouPayApp parentFrame;
    private JTextField nameField, descField;
    private JPasswordField passField;
    private String selectedType = "";
    private int permitirValue = 1; 
    
    private JButton btnTodos, btnAdmin;
    private JButton[] typeBtns;
    private final Color COLOR_TYPE_SELECTED = new Color(0x5D4EB2);

    public CreateGroupPanel(GrouPayApp parentFrame) {
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
        topBar.setBorder(new EmptyBorder(25, 40, 0, 40));
        
        JButton backBtn = new JButton("◀");
        backBtn.setPreferredSize(new Dimension(50, 50));
        backBtn.setBackground(GrouPayApp.COLOR_BUTTON);
        backBtn.setForeground(Color.WHITE);
        backBtn.setBorder(new LineBorder(GrouPayApp.COLOR_ACCENT, 1));
        backBtn.addActionListener(e -> parentFrame.showNoFamilyPanel());
        topBar.add(backBtn, BorderLayout.WEST);
        add(topBar, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Configuración del grupo", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 32));
        gbc.gridy = 0; gbc.gridwidth = 2;
        center.add(title, gbc);

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
        card.setBorder(new EmptyBorder(30, 50, 30, 50));
        card.setPreferredSize(new Dimension(700, 550));

        JLabel infoLabel = new JLabel("Información básica", SwingConstants.CENTER);
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(infoLabel);
        card.add(Box.createVerticalStrut(15));

        nameField = createStyledTextField("Nombre del grupo*");
        passField = createStyledPasswordField("Contraseña del grupo*");
        descField = createStyledTextField("Descripción (Opcional)");

        card.add(nameField);
        card.add(Box.createVerticalStrut(12));
        card.add(passField);
        card.add(Box.createVerticalStrut(12));
        card.add(descField);

        card.add(Box.createVerticalStrut(25));
        JLabel typeLabel = new JLabel("Tipo de grupo", SwingConstants.CENTER);
        typeLabel.setForeground(Color.WHITE);
        typeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        typeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(typeLabel);
        card.add(Box.createVerticalStrut(10));

        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        typePanel.setOpaque(false);
        typePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        String[] types = {"Pareja", "Familia", "Amigos", "Otros"};
        typeBtns = new JButton[types.length];
        for (int i = 0; i < types.length; i++) {
            final String t = types[i];
            typeBtns[i] = new JButton(t);
            typeBtns[i].setPreferredSize(new Dimension(100, 40));
            typeBtns[i].setBackground(GrouPayApp.COLOR_BUTTON);
            typeBtns[i].setForeground(Color.WHITE);
            typeBtns[i].setFocusPainted(false);
            typeBtns[i].addActionListener(e -> {
                selectedType = t;
                updateTypeSelection();
            });
            typePanel.add(typeBtns[i]);
        }
        card.add(typePanel);

        card.add(Box.createVerticalStrut(25));
        JLabel permLabel = new JLabel("¿Quién puede retirar dinero?", SwingConstants.CENTER);
        permLabel.setForeground(Color.WHITE);
        permLabel.setFont(new Font("Arial", Font.BOLD, 16));
        permLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(permLabel);
        card.add(Box.createVerticalStrut(10));

        JPanel permPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        permPanel.setOpaque(false);
        permPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnTodos = createSelectionBtn("Todos los Integrantes");
        btnAdmin = createSelectionBtn("Solo el Admin");
        
        btnTodos.addActionListener(e -> { permitirValue = 1; updatePermSelection(); });
        btnAdmin.addActionListener(e -> { permitirValue = 2; updatePermSelection(); });
        
        permPanel.add(btnTodos);
        permPanel.add(btnAdmin);
        card.add(permPanel);

        gbc.gridy = 1;
        gbc.insets = new Insets(20, 0, 30, 0);
        center.add(card, gbc);

        JButton createBtn = new JButton("Crear nuevo grupo");
        createBtn.setPreferredSize(new Dimension(400, 50));
        createBtn.setBackground(GrouPayApp.COLOR_BUTTON);
        createBtn.setForeground(Color.WHITE);
        createBtn.setFont(new Font("Arial", Font.BOLD, 18));
        createBtn.setBorder(new LineBorder(GrouPayApp.COLOR_ACCENT, 1));
        createBtn.addActionListener(e -> handleCreate());
        
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        center.add(createBtn, gbc);

        add(center, BorderLayout.CENTER);
        
        updatePermSelection();
    }

    private void updateTypeSelection() {
        for (JButton b : typeBtns) {
            if (b.getText().equals(selectedType)) {
                b.setBackground(COLOR_TYPE_SELECTED);
                b.setBorder(new LineBorder(GrouPayApp.COLOR_ACCENT, 2));
            } else {
                b.setBackground(GrouPayApp.COLOR_BUTTON);
                b.setBorder(null);
            }
        }
    }

    private void updatePermSelection() {
        btnTodos.setBackground(permitirValue == 1 ? COLOR_TYPE_SELECTED : GrouPayApp.COLOR_BUTTON);
        btnTodos.setBorder(permitirValue == 1 ? new LineBorder(GrouPayApp.COLOR_ACCENT, 2) : null);
        btnAdmin.setBackground(permitirValue == 2 ? COLOR_TYPE_SELECTED : GrouPayApp.COLOR_BUTTON);
        btnAdmin.setBorder(permitirValue == 2 ? new LineBorder(GrouPayApp.COLOR_ACCENT, 2) : null);
    }

    private void handleCreate() {
        String name = nameField.getText();
        String pass = new String(passField.getPassword());
        String desc = descField.getText();
        
        if (isPlaceholder(name, "Nombre del grupo*") || isPlaceholder(pass, "Contraseña del grupo*") || selectedType.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor complete el nombre, contraseña y tipo de grupo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String actualDesc = (isPlaceholder(desc, "Descripción (Opcional)") ? "" : desc);
        String finalDesc = actualDesc + " - " + selectedType;

        if (parentFrame.getUserDAO().createFamily(parentFrame.getCurrentUser(), name, pass, finalDesc, permitirValue)) {
            JOptionPane.showMessageDialog(this, "¡Grupo '" + name + "' creado con éxito!");
            parentFrame.showHomePanel();
        } else {
            JOptionPane.showMessageDialog(this, "El nombre de grupo ya existe o hubo un error técnico.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField(placeholder);
        field.setMaximumSize(new Dimension(600, 45));
        field.setBackground(GrouPayApp.COLOR_INPUT_BG);
        field.setForeground(Color.GRAY);
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(GrouPayApp.COLOR_ACCENT, 2),
            new EmptyBorder(5, 15, 5, 15)
        ));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
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
        return field;
    }

    private JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField(placeholder);
        field.setEchoChar((char) 0);
        field.setMaximumSize(new Dimension(600, 45));
        field.setBackground(GrouPayApp.COLOR_INPUT_BG);
        field.setForeground(Color.GRAY);
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(GrouPayApp.COLOR_ACCENT, 2),
            new EmptyBorder(5, 15, 5, 15)
        ));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
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
        return field;
    }

    private JButton createSelectionBtn(String text) {
        JButton b = new JButton(text);
        b.setPreferredSize(new Dimension(250, 40)); 
        b.setBackground(GrouPayApp.COLOR_BUTTON);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        return b;
    }

    private boolean isPlaceholder(String text, String placeholder) {
        return text.isEmpty() || text.equals(placeholder);
    }
}
