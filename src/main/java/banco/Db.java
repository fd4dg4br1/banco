package banco;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Db {

    private Connection con = null;
    private PreparedStatement sqlCli = null, sqlCon = null;

    public Db(String url, String user, String password) {
        conectar(url, user, password);
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

            sqlCon.setString(1, datahora);
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

    protected void deletarCliente(String cpf) {
        String cliente = "DELETE FROM cliente WHERE cpf = ?";
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
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

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
