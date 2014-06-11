import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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

        MenuItem fileMenuHeader=new MenuItem(menuBar, SWT.CASCADE);
        fileMenuHeader.setText("Edit");

        Menu fileMenu=new Menu(shell, SWT.DROP_DOWN);
        fileMenuHeader.setMenu(fileMenu);

        MenuItem addNewNode=new MenuItem(fileMenu, SWT.PUSH);
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
        MenuItem addNodeStep=new MenuItem(fileMenu, SWT.PUSH);
        final MenuItem browseAllNode=new MenuItem(fileMenu, SWT.PUSH);
        browseAllNode.setText("BrowseAllNode");
        browseAllNode.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                drawPanel.browseAllNode(drawPanel.tree.root);
            }
        });
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

        MenuItem findNode=new MenuItem(fileMenu, SWT.PUSH);
        findNode.setText("Find a node");
        findNode.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                InputDialog inputDialog=new InputDialog(shell);
                Integer value=inputDialog.open("Find node by value", "Please give the value of node: ");
                if (value!=null){
                    drawPanel.findNode(drawPanel.tree.root, value);
                }
            }
        });
        MenuItem removeNode=new MenuItem(fileMenu, SWT.PUSH);
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
