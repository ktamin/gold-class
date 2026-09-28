package lineage.bean.database;

public class TalkScroll {

    private int uid;
    private String name;
    private int x;
    private int y;
    private int x2;
    private int y2;
    private int x3;
    private int y3;
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
    
    public int getX2() {
		return x2;
	}

	public void setX2(int x2) {
		this.x2 = x2;
	}

	public int getY2() {
		return y2;
	}

	public void setY2(int y2) {
		this.y2 = y2;
	}

	// 3번 좌표
	public int getX3() {
		return x3;
	}

	public void setX3(int x3) {
		this.x3 = x3;
	}

	public int getY3() {
		return y3;
	}

	public void setY3(int y3) {
		this.y3 = y3;
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
    
 // 💡 [핵심] 3개의 좌표 중 값이 있는(0이 아닌) 안전한 좌표 하나를 랜덤으로 뽑아주는 메서드
 	public int[] getSafeRandomLocation() {
 		java.util.List<int[]> locs = new java.util.ArrayList<int[]>();

 		// 1. 첫 번째 좌표 (기본 좌표는 무조건 넣음)
 		locs.add(new int[]{this.x, this.y});

 		// 2. 두 번째 좌표 (DB에 값을 넣어서 0이 아닐 때만 추가)
 		if (this.x2 > 0 && this.y2 > 0) {
 			locs.add(new int[]{this.x2, this.y2});
 		}

 		// 3. 세 번째 좌표 (DB에 값을 넣어서 0이 아닐 때만 추가)
 		if (this.x3 > 0 && this.y3 > 0) {
 			locs.add(new int[]{this.x3, this.y3});
 		}

 		// 수집된 좌표들 중 하나를 랜덤으로 뽑아서 반환
 		int randomIndex = lineage.util.Util.random(0, locs.size() - 1);
 		return locs.get(randomIndex);
 	}
}
