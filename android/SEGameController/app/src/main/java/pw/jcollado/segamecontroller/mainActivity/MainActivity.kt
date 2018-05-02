package pw.jcollado.segamecontroller.mainActivity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.property_card.view.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton
import org.json.JSONObject
import pw.jcollado.segamecontroller.R
import pw.jcollado.segamecontroller.connections.AsyncResponse
import pw.jcollado.segamecontroller.connections.ServerConnectionThread
import pw.jcollado.segamecontroller.listPropertiesActivity.ListPropertiesActivity
import org.json.JSONArray
import pw.jcollado.segamecontroller.JoinActivity.JoinActivity
import pw.jcollado.segamecontroller.model.*
import pw.jcollado.segamecontroller.utils.getPropertyImageURL


class MainActivity : App() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setActionBar()
        rollButton.onClick { onRoll() }
        buyButton.onClick { onBuy() }
        finishTurnButton.onClick { onFinish() }
        payButton.onClick { onPay() }
        boostButton.onClick { onBoost() }
        bankruptButton.onClick { onBankrupt() }


        connectToNewPort()
    }

    private fun onFinish() {
        setMessageToServer(Request(preferences.playerID, "done","0").toJSONString())
    }


    fun connectToNewPort(){
        val joinGameRequest = Request(preferences.playerID,"connect","0")
        val jsonStringRequest = joinGameRequest.toJSONString()
        startServer(this)
        setMessageToServer(jsonStringRequest)

    }

    fun onRoll() {
        setMessageToServer(Request(preferences.playerID, "roll","0").toJSONString())
    }
    fun onPay() {
        setMessageToServer(Request(preferences.playerID, "pay","0").toJSONString())
    }
    fun onBuy() {
        setMessageToServer(Request(preferences.playerID, "buy","0").toJSONString())
    }
    fun onBoost(){
        setMessageToServer(Request(preferences.playerID, "boost","0").toJSONString())

    }
    fun onBankrupt(){
        alert("Are you sure that you want to leave the game?","Bankrupt" ) {
            yesButton {
                setMessageToServer(Request(preferences.playerID, "bankrupt","0").toJSONString())
                killGameThread()
                preferences.port = 8080
                startActivity(Intent(applicationContext, JoinActivity::class.java))

            }
            noButton {}
        }.show()


    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.propertiesMenu -> {
                startActivity(Intent(this, ListPropertiesActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



    override fun updateGameState(){
        runOnUiThread {
            val intColor = Player.colour
            val hexColor = "#" + Integer.toHexString(intColor).substring(2)
            val color = Color.parseColor(hexColor)

            supportActionBar?.title = Player.character
            supportActionBar?.setBackgroundDrawable(ColorDrawable(color))
            balanceTX.text = Player.balance.toString()
            positionTX.text = Player.position

            Picasso.get().load(getPropertyImageURL(Player.position)).placeholder(R.drawable.joinlogo).into(positionImage)

        }
    }





    private fun setActionBar() {
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setHomeButtonEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

    }
}

