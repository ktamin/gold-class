package Fx.server.UIBoard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UIBoardDataTable<T> {
	public static <T> UIBoardDataTable<T> newTable(){
		return new UIBoardDataTable<T>();
	}
	
	private HashMap<T, Integer> mMap;
	private ArrayList<T> mArray;
	
	private UIBoardDataTable(){
		mMap = new HashMap<>();
		mArray = new ArrayList<>();
	}
	
	public int add(T t){
		int index = mMap.getOrDefault(t, -1);
		if(index == -1){
			index = mMap.size();
			mMap.put(t, index);
			mArray.add(t);
		}
		return index;
	}
	
	public T getAt(int index){
		return mArray.get(index);
	}
	
	public List<T> getArray(){
		return mArray;
	}
	
	public int getSize(){
		return mArray.size();
	}
}
