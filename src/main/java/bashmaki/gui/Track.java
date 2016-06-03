package bashmaki.gui;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Track {
	String name;
	String filename;
	boolean loaded;
	TrackType type;
	Integer bumv;
	
	//DATA
	Integer kd;
	Double Len;
	Double[] pieces;
	Double[] uhyls;
	Double[] osey;
	Double[] formula1;
	Double[] formula2;
	Double[] sum_dovj_pr;
	Double[] pryv_uh_pr;
	Double pryv_uh_total;
	Integer osey_total;
	Double[] pryv_uh_zv;
	Double[] sum_dovj_zv;
	
	
	public void setDirectBackFormulas(boolean direct, boolean back, boolean f1, boolean f2){
		this.directF1 = null;
		this.backF1 = null;
		this.directF2 = null;
		this.backF2 = null;
		if(f1){
			if(direct) this.directF1 = new ArrayList<ReportRecord>();
			if(back) this.backF1 = new ArrayList<ReportRecord>();
		}
		if(f2){
			if(direct) this.directF2 = new ArrayList<ReportRecord>();
			if(back) this.backF2 = new ArrayList<ReportRecord>();
		}
	}
	
	class ReportRecord implements Comparable<ReportRecord>{
		int i;
		int n;
		double b_x;
		double osey_x;
		double l_x;
		double uh_x;
		String string;

		public ReportRecord(int i, int n, double b_x, double osey_x,
			double l_x, double uh_x, String string){
			this.i = i;
			this.n = n;
			this.b_x = b_x;
			this.osey_x = osey_x;
			this.l_x = l_x;
			this.uh_x = uh_x;
			this.string = string;
		}

		@Override
		public int compareTo(ReportRecord o) {
			// TODO Auto-generated method stub
			int result;
			if(this.i!=o.i){
				result = (new Integer(i).compareTo(new Integer(o.i)));
			} else
				result = (new Integer(this.n).compareTo(new Integer(o.n)));
			
			return result;
		}
	}

	ArrayList<ReportRecord> directF1;
	ArrayList<ReportRecord> backF1;
	ArrayList<ReportRecord> directF2;
	ArrayList<ReportRecord> backF2;
	
	public Track(String name, String filename, boolean direct, boolean back, boolean f1, boolean f2){
		this(name, filename, 14, direct, back, f1, f2);
	}
	
	public Track(String name, String filename, Integer bumv, boolean direct, boolean back, boolean f1, boolean f2){
		this.name = name;
		this.filename = filename;
		this.type = type;
		this.loaded = false;
		this.bumv = bumv;
		if(f1){
			if(direct) this.directF1 = new ArrayList<ReportRecord>();
			if(back) this.backF1 = new ArrayList<ReportRecord>();
		}
		if(f2){
			if(direct) this.directF2 = new ArrayList<ReportRecord>();
			if(back) this.backF2 = new ArrayList<ReportRecord>();
		}
	}
	
	public void load(){
		ArrayList<Double> x_poIntegers = new ArrayList<Double>();
		ArrayList<Double> y_poIntegers = new ArrayList<Double>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line = "";
			while((line = reader.readLine())!=null){
	            if(line.startsWith("#")) continue;
	            String[] tokens = line.split(";");
	            if(tokens.length<2)
	            	continue;
	            try{
	            	x_poIntegers.add(new Double(tokens[0]));
	            	y_poIntegers.add(new Double(tokens[1]));
	            } catch (NumberFormatException e){
	            	throw new RuntimeException("Не удалось загрузить файл. Ошибка в строке:\n"+line);
	            }
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("Файл не найден или недоступен для чтения.");
		}
		if(x_poIntegers.size()!=y_poIntegers.size()){
			throw new RuntimeException("Количество горизонтальных и вертикальных отметок не совпадает.\n Проверьте файл.");
		}
		this.kd = x_poIntegers.size()-1;
		//make pieces and uhyls
		this.pieces = new Double[kd];
		this.uhyls = new Double[kd];
		this.osey = new Double[kd];
		this.formula1 = new Double[kd];
		this.formula2 = new Double[kd];
		this.sum_dovj_pr = new Double[kd];
		Double acc_len = 0.0;
		Double acc_osey = 0.0;
		Double pryv_uh_acc = 0.0;
		this.pryv_uh_pr = new Double[kd];
		for (Integer i = 0; i < kd; i++) {
			pieces[i]= myRound(x_poIntegers.get(i+1)-x_poIntegers.get(i), 2);
			if(pieces[i]<5)
				throw new RuntimeException("Входной файл содержит менее пяти отрезков. Выберите другой файл.");
			acc_len += pieces[i];
			sum_dovj_pr[i] = myRound(acc_len,2);
			Double delta_h = y_poIntegers.get(i+1)-y_poIntegers.get(i);
			uhyls[i]= 1.0*Math.round(delta_h*10000/pieces[i])/10;
			osey[i] = myRound((4*pieces[i]/bumv),1);
			acc_osey += osey[i];
			Double formula1_t = osey[i]*(1.5*Math.abs(uhyls[i])+1)/200;
			formula1_t = (uhyls[i]<0)?(-formula1_t):formula1_t;
			Double formula2_t = osey[i]*(4*Math.abs(uhyls[i])+1)/200;
			formula2_t = (uhyls[i]<0)?(-formula2_t):formula2_t;
			formula1[i] = myRound(formula1_t, 3);
			formula2[i] = myRound(formula2_t, 3);
			pryv_uh_acc += pieces[i]*uhyls[i];
			pryv_uh_pr[i] = myRound(pryv_uh_acc/acc_len, 2);
		}
		this.Len = acc_len;
		this.pryv_uh_total = myRound(pryv_uh_acc/Len, 1);
		this.osey_total = (int)(Math.floor(acc_osey));
		if(this.osey_total%2 == 1)
			this.osey_total--;
		
		acc_len=0.0;
		pryv_uh_acc = 0.0;
		
		//test
		
		this.pryv_uh_zv = new Double[kd];
		this.sum_dovj_zv = new Double[kd];
		for(Integer i=kd-1; i>=0; i--){
			acc_len += pieces[i];
			pryv_uh_acc += pieces[i]*uhyls[i];
			sum_dovj_zv[i] = myRound(acc_len,2);
			pryv_uh_zv[i] = myRound(pryv_uh_acc/acc_len, 1);
		}
	}
	
	static Double myRound(Double d, Integer i){
		Double tens = Math.pow(10, i);
		return Math.round(d*tens)/tens;
	}
	
	private static <T> String tabbedPrint(T[] arr){
		StringBuilder b = new StringBuilder();
		for (Integer i = 0; i < arr.length; i++) {
			b.append(arr[i]);
			b.append("\t");
		}
		return b.toString();
	}
	
	private static Double sumArr(Double[] arr){
		Double acc = 0.0;
		for (int i = 0; i < arr.length; i++) {
			acc += arr[i];
		}
		return acc;
	}
	

	private static Double sumArr(Double[] arr, int count){
		Double acc = 0.0;
		for (int i = 0; i < count; i++) {
			acc += arr[i];
		}
		return acc;
	}
	
	private static Double sumArrBack(Double[] arr, int count){
		Double acc = 0.0;
		for (int i = 0; i <count; i--) {
			System.out.printf("sumArrBack i=%d arr.length-1-i=%d\n", i,arr.length-1-i);
			acc += arr[arr.length-1-i];
		}
		return acc;
	}
	
	private static Double sumArrBack2(Double[] arr, int count){
		Double acc = 0.0;
		for (int i = arr.length-1; i >=count; i--) {
			//System.out.printf("sumArrBack i=%d arr.length-1-i=%d\n", i,arr.length-1-i);
			acc += arr[i];
		}
		return acc;
	}
		
	public boolean Calculate(){
		if(directF1 != null) calculateDirect(true);
		if(directF2 != null) calculateDirect(false);
		if(backF1 != null) calculateBack(true);
		if(backF2 != null) calculateBack(false);
		return true;
	}
	
	boolean calculateDirect(boolean formula1){
		System.out.println("CALCULATE DIRECT!");
		Double[] formula;
		if(formula1){
			formula = this.formula1;
		} else
			formula = this.formula2;
		
		int N = 1;
		int i = 0;
		double sum_b = 0;
		double prev_sum_b = 0;
		double prev_pryv_uhyl = pryv_uh_pr[0];
		//get normal points
		while(i<kd){
			sum_b = sumArr(formula, i+1);
			if(sum_b>N){
				double b = myRound(N - prev_sum_b,3);
				System.out.printf("b=%f\n", b);
				double o = osey[i]*b/formula[i];
				System.out.printf("osey[i]=%f b=%f formula[i]=%f prev_sum_b=%f N=%d i=%d\n", osey[i], b, formula[i], prev_sum_b, N, i);
				double l = pieces[i]*o/osey[i];
				double acc = 0;
				double prev_l = (i>0)?sum_dovj_pr[i-1]:0;
				/*for(int j=0; j<i; j++) {
					acc += pieces[j]*uhyls[j];
				}*/
				acc = (i>0)?pryv_uh_pr[i-1]*sum_dovj_pr[i-1]:0;
				double pryv_uhyl = (acc + uhyls[i]*l)/(prev_l + l);
				if((Math.abs(prev_pryv_uhyl)>1 && Math.abs(pryv_uhyl)<1) || (Math.abs(prev_pryv_uhyl)<1 && Math.abs(pryv_uhyl)>1)){
					System.out.printf("1from %f to %f\n", prev_pryv_uhyl,pryv_uhyl);
					//special point
					double uh_x = Math.signum(pryv_uhyl);
					double l_x = Math.abs(prev_l*(prev_pryv_uhyl-uh_x)/(uh_x-uhyls[i]));
					double osey_x = osey[i]*l_x/pieces[i];
					double b_x = formula[i] * osey_x/osey[i];
					generate_calc_record(i, N, b_x, osey_x, l_x, uh_x, "special", formula1, true);
				}
				generate_calc_record(i, N, b, o, l, pryv_uhyl, "", formula1, true);
				
				N++;
				if(sum_b>N){
					//prev_sum_b = prev_sum_b +1;
					prev_pryv_uhyl = pryv_uhyl;
					System.out.println("+1\n");
				} else {
					prev_pryv_uhyl = pryv_uh_pr[i];
					i++;
					prev_sum_b = sum_b;

				}
			} else {
				double prev_l = (i>0)?sum_dovj_pr[i-1]:0;
				double pryv_uhyl = pryv_uh_pr[i];
				if((Math.abs(prev_pryv_uhyl)>1 && Math.abs(pryv_uhyl)<1) || (Math.abs(prev_pryv_uhyl)<1 && Math.abs(pryv_uhyl)>1)){
					System.out.printf("1from %f to %f\n", prev_pryv_uhyl,pryv_uhyl);
					//special point
					double uh_x = Math.signum(pryv_uhyl);
					double l_x = Math.abs(prev_l*(prev_pryv_uhyl-uh_x)/(uh_x-uhyls[i]));
					double osey_x = osey[i]*l_x/pieces[i];
					double b_x = formula[i] * osey_x/osey[i];
					generate_calc_record(i, N, b_x, osey_x, l_x, uh_x, "special", formula1, true);
				}
				prev_pryv_uhyl = pryv_uh_pr[i];
				i++;
				prev_sum_b = sum_b;
			}
		}
		
		//final
		generate_calc_record(kd-1, N, sumArr(formula), this.osey_total, this.Len, this.pryv_uh_total, "final", formula1, true);
		return true;
	}
	
	boolean calculateBack(boolean formula1){
		System.out.println("CALCULATE BACK");
		Double[] formula;
		if(formula1){
			formula = this.formula1;
		} else
			formula = this.formula2;
		
		int N = 1;
		int i = kd-1;
		double sum_b = 0;
		double prev_sum_b = 0;
		double prev_pryv_uhyl = pryv_uh_zv[10];
		//get normal points
		while(i>=0){
			sum_b = sumArrBack2(formula, i);
			System.out.printf("i=%d sum_b=%f N=%d\n", i, sum_b, N);
			if(sum_b>N){
				double b = myRound(N - prev_sum_b,3);
				System.out.printf("b=%f\n", b);
				double o = osey[i]*b/formula[i];
				System.out.printf("osey[i]=%f b=%f formula[i]=%f prev_sum_b=%f N=%d i=%d\n", osey[i], b, formula[i], prev_sum_b, N, i);
				double l = pieces[i]*o/osey[i];
				double acc = 0;
				double prev_l = (i<kd-1)?sum_dovj_zv[i+1]:0;
				acc = (i<kd-1)?pryv_uh_zv[i+1]*sum_dovj_zv[i+1]:0;
				//System.out.printf("%f = (i<kd-1)?pryv_uh_zv[i+1]*sum_dovj_zv[i+1]:0;");
				double pryv_uhyl = (acc + uhyls[i]*l)/(prev_l + l);
				System.out.printf("acc=%f uhyls[i]=%f l=%f prev_l=%f\n, o=%f", acc, uhyls[i], l, prev_l, o);
				if((Math.abs(prev_pryv_uhyl)>1 && Math.abs(pryv_uhyl)<1) || (Math.abs(prev_pryv_uhyl)<1 && Math.abs(pryv_uhyl)>1)){
					System.out.printf("1from %f to %f\n", prev_pryv_uhyl,pryv_uhyl);
					//special point
					double uh_x = Math.signum(pryv_uhyl);
					double l_x = Math.abs(prev_l*(prev_pryv_uhyl-uh_x)/(uh_x-uhyls[i]));
					double osey_x = osey[i]*l_x/pieces[i];
					double b_x = formula[i] * osey_x/osey[i];
					generate_calc_record(kd-1-i, N, b_x, osey_x, l_x, uh_x, "special", formula1, false);
				}
				generate_calc_record(kd-1-i, N, b, o, l, pryv_uhyl, "", formula1, false);
				
				N++;
				if(sum_b>N){
					//prev_sum_b = prev_sum_b +1;
					prev_pryv_uhyl = pryv_uhyl;
					System.out.println("+1\n");
				} else {
					prev_pryv_uhyl = pryv_uh_zv[i];
					i--;
					prev_sum_b = sum_b;

				}
			} else {
				double prev_l = (i<kd-1)?sum_dovj_zv[i+1]:0;
				double pryv_uhyl = pryv_uh_zv[i];
				if((Math.abs(prev_pryv_uhyl)>1 && Math.abs(pryv_uhyl)<1) || (Math.abs(prev_pryv_uhyl)<1 && Math.abs(pryv_uhyl)>1)){
					System.out.printf("1from %f to %f\n", prev_pryv_uhyl,pryv_uhyl);
					//special point
					double uh_x = Math.signum(pryv_uhyl);
					double l_x = Math.abs(prev_l*(prev_pryv_uhyl-uh_x)/(uh_x-uhyls[i]));
					double osey_x = osey[i]*l_x/pieces[i];
					double b_x = formula[i] * osey_x/osey[i];
					generate_calc_record(kd-1-i, N, b_x, osey_x, l_x, uh_x, "special", formula1, false);
				}
				prev_pryv_uhyl = pryv_uh_zv[i];
				i--;
				prev_sum_b = sum_b;
			}
		}
		
		//final
		generate_calc_record(kd-1, N, sumArr(formula), this.osey_total, this.Len, this.pryv_uh_total, "final", formula1, false);
		return false;
	}
	

	private void generate_calc_record(int i, int n, double b_x, double osey_x,
			double l_x, double uh_x, String string, boolean formula1, boolean direct) {
		System.out.println("---------------");
		System.out.println(string);
		System.out.printf("башмаков: %d\n",n);
		System.out.printf("условных башмаков: %f\n",b_x);
		System.out.printf("условных осей: %f\n",osey_x);
		System.out.printf("длина: %f\n",l_x);
		System.out.printf("уклон: %f\n",uh_x);
		System.out.println("---------------");
		ReportRecord rec = new ReportRecord(i, n, b_x, osey_x, l_x, uh_x, string);
		if(formula1){
			if(direct){
				this.directF1.add(rec);
				System.out.println("\t\t\t\t\t\t\t\tadded to directF1.");
			} else {
				this.backF1.add(rec);
				System.out.println("\t\t\t\t\t\t\t\tadded to backF1.");
			}
		} else {
			if(direct){
				this.directF2.add(rec);
				System.out.println("\t\t\t\t\t\t\t\tadded to directF2.");
			} else {
				System.out.println("\t\t\t\t\t\t\t\tadded to backF2.");
				this.backF2.add(rec);
			}
		}
	}

	private boolean calculateHollow() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not yet implemented.");
	}

	private boolean calculateHill() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not yet implemented.");		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Track tr = new Track("asd", "saw_profile1.csv", 14, true, true, true, true);
		tr.load();
		System.out.println(tabbedPrint(tr.pieces));
		System.out.println(tabbedPrint(tr.uhyls));
		System.out.println(tabbedPrint(tr.osey));
		System.out.println(tabbedPrint(tr.formula1));
		System.out.println(tabbedPrint(tr.formula2));
		
		System.out.println(tabbedPrint(tr.pryv_uh_pr));
		System.out.println(tabbedPrint(tr.pryv_uh_zv));
		System.out.println(tr.pryv_uh_total);
		System.out.println(tabbedPrint(tr.sum_dovj_pr));
		System.out.println(tabbedPrint(tr.sum_dovj_zv));
		System.out.println(Track.myRound(123.4557, 2));
		System.out.println(Track.myRound(123.4557, 3));
		
		System.out.println(tabbedPrint(tr.formula1));
		System.out.println(tabbedPrint(tr.pieces));
		System.out.println("--------");
		Double[] arr = Arrays.copyOfRange(tr.formula1, 0, 1);
		System.out.println(arr.length);
		System.out.println(tabbedPrint(arr));
		tr.Calculate();
		
		
		System.out.println(sumArrBack2(tr.pieces, 0));
		//System.out.println(sumArrBack(tr.pieces, 1));
		//System.out.println(sumArrBack(tr.pieces, -3));
		System.out.println(sumArrBack2(tr.pieces, 10));
	}

}
