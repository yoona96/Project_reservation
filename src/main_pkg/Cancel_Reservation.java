package main_pkg;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Cancel_Reservation {
	
	private ArrayList<int[]> user_inform[] = new ArrayList[3]; // 0: 당일, 1: 내일, 2: 모레
	private String date;
	private String time;
	
	public void cancel_reservation_main() {
		Reservation_Check RC = new Reservation_Check();
		RC.input_inform("Cancle");
		user_inform = RC.show_reservation_inform("Cancle");
		
		
	}
	
	public void input_reservation_date() {
		LocalDate date_format = LocalDate.now();
		String reserv_date = "";
		String reserv_time = "";
		
		
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
			System.out.println("형식 경고문");
			continue;
		}
		if(input_value[0].matches("[0-9]{4,4}"+"-"+"[0-9]{2,2}"+"-"+"[0-9]{2,2}") || input_value[0].matches("[0-9]{8,8}")) {
			//for the format of reservation date
			reserv_date = input_value[0];
		}else {
			System.out.println("날짜 경고문");
			continue;
		}
		if(input_value[1].matches("[0-9][0-9]") || input_value[1].matches("[0-9][0-9]:00") || input_value[1].matches("[0-9][0-9]��")) {
			//for the format of reservation time
			reserv_time = input_value[1];
		}else {
			System.out.println("시간 경고문");
			continue;
		}
		
		date = reserv_date.replace("-", "");
		time = reserv_time.substring(0, 2);
		
		break;	
	}
		System.out.println(date);
		System.out.println(time);
	}
	
	
	
}
