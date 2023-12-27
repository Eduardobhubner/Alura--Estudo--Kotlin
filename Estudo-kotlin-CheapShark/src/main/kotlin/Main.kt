import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers

fun main() {

    //declarando um objeto cliente
    val client = HttpClient.newHttpClient()
    //definindo o corpo da requisição
    val request = HttpRequest.newBuilder().uri(URI("https://www.cheapshark.com/api/1.0/games?title=batman")).build()
    //enviando a requisição e armazenando seu retorno
    val response = client.send(request, BodyHandlers.ofString())

    val json = response.statusCode()

    println(json)


}