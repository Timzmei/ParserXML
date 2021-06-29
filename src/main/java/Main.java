import javax.xml.namespace.QName;
import javax.xml.stream.*;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by timurg on 29.06.2021.
 */
public class Main {
    public static void main(String[] args) throws XMLStreamException, FileNotFoundException {
        String path = args[0];
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLEventReader reader = xmlInputFactory.createXMLEventReader(new FileInputStream(path));
        Set<String> modificationName = new HashSet<>();
        System.out.println(parseXML(reader, modificationName).size());
    }

    private static Set<String> parseXML(XMLEventReader reader, Set<String> modificationName) throws XMLStreamException {
        while (reader.hasNext()) {
            XMLEvent nextEvent = reader.nextEvent();
            if (nextEvent.isStartElement()) {
                StartElement startElement = nextEvent.asStartElement();
                if (startElement.getName().getLocalPart().equals("modification")) {
                    String value = startElement.getAttributeByName(new QName("name")).getValue();
                    Pattern pattern = Pattern.compile("((\\d\\.\\d[\\D]*\\s)?[\\D]?\\DT\\s\\([\\d\\D]*\\)[\\d\\D]*\\Z)");
                    Matcher matcher = pattern.matcher(value);
                    if (matcher.find()){
                        modificationName.add(matcher.group());
                    }
                }
            }
        }
        return modificationName;
    }
}

