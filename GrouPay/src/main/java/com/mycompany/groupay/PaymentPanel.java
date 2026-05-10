package com.mycompany.groupay;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

public class PaymentPanel extends JPanel {

    private GrouPayApp parentFrame;
    private User currentUser;
    
    private JComboBox<PaymentMethod> comboMethod;
    private JComboBox<Category> comboCategory;
    private JTextField amountField;
    private JTextField descField;
    private JButton confirmBtn;

    public PaymentPanel(GrouPayApp parentFrame) {
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
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setBorder(new EmptyBorder(30, 40, 10, 40));

        JButton backBtn = new JButton("◀");
        backBtn.setBackground(GrouPayApp.COLOR_BUTTON);
        backBtn.setForeground(Color.WHITE);
        backBtn.setPreferredSize(new Dimension(50, 50));
        backBtn.addActionListener(e -> parentFrame.showHomePanel());
        topBar.add(backBtn, BorderLayout.WEST);

        JLabel title = new JLabel("Realizar Pago", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 32));
        topBar.add(title, BorderLayout.CENTER);

        JPanel dummy = new JPanel();
        dummy.setPreferredSize(new Dimension(50, 50));
        dummy.setOpaque(false);
        headerPanelAdd(topBar, dummy, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

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
        card.setBorder(new EmptyBorder(30, 50, 30, 50));
        card.setPreferredSize(new Dimension(650, 480));

        comboMethod = new JComboBox<>();
        comboMethod.setPreferredSize(new Dimension(500, 45));
        comboMethod.setMaximumSize(new Dimension(500, 45));
        List<PaymentMethod> methods = parentFrame.getUserDAO().getPaymentMethods();
        for (PaymentMethod m : methods) comboMethod.addItem(m);

        comboCategory = new JComboBox<>();
        comboCategory.setPreferredSize(new Dimension(500, 45));
        comboCategory.setMaximumSize(new Dimension(500, 45));
        List<Category> categories = parentFrame.getUserDAO().getPaymentCategories();
        for (Category c : categories) comboCategory.addItem(c);

        amountField = createStyledTextField("Monto*");
        descField = createStyledTextField("Descripción*");

        card.add(createLabel("Método de pago"));
        card.add(comboMethod);
        card.add(Box.createVerticalStrut(20));
        
        card.add(createLabel("Categoría"));
        card.add(comboCategory);
        card.add(Box.createVerticalStrut(20));

        card.add(createLabel("Monto a pagar"));
        card.add(amountField);
        card.add(Box.createVerticalStrut(20));

        card.add(createLabel("Descripción"));
        card.add(descField);
        card.add(Box.createVerticalStrut(30));

        confirmBtn = new JButton("Confirmar Pago");
        confirmBtn.setPreferredSize(new Dimension(300, 50));
        confirmBtn.setBackground(GrouPayApp.COLOR_BUTTON);
        confirmBtn.setForeground(Color.WHITE);
        confirmBtn.setFont(new Font("Arial", Font.BOLD, 18));
        confirmBtn.setBorder(new LineBorder(GrouPayApp.COLOR_ACCENT, 1));
        confirmBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmBtn.addActionListener(e -> handlePayment());
        card.add(confirmBtn);

        center.add(card, gbc);
        add(center, BorderLayout.CENTER);
    }

    private void headerPanelAdd(JPanel parent, Component c, String pos) { parent.add(c, pos); }

    private void handlePayment() {
        String amtStr = amountField.getText();
        String desc = descField.getText();
        PaymentMethod method = (PaymentMethod) comboMethod.getSelectedItem();
        Category cat = (Category) comboCategory.getSelectedItem();

        if (amtStr.isEmpty() || amtStr.equals("Monto*") || desc.isEmpty() || desc.equals("Descripción*")) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos");
            return;
        }

        try {
            double amount = Double.parseDouble(amtStr);
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "El monto debe ser mayor a 0");
                return;
            }
            if (currentUser.getSaldo() < amount) {
                JOptionPane.showMessageDialog(this, "Saldo insuficiente");
                return;
            }

            if (parentFrame.getUserDAO().processPayment(currentUser, method.getIdMetodo(), cat.getIdCategoria(), amount, desc)) {
                JOptionPane.showMessageDialog(this, "Pago realizado con éxito");
                currentUser.setSaldo(currentUser.getSaldo() - amount);
                parentFrame.showHomePanel();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Monto inválido");
        }
    }

    private JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Arial", Font.BOLD, 14));
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        return l;
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
