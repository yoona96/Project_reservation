package main_pkg;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Scanner;

import main_pkg.textDB;

public class Reservation {

	String[] input_value = new String[0];
	File_IO file = new File_IO();
	textDB db = new textDB();

    private String count, date, day, time;
    private String name;
    private String phone;
    private String st_num0;
    private String st_num1;
    private String table;
    private String[][] menu = new String[4][5]; //menu파일 배열
    private String[] str_menu_num = new String[5];//사용자가 입력하는 주문수량 string배열
    private int[] int_menu_num = new int[5];//사용자 입력 주문수량 int배열
    private int stock_result_index = 0;//재고 부족한 상품저장할 배열의 인덱스
    private int price = 0; //주문한 총 금액

    File_IO fio = new File_IO();

    /*
     * print the 'today', user can input date, time, number of people
     */
    public void user_input(){

        LocalDate date_format = LocalDate.now();
        String today = date_format.toString();

        String reserv_date, reserv_time, reserv_count, sub_time;

        Scanner scanner = new Scanner(System.in);

        while(true) {
            System.out.println("예약 접수일: " + today);
            System.out.println("예약 희망 일자와, 시간, 그리고 방문하는 인원수를 차례대로 입력하세요.");
            System.out.print("→");

            String reservation_input = scanner.nextLine();

            String[] input_value = null;


            //check if format is right
            if(reservation_input.contains("	") || reservation_input.contains(" ")) {
                //for the format of input String
                input_value = reservation_input.trim().split("	");
            }else {
                System.out.println("입력하신 문자열이 올바르지 않습니다. <예약 희망일> + <tab> 1개 + <시간> + <tab> 1개 + <인원수>에 알맞은 형식으로 문자열을 입력해주세요!\n");
                continue;
            }
            if(input_value[0].matches("[0-9]{4,4}"+"-"+"[0-9]{2,2}"+"-"+"[0-9]{2,2}") || input_value[0].matches("[0-9]{8,8}")) {
                //for the format of reservation date
                reserv_date = input_value[0];
            }else {
                System.out.println("입력하신 문자열이 올바르지 않습니다. <예약 희망일> + <tab> 1개 + <시간> + <tab> 1개 + <인원수>에 알맞은 형식으로 문자열을 입력해주세요!\n");
                continue;
            }
            if(input_value[1].matches("[0-9][0-9]") || input_value[1].matches("[0-9][0-9]:00") || input_value[1].matches("[0-9][0-9]시")) {
                //for the format of reservation time
                reserv_time = input_value[1];
            }else {
                System.out.println("입력하신 문자열이 올바르지 않습니다. <예약 희망일> + <tab> 1개 + <시간> + <tab> 1개 + <인원수>에 알맞은 형식으로 문자열을 입력해주세요!\n");
                continue;
            }
            if(input_value[2].matches("[0-9]{1,2}")) { //for the format of reservation count
                reserv_count = input_value[2];
            }else {
                System.out.println("입력하신 문자열이 올바르지 않습니다. <예약 희망일> + <tab> 1개 + <시간> + <tab> 1개 + <인원수>에 알맞은 형식으로 문자열을 입력해주세요!\n");
                continue;
            }

            sub_time = reserv_time.substring(0, 2);

            if((reserv_date.contentEquals(date_format.plusDays(1).toString()) || reserv_date.replace("-", "").contentEquals(date_format.plusDays(1).toString().replace("-", ""))
                    || reserv_date.contentEquals(date_format.plusDays(2).toString()) || reserv_date.replace("-", "").contentEquals(date_format.plusDays(2).toString().replace("-", "")))
                    && (Integer.parseInt(sub_time) >= 10 && Integer.parseInt(sub_time) <= 20) && Integer.parseInt(reserv_count) >=1 && Integer.parseInt(reserv_count) <= 12) {
                //check if value inputed fits data rule
                this.date = reserv_date;
                this.time = sub_time;

                if(search_table(reserv_date, sub_time, reserv_count).isBlank() == false) {
                    //check if value inputed is available for reservation
                    break;
                }else {
                    System.out.println("입력하신 시간대에 해당 인원수가 앉을 수 있는 좌석이 존재하지 않습니다. 다른 시간대를 입력해주시기 바랍니다.\n");
                    continue;
                }
            }else {
                System.out.println("입력하신 문자열이 올바르지 않습니다. 예약은 예약접수일 당일을 제외하고 +2일까지 가능하며, 입력 가능한 시간은 10:00 ~ 20:00입니다.\n");
                continue;
            }
        }
        show_table(reserv_date, sub_time, reserv_count);
    }

    /*
     * search for available table with inputed day, time, count
     */
    private String search_table(String date, String time, String count){
    	fio.read_file(date.replaceAll("-", ""));
    	String[][][]
    			tmp = fio.tb.get_day();

    	int attached_table = 0;

    	StringBuilder available_tables = new StringBuilder();
    	//should split it with space(" ") to check each available table number
    	//for tables attached, format is "(table number)-(table number)"

    	int tmp_count = 1; //index no. of available number of people to use table
    	int tmp_table = 0; //table number
    	int tmp_time = Integer.parseInt(time) - 10;

    	for(tmp_table = 0; tmp_table < 20; tmp_table++) { //check if there are any table available with chosen day,time,count with availability(3rd value)
    		if(tmp[2][tmp_time][tmp_table].equals("0")) { //if number of people using table is 0
    			if(Integer.parseInt(tmp[tmp_count][tmp_time][tmp_table]) >= Integer.parseInt(count)){
    				//compare available number or people to use table with inputed number of people
    				if(Integer.parseInt(count) < 3 && (tmp[tmp_count][tmp_time][tmp_table]).contentEquals("4") || tmp[tmp_count][tmp_time][tmp_table].equals("6")) {
    					//if current number of people is less than 3, cannot use table for 4 or 6 people
    					continue;
    				}else if(Integer.parseInt(count) < 4 && tmp[tmp_count][tmp_time][tmp_table].contentEquals("6")){
    					//if current number of people is less than 4, cannot use table for 6 people
    					continue;
    				}else { //add string in available_tables
    					available_tables.append(tmp_table);
    					available_tables.append(" ");
    				}
    			}else {
    				continue;
    			}
    		}else {
    			continue;
    		}
    	}
    	for(attached_table = 0; attached_table < 20; attached_table += 2) { //tables can be attached
    		if(tmp[2][tmp_time][attached_table].contentEquals("0") && tmp[2][tmp_time][attached_table++].contentEquals("0")) {
				//if attached tables are both empty
    			if(attached_table >= 0 && attached_table < 8) {
    				//table 1~8
    				if(Integer.parseInt(count) > 2 && Integer.parseInt(count) < 5) {
    					//only more than 2, less than 5 people can use attached tables for 4 people
    					available_tables.append(attached_table);
    					available_tables.append("-");
    					available_tables.append(Integer.valueOf(attached_table) + 1);
    					available_tables.append(" ");
    				}else {
    					continue;
    				}
    			}else if (attached_table >= 8 && attached_table <16) {
    				if(Integer.parseInt(count) > 4 && Integer.parseInt(count) < 9) {
        				//only more than 4, less than 9 people can use attached tables for 8 people
    					available_tables.append(attached_table);
    					available_tables.append("-");
    					available_tables.append(Integer.valueOf(attached_table) + 1);
    					available_tables.append(" ");
        			}else {
        				continue;
        			}
    			}else {
    				if(Integer.parseInt(count) > 8) {
    					//only more than 8 people can use attached tables for 12 people
    					available_tables.append(attached_table);
    					available_tables.append("-");
    					available_tables.append(Integer.valueOf(attached_table) + 1);
    					available_tables.append(" ");
    				}else {
    					continue;
    				}
    			}
    		}
    	}
		return available_tables.toString();
    }

	private void show_table(String date, String time, String count) {
		String[] tmp_date_arr = new String[3];
		tmp_date_arr[0] = date.substring(0, 4);
		tmp_date_arr[1] = date.substring(5, 7);
		tmp_date_arr[2] = date.substring(8, 10);
		//split inputed reservation date into yyyy mm dd format

		String avail_table = search_table(date, time, count);
		//has no. of available tables

		for(int table_num = 1; table_num <= 20; table_num++) {

			if(avail_table.contains(Integer.toString(table_num))) {
				if(table_num < 5) {

				}
			}
		}

		System.out.println(tmp_date_arr[0] + "년 " + tmp_date_arr[1] + "월 " + tmp_date_arr[2] + "일 " + time + "시 예약 가능 좌석 현황");
		System.out.println("--------------------------------------------------");
		System.out.println("");
	}


	/*좌석을 자동으로 할당받을 것인지 수동으로 선택할 것인지 결정합니다.*/
    public void choose_auto(){

    	System.out.println("자동으로 할당하시겠습니까(y/n)?: ");
    	Scanner sc= new Scanner(System.in);
    	String st = sc.next();

    	while(st.matches("^y$ | ^n$")){
			System.out.println("자동 선택 답변은 단문자 'y' 흑은 'n'만 가능합니다 다시입력해주세요");
		}

		if(st =="y"){
		    System.out.println("자동할당을 선택하셨습니다.");
		    auto_table();
	    }
		else {
			System.out.println("수동선택을 선택하셨습니다.");
			choose_table();
		}
    	sc.close();
    }

	public void choose_table() {
    	show_table(date,time,count);
		String available = search_table(date,time,count);
		String[] available_table =  available.split(" ");
    	System.out.println("좌석을 선택하세요!: ");
	}

	public void auto_table() {
		show_table(date,time,count);

	}

	private void input_inform() {
    	System.out.println("이름과 휴대전화 번호를 입력하세요");
    	Scanner sc = new Scanner(System.in);



	}




	   public void choose_menu() {
	      File_IO file = new File_IO();
	      Scanner scan = new Scanner(System.in);
	      String patterns0 = "^[가-힣]*";
	      String patterns1 = "[0-9]";
	      String patterns2 = "[a-zA-Z]";
	      String patterns3 = "\t";

	      //메뉴 파일 menu이차원 배열에 저장
	      file.read_menu();
	      menu = file.tb.get_menu();

	      while (true) {
	         // 화면 출력
	         String temp_num;
	         stock_result_index = 0;
	         price = 0;
	         System.out.println("--------------------------");
	         System.out.println("\t메뉴 선택");
	         System.out.println("--------------------------");
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
	         //주문수량 입력받기
	         temp_num = scan.nextLine();
	         if (temp_num.contains("\t") || temp_num.contains("")) {
	            str_menu_num = temp_num.trim().split("\t");
	         }
	         // 문법 규칙 위배시
	         if (temp_num.matches(patterns0 + patterns3 + patterns1 + patterns3 + patterns1 + patterns3 + patterns1
	               + patterns3 + patterns1 + patterns3 + patterns1 + "|"+patterns2 + patterns3 + patterns3 + patterns1
	               + patterns3 + patterns1 + patterns3 + patterns1 + patterns3 + patterns1 + patterns3 + patterns1)) {
	            System.out.println("주문입력 형식에 오류가 있습니다. 입력 방식은 (ex.\t2\t3\t0\t0\t0) 형식입니다 ");
	            continue;
	         }
	         //사용자가 입력한 주문 수량 integer 배열에 저장
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
	   //주문시간대 체크
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
	   //재고 체크
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
	   //메뉴 확정
	   private void menu_confirm() {

	      Scanner scan1 = new Scanner(System.in);

	      while (true) {
	    	 System.out.println("------------------------------");
	    	 System.out.println("\t주문 내역 확인");
	    	 System.out.println("------------------------------");
	         System.out.println("[메뉴]\t[가격]\t[주문 수량]");
	         for (int i = 0; i < menu.length; i++) {
	            System.out.println(menu[0][i] + "\t\\" + menu[1][i] + "\t" + str_menu_num[i]);
	            price += Integer.parseInt(menu[1][i]) * int_menu_num[i];
	         }
	         System.out.println();

	         System.out.println("결제 예정 금액: " + price);
	         System.out.print("주문 내역을 확정하고 주문을 완료하시겠습니까?(y/n) :");
	         String menu_confirm = scan1.next();
	         String[] yorn_value = new String[0];

	         if (menu_confirm.contains(" ") || menu_confirm.contains("")) {
	            yorn_value = menu_confirm.trim().split(" ");
	         }

	         if (yorn_value[0].equals("y")) {
	            String str = "";
	            for (int i = 0; i < menu.length; i++) {
	               if (int_menu_num[i] != 0) {
	                  str += menu[0][i] + " ";
	               }
	            }
	            System.out.println(str + "를(을) 주문합니다.");
	            reservation_confirm();
	            break;
	         } else if (yorn_value[0].equals("n")) {
	            choose_menu();
	            break;
	         } else {
	            System.out.println("입력은 y 혹은 n만 가능합니다. 다시 입력해주세요.");
	            continue;
	         }
	      }

	   }
	   //예약 확정
	   private void reservation_confirm() {

	      Scanner scan2 = new Scanner(System.in);

	      //이 부분은 위쪽이 완료되면 해당 변수로 채우면 됨 - 현재 테이블 번호 없음
	      while (true) {

	         System.out.println("\n--------------------------\n예약 내역 확인\n------------------------------");
	         System.out.println("예약자 이름: " + this.name);
	         System.out.println("전화번호: " + this.phone);
	         System.out.println("예약 시간: " + this.time + ":00 ~ " + (Integer.parseInt(this.time) + 2) + ":00");
	         System.out.println("인원 수: " + this.count);
	         System.out.println("예약 좌석: " + 5);//this.table);
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
	         String[] yorn_value = new String[0];

	         if (reservation_confirm.contains(" ") || reservation_confirm.contains("")) {
	            yorn_value = reservation_confirm.trim().split(" ");
	         }

	         if (yorn_value[0].equals("y")) {
	            System.out.println("예약이 완료되었습니다.");
	            out_reservation_data();
	            break;
	         } else if (yorn_value[0].equals("n")) {
	            reservation_cancle_confirm();
	            break;
	         } else {
	            System.out.println("입력은 y 혹은 n만 가능합니다. 다시 입력해주세요.");
	            continue;
	         }
	      }

	   }
	   //예약 취소
	   private void reservation_cancle_confirm() {
	      Scanner scan3 = new Scanner(System.in);


	      while (true) {
	    	 System.out.println("--------------------------------");
	    	 System.out.println("\t예약 취소 확인");
	    	 System.out.println("--------------------------------");
	         System.out.println("예약 취소시, 모든 예약 정보가 삭제됩니다.");
	         System.out.print("정말 예약을 취소하시겠습니까?(y/n): ");

	         String reservation_cancel = scan3.next();
	         String[] yorn_value = new String[0];
	         if (reservation_cancel.contains(" ") || reservation_cancel.contains("")) {
	            yorn_value = reservation_cancel.trim().split(" ");
	         }

	         if (yorn_value[0].equals("y")) {
	            System.out.println("예약이 취소되었습니다.");
	            break;
	         } else if (yorn_value[0].equals("n")) {
	            reservation_confirm();
	            break;
	         } else {
	            System.out.println("입력은 y 혹은 n만 가능합니다. 다시 입력해주세요.");
	            continue;
	         }
	      }
	   }
	   //예약 확정후 파일에 새로운 정보 저장
	   private void out_reservation_data() {
	      File_IO file2 = new File_IO();
	      // 예약정보 file에 저장
	      file2.write_file(this.date);//테이블번호));
	      // 메뉴파일에서 메뉴이름에 해당하는 메뉴의 메뉴 재고 주문 수량만큼 제외
	      file2.read_menu();
	      menu = file2.tb.get_menu();
	      for (int i = 0; i < 5; i++) {
	         if (int_menu_num[i] != 0) {
	            int origin_stock = Integer.parseInt(menu[2][i]);
	            int new_stock = origin_stock - int_menu_num[i];
	            menu[2][i] = Integer.toString(new_stock);
	            //재고 정보 update
	            file2.tb.set_menu(menu);
	            file2.write_menu();
	         }
	      }
	   }
}
