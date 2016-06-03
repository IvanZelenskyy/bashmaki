package bashmaki.gui;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Text;

public class Bbbb extends Composite {

	private DataBindingContext m_bindingContext;
	private bashmaki.gui.ABean aBean = new bashmaki.gui.ABean();
	private Scale iScale;
	private Text textText;

	public Bbbb(Composite parent, int style, bashmaki.gui.ABean newABean) {
		this(parent, style);
		setABean(newABean);
	}

	public Bbbb(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));

		new Label(this, SWT.NONE).setText("I:");

		iScale = new Scale(this, SWT.HORIZONTAL);
		iScale.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		new Label(this, SWT.NONE).setText("Text:");

		textText = new Text(this, SWT.BORDER | SWT.SINGLE);
		textText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		if (aBean != null) {
			m_bindingContext = initDataBindings();
		}
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	private DataBindingContext initDataBindings() {
		IObservableValue iObserveWidget = SWTObservables
				.observeSelection(iScale);
		IObservableValue iObserveValue = PojoObservables.observeValue(aBean,
				"i");
		IObservableValue textObserveWidget = SWTObservables.observeText(
				textText, SWT.Modify);
		IObservableValue textObserveValue = PojoObservables.observeValue(aBean,
				"text");
		//
		DataBindingContext bindingContext = new DataBindingContext();
		//
		bindingContext.bindValue(iObserveWidget, iObserveValue, null, null);
		bindingContext.bindValue(textObserveWidget, textObserveValue, null,
				null);
		//
		return bindingContext;
	}

	public bashmaki.gui.ABean getABean() {
		return aBean;
	}

	public void setABean(bashmaki.gui.ABean newABean) {
		setABean(newABean, true);
	}

	public void setABean(bashmaki.gui.ABean newABean, boolean update) {
		aBean = newABean;
		if (update) {
			if (m_bindingContext != null) {
				m_bindingContext.dispose();
				m_bindingContext = null;
			}
			if (aBean != null) {
				m_bindingContext = initDataBindings();
			}
		}
	}

}
