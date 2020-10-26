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

		String reserv_date, reserv_time, reserv_count;

    	Scanner scanner = new Scanner(System.in);

    	while(true) {
    		System.out.println("���� ������: " + today);
    		System.out.println("���� ��� ���ڿ� �ð�, �׸��� �湮�ϴ� �ο����� ���ʴ�� �Է��ϼ���.");
    		System.out.print("��");

    		String reservation_input = scanner.nextLine();

			String[] input_value = null;


			//check if format is right
    		if(reservation_input.contains("	") || reservation_input.contains(" ")) {
    			//for the format of input String
    			input_value = reservation_input.trim().split("	");
    		}else {
    			System.out.println("�Է��Ͻ� ���ڿ��� �ùٸ��� �ʽ��ϴ�. <���� �����> + <tab> 1�� + <�ð�> + <tab> 1�� + <�ο���>�� �˸��� �������� ���ڿ��� �Է����ּ���!\n");
    			continue;
    		}
    		if(input_value[0].matches("[0-9]{4,4}"+"-"+"[0-9]{2,2}"+"-"+"[0-9]{2,2}") || input_value[0].matches("[0-9]{8,8}")) {
    			//for the format of reservation date
    			reserv_date = input_value[0];
    		}else {
    			System.out.println("�Է��Ͻ� ���ڿ��� �ùٸ��� �ʽ��ϴ�. <���� �����> + <tab> 1�� + <�ð�> + <tab> 1�� + <�ο���>�� �˸��� �������� ���ڿ��� �Է����ּ���!\n");
    			continue;
    		}
    		if(input_value[1].matches("[0-9][0-9]") || input_value[1].matches("[0-9][0-9]:00") || input_value[1].matches("[0-9][0-9]��")) {
    			//for the format of reservation time
    			reserv_time = input_value[1];
    		}else {
    			System.out.println("�Է��Ͻ� ���ڿ��� �ùٸ��� �ʽ��ϴ�. <���� �����> + <tab> 1�� + <�ð�> + <tab> 1�� + <�ο���>�� �˸��� �������� ���ڿ��� �Է����ּ���!\n");
    			continue;
    		}
    		if(input_value[2].matches("[0-9]{1,2}")) { //for the format of reservation count
    			reserv_count = input_value[2];
    		}else {
    			System.out.println("�Է��Ͻ� ���ڿ��� �ùٸ��� �ʽ��ϴ�. <���� �����> + <tab> 1�� + <�ð�> + <tab> 1�� + <�ο���>�� �˸��� �������� ���ڿ��� �Է����ּ���!\n");
    			continue;
    		}

			String sub_time = reserv_time.substring(0, 2);

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
    				System.out.println("�Է��Ͻ� �ð��뿡 �ش� �ο����� ���� �� �ִ� �¼��� �������� �ʽ��ϴ�. �ٸ� �ð��븦 �Է����ֽñ� �ٶ��ϴ�.\n");
    				continue;
    			}
    		}else {
    			System.out.println("�Է��Ͻ� ���ڿ��� �ùٸ��� �ʽ��ϴ�.������ ���������� ������ �����ϰ� +2�ϱ��� �����ϸ�, �Է� ������ �ð��� 10:00 ~ 20:00�Դϴ�.\n");
    			continue;
    		}
    	}

    	show_table(reserv_date, reserv_time, reserv_count);
    }

    /*
     * search for available table with inputed day, time, count
     */
    private String search_table(String date, String time, String count){
    	fio.read_file(date.replaceAll("-", ""));
    	String[][][] tmp = fio.tb.get_day();

    	int table1 = 0, table2 = table1 + 1;

    	StringBuilder available_tables = new StringBuilder();
    	//should split it with space(" ") to check each available table number
    	//for tables attached, format is "(table number)-(table number)"

    	int tmp_count = 1; //index no. of available number of people to use table
    	int tmp_table = 0; //table number
    	int tmp_time = Integer.parseInt(time) - 10;

    	for(tmp_table = 0; tmp_table < 20; tmp_table++) { //check if there are any table available with chosen day,time,count with availability(3rd value)
    		System.out.println("a" + tmp_table);
    		if(tmp[2][tmp_time][tmp_table].equals("0")) { //if number of people using table is 0
    			System.out.println("b" + tmp_table);
    			if(Integer.parseInt(tmp[tmp_count][tmp_time][tmp_table]) >= Integer.parseInt(count)){
    				//compare available number or people to use table with inputed number of people
    				System.out.println("c" + tmp_table);
    				if(Integer.parseInt(count) < 3 && (tmp[tmp_count][tmp_time][tmp_table]).contentEquals("4") || tmp[tmp_count][tmp_time][tmp_table].equals("6")) {
    					//if current number of people is less than 3, cannot use table for 4 or 6 people
    					System.out.println("d" + tmp_table);
    					continue;
    				}else if(Integer.parseInt(count) < 4 && tmp[tmp_count][tmp_time][tmp_table].contentEquals("6")){
    					//if current number of people is less than 4, cannot use table for 6 people
    					System.out.println("e" + tmp_table);
    					continue;
    				}else { //add string in available_tables
    					System.out.println("f" + tmp_table);
    					available_tables.append(tmp_table);
    					available_tables.append(" ");
    				}
    			}else {
    				System.out.println("g" + tmp_table);
    				continue;
    			}
    		}else {
    			System.out.println("h" + tmp_table);
    			continue;
    		}
    	}
    	for(table1 = 0; table1 < 20; table1 += 2) { //tables can be attached
    		if(tmp[2][tmp_time][table1].contentEquals("0") && tmp[2][tmp_time][table2].contentEquals("0")) {
				//if attached tables are both empty
    			if(table1 >= 0 && table1 < 8) {
    				//table 1~8
    				if(Integer.parseInt(count) > 2 && Integer.parseInt(count) < 5) {
    					//only more than 2, less than 5 people can use attached tables for 4 people
    					available_tables.append(table1);
    					available_tables.append("-");
    					available_tables.append(table2);
    					available_tables.append(" ");
    				}else {
    					continue;
    				}
    			}else if (table1 >= 8 && table1 <16) {
    				if(Integer.parseInt(count) > 4 && Integer.parseInt(count) < 9) {
        				//only more than 4, less than 9 people can use attached tables for 8 people
    					available_tables.append(table1);
    					available_tables.append("-");
    					available_tables.append(table2);
    					available_tables.append(" ");
        			}else {
        				continue;
        			}
    			}else {
    				if(Integer.parseInt(count) > 8) {
    					available_tables.append(table1);
    					available_tables.append("-");
    					available_tables.append(table2);
    					available_tables.append(" ");
    				}else {
    					continue;
    				}
    			}
    		}
    	}
		return available_tables.toString();
    }

	private void show_table(String day, String time, String count) {

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
		String patterns0 = "^[��-�R]*";
		String patterns1 = "[0-9]";
		String patterns2 = "[a-zA-Z]";
		String patterns3 = "\t";



		menu = file.tb.get_menu();
		while (true) {
			// ȭ�� ���
			System.out.println("[�޴�]\t[����]\t[�ֹ� ���� �ð�]");
			for (int i = 0; i < menu.length; i++) {
				System.out.print(menu[0][i] + "\t\\" + menu[1][i] + "\t");
				if (menu[3][i] != null) {// all�� �ƴѰ��
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
			System.out.println(menu[0][menu.length - 1] + "�� �ֹ� ������ ���ʴ�� �Է��ϼ���(ex.\t2\t3\t0\t0\t0)");
			System.out.print("��");

			String temp_num = scan.nextLine();

			if (temp_num.contains("\t") || temp_num.contains("")) {
				str_menu_num = temp_num.trim().split("\t");
			}


			if(temp_num.matches(patterns0+patterns3+patterns1+patterns3+patterns1+patterns3+patterns1+patterns3+patterns1+patterns3+patterns1+"|"+patterns3+patterns3+patterns1+patterns3+patterns1+patterns3+patterns1+patterns3+patterns1+patterns3+patterns1)){
				System.out.println("�ֹ��Է� ���Ŀ� ������ �ֽ��ϴ�. �Է� ����� (ex.\t2\t3\t0\t0\t0) �����Դϴ� ");
				continue;
			}


			for (int i = 0; i < menu.length; i++) {
				int_menu_num[i] = Integer.parseInt(str_menu_num[i]);
			}
			// �ǹ� ��Ģ �����
			// 1. �ֹ� �Ұ����� �ð����� ���
			for (int i = 0; i < menu.length; i++) {
				if (int_menu_num[i] != 0) {// �ֹ��� �޴���
					if (!menu_time_check(i)) {// �ֹ� ���ɽð��� ����ٸ� ���� ���� ���
						System.out.println(menu[0][i] + "��(��) �ֹ��� �� �����ϴ�. �ֹ� ���� �ð��븦 Ȯ���� �ּ���.");
						continue;
					}
				}
			}
			// 2. ��� �����Ұ��
			int[] stock_temp = menu_stock_check();
			if (stock_result_index != 0) {
				for (int i = 0; i < stock_result_index; i++) {
					System.out.println(menu[0][stock_temp[i]] + "�� ������ �����մϴ�(���� ����:"
							+ menu[2][stock_temp[i]] + "��)");
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
		if ((Integer.parseInt(this.time) < this_menu_time) || (this_menu_time + 2 < Integer.parseInt(this.time + 2))) {// �ֹ����ɽð�																						// ���
			return false;
		}

		return true;
	}

	private int[] menu_stock_check() {
		int[] result = new int[menu.length];
		for (int i = 0; i < menu.length; i++) {
			if(int_menu_num[i]!=0) {
				int this_menu_stock = Integer.parseInt(menu[2][i]);
				if (int_menu_num[i] > this_menu_stock) {// �ֹ������� ����� ������
					result[stock_result_index++] = i;
				} // ��� ������ ��� �޴� �����ؼ� ��ȯ
			}
		}
		return result;
	}

	private void menu_confirm() {
		Scanner scan = new Scanner(System.in);

		System.out.println("[�޴�]\t[����]\t[�ֹ� ����]");
		for (int i = 0; i < menu.length; i++) {
			System.out.println(menu[0][i] + "\t\\" + menu[1][i] + "\t" + str_menu_num[i]);
			price += Integer.parseInt(menu[1][i]) * int_menu_num[i];
		}
		System.out.println();

		System.out.println("���� ���� �ݾ�: " + price);
		System.out.print("�ֹ� ������ Ȯ���ϰ� �ֹ��� �Ϸ��Ͻðڽ��ϱ�?(y/n) :");
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
			System.out.println("��(��) �ֹ��մϴ�.");
			reservation_confirm();
		} else {
			choose_menu();
		}
	}

	private void reservation_confirm() {
		Scanner scan = new Scanner(System.in);
		System.out.println("���� ������ Ȯ���ϰڽ��ϴ�.\n");
		System.out.println("������ �̸�: " + this.name);
		System.out.println("��ȭ��ȣ: " + this.phone);
		System.out.println("���� �ð�: ");
		System.out.println("�ο� ��: ");
		System.out.println("���� �¼�: ");
		System.out.print("�ֹ� �޴�: ");
		for (int i = 0; i < menu.length; i++) {
			if (int_menu_num[i] != 0) {
				System.out.print(menu[0][i] + ": " + menu[2][i] + "  ");
			}
		}
		System.out.println();

		System.out.println("���� ���� �ݾ�: \\" + price + "\n");
		System.out.print("������ Ȯ���Ͻðڽ��ϱ�?(y/n): ");
		String reservation_confirm = scan.next();
		if (reservation_confirm.contains(" ") || reservation_confirm.contains("")) {
			input_value = reservation_confirm.trim().split(" ");
		}

		if (input_value[0] == "y") {
			System.out.println("������ �Ϸ�Ǿ����ϴ�.");
			out_reservation_data();
		} else {
			reservation_cancle_confirm();
		}
	}

	private void reservation_cancle_confirm() {
		Scanner scan = new Scanner(System.in);
		System.out.println("���� ��ҽ�, ��� ���� ������ �����˴ϴ�.");
		System.out.println("���� ������ ����Ͻðڽ��ϱ�?(y/n): ");

		String reservation_cancle = scan.next();
		if (reservation_cancle.contains(" ") || reservation_cancle.contains("")) {
			input_value = reservation_cancle.trim().split(" ");
		}

		if (input_value[0] == "y") {
			System.out.println("������ ��ҵǾ����ϴ�.");
			// ��������Ʈ��, main���� ���ư���
		} else {
			reservation_confirm();
		}
	}

	private void out_reservation_data() {
		file.write_file(this.date, Integer.parseInt(this.time),5);
		//�޴����Ͽ��� �޴��̸��� �ش��ϴ� �޴��� �޴� ��� �ֹ� ������ŭ ����
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
