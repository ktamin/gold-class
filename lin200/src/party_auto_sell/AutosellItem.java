package party_auto_sell;

import java.util.List;

import lineage.world.object.instance.ItemInstance;

public class AutosellItem {
	

	
	private long chaobjid;
	private long itemobjid;
	private int item_id;
	private int bless;
	private int enLevel;
	private long count;
	private int type;
	private int invGfx;
	private String name;
	private long price;
	private long sellPrice;
	private boolean definite;
	private ItemInstance mainItem;
	private int enEarth; // 땅 속성 인첸트
	private int enWater; // 물 속성 인첸트
	private int enFire; // 불 속성 인첸트
	private int enWind; // 바람 속성 인첸트
	private long petObjId;
	private String shoptype;
	private String charName;
	
	public long getchaobjid() {
		return chaobjid;
	}
	public void setchaobjid(long chaobjid) {
		this.chaobjid = chaobjid;
	}
	
	public long getItemobjid() {
		return itemobjid;
	}
	public void setItemobjid(long itemobjid) {
		this.itemobjid = itemobjid;
	}
	public int getItem_id() {
		return item_id;
	}
	public void setItem_id(int item_id) {
		this.item_id = item_id;
	}
	public int getBless() {
		return bless;
	}
	public void setBless(int bless) {
		this.bless = bless;
	}
	public int getEnLevel() {
		return enLevel;
	}
	public void setEnLevel(int enLevel) {
		this.enLevel = enLevel;
	}

	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}

	public int getEnEarth() {
		return enEarth;
	}

	public void setEnEarth(int enEarth) {
		if (enEarth > 5)
			enEarth = 5;
		this.enEarth = enEarth;
	}

	public int getEnWater() {
		return enWater;
	}

	public void setEnWater(int enWater) {
		if (enWater > 5)
			enWater = 5;
		this.enWater = enWater;
	}

	public int getEnFire() {
		return enFire;
	}

	public void setEnFire(int enFire) {
		if (enFire > 3)
			enFire = 3;
		this.enFire = enFire;
	}

	public int getEnWind() {
		return enWind;
	}

	public void setEnWind(int enWind) {
		if (enWind > 5)
			enWind = 5;
		this.enWind = enWind;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getInvGfx() {
		return invGfx;
	}
	public void setInvGfx(int invGfx) {
		this.invGfx = invGfx;
	}
	public String getName() {
		
		StringBuffer sb = new StringBuffer();
		
//		// 축저주 구분
//		sb.append("[").append(getBless() == 0 ? "축복" : (getBless() == 1 ? "일반" : "저주")).append("]");
//		// 속성
//		if (getEnFire() >= 1)
//			sb.append("").append(getEnFire()).append(getEnLevel() >= 0 ? " 단" : " ");
//		if (getEnEarth() >= 1)
//			sb.append("지령").append(getEnEarth()).append(getEnLevel() >= 0 ? "단" : " ");
//		if (getEnWind() >= 1)
//			sb.append("풍령").append(getEnWind()).append(getEnLevel() >= 0 ? "단" : " ");
//		if (getEnWater() >= 1)
//			sb.append("수령").append(getEnWater()).append(getEnLevel() >= 0 ? "단" : " ");
//	
//			
//		// 인첸트 레벨 표현
//		if (getEnLevel() >= 0 && getType() != 3)
//			sb.append("").append(getEnLevel() >= 0 ? "+" : "-").append(getEnLevel());
		// 이름 표현
		sb.append(name);
//		// 수량 표현
//		if (getCount() > 1)
//			sb.append(" (").append(getCount()).append(")");
		
		return sb.toString();
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getPrice() {
		return price;
	}
	public void setPrice(long price) {
		this.price = price;
	}
	public ItemInstance getMainItem() {
		return mainItem;
	}
	public void setMainItem(ItemInstance mainItem) {
		this.mainItem = mainItem;
	}
	public long getPetObjId() {
		return petObjId;
	}
	public void setPetObjId(long petObjId) {
		this.petObjId = petObjId;
	}
	public boolean isDefinite() {
		return definite;
	}
	public void setDefinite(boolean definite) {
		this.definite = definite;
	}
	public long getSellPrice() {
		return sellPrice;
	}
	public void setSellPrice(long sellPrice) {
		this.sellPrice = sellPrice;
	}
	public String getShoptype() {
		return shoptype;
	}
	public void setShoptype(String shoptype) {
		this.shoptype = shoptype;
	}
	public String getCharName() {
		return charName;
	}
	public void setCharName(String charName) {
		this.charName = charName;
	}	

}
