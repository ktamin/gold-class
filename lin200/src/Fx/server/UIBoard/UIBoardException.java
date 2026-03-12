package Fx.server.UIBoard;

import java.util.List;

import lineage.share.Lineage;
import lineage.world.controller.ChattingController;
import lineage.world.object.instance.PcInstance;

public class UIBoardException extends Exception{
	private static final long serialVersionUID = 1L;

	public static void throwException(String message) throws UIBoardException{
		throw new UIBoardException(message);
	}
	
	public static void isFalse(boolean bool, String message) throws UIBoardException{
		if(!bool){
			throwException(message);
		}
	}

	public static void isTrue(boolean bool, String message) throws UIBoardException{
		isFalse(!bool, message);
	}
	
	public static <T> void isNullOrEmpty(List<T> list, String message) throws UIBoardException{
		isTrue(list == null || list.size() <= 0, message);
	}

	public static <T> void isInIndex(List<T> list, int index, String message) throws UIBoardException{
		isTrue(list == null || 
				list.size() <= 0 ||
				index < 0 ||
				index >= list.size(), message);
	}
	
	public UIBoardException(String message) {
		super(message);
	}
	
	public void sendMessage(PcInstance pc){
		ChattingController.toChatting(pc, getMessage(), Lineage.CHATTING_MODE_MESSAGE);
	}
}
