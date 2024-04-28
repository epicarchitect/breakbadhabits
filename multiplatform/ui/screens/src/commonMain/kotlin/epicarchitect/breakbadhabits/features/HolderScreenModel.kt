package epicarchitect.breakbadhabits.features

//
//class HolderScreenModel<T : ViewModel>(val viewModel: T) : ScreenModel {
//    override fun onDispose() {
//        super.onDispose()
//        viewModel.clear()
//    }
//}
//
//@Composable
//fun <T : ViewModel> Screen.hold(
//    tag: String? = null,
//    factory: () -> T
//): T = rememberScreenModel(tag) {
//    HolderScreenModel(factory())
//}.viewModel