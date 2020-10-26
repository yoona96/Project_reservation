package main_pkg;

import java.io.*;
import java.util.*;

public class validate {

	private ArrayList<String> menu_list = new ArrayList();

	public void validate_menu_file() { // menu.txt 문법 검사 및 수정
		// 메뉴 정보 파일 경로 얻기
		String os_name = System.getProperty("os.name").toLowerCase(); // 사용 중인 컴퓨터의 OS 얻기
		String user_name = "";
		String directory = "";

		if (os_name.contains("windows")) { // Windows
			user_name = new com.sun.security.auth.module.NTSystem().getName();
			directory = "C:\\Users\\" + user_name + "\\data\\menu.txt";
		} else if (os_name.contains("linux")) { // Linux
			user_name = new com.sun.security.auth.module.UnixSystem().getUsername();
			directory = "/home/" + user_name + "/data/menu.txt";
		} else { // macOS(macOS에서 계정명 얻는 방법을 아직 찾지 못해 아래 함수를 사용, 환경 변수가 위조될 가능성 있어 안좋음)
			user_name = System.getProperty("user.name");
			directory = "/Users/" + user_name + "/data/menu.txt";
		}

		// 문법 검사 및 수정
		File file = new File(directory);
		BufferedReader buf_reader;
		try {
			FileReader file_reader = new FileReader(file);
			buf_reader = new BufferedReader(file_reader);
			String line = "";
			while ((line = buf_reader.readLine()) != null) {
				if (line.trim().length() == 0) // 빈 줄이면 무시
					continue;
				menu_list.add(line.trim());
			}
			buf_reader.close();
		} catch (FileNotFoundException e) {
			System.out.println(e);
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
		
		for(int i = 0; i < menu_list.size(); i++)
			System.out.println(menu_list.get(i));
	}

	public void check_menu_overlap() { // 메뉴 중복을 확인하는 함수
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

	public void check_menu_grammer() {
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

	public void validate_reservation_file() {

	}

	public void check_all(String directory, String file_name) {

	}

}
