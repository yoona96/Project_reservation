package main_pkg;

public class textDB {

    public String[][][] get_day() {
        return day;
    }

    public void set_day(String[][][] today) {
        this.day = today;
    }

    private String[][][] day  = new String[11][11][20];


    public String[][] getMenu() {
        return menu;
    }

    public void setMenu(String[][] menu) {
        this.menu = menu;
    }

    String[][] menu = new String[5][5];


}