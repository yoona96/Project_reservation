package main_pkg;

import java.util.Scanner;

public class Reservation {

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
	String[] yorn_value = new String[0];
	File_IO file = new File_IO();

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
		Scanner scan = new Scanner(System.in);
		// 다시올때 문제!
		String patterns0 = "^[가-힣]*";
		String patterns1 = "[0-9]";
		String patterns2 = "[a-zA-Z]";
		String patterns3 = "\t";
		file.read_menu();
		menu = file.tb.getMenu();

		while (true) {
			// 화면 출력
			String temp_num;
			stock_result_index = 0;
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

			temp_num = scan.nextLine();
			if (temp_num.contains("\t") || temp_num.contains("")) {
				str_menu_num = temp_num.trim().split("\t");
			}
			// 문법 규칙 위배시
			if (temp_num.matches(patterns0 + patterns3 + patterns1 + patterns3 + patterns1 + patterns3 + patterns1
					+ patterns3 + patterns1 + patterns3 + patterns1 + "|" + patterns3 + patterns3 + patterns1
					+ patterns3 + patterns1 + patterns3 + patterns1 + patterns3 + patterns1 + patterns3 + patterns1)) {
				System.out.println("주문입력 형식에 오류가 있습니다. 입력 방식은 (ex.\t2\t3\t0\t0\t0) 형식입니다 ");
				continue;
			}

			for (int i = 0; i < menu.length; i++) {
				int_menu_num[i] = Integer.parseInt(str_menu_num[i]);
			}

			// 의미 규칙 위배시
			// 1. 주문 불가능한 시간대일 경우
			int time_check_temp = menu_time_check();
			if (time_check_temp != -1) {
				System.out.println(menu[0][time_check_temp] + "을(를) 주문할 수 없습니다. 주문 가능 시간대를 확인해 주세요.");
				continue;
			}
			// 2. 재고가 부족할경우
			int[] stock_temp = menu_stock_check();
			if (stock_result_index != 0) {
				for (int i = 0; i < stock_result_index; i++) {
					System.out.println(menu[0][stock_temp[i]] + "의 수량이 부족합니다(남은 수량:" + menu[2][stock_temp[i]] + "개)");
				}
				continue;
			}

			break;
		}

		menu_confirm();
	}

	private int menu_time_check() {

		for (int i = 0; i < menu.length; i++) {
			if (int_menu_num[i] != 0) {
				if (menu[3][i] == null) {
					continue;
				} else {
					String[] menu_time = menu[3][i].split("-");
					if ((Integer.parseInt(this.time) < Integer.parseInt(menu_time[0]))
							|| (Integer.parseInt(menu_time[1]) < Integer.parseInt(this.time + 2))) {// 주문가능시간 // 벗어남
						return i;
					}
				}
			}
		}
		return -1;// 주문가능시간!
	}

	private int[] menu_stock_check() {
		int[] result = new int[menu.length];
		for (int i = 0; i < menu.length; i++) {
			if (int_menu_num[i] != 0) {
				int this_menu_stock = Integer.parseInt(menu[2][i]);
				if (int_menu_num[i] > this_menu_stock) {// 주문수량이 재고보다 많을때
					result[stock_result_index++] = i;
				} // 재고가 부족한 모든 메뉴 저장해서 반환
			}
		}
		return result;
	}

	private void menu_confirm() {

		Scanner scan1 = new Scanner(System.in);
		String patterns0 = "^[가-힣]*";
		String patterns1 = "[0-9]";
		String patterns2 = "[a-zA-Z]";
		String patterns3 = "\t";

		while (true) {
			System.out.println("[메뉴]\t[가격]\t[주문 수량]");
			for (int i = 0; i < menu.length; i++) {
				System.out.println(menu[0][i] + "\t\\" + menu[1][i] + "\t" + str_menu_num[i]);
				price += Integer.parseInt(menu[1][i]) * int_menu_num[i];
			}
			System.out.println();

			System.out.println("결제 예정 금액: " + price);
			System.out.print("주문 내역을 확정하고 주문을 완료하시겠습니까?(y/n) :");
			String menu_confirm = scan1.next();

			if (menu_confirm.contains(" ") || menu_confirm.contains("")) {
				yorn_value = menu_confirm.trim().split(" ");
			}
			// 문법 규칙 위배시
			if (menu_confirm.matches(patterns0 + patterns3 + patterns1 + patterns3 + patterns1 + patterns3 + patterns1
					+ patterns3 + patterns1 + patterns3 + patterns1 + "|" + patterns3 + patterns3 + patterns1
					+ patterns3 + patterns1 + patterns3 + patterns1 + patterns3 + patterns1 + patterns3 + patterns1)) {
				System.out.println("입력은 y 혹은 n만 가능합니다. 다시 입력해주세요.");
				continue;
			}

			if (yorn_value[0].contains("y")) {
				String str = "";
				for (int i = 0; i < menu.length; i++) {
					if (int_menu_num[i] != 0) {
						str += menu[0][i] + " ";
					}
				}
				System.out.println(str + "를(을) 주문합니다.");
				reservation_confirm();
			} else {
				choose_menu();
			}
		}

	}

	private void reservation_confirm() {

		Scanner scan2 = new Scanner(System.in);
		String patterns0 = "^[가-힣]*";
		String patterns1 = "[0-9]";
		String patterns2 = "[a-zA-Z]";
		String patterns3 = "\t";

		while (true) {
			System.out.println("\n예약 내용을 확인하겠습니다.\n");
			System.out.println("예약자 이름: " + this.name);
			System.out.println("전화번호: " + this.phone);
			System.out.println("예약 시간: ");
			System.out.println("인원 수: ");
			System.out.println("예약 좌석: ");
			System.out.print("주문 메뉴: ");
			for (int i = 0; i < menu.length; i++) {
				if (int_menu_num[i] != 0) {
					System.out.print(menu[0][i] + ": " + int_menu_num[i] + "  ");
				}
			}
			System.out.println();

			System.out.println("결제 예정 금액: \\" + price + "\n");
			System.out.print("예약을 확정하시겠습니까?(y/n): ");
			String reservation_confirm = scan2.next();
			if (reservation_confirm.contains(" ") || reservation_confirm.contains("")) {
				yorn_value = reservation_confirm.trim().split(" ");
			}
			// 문법 규칙 위배시
			if (reservation_confirm.matches(
					patterns0 + patterns3 + patterns1 + patterns3 + patterns1 + patterns3 + patterns1 + patterns3
							+ patterns1 + patterns3 + patterns1 + "|" + patterns3 + patterns3 + patterns1 + patterns3
							+ patterns1 + patterns3 + patterns1 + patterns3 + patterns1 + patterns3 + patterns1)) {
				System.out.println("입력은 y 혹은 n만 가능합니다. 다시 입력해주세요.");
				continue;
			}

			if (yorn_value[0].contains("y")) {
				System.out.println("예약이 완료되었습니다.");
				out_reservation_data();
			} else {
				reservation_cancle_confirm();
			}
		}

	}

	private void reservation_cancle_confirm() {
		Scanner scan3 = new Scanner(System.in);
		String patterns0 = "^[가-힣]*";
		String patterns1 = "[0-9]";
		String patterns2 = "[a-zA-Z]";
		String patterns3 = "\t";

		System.out.println("예약 취소시, 모든 예약 정보가 삭제됩니다.");
		System.out.println("정말 예약을 취소하시겠습니까?(y/n): ");

		String reservation_cancel = scan3.next();
		if (reservation_cancel.contains(" ") || reservation_cancel.contains("")) {
			yorn_value = reservation_cancel.trim().split(" ");
		}
		// 문법 규칙 위배시
		if (reservation_cancel.matches(patterns0 + patterns3 + patterns1 + patterns3 + patterns1 + patterns3
				+ patterns1 + patterns3 + patterns1 + patterns3 + patterns1 + "|" + patterns3 + patterns3 + patterns1
				+ patterns3 + patterns1 + patterns3 + patterns1 + patterns3 + patterns1 + patterns3 + patterns1)) {
			System.out.println("입력은 y 혹은 n만 가능합니다. 다시 입력해주세요.");
			
		}

		if (yorn_value[0].contains("y")) {
			System.out.println("예약이 취소되었습니다.");
			// 주프롬프트로, main으로 돌아가기
		} else {
			reservation_confirm();
		}
	}

	private void out_reservation_data() {
		file.write_file("20201026", 1, 5);
		// 메뉴파일에서 메뉴이름에 해당하는 메뉴의 메뉴 재고 주문 수량만큼 제외
		for (int i = 0; i < menu.length; i++) {
			if (int_menu_num[i] != 0) {
				int origin_stock = Integer.parseInt(menu[2][i]);
				int new_stock = origin_stock - int_menu_num[i];
				menu[2][i] = Integer.toString(new_stock);
			}
			file.tb.setMenu(menu);
			file.write_menu(i);
		}
	}
}