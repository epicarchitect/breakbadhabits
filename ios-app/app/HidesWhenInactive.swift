import SwiftUI

private struct HidesWhenInactive: ViewModifier {
    @Environment(\.scenePhase) private var scenePhase

    func body(content: Content) -> some View {
        content.overlay {
            if scenePhase != .active {
                Color(uiColor: .systemBackground)
                    .ignoresSafeArea()
            }
        }
    }
}

extension View {
    func hidesWhenInactive() -> some View {
        modifier(HidesWhenInactive())
    }
}
