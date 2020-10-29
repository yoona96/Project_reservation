package main_pkg;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import main_pkg.*;

public class Reservation_Change {
	private String name,phone;
	private String date,time,table; //index of the old reservation
	private ArrayList<int[]> user_inform[];
	private String temp[]=new String[11];
	
	Scanner s =new Scanner(System.in);
	Reservation rsv=new Reservation();
	Reservation_Check rc=new Reservation_Check();
	Cancel_Reservation cr=new Cancel_Reservation();
	Validate v=new Validate();
	
	private void new_reservation() {
		rsv.set_rsv("예약 변경",name,phone);
		rsv.user_input();
		delete_old_reservation();
		
	}
	
	private boolean confirm_change() {
		while(true) {
			System.out.println("선택하신 예약정보를 변경하시겠습니까?");	
			String confirm = s.next();
	        String[] yorn_value = new String[0];
	
	        if (confirm.contains(" ") || confirm.contains("")) {
	           yorn_value = confirm.trim().split(" ");
	        }

        	if (yorn_value[0].equals("y")) {
            	return true;
            }
            else if(yorn_value[0].equals("n")) {
            	return false;
            }
            else {
            	System.out.println("입력은 y 혹은 n 만 가능합니다. 다시 입력해주세요.");
            }
        }
        
	}
	
	private void delete_old_reservation() {
		try {
			File file=new File(v.get_directory()+date+".txt");
			FileReader fr=new FileReader(file);
			BufferedReader br=new BufferedReader(fr);
			String line="";
			String new_line="";
			
			while((line=br.readLine())!=null) { //read file
				if(line.trim().split("\t")[0].equals(table)) {
					if(line.trim().split("\t")[3].equals(time)||line.trim().split("\t")[3].equals(time+1)){
						new_line+=line.trim().split("\t")[0]+"\t"+line.trim().split("\t")[1]+"\t0\t"+line.trim().split("\t")[3]+"\n";
					}	
				}
				new_line+=line;
			}
			
			
			FileWriter fw=new FileWriter(file);
			fw.write(new_line); //overwrite file with new_line
			
			fr.close();
			br.close();
			fw.close();
			
		}catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		
		File_IO fi=new File_IO();
		fi.read_menu();
		String[][] menu = new String[4][5]; //menu file array
		int[] int_menu_num = new int[5];//menu info before changing int array
	    menu = fi.tb.get_menu();
	    for (int i = 0; i < 5; i++) {
	    	int_menu_num[i]=Integer.parseInt(temp[i+5]);
	        if (int_menu_num[i] != 0) {
	           int origin_stock = Integer.parseInt(menu[2][i]);
	           int new_stock = origin_stock + int_menu_num[i];
	           menu[2][i] = Integer.toString(new_stock);
	           //재고 정보 update
	           fi.tb.set_menu(menu);
	           fi.write_menu();
	       }
	    }
		
		
	}
	
	public void change_main() {
		user_inform=rc.show_reservation_inform("Change");
		while(true) {
			String str[]=new String[2];
			str=cr.input_reservation_date(); 
			date=str[0]; //ex.20201029
			time=str[1]; //ex.13
			File_IO fi=new File_IO();
			String today=fi.get_date(0);
			int change_day=0;
			
			if(Integer.parseInt(date)==Integer.parseInt(today)+1) {
				change_day=1; //tomorrow
			}
			else if(Integer.parseInt(date)==Integer.parseInt(today)+2)
				change_day=2; //a day after tomorrow
			
			for(int i=0;i<user_inform[change_day].size();i++) {
				if(user_inform[change_day].get(i)[0]==Integer.parseInt(time)) {
					table=Integer.toString(user_inform[change_day].get(i)[1]);
				}
			}//setting date,time, and table
			
			if(cr.compare_reservation_date(user_inform)) {	
				try {
					File file=new File(v.get_directory()+date+".txt");
					FileReader fr=new FileReader(file);
					BufferedReader br=new BufferedReader(fr);
					String line="";
					String new_line="";
					
					
					while((line=br.readLine())!=null) { //read file
						if(line.trim().split("\t")[0].equals(table)&&line.trim().split("\t")[3]==time){
							for(int i=0;i<line.trim().split("\t").length;i++) {
								temp[i]=line.trim().split("\t")[i]; //save the old reservation info
							}
							break;
						}
					}
					fr.close();
					br.close();
				}catch (FileNotFoundException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
				
				
				
				if(confirm_change())
					new_reservation();
				else
					return; //go to main
				
			}
			else {
				System.out.println("예약정보가 존재하지 않습니다. 선택한 예약 일자와 시간을 다시 한번 확인 후 입력바랍니다.");
			}
		}

	}
	
}
