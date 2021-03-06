package cs412.dinghyprop.interpreter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
* A drawable node in a tree.
*/
final class Node extends JComponent {
    private static Logger log = Logger.getLogger("Node");
    private static final int HSPACE = 20;
    private static final int VSPACE = 20;
    private static final long serialVersionUID = 8064723739784397824L;
    String value;
    List<Node> children;
    int x = 0, y = VSPACE;
    int w = 0, h = 0;
    int strHeight = 0;

    /**
     * Create a new Node tree.
     * @param value       The value to print for this node.
     * @param children    The subtree below this node.
     */
    public Node(String value, List<Node> children) {
        this.value = value;
        if (children == null) {
            this.children = new ArrayList<Node>(0);
        } else {
            this.children = children;
        }
    }

    public void computeSize(Graphics g) {
        log.entering("Node", "computeSize");

        FontMetrics fm = g.getFontMetrics();
        int height = (int) fm.getLineMetrics(value, g).getHeight();
        strHeight = height;
        int width = fm.stringWidth(value);

        int maxH = 0, maxW = 0;
        for (Node n : children) {
            n.computeSize(g);
            maxW = (n.w > maxW) ? n.w : maxW;
            maxH = (n.h > maxH) ? n.h : maxH;
        }

        // normalize children's widths
        for (Node n : children) {
            n.w = maxW;
        }

        w = maxW * children.size();
        h = height;
        if (! children.isEmpty()) {
            w += ((children.size() - 1) * HSPACE);
            h += VSPACE + maxH;
        }
        // ensure this width accounts for the width of 'value'
        if (w < width) {
            w = width;
        }
    }

    @Override
    public void paint(Graphics g) {
        log.entering("Node ", "paint");

        int strWidth = g.getFontMetrics().stringWidth(value);
        g.drawString(value, x + w/2 - strWidth/2, y);
        int cx = x;
        int cy = y + strHeight + VSPACE;
        for (Node node : children) {
            node.x = cx;
            node.y = cy;
            g.drawLine(x + w/2, y + strHeight/2, node.x + node.w/2, node.y - strHeight);
            node.paint(g);
            cx += node.w + HSPACE;
        }
    }
}
