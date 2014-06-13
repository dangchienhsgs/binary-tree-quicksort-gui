
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



    /* Constructor
     *
     */


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

    /* add mouse listener
     * Drag and Drop for mouse
     *
     */
    private TreeNode clickedNode;

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


    /* These codes process removing a node of tree
     *  reDrawComposite: Redraw all the things of composite
     *  remove_findNode: Find the node which must be remove
     *  remove_findMinNode: Find its successor
     *  remove_makeChange: Completely remove node
     *  remove_resetLine: reset all Line connect node
     */
    TreeNode treeNode;
    private TreeNode rightMinNode;

    PaintListener paintListener[] = new PaintListener[1000];
    int countListener = 0;

    public void reDrawComposite() {
        removeAllPaintListener();
        tree.computeAllNodeTree();
        resetBounds(tree.root);
        resetLine(tree.root);
        composite.redraw();
    }

    public void remove_findNode(final int value) {
        if (treeNode == null) {
            new PopupDialog(getShell()).open("Remove", "There is no " + new Integer(value).toString() + "-node existing !");
        } else if (treeNode.getValue() > value) {
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
            KeyListener keyListener = new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent keyEvent) {
                    if (keyEvent.keyCode == 13) {
                        removeKeyListener(this);
                        if (treeNode == tree.root) {
                            if ((treeNode.right == null) && (treeNode.left == null)) {
                                treeNode.node.dispose();
                                treeNode = null;
                                tree.root = null;
                                reDrawComposite();
                            } else if ((treeNode.left != null) && (treeNode.right == null)) {
                                treeNode.node.dispose();
                                tree.root = treeNode.left;
                                reDrawComposite();
                            } else if ((treeNode.right != null) && (treeNode.left == null)) {
                                treeNode.node.dispose();
                                tree.root = treeNode.right;
                                reDrawComposite();
                            } else {
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
        rightMinNode.node.setColor(SWT.COLOR_RED, SWT.COLOR_WHITE);
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
                        remove_makeChange();
                    }
                }
            }
        };
        this.addKeyListener(keyListener);
    }

    public void remove_makeChange() {
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


    /* Browse all node with the order FIRST, LAST, MIDDLE
     * createQuene: create node quene to browse
     * browseNodeStep: browse Node each step
     * browseNode: browswNode, call to function browseNodeStep
     */

    TreeNode nodeQuene[] = new TreeNode[1000];
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

    void browseNodeStep(final int i) {
        if ((i < n)) {
            nodeQuene[i].node.setColor(SWT.COLOR_DARK_MAGENTA, SWT.COLOR_WHITE);
            KeyListener keyListener = new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent keyEvent) {
                    removeKeyListener(this);
                    nodeQuene[i].node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                    browseNodeStep(i + 1);
                }
            };
            this.addKeyListener(keyListener);
        }
    }

    public void browseNode(TreeNode treeNode, int typeBrowse) {
        if (treeNode != null) {
            n = 0;
            switch (typeBrowse) {
                case 1:
                    createQueneNodeFirst(treeNode);
                    break;
                case 2:
                    createQueneNodeMiddle(treeNode);
                    break;
                case 3:
                    createQueneNodeLast(treeNode);
                    break;
            }
            browseNodeStep(0);
        } else {
            new PopupDialog(getShell()).open("Alert", "There is no node to browse");
        }
    }

    /* Remove all paint Listener of composite
     *
     */
    public void removeAllPaintListener() {
        for (int i = 0; i < countListener; i++) {
            composite.removePaintListener(paintListener[i]);
        }
        countListener = 0;
    }


    /* add a node to tree
     * addNodeStep, addNode similar but different in step
     */
    public void addNode(int value) {
        final TreeNode newNode = tree.addNode(value, tree.root);
        if (newNode != null) {
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
            statusBar.setText("");
        } else {

            treeNode.node.setColor(SWT.COLOR_DARK_MAGENTA, SWT.COLOR_WHITE);
            if (treeNode.getValue() == value) {
                String s = "This node has the value : " + treeNode.getValue() + " is equal to " + value + " \n";
                statusBar.setText(s);
                KeyListener keyListener = new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.keyCode == 13) {
                            removeKeyListener(this);
                            treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                            new PopupDialog(getShell()).open("Create a new node", "This node has been existed");
                        }
                    }
                };
                this.addKeyListener(keyListener);

            }
            if (treeNode.getValue() > value) {

                String s = "This node has the value : " + treeNode.getValue() + " is higher than " + value + " \n";
                s = s + ", we must go to the left side";
                s = s + " (press Enter to continue)";
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
                s = s + ", we must go to the right side";
                s = s + ", (press Enter to continude)";
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

    /* Find Node by value
     * Nothing difficult;
     */
    public TreeNode findNodeStep(final TreeNode treeNode, final int value) {
        if (treeNode == null) {
            statusBar.setText("There is no node with the value of " + new Integer(value).toString());
            new PopupDialog(this.getShell()).open("Find a node", "There is no node with the value " + new Integer(value).toString());
            statusBar.setText("");
        } else {
            treeNode.node.setColor(SWT.COLOR_DARK_MAGENTA, SWT.COLOR_WHITE);
            if (treeNode.getValue() == value) {
                KeyListener keyListener = new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.keyCode == 13) {
                            statusBar.setText("");
                            new PopupDialog(getShell()).open("Find a Node", "This node was created in our tree");
                            treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                            removeKeyListener(this);
                        } else if (e.keyCode == 8) {
                            statusBar.setText("");
                            removeKeyListener(this);
                            treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                            findNodeStep(treeNode.parent, value);
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
                        statusBar.setText("It is here");
                        if (e.keyCode == 13) {
                            statusBar.setText("");
                            treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                            removeKeyListener(this);
                            findNodeStep(treeNode.right, value);
                        } else if (e.keyCode == 8) {
                            statusBar.setText("");
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


    /* Find the Successor
     * findNode: find node by value immediately
     * findSuccessor: call to SuccessorStep or SuccessorBack
     * findSuccessorBack: if that node doesn't have its right child
     * findSuccessorStep: if that node have its right child
     */
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

    public void findSuccessorStep(final TreeNode treeNode, final int value) {
        treeNode.node.setColor(SWT.COLOR_DARK_MAGENTA, SWT.COLOR_WHITE);
        KeyListener keyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.keyCode == 13) {
                    removeKeyListener(this);
                    if (treeNode.left != null) {
                        // Neu co con trai
                        treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                        findSuccessorStep(treeNode.left, value);
                    } else {
                        // Da tim thay
                        treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                        new PopupDialog(getShell()).open("Get Successor", "The " + new Integer(treeNode.getValue()).toString()
                                + "-node is the successor of " + new Integer(value).toString() + "-node");
                    }
                }
            }
        };
        this.addKeyListener(keyListener);
    }

    public void findSuccessorBack(final TreeNode treeNode, final int value) {
        treeNode.node.setColor(SWT.COLOR_DARK_MAGENTA, SWT.COLOR_WHITE);
        if (treeNode.left == null) {
            if (treeNode == tree.root) {
                new PopupDialog(getShell()).open("Finding Successor", new Integer(treeNode.getValue()).toString()+"-node is the successor of "+
                new Integer(value).toString()+"-node");
            } else {
                statusBar.setText(new Integer(treeNode.getValue()).toString()+"-node don't have any left child, let browse its parent");
                KeyListener keyListener = new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent keyEvent) {
                        if (keyEvent.keyCode == 13) {
                            statusBar.setText("");
                            removeKeyListener(this);
                            treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                            findSuccessorBack(treeNode.parent, value);
                        }
                    }
                };
                this.addKeyListener(keyListener);
            }
        } else if (treeNode.left != null) {
            statusBar.setText(new Integer(treeNode.getValue()).toString()+"-node have a left child");
            KeyListener keyListener = new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent keyEvent) {
                    if (keyEvent.keyCode == 13) {
                        removeKeyListener(this);
                        treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                        new PopupDialog(getShell()).open("Finding Sucessor", "The " + new Integer(treeNode.getValue()).toString()
                                + "-node is the successor of the " + new Integer(value).toString()+"-node");
                    }
                }
            };
            this.addKeyListener(keyListener);
        }
    }

    public void findSuccessor(final int value) {
        treeNode = null;
        findNode(tree.root, value);
        if (treeNode != null) {
            if (treeNode.right == null) {
                System.out.println ("Hello World Nguyen Daang Chien");
                statusBar.setText(new Integer(value).toString() + "-node don't have any right child, we must browse to its parent (press Enter to continue)");
                treeNode.node.setColor(SWT.COLOR_DARK_MAGENTA, SWT.COLOR_WHITE);
                KeyListener keyListener = new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent keyEvent) {
                        if (keyEvent.keyCode == 13) {
                            removeKeyListener(this);
                            treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                            if (treeNode == tree.root) {
                                statusBar.setText("This node is the tree's root, it don't have any parent");
                                new PopupDialog(getShell()).open("Finding Successor", "This node is its successor");
                            } else
                                findSuccessorBack(treeNode.parent, value);
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
                            findSuccessorStep(treeNode.right, value);
                        }
                    }
                };
                this.addKeyListener(keyListener);
            }
        } else {
            new PopupDialog(getShell()).open("Alert", "The node " + new Integer(value).toString() + " hasn't been create");
        }
    }


    /* Find the Predecessor
     * Similar to Successor
     */
    public void findPredecessorStep(final TreeNode treeNode, final int value) {
        treeNode.node.setColor(SWT.COLOR_DARK_MAGENTA, SWT.COLOR_WHITE);
        KeyListener keyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.keyCode == 13) {
                    removeKeyListener(this);
                    if (treeNode.right != null) {
                        treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                        findPredecessorStep(treeNode.right, value);
                    } else {
                        // Da tim thay
                        treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                        new PopupDialog(getShell()).open("Find Predecessor", new Integer(treeNode.getValue()).toString() + "-node is the predecessor of " + new Integer(value).toString() + "-node");
                    }
                }
            }
        };
        this.addKeyListener(keyListener);
    }

    public void findPredecessorBack(final TreeNode treeNode, final int value) {
        treeNode.node.setColor(SWT.COLOR_DARK_MAGENTA, SWT.COLOR_WHITE);
        if (treeNode.right == null) {
            if (treeNode == tree.root) {
                statusBar.setText(new Integer(treeNode.getValue()).toString() + "-node don't have right child");
                new PopupDialog(getShell()).open("Finding Successor", new Integer(treeNode.getValue()).toString() +
                        "-node is the " + new Integer(value).toString() + "-node 's predecessor");
                statusBar.setText("");
            } else {
                KeyListener keyListener = new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent keyEvent) {
                        if (keyEvent.keyCode == 13) {
                            removeKeyListener(this);
                            treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                            findSuccessorBack(treeNode.parent, value);
                        }
                    }
                };
                this.addKeyListener(keyListener);
            }
        } else if (treeNode.right != null) {
            KeyListener keyListener = new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent keyEvent) {
                    if (keyEvent.keyCode == 13) {
                        removeKeyListener(this);
                        treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                        new PopupDialog(getShell()).open("Finding Predcessor", new Integer(treeNode.getValue()).toString()
                                + "-node is the predecessor of the " + new Integer(value).toString() + "-node");
                    }
                }
            };
            this.addKeyListener(keyListener);
        }
    }

    public void findPredecessor(final int value) {
        treeNode = null;
        findNode(tree.root, value);
        if (treeNode != null) {
            if (treeNode.left == null) {
                treeNode.node.setColor(SWT.COLOR_DARK_MAGENTA, SWT.COLOR_WHITE);
                statusBar.setText("This node doesn't have any left child, we must go to its parent");
                KeyListener keyListener = new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent keyEvent) {
                        if (keyEvent.keyCode == 13) {
                            treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                            removeKeyListener(this);
                            if (treeNode == tree.root) {
                                statusBar.setText("This node is the tree's root, it doesn't have any parent");
                                new PopupDialog(getShell()).open("Findding Predesscor", "This node is its Predesscor");
                                statusBar.setText("");
                            } else {
                                findPredecessorBack(treeNode.parent, value);
                            }
                        }
                    }
                };
                this.addKeyListener(keyListener);
            } else {
                treeNode.node.setColor(SWT.COLOR_DARK_MAGENTA, SWT.COLOR_WHITE);
                statusBar.setText("This node have a left node, we can go to its left node (press ENTER to continue)");
                KeyListener keyListener = new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent keyEvent) {
                        if (keyEvent.keyCode == 13) {
                            removeKeyListener(this);
                            statusBar.setText("");
                            treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                            findPredecessorStep(treeNode.left, value);
                        }
                    }
                };
                this.addKeyListener(keyListener);
            }
        } else {
            new PopupDialog(getShell()).open("Finding Predesscor", "There is no node with that value");
        }
    }

    /* Find Max
     *
     */
    public void findMaxNode(final TreeNode treeNode) {
        if (treeNode == null) {
            new PopupDialog(getShell()).open("Alert", "This tree is empty");
        } else if (treeNode.right != null) {
            statusBar.setText("This node has a right child, let go to next step (press Enter)");
            treeNode.node.setColor(SWT.COLOR_DARK_MAGENTA, SWT.COLOR_WHITE);
            KeyListener keyListener = new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent keyEvent) {
                    if (keyEvent.keyCode == 13) {
                        removeKeyListener(this);
                        treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                        findMaxNode(treeNode.right);
                    }
                }
            };
            addKeyListener(keyListener);
        } else {
            // This node is maxx
            treeNode.node.setColor(SWT.COLOR_DARK_MAGENTA, SWT.COLOR_WHITE);
            KeyListener keyListener = new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent keyEvent) {
                    if (keyEvent.keyCode == 13) {
                        removeKeyListener(this);
                        statusBar.setText("This node don't have any right child");
                        new PopupDialog(getShell()).open("Find Max Node", "The " + new Integer(treeNode.getValue()).toString() +
                                "-node is the maximum node of tree");
                        statusBar.setText("");
                        treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                    }
                }
            };
            addKeyListener(keyListener);
        }
    }

    /*
    Find Min node
     */
    public void findMinNode(final TreeNode treeNode) {
        if (treeNode == null) {
            new PopupDialog(getShell()).open("Alert", "This tree is empty");
        } else if (treeNode.left != null) {
            treeNode.node.setColor(SWT.COLOR_DARK_MAGENTA, SWT.COLOR_WHITE);
            KeyListener keyListener = new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent keyEvent) {
                    if (keyEvent.keyCode == 13) {
                        removeKeyListener(this);
                        treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                        findMaxNode(treeNode.left);
                    }
                }
            };
            addKeyListener(keyListener);
        } else {
            // This node is maxx
            treeNode.node.setColor(SWT.COLOR_DARK_MAGENTA, SWT.COLOR_WHITE);
            KeyListener keyListener = new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent keyEvent) {
                    if (keyEvent.keyCode == 13) {
                        removeKeyListener(this);
                        new PopupDialog(getShell()).open("Find Min Node", "The " + new Integer(treeNode.getValue()).toString() +
                                "-node is the maximum node of tree");
                        treeNode.node.setColor(SWT.COLOR_WHITE, SWT.COLOR_BLACK);
                    }
                }
            };
            addKeyListener(keyListener);
        }
    }

    /* Print all its elements
     *
     */
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

}

