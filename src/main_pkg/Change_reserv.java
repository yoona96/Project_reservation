package main_pkg;
import java.util.Scanner;
import java.io.*;
import main_pkg.*;

public class Change_reserv {
	private String name,phon_num;
	private String date,line_index; //index of the old reservation
	
	Scanner s =new Scanner(System.in);
	Reservation rsv=new Reservation();
	
	private boolean new_reservation() {
		rsv.user_input();
		rsv.search_table();
		//좌석 자동할당까지
		rsv.choose_menu();
		//예약내역 출력까지 동일

		this.delete_reservation();
		
	}
	private boolean choose_reservation() {
		while(true) {
			System.out.println("선택하신 예약정보를 변경하시겠습니까?");	
			String menu_confirm = s.next();
	        String[] yorn_value = new String[0];
	
	        if (menu_confirm.contains(" ") || menu_confirm.contains("")) {
	           yorn_value = menu_confirm.trim().split(" ");
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
	private void delete_reservation() {
		File file=new File("src/date/"+date+".txt");
		FileReader fr=new FileReader(file);
		BufferedReader br=new BufferedReader(fr);
		String line="";
		String new_line="";
		for(int i=0;i<line_index;i++) {
			new_line+=br.readLine();
		}//read file before the line index
		
		line=br.readLine();
		String[] buf_line=line.trim().split("\t");
		for(int i=0;i<4;i++) {
			switch(i){
			case 0:{ //table number
				new_line+=buf_line[i]+"\t";
				break;
			}
			case 1:{ //table count limit
				new_line=buf_line[i]+"\t";
				break;
			}
			case 2:{ //reserved count
				new_line+="0\t";
			}
			case 3:{ //time
				new_line+=buf_line[i]+"\n";
			}
			}
		}
		
		while((line=br.readLine())!=null) {
			new_line+=line;
		}
		
		FileWriter fw=new FileWriter(file);
		fw.write(new_line); //overwrite file with new_line
		
		fr.close();
		br.close();
		fw.close();
		
	}
	
	public void change() {
		//input user name and phone number
		//show all reservations of user
		//choose reservation
		if(choose_reservation())
			new_reservation();
		else
			return; //go to main

	}
	
}

