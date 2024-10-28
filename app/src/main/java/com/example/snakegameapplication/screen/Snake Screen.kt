package com.example.snakegameapplication.screen

import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.snakegameapplication.common.Coordinate
import com.example.snakegameapplication.common.Direction
import com.example.snakegameapplication.common.GameState
import com.example.snakegameapplication.R
import com.example.snakegameapplication.common.SnakeEvent
import com.example.snakegameapplication.common.SnakeState
import com.example.snakegameapplication.ui.theme.Citrine
import com.example.snakegameapplication.ui.theme.Custard
import com.example.snakegameapplication.ui.theme.RoyalBlue

@SuppressLint("Range")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SnakeScreen(
        state: SnakeState,
        onEvent: (SnakeEvent) -> Unit
               )
{


    val foodImageBitmap = ImageBitmap.imageResource(id = R.drawable.img_apple)

//   Here we use image according to snake head at which direction
    val snakeHeadImageBitmap = when (state.direction)
    {
        Direction.RIGHT->ImageBitmap.imageResource(id = R.drawable.img_snake_head)
        Direction.LEFT->ImageBitmap.imageResource(id = R.drawable.img_snake_head2)
        Direction.UP->ImageBitmap.imageResource(id = R.drawable.img_snake_head3)
        Direction.DOWN->ImageBitmap.imageResource(id = R.drawable.img_snake_head4)
    }

    val context = LocalContext.current
    val foodSoundMP = remember { MediaPlayer.create(context, R.raw.food) }
    val gameOverSoundMP = remember { MediaPlayer.create(context, R.raw.gameover) }

    LaunchedEffect(key1 = state.snake.size) {
        if (state.snake.size != 1)
        {
            foodSoundMP?.start()
        }
    }

    LaunchedEffect(key1 = state.isGameOver) {
        if (state.isGameOver)
        {
            gameOverSoundMP?.start()
        }
    }

    TopAppBar(title = { Text("Score is ${state.snake.size-1}",
                             style = MaterialTheme.typography.bodyLarge,
                             fontWeight = FontWeight.Bold,
                             fontSize = 20.sp,
                             fontStyle = FontStyle.Italic,
                             modifier = Modifier.padding(16.dp)
                            )})
    Box(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
       , contentAlignment = Alignment.Center

       )
    {
        Column(
            modifier = Modifier.fillMaxSize(),
               verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.Start
              )
        {
            Canvas(modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(ratio = 2 / 3f)
                    .pointerInput(state.gameState) {
                        if (state.gameState != GameState.STARTED)
                        {
                            return@pointerInput
                        }
                        detectTapGestures { offset ->
                            onEvent(SnakeEvent.UpdateDirection(offset, size.width))
                        }
                    })
            {
                val cellSize = size.width / 20
                drawGameBoard(cellSize = cellSize ,
                              cellColor = Custard ,
                              borderCellColor = RoyalBlue ,
                              gridWidth = state.xAxisGridSize ,
                              gridHeight = state.yAxisGridSize)

                drawFood(foodImage = foodImageBitmap ,
                         cellSize = cellSize.toInt() ,
                         coordinate = state.food)

                drawSnake(snakeHeadImage = snakeHeadImageBitmap ,
                          snake = state.snake ,
                          cellSize = cellSize)
            }

            Row(modifier = Modifier
                    .padding(11.dp)
                    .fillMaxWidth()
                    .fillMaxSize(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.End
               )
            {
                Button(modifier = Modifier.weight(1f),
                       onClick = { onEvent(SnakeEvent.ResetGame) },
                       enabled = state.gameState == GameState.PAUSED || state.isGameOver) {
                    Text(text = if (state.isGameOver) "Reset" else "New Game")
                }

//                Spacer(modifier = Modifier.width(10.dp))

                Button(modifier = Modifier.weight(1f) , onClick = {
                    when (state.gameState)
                    {
                        GameState.IDLE, GameState.PAUSED->onEvent(SnakeEvent.StartGame)
                        GameState.STARTED->onEvent(SnakeEvent.PauseGame)
                    }
                    } , enabled = ! state.isGameOver)
                {
                    Text(text = when (state.gameState)
                    {
                        GameState.IDLE->"Start"
                        GameState.STARTED->"Pause"
                        GameState.PAUSED->"Resume"
                    }
                        )
                }
            }
        }
        AnimatedVisibility(visible = state.isGameOver) {
            Text(modifier = Modifier.padding(16.dp) ,
                 text = "Game Over" ,
                 style = MaterialTheme.typography.displayMedium)
        }
    }
}


// This will be our gaming board
private fun DrawScope.drawGameBoard(
        cellSize : Float,
        cellColor : Color,
        borderCellColor : Color,
        gridWidth : Int,
        gridHeight : Int
                                   )
{
    for (i in 0 until gridWidth){
        for (j in 0 until gridHeight){

            val isBorderCell = i == 0 || j == 0 || i == gridWidth - 1 || j == gridHeight -1
            drawRect(
                color = if(isBorderCell) borderCellColor
                        else if ((i + j) % 2 == 0) cellColor
                        else cellColor.copy(alpha = 0.5f),
                topLeft = Offset(x = i * cellSize, y = j * cellSize),
                size =  Size(cellSize,cellSize))
        }
    }


}


// This will be the food for snake in game
private fun DrawScope.drawFood(
        foodImage : ImageBitmap,
        cellSize : Int,
        coordinate : Coordinate
                              )
{
    drawImage(
        image = foodImage ,
        dstOffset = IntOffset(
            x = (coordinate.x * cellSize),
            y = (coordinate.y * cellSize)
                             ),
        dstSize = IntSize(cellSize, cellSize)
             )
}


// This will be our snake
private fun DrawScope.drawSnake(
        snakeHeadImage: ImageBitmap,
        cellSize: Float,
        snake: List<Coordinate>
                               )
{
    val cellSizeInt = cellSize.toInt()
    snake.forEachIndexed {
        index : Int , coordinate ->
        val radius = if (index == snake.lastIndex) cellSize / 2.5f else cellSize / 2
        if (index == 0) {
            drawImage(
                image = snakeHeadImage,
                dstOffset = IntOffset(
                    x = (coordinate.x * cellSizeInt),
                    y = (coordinate.y * cellSizeInt)
                                     ),
                dstSize = IntSize(cellSizeInt, cellSizeInt)
                     )
        } else {
            drawCircle(
                color = Citrine,
                center = Offset(
                    x = (coordinate.x * cellSize) + radius,
                    y = (coordinate.y * cellSize) + radius
                               ),
                radius = radius
                      )
        }
    }
}