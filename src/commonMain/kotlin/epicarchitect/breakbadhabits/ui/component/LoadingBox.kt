package epicarchitect.breakbadhabits.ui.component

//@Composable
//fun <DATA> LoadingBox(
//    controller: DataFlowController<DATA>,
//    modifier: Modifier = Modifier,
//    loaded: @Composable BoxScope.(DATA) -> Unit
//) {
//    val state by controller.state.collectAsState()
//
//    Box(modifier) {
//        when (val state = state) {
//            is DataFlowController.State.Loaded -> {
//                loaded(state.data)
//            }
//
//            is DataFlowController.State.Loading -> {
////                Box(
////                    modifier = Modifier.fillMaxSize(),
////                    contentAlignment = Alignment.Center
////                ) {
////                    ProgressIndicator()
////                }
//            }
//        }
//    }
//}