package bashmaki.gui;

import bashmaki.gui.Track.ReportRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class TrackReportGenerator {

	public static void saveReport(Track track, String filename) {
		StringBuffer html = new StringBuffer();
		InputStream templateStream = null;// TrackReportGenerator.class.getResourceAsStream("/bashmaki/gui/reportTemplate.html");
		try {
			templateStream = new FileInputStream("res/reportTemplate.html");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(templateStream));
		String line = null;
		try {
			while((line=br.readLine()) != null){
				html.append(line);
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Integer[] i = new Integer[track.kd];
		for (int j = 0; j < i.length; j++) {
			i[j] = j+1;
		}
		String result = html.toString()
			.replaceAll("_HEADER_", track.name)
			.replaceAll("_len_of_block_", track.kd.toString())
			.replaceAll("_n_diljanky_", arrayToCells(i, "small"))
			.replaceAll("_uhylyY_", arrayToCells(track.uhyls, "float"))
			.replaceAll("_dovjyny_", arrayToCells(track.pieces, "float"))
			.replaceAll("_total_len_", track.Len.toString())
			.replaceAll("_total_uhyl_", track.pryv_uh_total.toString())
			.replaceAll("_mistkist_kolii_", track.osey_total.toString())
			.replaceAll("_mistkist_dilanok_", arrayToCells(track.osey, "float"))
			.replaceAll("_normy_f1_", arrayToCells(track.formula1, "float"))
			.replaceAll("_normy_f2_", arrayToCells(track.formula2, "float"))
			.replaceAll("_pryv_uhyly_pr_", arrayToCells(track.pryv_uh_pr, "float"))
			.replaceAll("_sum_dovj_pr_", arrayToCells(track.sum_dovj_pr, "float"))
			.replaceAll("_pryv_uhyly_zv_", arrayToCells(track.pryv_uh_zv, "float"))
			.replaceAll("_sum_dovj_zv_", arrayToCells(track.sum_dovj_zv, "float"))
			.replaceAll("_table_DF1_", createDirectCalcTable(track, true))
			.replaceAll("_table_DF2_", createDirectCalcTable(track, false))
			.replaceAll("_table_BF1_", createBackCalcTable(track, true))
			.replaceAll("_table_BF2_", createBackCalcTable(track, false));
			
		//System.out.println(html.toString());
		try {
			Writer w = new FileWriter(filename);
			BufferedWriter wr = new BufferedWriter(w);
			wr.write(result);
			wr.flush();
			wr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static <T> String arrayToCells(T[] arr, String className){
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			sb.append("<td class=\"").append(className).append("\">")
			.append(arr[i]).append("</td>\n");
		}
		return sb.toString();
	}
	
	
	
	private static String createDirectCalcTable(Track track, boolean formula1){
		Double[] formula = formula1?track.formula1:track.formula2;
		ArrayList<ReportRecord> list;
		if(formula1){
			list = track.directF1;
		} else list = track.directF2;
		//System.out.print("list:");
		//System.out.println(list);
		if(list==null)
			return "";
		
		StringBuilder sb = new StringBuilder();
		sb.append("<h3>Розрахунок норм закріплення гальмовими башмаками за формулою ").append(formula1?1:2)
			.append(" у прямому напрямку</h3>");
		sb.append("<table class=\"calc\"><thead>");
		sb.append("<tr>")
			.append("<td rowspan=\"2\" colspan=\"2\" ><p>Роз&shy;ра&shy;хун&shy;ко&shy;ва ве&shy;ли&shy;чи&shy;на</p></td>")
			.append("<td rowspan=\"2\" ><p>Оди&shy;ниця виміру</p></td>")
			//.append("<td rowspan=\"2\" ><p>Обчислення пропорції для останньої ділянки зони</p></td>")
			.append("<td colspan=\"").append(track.kd).append("\"><p>Дані та результати по ділянкам спрямлення</p></td>")
			.append("<td rowspan=\"2\" ><p>Під&shy;сум&shy;кове </p>")
			.append("<p>зна&shy;чен&shy;ня</p></td>")
			.append("</tr>");
		Integer[] i = new Integer[track.kd];
		for (int j = 0; j < i.length; j++) {
			i[j] = j+1;
		}
		sb.append("<tr>");
		sb.append(arrayToCells(i, "small"));
		sb.append("</tr>");
		sb.append("</thead><tbody>");
		Collections.sort(list);
		for(ReportRecord rec : list){
			if(rec.string.equals("special"))
				sb.append("<tr><td colspan=\"").append(4+track.kd)
					.append("\"class=\"small\">Визначення межі зони з приведеними ухилами і < 1,0</td></tr>");
			sb.append("<tr><td rowspan=\"4\"><b>")
				.append(rec.n).append("</b>");
			String s = " баш&shy;мак";
			if(rec.n>4){
				s=" баш&shy;маків";
			} else if(rec.n>1){
				s=" баш&shy;маки";
			}
			sb.append(s);
			if(Math.abs(rec.uh_x)<=1)
				sb.append("<b>+1</b> баш&shy;мак з проти&shy;леж&shy;ного боку");
			sb.append("</td>").append("<td>Зона дії</td><td>баш&shy;маків</td>");
			//fill formula
			double acc = 0.0;
			for (int j = 0; j < track.kd; j++) {
				sb.append("<td>");
				if(j<rec.i){
					sb.append(formula[j]);
					acc+=formula[j];
				} else if(j==rec.i){
					sb.append(Track.myRound(rec.b_x,3));
					acc+=rec.b_x;
				}
				sb.append("</td>");
			}
			if(rec.string.equals("special")){
				sb.append("<td class=\"result\">").append(Track.myRound(acc,3)).append("</td>");
			} else
				sb.append("<td class=\"result\">").append(rec.n).append("</td>");
			
			sb.append("</tr><tr><td>Міст&shy;кість зони</td><td>Осей умов&shy;них вагонів</td>");
			//fill osey
			if(rec.string.equals("final")){
				sb.append("<td colspan=\"").append(track.kd)
					.append("\"><p class=\"center\">Місткість колії</p></td><td class=\"result\">")
					.append(track.osey_total).append("</td>");
			} else {
				acc = 0.0;
				for (int j = 0; j < track.kd; j++) {
					sb.append("<td>");
					if(j<rec.i){
						sb.append(track.osey[j]);
						acc+=track.osey[j];
					} else if(j==rec.i){
						sb.append(Track.myRound(rec.osey_x,1));
						acc+=rec.osey_x;
					}
					sb.append("</td>");
				}
				//System.out.println("acc = "+ (new Double(acc)).toString());
				int x = (int) Math.floor(acc);
				x = (x%2==1)?x-1:x;
				sb.append("<td class=\"result\">&asymp;").append(x).append("</td>");
			}
				
			sb.append("</tr><tr><td>Дов&shy;жина зони</td><td>м</td>");
			//fill len
			if(rec.string.equals("final")){
				sb.append("<td colspan=\"").append(track.kd)
					.append("\"><p class=\"center\">Зона дії ").append(rec.n)
					.append(" башмаків перевищує корисну довжину колії (ділянки). Розрахунок закінчено.</p></td><td class=\"result\">")
					.append(track.Len).append("</td>");
			} else {
				acc = 0.0;
				for (int j = 0; j < track.kd; j++) {
					sb.append("<td>");
					if(j<rec.i){
						sb.append(track.pieces[j]);
						acc+=track.pieces[j];
					} else if(j==rec.i){
						sb.append(Track.myRound(rec.l_x,1));
						acc+=rec.l_x;
					}
					sb.append("</td>");
				}
				sb.append("<td class=\"result\">").append(Track.myRound(acc, 1)).append("</td>");
			}
			sb.append("</tr><tr><td>При&shy;ве&shy;де&shy;ний ухил зони</td><td>&permil;</td>");
			sb.append("<td colspan=\"").append(track.kd).append("\"></td><td class=\"result\">")
				.append(Track.myRound(rec.uh_x,1)).append("</td></tr>");
		}
		sb.append("</tbody></table>");
		return sb.toString();
	}

	private static String createBackCalcTable(Track track, boolean formula1){
		Double[] formula = formula1?track.formula1:track.formula2;
		ArrayList<ReportRecord> list;
		if(formula1){
			list = track.backF1;
		} else list = track.backF2;
		//System.out.print("list:");
		//System.out.println(list);
		if(list==null)
			return "";
		
		StringBuilder sb = new StringBuilder();
		sb.append("<h3>Розрахунок норм закріплення гальмовими башмаками за формулою ").append(formula1?1:2)
		.append(" у зворотньому напрямку</h3>");
		sb.append("<table class=\"calc\"><thead>");
		sb.append("<tr>")
			.append("<td rowspan=\"2\" colspan=\"2\" ><p>Роз&shy;ра&shy;хун&shy;ко&shy;ва ве&shy;ли&shy;чи&shy;на</p></td>")
			.append("<td rowspan=\"2\" ><p>Оди&shy;ниця виміру</p></td>")
			//.append("<td rowspan=\"2\" ><p>Обчислення пропорції для останньої ділянки зони</p></td>")
			.append("<td colspan=\"").append(track.kd).append("\"><p>Дані та результати по ділянкам спрямлення</p></td>")
			.append("<td rowspan=\"2\" ><p>Під&shy;сум&shy;кове </p>")
			.append("<p>зна&shy;чен&shy;ня</p></td>")
			.append("</tr>");
		Integer[] i = new Integer[track.kd];
		for (int j = 0; j < i.length; j++) {
			i[j] = j+1;
		}
		sb.append("<tr>");
		sb.append(arrayToCells(i, "small"));
		sb.append("</tr>");
		sb.append("</thead><tbody>");
		Collections.sort(list);
		for(ReportRecord rec : list){
			if(rec.string.equals("special"))
				sb.append("<tr><td colspan=\"").append(4+track.kd)
					.append("\"class=\"small\">Визначення межі зони з приведеними ухилами і < 1,0</td></tr>");
			sb.append("<tr><td rowspan=\"4\"><b>")
				.append(rec.n).append("</b>");
			String s = " баш&shy;мак";
			if(rec.n>4){
				s=" баш&shy;маків";
			} else if(rec.n>1){
				s=" баш&shy;маки";
			}
			sb.append(s);
			if(Math.abs(rec.uh_x)<=1)
				sb.append("<b>+1</b> баш&shy;мак з проти&shy;леж&shy;ного боку");
			sb.append("</td>").append("<td>Зона дії</td><td>баш&shy;маків</td>");
			//fill formula
			double acc = 0.0;
			for (int j = 0; j < track.kd; j++) {
				sb.append("<td>");
				if(j>track.kd-1-rec.i){
					sb.append(formula[j]);
					acc+=formula[j];
				} else if(j==track.kd-1-rec.i){
					sb.append(Track.myRound(rec.b_x,3));
					acc+=rec.b_x;
				}
				sb.append("</td>");
			}
			if(rec.string.equals("special")){
				sb.append("<td class=\"result\">").append(Track.myRound(acc,3)).append("</td>");
			} else
				sb.append("<td class=\"result\">").append(rec.n).append("</td>");
			
			sb.append("</tr><tr><td>Міст&shy;кість зони</td><td>Осей умов&shy;них вагонів</td>");
			//fill osey
			if(rec.string.equals("final")){
				sb.append("<td colspan=\"").append(track.kd)
					.append("\"><p class=\"center\">Місткість колії</p></td><td class=\"result\">")
					.append(track.osey_total).append("</td>");
			} else {
				acc = 0.0;
				for (int j = 0; j < track.kd; j++) {
					sb.append("<td>");
					if(j>track.kd-1-rec.i){
						sb.append(track.osey[j]);
						acc+=track.osey[j];
					} else if(j==track.kd-1-rec.i){
						sb.append(Track.myRound(rec.osey_x,1));
						acc+=rec.osey_x;
					}
					sb.append("</td>");
				}
				//System.out.println("acc = "+ (new Double(acc)).toString());
				int x = (int) Math.floor(acc);
				x = (x%2==1)?x-1:x;
				sb.append("<td class=\"result\">&asymp;").append(x).append("</td>");
			}
				
			sb.append("</tr><tr><td>Дов&shy;жина зони</td><td>м</td>");
			//fill len
			if(rec.string.equals("final")){
				sb.append("<td colspan=\"").append(track.kd)
					.append("\"><p class=\"center\">Зона дії ").append(rec.n)
					.append(" башмаків перевищує корисну довжину колії (ділянки). Розрахунок закінчено.</p></td><td class=\"result\">")
					.append(track.Len).append("</td>");
			} else {
				acc = 0.0;
				for (int j = 0; j < track.kd; j++) {
					sb.append("<td>");
					if(j>track.kd-1-rec.i){
						sb.append(track.pieces[j]);
						acc+=track.pieces[j];
					} else if(j==track.kd-1-rec.i){
						sb.append(Track.myRound(rec.l_x,1));
						acc+=rec.l_x;
					}
					sb.append("</td>");
				}
				sb.append("<td class=\"result\">").append(Track.myRound(acc, 1)).append("</td>");
			}
			sb.append("</tr><tr><td>При&shy;ве&shy;де&shy;ний ухил зони</td><td>&permil;</td>");
			sb.append("<td colspan=\"").append(track.kd).append("\"></td><td class=\"result\">")
				.append(Track.myRound(rec.uh_x,1)).append("</td></tr>");
		}
		sb.append("</tbody></table>");
		return sb.toString();
	}
}
