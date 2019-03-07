# Instruções para rodar o projeto

# Requisitos para rodar o projeto:

Uma das dependências do projeto (org.projectlombok:lombok) exige configuração da IDE, segue o link do site da lib: https://projectlombok.org,
Para o Eclipse siga as seguintes instruções (realize os passos a seguir com a IDE fechada), caso contrário visite o site e procure pela configuração de sua IDE:

* Clique 2x no jar (o mesmo que o projeto usa);

* Uma janela de configurações irá abrir, clique no botão "specify location";

* acessa a pasta de instalação do eclipse e procure pelo arquivo autoexecutável do mesmo e selecione-o;

* Clique em "Install/Update" para concluir a instalação;

# Alternar entre ambientes:

* troque o valor da chave "spring.profiles.active" de acordo com o ambiente que deseja rodar (dev = desenvolvimento, test = testes)

# Ambiente de desenvolvimento:

* O ambiente está configurado para criar o banco de dados no MySQL pela url de conexão no application-dev.properties;

* para fazer a conexão com a api do spotify modifique as seguintes chaves no application.properties:
    para o clienteId = venda.spotify.cliente-id
    para o clientSecret = venda.spotify.cliente-secret
    (opcional) quantidade de discos para gerar = venda.spotify.limit
    
* O projeto assim que inicia verifica se já tem discos cadastrados na base, se tiver ira realizar a busca no spotify, caso contrário a rotina é ignorada

* Os gêneros são cadastrados automaticamente na primeira migração que a lib do flyway roda na base de dados

# Ambiente de testes:

* O ambiente está configurado para usar o banco H2 que consta em suas dependências

# Endpoits da aplicação

* consulta de discos pagináda: GET /discos?page={pagina}&size={tamanhoPagina}&codigoGenero={codigoGenero}

* consulta de disco pelo codigo: GET /discos/{codigo}

* consulta de vendas paginada: GET /vendas?page{pagina}&limit=20&dataInicial={"yyyy-MM-dd" opcional}$dataFinal={"yyyy-MM-dd" opcional}

* consulta de venda por codigo: GET /vendas/1

* criar uma nova venda: POST /vendas

