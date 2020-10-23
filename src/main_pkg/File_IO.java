package main_pkg;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import main_pkg.textDB;

public class File_IO {
	
	// 현재 날짜로부터 n일 뒤의 날짜를 구하는 메소드
	private String get_date(int n) {
		SimpleDateFormat new_format = new SimpleDateFormat("yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		Date today = new Date(System.currentTimeMillis());
		cal.setTime(today);
		cal.add(Calendar.DATE, n);
		String date = new_format.format(cal.getTime());
		return date;
	}

    private void read_file(String directory,String name) {

    }
    private void write_file(String directory, String name,String line) {

    }

    void create_file(){
//    	String user_name = "konkuk";
//    	String directory =  "C:\\Users\\";
//    	String home_directory =  directory + user_name ;
//    	String data_directory =  home_directory + "\\data" ;
    	String data_directory =  "data\\" ;
    		
    	String days[] = new String[3];
		days[0] = get_date(0);
		days[1] = get_date(1);
		days[2] = get_date(2);
		
		for(int k = 0; k<3; k++) {
			String filename = days[k]+".txt";
			File file = new File(data_directory+filename);
			boolean file_is_exist = file.exists();
			
			if(!file_is_exist) {
				try {
					file.createNewFile();
					FileWriter fw = new FileWriter(file, false);
					String line = "";
					for(int i = 1; i<=20; i++) {
						for(int j = 10; j<= 20; j++) {
							line += i + " ";
							if(1<=i && i<=6) line += 2 + " ";
							if(7<=i && i<=16) line += 4 + " ";
							if(17<=i && i<=20) line += 6 + " ";
							line += 0 + " " + j + "\n";
						}
						fw.write(line);
						line = "";
					}
					fw.close();
					System.out.println("데이터 경로에 필요한 <예약 정보 파일 : "+days[k]+".txt>이 존재하지 않습니다. 필요한 데이터 파일을 생성합니다.");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}
			
	

    }
    void delete_file(){
//    	String user_name = "konkuk";
//    	String directory =  "C:\\Users\\";
//    	String home_directory =  directory + user_name ;
//    	String data_directory =  home_directory + "\\data" ;
    	String data_directory =  "data\\" ;
    	String str_today = get_date(0);
    	int int_today = Integer.parseInt(str_today);
    	
    	File file = new File(data_directory);
        String[] files = file.list();
       
        for(int i=0 ; i < files.length ; i++){
        	String file_name = files[i];
            file_name = file_name.replace(".txt", "");
            int file_date = Integer.parseInt(file_name);
            
            if(file_date<int_today) {
            	File file_ = new File(data_directory+files[i]);
            	file_.delete();
            	System.out.println("데이터 경로에 과거의 <예약 정보 파일 : "+files[i]+">이 존재합니다. 해당 데이터를 삭제합니다.");
             }
        }


    	
    }

	
}
