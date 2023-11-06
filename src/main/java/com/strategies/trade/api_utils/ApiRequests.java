package com.strategies.trade.api_utils;

import com.strategies.trade.api_test_beans.*;
import com.strategies.trade.test_data_beans.Exchange;
import com.strategies.trade.test_data_beans.FilePaths;
import com.strategies.trade.utilities.CustomLogging;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.xsoup.XElements;
import us.codecraft.xsoup.Xsoup;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ApiRequests {

    public static CustomResponse<BaseResponse> getSmallCaseBannerDetailsForStock(String kiteStockName) throws IOException {

        Instant start = Instant.now();

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/html");
        headers.put("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        headers.put("accept-language", "en-US,en;q=0.9");

        String url = "https://stocks.tickertape.in/" + kiteStockName.replaceAll(" ", "%20") + "?broker=kite&theme=default";

        HttpResponse response = RestApiUtils.sendingGetRequest(url, headers);
//        CustomLogging.writeLog("Response Code : " + response.getStatusLine().getStatusCode());

        CustomLogging.writeLog("Time taken for LR Location Details api request is " + Duration.between(start, Instant.now()).getSeconds());

        CustomResponse<BaseResponse> lrLocationDetailsResponseCustomResponse = new CustomResponse<>(response, BaseResponse.class);
        return lrLocationDetailsResponseCustomResponse;

    }

    public static CustomResponse<BaseResponse> getScreenerStockDetails(String kiteStockName) throws IOException {

        Instant start = Instant.now();

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/html");
        headers.put("accept", "*/*");
//        headers.put("accept-language", "en-US,en;q=0.9");

        String url = "https://www.screener.in/company/" + kiteStockName/*.replaceAll(" ", "%20")*/;

        HttpResponse response = RestApiUtils.sendingGetRequest(url, headers);
//        CustomLogging.writeLog("Response Code : " + response.getStatusLine().getStatusCode());

        CustomLogging.writeLog("Time taken for api request is " + Duration.between(start, Instant.now()).getSeconds());

        CustomResponse<BaseResponse> screenerStockDetailsResponseCustomResponse = new CustomResponse<>(response, BaseResponse.class);
        return screenerStockDetailsResponseCustomResponse;

    }

    public static CustomResponse<IndexResponse> getIndexDetails(String indexName) throws IOException {
        indexName = indexName.replace(" ", "%20");
        indexName = indexName.replace("&", "%26");
        String urlString = ApiUrl.NSE_INDEX_DATA_URL
                .replace("${INDEX_NAME}", indexName);
        URLEncoder.encode(urlString, StandardCharsets.UTF_8.toString());

        HashMap<String, String> headers = new HashMap<>();
        headers.put("authority", "www.nseindia.com");
        headers.put("Accept", "*/*");
        headers.put("Referer", ApiUrl.NSE_MARKET_DATA_REFERER.replace("${SYMBOL_NAME}", indexName));
        headers.put("accept-language", "en-US,en;q=0.9");
        //Cookie is important here. Can get it from API calls of https://www.nseindia.com/market-data/live-market-indices by clicking on any Index
        headers.put("cookie", "_ga=GA1.2.1894959814.1643828063; _gid=GA1.2.818958865.1644086855; nsit=9FyZum6qjaZZ8Sm_D31wlI_F; AKA_A2=A; bm_mi=3F66E32D259A7C9BCCCD529D5D0EDFFC~MLh4wZRyt50Mx4My3fXJRZixFDkg6mzTOXY7W0eXFOaJnOmFovC3wFWXba8qyTrrl4yHT8bcz8gxj7V51FULrM+tQfCKb7cYE3mg1HVV3S96oM0KaxvbPdbnQ5neFFmQ3Y2w37ME1UaU3bnIBn+S0ka+VW88+AGFOXpXs/wOuc0sO6DliH13nU0G47fLDJS/LMe0Zq2zuIZYCwX6OhwsyCluJwLvto6BYnjITWEMkeW7eEiX0i9v4vv4tm1UHZESgRPkBv4NJFqgB9DWpH+Tb1sJvRqsd4Hn870SnDWaNlhkLc5Hy3QQRGR21Ha1PmRS; _gat=1; ak_bmsc=A96C28895284CA4DD3CE57AD11E0DE21~000000000000000000000000000000~YAAQBNcLFyZMo3t+AQAAsc2szQ6iUJbiuaNKEoZ+yuLCtvvVu3d6kTRD4JFMR76S71NqUpQv4ZwhYzlkmGuVqA9XOq4vmGALSkboBhwQl8naCKUGdE459mD9dbpROe2qZAf39O/DHmOTuc/LiGBC70JbDzxDn9XV3WvJQzacve5EiPX49KUIU4FT9p2g+R46mVxK6RmjkfUzvLgMLp3NVFZSZfxEpm+UCmZ3ezYtcJ0J0bqzwGaerB8g1pEJ4anAEOs7AvaY6j8VpyqAhqBoSi/9KVYiEqfv1jJPH+fQ8mayj6g25EHTnqDlY+FEj2s0PE2GbZYVC3hqLqujdKQDGuLU18We7nBnYNXRdUrYszrG44XKERzBshNsooxoOLomFc3j/G5luayn7b8nSMfrFWInQBxj5T75kTBrNVope5eMFNs5FLMMBmjvs7KXMwTJSgL7GUFfpRjuZ43h1MA=; nseappid=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhcGkubnNlIiwiYXVkIjoiYXBpLm5zZSIsImlhdCI6MTY0NDEyODE3MCwiZXhwIjoxNjQ0MTMxNzcwfQ.mfpTe9f2DsaF_zN7CXkWkqp4DW2jPyPBsmh97hn3RhM; RT=\"z=1&dm=nseindia.com&si=93a172d2-3336-47ec-88a1-6b040c4174ea&ss=kzav3s3b&sl=1&tt=38a&bcn=//684d0d4a.akstat.io/\"; bm_sv=A101296B389F7EEFD48BCD78404D9266~1s7S09+ZHwT8Y56h1pqE1sqRwEsxnPVl04nq6It8fJomjxa4Tja4t9GUMuAj3Eh8t+Ce3R2IVIViHHgrgSQGZa2/RI6GDBXqlWbVkLZUPF+uwfTdJLTbfXZPuj5DiuSEvlm75z6r2Fp7exKSyRPMH82r9n/f85q6qjseyT9VpMs=");

        HttpResponse response = RestApiUtils.sendingGetRequest(urlString, headers);

        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println(statusCode);
        if (statusCode == 200) {
            CustomResponse<IndexResponse> indexResponseCustomResponse = new CustomResponse<>(response, IndexResponse.class);
            return indexResponseCustomResponse;
        } else {
            throw new RuntimeException(RestApiUtils.getResponse(response));
        }
    }

    public static CustomResponse<BseIndexResponse> getBseIndexDetails(String indexName) throws IOException {
        indexName = indexName.replace(" ", "%20");
        indexName = indexName.replace("&", "%26");

        HashMap<String, String> headers = new HashMap<>();

        HttpResponse response = RestApiUtils.sendingGetRequest("https://api.bseindia.com/BseIndiaAPI/api/GetMktData/w?ordcol=NS&strType=index&strfilter=" + indexName, headers);

        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println(statusCode);
        if (statusCode == 200) {
            CustomResponse<BseIndexResponse> indexResponseCustomResponse = new CustomResponse<>(response, BseIndexResponse.class);
            return indexResponseCustomResponse;
        } else {
            throw new RuntimeException(RestApiUtils.getResponse(response));
        }
    }

    public static CustomResponse<IndicesList> getIndicesList() throws IOException {

        HashMap<String, String> headers = new HashMap<>();
//        headers.put("authority", "www.nseindia.com");
        headers.put("Accept", "*/*");
//        headers.put("Referer", ApiUrl.NSE_MARKET_DATA_REFERER.replace("${SYMBOL_NAME}", "NIFTY%2050"));
        headers.put("accept-language", "en-US,en;q=0.9");
//        headers.put("cookie", "_ga=GA1.2.606479476.1619331412; _gid=GA1.2.1870324333.1619331412; nsit=hn7tEu3F6aa2IP06ejYGCffx; AKA_A2=A; ak_bmsc=9095DF916699A7FB46095E8F4CAF4D2417CB3F26AE7E0000167585600E6CB32C~pl6X3u/QgJq6qlXnDk12KWi5KSE6NXI+S9C7dZrrZXv8OOB70MVI7vTbzdcKP6dFk5O+UMWhc6fpTwFGHSxUzHngTq+dEj7mY8JUMrDwPiNBlJxqBrPfSz1aJd90s63hSMwgIWlDHgCztYn5Fu9k2BpC2jxB5Ih5/X7JYGbokpoNq5+7DcilXGvbJSDwX1kOCtHKSBzoP2r0Oj8tIhjgnTYZOXvWEI9LJWfBGjmAt01EVIXJ5zv2GyLhBFbSVGq3If; nseappid=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhcGkubnNlIiwiYXVkIjoiYXBpLm5zZSIsImlhdCI6MTYxOTM2MDU1NywiZXhwIjoxNjE5MzY0MTU3fQ.zpuWyldUDwxYgk2rgeZuBJBpyX2Mvg7qvx5dD9sUzgY; _gat_UA-143761337-1=1; bm_sv=E57D95BDBEB0A5B325C0D2E79C5AFB3C~/G13ZJdF37Yn0aF0HfaSLMUOtEGFK0++hvPnkwNZUejICO3Eb0sI0Z89GQrfG1XTe/4K7/QzpvW/KkE0o1+lrZsmC8TA9KC7ArPtZuxWKYNXG7EQ9Dgp1K2fXfgQ1Juqt7JC54GfFdYsuq3I7zruNC3TaHw5ZYytKlZhyU+YoW8=; RT=\"z=1&dm=nseindia.com&si=a97b6a02-dd3c-40b5-a737-8fe7ce1b3360&ss=knx8jb9c&sl=b&tt=psd&bcn=%2F%2F684fc53e.akstat.io%2F&ld=xgdd&nu=ed8553192c8d124be55949cc1b2e99dc&cl=xxme\"");

        HttpResponse response = RestApiUtils.sendingGetRequest(ApiUrl.NSE_INDICES_LIST_URL, headers);

        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println(statusCode);
        if (statusCode == 200) {
            CustomResponse<IndicesList> indicesListCustomResponse = new CustomResponse<>(response, IndicesList.class);

            return indicesListCustomResponse;
        } else {
            throw new RuntimeException("API request failed");
        }
    }

    public static CustomResponse<BseIndicesList> getBseIndicesList() throws IOException {

        HashMap<String, String> headers = new HashMap<>();
        HashMap<String, String> params = new HashMap<>();
        params.put("json", "{\"flag\":\"\",\"ln\":\"en\",\"pg\":\"1\",\"cnt\":\"100\",\"fields\":\"1,2,3,4,5,6,7,8\",\"hmpg\":\"1\"}");

        HttpResponse response = RestApiUtils.sendingGetRequest("https://api.bseindia.com/bseindia/api/Indexmasternew/GetData", headers, params);

        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println(statusCode);
        if (statusCode == 200) {
            CustomResponse<BseIndicesList> indicesListCustomResponse = new CustomResponse<>(response, BseIndicesList.class);

            return indicesListCustomResponse;
        } else {
            throw new RuntimeException("API request failed");
        }
    }

    /**
     * @param stockName stock name
     * @param fromDate  in dd-mm-yyyy format
     * @param toDate    in dd-mm-yyyy format
     * @return response of the request
     * @throws IOException
     */
    public static List<CandleStick> getStockHistoricalData(String stockName, String fromDate, String toDate) throws IOException {

        String urlString = ApiUrl.NSE_HISTORICAL_DATA_URL
                .replace("${stockName}", URLEncoder.encode(stockName, StandardCharsets.UTF_8))
                .replace("${fromDate}", fromDate)
                .replace("${toDate}", toDate);

        HashMap<String, String> headers = new HashMap<>();
        headers.put("accept-language", "en-US,en;q=0.9");
        headers.put("Connection", "keep-alive");
        headers.put("Accept", "*/*");
        headers.put("X-Requested-With", "XMLHttpRequest");
        headers.put("Sec-Fetch-Site", "same-origin");
        headers.put("Sec-Fetch-Mode", "cors");
        headers.put("Sec-Fetch-Dest", "empty");
        headers.put("Referer", "https://www1.nseindia.com/products/content/equities/equities/eq_security.htm");

        HttpResponse response = RestApiUtils.sendingGetRequest(urlString, headers);

        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println(statusCode);
        if (statusCode == 200) {
            String s = EntityUtils.toString(response.getEntity());
            s = Arrays.stream(s.split("\n"))
                    .filter(item -> !item.trim().equalsIgnoreCase(""))
                    .collect(Collectors.joining());

            Document document = Jsoup.parse(s);
            List<String> th = document.getElementsByTag("th").eachText();
            String text = document.getElementsByTag("tr").text();
            String html = document.getElementsByTag("tr").html();
            String outerHtml = document.getElementsByTag("tr").outerHtml();

            outerHtml = outerHtml.replace("<th nowrap>", "")
                    .replace("<td class=\"normalText\" nowrap>", "")
                    .replace("<td class=\"date\" nowrap>", "")
                    .replace("<td class=\"number\" nowrap>", "")
                    .replace("<img src=\"/images/rup_t1.gif\" alt=\"Rs.\" border=\"0\">", " Rs")
                    .replace("<br>", " ")
                    .replace(" style=\"height:20\"", "")
                    .replace("</td>", "")
                    .replace("</th>", "")
                    .replace("<th colspan=\"4\" height=\"25px\">", "")
                    .replace("<td class=\"normalText\" style=\"background-color:yellow !important;\" title=\"SYMBOL CHANGE\" nowrap>", "");

            List<CandleStick> collect1 = Arrays.stream(outerHtml.split("</tr>"))
                    .skip(1)
                    .map(String::trim)
                    .map(item -> Arrays.stream(item.split("\n")).map(String::trim).filter(item1 -> !item1.startsWith("<tr")).map(String::trim).collect(Collectors.toList()))
                    .filter(item -> !item.isEmpty())
                    .map(item -> new CandleStick(item))
                    .collect(Collectors.toList());

            return collect1;
        } else {
            throw new RuntimeException("API request failed");
        }
    }

    public static List<CandleStick> getStockHistoricalData(String stockName, LocalDate start, LocalDate end) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return getStockHistoricalData(stockName, start.format(formatter), end.format(formatter));
    }

    public static List<CandleStick> getStockHistoricalDataFor3Months(String stockName, int month, int year) throws IOException {

        LocalDate start = LocalDate.now()
                .withMonth(month)
                .withYear(year)
                .withDayOfMonth(1);

        LocalDate end = start.plusMonths(3).minusDays(1);
        end.withDayOfMonth(end.lengthOfMonth());

        return getStockHistoricalData(stockName, start, end);
    }

    public static List<CandleStick> getStockHistoricalDataFor3Months(String stockName, LocalDate startDate) throws IOException {
        LocalDate endDate = startDate.plusMonths(3).minusDays(1);
//        endDate.withDayOfMonth(endDate.lengthOfMonth());

        return getStockHistoricalData(stockName, startDate, endDate);

    }

    public static CandleStick getStockHistoricalDataForSingleDay(String stockName, LocalDate date) throws IOException {
        return getStockHistoricalData(stockName, date, date).get(0);
    }

    public static CustomResponse<TradingviewClosePrice> getTradingViewClosePrice(Exchange exchange, List<String> securityNames_NSE) throws IOException {

        String collect = securityNames_NSE.stream()
                .map(item -> exchange.name().toUpperCase() + ":" + item)
                .collect(Collectors.joining("\",\""));
        collect = "\"" + collect + "\"";
        System.out.println(collect);
        String requestString = Files.lines(Paths.get(FilePaths.TRADING_VIEW_LTP_JSON_REQUEST_FILE_PATH))
                .collect(Collectors.joining("\n"))
                .replace("${NSE_CODES}", collect);

        HttpResponse response = RestApiUtils.sendingPostRequest(ApiUrl.TRADING_VIEW_SCAN_URL, requestString);
        CustomResponse<TradingviewClosePrice> lrLocationDetailsResponseCustomResponse = new CustomResponse<>(response, TradingviewClosePrice.class);

        return lrLocationDetailsResponseCustomResponse;

    }
//
//    public static String getGoogleFinanceClosePrice(Exchange exchange, String stockSymbol) throws IOException {
//        return getGoogleFinanceDetails(exchange, stockSymbol).getLtp();
//    }

    public static GoogleFinanceStockObject getGoogleFinanceDetails(Exchange exchange, String stockSymbol) throws IOException {
        HttpResponse response = RestApiUtils.sendingGetRequest("https://www.google.com/finance/quote/"
                + stockSymbol.toUpperCase() + ":" + exchange.getSymbol(), new HashMap<>());

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
//            String htmlResponse = JavaUtils.executeCommand("curl https://www.google.com/finance/quote/FEDERALBNK:NSE".split(" "));
            String htmlResponse = EntityUtils.toString(response.getEntity());
            htmlResponse = Arrays.stream(htmlResponse.split("\n"))
                    .filter(item -> !item.trim().equalsIgnoreCase(""))
                    .collect(Collectors.joining());

            Document document = Jsoup.parse(htmlResponse);

            String ltp = String.valueOf(Xsoup.compile("//div[@data-last-price]//span/div/div/text()").evaluate(document));
            String peRatio = null;
            XElements evaluate = Xsoup.compile("//div[@aria-labelledby='key-stats-heading']/div").evaluate(document);
            Elements elements = evaluate.getElements();
            for (Element element : elements) {
                String attributeName = null;
                try {
                    Element span = element.getElementsByTag("span").get(0);
                    attributeName = span.getElementsByTag("div").get(0).text().trim();
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    throw e;
                }
                if (attributeName.equalsIgnoreCase("P/E ratio")) {
                    peRatio = element.child(1).text();
                    break;
                }
            }
//            String peRatio = String.valueOf(Xsoup.compile("//div[@aria-labelledby='key-stats-heading']//span[div[text()='P/E ratio']]/following-sibling::div/text()").evaluate(document));

            ltp = ltp.replace("₹", "")
                    .replace(",", "")
                    .trim();
            peRatio = peRatio.replace(",", "").trim();

            return new GoogleFinanceStockObject(ltp, peRatio);
        } else
            System.out.println(statusCode);
        return null;
    }

    public static CustomResponse<VacsineResponse> getVaccineDetails() throws IOException {
        String urlString = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByDistrict?district_id=5&date="
                + LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        HashMap<String, String> headers = new HashMap<>();
        headers.put("authority", "cdn-api.co-vin.in");
        headers.put("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"90\", \"Microsoft Edge\";v=\"90\"");
        headers.put("accept", "application/json, text/plain, */*");
//        headers.put("authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJjNWQ3OTU5MC0wMzBhLTQzYjctYjI0Ni00MjBmMWEzNWFmMjEiLCJ1c2VyX2lkIjoiYzVkNzk1OTAtMDMwYS00M2I3LWIyNDYtNDIwZjFhMzVhZjIxIiwidXNlcl90eXBlIjoiQkVORUZJQ0lBUlkiLCJtb2JpbGVfbnVtYmVyIjo5OTQwMjIyMDEwLCJiZW5lZmljaWFyeV9yZWZlcmVuY2VfaWQiOjE5NTM2NTc1OTI2MjcwLCJ1YSI6Ik1vemlsbGEvNS4wIChNYWNpbnRvc2g7IEludGVsIE1hYyBPUyBYIDEwXzE1XzcpIEFwcGxlV2ViS2l0LzUzNy4zNiAoS0hUTUwsIGxpa2UgR2Vja28pIENocm9tZS85MC4wLjQ0MzAuOTMgU2FmYXJpLzUzNy4zNiBFZGcvOTAuMC44MTguNTEiLCJkYXRlX21vZGlmaWVkIjoiMjAyMS0wNS0wMVQwNzowNTozNS40NzNaIiwiaWF0IjoxNjE5ODUyNzM1LCJleHAiOjE2MTk4NTM2MzV9.u8aIMmbUa2r6jOvokPlVucPOxNjU1ETxuqwsYQC26sk");
        headers.put("sec-ch-ua-mobile", "?0");
        headers.put("origin", "https://www.cowin.gov.in");
        headers.put("sec-fetch-site", "cross-site");
        headers.put("sec-fetch-mode", "cors");
        headers.put("sec-fetch-dest", "empty");
        headers.put("referer", "https://www.cowin.gov.in");
        headers.put("accept-language", "en-US,en;q=0.9");
//        headers.put("if-none-match", "W/\"23a32-9Wp/MpisGQBMbgXNnRb6dthAQ28\"");

        HttpResponse response = RestApiUtils.sendingGetRequest(urlString, headers);

        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println(statusCode);
        if (statusCode == 200) {
            CustomResponse<VacsineResponse> vacsineResponseCustomResponse = new CustomResponse<>(response, VacsineResponse.class);
            return vacsineResponseCustomResponse;
        } else {
            throw new RuntimeException("API request failed");
        }
    }
}