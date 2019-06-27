package br.edu.utfpr.servico;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.utfpr.dto.ClienteDTO;
import br.edu.utfpr.dto.PaisDTO;
import io.micrometer.core.ipc.http.HttpSender.Response;

@RestController
public class ServicoCliente {
    private List<ClienteDTO> clientes;

    public ServicoCliente() {
        clientes = Stream.of(
            ClienteDTO.builder().id(1).nome("Lucas Mussi").idade(21).telefone("439xxxxxxxx").limiteCredito(35.5).pais(PaisDTO.builder().id(1).nome("Brasil").build()).build(),
            ClienteDTO.builder().id(2).nome("Kurt Cobain").idade(21).telefone("0800xxxxxxx").limiteCredito(23).pais(PaisDTO.builder().id(2).nome("Estados Unidos da América").build()).build(),
            ClienteDTO.builder().id(3).nome("Raul Seixas").idade(21).telefone("159xxxxxxxx").limiteCredito(57.3).pais(PaisDTO.builder().id(1).nome("Brasil").build()).build()
        ).collect(Collectors.toList());
    }

    @GetMapping("/servico/cliente")
    public ResponseEntity<List<ClienteDTO>> findAll() {
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/servico/cliente/{id}")
    public ResponseEntity<ClienteDTO> findById(@PathVariable int id) {
        return ResponseEntity.of(clientes.stream()
            .filter(c -> c.getId() == id).findAny());
    }

    @PostMapping("/servico/cliente")
    public ResponseEntity<ClienteDTO> create(@RequestBody ClienteDTO cliente) {
        cliente.setId(clientes.size() + 1);
        clientes.add(cliente);
        return ResponseEntity.status(201).body(cliente);
    }

    @PutMapping("/servico/cliente/{id}")
    public ResponseEntity<ClienteDTO> update(@PathVariable int id, @RequestBody ClienteDTO cliente) {
        Optional<ClienteDTO> obj = clientes.stream()
            .filter(c -> c.getId() == id).findAny();
        obj.ifPresent(c -> {
            c.setNome(cliente.getNome());
            c.setIdade(cliente.getIdade());
            c.setTelefone(cliente.getTelefone());
            c.setLimiteCredito(cliente.getLimiteCredito());
            c.setPais(cliente.getPais());
        });
        return ResponseEntity.of(obj);
    }

    @DeleteMapping("/servico/cliente/{id}")
    public ResponseEntity<ClienteDTO> delete(@PathVariable int id) {
        if(clientes.removeIf(cliente -> cliente.getId() == id))
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.notFound().build();
    }
}