package com.strategies.trade.copied;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * @author hemasundarpenugonda
 */
public class TestNseApi {

    @Test
    public void test() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://www1.nseindia.com/products/dynaContent/common/productsSymbolMapping.jsp?symbol=HDFC&segmentLink=3&symbolCount=2&series=EQ&dateRange=24month&fromDate=&toDate=&dataType=PRICEVOLUMEDELIVERABLE")
                .method("GET", null)
                .addHeader("Connection", "keep-alive")
                .addHeader("Accept", "*/*")
                .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 11_2_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36 Edg/89.0.774.63")
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .addHeader("Sec-Fetch-Site", "same-origin")
                .addHeader("Sec-Fetch-Mode", "cors")
                .addHeader("Sec-Fetch-Dest", "empty")
                .addHeader("Referer", "https://www1.nseindia.com/products/content/equities/equities/eq_security.htm")
                .addHeader("Accept-Language", "en-US,en;q=0.9")
//                .addHeader("Cookie", "_ga=GA1.2.1514032934.1616752736; _gid=GA1.2.283623305.1616869949; NSE-TEST-1=1910513674.20480.0000; JSESSIONID=15BFC2A4FFBAD9B643D40FFB000230A6.tomcat2; ak_bmsc=354C474BAAB12DD0F250FFC7C1CD9D84B81A367F4C2A00007E536060F3C48B1A~plL4mJRDi7aebU5pOGI4MKRdi7xTHpEqBw5WyUwW5O1LzIx3+YGUeMyF2CoSICYXcyPHfK3GW3Drf4tMKGx52X7ROzcBmbGI8d6calywe2J9sSFpuYSozOyBVPmLuo4JC1qa2w2yM4S6WihLm1vBeGo7vHY8PKXsXzFYsNZK1cO8vlMSUe/e2mjQlwZ/oz0UpI3EY8vb4V7nHsn6d8zni0AwNIRqydO1QZ/GJwCk8UMX8=; RT=\"z=1&dm=nseindia.com&si=8ca0ad6a-8617-444c-bad8-f7661cafe513&ss=kms89uhn&sl=0&tt=0&bcn=%2F%2F684d0d3e.akstat.io%2F\"; bm_mi=80D28F88BDEEBDCBFC8E6B09DD5E6881~jD7DU6g339eevGthunTjZO8623bBQG77IW+R3fmqmJhlLwUQ5fiRIHOl2S+2EfaGxKDJV9dDkRLVkdj0furQsgUkkVt/zWYFY3LGwmSO2te4tFhoUBSuN56DNSuZKPr8rZxEhG80K0Iukzt6Qyhb5XJHSBIEnV1BeOFw0ZbPJ+QQe52/djdL343EWDY4/dwqXRCH2UwZ7vIHB2ELPugojMW+yIqKy+G5CH9wSotu/Teb9H9Yu5NfB8XlG2rdwpwuYO7ZU+nDFtbd0bz9Sh0xXw==; bm_sv=7CD6269F8E2F73E81931741F133686A9~sop92As4d7T/nKqIAnq9yrKfhMZ7Ur7sTcj68Vw1pw05/irvhFp1t0hMvWew8W1TO4flazcyYqXXaeq9sF3UkEofYvY3HR+ofkpMHMShw3TbFw+eqyooHuITxT77YKLpZ4L704UFv3YJKETFJg3alaFKgKLDAifW64QY0/cEwsI=; bm_mi=80D28F88BDEEBDCBFC8E6B09DD5E6881~jD7DU6g339eevGthunTjZO8623bBQG77IW+R3fmqmJhlLwUQ5fiRIHOl2S+2EfaGeJnJAbCa07Sva98RGfdYoLfmOrAILmw4+YN15TvUk+UmdwBE/4spzHqmq0HBBV0epxYozCo1ZdzIewiQE9YVRIfZEvTpoa5Nfu1wmIFIOujxH4Z9rolArGM7ZFGiK+doPUx3jXMgvmBAtvc6vw1GjhzJlwX7H/SmxZMBlLnZ75r4UgMWznGsjZGu0Si3P1RaUfxNaI2PHvRs9/Z8jEHS4m/e07dLRQKLk7h6s+rZpfE=; bm_sv=7CD6269F8E2F73E81931741F133686A9~sop92As4d7T/nKqIAnq9yrKfhMZ7Ur7sTcj68Vw1pw05/irvhFp1t0hMvWew8W1TO4flazcyYqXXaeq9sF3UkEofYvY3HR+ofkpMHMShw3Tik3g6clhCa6w3jXv4rCHKK7IKMrtHpBlZWLgkKyQWaLkWTwFR9SjGJxHrjmXyR2I=; JSESSIONID=858CC3922E2AAB1534D38498C6C000BE.tomcat2; NSE-TEST-1=1910513674.20480.0000")
                .build();
        Response response = client.newCall(request).execute();

        System.out.println(response.body().string());
    }
}
