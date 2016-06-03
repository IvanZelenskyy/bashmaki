package bashmaki.gui;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.wb.swt.SWTResourceManager;

public class SelectFile extends WizardPage {
	private Text text;
	private Text text_1;
	private Button checkButton;

	/**
	 * Create the wizard.
	 */
	public SelectFile() {
		super("wizardPage");
		setTitle("Шаг 1");
		setDescription("Введите название пути (участка пути) и выберите файл с точками.");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		
		Label label = new Label(container, SWT.NONE);
		label.setBounds(10, 10, 380, 17);
		label.setText("Название пути или участка пути:");
		
		text = new Text(container, SWT.BORDER);
		text.setBounds(10, 33, 568, 27);
		
		text_1 = new Text(container, SWT.BORDER);
		text_1.setBounds(10, 95, 471, 27);
		
		Label label_1 = new Label(container, SWT.NONE);
		label_1.setBounds(10, 72, 214, 17);
		label_1.setText("Выберите файл:");
		
		Button btnNewButton = new Button(container, SWT.PUSH);
		final SelectFile p = this;
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDlg = new FileDialog(p.getShell(), SWT.OPEN);
				fileDlg.setText("Открыть файл");
				String[] filterExt = {"*.csv", "*.txt"};
				fileDlg.setFilterExtensions(filterExt);
				p.setFilename(fileDlg.open());
			}
		});
		
		btnNewButton.setBounds(487, 93, 91, 29);
		btnNewButton.setText("Файл...");
		
		checkButton = new Button(container, SWT.CHECK);
		checkButton.setBounds(10, 236, 214, 24);
		checkButton.setText("Только цистерны");
		
		//setPageComplete(false);
	}
	
	public void setFilename(String s){
		this.text_1.setText(s);
	}

	public String getFilename() {
		return this.text_1.getText();
	}

	public String getTrackTitle() {
		return this.text.getText();
	}

	public void setTrackTitle(String trackTitle) {
		this.text.setText(trackTitle);
	}

	public int getWagonLen() {
		if(this.checkButton.getSelection()){
			return 12;
		} else {
			return 14;
		}
	}
	
	

}
