package com.example.snakegameapplication.common

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SnakeViewModel: ViewModel()
{
    private val _state = MutableStateFlow(SnakeState())
    val state = _state.asStateFlow()

    fun onEvent(event : SnakeEvent){
        when(event){

            SnakeEvent.StartGame-> {
                _state.update { it.copy(gameState = GameState.STARTED) }
                viewModelScope.launch {
                    while (state.value.gameState == GameState.STARTED) {
                        val delayMillis = when (state.value.snake.size) {
                            in 1..5 -> 120L
                            in 6..10 -> 110L
                            else -> 100L
                        }
                        delay(delayMillis)
                        _state.value = updateGame(state.value)
                    }
                }
            }

            SnakeEvent.PauseGame-> { _state.update { it.copy(gameState = GameState.PAUSED) } }

            SnakeEvent.ResetGame-> { _state.value = SnakeState() }

            is SnakeEvent.UpdateDirection-> { updateDirection(event.offset, event.canvasWidth) }

        }
    }


    private fun updateDirection(offset: Offset , canvasWidth: Int) {
        if (!state.value.isGameOver) {
            val cellSize = canvasWidth / state.value.xAxisGridSize
            val tapX = (offset.x / cellSize).toInt()
            val tapY = (offset.y / cellSize).toInt()
            val head = state.value.snake.first()

            _state.update {
                it.copy(
                    direction = when (state.value.direction) {
                        Direction.UP, Direction.DOWN -> {
                            if (tapX < head.x) Direction.LEFT else Direction.RIGHT
                        }

                        Direction.LEFT, Direction.RIGHT -> {
                            if (tapY < head.y) Direction.UP else Direction.DOWN
                        }
                    }
                       )
            }
        }
    }


    private fun updateGame(currentGame : SnakeState): SnakeState
    {
        if (currentGame.isGameOver)
        {
            return currentGame
        }
        val head = currentGame.snake.first()
        val xAxisGirdSize = currentGame.xAxisGridSize
        val yAxisGirdSize = currentGame.yAxisGridSize

//        when snake is update
        val newHead = when (currentGame.direction)
        {
            Direction.UP -> Coordinate(x = head.x, y = (head.y - 1))
            Direction.DOWN -> Coordinate(x = head.x, y = (head.y + 1))
            Direction.LEFT -> Coordinate(x = head.x - 1, y = (head.y))
            Direction.RIGHT -> Coordinate(x = head.x + 1, y = (head.y))
        }
//        Check if the snake collides with itself or goes out of bounds
        if (
            currentGame.snake.contains(newHead) ||
            !isWithinBounds(newHead, xAxisGirdSize, yAxisGirdSize)
        ) {
            return currentGame.copy(isGameOver = true)
        }

        //Check if the snake eats the food
        var newSnake = mutableListOf(newHead) + currentGame.snake
        val newFood = if (newHead == currentGame.food) SnakeState.generateRandomFoodCoordinate()
        else currentGame.food

        //Update snake length
        if (newHead != currentGame.food) {
            newSnake = newSnake.toMutableList()
            newSnake.removeAt(newSnake.size - 1)
        }

        return currentGame.copy(snake = newSnake, food = newFood)
    }

    private fun isWithinBounds(
            coordinate: Coordinate,
            xAxisGridSize: Int,
            yAxisGridSize: Int
                              ): Boolean {
        return coordinate.x in 1 until xAxisGridSize - 1
                && coordinate.y in 1 until yAxisGridSize - 1
    }
}