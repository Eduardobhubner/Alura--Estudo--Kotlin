# Aprendendo a desserializar o json em objeto.

Em muitos sistemas temos que fazer o consumo de uma api para poder resgatar dados que nos são importantes e atribui-los em nosso sistema, exemplo, quando queremos fazer os cartazes dos nossos filmes ou jogos em uma página web para o cliente, esses dados devem ser puxados do nosso banco de dados e tratado dentro do back-end como objeto e repassado para o fron-end.

Mas vamos pegar essa etapa em especifico “Tratado dentro do back-end”.  Quando fazemos uma consulta via api, seja para um db ou api externa, devemos considerar que por fins de eficiência  e velocidade, o response sempre vai enviar um json em formato de string, logo, ele pega os dados e “SERREALIZA” para string e nos envia.

Mas como não queremos trabalhar com uma string gigante e sim com um json(java script object notation), um objeto no fim das contas, assim como o nome já fala. temos que “DESSERIALIZAR” a informação, e para isso temos algumas libs que nos ajudam, uma delas é a da própria google, chamamos ela de “gson” quase que podemos chama-lá de tio Gerson, rsrsrs.

---

Segue link do repositório da lib:

https://github.com/google/gson

Como eu estou usando o Maven para as minhas dependências, basta adiciona-la no pom.xls, segue:

```
<dependency>
  <groupId>com.google.code.gson</groupId>
  <artifactId>gson</artifactId>
  <version>2.10.1</version>
</dependency>
```

---

Para usar é bem tranquilo, vamos usar normalmente apenas duas funções e ele precisa de dois parâmetros:

### Funções principais:

- fromJson → Transformar a string(retorno da api) para um obj
- toString → Transformar o json em string ( muito usado para quando vc que vai enviar o objeto)

### PARÂMETROS:

- Objeto ou string a ser tratado
- Função ou Class que deve receber como parâmetro o retorno.

---



### Exemplo de code </>

```kotlin
import com.google.gson.Gson
val req = gson.fromJson(jsonRequest, classeQueVaiTratarReturn::Class.Java)
...
```

```kotlin
import com.google.gson.Gson
Gson gson = new Gson();
gson.toJson(1);
```

---

# Etapa 2 - Criar objeto a partir do json

Tendo o retorno da api como objeto sendo tratado por uma classe, agora podemos trata-lá dentro da classe o seu comportamento.

Uma das primeiras coisas que precisamos entender é qual key/value da api de fato vamos estar querendo trabalhar, eu vou usar como exemplo de retorno uma api publica do site cheapshark, segue escopo:

Request A:

```json
https://www.cheapshark.com/api/1.0/games?id=146 
```

retorno minificado:

```json
{
	info": {
	"title": "Batman: Arkham Asylum Game of the Year Edition",
	"steamAppID": "35140",
	"thumb": "https://cdn.cloudflare.steamstatic.com/steam/apps/35140/capsule_sm_120.jpg?t=1702934705"
	}
}
```

Vale ressaltar que ela trás mais coisas, mas para aprender/ensinar já da pra trabalhar apenas com essas 3 chaves, que até então vou querer apenas a “title” e “thumb”.

Dentro da classe temos que gerar as duas variaveis que vão receber os valores das duas chaves, para isso, é necessário de que os nomes das variáveis seja o mesmo que o nome da key do json.

```kotlin
Data Class Jogo(val title: String, val thumb:String)
```

Por se tratar de uma “Data Class”, o Kotlin já cria alguns métodos por baixo dos panos sem ter a necessidade de declará-las, exemplo disso é o .toString() ou .equals(), dessa forma não precisamos criar um corpo para a classe e nem definir um retorno, mas caso queira explicitar, ele também deixa você sobrescrever, basta sobrescrever a função, segue exemplo:

```kotlin
Data Class Jogo(val title: String, val thumb:String){
	override fun toString(): String {
        return "Jogo -- (titulo='$titulo', capa='$capa')"
    }
}
```

Data Class(Docs) — https://kotlinlang.org/docs/data-classes.html#properties-declared-in-the-class-body

### Aqui vão dois pontos importantes:

1 - existem muitas api que o nome da key muita das vezes não nos agrada e queremos fazer uma conversão de nome, isso ajuda a outras pessoas do time a entender o que de fato aquela key faz(existem algumas keys que vem como número que não tem uma informação muito precisa sobre o que se trata), para isso podemos usar uma lib nativa do kotlin chamada “SerializedName”, segue exemplo:

```kotlin
import com.google.gson.annotations.SerializedName

Data Class Jogo(@SerializedName('title') val titulo:String, @SerializedName('thumb') val capa:String)
//dessa forma todo o valor presente em title = titulo e thumb = capa
//(@SerializedName('steamAppID') val numIdentificadorJogo:Number) -- outro exemplo
```

2 - Quase sempre uma api nos retorna um objeto de matrix, isso quer dizer de que são objetos dentro de objetos que podem conter outros objetos, logo, temos que estar andando entre os objetos para chegar no end-point desejado(que diga de passagem é quase sempre o último objeto da hierarquia da matrix), então quando isso ocorre temos que criar classes intermediarias que vão receber o valor dos objetos que passando para o “próximo nvl(obj)”.

Vamos supor que agora o retorno da nossa api esteja assim:

```json
{
	"info": {
    "title": "Batman: Arkham Asylum Game of the Year Edition",
    "steamAppID": "35140",
    "thumb": "https://cdn.cloudflare.steamstatic.com/steam/apps/35140/capsule_sm_120.jpg?t=1702934705"
  },
  "cheapestPriceEver": {
    "price": "3.80",
    "date": 1567154111
  }
}
```

Antes de pegarmos o valor de title e thumb, primeiro temos que informar ao sistema de que esses valores estão dentro de “Info”, e para isso vamos criar uma nova classe, o nome dela vai ser InfoJogo(não sou criativo).

```kotlin
data class InfoJogo(val info: Jogo){
	override fun toString(): String {
        return info.toString()
    }
}
```

Agora temos que voltar no construtor do obj e trocar a Classe que vai tratar a desserialização do json:



```kotlin
//val req = gson.fromJson(jsonRequest, Jogo::Class.Java) old
val req = gson.fromJson(jsonRequest, InfoJogo::Class.Java) //new
```

Em resumo: Basicamente o que estamos fazendo é considerando o valor da chave "Info" do retorno da api como ponteiro(caminho) para dentro da classe InfoJogo, todo o value da chave vai ser passado para a classe Jogo que deve seguir com a criação obj conforme era nos passos anteriores.

Se tivéssemos que ter voltado ainda mais na matrix, teríamos que fazer mais uma classe para tratar da key anterior a “info”(apenas um exemplo de processo).