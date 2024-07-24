package com.sjz.community.provider;

import com.alibaba.fastjson2.JSON;
import com.sjz.community.dto.AccessTokenDTO;
import com.sjz.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GithubProvider {
    public String getAccessTokenDTO(AccessTokenDTO accessTokenDTO){
        MediaType mediaType = MediaType.get("application/json");

        OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(mediaType,JSON.toJSONString(accessTokenDTO) );
            Request request = new Request.Builder()
                    .url("https://github.com/login/oauth/access_token")
                    .post(body)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                String string = response.body().string();
                System.out.println(string);
                String[] split = string.split("&");
                String access_token = split[0].split("=")[1];
                return access_token;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
    }
    public GithubUser getGithubUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://api.github.com/user")
                    .header("Authorization", "token " + accessToken)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                String user = response.body().string();
                GithubUser githubUser = JSON.parseObject(user, GithubUser.class);
                return githubUser;
            } catch (IOException e) {
                e.printStackTrace();
            }
        return null;
    }
}
