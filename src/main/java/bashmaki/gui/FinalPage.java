package bashmaki.gui;

//import org.eclipse.core.internal.registry.ThirdLevelConfigurationElementHandle;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.*;

public class FinalPage extends WizardPage {
	private Text text;
	private Button checkbox;

	/**
	 * Create the wizard.
	 */
	public FinalPage() {
		super("wizardPage");
		setTitle("Наконец,");
		setDescription("укажите, куда сохранить расчет и нажмите \"Закончить\"");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		
		Label label = new Label(container, SWT.NONE);
		label.setBounds(10, 10, 162, 17);
		label.setText("Сохранить как:");
		
		text = new Text(container, SWT.BORDER);
		text.setBounds(10, 33, 462, 27);
		
		Button btnNewButton = new Button(container, SWT.PUSH);
		btnNewButton.setBounds(478, 31, 100, 29);
		btnNewButton.setText("Сохранить...");
		final FinalPage p = this;
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDlg = new FileDialog(p.getShell(), SWT.SAVE);
				fileDlg.setText("Сохранить файл");
				String[] filterExt = {"*.html", "*.*"};
				fileDlg.setFilterExtensions(filterExt);
				p.setFilename(fileDlg.open());
			}
		});
		
		checkbox = new Button(container, SWT.CHECK);
		checkbox.setBounds(10, 236, 265, 24);
		checkbox.setText("Открыть в браузере");
	}

	protected void setFilename(String open) {
		text.setText(open);
	}

	public String getFilename() {
		return text.getText();
	}

	public boolean getCheckbox(){
		return checkbox.getSelection();
	}
}
