package main_pkg;

import java.io.File;
import java.util.Scanner;
import main_pkg.File_IO;
import main_pkg.Validate;

public class Main {

    public static void main(String[] args) {


        Reservation reservation = new Reservation();
        Reservation_Change rc= new Reservation_Change();
        Reservation_Check rch = new Reservation_Check();
        Cancel_Reservation cr = new Cancel_Reservation();
        Validate validate = new Validate();

        validate.check_all();

        while (true) {

            print_menu();
            Scanner input_scan = new Scanner(System.in);
            Scanner pwd_scan = new Scanner(System.in);
            String menu_choice = input_scan.nextLine();

            if (menu_choice.matches("[1-7]")) {
                int chosen_menu = Integer.parseInt(menu_choice);
                if (chosen_menu >= 1 && chosen_menu <= 7) {
                    switch (chosen_menu) {
                        case 1:
                            reservation.user_input();
                            break;
                        case 2:
                            rch.show_reservation_inform("Check");
                            break;
                        case 3:
                            rc.change_main();
                            break;
                        case 4:
                            cr.cancel_reservation_main();
                            break;
                        case 5: {
                            String pwd = pwd_scan.next();
                            if (password(pwd) == true) {
                                System.exit(0);
                            } else {
                                System.out.println("비밀번호가 올바르지 않습니다.\n");
                                break;
                            }
                        }
                        case 6:
                            print_help();
                            break;
                        case 7: {
                            String pwd = pwd_scan.next();
                            if (password(pwd) == true) {

                            } else {
                                break;
                            }
                        }
                        default:
                            input_scan = null;
                            continue;
                    }
                }
            } else {
                System.out.println("해당 명령어는 존재하지 않습니다.\n");
                continue;
            }
        }

    }

    public static void print_menu() {
        System.out.println("--------------------");
        System.out.println("	메뉴");
        System.out.println("--------------------");
        System.out.println("1. 예약하기");
        System.out.println("2. 예약 조회");
        System.out.println("3. 예약 취소");
        System.out.println("4. 예약 변경");
        System.out.println("5. 프로그램 종료");
        System.out.println("6. 도움말 출력");
        System.out.println("7. 무결성 검사\n");
        System.out.print("원하시는 번호를 선택하세요: ");
    }

    private static boolean password(String pwd) {

        if (pwd.trim().equals("1234"))
            return true;
        else
            return false;
    }

    public static void print_help() {

    }

}
