package com.mycompany.groupay;

import javax.swing.*;
import java.awt.*;

public class GrouPayApp extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;
    private HomePanel homePanel;
    private TransferPanel transferPanel;
    private PaymentPanel paymentPanel;
    private DepositPanel depositPanel;
    private NoFamilyPanel noFamilyPanel;
    private CreateGroupPanel createGroupPanel;
    private JoinGroupPanel joinGroupPanel;
    private FamilyDashboardPanel familyDashboardPanel;
    
    private UserDAO userDAO;
    private User currentUser;

    public static final Color COLOR_BG = new Color(0x2C2456);
    public static final Color COLOR_CARD = new Color(0x37334F);
    public static final Color COLOR_ACCENT = new Color(0xEEB006);
    public static final Color COLOR_TEXT_MAIN = Color.WHITE;
    public static final Color COLOR_TEXT_SECONDARY = new Color(0xD9D9D9);
    public static final Color COLOR_BUTTON = new Color(0x8C7BE4);
    public static final Color COLOR_INPUT_BG = new Color(0xEAEAEA);

    public GrouPayApp() {
        userDAO = new UserDAO();
        setTitle("GrouPay");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720); 
        setMinimumSize(new Dimension(1000, 600));
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(COLOR_BG);

        loginPanel = new LoginPanel(this);
        registerPanel = new RegisterPanel(this);
        homePanel = new HomePanel(this);
        transferPanel = new TransferPanel(this);
        paymentPanel = new PaymentPanel(this);
        depositPanel = new DepositPanel(this);
        noFamilyPanel = new NoFamilyPanel(this);
        createGroupPanel = new CreateGroupPanel(this);
        joinGroupPanel = new JoinGroupPanel(this);
        familyDashboardPanel = new FamilyDashboardPanel(this);

        mainPanel.add(loginPanel, "Login");
        mainPanel.add(registerPanel, "Register");
        mainPanel.add(homePanel, "Home");
        mainPanel.add(transferPanel, "Transfer");
        mainPanel.add(paymentPanel, "Payment");
        mainPanel.add(depositPanel, "Deposit");
        mainPanel.add(noFamilyPanel, "NoFamily");
        mainPanel.add(createGroupPanel, "CreateGroup");
        mainPanel.add(joinGroupPanel, "JoinGroup");
        mainPanel.add(familyDashboardPanel, "FamilyDashboard");

        add(mainPanel);
        showLoginPanel();
    }

    private void updateView(String name) {
        cardLayout.show(mainPanel, name);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void showLoginPanel() { updateView("Login"); }
    public void showRegisterPanel() { updateView("Register"); }
    public void showHomePanel() { homePanel.setUser(currentUser); updateView("Home"); }
    public void showTransferPanel(TransferPanel.Mode mode) { transferPanel.setMode(mode, currentUser); updateView("Transfer"); }
    public void showPaymentPanel() { paymentPanel.setUser(currentUser); updateView("Payment"); }
    public void showDepositPanel() { depositPanel.setUser(currentUser); updateView("Deposit"); }
    public void showNoFamilyPanel() { updateView("NoFamily"); }
    public void showCreateGroupPanel() { createGroupPanel.init(); updateView("CreateGroup"); }
    public void showJoinGroupPanel() { joinGroupPanel.init(); updateView("JoinGroup"); }
    public void showFamilyDashboard() { familyDashboardPanel.setUser(currentUser); updateView("FamilyDashboard"); }

    public void handleFamilyNavigation() {
        if (currentUser.getIdFamilia() == null) showNoFamilyPanel();
        else showFamilyDashboard();
    }

    public boolean registerUser(String n, String s, String e, String p) { return userDAO.registerUser(n, s, e, p); }
    public boolean authenticateUser(String email, String password) {
        if (userDAO.authenticateUser(email, password)) {
            currentUser = userDAO.getUserInfo(email);
            showHomePanel();
            return true;
        }
        return false;
    }

    public UserDAO getUserDAO() { return userDAO; }
    public User getCurrentUser() { return currentUser; }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GrouPayApp().setVisible(true));
    }
}
