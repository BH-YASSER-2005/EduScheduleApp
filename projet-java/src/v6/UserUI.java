package v6;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;


public class UserUI extends JPanel implements ActionListener {

    private MainMenuUI parent;
    private DefaultTableModel tableModel;
    private JTable table;

    private JTextField     usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;
    private JComboBox<SeanceUI.Item> lienCombo;   
    private JLabel lblLien;
    private final java.util.Map<Integer, Utilisateur> utilisateurs = new java.util.HashMap<>();

    private JButton btnAjouter;
    private JButton btnModifier;
    private JButton btnSupprimer;
    private JButton btnEffacer;

    private int idSelectionne  = -1;   
    private int ligneEdition   = -1;   

    private final Color PRIMARY    = new Color(41, 128, 185);
    private final Color BACKGROUND = new Color(236, 240, 241);
    private final Color WHITE      = Color.WHITE;
    private final Color TEXT       = new Color(44, 62, 80);

    public UserUI(MainMenuUI parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(BACKGROUND);

        
        JPanel panelForm = new JPanel();
        panelForm.setLayout(null);
        panelForm.setBackground(WHITE);
        panelForm.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PRIMARY), "Nouvel Utilisateur"));
        panelForm.setPreferredSize(new Dimension(400, 340));

        JLabel lblUsername = new JLabel("Identifiant");
        lblUsername.setForeground(TEXT);
        lblUsername.setBounds(20, 30, 150, 20);
        panelForm.add(lblUsername);

        usernameField = new JTextField();
        usernameField.setBounds(20, 55, 350, 30);
        usernameField.setBorder(BorderFactory.createLineBorder(PRIMARY));
        panelForm.add(usernameField);

        JLabel lblPassword = new JLabel("Mot de passe");
        lblPassword.setForeground(TEXT);
        lblPassword.setBounds(20, 95, 150, 20);
        panelForm.add(lblPassword);

        passwordField = new JPasswordField();
        passwordField.setBounds(20, 120, 350, 30);
        passwordField.setBorder(BorderFactory.createLineBorder(PRIMARY));
        panelForm.add(passwordField);

        JLabel lblRole = new JLabel("Rôle");
        lblRole.setForeground(TEXT);
        lblRole.setBounds(20, 160, 150, 20);
        panelForm.add(lblRole);

        
        String[] roles = {"Admin", "Enseignant", "Etudiant"};
        roleCombo = new JComboBox<>(roles);
        roleCombo.setBounds(20, 185, 350, 30);
        panelForm.add(roleCombo);

        lblLien = new JLabel("Rattachement");
        lblLien.setForeground(TEXT);
        lblLien.setBounds(20, 225, 250, 20);
        panelForm.add(lblLien);

        lienCombo = new JComboBox<>();
        lienCombo.setBounds(20, 250, 350, 30);
        panelForm.add(lienCombo);
        roleCombo.addActionListener(ev -> chargerLien(0));
        chargerLien(0);

        btnAjouter = new JButton("Ajouter");
        btnAjouter.setBackground(PRIMARY);
        btnAjouter.setForeground(WHITE);
        btnAjouter.setFocusPainted(false);
        btnAjouter.setBounds(20, 300, 110, 30);
        btnAjouter.addActionListener(this);
        panelForm.add(btnAjouter);

        btnEffacer = new JButton("Effacer");
        btnEffacer.setBackground(new Color(149, 165, 166));
        btnEffacer.setForeground(WHITE);
        btnEffacer.setFocusPainted(false);
        btnEffacer.setBounds(140, 300, 110, 30);
        btnEffacer.addActionListener(this);
        panelForm.add(btnEffacer);

        btnModifier = new JButton("Modifier");
        btnModifier.setBackground(PRIMARY);
        btnModifier.setForeground(WHITE);
        btnModifier.setFocusPainted(false);
        btnModifier.setBounds(260, 300, 110, 30);
        btnModifier.addActionListener(this);
        btnModifier.setEnabled(false);
        panelForm.add(btnModifier);

        btnSupprimer = new JButton("Supprimer");
        btnSupprimer.setBackground(new Color(231, 76, 60));
        btnSupprimer.setForeground(WHITE);
        btnSupprimer.setFocusPainted(false);
        btnSupprimer.setBounds(20, 345, 110, 30);
        btnSupprimer.addActionListener(this);
        btnSupprimer.setEnabled(false);
        panelForm.add(btnSupprimer);

        
        JPanel panelTable = new JPanel(new BorderLayout());
        panelTable.setBackground(WHITE);
        panelTable.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PRIMARY), "Liste des Utilisateurs"));

        
        String[] colonnes = {"ID", "Identifiant", "Rôle"};
        tableModel = new DefaultTableModel(colonnes, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        table = new JTable(tableModel);
        table.setBackground(WHITE);
        table.setForeground(TEXT);
        table.getTableHeader().setBackground(PRIMARY);
        table.getTableHeader().setForeground(WHITE);

        
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    ligneEdition   = row;
                    idSelectionne  = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                    usernameField.setText(tableModel.getValueAt(row, 1).toString());
                    passwordField.setText("");
                    roleCombo.setSelectedItem(tableModel.getValueAt(row, 2).toString());
                    Utilisateur sel = utilisateurs.get(idSelectionne);
                    if (sel != null) {
                        chargerLien("Enseignant".equals(sel.getRole()) ? sel.getEnseignantId()
                                  : "Etudiant".equals(sel.getRole()) ? sel.getGroupeId() : 0);
                    }
                    btnModifier.setEnabled(true);
                    btnSupprimer.setEnabled(true);
                    btnAjouter.setEnabled(false);
                }
            }
        });

        panelTable.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel panelCorps = new JPanel(new GridLayout(1, 2, 10, 10));
        panelCorps.setBackground(BACKGROUND);
        panelCorps.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelCorps.add(panelForm);
        panelCorps.add(panelTable);

        add(panelCorps, BorderLayout.CENTER);

        
        chargerDonnees();
    }

    
    
    private void chargerLien(int idASelectionner) {
        String role = roleCombo.getSelectedItem().toString();
        lienCombo.removeAllItems();
        lienCombo.setEnabled(!"Admin".equals(role));
        lblLien.setText("Enseignant".equals(role) ? "Enseignant rattaché"
                      : "Etudiant".equals(role) ? "Groupe de l'étudiant" : "Rattachement (aucun pour Admin)");
        try {
            if ("Enseignant".equals(role)) {
                for (Enseignant x : new EnseignantDAO().getTous())
                    lienCombo.addItem(new SeanceUI.Item(x.getId(), x.getNom() + " " + x.getPrenom()));
            } else if ("Etudiant".equals(role)) {
                for (Groupe x : new GroupeDAO().getTous())
                    lienCombo.addItem(new SeanceUI.Item(x.getId(), x.getNom()));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur chargement :\n" + ex.getMessage(),
                "Erreur BD", JOptionPane.ERROR_MESSAGE);
        }
        for (int i = 0; i < lienCombo.getItemCount(); i++)
            if (lienCombo.getItemAt(i).id == idASelectionner) { lienCombo.setSelectedIndex(i); break; }
    }

    private int lienId(boolean enseignant) {
        String role = roleCombo.getSelectedItem().toString();
        SeanceUI.Item it = (SeanceUI.Item) lienCombo.getSelectedItem();
        if (it == null) return 0;
        return (enseignant ? "Enseignant" : "Etudiant").equals(role) ? it.id : 0;
    }

    private void chargerDonnees() {
        tableModel.setRowCount(0);
        utilisateurs.clear();
        try {
            UtilisateurDAO dao = new UtilisateurDAO();
            List<Utilisateur> liste = dao.getTous();
            for (Utilisateur u : liste) {
                utilisateurs.put(u.getId(), u);
                tableModel.addRow(new Object[]{u.getId(), u.getIdentifiant(), u.getRole()});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Erreur chargement :\n" + ex.getMessage(), "Erreur BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void effacerFormulaire() {
        usernameField.setText("");
        passwordField.setText("");
        roleCombo.setSelectedIndex(0);
        ligneEdition  = -1;
        idSelectionne = -1;
        table.clearSelection();
        btnAjouter.setEnabled(true);
        btnModifier.setEnabled(false);
        btnSupprimer.setEnabled(false);
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnAjouter) {
            String identifiant = usernameField.getText().trim();
            String motDePasse  = new String(passwordField.getPassword()).trim();
            String role        = roleCombo.getSelectedItem().toString();

            if (identifiant.isEmpty() || motDePasse.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Identifiant et mot de passe sont obligatoires.", "Erreur", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                UtilisateurDAO dao = new UtilisateurDAO();
                Utilisateur u = new Utilisateur(0, identifiant, motDePasse, role, lienId(true), lienId(false));
                if (dao.ajouter(u)) {
                    chargerDonnees();
                    effacerFormulaire();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                    "Erreur ajout :\n" + ex.getMessage(), "Erreur BD", JOptionPane.ERROR_MESSAGE);
            }

        } else if (e.getSource() == btnModifier && ligneEdition >= 0) {
            String identifiant = usernameField.getText().trim();
            String motDePasse  = new String(passwordField.getPassword()).trim();
            String role        = roleCombo.getSelectedItem().toString();

            if (identifiant.isEmpty() || motDePasse.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Tous les champs sont obligatoires.", "Erreur", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                UtilisateurDAO dao = new UtilisateurDAO();
                Utilisateur u = new Utilisateur(idSelectionne, identifiant, motDePasse, role, lienId(true), lienId(false));
                if (dao.modifier(u)) {
                    chargerDonnees();
                    effacerFormulaire();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                    "Erreur modification :\n" + ex.getMessage(), "Erreur BD", JOptionPane.ERROR_MESSAGE);
            }

        } else if (e.getSource() == btnSupprimer && ligneEdition >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Supprimer cet utilisateur ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    UtilisateurDAO dao = new UtilisateurDAO();
                    if (dao.supprimer(idSelectionne)) {
                        chargerDonnees();
                        effacerFormulaire();
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this,
                        "Erreur suppression :\n" + ex.getMessage(), "Erreur BD", JOptionPane.ERROR_MESSAGE);
                }
            }

        } else if (e.getSource() == btnEffacer) {
            effacerFormulaire();
        }
    }
}
