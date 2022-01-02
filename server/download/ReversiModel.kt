package pt.isec.chichorrations.reversi_tp_am

import android.graphics.Canvas
import android.util.Log
import java.io.InputStream
import java.io.OutputStream
import java.io.Serializable
import java.net.ServerSocket
import java.net.Socket
import androidx.lifecycle.MutableLiveData
import java.net.InetSocketAddress
import kotlin.concurrent.thread

const val SERVER_PORT = 3000

class ReversiModel : Serializable{

    enum class State {
        STARTING, MODE_1, MODE_2, MODE_3, GAME_OVER
    }

    enum class ConnectionState {
        SETTING_PARAMETERS, SERVER_CONNECTING, CLIENT_CONNECTING, CONNECTION_ESTABLISHED,
        CONNECTION_ERROR, CONNECTION_ENDED
    }

    val state = MutableLiveData(State.STARTING)
    var caixa = mutableSetOf<ReversiPiece>()

    init {
        reset()
    }

    fun addPiece(toCol: Int,toLin: Int,player: ReversiPlayer){

        if(player == ReversiPlayer.WHITE){
            caixa.add(ReversiPiece(toCol, toLin, player,ReversiRankPeca.WHITE,R.drawable.pe_abrancajonix))
    }else
            caixa.add(ReversiPiece(toCol, toLin, player,ReversiRankPeca.BLACK,R.drawable.pe_apretajonix))

    }
    fun changePiece(toCol: Int,toLin: Int,player: ReversiPlayer){
    //    if(precisar de mudar){
        val deletePiece = pieceAt(toCol,toLin) ?: return
        caixa.remove(deletePiece)
        caixa.add(ReversiPiece(toCol, toLin, player,ReversiRankPeca.WHITE,R.drawable.pe_apretajonix))
 // }
}
    private fun reset(){
        caixa.removeAll(caixa)
       caixa.add(ReversiPiece(3, 3, ReversiPlayer.WHITE,ReversiRankPeca.WHITE,R.drawable.pe_abrancajonix))
       caixa.add(ReversiPiece(4, 3, ReversiPlayer.BLACK,ReversiRankPeca.BLACK,R.drawable.pe_apretajonix))
       caixa.add(ReversiPiece(4, 4, ReversiPlayer.WHITE,ReversiRankPeca.WHITE,R.drawable.pe_abrancajonix))
       caixa.add(ReversiPiece(3, 4, ReversiPlayer.BLACK,ReversiRankPeca.BLACK,R.drawable.pe_apretajonix))
    }

    fun pieceAt(col: Int, row: Int): ReversiPiece? {
        for (piece in caixa) {
            if (col == piece.col && row == piece.lin) {
                return piece
            }
        }
        return null
    }

    /** Communication */

    val connectionState = MutableLiveData(ConnectionState.SETTING_PARAMETERS)

    private var socket: Socket? = null
    private val socketI: InputStream?
        get() = socket?.getInputStream()
    private val socketO: OutputStream?
        get() = socket?.getOutputStream()

    private var serverSocket: ServerSocket? = null

    private var threadComm: Thread? = null

    fun startServer() {
        if (serverSocket != null ||
            socket != null ||
            connectionState.value != ConnectionState.SETTING_PARAMETERS)
            return

        connectionState.postValue(ConnectionState.SERVER_CONNECTING)

        thread {
            serverSocket = ServerSocket(SERVER_PORT)
            serverSocket?.apply {
                try {
                    startComm(serverSocket!!.accept())
                } catch (_: Exception) {
                    connectionState.postValue(ConnectionState.CONNECTION_ERROR)
                } finally {
                    serverSocket?.close()
                    serverSocket = null
                }
            }
        }
    }

    fun stopServer() {
        serverSocket?.close()
        connectionState.postValue(ConnectionState.CONNECTION_ENDED)
        serverSocket = null
    }

    fun startClient(serverIP: String,serverPort: Int = SERVER_PORT) {
        if (socket != null || connectionState.value != ConnectionState.SETTING_PARAMETERS)
            return

        thread {
            connectionState.postValue(ConnectionState.CLIENT_CONNECTING)
            try {
                //val newsocket = Socket(serverIP, serverPort)
                val newsocket = Socket()
                newsocket.connect(InetSocketAddress(serverIP,serverPort),5000)
                startComm(newsocket)
            } catch (_: Exception) {
                conpackage pt.isec.chichorrations.reversi_tp_am

import android.graphics.Canvas
import android.util.Log
import java.io.InputStream
import java.io.OutputStream
import java.io.Serializable
import java.net.ServerSocket
import java.net.Socket
import androidx.lifecycle.MutableLiveData
import java.net.InetSocketAddress
import kotlin.concurrent.thread

const val SERVER_PORT = 3000

class ReversiModel : Serializable{

    enum class State {
        STARTING, MODE_1, MODE_2, MODE_3, GAME_OVER
    }

    enum class ConnectionState {
        SETTING_PARAMETERS, SERVER_CONNECTING, CLIENT_CONNECTING, CONNECTION_ESTABLISHED,
        CONNECTION_ERROR, CONNECTION_ENDED
    }

    val state = MutableLiveData(State.STARTING)
    var caixa = mutableSetOf<ReversiPiece>()

    init {
        reset()
    }

    fun addPiece(toCol: Int,toLin: Int,player: ReversiPlayer){

        if(player == ReversiPlayer.WHITE){
            caixa.add(ReversiPiece(toCol, toLin, player,ReversiRankPeca.WHITE,R.drawable.pe_abrancajonix))
    }else
            caixa.add(ReversiPiece(toCol, toLin, player,ReversiRankPeca.BLACK,R.drawable.pe_apretajonix))

    }
    fun changePiece(toCol: Int,toLin: Int,player: ReversiPlayer){
    //    if(precisar de mudar){
        val deletePiece = pieceAt(toCol,toLin) ?: return
        caixa.remove(deletePiece)
        caixa.add(ReversiPiece(toCol, toLin, player,ReversiRankPeca.WHITE,R.drawable.pe_apretajonix))
 // }
}
    private fun reset(){
        caixa.removeAll(caixa)
       caixa.add(ReversiPiece(3, 3, ReversiPlayer.WHITE,ReversiRankPeca.WHITE,R.drawable.pe_abrancajonix))
       caixa.add(ReversiPiece(4, 3, ReversiPlayer.BLACK,ReversiRankPeca.BLACK,R.drawable.pe_apretajonix))
       caixa.add(ReversiPiece(4, 4, ReversiPlayer.WHITE,ReversiRankPeca.WHITE,R.drawable.pe_abrancajonix))
       caixa.add(ReversiPiece(3, 4, ReversiPlayer.BLACK,ReversiRankPeca.BLACK,R.drawable.pe_apretajonix))
    }

    fun pieceAt(col: Int, row: Int): ReversiPiece? {
        for (piece in caixa) {
            if (col == piece.col && row == piece.lin) {
                return piece
            }
        }
        return null
    }

    /** Communication */

    val connectionState = MutableLiveData(ConnectionState.SETTING_PARAMETERS)

    private var socket: Socket? = null
    private val socketI: InputStream?
        get() = socket?.getInputStream()
    private val socketO: OutputStream?
        get() = socket?.getOutputStream()

    private var serverSocket: ServerSocket? = null

    private var threadComm: Thread? = null

    fun startServer() {
        if (serverSocket != null ||
            socket != null ||
            connectionState.value != ConnectionState.SETTING_PARAMETERS)
            return

        connectionState.postValue(ConnectionState.SERVER_CONNECTING)

        thread {
            serverSocket = ServerSocket(SERVER_PORT)
            serverSocket?.apply {
                try {
                    startComm(serverSocket!!.accept())
                } catch (_: Exception) {
                    connectionState.postValue(ConnectionState.CONNECTION_ERROR)
                } finally {
                    serverSocket?.close()
                    serverSocket = null
                }
            }
        }
    }

    fun stopServer() {
        serverSocket?.close()
        connectionState.postValue(ConnectionState.CONNECTION_ENDED)
        serverSocket = null
    }

    fun startClient(serverIP: String,serverPort: Int = SERVER_PORT) {
        if (socket != null || connectionState.value != ConnectionState.SETTING_PARAMETERS)
            return

        thread {
            connectionState.postValue(ConnectionState.CLIENT_CONNECTING)
            try {
                //val newsocket = Socket(serverIP, serverPort)
                val newsocket = Socket()
                newsocket.connect(InetSocketAddress(serverIP,serverPort),5000)
                startComm(newsocket)
            } catch (_: Exception) {
                con