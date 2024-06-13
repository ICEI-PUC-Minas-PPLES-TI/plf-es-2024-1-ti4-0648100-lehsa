# LEHSA

O sistema LEHSA é destinado
ao gerenciamento do Laboratório de Estudos em Hidráulica e Saneamento Ambiental
(LEHSA) no Instituto Federal de Sergipe (IFS). Diante da falta de robustez nos sistemas
atuais, que ainda se apoiam fortemente em métodos analógicos como planilhas de Excel, papel e comunicacão direta entre os envolvidos, a digitalizacão se apresenta como
resposta vital. Essa transição tornou-se não apenas desejável, mas uma necessidade essencial,à medida que as responsabilidades e dependências do laboratório cresceram, tornando a abordagem tradicional insustentável

## Alunos integrantes da equipe

* Lucas Cabral Soares
* Lucas Hemétrio Teixeira
* Lucca Oliveira Vasconcelos de Faria
* Maria Eduarda Amaral Muniz
* Vítor Lagares Stahlberg

## Professores responsáveis

* Lucila Ishitani
* Soraia Lúcia da Silva

## Instruções de utilização

[Assim que a primeira versão do sistema estiver disponível, deverá complementar com as instruções de utilização. Descreva como instalar eventuais dependências e como executar a aplicação.]

Para usar o sistema o primeiro a ser feito é baixar o arquivo .zip do repositório, descompactar o arquivo e abrir na sua IDE de preferência (Durante o desenvolvimento foi usado Intellij para o Back-end, e Visual Studio Code para o Front-end). Para executar o Back-end deve configurar a IDE para usar a SDK do Java 17 e após isso inicializar o Main. Já para o Front-end deve instalar as dependências digitando "npm install" no terminal e "npm run dev" para inicializar. Um banco de dados PostgreSql deve estar criado com o nome de "gerenciador-lehsa" e um usuario admin para que o sistema seja inicializado com sucesso.

O sistema foi desenhado com o foco em simplicidade. A página inicial é um login, com email e senha, onde uma conta admin poderá preencher com os dados e começar a usar o sistema. Após fazer o login você será redirecionado para a tela de Itens, onde todos os esquipamentos e materiais do laboratório estarão aparecendo. Na direita superior da tela tem o botão para adiocionar novos Itens, é só clicar no botão e preencher o dados do formulário e o Item estará disponível para acesso na página principal. No menu a esquerda tem as opções de navegação dentro do sistema. Em agendamentos pode ver os agendamentos feitos por outros usuários e Aceitar ou Recusar a solicitação; o mesmo se aplica para empréstimos. Na aba de usuários todos as pessoas cadastradas no sistema estarão disponíveis para visualização.

Caso não possua uma conta admin é possível fazer um cadastro para acessar a funcionalidade de agendamento e empréstimo de equipamentos do laboratório. É somente selecionar um Item disponível e preencher o formulário, o admin receberá sua solicitação e você saberá sua resposta dentro de alguns dias.
