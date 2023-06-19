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

class Hyperbolograaf : Scene() {
    private lateinit var pointC: Circle
    private lateinit var pointF: Circle
    private lateinit var pointD: Circle
    private lateinit var pointM: Circle
    private lateinit var pointL: Circle
    private lateinit var pointE: Circle

    private lateinit var cCircle: Circle
    private lateinit var fCircle: Circle
    private lateinit var dCircle: Circle

    private lateinit var labelC: Text
    private lateinit var labelF: Text
    private lateinit var labelD: Text
    private lateinit var labelL: Text
    private lateinit var labelM: Text
    private lateinit var labelE: Text

    private lateinit var lineMD: Line
    private lateinit var lineLD: Line
    private lateinit var lineCD: Line
    private lateinit var lineLF: Line
    private lateinit var lineML: Line
    private lateinit var lineMF: Line

    private lateinit var sliderLength: UISlider
    private lateinit var sliderRadius: UISlider

    private var circlesVisible = false
    private var showDrawnDots = true
    private var labelsVisible = true

    private val drawnPointsList = arrayListOf<Circle>()

    private var radiusC: Float = sqrt(2.65f) *100f
    private var radiusFD: Float = sqrt(3.73f) *100f

    override suspend fun SContainer.sceneMain() {
        options()
        resetButtons()

        pointC = circle(10f).colorMul(Colors.CYAN).xy(400, 200)
        pointF = circle(10f).colorMul(Colors.CYAN).xy(600, 400).draggable {
            updateScreen()
        }

        pointD = circle(10f).colorMul(Colors.RED).position(getClosestPointToCircle(pointC.center, radiusC, Point(600, 200)))

        pointM = circle(10f).colorMul(Colors.GREEN)
        pointL = circle(10f).colorMul(Colors.GREEN)
        pointE = circle(10f).colorMul(Colors.ORANGE)

        labelC = text("C").colorMul(Colors.CYAN)
        labelF = text("F").colorMul(Colors.CYAN)
        labelD = text("D").colorMul(Colors.RED)
        labelL = text("L").colorMul(Colors.GREEN)
        labelM = text("M").colorMul(Colors.GREEN)
        labelE = text("E").colorMul(Colors.ORANGE)

        lineMD = line(pointM.center, pointD.center).colorMul(Colors.WHITE)
        lineLD = line(pointL.center, pointD.center).colorMul(Colors.WHITE)
        lineCD = line(pointC.center, pointD.center).colorMul(Colors.WHITE)
        lineLF = line(pointL.center, pointF.center).colorMul(Colors.WHITE)
        lineML = line(pointM.center, pointL.center).colorMul(Colors.WHITE)
        lineMF = line(pointM.center, pointF.center).colorMul(Colors.WHITE)


        pointC.draggable {
            pointD.position(getClosestPointToCircle(Point(it.viewNextX, it.viewNextY), radiusC, pointD.pos))
            updateScreen()
        }

        pointD.draggable(autoMove = false) {
            if (getAngleBetweenPoints(pointC.pos, Point(it.viewNextX, it.viewNextY)) > getAngleBetweenPoints(pointM.pos, pointL.pos) &&
                getAngleBetweenPoints(Point(it.viewNextX, it.viewNextY), pointC.pos) < getAngleBetweenPoints(pointM.pos, pointL.pos)
                ) {
                    pointD.position(
                        getClosestPointToCircle(
                            pointC.center,
                            radiusC,
                            Point(it.viewNextX, it.viewNextY)
                        ).originFromCenter
                    )
                    updateScreen()
                }
        }

        cCircle = circle(radiusC, fill = Colors.TRANSPARENT, stroke = Colors.WHITE, strokeThickness = 3f)
        fCircle = circle(radiusFD, fill = Colors.TRANSPARENT, stroke = Colors.WHITE, strokeThickness = 3f)
        dCircle = circle(radiusFD, fill = Colors.TRANSPARENT, stroke = Colors.WHITE, strokeThickness = 3f)

        updateScreen()
        moveToTop()
    }

    private fun SContainer.updateScreen() {
        cCircle.position(pointC.center - Point(radiusC, radiusC)).visible(circlesVisible).radius = radiusC
        fCircle.position(pointF.center - Point(radiusFD, radiusFD)).visible(circlesVisible).radius = radiusFD
        dCircle.position(pointD.center - Point(radiusFD, radiusFD)).visible(circlesVisible).radius = radiusFD

        pointD.position(getClosestPointToCircle(pointC.center, radiusC, pointD.center).originFromCenter)

        pointM.position(getCircleIntersect(pointF.center, pointD.center, radiusFD, radiusFD).originFromCenter)
        pointL.position(getCircleIntersect(pointD.center, pointF.center, radiusFD, radiusFD).originFromCenter)

        pointE.position(getIntersectionFourPoints(pointC.center, pointD.center, pointM.center, pointL.center)?.originFromCenter ?: Point(0, 0))

        lineCD.setPoints(pointC.center, pointE.center)
        lineMD.setPoints(pointM.center, pointD.center)
        lineLD.setPoints(pointL.center, pointD.center)
        lineLF.setPoints(pointL.center, pointF.center)
        lineML.setPoints(pointM.center, pointE.center)
        lineMF.setPoints(pointM.center, pointF.center)

        println("ML" + getAngleBetweenPoints(pointM.center, pointL.center))
        println("CD" + getAngleBetweenPoints(pointD.center, pointC.center))


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
        pointC.bringToTop()
        pointF.bringToTop()
        pointD.bringToTop()
        pointL.bringToTop()
        pointM.bringToTop()
        pointE.bringToTop()
        labelC.bringToTop()
        labelF.bringToTop()
        labelD.bringToTop()
        labelL.bringToTop()
        labelM.bringToTop()
        labelE.bringToTop()
    }

    private fun updateLabels() {
        val offset = 20

        labelC.position(pointC.center + Point(offset, offset))
        labelF.position(pointF.center + Point(offset, offset))
        labelD.position(pointD.center + Point(offset, offset))
        labelL.position(pointL.center + Point(offset, offset))
        labelM.position(pointM.center + Point(offset, offset))
        labelE.position(pointE.center + Point(offset, offset))
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
            radiusC = sqrt(2.65f) *100f
            radiusFD = sqrt(3.73f) *100f
            sliderLength.value = radiusFD/100.0
            sliderLength.text.text = "Diamond size: ${sliderLength.value.roundDecimalPlaces(2)}"
            sliderRadius.value = radiusC/100.0
            sliderRadius.text.text = "Circle radius: ${sliderRadius.value.roundDecimalPlaces(2)}"
            pointC.position(Point(400, 200))
            pointF.position(Point(600, 400))

            pointD.position(getClosestPointToCircle(pointC.center, radiusC, Point(600, 200)))
            drawnPointsList.forEach { it.removeFromParent() }
            drawnPointsList.clear()

            updateScreen()
        }
    }

    private fun SContainer.options() {
        sliderLength = uiSlider(radiusFD/100, min = 0.01f, max = 6.38f, step = 0.01f).xy(10, 10)
        sliderLength.text.color = Colors.WHITE
        sliderLength.text.text = "Diamond size: ${sliderLength.value.roundDecimalPlaces(2)}"
        sliderLength.onChange {
            radiusFD = it * 100
            sliderLength.text.text = "Diamond size: ${sliderLength.value.roundDecimalPlaces(2)}"
            updateScreen()
        }

        sliderRadius = uiSlider(radiusC/100, min = 0.01f, max = 6.38f, step = 0.01f).xy(10, 30)
        sliderRadius.text.text = "Circle radius: ${sliderRadius.value.roundDecimalPlaces(2)}"
        sliderRadius.text.color = Colors.WHITE
        sliderRadius.onChange {
            radiusC = it * 100
            pointD.position(getClosestPointToCircle(pointC.center, radiusC, pointD.pos))
            sliderRadius.text.text = "Circle radius: ${sliderRadius.value.roundDecimalPlaces(2)}"
            updateScreen()
        }

        uiCheckBox(size = Size(150, 32), checked = circlesVisible, text = "Show circles").xy(10, 50).onChange {
            circlesVisible = it.checked
            cCircle.visible(circlesVisible)
            fCircle.visible(circlesVisible)
            dCircle.visible(circlesVisible)
        }

        uiCheckBox(size = Size(200, 32), checked = showDrawnDots, text = "Show drawn dots").xy(10, 80).onChange { checkbox ->
            showDrawnDots = checkbox.checked
            drawnPointsList.forEach { it.removeFromParent() }
            drawnPointsList.clear()
        }

        uiCheckBox(size = Size(150, 32), checked = labelsVisible, text = "Show labels").xy(10, 110).onChange {
            labelsVisible = it.checked
            labelC.visible(labelsVisible)
            labelF.visible(labelsVisible)
            labelD.visible(labelsVisible)
            labelL.visible(labelsVisible)
            labelM.visible(labelsVisible)
        }
    }
}
