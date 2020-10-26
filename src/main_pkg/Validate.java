package main_pkg;
import main_pkg.File_IO;
import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
public class Validate {

	File_IO fi=new File_IO();
	private String date;
	private String home=fi.get_home_directory();
	private static int two,four,six=0;
	
    public boolean validate_reservation_file() {
    	String date;
    	fi.create_file();
    	fi.delete_file();
    	int day=0;
    	while(day<3) { //오늘,내일,모레 3개의 파일에 대해 실행
    		try {
    			two=0;
		        four=0;
		        six=0; //테이블 수 초기화
	    		date=fi.get_date(day);
		    	File file = new File(home+"/data/" + date + ".txt");
		        FileReader fr = new FileReader(file);
		        BufferedReader br = new BufferedReader(fr);
		        String line = "";
		        String asc_line="";
		        boolean is=false;
		        while((line=br.readLine())!=null) {
		        	if(line.length()==0)//빈 줄이라면
		        		continue;
		        	asc_line+=line+"\n";
		        	String temp[]=line.split("\t");
		        	is=reservation_file_grammer(temp,date);	 //한 줄씩 문법확인
		        	if(!is) { //오류가 있었다면 메소드를 종료합니다
		        		br.close();
				        fr.close();
		        		return false;
		        	}
		        }
		        
		        String buffer[]=asc_line.split("\n");
		        Arrays.sort(buffer, new Comparator<String>(){
		        	public int compare(String o1, String o2) {
		        		int t1=Integer.parseInt(o1.split("\t")[0]);
		        		int t2=Integer.parseInt(o2.split("\t")[0]); 
		        		if(t1==t2) //테이블 번호가 같다면 시간순으로 오름차순
		        			return Integer.parseInt(o1.split("\t")[3])-Integer.parseInt(o2.split("\t")[3]);
		        		else //테이블 번호로 오름차순
		        			return Integer.parseInt(o1.split("\t")[0])-Integer.parseInt(o2.split("\t")[0]);
		        	}
		        }); //오름차순 정렬
		        FileWriter fw=new FileWriter(file);
		        asc_line="";
		        for(int i=0;i<buffer.length;i++) {
		        	asc_line+=buffer[i]+"\n";
		        }
		        fw.write(asc_line);  //오름차순으로 정렬된 값으로 파일을 덮어씁니다
		        fw.close();
		        br.close();
		        fr.close();

		        if((two+four+six)!=20*11) { //테이블 총 개수 확인
		        	System.out.println(date+".txt의 좌석 정보가 올바르지 않습니다. 레스토랑의 총 테이블 수는 20개입니다 ");
		        	return false;
		        }
		        
		        if(two!=6*11||four!=10*11||six!=4*11) { //인원 테이블 수 확인
		        	System.out.println(date+".txt의 좌석 정보가 올바르지 않습니다. 식당은 2 인용 테이블 " + 
		        			"6 개, 4 인용 테이블 10 개, 6 인용 테이블 4 개로 구성되어 있습니다. 따라서 <예약 정보 파일>에도 " + 
		        			"각각의 좌석은 해당 개수만큼 존재해야 합니다.");
			       	return false;
			    }
		        
		        day++;
    		}
    		catch(FileNotFoundException e) {
    			e.printStackTrace();
    		}
    		catch(IOException e) {
    			e.printStackTrace();
    		}
    	}
    	return true; //무결성 검사를 다 통과하면 true가 반환됩니다
    	
    }
    public boolean reservation_file_grammer(String[] temp, String date) {
    	for(int i=0;i<temp.length;i++) {
	    		switch(i) {
	    		case 0:{ //테이블 번호
	    			if(Integer.parseInt(temp[i])>=1&&Integer.parseInt(temp[i])<=20)
	    				break;
	    			else {
	    				System.out.println(date+".txt에서 테이블 번호는 1번부터 20번까지입니다.");
	    				return false;
	    			}
	    				
	    		}
	    		case 1:{ //테이블 인원수
	    			if(Integer.parseInt(temp[i])==2||Integer.parseInt(temp[i])==4||Integer.parseInt(temp[i])==6) {
	    				if(Integer.parseInt(temp[i])==2)
	    					two++;
	    				else if(Integer.parseInt(temp[i])==4)
	    					four++;
	    				else if(Integer.parseInt(temp[i])==6)
	    					six++;
	    				break;
	    			}
	    			else {
	    				System.out.println(date+".txt의 좌석 인원 제한이 올바르지 않습니다. 식당을 구성하는 모든 좌석은 2, 4, 6 인용 좌석 중 하나입니다.");
	    				return false;
	    			}
	    		}
	    		case 2:{ //예약 인원 수 
	    			if(Integer.parseInt(temp[i])>=0&&Integer.parseInt(temp[i])<=12)
	    				break;
	    			else {
	    				System.out.println(date+".txt에서 인원수가 맞지 않습니다. 인원수는 0~12명입니다.");
	    				return false;
	    			}
	    				
	    		}
	    		case 3:{//시간
	    			if(Integer.parseInt(temp[i])>=10&&Integer.parseInt(temp[i])<=20)
	    				break;
	    			else {
	    				System.out.println(date+".txt에서 시간이 맞지 않습니다. 시간는 10~20시 입니다.");
	    				return false;
	    			}
	    				
	    		}    					
	       		case 4:{ //이름
	       			if(temp[i].matches("^[가-힣]*$")&&temp[i].length()>=2)
	       				break;
					else {
						System.out.println(date+".txt에서 이름이 맞지 않습니다. 이름은 성 포함 2자이상 공백 없는 한글이름입니다.");
						return false;
					}
						
	       		}
	       		case 5:{ //전화번호
	       			if(temp[i].matches("^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$"))
	       				break;
	       			else {
	       				System.out.println(date+".txt에서 전화번호가 맞지 않습니다. 010-1111-1111 또는 019-111-1111이어야 합니다.");
						return false;
	       			}
	       		}
	       		default:{ //메뉴 1,2,3,4,5
	       			if(temp[i].matches("^[0-9]*$"))
	       				break;
	       			else
	       			{
	       				System.out.println(date+".txt에서 메뉴정보가 맞지 않습니다. 메뉴는 0이상 정수이어야 합니다.");
						return false;
	       			}
	    		}
	    		}
    		}
    	return true;
    }

   public void check_all(String directory, String file_name){

    }
    
}
