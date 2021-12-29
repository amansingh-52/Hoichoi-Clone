package com.example.hoichoihome

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import  com.example.hoichoihome.ui.theme.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.example.hoichoihome.data.model.HomePageResponse
import com.example.hoichoihome.data.model.Module
import com.example.hoichoihome.ui.theme.HoichoiHomeTheme
import com.example.hoichoihome.viewModel.HomeViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager


class MainActivity : ComponentActivity() {


    private val viewModel: HomeViewModel by viewModels()

    @ExperimentalCoilApi
    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HoichoiHomeTheme {
                Surface(
                    color = Black, modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    HomeScreen(viewModel = viewModel)
                }
            }
        }
        viewModel.getDataForHome()
    }
}


@ExperimentalPagerApi
@ExperimentalCoilApi
@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val data: HomePageResponse? = viewModel.getHomeData.observeAsState().value?.data
    LazyColumn {
        itemsIndexed(items = data?.modules ?: arrayListOf()) { index, column ->
            if (index == 0) {
                TopImagePager(data = column)
            } else {
                Spacer(modifier = Modifier.padding(10.dp))
                column.title?.let {
                    Text(
                        text = it,
                        modifier = Modifier.padding(start = 15.dp, top = 10.dp, bottom = 10.dp),
                        style = TextStyle(
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    )
                }
                LazyRow {
                    itemsIndexed(column.contentData ?: arrayListOf()) { index, item ->
                        Column {
                            item.gist?.posterImageUrl?.let {
                                CoilImage(url = it.toString(), width = 120, height = 160)
                            }
                            Text(text = item.gist?.title ?: "", style = TextStyle(color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Light), modifier = Modifier.width(100.dp),  maxLines = 1,
                                overflow = TextOverflow.Ellipsis)
                        }

                    }
                }
            }
        }
    }
    Spacer(modifier = Modifier.padding(10.dp))
}

@ExperimentalPagerApi
@ExperimentalCoilApi
@Composable
fun TopImagePager(data: Module) {
    HorizontalPager(
        count = data.contentData?.size ?: 0,

        ) { page ->
        Row {
            val url = data.contentData?.get(page)?.gist?.imageGist?._3x4
            CoilImageTop(url = url ?: "", height = 500, data, page)
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
            contentScale = ContentScale.Crop
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
                        text = content?.gist?.title ?: "", style = TextStyle(
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
                .padding(2.dp).fillMaxSize(), contentScale = ContentScale.Crop
        )
        if (painter.state is ImagePainter.State.Loading) {
            CircularProgressIndicator()
        }
    }
}

@ExperimentalCoilApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HoichoiHomeTheme {

    }
}
