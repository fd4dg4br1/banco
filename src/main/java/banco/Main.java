package banco;

import java.sql.Timestamp;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        String URL = "jdbc:postgresql://localhost:5432/bduser"; // URL do banco
        String USER = "postgres"; // Usuário do banco
        String PASSWORD = "123456"; // Senha do banco
        String consultas;
        Db banco = new Db(URL, USER, PASSWORD);
        banco.insertClient("s", "12345678912", "61999999999", "134245341");

        banco.insertConsult("s", "10/12/2024 10:12", "12345678912");
        banco.insertConsult("s", "15/12/2024 10:10", "12345678912");

        System.out.println(banco.verificarPessoaDB("12345678912"));

        System.out.println(banco.verificarPessoaConsultas("12345678912"));

        System.out.println(banco.procurarPessoa("12345678912"));

        Map<String, List<List<String>>> map = banco.procurarConsultas("12345678912");

        for (Map.Entry<String, List<List<String>>> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
        System.out.println();

        // banco.deletarCliente("12345678912");
        banco.fecharConexao();
    }
}
