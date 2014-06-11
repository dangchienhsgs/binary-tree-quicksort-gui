import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;

public class Node extends Canvas{
    String label;
    Point topLeft;
    Point topRight;
    Point bellowLeft;
    Point bellowRight;
    Point StringPosition;
    int nodeHeight=50;
    int nodeWidth=50;

    /*Constructor */
    public Node(Composite parent, int style){
        super (parent, style);
    }

    public Node(Composite parent, int style, final String label) {
        super(parent, style);
        this.label = label;
    }

    /* Set the node width */
    public void setNodeWidth(int nodeWidth){
        this.nodeWidth=nodeWidth;
    }

    /* Set the label */
    public void setLabel(String label){
        this.label=label;
        this.setToolTipText("value: "+label);
    }

    /* set the topleft include set Location*/
    public void setTopLeft(int x, int y){
        this.setLocation(x, y);
        this.setBounds(x, y, nodeWidth, nodeHeight);
        this.topLeft=this.getLocation();
    }

    /* reset new color */
    public void setColor(int background, int foreground){
        this.setBackground(getDisplay().getSystemColor(background));
        this.setForeground(getDisplay().getSystemColor(foreground));
    }

    /* get TopLeft corner */
    public Point getTopLeft(){
        return this.topLeft;
    }

    /* get TopRight corner */
    public Point getTopRight(){
        return new Point(this.getTopLeft().x+nodeWidth, this.getTopLeft().y);
    }

    /*get BellowLeft corner */
    public Point getBellowLeft(){
        return new Point(this.getTopLeft().x, this.getTopLeft().y+nodeHeight);
    }

    /* get bellowRight corner */
    public Point getBellowRight(){
        return new Point(this.getTopLeft().x+nodeWidth, this.getTopLeft().y+nodeHeight);
    }

    /* draw a Node */
    public void drawNode(){
        final Rectangle rec=this.getBounds();
        this.addPaintListener(new PaintListener() {
            @Override
            public void paintControl(PaintEvent e) {
                e.gc.drawString(label, 10, 15, true);
                e.gc.drawRectangle(0, 0, nodeWidth-1, nodeHeight-1);
            }
        });
    }

    /* transfrom */
}
