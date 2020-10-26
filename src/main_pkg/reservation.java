package main_pkg;

import java.util.Scanner;

public class reservation {

	private static String count;
	private static String date;
	private String time = "13";
	private String table;
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
	
	
	private void user_input(String day, String time, String count) {

	}

	private boolean search_table(String day, String time, String count) {

		return false;
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
		String patterns0 = "^[가-힣]*";
		String patterns1 = "[0-9]";
		String patterns2 = "[a-zA-Z]";
		String patterns3 = "\t";


		
		menu = file.tb.getMenu();
		while (true) {
			// 화면 출력
			System.out.println("[메뉴]\t[가격]\t[주문 가능 시간]");
			for (int i = 0; i < menu.length; i++) {
				System.out.print(menu[0][i] + "\t\\" + menu[1][i] + "\t");
				if (menu[3][i] != null) {// all이 아닌경우
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
			System.out.println(menu[0][menu.length - 1] + "의 주문 수량을 차례대로 입력하세요(ex.\t2\t3\t0\t0\t0)");
			System.out.print("→");

			String temp_num = scan.nextLine();

			if (temp_num.contains("\t") || temp_num.contains("")) {
				str_menu_num = temp_num.trim().split("\t");
			}


			if(temp_num.matches(patterns0+patterns3+patterns1+patterns3+patterns1+patterns3+patterns1+patterns3+patterns1+patterns3+patterns1+"|"+patterns3+patterns3+patterns1+patterns3+patterns1+patterns3+patterns1+patterns3+patterns1+patterns3+patterns1)){
				System.out.println("주문입력 형식에 오류가 있습니다. 입력 방식은 (ex.\t2\t3\t0\t0\t0) 형식입니다 ");
				continue;
			}

			
			for (int i = 0; i < menu.length; i++) {
				int_menu_num[i] = Integer.parseInt(str_menu_num[i]);
			}
			// 의미 규칙 위배시
			// 1. 주문 불가능한 시간대일 경우
			for (int i = 0; i < menu.length; i++) {
				if (int_menu_num[i] != 0) {// 주문한 메뉴중
					if (!menu_time_check(i)) {// 주문 가능시간을 벗어났다면 오류 문구 출력
						System.out.println(menu[0][i] + "을(를) 주문할 수 없습니다. 주문 가능 시간대를 확인해 주세요.");
						continue;
					}
				}
			}
			// 2. 재고가 부족할경우
			int[] stock_temp = menu_stock_check();
			if (stock_result_index != 0) {
				for (int i = 0; i < stock_result_index; i++) {
					System.out.println(menu[0][stock_temp[i]] + "의 수량이 부족합니다(남은 수량:"
							+ menu[2][stock_temp[i]] + "개)");
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
		if ((Integer.parseInt(this.time) < this_menu_time) || (this_menu_time + 2 < Integer.parseInt(this.time + 2))) {// 주문가능시간																						// 벗어남
			return false;
		}

		return true;
	}

	private int[] menu_stock_check() {
		int[] result = new int[menu.length];
		for (int i = 0; i < menu.length; i++) {
			if(int_menu_num[i]!=0) {
				int this_menu_stock = Integer.parseInt(menu[2][i]);
				if (int_menu_num[i] > this_menu_stock) {// 주문수량이 재고보다 많을때
					result[stock_result_index++] = i;
				} // 재고가 부족한 모든 메뉴 저장해서 반환
			}
		}
		return result;
	}

	private void menu_confirm() {
		Scanner scan = new Scanner(System.in);

		System.out.println("[메뉴]\t[가격]\t[주문 수량]");
		for (int i = 0; i < menu.length; i++) {
			System.out.println(menu[0][i] + "\t\\" + menu[1][i] + "\t" + str_menu_num[i]);
			price += Integer.parseInt(menu[1][i]) * int_menu_num[i];
		}
		System.out.println();

		System.out.println("결제 예정 금액: " + price);
		System.out.print("주문 내역을 확정하고 주문을 완료하시겠습니까?(y/n) :");
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
			System.out.println("를(을) 주문합니다.");
			reservation_confirm();
		} else {
			choose_menu();
		}
	}

	private void reservation_confirm() {
		Scanner scan = new Scanner(System.in);
		System.out.println("예약 내용을 확인하겠습니다.\n");
		System.out.println("예약자 이름: " + this.name);
		System.out.println("전화번호: " + this.phone);
		System.out.println("예약 시간: ");
		System.out.println("인원 수: ");
		System.out.println("예약 좌석: ");
		System.out.print("주문 메뉴: ");
		for (int i = 0; i < menu.length; i++) {
			if (int_menu_num[i] != 0) {
				System.out.print(menu[0][i] + ": " + menu[2][i] + "  ");
			}
		}
		System.out.println();

		System.out.println("결제 예정 금액: \\" + price + "\n");
		System.out.print("예약을 확정하시겠습니까?(y/n): ");
		String reservation_confirm = scan.next();
		if (reservation_confirm.contains(" ") || reservation_confirm.contains("")) {
			input_value = reservation_confirm.trim().split(" ");
		}

		if (input_value[0] == "y") {
			System.out.println("예약이 완료되었습니다.");
			out_reservation_data();
		} else {
			reservation_cancle_confirm();
		}
	}

	private void reservation_cancle_confirm() {
		Scanner scan = new Scanner(System.in);
		System.out.println("예약 취소시, 모든 예약 정보가 삭제됩니다.");
		System.out.println("정말 예약을 취소하시겠습니까?(y/n): ");

		String reservation_cancle = scan.next();
		if (reservation_cancle.contains(" ") || reservation_cancle.contains("")) {
			input_value = reservation_cancle.trim().split(" ");
		}

		if (input_value[0] == "y") {
			System.out.println("예약이 취소되었습니다.");
			// 주프롬프트로, main으로 돌아가기
		} else {
			reservation_confirm();
		}
	}

	private void out_reservation_data() {
		file.write_file(this.date, Integer.parseInt(this.time),5);
		//메뉴파일에서 메뉴이름에 해당하는 메뉴의 메뉴 재고 주문 수량만큼 제외
		for(int i=0;i<menu.length;i++) {
			if(int_menu_num[i]!=0) {
				int origin_stock = Integer.parseInt(menu[2][i]);
				int new_stock = origin_stock - int_menu_num[i];
				menu[2][i] = Integer.toString(new_stock);
			}
			db.setMenu(menu);
			//file.write_menu(i);
		}
	}
}
