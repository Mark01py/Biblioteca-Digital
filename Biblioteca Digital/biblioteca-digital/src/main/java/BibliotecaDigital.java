import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BibliotecaDigital {

    private MongoDatabase database;

    public BibliotecaDigital() {
        Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);
        
        try {
            MongoClientURI uri = new MongoClientURI(
                "mongodb+srv://Biblioteca:9ie0U6cH2GXN8Dsw@mongodb.5nasz.mongodb.net/" +
                "Biblioteca?retryWrites=true&w=majority&connectTimeoutMS=5000" +
                "&socketTimeoutMS=5000&serverSelectionTimeoutMS=5000"
            );
            
            MongoClient mongoClient = new MongoClient(uri);
            this.database = mongoClient.getDatabase("Biblioteca");
            database.listCollectionNames().first();
            
        } catch (Exception e) {
            System.err.println("Erro ao conectar ao MongoDB: " + e.getMessage());
            throw new RuntimeException("Falha na conexão com o banco de dados", e);
        }
    }

    public void adicionarLivro(Livro livro) {
        try {
            MongoCollection<Document> colecao = database.getCollection("livros");
            Document doc = new Document("titulo", livro.getTitulo())
                    .append("autor", livro.getAutor())
                    .append("ano", livro.getAno())
                    .append("lido", livro.isLido());
            colecao.insertOne(doc);
        } catch (Exception e) {
            System.err.println("Erro ao adicionar livro: " + e.getMessage());
            throw e;
        }
    }

    public void adicionarLivros(List<Livro> livros) {
        try {
            MongoCollection<Document> colecao = database.getCollection("livros");
            for (Livro livro : livros) {
                Document doc = new Document("titulo", livro.getTitulo())
                        .append("autor", livro.getAutor())
                        .append("ano", livro.getAno())
                        .append("lido", livro.isLido());
                colecao.insertOne(doc);
            }
        } catch (Exception e) {
            System.err.println("Erro ao adicionar lista de livros: " + e.getMessage());
            throw e;
        }
    }

    public void marcarComoLido(String titulo, boolean lido) {
        try {
            MongoCollection<Document> colecao = database.getCollection("livros");
            Document filtro = new Document("titulo", titulo);
            Document atualizacao = new Document("$set", new Document("lido", lido));
            colecao.updateOne(filtro, atualizacao);
        } catch (Exception e) {
            System.err.println("Erro ao atualizar status do livro: " + e.getMessage());
            throw e;
        }
    }

    public boolean verificarStatusLeitura(String titulo) {
        try {
            MongoCollection<Document> colecao = database.getCollection("livros");
            Document filtro = new Document("titulo", titulo);
            Document livroDoc = colecao.find(filtro).first();
            return livroDoc != null && livroDoc.getBoolean("lido");
        } catch (Exception e) {
            System.err.println("Erro ao verificar status do livro: " + e.getMessage());
            throw e;
        }
    }

    public List<Livro> listarTodosLivros() {
        List<Livro> livros = new ArrayList<>();
        try {
            MongoCollection<Document> colecao = database.getCollection("livros");
            FindIterable<Document> documentos = colecao.find();
            
            for (Document doc : documentos) {
                Livro livro = new Livro(
                    doc.getString("titulo"),
                    doc.getString("autor"),
                    doc.getInteger("ano")
                );
                livro.setLido(doc.getBoolean("lido", false));
                livros.add(livro);
            }
        } catch (Exception e) {
            System.err.println("Erro ao listar livros: " + e.getMessage());
            throw e;
        }
        return livros;
    }

    public List<Livro> buscarPorTitulo(String tituloBusca) {
        List<Livro> livros = new ArrayList<>();
        try {
            MongoCollection<Document> colecao = database.getCollection("livros");
            Pattern regex = Pattern.compile(tituloBusca, Pattern.CASE_INSENSITIVE);
            Document filtro = new Document("titulo", regex);
            FindIterable<Document> documentos = colecao.find(filtro);
            
            for (Document doc : documentos) {
                Livro livro = new Livro(
                    doc.getString("titulo"),
                    doc.getString("autor"),
                    doc.getInteger("ano")
                );
                livro.setLido(doc.getBoolean("lido", false));
                livros.add(livro);
            }
        } catch (Exception e) {
            System.err.println("Erro ao buscar por título: " + e.getMessage());
            throw e;
        }
        return livros;
    }

    public List<Livro> buscarPorAutor(String autorBusca) {
        List<Livro> livros = new ArrayList<>();
        try {
            MongoCollection<Document> colecao = database.getCollection("livros");
            Pattern regex = Pattern.compile(autorBusca, Pattern.CASE_INSENSITIVE);
            Document filtro = new Document("autor", regex);
            FindIterable<Document> documentos = colecao.find(filtro);
            
            for (Document doc : documentos) {
                Livro livro = new Livro(
                    doc.getString("titulo"),
                    doc.getString("autor"),
                    doc.getInteger("ano")
                );
                livro.setLido(doc.getBoolean("lido", false));
                livros.add(livro);
            }
        } catch (Exception e) {
            System.err.println("Erro ao buscar por autor: " + e.getMessage());
            throw e;
        }
        return livros;
    }

    public boolean removerLivro(String titulo) {
        try {
            MongoCollection<Document> colecao = database.getCollection("livros");
            Document filtro = new Document("titulo", titulo);
            long resultado = colecao.deleteOne(filtro).getDeletedCount();
            return resultado > 0;
        } catch (Exception e) {
            System.err.println("Erro ao remover livro: " + e.getMessage());
            return false;
        }
    }
}