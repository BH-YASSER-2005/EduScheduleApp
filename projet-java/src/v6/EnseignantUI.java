package v6;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;


public class EnseignantUI extends JPanel implements ActionListener {

    private MainMenuUI parent;
    private DefaultTableModel tableModel;
    private JTable table;

    private JTextField nomField;
    private JTextField prenomField;
    private JTextField emailField;
    private JTextField specialiteField;

    private JButton btnAjouter;
    private JButton btnModifier;
    private JButton btnSupprimer;
    private JButton btnEffacer;

    private int idSelectionne = -1; 
    private int ligneEdition  = -1;  

    private final Color PRIMARY    = new Color(41, 128, 185);
    private final Color BACKGROUND = new Color(236, 240, 241);
    private final Color WHITE      = Color.WHITE;
    private final Color TEXT       = new Color(44, 62, 80);

    public EnseignantUI(MainMenuUI parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(BACKGROUND);

        
        JPanel panelForm = new JPanel();
        panelForm.setLayout(null);
        panelForm.setBackground(WHITE);
        panelForm.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PRIMARY), "Nouvel Enseignant"));
        panelForm.setPreferredSize(new Dimension(400, 300));

        JLabel lblNom = new JLabel("Nom");
        lblNom.setForeground(TEXT);
        lblNom.setBounds(20, 30, 150, 20);
        panelForm.add(lblNom);

        nomField = new JTextField();
        nomField.setBounds(20, 55, 350, 30);
        nomField.setBorder(BorderFactory.createLineBorder(PRIMARY));
        panelForm.add(nomField);

        JLabel lblPrenom = new JLabel("Prénom");
        lblPrenom.setForeground(TEXT);
        lblPrenom.setBounds(20, 95, 150, 20);
        panelForm.add(lblPrenom);

        prenomField = new JTextField();
        prenomField.setBounds(20, 120, 350, 30);
        prenomField.setBorder(BorderFactory.createLineBorder(PRIMARY));
        panelForm.add(prenomField);

        JLabel lblEmail = new JLabel("Email");
        lblEmail.setForeground(TEXT);
        lblEmail.setBounds(20, 160, 150, 20);
        panelForm.add(lblEmail);

        emailField = new JTextField();
        emailField.setBounds(20, 185, 350, 30);
        emailField.setBorder(BorderFactory.createLineBorder(PRIMARY));
        panelForm.add(emailField);

        JLabel lblSpecialite = new JLabel("Spécialité");
        lblSpecialite.setForeground(TEXT);
        lblSpecialite.setBounds(20, 225, 150, 20);
        panelForm.add(lblSpecialite);

        specialiteField = new JTextField();
        specialiteField.setBounds(20, 250, 350, 30);
        specialiteField.setBorder(BorderFactory.createLineBorder(PRIMARY));
        panelForm.add(specialiteField);

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
            BorderFactory.createLineBorder(PRIMARY), "Liste des Enseignants"));

        String[] colonnes = {"ID", "Nom", "Prénom", "Email", "Spécialité"};
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
                    ligneEdition  = row;
                    idSelectionne = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                    nomField.setText(tableModel.getValueAt(row, 1).toString());
                    prenomField.setText(tableModel.getValueAt(row, 2).toString());
                    emailField.setText(tableModel.getValueAt(row, 3).toString());
                    specialiteField.setText(tableModel.getValueAt(row, 4).toString());
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

    
    private void chargerDonnees() {
        tableModel.setRowCount(0);
        try {
            EnseignantDAO dao = new EnseignantDAO();
            List<Enseignant> liste = dao.getTous();
            for (Enseignant en : liste) {
                tableModel.addRow(new Object[]{
                    en.getId(), en.getNom(), en.getPrenom(),
                    en.getEmail(), en.getSpecialite()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Erreur chargement :\n" + ex.getMessage(), "Erreur BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void effacerFormulaire() {
        nomField.setText("");
        prenomField.setText("");
        emailField.setText("");
        specialiteField.setText("");
        ligneEdition  = -1;
        idSelectionne = -1;
        table.clearSelection();
        btnAjouter.setEnabled(true);
        btnModifier.setEnabled(false);
        btnSupprimer.setEnabled(false);
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnAjouter) {
            String nom    = nomField.getText().trim();
            String prenom = prenomField.getText().trim();
            if (nom.isEmpty() || prenom.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Nom et Prénom sont obligatoires.", "Erreur", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                EnseignantDAO dao = new EnseignantDAO();
                Enseignant en = new Enseignant(0, nom, prenom,
                    emailField.getText().trim(), specialiteField.getText().trim());
                if (dao.ajouter(en)) {
                    chargerDonnees();
                    effacerFormulaire();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                    "Erreur ajout :\n" + ex.getMessage(), "Erreur BD", JOptionPane.ERROR_MESSAGE);
            }

        } else if (e.getSource() == btnModifier && ligneEdition >= 0) {
            String nom    = nomField.getText().trim();
            String prenom = prenomField.getText().trim();
            if (nom.isEmpty() || prenom.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Nom et Prénom sont obligatoires.", "Erreur", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                EnseignantDAO dao = new EnseignantDAO();
                Enseignant en = new Enseignant(idSelectionne, nom, prenom,
                    emailField.getText().trim(), specialiteField.getText().trim());
                if (dao.modifier(en)) {
                    chargerDonnees();
                    effacerFormulaire();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                    "Erreur modification :\n" + ex.getMessage(), "Erreur BD", JOptionPane.ERROR_MESSAGE);
            }

        } else if (e.getSource() == btnSupprimer && ligneEdition >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Supprimer cet enseignant ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    EnseignantDAO dao = new EnseignantDAO();
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
