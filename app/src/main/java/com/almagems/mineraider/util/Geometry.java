package com.almagems.mineraider.util;

import android.util.FloatMath;

public class Geometry {
	
	public static class Point {
		public final float x, y, z;
		
		public Point(float x, float y, float z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
		public Point translateY(float distance) {
			return new Point(x, y + distance, z);
		}
		
		public Point translate(Vector vector) {
			return new Point(
					x + vector.x,
					y + vector.y,
					z + vector.z);
		}
	}
	
	public static Vector vectorBetween(Point from, Point to) {
		return new Vector(
				to.x - from.x,
				to.y - from.y,
				to.z - from.z);
	}
	
	public static class Ray {
		public final Point point;
		public final Vector vector;
		
		public Ray(Point point, Vector vector) {
			this.point = point;
			this.vector = vector;
		}
	}
	
	public static class Circle {
		public final Point center;
		public final float radius;
		
		public Circle(Point center, float radius) {
			this.center = center;
			this.radius = radius;
		}
		
		public Circle scale(float scale) {
			return new Circle(center, radius * scale);
		}
	}
	
	public static class Cylinder {
		public final Point center;
		public final float radius;
		public final float height;
	
		public Cylinder(Point center, float radius, float height) {
			this.center = center;
			this.radius = radius;
			this.height = height;
		}
	}
	
	public static class Sphere {
		public final Point center;
		public final float radius;
		
		public Sphere(float x, float y, float z, float radius) {
			this.center = new Point(x, y, z);
			this.radius = radius;
		}
	}
	
	public static boolean intersects(Sphere sphere, Ray ray) {
		return distanceBetween(sphere.center, ray) < sphere.radius;
	}

	public static float distanceBetween(Point point, Ray ray) {
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
	
	public static class Plane {
		public final Point point;
		public final Vector normal;
		
		public Plane(Point point, Vector normal) {
			this.point = point;
			this.normal = normal;
		}
	}
	
	public static Point intersectionPoint(Ray ray, Plane plane) {
		Vector rayToPlaneVector = vectorBetween(ray.point, plane.point);
		float scaleFactor = rayToPlaneVector.dotProduct(plane.normal)
				/ ray.vector.dotProduct(plane.normal);
		
		Point intersectionPoint = ray.point.translate(ray.vector.scale(scaleFactor));
		return intersectionPoint;		
	}
	
	
}