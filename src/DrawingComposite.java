
/* This class Drawing Composite describe a Panel which display our tree
 * This class extends a ScrolledComposite which can be embedded into any shell, any composite
 */

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.*;

public class DrawingComposite extends ScrolledComposite {

    Tree tree; // The tree of this program
    Composite composite; // The container of nodes in the screen
    Label statusBar; // The bellow status bar
    TreeNode clickedNode;

    /* constructor */
    public void delay(int time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DrawingComposite(Composite parent, int style) {
        super(parent, style); // Inherit
        this.setLayout(new FillLayout()); // Setlayout
        // Create a status bar
        statusBar = new Label(getParent(), SWT.NONE);
        statusBar.setText("Welcome to TreeApplication !");
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
        this.tree = new Tree();
    }

    /* Draw a line connecting two node */
    /*public void drawLineConnect(PaintEvent e, Node node1, Node node2) {
        Point middle1 = new Point((node1.getBellowLeft().x + node1.getBellowRight().x) / 2, (node1.getBellowLeft().y + node1.getBellowRight().y) / 2);
        Point middle2 = new Point((node2.getTopLeft().x + node2.getTopRight().x) / 2, (node2.getTopLeft().y + node2.getTopRight().y) / 2);
        e.gc.drawLine(middle1.x, middle1.y, middle2.x, middle2.y);
    }*/

    /* add MouseListener for the nodes */
    public void setNodeMouseListener(final TreeNode treeNode) {

        Menu menu = new Menu(treeNode.node);
        treeNode.node.setMenu(menu);


        final Point[] offset = new Point[1];
        // add mouse click
        treeNode.node.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDown(MouseEvent e) {
                clickedNode = treeNode;
                offset[0] = new Point(e.x, e.y);
            }

            public void mouseUp(MouseEvent e) {
                offset[0] = null;
            }
        });
        // add mouse move
        treeNode.node.addMouseMoveListener(new MouseMoveListener() {
            @Override
            public void mouseMove(MouseEvent e) {
                if (offset[0] != null) {
                    Point pt = offset[0];
                    Point pt2 = new Point(e.x, e.y);
                    Point trans = new Point(pt2.x - pt.x, pt2.y - pt.y);
                    treeNode.node.setTopLeft(treeNode.node.getBounds().x + trans.x, treeNode.node.getBounds().y + trans.y);
                    composite.redraw();
                }
            }
        });
        // add Mouse track listener
        treeNode.node.addMouseTrackListener(new MouseTrackAdapter() {
            @Override
            public void mouseExit(MouseEvent e) {
                treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                if (treeNode.left != null) {
                    treeNode.left.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                }
                if (treeNode.right != null) {
                    treeNode.right.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                }
                if (treeNode.parent != null) {
                    treeNode.parent.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                }
            }

            public void mouseEnter(MouseEvent e) {
                treeNode.node.setColor(SWT.COLOR_DARK_MAGENTA, SWT.COLOR_WHITE);
                if (treeNode.left != null) {
                    treeNode.left.node.setColor(SWT.COLOR_RED, SWT.COLOR_WHITE);
                }
                if (treeNode.right != null) {
                    treeNode.right.node.setColor(SWT.COLOR_BLACK, SWT.COLOR_WHITE);
                }
                if (treeNode.parent != null) {
                    treeNode.parent.node.setColor(SWT.COLOR_BLUE, SWT.COLOR_WHITE);
                }
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////
    TreeNode treeNode;
    TreeNode rightMinNode;
    public void remove_findNode(final int value){
        treeNode.node.setColor(SWT.COLOR_DARK_MAGENTA, SWT.COLOR_WHITE);
        if (treeNode.getValue()>value){
            KeyListener keyListener=new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent keyEvent) {
                    if (keyEvent.keyCode==13){
                        treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                        removeKeyListener(this);
                        treeNode = treeNode.left;
                        remove_findNode(value);
                    }
                }
            };
            this.addKeyListener(keyListener);
        } else if (treeNode.getValue()<value){
            KeyListener keyListener=new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent keyEvent) {
                    if (keyEvent.keyCode==13){
                        treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                        removeKeyListener(this);
                        treeNode = treeNode.right;
                        remove_findNode(value);
                    }
                }
            };
            this.addKeyListener(keyListener);
        }
        else if (treeNode.getValue()==value){
            if (treeNode.right!=null){
                // Da xac dinh duoc ndoe can xoa
                rightMinNode=treeNode.right;
                remove_findMinNode();
            }
        }
    }
    public void remove_findMinNode (){
        rightMinNode.node.setColor(SWT.COLOR_MAGENTA, SWT.COLOR_WHITE);
        KeyListener keyListener=new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent keyEvent) {
                    if (keyEvent.keyCode==13){
                        removeKeyListener(this);
                        rightMinNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                        if (rightMinNode.left!=null){
                            rightMinNode=rightMinNode.left;
                            remove_findMinNode();
                        } else {
                            // Da xac dinh duoc node min right
                            remove_changeValue();
                        }
                    }
                }
            };
        this.addKeyListener(keyListener);
    }
    public void remove_changeValue(){
        treeNode.setValue(rightMinNode.getValue());
        treeNode.node.setLabel(new Integer(rightMinNode.getValue()).toString());
        treeNode.node.drawNode();
        treeNode.node.redraw();
        rightMinNode.node.dispose();

        rightMinNode=rightMinNode.right;
        resetBounds(tree.root);
        resetLine(treeNode);
        composite.redraw();
    }
    public void resetLine(TreeNode treeNode){
        if (treeNode!=null){
            if (treeNode.left!=null){
                composite.removePaintListener(treeNode.lineConnectLeft.paintListener); // xoa cai line cu
                treeNode.lineConnectLeft=new Line(treeNode.node, treeNode.left.node); // tao cai line moi
                composite.addPaintListener(treeNode.lineConnectLeft.paintListener); // add cai moi

                treeNode.left.lineConnectParent=treeNode.lineConnectLeft;

                resetLine(treeNode.left);
            }
            if (treeNode.right!=null){
                composite.removePaintListener(treeNode.lineConnectRight.paintListener); // xoa cai line cu
                treeNode.lineConnectRight=new Line(treeNode.node, treeNode.right.node); // tao cai line moi
                composite.addPaintListener(treeNode.lineConnectRight.paintListener);

                treeNode.right.lineConnectParent=treeNode.lineConnectParent;

                resetLine(treeNode.left);
            }
        }
    }
    ////////////////////////////////////////////////////////
    /* This is the part of constructing browsing node
     *
     */
    TreeNode nodeQuene[] = new TreeNode[100];
    int n = 0;

    void createQueneNodeFirst(TreeNode treeNode) {
        if (treeNode != null) {
            nodeQuene[n] = treeNode;
            n = n + 1;
            createQueneNodeFirst(treeNode.left);
            createQueneNodeFirst(treeNode.right);

        }
    }
    void createQueneNodeMiddle(TreeNode treeNode) {
        if (treeNode != null) {
            createQueneNodeMiddle(treeNode.left);
            nodeQuene[n] = treeNode;
            n = n + 1;
            createQueneNodeMiddle(treeNode.right);

        }
    }
    void createQueneNodeLast(TreeNode treeNode) {
        if (treeNode != null) {
            createQueneNodeLast(treeNode.left);
            createQueneNodeLast(treeNode.right);
            nodeQuene[n] = treeNode;
            n = n + 1;
        }
    }

    void browseNode(final int i) {
        if ((i < n)) {
            nodeQuene[i].node.setColor(SWT.COLOR_DARK_MAGENTA, SWT.COLOR_WHITE);
            KeyListener keyListener = new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent keyEvent) {
                    removeKeyListener(this);
                    nodeQuene[i].node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                    browseNode(i + 1);
                }
            };
            this.addKeyListener(keyListener);
        }
    }

    public void browseAllNode(TreeNode treeNode, int typeBrowse) {
        n = 0;
        if (typeBrowse==1){
            createQueneNodeFirst(treeNode);
        } else if (typeBrowse==2){
            createQueneNodeMiddle(treeNode);
        } else if (typeBrowse==3){
            createQueneNodeLast(treeNode);
        }
        browseNode(0);
    }

    ///////////////////////////////////////////////////////////
    public void nodeMovement(final Node node, final int destX, final int destY) {
        final int i, j;
        final int TIMER_INTERVAL = 10;
        int startX = node.getLocation().x;
        int startY = node.getLocation().y;
        if (startX > destX) i = -1;
        else i = 1;
        if (startY > destY) j = -1;
        else j = 1;
        Runnable runnable = new Runnable() {
            public void run() {
                if ((node.getLocation().x != destX) && (node.getLocation().y != destY)) {
                    node.setBounds(node.getLocation().x + i, node.getLocation().y + j, node.nodeWidth, node.nodeHeight);
                    node.redraw();
                    getDisplay().timerExec(TIMER_INTERVAL, this);
                }
            }
        };

        getDisplay().timerExec(TIMER_INTERVAL, runnable);

    }

    /* add a node not include steps */
    ///////////////////////////////////////////////////////////////////
    public void addNode(int value) {
        final TreeNode newNode = tree.addNode(value, tree.root);
        resetBounds(tree.root);
        newNode.node.drawNode();
        setNodeMouseListener(newNode);
        if (newNode.parent != null) {
            newNode.lineConnectParent=new Line(newNode.parent.node, newNode.node);
            if (newNode.parent.left==newNode){
                newNode.parent.lineConnectLeft=newNode.lineConnectParent;
                composite.addPaintListener(newNode.parent.lineConnectLeft.paintListener);
            }
            if (newNode.parent.right==newNode){
                newNode.parent.lineConnectRight=newNode.lineConnectParent;
                composite.addPaintListener(newNode.parent.lineConnectRight.paintListener);
            }
        }
        composite.redraw();
        this.setMinSize((tree.getWidth() + 2) * 100, ((tree.getHeight(tree.root)) + 1) * 70);
    }

    /* add a node include steps */
    public void addNodeStep(final TreeNode treeNode, final int value) {
        if (treeNode == null) {
            addNode(value);
        } else {

            treeNode.node.setColor(SWT.COLOR_DARK_MAGENTA, SWT.COLOR_WHITE);

            if (treeNode.getValue() > value) {

                String s = "This node has the value : " + treeNode.getValue() + " higher than " + value + " \n";
                s = s + ", we must go to the left";
                s = s + " press Enter to continue";
                statusBar.setText(s);
                KeyListener keyListener = new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.keyCode == 13) {

                            treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);

                            removeKeyListener(this);
                            addNodeStep(treeNode.left, value);
                        }
                    }
                };
                this.addKeyListener(keyListener);
            } else if (treeNode.getValue() <= value) {
                String s = "This node has the value : " + treeNode.getValue() + " lower than " + value;
                s = s + ", we must go to the right";
                s = s + ", please press Enter to go to next step";
                statusBar.setText(s);
                KeyListener keyListener = new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.keyCode == 13) {

                            treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);

                            removeKeyListener(this);
                            addNodeStep(treeNode.right, value);
                        }
                    }
                };
                this.addKeyListener(keyListener);
            }
        }
    }

    /* Reset posstion of this node */
    public void resetBounds(TreeNode treeNode) {
        if (treeNode != null) {
            if (treeNode.node == null) {
                treeNode.node = new Node(composite, SWT.NONE);
            }
            if (!treeNode.node.isDisposed()){
                System.out.println (treeNode.getValue());
                treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                treeNode.node.setLabel(Integer.toString(treeNode.getValue()));
                treeNode.node.setTopLeft(100 * (treeNode.position - tree.getLeft()) + 20, 70 * (treeNode.level - 1) + 20);
                treeNode.node.setBounds(treeNode.node.getTopLeft().x, treeNode.node.getTopLeft().y, treeNode.node.nodeWidth, treeNode.node.nodeHeight);
                resetBounds(treeNode.left);
                resetBounds(treeNode.right);
            }
        }
    }

    /* find a node by value */
    public TreeNode findNode(final TreeNode treeNode, final int value) {
        if (treeNode == null) {
            statusBar.setText("There is no node");
        } else {
            treeNode.node.setColor(SWT.COLOR_DARK_MAGENTA, SWT.COLOR_WHITE);
            if (treeNode.getValue() == value) {
                statusBar.setText("Find a node that have the value +" + value);
                statusBar.setText("Press enter to continume");
                KeyListener keyListener = new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.keyCode == 13) {
                            treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                            removeKeyListener(this);
                        } else if (e.keyCode==27){
                            removeKeyListener(this);
                            treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                            findNode(treeNode.parent, value);
                        }
                    }
                };
                this.addKeyListener(keyListener);
                //System.out.println (treeNode.getValue());
            }
            if (treeNode.getValue() > value) {
                String s = "This node has the value : " + treeNode.getValue() + " higher than " + value + " \n";
                s = s + ", we must go to the left";
                s = s + " press Enter to continue";
                statusBar.setText(s);
                KeyListener keyListener = new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.keyCode == 13) {
                            treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                            removeKeyListener(this);
                            findNode(treeNode.left, value);
                        } else if (e.keyCode==27){
                            removeKeyListener(this);
                            treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                            findNode(treeNode.parent, value);
                        }
                    }
                };
                this.addKeyListener(keyListener);
            } else if (treeNode.getValue() < value) {
                String s = "This node has the value : " + treeNode.getValue() + " lower than " + value;
                s = s + ", we must go to the right";
                s = s + ", please press Enter to go to next step";
                statusBar.setText(s);
                KeyListener keyListener = new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.keyCode == 13) {
                            treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                            removeKeyListener(this);
                            findNode(treeNode.right, value);
                        } else if (e.keyCode==27){
                            removeKeyListener(this);
                            treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                            findNode(treeNode.parent, value);
                        }
                    }
                };
                this.addKeyListener(keyListener);
            }
        }
        return null;
    }

}

