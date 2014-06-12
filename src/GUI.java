import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
/* The main of this program
 *
 */
public class GUI {
    Display display;
    Shell shell;
    Node node;
    DrawingComposite drawPanel;
    Menu menuBar;


    /* Constructor
     *
     */
    public GUI(){
        display=new Display();
        shell=new Shell(display);
        shell.setLayout(new GridLayout());
        shell.setText("Java Binary Tree Program");
        drawPanel=new DrawingComposite(this.shell, SWT.BORDER| SWT.V_SCROLL| SWT.H_SCROLL);
        drawPanel.setLayoutData(new GridData(GridData.FILL_VERTICAL | GridData.FILL_HORIZONTAL));

    }

    /* Open program
     *
     */
    public void open(){
        this.shell.open();
        this.shell.layout();

        while (!this.shell.isDisposed()){
            if (!this.display.readAndDispatch()){
                this.display.sleep();
            }
        }
    }

    /* Create its menuBar
     *
     */
    public void createMenuBar(){
        menuBar=new Menu(shell, SWT.BAR);

        MenuItem editMenuHeader=new MenuItem(menuBar, SWT.CASCADE);
        MenuItem actionMenuHeader=new MenuItem(menuBar, SWT.CASCADE);
        editMenuHeader.setText("Edit");
        actionMenuHeader.setText("Action");

        Menu editMenu=new Menu(shell, SWT.DROP_DOWN);
        Menu actionMenu=new Menu(shell, SWT.DROP_DOWN);

        editMenuHeader.setMenu(editMenu);
        actionMenuHeader.setMenu(actionMenu);

        MenuItem addNewNode=new MenuItem(editMenu, SWT.PUSH);
        addNewNode.setText("Create a new node");
        addNewNode.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                InputDialog numberInputDialog=new InputDialog(shell);
                Integer value=numberInputDialog.open("Create a new node", "Input new node: ");
                if (value!=null){
                    drawPanel.addNode(value);
                }

            }
        });
        //////////////////////////////////////////////////

        MenuItem browseNodeFirst=new MenuItem(actionMenu, SWT.PUSH);
        browseNodeFirst.setText("Browse (first type)");
        browseNodeFirst.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                drawPanel.browseNode(drawPanel.tree.root, 1);
                drawPanel.statusBar.setText("");
            }
        });
        //---------------------------------//
        MenuItem browseNodeMiddle=new MenuItem(actionMenu, SWT.PUSH);
        browseNodeMiddle.setText("Browse (middle type)");
        browseNodeMiddle.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                drawPanel.browseNode(drawPanel.tree.root, 2);
                drawPanel.statusBar.setText("");
            }
        });
        //---------------------------------//
        MenuItem browseNodeLast=new MenuItem(actionMenu, SWT.PUSH);
        browseNodeLast.setText("Browse (last type)");
        browseNodeLast.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                drawPanel.browseNode(drawPanel.tree.root, 3);
                drawPanel.statusBar.setText("");
            }
        });
        ///////////////////////////////////////////////////////////
        MenuItem addNodeStep=new MenuItem(editMenu, SWT.PUSH);
        addNodeStep.setText("Create a new node (Step)");
        addNodeStep.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        InputDialog inputDialog = new InputDialog(shell);
                        Integer value = inputDialog.open("Create a new node (Step)", "Input new node: ");
                        if (value != null) {
                            drawPanel.addNodeStep(drawPanel.tree.root, value);
                            drawPanel.statusBar.setText("");
                        }
                    }
                });
        MenuItem findNode=new MenuItem(actionMenu, SWT.PUSH);
        findNode.setText("Find");
        findNode.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                InputDialog inputDialog=new InputDialog(shell);
                Integer value=inputDialog.open("Find", "Input value: ");
                if (value!=null){
                    drawPanel.findNodeStep(drawPanel.tree.root, value);
                    drawPanel.statusBar.setText("");
                }
            }
        });
        ///////////////////////////
        MenuItem removeNode=new MenuItem(editMenu, SWT.PUSH);
        removeNode.setText("Remove");
        removeNode.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                InputDialog inputDialog=new InputDialog(shell);
                Integer value=inputDialog.open("Remove", "Input value: ");
                if (value!=null){
                    drawPanel.treeNode =drawPanel.tree.root;
                    drawPanel.remove_findNode(value);
                    drawPanel.statusBar.setText("");
                }
            }
        });
        shell.setMenuBar(menuBar);
        /////////////////////////////
        MenuItem findSuccessor=new MenuItem(actionMenu, SWT.PUSH);
        findSuccessor.setText("Get successor");
        findSuccessor.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                InputDialog inputDialog=new InputDialog(shell);
                Integer value=inputDialog.open("Get successor of a node", "Input value: ");
                if (value!=null){
                    drawPanel.findSucesscor(value);
                    drawPanel.statusBar.setText("");
                }
            }
        });
        ///////////////////////////////
        MenuItem findPredecessor=new MenuItem(actionMenu, SWT.PUSH);
        findPredecessor.setText("Get predecessor");
        findPredecessor.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                InputDialog inputDialog=new InputDialog(shell);
                Integer value=inputDialog.open("Get predecessor", "Input value: ");
                if (value!=null){
                    drawPanel.findPredecessor(value);
                    drawPanel.statusBar.setText("");
                }
            }
        });
        MenuItem findMax=new MenuItem(actionMenu, SWT.PUSH);
        findMax.setText("Find Max Node");
        findMax.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                drawPanel.findMaxNode(drawPanel.tree.root);
            }
        });
        MenuItem findMin=new MenuItem(actionMenu, SWT.PUSH);
        findMin.setText("Find Min Node");
        findMin.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                drawPanel.findMinNode(drawPanel.tree.root);
            }
        });
        final MenuItem heapSort=new MenuItem(actionMenu, SWT.PUSH);
        heapSort.setText("Heap Sort Algorithm");
        heapSort.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                int value[]=new InputArray(shell).open("Heap Sort Algorithm", "Input an integer array: ");
                if (value!=null){
//                    Display display = new Display();
                    Shell shell2 = new Shell(display);
                    shell2 = new Shell(display);
                    shell2.setLayout(new GridLayout());
                    shell2.setText("Java Binary Tree Program");
                    HeapComposite heapComposite = new HeapComposite(shell2, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
                    heapComposite.setLayoutData(new GridData(GridData.FILL_VERTICAL | GridData.FILL_HORIZONTAL));
                    shell2.open();
                    shell2.layout();
                    heapComposite.setTree(value);
                    heapComposite.resetBounds();
                    heapComposite.drawCompostite();
                    heapComposite.str="";
                    heapComposite.numberOfNode=heapComposite.tree.n;
                    heapComposite.buildMaxHeap();
                    while (!shell2.isDisposed()) {
                        if (!display.readAndDispatch()) {
                            display.sleep();
                        }
                    }
                }
                drawPanel.statusBar.setText("");
            }
        });
        /////////////////////////////
        MenuItem printItem=new MenuItem(actionMenu, SWT.PUSH);
        printItem.setText("Get sorted list");
        printItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                drawPanel.printElement();
                drawPanel.statusBar.setText("");
            }
        });
    }
    /*
     * Main
     */
    public static void main(String args[]){
        GUI gui=new GUI();
        gui.createMenuBar();
        gui.drawPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

            }
        });
        gui.open();
    }
}
