package lineage.bean.database;

public class TalkScroll {

    private int uid;
    private String name;
    private int x;
    private int y;
    private int map;
    private int minLevel;
    private String classType;
    private int price;
    private boolean enable;
    private String group;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getMap() {
        return map;
    }

    public void setMap(int map) {
        this.map = map;
    }

    public int getMinLevel() {
        return minLevel;
    }

    public void setMinLevel(int minLevel) {
        this.minLevel = minLevel;
    }

    public String getClassType() {
        return classType == null ? "ALL" : classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getGroup() {
        return group == null ? "기타" : group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
