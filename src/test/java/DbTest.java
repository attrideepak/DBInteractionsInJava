import core.ConfigInitializer;
import org.testng.annotations.Test;
import utils.db_utils.DBUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DbTest extends ConfigInitializer {

    String query = "Select * from brands";

    @Test
    public void testDb(){
        List<Map<String,String>>map = DBUtils.doSelect("zoomcar",query);
        System.out.println(map.toString());
    }
}
