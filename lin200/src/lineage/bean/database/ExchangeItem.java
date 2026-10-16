package lineage.bean.database;

public class ExchangeItem {
	private int id;				// 거래소 등록 고유 번호 (DB의 id)
	private long itemObjId;		// 아이템 고유 오브젝트 ID (복사 방지용)
	private String itemName;	// 아이템 이름
	private int enchant;		// 인챈트 수치
	private int attrLevel;		// 속성 인챈트 (화, 수, 풍, 지 단계)
	private int bless;			// 축복 여부
	private long count;			// 아이템 수량 (겹치는 아이템 고려 long)
	private String sellerName;	// 판매자 캐릭터명
	private long price;			// 판매 가격 (고액 고려 long)
	private int currencyType;	// 결제 화폐 (0: 아데나, 1: 포인트)
	private int category;		// 카테고리 (1:무기, 2:방어구, 3:장신구, 4:기타)
	private int isSold;			// 상태 (0: 판매중, 1: 판매완료, 2: 정산완료)
	private int status;
	private long sellerObjectId;
	
	public ExchangeItem() {
		// 기본 생성자
	}

	// ---------------- Getter & Setter ----------------
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public long getItemObjId() {
		return itemObjId;
	}
	public void setItemObjId(long itemObjId) {
		this.itemObjId = itemObjId;
	}

	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getEnchant() {
		return enchant;
	}
	public void setEnchant(int enchant) {
		this.enchant = enchant;
	}

	public int getAttrLevel() {
		return attrLevel;
	}
	public void setAttrLevel(int attrLevel) {
		this.attrLevel = attrLevel;
	}

	public int getBless() {
		return bless;
	}
	public void setBless(int bless) {
		this.bless = bless;
	}

	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}

	public String getSellerName() {
		return sellerName;
	}
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public long getPrice() {
		return price;
	}
	public void setPrice(long price) {
		this.price = price;
	}

	public int getCurrencyType() {
		return currencyType;
	}
	public void setCurrencyType(int currencyType) {
		this.currencyType = currencyType;
	}

	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}

	public int getIsSold() {
		return isSold;
	}
	public void setIsSold(int isSold) {
		this.isSold = isSold;
	}
	// 기존 getter/setter 아래에 추가
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public long getSellerObjectId() {
		return sellerObjectId;
	}

	public void setSellerObjectId(long sellerObjectId) {
		this.sellerObjectId = sellerObjectId;
	}
}