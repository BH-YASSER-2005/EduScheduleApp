package v6;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;


public class SalleUI extends JPanel implements ActionListener {

    private MainMenuUI parent;
    private DefaultTableModel tableModel;
    private JTable table;

    private JTextField     nomField;
    private JComboBox<String> typeCombo;
    private JTextField     capaciteField;
    private JTextField     batimentField;

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

    public SalleUI(MainMenuUI parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(BACKGROUND);

        
        JPanel panelForm = new JPanel();
        panelForm.setLayout(null);
        panelForm.setBackground(WHITE);
        panelForm.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PRIMARY), "Nouvelle Salle"));
        panelForm.setPreferredSize(new Dimension(400, 300));

        JLabel lblNom = new JLabel("Nom de la salle");
        lblNom.setForeground(TEXT);
        lblNom.setBounds(20, 30, 150, 20);
        panelForm.add(lblNom);

        nomField = new JTextField();
        nomField.setBounds(20, 55, 350, 30);
        nomField.setBorder(BorderFactory.createLineBorder(PRIMARY));
        panelForm.add(nomField);

        JLabel lblType = new JLabel("Type");
        lblType.setForeground(TEXT);
        lblType.setBounds(20, 95, 150, 20);
        panelForm.add(lblType);

        String[] types = {"Amphithéâtre", "Salle TD", "Labo Informatique", "Salle TP"};
        typeCombo = new JComboBox<>(types);
        typeCombo.setBounds(20, 120, 350, 30);
        panelForm.add(typeCombo);

        JLabel lblCapacite = new JLabel("Capacité");
        lblCapacite.setForeground(TEXT);
        lblCapacite.setBounds(20, 160, 150, 20);
        panelForm.add(lblCapacite);

        capaciteField = new JTextField();
        capaciteField.setBounds(20, 185, 350, 30);
        capaciteField.setBorder(BorderFactory.createLineBorder(PRIMARY));
        panelForm.add(capaciteField);

        JLabel lblBatiment = new JLabel("Bâtiment");
        lblBatiment.setForeground(TEXT);
        lblBatiment.setBounds(20, 225, 150, 20);
        panelForm.add(lblBatiment);

        batimentField = new JTextField();
        batimentField.setBounds(20, 250, 350, 30);
        batimentField.setBorder(BorderFactory.createLineBorder(PRIMARY));
        panelForm.add(batimentField);

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
            BorderFactory.createLineBorder(PRIMARY), "Liste des Salles"));

        String[] colonnes = {"ID", "Nom", "Type", "Capacité", "Bâtiment"};
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
                    typeCombo.setSelectedItem(tableModel.getValueAt(row, 2).toString());
                    capaciteField.setText(tableModel.getValueAt(row, 3).toString());
                    batimentField.setText(tableModel.getValueAt(row, 4).toString());
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
            SalleDAO dao = new SalleDAO();
            List<Salle> liste = dao.getTous();
            for (Salle s : liste) {
                tableModel.addRow(new Object[]{
                    s.getId(), s.getNom(), s.getType(),
                    s.getCapacite(), s.getBatiment()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Erreur chargement :\n" + ex.getMessage(), "Erreur BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void effacerFormulaire() {
        nomField.setText("");
        typeCombo.setSelectedIndex(0);
        capaciteField.setText("");
        batimentField.setText("");
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
                    "Le nom de la salle est obligatoire.", "Erreur", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                SalleDAO dao = new SalleDAO();
                Salle s = new Salle(0, nom, typeCombo.getSelectedItem().toString(),
                    capaciteField.getText().trim(), batimentField.getText().trim());
                if (dao.ajouter(s)) {
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
                    "Le nom de la salle est obligatoire.", "Erreur", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                SalleDAO dao = new SalleDAO();
                Salle s = new Salle(idSelectionne, nom, typeCombo.getSelectedItem().toString(),
                    capaciteField.getText().trim(), batimentField.getText().trim());
                if (dao.modifier(s)) {
                    chargerDonnees();
                    effacerFormulaire();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                    "Erreur modification :\n" + ex.getMessage(), "Erreur BD", JOptionPane.ERROR_MESSAGE);
            }

        } else if (e.getSource() == btnSupprimer && ligneEdition >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Supprimer cette salle ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    SalleDAO dao = new SalleDAO();
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
