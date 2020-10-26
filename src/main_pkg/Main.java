package main_pkg;
import main_pkg.File_IO;
public class Main {


    public static void main(String[] args) {
    	File_IO fi = new File_IO();
    	fi.create_file();
    	fi.delete_file();
    	
    }

    public void call_menu() {
        System.out.println("------------------------------");
        System.out.println("              筌�遺얜��");
        System.out.println("------------------------------");
        System.out.println("1. 占쎌��占쎈�占쎈릭疫뀐옙");
        System.out.println("2. 占쎌��占쎈� 鈺곌���");
        System.out.println("3. 占쎌��占쎈� ���λ��");
        System.out.println("4. 占쎌��占쎈� 癰�占썲��占�");
        System.out.println("5. 占쎈늄嚥≪����占쎌�� �ル��利�");
        System.out.println("6. 占쎈�占쏙옙筌�占� �곗����");
        System.out.println("7. �얜�욧�占쎄쉐 野�占쏙옙沅�\n");
        System.out.println("占쎌��占쎈릭占쎈��占쎈�� 甕곕������占� 占쎄�占쎄문占쎈릭占쎄쉭占쎌��:");
    }

    private static boolean password(String pwd) {

        if(pwd.equals("1234"))
            return true;
        else
            return false;
    }

}
