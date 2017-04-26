package fr.ebiz.computerdatabase.interfaces;

import java.util.List;

import fr.ebiz.computerdatabase.exceptions.MapperException;

public interface MapperInterface<DTO, MODEL> {

    /**
     * Map a model to a DTO.
     * @param model model to map
     * @return a DTO
     */
    DTO toDTO(MODEL model);

    /**
     * Map a list of model to list of DTO.
     * @param list models to map
     * @return a list of DTOs
     */
    List<DTO> toDTO(List<MODEL> list);

    /**
     * Map a DTO into a model.
     * @param dto to map
     * @return a computer
     * @throws MapperException error on mapping DTO
     */
    MODEL toModel(DTO dto) throws MapperException;
}
