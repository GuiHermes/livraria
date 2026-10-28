package com.example.livraria.controller;

import com.example.livraria.model.Livro;
import com.example.livraria.repository.LivroRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/livros")
public class LivroController {

	private final LivroRepository livroRepository;

	public LivroController(LivroRepository livroRepository) {
		this.livroRepository = livroRepository;
	}

	@PostMapping
	public ResponseEntity<Livro> criarLivro(@RequestBody Livro livro) {
		livro.setId(null);
		Livro livroSalvo = livroRepository.save(livro);
		return ResponseEntity.status(HttpStatus.CREATED).body(livroSalvo);
	}

	@GetMapping
	public List<Livro> listarLivros() {
		return livroRepository.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Livro> buscarLivroPorId(@PathVariable Long id) {
		return livroRepository.findById(id)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PutMapping("/{id}")
	public ResponseEntity<Livro> atualizarLivro(@PathVariable Long id, @RequestBody Livro dadosLivro) {
		return livroRepository.findById(id)
				.map(livro -> {
					livro.setTitulo(dadosLivro.getTitulo());
					livro.setAutor(dadosLivro.getAutor());
					livro.setCodigoBarras(dadosLivro.getCodigoBarras());
					livro.setAnoPublicacao(dadosLivro.getAnoPublicacao());
					return ResponseEntity.ok(livroRepository.save(livro));
				})
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletarLivro(@PathVariable Long id) {
		if (!livroRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}

		livroRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
}
