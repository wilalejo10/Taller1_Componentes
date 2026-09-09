package com.example.taller1_componentes.game

import kotlin.random.Random

class GameEngine {

    var targetDirection: Int = 0
        private set

    fun startGame() {
        targetDirection = Random.nextInt(0, 360)
    }
}