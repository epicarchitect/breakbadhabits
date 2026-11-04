import SwiftUI
import KMPLib

struct AppView: UIViewControllerRepresentable {

    func makeUIViewController(context: Context) -> UIViewController {
        let controller = IosAppKt.createAppViewController()
        controller.view.backgroundColor = .clear
        return controller
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
