/*package src.main_pkg;
import java.util.Scanner;
import src.main_pkg.*;

public class Change_reserv {
	String name,phon_num;
	String date,line_index //index of the old reservation
	
	Scanner s =new Scanner(System.in);
	Reservation rsv=new Reservation();
	
	private boolean new_reservation() {
		rsv.user_input();
		rsv.search_table();
		//좌석 자동할당까지
		rsv.choose_menu();
		//예약내역 출력까지 동일
		this.delete_reservation();
		
	}
	private boolean choose_reservation() {
		System.out.println("선택하신 예약정보를 변경하시겠습니까?");
		int yn=s.next();
		if(yn=='y')
			return true;
		else if(yn=='n')
			return false;
	}
	private void delete_reservation() {
		File file=new File("src/date/"+date+".txt");
		FileReader fr=new FileReader(file);
		BufferedReader br=new BufferedReader(fr);
		String line="";
		String new_line="";
		for(int i=0;i<line_index;i++) {
			new_line+=fr.readLine();
		}//read file before the line index
		
		line=fr.readLine();
		String[] buf_line=line.trim().split("\t");
		for(int i=0;i<4;i++) {
			switch(i){
			case 0:{ //table number
				new_line+=buf_line[i]+"\t";
				break;
			}
			case 1:{ //table count limit
				new_line=buf_line[i]+"\t";
				break;
			}
			case 2:{ //reserved count
				new_line+="0\t";
			}
			case 3:{ //time
				new_line+=buf_line[i]+"\n";
			}
			}
		}
		
		while((line=fr.readLine())!=null) {
			new_line+=line;
		}
		
		FileWriter fw=new FileWriter(file);
		fw.write(new_line); //overwrite file with new_line
		
		fr.close();
		br.close();
		fw.close();
		
	}
	
	public void change() {
		//input user name and phone number
		//show all reservations of user
		//choose reservation
		if(choose_reservation()) {
			if(new_reservation())
				delete_reservation();
			else
				return;
		}
		else
			choose_reservation();
			
	}
	
}
*/
