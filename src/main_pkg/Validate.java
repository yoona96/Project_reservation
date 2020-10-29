package main_pkg;

import main_pkg.File_IO;
import java.io.*;
import java.util.*;

public class Validate {

	File_IO fi = new File_IO();
	private String date;
	private static int two, four, six = 0;
	private ArrayList<String> menu_list;

	public String get_directory() { // directory를 반환합니다. 반환받은 문자열에 파일 이름만 추가하면 됩니다.
		String os_name = System.getProperty("os.name").toLowerCase(); // 사용 중인 컴퓨터의 OS 얻기
		String user_name = "";
		String directory = "";

		if (os_name.contains("windows")) { // Windows
			user_name = new com.sun.security.auth.module.NTSystem().getName();
			directory = "C:\\Users\\" + user_name + "\\data\\";
		} else if (os_name.contains("linux")) { // Linux
			user_name = new com.sun.security.auth.module.UnixSystem().getUsername();
			directory = "/home/" + user_name + "/data/";
		} else { // macOS(macOS에서 계정명 얻는 방법을 아직 찾지 못해 아래 함수를 사용, 환경 변수가 위조될 가능성 있어 안좋음)
			user_name = System.getProperty("user.name");
			directory = "/Users/" + user_name + "/data/";
		}

		return directory;
	}

	private boolean validate_reservation_file() {
		String date;
		fi.create_file();
		fi.delete_file();
		int day = 0;
		while (day < 3) { // 오늘,내일,모레 3개의 파일에 대해 실행
			try {
				two = 0;
				four = 0;
				six = 0; // 테이블 수 초기화
				date = fi.get_date(day);
				File file = new File(this.get_directory()+ date + ".txt");
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				String line = "";
				String asc_line = "";
				boolean is = false;
				while ((line = br.readLine()) != null) {
					if (line.length() == 0)// 빈 줄이라면
						continue;
					asc_line += line + "\n";
					String temp[] = line.split("\t");
					is = reservation_file_grammer(temp, date); // 한 줄씩 문법확인
					if (!is) { // 오류가 있었다면 메소드를 종료합니다
						br.close();
						fr.close();
						return false;
					}
				}

				String buffer[] = asc_line.split("\n");
				Arrays.sort(buffer, new Comparator<String>() {
					public int compare(String o1, String o2) {
						int t1 = Integer.parseInt(o1.split("\t")[0]);
						int t2 = Integer.parseInt(o2.split("\t")[0]);
						if (t1 == t2) // 테이블 번호가 같다면 시간순으로 오름차순
							return Integer.parseInt(o1.split("\t")[3]) - Integer.parseInt(o2.split("\t")[3]);
						else // 테이블 번호로 오름차순
							return Integer.parseInt(o1.split("\t")[0]) - Integer.parseInt(o2.split("\t")[0]);
					}
				}); // 오름차순 정렬
				FileWriter fw = new FileWriter(file);
				asc_line = "";
				for (int i = 0; i < buffer.length; i++) {
					asc_line += buffer[i] + "\n";
				}
				fw.write(asc_line); // 오름차순으로 정렬된 값으로 파일을 덮어씁니다
				fw.close();
				br.close();
				fr.close();

				if ((two + four + six) != 20 * 11) { // 테이블 총 개수 확인
					System.out.println(date + ".txt의 형식이 올바르지 않습니다.");
					return false;
				}

				if (two != 6 * 11 || four != 10 * 11 || six != 4 * 11) { // 인원 테이블 수 확인
					System.out.println("<예약 정보 파일>의 좌석 정보가 올바르지 않습니다. 식당은 2 인용 테이블 "
							+ "6 개, 4 인용 테이블 10 개, 6 인용 테이블 4 개로 구성되어 있습니다. 따라서 <예약 정보 파일>에도 "
							+ "각각의 좌석은 해당 개수만큼 존재해야 합니다.");
					return false;
				}

				day++;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true; // 무결성 검사를 다 통과하면 true가 반환됩니다

	}

	private boolean reservation_file_grammer(String[] temp, String date) {
		for (int i = 0; i < temp.length; i++) {
			switch (i) {
			case 0: { // 테이블 번호
				if (Integer.parseInt(temp[i]) >= 1 && Integer.parseInt(temp[i]) <= 20)
					break;
				else {
					System.out.println(date + ".txt의 형식이 올바르지 않습니다.");
					return false;
				}

			}
			case 1: { // 테이블 인원수
				if (Integer.parseInt(temp[i]) == 2 || Integer.parseInt(temp[i]) == 4
						|| Integer.parseInt(temp[i]) == 6) {
					if (Integer.parseInt(temp[i]) == 2)
						two++;
					else if (Integer.parseInt(temp[i]) == 4)
						four++;
					else if (Integer.parseInt(temp[i]) == 6)
						six++;
					break;
				} else {
					System.out.println(date + ".txt의 좌석 인원 제한이 올바르지 않습니다. 식당을 구성하는 모든 좌석은 2, 4, 6 인용 좌석 중 하나입니다.");
					return false;
				}
			}
			case 2: { // 예약 인원 수
				if (Integer.parseInt(temp[i]) >= 0 && Integer.parseInt(temp[i]) <= 12)
					break;
				else {
					System.out.println(date + ".txt의 형식이 올바르지 않습니다.");
					return false;
				}

			}
			case 3: {// 시간
				if (Integer.parseInt(temp[i]) >= 10 && Integer.parseInt(temp[i]) <= 20)
					break;
				else {
					System.out.println(date + ".txt의 형식이 올바르지 않습니다.");
					return false;
				}

			}
			case 4: { // 이름
				if (temp[i].matches("^[가-힣]*$")||temp[i].matches("^[a-zA-Z]*$") && temp[i].length() >= 2)
					break;
				else {
					System.out.println(date + ".txt의 형식이 올바르지 않습니다.");
					return false;
				}

			}
			case 5: { // 전화번호
				if (temp[i].matches("^01(?:1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$")||temp[i].matches("^01(?:0)-(?:\\d{4})-\\d{4}$"))
					break;
				else {
					System.out.println(date + ".txt의 형식이 올바르지 않습니다.");
					return false;
				}
			}
			default: { // 메뉴 1,2,3,4,5
				if (temp[i].matches("^[0-9]*$"))
					break;
				else {
					System.out.println(date + ".txt의 형식이 올바르지 않습니다.");
					return false;
				}
			}
			}
		}
		return true;
	}

	public boolean validate_menu_file() { // menu.txt 문법 검사 및 수정
		// 메뉴 정보 파일 경로 얻기
		String directory = this.get_directory() + "menu.txt";
		menu_list = new ArrayList();
		// 문법 검사 및 수정
		File file = new File(directory);
		try {
			FileReader file_reader = new FileReader(file);
			BufferedReader buf_reader = new BufferedReader(file_reader);
			String line = "";
			while ((line = buf_reader.readLine()) != null) {
				if (line.trim().length() == 0) // 빈 줄이면 무시
					continue;
				menu_list.add(line.trim());
			}
			file_reader.close();
			buf_reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("데이터 경로에 <메뉴 정보 파일>이 존재하지 않습니다.");
			System.exit(0);
		} catch (IOException e) {
			System.out.println(e);
		}

		check_menu_overlap(); // 중복된 메뉴 확인
		check_menu_grammer(); // 문법 검사

		try {
			FileWriter fw = new FileWriter(file, false);
			for (int i = 0; i < 5; i++) {
				fw.write(menu_list.get(i) + "\n");
			}
			fw.close();
		} catch (IOException e) {
			System.out.println(e);
		}
		return true;
	}

	private void check_menu_overlap() { // 메뉴 중복을 확인하는 함수
		StringTokenizer token;
		String menu_name = "";
		ArrayList<String> temp_list = new ArrayList();
		String[] temp_array = new String[menu_list.size()];

		for (int i = 0; i < menu_list.size(); i++) {
			temp_list.add("/");
			temp_array[i] = "";
		}
		for (int i = menu_list.size() - 1; i > -1; i--) { // 만약 메뉴 이름이 중복이면 가장 마지막에 등장한 행이 유효하므로 마지막 요소부터 검사 시작
			token = new StringTokenizer(menu_list.get(i), "\t");
			menu_name = token.nextToken().trim(); // 메뉴 이름만 잘라서 가져오기
			temp_array[i] = menu_list.get(i);

			if (!temp_list.contains(menu_name)) // temp_list에 존재하지 않으면 추가
				temp_list.set(i, menu_name);
		}
		menu_list.clear(); // menu_list 요소 전부 지우기
		for (int i = 0; i < temp_list.size(); i++) {
			if (temp_list.get(i) != "/") {
				menu_list.add(temp_array[i]);
			} else {
				System.out.println("<메뉴 정보 파일>에 같은 이름의 메뉴가 두 번 이상 등장했습니다. 중복된 메뉴를 삭제합니다.");
				continue;
			}
		}
	}

	private void check_menu_grammer() {
		boolean exit_judge = false;
		StringTokenizer token;

		if (menu_list.size() != 5) { // 메뉴가 5개가 아닌 경우 종료
			System.out.println("<메뉴 정보 파일>의 메뉴 정보가 올바르지 않습니다. 메뉴 정보의 형식을 확인해주세요.");
			System.exit(0);
		}
		for (int i = 0; i < 5; i++) { // 문법 검사
			token = new StringTokenizer(menu_list.get(i), "\t");
			String str = menu_list.get(i);
			int token_num = token.countTokens();
			if (str.contains("\t\t") || str.contains(" \t") || (token_num != 3 && token_num != 4)) {
				exit_judge = true;
				break;
			}
			str = token.nextToken(); // 메뉴 이름
			if (str.contains("  ") || str.contains("\t") || str.contains(":") || str.contains("/") || str.contains("<")
					|| str.contains(">")) {
				exit_judge = true;
				break;
			}
			try {
				str = token.nextToken(); // 메뉴 가격
				int price = Integer.parseInt(str);
				str = token.nextToken(); // 메뉴 재고
				int stock = Integer.parseInt(str);
				if (price < 0 || price > 1000000) {
					exit_judge = true;
					break;
				}
				if (stock < 0) {
					System.out.println("<메뉴 정보 파일>에 작성된 모든 메뉴의 재고는 0 이상의 정수여야 합니다.");
					System.exit(0);
				}
				if (token_num == 4) { // 판매 시작, 종료 시간 서술된 경우
					str = token.nextToken();
					if (!str.contains("-") || str.contains(" ") || str.contains("\t")) {
						exit_judge = true;
						break;
					}
					String[] time = str.split("-");
					int start_time = Integer.parseInt(time[0]); // 판매 시작 시간
					int end_time = Integer.parseInt(time[1]); // 판매 종료 시간
					if (start_time < 10 || start_time >= end_time) {
						System.out.println("<메뉴 정보 파일>에 작성된 메뉴의 판매 시작 시간은 판매 종료 시간보다 빨라야 합니다.");
						System.exit(0);
					}
					if (end_time > 20) {
						System.out.println("<메뉴 정보 파일>에 작성된 메뉴의 판매 종료 시간은 20시를 넘을 수 없습니다.");
						System.exit(0);
					}
				}
			} catch (NumberFormatException e) {
				exit_judge = true;
				break;
			}
		}
		if (exit_judge) {
			System.out.println("<메뉴 정보 파일>의 메뉴 정보가 올바르지 않습니다. 메뉴 정보의 형식을 확인해주세요.");
			System.exit(0);
		}
	}

	public void check_all() {
		if (validate_menu_file() == false || validate_reservation_file() == false)
			System.exit(0);
		else
			return;
	}

}
