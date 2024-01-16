<h1>SignUpSQLiteApp</h1>

  <h2>Descrição</h2>

  <p>SignUpSQLiteApp é um aplicativo Android que demonstra o uso de SQLite para gerenciar registros de usuários, validação de formulários e funcionalidades básicas de autenticação. A aplicação possui funcionalidades como registro de usuários, login e um formulário de registro com validações.</p>

  <h2>Pré-requisitos</h2>

  <ul>
    <li>Android Studio</li>
    <li>JDK 8 ou superior</li>
    <li>Dispositivo Android ou Emulador com API 26 ou superior</li>
  </ul>

  <h2>Configuração do Ambiente de Desenvolvimento</h2>
  
```bash
git clone https://github.com/seu-username/SignUpSQLiteApp.git
```
  <ol>
    <li>Clone o repositório para sua máquina local.</li>
    <li>Abra o projeto no Android Studio.</li>
    <li>Certifique-se de ter configurado o JDK 8 nas configurações do projeto.</li>
    <li>Execute o aplicativo em um emulador ou dispositivo Android com API 26 ou superior.</li>
  </ol>
  <h2>Funcionalidades Principais</h2>
  <ul>
    <li>Registro de usuários</li>
    <li>Login</li>
    <li>Formulário de registro de dados do usuário com validações</li>
    <li>Listagem de usuários com filtro por nome</li>
    <li>Atualização e exclusão de dados do usuário logado</li>
    <li>Envio de requisição POST com dados do usuário logado</li>
  </ul>
  <h2>Arquitetura Utilizada</h2>
  <p>O projeto segue a arquitetura clássica Model-View-Controller (MVC):</p>
  <h3>Model (DatabaseHelper)</h3>
  <p>A classe DatabaseHelper interage diretamente com o banco de dados SQLite, gerenciando operações CRUD e validações relacionadas aos usuários.</p>
  <h3>View (LoginActivity, SignUpActivity, RegistryActivity, MainActivity)</h3>
  <p>As classes de interface do usuário gerenciam a apresentação e interação do usuário. Elas utilizam vinculação de dados para conectar a interface ao código Java e interagem com o banco de dados.</p>
  <h3>Controller (SessionManager)</h3>
  <p>A classe SessionManager atua como um controlador de sessão, gerenciando informações como email e ID do usuário, mantendo o estado da sessão entre atividades.</p>
  <h3>Model e View (User)</h3>
  <p>A classe User representa o modelo de dados do usuário, facilitando a passagem de informações entre o banco de dados e as atividades de interface do usuário.</p>
  <h2>Como Contribuir</h2>
  <p>Se desejar contribuir para o desenvolvimento deste projeto, siga os passos abaixo:</p>
  <ol>
    <li>Faça um fork do repositório.</li>
    <li>Crie uma branch para sua feature (`git checkout -b feature/sua-feature`).</li>
    <li>Faça commit de suas alterações (`git commit -am 'Adiciona nova feature'`).</li>
    <li>Faça push para a branch (`git push origin feature/sua-feature`).</li>
    <li>Abra um Pull Request.</li>
  </ol>
  <h2>Autor</h2>
  <p>Nicollas Guedes</p>
