package com.diego.prandini.exerciciotecnicoelotech.infra.controller.pessoa;

import com.diego.prandini.exerciciotecnicoelotech.application.pessoa.AlterarPessoa;
import com.diego.prandini.exerciciotecnicoelotech.application.pessoa.BuscarPessoa;
import com.diego.prandini.exerciciotecnicoelotech.application.pessoa.CriarPessoa;
import com.diego.prandini.exerciciotecnicoelotech.application.pessoa.ListarPessoas;
import com.diego.prandini.exerciciotecnicoelotech.application.pessoa.RemoverPessoa;
import com.diego.prandini.exerciciotecnicoelotech.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/pessoas")
@RequiredArgsConstructor
public class PessoaController {

    private final ListarPessoas listarPessoas;
    private final BuscarPessoa buscarPessoa;
    private final CriarPessoa criarPessoa;
    private final AlterarPessoa alterarPessoa;
    private final RemoverPessoa removerPessoa;

    @GetMapping
    public ResponseEntity<ListarPessoas.Output> listar(
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "15") Integer size,
            @RequestParam(name = "nome", required = false, defaultValue = "") String nome,
            @RequestParam(name = "cpf", required = false, defaultValue = "") String cpf
    ) {
        ListarPessoas.PaginationInput paginationInput = new ListarPessoas.PaginationInput(page, size);
        ListarPessoas.FilterInput filterInput = new ListarPessoas.FilterInput(nome, cpf);
        ListarPessoas.Input input = new ListarPessoas.Input(paginationInput, filterInput);
        ListarPessoas.Output output = listarPessoas.execute(input);
        return ResponseEntity.ok(output);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BuscarPessoa.Output> buscar(@PathVariable UUID id) {
        BuscarPessoa.Output output = buscarPessoa.execute(id);
        return ResponseEntity.ok(output);
    }

    @PostMapping
    public ResponseEntity<CriarPessoa.Output> criar(@RequestBody CriarPessoa.Input input) {
        CriarPessoa.Output output = criarPessoa.execute(input);
        return ResponseEntity.created(ApiUtils.createUri(output.id(), "/pessoas")).body(output);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlterarPessoa.Output> alterar(@PathVariable UUID id, @RequestBody AlterarPessoa.Input input) {
        AlterarPessoa.Output output = alterarPessoa.execute(id, input);
        return ResponseEntity.ok(output);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable UUID id) {
        removerPessoa.execure(id);
        return ResponseEntity.noContent().build();
    }
}
