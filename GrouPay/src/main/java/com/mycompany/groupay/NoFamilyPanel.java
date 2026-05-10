package com.mycompany.groupay;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class NoFamilyPanel extends JPanel {

    private GrouPayApp parentFrame;

    public NoFamilyPanel(GrouPayApp parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout());
        setBackground(GrouPayApp.COLOR_BG);
        initUI();
    }

    private void initUI() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setBorder(new EmptyBorder(30, 40, 0, 40));

        JButton backBtn = new JButton("◀");
        backBtn.setBackground(GrouPayApp.COLOR_BUTTON);
        backBtn.setForeground(Color.WHITE);
        backBtn.setPreferredSize(new Dimension(50, 50));
        backBtn.setFocusPainted(false);
        backBtn.setBorder(new LineBorder(GrouPayApp.COLOR_ACCENT, 1));
        backBtn.addActionListener(e -> parentFrame.showHomePanel());
        
        JPanel backWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backWrapper.setOpaque(false);
        backWrapper.add(backBtn);
        topBar.add(backWrapper, BorderLayout.WEST);

        add(topBar, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 20, 10);
        gbc.fill = GridBagConstraints.NONE;

        JLabel title = new JLabel("Parece que todavía no tenés un grupo familiar");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        gbc.gridy = 0;
        centerPanel.add(title, gbc);

        JPanel cardPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 60, 0));
        cardPanel.setOpaque(false);
        cardPanel.add(createActionCard("Crear grupo", "NUEVO"));
        cardPanel.add(createActionCard("Unirse a grupo", "UNIRSE"));

        gbc.gridy = 1;
        gbc.insets = new Insets(30, 10, 50, 10);
        centerPanel.add(cardPanel, gbc);

        JPanel infoGrid = new JPanel(new GridLayout(1, 4, 30, 0));
        infoGrid.setOpaque(false);
        infoGrid.add(createInfoSection("Gestión de gastos", "Controlá los gastos de tu hogar de forma simple."));
        infoGrid.add(createInfoSection("Fondo común familiar", "Reuní dinero con tu familia para gastos compartidos."));
        infoGrid.add(createInfoSection("Análisis de finanzas", "Visualizá en qué gasta más tu familia mes a mes."));
        infoGrid.add(createInfoSection("Metas de ahorro", "Ahorrá en conjunto para un objetivo común."));

        gbc.gridy = 2;
        gbc.insets = new Insets(20, 40, 20, 40);
        centerPanel.add(infoGrid, gbc);

        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createActionCard(String text, String action) {
        JPanel container = new JPanel(new BorderLayout(0, 15));
        container.setOpaque(false);

        JButton card = new JButton();
        card.setPreferredSize(new Dimension(220, 220));
        card.setBackground(GrouPayApp.COLOR_CARD);
        card.setBorder(new LineBorder(GrouPayApp.COLOR_ACCENT, 2));
        card.setFocusPainted(false);
        card.addActionListener(e -> {
            if (action.equals("NUEVO")) parentFrame.showCreateGroupPanel();
            else parentFrame.showJoinGroupPanel();
        });

        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 20));

        container.add(card, BorderLayout.CENTER);
        container.add(label, BorderLayout.SOUTH);
        return container;
    }

    private JPanel createInfoSection(String title, String desc) {
        JPanel p = new JPanel(new BorderLayout(15, 5));
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(200, 100));

        JPanel iconSpace = new JPanel();
        iconSpace.setPreferredSize(new Dimension(50, 50));
        iconSpace.setOpaque(false);
        p.add(iconSpace, BorderLayout.WEST);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel t = new JLabel("<html><b>" + title + "</b></html>");
        t.setForeground(Color.WHITE);
        t.setFont(new Font("Arial", Font.BOLD, 14));
        
        JTextArea d = new JTextArea(desc);
        d.setForeground(GrouPayApp.COLOR_TEXT_SECONDARY);
        d.setFont(new Font("Arial", Font.PLAIN, 12));
        d.setWrapStyleWord(true);
        d.setLineWrap(true);
        d.setOpaque(false);
        d.setEditable(false);
        d.setFocusable(false);

        textPanel.add(t);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(d);

        p.add(textPanel, BorderLayout.CENTER);
        return p;
    }
}
