package com.almagems.mineraider;

import java.util.ArrayList;


public final class ModelLoader {
	private final Vector zeroVector = new Vector(0f, 0f, 0f);

	public final ArrayList<Vertex> vertices = new ArrayList<Vertex>(100);
	private final ArrayList<Vector> verts = new ArrayList<Vector>(300);
	private final ArrayList<Vector> norms = new ArrayList<Vector>(300);
	private final ArrayList<Vector> coords = new ArrayList<Vector>(300);
	
	public String name;	
	
	public boolean hasTextureCoords() {
		return coords.size() > 1;
	}	
	
	public void init(String name, String buffer) {
        //System.out.println("Model name is: " + name);
        this.name = name;

        vertices.clear();
        verts.clear();
        norms.clear();
        coords.clear();

		//System.out.println("buffer length is: " + buffer.length());
        String[] arrayStrings;
        String numberString;
        Float number;
        float[] numbers = new float[3];

        int[] v = new int[3];
        int[] t = new int[3];
        int[] n = new int[3];

        Vector v0, v1, v2;
        Vector t0, t1, t2;
        Vector n0, n1, n2;

        // dummy element obj index starts from 1
		verts.add(zeroVector);
		norms.add(zeroVector);
		coords.add(zeroVector);

        String[] lines = buffer.split("\n");
        String line;
        int len = lines.length;
		for(int i = 0; i < len; ++i) {
            line = lines[i];
			
			if (line.charAt(0) == '#') {
                //System.out.println("skipping comment line");
                continue;
            }

			if (line.charAt(0) == 'v' && line.charAt(1) == ' ') {
				//System.out.println("reading vertex");
				arrayStrings = line.split(" ");
				for (int j = 1; j < arrayStrings.length; ++j) {
					numberString = arrayStrings[j];
					number = Float.valueOf(numberString);
					numbers[j-1] = number;
				}
				verts.add(new Vector(numbers[0], numbers[1], numbers[2]));
			} else	if (line.charAt(0) == 'v' && line.charAt(1) == 'n') {
				//System.out.println("reading vertex normal");
				arrayStrings = line.split(" ");
				for (int j = 1; j < arrayStrings.length; ++j) {
					numberString = arrayStrings[j];
					number = Float.valueOf(numberString);
					numbers[j-1] = number;
				}
				norms.add(new Vector(numbers[0], numbers[1], numbers[2]));
			} else if (line.charAt(0) == 'v' && line.charAt(1) == 't') {
				//System.out.println("reading vertex texture coordinates");
				arrayStrings = line.split(" ");
				for (int j = 1; j < arrayStrings.length; ++j) {
					numberString = arrayStrings[j];
					number = Float.valueOf(numberString);
					numbers[j-1] = number;
				}
				coords.add(new Vector(numbers[0], numbers[1], -1f));
			} else if (line.charAt(0) == 'f' && line.charAt(1) == ' ') {
				//System.out.println("reading face");
				// vertex, texture and normal indices
				arrayStrings = line.split(" ");
				int index = 0;
                String group;
                String[] indexStrings;
				for (int j = 1; j < arrayStrings.length; ++j, ++index) {
					group = arrayStrings[j];
					indexStrings = group.split("/");

					v[index] = 0;
					t[index] = 0;
					n[index] = 0;
					
					if (indexStrings[0].length() > 0) {
                        v[index] = Integer.valueOf(indexStrings[0]);
                    }
					
					if (indexStrings[1].length() > 0) {
                        t[index] = Integer.valueOf(indexStrings[1]);
                    }
					
					if (indexStrings[2].length() > 0) {
                        n[index] = Integer.valueOf(indexStrings[2]);
                    }
					
					//System.out.println("vertex Index is: " + v[index]);
					//System.out.println("texture coord Index is: " + t[index]);
					//System.out.println("normal Index is: " + n[index]);
				}
				
				v0 = verts.get(v[0]);
				v1 = verts.get(v[1]);
				v2 = verts.get(v[2]);
				
				t0 = coords.get(t[0]);
				t1 = coords.get(t[1]);
				t2 = coords.get(t[2]);
				
				n0 = norms.get(n[0]);
				n1 = norms.get(n[1]);
				n2 = norms.get(n[2]);

				vertices.add(new Vertex(v0.x, v0.y, v0.z, 	t0.x, t0.y, 	n0.x, n0.y, n0.z));
				vertices.add(new Vertex(v1.x, v1.y, v1.z, 	t1.x, t1.y, 	n1.x, n1.y, n1.z));
				vertices.add(new Vertex(v2.x, v2.y, v2.z, 	t2.x, t2.y, 	n2.x, n2.y, n2.z));
			}
		}
	}
}