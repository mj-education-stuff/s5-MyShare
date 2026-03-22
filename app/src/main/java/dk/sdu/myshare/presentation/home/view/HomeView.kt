package dk.sdu.myshare.presentation.home.view

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateInt
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dk.sdu.myshare.business.utility.ViewModelFactory
import dk.sdu.myshare.presentation.friends.view.FriendsView
import dk.sdu.myshare.presentation.group.mygroups.view.MyGroupsViewRoot
import dk.sdu.myshare.presentation.home.viewmodel.HomeViewModel

@Composable
fun HomeViewRoot(
    navController: NavHostController,
    viewModel: HomeViewModel
) {
//    HomeView(navController = navController)
    ColorChangingSlider()
}

@Composable
fun HomeView(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    var selectedView by remember { mutableStateOf("Groups") }

    Box(
        modifier = modifier.fillMaxHeight(),
        contentAlignment = Alignment.TopCenter,
        content = {
            Column(
                content = {
//                    if (selectedView == "Groups") {
//                        MyGroupsViewRoot(navController, ViewModelFactory.getMyGroupsViewModel(1))
//                    } else if (selectedView == "Friends") {
//                        FriendsView(navController, ViewModelFactory.getFriendsViewModel())
//                    }

                    AnimatedVisibility(
                        visible = selectedView == "Groups",
//                        enter = slideInHorizontally(),
//                        exit = slideOutHorizontally(),
                        enter = slideInHorizontally(
                            initialOffsetX = { -it * 4 },
                            animationSpec = tween(
                                durationMillis = 300,
                                delayMillis = 100
                            )
                        ),
                        exit = slideOutVertically(
                            targetOffsetY = { it * 8 },
                            animationSpec = tween(
                                durationMillis = 500,
                                delayMillis = 0
                            )
                        ),
                        content = {
                            MyGroupsViewRoot(navController, ViewModelFactory.getMyGroupsViewModel(1))
                        }
                    )

                    AnimatedVisibility(
                        visible = selectedView == "Friends",
//                        enter = slideInHorizontally(),
//                        exit = slideOutHorizontally(),
                        enter = slideInHorizontally(
                            initialOffsetX = { it * 4 },
                            animationSpec = tween(
                                durationMillis = 300,
                                delayMillis = 300
                            )
                        ),
                        exit = slideOutVertically(
//                            targetOffsetY = { if (it < 1) it * 4 else it }, just random example to show that if statements are possible
                            targetOffsetY = {  it * 4 },
                            animationSpec = tween(
                                durationMillis = 500,
                                delayMillis = 0
                            )
                        ),
                        content = {
                            FriendsView(navController, ViewModelFactory.getFriendsViewModel())
                        }
                    )
                }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                content = {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Groups",
                        modifier = Modifier
                            .weight(1f)
                            .height(28.dp)
                            .clickable { selectedView = "Groups" }
                    )
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = "Friends",
                        modifier = Modifier
                            .weight(1f)
                            .height(28.dp)
                            .clickable { selectedView = "Friends" }
                    )
                }
            )
        }
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ColorChangingSlider() {

    // Color changing slider
    val sliderPosition = remember { mutableStateOf(0f) }

    // Calculate color based on slider position
    val sliderColor = Color(
        red = sliderPosition.value,
        green = 1f - sliderPosition.value,
        blue = 0.5f
    )

    val transitionColor = updateTransition( // can be used to animate different / multiple animations
        targetState = sliderPosition,
        label = "sliderTransition"
    )

    val color by transitionColor.animateColor(
        transitionSpec = { tween(1000) },
        label = "color",
        targetValueByState = { sliderPosition ->
            if (sliderPosition.value > 0.5f) Color.Green else Color.LightGray
        }
    )

    // Shape changing button
    var isRound by remember { mutableStateOf(false) }

    val borderRadius by animateIntAsState(
        targetValue = if (isRound) 40 else 20,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessVeryLow,
        )
    )

    val transitionRound = updateTransition(
        targetState = isRound,
        label = null
    )

    val borderRadius2 by transitionRound.animateInt(
        transitionSpec = { tween(1000) },
        label = "borderRadius",
        targetValueByState = { isRound ->
            if (isRound) 100 else 0
        }
    )

    // Infinite loop
    val transitionInfinite = rememberInfiniteTransition()
    val infiniteColorLoop by transitionInfinite.animateColor(
        initialValue = Color.DarkGray,
        targetValue = Color.Magenta,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
        content = {
            Column(Modifier
                .width(200.dp),
                content = {

                    // Shape
                    Button(
                        onClick = {
                            isRound = !isRound
                        },
                        content = {
                            Text(text = "Toggle")
                        }
                    )
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(borderRadius))
                            .background(Color.Red)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(borderRadius2))
                            .background(Color.Blue)
                    )

                    // Color
                    Spacer(modifier = Modifier.height(20.dp))

                    Slider(
                        value = sliderPosition.value,
                        onValueChange = { sliderPosition.value = it },
                        valueRange = 0f..1f,
                        colors = SliderDefaults.colors(
                            thumbColor = sliderColor,
                            activeTrackColor = sliderColor
                        )
                    )

                    Box(
                        Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(10))
                            .background(color)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Box(
                        Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(20))
                            .background(infiniteColorLoop)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Animated content animation
                    AnimatedContent(
                        targetState = isRound, // off course something else
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),

                        content = { isRound ->
                            if (isRound) {
                                Box(modifier = Modifier
                                    .size(100 .dp)
                                    .clip(RoundedCornerShape(40))
                                    .background(Color.Yellow)
                                )
                            } else {
                                Box(modifier = Modifier
                                    .size(10 .dp)
                                    .clip(RoundedCornerShape(40))
                                    .background(Color.Black)
                                )
                            }
                        },
                        transitionSpec = {
                            fadeIn() with fadeOut()
                        }
                    )
                }
            )
        }
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewHomeViewRoot() {
    val homeViewModel: HomeViewModel = ViewModelFactory.getHomeViewModel()
    val navController = rememberNavController()
    HomeViewRoot(navController, homeViewModel)
}