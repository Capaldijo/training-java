package fr.ebiz.computerdatabase.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ebiz.computerdatabase.daos.ComputerDAO;
import fr.ebiz.computerdatabase.exceptions.ConnectionException;
import fr.ebiz.computerdatabase.exceptions.DAOException;
import fr.ebiz.computerdatabase.exceptions.MapperException;
import fr.ebiz.computerdatabase.interfaces.ServiceInterface;
import fr.ebiz.computerdatabase.mappers.ComputerMapper;
import fr.ebiz.computerdatabase.models.Computer;
import fr.ebiz.computerdatabase.models.PaginationFilters;
import fr.ebiz.computerdatabase.persistence.ConnectionDB;
import fr.ebiz.computerdatabase.validators.ComputerValidator;
import fr.ebiz.computerdatanase.dtos.ComputerDTO;

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

    private Connection co;

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
                    co = ConnectionDB.getInstance().getConnection();
                    co.setSavepoint("ADD");
                    TransactionHolder.set(co);
                    if (computerDAO.insert(computer) == 1) {
                        LOG.info("insert computer done.\n");
                        res = 1;
                    } else {
                        LOG.info("insert computer error.\n");
                        res = -1;
                    }
                    TransactionHolder.unset();
                    co.commit();
                }
            } catch (MapperException | DAOException | ConnectionException | SQLException e) {
                try {
                    co.rollback();
                } catch (SQLException e1) {
                    LOG.error(e.getMessage());
                    throw new RuntimeException(e.getMessage());
                }
                LOG.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            } finally {
                try {
                    co.close();
                } catch (SQLException e) {
                    LOG.error(e.getMessage());
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
        return res;
    }

    @Override
    public ComputerDTO get(String id) {
        ComputerDTO computerDTO = null;
        Computer computer = null;

        try {
            /* ------ GET COMPUTER BY ID ----- */
            Long idComp = Long.parseLong(id);

            co = ConnectionDB.getInstance().getConnection();
            co.setSavepoint("GETID");
            TransactionHolder.set(co);

            computer = computerDAO.find(idComp);

            TransactionHolder.unset();
            co.commit();

            computerDTO = computerMapper.toDTO(computer);
        } catch (DAOException | ConnectionException | SQLException e) {
            try {
                co.rollback();
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
                co.close();
            } catch (SQLException e) {
                LOG.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
        }

        return computerDTO;
    }

    @Override
    public List<ComputerDTO> getByPage(String numPage, String nbLine, PaginationFilters filters) {
        List<ComputerDTO> listComputerDTO = null;
        try {
            int numP = Integer.parseInt(numPage);
            int nbL = Integer.parseInt(nbLine);

            co = ConnectionDB.getInstance().getConnection();
            co.setSavepoint("GETBYPAGE");
            TransactionHolder.set(co);

            listComputerDTO = computerDAO.findByPage(filters, numP, nbL);

            TransactionHolder.unset();
            co.commit();
        } catch (NumberFormatException | DAOException e) {
            LOG.error("[GETCOMPUTERBYPAGE] Error on getting data.");
            throw new RuntimeException("[GETCOMPUTERBYPAGE] Error on getting data.");
        } catch (ConnectionException | SQLException e) {
            try {
                co.rollback();
            } catch (SQLException e1) {
                LOG.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
            LOG.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                co.close();
            } catch (SQLException e) {
                LOG.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
        }
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

                co = ConnectionDB.getInstance().getConnection();
                co.setSavepoint("UPDATE");
                TransactionHolder.set(co);

                computerDAO.update(computer);
                if (computerDAO.update(computer) == 1) {
                    LOG.info("Update computer done.\n");
                    res = 1;
                } else {
                    LOG.info("Update computer error.\n");
                    res = -1;
                }
                TransactionHolder.unset();
                co.commit();
            } catch (DAOException | MapperException | SQLException | ConnectionException e) {
                try {
                    co.rollback();
                } catch (SQLException e1) {
                    LOG.error(e.getMessage());
                    throw new RuntimeException(e.getMessage());
                }
                LOG.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            } finally {
                try {
                    co.close();
                } catch (SQLException e) {
                    LOG.error(e.getMessage());
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
        return res;
    }

    @Override
    public int count() {
        int count = 0;
        try {
            co = ConnectionDB.getInstance().getConnection();
            co.setSavepoint("COUNT");
            TransactionHolder.set(co);

            count = computerDAO.count();

            TransactionHolder.unset();
            co.commit();
        } catch (DAOException | SQLException | ConnectionException e) {
            try {
                co.rollback();
            } catch (SQLException e1) {
                LOG.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
            LOG.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                co.close();
            } catch (SQLException e) {
                LOG.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
        }
        return count;
    }

    @Override
    public int count(String research) {
        int count = 0;
        try {
            co = ConnectionDB.getInstance().getConnection();
            co.setSavepoint("COUNTSEARCH");
            TransactionHolder.set(co);

            count = computerDAO.countAfterSearch(research);

            TransactionHolder.unset();
            co.commit();
        } catch (DAOException | SQLException | ConnectionException e) {
            try {
                co.rollback();
            } catch (SQLException e1) {
                LOG.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
            LOG.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                co.close();
            } catch (SQLException e) {
                LOG.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
        }
        return count;
    }

    @Override
    public int delete(String...ids) {
        int res = 0;
        try {
            co = ConnectionDB.getInstance().getConnection();
            co.setSavepoint("DELETE");
            TransactionHolder.set(co);
            if (computerDAO.delete(ids) == 1) {
                LOG.info("Delete computer done.\n");
                res = 1;
            } else {
                LOG.info("Delete computer error.\n");
                res = 0;
            }
            TransactionHolder.unset();
            co.commit();
        } catch (DAOException | SQLException | ConnectionException e) {
            try {
                co.rollback();
            } catch (SQLException e1) {
                LOG.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
            LOG.error(e.getMessage());
            throw new RuntimeException("[DELETECOMPUTER] Error on accessing data.");
        } finally {
            try {
                co.close();
            } catch (SQLException e) {
                LOG.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
        }
        return res;
    }

    @Override
    public int delete(String id) {
        int res = 0;
        try {
            if (computerDAO.delete(id) == 1) {
                LOG.info("Delete computer done.\n");
                res = 1;
            } else {
                LOG.info("Delete computer error.\n");
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
            co = ConnectionDB.getInstance().getConnection();
            co.setSavepoint("DELETEDROMCOMPID");
            TransactionHolder.set(co);

            if (computerDAO.deleteFromCompanyId(id) == 1) {
                LOG.info("Delete computer done.\n");
                res = 1;
            } else {
                LOG.info("Delete computer error.\n");
                res = 0;
            }
            TransactionHolder.unset();
            co.commit();
        } catch (SQLException | ConnectionException e) {
            try {
                co.rollback();
            } catch (SQLException e1) {
                LOG.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
            LOG.error(e.getMessage());
            throw new RuntimeException("[DELETECOMPANYFROMID] Error on accessing data.");
        } finally {
            try {
                co.close();
            } catch (SQLException e) {
                LOG.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
        }
        return res;
    }

    @Override
    public List<ComputerDTO> getAll() {
        LOG.error("[SERVICE] [GETALL] Not implemented anymore.");
        throw new RuntimeException("[SERVICE] [GETALL] Not implemented anymore.");
    }
}
