import UIKit
import SwiftUI
import MultiplatformApp

@main
struct iosApp: App {
    init() {
        DiKt.setupAppModuleHolder()
    }

    var body: some Scene {
        WindowGroup {
            AppView().ignoresSafeArea(.keyboard)
        }
    }
}

struct AppView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        AppKt.AppViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
