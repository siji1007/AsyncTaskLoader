package com.example.asynctaskloader;

import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FetchBook extends AsyncTask<String, Void, String> {

    private TextView mTitleText;
    private TextView mAuthorText;

    public FetchBook(TextView titleText, TextView authorText) {
        mTitleText = titleText;
        mAuthorText = authorText;
    }

    @Override
    protected String doInBackground(String... strings) {
        // Fetch book information from NetworkUtils
        return NetworkUtils.getBookInfo(strings[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        // Check if the response is not null and parse the JSON
        if (s != null) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray itemsArray = jsonObject.getJSONArray("items");

                if (itemsArray.length() > 0) {
                    JSONObject book = itemsArray.getJSONObject(0);
                    JSONObject volumeInfo = book.getJSONObject("volumeInfo");

                    // Get the title and author
                    String title = volumeInfo.getString("title");
                    JSONArray authorsArray = volumeInfo.getJSONArray("authors");
                    String author = (authorsArray.length() > 0) ? authorsArray.getString(0) : "Unknown Author";

                    // Display the title and author in the respective TextViews
                    mTitleText.setText("Title: " + title);
                    mAuthorText.setText("Author: " + author);
                } else {
                    // No books found
                    mTitleText.setText("No books found");
                    mAuthorText.setText("");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                // Handle JSON parsing error
                mTitleText.setText("Error parsing JSON");
                mAuthorText.setText("");
            }
        } else {
            // Response is null
            mTitleText.setText("No response");
            mAuthorText.setText("");
        }
    }

}
