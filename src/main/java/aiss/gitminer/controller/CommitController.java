package aiss.gitminer.controller;

import aiss.gitminer.exception.CommitNotFoundException;
import aiss.gitminer.model.Commit;
import aiss.gitminer.repository.CommitRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Commit", description = "Commit management API")
@RestController // indicar que es controlador
@RequestMapping("/gitminer/commits")
public class CommitController {

    @Autowired // cargar repositorio de commit con datos
    CommitRepository commitRepository;

    // Devolver todos los users
    @Operation(
            summary = "Get a list of all commits",
            description = "Retrieve a list of all commits",
            tags = { "commit", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content =
                    {@Content(schema = @Schema(implementation = Commit.class),
                            mediaType = "application/json")})
    })
    @GetMapping // especificar metodo HTTP a utilizar
    public List<Commit> findAll (@RequestParam(required = false) String author_name,
                                 @RequestParam(required = false) String order,
                                 @RequestParam(defaultValue = "5") int page,
                                 @RequestParam(defaultValue = "5") int size) {
        Pageable paging;

        if (order != null) {
            if (order.startsWith("-")) {
                paging = PageRequest.of(page, size, Sort.by(order.substring(1)).descending());
            }
            else {
                paging = PageRequest.of(page, size, Sort.by(order).ascending());
            }
        }
        else {
            paging = PageRequest.of(page, size);
        }

        Page<Commit> pageCommits;

        if (author_name == null) {
            pageCommits = commitRepository.findAll(paging);
        }
        else {
            pageCommits = commitRepository.findByAuthorName(author_name, paging);
        }
        return pageCommits.getContent();
    }


    @Operation(
            summary = "Get a commit by id",
            description = "Find a commit by it's id",
            tags = {"get by id", "commit"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content =
                    {@Content(schema = @Schema(implementation = Commit.class),
                            mediaType = "application/json")})
    })
    @GetMapping("/{id}")
    public Commit findById(@Parameter(description = "id of a commit to be searched")
                           @PathVariable String id) throws CommitNotFoundException {
        Optional<Commit> foundCommit = commitRepository.findById(id);

        if (!foundCommit.isPresent()) {
            throw new CommitNotFoundException();
        }
        return foundCommit.get();
    }
}

