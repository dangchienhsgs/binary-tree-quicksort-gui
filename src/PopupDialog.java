import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class PopupDialog extends Dialog {
    Integer value;

    /* Constructor */
    public PopupDialog(Shell parent) {
        super(parent);
    }

    public PopupDialog(Shell parent, int style) {
        super(parent, style);
    }

    /* Open a Dialog */
    public Integer open(String title, String  labelString) {
        Shell parent = getParent();
        final Shell shell =new Shell(parent, SWT.TITLE | SWT.BORDER);
        shell.setText(title);
        GridLayout gridLayout=new GridLayout();
        gridLayout.numColumns=1;
        gridLayout.makeColumnsEqualWidth = true;

        shell.setLayout(gridLayout);

        Label label = new Label(shell, SWT.CENTER);
        label.setLayoutData(new GridData(GridData.CENTER| GridData.HORIZONTAL_ALIGN_CENTER));
        label.setText(labelString);

        //final Text text = new Text(shell, SWT.SINGLE | SWT.BORDER);

        final Button buttonOK = new Button(shell, SWT.PUSH | SWT.CENTER);
        buttonOK.setText("OK");


        buttonOK.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                shell.dispose();
            }
        });

        shell.addListener(SWT.Traverse, new Listener() {
            public void handleEvent(Event event) {
                if(event.detail == SWT.TRAVERSE_ESCAPE)
                    event.doit = false;
            }
        });

//        text.setText("");
        shell.setSize((labelString.length())*9, 150);
        shell.open();

        Display display = parent.getDisplay();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        return value;
    }

}

