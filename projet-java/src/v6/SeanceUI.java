package v6;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;


public class SeanceUI extends JPanel implements ActionListener {

    private MainMenuUI parent;
    private DefaultTableModel tableModel;
    private JTable table;

    private JComboBox<String> jourCombo;
    private JTextField heureField;
    private JComboBox<Item> enseignantField;
    private JComboBox<Item> salleField;
    private JComboBox<Item> groupeField;
    private JComboBox<Item> matiereField;

    
    static class Item {
        final int id; final String label;
        Item(int id, String label) { this.id = id; this.label = label; }
        public String toString() { return label; }
    }

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

    public SeanceUI(MainMenuUI parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(BACKGROUND);

        
        JPanel panelForm = new JPanel();
        panelForm.setLayout(null);
        panelForm.setBackground(WHITE);
        panelForm.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PRIMARY), "Nouvelle Séance"));
        panelForm.setPreferredSize(new Dimension(400, 380));

        JLabel lblJour = new JLabel("Jour");
        lblJour.setForeground(TEXT);
        lblJour.setBounds(20, 30, 150, 20);
        panelForm.add(lblJour);

        String[] jours = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi"};
        jourCombo = new JComboBox<>(jours);
        jourCombo.setBounds(20, 55, 350, 30);
        panelForm.add(jourCombo);

        JLabel lblHeure = new JLabel("Heure");
        lblHeure.setForeground(TEXT);
        lblHeure.setBounds(20, 95, 150, 20);
        panelForm.add(lblHeure);

        heureField = new JTextField();
        heureField.setBounds(20, 120, 350, 30);
        heureField.setBorder(BorderFactory.createLineBorder(PRIMARY));
        panelForm.add(heureField);

        JLabel lblEnseignant = new JLabel("Enseignant");
        lblEnseignant.setForeground(TEXT);
        lblEnseignant.setBounds(20, 160, 150, 20);
        panelForm.add(lblEnseignant);

        enseignantField = new JComboBox<>();
        enseignantField.setBounds(20, 185, 350, 30);
        panelForm.add(enseignantField);

        JLabel lblSalle = new JLabel("Salle");
        lblSalle.setForeground(TEXT);
        lblSalle.setBounds(20, 225, 150, 20);
        panelForm.add(lblSalle);

        salleField = new JComboBox<>();
        salleField.setBounds(20, 250, 350, 30);
        panelForm.add(salleField);

        JLabel lblGroupe = new JLabel("Groupe");
        lblGroupe.setForeground(TEXT);
        lblGroupe.setBounds(20, 290, 150, 20);
        panelForm.add(lblGroupe);

        groupeField = new JComboBox<>();
        groupeField.setBounds(20, 315, 350, 30);
        panelForm.add(groupeField);

        JLabel lblMatiere = new JLabel("Matière");
        lblMatiere.setForeground(TEXT);
        lblMatiere.setBounds(20, 355, 150, 20);
        panelForm.add(lblMatiere);

        matiereField = new JComboBox<>();
        matiereField.setBounds(20, 380, 350, 30);
        panelForm.add(matiereField);

        btnAjouter = new JButton("Ajouter");
        btnAjouter.setBackground(PRIMARY);
        btnAjouter.setForeground(WHITE);
        btnAjouter.setFocusPainted(false);
        btnAjouter.setBounds(20, 430, 110, 30);
        btnAjouter.addActionListener(this);
        panelForm.add(btnAjouter);

        btnEffacer = new JButton("Effacer");
        btnEffacer.setBackground(new Color(149, 165, 166));
        btnEffacer.setForeground(WHITE);
        btnEffacer.setFocusPainted(false);
        btnEffacer.setBounds(140, 430, 110, 30);
        btnEffacer.addActionListener(this);
        panelForm.add(btnEffacer);

        btnModifier = new JButton("Modifier");
        btnModifier.setBackground(PRIMARY);
        btnModifier.setForeground(WHITE);
        btnModifier.setFocusPainted(false);
        btnModifier.setBounds(260, 430, 110, 30);
        btnModifier.addActionListener(this);
        btnModifier.setEnabled(false);
        panelForm.add(btnModifier);

        btnSupprimer = new JButton("Supprimer");
        btnSupprimer.setBackground(new Color(231, 76, 60));
        btnSupprimer.setForeground(WHITE);
        btnSupprimer.setFocusPainted(false);
        btnSupprimer.setBounds(20, 475, 110, 30);
        btnSupprimer.addActionListener(this);
        btnSupprimer.setEnabled(false);
        panelForm.add(btnSupprimer);

        
        JPanel panelTable = new JPanel(new BorderLayout());
        panelTable.setBackground(WHITE);
        panelTable.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PRIMARY), "Liste des Séances"));

        String[] colonnes = {"ID", "Jour", "Heure", "Enseignant", "Salle", "Groupe", "Matière"};
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
                    jourCombo.setSelectedItem(tableModel.getValueAt(row, 1).toString());
                    heureField.setText(tableModel.getValueAt(row, 2).toString());
                    selectionner(enseignantField, tableModel.getValueAt(row, 3).toString());
                    selectionner(salleField, tableModel.getValueAt(row, 4).toString());
                    selectionner(groupeField, tableModel.getValueAt(row, 5).toString());
                    selectionner(matiereField, tableModel.getValueAt(row, 6).toString());
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

        
        chargerListes();
        chargerDonnees();
    }

    
    private void selectionner(JComboBox<Item> combo, String label) {
        for (int i = 0; i < combo.getItemCount(); i++)
            if (combo.getItemAt(i).label.equals(label)) { combo.setSelectedIndex(i); return; }
        if (combo.getItemCount() > 0) combo.setSelectedIndex(0);
    }

    private Seance construire(int id, String heure) {
        Item e = (Item) enseignantField.getSelectedItem();
        Item sa = (Item) salleField.getSelectedItem();
        Item g = (Item) groupeField.getSelectedItem();
        Item m = (Item) matiereField.getSelectedItem();
        return new Seance(id, jourCombo.getSelectedItem().toString(), heure,
            e == null ? "" : e.label, sa == null ? "" : sa.label,
            g == null ? "" : g.label, m == null ? "" : m.label,
            e == null ? 0 : e.id, sa == null ? 0 : sa.id,
            g == null ? 0 : g.id, m == null ? 0 : m.id);
    }

    private void chargerListes() {
        try {
            for (Enseignant x : new EnseignantDAO().getTous())
                enseignantField.addItem(new Item(x.getId(), x.getNom() + " " + x.getPrenom()));
            for (Salle x : new SalleDAO().getTous())
                salleField.addItem(new Item(x.getId(), x.getNom()));
            for (Groupe x : new GroupeDAO().getTous())
                groupeField.addItem(new Item(x.getId(), x.getNom()));
            for (Matiere x : new MatiereDAO().getTous())
                matiereField.addItem(new Item(x.getId(), x.getNom()));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Erreur chargement des listes :\n" + ex.getMessage(), "Erreur BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void chargerDonnees() {
        tableModel.setRowCount(0);
        try {
            SeanceDAO dao = new SeanceDAO();
            List<Seance> liste = dao.getTous();
            for (Seance s : liste) {
                tableModel.addRow(new Object[]{
                    s.getId(), s.getJour(), s.getHeure(),
                    s.getEnseignant(), s.getSalle(), s.getGroupe(), s.getMatiere()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Erreur chargement :\n" + ex.getMessage(), "Erreur BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void effacerFormulaire() {
        jourCombo.setSelectedIndex(0);
        heureField.setText("");
        if (enseignantField.getItemCount() > 0) enseignantField.setSelectedIndex(0);
        if (salleField.getItemCount() > 0) salleField.setSelectedIndex(0);
        if (groupeField.getItemCount() > 0) groupeField.setSelectedIndex(0);
        if (matiereField.getItemCount() > 0) matiereField.setSelectedIndex(0);
        ligneEdition  = -1;
        idSelectionne = -1;
        table.clearSelection();
        btnAjouter.setEnabled(true);
        btnModifier.setEnabled(false);
        btnSupprimer.setEnabled(false);
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnAjouter) {
            String heure = heureField.getText().trim();
            if (heure.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "L'heure est obligatoire.", "Erreur", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                SeanceDAO dao = new SeanceDAO();
                Seance s = construire(0, heure);
                if (dao.ajouter(s)) {
                    chargerDonnees();
                    effacerFormulaire();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                    "Erreur ajout :\n" + ex.getMessage(), "Erreur BD", JOptionPane.ERROR_MESSAGE);
            }

        } else if (e.getSource() == btnModifier && ligneEdition >= 0) {
            String heure = heureField.getText().trim();
            if (heure.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "L'heure est obligatoire.", "Erreur", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                SeanceDAO dao = new SeanceDAO();
                Seance s = construire(idSelectionne, heure);
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
                "Supprimer cette séance ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    SeanceDAO dao = new SeanceDAO();
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
