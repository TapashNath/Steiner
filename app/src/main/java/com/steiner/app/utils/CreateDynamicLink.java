package com.steiner.app.utils;

import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

public class CreateDynamicLink {

    private AppCompatActivity activity;
    private Uri shortLink;

    public CreateDynamicLink(AppCompatActivity activity) {
        this.activity = activity;
    }

    public Uri createDynamicLink_Advanced(String title, String details, String image, String itemId, String category) {
        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://www.google.com/?category="+category+"&itemId="+itemId))
                .setDomainUriPrefix("https://steiner.page.link/")
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder("com.steiner.app")
                                .setMinimumVersion(1)
                                .build())
                .setSocialMetaTagParameters(
                        new DynamicLink.SocialMetaTagParameters.Builder()
                                .setTitle(title)
                                .setDescription(details)
                                .setImageUrl(Uri.parse(image))
                                .build())

                .buildDynamicLink();

        Uri dynamicLinkUri = dynamicLink.getUri();
//        Config.Log(String.valueOf(dynamicLinkUri));


        return dynamicLinkUri;
    }
}
