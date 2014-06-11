import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class GUI {
    Display display;
    Shell shell;
    Node node;
    DrawingComposite drawPanel;
    Menu menuBar;
    public GUI(){
        display=new Display();
        shell=new Shell(display);
        shell.setLayout(new GridLayout());
        shell.setText("Java Binary Tree Program");
        drawPanel=new DrawingComposite(this.shell, SWT.BORDER| SWT.V_SCROLL| SWT.H_SCROLL);
        drawPanel.setLayoutData(new GridData(GridData.FILL_VERTICAL | GridData.FILL_HORIZONTAL));
    }
    public void open(){
        this.shell.open();
        this.shell.layout();

        while (!this.shell.isDisposed()){
            if (!this.display.readAndDispatch()){
                this.display.sleep();
            }
        }
    }
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
        addNewNode.setText("Add new node");
        addNewNode.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                InputDialog numberInputDialog=new InputDialog(shell);
                Integer value=numberInputDialog.open("Add a node by value", "Please give the value of node: ");
                if (value!=null){
                    drawPanel.addNode(value);
                }

            }
        });
        ///////////////////////////////

        MenuItem browseNodeFirst=new MenuItem(actionMenu, SWT.PUSH);
        browseNodeFirst.setText("Browse All Node First Type");
        browseNodeFirst.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                drawPanel.browseAllNode(drawPanel.tree.root, 1);
            }
        });
        //---------------------------------//
        MenuItem browseNodeMiddle=new MenuItem(actionMenu, SWT.PUSH);
        browseNodeMiddle.setText("Browse All Node Middle Type");
        browseNodeMiddle.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                drawPanel.browseAllNode(drawPanel.tree.root, 2);
            }
        });
        //---------------------------------//
        MenuItem browseNodeLast=new MenuItem(actionMenu, SWT.PUSH);
        browseNodeLast.setText("Browse All Node Last Type");
        browseNodeLast.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                drawPanel.browseAllNode(drawPanel.tree.root, 3);
            }
        });
        ///////////////////////////////////////////////////////////
        MenuItem addNodeStep=new MenuItem(editMenu, SWT.PUSH);
        addNodeStep.setText("Add node step");
        addNodeStep.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        InputDialog inputDialog = new InputDialog(shell);
                        Integer value = inputDialog.open("Add a node by value", "Please give the value of node: ");
                        if (value != null) {
                            drawPanel.addNodeStep(drawPanel.tree.root, value);
                        }
                    }
                });
        MenuItem findNode=new MenuItem(actionMenu, SWT.PUSH);
        findNode.setText("Find a node");
        findNode.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                InputDialog inputDialog=new InputDialog(shell);
                Integer value=inputDialog.open("Find node by value", "Please give the value of node: ");
                if (value!=null){
                    drawPanel.findNodeStep(drawPanel.tree.root, value);
                }
            }
        });
        ///////////////////////////
        MenuItem removeNode=new MenuItem(editMenu, SWT.PUSH);
        removeNode.setText("Remove a node by value");
        removeNode.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                InputDialog inputDialog=new InputDialog(shell);
                Integer value=inputDialog.open("Delete a node", "Please give the value of the node: ");
                drawPanel.treeNode =drawPanel.tree.root;
                drawPanel.remove_findNode(value);
            }
        });
        shell.setMenuBar(menuBar);
        /////////////////////////////
        MenuItem findSuccessor=new MenuItem(actionMenu, SWT.PUSH);
        findSuccessor.setText("Find a succesor of a node");
        findSuccessor.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                InputDialog inputDialog=new InputDialog(shell);
                Integer value=inputDialog.open("Delete a node", "Please give the value of the node: ");
                drawPanel.findSucesscor(value);
            }
        });
        ///////////////////////////////
        MenuItem findPredecessor=new MenuItem(actionMenu, SWT.PUSH);
        findPredecessor.setText("Find a predesesscor of a node");
        findPredecessor.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                InputDialog inputDialog=new InputDialog(shell);
                Integer value=inputDialog.open("Delete a node", "Please give the value of the node: ");
                drawPanel.findPredesscor(value);
            }
        });
        /////////////////////////////
        MenuItem printItem=new MenuItem(actionMenu, SWT.PUSH);
        printItem.setText("Give the list sort elements");
        printItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                drawPanel.printElement();
            }
        });
    }
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
