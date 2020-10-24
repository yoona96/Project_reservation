package main_pkg;

public class textDB {

    public String[][][] getToday() {
        return today;
    }

    public void setToday(String[][][] today) {
        this.today = today;
    }

    public String[][][] getTomorrow() {
        return tomorrow;
    }

    public void setTomorrow(String[][][] tomorrow) {
        this.tomorrow = tomorrow;
    }

    public String[][][] getDay_after_tomorrow() {
        return day_after_tomorrow;
    }

    public void setDay_after_tomorrow(String[][][] day_after_tomorrow) {
        this.day_after_tomorrow = day_after_tomorrow;
    }

    String[][][] today  = new String[11][11][20];
    String[][][] tomorrow = new String[11][11][20];
    String[][][] day_after_tomorrow = new String[11][11][20];

    public String[][] getMenu() {
        return menu;
    }

    public void setMenu(String[][] menu) {
        this.menu = menu;
    }

    String[][] menu = new String[5][5];
}
