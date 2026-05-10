package com.mycompany.groupay;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class DepositPanel extends JPanel {

    private GrouPayApp parentFrame;
    private User currentUser;
    private JTextField amountField;
    private JButton confirmBtn;

    public DepositPanel(GrouPayApp parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout());
        setBackground(GrouPayApp.COLOR_BG);
    }

    public void setUser(User user) {
        this.currentUser = user;
        removeAll();
        initUI();
        revalidate();
        repaint();
    }

    private void initUI() {
        // Header
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setBorder(new EmptyBorder(30, 40, 10, 40));

        JButton backBtn = new JButton("◀");
        backBtn.setBackground(GrouPayApp.COLOR_BUTTON);
        backBtn.setForeground(Color.WHITE);
        backBtn.setPreferredSize(new Dimension(50, 50));
        backBtn.addActionListener(e -> parentFrame.showHomePanel());
        topBar.add(backBtn, BorderLayout.WEST);

        JLabel title = new JLabel("Ingresar Dinero", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 32));
        topBar.add(title, BorderLayout.CENTER);

        JPanel dummy = new JPanel();
        dummy.setPreferredSize(new Dimension(50, 50));
        dummy.setOpaque(false);
        topBar.add(dummy, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        // Center
        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(GrouPayApp.COLOR_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(40, 50, 40, 50));
        card.setPreferredSize(new Dimension(650, 300));

        JLabel info = new JLabel("¿Cuánto dinero querés ingresar?", SwingConstants.CENTER);
        info.setForeground(Color.WHITE);
        info.setFont(new Font("Arial", Font.BOLD, 18));
        info.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(info);
        card.add(Box.createVerticalStrut(30));

        amountField = createStyledTextField("Monto*");
        card.add(amountField);
        card.add(Box.createVerticalStrut(30));

        confirmBtn = new JButton("Confirmar Ingreso");
        confirmBtn.setPreferredSize(new Dimension(350, 50));
        confirmBtn.setBackground(GrouPayApp.COLOR_BUTTON);
        confirmBtn.setForeground(Color.WHITE);
        confirmBtn.setFont(new Font("Arial", Font.BOLD, 18));
        confirmBtn.setBorder(new LineBorder(GrouPayApp.COLOR_ACCENT, 1));
        confirmBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmBtn.addActionListener(e -> handleDeposit());
        card.add(confirmBtn);

        center.add(card, gbc);
        add(center, BorderLayout.CENTER);
    }

    private void handleDeposit() {
        String amtStr = amountField.getText();
        if (amtStr.isEmpty() || amtStr.equals("Monto*")) {
            JOptionPane.showMessageDialog(this, "Ingrese un monto");
            return;
        }

        try {
            double amount = Double.parseDouble(amtStr);
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "El monto debe ser mayor a 0");
                return;
            }

            if (parentFrame.getUserDAO().processDeposit(currentUser, amount)) {
                JOptionPane.showMessageDialog(this, "Dinero ingresado con éxito");
                currentUser.setSaldo(currentUser.getSaldo() + amount);
                parentFrame.showHomePanel();
            } else {
                JOptionPane.showMessageDialog(this, "Error al procesar el ingreso", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Monto inválido");
        }
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField(placeholder);
        field.setMaximumSize(new Dimension(500, 45));
        field.setBackground(GrouPayApp.COLOR_INPUT_BG);
        field.setForeground(Color.GRAY);
        field.setBorder(BorderFactory.createCompoundBorder(new LineBorder(GrouPayApp.COLOR_ACCENT, 2), new EmptyBorder(5, 15, 5, 15)));
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) { if (field.getText().equals(placeholder)) { field.setText(""); field.setForeground(Color.BLACK); } }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) { if (field.getText().isEmpty()) { field.setText(placeholder); field.setForeground(Color.GRAY); } }
        });
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        return field;
    }
}
