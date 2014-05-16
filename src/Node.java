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
    public void setNodeWidth(int nodeWidth){
        this.nodeWidth=nodeWidth;
    }
    public void setLabel(String label){
        this.label=label;
        this.setToolTipText(label);
    }
    public void setTopLeft(int x, int y){
        this.setLocation(x, y);
        this.topLeft=this.getLocation();
    }
    public Node(Composite parent, int style){
        super (parent, style);
    }
    public Node(Composite parent, int style, final String label) {
        super(parent, style);
        this.label = label;
    }
    public Point getTopLeft(){
        return this.topLeft;
    }
    public Point getTopRight(){
        return new Point(this.getTopLeft().x+nodeWidth, this.getTopLeft().y);
    }
    public Point getBellowLeft(){
        return new Point(this.getTopLeft().x, this.getTopLeft().y+nodeHeight);
    }
    public Point getBellowRight(){
        return new Point(this.getTopLeft().x+nodeWidth, this.getTopLeft().y+nodeHeight);
    }
    public void drawNode(){
        final Rectangle rec=this.getBounds();
        this.addPaintListener(new PaintListener() {
            @Override
            public void paintControl(PaintEvent e) {
                ////e.gc.setBackground(getDisplay().getSystemColor(SWT.CO));
                //e.gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_GRAY));
               // e.gc.fillGradientRectangle(0, 0, nodeWidth, nodeHeight, true);
                //e.gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_BLACK));
                e.gc.drawString(label, 10, 10, true);
                e.gc.drawRectangle(0, 0, nodeWidth, nodeHeight);
            }
        });
    }
    }
