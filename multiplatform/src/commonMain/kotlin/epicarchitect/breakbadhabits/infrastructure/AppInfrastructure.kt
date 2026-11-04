package epicarchitect.breakbadhabits.infrastructure

import epicarchitect.breakbadhabits.analytics.AnalyticsOperator
import epicarchitect.breakbadhabits.database.AppDatabaseFactory
import epicarchitect.breakbadhabits.export.DataExporter
import epicarchitect.breakbadhabits.format.DateTimeFormatter
import epicarchitect.breakbadhabits.monetization.MonetizationEnvironment
import epicarchitect.breakbadhabits.url.UrlLauncher

var isDebug = false

var appVersion: String = ""

lateinit var AppDatabaseFactory: AppDatabaseFactory
lateinit var DateTimeFormatter: DateTimeFormatter
lateinit var AnalyticsOperator: AnalyticsOperator
lateinit var UrlOpener: UrlLauncher
lateinit var Monetization: MonetizationEnvironment
lateinit var DataExporter: DataExporter

val AppDatabase by lazy {
    AppDatabaseFactory.create(databaseName = "breakbadhabits.db")
}
