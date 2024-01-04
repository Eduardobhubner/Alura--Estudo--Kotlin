import com.google.gson.Gson
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


fun main() {

    //Ignore tls for using in requestHTTP
    val sslContext = SSLContext.getInstance("TLS")
    val trustManager: X509TrustManager = object : X509TrustManager {
        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return arrayOf<X509Certificate>()
        }

        override fun checkClientTrusted(certs: Array<X509Certificate?>?, authType: String?) {
            println("ignorando protocolos tls")
        }

        override fun checkServerTrusted(certs: Array<X509Certificate?>?, authType: String?) {
            println("ignorando protocolos tls")
        }
    }

    sslContext.init(null, arrayOf<TrustManager>(trustManager), SecureRandom())

    val client = HttpClient.newBuilder()
        .sslContext(sslContext)
        .build()

    //definindo o corpo da requisição
    val request = HttpRequest.newBuilder().uri(URI("https://www.cheapshark.com/api/1.0/games?id=146")).build()
    //enviando a requisição e armazenando seu retorno
    val response = client.send(request, BodyHandlers.ofString())

    val json = response.body()
//    println(json)
    val gson = Gson()

    val dataApiGame = gson.fromJson(json, ReturnJogo::class.java)
    val jogo = DataApiShark(dataApiGame.info.title, dataApiGame.info.thumb)

    println(jogo)

}