import korlibs.event.*
import korlibs.image.color.*
import korlibs.korge.input.*
import korlibs.korge.scene.*
import korlibs.korge.ui.*
import korlibs.korge.view.*
import korlibs.korge.view.Circle
import korlibs.korge.view.Line
import korlibs.math.*
import korlibs.math.geom.*
import kotlin.math.*

class Parabolograaf : Scene() {
    private lateinit var pointJ: Circle
    private lateinit var pointI: Circle
    private lateinit var pointG: Circle
    private lateinit var pointH: Circle
    private lateinit var pointF: Circle
    private lateinit var pointE: Circle
    private lateinit var pointB: Circle

    private lateinit var gCircle: Circle
    private lateinit var bCircle: Circle

    private lateinit var labelJ: Text
    private lateinit var labelI: Text
    private lateinit var labelG: Text
    private lateinit var labelH: Text
    private lateinit var labelF: Text
    private lateinit var labelE: Text
    private lateinit var labelB: Text

    private lateinit var lineJI: Line
    private lateinit var lineGH: Line
    private lateinit var lineGF: Line
    private lateinit var lineFB: Line
    private lateinit var lineBH: Line
    private lateinit var lineHF: Line

    private lateinit var lineGE: Line

    private lateinit var sliderLength: UISlider
    private lateinit var sliderRadius: UISlider

    private var circlesVisible = false
    private var showDrawnDots = true
    private var labelsVisible = true

    private val drawnPointsList = arrayListOf<Circle>()

    private var radius: Float = sqrt(11.73f) *100f

    override suspend fun SContainer.sceneMain() {
        options()
        resetButtons()

        pointJ = circle(10f).colorMul(Colors.CYAN).xy(200, 400)
        pointI = circle(10f).colorMul(Colors.CYAN).xy(1000, 400)

        pointB = circle(10f).colorMul(Colors.CYAN).xy(600, 300)

        pointG = circle(10f).colorMul(Colors.RED).position(getClosestPointToLine(pointJ.pos, pointI.pos, Point(600f, 600f)))

        pointH = circle(10f).colorMul(Colors.GREEN)
        pointF = circle(10f).colorMul(Colors.GREEN)
        pointE = circle(10f).colorMul(Colors.ORANGE)

        lineGE = line(Point(0,0), Point(0,0), Colors.WHITE)

        labelJ = text("J").colorMul(Colors.WHITE)
        labelI = text("I").colorMul(Colors.WHITE)
        labelG = text("G").colorMul(Colors.WHITE)
        labelH = text("H").colorMul(Colors.WHITE)
        labelF = text("F").colorMul(Colors.WHITE)
        labelE = text("E").colorMul(Colors.WHITE)
        labelB = text("B").colorMul(Colors.WHITE)


        lineJI = line(pointJ.pos, pointI.pos, Colors.WHITE)
        lineGH = line(pointG.pos, pointH.pos, Colors.WHITE)
        lineGF = line(pointG.pos, pointF.pos, Colors.WHITE)
        lineFB = line(pointF.pos, pointB.pos, Colors.WHITE)
        lineBH = line(pointB.pos, pointH.pos, Colors.WHITE)
        lineHF = line(pointH.pos, pointF.pos, Colors.WHITE)


        pointB.draggable {
            updateScreen()
        }

        pointI.draggable {
            pointG.position(getClosestPointToLine(Point(it.viewNextX, it.viewNextY), pointJ.pos, pointG.pos))
            updateScreen()
        }

        pointJ.draggable {
            pointG.position(getClosestPointToLine(Point(it.viewNextX, it.viewNextY), pointI.pos, pointG.pos))
            updateScreen()
        }

        pointG.draggable(autoMove = false) {
            pointG.position(getClosestPointToLine(pointJ.pos, pointI.pos, Point(it.viewNextX, it.viewNextY)))
            updateScreen()
        }

        gCircle = circle(radius, fill = Colors.TRANSPARENT, stroke = Colors.WHITE, strokeThickness = 3f)
        bCircle = circle(radius, fill = Colors.TRANSPARENT, stroke = Colors.WHITE, strokeThickness = 3f)

        updateScreen()
        moveToTop()
    }

    private fun SContainer.updateScreen() {
        gCircle.position(pointG.center - Point(radius, radius)).visible(circlesVisible).radius = radius
        bCircle.position(pointB.center - Point(radius, radius)).visible(circlesVisible).radius = radius

        pointG.position(getClosestPointToLine(pointJ.pos, pointI.pos, pointG.pos))

        pointH.position(getCircleIntersect(pointB.center, pointG.center, radius, radius).originFromCenter)
        pointF.position(getCircleIntersect(pointG.center, pointB.center, radius, radius).originFromCenter)

        val points = getPointPerpendicularToLine(pointJ.pos, pointI.pos, pointG.pos, 5000f)

        pointE.position(getIntersectionFourPoints(pointG.center, points.first.center, pointF.center, pointH.center)?.originFromCenter ?: Point(0, 0))

        lineBH.setPoints(pointB.center, pointH.center)
        lineFB.setPoints(pointF.center, pointB.center)
        lineGF.setPoints(pointG.center, pointF.center)
        lineGH.setPoints(pointG.center, pointH.center)
        lineJI.setPoints(pointJ.center, pointI.center)
        lineHF.setPoints(pointH.center, pointF.center)
        lineGE.setPoints(points.first.center, points.second.center)


        if (showDrawnDots) {
            drawnPointsList.add(circle(10f).position(pointE.pos).colorMul(Colors.WHITE).apply { bringToBottom() })
            if (drawnPointsList.size > 1000) {
                drawnPointsList[0].removeFromParent()
                drawnPointsList.removeAt(0)
            }
        }

        updateLabels()
    }

    private fun moveToTop() {
        pointJ.bringToTop()
        pointI.bringToTop()
        pointG.bringToTop()
        pointF.bringToTop()
        pointH.bringToTop()
        pointE.bringToTop()
        pointB.bringToTop()

        labelJ.bringToTop()
        labelI.bringToTop()
        labelG.bringToTop()
        labelF.bringToTop()
        labelH.bringToTop()
        labelE.bringToTop()
        labelB.bringToTop()
    }

    private fun updateLabels() {
        val offset = 20

        labelJ.position(pointJ.pos + Point(offset, offset))
        labelI.position(pointI.pos + Point(offset, offset))
        labelG.position(pointG.pos + Point(offset, offset))
        labelF.position(pointF.pos + Point(offset, offset))
        labelH.position(pointH.pos + Point(offset, offset))
        labelE.position(pointE.pos + Point(offset, offset))
        labelB.position(pointB.pos + Point(offset, offset))
    }

    private fun SContainer.resetButtons() {
        keys.down {
            if (it.key == Key.ESCAPE) {
                sceneContainer.changeTo<Menu>()
            }
        }

        uiButton(label = "Back").xy(300, 10).onClick {
            sceneContainer.changeTo<Menu>()
        }

        uiButton(label = "Reset").xy(450, 10).onClick {
            radius = sqrt(11.73f)*100
            sliderLength.value = radius/100.0
            sliderRadius.text.text = "Circle radius: ${sliderRadius.value.roundDecimalPlaces(2)}"
            pointJ.position(Point(200, 800))
            pointI.position(Point(1000, 800))

            pointB.position(Point(600, 400))

            pointG.position(Point(600, 600))

            drawnPointsList.forEach { it.removeFromParent() }
            drawnPointsList.clear()

            updateScreen()
        }
    }

    private fun SContainer.options() {
        uiCheckBox(size = Size(150, 32), checked = circlesVisible, text = "Show circles").xy(10, 10).onChange {
            circlesVisible = it.checked
            gCircle.visible(circlesVisible)
            bCircle.visible(circlesVisible)
        }

        uiCheckBox(size = Size(200, 32), checked = showDrawnDots, text = "Show drawn dots").xy(10, 40).onChange { checkbox ->
            showDrawnDots = checkbox.checked
            drawnPointsList.forEach { it.removeFromParent() }
            drawnPointsList.clear()
        }

        uiCheckBox(size = Size(150, 32), checked = labelsVisible, text = "Show labels").xy(10, 70).onChange {
            labelsVisible = it.checked
            labelJ.visible(labelsVisible)
            labelI.visible(labelsVisible)
            labelG.visible(labelsVisible)
            labelF.visible(labelsVisible)
            labelH.visible(labelsVisible)
            labelE.visible(labelsVisible)
            labelB.visible(labelsVisible)
        }
    }
}
