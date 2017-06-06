package fr.ebiz.computerdatabase.controller;


import fr.ebiz.computerdatabase.dto.ComputerDTO;
import fr.ebiz.computerdatabase.model.utils.PaginationFilters;

import fr.ebiz.computerdatabase.service.IComputerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping(value = "/api/computers")
public class ComputerRestController {

    private final IComputerService computerService;

    /**
     * Default ComputerRestController constructor for autowired.
     * @param computerService .
     */
    @Autowired
    public ComputerRestController(IComputerService computerService) {
        this.computerService = computerService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ComputerDTO>> listAllComputers() {
        List<ComputerDTO> computerDTOS = computerService.getAll();
        if (computerDTOS.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(computerDTOS, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<ComputerDTO> getComputer(@PathVariable("id") String id) {
        ComputerDTO computerDTO;
        try {
            computerDTO = computerService.get(id);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(computerDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "/pages/{page}/lines/{line}", method = RequestMethod.GET)
    public ResponseEntity<List<ComputerDTO>> listComputersByPage(@PathVariable("page") int page,
                                                           @PathVariable("line") int line) {
        int numPage = page * line;
        List<ComputerDTO> computerDTOS = computerService.getByPage(numPage, line, new PaginationFilters.Builder().build());
        if (computerDTOS.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(computerDTOS, HttpStatus.OK);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<Void> createComputer(@RequestBody ComputerDTO computerDTO, UriComponentsBuilder ucBuilder) {
        int insert = computerService.add(computerDTO);
        if (insert < 0) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<ComputerDTO> updateComputer(@PathVariable("id") String id,
                                                      @RequestBody ComputerDTO computerDTO) {
        if (id.equals(computerDTO.getId())) {
            int update = computerService.update(computerDTO);
            if (update > 0) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteComputer(@PathVariable("id") String id) {
        int delete = computerService.delete(id);
        if (delete > 0) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public ResponseEntity<ComputerDTO> createComputerError() {
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }
}
