package main_pkg;
import main_pkg.textDB;
import java.io.*;

public class File_IO {

    textDB tb = new textDB();
    /*파일을 읽어 textDB에 저장합니다*/
    public void read_file(String date,int dateType){

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
            /*dateType이 0일경우 오늘, 1일경우 내일, 2일경우 내일 모레의 db에 기록해줍니다.*/
            switch (dateType) {
                case 0:
                    tb.setToday(temp);
                    break;
                case 1:
                    tb.setTomorrow(temp);
                    break;
                case 2:
                    tb.setDay_after_tomorrow(temp);
                    break;
                default:
                    System.out.println("올바르지 않은 날짜 타입입니다. 데이터를 저장하는데 실패하였습니다.");
            }
            /*모든 작업을 끝내고 버퍼를 닫습니다.*/
            buffered_reader.close();
        }catch (FileNotFoundException e) {
            System.out.println(e);
        }catch(IOException e){
            System.out.println(e);
        }
    }
    /*textDB의 내용을 파일에 덮어씌웁니다.특정 시간, 테이블이 들어간 줄만 바꿉니다.*/
    private void write_file(String date, int dateType,int time, int table) {

        try{
            File file = new File("src/data/"+date+".txt");
            FileReader file_reader = new FileReader(file);
            BufferedReader buffered_reader = new BufferedReader(file_reader);

            int position = (11*(table-1)+(time-10));

            String line="";
            String temp="";
            String change_line="";

            switch (dateType) {
                case 0:
                    for (int i= 0; i<11; i++){
                        change_line += tb.getToday()[i][time][table];
                    }
                    break;
                case 1:
                    for (int i= 0; i<11; i++){
                        change_line += tb.getTomorrow()[i][time][table];
                    }
                    break;
                case 2:
                    for (int i= 0; i<11; i++){
                        change_line += tb.getDay_after_tomorrow()[i][time][table];
                    }
                    break;
                default:
                    System.out.println("올바르지 않은 날짜 타입입니다. 데이터를 저장하는데 실패하였습니다.");
            }

            for(int i=0;i<position;i++){
                buffered_reader.readLine();
                temp += (line +"\r\n");
            }

            temp += change_line;

            while ((line = buffered_reader.readLine()) != null){
                temp += (line +"\r\n");
            }

            FileWriter file_writer = new FileWriter(file);
            file_writer.write(temp);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void create_file(){

    }
    private void delete_file(){

    }
}
