package main_pkg;
import main_pkg.File_IO;
public class Main {


    public static void main(String[] args) {
    	File_IO f = new File_IO();
    	f.delete_file();

    }

    public void call_menu() {
        System.out.println("------------------------------");
        System.out.println("              硫붾돱");
        System.out.println("------------------------------");
        System.out.println("1. �삁�빟�븯湲�");
        System.out.println("2. �삁�빟 議고쉶");
        System.out.println("3. �삁�빟 痍⑥냼");
        System.out.println("4. �삁�빟 蹂�寃�");
        System.out.println("5. �봽濡쒓렇�옩 醫낅즺");
        System.out.println("6. �룄��留� 異쒕젰");
        System.out.println("7. 臾닿껐�꽦 寃��궗\n");
        System.out.println("�썝�븯�떆�뒗 踰덊샇瑜� �꽑�깮�븯�꽭�슂:");
    }

    private static boolean password(String pwd) {

        if(pwd.equals("1234"))
            return true;
        else
            return false;
    }

}
