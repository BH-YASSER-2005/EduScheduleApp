package v6;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;


public class VueEtudiantUI extends JFrame implements ActionListener {

    private String nomEtudiant;
    private int groupeId;
    private DefaultTableModel tableModel;
    private JTable table;
    private JButton btnDeconnexion;

    
    private final Color PRIMARY    = new Color(41, 128, 185);
    private final Color BACKGROUND = new Color(236, 240, 241);
    private final Color WHITE      = Color.WHITE;
    private final Color TEXT       = new Color(44, 62, 80);

    
    public VueEtudiantUI(Utilisateur utilisateur) {
        this.nomEtudiant = utilisateur.getIdentifiant();
        this.groupeId    = utilisateur.getGroupeId();

        setTitle("EduSchedule — Espace Étudiant");
        setSize(UIScale.size(750, 550));
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        
        JPanel topBar = new JPanel(null);
        topBar.setBackground(PRIMARY);
        topBar.setPreferredSize(new Dimension(750, 60));

        JLabel titre = new JLabel("Bonjour, " + nomEtudiant + "  —  Emploi du temps");
        titre.setFont(new Font("Arial", Font.BOLD, 16));
        titre.setForeground(WHITE);
        titre.setBounds(20, 18, 500, 24);
        topBar.add(titre);

        btnDeconnexion = new JButton("Déconnexion");
        btnDeconnexion.setBounds(610, 15, 110, 30);
        btnDeconnexion.setBackground(WHITE);
        btnDeconnexion.setForeground(PRIMARY);
        btnDeconnexion.setFocusPainted(false);
        btnDeconnexion.setBorder(BorderFactory.createLineBorder(WHITE));
        btnDeconnexion.addActionListener(this);
        topBar.add(btnDeconnexion);

        
        JPanel panelCentre = new JPanel(new BorderLayout());
        panelCentre.setBackground(BACKGROUND);
        panelCentre.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel panelTable = new JPanel(new BorderLayout());
        panelTable.setBackground(WHITE);
        panelTable.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PRIMARY), "Liste des Séances"));

        
        String[] colonnes = {"Matière", "Enseignant", "Salle", "Jour", "Heure"};
        tableModel = new DefaultTableModel(colonnes, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        table = new JTable(tableModel);
        table.setBackground(WHITE);
        table.setForeground(TEXT);
        table.setRowHeight(30);
        table.getTableHeader().setBackground(PRIMARY);
        table.getTableHeader().setForeground(WHITE);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        panelTable.add(new JScrollPane(table), BorderLayout.CENTER);
        panelCentre.add(panelTable, BorderLayout.CENTER);

        
        JLabel info = new JLabel(
            "Emploi du temps en lecture seule. Pour toute question, contactez l'administration.");
        info.setForeground(TEXT);
        info.setFont(new Font("Arial", Font.ITALIC, 11));
        info.setHorizontalAlignment(SwingConstants.CENTER);
        info.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        panelCentre.add(info, BorderLayout.SOUTH);

        add(topBar, BorderLayout.NORTH);
        add(panelCentre, BorderLayout.CENTER);

        
        chargerDonnees();

        setLocationRelativeTo(null);
        UIScale.apply(topBar);
        UIScale.apply(panelCentre);
        setVisible(true);
    }

    
    private void chargerDonnees() {
        tableModel.setRowCount(0);
        try {
            SeanceDAO dao = new SeanceDAO();
            List<Seance> liste = dao.getParGroupeId(groupeId);
            for (Seance s : liste) {
                tableModel.addRow(new Object[]{
                    s.getMatiere(), s.getEnseignant(), s.getSalle(), s.getJour(), s.getHeure()
                });
            }
            if (liste.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    groupeId <= 0 ? "Ce compte n'est rattaché à aucun groupe. Contactez l'administrateur."
                                : "Aucune séance disponible pour le moment.",
                    "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Erreur chargement :\n" + ex.getMessage(), "Erreur BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnDeconnexion) {
            this.dispose();
            new LoginUI();
        }
    }
}
