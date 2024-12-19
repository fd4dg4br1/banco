package banco;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Db {

    private Connection con = null;
    private PreparedStatement sqlCli = null, sqlCon = null;
    private static int num;

    public Db(String url, String user, String password) {
        conectar(url, user, password);
        num = 0;
    }

    public int index(boolean qtIndex) {
        // se for falso, eu somo mais um indice
        if (qtIndex = false) {
            num += 1;
            return num;
        }
        // agora se for verdadeiro, ele mostra em qual indice está
        return num;
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
        String consulta = "INSERT INTO cliente(datahora,nome,cpf) VALUES(?,?,?)";

        try {
            sqlCon = con.prepareStatement(consulta);

            LocalDateTime localDateTime = LocalDateTime.parse(datahora,
                    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

            Timestamp timestamp = Timestamp.valueOf(localDateTime);

            sqlCon.setTimestamp(index(false), timestamp);
            sqlCon.setString(index(false), nome);
            sqlCon.setString(index(false), cpf);

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

    // Arruma pra quando deletar alguem, ser deletado as consultas dele
    protected void deletarCliente(String cpf) {
        String cliente = "DELETE FROM cliente WHERE cpf = ?";
        String consultCliente = "SELECT cliente.nome,consulta.datahora FROM consulta INNER JOIN cliente ON cliente.cpf = consulta.cpf WHERE cliente.cpf = ?";
        try {
            sqlCon = con.prepareStatement(consultCliente);
            sqlCon.setString(1, consultCliente);
            ResultSet aux = sqlCon.executeQuery();

            if (aux.next()) {

                sqlCli = con.prepareStatement(cliente);
                sqlCli.setString(1, cpf);
                int num = sqlCli.executeUpdate();
                System.out.printf("Deletado: (%d) || %s \n", num, cpf);

            } else {
                System.out.println("Não existe:" + cpf + " no banco de dados.");
            }
            aux.close();
            sqlCon.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (sqlCli != null) {
                    sqlCli.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    // fazer o agendamento da consulta
    protected void agendarConsulta() {

    }

    // verificar se existe a pessoa no banco de dados
    protected boolean verificarPessoaDB(String cpf) {
        String procurarPessoa = "SELECT nome FROM clientes WHERE cpf = ?";
        try {

            sqlCli = con.prepareStatement(procurarPessoa);
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
        String procurarConsultas = "SELECT consulta.cpf FROM cliente INNER JOIN consulta ON cliente.cpf = consulta.cpf WHERE cliente.cpf = ? ";
        try {
            sqlCli = con.prepareStatement(procurarConsultas);
            sqlCli.setString(1, procurarConsultas);
            ResultSet aux = sqlCli.executeQuery();
            if (aux.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    protected void conectar(String url, String user, String password) {
        try {
            con = DriverManager.getConnection(url, user, password);
            System.out.println("Conexão bem-sucedida!");
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
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
