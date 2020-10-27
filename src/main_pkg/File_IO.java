package main_pkg;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import main_pkg.textDB;
import java.io.*;




public class File_IO {


    public String get_date(int n) { // Get date(today + 'n') 
        SimpleDateFormat new_format = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        Date today = new Date(System.currentTimeMillis());
        cal.setTime(today);
        cal.add(Calendar.DATE, n);
        String date = new_format.format(cal.getTime());
        return date;
    }

    public String get_home_directory() {	// Get home data_directory
        String OS = System.getProperty("os.name").toLowerCase();	// Get current OS name
        String user_name = new com.sun.security.auth.module.NTSystem().getName(); // Get current User name 
        String home_directory = "";		// default directory = null
        if (OS.indexOf("win") >= 0)		
            home_directory = "C:\\Users\\";		// home directory in window
        if (OS.indexOf("mac") >= 0)
            home_directory = "/home/"; 	// home directory in mac
        if (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0)
            home_directory = "/Users/";		// home directory in linux
        home_directory += user_name;		
        return home_directory;

    }

    textDB tb = new textDB();


    public void read_file(String date) {

        try {

            File file = new File("src/data/" + date + ".txt");
            FileReader file_reader = new FileReader(file);
            BufferedReader buffered_reader = new BufferedReader(file_reader);
            String line = " ";
            String[][][] temp = new String[11][11][20];
            int time = 0, table = 0;

            while ((line = buffered_reader.readLine()) != null) {

                String[] line_split = line.split("\t");

                if (time > 10) {
                    time = 0;
                    table++;
                } else {

                    for (int i = 0; i < line_split.length; i++) {

                        if (line_split[i] == null) {
                            break;
                        } else {
                            temp[i][time][table] = line_split[i];
                        }
                    }

                    time++;
                }
            }

            tb.set_day(temp);

            buffered_reader.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
    }


    public void write_file(String date, int time, int table) {

        try {
            File file = new File("src/data/" + date + ".txt");
            FileReader file_reader = new FileReader(file);
            BufferedReader buffered_reader = new BufferedReader(file_reader);


            int position = (11 * (table) + (time));

            String line = "";
            String temp = "";
            String change_line = "";

            for (int i = 0; i < 11; i++) {
                change_line += tb.get_day()[i][time][table] + "\t";
            }
            change_line += "\r\n";

            for (int i = 0; i < position; i++) {
                buffered_reader.readLine();
                temp += (line + "\r\n");
            }

            buffered_reader.readLine();
            temp += change_line;

            while ((line = buffered_reader.readLine()) != null) {
                temp += (line + "\r\n");
            }
            file_reader.close();
            buffered_reader.close();


            FileWriter file_writer = new FileWriter(file);
            file_writer.write(temp);

            file_writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void read_menu() {		
        try {

            File file = new File("src/data/menu.txt");
            FileReader file_reader = new FileReader(file);
            BufferedReader buffered_reader = new BufferedReader(file_reader);
            String line = " ";
            String[][] temp = new String[4][5];
            int menu_num = 0;

            while ((line = buffered_reader.readLine()) != null) {

                String[] line_split = line.split("\t");
                for (int i = 0; i < line_split.length; i++) {
                    temp[i][menu_num] = line_split[i];
                }
                menu_num++;
            }

            tb.set_menu(temp);

            buffered_reader.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void write_menu(){
        try {
            File file = new File("src/data/menu.txt");
            FileWriter file_writer = new FileWriter(file);
            String line = "";
            String temp = "";
            String change_line = "";
            for (int k=0;k<5;k++){
                for (int i = 0; i < 4; i++) {
                    change_line += tb.get_menu()[i][k] + "\t";
                }
                change_line += "\r\n";
            }

           temp = change_line;
           file_writer.write(temp);
           file_writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void create_file() {		 
        String data_directory = "src/data/";

        String days[] = new String[3];		// Save the 3-days dates in the array.
        days[0] = get_date(0);
        days[1] = get_date(1);
        days[2] = get_date(2);

        for (int k = 0; k < 3; k++) {
            String filename = days[k] + ".txt";
            File file = new File(data_directory + filename);

            boolean file_is_exist = file.exists(); 
            if (!file_is_exist) {
                try {
                    System.out.println("데이터 경로에 필요한 <예약 정보 파일 : " + days[k]+ ".txt>이 존재하지 않습니다. 필요한 데이터 파일을 생성합니다.");
                    file.createNewFile();		// if the file isn't exist, create file
                    FileWriter fw = new FileWriter(file, false);
                    String line = "";
                    for (int i = 1; i <= 20; i++) {
                        for (int j = 10; j <= 20; j++) {
                            line += i + "\t";
                            if (1 <= i && i <= 6)
                                line += 2 + "\t";
                            if (7 <= i && i <= 16)
                                line += 4 + "\t";
                            if (17 <= i && i <= 20)
                                line += 6 + "\t";
                            line += 0 + "\t" + j + "\n";
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

    public void delete_file() {
        String data_directory = "src/data/";

        String str_today = get_date(0);		// get today's date
        int int_today = Integer.parseInt(str_today);	// change to integer type for comparison
        File file = new File(data_directory);
        String[] files = file.list(); // Store a list of files in the data_directory in an array.

        for (int i = 0; i < files.length; i++) {
            String file_name = files[i];
            file_name = file_name.replace(".txt", ""); // remove ".txt"
            int file_date = Integer.parseInt(file_name); // 
            //TODO Fix all text encoding to UTF-8
            if (file_date < int_today) {
                System.out.println("데이터 경로에 과거의 <예약 정보 파일 : " + files[i] + ">이 존재합니다. 해당 데이터를 삭제합니다.");
                File file_ = new File(data_directory + "\\" + files[i]);
                file_.delete();
            }
        }
          String test = new String();
    }
}