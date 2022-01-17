package com.example.hoichoihome

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.example.hoichoihome.data.api.ApiHelperImpl
import com.example.hoichoihome.data.api.RetrofitBuilder
import com.example.hoichoihome.data.model.HomePageResponse
import com.example.hoichoihome.data.model.Module
import com.example.hoichoihome.ui.main.intent.MainIntent
import com.example.hoichoihome.ui.main.viewmodel.MainViewModel
import com.example.hoichoihome.ui.main.viewstate.ViewState
import com.example.hoichoihome.ui.theme.Black
import com.example.hoichoihome.ui.theme.HoichoiHomeTheme
import com.example.hoichoihome.ui.theme.MyGray
import com.example.hoichoihome.utils.ViewModelFactory
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

private lateinit var mainViewModel: MainViewModel

private val isLoaded = MutableLiveData<Boolean>()
private lateinit var data: HomePageResponse

private val topSelectedLive = MutableLiveData<Int>()
private val bottomSelectedLive = MutableLiveData<Int>()

class MainActivity : AppCompatActivity() {


    @ExperimentalCoilApi
    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
        lifecycleScope.launch {
            mainViewModel.intent.send(MainIntent.FetchHomeData)
            mainViewModel.state.collect {
                when (it) {
                    is ViewState.HomePageResponses -> {
                        data = it.data
                        isLoaded.postValue(true)
                        topSelectedLive.postValue(1)
                    }
                    else -> {
                        isLoaded.postValue(false)
                    }
                }
            }
        }
        setContent {
            HoichoiHomeTheme {

                Surface(
                    color = Black, modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    HomeScreen()
                }
            }
        }
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProviders.of(
            this, ViewModelFactory(
                ApiHelperImpl(
                    RetrofitBuilder.apiService
                )
            )
        ).get(MainViewModel::class.java)
    }


}

@ExperimentalPagerApi
@ExperimentalCoilApi
@Composable
fun HomeScreen() {
    val loaded = isLoaded.observeAsState().value
    val topSelected = topSelectedLive.observeAsState(1).value
    val bottomSelected = bottomSelectedLive.observeAsState(initial = 1).value
    if (loaded == true) {
        Box {
            LazyColumn {
                itemsIndexed(items = data.modules ?: arrayListOf()) { index, column ->
                    if (index == 0) {
                        TopImagePager(data = column)
                    } else {
                        Spacer(modifier = Modifier.padding(10.dp))
                        column.title?.let {
                            Text(
                                text = it,
                                modifier = Modifier.padding(
                                    start = 15.dp,
                                    top = 10.dp,
                                    bottom = 10.dp
                                ),
                                style = TextStyle(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            )
                        }
                        LazyRow {
                            itemsIndexed(column.contentData ?: arrayListOf()) { _, item ->
                                Column {
                                    item.gist?.posterImageUrl?.let {
                                        CoilImage(
                                            url = it.toString(),
                                            width = 120,
                                            height = 160
                                        )
                                    }
                                    Text(
                                        text = item.gist?.title ?: "",
                                        style = TextStyle(
                                            color = Color.White,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Light
                                        ),
                                        modifier = Modifier.width(100.dp),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.padding(10.dp))
            TopNavBar(topSelected)
            Box(
                modifier = Modifier.align(
                    Alignment.BottomCenter
                )
            ) {
                BottomNavBar(bottomSelected)
            }


        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }


}

@ExperimentalCoilApi
@Composable
fun BottomNavBar(bottomSelected: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
            .background(color = MyGray, shape = RoundedCornerShape(25.dp))
    ) {
        Row {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp, end = 10.dp, top = 3.dp, bottom = 3.dp)
            ) {
                Column {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            bottomSelectedLive.postValue(1)
                        }) {
                        Box(modifier = Modifier.align(Alignment.Center)) {
                            CoilImageDrawable(
                                drawable = R.drawable.ic_baseline_home_24,
                                width = 40,
                                height = 40,
                                tint = if (bottomSelected == 1) Color.Red else Color.White
                            )
                        }

                    }
                    Text(
                        text = "Home",
                        Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = if (bottomSelected == 1) Color.Red else Color.White,
                        fontSize = 10.sp
                    )
                }

            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp, end = 10.dp, top = 3.dp, bottom = 3.dp)
            ) {
                Column {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            bottomSelectedLive.postValue(2)
                        }) {
                        Box(modifier = Modifier.align(Alignment.Center)) {
                            CoilImageDrawable(
                                drawable = R.drawable.ic_baseline_search_24,
                                width = 40,
                                height = 40,
                                tint = if (bottomSelected == 2) Color.Red else Color.White
                            )
                        }

                    }

                    Text(
                        text = "Discover",
                        Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = if (bottomSelected == 2) Color.Red else Color.White,
                        fontSize = 10.sp
                    )
                }

            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp, end = 10.dp, top = 3.dp, bottom = 3.dp)
            ) {
                Column {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            bottomSelectedLive.postValue(3)
                        }) {
                        Box(modifier = Modifier.align(Alignment.Center)) {
                            CoilImageDrawable(
                                drawable = R.drawable.ic_baseline_cloud_download_24,
                                width = 40,
                                height = 40,
                                tint = if (bottomSelected == 3) Color.Red else Color.White
                            )
                        }

                    }

                    Text(
                        text = "Downloads",
                        Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = if (bottomSelected == 3) Color.Red else Color.White,
                        fontSize = 10.sp
                    )
                }

            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp, end = 10.dp, top = 3.dp, bottom = 3.dp)
            ) {
                Column {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            bottomSelectedLive.postValue(4)
                        }) {
                        Box(modifier = Modifier.align(Alignment.Center)) {
                            CoilImageDrawable(
                                drawable = R.drawable.ic_baseline_notifications_24,
                                width = 40,
                                height = 40,
                                tint = if (bottomSelected == 4) Color.Red else Color.White
                            )
                        }

                    }

                    Text(
                        text = "Upcoming",
                        Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = if (bottomSelected == 4) Color.Red else Color.White,
                        fontSize = 10.sp
                    )
                }

            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp, end = 10.dp, top = 3.dp, bottom = 3.dp)
            ) {
                Column {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            bottomSelectedLive.postValue(5)
                        }) {
                        Box(modifier = Modifier.align(Alignment.Center)) {
                            CoilImageDrawable(
                                drawable = R.drawable.ic_baseline_person_24,
                                width = 40,
                                height = 40,
                                tint = if (bottomSelected == 5) Color.Red else Color.White
                            )
                        }

                    }

                    Text(
                        text = "Account",
                        Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = if (bottomSelected == 5) Color.Red else Color.White,
                        fontSize = 10.sp
                    )
                }

            }
        }
    }
}

@Composable
fun TopNavBar(topSelected: Int) {
    Box {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black,
                            Color.Transparent
                        ),
                        startY = 50f
                    )
                )
                .align(Alignment.TopCenter)

        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            Column(modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp, end = 10.dp)
                .clickable {
                    topSelectedLive.postValue(1)
                }) {
                Text(
                    text = "All",
                    style = TextStyle(color = Color.White, fontSize = 12.sp),
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                if (topSelected == 1) {
                    Divider(color = Color.Red, thickness = 1.dp)
                }
            }
            Column(modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp, end = 10.dp)
                .clickable {
                    topSelectedLive.postValue(2)
                }) {
                Text(
                    text = "Movies",
                    style = TextStyle(color = Color.White, fontSize = 12.sp),
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                if (topSelected == 2) {
                    Divider(color = Color.Red, thickness = 1.dp)
                }

            }
            Column(modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp, end = 10.dp)
                .clickable {
                    topSelectedLive.postValue(3)
                }) {
                Text(
                    text = "Shows",
                    style = TextStyle(color = Color.White, fontSize = 12.sp),
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                if (topSelected == 3) {
                    Divider(color = Color.Red, thickness = 1.dp)
                }
            }
            Column(modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp, end = 10.dp)
                .clickable {
                    topSelectedLive.postValue(4)
                }) {
                Text(
                    text = "Music",
                    style = TextStyle(color = Color.White, fontSize = 12.sp),
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                if (topSelected == 4) {
                    Divider(color = Color.Red, thickness = 1.dp)
                }
            }
            Column(modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp, end = 10.dp)
                .clickable {
                    topSelectedLive.postValue(5)
                }) {
                Text(
                    text = "Live TV",
                    style = TextStyle(color = Color.White, fontSize = 12.sp),
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                if (topSelected == 5) {
                    Divider(color = Color.Red, thickness = 1.dp)
                }
            }
            Spacer(modifier = Modifier.width(30.dp))
        }
    }
}

@ExperimentalPagerApi
@ExperimentalCoilApi
@Composable
fun TopImagePager(data: Module) {
    HorizontalPager(
        count = data.contentData?.size ?: 0,

        ) { page ->
        Row {
            val url = data.contentData?.get(page)?.gist?.posterImageUrl
            CoilImageTop(url = url ?: "", height = 550, data, page)
        }


    }
}

@ExperimentalCoilApi
@Composable
fun CoilImageTop(url: String, height: Int, data: Module, page: Int) {
    val content = data.contentData?.get(page)
    Box(
        modifier = Modifier
            .height(height.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = rememberImagePainter(data = url, builder = {

            }),
            contentDescription = "ImageView",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.padding(top = 10.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black
                        ),
                        startY = 50f
                    )
                )
                .align(Alignment.BottomCenter)

        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 12.dp, bottom = 8.dp
                ),
            contentAlignment = Alignment.BottomStart
        ) {
            Row {
                Column(Modifier.weight(10f)) {
                    Text(
                        text = (content?.gist?.title ?: ""), style = TextStyle(
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    var bottomText = ""
                    if (content?.gist?.contentType?.equals("SERIES") == true) {
                        val seasons = content.seasons?.size
                        var episodes = 0
                        val category = content.gist.primaryCategory?.title
                        for (item in content.seasons!!) {
                            episodes += item.episodes?.size ?: 0
                        }
                        bottomText = "$seasons Seasons. $episodes Episodes. $category"
                    } else {
                        val category =
                            content?.gist?.primaryCategory?.title ?: "Something went wrong"
                        bottomText = category
                    }
                    Text(
                        text = bottomText, style = TextStyle(
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Light
                        )
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.ic_outline_play_arrow_24),
                    contentDescription = "Image",
                    modifier = Modifier
                        .weight(1F)
                        .height(48.dp)
                        .width(48.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_add_24),
                    contentDescription = "Image",
                    modifier = Modifier
                        .weight(1F)
                        .height(48.dp)
                        .width(48.dp)
                )


            }

        }
    }

}

@ExperimentalCoilApi
@Composable
fun CoilImage(url: String, width: Int, height: Int) {
    Box(
        modifier = Modifier
            .height(height.dp)
            .width(width.dp),
        contentAlignment = Alignment.Center
    ) {
        val painter = rememberImagePainter(data = url, builder = {

        })
        Image(
            painter = painter, contentDescription = "ImageView", modifier = Modifier
                .clip(
                    RoundedCornerShape(10.dp)
                )
                .padding(2.dp)
                .fillMaxSize(), contentScale = ContentScale.Crop
        )
        if (painter.state is ImagePainter.State.Loading) {
            CircularProgressIndicator()
        }
    }
}

@ExperimentalCoilApi
@Composable
fun CoilImageDrawable(drawable: Int, width: Int, height: Int, tint: Color) {
    Box(
        modifier = Modifier
            .height(height.dp)
            .width(width.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painterResource(drawable),
            "content description",
            modifier = Modifier
                .fillMaxWidth()
                .align(
                    Alignment.Center
                ),
            colorFilter = ColorFilter.tint(tint)
        )
    }
}

@ExperimentalPagerApi
@ExperimentalCoilApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HoichoiHomeTheme {
        HomeScreen()
    }
}
