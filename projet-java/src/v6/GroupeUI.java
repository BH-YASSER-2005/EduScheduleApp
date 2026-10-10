package v6;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;


public class GroupeUI extends JPanel implements ActionListener {

    private MainMenuUI parent;
    private DefaultTableModel tableModel;
    private JTable table;

    private JTextField nomField;
    private JTextField niveauField;
    private JTextField sectionField;
    private JTextField effectifField;

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

    public GroupeUI(MainMenuUI parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(BACKGROUND);

        JPanel panelForm = new JPanel();
        panelForm.setLayout(null);
        panelForm.setBackground(WHITE);
        panelForm.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PRIMARY), "Nouveau Groupe"));
        panelForm.setPreferredSize(new Dimension(400, 260));

        JLabel lblNom = new JLabel("Nom du groupe");
        lblNom.setForeground(TEXT);
        lblNom.setBounds(20, 30, 150, 20);
        panelForm.add(lblNom);

        nomField = new JTextField();
        nomField.setBounds(20, 55, 350, 30);
        nomField.setBorder(BorderFactory.createLineBorder(PRIMARY));
        panelForm.add(nomField);

        JLabel lblNiveau = new JLabel("Niveau");
        lblNiveau.setForeground(TEXT);
        lblNiveau.setBounds(20, 95, 150, 20);
        panelForm.add(lblNiveau);

        niveauField = new JTextField();
        niveauField.setBounds(20, 120, 350, 30);
        niveauField.setBorder(BorderFactory.createLineBorder(PRIMARY));
        panelForm.add(niveauField);

        JLabel lblSection = new JLabel("Section");
        lblSection.setForeground(TEXT);
        lblSection.setBounds(20, 160, 150, 20);
        panelForm.add(lblSection);

        sectionField = new JTextField();
        sectionField.setBounds(20, 185, 350, 30);
        sectionField.setBorder(BorderFactory.createLineBorder(PRIMARY));
        panelForm.add(sectionField);

        JLabel lblEffectif = new JLabel("Effectif");
        lblEffectif.setForeground(TEXT);
        lblEffectif.setBounds(20, 225, 150, 20);
        panelForm.add(lblEffectif);

        effectifField = new JTextField();
        effectifField.setBounds(20, 250, 350, 30);
        effectifField.setBorder(BorderFactory.createLineBorder(PRIMARY));
        panelForm.add(effectifField);

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
            BorderFactory.createLineBorder(PRIMARY), "Liste des Groupes"));

        String[] colonnes = {"ID", "Nom", "Niveau", "Section", "Effectif"};
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
                    niveauField.setText(tableModel.getValueAt(row, 2).toString());
                    sectionField.setText(tableModel.getValueAt(row, 3).toString());
                    effectifField.setText(tableModel.getValueAt(row, 4).toString());
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
            GroupeDAO dao = new GroupeDAO();
            List<Groupe> liste = dao.getTous();
            for (Groupe g : liste) {
                tableModel.addRow(new Object[]{
                    g.getId(), g.getNom(), g.getNiveau(),
                    g.getSection(), g.getEffectif()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Erreur chargement :\n" + ex.getMessage(), "Erreur BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void effacerFormulaire() {
        nomField.setText("");
        niveauField.setText("");
        sectionField.setText("");
        effectifField.setText("");
        ligneEdition  = -1;
        idSelectionne = -1;
        table.clearSelection();
        btnAjouter.setEnabled(true);
        btnModifier.setEnabled(false);
        btnSupprimer.setEnabled(false);
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnAjouter) {
            String nom = nomField.getText().trim();
            if (nom.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Le nom du groupe est obligatoire.", "Erreur", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                GroupeDAO dao = new GroupeDAO();
                Groupe g = new Groupe(0, nom, niveauField.getText().trim(),
                    sectionField.getText().trim(), effectifField.getText().trim());
                if (dao.ajouter(g)) {
                    chargerDonnees();
                    effacerFormulaire();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                    "Erreur ajout :\n" + ex.getMessage(), "Erreur BD", JOptionPane.ERROR_MESSAGE);
            }

        } else if (e.getSource() == btnModifier && ligneEdition >= 0) {
            String nom = nomField.getText().trim();
            if (nom.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Le nom du groupe est obligatoire.", "Erreur", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                GroupeDAO dao = new GroupeDAO();
                Groupe g = new Groupe(idSelectionne, nom, niveauField.getText().trim(),
                    sectionField.getText().trim(), effectifField.getText().trim());
                if (dao.modifier(g)) {
                    chargerDonnees();
                    effacerFormulaire();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                    "Erreur modification :\n" + ex.getMessage(), "Erreur BD", JOptionPane.ERROR_MESSAGE);
            }

        } else if (e.getSource() == btnSupprimer && ligneEdition >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Supprimer ce groupe ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    GroupeDAO dao = new GroupeDAO();
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
