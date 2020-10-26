package main_pkg;

public class textDB {

    public String[][][] get_day() {
        return day;
    }

    public void set_day(String[][][] today) {
        this.day = today;
    }

    private String[][][] day  = new String[11][11][20];


    public String[][] get_menu() {
        return menu;
    }

    public void set_menu(String[][] menu) {
        this.menu = menu;
    }

    String[][] menu = new String[5][5];
}