package main_pkg;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import main_pkg.*;

public class Reservation_Change {

	private String name="";
	private String phone="";
	private String date,time,table1,table2,count; //index of the old reservation
	int[] int_menu_num = new int[5];//menu info before changing int array
	private ArrayList<int[]> user_inform[]=new ArrayList[3];
	private String temp[]=new String[11];
	
	Scanner s =new Scanner(System.in);
	Reservation rsv= new Reservation();
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
			String time2=Integer.toString(Integer.parseInt(time+1));
			
			if(attached()) {
				while((line=br.readLine())!=null) { //read file
					if(line.trim().split("\t")[0].equals(table1)||line.trim().split("\t")[0].equals(table2)) {
						if(line.trim().split("\t")[3].equals(time)||line.trim().split("\t")[3].equals(time2))
							new_line+=line.trim().split("\t")[0]+"\t"+line.trim().split("\t")[1]+"\t0\t"+line.trim().split("\t")[3]+"\n";
					}
					new_line+=line+"\n";
				}
			}
			else {
				while((line=br.readLine())!=null) { //read file
					if(line.trim().split("\t")[0].equals(table1)) {
						if(line.trim().split("\t")[3].equals(time))
							new_line+=line.trim().split("\t")[0]+"\t"+line.trim().split("\t")[1]+"\t0\t"+line.trim().split("\t")[3]+"\n";
					}
					new_line+=line+"\n";
				}
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
	    menu = fi.tb.get_menu();
	    for (int i = 0; i < 5; i++) {
	        if (int_menu_num[i] != 0) {
	           int origin_stock = Integer.parseInt(menu[2][i]);
	           int new_stock = origin_stock + int_menu_num[i];
	           menu[2][i] = Integer.toString(new_stock);
	           //재고 정보 update
	           fi.tb.set_menu(menu);
	           fi.write_menu();
	       }
	    }
		
		return;
	}
	
	private boolean attached() {
		int i_table1=Integer.parseInt(table1);
		int i_count=Integer.parseInt(count);
		if(i_table1>=1&&i_table1<=5) {//2인용 좌석인데
			if(i_count>2&&i_count<=4) //3,4명이라면
				return true;
			else
				return false;
		}
		else if(i_table1>=7&&i_table1<=15) {//4인용 좌석인데
			if(i_count>4&&i_count<=8) //인원수가 5~8명이라면
				return true;
			else
				return false;
		}
		else if(i_table1>=17&&i_table1<=19) { //6인용 좌석인데
			if(i_count>7&&i_count<=12) //인원수가 9~12명이라면
				return true;
			else
				return false;
		}
		return false;
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

			String change_date = fi.get_date(change_day);
			fi.read_file(change_date);
			String[][][] db = fi.tb.get_day();
			
			for(int i =0; i<user_inform[change_day].size(); i=i+2) { // 예약은 두시간씩 묶음으로 저장되니, 예약 시작시간만 입력받도록 한다.
				int[] pos = user_inform[change_day].get(i);	// user_inform[change_day]에 있는 정보 값을 하나씩 불러온다
				int table_inform = pos[1];	//만약 붙어있는 좌석이라면 7,8 중 7번째 테이블 값을 가져온다
				int time_inform = pos[0];	//예약 시작 시간
				//[][time_inform][table_infrom]형식
				
				String reserved_time = db[3][time_inform][table_inform];	// 예약된 정보 중 시간을 받아온다.
				if(Integer.parseInt(reserved_time)==Integer.parseInt(time)) { //입력받은 시간과 같다면
					count=db[2][time_inform][table_inform];
					if(attached()) {
						table1=db[0][time_inform][table_inform];
						table2=db[0][time_inform][table_inform+1];
					}
					else {
						table1=db[0][time_inform][table_inform];
					}
					
					name=db[4][time_inform][table_inform];
					phone=db[5][time_inform][table_inform];
					int_menu_num[0]=Integer.parseInt(db[6][time_inform][table_inform]);
					int_menu_num[1]=Integer.parseInt(db[7][time_inform][table_inform]);
					int_menu_num[2]=Integer.parseInt(db[8][time_inform][table_inform]);
					int_menu_num[3]=Integer.parseInt(db[9][time_inform][table_inform]);
					int_menu_num[4]=Integer.parseInt(db[10][time_inform][table_inform]);
				}
					
					
			}//setting date,time, and table
			if(cr.compare_reservation_date(user_inform,date,time)) {	
		
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
