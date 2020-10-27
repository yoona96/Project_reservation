package main_pkg;

import main_pkg.File_IO;
import main_pkg.Validate;

public class Main {

    public static void main(String[] args) {
        /*한글 테스팅*/

    
    }
    
    public void call_menu() {
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

        if (pwd.equals("1234"))
            return true;
        else
            return false;
    }
 
}
