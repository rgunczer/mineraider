package com.almagems.mineraider;

import static android.opengl.Matrix.*;

public final class Geometry {

    private static final Vector rayToPlaneVector = new Vector();

    private static final Vector p1ToPoint = new Vector();
    private static final Vector p2ToPoint = new Vector();

    private static final Vector vecBetween = new Vector();


    public static Vector intersectionPoint(Ray ray, Plane plane) {
        vectorBetween(ray.point, plane.point, rayToPlaneVector);
        float scaleFactor = rayToPlaneVector.dotProduct(plane.normal)
                / ray.vector.dotProduct(plane.normal);

        Vector intersectionPoint = ray.point.translate(ray.vector.scale(scaleFactor));
        return intersectionPoint;
    }

	public static void vectorBetween(Vector from, Vector to, Vector result) {
		result.x = to.x - from.x;
        result.y = to.y - from.y;
        result.z = to.z - from.z;
	}

	public static boolean intersects(Sphere sphere, Ray ray) {
		return distanceBetween(sphere.center, ray) < sphere.radius;
	}

	public static float distanceBetween(Vector point, Ray ray) {
        vectorBetween(ray.point, point, p1ToPoint);
        vectorBetween(ray.point.translate(ray.vector), point, p2ToPoint);
		
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

    public static void divideByW(float[] vector) {
        vector[0] /= vector[3];
        vector[1] /= vector[3];
        vector[2] /= vector[3];
    }

    public static Ray convertNormalized2DPointToRay(float normalizedX, float normalizedY, float[] invertedViewProjectionMatrix) {
        // Convert normalized device coordinates (NDC) into world-space coordinates.
        // We will pick a point on the near and far planes, and draw a
        // line between them. To do this transform, we need to first multiply by
        // the inverse matrix, and then we need to undo the perspective divide
        final float[] nearPointNdc = { normalizedX, normalizedY, -1, 1 };
        final float[] farPointNdc  = { normalizedX, normalizedY,  1, 1 };

        final float[] nearPointWorld = new float[4];
        final float[] farPointWorld = new float[4];

        multiplyMV(nearPointWorld, 0, invertedViewProjectionMatrix, 0, nearPointNdc, 0);
        multiplyMV(farPointWorld, 0, invertedViewProjectionMatrix, 0, farPointNdc, 0);

        divideByW(nearPointWorld);
        divideByW(farPointWorld);

        Vector nearPointRay = new Vector(nearPointWorld[0], nearPointWorld[1], nearPointWorld[2]);
        Vector farPointRay = new Vector(farPointWorld[0], farPointWorld[1], farPointWorld[2]);

        Geometry.vectorBetween(nearPointRay, farPointRay, vecBetween);
        return new Ray(nearPointRay, vecBetween);
    }

    // screen to world 2D
    public static Vector convertNormalized2DPointToNormalizedDevicePoint2D(float normalizedX, float normalizedY, float[] invertedViewProjectionMatrix) {

        float[] normalizedPoint = new float[4];
        float[] outPoint = new float[4];

        normalizedPoint[0] = normalizedX;
        normalizedPoint[1] = normalizedY;
        normalizedPoint[2] = -1f;
        normalizedPoint[3] =  1f;

        multiplyMV( outPoint, 0,
                    invertedViewProjectionMatrix, 0,
                    normalizedPoint, 0);

        //System.out.println("outPoint is: " + outPoint[0] + ", " + outPoint[1]);

        return new Vector(outPoint[0], outPoint[1], 0f);
    }

}