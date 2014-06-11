import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Popup extends Dialog {

    /* Constructor */
    public Popup(Shell parent) {
        super(parent);
    }

    public Popup(Shell parent, int style) {
        super(parent, style);
    }

    /* Open a Dialog */
    public void open(String title, String  labelString) {
        Shell parent = getParent();
        final Shell shell =
                new Shell(parent, SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL);
        shell.setText(title);

        shell.setLayout(new FillLayout(SWT.VERTICAL));

        Label label = new Label(shell, SWT.NONE);
        label.setText(labelString);


        final Button buttonOK = new Button(shell, SWT.PUSH);
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

        shell.pack();
        shell.open();

        Display display = parent.getDisplay();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
    }

}

