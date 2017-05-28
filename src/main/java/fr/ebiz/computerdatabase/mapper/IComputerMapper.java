package fr.ebiz.computerdatabase.mapper;

import java.util.List;

import fr.ebiz.computerdatabase.dto.ComputerDTO;
import fr.ebiz.computerdatabase.exception.MapperException;
import fr.ebiz.computerdatabase.model.Company;
import fr.ebiz.computerdatabase.model.Computer;

public interface IComputerMapper {

    /**
     * Map a model to a DTO.
     * @param model model to map
     * @return a DTO
     */
    ComputerDTO toDTO(Computer model);

    /**
     * Map a list of model to list of ComputerDTO.
     * @param list models to map
     * @return a list of DTOs.
     */
    List<ComputerDTO> toDTO(List<Computer> list);

    /**
     * Map a ComputerDTO into a model.
     * @param dto to map.
     * @return a computer.
     * @throws MapperException error on mapping DTO
     */
    Computer toModel(ComputerDTO dto) throws MapperException;
}
