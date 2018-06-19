package br.upe.basileiro.data;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.translate.Translate;
import com.google.api.services.translate.model.TranslationsListResponse;
import com.google.api.services.translate.model.TranslationsResource;

public class GoogleTranslate {

	public String translate(String data) {
		String textTranslated = null;
		
		try {
			Translate t = new Translate.Builder(
	                GoogleNetHttpTransport.newTrustedTransport()
	                , GsonFactory.getDefaultInstance(), null)
	                // Set your application name
	                .setApplicationName("Stackoverflow-Example")
	                .build();
	        Translate.Translations.List list = t.new Translations().list(
	                Arrays.asList(data),
	                // Target language
	                "en");
	
	        list.setKey("AIzaSyDFfDTSZ5tYDEgMx7_b6bGxoYsQ1bvMjhg");
	        TranslationsListResponse response = list.execute();
	        for (TranslationsResource translationsResource : response.getTranslations())
	        {
	        	textTranslated = translationsResource.getTranslatedText();
	        }
		} catch(Exception e) {
			e.printStackTrace();
		}
        
        return textTranslated;
	}
	
}
