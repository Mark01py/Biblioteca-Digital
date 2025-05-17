function adicionarLivro() {
  const titulo = document.getElementById("titulo").value;
  const autor = document.getElementById("autor").value;
  const ano = parseInt(document.getElementById("ano").value);

  fetch("/livros", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ titulo, autor, ano })
  })
    .then(res => res.text())
    .then(alert);
}

function listarLivros() {
  fetch("/livros")
    .then(res => res.json())
    .then(data => {
      const div = document.getElementById("livros");
      div.innerHTML = data.map(l => `
        <div class="livro">
          <strong>${l.titulo}</strong> - ${l.autor} (${l.ano})<br />
          Status: ${l.lido ? "✅ Lido" : "❌ Não Lido"}
        </div>
      `).join("");
    });
}


function buscarPorTitulo() {
  const titulo = document.getElementById("buscaTitulo").value;
  fetch(`/livros/titulo/${titulo}`)
    .then(res => res.json())
    .then(data => {
      const div = document.getElementById("resultadoTitulo");
      if (data.length === 0) {
        div.innerHTML = "Nenhum livro encontrado.";
      } else {
        div.innerHTML = data.map(l => `<div><strong>${l.titulo}</strong> - ${l.autor} (${l.ano})</div>`).join("");
      }
    });
}

function buscarPorAutor() {
  const autor = document.getElementById("buscaAutor").value;
  fetch(`/livros/autor/${autor}`)
    .then(res => res.json())
    .then(data => {
      const div = document.getElementById("resultadoAutor");
      if (data.length === 0) {
        div.innerHTML = "Nenhum livro encontrado.";
      } else {
        div.innerHTML = data.map(l => `<div><strong>${l.titulo}</strong> - ${l.autor} (${l.ano})</div>`).join("");
      }
    });
}

function marcarComoLido() {
  const titulo = document.getElementById("tituloLido").value;
  const status = document.getElementById("statusLido").value;
  fetch(`/livros/${titulo}/lido/${status}`, { method: "PUT" })
    .then(res => res.text())
    .then(alert);
}

function verificarStatus() {
  const titulo = document.getElementById("tituloStatus").value;
  fetch(`/livros/${titulo}/lido`)
    .then(res => res.json())
    .then(data => {
      document.getElementById("statusResultado").innerText =
        `${data.titulo} está ${data.lido ? "✅ Lido" : "❌ Não Lido"}`;
    });
}
