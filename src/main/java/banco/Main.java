package banco;

public class Main {
    public static void main(String[] args) {
        System.out.println("seila");
        String URL = "jdbc:postgresql://localhost:5432/bduser"; // URL do banco
        String USER = "postgres"; // Usuário do banco
        String PASSWORD = "123456"; // Senha do banco
        Db banco = new Db(URL, USER, PASSWORD);

        // banco.insertClient("", "", "", "");
        banco.deletarCliente("");
        banco.fecharConexao();
    }
}
