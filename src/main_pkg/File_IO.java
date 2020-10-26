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

    // 占쏙옙占쏙옙 占쏙옙짜占싸븝옙占쏙옙 n占쏙옙 占쏙옙占쏙옙 占쏙옙짜占쏙옙 占쏙옙占싹댐옙 占쌨소듸옙
    public String get_date(int n) {
        SimpleDateFormat new_format = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        Date today = new Date(System.currentTimeMillis());
        cal.setTime(today);
        cal.add(Calendar.DATE, n);
        String date = new_format.format(cal.getTime());
        return date;
    }

    public String get_home_directory() {
        String OS = System.getProperty("os.name").toLowerCase();
        String user_name = new com.sun.security.auth.module.NTSystem().getName(); // 占쏙옙占쏙옙占� 占싱몌옙
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

    /* ���쇱�� �쎌�� textDB�� String[][][]�����쇰� ���ν�⑸���� */
    public void read_file(String date) {

        try {
            /* "date.txt"라는 명칭의 파일을 "src/data/"경로에서 로드합니다. */
            File file = new File("src/data/" + date + ".txt");
            FileReader file_reader = new FileReader(file);
            BufferedReader buffered_reader = new BufferedReader(file_reader);
            String line = " ";
            String[][][] temp = new String[11][11][20];
            int time = 0, table = 0;
            /* 鍮��쇱�몄��(null)�� ������ ��源�吏� ��以� �⑹�쎌�� 踰��쇱�� ���ν�⑸���� */
            while ((line = buffered_reader.readLine()) != null) {
                /* 읽은 라인을 공백(" ")을 기준으로 분할하여 line_split[]에 넣어줍니다 */
                String[] line_split = line.split("\t");
                /* time이 10보다 커지면,(20시까지 모두 정보를 채웠다면) 다음 table을 1증가시킵니다. */
                if (time > 10) {
                    time = 0;
                    table++;
                } else {
                    /* time�� 10蹂대�� ���ㅻ㈃,line_split�� temp�� 梨���以�����. */
                    for (int i = 0; i < line_split.length; i++) {
                        /* ���쎌��蹂닿� ���� 移몄�� 諛�����硫� 利��� 猷⑦��瑜� ��異��⑸���� */
                        if (line_split[i] == null) {
                            break;
                        } else {
                            temp[i][time][table] = line_split[i];
                        }
                    }
                    /* time�� 1利�媛���耳� �ㅼ�� ��媛����� ��蹂대�� 湲곕�����濡� �⑸����. */
                    time++;
                }
            }
            /* textDB�� ���ν�⑸����. */
            tb.setday(temp);
            /* 紐⑤�� ������ ���닿� 踰��쇰�� �レ�듬����. */
            buffered_reader.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /* textDB�� String[][][] today�� 蹂�寃쎈�� �댁�⑹�� ���ㅽ�� ���쇱�� ���ν�⑸���� */
    /* time�� 0~10源�吏��� 媛��대ŉ table�� 0~19源�吏��� 媛������� */
    public void write_file(String date, int time, int table) {
        /* "date.txt"�쇰�� 紐�移��� ���쇱�� "src/data/"寃쎈����� 濡����⑸����. */
        try {
            File file = new File("src/data/" + date + ".txt");
            FileReader file_reader = new FileReader(file);
            BufferedReader buffered_reader = new BufferedReader(file_reader);
            /* position+1 以��� 議댁�ы���� �쇱�몄�� �몃�깆�ㅼ������ */

            int position = (11 * (table) + (time));

            String line = "";
            String temp = "";
            String change_line = "";
            /* textDB�� ���ν�⑸����. */
            for (int i = 0; i < 11; i++) {
                change_line += tb.getday()[i][time][table] + "\t";
            }
            change_line += "\r\n";
            /* position 以� �댁��源�吏��� �댁�⑹�� ���� String�� ���ν�⑸����. */
            for (int i = 0; i < position; i++) {
                buffered_reader.readLine();
                temp += (line + "\r\n");
            }
            /* ���� String�� position 以��� �쎌�댁�� 吏���移⑸����. */
            buffered_reader.readLine();
            temp += change_line;
            /* 洹� �댄�� 以��� �쎄� ���� String�� ���ν�⑸����. */
            while ((line = buffered_reader.readLine()) != null) {
                temp += (line + "\r\n");
            }
            file_reader.close();
            buffered_reader.close();

            /* �대�� file�� ���� String�� ���� ��������. */
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
            /* "date.txt"�쇰�� 紐�移��� ���쇱�� "src/data/"寃쎈����� 濡����⑸����. */
            File file = new File("src/data/menu.txt");
            FileReader file_reader = new FileReader(file);
            BufferedReader buffered_reader = new BufferedReader(file_reader);
            String line = " ";
            String[][] temp = new String[5][5];
            int menu_num = 0;
            /* 鍮��쇱�몄��(null)�� ������ ��源�吏� ��以� �⑹�쎌�� 踰��쇱�� ���ν�⑸���� */
            while ((line = buffered_reader.readLine()) != null) {
                /* 읽은 라인을 공백(" ")을 기준으로 분할하여 line_split[]에 넣어줍니다 */
                String[] line_split = line.split("\t");
                for (int i = 0; i < line_split.length; i++) {
                    temp[i][menu_num] = line_split[i];
                }
                menu_num++;
            }
            /* textDB�� ���ν�⑸����. */
            tb.setMenu(temp);
            /* 紐⑤�� ������ ���닿� 踰��쇰�� �レ�듬����. */
            buffered_reader.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void create_file() { // 占십울옙占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙 占쏙옙, 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占싹댐옙 占쌨소듸옙

        // String home_directory = get_home_directory(); // {HOME}占쏙옙占�
        // String data_directory = home_directory + "\\data" ; // data占쏙옙占�
        String data_directory = "src/data/";

        String days[] = new String[3]; // 占쏙옙占쏙옙 占쏙옙짜占싸븝옙占쏙옙 3占싹깍옙占쏙옙占쏙옙 占쏙옙占쏙옙占싹울옙 string타占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쌌니댐옙. ex)
                                       // 20201023
        days[0] = get_date(0);
        days[1] = get_date(1);
        days[2] = get_date(2);

        for (int k = 0; k < 3; k++) { // 占쏙옙占쏙옙, 占쏙옙占쏙옙, 占쏙옙 占쏙옙 3占싹곤옙占쏙옙 占쏙옙占쏙옙占쏙옙占쏙옙占쏙옙占쏙옙 확占쏙옙占싹울옙 占쏙옙占쏙옙 占쏙옙
                                      // 占쏙옙占쏙옙占쏙옙占쌥니댐옙.
            String filename = days[k] + ".txt";
            File file = new File(data_directory + filename);

            boolean file_is_exist = file.exists();
            if (!file_is_exist) { // 占쌔댐옙 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙占쏙옙 占십는다몌옙,
                try {
                    System.out.println("占쏙옙占쏙옙占쏙옙 占쏙옙恝占� 占십울옙占쏙옙 <占쏙옙占쏙옙 占쏙옙占쏙옙 占쏙옙占쏙옙 : " + days[k]
                            + ".txt>占쏙옙 占쏙옙占쏙옙占쏙옙占쏙옙 占십쏙옙占싹댐옙. 占십울옙占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쌌니댐옙.");
                    file.createNewFile(); // 占쌔댐옙 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쌌니댐옙.
                    FileWriter fw = new FileWriter(file, false); // 占쏙옙占쏙옙占쏙옙 占쏙옙占싹울옙 占썩본 占쏙옙占쏙옙占쏙옙 占쌉뤄옙占쌌니댐옙.
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

    public void delete_file() { // 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙, 占쏙옙占쏙옙占싹댐옙 占쌨소듸옙
        // String home_directory = get_home_directory();
        // String data_directory = home_directory + "\\data" ;
        String data_directory = "src/data/";

        String str_today = get_date(0); // 占쏙옙占쏙옙 占쏙옙짜占쏙옙 string타占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙
        int int_today = Integer.parseInt(str_today); // 占쏙옙占쏙옙占쏙옙 占쏙옙짜占쏙옙 占쏙옙占싹깍옙 占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙짜占쏙옙 int
                                                     // 타占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙

        File file = new File(data_directory); // directory占쏙옙 占쏙옙占쏙옙占싹댐옙 占쏙옙占� 占쏙옙占쏙옙占쏙옙 占싱몌옙占쏙옙 list占쏙옙 占쏙옙占쏙옙
        String[] files = file.list();

        for (int i = 0; i < files.length; i++) { // list占쏙옙 占쏙옙占쏙옙占� 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙큼 占쌥븝옙
            String file_name = files[i];
            file_name = file_name.replace(".txt", ""); // 占쏙옙占쏙옙占쏙옙 占싱몌옙(占쏙옙짜)占쏙옙 占쏙옙占쏙옙 占쏙옙짜占쏙옙 占쏙옙占싹깍옙 占쏙옙占쏙옙
                                                       // 占쏙옙占쏙옙환占쏙옙 占쏙옙占쏙옙
            int file_date = Integer.parseInt(file_name);

            if (file_date < int_today) { // 占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占싼다몌옙,
                System.out.println("占쏙옙占쏙옙占쏙옙 占쏙옙恝占� 占쏙옙占쏙옙占쏙옙 <占쏙옙占쏙옙 占쏙옙占쏙옙 占쏙옙占쏙옙 : " + files[i]
                        + ">占쏙옙 占쏙옙占쏙옙占쌌니댐옙. 占쌔댐옙 占쏙옙占쏙옙占싶몌옙 占쏙옙占쏙옙占쌌니댐옙.");
                File file_ = new File(data_directory + "\\" + files[i]);
                file_.delete(); // 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占싼댐옙.
            }
        }

    }
}