package com.minitac

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import java.util.Locale
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.minitac.databinding.ActivityGameBinding
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import java.util.concurrent.TimeUnit

class GameActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binding: ActivityGameBinding

    private var gameModel : GameModel? = null
    private var currentTime: Long = 0

    // Define sharedPreferences here
    private lateinit var sharedPreferences: SharedPreferences

    lateinit var recordTimeTextView: TextView
    lateinit var currentTimeTextView: TextView

    private var timer: CountDownTimer? = null

    private var millisUntilFinished: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize sharedPreferences, recordTimeTextView, and currentTimeTextView here
        sharedPreferences = getSharedPreferences("MiniTac", Context.MODE_PRIVATE)
        recordTimeTextView = findViewById(R.id.record_time_text)
        currentTimeTextView = findViewById(R.id.current_time_text)

        // Check if it's the first time the app is launched
        if (!sharedPreferences.contains("recordTime")) {
            // If it is, save the current time as the record time
            val editor = sharedPreferences.edit()
            editor.putLong("recordTime", currentTime)
            editor.apply()
        }

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

        GameData.gameModel.observe(this){
            gameModel = it
            setUI()
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }

        updateRecordTime()
    }

    fun setUI(){
        gameModel?.apply {
            binding.btn0.text = filledPos[0]
            binding.btn1.text = filledPos[1]
            binding.btn2.text = filledPos[2]
            binding.btn3.text = filledPos[3]
            binding.btn4.text = filledPos[4]
            binding.btn5.text = filledPos[5]
            binding.btn6.text = filledPos[6]
            binding.btn7.text = filledPos[7]
            binding.btn8.text = filledPos[8]

            binding.startGameBtn.visibility = View.VISIBLE

            binding.gameStatusText.text =
                when(gameStatus){
                    GameStatus.CREATED -> {
                        binding.startGameBtn.visibility = View.INVISIBLE
                        "Game ID :"+ gameId
                    }
                    GameStatus.JOINED ->{
                        "Click On Start Game"
                    }
                    GameStatus.INPROGRESS ->{
                        binding.startGameBtn.visibility = View.INVISIBLE
                        " Turn Of  " + currentPlayer
                    }
                    GameStatus.FINISHED ->{
                        if(winner.isNotEmpty()) winner + " Won"
                        else "DRAW"
                    }
                }
        }
    }

    fun startGame(){
        gameModel?.apply {
            // Start the timer when the game starts
            timer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    this@GameActivity.millisUntilFinished = millisUntilFinished
                    val elapsedSeconds = (Long.MAX_VALUE - millisUntilFinished) / 1000
                    val hours = TimeUnit.SECONDS.toHours(elapsedSeconds)
                    val minutes = TimeUnit.SECONDS.toMinutes(elapsedSeconds) % 60
                    val seconds = elapsedSeconds % 60
                    currentTimeTextView.text = String.format(Locale.getDefault(), "Current Time: %02d : %02d : %02d", hours, minutes, seconds)
                    // Save the current time to SharedPreferences only if it's less than the record time
                    val recordTime = sharedPreferences.getLong("recordTime", Long.MAX_VALUE)
                    if (elapsedSeconds < recordTime) {
                        val editor = sharedPreferences.edit()
                        editor.putLong("recordTime", elapsedSeconds)
                        editor.apply()
                    }
                }

                override fun onFinish() {
                    // This should never be called because we're counting down from Long.MAX_VALUE
                }
            }.start()

            updateGameData(
                GameModel(
                    gameId = gameId,
                    gameStatus = GameStatus.INPROGRESS,
                    currentPlayer = "X" // User always starts the game
                )
            )
        }
    }

    fun updateGameData(model : GameModel){
        GameData.saveGameModel(model)
    }

    fun checkForWinner(){
        val winningPos = arrayOf(
            intArrayOf(0,1,2),
            intArrayOf(3,4,5),
            intArrayOf(6,7,8),
            intArrayOf(0,3,6),
            intArrayOf(1,4,7),
            intArrayOf(2,5,8),
            intArrayOf(0,4,8),
            intArrayOf(2,4,6),
        )

        gameModel?.apply {
            for ( i in winningPos){
                if(
                    filledPos[i[0]] == filledPos[i[1]] &&
                    filledPos[i[1]]== filledPos[i[2]] &&
                    filledPos[i[0]].isNotEmpty()
                ){
                    gameStatus = GameStatus.FINISHED
                    winner = filledPos[i[0]]
                    timer?.cancel()
                    // If the user has won the game, save the current time to SharedPreferences only if it's less than the record time
                    if (winner == "X") {
                        val elapsedSeconds = (Long.MAX_VALUE - this@GameActivity.millisUntilFinished) / 1000
                        val recordTime = sharedPreferences.getLong("recordTime", Long.MAX_VALUE)
                        if (elapsedSeconds < recordTime) {
                            val editor = sharedPreferences.edit()
                            editor.putLong("recordTime", elapsedSeconds)
                            editor.apply()
                        }
                    }
                }
            }

            if( filledPos.none(){ it.isEmpty() }){
                gameStatus = GameStatus.FINISHED
                // Stop the timer when the game finishes
                timer?.cancel()
            }
            updateGameData(this)
        }
    }

    override fun onClick(v: View?) {
        gameModel?.apply {
            if(gameStatus!= GameStatus.INPROGRESS){
                Toast.makeText(applicationContext,"Game Not Started",Toast.LENGTH_SHORT).show()
                return
            }
            //game is in progress
            val clickedPos =(v?.tag  as String).toInt()
            if(filledPos[clickedPos].isEmpty() && currentPlayer == "X"){
                filledPos[clickedPos] = currentPlayer
                currentPlayer = "O"
                checkForWinner()
                updateGameData(this)
                // Delay the app's move by 0.5 seconds
                Handler(Looper.getMainLooper()).postDelayed({
                    appMove()
                }, 500) // Delay of 0.5 seconds
            }
        }
    }

    private fun appMove() {
        gameModel?.apply {
            if(gameStatus != GameStatus.INPROGRESS) {
                return
            }

            // Check if the app can win in the next move
            val appWinningPos = getAppWinningPos()
            if(appWinningPos != -1) {
                // If the app can win, prioritize it over blocking the user's winning chance
                filledPos[appWinningPos] = currentPlayer
            } else {
                // If the app can't win, check if the user is about to win in the next move
                val userWinningPos = getUserWinningPos()
                if(userWinningPos != -1) {
                    // If the user is about to win, block the user by placing the app's move in that position
                    filledPos[userWinningPos] = currentPlayer
                } else {
                    // If the user is not about to win, select the first empty position for the app's move
                    val appPos = filledPos.indexOfFirst { it.isEmpty() }
                    if(appPos != -1) {
                        filledPos[appPos] = currentPlayer
                    }
                }
            }

            currentPlayer = "X"
            checkForWinner()
            updateGameData(this)
        }
    }

    private fun getAppWinningPos(): Int {
        val winningPos = arrayOf(
            intArrayOf(0,1,2),
            intArrayOf(3,4,5),
            intArrayOf(6,7,8),
            intArrayOf(0,3,6),
            intArrayOf(1,4,7),
            intArrayOf(2,5,8),
            intArrayOf(0,4,8),
            intArrayOf(2,4,6),
        )

        gameModel?.filledPos?.let { filledPos ->
            for (i in winningPos) {
                if (filledPos[i[0]] == "O" && filledPos[i[1]] == "O" && filledPos[i[2]].isEmpty()) {
                    return i[2]
                }
                if (filledPos[i[0]] == "O" && filledPos[i[2]] == "O" && filledPos[i[1]].isEmpty()) {
                    return i[1]
                }
                if (filledPos[i[1]] == "O" && filledPos[i[2]] == "O" && filledPos[i[0]].isEmpty()) {
                    return i[0]
                }
            }
        }

        return -1
    }

    private fun getUserWinningPos(): Int {
        val winningPos = arrayOf(
            intArrayOf(0,1,2),
            intArrayOf(3,4,5),
            intArrayOf(6,7,8),
            intArrayOf(0,3,6),
            intArrayOf(1,4,7),
            intArrayOf(2,5,8),
            intArrayOf(0,4,8),
            intArrayOf(2,4,6),
        )

        gameModel?.filledPos?.let { filledPos ->
            for (i in winningPos) {
                if (filledPos[i[0]] == "X" && filledPos[i[1]] == "X" && filledPos[i[2]].isEmpty()) {
                    return i[2]
                }
                if (filledPos[i[0]] == "X" && filledPos[i[2]] == "X" && filledPos[i[1]].isEmpty()) {
                    return i[1]
                }
                if (filledPos[i[1]] == "X" && filledPos[i[2]] == "X" && filledPos[i[0]].isEmpty()) {
                    return i[0]
                }
            }
        }

        return -1
    }

    private fun updateRecordTime() {
        val recordTime = sharedPreferences.getLong("recordTime", Long.MAX_VALUE)
        val hours = TimeUnit.SECONDS.toHours(recordTime)
        val minutes = TimeUnit.SECONDS.toMinutes(recordTime) % 60
        val seconds = recordTime % 60
        recordTimeTextView.text = String.format(Locale.getDefault(), "Record Time: %02d : %02d : %02d", hours, minutes, seconds)
    }
}