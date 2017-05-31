package fr.ebiz.computerdatabase.test;

import static org.junit.Assert.assertEquals;

import fr.ebiz.computerdatabase.dao.IComputerDAO;
import fr.ebiz.computerdatabase.dto.CompanyDTO;
import fr.ebiz.computerdatabase.model.Company;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fr.ebiz.computerdatabase.dto.ComputerDTO;
import fr.ebiz.computerdatabase.dao.DAOException;
import fr.ebiz.computerdatabase.model.Computer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@ContextConfiguration(locations = "/WEB-INF/applicationContext.xml")
@TestExecutionListeners(DependencyInjectionTestExecutionListener.class)
public class ComputerServiceMockTest {

    @Mock
    @Autowired
    IComputerDAO computerDAO;

    ComputerDTO computerDTO;

    /**
     *
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        computerDTO = new ComputerDTO.Builder("CM-2a").company(new CompanyDTO("2", null)).build();
    }

    /**
     * @throws DAOException error getting data
     */
    @Test
    public void testFindComputer() throws DAOException {
        // add the behavior of computer service to return a computer
        Mockito.when(computerDAO.find(2L)).thenReturn(new Computer.Builder("CM-2a").company(new Company(2L, null)).build());

        assertEquals("Get computer id 2", true, true);
        Mockito.verify(computerDAO, Mockito.atLeastOnce());
    }

}
