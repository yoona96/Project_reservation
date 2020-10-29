package main_pkg;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Cancel_Reservation {
	
	private ArrayList<int[]> user_inform[] = new ArrayList[3]; // 0: 당일, 1: 내일, 2: 모레
	private String date;
	private String time;
	private String table_number;
	private String[] user_time_data = new String[2];
	
	public void cancel_reservation_main() {
		Reservation_Check RC = new Reservation_Check();
		user_inform = RC.show_reservation_inform("Cancle");
		user_time_data = input_reservation_date();
		date = user_time_data[0];
		time = user_time_data[1];
		boolean is_exist_reservatrion = compare_reservation_date(user_inform);
		System.out.println(is_exist_reservatrion);
		
		if (is_exist_reservatrion)
			confirm_cancel();
	}

	public String[] input_reservation_date() {
		LocalDate date_format = LocalDate.now();
		String reserv_date = "";
		String reserv_time = "";
		String input_date;
		String input_time;
		
		Scanner scan = new Scanner(System.in);
		System.out.println("취소하려는 예약일자와 시간을 입력해주세요(ex: 20201012	13) : ");
		
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
	
	public boolean compare_reservation_date(ArrayList<int[]>[] reserved_data) {
		File_IO IO = new File_IO();
		
		String today = IO.get_date(0);
		int i_today = Integer.parseInt(today);
		int i_date = Integer.parseInt(date);
		int gab = i_date - i_today;
		
		String date = IO.get_date(gab);
		IO.read_file(date);
		String[][][] db = IO.tb.get_day();
		
		for(int i =0; i<=reserved_data[gab].size()-1; i++) {
			int[] pos = reserved_data[gab].get(i);
			int table_inform = pos[1];
			int time_inform = pos[0];
			String reserved_time = db[3][time_inform][table_inform];
			System.out.println(reserved_time);
			System.out.println(time);
			if(time.equals(reserved_time)) {
				table_number = db[0][time_inform][table_inform];
				return true;
			}
		}
		System.out.println("입력하신 문자열이 올바르지 않습니다. 입력을 확인 후 다시 입력해주세요");
		return false;
				
	}
	
	private void confirm_cancel() {
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
	
	private void file_update() {
		File_IO IO = new File_IO();
		IO.read_file(date);
		System.out.println(date+".txt");
		String temp[][][] = new String[11][11][20];
		
		int i_table_number = Integer.parseInt(table_number);
		i_table_number--;
		
		String table_size = "";
		if(i_table_number<20) table_size = "6";
		if(i_table_number<16) table_size = "4";
		if(i_table_number<6) table_size = "2";
		
		int i_time = Integer.parseInt(time);
		i_time = i_time-10;
		
		
		temp = IO.tb.get_day();
		temp[0][i_time][i_table_number] = table_number;
		temp[1][i_time][i_table_number] = table_size;
		temp[2][i_time][i_table_number] = "0";
		temp[3][i_time][i_table_number] = time;
		for (int i=4;i<11;i++){
          temp[i][i_time][i_table_number] =null;
		}
		IO.tb.set_day(temp);
		IO.write_file(date);

	}
}
