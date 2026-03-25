package com.example;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.neo4j.driver.Values;

public class Main {

    public static void main(String[] args) {
        String arquivoCaminho = "stress_test.txt";

        // Configuração da conexão
        Driver driver = GraphDatabase.driver("bolt://localhost:7687",
                AuthTokens.basic("neo4j", "senha123"));

        // Usamos APENAS UMA session
        try (Session session = driver.session()) {
            System.out.println("🚀 Iniciando motor de ingestão BlastRadius-Zero em LOTE...");
            
            AtomicInteger contador = new AtomicInteger(0);

            // executeWrite agrupa tudo em uma única transação gigante (Muito mais rápido!)
            session.executeWrite(tx -> {
                try (Stream<String> linhas = Files.lines(Paths.get(arquivoCaminho))) {
                    linhas.forEach(linha -> {
                        String[] partes = linha.split(":");
                        if (partes.length < 4) return; 

                        String tipo = partes[0];
                        
                        if (tipo.equals("USER")) {
                            tx.run("MERGE (u:Usuario {nome: $nome, uid: $uid}) " +
                                   "MERGE (g:Grupo {nome: $gNome}) " +
                                   "MERGE (u)-[:PERTENCE_A]->(g)",
                                Values.parameters("nome", partes[1], "uid", partes[2], "gNome", partes[3]));
                        } 
                        else if (tipo.equals("ACCESS")) {
                            tx.run("MERGE (origem {nome: $o}) " +
                                   "MERGE (alvo {nome: $a}) " +
                                   "MERGE (origem)-[:" + partes[3] + "]->(alvo)",
                                Values.parameters("o", partes[1], "a", partes[2]));
                        }

                        // Feedback visual de progresso
                        int atual = contador.incrementAndGet();
                        if (atual % 5000 == 0) {
                            System.out.println("⏳ " + atual + " registros processados...");
                        }
                    });
                } catch (Exception e) {
                    throw new RuntimeException("Erro ao ler o arquivo: " + e.getMessage());
                }
                return null;
            });

            System.out.println("🏁 Ingestão concluída com sucesso!");

        } catch (Exception e) {
            System.err.println("❌ Erro crítico no motor: " + e.getMessage());
            e.printStackTrace();
        } finally {
            driver.close();
        }
    }
}