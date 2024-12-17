package banco;

public class Main {
    public static void main(String[] args) {
        String URL = "jdbc:postgresql://localhost:5432/bduser"; // URL do banco
        String USER = "postgres"; // Usuário do banco
        String PASSWORD = "123456"; // Senha do banco
        Db banco = new Db(URL, USER, PASSWORD);

        // banco.insertClient("Gabriel", "06879834108", "61981086769", "72880000");
        banco.deletarCliente("06879834108");
        banco.fecharConexao();
    }
}
