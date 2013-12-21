package server.resourseSystem;

import org.junit.Test;
import resource.DbInfo;
import resource.ResourceSystemImpl;
import server.base.ResourceSystem;

public class ResourceSystemTest {

    @Test
    public void testResourseServiceGet() {
        ResourceSystem resourceSystem = new ResourceSystemImpl();
        DbInfo testInfo = (DbInfo) resourceSystem.getResource("testDB.xml");
        assert testInfo.address.equals("localhost") : "incorrect host address";
        assert testInfo.dataBaseName.equals("imdb") : "incorrect database name";
        assert testInfo.user.equals("root") : "incorrect user";
        assert testInfo.password.equals("1fear1") : "incorrect password";
        assert testInfo.type.equals("mysql") : "incorrect database type";
        assert testInfo.port.equals("3306") : "incorrect port";
    }
}
