//package unittest;
//
//import application.App;
//import org.junit.Assert;
//import org.junit.Test;
//
//import javax.json.JsonArray;
//import javax.json.JsonObject;
//import javax.json.JsonString;
//
//public class FunctionalityTest {
//    @Test
//    public void when_QA1_and_BKG_selected_should_return_QA1_BKG_url() {
//        App app = new App();
//        app.domain = "BKG";
//        JsonArray urls = app.getUrls(true, false);
//        JsonObject obj = (JsonObject) urls.get(0);
//        JsonString url = (JsonString) obj.get("QA1");
//        Assert.assertEquals("http://irisqa1.oocl.com/wls_prs_bkg/verification/version.jsp", url.toString());
//    }
//}
