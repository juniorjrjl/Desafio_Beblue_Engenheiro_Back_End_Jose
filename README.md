#Instru��es para rodar o projeto

#Requisitos para rodar o projeto:

Uma das depend�ncias do projeto (org.projectlombok:lombok) exige configura��o da IDE, segue o link do site da lib: https://projectlombok.org,
Para o Eclipse siga as seguintes instru��es (realize os passos a seguir com a IDE fechada), caso contr�rio visite o site e procure pela configura��o de sua IDE:

*Clique 2x no jar (o mesmo que o projeto usa);

*Uma janela de configura��es ir� abrir, clique no bot�o "specify location";

*acessa a pasta de instala��o do eclipse e procure pelo arquivo autoexecut�vel do mesmo e selecione-o;

*Clique em "Install/Update" para concluir a instala��o;

#Alternar entre ambientes:

*troque o valor da chave "spring.profiles.active" de acordo com o ambiente que deseja rodar (dev = desenvolvimento, test = testes)

#Ambiente de desenvolvimento:

*O ambiente est� configurado para criar o banco de dados no MySQL pela url de conex�o no application-dev.properties;

*para fazer a conex�o com a api do spotify modifique as seguintes chaves no application.properties:
    para o clienteId = venda.spotify.cliente-id
    para o clientSecret = venda.spotify.cliente-secret
    (opcional) quantidade de discos para gerar = venda.spotify.limit
    
*O projeto assim que inicia verifica se j� tem discos cadastrados na base, se tiver ira realizar a busca no spotify, caso contr�rio a rotina � ignorada

*Os g�neros s�o cadastrados automaticamente na primeira migra��o que a lib do flyway roda na base de dados

#Ambiente de testes:

*O ambiente est� configurado para usar o banco H2 que consta em suas depend�ncias

#Endpoits da aplica��o

*consulta de discos pagin�da: GET /discos?page={pagina}&size={tamanhoPagina}&codigoGenero={codigoGenero}

*consulta de disco pelo codigo: GET /discos/{codigo}

*consulta de vendas paginada: GET /vendas?page{pagina}&limit=20&dataInicial={"yyyy-MM-dd" opcional}$dataFinal={"yyyy-MM-dd" opcional}

*consulta de venda por codigo: GET /vendas/1

*criar uma nova venda: POST /vendas

