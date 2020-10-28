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
    		System.out.println("\n예약 접수일: " + today);
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
    			System.out.println("입력하신 문자열이 올바르지 않습니다. <예약 희망일은> yyyy-mm-dd 또는 yyyymmdd의 형식으로 입력해주세요!\n");
    			continue;
    		}
    		if(input_value[1].matches("[0-9][0-9]") || input_value[1].matches("[0-9][0-9]:00") || input_value[1].matches("[0-9][0-9]시")) {
    			//for the format of reservation time
    			reserv_time = input_value[1];
    		}else {
    			System.out.println("입력하신 문자열이 올바르지 않습니다. <시간>은 hh 또는 hh:00 또는 hh시의 형식으로 입력해주세요!\n");
    			continue;
    		}
    		if(Integer.parseInt(input_value[2]) >=1 && Integer.parseInt(input_value[2]) <= 12) { //for the format of reservation count
    			reserv_count = input_value[2];
    		}else {
    			System.out.println("입력하신 문자열이 올바르지 않습니다. 예약 가능 인원은 최소 1명, 최대 12명입니다!\n");
    			continue;
    		}

			sub_time = reserv_time.substring(0, 2);

    		if((reserv_date.contentEquals(date_format.plusDays(1).toString()) || reserv_date.replace("-", "").contentEquals(date_format.plusDays(1).toString().replace("-", ""))
    			|| reserv_date.contentEquals(date_format.plusDays(2).toString()) || reserv_date.replace("-", "").contentEquals(date_format.plusDays(2).toString().replace("-", "")))
    			&& (Integer.parseInt(sub_time) >= 10 && Integer.parseInt(sub_time) <= 20)) {
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
    	scanner.close();
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
    	int tmp_table; //table number
    	int tmp_time = Integer.parseInt(time) - 10;

    	for(tmp_table = 0; tmp_table < 20; tmp_table++) { //check if there are any table available with chosen day,time,count with availability(3rd value)
    		if(tmp[2][tmp_time][tmp_table].equals("0")) { //if number of people using table is 0
    			if(Integer.parseInt(tmp[tmp_count][tmp_time][tmp_table]) >= Integer.parseInt(count)){
    				//compare available number of people to use table with inputed number of people
    				if(Integer.parseInt(count) < 3 && ((tmp[tmp_count][tmp_time][tmp_table]).equals("4") || tmp[tmp_count][tmp_time][tmp_table].equals("6"))) {
    					//if current number of people is less than 3, cannot use table for 4 or 6 people
    					continue;
    				}else if((Integer.parseInt(count) < 4 || Integer.parseInt(count) > 6) && tmp[tmp_count][tmp_time][tmp_table].equals("6")){
    					//if current number of people is less than 4, and more than 6, cannot use table for 6 people
    					continue;
    				}else { //add string in available_tables
    					available_tables.append(tmp_table + 1);
    					available_tables.append(" ");
    				}
    			}else {
    				continue;
    			}
    		}else {
    			continue;
    		}
    	}
    	for(attached_table = 0; attached_table < 20; attached_table++) { //tables can be attached
    		if(tmp[2][tmp_time][attached_table].contentEquals("0") && tmp[2][tmp_time][attached_table++].contentEquals("0")) {
				//if attached tables are both empty
    			if(attached_table >= 0 && attached_table < 6) {
    				//table 1~6
    				if(Integer.parseInt(count) > 2 && Integer.parseInt(count) < 5) {
    					//only more than 2, less than 5 people can use attached tables for 4 people
    					available_tables.append(attached_table);
    					available_tables.append("-");
    					available_tables.append(Integer.valueOf(attached_table) + 1);
    					available_tables.append(" ");
    				}else {
    					continue;
    				}
    			}else if (attached_table >= 6 && attached_table <16) {
    				//table 7~16
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
		String tmp_date = date.replaceAll("-", "");
		//replace all - from date, make it into format of yyyymmdd
		tmp_date_arr[0] = tmp_date.substring(0, 4);
		tmp_date_arr[1] = tmp_date.substring(4, 6);
		tmp_date_arr[2] = tmp_date.substring(6);
		//split inputed reservation date into yyyy mm dd format
		
		String avail_table = search_table(date, time, count);
		//has no. of available tables. same as available_tables in search table function
		String[] every_table = null;
		every_table = avail_table.split(" ");
		//every available tables are in this array
		String[] every_attached_table = new String[5];
		//every available attached tables will be in this array
		
		for(int i = 0; i < every_table.length; i++) {
			System.out.print(every_table[i] + "\t");
		}
		System.out.println("");
		
		for(int i = 0; i < every_table.length; i++) {
			if(every_table[i].matches("[0-9]" + "-" + "[0-9]{1,2}")) {
				int j = 0;
				every_attached_table[j] = every_table[i].substring(0,1);
				every_attached_table[j+1] = every_table[i].substring(2);
				every_table[i] = "";
				System.out.print(every_attached_table[j] + "\t");
				System.out.print(every_attached_table[j+1] + "\t");
				j += 2;
			}else if(every_table[i].matches("[0-9][0-9]"+ "-" + "[0-9][0-9]")) {
				int j = 0;
				every_attached_table[j] = every_table[i].substring(0,2);
				every_attached_table[j+1] = every_table[i].substring(3);
				every_table[i] = "";
				System.out.print(every_attached_table[j] + "t\t");
				System.out.print(every_attached_table[j+1] + "t\t");
				j += 2;
			}
		}
		//replace attached tables from every_table array
		//now every 'not attached' tables are in every_table array
		//every 'attached' tables are in every_attached_table array
		
		for(int i = 0; i < every_table.length; i++) {
			System.out.print(every_table[i] + "\t");
		}
		System.out.println("");
		
		String[] table_view = new String[20];
		for(int i = 0; i < 20; i++) {
			table_view[i] = "0";
		}
		
		//rows of current table chart
		
		Scanner choose_table_input = new Scanner(System.in);

		while(true) {
			
			System.out.println("\n" + tmp_date_arr[0] + "년 " + tmp_date_arr[1] + "월 " + tmp_date_arr[2] + "일 " + time + "시 예약 가능 좌석 현황");
			System.out.println("--------------------------------------------------");
			
			if(every_table != null) {
				for(int i = 0; i < every_table.length; i++) {
					for(int j = 0; j < 20; j++) {
						if(!table_view[j].contains(".")) {
							if(!every_table[i].equals(Integer.toString(j + 1))) {
								table_view[j] = Integer.toString(j + 1);
							}else {
								table_view[j] = Integer.toString(j + 1) + ".";
							}
						}
						System.out.print(table_view[j] + "\t");
					}
				}
			}
			System.out.println("");
			if(every_attached_table != null) {
				for(int k = 0; k < every_attached_table.length; k++) {
					for(int l = 0; l < 20; l++) {
						if(table_view[l].contains(".")) {
							System.out.println("c");
							continue;
						}else {
							System.out.println("a");
							if(!every_attached_table[k].equals(Integer.toString(l + 1))) {
								System.out.println("b");
								table_view[l] = "//";
							}else {
								table_view[l] = Integer.toString(l + 1) + ".";
							}
						}
						System.out.println(table_view[l] + "`");
					}
				}
			}
			
			System.out.println("--------------------------------------------------\n");
			
			System.out.println("[01] ~ [06] : 2인용 좌석");
			System.out.println("[07] ~ [16] : 4인용 좌석");
			System.out.println("[17] ~ [20] : 6인용 좌석");
			System.out.println("[//] : 예약 불가능한 좌석");
			System.out.println("붙어있는 좌석 : (1,2) (3,4) (5,6) (7,8) (9,10) (11,12) (13,14) (15,16) (17,18) (19,20)\n");
			System.out.println("좌석을 자동으로 할당 받으시겠습니까? (y/n) : ");
			
			String table_choice = choose_table_input.next();
			
			if(table_choice.matches("[Yy]")) {
				choose_auto('y');
				break;
			}else if(table_choice.matches("[Nn]")) {
				choose_table("");
				break;
			}else {
				System.out.println("입력은 y 혹은 n만 가능합니다. 다시 입력해주세요.");
				continue;
			}
		}
		choose_table_input.close();
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
