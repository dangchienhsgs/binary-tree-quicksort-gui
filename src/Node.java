import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public class Node extends Canvas{
    private String label;
    private Color background;
    private Color foreground;

    private Point point;
    private Point topLeft;
    private Point topRight;
    private Point bellowLeft;
    private Point bellowRight;
    private int height;
    private int width;
    public Node(Composite parent, int style){
        super (parent, style);
    }
    public Node(Composite parent, int style, final String label){
        super (parent, style);
        this.label=label;

    }
    public void draw (PaintEvent e){
        FontMetrics fontMetrics=e.gc.getFontMetrics();
        Point size=e.gc.stringExtent(label);

        e.gc.drawRectangle(point.x-size.y/3, point.y-size.y/4, size.x+size.y*2/3, size.y+size.y/2);
        e.gc.drawString(label, point.x ,point.y);

        this.topLeft=new Point(point.x-size.y/3, point.y-size.y/4);
        this.topRight=new Point(point.x+size.x+size.y/3, point.y-size.y/4);
        this.bellowLeft=new Point(point.x+size.y/3, point.y+size.y+size.y/4);
        this.bellowRight=new Point(point.x+size.x+size.y/3, point.y+size.y+size.y/4);

        this.width=this.topLeft.x-this.topRight.x;
        this.height=this.topLeft.y-this.bellowLeft.y;

    }
    public Point getTopLeft(){
        return this.topLeft;
    }
    public Point getBellowLeft(){
        return this.bellowLeft;
    }
    public Point getTopRight(){
        return this.topRight;
    }
    public Point getBellowRight(){
        return this.bellowRight;
    }
    public void setPoint(int x, int y){
        this.point=new Point(x, y);
    }
    public void setLabel(String label){
        this.label=label;
    }
    public String getLabel(){
        return this.label;
    }
    public Point getPoint(){
        return this.point;
    }
    public int getHeight(){
        return this.height;
    }
    public int getWidth(){
        return this.width;
    }
    public Color getBackground(){
        return this.background;
    }
    public void setBackground(Color color){
        this.background=color;
    }
    public Color getForeground(){
        return this.foreground;
    }
    public void setForeground(Color color){
        this.foreground=color;
    }
}
