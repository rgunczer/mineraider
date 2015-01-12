package com.almagems.mineraider.util;

public class Geometry {

    public static Vector intersectionPoint(Ray ray, Plane plane) {
        Vector rayToPlaneVector = vectorBetween(ray.point, plane.point);
        float scaleFactor = rayToPlaneVector.dotProduct(plane.normal)
                / ray.vector.dotProduct(plane.normal);

        Vector intersectionPoint = ray.point.translate(ray.vector.scale(scaleFactor));
        return intersectionPoint;
    }

	public static Vector vectorBetween(Vector from, Vector to) {
		return new Vector( to.x - from.x,
				           to.y - from.y,
				           to.z - from.z);
	}

	public static boolean intersects(Sphere sphere, Ray ray) {
		return distanceBetween(sphere.center, ray) < sphere.radius;
	}

	public static float distanceBetween(Vector point, Ray ray) {
		Vector p1ToPoint = vectorBetween(ray.point, point);
		Vector p2ToPoint = vectorBetween(ray.point.translate(ray.vector), point);
		
		// the length of the cross product gives the area of an imagenary
		// paralelogram having the two vectors as sides. A paralelogram can be
		// thought of as consisting of two triangles, so this is the same as
		// twice the area of the triangle defined by the two vectors
		float areaOfTriangleTimesTwo = p1ToPoint.crossProduct(p2ToPoint).length();
		float lengthOfBase = ray.vector.length();
		
		// the area of a triangle is also equal to (base * height) / 2
		// in other words, the height is equal to (area * 2) / base
		// the height of this triangle is the distance form the point to the ray
		float distanceFromPointToRay = areaOfTriangleTimesTwo / lengthOfBase;
		return distanceFromPointToRay;
	}
	

	
}