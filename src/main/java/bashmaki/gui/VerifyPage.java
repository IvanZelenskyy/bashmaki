package bashmaki.gui;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

public class VerifyPage extends WizardPage {
	private Text text;

	/**
	 * Create the wizard.
	 */
	public VerifyPage() {
		super("wizardPage");
		setTitle("Шаг 2");
		setDescription("Проверьте данные.");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		
		text = new Text(container, SWT.BORDER | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		text.setBounds(0, 0, 588, 270);
	}

	public void setText(String s){
		text.setText(s);
	}
	
	@Override
	public void setVisible(boolean visible) {
		// TODO Auto-generated method stub
		super.setVisible(visible);
		BWizard w = (BWizard)this.getWizard();
		if(getPreviousPage() == w.firstPage){
			String t = w.firstPage.getTrackTitle();
			String f = w.firstPage.getFilename();
			int l = w.firstPage.getWagonLen();
			w.track = new Track(t, f, l, false, false, false, false);
			try{
				w.track.load();
			} catch(Exception e){
				MessageBox msg = new MessageBox(getShell(), SWT.ICON_ERROR);
				msg.setMessage((e.getMessage()!=null)?e.getMessage():"Не удалось загрузить файл.");
				msg.open();
			}
			StringBuilder sb = new StringBuilder();
			sb.append(t).append("\n")
				.append("Длина условного вагона: ").append(w.track.bumv).append("\n")
				.append("Длина пути:").append(w.track.Len).append("\n")
				.append("Количество участков:").append(w.track.kd).append("\n");
			this.setText(sb.toString());
		}
	}
}
