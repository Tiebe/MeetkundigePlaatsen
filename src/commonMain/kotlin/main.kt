import korlibs.event.*
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

lateinit var sceneContainer: SceneContainer

suspend fun main() = Korge(
    title = "Meetkundige plaatsen",
    windowSize = Size(1280, 720),
    backgroundColor = Colors["#2b2b2b"],
    displayMode = KorgeDisplayMode(ScaleMode.SHOW_ALL, Anchor.TOP_LEFT, clipBorders = false),
    forceRenderEveryFrame = false,
) {
    sceneContainer = sceneContainer()

    sceneContainer.changeTo({ Menu() })
}

class Menu : Scene() {
    override suspend fun SContainer.sceneInit() {
        text("Start Ellipsograaf (1)") {
            position(50, 100)
            colorMul = Colors.WHITE
            fontSize = 100.0f
        }.onClick {
            sceneContainer.changeTo({ Ellipsograaf() })
        }

        text("Start Hyperbolograaf (2)") {
            position(50, 200)
            colorMul = Colors.WHITE
            fontSize = 100.0f
        }.onClick {
            sceneContainer.changeTo({ Hyperbolograaf() })
        }

        text("Start Parabolograaf (3)") {
            position(50, 300)
            colorMul = Colors.WHITE
            fontSize = 100.0f
        }.onClick {
            sceneContainer.changeTo({ Parabolograaf() })
        }
    }

    override suspend fun SContainer.sceneMain() {
        keys {
            down {
                when (it.key) {
                    Key.N1 -> sceneContainer.changeTo({ Ellipsograaf() })
                    Key.N2 -> sceneContainer.changeTo({ Hyperbolograaf() })
                    Key.N3 -> sceneContainer.changeTo({ Parabolograaf() })
                    else -> {}
                }
            }
        }
    }
}
