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
    
    private void create_file(){ // 필요한 파일이 없을 때, 파일을 생성하는 메소드
    	String user_name = new com.sun.security.auth.module.NTSystem().getName();	// 사용자 이름
    	String directory =  "C:\\Users\\"; 
    	String home_directory =  directory + user_name ; // {HOME}경로
    	String data_directory =  home_directory + "\\data" ; // data경로
    		
    	String days[] = new String[3]; // 현재 날짜로부터 3일까지를 생성하여 string타입으로 저장합니다. ex) 20201023 
		days[0] = get_date(0);
		days[1] = get_date(1);
		days[2] = get_date(2);
		
		for(int k = 0; k<3; k++) { // 오늘, 내일, 모레 총 3일간의 데이터파일을 확인하여 없을 시 생성해줍니다. 
			String filename = "\\"+days[k]+".txt"; 
			File file = new File(data_directory+filename);
			
			boolean file_is_exist = file.exists(); 
			if(!file_is_exist) { // 해당 파일이 존재하지 않는다면,
				try {
					System.out.println("데이터 경로에 필요한 <예약 정보 파일 : "+days[k]+".txt>이 존재하지 않습니다. 필요한 데이터 파일을 생성합니다.");
					file.createNewFile(); // 해당 파일을 생성합니다. 
					FileWriter fw = new FileWriter(file, false); // 데이터 파일에 기본 형식을 입력합니다.
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
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
    }
    
    private void delete_file(){ // 과거의 데이터 파일이 존재할 때, 삭제하는 메소드
    	String user_name = new com.sun.security.auth.module.NTSystem().getName(); // 사용자 이름
    	String directory =  "C:\\Users\\";
    	String home_directory =  directory + user_name ;
    	String data_directory =  home_directory + "\\data" ;
    
    	String str_today = get_date(0); // 오늘 날짜를 string타입으로 저장
    	int int_today = Integer.parseInt(str_today); // 과거의 날짜와 비교하기 위해 오늘의 날짜를 int 타입으로 저장
    	
    	File file = new File(data_directory); // directory에 존재하는 모든 파일의 이름을 list에 저장
        String[] files = file.list();
       
        for(int i=0 ; i < files.length ; i++){ // list에 저장된 파일의 개수만큼 반복
        	String file_name = files[i];
            file_name = file_name.replace(".txt", ""); // 파일의 이름(날짜)를 현재 날짜와 비교하기 위해 형변환을 진행
            int file_date = Integer.parseInt(file_name);
            
            if(file_date<int_today) { // 만약 과거의 데이터 파일이 존재한다면,
            	System.out.println("데이터 경로에 과거의 <예약 정보 파일 : "+files[i]+">이 존재합니다. 해당 데이터를 삭제합니다.");
            	File file_ = new File(data_directory+"\\"+files[i]);
            	file_.delete(); // 파일을 삭제한다. 	
             }
        }


    	
    }

	
}
