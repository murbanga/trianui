package sampleui;

import java.nio.Buffer;

public class Mesh {
	private Object vertices;
	private Object indexes;
	
	public static native Mesh load(String filename);
	public native Mesh save(String filename);
	public native Mesh[] cut(float a, float b, float c, float d);
	
	private native float[] getVertices();
	private native int[] getIndexes();
	private native Buffer getTexture();
	
	public void draw()
	{
		
	}
}
