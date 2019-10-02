package com.mystique.guidebook;

import android.content.Context;
import android.os.Build;

import androidx.test.platform.app.InstrumentationRegistry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mystique.guidebook.io.ApiInterface;
import com.mystique.guidebook.model.GuideResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.O_MR1)
public class MainActivityTest {

    Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

    @Rule
    public MockWebServer mockBackend = new MockWebServer();

    @Before
    public void setUp() {
        mockApi();
    }

    public Retrofit provideRetrofit(String url) {
        return new Retrofit.Builder()
                .baseUrl(url)
                //Custom converters should come before Gson converter
                .addConverterFactory(GsonConverterFactory.create((new GsonBuilder()
                        .setLenient()
                        .create())))
                .client(new OkHttpClient())
                .build();
    }

    private ApiInterface mockApi() {
        return provideRetrofit(mockBackend.url("/").toString()).create(ApiInterface.class);
    }

    private String fileName = "response_ok";

    //Test to show our builder is configured correctly
    @Test
    public void getsBuilderInstance() {
        final ApiInterface messagesApi = provideRetrofit(Constants.BASE_URL).create(ApiInterface.class);
        assertNotNull(messagesApi);
    }

    //Test to show if our endpoint call and config is correct
    //i.e if is correct request type and if path is correct
    @Test
    public void getsExpectedRequest() throws IOException, InterruptedException {
        try {
            mockBackend.enqueue(new MockResponse()
                    .setBody(RestServiceTestHelper.getStringFromFile(context, fileName))
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        mockApi().getData().execute();

        final RecordedRequest recordedRequest = mockBackend.takeRequest();
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/upcomingGuides", recordedRequest.getPath());
    }

    //Test to show if our endpoint call and config is correct
    //i.e if contentType is set correctly
    @Test
    public void getsScriptedSuccessResponseFromMockBackend() throws IOException {
        try {
            mockBackend.enqueue(
                    new MockResponse()
                            .setBody(RestServiceTestHelper.getStringFromFile(context, fileName))
                            .setResponseCode(200)
                            .addHeader("Content-Type", "application/json;charset=utf-8")
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        final Response<GuideResponse> response = mockApi().getData().execute();

        assertEquals(200, response.code());
        assertTrue(response.headers().get("Content-Type").contains("application/json"));

        final ResponseBody rawResponseBody = response.raw().body();
        assertEquals("application", rawResponseBody.contentType().type());
        assertEquals("json", rawResponseBody.contentType().subtype());
    }

    //Test to find out if our retrofit instance shows error response
    @Test
    public void getsScriptedErrorResponseFromMockBackend() throws IOException {
        String fileName = "quote_500";

        try {
            mockBackend.enqueue(
                    new MockResponse()
                            .setBody(RestServiceTestHelper.getStringFromFile(context, fileName))
                            .setResponseCode(500)
                            .addHeader("Content-Type", "application/json;charset=utf-8")
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        final Response<GuideResponse> response = mockApi().getData().execute();

        assertEquals(500, response.code());

        final GuideResponse fortuneResponse = response.body();
        assertNull(fortuneResponse);
    }

    //Test to find out if json is serialized successfully
    @Test
    public void getsEntityObject() throws IOException {

        String expectedResponse = "";

        try {
            expectedResponse = RestServiceTestHelper.getStringFromFile(context, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mockBackend.enqueue(new MockResponse()
                .setBody(expectedResponse)
        );

        final Response<GuideResponse> response = mockApi().getData().execute();
        final GuideResponse guideResponse = response.body();

        assertEquals("{\"total\":\"150\",\"data\":[{\"startDate\":\"Oct 05, 2019\",\"objType\":\"guide\",\"endDate\":\"Oct 05, 2019\",\"name\":\"Akron - Abernathy Construction Open House\",\"loginRequired\":false,\"url\":\"/guide/166682\",\"venue\":{},\"icon\":\"https://s3.amazonaws.com/media.guidebook.com/service/79PYfygE5bDED8KIjwHQVOX5OSX5AGFgoJ411qgO/logo.png\"},{\"startDate\":\"Oct 05, 2019\",\"objType\":\"guide\",\"endDate\":\"Oct 05, 2019\",\"name\":\"Belmont - Heathcliffe Construction Open House\",\"loginRequired\":false,\"url\":\"/guide/166613\",\"venue\":{},\"icon\":\"https://s3.amazonaws.com/media.guidebook.com/service/mYCUTUx1lHgYIJvKtbFxlqrYWpmHuKLRFQMDlepP/logo.png\"}]}",
                new Gson().toJson(guideResponse));
    }

    @After
    public void tearDown() throws Exception {
        mockBackend.shutdown();
    }
}
