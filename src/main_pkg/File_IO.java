package main_pkg;
import main_pkg.textDB;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class File_IO {



    textDB tb = new textDB();
    /*파일을 읽어 textDB에 String[][][]형식으로 저장합니다*/
    public void read_file(String date){

        try{
            /*"date.txt"라는 명칭의 파일을 "src/data/"경로에서 로드합니다.*/
            File file = new File("src/data/"+date+".txt");
            FileReader file_reader = new FileReader(file);
            BufferedReader buffered_reader = new BufferedReader(file_reader);
            String line = " ";
            String[][][] temp = new String[11][11][20];
            int time=0,table=0;
            /*빈라인이(null)이 나타날 때까지 한줄 씩읽어 버퍼에 저장합니다*/
            while( (line=buffered_reader.readLine()) != null){
                /*읽은 라인을 공백(" ")을 기준으로 분할하여 line_split[]에 넣어줍니다*/
                String[] line_split = line.split(" ");
                /*time이 10보다 커지면,(20시까지 모두 정보를 채웠다면) 다음 table을 1증가시킵니다.*/
                if(time > 10){
                    time = 0;
                    table++;
                }
                else{
                    /*time이 10보다 작다면,line_split을 temp에 채워줍니다.*/
                    for(int i=0;i<line_split.length;i++){
                        /*예약정보가 없는 칸이 발생하면 즉시 루프를 탈출합니다*/
                        if(line_split[i] == null){
                            break;
                        }
                        else{
                            temp[i][time][table] = line_split[i];
                        }
                    }
                    /*time을 1증가시켜 다음 시간대의 정보를 기록하도록 합니다.*/
                    time++;
                }
            }
            /*textDB에 저장합니다.*/
            tb.setday(temp);
            /*모든 작업을 끝내고 버퍼를 닫습니다.*/
            buffered_reader.close();
        }catch (FileNotFoundException e) {
            System.out.println(e);
        }catch(IOException e){
            System.out.println(e);
        }
    }
    /*textDB에 String[][][] today에 변경된 내용을 텍스트 파일에 저장합니다*/
    /*time은 0~10까지의 값이며 table은 0~19까지의 값입니다*/
    public void write_file(String date,int time, int table) {
        /*"date.txt"라는 명칭의 파일을 "src/data/"경로에서 로드합니다.*/
        try{
            File file = new File("src/data/"+date+".txt");
            FileReader file_reader = new FileReader(file);
            BufferedReader buffered_reader = new BufferedReader(file_reader);
            /*position+1 줄에 존재하는 라인의 인덱스입니다*/

            int position = (11*(table)+(time));

            String line="";
            String temp="";
            String change_line="";
            /*textDB에 저장합니다.*/
            for (int i= 0; i<11; i++){
                change_line += tb.getday()[i][time][table]+" ";
            }
            change_line += "\r\n";
            /*position 줄 이전까지의 내용을 임시 String에 저장합니다.*/
            for(int i=0;i<position;i++){
                buffered_reader.readLine();
                temp += (line +"\r\n");
            }
            /*임시 String에 position 줄을 읽어서 지나칩니다.*/
            buffered_reader.readLine();
            temp += change_line;
            /*그 이후 줄을 읽고 임시 String에 저장합니다.*/
            while ((line = buffered_reader.readLine()) != null){
                temp += (line +"\r\n");
            }
            file_reader.close();
            buffered_reader.close();

            /*해당 file에 임시 String을 덮어 씌웁니다.*/
            FileWriter file_writer = new FileWriter(file);
            file_writer.write(temp);

            file_writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    	File file = new File(data_directory);  // directory에 존재하는 모든 파일의 이름을 list에 저장
        String[] files = file.list();

        for(int i=0 ; i < files.length ; i++){ /// list에 저장된 파일의 개수만큼 반복
        	String file_name = files[i];
            file_name = file_name.replace(".txt", ""); // 파일의 이름(날짜)를 현재 날짜와 비교하기 위해 형변환을 진행
            int file_date = Integer.parseInt(file_name);

            if(file_date<int_today) { // 만약 과거의 데이터 파일이 존재한다면,
            	System.out.println(("데이터 경로에 과거의 <예약 정보 파일 : "+files[i]+">이 존재합니다. 해당 데이터를 삭제합니다."));
            	File file_ = new File(data_directory+"\\"+files[i]);
            	file_.delete(); // 파일을 삭제한다.
             }
        }

    }
}
