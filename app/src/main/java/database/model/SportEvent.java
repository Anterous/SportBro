package database.model;

public class SportEvent {
    public static final String TABLE_NAME = "sportevents";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_LENGTH = "length";
    public static final String COLUMN_KCAL = "kcal";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private int id;
    private int length;
    private int kcal;
    private String type;
    private String timestamp;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_TYPE + " TEXT,"
                    + COLUMN_LENGTH + " INTEGER,"
                    + COLUMN_KCAL + " INTEGER DEFAULT 0,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public SportEvent() {
    }

    public SportEvent(int id,String type,int length,int kcal, String timestamp) {
        this.id = id;
        this.type = type;
        this.length = length;
        this.kcal = kcal;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getKcal() {
        return kcal;
    }

    public void setKcal(int kcal) {
        this.kcal = kcal;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}

