package main_pkg;

import java.text.DecimalFormat;
import java.util.*;

public class Reservation_Check {
	private File_IO file = new File_IO();
	private String[][][] db = new String[11][11][20];
	private ArrayList<int[]> user_inform[] = new ArrayList[3]; // 0: 당일, 1: 내일, 2: 모레
	private String user_name;
	private String phone_num;

	private void input_inform(String section) {
		Scanner scan = new Scanner(System.in);

		boolean check = true;

		while (check) {
			if (section.equals("Check"))
				System.out.println("<예약 조회>");
			else if (section.equals("Cancle"))
				System.out.println("<예약 취소>");
			else if (section.equals("Change"))
				System.out.println("<예약 변경>");

			System.out.print("예약 시 사용하신 이름과 전화번호를 입력해 주세요(ex. 김건국          010-1234-5678):");
			String str = scan.nextLine();
			StringTokenizer token = new StringTokenizer(str, "\t");
			if (token.countTokens() != 2 || str.contains("\t\t")) {
				System.out.println("이름+<tab> 1개+전화번호의 형식에 맞춰서 정확히 입력하세요.\n");
				continue;
			}
			user_name = token.nextToken();
			phone_num = token.nextToken();
			String pattern = "^[가-힣a-zA-Z]*$";
			if (!user_name.matches(pattern)) {
				System.out.println("이름+<tab> 1개+전화번호의 형식에 맞춰서 정확히 입력하세요.\n");
				continue;
			}
			String pattern2 = "^01(?:0)-(?:\\d{4})-\\d{4}$";
			String pattern3 = "^01(?:0)(?:\\d{4})\\d{4}$";
			String pattern4 = "^01(?:1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$";
			String pattern5 = "^01(?:1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$";
			if (!phone_num.matches(pattern2) && !phone_num.matches(pattern3) && !phone_num.matches(pattern4)
					&& !phone_num.matches(pattern5)) {
				System.out.println("이름+<tab> 1개+전화번호의 형식에 맞춰서 정확히 입력하세요.\n");
				continue;
			}
			if (!phone_num.contains("-")) {
				StringBuilder sb = new StringBuilder();
				sb.append(phone_num);
				sb.insert(3, "-");
				if (phone_num.length() == 10)
					sb.insert(7, "-");
				else
					sb.insert(8, "-");
				phone_num = sb.toString();
			}
			if (!check_DB()) {
				System.out.println("예약 정보가 존재하지 않습니다. 정확한 예약자의 이름과 전화번호를 입력해주세요.\n");
				continue;
			}
			check = false;
		}
		// System.out.println(user_name + " " + phone_num); // 확인용
	}

	private boolean check_DB() {
		boolean check = false;

		for (int i = 0; i < 3; i++) {
			String date = file.get_date(i);
			file.read_file(date);
			db = file.tb.get_day();
			user_inform[i] = new ArrayList();

			for (int j = 0; j < 20; j++) {
				for (int k = 0; k < 11; k++) {
					if (user_name.equals(db[4][k][j]) && phone_num.equals(db[5][k][j])) {
						int[] pos = new int[2];
						pos[0] = k; // pos[0]: time
						pos[1] = j; // pos[1]: table
						user_inform[i].add(pos);
						check = true;
					}
				}
			}

		}
		return check;
	}

	public ArrayList[] show_reservation_inform(String section) { // Check, Cancle, Change 중 하나

		this.input_inform(section);
		
		System.out.println("\n[예약 내용]\n");

		for (int i = 0; i < 3; i++) {
			String date = file.get_date(i);
			file.read_file(date);
			db = file.tb.get_day();
			file.read_menu();
			String[][] menu = file.tb.get_menu();
			StringBuilder sb = new StringBuilder();
			sb.append(date);
			sb.insert(4, "-");
			sb.insert(7, "-");
			date = sb.toString();
			int price = 0;

			if (user_inform[i].size() == 0)
				continue;
			for (int j = 0; j < user_inform[i].size(); j += 2) {
				int[] pos = user_inform[i].get(j);
				int time = pos[0];
				int table = pos[1];
				System.out.println("예약 일자: " + date);
				if (section.equals("Check")) {
					System.out.println("예약자 이름: " + db[4][time][table]);
					System.out.println("전화번호: " + db[5][time][table]);
				}
				int start_time = Integer.parseInt(db[3][time][table]);
				System.out.println("예약 시간: " + start_time + ":00 ~ " + (start_time + 2) + ":00");
				System.out.println("인원 수: " + db[2][time][table] + "명");
				System.out.println("예약 좌석: " + db[0][time][table] + "번");
				System.out.print("주문 메뉴: ");
				for (int k = 6; k < 11; k++) {
					int num = Integer.parseInt(db[k][time][table]);
					if (num != 0) {
						price += (Integer.parseInt(menu[1][k - 6]) * num);
						if (k == 10)
							System.out.println(menu[0][k - 6] + " " + num);
						else
							System.out.print(menu[0][k - 6] + " " + num + ", ");
					}
				}
				DecimalFormat formatter = new DecimalFormat("###,###");
				System.out.println("\n결제 예정 금액: " + formatter.format(price) + "\n");
				price = 0;

			}
		}
		return user_inform;
	}

}
