package lk.ijse.dep11.pos.db;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SingleConnectionDataSourceTest {

    @Test
    void generateSchema() {
    }

    @Test
    void getConnection() {
        assertDoesNotThrow(() -> SingleConnectionDataSource.getInstance().getConnection());
    }

    @Test
    void getInstance() {
    }
}