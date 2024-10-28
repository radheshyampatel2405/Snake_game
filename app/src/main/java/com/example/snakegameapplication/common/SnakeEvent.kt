package com.example.snakegameapplication.common

import androidx.compose.ui.geometry.Offset

sealed class SnakeEvent
{
    data object StartGame : SnakeEvent()
    data object PauseGame : SnakeEvent()
    data object ResetGame : SnakeEvent()
    data class UpdateDirection(val offset : Offset ,val canvasWidth : Int ) : SnakeEvent()

}