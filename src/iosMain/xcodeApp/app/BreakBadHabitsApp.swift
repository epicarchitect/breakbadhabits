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
            AppView()
                .ignoresSafeArea()
        }
    }
}

struct AppView: UIViewControllerRepresentable {
    
    func makeUIViewController(context: Context) -> UIViewController {
        IosAppKt.createAppViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
