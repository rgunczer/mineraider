package com.almagems.mineraider.util;

import java.util.ArrayList;

import android.content.Context;

import com.almagems.mineraider.util.Vertex;
import com.almagems.mineraider.util.TextResourceReader;

public class ModelLoader {
	
	public ArrayList<Vertex> vertices = new ArrayList<Vertex>();
	private ArrayList<Vector> verts = new ArrayList<Vector>();
	private ArrayList<Vector> norms = new ArrayList<Vector>();
	private ArrayList<Vector> coords = new ArrayList<Vector>();
	
	public String name;	
	
	public boolean hasTextureCoords() {
		return coords.size() > 1;
	}	
	
	public void init(Context context, int resourceId, String name) {
		//System.out.println("Model name is: " + name + ", resource id is: " + resourceId);
		String buffer =	TextResourceReader.readTextFileFromResource(context, resourceId);
		this.name = name;		
		
		//System.out.println("buffer length is: " + buffer.length());				
		String[] lines = buffer.split("\n");
		
		// dummy element obj index starts from 1
		verts.add(new Vector(0f, 0f, 0f));
		norms.add(new Vector(0f, 0f, 0f));
		coords.add(new Vector(0f, 0f, 0f));
				
		for(int i = 0; i < lines.length; ++i) {
			String line = lines[i];
			
			if (line.charAt(0) == '#') {
				//System.out.println("skipping comment line");
				continue;
			} else	if (line.charAt(0) == 'v' && line.charAt(1) == ' ') {
				//System.out.println("reading vertex");
				
				String[] numberStrings = line.split(" ");
				ArrayList<Float> numbers = new ArrayList<Float>();
				for (int j = 1; j < numberStrings.length; ++j) {
					String numberString = numberStrings[j];
					Float number = Float.valueOf(numberString);
					numbers.add(number);					
				}
				verts.add(new Vector(numbers.get(0), numbers.get(1), numbers.get(2)));
				continue;
			} else	if (line.charAt(0) == 'v' && line.charAt(1) == 'n') {
				//System.out.println("reading vertex normal");
				
				String[] numberStrings = line.split(" ");
				ArrayList<Float> numbers = new ArrayList<Float>();
				for (int j = 1; j < numberStrings.length; ++j) {
					String numberString = numberStrings[j];
					Float number = Float.valueOf(numberString);
					numbers.add(number);					
				}
				norms.add(new Vector(numbers.get(0), numbers.get(1), numbers.get(2)));
				continue;
			} else if (line.charAt(0) == 'v' && line.charAt(1) == 't') {
				//System.out.println("reading vertex texture coordinates");
				
				String[] numberStrings = line.split(" ");
				ArrayList<Float> numbers = new ArrayList<Float>();
				for (int j = 1; j < numberStrings.length; ++j) {
					String numberString = numberStrings[j];
					Float number = Float.valueOf(numberString);
					numbers.add(number);
				}
				coords.add(new Vector(numbers.get(0), numbers.get(1), -1f));
				continue;				
			} else if (line.charAt(0) == 'f' && line.charAt(1) == ' ') {
				//System.out.println("reading face");
				// vertex, texture and normal indices
				int[] v = new int[3];
				int[] t = new int[3];
				int[] n = new int[3];

				String[] groupStrings = line.split(" ");
				int index = 0;
				for (int j = 1; j < groupStrings.length; ++j, ++index) {
					String group = groupStrings[j];
					String[] indexStrings = group.split("/");

					v[index] = 0;
					t[index] = 0;
					n[index] = 0;
					
					if (indexStrings[0].length() > 0)
						v[index] = Integer.valueOf(indexStrings[0]);
					
					if (indexStrings[1].length() > 0)
						t[index] = Integer.valueOf(indexStrings[1]);
					
					if (indexStrings[2].length() > 0)
						n[index] = Integer.valueOf(indexStrings[2]);
					
					//System.out.println("vertex Index is: " + v[index]);
					//System.out.println("texture coord Index is: " + t[index]);
					//System.out.println("normal Index is: " + n[index]);
				}
				
				Vector v0 = verts.get(v[0]);
				Vector v1 = verts.get(v[1]);
				Vector v2 = verts.get(v[2]);
				
				Vector t0 = coords.get(t[0]);
				Vector t1 = coords.get(t[1]);
				Vector t2 = coords.get(t[2]);
				
				Vector n0 = norms.get(n[0]);
				Vector n1 = norms.get(n[1]);
				Vector n2 = norms.get(n[2]);
				
				Vertex vert0 = new Vertex(v0.x, v0.y, v0.z, 	t0.x, t0.y, 	n0.x, n0.y, n0.z);
				Vertex vert1 = new Vertex(v1.x, v1.y, v1.z, 	t1.x, t1.y, 	n1.x, n1.y, n1.z);
				Vertex vert2 = new Vertex(v2.x, v2.y, v2.z, 	t2.x, t2.y, 	n2.x, n2.y, n2.z);
				
				vertices.add(vert0);
				vertices.add(vert1);
				vertices.add(vert2);
				continue;
			}
			else
			{
				//System.out.println("someting else, skipping");
				continue;
			}
		}
	}
}
