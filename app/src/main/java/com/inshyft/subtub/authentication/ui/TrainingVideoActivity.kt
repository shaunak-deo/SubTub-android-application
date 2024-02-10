package com.inshyft.subtub.authentication.ui
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.youtube.player.YouTubeStandalonePlayer
import com.inshyft.subtub.R
import kotlinx.android.synthetic.main.activity_training_video.*
import java.lang.IllegalArgumentException

//import android.support.v4.app.AppCompatActivity
class TrainingVideoActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training_video)

        btnPlayList.setOnClickListener(this)

        //  btnPlayVideo.setOnClickListener(object : View.OnClickListener{
        //    override fun onClick(v: View?) {
        //      TODO("Not yet implemented")
        //}
        //})

        //  btnPlayVideo.setOnClickListener(View.OnClickListener { v ->
        //       TODO("Not yet implemented")
        //   })

        //       val listener = View.OnClickListener { v ->
        //        TODO("Not yet implemented")
        //      }
        //  btnPLayVideo.setOnClickListener(listener)
        //btnPlayList.setOnClickListener(listener)


    }

    override fun onClick(view: View) {
        val intent = when (view.id) {
            R.id.btnPlayList -> YouTubeStandalonePlayer.createPlaylistIntent(
                this, getString(R.string.GOOGLE_API_KEY), YOUTUBE_PLAYLIST, 0,0, true,true)

            else -> throw IllegalArgumentException("Undefined button clicked")

        }
        startActivity(intent)
    }
}