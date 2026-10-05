# Controle de Doação de Sangue

Aplicativo Android simples para controle de doações de sangue.

## Descrição

Aplicativo desenvolvido em Java para Android Studio que permite o registro e acompanhamento de doações de sangue. Utiliza SharedPreferences para persistência de dados local, sem necessidade de banco de dados externo.

## Funcionalidades

- Login de usuários cadastrados
- Cadastro de novos usuários com tipo sanguíneo
- Perfil em formato de cartazinho, com edição de nome, email e senha
- Registro de doações (data, local, quantidade)
- Histórico de doações realizadas
- Interface simples e intuitiva

## Tecnologias

- **Linguagem**: Java
- **IDE**: Android Studio
- **Persistência**: SharedPreferences
- **JSON**: Gson
- **Lista**: RecyclerView
- **UI**: Material 3 (tokens de cor, tema claro/escuro automático)

## Tema e Auto-contraste

A identidade continua no vermelho (`#AD2C2C` / logo `#C02040`), mas a cor deixou de
ser escrita direto no layout. Tudo passa por **tokens do Material 3**, o que faz o
app alternar entre tema claro e escuro sozinho e mantém o texto sempre legível.

### Como funciona

| Arquivo | Papel |
|---------|-------|
| `res/values/colors.xml` | Paleta do tema **claro** |
| `res/values-night/colors.xml` | Mesmos nomes, tonalidades do tema **escuro** |
| `res/values/themes.xml` | **Único** tema: mapeia os papéis do Material 3 |
| `res/values/bools.xml` + `res/values-night/bools.xml` | Cor dos ícones das barras do sistema |
| `res/values/styles.xml` | Estilos de componente (botão, campo, cartão, pílula) |
| `res/values/dimens.xml` + `res/values-sw600dp/dimens.xml` | Espaçamentos e tipografia responsivos |
| `res/color/text_field_stroke.xml` | Cor da borda dos campos (seletor de estado) |

Como as paletas clara e escura usam **os mesmos nomes de recurso**, existe só uma
definição de tema: o Android escolhe a tonalidade certa conforme o modo do sistema.

### Regra de auto-contraste

Cada papel do Material 3 é usado junto do seu par `onX` (`colorOnSurface` sobre
`colorSurface`, `colorOnPrimary` sobre `colorPrimary`, etc.). Todos esses pares
passam de WCAG AA, então nenhuma combinação de tela fica ilegível — no claro nem
no escuro. As pílulas de status seguem a mesma lógica com os tokens
`status_*` / `status_*_container`.

**Ao criar uma tela nova, nunca use cor literal** (`#FF0000`, `0xFFFF0000`).
Use `?attr/colorOnSurface`, `?attr/colorSurfaceContainerLow`,
`?attr/colorOutlineVariant` etc.

### Layout responsivo

- `BaseActivity` ativa o *edge-to-edge* e reserva as margens da status bar /
  barra de navegação no container `@id/rootConteudo` de cada layout.
- `AutoFitGridLayoutManager` calcula o número de colunas da RecyclerView pela
  largura da tela: uma coluna no celular, duas ou mais em tablets — sem layout
  alternativo.
- `content_max_width` + `values-sw600dp` centralizam o conteúdo em telas largas.

### Identidade visual preservada

- Cabeçalhos com o gradiente vermelho (`drawable/bg_hero`) em todas as telas.
- Logo dentro de um círculo claro (`drawable/bg_logo_badge`), para o logo vermelho
  não sumir sobre o fundo vermelho.
- Fontes Bungee (títulos) e Montserrat (corpo) mantidas.

## Estrutura do Projeto

```
app/src/main/java/br/edu/unifaj/cc/mobile/logincomnavegacao/
├── model/
│   ├── entity/            # Hemocentro, Endereco, Agendamento, BolsaSangue
│   ├── enums/             # TipoSanguineo, FatorRh, StatusAgendamento, StatusBolsa
│   ├── user/              # User, Doador
│   └── NivelDoador.java   # Nível do cartazinho (teto de 3 registros)
├── util/
│   ├── PrefsManager.java         # Gerenciador de SharedPreferences
│   ├── AutoFitGridLayoutManager.java # Grade com colunas calculadas pela largura
│   ├── InsetsUtils.java          # Margens das barras do sistema (edge-to-edge)
│   ├── DateUtils.java
│   └── ValidacaoUtils.java
├── adapter/
│   ├── MenuHomeAdapter.java   # Cards do menu principal
│   ├── AgendamentoAdapter.java
│   └── BolsaSangueAdapter.java
├── activity/
│   ├── BaseActivity.java      # Base: edge-to-edge + insets
│   ├── LoginActivity.java
│   ├── CadastroActivity.java
│   ├── HomeActivity.java
│   ├── DoacaoActivity.java
│   ├── AgendamentoActivity.java
│   ├── HistoricoActivity.java
│   ├── ListaAgendamentosActivity.java
│   ├── PerfilActivity.java
│   └── EditarPerfilActivity.java
└── dao/
    └── HemocentroDAO.java
```

## Fluxo das Telas

1. **Login** → Usuário acessa com email e senha
2. **Cadastro** → Novo usuário se registra
3. **Home** → Menu principal após login
4. **Perfil** → Cartazinho do doador, aberto pelo topo da Home
5. **Editar Perfil** → Altera nome, email e senha
6. **Doação** → Registra nova doação
7. **Histórico** → Lista de doações realizadas

## Como Executar

1. Clone o repositório
2. Abra no Android Studio
3. Aguarde a sincronização do Gradle
4. Execute no emulador ou dispositivo

### Gerar APK

1. No menu superior, vá em: **Build → Build Bundle(s) / APK(s) → Build APK(s)**
2. Aguarde a compilação
3. O APK será gerado em: `app/build/outputs/apk/debug/app-debug.apk`

### Via Linha de Comando

```bash
./gradlew assembleDebug
```

## Dados Armazenados

Os dados são armazenados localmente no dispositivo usando SharedPreferences no formato JSON:

### Usuário (SharedPreferences key: "user")
```json
{
  "nome": "João Silva",
  "email": "joao@email.com",
  "senha": "123456",
  "tipoSanguineo": "O+"
}
```

### Lista de Doações (SharedPreferences key: "doacoes")
```json
[
  {
    "data": "13/04/2026",
    "local": "Hospital Central",
    "quantidade": "450ml"
  },
  {
    "data": "01/03/2026",
    "local": "Hemocentro Municipal",
    "quantidade": "450ml"
  }
]
```

## Regras de Negócio

- Campos vazios não são permitidos em nenhuma tela
- Login validar email e senha salvos
- Apenas usuários logados podem registrar doações
- Histórico exibe todas as doações do usuário

## Descrição das Classes

### Model

- **User.java**: Classe modelo com atributos nome, email, senha e tipoSanguineo
- **Doacao.java**: Classe modelo com atributos data, local e quantidade

### Util

- **PrefsManager.java**: Classe auxiliar para gerenciar dados no SharedPreferences, com métodos para salvar/recuperar usuário, fazer login, salvar/listar doações

### Adapter

- **MenuHomeAdapter.java**: Cards do menu principal (ícone, título e descrição)
- **AgendamentoAdapter.java**: Lista de agendamentos, com pílula de status e
  contador de bolsas registradas
- **AgendamentoParaRegistroAdapter.java**: Agendamentos que o agente pode registrar
- **BolsaSangueAdapter.java**: Histórico de lotes doados, com local e quantidade

### Activities

Todas estendem `BaseActivity`, que ativa o edge-to-edge e aplica as margens das
barras do sistema no container `@id/rootConteudo` de cada layout.

- **BaseActivity.java**: Base das telas (edge-to-edge e tratamento de insets)
- **LoginActivity.java**: Tela inicial com validação de email e senha
- **CadastroActivity.java**: Tela para cadastrar novos usuários
- **HomeActivity.java**: Tela principal com menu de opções em cards
- **DoacaoActivity.java**: Agente escolhe qual agendamento registrar (lista somente-leitura)
- **RegistrarDoacaoActivity.java**: Confirma a coleta; data e local travados
- **AgendamentoActivity.java**: Doador cria o agendamento
- **HistoricoActivity.java**: Lista dos lotes doados
- **ListaAgendamentosActivity.java**: Lista de agendamentos do doador
- **PerfilActivity.java**: Cartazinho do doador, somente-leitura
- **EditarPerfilActivity.java**: Altera nome, email e senha

### Modelo

- **BolsaSangue.java**: `agendamentoId` liga a bolsa ao agendamento (garante uma
  bolsa por agendamento) e `quantidade` guarda quantas bolsas saíram na sessão
- **BolsaSangue.fromAgendamento()** herda data, local e validade do agendamento, para
  que o registro nunca divirja do que foi agendado
- **NivelDoador.java**: regra do nível do cartazinho - a barra enche com os
  registros de coleta e trava em `LIMITE` (3). Classe pura, sem Android, para
  ficar testável sem emulador

## Manual de Usuário

> O app segue o tema claro/escuro do sistema. Para ver o outro modo, altere
> **Configurações → Tela → Modo escuro**.

### Primeiro Acesso

1. Na tela de login, clique em "Ainda não tenho cadastro"
2. Preencha todos os campos (nome, email, senha, CPF, tipo sanguíneo)
3. Clique em "Cadastrar"
4. Você será redirecionado para a tela de login
5. Faça login com os dados cadastrados

### Registrar Coleta

> A coleta é registrada pelo **agente de saúde**, e sempre a partir de um
> agendamento que o doador já criou. Por isso data e local não são digitados:
> eles vêm do agendamento e aparecem travados na tela.

1. Na tela principal, toque em "Registrar coleta"
2. Escolha o agendamento na lista (só aparecem os de data de hoje ou anterior,
   que ainda não têm coleta registrada)
3. Confira a data e o hemocentro mostrados no topo
4. Informe o **volume por bolsa** e a **quantidade de bolsas**
5. Toque em "Registrar coleta"
6. A bolsa entra no histórico e o agendamento passa a "Realizado"

### Agendar Doação

1. Na tela principal (Home), toque em "Agendar doação"
2. Escolha o hemocentro, a data e a hora
3. Clique em "Confirmar agendamento"
4. O agendamento aparece em "Meus agendamentos", onde também pode ser cancelado

### Ver Histórico

1. Na tela principal (Home), toque em "Ver histórico"
2. Veja a lista de todos os lotes doados
3. Cada item mostra: código, tipo, quantidade, volume, local, coleta e validade

### Ver o Perfil

1. Na tela principal (Home), toque no cartazinho do topo
2. O cartão mostra nome, email, CPF e tipo sanguíneo
3. Veja a barra **Nível de doador** e a contagem de registros
4. Toque em "Editar perfil" para alterar nome, email ou senha

O cartazinho substitui a antiga saudação "Olá, usuário". Ele é a única entrada
para a tela de perfil, então não existe um card extra no menu.

#### Nível de doador

A barra do cartazinho enche com base no **número de registros de coleta já
existentes** e chega ao máximo em **3 registros**:

| Registros | Barra | Texto |
|-----------|-------|-------|
| 0 | vazia | "0 de 3 registros" |
| 1 | 33% | "1 de 3 registro" |
| 2 | 67% | "2 de 3 registros" |
| 3 ou mais | 100% | "Nível máximo alcançado" |

Um registro é um agendamento registrado pela agente. A quantidade de bolsas da
sessão não altera o nível: 1 ou 6 bolsas na mesma sessão contam como 1 registro
só. A regra mora em `NivelDoador.java`, que é uma classe pura de Java, sem
dependência de Android, justamente para o teto ser testável sem emulador.

O valor é recalculado a cada vez que a tela de perfil aparece, então a barra
sempre reflete a lista de registros gravada. Na prática ela só cresce, porque
hoje nenhum fluxo apaga um registro de coleta.

### Editar Perfil

1. Na tela de perfil, toque em "Editar perfil"
2. Altere o nome e/ou o email
3. Para trocar a senha, preencha **senha atual** e **nova senha**; deixar os
   dois campos em branco mantém a senha atual
4. Toque em "Salvar alterações" e volte ao cartazinho, que já mostra os dados novos

CPF e tipo sanguíneo aparecem na tela mas **não podem ser editados**:

- O **CPF** é a chave que associa o doador aos agendamentos. Se fosse editável,
  os agendamentos antigos deixariam de aparecer, por isso a tela avisa que ele é fixo.
- O **tipo sanguíneo** vem do cadastro e é exibido como referência.

### Sair

1. Na tela principal (Home), toque em "Sair da conta"
2. Você será redirecionado para a tela de login

## Autores

- Rodolfo Rodrigues Pinheiro - RA: 12530689
- Rodrigo Pereira Junior - RA: 12529249
- Camilli dos Santos - RA: 12529495
- Otávio Siqueira Gonçalves - RA: 12529937
- Gabriel Rodrigues de Oliveira - RA: 12529520

## Diagramas de Classe

Os diagramas de classes do projeto estão disponíveis na pasta `diagramas/`:

### Arquivos Disponíveis

| Arquivo | Descrição |
|--------|-----------|
| `diagramas/classes-dominio.plantuml` | Diagrama do modelo de domínio (PlantUML) |
| `diagramas/diagrama-doacaoSangue.png` | Diagrama visual em PNG |

![Diagrama](diagramas/diagrama-doacaoSangue.png)

### Estrutura do Modelo de Domínio

```
model/
├── enums/
│   ├── TipoSanguineo (A, B, AB, O)
│   ├── FatorRh (POSITIVO, NEGATIVO)
│   ├── StatusAgendamento (PENDENTE, CONFIRMADO, CANCELADO, REALIZADO)
│   └── StatusBolsa (DISPONIVEL, RESERVADA, UTILIZADA, VENCIDA)
├── entity/
│   ├── Hemocentro
│   ├── Endereco
│   ├── Agendamento
│   └── BolsaSangue
└── user/
    ├── User
    └── Doador
```

## Licença

MIT