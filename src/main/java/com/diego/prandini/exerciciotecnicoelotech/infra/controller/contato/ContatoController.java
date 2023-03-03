package com.diego.prandini.exerciciotecnicoelotech.infra.controller.contato;

import com.diego.prandini.exerciciotecnicoelotech.application.contato.AdicionarContatoPessoa;
import com.diego.prandini.exerciciotecnicoelotech.application.contato.BuscarContatoPessoa;
import com.diego.prandini.exerciciotecnicoelotech.application.contato.ListarContatosPessoa;
import com.diego.prandini.exerciciotecnicoelotech.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pessoas/{idPessoa}/contatos")
public class ContatoController {

    private final ListarContatosPessoa listarContatosPessoa;
    private final BuscarContatoPessoa buscarContatoPessoa;
    private final AdicionarContatoPessoa adicionarContatoPessoa;

    @GetMapping
    public ResponseEntity<ListarContatosPessoa.Output> listar(@PathVariable UUID idPessoa) {
        ListarContatosPessoa.Output output = listarContatosPessoa.execute(idPessoa);
        return ResponseEntity.ok(output);
    }

    @GetMapping("/{idContato}")
    public ResponseEntity<BuscarContatoPessoa.Output> buscar(@PathVariable UUID idPessoa, @PathVariable UUID idContato) {
        BuscarContatoPessoa.Output output = buscarContatoPessoa.execute(idPessoa, idContato);
        return ResponseEntity.ok(output);
    }

    @PostMapping
    public ResponseEntity<AdicionarContatoPessoa.Output> adicionar(@PathVariable UUID idPessoa, @RequestBody AdicionarContatoPessoa.Input input) {
        AdicionarContatoPessoa.Output output = adicionarContatoPessoa.execute(idPessoa, input);
        String resource = String.format("/pessoas/%s/contatos/%s", output.idPessoa(), output.id());
        return ResponseEntity.created(ApiUtils.createUri(output.id(), resource)).body(output);
    }
}
