package main_pkg;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Scanner;

import main_pkg.textDB;

public class Reservation {

    private String count, date, day, time;
    private String name;
    private String phone;
    private String st_num0;
    private String st_num1;
    private String menu1_num;
    private String menu2_num;
    private String menu3_num;
    private String menu4_num;
    private String menu5_num;
    
    File_IO fio = new File_IO();

    /*
     * print the 'today', user can input date, time, number of people
     */
    public void user_input(){
    	
    	LocalDate date_format = LocalDate.now();
    	String today = date_format.toString();

		String reserv_date, reserv_time, reserv_count;
    	
    	Scanner scanner = new Scanner(System.in);
    	
    	while(true) {
    		System.out.println("예약 접수일: " + today);
    		System.out.println("예약 희망 일자와 시간, 그리고 방문하는 인원수를 차례대로 입력하세요.");
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
			
			String sub_time = reserv_time.substring(0, 2);
    	
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
    			System.out.println("입력하신 문자열이 올바르지 않습니다.예약은 예약접수일 당일을 제외하고 +2일까지 가능하며, 입력 가능한 시간은 10:00 ~ 20:00입니다.\n");
    			continue;
    		}
    	}
    	
    	show_table(reserv_date, reserv_time, reserv_count);
    }

    /*
     * search for available table with inputed day, time, count
     */
    private String search_table(String date, String time, String count){
    	fio.read_file(date.replaceAll("-", ""));
    	String[][][] tmp = fio.tb.get_day();
    	
    	int table1 = 0, table2 = table1 + 1;
    	
    	StringBuilder available_tables = new StringBuilder();
    	//should split it with space(" ") to check each available table number
    	//for tables attached, format is "(table number)-(table number)"
    	
    	int tmp_count = 1; //index no. of available number of people to use table 
    	int tmp_table = 0; //table number
    	int tmp_time = Integer.parseInt(time) - 10;
    	
    	for(tmp_table = 0; tmp_table < 20; tmp_table++) { //check if there are any table available with chosen day,time,count with availability(3rd value)
    		System.out.println("a" + tmp_table);
    		if(tmp[2][tmp_time][tmp_table].equals("0")) { //if number of people using table is 0
    			System.out.println("b" + tmp_table);
    			if(Integer.parseInt(tmp[tmp_count][tmp_time][tmp_table]) >= Integer.parseInt(count)){
    				//compare available number or people to use table with inputed number of people
    				System.out.println("c" + tmp_table);
    				if(Integer.parseInt(count) < 3 && (tmp[tmp_count][tmp_time][tmp_table]).contentEquals("4") || tmp[tmp_count][tmp_time][tmp_table].equals("6")) {
    					//if current number of people is less than 3, cannot use table for 4 or 6 people
    					System.out.println("d" + tmp_table);
    					continue;
    				}else if(Integer.parseInt(count) < 4 && tmp[tmp_count][tmp_time][tmp_table].contentEquals("6")){
    					//if current number of people is less than 4, cannot use table for 6 people
    					System.out.println("e" + tmp_table);
    					continue;
    				}else { //add string in available_tables
    					System.out.println("f" + tmp_table);
    					available_tables.append(tmp_table);
    					available_tables.append(" ");
    				}
    			}else {
    				System.out.println("g" + tmp_table);
    				continue;
    			}
    		}else {
    			System.out.println("h" + tmp_table);
    			continue;
    		}
    	}
    	for(table1 = 0; table1 < 20; table1 += 2) { //tables can be attached
    		if(tmp[2][tmp_time][table1].contentEquals("0") && tmp[2][tmp_time][table2].contentEquals("0")) {
				//if attached tables are both empty
    			if(table1 >= 0 && table1 < 8) {
    				//table 1~8
    				if(Integer.parseInt(count) > 2 && Integer.parseInt(count) < 5) {
    					//only more than 2, less than 5 people can use attached tables for 4 people
    					available_tables.append(table1);
    					available_tables.append("-");
    					available_tables.append(table2);
    					available_tables.append(" ");
    				}else {
    					continue;
    				}
    			}else if (table1 >= 8 && table1 <16) {
    				if(Integer.parseInt(count) > 4 && Integer.parseInt(count) < 9) {
        				//only more than 4, less than 9 people can use attached tables for 8 people
    					available_tables.append(table1);
    					available_tables.append("-");
    					available_tables.append(table2);
    					available_tables.append(" ");
        			}else {
        				continue;
        			}
    			}else {
    				if(Integer.parseInt(count) > 8) {
    					available_tables.append(table1);
    					available_tables.append("-");
    					available_tables.append(table2);
    					available_tables.append(" ");
    				}else {
    					continue;
    				}
    			}
    		}
    	}
		return available_tables.toString();
    }

    private void show_table(String day,String time,String count){

    }

    private boolean choose_auto(char c){

        return false;
    }

    private void choose_table(String st_num){

    }

    private String auto_table(String count){
        String auto ="";
        return auto;
    }

    private void input_inform(String name, String phone){

    }

    private void choose_menu(){

    }

    private boolean menu_time_check(){

        return false;
    }

    private boolean menu_stock_check(){
        return false;
    }

    private boolean menu_confirm(){
        return false;
    }

    private boolean reservation_confirm(){
        return false;
    }

    private boolean reservation_cancle_confirm(){
        return false;
    }
    
    private void out_reservation_data(){

    }
}
