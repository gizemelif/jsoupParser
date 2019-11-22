import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        /*List<String> strings = new ArrayList<String>();

        Document doc = Jsoup.connect("https://www.bim.com.tr/Categories/104/magazalar.aspx?").get();

        Element city = doc.getElementById("gzoneortaalandegisken2_BimFiltre2_DrpCity");
        Elements cities = city.getElementsByTag("option");

        for (int i = 1; i < cities.size(); i++) {
            String selected = cities.get(i).text() + ":" + cities.get(i).attr("value");
            System.out.println(selected);

            try {
                Document doc2 = Jsoup.connect("https://www.bim.com.tr/Categories/104/magazalar.aspx?CityKey=" + cities.get(i).attr("value")).get();
                Element county = doc2.getElementById("gzoneortaalandegisken2_BimFiltre2_DrpCounty");
                Elements counties = county.getElementsByTag("option");
                for (int j = 1; j < counties.size(); j++) {

                    String keyValuePair = "{"+"\"" + "city" + "\":" + "\"" + cities.get(i).attr("value") +"\""+ "," + "\"" + "county" + "\":" + "\"" + counties.get(j).attr("value") + "\","+ "\"" + "countyName" + "\":" + "\"" + counties.get(j).text() + "\""+"}";

                    strings.add(keyValuePair);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        File file = new File("C:\\Users\\geatalay\\Desktop\\bimCountyNames2");
        FileWriter writer = new FileWriter(file);
        for (String str : strings) {
            writer.write(str + ",");
        }
        writer.close();*/

        String filePath = "C:\\Users\\geatalay\\Desktop\\bimCountyNames2";
        String fileString = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);

        JSONArray jsonArray = new JSONArray(fileString);

        ArrayList<String> storeAddresses = new ArrayList<>();
        for(int i=0; i<jsonArray.length(); i++){
            JSONObject object = jsonArray.getJSONObject(i);
            String cityValue="";
            String countyValue="";
            String countyName ="";
            cityValue = object.getString("city");
            countyValue = object.getString("county");
            countyName = object.getString("countyName");

            try{
                String url = "https://www.bim.com.tr/Categories/104/magazalar.aspx?CityKey=" + cityValue + "&CountyKey=" + countyValue;
                Document doc = Jsoup.connect(url).get();
                Element table = doc.select("table").get(0); //select the first table.
                Elements rows = table.select("tr");

                for (int j = 1; j < rows.size(); j++) { //first row is the col names so skip it.
                    Element row = rows.get(j);
                    Elements cols = row.select("td");

                    for(int k=1; k<cols.size(); k++){
                        storeAddresses.add(cityValue+";"+countyName+";"+cols.get(0).text()+";"+cols.get(1).text());
                    }

                }
            }catch (Exception e){e.printStackTrace();}
        }


        File file = new File("C:\\Users\\geatalay\\Desktop\\bimStoresInfo2.txt");
        FileWriter writer = new FileWriter(file);
        for (String str : storeAddresses) {
            writer.write(str+"\r\n");
        }
        writer.close();
    }
}