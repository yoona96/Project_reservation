package main_pkg;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.*;

public class Reservation_Check {

	private File_IO file = new File_IO();
	private String[][][] db = new String[11][11][20];
	private ArrayList<int[]> user_inform[] = new ArrayList[3]; // 0: 당일, 1: 내일, 2: 모레
	private String user_name;
	private String phone_num;

	private void input_inform(String section) throws UnsupportedEncodingException {
		Scanner scan = new Scanner(System.in, "euc-kr");

		boolean check = true;

		while (check) {
			if (section.equals("Check"))
				System.out.println("<예약 조회>");
			else if (section.equals("Cancel"))
				System.out.println("<예약 취소>");
			else if (section.equals("Change"))
				System.out.println("<예약 변경>");

			System.out.print("예약 시 사용하신 이름과 전화번호를 입력해 주세요(ex. 김건국          010-1234-5678):");
			String str = scan.nextLine();
			byte euc_byte[] = str.getBytes("euc-kr");
			String str_from_euc = new String(euc_byte,"euc-kr");
			byte utf_byte[] = str_from_euc.getBytes("utf-8");
			String str_from_utf = new String(utf_byte,"utf-8");
			StringTokenizer token = new StringTokenizer(str_from_utf, "\t");
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
						int[] pos = new int[2]; // k와 j가 순서대로 들어갑니다.
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

	private void sort_user_inform() { // 붙어있는 좌석 순서대로 ArrayList에 저장되게 수정
		for (int i = 0; i < 3; i++) {		
			String date = file.get_date(i);
			file.read_file(date);
			db = file.tb.get_day();
			for (int j = 0; j < user_inform[i].size(); j += 2) {
				int[] pos = user_inform[i].get(j);
				int limit = Integer.parseInt(db[1][pos[0]][pos[1]]);
				int num = Integer.parseInt(db[2][pos[0]][pos[1]]);
				if (limit < num) { // 붙어있는 좌석 예약이라면
					for (int k = j + 2; k < user_inform[i].size(); k += 2) {
						int[] pos2 = user_inform[i].get(k);
						int[] pos3 = user_inform[i].get(k + 1);
						if ((pos[0] == pos2[0]) && (pos[1] + 1 == pos2[1])) {
							user_inform[i].remove(k);
							user_inform[i].remove(k);
							user_inform[i].add(j + 2, pos2);
							user_inform[i].add(j + 3, pos3);
							break;
						}
					}
				}
			}
		}
	}

	public ArrayList[] show_reservation_inform(String section) throws UnsupportedEncodingException { // Check, Cancel, Change 중 하나

		this.input_inform(section);
		this.sort_user_inform();

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
			for (int j = 0; j < user_inform[i].size(); j += 2) { // 예약 시간이 10시라면 텍스트 파일에는 10시, 11시 행에 똑같은 정보가 쓰여있기 때문에
																	// j += 2
				int[] pos = user_inform[i].get(j);
				int time = pos[0]; // 항상 textDB에 존재하는 3차원 배열에 [][pos[0]][pos[1]] 넣어서 사용하시면 됩니다.
				int table = pos[1];
				System.out.println("예약 일자: " + date);
				if (section.equals("Check")) {
					System.out.println("예약자 이름: " + db[4][time][table]);
					System.out.println("전화번호: " + db[5][time][table]);
				}
				int start_time = Integer.parseInt(db[3][time][table]);
				System.out.println("예약 시간: " + start_time + ":00 ~ " + (start_time + 2) + ":00");
				System.out.println("인원 수: " + db[2][time][table] + "명");

				int limit = Integer.parseInt(db[1][pos[0]][pos[1]]);
				int cnt = Integer.parseInt(db[2][pos[0]][pos[1]]);
				int seat = Integer.parseInt(db[0][time][table]);
				if (limit < cnt) {
					System.out.println("예약 좌석: " + seat + ", " + (seat + 1) + "번");
					j += 2;
				} else {
					System.out.println("예약 좌석: " + seat + "번");
				}

				System.out.print("주문 메뉴: ");
				String menu_inform = "";
				for (int k = 6; k < 11; k++) { // 메뉴 출력을 위한 for문
					int num = Integer.parseInt(db[k][time][table]); // 메뉴 주문 개수 num에 대입
					if (num != 0) {
						price += (Integer.parseInt(menu[1][k - 6]) * num); // 가격에 주문 개수를 곱
						menu_inform += menu[0][k - 6] + " " + num + ", ";
					}
				}
				StringBuffer str_buf = new StringBuffer(menu_inform);
				if(str_buf.length() > 0) {
					str_buf.delete(str_buf.length() - 2, str_buf.length());
				}
				System.out.println(str_buf);
				
				DecimalFormat formatter = new DecimalFormat("###,###");
				System.out.println("결제 예정 금액: ￦" + formatter.format(price) + "\n");
				price = 0;

			}
		}
		return user_inform;
	}

}
