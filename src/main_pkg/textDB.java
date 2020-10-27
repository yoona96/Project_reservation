package main_pkg;

public class textDB {

    public String[][][] getday() {
        return day;
    }

    public void setday(String[][][] today) {
        this.day = today;
    }

    private String[][][] day  = new String[11][11][20];


    public String[][] getMenu() {
        return menu;
    }

    public void setMenu(String[][] menu) {
        this.menu = menu;
    }

    String[][] menu = new String[4][5];
}