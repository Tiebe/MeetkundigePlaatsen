import korlibs.image.color.*
import korlibs.korge.*
import korlibs.korge.input.*
import korlibs.korge.scene.*
import korlibs.korge.view.*
import korlibs.korge.view.Circle
import korlibs.korge.view.Line
import korlibs.math.geom.*
import korlibs.math.geom.Anchor
import kotlin.math.*

suspend fun main() = Korge(
    title = "Korge Compose",
    windowSize = Size(1280, 720),
    backgroundColor = Colors["#2b2b2b"],
    displayMode = KorgeDisplayMode(ScaleMode.SHOW_ALL, Anchor.TOP_LEFT, clipBorders = false),
    forceRenderEveryFrame = false,
) {
    val sceneContainer = sceneContainer()

    sceneContainer.changeTo({ MyScene() })
}

class MyScene : Scene() {
    private lateinit var pointH: Circle
    private lateinit var pointI: Circle
    private lateinit var pointG: Circle
    private lateinit var pointP: Circle
    private lateinit var pointO: Circle
    private lateinit var pointE: Circle

    private lateinit var lineGH: Line
    private lateinit var lineOP: Line
    private lateinit var lineGO: Line
    private lateinit var lineIP: Line
    private lateinit var lineGP: Line
    private lateinit var lineOI: Line

    private lateinit var hCircle: Circle

    private lateinit var labelH: Text
    private lateinit var labelI: Text
    private lateinit var labelG: Text
    private lateinit var labelP: Text
    private lateinit var labelO: Text

    override suspend fun SContainer.sceneMain() {
        pointH = circle(10f).colorMul(Colors.CYAN).xy(400, 400)
        pointI = circle(10f).colorMul(Colors.CYAN).xy(600, 400)
        pointG = circle(10f).colorMul(Colors.RED)
        pointP = circle(10f).colorMul(Colors.RED)
        pointO = circle(10f).colorMul(Colors.GREEN)
        pointE = circle(10f).colorMul(Colors.ORANGE)

        lineGH = line(pointG.center, pointH.center).colorMul(Colors.WHITE)
        lineOP = line(pointO.center, pointP.center).colorMul(Colors.WHITE)
        lineGO = line(pointG.center, pointO.center).colorMul(Colors.WHITE)
        lineIP = line(pointI.center, pointP.center).colorMul(Colors.WHITE)
        lineGP = line(pointG.center, pointP.center).colorMul(Colors.WHITE)
        lineOI = line(pointO.center, pointI.center).colorMul(Colors.WHITE)


        labelH = text("H").colorMul(Colors.CYAN)
        labelI = text("I").colorMul(Colors.CYAN)
        labelG = text("G").colorMul(Colors.RED)
        labelP = text("P").colorMul(Colors.RED)
        labelO = text("O").colorMul(Colors.GREEN)

        hCircle = circle(sqrt(11.39f)* 100, fill = Colors.TRANSPARENT, stroke = Colors.WHITE, strokeThickness = 3f)

        pointH.draggable {
            pointG.position(getClosestPointToCircle(Point(it.viewNextX, it.viewNextY), sqrt(11.39f) * 100, pointG.pos))
            updateScreen()
        }

        pointI.draggable {
            updateScreen()
        }

        pointG.draggable(autoMove = false) {
            pointG.position(getClosestPointToCircle(pointH.center, sqrt(11.39f) * 100, Point(it.viewNextX, it.viewNextY)).originFromCenter)
            updateScreen()
        }

        updateScreen()



    }

    private fun updateScreen() {
        val radius1 = sqrt(8.7f) * 100

        pointP.position(getCircleIntersect(pointG.center, pointI.center, radius1, radius1))
        pointO.position(getCircleIntersect(pointI.center, pointG.center, radius1, radius1))

        lineGH.setPoints(pointG.center, pointH.center)
        lineOP.setPoints(pointO.center, pointP.center)
        lineGO.setPoints(pointG.center, pointO.center)
        lineIP.setPoints(pointI.center, pointP.center)
        lineGP.setPoints(pointG.center, pointP.center)
        lineOI.setPoints(pointO.center, pointI.center)

        hCircle.position(pointH.center - Point(sqrt(11.39f) * 100, sqrt(11.39f) * 100))

        updateLabels()
    }

    private fun updateLabels() {
        val offset = 20

        labelH.position(pointH.center + Point(offset, offset))
        labelI.position(pointI.center + Point(offset, offset))
        labelG.position(pointG.center + Point(offset, offset))
        labelP.position(pointP.center + Point(offset, offset))
        labelO.position(pointO.center + Point(offset, offset))

    }

    private fun getCircleIntersect(m1: Point, m2: Point, r1: Float, r2: Float): Point {
        val d = sqrt((m1.x - m2.x).pow(2) + (m1.y - m2.y).pow(2))
        val a = (r1.pow(2) - r2.pow(2) + d.pow(2)) / (2 * d)
        val h = sqrt(r1.pow(2) - a.pow(2))

        val point1 = Point(m1.x + a * (m2.x - m1.x) / d, m1.y + a * (m2.y - m1.y) / d)


        return Point(point1.x + h * (m2.y - m1.y) / d, point1.y - h * (m2.x - m1.x) / d)
    }

    private val Circle.center get() = Point(x + width / 2, y + height / 2)
    private val Point.originFromCenter get() = Point(x - 10, y - 10)

    private fun getClosestPointToCircle(m: Point, r: Float, p: Point): Point {
        val dX = p.x - m.x
        val dY = p.y - m.y

        val magV = sqrt(dX.pow(2) + dY.pow(2))
        val aX = m.x + (dX / magV) * r
        val aY = m.y + (dY / magV) * r

        return Point(aX, aY)
    }
}
