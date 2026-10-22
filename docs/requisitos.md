# Especificação de requisitos — Aplicação de Receitas e Livros

**Arquivo:** `docs/requisitos.md`  
**Versão:** 1.1 — revisão P03 (classificação dos registros) — proposta para validação  
**Data:** 21/10/2026  
**Estado:** levantamento de requisitos; nenhuma funcionalidade implementada neste documento.

> **Convenção:** **Confirmado** identifica decisões expressas pelo solicitante. **Proposto** identifica detalhamentos necessários para tornar o projeto implementável, mas ainda sujeitos à validação. Em caso de divergência, a decisão do responsável pelo produto prevalece.

## 1. Objetivo e escopo

### 1.1 Objetivo

Desenvolver uma aplicação web de organização de receitas culinárias e de livros de receitas, acessível em uma rede interna. Usuários com perfil `USER` poderão cadastrar e organizar receitas com ingredientes estruturados; usuários com perfil `ADMIN` administrarão contas, configurações, logs operacionais, eventos de segurança e registros de auditoria, sem gerenciar o conteúdo culinário.

### 1.2 Escopo da primeira versão (MVP)

- Autenticação, encerramento de sessão e controle de acesso por perfil.
- Administração de usuários: cadastro, atualização de dados, redefinição de senha, ativação e desativação (exclusão lógica).
- Cadastro, consulta, edição, exclusão lógica e pesquisa de receitas.
- Gestão dos ingredientes pertencentes a cada receita, com quantidade e unidade de medida.
- Cadastro, consulta, edição, exclusão lógica e pesquisa de livros de receitas.
- Associação de uma receita a vários livros, inclusive livros de usuários distintos.
- Administração das unidades de medida e de configurações básicas do sistema.
- Registros distintos de **operação**, **segurança** e **auditoria**, com consulta, filtros e exportação no painel administrativo.
- Persistência, implantação em contêineres, testes, backup e operação sem dependência de internet externa.

### 1.3 Atores

| Ator | Responsabilidade |
| --- | --- |
| `USER` | Consultar receitas compartilhadas; cadastrar e gerenciar suas receitas, ingredientes e livros; incluir receitas compartilhadas nos próprios livros. |
| `ADMIN` | Cadastrar, atualizar, redefinir senhas, ativar/desativar usuários; administrar unidades de medida e configurações; consultar, filtrar e exportar logs. |
| Não autenticado | Acessar somente a tela e o serviço de autenticação, sem consultar dados da aplicação. |

**Regra de separação de funções (proposta):** `ADMIN` não recebe automaticamente permissões de `USER`. Operações de conteúdo exigem conta `USER`, mesmo que o administrador tenha acesso às telas administrativas.

## 2. Requisitos funcionais (RF)

### 2.1 Gestão de usuários

| ID | Requisito | Prioridade |
| --- | --- | --- |
| RF-001 | Autenticar usuários por identificador de login e senha. | Alta |
| RF-002 | Diferenciar permissões de `ADMIN` e `USER`, inclusive nas APIs. | Alta |
| RF-003 | Permitir ao administrador cadastrar usuários. | Alta |
| RF-004 | Permitir ao administrador desativar e reativar usuários. | Alta |
| RF-005 | Permitir ao administrador solicitar exclusão de usuários **somente de forma lógica**, equivalente à desativação no MVP. | Alta |
| RF-006 | Permitir que o usuário autenticado encerre sua sessão. | Alta |
| RF-017 | Permitir ao administrador atualizar dados cadastrais e redefinir senhas de usuários, sem visualizar a senha anterior. | Alta |

> **Ajuste de consistência:** RF-004 e RF-005 representam a mesma transição prática no MVP. Não haverá exclusão física de contas. Na interface, recomenda-se a ação única **Desativar usuário**, evitando dois botões com o mesmo efeito.

### 2.2 Gestão de receitas, ingredientes e livros

| ID | Requisito | Prioridade |
| --- | --- | --- |
| RF-007 | Permitir ao usuário cadastrar, consultar, editar e excluir logicamente receitas, respeitando permissões de edição/exclusão. | Alta |
| RF-008 | Permitir ao usuário adicionar, editar e remover ingredientes das receitas que pode editar. | Alta |
| RF-009 | Permitir ao usuário cadastrar, consultar, editar e excluir logicamente seus livros. | Alta |
| RF-010 | Permitir associar e desassociar receitas a livros, inclusive uma receita a diversos livros. | Alta |
| RF-011 | Permitir pesquisar receitas compartilhadas e livros próprios por nome. | Alta |
| RF-012 | Permitir listar registros com paginação. | Média |
| RF-019 | Permitir a usuários `USER` consultar receitas compartilhadas, sem conceder automaticamente permissão de edição ou exclusão. | Alta |

### 2.3 Administração, logs e auditoria

| ID | Requisito | Prioridade |
| --- | --- | --- |
| RF-013 | Registrar, com **classificação explícita e informações apropriadas**, (a) logs operacionais da aplicação, (b) eventos de segurança e (c) trilhas de auditoria das operações críticas. | Alta |
| RF-014 | Permitir ao `ADMIN` consultar as **três categorias** de registros, com classificação visível e detalhes sanitizados adequados a cada categoria. | Alta |
| RF-015 | Permitir filtrar registros por período, categoria, tipo/código do evento, gravidade e resultado; por usuário ou identificador de correlação quando aplicável. | Média |
| RF-016 | Permitir ao `ADMIN` exportar registros consultáveis em **CSV ou TXT**, preservando a categoria, os campos pertinentes e os filtros aplicados, sem divulgar informações protegidas. | Alta |
| RF-018 | Permitir ao administrador cadastrar, consultar, editar e desativar unidades de medida. | Alta |
| RF-020 | Permitir ao administrador consultar e alterar configurações básicas e explicitamente autorizadas da aplicação. | Média |

**Escopo de consulta/exportação:** o painel expõe registros **operacionais**, **de segurança** e **de auditoria**. O administrador visualiza mensagens de erro **sanitizadas**; dumps de memória, stack traces completos, SQL com parâmetros, variáveis de ambiente e detalhes de infraestrutura sensíveis **não** são exibidos nem exportados. Logs técnicos detalhados, caso indispensáveis ao suporte, exigem processo e acesso de infraestrutura próprios, não incluídos no MVP.

**Observação:** “Gerenciar logs” significa consultar, filtrar, exportar e administrar políticas de retenção aprovadas; não autoriza editar ou apagar eventos individualmente. Consultas e exportações administrativas também devem gerar auditoria, sem realimentação infinita do próprio mecanismo de logging.

## 3. Requisitos não funcionais (RNF)

| ID | Requisito | Condição de verificação |
| --- | --- | --- |
| RNF-001 | Aplicação utilizável em navegador moderno. | Fluxos principais funcionam nas versões suportadas de Chrome, Firefox e Edge. |
| RNF-002 | Acesso pela rede interna, sem exigir internet para operar. | Com a conexão externa desativada, login e operações essenciais continuam disponíveis na rede interna. |
| RNF-003 | Dados persistidos em PostgreSQL. | Dados permanecem após reinício da aplicação e dos contêineres. |
| RNF-004 | Contêineres para implantação reproduzível. | Ambiente sobe mediante configuração documentada e sem instalação manual de dependências da aplicação. |
| RNF-005 | Credenciais e segredos fora do código-fonte. | Segredos provenientes de variáveis/gestor de segredos; ausentes do repositório e das imagens publicadas. |
| RNF-006 | Testes automatizados nas funcionalidades essenciais. | Testes de validação, autorização e regras de negócio são executados no pipeline. |
| RNF-007 | Ambientes separados de desenvolvimento, homologação e produção. | Cada ambiente utiliza configurações e bases segregadas. |
| RNF-008 | Pipeline de integração e entrega contínuas. | Pipeline executa verificações automatizadas antes de liberar uma implantação. |
| RNF-009 | Registros e exportações sem segredos ou dados pessoais desnecessários. | Inspeção de exemplos operacionais, de segurança e auditoria confirma ausência de senhas, hashes de senha, tokens, cookies, strings de conexão, SQL com parâmetros e chaves; dados pessoais são minimizados. |
| RNF-010 | Backup e recuperação dos dados. | Procedimentos documentados e restauração testada periodicamente em ambiente isolado. |
| RNF-011 | Proteção de autenticação e autorização. | Senhas armazenadas com hash seguro; cada operação protegida é autorizada no servidor, não apenas na interface. |
| RNF-012 | Integridade e rastreabilidade dos registros de auditoria. | Eventos são cronológicos e atribuíveis, com ator (ou origem de sistema), instante, ação, objeto, resultado e proteção contra alteração/exclusão não autorizada. |
| RNF-013 | Integridade referencial e validação no servidor. | Entradas inválidas ou referências inexistentes são rejeitadas sem gerar estado inconsistente. |
| RNF-014 | Classificação e correlação de logs. | Logs operacionais, de segurança e de auditoria exibem categoria e estrutura padronizada; eventos da mesma requisição possuem identificador de correlação comum. |
| RNF-015 | Controle de acesso e proteção dos registros. | Somente `ADMIN` consulta/exporta registros pelo produto; leitura, exportação e tentativas negadas são rastreáveis; armazenamento impede edição comum e permite detectar adulteração, conforme mecanismo a definir. |
| RNF-016 | Registro operacional resiliente a falhas do PostgreSQL. | Falha simulada de conexão com o banco gera um registro operacional em destino local/interno independente do próprio PostgreSQL, sem vazamento de segredo; indisponibilidade do painel não é apresentada como lista vazia. |

**Metas quantitativas ainda não definidas:** tempo de resposta, volume de usuários simultâneos, RPO/RTO de backup e **retenção por categoria** (operacional, segurança e auditoria). Esses números e a política de descarte não devem ser tratados como aprovados sem decisão de negócio e avaliação legal.

## 4. Seis decisões de negócio

| Nº | Decisão | Status | Resolução e justificativa |
| --- | --- | --- | --- |
| D-01 | Receitas privadas ou compartilhadas? | **Confirmado** | **Compartilhadas:** usuários `USER` autenticados podem consultar receitas de outros usuários. Evita duplicação de receitas e facilita colaboração. Compartilhamento de leitura **não implica** edição irrestrita (ver regra proposta RN-REC-04). |
| D-02 | Uma receita pode aparecer em vários livros? | **Confirmado** | **Sim, sem limite de livros por receita.** O livro funciona como um caderno/coleção. A associação é muitos-para-muitos; repetir o mesmo vínculo dentro de um único livro não acrescenta valor e será impedido. |
| D-03 | Ingredientes são um catálogo global? | **Confirmado** | **Não.** Ingredientes são registros vinculados a cada receita, com nome, quantidade e unidade próprios. Isso reduz dependências de um catálogo central; unidades de medida, diferentemente de ingredientes, são administradas globalmente. |
| D-04 | O administrador gerencia receitas? | **Confirmado** | **Não.** `ADMIN` atua exclusivamente em usuários, logs e configurações (inclusive unidades de medida). A separação diminui o risco de alteração indevida de conteúdo. |
| D-05 | Excluir usuário remove suas receitas? | **Confirmado** | **Não.** Exclusão de usuário será lógica e, no MVP, significará **desativação da conta**, mantendo dados e vínculos para auditoria. Receitas compartilhadas continuam consultáveis; novas alterações pelo usuário desativado ficam bloqueadas. |
| D-06 | O que ocorre com registros excluídos? | **Confirmado** | **Preservar para auditoria.** Usar exclusão lógica para contas, receitas e livros, registrando autoria, data e motivo, quando informado. Registros excluídos não aparecem nas listagens usuais, mas permanecem disponíveis aos mecanismos de auditoria autorizados. |

**Justificativa de escolhas que divergem de alternativas usuais:** receitas serão compartilhadas em vez de privadas por usuário; ingredientes serão locais à receita em vez de formar um catálogo global; o administrador não terá poder de editar receitas; a exclusão será lógica em vez de física. Essas escolhas privilegiam organização em cadernos, simplicidade do domínio e preservação do histórico. Não foi fornecida uma lista formal de recomendações da “seção 4” para confronto item a item; a tabela registra integralmente as seis decisões informadas.

## 5. Regras de negócio (RN)

### 5.1 Usuários e acesso

| ID | Regra | Status |
| --- | --- | --- |
| RN-USU-01 | Somente `ADMIN` cadastra, atualiza, redefine senha, ativa e desativa contas. | Confirmado |
| RN-USU-02 | Um usuário tem um perfil operacional `ADMIN` ou `USER`. A autorização deve ocorrer no backend, em todas as operações. | Proposto |
| RN-USU-03 | Somente contas ativas podem autenticar e acessar serviços protegidos; desativação invalida sessões e impede novas requisições autenticadas. | Proposto |
| RN-USU-04 | A exclusão de usuário é lógica e equivale à desativação no MVP. Não apaga receitas, livros, vínculos ou logs. | Confirmado |
| RN-USU-05 | A senha nunca é consultável em texto claro; redefinição substitui sua credencial, com registro do evento sem conteúdo da senha. | Proposto |
| RN-USU-06 | O identificador de login (proposta: e-mail) deve ser único entre usuários, inclusive os desativados. | Proposto |
| RN-USU-07 | `ADMIN` não consulta nem modifica receitas/livros por possuir o perfil administrativo; acesso administrativo limita-se a usuários, logs e configurações. | Confirmado |

### 5.2 Receitas

| ID | Regra | Status |
| --- | --- | --- |
| RN-REC-01 | A receita possui obrigatoriamente nome, descrição, tempo de preparo em minutos, número de porções e ingredientes. | Proposto com base no escopo fornecido |
| RN-REC-02 | Nome e descrição não podem ser vazios; tempo de preparo e porções devem ser positivos. | Proposto |
| RN-REC-03 | Todas as contas ativas com perfil `USER` podem consultar as receitas não excluídas logicamente. | Confirmado |
| RN-REC-04 | **Somente o autor pode editar ou excluir sua receita**; demais usuários podem apenas visualizar e incluí-la em livros próprios. | **Proposto — requer aprovação** |
| RN-REC-05 | Excluir receita efetua exclusão lógica, preserva seu histórico e os vínculos para auditoria, mas a oculta das consultas habituais. | Confirmado (preservação); detalhamento proposto |
| RN-REC-06 | Quando uma receita estiver em livros de outros usuários, a interface deverá alertar sobre o impacto antes da exclusão lógica. | Proposto |
| RN-REC-07 | A receita deverá registrar autor, datas de criação/atualização e, quando excluída, data e responsável pela exclusão. | Proposto |

### 5.3 Ingredientes e unidades de medida

| ID | Regra | Status |
| --- | --- | --- |
| RN-ING-01 | Cada ingrediente pertence a uma receita, sem catálogo global de ingredientes. | Confirmado |
| RN-ING-02 | Cada ingrediente tem nome, quantidade numérica decimal positiva e uma unidade de medida cadastrada. | Proposto com base no escopo fornecido |
| RN-ING-03 | Alterar/remover ingrediente é alteração da receita e exige permissão para editar a receita. | Proposto |
| RN-ING-04 | Somente `ADMIN` cadastra, altera ou desativa unidades de medida disponíveis para seleção. | Confirmado |
| RN-ING-05 | Unidade já utilizada por ingredientes não pode ser removida fisicamente; deve ser desativada para novos usos, preservando o histórico. | Proposto |
| RN-ING-06 | Unidade desativada permanece válida em receitas antigas, mas não pode ser selecionada ao adicionar ou trocar a unidade de um ingrediente. | Proposto |

### 5.4 Livros e associações

| ID | Regra | Status |
| --- | --- | --- |
| RN-LIV-01 | Livros são coleções pessoais: cada livro pertence a um `USER`, que pode criá-lo, editá-lo e excluí-lo logicamente. | Proposto com base no modelo de cadernos |
| RN-LIV-02 | Somente o proprietário consulta e gerencia os próprios livros; receitas neles referenciadas continuam compartilhadas para leitura. | **Proposto — requer aprovação** |
| RN-LIV-03 | Uma receita pode pertencer a zero, um ou vários livros; um livro pode conter várias receitas. | Confirmado |
| RN-LIV-04 | Um livro pode referenciar receitas de outros autores, desde que essas receitas estejam ativas e acessíveis. | Proposto |
| RN-LIV-05 | Não permitir vínculo duplicado entre a mesma receita e o mesmo livro. Desassociar não exclui a receita. | Proposto |
| RN-LIV-06 | Livros excluídos logicamente deixam de aparecer na navegação, mas seus registros e vínculos históricos são preservados. | Confirmado (preservação); detalhamento proposto |

### 5.5 Logs operacionais, segurança, auditoria, exclusão e configurações

#### 5.5.1 Categorias de registro e acesso administrativo

A separação é **lógica e obrigatória**, mesmo que a implementação compartilhe um coletor ou uma interface de consulta. Um erro técnico não deve ser apresentado como alteração de negócio, nem uma exclusão de receita apenas como mensagem de erro. O `ADMIN` pode **consultar, filtrar e exportar todas as três categorias**, respeitando sanitização e limites de acesso.

| Categoria | Finalidade | Exemplos que devem ser registrados | Acesso e exportação no MVP |
| --- | --- | --- | --- |
| **Operacional (`OPERACIONAL`)** | Diagnóstico, disponibilidade e comportamento da aplicação. | Falha de conexão com PostgreSQL (`DB_CONNECTION_ERROR`), exceção não tratada, indisponibilidade de dependência interna, erro de inicialização e falha de processamento relevante. | `ADMIN` consulta e exporta metadados técnicos e descrição sanitizada; **não** vê stack trace bruto nem segredos. |
| **Segurança (`SEGURANCA`)** | Detectar tentativas de violação e investigar autenticação/autorização. | Login malsucedido, acesso negado (`403`), tentativa de consulta de logs por `USER`, sessão inválida e validação de entrada suspeita. | `ADMIN` consulta e exporta resultados, motivos codificados e contexto mínimo; credenciais e dados pessoais desnecessários são omitidos. |
| **Auditoria (`AUDITORIA`)** | Reconstruir quem tentou ou efetivamente realizou operações de negócio críticas. | Edição/exclusão de receita ou livro, mudança de ingredientes, ativação/desativação de conta, redefinição de senha, alteração de unidades/configurações, leitura/exportação de logs. | `ADMIN` consulta e exporta ator, ação, alvo, resultado e alterações permitidas; eventos são preservados conforme retenção formal. |

**Classificação de exemplos:** `DB_CONNECTION_ERROR` é **operacional**; `RECIPE_DELETE` é **auditoria**; `ACCESS_DENIED` é **segurança**. Um mesmo fluxo pode gerar **dois eventos diferentes** quando cumprir finalidades distintas (por exemplo, uma falha operacional que também impediu alteração auditável), desde que cada entrada seja classificada e correlacionada, sem duplicação indiscriminada.

#### 5.5.2 Informações mínimas de cada registro

Os campos a seguir representam o **contrato documental mínimo** de consulta e exportação, não uma definição obrigatória de tabela ou ferramenta de observabilidade. Identificadores devem ser estáveis; timestamps usam ISO 8601 com UTC (`Z`).

| Abrangência | Campos mínimos | Observações |
| --- | --- | --- |
| **Comuns às três categorias** | `evento_id`, `ocorrido_em`, `categoria`, `tipo_evento`, `severidade`, `resultado`, `servico`, `ambiente`, `mensagem_segura`, `correlacao_id`, `origem_ator`, `ator_id` | `origem_ator` diferencia `USER`, `ADMIN`, `SISTEMA` e `NAO_IDENTIFICADO`. `ator_id` pode ser **nulo** se não existe usuário identificável. `correlacao_id` identifica a operação/requisição; para evento de inicialização sem requisição pode ser um ID gerado para o próprio fluxo. |
| **Operacional** | `componente`, `codigo_erro`, `contexto_tecnico_seguro` | Ex.: componente `database`, código `DB_CONNECTION_ERROR`, contexto `Falha ao estabelecer conexão`. HTTP status/rota-modelo e duração são adicionados **quando aplicáveis**; não registrar string de conexão nem SQL com parâmetros. |
| **Segurança** | `acao_tentada`, `motivo_codigo`, `recurso_alvo` (se identificado) | Ex.: `READ_LOGS`, `ROLE_NOT_ALLOWED`, `LOGS`. Origem de rede minimizada/mascarada apenas se necessária e permitida; falha de login pode não ter `ator_id`. |
| **Auditoria** | `acao`, `entidade_tipo`, `entidade_id`, `resultado`, `ator_id` ou identificador de serviço responsável | Em operação humana autenticada, `ator_id` é **obrigatório**. `campos_alterados` pode conter nomes de campos; valores antes/depois apenas quando não sensíveis e necessários para rastreabilidade. |

**Valores e formatos:** `severidade` segue vocabulário consistente (por exemplo, `INFO`, `WARN`, `ERROR`, `CRITICAL`); `resultado` diferencia `SUCESSO`, `FALHA` e `NEGADO`, de acordo com a categoria. Campos não aplicáveis devem ser explicitamente nulos ou ausentes segundo contrato de API definido na implementação, **nunca** inventados. Uma exclusão lógica deve indicar o recurso e a transição, não depender do conteúdo integral da receita no log.

#### 5.5.3 Regras de coleta, proteção e retenção

| ID | Regra | Status |
| --- | --- | --- |
| RN-LOG-01 | Classificar cada evento como `OPERACIONAL`, `SEGURANCA` ou `AUDITORIA`; definir códigos de evento estáveis e permitir correlação entre registros da mesma operação. | Proposto (correção P03) |
| RN-LOG-02 | No painel administrativo, permitir consulta e exportação das três categorias, usando os mesmos critérios de autorização, classificação e sanitização. Os filtros por usuário não devem ocultar eventos técnicos sem usuário. | Proposto (correção P03) |
| RN-LOG-03 | Registrar operações técnicas relevantes com origem de serviço, componente e código de erro seguros; a captura de falhas do PostgreSQL não pode depender **exclusivamente** do PostgreSQL. | Proposto (correção P03) |
| RN-LOG-04 | Registrar tentativas de leitura/exportação de logs e as exportações concluídas como ações auditáveis, sem criar registros recursivos sobre a gravação dos próprios eventos. | Proposto (correção P03) |
| RN-LOG-05 | Proibir em logs, interface e arquivos exportados senhas, hashes de senha, tokens, cookies, chaves, strings de conexão, parâmetros SQL sensíveis, corpos completos de requisições e dados pessoais desnecessários. Sanitizar dados de entrada para impedir injeção de logs. | Proposto, alinhado à OWASP |
| RN-LOG-06 | `USER` não pode consultar nem exportar qualquer categoria, inclusive via API. `ADMIN` tem acesso somente às versões sanitizadas. Operações administrativas não permitem editar ou excluir entradas individuais. | Confirmado (ADMIN/USER); detalhamento proposto |
| RN-LOG-07 | Registros de auditoria devem ser acrescentados sem permitir reescrita comum do histórico, com mecanismos de integridade e detecção de adulteração; acesso e exportação devem ser rastreáveis. | Proposto, alinhado à OWASP |
| RN-LOG-08 | Definir retenção, rotação e descarte por categoria antes da produção, considerando finalidade, obrigação legal, armazenamento disponível e preservação do histórico crítico. Não assumir retenção permanente para logs técnicos. | Proposto — prazo pendente |
| RN-LOG-09 | Uma falha de coleta de log operacional não deve comprometer por si só todas as operações de negócio; o impacto de indisponibilidade da trilha de auditoria em uma **alteração crítica** deve ser decidido explicitamente antes da implementação. | Proposto — política de falhas pendente |
| RN-AUD-01 | Auditar operações críticas: edição e exclusão lógica de receitas/livros, alteração/remoção de ingredientes, ativação/desativação de usuários, redefinição de senhas, alterações de unidades e configurações. | Confirmado (categorias); detalhamento proposto |
| RN-AUD-02 | Falhas de autenticação e tentativas de acesso negado pertencem à categoria `SEGURANCA`, não substituem a trilha de auditoria das mutações de negócio. | Proposto (correção P03) |
| RN-AUD-03 | Eventos de auditoria seguem os campos mínimos da seção 5.5.2. Devem permitir identificar o ator, ação, objeto, instante e resultado; valores sensíveis não são copiados para o histórico. | Proposto |
| RN-AUD-04 | `ADMIN` pode consultar, filtrar e exportar registros de auditoria sem editar/apagar entradas; `USER` não pode acessá-los. A mesma restrição vale para logs operacionais e de segurança. | Confirmado (acesso); detalhamento proposto |
| RN-AUD-05 | Exclusão lógica deve preservar identidade, histórico e referências; o estado de exclusão precisa estar explícito no banco. | Confirmado (preservação); detalhamento proposto |
| RN-AUD-06 | Exclusão ou edição de unidade em uso não deve comprometer registros históricos. Alterações de configurações também devem ser auditadas. | Proposto |

**Nota de segurança:** orientação de referência: [OWASP — Logging Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Logging_Cheat_Sheet.html) e [OWASP — Implement Security Logging and Monitoring](https://devguide.owasp.org/en/04-design/02-web-app-checklist/09-logging-monitoring/). A OWASP recomenda separar finalidades, manter atributos de **quando, onde, quem e o quê**, aplicar controle de acesso, proteger contra adulteração e limitar dados sensíveis. A solução tecnológica concreta será definida na etapa de arquitetura.

## 6. Critérios de aceitação (CA)

Os cenários seguem o formato **Dado / Quando / Então** e podem originar testes manuais, de integração ou E2E. Cenários dependentes de regra proposta serão confirmados antes da implementação.

| ID | Cenário | Dado / Quando / Então | Referências |
| --- | --- | --- | --- |
| CA-001 | **Sucesso — cadastro de receita** | **Dado** um `USER` ativo e autenticado, **quando** informar nome, descrição, tempo positivo, porções positivas e pelo menos um ingrediente válido, **então** a receita deve ser criada e consultável por outros usuários `USER` ativos. | RF-007, RF-008, RN-REC-01 |
| CA-002 | **Erro de validação — receita inválida** | **Dado** um `USER` autenticado, **quando** tentar salvar receita com tempo de preparo negativo, **então** a operação deve ser rejeitada com mensagem de validação e nenhum registro inválido deve ser persistido. | RF-007, RN-REC-02, RNF-013 |
| CA-003 | **Acesso não autorizado — edição alheia** | **Dado** um `USER` autenticado que não é autor de determinada receita, **quando** tentar editá-la ou excluí-la diretamente pela API, **então** a operação deve ser negada e os dados devem permanecer inalterados. **Sujeito à aprovação da RN-REC-04.** | RF-002, RF-019, RN-REC-04 |
| CA-004 | **Sucesso — livro com receita compartilhada** | **Dado** um `USER` com livro próprio e receita ativa criada por outro usuário, **quando** associar essa receita ao livro, **então** o vínculo deve ser salvo sem alterar a autoria da receita. | RF-010, RN-LIV-04 |
| CA-005 | **Validação — associação duplicada** | **Dado** uma receita já associada a um livro, **quando** tentar associá-la novamente ao mesmo livro, **então** o sistema deve impedir vínculo duplicado sem afetar a associação existente. | RF-010, RN-LIV-05 |
| CA-006 | **Acesso não autorizado — administrador** | **Dado** um `ADMIN` autenticado, **quando** tentar criar ou editar receita pela interface ou API, **então** o sistema deve impedir a operação. | RF-002, RN-USU-07 |
| CA-007 | **Sucesso — desativação de usuário** | **Dado** um `ADMIN` autenticado, **quando** desativar um usuário, **então** novas autenticações e requisições autenticadas desse usuário devem ser bloqueadas, mantendo seus registros e receitas armazenados. | RF-004, RF-005, RN-USU-03, RN-USU-04 |
| CA-008 | **Auditoria — operação crítica** | **Dado** um `USER` autorizado, **quando** editar uma receita, **então** o sistema deve salvar a edição e criar um evento `AUDITORIA` com ator, instante, ação, objeto, resultado e correlação, sem credenciais. | RF-013, RN-AUD-01, RN-AUD-03, RN-LOG-01 |
| CA-009 | **Acesso não autorizado — logs** | **Dado** um `USER` autenticado, **quando** tentar consultar ou exportar qualquer categoria de log, **então** a interface e a API devem negar o acesso, mantendo registro `SEGURANCA` da tentativa, sem expor o conteúdo dos logs. | RF-014, RF-016, RN-AUD-04, RN-LOG-06 |
| CA-010 | **Exclusão lógica — histórico preservado** | **Dado** uma receita existente, **quando** seu autor a excluir, **então** ela não deve aparecer nas buscas usuais, mas o registro e seu histórico devem continuar preservados para auditoria. | RF-007, RN-REC-05, RN-AUD-05 |
| CA-011 | **Unidade em uso** | **Dado** uma unidade de medida utilizada por uma receita, **quando** o `ADMIN` solicitar sua exclusão, **então** a unidade deve ser desativada para novos usos e as receitas antigas devem manter suas referências. | RF-018, RN-ING-05, RN-ING-06 |
| CA-012 | **Exportação** | **Dado** um `ADMIN` autenticado com filtro por categoria e período, **quando** exportar em CSV ou TXT, **então** o arquivo deve conter somente os registros autorizados correspondentes ao filtro, identificados por categoria e com os campos mínimos pertinentes, sem dados protegidos. | RF-015, RF-016, RN-LOG-02, RNF-009 |
| CA-013 | **Erro operacional — PostgreSQL** | **Dado** o serviço da aplicação em funcionamento, **quando** uma conexão com PostgreSQL falhar, **então** deve ser produzido um evento `OPERACIONAL` de código `DB_CONNECTION_ERROR`, com instante, serviço, componente, correlação, resultado e mensagem sanitizada, mesmo sem escrita no PostgreSQL. | RF-013, RN-LOG-03, RNF-016 |
| CA-014 | **Consulta diferenciada — auditoria vs. operação** | **Dado** um `ADMIN` autenticado e dois eventos (`RECIPE_DELETE` e `DB_CONNECTION_ERROR`), **quando** filtrar por `AUDITORIA`, **então** verá a exclusão e não a falha técnica; ao filtrar por `OPERACIONAL`, verá a falha e não a exclusão. | RF-014, RF-015, RN-LOG-01, RN-LOG-02 |
| CA-015 | **Segurança — tentativa negada** | **Dado** um `USER` autenticado, **quando** tentar consultar logs diretamente pela API, **então** receberá acesso negado e haverá um evento `SEGURANCA` contendo a ação tentada, o motivo codificado e o resultado `NEGADO`, sem conteúdo de log. | RF-013, RN-AUD-02, RN-LOG-06 |
| CA-016 | **Proteção dos registros exportados** | **Dado** um `ADMIN` autenticado, **quando** consultar ou exportar um evento de falha de banco, **então** o resultado não deve conter senha, hash, token, string de conexão, parâmetros SQL sensíveis ou stack trace bruto. | RF-014, RF-016, RN-LOG-05, RNF-009 |
| CA-017 | **Rastreabilidade — exportação de registros** | **Dado** um `ADMIN` autenticado, **quando** concluir a exportação de logs, **então** a operação deve gerar evento `AUDITORIA` com ator, ação `EXPORT_LOGS`, instante, resultado e referência aos filtros, sem incluir o conteúdo exportado. | RF-013, RF-016, RN-LOG-04 |

## 7. Fora do escopo da primeira versão

- Execução pública na internet, cadastro autônomo de usuários e autenticação por provedores externos (Google, etc.).
- Compartilhamento público por link ou publicação de livros para usuários não autenticados.
- Permissões granulares de colaboração/edição coletiva de receitas, compartilhamento de livros e transferência de autoria.
- Catálogo global de ingredientes, estoque, preços, compras ou controle de despensa.
- Recomendações automáticas, IA, cálculo nutricional e planejamento alimentar.
- Upload/armazenamento de fotos ou vídeos, importação por URL e exportação em PDF dos livros.
- Versionamento editorial de receitas e restauração de versões antigas pela interface.
- Aplicativos móveis nativos e funcionamento offline no dispositivo do usuário (diferente de operar sem internet **externa** na rede interna).
- Eliminação física automática de registros ou logs e ferramentas de expurgo definitivo sem política formal de retenção.

## 8. Pontos de atenção e decisões pendentes

1. **Edição de receitas compartilhadas — principal risco:** o compartilhamento foi confirmado, mas não quem pode modificar receitas de terceiros. **Proposta para o MVP:** qualquer `USER` ativo lê; somente o autor edita/exclui. Editar livremente por todos exigiria estratégia de conflitos e auditoria mais rigorosa.
2. **Visibilidade dos livros:** receitas são compartilhadas, porém não foi definido se livros também são. **Proposta:** livros pessoais, visíveis apenas ao proprietário; uma receita de qualquer autor pode ser usada nesses livros.
3. **Desativação do autor:** suas receitas continuam visíveis, mas não poderão ser editadas por terceiros na regra proposta. **Consequência:** ficam congeladas até reativação ou futura funcionalidade de transferência de autoria.
4. **Exclusão de receita usada em livros alheios:** exclusão lógica global pode tornar a receita indisponível nesses livros. **Proposta:** aviso explícito antes da exclusão; manter vínculos históricos para auditoria. Avaliar futuramente arquivamento de receita ou cópia independente.
5. **Campos opcionais e limites:** tamanho máximo de nomes/descrições, possibilidade de receita sem ingredientes durante rascunho e lista inicial de unidades precisam de definição. **Padrão proposto para publicação:** ao menos um ingrediente; rascunhos não entram no MVP.
6. **Operação e compliance:** definir retenção/rotação **por categoria de registro**, backup (frequência, RPO/RTO), estratégia de restauração e desempenho esperado antes de homologar a produção.
7. **Configurações da aplicação:** limitar inicialmente o painel à gestão de unidades de medida e a parâmetros seguros definidos em lista aprovada; impedir edição arbitrária de configurações sensíveis.
8. **Política de falha da auditoria:** decidir se uma alteração crítica deve ser **rejeitada** caso não seja possível persistir sua trilha de auditoria (preferência para integridade), ou se haverá fila durável e reconciliação comprovável. A decisão deve preceder o desenho transacional.
9. **Operação do coletor de logs:** definir destino interno independente do PostgreSQL para logs operacionais, proteção contra lotação do armazenamento e indisponibilidade do painel; solução técnica não aprovada nesta etapa.

## 9. Critério de conclusão do levantamento

Este documento pode ser usado como base para planejamento, estimativa e desenho técnico após validação das regras identificadas como **Proposto — requer aprovação**, em especial autoria/edição de receitas e visibilidade dos livros. Implementação de código e criação do banco de dados **não** fazem parte desta entrega.
