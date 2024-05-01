import UIKit
import SwiftUI
import KMPLib

@main
struct iosApp: App {
    var body: some Scene {
        WindowGroup {
            AppView().ignoresSafeArea(.keyboard)
        }
    }
}

struct AppView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        IosAppKt.AppViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
