package com.compose.expandablelistapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.ContentAlpha
import androidx.wear.compose.material3.ripple
import com.compose.expandablelistapp.components.Objects
import com.compose.expandablelistapp.components.list
import com.compose.expandablelistapp.ui.theme.ExpandableListAppTheme
import com.compose.expandablelistapp.utils.BoxWithLayout

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExpandableListAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GroupedItems()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupedItems() {
    val collapseState = remember(list) { list.map { false }.toMutableStateList() }
    val groupedList = list.groupBy { itemA-> itemA.listOf.groupBy { itemB-> itemB.group } }
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        item { /*Add any other compose as a header*/ }
        groupedList.values.forEachIndexed { indexA, listOfListOfObjects ->
            listOfListOfObjects.forEachIndexed { indexB, listOfObjects->
                val collapsed = collapseState[indexA]
                item { Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp))
                }
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateContentSize(
                                animationSpec = tween(
                                    durationMillis = 3000,
                                    easing = LinearOutSlowInEasing
                                )
                            ),
                        shape = RoundedCornerShape(1.dp),
                        onClick = {
                            collapseState[indexA] = !collapsed
                        }
                    ) {
                        Row(
                            modifier = Modifier
                                .clickable {
                                    collapseState[indexA] = !collapsed
                                }
                                .shadow(
                                    elevation = 1.dp,
                                    ambientColor = MaterialTheme.colorScheme.primary.copy(1f)
                                )
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                modifier = Modifier
                                    .width(100.dp)
                                    .padding(10.dp),
                                text = listOfObjects.listOf[0].group,
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                ),
                                textAlign = TextAlign.Start,
                                maxLines = 1
                            )
                            val rotationState by animateFloatAsState(
                                targetValue = if (expanded) 180f else 0f, label = ""
                            )
                            IconButton(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .size(width = 20.dp, height = 20.dp)
                                    .alpha(ContentAlpha.medium)
                                    .rotate(rotationState),
                                onClick = {
                                    collapseState[indexA] = !collapsed
                                }) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowUp,
                                    contentDescription = "Drop-Down Arrow"
                                )
                            }
                        }
                    }
                }
                if (!collapsed) {
                    items(listOfObjects.listOf.size) {
                        RowItem(obj = listOfObjects.listOf[it])
                    }
                }
            }
        }
    }
}

@Composable
fun RowItem(obj: Objects) {
    BoxWithLayout {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .shadow(elevation = 0.5.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(bounded = true, color = MaterialTheme.colorScheme.primary),
                    onClick = {}
                )
                .weight(10f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .fillMaxHeight()
                    .padding(8.dp)
            ) {
                Text(
                    modifier = Modifier
                        .weight(4f)
                        .width(100.dp),
                    text = obj.name,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    ),
                    maxLines = 1
                )
                Row(
                    modifier = Modifier.weight(6f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        modifier = Modifier.width(30.dp),
                        text = obj.rank,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        ),
                        maxLines = 1
                    )
                    Text(
                        modifier = Modifier.width(30.dp),
                        text = obj.points,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        ),
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    Surface {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            GroupedItems()
        }
    }
}
