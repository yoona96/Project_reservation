package main_pkg;
import main_pkg.textDB;
import java.io.*;

public class File_IO {

    textDB tb = new textDB();
    /*파일을 읽어 textDB에 저장합니다*/
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
    /*textDB의 내용을 파일에 덮어씌웁니다.특정 시간, 테이블이 들어간 줄만 바꿉니다.*/
    public void write_file(String date,int time, int table) {
        /*"date.txt"라는 명칭의 파일을 "src/data/"경로에서 로드합니다.*/
        try{
            File file = new File("src/data/"+date+".txt");
            FileReader file_reader = new FileReader(file);
            BufferedReader buffered_reader = new BufferedReader(file_reader);
            /*position+1 줄에 존재하는 라인의 인덱스입니다*/
            int position = (11*(table-1)+(time-10));

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

    private void create_file(){

    }
    private void delete_file(){

    }
}
