package main_pkg;

import java.util.Scanner;
import main_pkg.File_IO;

public class Main {
   
    public static void main(String[] args) {
       
       /* file_write 사용예제 */

       /* File_IO fi = new File_IO();
        
        fi.create_file();
        fi.delete_file();
        
        String date = "20201027";
        fi.read_file(date);
       String[][][] temp = fi.tb.get_day();
        temp[5][0][0] = "010-1111-1111";
        fi.tb.set_day(temp);
        fi.write_file(date, 0, 0);*/

        /* file_create, delete 사용예제*/
        
       Reservation reservation = new Reservation();
       Validate validate = new Validate();
        
       while(true) {
            print_menu();
            
            Scanner input_scan = new Scanner(System.in);
            Scanner pwd_scan = new Scanner(System.in);
            String menu_choice = input_scan.next();
            
            if(menu_choice.matches("[1-7]")) {
               int chosen_menu = Integer.parseInt(menu_choice);
               if(chosen_menu >= 1 && chosen_menu <= 7) {
                  switch(chosen_menu){
                     case 1: reservation.choose_menu();
                           break;
                  
                     default: continue;
                  }
               }
           }else {
              System.out.println("해당 명령어는 존재하지 않습니다.\n");
              continue;
           }
      }
    }


   public static void print_menu() {
      System.out.println("--------------------");
      System.out.println("   메뉴");
      System.out.println("--------------------");
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

        if (pwd.trim().equals("1234"))
            return true;
        else
            return false;
    }

}