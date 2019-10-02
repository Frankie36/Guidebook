package com.mystique.guidebook;

import android.os.Build;

import com.google.gson.Gson;
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

import javax.inject.Inject;

import okhttp3.ResponseBody;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Response;

import static androidx.test.InstrumentationRegistry.getContext;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.O_MR1)
public class MainActivityTest {

    @Inject
    private ApiInterface apiInterface;

    @Rule
    public MockWebServer mockBackend = new MockWebServer();

    @Before
    public void setUp() {
        configureDi();
    }

    private void configureDi() {

    }

    private ApiInterface mockApi() {
        return MainApplication.getInstance().provideRetrofit(mockBackend.url("/").toString()).create(ApiInterface.class);
    }

    private String fileName = "response_ok";

    //Test to show our builder is configured correctly
    @Test
    public void getsBuilderInstance() {
        assertNotNull(apiInterface);
    }

    //Test to show if our endpoint call and config is correct
    //i.e if is correct request type and if path is correct
    @Test
    public void getsExpectedRequest() throws IOException, InterruptedException {
        try {
            mockBackend.enqueue(new MockResponse()
                    .setBody(RestServiceTestHelper.getStringFromFile(getContext(), fileName))
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
                            .setBody(RestServiceTestHelper.getStringFromFile(getContext(), fileName))
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
                            .setBody(RestServiceTestHelper.getStringFromFile(getContext(), fileName))
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
            expectedResponse = RestServiceTestHelper.getStringFromFile(getContext(), fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mockBackend.enqueue(new MockResponse()
                .setBody(expectedResponse)
        );

        final Response<GuideResponse> response = mockApi().getData().execute();
        final GuideResponse guideResponse = response.body();

        assertEquals(expectedResponse, new Gson().toJson(guideResponse));
        //assertEquals("Many pages make a thick book.", guideResponse.guideList().get(0));
    }

    @After
    public void tearDown() throws Exception {
        mockBackend.shutdown();
    }
}
