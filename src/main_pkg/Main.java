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
                        	cr.cancel_reservation_main();
                            break;
                        case 4:
                        	rc.change_main();
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
                                validate.check_all();

                            } else {
                                System.out.println("비밀번호가 올바르지 않습니다.\n");
                                break;
                            }
                        }
                        default:
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
        System.out.println("1. 프로그램 안내");
        System.out.println("- 예약제로만 운영되는 레스토랑을 위한 예약 프로그램입니다.");
        System.out.println("- 해당 프로그램에서는 고객분들을 위해 예약, 예약 조회, 예약 취소, 예약 변경 총 네가지 기능을 제공합니다.");
        System.out.println("");
        System.out.println("2. 레스토랑 이용 정보");
        System.out.println("- 레스토랑의 영업 시간은 10:00 ~ 22:00입니다.");
        System.out.println("- 2인 테이블 6개, 4인 테이블 10개, 6인 테이블 4개로 총 20개의 테이블이 존재합니다. 동시 최대 수용 가능 인원은 76인입니다.");
        System.out.println("- 하나의 예약 건당 최대 예약 가능한 인원은 12명입니다.");
        System.out.println("");
        System.out.println("3. 예약 관련 정보");
        System.out.println("- 당일 방문 건에 대한 예약은 불가능하며, 예약 접수일로부터 최대 이틀 후까지 가능합니다.");
        System.out.println("- 이미 확정하신 예약에 대해, 예약하셨던 당일에 방문하셔서 변경하시거나 취소하시는 것은 불가능하며, 최소 하루 전에 진행해 주셔야 합니다. ");
        System.out.println("- 예약 시 예약 당일에 이용하고자 하는 코스 요리를 함께 주문해 주셔야 합니다.");
        System.out.println("");
        System.out.println("4. 메뉴 관련 정보");
        System.out.println("- 이용 가능한 코스 요리는 총 5가지입니다.");
        System.out.println("- 런치의 경우 11:00~14:00, 디너의 경우 17:00~20:00에 이용 가능합니다.");
        System.out.println("- 런치와 디너를 제외한 나머지 3가지 코스 요리는 이용 시간에 제약이 없으며, 레스토랑의 사정에 따라 변동될 수 있습니다.");
        System.out.println("");
        System.out.println("5. 관리자 전용 메뉴");
        System.out.println("- 메인 메뉴의 5. 프로그램 종료와 7. 무결성 검사는 관리자 전용 메뉴입니다. 해당 메뉴를 이용하기 위해서는 추가적인 접근 권한이 필요합니다.");
        System.out.println("");
    }

}
