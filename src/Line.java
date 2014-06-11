import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Point;

public class Line {
    PaintListener paintListener;
    Node node1;
    Node node2;
    public Line(Node start, Node end){
        this.node1=start;
        this.node2=end;
        paintListener=new PaintListener() {
            @Override
            public void paintControl(PaintEvent e) {
                Point middle1 = new Point((node1.getBellowLeft().x + node1.getBellowRight().x) / 2, (node1.getBellowLeft().y + node1.getBellowRight().y) / 2);
                Point middle2 = new Point((node2.getTopLeft().x + node2.getTopRight().x) / 2, (node2.getTopLeft().y + node2.getTopRight().y) / 2);
                e.gc.drawLine(middle1.x, middle1.y, middle2.x, middle2.y);
            }
        };
    }
}
