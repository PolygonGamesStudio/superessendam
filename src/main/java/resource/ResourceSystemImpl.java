package resource;

import server.base.*;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: rudi
 * Date: 21.12.13
 * Time: 3:46
 * To change this template use File | Settings | File Templates.
 */
public class ResourceSystemImpl implements ResourceSystem {
    private final String resourcePath = "src/main/resources";
    private Map<String, Resource>  resources = new HashMap<>();

    public ResourceSystemImpl()
    {
        try
        {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            SaxEmptyHandler saxHandler = new SaxEmptyHandler();
            Iterator iterator = new Iterator(resourcePath);

            while(iterator.hasNext())
            {
                String fileName = iterator.getFileName();
                saxParser.parse(iterator.getFile(), saxHandler);
                Object obj = saxHandler.getObject();

                if(obj instanceof Resource)
                {
                    resources.put(fileName, (Resource)obj);
                }

            }
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public Resource getResource(String fileName)
    {
        return resources.get(fileName);
    }
}
