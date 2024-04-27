package com.minitac

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.minitac.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binding: ActivityGameBinding

    private var gameModel : GameModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btn0.setOnClickListener(this)
        binding.btn1.setOnClickListener(this)
        binding.btn2.setOnClickListener(this)
        binding.btn3.setOnClickListener(this)
        binding.btn4.setOnClickListener(this)
        binding.btn5.setOnClickListener(this)
        binding.btn6.setOnClickListener(this)
        binding.btn7.setOnClickListener(this)
        binding.btn8.setOnClickListener(this)

        binding.startGameBtn.setOnClickListener {
            startGame()
        }

    }
    fun startGame(){

    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}