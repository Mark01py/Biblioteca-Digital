import static spark.Spark.*;
import com.google.gson.Gson;

public class Api {
    public static void main(String[] args) {
        port(4567);
        staticFiles.location("/public"); // HTML/CSS/JS em resources/public

        BibliotecaDigital bd = new BibliotecaDigital();
        Gson gson = new Gson();

        get("/livros", (req, res) -> {
            res.type("application/json");
            return gson.toJson(bd.listarTodosLivros());
        });

        post("/livros", (req, res) -> {
            Livro livro = gson.fromJson(req.body(), Livro.class);
            bd.adicionarLivro(livro);
            return "Livro adicionado com sucesso!";
        });

        get("/livros/titulo/:titulo", (req, res) -> {
            res.type("application/json");
            return gson.toJson(bd.buscarPorTitulo(req.params(":titulo")));
        });

        get("/livros/autor/:autor", (req, res) -> {
            res.type("application/json");
            return gson.toJson(bd.buscarPorAutor(req.params(":autor")));
        });

        get("/livros/:titulo/lido", (req, res) -> {
            res.type("application/json");
            boolean status = bd.verificarStatusLeitura(req.params(":titulo"));
            return gson.toJson(new StatusResponse(req.params(":titulo"), status));
        });

        put("/livros/:titulo/lido/:status", (req, res) -> {
            boolean status = Boolean.parseBoolean(req.params(":status"));
            bd.marcarComoLido(req.params(":titulo"), status);
            return "Status atualizado com sucesso!";
        });

        delete("/livros/:titulo", (req, res) -> {
            String titulo = req.params(":titulo");
            boolean removido = bd.removerLivro(titulo);
            if (removido) {
                return "Livro removido com sucesso!";
            } else {
                res.status(404);
                return "Livro não encontrado.";
            }
        });

        System.out.println("Rodando em: http://localhost:4567/");
    }
}

class StatusResponse {
    private String titulo;
    private boolean lido;

    public StatusResponse(String titulo, boolean lido) {
        this.titulo = titulo;
        this.lido = lido;
    }

    public String getTitulo() { return titulo; }
    public boolean isLido() { return lido; }
}
