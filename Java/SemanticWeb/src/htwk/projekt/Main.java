package htwk.projekt;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.ArrayList;

public class Main {

    static ArrayList<String> alreadyDownloadedList = new ArrayList<>();
    static ArrayList<String> directionsAlreadyDownloadedList = new ArrayList<>();
    static DocumentBuilderFactory dbFactory;
    static DocumentBuilder dBuilder;

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        dbFactory = DocumentBuilderFactory.newInstance();
        dBuilder = dbFactory.newDocumentBuilder();

        //downloadPerformersData();
        //cleanLastFMFolder();
        //downloadDirectionsData();

        /*
        File dir = new File("D:\\Benutzer\\philt\\Documents\\HTWK\\SemanticWeb\\data\\lastfm");
        File[] files = dir.listFiles((dir1, name) -> name.toLowerCase().endsWith(".xml"));
        for (File file : files) {
            minifyFile(file, new File("D:\\Benutzer\\philt\\Documents\\HTWK\\SemanticWeb\\data_minified\\lastfm\\" + file.getName()));
        }
        */

        //minifyFile(new File("D:\\Benutzer\\philt\\Documents\\HTWK\\SemanticWeb\\data\\lastfm\\2raumwohnung.xml"), new File("D:\\Benutzer\\philt\\Documents\\HTWK\\SemanticWeb\\data_minified\\lastfm\\2raumwohnung.xml"));
        //normalizeXML();
        insertLocationDurations();
    }

    public static void normalizeXML() throws IOException, SAXException {
        File dir = new File("D:\\Benutzer\\philt\\Documents\\HTWK\\SemanticWeb\\data\\googledirections");
        File[] files = dir.listFiles((dir1, name) -> name.toLowerCase().endsWith(".xml"));
        for(File file : files) {
            Document doc = dBuilder.parse(file);
            doc.normalizeDocument();

            Transformer transformer = null;
            try {
                transformer = TransformerFactory.newInstance().newTransformer();
                Result output = new StreamResult(file);
                Source input = new DOMSource(doc);

                transformer.transform(input, output);
            } catch (TransformerException e) {
                e.printStackTrace();
            }

        }
    }

    public static void downloadDirectionsData() throws IOException, SAXException {
        File lastFmDir = new File("D:\\Benutzer\\philt\\Documents\\HTWK\\SemanticWeb\\data\\googledirections");
        File[] lastFmFiles = lastFmDir.listFiles((dir1, name) -> name.toLowerCase().endsWith(".xml"));
        for(File file : lastFmFiles){
            directionsAlreadyDownloadedList.add(file.getName().replace(".xml", ""));
        }

        File dir = new File("D:\\Benutzer\\philt\\Documents\\HTWK\\SemanticWeb\\data\\eventful");
        File[] files = dir.listFiles((dir1, name) -> name.toLowerCase().endsWith(".xml"));
        for(File file : files) {
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            NodeList eventList = doc.getElementsByTagName("event");

            for (int temp = 0; temp < eventList.getLength(); temp++) {
                Element event = (Element) eventList.item(temp);
                Element performers = (Element) event.getElementsByTagName("performers").item(0);
                NodeList performerList = performers.getElementsByTagName("performer");
                int performerCount = performerList.getLength();
                if (performerCount > 0) {
                    String venue = event.getElementsByTagName("venue_name").item(0).getTextContent() + " " + event.getElementsByTagName("city_name").item(0).getTextContent();
                    System.out.println(venue);
                    downloadDirectionsInfo(venue);
                }
            }
        }
    }

    public static void insertLocationDurations() {
        File dir = new File("D:\\Benutzer\\philt\\Documents\\HTWK\\SemanticWeb\\data\\googledirections");
        File[] files = dir.listFiles((dir1, name) -> name.toLowerCase().endsWith(".xml"));
        for(File file : files) {
            Document doc = null;
            try {
                doc = dBuilder.parse(file);
                doc.normalize();


                /*
                NodeList leg = doc.getElementsByTagName("leg");
                Element performerName = doc.createElement("location");
                performerName.setTextContent(file.getName().replace(".xml", ""));
                leg.item(0).appendChild(performerName);
                */

                NodeList nodeList = doc.getElementsByTagName("leg");
                Element leg = (Element) nodeList.item(0);
                NodeList nodeList2 = leg.getElementsByTagName("location");
                Element location = (Element) nodeList2.item(0);
                String textContent = location.getTextContent();
                location.setTextContent(textContent.substring(0, textContent.length() - 1));

                /*
                NodeList eventList = doc.getElementsByTagName("step");
                System.out.println(eventList.getLength());

                for (int temp = 0; temp < eventList.getLength(); temp++) {
                    Element step = (Element) eventList.item(temp);
                    step.getParentNode().removeChild(step);
                }
                */

                doc.normalize();
                Transformer transformer = null;
                try {
                    transformer = TransformerFactory.newInstance().newTransformer();
                    Result output = new StreamResult(file);
                    Source input = new DOMSource(doc);

                    transformer.transform(input, output);
                } catch (TransformerException e) {
                    e.printStackTrace();
                }
            } catch (SAXException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void downloadPerformersData() throws ParserConfigurationException, IOException, SAXException {
        File lastFmDir = new File("D:\\Benutzer\\philt\\Documents\\HTWK\\SemanticWeb\\data\\lastfm");
        File[] lastFmFiles = lastFmDir.listFiles((dir1, name) -> name.toLowerCase().endsWith(".xml"));
        for(File file : lastFmFiles){
            alreadyDownloadedList.add(file.getName().replace(".xml", ""));
        }

        File dir = new File("D:\\Benutzer\\philt\\Documents\\HTWK\\SemanticWeb\\data\\eventful");
        File[] files = dir.listFiles((dir1, name) -> name.toLowerCase().endsWith(".xml"));
        for(File file : files) {
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            NodeList eventList = doc.getElementsByTagName("event");

            for (int temp = 0; temp < eventList.getLength(); temp++) {
                Element event = (Element) eventList.item(temp);
                Element performers = (Element) event.getElementsByTagName("performers").item(0);
                NodeList performerList = performers.getElementsByTagName("performer");
                int performerCount = performerList.getLength();
                if (performerCount > 0) {
                    for (int i = 0; i < performerList.getLength(); i++) {
                        Element performer = (Element) performerList.item(i);
                        String name = performer.getElementsByTagName("name").item(0).getTextContent();
                        downloadArtistInfo(name);
                        System.out.println(name);
                    }
                } else {
                    String title = event.getElementsByTagName("title").item(0).getTextContent();
                    String name = title.split("-")[0].trim();
                    System.out.println(name);
                    int tagcount = downloadArtistInfo(name);
                    if (tagcount > 0) {
                        Element performer = doc.createElement("performer");
                        Element performerName = doc.createElement("name");
                        performerName.setTextContent(name);
                        performer.appendChild(performerName);
                        performers.appendChild(performer);
                    }
                }
            }
            Transformer transformer = null;
            try {
                transformer = TransformerFactory.newInstance().newTransformer();
                Result output = new StreamResult(file);
                Source input = new DOMSource(doc);

                transformer.transform(input, output);
            } catch (TransformerException e) {
                e.printStackTrace();
            }

        }
    }

    public static void cleanLastFMFolder(){

        File dir = new File("D:\\Benutzer\\philt\\Documents\\HTWK\\SemanticWeb\\data\\lastfm");
        File[] files = dir.listFiles((dir1, name) -> name.toLowerCase().endsWith(".xml"));
        for(File file : files) {
            boolean deleteFile = true;
            Document doc = null;

            try {
                doc = dBuilder.parse(file);
                doc.getDocumentElement().normalize();
                NodeList tagList = doc.getElementsByTagName("tag");
                if(tagList.getLength() > 0)
                    deleteFile = false;
            } catch (SAXException | IOException e) {
                e.printStackTrace();
            }
            if(deleteFile){
                try {
                    Files.deleteIfExists(file.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static int downloadArtistInfo(String name) {
        File saveFile = new File("D:\\Benutzer\\philt\\Documents\\HTWK\\SemanticWeb\\data\\lastfm\\" + name + ".xml");
        if(!alreadyDownloadedList.contains(name)) {

            try {
                OutputStream out = new FileOutputStream(saveFile);


                URL url = new URL("http://ws.audioscrobbler.com/2.0/?method=artist.gettoptags&artist=" + name.replace(" ", "%20") + "&api_key=7117c13d0e7c72d5ab74cc26096c5a7b");
                URLConnection conn = url.openConnection();
                conn.connect();
                InputStream is = conn.getInputStream();

                copy(is, out);
                is.close();
                out.close();
                alreadyDownloadedList.add(name);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(saveFile.exists() && saveFile.isFile()){
            try {
                Document doc = dBuilder.parse(saveFile);
                doc.getDocumentElement().normalize();
                NodeList tagList = doc.getElementsByTagName("tag");
                return tagList.getLength();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return 0;
    }

    public static void downloadDirectionsInfo(String name) {
        name = name.replace("/", "_").replace("\\", "_");
        File saveFile = new File("D:\\Benutzer\\philt\\Documents\\HTWK\\SemanticWeb\\data\\googledirections\\" + name + ".xml");
        if(!directionsAlreadyDownloadedList.contains(name)) {

            try {
                OutputStream out = new FileOutputStream(saveFile);


                URL url = new URL("https://maps.googleapis.com/maps/api/directions/xml?origin=Leipzig&destination=" + name.replace(" ", "%20") + "&key=AIzaSyCVm5w5MwegFbvAx6SBNmVoHXLdwgojMcM");
                URLConnection conn = url.openConnection();
                conn.connect();
                InputStream is = conn.getInputStream();

                copy(is, out);
                is.close();
                out.close();
                directionsAlreadyDownloadedList.add(name);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void copy(InputStream from, OutputStream to) throws IOException {
        byte[] buffer = new byte[4096];
        while (true) {
            int numBytes = from.read(buffer);
            if (numBytes == -1) {
                break;
            }
            to.write(buffer, 0, numBytes);
        }
    }

    private static void minifyFile(File sourcefile, File targetfile) throws IOException {
        Files.createDirectories(targetfile.getParentFile().toPath());
        BufferedReader br = new BufferedReader(new FileReader(sourcefile));
        BufferedWriter wr = new BufferedWriter(new FileWriter(targetfile));
        String line;

        while((line=br.readLine())!= null){
            wr.append(line.trim());
        }
    }
}
