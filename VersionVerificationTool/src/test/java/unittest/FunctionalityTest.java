package unittest;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FunctionalityTest {
    @Test
    public void getResponse() {
        System.out.println("hello world");
        //TODO create a Webservice call to recieve response
        //http://services.groupkt.com/state/get/IND/all
        String fileName = "C://Users//Ana Katrina De Leon//Documents//Work//TW//urls.txt";
        List<String> list = new ArrayList<String>();
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

            list = stream
                    .map(String::toUpperCase)
                    .collect(Collectors.toList());
            for(String test: list){
                System.out.println(test);
            }

            URL url = new URL("http://services.groupkt.com/state/get/IND/all");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }

            conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }
}
