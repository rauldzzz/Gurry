package com.example.gurry.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import io.github.sceneview.Scene
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes

@Composable
fun Horse3DViewer(
    modelUrl: String,
    modifier: Modifier = Modifier,
    enableTouch: Boolean = true
) {
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)

    val modelNode = remember {
        try {
            ModelNode(
                modelInstance = modelLoader.createModelInstance(
                    assetFileLocation = modelUrl
                ),
                // 1. ZOOM: Aumentamos el tamaño (antes 1.5f).
                // Prueba con 2.3f o 2.5f. Si es mucho, bájalo a 2.0f.
                scaleToUnits = 13f
            ).apply {
                // Centrado (esto lo mantenemos porque funcionaba bien)
                centerOrigin(Position(x = 0.0f, y = 0.0f, z = 0.0f))

                // 2. ROTACIÓN (Mirando al frente):
                // x = -10.0f: Para levantarle un poco la cabeza si miraba al suelo.
                // y = 15.0f:  Casi 0 es mirando al frente. Le damos 15 grados
                //             para que se vea un pelín de lado (se aprecia mejor el 3D).
                //             Si lo quieres TOTALMENTE de frente, pon y = 0.0f.
                rotation = Rotation(x = -93.0f, y = -30.0f, z = 0.0f)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    if (modelNode != null) {
        val finalModifier = if (enableTouch) {
            modifier.fillMaxSize()
        } else {
            modifier.fillMaxSize().pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        awaitPointerEvent()
                    }
                }
            }
        }

        Scene(
            modifier = finalModifier,
            engine = engine,
            modelLoader = modelLoader,
            childNodes = rememberNodes {
                add(modelNode)
            }
            // Eliminado onViewCreated para evitar errores
        )
    }
}