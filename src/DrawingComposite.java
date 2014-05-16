import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.*;

/* This class Drawing Composite describe a Panel which display our tree
 * This class extends a ScrolledComposite which can be embedded into any shell, any composite
 */
public class DrawingComposite  extends ScrolledComposite{

    static Display display;
    static Shell shell1;
    Tree tree;
    Composite composite;

    // Constructor of Drawing Composite
    public DrawingComposite(Composite parent, int style){
        super(parent, style); //
        composite=new Composite(this, SWT.NONE);
        composite.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
        this.setExpandHorizontal(true);
        this.setExpandVertical(true);
        this.setContent(composite);
        this.tree=new Tree();
    }
    /////////////////////////////////////////////////////////////////////////////////
    // Draw all Node and lines
    public void DrawNodeAndLine(){
        PaintNode(tree.root, 1);
        PaintLineConnectNode(tree.root);
        this.setMinSize((tree.getWidth()+2) * 100, ((tree.getHeight(tree.root))+1) * 70);
    }
    ///////////////////////////////////////////////////////////////////////////////
    /* Two function is draw lines by recursion */
    public void PaintLineConnectNode(final TreeNode treeNode){
        final Node node=treeNode.node;
        if (treeNode.left!=null){
            final Node left=treeNode.left.node;
            composite.addPaintListener(new PaintListener() {
                @Override
                public void paintControl(PaintEvent e) {
                    drawLineConnect(e, node, left);
                }
            });
            PaintLineConnectNode(treeNode.left);
        }
        if (treeNode.right!=null){
            final Node right=treeNode.right.node;
            composite.addPaintListener(new PaintListener() {
                @Override
                public void paintControl(PaintEvent e) {
                    drawLineConnect(e, node, right);
                }
            });
            PaintLineConnectNode(treeNode.right);
        }
    }
    public void drawLineConnect(PaintEvent e, Node node1, Node node2){
        Point middle1=new Point((node1.getBellowLeft().x+node1.getBellowRight().x)/2,(node1.getBellowLeft().y+node1.getBellowRight().y)/2);
        Point middle2=new Point((node2.getTopLeft().x+node2.getTopRight().x)/2,(node2.getTopLeft().y+node2.getTopRight().y)/2);

        e.gc.drawLine(middle1.x, middle1.y, middle2.x, middle2.y);
    }
    //////////////////////////////////////////////////////////////////////////////////
    /* Draw all node off tree */
    public void PaintNode(TreeNode treeNode, int level){
        PaintTreeNode(treeNode, level);
        if (treeNode.left!=null){
            PaintNode(treeNode.left, level+1);
        }
        if (treeNode.right!=null){
            PaintNode(treeNode.right, level+1);
        }

    }
    public void PaintTreeNode(final TreeNode treeNode, final int level){

        // Create new node
        treeNode.node=new Node(composite, SWT.BORDER);

        // Set its background and foreground
        treeNode.node.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
        treeNode.node.setForeground(display.getSystemColor(SWT.COLOR_BLACK));

        // Set its label and bounds
        treeNode.node.setLabel(Integer.toString(treeNode.getValue()));
        treeNode.node.setTopLeft(100 * (treeNode.position - tree.getLeft()) + 20, 70 * (level - 1) + 20);
        treeNode.node.setBounds(treeNode.node.getTopLeft().x, treeNode.node.getTopLeft().y, treeNode.node.nodeWidth , treeNode.node.nodeHeight);

        // Draw it
        treeNode.node.drawNode();

        // Set the mouse  actions with this node

        //Set drag and drop
        final Point[] offset = new Point[1];
        treeNode.node.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDown(MouseEvent e) {
                offset[0]=new Point(e.x, e.y);
            }
            public void mouseUp(MouseEvent e){
                offset[0] = null;
            }
        });
        treeNode.node.addMouseMoveListener(new MouseMoveListener() {
            @Override
            public void mouseMove(MouseEvent e) {
                if (offset[0] != null) {
                    Point pt = offset[0];
                    Point pt2=new Point(e.x, e.y);
                    Point trans=new Point(pt2.x-pt.x, pt2.y-pt.y);
                    treeNode.node.setTopLeft(treeNode.node.getBounds().x+trans.x, treeNode.node.getBounds().y+trans.y);
                    composite.redraw();
                }
            }
        });
        // add Mouse track listener
        treeNode.node.addMouseTrackListener(new MouseTrackAdapter() {
            @Override
            public void mouseExit(MouseEvent e) {
                if (treeNode.left!=null){
                    treeNode.left.node.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
                    treeNode.left.node.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
                }
                if (treeNode.right!=null){
                    treeNode.right.node.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
                    treeNode.right.node.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
                }
            }

            public void mouseEnter(MouseEvent e){
                if (treeNode.left!=null){
                    treeNode.left.node.setBackground(display.getSystemColor(SWT.COLOR_RED));
                    treeNode.left.node.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
                }
                if (treeNode.right!=null){
                    treeNode.right.node.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
                    treeNode.right.node.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
                }
            }
        });
    }
    ///////////////////////////////////////////////////////////////////////////////////


    public static void main(String args[]){
        display=new Display();
        shell1=new Shell(display);
        Shell shell2=new Shell(display);
        shell1.setLayout(new FillLayout());
        DrawingComposite drawPanel=new DrawingComposite(shell1, SWT.BORDER| SWT.V_SCROLL| SWT.H_SCROLL);
        drawPanel.tree.makeRoot(4);
        drawPanel.tree.addNode(5, drawPanel.tree.root);
        drawPanel.tree.addNode(10, drawPanel.tree.root);
        drawPanel.tree.addNode(1, drawPanel.tree.root);
        drawPanel.tree.addNode(2, drawPanel.tree.root);
        drawPanel.tree.addNode(100, drawPanel.tree.root);
        drawPanel.tree.addNode(200, drawPanel.tree.root);
        drawPanel.tree.addNode(90, drawPanel.tree.root);
        drawPanel.tree.addNode(10, drawPanel.tree.root);
        drawPanel.tree.addNode(-4, drawPanel.tree.root);
        drawPanel.tree.addNode(-6, drawPanel.tree.root);
        drawPanel.tree.addNode(-7, drawPanel.tree.root);
        drawPanel.DrawNodeAndLine();


        Menu menuBar=new Menu(shell1, SWT.BAR);
        MenuItem fileMenuHeader=new MenuItem(menuBar, SWT.CASCADE);
        fileMenuHeader.setText("Edit");

        Menu fileMenu=new Menu(shell1, SWT.DROP_DOWN);
        fileMenuHeader.setMenu(fileMenu);
        MenuItem fileSaveItem=new MenuItem(fileMenu, SWT.PUSH);
        fileSaveItem.setText("Save");
        shell1.setMenuBar(menuBar);

        shell1.open();
        shell1.layout();
        while (!shell1.isDisposed()){
            if (!display.readAndDispatch()){
                display.sleep();
            }
        }
    }
}

