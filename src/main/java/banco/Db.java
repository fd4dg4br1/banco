package banco;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Db {

    private Connection con = null;
    private PreparedStatement sqlCli = null, sqlCon = null;
    private static int num;

    public Db(String url, String user, String password) {
        conectar(url, user, password);
        num = 0;
    }

    public void insertClient(String nome, String cpf, String telefone, String endereco) {
        String cliente = "INSERT INTO cliente(nome,cpf,telefone,endereco) VALUES(?,?,?,?)";

        try {
            sqlCli = con.prepareStatement(cliente);

            sqlCli.setString(1, nome);
            sqlCli.setString(2, cpf);
            sqlCli.setString(3, telefone);
            sqlCli.setString(4, endereco);

            sqlCli.executeUpdate();
            System.out.println("Dados inseridos.");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {

                if (sqlCli != null)
                    sqlCli.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void insertConsult(String nome, String datahora, String cpf) {
        String consulta = "INSERT INTO consulta(datahora,nome,cpf) VALUES(?,?,?)";

        try {
            sqlCon = con.prepareStatement(consulta);

            LocalDateTime localDateTime = LocalDateTime.parse(datahora,
                    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

            Timestamp timestamp = Timestamp.valueOf(localDateTime);

            sqlCon.setTimestamp(1, timestamp);
            sqlCon.setString(2, nome);
            sqlCon.setString(3, cpf);

            sqlCon.executeUpdate();
            System.out.println("Dados inseridos.");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (sqlCon != null) {
                    sqlCon.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // fazer o agendamento da consulta
    // protected void insertConsult(String nome, Timestamp datahora, String cpf) {

    // String agendar = "INSERT INTO consulta(nome,cpf,datahora) VALUES(?,?,?)";
    // try {
    // sqlCon = con.prepareStatement(agendar);
    // sqlCon.setString(1, nome);
    // sqlCon.setString(2, cpf);
    // sqlCon.setTimestamp(3, datahora);

    // sqlCon.executeUpdate();

    // System.out.println("Consulta agendada!");
    // sqlCon.close();
    // } catch (SQLException e) {
    // e.printStackTrace();
    // }

    // }

    // verificar se existe a pessoa no banco de dados
    protected boolean verificarPessoaDB(String cpf) {
        String procurarPessoa = "SELECT 1 FROM cliente WHERE cpf = ?";
        try {

            sqlCli = con.prepareStatement(procurarPessoa);
            sqlCli.setString(1, cpf);
            ResultSet aux = sqlCli.executeQuery();
            if (aux.next()) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    protected boolean verificarPessoaConsultas(String cpf) {
        String procurarConsultas = "SELECT 1 FROM cliente INNER JOIN consulta ON cliente.cpf = consulta.cpf WHERE cliente.cpf = ? ";
        try {
            sqlCon = con.prepareStatement(procurarConsultas);
            sqlCon.setString(1, cpf);
            ResultSet aux = sqlCon.executeQuery();
            if (aux.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    protected Map procurarPessoa(String cpf) {
        Map<String, List<String>> pessoa = new HashMap<>();
        String procurar = "SELECT nome,telefone,endereco FROM cliente WHERE cpf = ?";
        try {
            sqlCli = con.prepareStatement(procurar);
            sqlCli.setString(1, cpf);
            ResultSet resultado = sqlCli.executeQuery();

            if (resultado.next()) {
                pessoa.put(cpf, List.of(resultado.getString("nome"), resultado.getString("telefone"),
                        resultado.getString("endereco")));
            }
            resultado.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pessoa;
    }

    protected Map<String, List<List<String>>> procurarConsultas(String cpf) {
        String procurar = "SELECT nome, datahora FROM consulta WHERE cpf = ?";
        Map<String, List<List<String>>> consultas = new HashMap<>();
        try {
            sqlCon = con.prepareStatement(procurar);
            sqlCon.setString(1, cpf);
            ResultSet aux = sqlCon.executeQuery();
            List<List<String>> listaMaior = new ArrayList<>();
            while (aux.next()) {
                String nome = aux.getString("nome");
                String datahora = aux.getString("datahora");
                List<String> lista = new ArrayList<>();
                lista.add(nome);
                lista.add(datahora);
                listaMaior.add(lista);
                consultas.put(cpf, listaMaior);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return consultas;
    }

    protected void deletarCliente(String cpf) {
        String cliente = "DELETE FROM cliente WHERE cpf = ?";
        // String consultCliente = "SELECT cliente.nome,consulta.datahora FROM consulta
        // INNER JOIN cliente ON cliente.cpf = consulta.cpf WHERE cliente.cpf = ?";
        try {
            sqlCli = con.prepareStatement(cliente);
            sqlCli.setString(1, cpf);
            int num = sqlCli.executeUpdate();
            System.out.printf("Deletado: (%d) || %s \n", num, cpf);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (sqlCli != null) {
                    sqlCli.close();
                    sqlCon.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    protected Connection conectar(String url, String user, String password) {
        try {
            con = DriverManager.getConnection(url, user, password);
            System.out.println("Conexão bem-sucedida!");

            return con;
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
        return null;
    }

    public void fecharConexao() {
        try {
            if (con != null)
                con.close();
            System.out.println("Conexão fechada.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
