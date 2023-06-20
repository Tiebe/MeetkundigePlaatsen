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

class Ellipsograaf : Scene() {
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
    private lateinit var iCircle: Circle
    private lateinit var gCircle: Circle

    private lateinit var labelH: Text
    private lateinit var labelI: Text
    private lateinit var labelG: Text
    private lateinit var labelP: Text
    private lateinit var labelO: Text
    private lateinit var labelE: Text

    private lateinit var sliderLength: UISlider
    private lateinit var sliderRadius: UISlider

    private var circlesVisible = false
    private var showDrawnDots = true
    private var labelsVisible = true

    private val drawnPointsList = arrayListOf<Circle>()

    private var radiusH: Float = sqrt(11.39f) *100f
    private var radiusIG: Float = sqrt(8.7f) *100f

    override suspend fun SContainer.sceneMain() {


        pointH = circle(10f).colorMul(Colors.CYAN).xy(400, 400)
        pointI = circle(10f).colorMul(Colors.CYAN).xy(600, 400)
        pointG = circle(10f).colorMul(Colors.RED).position(getClosestPointToCircle(pointH.center, radiusH, Point(600, 200)))
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
        labelE = text("E").colorMul(Colors.ORANGE)

        hCircle = circle(radiusH, fill = Colors.TRANSPARENT, stroke = Colors.WHITE, strokeThickness = 3f)
        iCircle = circle(radiusIG, fill = Colors.TRANSPARENT, stroke = Colors.WHITE, strokeThickness = 3f)
        gCircle = circle(radiusIG, fill = Colors.TRANSPARENT, stroke = Colors.WHITE, strokeThickness = 3f)

        pointH.draggable {
            pointG.position(getClosestPointToCircle(Point(it.viewNextX, it.viewNextY), radiusH, pointG.pos))
            updateScreen()
        }

        pointI.draggable {
            updateScreen()
        }

        pointG.draggable(autoMove = false) {
            pointG.position(getClosestPointToCircle(pointH.center, radiusH, Point(it.viewNextX, it.viewNextY)).originFromCenter)
            updateScreen()
        }

        options()
        resetButtons()
        updateScreen()
        moveToTop()
    }

    private fun SContainer.updateScreen() {
        pointP.position(getCircleIntersect(pointG.center, pointI.center, radiusIG, radiusIG).originFromCenter)
        pointO.position(getCircleIntersect(pointI.center, pointG.center, radiusIG, radiusIG).originFromCenter)

        val eCoord = getIntersectionFourPoints(pointG.center, pointH.center, pointO.center, pointP.center)
        if (eCoord != null) {
            pointE.visible(true).position(eCoord.originFromCenter)
            labelE.visible(true)
        } else {
            pointE.visible(false)
            labelE.visible(false)
        }

        lineGH.setPoints(pointG.center, pointH.center)
        lineOP.setPoints(pointO.center, pointP.center)
        lineGO.setPoints(pointG.center, pointO.center)
        lineIP.setPoints(pointI.center, pointP.center)
        lineGP.setPoints(pointG.center, pointP.center)
        lineOI.setPoints(pointO.center, pointI.center)

        hCircle.position(pointH.center - Point(radiusH, radiusH)).radius = radiusH
        iCircle.position(pointI.center - Point(radiusIG, radiusIG)).radius = radiusIG
        gCircle.position(pointG.center - Point(radiusIG, radiusIG)).radius = radiusIG

        hCircle.visible(circlesVisible)
        iCircle.visible(circlesVisible)
        gCircle.visible(circlesVisible)

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
        pointH.bringToTop()
        pointI.bringToTop()
        pointG.bringToTop()
        pointP.bringToTop()
        pointO.bringToTop()
        pointE.bringToTop()
        labelH.bringToTop()
        labelI.bringToTop()
        labelG.bringToTop()
        labelP.bringToTop()
        labelO.bringToTop()
        labelE.bringToTop()
    }

    private fun updateLabels() {
        val offset = 20

        labelH.position(pointH.center + Point(offset, offset))
        labelI.position(pointI.center + Point(offset, offset))
        labelG.position(pointG.center + Point(offset, offset))
        labelP.position(pointP.center + Point(offset, offset))
        labelO.position(pointO.center + Point(offset, offset))
        labelE.position(pointE.center + Point(offset, offset))
    }

    private fun SContainer.options() {

        sliderLength = uiSlider(radiusIG/100, min = 0.01f, max = 6.38f, step = 0.01f).xy(10, 10)
        sliderLength.text.color = Colors.WHITE
        sliderLength.text.text = "Diamond size: ${sliderLength.value.roundDecimalPlaces(2)}"
        sliderLength.onChange {
            radiusIG = it * 100
            sliderLength.text.text = "Diamond size: ${sliderLength.value.roundDecimalPlaces(2)}"
            updateScreen()
        }

        sliderRadius = uiSlider(radiusH/100, min = 0.01f, max = 6.38f, step = 0.01f).xy(10, 30)
        sliderRadius.text.text = "Circle radius: ${sliderRadius.value.roundDecimalPlaces(2)}"
        sliderRadius.text.color = Colors.WHITE
        sliderRadius.onChange {
            radiusH = it * 100
            pointG.position(getClosestPointToCircle(pointH.center, radiusH, pointG.pos))
            sliderRadius.text.text = "Circle radius: ${sliderRadius.value.roundDecimalPlaces(2)}"
            updateScreen()
        }

        uiCheckBox(size = Size(150, 32), checked = circlesVisible, text = "Show circles").xy(10, 50).onChange {
            circlesVisible = it.checked
            hCircle.visible(circlesVisible)
            iCircle.visible(circlesVisible)
            gCircle.visible(circlesVisible)
        }

        uiCheckBox(size = Size(200, 32), checked = showDrawnDots, text = "Show drawn dots").xy(10, 80).onChange { checkbox ->
            showDrawnDots = checkbox.checked
            drawnPointsList.forEach { it.removeFromParent() }
            drawnPointsList.clear()
        }

        uiCheckBox(size = Size(150, 32), checked = labelsVisible, text = "Show labels").xy(10, 110).onChange {
            labelsVisible = it.checked
            labelH.visible(labelsVisible)
            labelI.visible(labelsVisible)
            labelG.visible(labelsVisible)
            labelP.visible(labelsVisible)
            labelO.visible(labelsVisible)
            labelE.visible(labelsVisible)
        }


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
            radiusH = sqrt(11.39f) *100f
            radiusIG = sqrt(8.7f) *100f
            sliderLength.value = radiusIG/100.0
            sliderLength.text.text = "Diamond size: ${sliderLength.value.roundDecimalPlaces(2)}"
            sliderRadius.value = radiusH/100.0
            sliderRadius.text.text = "Circle radius: ${sliderRadius.value.roundDecimalPlaces(2)}"
            pointH.position(400, 400)
            pointI.position(600, 400)

            pointG.position(getClosestPointToCircle(pointH.center, radiusH, Point(600, 200)))
            drawnPointsList.forEach { it.removeFromParent() }
            drawnPointsList.clear()

            updateScreen()
        }
    }

}
