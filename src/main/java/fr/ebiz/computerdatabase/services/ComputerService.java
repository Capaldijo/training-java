package fr.ebiz.computerdatabase.services;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ebiz.computerdatabase.daos.ComputerDAO;
import fr.ebiz.computerdatabase.dtos.ComputerDTO;
import fr.ebiz.computerdatabase.exceptions.ConnectionException;
import fr.ebiz.computerdatabase.exceptions.DAOException;
import fr.ebiz.computerdatabase.exceptions.MapperException;
import fr.ebiz.computerdatabase.interfaces.ServiceInterface;
import fr.ebiz.computerdatabase.mappers.ComputerMapper;
import fr.ebiz.computerdatabase.models.Computer;
import fr.ebiz.computerdatabase.models.PaginationFilters;
import fr.ebiz.computerdatabase.persistence.ConnectionDB;
import fr.ebiz.computerdatabase.validators.ComputerValidator;

/**
 * Computer service is a singleton class. that will handle
 * all the Computer service part, get all or one company
 * using the ComputerDAO class and ComputerMapper class
 */
public final class ComputerService implements ServiceInterface<ComputerDTO> {

    private static ComputerService instance = new ComputerService();

    static final Logger LOG = LoggerFactory.getLogger(ComputerService.class);

    private ComputerDAO computerDAO;

    private ComputerMapper computerMapper;

    /**
     * Constructor ComputerService.
     * @throws ConnectionException Error on co to db
     */
    private ComputerService() {
        computerDAO = new ComputerDAO();
        computerMapper = new ComputerMapper();
    }

    /**
     * Get the instance of ComputerService Singleton class.
     * @return the instance of ComputerSerice.
     */
    public static ComputerService getInstance() {

        if (ComputerService.instance == null) {
            ComputerService.instance = new ComputerService();
        }
        return ComputerService.instance;
    }

    @Override
    public int add(ComputerDTO computerDTO) {
        int res = 0;

        if (!ComputerValidator.isValid(computerDTO)) {
            LOG.error("[VALIDATION] The computer you tried to add is not valid.");
        } else {
            try {
                Computer computer = computerMapper.toModel(computerDTO);
                if (computer != null) {
                    TransactionHolder.set(ConnectionDB.getInstance().getHikariDS().getConnection());

                    if (computerDAO.insert(computer) == 1) {
                        res = 1;
                    } else {
                        LOG.error("insert computer error.\n");
                        res = -1;
                    }

                    TransactionHolder.get().commit();
                }
            } catch (MapperException | DAOException | SQLException e) {
                try {
                    TransactionHolder.get().rollback();
                } catch (SQLException e1) {
                    LOG.error(e.getMessage());
                    throw new RuntimeException(e.getMessage());
                }
                LOG.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            } finally {
                try {
                    TransactionHolder.get().close();
                } catch (SQLException e) {
                    LOG.error(e.getMessage());
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
        TransactionHolder.unset();
        return res;
    }

    @Override
    public ComputerDTO get(String id) {
        ComputerDTO computerDTO = null;
        Computer computer = null;

        try {
            /* ------ GET COMPUTER BY ID ----- */
            Long idComp = Long.parseLong(id);

            TransactionHolder.set(ConnectionDB.getInstance().getHikariDS().getConnection());

            computer = computerDAO.find(idComp);

            TransactionHolder.get().commit();

            computerDTO = computerMapper.toDTO(computer);
        } catch (DAOException | SQLException e) {
            try {
                TransactionHolder.get().rollback();
            } catch (SQLException e1) {
                LOG.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
            LOG.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        } catch (NumberFormatException e) {
            LOG.error("[GETCOMPUTER] Error on ID given.");
            throw new RuntimeException("[GETCOMPUTER] Error on ID given.");
        } finally {
            try {
                TransactionHolder.get().close();
            } catch (SQLException e) {
                LOG.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
        }
        TransactionHolder.unset();
        return computerDTO;
    }

    @Override
    public List<ComputerDTO> getByPage(String numPage, String nbLine, PaginationFilters filters) {
        List<ComputerDTO> listComputerDTO = null;

        try {
            int numP = Integer.parseInt(numPage);
            int nbL = Integer.parseInt(nbLine);

            TransactionHolder.set(ConnectionDB.getInstance().getHikariDS().getConnection());

            listComputerDTO = computerDAO.findByPage(filters, numP, nbL);


            TransactionHolder.get().commit();
        } catch (NumberFormatException | DAOException e) {
            LOG.error("[GETCOMPUTERBYPAGE] Error on getting data.");
            throw new RuntimeException("[GETCOMPUTERBYPAGE] Error on getting data.");
        } catch (SQLException e) {
            try {
                TransactionHolder.get().rollback();
            } catch (SQLException e1) {
                LOG.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
            LOG.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                TransactionHolder.get().close();
            } catch (SQLException e) {
                LOG.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
        }
        TransactionHolder.unset();
        return listComputerDTO;
    }

    @Override
    public int update(ComputerDTO computerDTO) {
        int res = 0;

        if (!ComputerValidator.isValid(computerDTO)) {
            throw new RuntimeException("[VALIDATION] The computer you tried to update is not valid.");
        } else {
            try {
                Computer computer = computerMapper.toModel(computerDTO);

                TransactionHolder.set(ConnectionDB.getInstance().getHikariDS().getConnection());

                computerDAO.update(computer);
                if (computerDAO.update(computer) == 1) {
                    res = 1;
                } else {
                    LOG.error("Update computer error.\n");
                    res = -1;
                }

                TransactionHolder.get().commit();
            } catch (DAOException | MapperException | SQLException e) {
                try {
                    TransactionHolder.get().rollback();
                } catch (SQLException e1) {
                    LOG.error(e.getMessage());
                    throw new RuntimeException(e.getMessage());
                }
                LOG.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            } finally {
                try {
                    TransactionHolder.get().close();
                } catch (SQLException e) {
                    LOG.error(e.getMessage());
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
        TransactionHolder.unset();
        return res;
    }

    @Override
    public int count() {
        int count = 0;

        try {
            TransactionHolder.set(ConnectionDB.getInstance().getHikariDS().getConnection());

            count = computerDAO.count();

            TransactionHolder.get().commit();
        } catch (DAOException | SQLException e) {
            try {
                TransactionHolder.get().rollback();
            } catch (SQLException e1) {
                LOG.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
            LOG.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                TransactionHolder.get().close();
            } catch (SQLException e) {
                LOG.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
        }
        TransactionHolder.unset();
        return count;
    }

    @Override
    public int count(String research) {
        int count = 0;

        try {
            TransactionHolder.set(ConnectionDB.getInstance().getHikariDS().getConnection());

            count = computerDAO.countAfterSearch(research);

            TransactionHolder.get().commit();
        } catch (DAOException | SQLException e) {
            try {
                TransactionHolder.get().rollback();
            } catch (SQLException e1) {
                LOG.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
            LOG.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                TransactionHolder.get().close();
            } catch (SQLException e) {
                LOG.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
        }
        TransactionHolder.unset();
        return count;
    }

    @Override
    public int delete(String...ids) {
        int res = 0;

        try {
            TransactionHolder.set(ConnectionDB.getInstance().getHikariDS().getConnection());
            if (computerDAO.delete(ids) == 1) {
                res = 1;
            } else {
                LOG.error("Delete computer error.\n");
                res = 0;
            }

            TransactionHolder.get().commit();
        } catch (DAOException | SQLException e) {
            try {
                TransactionHolder.get().rollback();
            } catch (SQLException e1) {
                LOG.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
            LOG.error(e.getMessage());
            throw new RuntimeException("[DELETECOMPUTER] Error on accessing data.");
        } finally {
            try {
                TransactionHolder.get().close();
            } catch (SQLException e) {
                LOG.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
        }
        TransactionHolder.unset();
        return res;
    }

    @Override
    public int delete(String id) {
        int res = 0;

        try {
            if (computerDAO.delete(id) == 1) {
                res = 1;
            } else {
                LOG.error("Delete computer error.\n");
                res = 0;
            }
        } catch (SQLException | ConnectionException e) {
            throw new RuntimeException("[DELETECOMPUTER] Error on accessing data.");
        }
        return res;
    }

    /**
     * Delete a computer which is built by company id parameter.
     * @param id of company id ref
     * @return 1 if deleted 0 either way
     */
    public int deleteFromCompanyId(String id) {
        int res = 0;

        try {
            TransactionHolder.set(ConnectionDB.getInstance().getHikariDS().getConnection());

            if (computerDAO.deleteFromCompanyId(id) == 1) {
                res = 1;
            } else {
                LOG.error("Delete computer error.\n");
                res = 0;
            }

            TransactionHolder.get().commit();
        } catch (SQLException | ConnectionException e) {
            try {
                TransactionHolder.get().rollback();
            } catch (SQLException e1) {
                LOG.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
            LOG.error(e.getMessage());
            throw new RuntimeException("[DELETECOMPANYFROMID] Error on accessing data.");
        } finally {
            try {
                TransactionHolder.get().close();
            } catch (SQLException e) {
                LOG.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
        }
        TransactionHolder.unset();
        return res;
    }

    @Override
    public List<ComputerDTO> getAll() {
        LOG.error("[SERVICE] [GETALL] Not implemented anymore.");
        throw new RuntimeException("[SERVICE] [GETALL] Not implemented anymore.");
    }
}
