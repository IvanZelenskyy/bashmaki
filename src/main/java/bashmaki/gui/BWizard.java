package bashmaki.gui;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

public class BWizard extends Wizard {

	public Track track;
	public String filename;
	public String trackTitle;
	public int wagonLen;
	
	SelectFile firstPage;
	VerifyPage secondPage;
	SetCalculationParamsPage thirdPage;
	FinalPage finalPage;
	
	public BWizard() {
		setWindowTitle("New Wizard");
	}

	@Override
	public void addPages() {
		firstPage = new SelectFile();
		this.addPage(firstPage);
		secondPage = new VerifyPage();
		this.addPage(secondPage);
		thirdPage = new SetCalculationParamsPage();
		this.addPage(thirdPage);
		finalPage = new FinalPage();
		this.addPage(finalPage);
	}

	@Override
	public boolean performFinish() {
		String s = finalPage.getFilename();
		track.setDirectBackFormulas(thirdPage.getDirect(), thirdPage.getBack(), thirdPage.getFormula1(), thirdPage.getFormula2());
		System.out.println(track.directF1);
		track.Calculate();
		TrackReportGenerator.saveReport(track, s);
		if(finalPage.getCheckbox() && Desktop.isDesktopSupported()){
			try {
				File f = new File(s);
				Desktop.getDesktop().browse(f.toURI());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}

	/*@Override
	public IWizardPage getNextPage(IWizardPage page) {
		// TODO Auto-generated method stub
		if(page==firstPage && firstPage.isPageComplete()){
			String t = firstPage.getTrackTitle();
			String f = firstPage.getFilename();
			int l = firstPage.getWagonLen();
			this.track = new Track(t, f, l, false, false, false, false);
			try{
				this.track.load();
			} catch(Exception e){
				MessageBox msg = new MessageBox(getShell(), SWT.ICON_ERROR);
				msg.setMessage((e.getMessage()!=null)?e.getMessage():"Не удалось загрузить файл.");
				msg.open();
				return firstPage;
			}
			StringBuilder sb = new StringBuilder();
			sb.append(t).append("\n")
				.append("Длина условного вагона: ").append(track.bumv).append("\n")
				.append("Длина пути:").append(track.Len).append("\n")
				.append("Количество участков:").append(track.kd).append("\n");
			secondPage.setText(sb.toString());
			return secondPage;
		}
		return firstPage;
	}*/
	
	
}
