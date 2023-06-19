import korlibs.korge.view.*
import korlibs.korge.view.Circle
import korlibs.math.geom.*
import kotlin.math.*

fun getCircleIntersect(m1: Point, m2: Point, r1: Float, r2: Float): Point {
    val d = sqrt((m1.x - m2.x).pow(2) + (m1.y - m2.y).pow(2))
    val a = (r1.pow(2) - r2.pow(2) + d.pow(2)) / (2 * d)
    val h = sqrt(r1.pow(2) - a.pow(2))

    val point1 = Point(m1.x + a * (m2.x - m1.x) / d, m1.y + a * (m2.y - m1.y) / d)

    return Point(point1.x + h * (m2.y - m1.y) / d, point1.y - h * (m2.x - m1.x) / d)
}

val Circle.center get() = Point(x + width / 2, y + height / 2)
val Point.center get() = Point(x + 10, y + 10)
val Point.originFromCenter get() = Point(x - 10, y - 10)

fun getClosestPointToCircle(m: Point, r: Float, p: Point): Point {
    val dX = p.x - m.x
    val dY = p.y - m.y

    val magV = sqrt(dX.pow(2) + dY.pow(2))
    val aX = m.x + (dX / magV) * r
    val aY = m.y + (dY / magV) * r

    return Point(aX, aY)
}

fun getIntersectionFourPoints(point1: Point, point2: Point, point3: Point, point4: Point): Point? {
    if ((point1.x - point2.x) * (point3.y - point4.y) - (point1.y - point2.y) * (point3.x - point4.x) == 0f) {
        return null
    }

    val pX = (
        (point1.x * point2.y - point1.y * point2.x) * (point3.x - point4.x) - (point1.x - point2.x) * (point3.x * point4.y - point3.y * point4.x))/
        ((point1.x - point2.x) * (point3.y - point4.y) - (point1.y - point2.y) * (point3.x - point4.x))

    val pY = (
        (point1.x * point2.y - point1.y * point2.x) * (point3.y - point4.y) - (point1.y - point2.y) * (point3.x * point4.y - point3.y * point4.x))/
        ((point1.x - point2.x) * (point3.y - point4.y) - (point1.y - point2.y) * (point3.x - point4.x))

    return Point(pX, pY)
}

fun getPointFromAngleAndDistance(point: Point, angle: Float, distance: Float): Point {
    val x = point.x + distance * cos(angle)
    val y = point.y + distance * sin(angle)

    return Point(x, y)
}

fun getAngleBetweenPoints(point1: Point, point2: Point): Float {
    return atan2(point2.y - point1.y, point2.x - point1.x)
}

fun getDistanceBetweenPoints(point1: Point, point2: Point): Float {
    return sqrt((point1.x - point2.x).pow(2) + (point1.y - point2.y).pow(2))
}

fun getClosestPointToLine(linePoint1: Point, linePoint2: Point, point: Point): Point {
    val AB = linePoint2-linePoint1
    val AP = point-linePoint1
    val ab2 = AB.x*AB.x + AB.y*AB.y
    val ap_ab = AP.x*AB.x + AP.y*AB.y
    val t = ap_ab / ab2
    if (t < 0) return linePoint1
    else if (t > 1) return linePoint2
    return linePoint1 + AB*t
}


fun getPointPerpendicularToLine(linePoint1: Point, linePoint2: Point, point: Point, distance: Float): Pair<Point, Point> {
    val dx = linePoint2.x - linePoint1.x
    val dy = linePoint2.y - linePoint1.y

    val mag = sqrt(dx*dx + dy*dy)
    val dxn = dx / mag
    val dyn = dy / mag

    val px = -dyn
    val py = dxn

    val p1 = Point(point.x + px * distance, point.y + py * distance)
    val p2 = Point(point.x - px * distance, point.y - py * distance)

    return Pair(p1, p2)
}
