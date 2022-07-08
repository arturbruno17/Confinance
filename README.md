<h1 align="center">Confinance</h1>
<p align="center">Aplicativo para controle de gastos. Nele, você pode cadastrar contas bancárias e cartões de crédito, assim como registrar transações que você fez nos mesmos. </p>

<p align="center">
	<img src="https://img.shields.io/static/v1?label=room&message=2.4.1&color=2196F3&style=flat-square"/>
  <img src="https://img.shields.io/static/v1?label=kotlinx-datetime&message=2.3.0&color=7F52FF&style=flat-square"/>
  <img src="https://img.shields.io/static/v1?label=hilt&message=2.40.5&color=2196F3&style=flat-square"/>
  <img src="https://img.shields.io/static/v1?label=RecyclerViewSwipeDecorator&message=1.3&color=2196F3&style=flat-square"/>
</p>

<p align="center">
 <a href="#-sobre">Sobre</a> •
 <a href="#-screenshots">Screenshots</a> • 
 <a href="#-bibliografia">Bibliografia</a> •
 <a href="#-contribuição">Contribuição</a>
</p>

# 📜 Sobre
Aplicativo de controle e registro de gastos feito com Android nativo. Ao entrar, é possível ver o dashboard que, inicialmente, não possui nenhuma informação. Possui também um _floating action button_ capaz de cadastrar tanto cartões de crédito como contas bancárias.

Ao cadastrar uma conta bancária, é possível registrar depósitos e saques realizados. Ao cadastrar um cartão de crédito, é possível registrar compras feitas e pagamento de faturas com as contas cadastradas por você.

* **Room**: biblioteca de persistência que oferece uma camada de abstração sobre o SQLite para permitir um acesso mais robusto ao banco de dados. 

* **kotlinx-datetime**: Uma biblioteca Kotlin multiplataforma para trabalhar com data e hora.

* **Hilt**: O Hilt fornece uma maneira padrão de incorporar a injeção de dependência do Dagger em um aplicativo Android.

* **RecyclerViewSwipeDecorator**: Uma classe de utilitário simples para adicionar um plano de fundo, um ícone e um rótulo a um item do RecyclerView enquanto o desliza para a esquerda ou para a direita.

# 📱 Screenshots
<p align="center">
  <img src="https://i.ibb.co/cD7jpZ3/Screenshot-2022-07-08-09-10-21-543-me-arturbruno-confinance.jpg" width="30%"/>
	<img src="https://i.ibb.co/HK6R9LK/Screenshot-2022-07-08-09-10-24-289-me-arturbruno-confinance.jpg" width="30%"/>
  <img src="https://i.ibb.co/YQzWcqc/Screenshot-2022-07-08-09-10-30-397-me-arturbruno-confinance.jpg" width="30%"/>
</p>

# 📚 Bibliografia
Nesta seção, você encontrará vários links e recursos que falam acerca das bibliotecas e extras utilizados no projeto.

|  Biblioteca   |  Link 	|
|---	|---	|
|   Room	|   https://developer.android.com/training/data-storage/room	|
|   kotlinx-datetime	|    	https://github.com/Kotlin/kotlinx-datetime	|
|   Hilt	|   https://developer.android.com/training/dependency-injection/hilt-android?hl=pt-br#inject-interfaces	|
|   RecyclerViewSwipeDecorator	|   https://github.com/xabaras/RecyclerViewSwipeDecorator	|

# 🤝 Contribuição
O app foi criado e testado em um dispositivo físico, Redmi Note 9s, mas é disponível para qualquer um que queira contribuir.

Caso tenha alguma ideia de como melhorar o app, realize os seguintes passos:

1. Para contribuir, basta fazer um fork. 
(<https://github.com/arturbruno17/Confinance/fork>)

2. Crie uma branch para sua modificação
(`git checkout -b feature/fooBar`)

3. Faça o commit
(`git commit -am "Add some fooBar"`)

4. Push
(`git push origin feature/fooBar`)

5. Crie um novo *Pull Request*
