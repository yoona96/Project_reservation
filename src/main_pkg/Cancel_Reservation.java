package main_pkg;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Cancel_Reservation {
	
	private ArrayList<int[]> user_inform[] = new ArrayList[3]; // 0: 당일, 1: 내일, 2: 모레
	private String date;	// user_input_date
	private String time;	// user_input_time
	private String table_number;	// table number for changing reservation
	private String[] user_time_data = new String[2];	// Temporary data for return values in input_reservation_date
	
	public void cancel_reservation_main() {	
		Reservation_Check RC = new Reservation_Check();	
		user_inform = RC.show_reservation_inform("Cancel");	// Get Reserved information
		user_time_data = input_reservation_date();	// Get user_input information
		date = user_time_data[0];	
		time = user_time_data[1];
		boolean is_exist_reservatrion = compare_reservation_date(user_inform,date,time); // Checked for match between Reserved information and user_input information		
		
		if (is_exist_reservatrion)
			confirm_cancel();
	}

	public String[] input_reservation_date() {	//	사용자로부터 예약을 변경하려는 날짜와 시간을 입력받아서 1차원 배열에 저장한다.
		LocalDate date_format = LocalDate.now();
		String reserv_date = "";
		String reserv_time = "";
		String input_date;
		String input_time;
		
		Scanner scan = new Scanner(System.in);
		System.out.print("예약일자와 시간을 입력해주세요(ex: 20201012	13) : ");
		
		while(true) {
		String reservation_input = scan.nextLine();
		String[] input_value = null;
		
		
		//check if format is right
		if(reservation_input.contains("	") || reservation_input.contains(" ")) {
			//for the format of input String
			input_value = reservation_input.trim().split("	");
		}else {
			System.out.println("입력하신 문자열이 올바르지 않습니다. 입력을 확인 후 다시 입력해주세요");
			continue;
		}
		if(input_value[0].matches("[0-9]{4,4}"+"-"+"[0-9]{2,2}"+"-"+"[0-9]{2,2}") || input_value[0].matches("[0-9]{8,8}")) {
			//for the format of reservation date
			reserv_date = input_value[0];
		}else {
			System.out.println("입력하신 문자열이 올바르지 않습니다. 입력을 확인 후 다시 입력해주세요");
			continue;
		}
		if(input_value[1].matches("[0-9][0-9]") || input_value[1].matches("[0-9][0-9]:00") || input_value[1].matches("[0-9][0-9]��")) {
			//for the format of reservation time
			reserv_time = input_value[1];
		}else {
			System.out.println("입력하신 문자열이 올바르지 않습니다. 입력을 확인 후 다시 입력해주세요");
			continue;
		}
		
		input_date = reserv_date.replace("-", "");
		input_time = reserv_time.substring(0, 2);
		break;	
	}
		String[] input_data = new String[2];
		input_data[0] = input_date;
		input_data[1] = input_time;
		return input_data;
	}
	
	public boolean compare_reservation_date(ArrayList<int[]>[] reserved_data,String input_date,String input_time) {	
		File_IO IO = new File_IO();
		
		String today = IO.get_date(0);
		String tomorrow= IO.get_date(1);
		String after_tomorrow=IO.get_date(2);
		int i_today = Integer.parseInt(today);
		int i_tomorrow=Integer.parseInt(tomorrow);
		int i_after_tomorrow=Integer.parseInt(after_tomorrow);
		int i_date = Integer.parseInt(input_date);
		int gab=0;
		if(i_date==i_today)
			gab=0;
		else if(i_date==i_tomorrow)
			gab=1;
		else if(i_date==i_after_tomorrow)
			gab=2;
			
		String date = IO.get_date(gab);
		IO.read_file(date);
		String[][][] db = IO.tb.get_day();
		
		for(int i =0; i<=reserved_data[gab].size()-1; i=i+2) { // 예약은 두시간씩 묶음으로 저장되니, 예약 시작시간만 입력받도록 한다.
			int[] pos = reserved_data[gab].get(i);	// i번째 예약정보를 담아온다.
			int table_inform = pos[1];	// i번쨰의 예약정보의 table 번호를(3차원 배열에서 2번째 항에 해당) 저장한다.
			int time_inform = pos[0];	//	i번째 예약정보의 시간을(3차원 배열에서 3번째 항에 해당) 저장한다.
			String reserved_time = db[3][time_inform][table_inform];	// 예약된 정보 중 시간을 받아온다.
			if(input_time.equals(reserved_time)) {	// 만약 사용자가 입력한 시간이 예약된 정보 중에 존재한다면,
				table_number = db[0][time_inform][table_inform];	// 헤당 예약 정보의 좌석 번호를 저장한다.
				return true;	
			}
		}
		System.out.println("예약정보가 존재하지 않습니다. 정확한 예약자의 이름과 전화번호를 입력해주세요");
		return false;
				
	}
	
	private void confirm_cancel() {	//	예약 취소를 확정짓는 메소드
		Scanner scan = new Scanner(System.in);
		System.out.print("정말 예약을 취소하시겠습니까? (y/n): ");
		while(true) {
			String reservation_cancel = scan.next();
	        String[] yorn_value = new String[0];
	        
	        if (reservation_cancel.contains(" ") || reservation_cancel.contains("")) {
	        	yorn_value = reservation_cancel.trim().split(" ");
	        }
	
	        if (yorn_value[0].equals("y")) {
	        	file_update();
	        	System.out.println("해당 예약이 취소되었습니다.");
	        	break;
	        } else if (yorn_value[0].equals("n")) {
	        	break;
	           // recall main prompt
	        } else {
	        	System.out.println("입력은 y 혹은 n만 가능합니다. 다시 입력해주세요.");
	        	continue;
	        }
		}
	}
	
	private void file_update() {	// 선택된 예약을 취소하고 해당 내용을 데이터 파일에서 삭제한다. 
		File_IO IO = new File_IO();
		IO.read_file(date);
		String temp[][][] = new String[11][11][20];
		temp = IO.tb.get_day();
		int int_menu_num[]=new int[5];
		
		int i_table_number = Integer.parseInt(table_number);
		i_table_number--;
		
		String table_size = "";
		if(i_table_number<20) table_size = "6";
		if(i_table_number<16) table_size = "4";
		if(i_table_number<6) table_size = "2";
		
		int i_time = Integer.parseInt(time);
		i_time = i_time-10;
		
		String count=temp[2][i_time][i_table_number];
		for(int i=0;i<5;i++) {
			int_menu_num[i]=Integer.parseInt(temp[6+i][i_time][i_table_number]);
		}
		
		for(int i=0; i<2; i++) {
			temp[0][i_time+i][i_table_number] = table_number;
			temp[1][i_time+i][i_table_number] = table_size;
			temp[2][i_time+i][i_table_number] = "0";
			temp[3][i_time+i][i_table_number] = time;
			for (int j=4;j<11;j++){
				temp[j][i_time+i][i_table_number] =null;
			}
		}
		if(attached(count)) {
			for(int i=0; i<2; i++) {
				temp[0][i_time+i][i_table_number+1] = table_number;
				temp[1][i_time+i][i_table_number+1] = table_size;
				temp[2][i_time+i][i_table_number+1] = "0";
				temp[3][i_time+i][i_table_number+1] = time;
				for (int j=4;j<11;j++){
		          temp[j][i_time+i][i_table_number+1] =null;
				}
			}
		}
		IO.tb.set_day(temp);
		IO.write_file(date);
		
		File_IO IO2=new File_IO();
		IO2.read_menu();
		String[][] menu = new String[4][5]; //menu file array
	    menu = IO2.tb.get_menu();
	    for (int i = 0; i < 5; i++) {
	        if (int_menu_num[i] != 0) {
	           int origin_stock = Integer.parseInt(menu[2][i]);
	           int new_stock = origin_stock + int_menu_num[i];
	           menu[2][i] = Integer.toString(new_stock);
	           //재고 정보 update
	           IO2.tb.set_menu(menu);
	           IO2.write_menu();
	       }
	    }

	}
	private boolean attached(String count) {
		int i_table1=Integer.parseInt(table_number);
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
}
