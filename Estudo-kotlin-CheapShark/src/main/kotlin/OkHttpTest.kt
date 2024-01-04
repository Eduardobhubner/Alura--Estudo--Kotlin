import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

fun main(){
    val trustManager: X509TrustManager = object : X509TrustManager {
        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return arrayOf<X509Certificate>()
        }

        override fun checkClientTrusted(certs: Array<X509Certificate?>?, authType: String?) {}
        override fun checkServerTrusted(certs: Array<X509Certificate?>?, authType: String?) {}
    }

    val sslContext = SSLContext.getInstance("SSL");
    sslContext.init(null,Array<TrustManager>(1) {trustManager} ,  java.security.SecureRandom());

    val newBuilder = OkHttpClient.Builder();
    newBuilder.sslSocketFactory(sslContext.getSocketFactory(), trustManager);
    newBuilder.hostnameVerifier(HostnameVerifier { hostname, session -> true });

    val client =  newBuilder.build()

    val request =  Request.Builder()
        .url("https://www.cheapshark.com/api/1.0/games?title=batman")
        .build();

    val response = client.newCall(request).execute()
        print(response.body?.string())

}
