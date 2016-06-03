package bashmaki.gui;

import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Shell;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		WizardDialog dlg = new WizardDialog(new Shell(), new BWizard());
		try {
			if (dlg.open() == Window.OK) {
				System.out.println("Ok.");
			} else {
				System.out.println("Cancelled.");
			}
		} catch (Exception e) {

		}
	}

}
