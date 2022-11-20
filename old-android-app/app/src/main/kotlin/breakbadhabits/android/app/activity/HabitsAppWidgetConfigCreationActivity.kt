package breakbadhabits.android.app.activity


//class HabitsAppWidgetConfigCreationActivity : ComposeActivity() {
//
//    override val themeResourceId = R.style.Activity
//
//    @Composable
//    override fun Content() {
//        RootEpicStore {
//            val appWidgetId = remember {
//                intent.extras!!.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID)
//            }
//
//            HabitsAppWidgetConfigCreationScreen(
//                appWidgetId = appWidgetId,
//                onFinished = {
//                    setResult(
//                        RESULT_OK,
//                        Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
//                    )
//                    finish()
//                }
//            )
//        }
//    }
//}