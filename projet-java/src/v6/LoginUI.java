package v6;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;


public class LoginUI extends JFrame implements ActionListener {

    private JTextField     champIdentifiant;
    private JPasswordField champMotDePasse;
    private JButton        btnConnexion;
    private JLabel         erreurLabel;

    private final Color PRIMARY    = new Color(41, 128, 185);
    private final Color BACKGROUND = new Color(236, 240, 241);
    private final Color WHITE      = Color.WHITE;
    private final Color TEXT       = new Color(44, 62, 80);

    public LoginUI() {
        super();
        setTitle("EduSchedule — Connexion");
        setSize(UIScale.size(900, 600));
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(1, 2));

        JPanel panneauGauche = new JPanel();
        panneauGauche.setLayout(null);
        panneauGauche.setBackground(PRIMARY);

        JLabel logo = new JLabel("EduSchedule", SwingConstants.CENTER);
        logo.setFont(new Font("Arial", Font.BOLD, 28));
        logo.setForeground(WHITE);
        logo.setBounds(0, 120, 450, 40);

        JLabel sousTitre = new JLabel("Système de Gestion des Emplois du Temps", SwingConstants.CENTER);
        sousTitre.setFont(new Font("Arial", Font.PLAIN, 14));
        sousTitre.setForeground(WHITE);
        sousTitre.setBounds(0, 168, 450, 24);

        String[] elements = {"Enseignants", "Séances", "Salles", "Matières"};
        int y = 230;
        for (String el : elements) {
            JLabel lbl = new JLabel("•  " + el);
            lbl.setFont(new Font("Arial", Font.PLAIN, 14));
            lbl.setForeground(WHITE);
            lbl.setBounds(120, y, 250, 24);
            panneauGauche.add(lbl);
            y += 30;
        }
        JLabel signature = new JLabel("BY BH YASSER", SwingConstants.CENTER);
        signature.setFont(new Font("Arial", Font.BOLD, 13));
        signature.setForeground(new Color(255, 255, 255, 200));
        signature.setBounds(0, 520, 450, 24);
        panneauGauche.add(signature);
        panneauGauche.add(logo);
        panneauGauche.add(sousTitre);

        JPanel panneauDroit = new JPanel();
        panneauDroit.setLayout(null);
        panneauDroit.setBackground(BACKGROUND);

        JLabel bienvenue = new JLabel("Bienvenue !");
        bienvenue.setFont(new Font("Arial", Font.BOLD, 22));
        bienvenue.setForeground(TEXT);
        bienvenue.setBounds(70, 90, 300, 34);

        JLabel connectez = new JLabel("Connectez-vous pour continuer");
        connectez.setFont(new Font("Arial", Font.PLAIN, 12));
        connectez.setForeground(TEXT);
        connectez.setBounds(70, 128, 300, 20);

        JLabel lblId = new JLabel("Identifiant");
        lblId.setFont(new Font("Arial", Font.PLAIN, 12));
        lblId.setForeground(TEXT);
        lblId.setBounds(70, 168, 300, 18);

        champIdentifiant = new JTextField();
        champIdentifiant.setBounds(70, 190, 300, 30);
        champIdentifiant.setBackground(WHITE);
        champIdentifiant.setBorder(BorderFactory.createLineBorder(PRIMARY));

        JLabel lblMdp = new JLabel("Mot de passe");
        lblMdp.setFont(new Font("Arial", Font.PLAIN, 12));
        lblMdp.setForeground(TEXT);
        lblMdp.setBounds(70, 232, 300, 18);

        champMotDePasse = new JPasswordField();
        champMotDePasse.setBounds(70, 252, 300, 30);
        champMotDePasse.setBackground(WHITE);
        champMotDePasse.setBorder(BorderFactory.createLineBorder(PRIMARY));

        erreurLabel = new JLabel("");
        erreurLabel.setForeground(Color.RED);
        erreurLabel.setBounds(70, 292, 300, 20);

        btnConnexion = new JButton("Se connecter");
        btnConnexion.setBounds(70, 320, 300, 36);
        btnConnexion.setBackground(PRIMARY);
        btnConnexion.setForeground(WHITE);
        btnConnexion.setFocusPainted(false);
        btnConnexion.setBorder(BorderFactory.createEmptyBorder());
        btnConnexion.addActionListener(this);

        panneauDroit.add(bienvenue);
        panneauDroit.add(connectez);
        panneauDroit.add(lblId);
        panneauDroit.add(champIdentifiant);
        panneauDroit.add(lblMdp);
        panneauDroit.add(champMotDePasse);
        panneauDroit.add(erreurLabel);
        panneauDroit.add(btnConnexion);

        add(panneauGauche);
        add(panneauDroit);
        UIScale.apply(panneauGauche);
        UIScale.apply(panneauDroit);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnConnexion) {
            String identifiant = champIdentifiant.getText().trim();
            String motDePasse  = new String(champMotDePasse.getPassword()).trim();

            if (identifiant.isEmpty() || motDePasse.isEmpty()) {
                erreurLabel.setText("Veuillez remplir tous les champs.");
                return;
            }

            try {
                UtilisateurDAO dao = new UtilisateurDAO();
                Utilisateur utilisateur = dao.connecter(identifiant, motDePasse);

                if (utilisateur == null) {
                    
                    erreurLabel.setText("Identifiants incorrects.");
                    champMotDePasse.setText("");
                } else {
                    
                    this.dispose();

                    if (utilisateur.getRole().equals("Admin")) {
                        new MainMenuUI();

                    } else if (utilisateur.getRole().equals("Enseignant")) {
                        new VueEnseignantUI(utilisateur);

                    } else if (utilisateur.getRole().equals("Etudiant")) {
                        new VueEtudiantUI(utilisateur);
                    }
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                    "Erreur de connexion à la base de données :\n" + ex.getMessage(),
                    "Erreur BD", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        new LoginUI();
    }
}
