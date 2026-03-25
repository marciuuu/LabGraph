# 🔬 Protótipo de Validação

> **Nota de Desenvolvimento:** Este repositório contém o código-fonte experimental e o ambiente de testes (PoC) para o projeto. O foco aqui é a **validação técnica da ingestão e modelagem de grafos**, e não a entrega de um software finalizado.

## 📌 Visão Geral
Este protótipo foca em resolver o gargalo de performance na transição de dados lineares (logs/permissões) para estruturas relacionais em grafos. O objetivo é testar os limites do driver Java do Neo4j e a eficiência das operações de escrita em lote (*Batch Writing*).

## 🚀 O que este protótipo valida?
- **Ingestão em Lote:** Uso do método `session.executeWrite` para minimizar o overhead de transações.
- **Modelagem de Identidade:** Conversão de strings brutas em nós e relacionamentos no Neo4j.
- **Stress Test:** Capacidade de processamento de volumes massivos de dados (100k+ registros) sem vazamento de memória.

## 🛠️ Stack de Testes
- **Java 17+**: Uso de *Streams* e *AtomicInteger* para controle de fluxo.
- **Neo4j Driver**: Conexão via protocolo Bolt.
- **Data Source**: Arquivos `.txt` gerados para simulação de carga.

## 📈 Resultados Atuais
O motor já demonstra estabilidade na criação de topologias complexas.

## 🏗️ Estrutura do Rascunho
- `Main.java`: Ponto de entrada que orquestra a leitura e o envio para o Neo4j.
- `Generator.java`: (Opcional) Script para gerar massa de dados sintética para o stress test.
- `stress_test.txt`: Base de dados crua para os testes de volumetria.

## ⚠️ Avisos Importantes
1. **Ambiente Controlado:** O código assume uma instância local do Neo4j rodando via Docker na porta padrão (7687).
2. **Segurança:** As credenciais no código são apenas para o ambiente de dev local. Em produção, utilizaremos variáveis de ambiente (`.env`).
3. **Refatoração:** Por ser um rascunho de pesquisa, a arquitetura de classes ainda está em fase de refinamento (voltada para o TG).
