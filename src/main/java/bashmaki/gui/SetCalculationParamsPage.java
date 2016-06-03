package bashmaki.gui;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;

public class SetCalculationParamsPage extends WizardPage {

	Button buttonDirect;
	Button buttonBack;
	Button buttonFormula1;
	Button buttonFormula2;
	
	/**
	 * Create the wizard.
	 */
	public SetCalculationParamsPage() {
		super("wizardPage");
		setTitle("Шаг 3");
		setDescription("Настройте расчет.");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		
		buttonDirect = new Button(container, SWT.CHECK);
		buttonDirect.setBounds(10, 10, 568, 24);
		buttonDirect.setText("Выполнить расчет для прямого направления");
		
		buttonBack = new Button(container, SWT.CHECK);
		
		buttonBack.setText("Выполнить расчет для обратного направления");
		buttonBack.setBounds(10, 40, 568, 24);
		
		buttonFormula1 = new Button(container, SWT.CHECK);
		buttonFormula1.setBounds(10, 81, 568, 24);
		buttonFormula1.setText("Выполнить расчет по формуле 1");
		
		Label label = new Label(container, SWT.WRAP);
		label.setBounds(10, 110, 578, 55);
		label.setText("(для составов или групп вагонов из одинаковых по массе нагруженных или пустых вагонов, а также для смешанных групп/составов при условии укладки башмаков под нагруженные вагоны с нагрузкой более 15т)");
		
		buttonFormula2 = new Button(container, SWT.CHECK);
		buttonFormula2.setBounds(10, 193, 568, 24);
		buttonFormula2.setText("Выполнить расчет по формуле 2");
		
		Label label_1 = new Label(container, SWT.WRAP);
		label_1.setBounds(10, 216, 568, 33);
		label_1.setText("(для смешанных групп вагонов при условии укладки башмаков под порожние вагоны или вагоны с неизвестной массой)");
	}

	public boolean getDirect(){
		return buttonDirect.getSelection();
	}
	public boolean getBack(){
		return buttonBack.getSelection();
	}
	public boolean getFormula1(){
		return buttonFormula1.getSelection();
	}
	public boolean getFormula2(){
		return buttonFormula2.getSelection();
	}
}
