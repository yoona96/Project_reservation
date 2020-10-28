package main_pkg;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Scanner;

import main_pkg.textDB;

public class Reservation {

    private String count, date, day, time;
    private String name;
    private String phone;
    private String st_num0;
    private String st_num1;

	private String[][] menu = new String[5][5];
	private String[] str_menu_num = new String[5];
	private int[] int_menu_num = new int[5];
	private int stock_result_index = 0;
	private int price = 0;
	String[] input_value = new String[0];
	File_IO file = new File_IO();
	textDB db = new textDB();

    File_IO fio = new File_IO();

    /*
     * print the 'today', user can input date, time, number of people
     */
    public void user_input(){

    	LocalDate date_format = LocalDate.now();
    	String today = date_format.toString();

		String reserv_date, reserv_time, reserv_count, sub_time;

    	Scanner scanner = new Scanner(System.in);

    	while(true) {
    		System.out.println("예약 접수일: " + today);
    		System.out.println("예약 희망 일자와, 시간, 그리고 방문하는 인원수를 차례대로 입력하세요.");
    		System.out.print("→");

    		String reservation_input = scanner.nextLine();

			String[] input_value = null;


			//check if format is right
    		if(reservation_input.contains("	") || reservation_input.contains(" ")) {
    			//for the format of input String
    			input_value = reservation_input.trim().split("	");
    		}else {
    			System.out.println("입력하신 문자열이 올바르지 않습니다. <예약 희망일> + <tab> 1개 + <시간> + <tab> 1개 + <인원수>에 알맞은 형식으로 문자열을 입력해주세요!\n");
    			continue;
    		}
    		if(input_value[0].matches("[0-9]{4,4}"+"-"+"[0-9]{2,2}"+"-"+"[0-9]{2,2}") || input_value[0].matches("[0-9]{8,8}")) {
    			//for the format of reservation date
    			reserv_date = input_value[0];
    		}else {
    			System.out.println("입력하신 문자열이 올바르지 않습니다. <예약 희망일> + <tab> 1개 + <시간> + <tab> 1개 + <인원수>에 알맞은 형식으로 문자열을 입력해주세요!\n");
    			continue;
    		}
    		if(input_value[1].matches("[0-9][0-9]") || input_value[1].matches("[0-9][0-9]:00") || input_value[1].matches("[0-9][0-9]시")) {
    			//for the format of reservation time
    			reserv_time = input_value[1];
    		}else {
    			System.out.println("입력하신 문자열이 올바르지 않습니다. <예약 희망일> + <tab> 1개 + <시간> + <tab> 1개 + <인원수>에 알맞은 형식으로 문자열을 입력해주세요!\n");
    			continue;
    		}
    		if(input_value[2].matches("[0-9]{1,2}")) { //for the format of reservation count
    			reserv_count = input_value[2];
    		}else {
    			System.out.println("입력하신 문자열이 올바르지 않습니다. <예약 희망일> + <tab> 1개 + <시간> + <tab> 1개 + <인원수>에 알맞은 형식으로 문자열을 입력해주세요!\n");
    			continue;
    		}

			sub_time = reserv_time.substring(0, 2);

    		if((reserv_date.contentEquals(date_format.plusDays(1).toString()) || reserv_date.replace("-", "").contentEquals(date_format.plusDays(1).toString().replace("-", ""))
    			|| reserv_date.contentEquals(date_format.plusDays(2).toString()) || reserv_date.replace("-", "").contentEquals(date_format.plusDays(2).toString().replace("-", "")))
    			&& (Integer.parseInt(sub_time) >= 10 && Integer.parseInt(sub_time) <= 20) && Integer.parseInt(reserv_count) >=1 && Integer.parseInt(reserv_count) <= 12) {
    			//check if value inputed fits data rule
    			this.date = reserv_date;
    			this.time = sub_time;

    			if(search_table(reserv_date, sub_time, reserv_count).isBlank() == false) {
    				//check if value inputed is available for reservation
        			break;
    			}else {
    				System.out.println("입력하신 시간대에 해당 인원수가 앉을 수 있는 좌석이 존재하지 않습니다. 다른 시간대를 입력해주시기 바랍니다.\n");
    				continue;
    			}
    		}else {
    			System.out.println("입력하신 문자열이 올바르지 않습니다. 예약은 예약접수일 당일을 제외하고 +2일까지 가능하며, 입력 가능한 시간은 10:00 ~ 20:00입니다.\n");
    			continue;
    		}
    	}
    	show_table(reserv_date, sub_time, reserv_count);
    }

    /*
     * search for available table with inputed day, time, count
     */
    private String search_table(String date, String time, String count){
    	fio.read_file(date.replaceAll("-", ""));
    	String[][][]
    			tmp = fio.tb.get_day();

    	int attached_table = 0;

    	StringBuilder available_tables = new StringBuilder();
    	//should split it with space(" ") to check each available table number
    	//for tables attached, format is "(table number)-(table number)"

    	int tmp_count = 1; //index no. of available number of people to use table
    	int tmp_table = 0; //table number
    	int tmp_time = Integer.parseInt(time) - 10;

    	for(tmp_table = 0; tmp_table < 20; tmp_table++) { //check if there are any table available with chosen day,time,count with availability(3rd value)
    		if(tmp[2][tmp_time][tmp_table].equals("0")) { //if number of people using table is 0
    			if(Integer.parseInt(tmp[tmp_count][tmp_time][tmp_table]) >= Integer.parseInt(count)){
    				//compare available number or people to use table with inputed number of people
    				if(Integer.parseInt(count) < 3 && (tmp[tmp_count][tmp_time][tmp_table]).contentEquals("4") || tmp[tmp_count][tmp_time][tmp_table].equals("6")) {
    					//if current number of people is less than 3, cannot use table for 4 or 6 people
    					continue;
    				}else if(Integer.parseInt(count) < 4 && tmp[tmp_count][tmp_time][tmp_table].contentEquals("6")){
    					//if current number of people is less than 4, cannot use table for 6 people
    					continue;
    				}else { //add string in available_tables
    					available_tables.append(tmp_table);
    					available_tables.append(" ");
    				}
    			}else {
    				continue;
    			}
    		}else {
    			continue;
    		}
    	}
    	for(attached_table = 0; attached_table < 20; attached_table += 2) { //tables can be attached
    		if(tmp[2][tmp_time][attached_table].contentEquals("0") && tmp[2][tmp_time][attached_table++].contentEquals("0")) {
				//if attached tables are both empty
    			if(attached_table >= 0 && attached_table < 8) {
    				//table 1~8
    				if(Integer.parseInt(count) > 2 && Integer.parseInt(count) < 5) {
    					//only more than 2, less than 5 people can use attached tables for 4 people
    					available_tables.append(attached_table);
    					available_tables.append("-");
    					available_tables.append(Integer.valueOf(attached_table) + 1);
    					available_tables.append(" ");
    				}else {
    					continue;
    				}
    			}else if (attached_table >= 8 && attached_table <16) {
    				if(Integer.parseInt(count) > 4 && Integer.parseInt(count) < 9) {
        				//only more than 4, less than 9 people can use attached tables for 8 people
    					available_tables.append(attached_table);
    					available_tables.append("-");
    					available_tables.append(Integer.valueOf(attached_table) + 1);
    					available_tables.append(" ");
        			}else {
        				continue;
        			}
    			}else {
    				if(Integer.parseInt(count) > 8) {
    					//only more than 8 people can use attached tables for 12 people
    					available_tables.append(attached_table);
    					available_tables.append("-");
    					available_tables.append(Integer.valueOf(attached_table) + 1);
    					available_tables.append(" ");
    				}else {
    					continue;
    				}
    			}
    		}
    	}
		return available_tables.toString();
    }

	private void show_table(String date, String time, String count) {
		String[] tmp_date_arr = new String[3];
		tmp_date_arr[0] = date.substring(0, 4);
		tmp_date_arr[1] = date.substring(5, 7);
		tmp_date_arr[2] = date.substring(8, 10);
		//split inputed reservation date into yyyy mm dd format
		
		String avail_table = search_table(date, time, count);
		//has no. of available tables

		for(int table_num = 1; table_num <= 20; table_num++) {
			
			if(avail_table.contains(Integer.toString(table_num))) {
				if(table_num < 5) {
					
				}
			}
		}
		
		System.out.println(tmp_date_arr[0] + "년 " + tmp_date_arr[1] + "월 " + tmp_date_arr[2] + "일 " + time + "시 예약 가능 좌석 현황");
		System.out.println("--------------------------------------------------");
		System.out.println("");
	}

	private boolean choose_auto(char c) {

		return false;
	}

	private void choose_table(String st_num) {

	}

	private String auto_table(String count) {
		String auto = "";
		return auto;
	}

	private void input_inform(String name, String phone) {

	}



	public void choose_menu() {
	
		file.read_menu();
		Scanner scan = new Scanner(System.in);
		String patterns0 = "^[占쏙옙-占폫]*";
		String patterns1 = "[0-9]";
		String patterns2 = "[a-zA-Z]";
		String patterns3 = "\t";



		menu = file.tb.get_menu();
		while (true) {
			// 화占쏙옙 占쏙옙占�
			System.out.println("[占쌨댐옙]\t[占쏙옙占쏙옙]\t[占쌍뱄옙 占쏙옙占쏙옙 占시곤옙]");
			for (int i = 0; i < menu.length; i++) {
				System.out.print(menu[0][i] + "\t\\" + menu[1][i] + "\t");
				if (menu[3][i] != null) {// all占쏙옙 占싣닌곤옙占�
					String[] menu_time = menu[3][i].split("-");
					System.out.println(menu_time[0] + ":00 ~ " + menu_time[1] + ":00");
				} else {
					System.out.println("all");
				}
			}
			System.out.println();
			for (int i = 0; i < menu.length - 1; i++) {
				System.out.print(menu[0][i] + ", ");
			}
			System.out.println(menu[0][menu.length - 1] + "占쏙옙 占쌍뱄옙 占쏙옙占쏙옙占쏙옙 占쏙옙占십댐옙占� 占쌉뤄옙占싹쇽옙占쏙옙(ex.\t2\t3\t0\t0\t0)");
			System.out.print("占쏙옙");

			String temp_num = scan.nextLine();

			if (temp_num.contains("\t") || temp_num.contains("")) {
				str_menu_num = temp_num.trim().split("\t");
			}


			if(temp_num.matches(patterns0+patterns3+patterns1+patterns3+patterns1+patterns3+patterns1+patterns3+patterns1+patterns3+patterns1+"|"+patterns3+patterns3+patterns1+patterns3+patterns1+patterns3+patterns1+patterns3+patterns1+patterns3+patterns1)){
				System.out.println("占쌍뱄옙占쌉뤄옙 占쏙옙占식울옙 占쏙옙占쏙옙占쏙옙 占쌍쏙옙占싹댐옙. 占쌉뤄옙 占쏙옙占쏙옙占� (ex.\t2\t3\t0\t0\t0) 占쏙옙占쏙옙占쌉니댐옙 ");
				continue;
			}


			for (int i = 0; i < menu.length; i++) {
				int_menu_num[i] = Integer.parseInt(str_menu_num[i]);
			}
			// 占실뱄옙 占쏙옙칙 占쏙옙占쏙옙占�
			// 1. 占쌍뱄옙 占쌀곤옙占쏙옙占쏙옙 占시곤옙占쏙옙占쏙옙 占쏙옙占�
			for (int i = 0; i < menu.length; i++) {
				if (int_menu_num[i] != 0) {// 占쌍뱄옙占쏙옙 占쌨댐옙占쏙옙
					if (!menu_time_check(i)) {// 占쌍뱄옙 占쏙옙占심시곤옙占쏙옙 占쏙옙占쏘났占쌕몌옙 占쏙옙占쏙옙 占쏙옙占쏙옙 占쏙옙占�
						System.out.println(menu[0][i] + "占쏙옙(占쏙옙) 占쌍뱄옙占쏙옙 占쏙옙 占쏙옙占쏙옙占싹댐옙. 占쌍뱄옙 占쏙옙占쏙옙 占시곤옙占쎈를 확占쏙옙占쏙옙 占쌍쇽옙占쏙옙.");
						continue;
					}
				}
			}
			// 2. 占쏙옙占� 占쏙옙占쏙옙占쌀곤옙占�
			int[] stock_temp = menu_stock_check();
			if (stock_result_index != 0) {
				for (int i = 0; i < stock_result_index; i++) {
					System.out.println(menu[0][stock_temp[i]] + "占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쌌니댐옙(占쏙옙占쏙옙 占쏙옙占쏙옙:"
							+ menu[2][stock_temp[i]] + "占쏙옙)");
				}
				continue;
			}
			break;
		}
		menu_confirm();
	}

	private boolean menu_time_check(int index) {

		if(menu[3][index]==null) {return true;}
 		int this_menu_time = Integer.parseInt(menu[3][index]);
		if ((Integer.parseInt(this.time) < this_menu_time) || (this_menu_time + 2 < Integer.parseInt(this.time + 2))) {// 占쌍뱄옙占쏙옙占심시곤옙																						// 占쏙옙占쏘남
			return false;
		}

		return true;
	}

	private int[] menu_stock_check() {
		int[] result = new int[menu.length];
		for (int i = 0; i < menu.length; i++) {
			if(int_menu_num[i]!=0) {
				int this_menu_stock = Integer.parseInt(menu[2][i]);
				if (int_menu_num[i] > this_menu_stock) {// 占쌍뱄옙占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占� 占쏙옙占쏙옙占쏙옙
					result[stock_result_index++] = i;
				} // 占쏙옙占� 占쏙옙占쏙옙占쏙옙 占쏙옙占� 占쌨댐옙 占쏙옙占쏙옙占쌔쇽옙 占쏙옙환
			}
		}
		return result;
	}

	private void menu_confirm() {
		Scanner scan = new Scanner(System.in);

		System.out.println("[占쌨댐옙]\t[占쏙옙占쏙옙]\t[占쌍뱄옙 占쏙옙占쏙옙]");
		for (int i = 0; i < menu.length; i++) {
			System.out.println(menu[0][i] + "\t\\" + menu[1][i] + "\t" + str_menu_num[i]);
			price += Integer.parseInt(menu[1][i]) * int_menu_num[i];
		}
		System.out.println();

		System.out.println("占쏙옙占쏙옙 占쏙옙占쏙옙 占쌥억옙: " + price);
		System.out.print("占쌍뱄옙 占쏙옙占쏙옙占쏙옙 확占쏙옙占싹곤옙 占쌍뱄옙占쏙옙 占싹뤄옙占싹시겠쏙옙占싹깍옙?(y/n) :");
		String menu_confirm = scan.next();

		if (menu_confirm.contains(" ") || menu_confirm.contains("")) {
			input_value = menu_confirm.trim().split(" ");
		}

		if (input_value[0] == "y") {
			String str = "";
			for (int i = 0; i < menu.length; i++) {
				if (int_menu_num[i] != 0) {
					str += menu[0][i] + " ";
				}
			}
			System.out.println("占쏙옙(占쏙옙) 占쌍뱄옙占쌌니댐옙.");
			reservation_confirm();
		} else {
			choose_menu();
		}
	}

	private void reservation_confirm() {
		Scanner scan = new Scanner(System.in);
		System.out.println("占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙 확占쏙옙占싹겠쏙옙占싹댐옙.\n");
		System.out.println("占쏙옙占쏙옙占쏙옙 占싱몌옙: " + this.name);
		System.out.println("占쏙옙화占쏙옙호: " + this.phone);
		System.out.println("占쏙옙占쏙옙 占시곤옙: ");
		System.out.println("占싸울옙 占쏙옙: ");
		System.out.println("占쏙옙占쏙옙 占승쇽옙: ");
		System.out.print("占쌍뱄옙 占쌨댐옙: ");
		for (int i = 0; i < menu.length; i++) {
			if (int_menu_num[i] != 0) {
				System.out.print(menu[0][i] + ": " + menu[2][i] + "  ");
			}
		}
		System.out.println();

		System.out.println("占쏙옙占쏙옙 占쏙옙占쏙옙 占쌥억옙: \\" + price + "\n");
		System.out.print("占쏙옙占쏙옙占쏙옙 확占쏙옙占싹시겠쏙옙占싹깍옙?(y/n): ");
		String reservation_confirm = scan.next();
		if (reservation_confirm.contains(" ") || reservation_confirm.contains("")) {
			input_value = reservation_confirm.trim().split(" ");
		}

		if (input_value[0] == "y") {
			System.out.println("占쏙옙占쏙옙占쏙옙 占싹뤄옙퓸占쏙옙占쏙옙求占�.");
			out_reservation_data();
		} else {
			reservation_cancle_confirm();
		}
	}

	private void reservation_cancle_confirm() {
		Scanner scan = new Scanner(System.in);
		System.out.println("占쏙옙占쏙옙 占쏙옙努占�, 占쏙옙占� 占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占싯니댐옙.");
		System.out.println("占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙絿챨黴占쏙옙歐占�?(y/n): ");

		String reservation_cancle = scan.next();
		if (reservation_cancle.contains(" ") || reservation_cancle.contains("")) {
			input_value = reservation_cancle.trim().split(" ");
		}

		if (input_value[0] == "y") {
			System.out.println("占쏙옙占쏙옙占쏙옙 占쏙옙撚퓸占쏙옙占쏙옙求占�.");
			// 占쏙옙占쏙옙占쏙옙占쏙옙트占쏙옙, main占쏙옙占쏙옙 占쏙옙占싣곤옙占쏙옙
		} else {
			reservation_confirm();
		}
	}

	private void out_reservation_data() {
		file.write_file(this.date, Integer.parseInt(this.time),5);
		//占쌨댐옙占쏙옙占싹울옙占쏙옙 占쌨댐옙占싱몌옙占쏙옙 占쌔댐옙占싹댐옙 占쌨댐옙占쏙옙 占쌨댐옙 占쏙옙占� 占쌍뱄옙 占쏙옙占쏙옙占쏙옙큼 占쏙옙占쏙옙
		for(int i=0;i<menu.length;i++) {
			if(int_menu_num[i]!=0) {
				int origin_stock = Integer.parseInt(menu[2][i]);
				int new_stock = origin_stock - int_menu_num[i];
				menu[2][i] = Integer.toString(new_stock);
			}
			db.set_menu(menu);
			//file.write_menu(i);
		}
	}
}
