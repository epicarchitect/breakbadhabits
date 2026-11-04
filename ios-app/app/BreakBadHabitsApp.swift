import SwiftUI
import KMPLib

@main
struct BreakBadHabitsApp: App {

    init() {
        IosAppInitializer().initialize()
    }

    var body: some Scene {
        WindowGroup {
            AppView()
                .ignoresSafeArea()
                .preferredColorScheme(.dark)
                .hidesWhenInactive()
        }
    }
}
