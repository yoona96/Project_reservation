package main_pkg;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import main_pkg.textDB;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class File_IO {

    // ���� ��¥�κ��� n�� ���� ��¥�� ���ϴ� �޼ҵ�
    private String get_date(int n) {
        SimpleDateFormat new_format = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        Date today = new Date(System.currentTimeMillis());
        cal.setTime(today);
        cal.add(Calendar.DATE, n);
        String date = new_format.format(cal.getTime());
        return date;
    }

    private String get_home_directory() {
        String OS = System.getProperty("os.name").toLowerCase();
        String user_name = new com.sun.security.auth.module.NTSystem().getName(); // ����� �̸�
        String home_directory = "";
        if (OS.indexOf("win") >= 0)
            home_directory = "C:\\Users\\";
        if (OS.indexOf("mac") >= 0)
            home_directory = "/home/";
        if (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0)
            home_directory = "/Users/konkuk";
        home_directory += user_name;
        return home_directory;

    }

    textDB tb = new textDB();

    /* 파일을 읽어 textDB에 String[][][]형식으로 저장합니다 */
    public void read_file(String date) {

        try {
            /* "date.txt"라는 명칭의 파일을 "src/data/"경로에서 로드합니다. */
            File file = new File("src/data/" + date + ".txt");
            FileReader file_reader = new FileReader(file);
            BufferedReader buffered_reader = new BufferedReader(file_reader);
            String line = " ";
            String[][][] temp = new String[11][11][20];
            int time = 0, table = 0;
            /* 빈라인이(null)이 나타날 때까지 한줄 씩읽어 버퍼에 저장합니다 */
            while ((line = buffered_reader.readLine()) != null) {
                /* 읽은 라인을 공백(" ")을 기준으로 분할하여 line_split[]에 넣어줍니다 */
                String[] line_split = line.split(" ");
                /* time이 10보다 커지면,(20시까지 모두 정보를 채웠다면) 다음 table을 1증가시킵니다. */
                if (time > 10) {
                    time = 0;
                    table++;
                } else {
                    /* time이 10보다 작다면,line_split을 temp에 채워줍니다. */
                    for (int i = 0; i < line_split.length; i++) {
                        /* 예약정보가 없는 칸이 발생하면 즉시 루프를 탈출합니다 */
                        if (line_split[i] == null) {
                            break;
                        } else {
                            temp[i][time][table] = line_split[i];
                        }
                    }
                    /* time을 1증가시켜 다음 시간대의 정보를 기록하도록 합니다. */
                    time++;
                }
            }
            /* textDB에 저장합니다. */
            tb.setday(temp);
            /* 모든 작업을 끝내고 버퍼를 닫습니다. */
            buffered_reader.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /* textDB에 String[][][] today에 변경된 내용을 텍스트 파일에 저장합니다 */
    /* time은 0~10까지의 값이며 table은 0~19까지의 값입니다 */
    public void write_file(String date, int time, int table) {
        /* "date.txt"라는 명칭의 파일을 "src/data/"경로에서 로드합니다. */
        try {
            File file = new File("src/data/" + date + ".txt");
            FileReader file_reader = new FileReader(file);
            BufferedReader buffered_reader = new BufferedReader(file_reader);
            /* position+1 줄에 존재하는 라인의 인덱스입니다 */

            int position = (11 * (table) + (time));

            String line = "";
            String temp = "";
            String change_line = "";
            /* textDB에 저장합니다. */
            for (int i = 0; i < 11; i++) {
                change_line += tb.getday()[i][time][table] + " ";
            }
            change_line += "\r\n";
            /* position 줄 이전까지의 내용을 임시 String에 저장합니다. */
            for (int i = 0; i < position; i++) {
                buffered_reader.readLine();
                temp += (line + "\r\n");
            }
            /* 임시 String에 position 줄을 읽어서 지나칩니다. */
            buffered_reader.readLine();
            temp += change_line;
            /* 그 이후 줄을 읽고 임시 String에 저장합니다. */
            while ((line = buffered_reader.readLine()) != null) {
                temp += (line + "\r\n");
            }
            file_reader.close();
            buffered_reader.close();

            /* 해당 file에 임시 String을 덮어 씌웁니다. */
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
            /* "date.txt"라는 명칭의 파일을 "src/data/"경로에서 로드합니다. */
            File file = new File("src/data/menu.txt");
            FileReader file_reader = new FileReader(file);
            BufferedReader buffered_reader = new BufferedReader(file_reader);
            String line = " ";
            String[][] temp = new String[5][5];
            int menu_num = 0;
            /* 빈라인이(null)이 나타날 때까지 한줄 씩읽어 버퍼에 저장합니다 */
            while ((line = buffered_reader.readLine()) != null) {
                /* 읽은 라인을 공백(" ")을 기준으로 분할하여 line_split[]에 넣어줍니다 */
                String[] line_split = line.split(" ");
                for (int i = 0; i < line_split.length; i++) {
                    temp[i][menu_num] = line_split[i];
                }
                menu_num++;
            }
            /* textDB에 저장합니다. */
            tb.setMenu(temp);
            /* 모든 작업을 끝내고 버퍼를 닫습니다. */
            buffered_reader.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void create_file() { // �ʿ��� ������ ���� ��, ������ �����ϴ� �޼ҵ�

        // String home_directory = get_home_directory(); // {HOME}���
        // String data_directory = home_directory + "\\data" ; // data���
        String data_directory = "src/data/";

        String days[] = new String[3]; // ���� ��¥�κ��� 3�ϱ����� �����Ͽ� stringŸ������ �����մϴ�. ex) 20201023
        days[0] = get_date(0);
        days[1] = get_date(1);
        days[2] = get_date(2);

        for (int k = 0; k < 3; k++) { // ����, ����, �� �� 3�ϰ��� ������������ Ȯ���Ͽ� ���� �� �������ݴϴ�.
            String filename = days[k] + ".txt";
            File file = new File(data_directory + filename);

            boolean file_is_exist = file.exists();
            if (!file_is_exist) { // �ش� ������ �������� �ʴ´ٸ�,
                try {
                    System.out.println("������ ��ο� �ʿ��� <���� ���� ���� : " + days[k]
                            + ".txt>�� �������� �ʽ��ϴ�. �ʿ��� ������ ������ �����մϴ�.");
                    file.createNewFile(); // �ش� ������ �����մϴ�.
                    FileWriter fw = new FileWriter(file, false); // ������ ���Ͽ� �⺻ ������ �Է��մϴ�.
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
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public void delete_file() { // ������ ������ ������ ������ ��, �����ϴ� �޼ҵ�
        // String home_directory = get_home_directory();
        // String data_directory = home_directory + "\\data" ;
        String data_directory = "src/data/";

        String str_today = get_date(0); // ���� ��¥�� stringŸ������ ����
        int int_today = Integer.parseInt(str_today); // ������ ��¥�� ���ϱ� ���� ������ ��¥�� int Ÿ������ ����

        File file = new File(data_directory); // directory�� �����ϴ� ��� ������ �̸��� list�� ����
        String[] files = file.list();

        for (int i = 0; i < files.length; i++) { // list�� ����� ������ ������ŭ �ݺ�
            String file_name = files[i];
            file_name = file_name.replace(".txt", ""); // ������ �̸�(��¥)�� ���� ��¥�� ���ϱ� ���� ����ȯ�� ����
            int file_date = Integer.parseInt(file_name);

            if (file_date < int_today) { // ���� ������ ������ ������ �����Ѵٸ�,
                System.out.println(
                        "������ ��ο� ������ <���� ���� ���� : " + files[i] + ">�� �����մϴ�. �ش� �����͸� �����մϴ�.");
                File file_ = new File(data_directory + "\\" + files[i]);
                file_.delete(); // ������ �����Ѵ�.
            }
        }

    }
}