package com.dnx.mc;

public class GameVars {
	public static final short BIT_GROUND = 2;
	public static final short BIT_CAR = 4;
	public static final short BIT_COIN = 8;
	public static final short BIT_WHEEL = 16;
	public static final short BIT_CHAIN = 32;
	public static final float PPM = 32f;
	public static enum BodyType{COIN, GROUND, CAR, CAR_SENSOR, WHEEL, CHAIN};
}
