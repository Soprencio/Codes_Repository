package com.mycompany.groupay;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

public class TransferPanel extends JPanel {

    public enum Mode { TRANSFER, DEPOSIT, WITHDRAW }

    private GrouPayApp parentFrame;
    private User currentUser;
    private Mode currentMode = Mode.TRANSFER;
    
    private JButton btnIntegrante, btnFondo;
    private JPanel destContent; 
    private JComboBox<String> comboDestinatario;
    private JTextField emailField;
    private JLabel lblModoTransferencia;
    
    private JTextField montoField;
    private JTextField conceptoField;
    private JPanel statusPanel;
    private JLabel statusLabel;
    private JButton actionBtn;
    
    private boolean isEmailMode = false;
    private boolean isConfirmed = false;
    private boolean isToFamilyFund = false;
    private User receiverUser = null;
    private List<User> familyMembers;

    public TransferPanel(GrouPayApp parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout());
        setBackground(GrouPayApp.COLOR_BG);
    }

    public void setMode(Mode mode, User user) {
        this.currentMode = mode;
        this.currentUser = user;
        this.isConfirmed = false;
        this.isEmailMode = false;
        this.receiverUser = null;
        
        if (mode == Mode.DEPOSIT) this.isToFamilyFund = true;
        else if (mode == Mode.WITHDRAW) this.isToFamilyFund = false;
        else this.isToFamilyFund = false;

        if (currentUser.getIdFamilia() != null) {
            this.familyMembers = parentFrame.getUserDAO().getFamilyMembers(currentUser.getIdFamilia());
        }

        removeAll();
        initUI();
        revalidate();
        repaint();
    }

    private void initUI() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(20, 20, 10, 20));

        JButton backBtn = new JButton("◀");
        backBtn.setBackground(GrouPayApp.COLOR_BUTTON);
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("Arial", Font.BOLD, 18));
        backBtn.setPreferredSize(new Dimension(50, 50));
        backBtn.addActionListener(e -> {
            if (currentMode == Mode.TRANSFER) parentFrame.showHomePanel();
            else parentFrame.showFamilyDashboard();
        });
        
        JPanel backWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backWrapper.setOpaque(false);
        backWrapper.add(backBtn);
        headerPanel.add(backWrapper, BorderLayout.WEST);

        String titleText = "Transferir dinero";
        if (currentMode == Mode.DEPOSIT) titleText = "Depositar dinero";
        if (currentMode == Mode.WITHDRAW) titleText = "Retirar dinero";

        JLabel title = new JLabel(titleText, SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 32));
        headerPanel.add(title, BorderLayout.CENTER);

        JPanel dummy = new JPanel();
        dummy.setPreferredSize(new Dimension(50, 50));
        dummy.setOpaque(false);
        headerPanel.add(dummy, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        JPanel centerContainer = new JPanel(new GridBagLayout());
        centerContainer.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;

        if (currentMode == Mode.TRANSFER) {
            JPanel togglePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
            togglePanel.setOpaque(false);
            btnIntegrante = createToggleBtn("A integrante", !isToFamilyFund);
            btnFondo = createToggleBtn("Al fondo familiar", isToFamilyFund);
            
            btnIntegrante.addActionListener(e -> { isToFamilyFund = false; updateModeToggles(); });
            btnFondo.addActionListener(e -> { isToFamilyFund = true; updateModeToggles(); });

            togglePanel.add(btnIntegrante);
            togglePanel.add(btnFondo);
            
            gbc.gridx = 0; gbc.gridy = 0;
            gbc.insets = new Insets(0, 0, 25, 0); 
            centerContainer.add(togglePanel, gbc);
        }

        JPanel mainCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(GrouPayApp.COLOR_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        mainCard.setLayout(new BoxLayout(mainCard, BoxLayout.Y_AXIS));
        mainCard.setOpaque(false);
        mainCard.setBorder(new EmptyBorder(25, 50, 25, 50));
        mainCard.setPreferredSize(new Dimension(650, 480));

        JLabel destLabel = new JLabel("Destinatario");
        destLabel.setForeground(Color.WHITE);
        destLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        destLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainCard.add(destLabel);
        mainCard.add(Box.createVerticalStrut(10));

        destContent = new JPanel(new CardLayout());
        destContent.setOpaque(false);
        destContent.setMaximumSize(new Dimension(500, 45));

        comboDestinatario = new JComboBox<>();
        emailField = createStyledTextField("Escribir email...");
        
        destContent.add(comboDestinatario, "COMBO");
        destContent.add(emailField, "EMAIL");
        mainCard.add(destContent);
        
        lblModoTransferencia = new JLabel(isEmailMode ? "A cuenta en la familia" : "A cuenta fuera de la familia");
        lblModoTransferencia.setForeground(GrouPayApp.COLOR_BUTTON);
        lblModoTransferencia.setFont(new Font("Arial", Font.BOLD, 14));
        lblModoTransferencia.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblModoTransferencia.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblModoTransferencia.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { toggleEmailMode(); }
        });
        
        if (currentMode == Mode.TRANSFER) {
            mainCard.add(Box.createVerticalStrut(10));
            mainCard.add(lblModoTransferencia);
        }

        mainCard.add(Box.createVerticalStrut(20));
        JLabel montoLabel = new JLabel("Monto");
        montoLabel.setForeground(Color.WHITE);
        montoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainCard.add(montoLabel);

        montoField = createStyledTextField("Ingresar monto*");
        mainCard.add(montoField);
        
        JLabel balanceText = new JLabel();
        if (currentMode == Mode.WITHDRAW) {
            Family f = parentFrame.getUserDAO().getFamilyInfo(currentUser.getIdFamilia());
            balanceText.setText("Fondo disponible: $" + String.format("%,d", (int)f.getFondoFam()).replace(",", "."));
        } else {
            balanceText.setText("Tu saldo: $" + String.format("%,d", (int)currentUser.getSaldo()).replace(",", "."));
        }
        balanceText.setForeground(GrouPayApp.COLOR_TEXT_SECONDARY);
        balanceText.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainCard.add(balanceText);

        mainCard.add(Box.createVerticalStrut(15));
        JLabel conceptoLabel = new JLabel("Concepto (Opcional)");
        conceptoLabel.setForeground(Color.WHITE);
        conceptoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainCard.add(conceptoLabel);

        conceptoField = createStyledTextField("Ej: Motivo del movimiento");
        mainCard.add(conceptoField);

        mainCard.add(Box.createVerticalStrut(20));
        statusPanel = new JPanel(new BorderLayout());
        statusPanel.setMaximumSize(new Dimension(500, 40));
        statusPanel.setVisible(false);
        statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statusPanel.add(statusLabel);
        mainCard.add(statusPanel);

        mainCard.add(Box.createVerticalStrut(15));
        String btnText = (currentMode == Mode.TRANSFER) ? "Transferir" : (currentMode == Mode.DEPOSIT ? "Depositar" : "Retirar");
        actionBtn = createStyledButton(btnText, GrouPayApp.COLOR_BUTTON);
        mainCard.add(actionBtn);

        gbc.gridy = 1; gbc.insets = new Insets(0, 10, 10, 10);
        centerContainer.add(mainCard, gbc);
        add(centerContainer, BorderLayout.CENTER);

        actionBtn.addActionListener(e -> handleAction());
        
        setupComboBox();
        updateModeToggles();
    }

    private void setupComboBox() {
        comboDestinatario.removeAllItems();
        if (isToFamilyFund) {
            comboDestinatario.addItem("Fondo Familiar");
            comboDestinatario.setEnabled(false);
        } else {
            comboDestinatario.setEnabled(true);
            if (familyMembers != null) {
                for (User m : familyMembers) {
                    if (currentMode == Mode.WITHDRAW || m.getIdCuenta() != currentUser.getIdCuenta()) {
                        comboDestinatario.addItem(m.getEmail());
                    }
                }
            }
        }
    }

    private void updateModeToggles() {
        if (currentMode != Mode.TRANSFER) return;
        btnIntegrante.setBackground(!isToFamilyFund ? GrouPayApp.COLOR_BUTTON : GrouPayApp.COLOR_CARD);
        btnFondo.setBackground(isToFamilyFund ? GrouPayApp.COLOR_BUTTON : GrouPayApp.COLOR_CARD);
        lblModoTransferencia.setVisible(!isToFamilyFund);
        if (isToFamilyFund) isEmailMode = false;
        setupComboBox();
        syncCardLayout();
    }

    private void syncCardLayout() {
        CardLayout cl = (CardLayout)destContent.getLayout();
        cl.show(destContent, isEmailMode ? "EMAIL" : "COMBO");
        lblModoTransferencia.setText(isEmailMode ? "A cuenta en la familia" : "A cuenta fuera de la familia");
    }

    private void toggleEmailMode() {
        if (isToFamilyFund) return;
        isEmailMode = !isEmailMode;
        isConfirmed = false;
        actionBtn.setText("Transferir");
        statusPanel.setVisible(false);
        syncCardLayout();
    }

    private void handleAction() {
        if (!isConfirmed) validateStep();
        else executeAction();
    }

    private void validateStep() {
        String dest = isEmailMode ? emailField.getText() : (String)comboDestinatario.getSelectedItem();
        String amtStr = montoField.getText();

        if ((dest == null || dest.isEmpty() || dest.equals("Escribir email...")) && !isToFamilyFund) {
            showStatus("✖ Seleccione un destinatario", Color.RED); return;
        }
        
        double amount;
        try { amount = Double.parseDouble(amtStr); } catch (Exception e) { showStatus("✖ Monto inválido", Color.RED); return; }

        if (currentMode == Mode.WITHDRAW) {
            Family f = parentFrame.getUserDAO().getFamilyInfo(currentUser.getIdFamilia());
            if (f.getFondoFam() < amount) { showStatus("✖ Fondos insuficientes", Color.RED); return; }
            receiverUser = parentFrame.getUserDAO().findUserByEmail(dest);
        } else {
            if (currentUser.getSaldo() < amount) { showStatus("✖ Fondos insuficientes", Color.RED); return; }
            if (!isToFamilyFund) {
                receiverUser = parentFrame.getUserDAO().findUserByEmail(dest);
                if (receiverUser == null) { showStatus("✖ Cuenta inexistente", Color.RED); return; }
            }
        }

        showStatus("✔ Datos validados. Pulse confirmar.", new Color(0xCCFFCC));
        statusLabel.setForeground(new Color(0x006600));
        actionBtn.setText("Confirmar " + actionBtn.getText());
        isConfirmed = true;
    }

    private void executeAction() {
        double amount = Double.parseDouble(montoField.getText());
        boolean success = false;
        if (currentMode == Mode.WITHDRAW) {
            success = parentFrame.getUserDAO().processWithdrawal(currentUser, receiverUser, amount);
        } else {
            success = parentFrame.getUserDAO().processTransfer(currentUser, receiverUser, amount, isToFamilyFund);
        }

        if (success) {
            JOptionPane.showMessageDialog(this, "Operación Exitosa");
            if (currentMode != Mode.WITHDRAW) currentUser.setSaldo(currentUser.getSaldo() - amount);
            parentFrame.showHomePanel();
        } else {
            showStatus("✖ Error al procesar", Color.RED);
        }
    }

    private void showStatus(String msg, Color bg) {
        statusPanel.setVisible(true); statusPanel.setBackground(bg);
        statusLabel.setText(msg); revalidate();
    }

    private JButton createToggleBtn(String text, boolean active) {
        JButton b = new JButton(text);
        b.setPreferredSize(new Dimension(220, 45));
        b.setBackground(active ? GrouPayApp.COLOR_BUTTON : GrouPayApp.COLOR_CARD);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Arial", Font.BOLD, 14));
        b.setFocusPainted(false);
        b.setBorder(new LineBorder(GrouPayApp.COLOR_ACCENT, 1));
        return b;
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField(placeholder);
        field.setPreferredSize(new Dimension(500, 40));
        field.setMaximumSize(new Dimension(500, 40));
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

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(350, 45));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 18));
        btn.setFocusPainted(false);
        btn.setBorder(new LineBorder(GrouPayApp.COLOR_ACCENT, 1));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        return btn;
    }
}
