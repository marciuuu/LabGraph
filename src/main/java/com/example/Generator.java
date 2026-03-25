package com.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Generator {
    public static void main(String[] args) {
        String arquivoNome = "stress_test.txt";
        int totalLinhas = 50000;
        Random random = new Random();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivoNome))) {
            System.out.println("🌪️ Gerando 50.000 linhas de caos controlado...");

            for (int i = 0; i < totalLinhas; i++) {
                // Alterna entre criar usuários e acessos aleatórios
                if (i == 25000) {
                    // INSERINDO A VULNERABILIDADE (A agulha no palheiro)
                    writer.write("USER:apache:48:web\n");
                    writer.write("ACCESS:web:backup.sh:PODE_EXECUTAR\n");
                    writer.write("ACCESS:backup.sh:/etc/shadow:PODE_ESCREVER\n");
                } else {
                    int id = random.nextInt(100000);
                    writer.write("USER:user" + id + ":" + id + ":group" + id + "\n");
                    writer.write("ACCESS:group" + id + ":file" + id + ".log:LEITURA\n");
                }
            }
            System.out.println("✅ Arquivo 'stress_test.txt' gerado com sucesso!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}