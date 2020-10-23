package main_pkg;

import java.util.Scanner;
import main_pkg.validate;

public class main{

	public static void main(String[] args) {
	//기본 흐름 제어하는 메인 함수
		validate();
		
		print_menu();
		
		Scanner pwd_scan = new Scanner(System.in);
		Scanner menu_scan = new Scanner(System.in);
		int menu_input = menu_scan.nextInt();
		
		if(menu_input == 1) {
			
		}else if(menu_input == 2) {
			
		}else if(menu_input == 3) {
			
		}else if(menu_input == 4) {
			
		}else if(menu_input == 5) {
			String pwd = pwd_scan.next();
			if(password(pwd) == true) {
				System.exit(0);
			}else {
				System.out.println("비밀번호가 올바르지 않습니다.");
				print_menu();
			}
		}else if(menu_input == 6) {
			print_help();
		}else if(menu_input == 7) {
			String pwd = pwd_scan.next();
			if(password(pwd) == true) {
				validate();
			}else {
				System.out.println("비밀번호가 올바르지 않습니다.");
			}
		}else {	
			System.out.println("해당 명령어는 존재하지 않습니다.");
			print_menu();
		}
	}

	private static void validate() {
		
	}
	
	private static void print_menu() {
		System.out.println("------------------------------");
		System.out.println("              메뉴");
		System.out.println("------------------------------");
		System.out.println("1. 예약하기");
		System.out.println("2. 예약 조회");
		System.out.println("3. 예약 취소");
		System.out.println("4. 예약 변경");
		System.out.println("5. 프로그램 종료");
		System.out.println("6. 도움말 출력");
		System.out.println("7. 무결성 검사\n");
		System.out.println("원하시는 번호를 선택하세요:");
		
		
	}
	
	private static boolean password(String pwd) {
		if(pwd == "1234") {
			return true;
		}else {
			return false;
		}
	}
	
	private static void print_help() {
		System.out.println("1. 프로그램 안내\n");
		System.out.println("- 예약제로만 운영되는 레스토랑을 위한 예약 프로그램입니다.");
		
	}
}