package fr.ebiz.computerdatabase.mapper;

import java.util.List;

import fr.ebiz.computerdatabase.dto.ComputerDTO;
import fr.ebiz.computerdatabase.model.Computer;

public interface IComputerMapper {

    /**
     * Map a fr.ebiz.computerdatabase.model to a DTO.
     * @param model fr.ebiz.computerdatabase.model to map
     * @return a DTO
     */
    ComputerDTO toDTO(Computer model);

    /**
     * Map a list of fr.ebiz.computerdatabase.model to list of ComputerDTO.
     * @param list models to map
     * @return a list of DTOs.
     */
    List<ComputerDTO> toDTO(List<Computer> list);

    /**
     * Map a ComputerDTO into a fr.ebiz.computerdatabase.model.
     * @param dto to map.
     * @return a computer.
     * @throws MapperException error on mapping DTO
     */
    Computer toModel(ComputerDTO dto) throws MapperException;
}
