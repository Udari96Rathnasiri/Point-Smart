package lk.ijse.dep11.pos.db;

import lk.ijse.dep11.pos.tm.Customer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerDataAccessTest {

    @BeforeEach
    void setUp() throws SQLException {
        SingleConnectionDataSource.getInstance().getConnection().setAutoCommit(false);
    }

    @AfterEach
    void tearDown() throws SQLException {
        SingleConnectionDataSource.getInstance().getConnection().rollback();
        SingleConnectionDataSource.getInstance().getConnection().setAutoCommit(true);

    }

    @Test
    void getAllCustomers() throws SQLException {
        CustomerDataAccess.saveCustomer(new Customer("BVC","Kasun Sampath","Panadura"));
        CustomerDataAccess.saveCustomer(new Customer("DFG","Nuwan Sameera","Galle"));
        assertDoesNotThrow(()->{
            List<Customer> customerList = CustomerDataAccess.getAllCustomers();
            assertTrue(customerList.size() >= 2);
        });


    }

    @Test
    void saveCustomer() {
        assertDoesNotThrow(()->{
            CustomerDataAccess.saveCustomer(new Customer("ABC","Kasun Sampath","Panadura"));
            CustomerDataAccess.saveCustomer(new Customer("EDF","Nuwan Sameera","Galle"));
        });
        assertThrows(SQLException.class, ()-> CustomerDataAccess
                .saveCustomer(new Customer("ABC", "Kasun", "Galle")));
    }
}