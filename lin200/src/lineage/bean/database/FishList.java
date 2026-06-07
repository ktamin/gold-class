package lineage.bean.database;

public class FishList {
	private String itemName;
	private int itemBless;
	private int itemEnchant;
	private int itemCountMin;
	private int itemCountMax;
	private int itemChance; // [추가] 아이템 획득 확률 변수

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getItemBless() {
		return itemBless;
	}

	public void setItemBless(int itemBless) {
		this.itemBless = itemBless;
	}

	public int getItemEnchant() {
		return itemEnchant;
	}

	public void setItemEnchant(int itemEnchant) {
		this.itemEnchant = itemEnchant;
	}

	public int getItemCountMin() {
		return itemCountMin;
	}

	public void setItemCountMin(int itemCountMin) {
		this.itemCountMin = itemCountMin;
	}

	public int getItemCountMax() {
		return itemCountMax;
	}

	public void setItemCountMax(int itemCountMax) {
		this.itemCountMax = itemCountMax;
	}

	// [수정 포인트] 메서드 이름을 setChance로 변경해서 에러를 잡습니다!
	public int getChance() {
		return itemChance;
	}

	public void setChance(int itemChance) {
		this.itemChance = itemChance;
	}

}
