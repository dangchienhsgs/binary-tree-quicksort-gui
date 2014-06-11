
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

import java.security.Key;

public class DrawingComposite extends ScrolledComposite {

    Tree tree; // The tree of this program
    Composite composite; // The container of nodes in the screen
    Label statusBar; // The bellow status bar
    TreeNode clickedNode;

    public DrawingComposite(Composite parent, int style) {
        super(parent, style); // Inherit
        this.setLayout(new FillLayout()); // Setlayout
        // Create a status bar
        statusBar = new Label(getParent(), SWT.NONE);
        statusBar.setText("Nhom 3 KSTN CNTT K57");
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
    }

    ////////////////////////////////////////////////////////////////////////////////////////
    TreeNode treeNode;
    TreeNode rightMinNode;

    PaintListener paintListener[] = new PaintListener[1000];
    int countListener = 0;
    public void reDrawComposite(){
        removeAllPaintListener();
        tree.computeAllNodeTree();
        resetBounds(tree.root);
        resetLine(tree.root);
        composite.redraw();
    }

    public void remove_findNode(final int value) {
        if (treeNode==null){
            new PopupDialog(getShell()).open("Remove a node", "The tree doesn't have any node with the value: "+new Integer(value).toString());
        } else
        if (treeNode.getValue() > value) {
            treeNode.node.setColor(SWT.COLOR_DARK_MAGENTA, SWT.COLOR_WHITE);
            KeyListener keyListener = new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent keyEvent) {
                    if (keyEvent.keyCode == 13) {
                        treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                        removeKeyListener(this);
                        treeNode = treeNode.left;
                        remove_findNode(value);
                    }
                }
            };
            this.addKeyListener(keyListener);
        } else if (treeNode.getValue() < value) {
            treeNode.node.setColor(SWT.COLOR_DARK_MAGENTA, SWT.COLOR_WHITE);
            KeyListener keyListener = new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent keyEvent) {
                    if (keyEvent.keyCode == 13) {
                        treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                        removeKeyListener(this);
                        treeNode = treeNode.right;
                        remove_findNode(value);
                    }
                }
            };
            this.addKeyListener(keyListener);
        } else if (treeNode.getValue() == value) {
            treeNode.node.setColor(SWT.COLOR_DARK_MAGENTA, SWT.COLOR_WHITE);
            KeyListener keyListener=new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent keyEvent) {
                    if (keyEvent.keyCode==13){
                        removeKeyListener(this);
                        if (treeNode == tree.root) {
                            if ((treeNode.right == null) && (treeNode.left == null)) {
                                treeNode.node.dispose();
                                treeNode = null;
                                tree.root=null;
                                reDrawComposite();
                            } else if ((treeNode.left != null) && (treeNode.right == null)) {
                                treeNode.node.dispose();
                                tree.root = treeNode.left;
                                reDrawComposite();
                            } else if ((treeNode.right != null) &&(treeNode.left == null)){
                                treeNode.node.dispose();
                                tree.root = treeNode.right;
                                reDrawComposite();
                            }else{
                                rightMinNode = treeNode.right;
                                remove_findMinNode();
                            }

                        } else if ((treeNode.right == null) && (treeNode.left == null)) {

                            if (treeNode.parent.left == treeNode) treeNode.parent.left = null;
                            else if (treeNode.parent.right == treeNode) treeNode.parent.right = null;

                            treeNode.node.dispose();
                            treeNode = null;

                            reDrawComposite();

                        } else if (treeNode.right == null) {
                            treeNode.left.parent = treeNode.parent;
                            if (treeNode.parent.left == treeNode) {
                                treeNode.parent.left = treeNode.left;
                            } else treeNode.parent.right = treeNode.left;
                            treeNode.node.dispose();
                            reDrawComposite();

                        } else if (treeNode.left == null) {
                            treeNode.right.parent = treeNode.parent;
                            if (treeNode.parent.right == treeNode) {
                                treeNode.parent.right = treeNode.right;
                            } else {
                                treeNode.parent.left = treeNode.right;
                            }
                            treeNode.node.dispose();
                            reDrawComposite();

                        } else {
                            rightMinNode = treeNode.right;
                            remove_findMinNode();
                        }
                    }
                }
            };
            this.addKeyListener(keyListener);
        }
    }

    public void remove_findMinNode() {
        rightMinNode.node.setColor(SWT.COLOR_MAGENTA, SWT.COLOR_WHITE);
        KeyListener keyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.keyCode == 13) {
                    removeKeyListener(this);
                    rightMinNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                    if (rightMinNode.left != null) {
                        rightMinNode = rightMinNode.left;
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

    public void remove_changeValue() {
        treeNode.setValue(rightMinNode.getValue());
        treeNode.node.setLabel(new Integer(rightMinNode.getValue()).toString());
        treeNode.node.drawNode();
        rightMinNode.node.dispose();

        if (rightMinNode == treeNode.right) {

            treeNode.right = rightMinNode.right;
            if (rightMinNode.right != null) {
                rightMinNode.right.parent = treeNode;
            }
        } else {
            if (rightMinNode.right != null) {
                rightMinNode.parent.left = rightMinNode.right;
                rightMinNode.right.parent = rightMinNode.parent;
                rightMinNode = rightMinNode.right;
            } else {
                rightMinNode.parent.lineConnectLeft = null;
                rightMinNode.parent.left = null;
                rightMinNode = null;
            }
        }

        reDrawComposite();
    }

    public void resetLine(TreeNode treeNode) {
        if (treeNode != null) {
            if (treeNode.left != null) {
                //composite.removePaintListener(treeNode.lineConnectLeft.paintListener); // xoa cai line cu
                treeNode.lineConnectLeft = new Line(treeNode.node, treeNode.left.node); // tao cai line moi

                paintListener[countListener] = treeNode.lineConnectLeft.paintListener;
                countListener++;

                composite.addPaintListener(treeNode.lineConnectLeft.paintListener); // add cai moi

                treeNode.left.lineConnectParent = treeNode.lineConnectLeft;

                resetLine(treeNode.left);
            }
            if (treeNode.right != null) {
                //composite.removePaintListener(treeNode.lineConnectRight.paintListener); // xoa cai line cu
                treeNode.lineConnectRight = new Line(treeNode.node, treeNode.right.node); // tao cai line moi

                paintListener[countListener] = treeNode.lineConnectRight.paintListener;
                countListener++;

                composite.addPaintListener(treeNode.lineConnectRight.paintListener);

                treeNode.right.lineConnectParent = treeNode.lineConnectParent;

                resetLine(treeNode.right);
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
        if (treeNode!=null){
            n = 0;
            if (typeBrowse == 1) {
                createQueneNodeFirst(treeNode);
            } else if (typeBrowse == 2) {
                createQueneNodeMiddle(treeNode);
            } else if (typeBrowse == 3) {
                createQueneNodeLast(treeNode);
            }
            browseNode(0);
        } else {
            new PopupDialog(getShell()).open("Alert", "There is no node to browse");
        }
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
    public void removeAllPaintListener() {
        for (int i = 0; i < countListener; i++) {
            composite.removePaintListener(paintListener[i]);
        }
        countListener = 0;
    }

    public void addNode(int value) {
        final TreeNode newNode = tree.addNode(value, tree.root);
        if (newNode!=null){
            resetBounds(tree.root);
            newNode.node.drawNode();
            setNodeMouseListener(newNode);
            if (newNode.parent != null) {
                newNode.lineConnectParent = new Line(newNode.parent.node, newNode.node);
                if (newNode.parent.left == newNode) {
                    newNode.parent.lineConnectLeft = newNode.lineConnectParent;

                    paintListener[countListener] = newNode.parent.lineConnectLeft.paintListener; //Luu listener vao mang
                    countListener = countListener + 1;

                    composite.addPaintListener(newNode.parent.lineConnectLeft.paintListener);
                } else if (newNode.parent.right == newNode) {
                    newNode.parent.lineConnectRight = newNode.lineConnectParent;

                    paintListener[countListener] = newNode.parent.lineConnectRight.paintListener;
                    countListener = countListener + 1;

                    composite.addPaintListener(newNode.parent.lineConnectRight.paintListener);
                }
            }
            composite.redraw();
            this.setMinSize((tree.getWidth() + 2) * 100, ((tree.getHeight(tree.root)) + 1) * 70);
        }
    }

    /* add a node include steps */
    public void addNodeStep(final TreeNode treeNode, final int value) {
        if (treeNode == null) {
            addNode(value);
        } else {

            treeNode.node.setColor(SWT.COLOR_DARK_MAGENTA, SWT.COLOR_WHITE);
            if (treeNode.getValue()==value){
                String s = "This node has the value : " + treeNode.getValue() + " equal to " + value + " \n";
                statusBar.setText(s);
                KeyListener keyListener = new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.keyCode == 13) {
                            treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                            new PopupDialog(getShell()).open("Alert","This node has been created");
                        }
                    }
                };
                this.addKeyListener(keyListener);

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
                            addNodeStep(treeNode.left, value);
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
            if (!treeNode.node.isDisposed()) {
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
    public TreeNode findNodeStep(final TreeNode treeNode, final int value) {
        if (treeNode == null) {
            statusBar.setText("There is no node with the value of "+new Integer(value).toString());
            new PopupDialog(this.getShell()).open("Find a node","There is node node with the value "+new Integer(value).toString());
        } else {
            treeNode.node.setColor(SWT.COLOR_DARK_MAGENTA, SWT.COLOR_WHITE);
            if (treeNode.getValue() == value) {
                statusBar.setText("Find a node that have the value +" + value);
                statusBar.setText("Press enter to continume");
                KeyListener keyListener = new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        System.out.println (e.keyCode);
                        if (e.keyCode == 13) {
                            treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                            removeKeyListener(this);
                        } else if (e.keyCode == 8) {
                            removeKeyListener(this);
                            treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                            findNodeStep(treeNode.parent, value);
                        }
                    }
                };
                this.addKeyListener(keyListener);
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
                            findNodeStep(treeNode.left, value);
                        } else if (e.keyCode == 8) {
                            removeKeyListener(this);
                            treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                            findNodeStep(treeNode.parent, value);
                        }
                    }
                };
                this.addKeyListener(keyListener);
            } else if (treeNode.getValue() < value) {
                String s = "This node has the value : " + treeNode.getValue() + " is lower than " + value;
                s = s + ", we must go to the right";
                s = s + ", please press Enter to go to next step";
                statusBar.setText(s);
                KeyListener keyListener = new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.keyCode == 13) {
                            treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                            removeKeyListener(this);
                            findNodeStep(treeNode.right, value);
                        } else if (e.keyCode == 8) {
                            removeKeyListener(this);
                            treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                            findNodeStep(treeNode.parent, value);
                        }
                    }
                };
                this.addKeyListener(keyListener);
            }
        }
        return null;
    }

    /////////////////////////////////////////////////////////////////////////////////
    /*
    Tim successsor
     */
    public void findSuccescorStep(final TreeNode treeNode) {
        treeNode.node.setColor(SWT.COLOR_DARK_MAGENTA, SWT.COLOR_WHITE);
        KeyListener keyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.keyCode == 13) {
                    removeKeyListener(this);
                    if (treeNode.left != null) {
                        // Neu co con trai
                        treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                        findSuccescorStep(treeNode.left);
                    } else {
                        // Da tim thay
                        treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                        statusBar.setText("Da tim thay");
                    }
                }
            }
        };
        this.addKeyListener(keyListener);
    }

    public void findNode(TreeNode treeNode, int value) {
        if (treeNode != null) {
            if (treeNode.getValue() == value) {
                this.treeNode = treeNode;
            } else if (treeNode.getValue() > value) {
                findNode(treeNode.left, value);
            } else if (treeNode.getValue() < value) {
                findNode(treeNode.right, value);
            }
        }
    }
    public void findSuccescorBack(final TreeNode treeNode, final int value){
        treeNode.node.setColor(SWT.COLOR_DARK_MAGENTA, SWT.COLOR_WHITE);
        if (treeNode.left==null){
            if (treeNode==tree.root){
                new PopupDialog(getShell()).open("Finding Successor", "This node is its sucesscor");
            } else {
               KeyListener keyListener=new KeyAdapter() {
                   @Override
                   public void keyPressed(KeyEvent keyEvent) {
                       if (keyEvent.keyCode==13){
                           removeKeyListener(this);
                           treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                           findSuccescorBack(treeNode.parent, value);
                       }
                   }
               };
               this.addKeyListener(keyListener);
            }
        } else if (treeNode.left!=null) {
            KeyListener keyListener=new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent keyEvent) {
                    if (keyEvent.keyCode==13){
                        removeKeyListener(this);
                        treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                        new PopupDialog(getShell()).open("Finding Sucessor","The node with the value "+new Integer(treeNode.getValue()).toString()
                                +" is the successor of the node with the value "+new Integer(value).toString());
                    }
                }
            };
            this.addKeyListener(keyListener);
        }
    }
    public void findSucesscor(final int value) {
        treeNode = null;
        findNode(tree.root, value);
        if (treeNode != null) {
            if (treeNode.right == null) {
                treeNode.node.setColor(SWT.COLOR_DARK_MAGENTA, SWT.COLOR_WHITE);
                KeyListener keyListener=new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent keyEvent) {
                        if (keyEvent.keyCode==13){
                            System.out.println ("Hello World");
                            removeKeyListener(this);
                            treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                            if (treeNode==tree.root){
                                new PopupDialog(getShell()).open("Finding Succesor", "This node is its succesccor");
                            } else
                            findSuccescorBack(treeNode.parent, value);
                        }
                    }
                };
                this.addKeyListener(keyListener);
            } else {
                treeNode.node.setColor(SWT.COLOR_DARK_MAGENTA, SWT.COLOR_WHITE);
                KeyListener keyListener = new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent keyEvent) {
                        if (keyEvent.keyCode == 13) {
                            treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                            removeKeyListener(this);
                            findSuccescorStep(treeNode.right);
                        }
                    }
                };
                this.addKeyListener(keyListener);
            }
        } else {
            new PopupDialog(getShell()).open("Alert", "The node "+new Integer(value).toString()+" hasn't been create");
        }
    }

    ///////////////////////////////////////
    public void findPredesscorStep(final TreeNode treeNode, final int value) {
        treeNode.node.setColor(SWT.COLOR_DARK_MAGENTA, SWT.COLOR_WHITE);
        KeyListener keyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.keyCode == 13) {
                    removeKeyListener(this);
                    if (treeNode.right != null) {
                        treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                        findPredesscorStep(treeNode.right, value);
                    } else {
                        // Da tim thay
                        treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                        statusBar.setText("Node "+new Integer(treeNode.getValue()).toString()+" is the predesccesor of"+new Integer(value).toString());
                    }
                }
            }
        };
        this.addKeyListener(keyListener);
    }
    public void findPredesscorBack(final TreeNode treeNode, final int value){
        treeNode.node.setColor(SWT.COLOR_DARK_MAGENTA, SWT.COLOR_WHITE);
        if (treeNode.right==null){
            if (treeNode==tree.root){
                new PopupDialog(getShell()).open("Finding Successor", "This node is its predecesscor");
            } else {
                KeyListener keyListener=new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent keyEvent) {
                        if (keyEvent.keyCode==13){
                            removeKeyListener(this);
                            treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                            findSuccescorBack(treeNode.parent, value);
                        }
                    }
                };
                this.addKeyListener(keyListener);
            }
        } else if (treeNode.right!=null) {
            KeyListener keyListener=new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent keyEvent) {
                    if (keyEvent.keyCode==13){
                        removeKeyListener(this);
                        treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                        new PopupDialog(getShell()).open("Finding Predcessor","The node with the value "+new Integer(treeNode.getValue()).toString()
                                +" is the Preccessor of the node with the value "+new Integer(value).toString());
                    }
                }
            };
            this.addKeyListener(keyListener);
        }
    }
    public void findPredesscor(final int value) {
        treeNode = null;
        findNode(tree.root, value);
        if (treeNode != null) {
            if (treeNode.left == null) {
                treeNode.node.setColor(SWT.COLOR_DARK_MAGENTA, SWT.COLOR_WHITE);
                KeyListener keyListener=new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent keyEvent) {
                        if (keyEvent.keyCode==13){
                            treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                            removeKeyListener(this);
                            if (treeNode==tree.root){
                                new PopupDialog(getShell()).open("Findding Predesscor", "This node is its Predesscor");
                            } else{
                                findPredesscorBack(treeNode.parent, value);
                            }
                        }
                    }
                };
            this.addKeyListener(keyListener);
            } else {
                treeNode.node.setColor(SWT.COLOR_DARK_MAGENTA, SWT.COLOR_WHITE);
                KeyListener keyListener = new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent keyEvent) {
                        if (keyEvent.keyCode == 13) {
                            removeKeyListener(this);
                            treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                            findPredesscorStep(treeNode.left, value);
                        }
                    }
                };
                this.addKeyListener(keyListener);
            }
        } else {
            new PopupDialog(getShell()).open("Finding Predesscor", "There is no node with that value");
        }
    }

    //////////////////////////////////////////////////////////////////////////////////
    public void printElement() {
        n = 0;
        createQueneNodeMiddle(tree.root);
        PopupDialog popup = new PopupDialog(this.getShell());
        String label = "";
        for (int i = 0; i < n; i++) {
            label = label + new Integer(i + 1).toString() + ". " + new Integer(nodeQuene[i].getValue()).toString() + "             ";
        }
        popup.open("Print the list of node ordered", "Order: " + label);
    }

    //////////////////////////////////////////////////////////////////////////////////
    public void HeapSort() {

    }
}

