package com.httprequest.HttpRequest;

import com.google.gson.Gson;
import com.httprequest.HttpRequest.Entity.Transcript;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@SpringBootApplication
public class HttpRequestApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(HttpRequestApplication.class, args);
		Transcript transcript = new Transcript();
		transcript.setAudio_url(
				"https://github.com/Viinutha/audiofiletotest/blob/main/Downloads/audiofiletotest/Thirsty.mp4?raw=true"
		);
		Gson gson = new Gson();
		String jsonObject = gson.toJson(transcript);
		HttpRequest postRequest = HttpRequest.newBuilder().uri(new URI("https://api.assemblyai.com/v2/transcript")).header("authorization","6918fb0dff2c41369452fa3073536b4d").POST(HttpRequest.BodyPublishers.ofString(jsonObject)).build();
		HttpClient httpClient = HttpClient.newHttpClient();
		HttpResponse<String> httpPostResponse = httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
		transcript = gson.fromJson(httpPostResponse.body(),Transcript.class);


		HttpRequest getRequest = HttpRequest.newBuilder().uri(new URI("https://api.assemblyai.com/v2/transcript/"+transcript.getId())).header("authorization","6918fb0dff2c41369452fa3073536b4d").GET().build();
		while(true) {
			HttpResponse<String> httpGetResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
			transcript = gson.fromJson(httpGetResponse.body(), Transcript.class);

			System.out.println(transcript.getStatus());
			if(transcript.getStatus().equalsIgnoreCase("completed")||transcript.getStatus().equals("error")){
				System.out.println(transcript.getText());
				break;
			}
			Thread.sleep(1000);
		}
		//httpGetResponse.body()
	}

}
