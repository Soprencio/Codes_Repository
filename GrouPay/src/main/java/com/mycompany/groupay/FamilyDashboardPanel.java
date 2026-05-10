package com.mycompany.groupay;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

public class FamilyDashboardPanel extends JPanel {

    private GrouPayApp parentFrame;
    private User currentUser;
    private Family currentFamily;
    private boolean isHidden = false;
    
    private JLabel fundLabel;
    private JPanel movementsPanel;
    private JPanel membersPanel;
    private JButton btnDeposit, btnWithdraw;

    public FamilyDashboardPanel(GrouPayApp parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout());
        setBackground(GrouPayApp.COLOR_BG);
    }

    public void setUser(User user) {
        this.currentUser = user;
        this.currentFamily = parentFrame.getUserDAO().getFamilyInfo(user.getIdFamilia());
        removeAll();
        if (currentFamily != null) initUI();
        revalidate(); repaint();
    }

    private void initUI() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setBorder(new EmptyBorder(25, 40, 0, 40));
        JButton backBtn = new JButton("◀");
        backBtn.setPreferredSize(new Dimension(50, 50));
        backBtn.setBackground(GrouPayApp.COLOR_BUTTON); backBtn.setForeground(Color.WHITE);
        backBtn.setBorder(new LineBorder(GrouPayApp.COLOR_ACCENT, 1));
        backBtn.addActionListener(e -> parentFrame.showHomePanel());
        topBar.add(backBtn, BorderLayout.WEST);
        JLabel title = new JLabel("Familia " + currentFamily.getNombre(), SwingConstants.CENTER);
        title.setForeground(Color.WHITE); title.setFont(new Font("Arial", Font.BOLD, 32));
        topBar.add(title, BorderLayout.CENTER);
        JLabel plusIcon = new JLabel("+", SwingConstants.CENTER);
        plusIcon.setForeground(GrouPayApp.COLOR_ACCENT); plusIcon.setFont(new Font("Arial", Font.BOLD, 40));
        plusIcon.setPreferredSize(new Dimension(50, 50));
        topBar.add(plusIcon, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 30, 20, 30); gbc.fill = GridBagConstraints.BOTH;

        JPanel leftPanel = new JPanel(new BorderLayout(0, 15));
        leftPanel.setOpaque(false);
        JLabel movTitle = new JLabel("Últimos movimientos");
        movTitle.setForeground(Color.WHITE); movTitle.setFont(new Font("Arial", Font.BOLD, 22));
        leftPanel.add(movTitle, BorderLayout.NORTH);
        movementsPanel = new JPanel();
        movementsPanel.setLayout(new BoxLayout(movementsPanel, BoxLayout.Y_AXIS));
        movementsPanel.setOpaque(false);
        JScrollPane scrollMov = new JScrollPane(movementsPanel);
        scrollMov.setOpaque(false); scrollMov.getViewport().setOpaque(false); scrollMov.setBorder(null);
        leftPanel.add(scrollMov, BorderLayout.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.5; gbc.weighty = 1.0;
        center.add(leftPanel, gbc);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setOpaque(false);
        JPanel fundCard = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create(); g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(GrouPayApp.COLOR_CARD); g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); g2.dispose();
            }
        };
        fundCard.setLayout(new BoxLayout(fundCard, BoxLayout.Y_AXIS));
        fundCard.setOpaque(false); fundCard.setBorder(new EmptyBorder(30, 40, 30, 40));
        fundCard.setMaximumSize(new Dimension(550, 220));
        JLabel fundTitle = new JLabel("Fondo compartido", SwingConstants.CENTER);
        fundTitle.setForeground(GrouPayApp.COLOR_BUTTON); fundTitle.setFont(new Font("Arial", Font.PLAIN, 18));
        fundTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        fundCard.add(fundTitle);
        JPanel fundValuePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        fundValuePanel.setOpaque(false);
        fundLabel = new JLabel("$" + String.format("%,.0f", currentFamily.getFondoFam()).replace(",", "."));
        fundLabel.setForeground(GrouPayApp.COLOR_ACCENT); fundLabel.setFont(new Font("Arial", Font.BOLD, 48));
        JButton eyeBtn = new JButton("👁");
        eyeBtn.setPreferredSize(new Dimension(60, 40)); eyeBtn.setBackground(null); eyeBtn.setBorder(null);
        eyeBtn.setForeground(Color.WHITE); eyeBtn.setFont(new Font("Arial", Font.PLAIN, 24));
        eyeBtn.addActionListener(e -> togglePrivacy());
        fundValuePanel.add(fundLabel); fundValuePanel.add(eyeBtn);
        fundCard.add(fundValuePanel);
        JPanel actionBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        actionBtns.setOpaque(false);
        btnDeposit = createActionBtn("Depositar");
        btnWithdraw = createActionBtn("Retirar");
        btnDeposit.addActionListener(e -> parentFrame.showTransferPanel(TransferPanel.Mode.DEPOSIT));
        btnWithdraw.addActionListener(e -> parentFrame.showTransferPanel(TransferPanel.Mode.WITHDRAW));
        boolean canInteract = (currentUser.getRol() == 1 || currentUser.getRol() == 3);
        btnDeposit.setEnabled(canInteract); btnWithdraw.setEnabled(canInteract);
        actionBtns.add(btnDeposit); actionBtns.add(btnWithdraw);
        fundCard.add(Box.createVerticalStrut(20)); fundCard.add(actionBtns);
        rightPanel.add(fundCard);
        rightPanel.add(Box.createVerticalStrut(30));
        JLabel membersTitle = new JLabel("Integrantes");
        membersTitle.setForeground(Color.WHITE); membersTitle.setFont(new Font("Arial", Font.BOLD, 22));
        rightPanel.add(membersTitle); rightPanel.add(Box.createVerticalStrut(15));
        membersPanel = new JPanel();
        membersPanel.setLayout(new BoxLayout(membersPanel, BoxLayout.Y_AXIS));
        membersPanel.setOpaque(false);
        JScrollPane scrollMembers = new JScrollPane(membersPanel);
        scrollMembers.setOpaque(false); scrollMembers.getViewport().setOpaque(false); scrollMembers.setBorder(null);
        rightPanel.add(scrollMembers);
        gbc.gridx = 1; gbc.weightx = 0.5;
        center.add(rightPanel, gbc);
        add(center, BorderLayout.CENTER);
        loadData();
    }

    private void togglePrivacy() {
        isHidden = !isHidden;
        fundLabel.setText(isHidden ? "$***.***" : "$" + String.format("%,.0f", currentFamily.getFondoFam()).replace(",", "."));
    }

    private void loadData() {
        movementsPanel.removeAll();
        List<Transaction> transactions = parentFrame.getUserDAO().getFamilyTransactions(currentFamily.getIdFamilia());
        for (Transaction t : transactions) {
            movementsPanel.add(createFamilyTransactionCard(t));
            movementsPanel.add(Box.createVerticalStrut(10));
        }
        membersPanel.removeAll();
        List<User> members = parentFrame.getUserDAO().getFamilyMembers(currentFamily.getIdFamilia());
        for (User m : members) {
            membersPanel.add(createMemberCard(m));
            membersPanel.add(Box.createVerticalStrut(10));
        }
        revalidate(); repaint();
    }

    private JPanel createFamilyTransactionCard(Transaction t) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setOpaque(false); card.setMaximumSize(new Dimension(420, 85));
        JPanel iconBox = new JPanel();
        iconBox.setPreferredSize(new Dimension(50, 50)); iconBox.setBackground(new Color(0x444444));
        card.add(iconBox, BorderLayout.WEST);
        JPanel info = new JPanel(new BorderLayout()); info.setOpaque(false);
        
        String[] catParts = t.getCategoria().split(":");
        String catName = catParts[0];
        int catId = Integer.parseInt(catParts[1]);
        
        String titleText = "";
        if (catId == 1) titleText = "Transfer. de " + t.getUserName() + " a " + t.getDescripcion();
        else if (catId == 2) {
            if (t.getDescripcion().equals(t.getUserName())) titleText = "Retiro del fondo de " + t.getUserName();
            else titleText = "Retiro del fondo a " + t.getDescripcion() + " de " + t.getUserName();
        } else {
            titleText = catName + ". " + t.getDescripcion() + " de " + t.getUserName();
        }
        
        JLabel title = new JLabel("<html><body style='width: 200px;'>" + titleText + "</body></html>");
        title.setForeground(Color.WHITE); title.setFont(new Font("Arial", Font.BOLD, 13)); 
        
        JLabel sub = new JLabel(t.getFecha().toString() + ", " + t.getMetodoNombre());
        sub.setForeground(GrouPayApp.COLOR_TEXT_SECONDARY); sub.setFont(new Font("Arial", Font.PLAIN, 11));
        
        info.add(title, BorderLayout.CENTER);
        info.add(sub, BorderLayout.SOUTH);
        card.add(info, BorderLayout.CENTER);

        JLabel amount = new JLabel((t.getMonto() > 0 ? "+$" : "-$") + String.format("%,.0f", Math.abs(t.getMonto())).replace(",", "."));
        amount.setForeground(t.getMonto() > 0 ? new Color(0xAAFFAA) : new Color(0xFFAAAA));
        amount.setFont(new Font("Arial", Font.BOLD, 14));
        card.add(amount, BorderLayout.EAST);
        return card;
    }

    private JPanel createMemberCard(User m) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setOpaque(false); card.setMaximumSize(new Dimension(500, 70));
        JPanel iconBox = new JPanel(); iconBox.setPreferredSize(new Dimension(50, 50)); iconBox.setBackground(new Color(0x555555));
        card.add(iconBox, BorderLayout.WEST);
        JPanel info = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15)); info.setOpaque(false);
        JLabel name = new JLabel(m.getNombre() + " " + m.getApellido());
        name.setForeground(Color.WHITE); name.setFont(new Font("Arial", Font.BOLD, 16));
        info.add(name);
        if (m.getRol() == 3) {
            JLabel badge = new JLabel(" Admin ", SwingConstants.CENTER); badge.setOpaque(true);
            badge.setBackground(new Color(0xAAFFAA)); badge.setForeground(new Color(0x006600)); badge.setFont(new Font("Arial", Font.BOLD, 10));
            info.add(badge);
        }
        card.add(info, BorderLayout.CENTER);
        JLabel email = new JLabel(m.getEmail()); email.setForeground(GrouPayApp.COLOR_TEXT_SECONDARY);
        card.add(email, BorderLayout.SOUTH);
        return card;
    }

    private JButton createActionBtn(String text) {
        JButton b = new JButton(text); b.setPreferredSize(new Dimension(180, 45));
        b.setBackground(GrouPayApp.COLOR_BUTTON); b.setForeground(Color.WHITE);
        b.setFont(new Font("Arial", Font.BOLD, 16)); b.setBorder(new LineBorder(GrouPayApp.COLOR_ACCENT, 1));
        b.setFocusPainted(false); return b;
    }
}
