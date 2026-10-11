package com.example.gdgoc.study.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class PostHttpTests {

  @Value("${local.server.port}")
  private int port;

  private final HttpClient client =
      HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();

  @Test
  void httpCrudPreservesSuccessContractAndMapsMissingPostsToPlainText404() throws Exception {
    assertJson(request("GET", "/posts"), 200, "[]");

    HttpResponse<String> created =
        request("POST", "/posts", "{\"title\":\"첫 제목\",\"content\":\"첫 내용\"}");

    assertEquals(201, created.statusCode());

    int id = new JSONObject(created.body()).getInt("id");
    String originalJson = "{\"id\":%d,\"title\":\"첫 제목\",\"content\":\"첫 내용\"}".formatted(id);

    assertJson(created, 201, originalJson);
    assertJson(request("GET", "/posts"), 200, "[" + originalJson + "]");
    assertJson(request("GET", "/posts/" + id), 200, originalJson);

    String updatedJson = "{\"id\":%d,\"title\":\"수정 제목\",\"content\":\"수정 내용\"}".formatted(id);

    assertJson(
        request("PUT", "/posts/" + id, "{\"title\":\"수정 제목\",\"content\":\"수정 내용\"}"),
        200,
        updatedJson);
    assertJson(request("GET", "/posts/" + id), 200, updatedJson);
    assertJson(request("GET", "/posts"), 200, "[" + updatedJson + "]");

    int missingId = id + 1;

    for (String method : new String[] {"GET", "PUT", "DELETE"}) {
      String body = method.equals("PUT") ? "{\"title\":\"없는 제목\",\"content\":\"없는 내용\"}" : null;
      HttpResponse<String> response = request(method, "/posts/" + missingId, body);

      assertEquals(404, response.statusCode(), method);
      assertEquals("Post " + missingId + " not found", response.body(), method);
      assertJson(request("GET", "/posts"), 200, "[" + updatedJson + "]");
    }

    HttpResponse<String> deleted = request("DELETE", "/posts/" + id);

    assertEquals(204, deleted.statusCode());
    assertEquals("", deleted.body());
    assertJson(request("GET", "/posts"), 200, "[]");

    for (String method : new String[] {"GET", "DELETE"}) {
      HttpResponse<String> response = request(method, "/posts/" + id);

      assertEquals(404, response.statusCode());
      assertEquals("Post " + id + " not found", response.body());
    }
  }

  private HttpResponse<String> request(String method, String path) throws Exception {
    return request(method, path, null);
  }

  private HttpResponse<String> request(String method, String path, String body) throws Exception {
    HttpRequest.BodyPublisher publisher =
        body == null
            ? HttpRequest.BodyPublishers.noBody()
            : HttpRequest.BodyPublishers.ofString(body);

    HttpRequest request =
        HttpRequest.newBuilder(URI.create("http://localhost:" + port + path))
            .timeout(Duration.ofSeconds(10))
            .header("Content-Type", "application/json")
            .method(method, publisher)
            .build();

    return client.send(request, HttpResponse.BodyHandlers.ofString());
  }

  private void assertJson(HttpResponse<String> response, int status, String expected)
      throws Exception {
    assertEquals(status, response.statusCode());
    JSONAssert.assertEquals(expected, response.body(), true);
  }
}
