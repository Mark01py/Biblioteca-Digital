public class Livro {
    private String titulo;
    private String autor;
    private int ano;
    private boolean lido;

    public Livro(String titulo, String autor, int ano) {
        this.titulo = titulo;
        this.autor = autor;
        this.ano = ano;
        this.lido = false;
    }

    // Getters e Setters
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public int getAno() { return ano; }
    public boolean isLido() { return lido; }
    public void setLido(boolean lido) { this.lido = lido; }

    @Override
    public String toString() {
        String status = lido ? "Lido" : "Não Lido";
        return titulo + " - " + autor + " (" + ano + ") [" + status + "]";
    }
}