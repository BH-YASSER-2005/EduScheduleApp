package v6;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * Adapte la taille de l'interface a l'ecran : meme mise en page, tout est
 * simplement agrandi proportionnellement (fenetres, composants, polices).
 */
public class UIScale {

    /** Facteur d'agrandissement : 1.0 (petit ecran) a 1.6, fenetre principale 1100x700 = 90 % max de l'ecran. */
    public static final double F;
    static {
        Dimension s = Toolkit.getDefaultToolkit().getScreenSize();
        double f = Math.min(s.width * 0.9 / 1100.0, s.height * 0.9 / 700.0);
        F = Math.max(1.0, Math.min(1.6, f));
    }

    public static int px(int v) { return (int) Math.round(v * F); }

    public static Dimension size(int w, int h) { return new Dimension(px(w), px(h)); }

    /** Agrandit recursivement un composant (positions, tailles, polices, bordures vides, lignes de tableau). */
    public static void apply(Component c) {
        if (F == 1.0 || c == null) return;
        if (c instanceof javax.swing.table.JTableHeader) return;   // traite via son JTable

        Font font = c.getFont();
        if (font != null) c.setFont(font.deriveFont((float) (font.getSize2D() * F)));

        if (c.isPreferredSizeSet()) {
            Dimension d = c.getPreferredSize();
            c.setPreferredSize(new Dimension(px(d.width), px(d.height)));
        }

        if (c instanceof JComponent) {
            JComponent jc = (JComponent) c;
            Border b = jc.getBorder();
            if (b instanceof TitledBorder) {
                TitledBorder tb = (TitledBorder) b;
                Font tf = tb.getTitleFont();
                if (tf != null) tb.setTitleFont(tf.deriveFont((float) (tf.getSize2D() * F)));
            } else if (b != null && b.getClass() == EmptyBorder.class) {
                Insets i = ((EmptyBorder) b).getBorderInsets();
                jc.setBorder(new EmptyBorder(px(i.top), px(i.left), px(i.bottom), px(i.right)));
            }
        }

        if (c instanceof JTable) {
            JTable t = (JTable) c;
            t.setRowHeight(px(t.getRowHeight()));
            Component h = t.getTableHeader();   // l'en-tete n'est rattache au JScrollPane qu'a l'affichage
            if (h != null) h.setFont(h.getFont().deriveFont((float) (h.getFont().getSize2D() * F)));
        }

        if (c instanceof Container) {
            Container ct = (Container) c;
            boolean absolu = ct.getLayout() == null;
            for (Component child : ct.getComponents()) {
                if (absolu) {
                    Rectangle r = child.getBounds();
                    child.setBounds(px(r.x), px(r.y), px(r.width), px(r.height));
                }
                apply(child);
            }
        }
    }
}
