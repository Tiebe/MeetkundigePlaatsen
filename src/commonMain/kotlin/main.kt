import korlibs.image.color.*
import korlibs.korge.*
import korlibs.korge.input.*
import korlibs.korge.scene.*
import korlibs.korge.ui.*
import korlibs.korge.view.*
import korlibs.korge.view.Circle
import korlibs.korge.view.Line
import korlibs.math.*
import korlibs.math.geom.*
import korlibs.math.geom.Anchor
import kotlin.math.*

suspend fun main() = Korge(
    title = "Ellipsograaf",
    windowSize = Size(1280, 720),
    backgroundColor = Colors["#2b2b2b"],
    displayMode = KorgeDisplayMode(ScaleMode.SHOW_ALL, Anchor.TOP_LEFT, clipBorders = false),
    forceRenderEveryFrame = false,
) {
    val sceneContainer = sceneContainer()

    sceneContainer.changeTo({ Ellipsograaf() })
}
