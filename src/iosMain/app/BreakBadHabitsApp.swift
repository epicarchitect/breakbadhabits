//
//  breakbadhabitsApp.swift
//  breakbadhabits
//
//  Created by alex on 01.05.2024.
//

import SwiftUI
import KMPLib

@main
struct BreakBadHabitsApp: App {
    var body: some Scene {
        WindowGroup {
            ComposeView()
                .ignoresSafeArea(.keyboard) // Compose has own keyboard handler
        }
    }
}

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        IosAppKt.AppViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
