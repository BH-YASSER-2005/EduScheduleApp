package v6;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MainMenuUI extends JFrame implements ActionListener {
    
    private JPanel panneauPrincipal;
    private JLabel titrePage;
    private JPanel topBar;
    
    private final Color PRIMARY = new Color(41, 128, 185);
    private final Color BACKGROUND = new Color(236, 240, 241);
    private final Color WHITE = Color.WHITE;
    private final Color TEXT = new Color(44, 62, 80);
    
    public MainMenuUI() {
        super();
        setTitle("EduSchedule — Tableau de bord");
        setSize(UIScale.size(1100, 700));
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        
        JPanel sidebar = new JPanel();
        sidebar.setLayout(null);
        sidebar.setPreferredSize(new Dimension(220, 700));
        sidebar.setBackground(PRIMARY);
        
        JLabel logo = new JLabel("EduSchedule");
        logo.setFont(new Font("Arial", Font.BOLD, 16));
        logo.setForeground(WHITE);
        logo.setBounds(20, 24, 200, 30);
        sidebar.add(logo);
        
        String[][] navItems = {
            {"Accueil", "home"},
            {"Enseignants", "enseignants"},
            {"Groupes", "groupes"},
            {"Salles", "salles"},
            {"Matières", "matieres"},
            {"Séances", "seances"},
            {"Utilisateurs", "users"}
        };
        
        int y = 74;
        for (String[] item : navItems) {
            JButton btn = new JButton(item[0]);
            btn.setBounds(10, y, 200, 32);
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setBackground(new Color(52, 152, 219));
            btn.setForeground(WHITE);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            btn.addActionListener(this);
            btn.setActionCommand(item[1]);
            sidebar.add(btn);
            y += 40;
        }
        
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        JLabel dateLbl = new JLabel(date, SwingConstants.CENTER);
        dateLbl.setForeground(WHITE);
        dateLbl.setBounds(10, 640, 200, 20);
        sidebar.add(dateLbl);
        
        
        panneauPrincipal = new JPanel();
        panneauPrincipal.setLayout(new BorderLayout());
        panneauPrincipal.setBackground(BACKGROUND);
        
        
        topBar = new JPanel(new BorderLayout());
        topBar.setBackground(WHITE);
        topBar.setPreferredSize(new Dimension(880, 50));
        topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));
        
        titrePage = new JLabel("Tableau de bord");
        titrePage.setFont(new Font("Arial", Font.BOLD, 16));
        titrePage.setForeground(TEXT);
        topBar.add(titrePage, BorderLayout.WEST);
        
        
        JButton btnDeconnexion = new JButton("Déconnexion");
        btnDeconnexion.setBackground(PRIMARY);
        btnDeconnexion.setForeground(WHITE);
        btnDeconnexion.setFocusPainted(false);
        btnDeconnexion.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        
        btnDeconnexion.setOpaque(true);
        btnDeconnexion.setContentAreaFilled(true);
        btnDeconnexion.setBorderPainted(false);
        btnDeconnexion.addActionListener(e -> {
            dispose();
            new LoginUI();
        });
        topBar.add(btnDeconnexion, BorderLayout.EAST);
        
        panneauPrincipal.add(topBar, BorderLayout.NORTH);
        
        
        afficherDashboard();
        
        UIScale.apply(sidebar);
        UIScale.apply(topBar);
        add(sidebar, BorderLayout.WEST);
        add(panneauPrincipal, BorderLayout.CENTER);
        
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void afficherDashboard() {
        JPanel dashboard = new JPanel();
        dashboard.setLayout(null);
        dashboard.setBackground(BACKGROUND);
        
        JLabel welcome = new JLabel("Bienvenue, Administrateur");
        welcome.setFont(new Font("Arial", Font.BOLD, 20));
        welcome.setForeground(TEXT);
        welcome.setBounds(30, 28, 500, 32);
        dashboard.add(welcome);
        
        JLabel sub = new JLabel("Gérez votre emploi du temps depuis un seul endroit");
        sub.setForeground(TEXT);
        sub.setBounds(30, 64, 500, 20);
        dashboard.add(sub);
        
        String[][] modules = {
            {"Enseignants", "enseignants"},
            {"Groupes", "groupes"},
            {"Salles", "salles"},
            {"Matières", "matieres"},
            {"Séances", "seances"},
            {"Utilisateurs", "users"}
        };
        
        int mx = 30, my = 120;
        for (String[] mod : modules) {
            JPanel card = new JPanel();
            card.setLayout(null);
            card.setBounds(mx, my, 220, 100);
            card.setBackground(WHITE);
            card.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
            
            JLabel titre = new JLabel(mod[0]);
            titre.setFont(new Font("Arial", Font.BOLD, 14));
            titre.setForeground(TEXT);
            titre.setBounds(14, 16, 190, 20);
            card.add(titre);
            
            JButton btn = new JButton("Ouvrir");
            btn.setBackground(PRIMARY);
            btn.setForeground(WHITE);
            btn.setFocusPainted(false);
            btn.setBounds(14, 64, 90, 26);
            btn.addActionListener(this);
            btn.setActionCommand(mod[1]);
            card.add(btn);
            
            dashboard.add(card);
            mx += 236;
            if (mx > 700) {
                mx = 30;
                my += 116;
            }
        }
        
        panneauPrincipal.removeAll();
        panneauPrincipal.add(topBar, BorderLayout.NORTH);
        UIScale.apply(dashboard);
        panneauPrincipal.add(dashboard, BorderLayout.CENTER);
        panneauPrincipal.revalidate();
        panneauPrincipal.repaint();
    }
    
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        
        if (cmd.equals("home")) {
            afficherDashboard();
        } else if (cmd.equals("enseignants")) {
            afficherPage(new EnseignantUI(this));
        } else if (cmd.equals("groupes")) {
            afficherPage(new GroupeUI(this));
        } else if (cmd.equals("salles")) {
            afficherPage(new SalleUI(this));
        } else if (cmd.equals("matieres")) {
            afficherPage(new MatiereUI(this));
        } else if (cmd.equals("seances")) {
            afficherPage(new SeanceUI(this));
        } else if (cmd.equals("users")) {
            afficherPage(new UserUI(this));
        }
    }
    
    private void afficherPage(JPanel page) {
        panneauPrincipal.removeAll();
        panneauPrincipal.add(topBar, BorderLayout.NORTH);
        UIScale.apply(page);
        panneauPrincipal.add(page, BorderLayout.CENTER);
        panneauPrincipal.revalidate();
        panneauPrincipal.repaint();
    }
}