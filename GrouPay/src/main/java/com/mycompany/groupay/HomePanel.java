package com.mycompany.groupay;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

public class HomePanel extends JPanel {

    private GrouPayApp parentFrame;
    private User currentUser;
    private JLabel balanceLabel;
    private JPanel movementsPanel;
    private boolean isHidden = false;

    public HomePanel(GrouPayApp parentFrame) {
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
        topBar.setBackground(GrouPayApp.COLOR_BG);
        topBar.setBorder(new EmptyBorder(15, 30, 15, 30));

        JLabel userLabel = new JLabel("👤 " + currentUser.getNombre() + " " + currentUser.getApellido());
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Arial", Font.BOLD, 18));
        topBar.add(userLabel, BorderLayout.WEST);

        JPanel navIcons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 40, 0));
        navIcons.setOpaque(false);
        navIcons.add(createNavIcon("Inicio", true));
        navIcons.add(createNavIcon("Familia", false));
        navIcons.add(createNavIcon("Analisis", false));
        topBar.add(navIcons, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 30, 10, 30);
        gbc.fill = GridBagConstraints.BOTH;

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);

        JLabel greetingLabel = new JLabel("Hola, " + currentUser.getNombre());
        greetingLabel.setForeground(GrouPayApp.COLOR_TEXT_SECONDARY);
        greetingLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        greetingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(greetingLabel);

        JLabel myFinancesLabel = new JLabel("Mis finanzas");
        myFinancesLabel.setForeground(Color.WHITE);
        myFinancesLabel.setFont(new Font("Arial", Font.BOLD, 32));
        myFinancesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        leftPanel.add(myFinancesLabel);
        leftPanel.add(Box.createVerticalStrut(25));

        JPanel balanceCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(GrouPayApp.COLOR_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                g2.dispose();
            }
        };
        balanceCard.setLayout(new BoxLayout(balanceCard, BoxLayout.Y_AXIS));
        balanceCard.setOpaque(false);
        balanceCard.setBorder(new EmptyBorder(40, 20, 40, 20));
        balanceCard.setPreferredSize(new Dimension(500, 350));
        balanceCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel personalBalanceText = new JLabel("Tu saldo personal", SwingConstants.CENTER);
        personalBalanceText.setForeground(GrouPayApp.COLOR_BUTTON);
        personalBalanceText.setFont(new Font("Arial", Font.PLAIN, 18));
        personalBalanceText.setAlignmentX(Component.CENTER_ALIGNMENT);
        balanceCard.add(personalBalanceText);

        JPanel balanceRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        balanceRow.setOpaque(false);
        balanceLabel = new JLabel(formatBalance(currentUser.getSaldo()), SwingConstants.CENTER);
        balanceLabel.setForeground(GrouPayApp.COLOR_ACCENT);
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 56));
        JButton eyeBtn = new JButton("👁");
        eyeBtn.setPreferredSize(new Dimension(60, 40));
        eyeBtn.setBackground(null); eyeBtn.setBorder(null); eyeBtn.setForeground(Color.WHITE);
        eyeBtn.setFont(new Font("Arial", Font.PLAIN, 24));
        eyeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        eyeBtn.addActionListener(e -> togglePrivacy());
        balanceRow.add(balanceLabel); balanceRow.add(eyeBtn);
        balanceCard.add(balanceRow);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(createActionBtn("Transferir", GrouPayApp.COLOR_BUTTON));
        buttonPanel.add(createActionBtn("Ingresar", GrouPayApp.COLOR_BUTTON));
        buttonPanel.add(createActionBtn("Pagar", GrouPayApp.COLOR_BUTTON));
        
        balanceCard.add(Box.createVerticalStrut(40));
        balanceCard.add(buttonPanel);
        leftPanel.add(balanceCard);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.6; gbc.weighty = 1.0;
        centerPanel.add(leftPanel, gbc);

        JPanel rightPanel = new JPanel(new BorderLayout(0, 20));
        rightPanel.setOpaque(false);
        JLabel movementsTitle = new JLabel("Últimos movimientos");
        movementsTitle.setForeground(Color.WHITE);
        movementsTitle.setFont(new Font("Arial", Font.BOLD, 24));
        rightPanel.add(movementsTitle, BorderLayout.NORTH);
        movementsPanel = new JPanel();
        movementsPanel.setLayout(new BoxLayout(movementsPanel, BoxLayout.Y_AXIS));
        movementsPanel.setOpaque(false);
        JScrollPane scrollPane = new JScrollPane(movementsPanel);
        scrollPane.setOpaque(false); scrollPane.getViewport().setOpaque(false); scrollPane.setBorder(null);
        rightPanel.add(scrollPane, BorderLayout.CENTER);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.4;
        centerPanel.add(rightPanel, gbc);
        add(centerPanel, BorderLayout.CENTER);

        loadTransactions();
    }

    private String formatBalance(double amount) {
        if (isHidden) return "$***.***";
        return "$" + String.format("%,d", (int)amount).replace(",", ".");
    }

    private void togglePrivacy() {
        isHidden = !isHidden;
        balanceLabel.setText(formatBalance(currentUser.getSaldo()));
    }

    private void loadTransactions() {
        movementsPanel.removeAll();
        List<Transaction> transactions = parentFrame.getUserDAO().getRecentTransactions(currentUser.getIdCuenta());
        for (Transaction t : transactions) {
            movementsPanel.add(createTransactionCard(t));
            movementsPanel.add(Box.createVerticalStrut(15));
        }
        movementsPanel.revalidate();
    }

    private JPanel createTransactionCard(Transaction t) {
        JPanel card = new JPanel(new BorderLayout(20, 0));
        card.setOpaque(false);
        card.setMaximumSize(new Dimension(400, 85));

        JPanel iconBox = new JPanel();
        iconBox.setPreferredSize(new Dimension(55, 55));
        iconBox.setBackground(new Color(0x555555)); 
        card.add(iconBox, BorderLayout.WEST);

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setOpaque(false);
        
        String[] catParts = t.getCategoria().split(":");
        String catName = catParts[0];
        int catId = Integer.parseInt(catParts[1]);
        
        String titleText = "";
        if (catId == 1) titleText = "Transfer. a " + t.getDescripcion();
        else if (catId == 2) {
            if (t.getDescripcion().equals(currentUser.getNombre() + " " + currentUser.getApellido())) titleText = "Retiro del fondo";
            else titleText = "Retiro del fondo a " + t.getDescripcion();
        } else {
            titleText = catName + ". " + t.getDescripcion();
        }
        
        JLabel title = new JLabel("<html><body style='width: 200px;'>" + titleText + "</body></html>");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 13)); 
        
        JLabel subtitle = new JLabel(t.getFecha().toString() + ", " + t.getMetodoNombre());
        subtitle.setForeground(GrouPayApp.COLOR_TEXT_SECONDARY);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 11));

        infoPanel.add(title, BorderLayout.CENTER);
        infoPanel.add(subtitle, BorderLayout.SOUTH);
        card.add(infoPanel, BorderLayout.CENTER);

        JLabel amount = new JLabel((t.getMonto() > 0 ? "+$" : "-$") + String.format("%,.0f", Math.abs(t.getMonto())).replace(",", "."));
        amount.setForeground(t.getMonto() > 0 ? new Color(0xAAFFAA) : new Color(0xFFAAAA));
        amount.setFont(new Font("Arial", Font.BOLD, 14));
        card.add(amount, BorderLayout.EAST);

        return card;
    }

    private JPanel createNavIcon(String text, boolean active) {
        JPanel p = new JPanel(new BorderLayout(0, 5));
        p.setOpaque(false);
        JButton box = new JButton();
        box.setPreferredSize(new Dimension(35, 35));
        box.setBackground(active ? GrouPayApp.COLOR_ACCENT : GrouPayApp.COLOR_CARD);
        box.setBorder(null); box.setFocusPainted(false);
        if(text.equals("Familia")) box.addActionListener(e -> parentFrame.handleFamilyNavigation());
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setForeground(active ? GrouPayApp.COLOR_ACCENT : Color.WHITE);
        l.setFont(new Font("Arial", Font.PLAIN, 14));
        p.add(box, BorderLayout.NORTH); p.add(l, BorderLayout.SOUTH);
        return p;
    }

    private JPanel createActionBtn(String text, Color bg) {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setOpaque(false);
        JButton btn = new JButton();
        btn.setPreferredSize(new Dimension(75, 75));
        btn.setBackground(bg); btn.setFocusPainted(false); btn.setBorder(new LineBorder(GrouPayApp.COLOR_ACCENT, 2));
        
        if(text.equals("Transferir")) btn.addActionListener(e -> parentFrame.showTransferPanel(TransferPanel.Mode.TRANSFER));
        else if(text.equals("Pagar")) btn.addActionListener(e -> parentFrame.showPaymentPanel());
        else if(text.equals("Ingresar")) btn.addActionListener(e -> parentFrame.showDepositPanel());
        
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setForeground(Color.WHITE); l.setFont(new Font("Arial", Font.BOLD, 14));
        p.add(btn, BorderLayout.NORTH); p.add(l, BorderLayout.SOUTH);
        return p;
    }
}
