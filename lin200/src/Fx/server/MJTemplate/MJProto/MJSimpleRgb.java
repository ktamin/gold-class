package Fx.server.MJTemplate.MJProto;

import java.awt.Color;

public class MJSimpleRgb {
	private static MJSimpleRgb _red;
	private static MJSimpleRgb _green;
	private static MJSimpleRgb _blue;

	private byte[] _rgb;

	// 기본 생성자
	public MJSimpleRgb(int r, int g, int b) {
		_rgb = new byte[] { (byte) r, (byte) g, (byte) b, };
	}

	// 정적 팩토리 메서드
	public static MJSimpleRgb fromRgb(int r, int g, int b) {
		return new MJSimpleRgb(r, g, b);
	}

	// Color 객체로 변환하는 메서드
	public Color toColor() {
		return new Color(_rgb[0] & 0xFF, _rgb[1] & 0xFF, _rgb[2] & 0xFF);
	}
	
	 // 새로운 fromColor 메서드: Color 객체에서 int RGB 값 생성
    public static int fromColorInt(Color color) {
        return (color.getRed() & 0xFF) | 
               ((color.getGreen() & 0xFF) << 8) | 
               ((color.getBlue() & 0xFF) << 16);
    }

	// Color 객체에서 MJSimpleRgb 객체로 변환하는 메서드
	public static MJSimpleRgb fromColor(Color color) {
		return fromRgb(color.getRed(), color.getGreen(), color.getBlue());
	}

	// 미리 정의된 빨강, 초록, 파랑 색상 상수
	public static MJSimpleRgb red() {
		if (_red == null)
			_red = fromRgb(255, 0, 0);
		return _red;
	}

	public static MJSimpleRgb green() {
		if (_green == null)
			_green = fromRgb(0, 255, 0);
		return _green;
	}

	public static MJSimpleRgb blue() {
		if (_blue == null)
			_blue = fromRgb(0, 0, 255);
		return _blue;
	}

	// 문자열에서 MJSimpleRgb 객체로 변환
	public static MJSimpleRgb from_string(String rgb) {
		String[] array = rgb.split("\\,");
		return fromRgb(Integer.parseInt(array[0]), Integer.parseInt(array[1]), Integer.parseInt(array[2]));
	}

	// byte 값으로부터 정수형 RGB 값 변환
	public static int convertToInt(byte r, byte g, byte b) {
		return (r & 0xFF) | ((g << 8) & 0xFF00) | ((b << 16) & 0xFF0000);
	}

	// 정수형 RGB 값을 이용한 변환
	public static int convertToInt(int r, int g, int b) {
		return convertToInt((byte) r, (byte) g, (byte) b);
	}

	// RGB 값을 바이트 배열로 반환
	public byte[] get_bytes() {
		return _rgb;
	}

	// RGB 값을 정수 배열로 반환
	public int[] getIntRgb() {
		return new int[] { _rgb[0] & 0xFF, _rgb[1] & 0xFF, _rgb[2] & 0xFF };
	}

	// 현재 RGB 값을 문자열로 반환
	public String toRgbString() {
		return (_rgb[0] & 0xFF) + "," + (_rgb[1] & 0xFF) + "," + (_rgb[2] & 0xFF);
	}

	@Override
	public String toString() {
		return "MJSimpleRgb(R=" + (_rgb[0] & 0xFF) + ", G=" + (_rgb[1] & 0xFF) + ", B=" + (_rgb[2] & 0xFF) + ")";
	}
}
