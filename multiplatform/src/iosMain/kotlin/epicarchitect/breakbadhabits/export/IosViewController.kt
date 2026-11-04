package epicarchitect.breakbadhabits.export

import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow
import platform.UIKit.UIWindowScene

@OptIn(ExperimentalForeignApi::class)
internal fun topPresentingViewController(): UIViewController? {
    val windows = UIApplication.sharedApplication.connectedScenes
        .filterIsInstance<UIWindowScene>()
        .flatMap { scene -> scene.windows.filterIsInstance<UIWindow>() }
    val window = windows.firstOrNull { it.isKeyWindow() } ?: windows.firstOrNull()
    var controller = window?.rootViewController ?: return null
    while (controller.presentedViewController != null) {
        controller = controller.presentedViewController ?: break
    }
    return controller
}
