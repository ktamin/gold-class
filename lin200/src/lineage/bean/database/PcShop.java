package lineage.bean.database;

import lineage.world.object.instance.PcShopInstance;

public class PcShop {
	//-- npc용
	private int npcid;		// 
	private int id;			// 
	private int count;		// 
	private String adenType; // 화폐타입
	private long price;		// 판매할 가격
	private int taxPrice;	// 세율에따라 얻는 금액 저장변수
	private boolean shop;	// 판매 가능 여부
	private Item item;		// 
	
	//-- pc용
	private PcShopInstance pc;
	private long InvItemObjectId;
	private long InvItemCount;
	private int InvItemEn;
	private int InvItemBress;
	private boolean InvItemDefinite;
	
	public PcShop(PcShopInstance pc_shop, long price, String adenType, long InvItemCount){
		this.pc = pc_shop;
		this.price = price;
		this.adenType = adenType;
		this.InvItemCount = InvItemCount;
	}
	
	public PcShopInstance getPc(){
		return pc;
	}
	public int getNpcid() {
		return npcid;
	}
	public void setNpcid(int npcid) {
		this.npcid = npcid;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public long getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public boolean isShop() {
		return shop;
	}
	public void setShop(boolean shop) {
		this.shop = shop;
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public int getTaxPrice() {
		return taxPrice;
	}
	public void setTaxPrice(int taxPrice) {
		this.taxPrice = taxPrice;
	}

	public long getInvItemObjectId() {
		return InvItemObjectId;
	}

	public void setInvItemObjectId(long invItemObjectId) {
		InvItemObjectId = invItemObjectId;
	}

	public long getInvItemCount() {
		return InvItemCount;
	}

	public void setInvItemCount(long invItemCount) {
		InvItemCount = invItemCount;
	}

	public int getInvItemEn() {
		return InvItemEn;
	}

	public void setInvItemEn(int invItemEn) {
		InvItemEn = invItemEn;
	}

	public int getInvItemBress() {
		return InvItemBress;
	}

	public void setInvItemBress(int invItemBress) {
		InvItemBress = invItemBress;
	}

	public boolean isInvItemDefinite() {
		return InvItemDefinite;
	}

	public void setInvItemDefinite(boolean invItemDefinite) {
		InvItemDefinite = invItemDefinite;
	}
	
	public String getAdenType() {
		return adenType;
	}

	public void setAdenType(String adenType) {
		this.adenType = adenType;
	}
	
}
