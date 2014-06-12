
/* This class Drawing Composite describe a Panel which display our tree
 * This class extends a ScrolledComposite which can be embedded into any shell, any composite
 */

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import java.security.Key;

public class HeapComposite extends ScrolledComposite {

    HeapTree tree; // The tree of this program
    Composite composite; // The container of nodes in the screen
    Label statusBar; // The bellow status bar

    public HeapComposite(Composite parent, int style) {
        super(parent, style); // Inherit
        this.setLayout(new FillLayout()); // Setlayout
        // Create a status bar
        statusBar = new Label(getParent(), SWT.NONE);
        // Set Layout for the status bar
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = 4;
        statusBar.setLayoutData(gridData);

        // Create this composite
        composite = new Composite(this, SWT.NONE);
        composite.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
        this.setExpandHorizontal(true);
        this.setExpandVertical(true);
        this.setContent(composite);
    }

    HeapNode clickedNode;

    public void setNodeMouseListener(final HeapNode heapNode) {

        Menu menu = new Menu(heapNode.node);
        heapNode.node.setMenu(menu);

        final Point[] offset = new Point[1];
        // add mouse click
        heapNode.node.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDown(MouseEvent e) {
                clickedNode = heapNode;
                offset[0] = new Point(e.x, e.y);
            }

            public void mouseUp(MouseEvent e) {
                offset[0] = null;
            }
        });
        // add mouse move
        heapNode.node.addMouseMoveListener(new MouseMoveListener() {
            @Override
            public void mouseMove(MouseEvent e) {
                if (offset[0] != null) {
                    Point pt = offset[0];
                    Point pt2 = new Point(e.x, e.y);
                    Point trans = new Point(pt2.x - pt.x, pt2.y - pt.y);
                    heapNode.node.setTopLeft(heapNode.node.getBounds().x + trans.x, heapNode.node.getBounds().y + trans.y);
                    composite.redraw();
                }
            }
        });
    }

    public void drawCompostite() {
        for (int i = 0; i < tree.n; i++) {
            tree.heapNode[i].node = new Node(composite, SWT.BORDER);
            tree.heapNode[i].node.setLabel(new Integer(tree.heapNode[i].value).toString());
            tree.heapNode[i].node.setTopLeft(tree.heapNode[i].position + 20, 70 * (tree.heapNode[i].level - 1) + 20);
            tree.heapNode[i].node.drawNode();
            setNodeMouseListener(tree.heapNode[i]);
            if (i != 0) {
                int parent = (i + 1) / 2 - 1;
                if (parent < tree.n) {
                    Line line = new Line(tree.heapNode[parent].node, tree.heapNode[i].node);
                    tree.heapNode[i].lineConnectParent=line;
                    composite.addPaintListener(line.paintListener);
                    composite.redraw();
                }
            }
        }
        this.setMinSize(tree.n * 200, tree.getHeight() * 150);
    }

    public void setTree(int a[]) {
        this.tree = new HeapTree(a);
    }

    public void resetBounds() {
        tree.setDistict(tree.heapNode[0]);
        tree.setPostion(tree.heapNode[0]);
        tree.setLevel(tree.heapNode[0]);
    }

    HeapNode indexNode;
    int largest;
    int preNode;

    public void maxHeapify(final int i, final int numberOfNode) {
        tree.heapNode[i].node.setColor(SWT.COLOR_DARK_MAGENTA, SWT.COLOR_WHITE);
        largest = i;
        if ((i * 2 + 1 < numberOfNode) && (tree.heapNode[i].left.value > tree.heapNode[i].value)) {
            largest = 2 * i + 1;
        } else largest = i;
        if ((i * 2 + 2 < numberOfNode) && (tree.heapNode[i].right.value > tree.heapNode[largest].value)) {
            largest = 2 * i + 2;
        }
        preNode = i - 1;

        if (largest != i) {
            tree.heapNode[largest].node.setColor(SWT.COLOR_MAGENTA, SWT.COLOR_WHITE);

            KeyListener keyListener = new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent keyEvent) {
                    if (keyEvent.keyCode == 13) {
                        composite.removeKeyListener(this);
                        int temp = tree.heapNode[i].value;
                        tree.heapNode[i].value = tree.heapNode[largest].value;
                        tree.heapNode[largest].value = temp;

                        tree.heapNode[i].node.setLabel(new Integer(tree.heapNode[i].value).toString());
                        tree.heapNode[largest].node.setLabel(new Integer(tree.heapNode[largest].value).toString());

                        tree.heapNode[i].node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                        tree.heapNode[largest].node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);

                        tree.heapNode[i].node.drawNode();
                        tree.heapNode[largest].node.redraw();

                        maxHeapify(largest, numberOfNode);
                    }
                }
            };
            composite.addKeyListener(keyListener);
        } else {
            tree.heapNode[i].node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
            if (preNode >= 0) maxHeapify(preNode, numberOfNode); else {
                heapSort();
            }
        }
    }

    public void buildMaxHeap() {
        maxHeapify(tree.n / 2 - 1, tree.n);
    }
    int numberOfNode;
    String str;
    public void heapSort() {

        final int k = numberOfNode-1;
        if (k==0){
            tree.heapNode[0].node.dispose();
            new PopupDialog(getShell()).open("Heap Sort", "Chuoi da sap xep tu lon den be: "+str);
            getShell().dispose();
        } else {

            tree.heapNode[k].node.setColor(SWT.COLOR_DARK_MAGENTA, SWT.COLOR_WHITE);
            tree.heapNode[0].node.setColor(SWT.COLOR_MAGENTA, SWT.COLOR_WHITE);

            KeyListener keyListener = new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent keyEvent) {
                    if (keyEvent.keyCode == 13) {
                        composite.removeKeyListener(this);

                        int temp = tree.heapNode[k].value;
                        tree.heapNode[k].value = tree.heapNode[0].value;
                        tree.heapNode[0].value = temp;

                        tree.heapNode[k].node.setLabel(new Integer(tree.heapNode[k].value).toString());
                        tree.heapNode[0].node.setLabel(new Integer(tree.heapNode[0].value).toString());

                        System.out.println ("Sau khi : "+tree.heapNode[k].node.label+" "+tree.heapNode[0].node.label);

                        tree.heapNode[k].node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                        tree.heapNode[0].node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);


                        tree.heapNode[k].node.redraw();
                        tree.heapNode[0].node.redraw();

                        composite.removePaintListener(tree.heapNode[k].lineConnectParent.paintListener);
                        composite.redraw();

                        tree.heapNode[k].node.dispose();

                        statusBar.setText(statusBar.getText()+"    "+tree.heapNode[k].value);
                        str=str+new Integer(tree.heapNode[k].value).toString()+"      ";
                        numberOfNode=numberOfNode-1;
                        if (numberOfNode>0) maxHeapify(0, numberOfNode);

                    }
                }
            };
            composite.addKeyListener(keyListener);
        }
    }

    public static void main(String args[]) {

    }
}

