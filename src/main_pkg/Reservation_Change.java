package main_pkg;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import main_pkg.*;

public class Reservation_Change {
	private String name,phon_num;
	private String date,line_index; //index of the old reservation
	private ArrayList<int[]> user_inform[];
	
	Scanner s =new Scanner(System.in);
	Reservation rsv=new Reservation();
	Reservation_Check rc=new Reservation_Check();
	Validate v=new Validate();
	
	private boolean new_reservation() {
		rsv.user_input();
		rsv.search_table();
		//좌석 자동할당까지
		rsv.choose_menu();
		//예약내역 출력까지 동일

		this.delete_old_reservation();
		
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
		date=user_inform[i];//무슨 날인지
		time=user_inform[i].get
				table; //time table 여러개 가능
		boolean attached=false;
		File file=new File(v.get_directory()+date+".txt");
		FileReader fr=new FileReader(file);
		BufferedReader br=new BufferedReader(fr);
		String line="";
		String new_line="";
		
		while((line=br.readLine())!=null) { //read file
			if(line.trim().split("\t")[0].equals(table)) {
				if(line.trim().split("\t")[3].equals(time)||line.trim().split("\t")[3].equals(time+1)){
					new_line+=line.trim().split("\t")[0]+"\t"+line.trim().split("\t")[1]+"0\t"+line.trim().split("\t")[3]+"\n";
				}	
			}
			if(attached) {
				if(line.trim().split("\t")[0].equals(table+1)) {
					if(line.trim().split("\t")[3].equals(time)||line.trim().split("\t")[3].equals(time+1)){
						new_line+=line.trim().split("\t")[0]+"\t"+line.trim().split("\t")[1]+"0\t"+line.trim().split("\t")[3]+"\n";
					}	
				}
				
			}	
			new_line+=br.readLine();
		}
		
		
		FileWriter fw=new FileWriter(file);
		fw.write(new_line); //overwrite file with new_line
		
		fr.close();
		br.close();
		fw.close();
		
	}
	
	public void change() {
		user_inform=rc.show_reservation_inform("Change");
		//choose date and time
		if(confirm_change())
			new_reservation();
		else
			return; //go to main

	}
	
}
