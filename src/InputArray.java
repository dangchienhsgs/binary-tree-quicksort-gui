import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class InputArray extends Dialog {
    int value[];

    /* Constructor */
    public InputArray(Shell parent) {
        super(parent);
    }

    public InputArray(Shell parent, int style) {
        super(parent, style);
    }

    /* Open a Dialog */
    public int[] open(String title, String labelString) {
        Shell parent = getParent();
        final Shell shell =
                new Shell(parent, SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL);
        shell.setText(title);

        shell.setLayout(new GridLayout(2, true));


        Label label = new Label(shell, SWT.NULL);
        label.setText(labelString);

        final Text text = new Text(shell, SWT.SINGLE | SWT.BORDER);

        final Button buttonOK = new Button(shell, SWT.PUSH);
        buttonOK.setText("OK");

        Button buttonCancel = new Button(shell, SWT.PUSH);
        buttonCancel.setText("Cancel");

        text.addListener(SWT.Modify, new Listener() {
            public void handleEvent(Event event) {
                String args[] = text.getText().split(" ");
                boolean check = true;
                value=new int[args.length];
                for (int i = 0; i < args.length; i++) {
                    try {
                        value[i]=Integer.parseInt(args[i]);
                    } catch (NumberFormatException e) {
                        check = false;
                    }
                }
                System.out.println (check);
                if (check) buttonOK.setEnabled(true); else
                buttonOK.setEnabled(false);
            }
        });

        buttonOK.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                shell.dispose();
            }
        });

        buttonCancel.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                value = null;
                shell.dispose();
            }
        });

        shell.addListener(SWT.Traverse, new Listener() {
            public void handleEvent(Event event) {
                if (event.detail == SWT.TRAVERSE_ESCAPE)
                    event.doit = false;
            }
        });

        text.setText("");
        shell.pack();
        shell.open();

        Display display = parent.getDisplay();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        return value;
    }

}

