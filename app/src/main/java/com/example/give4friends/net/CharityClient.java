package com.example.give4friends.net;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class CharityClient {

    OkHttpClient client;

    public CharityClient(){
        this.client = new OkHttpClient();



    }

    public OkHttpClient getClient() {
        return client;
    }

    public void getCharities(final String search, boolean search_by_name, Callback handler){
        /*
        Parameter:
            String search - String of what you want to be searched by the API
            boolean search_by_name - boolean parameter. Input true if you want to search the API by name
        Output:
            String array
         */

        Integer pageNum = 1;
        Integer pageSize = 20;
        String APP_ID  = "45f1c9cb";
        String API_KEY = "a6ec0399e2688d91fe0d419cf84443d0";
        String searchType = search_by_name ? "NAME_ONLY" : "DEFAULT";

        String base_url = "https://api.data.charitynavigator.org/v2/Organizations";
        //app_id code from the account
        base_url = base_url + "?" + "app_id=" + APP_ID;
        //API key from the account
        base_url = base_url + "&" + "app_key=" + API_KEY;
        //Number of results per page
        base_url = base_url + "&" + "pageSize=" + pageSize.toString();
        //The number of pages
        base_url = base_url + "&" + "pageNum=" + pageNum.toString();
        // Is the charity rated
        base_url = base_url + "&" + "rated=" + "true";

        if (!search.equals("")){
            base_url = base_url + "&" + "search=" + search;
        }
        base_url = base_url + "&" + "searchType=" + searchType;
        Request request = new Request.Builder()
                .url(base_url)
                .build();

        client.newCall(request).enqueue(handler);


    }

}
