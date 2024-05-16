package com.UgoWithoutH.SongsTextToYoutubePlaylist;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.*;
import java.util.*;

@SpringBootApplication
@Slf4j
public class SongsTextToYoutubePlaylistApplication {
	private static final String SEARCH_VIDEO_URI = "https://www.googleapis.com/youtube/v3/search";
	private static final String ADD_VIDEO_PLAYLIST_URI = "https://www.googleapis.com/youtube/v3/playlistItems";
	private static final String PLAYLIST_ID = "PLXdDvjKrFu6iMw4DCYTc0uhIdaYopoeqT";
	private static final String API_KEY = "XXX";
	private static final String BearerToken = "XXX";
	private static final WebClient client = WebClient.create();
	private static final Gson gson = new Gson();

	public static void main(String[] args) throws Exception {
		SpringApplication.run(SongsTextToYoutubePlaylistApplication.class, args);
		//copyProcess();
		//checkProcess();
		//deleteDuplicateMusic();
		//deleteDuplicateMusicWithoutExt();
		//checkDownloadedProcess();
		//copyRestProcess();
		deleteSameSongs();
	}

	public static void checkDownloadedProcess(){
		List<classes.VideoYT> youtubeItemsSong = getYoutubeSongs("/youtbe_songs.json");
		List<String> downloadedSong = new ArrayList<>();
		String cheminDossier = "C:\\Users\\ugovi\\OneDrive\\Bureau\\musiques";

		File dossier = new File(cheminDossier);
		
		if (dossier.isDirectory()) {
			File[] fichiers = dossier.listFiles();

			if (fichiers != null) {
				for (File fichier : fichiers) {
					if (fichier.isFile()) {
						String nomFichier = fichier.getName();
						downloadedSong.add(nomFichier);
					}
				}
			}
		}

		for (classes.VideoYT video : youtubeItemsSong){
			boolean find = false;
			for (String downloaded : downloadedSong){
				String nomBase = downloaded.substring(0, downloaded.lastIndexOf('.'));
				String extension = downloaded.substring(downloaded.lastIndexOf('.'));

				if(video.snippet.title.equals(nomBase) && extension.equals(".mp3")){
					find = true;
				}
			}
			if(!find){
				System.out.println(video.snippet.title);
			}
		}
	}

	public static void deleteSameSongs(){
		String cheminDossier = "C:\\Users\\ugovi\\OneDrive\\Bureau\\musiques";
		var scanner = new Scanner(System.in);
		File dossier = new File(cheminDossier);

		if (dossier.isDirectory()) {
			Map<String, String> nomsFichiers = new HashMap<>();

			File[] fichiers = dossier.listFiles();
			for (File fichier : fichiers){
				if (fichier.isFile()) {
					String nomFichier = fichier.getName();
					String nomBase = nomFichier.substring(0, nomFichier.lastIndexOf('.'));
					String extension = nomFichier.substring(nomFichier.lastIndexOf('.'));

					List<String> split = Arrays.stream(nomBase.split(" ")).toList();

					int count = (int) (split.size() * 0.80);
					boolean find = false;
					for(var entry : nomsFichiers.entrySet()){
						int currentCount = 0;
						var currentSplit = Arrays.stream(entry.getKey().split(" ")).toList();
						for (String it : split){
							if(currentSplit.contains(it))
								currentCount++;
						}

						if(currentCount >= count){
							find = true;
							System.out.println("fichier déjà présent : " + entry.getKey());
							System.out.println("fichier à supprimer  : " + nomBase);
							var choix = scanner.nextLine().toLowerCase();
							if(choix.equals("o")){
								if (fichier.delete()) {
									System.out.println("Le fichier en double " + fichier.getName() + " a été supprimé.");
								}
							}
						}
					}

					if(!find)
						nomsFichiers.put(nomBase, fichier.getAbsolutePath());
				}
			}
		}
	}

	public static void deleteDuplicateMusicWithoutExt(){
		String cheminDossier = "C:\\Users\\ugovi\\OneDrive\\Bureau\\musiques";

		File dossier = new File(cheminDossier);

		if (dossier.isDirectory()) {
			Map<String, List<String>> nomsFichiers = new HashMap<>();

			File[] fichiers = dossier.listFiles();

			if (fichiers != null) {
				for (File fichier : fichiers){
					if (fichier.isFile()) {
						String nomFichier = fichier.getName();
						String nomBase = nomFichier.substring(0, nomFichier.lastIndexOf('.'));
						String extension = nomFichier.substring(nomFichier.lastIndexOf('.'));

						if(extension.equals(".mp3")){
							List<String> list = new ArrayList<>();
							list.add(fichier.getAbsolutePath());
							list.add(extension);
							nomsFichiers.put(nomBase, list);
						}
					}
				}

				for (File fichier : fichiers) {
					if (fichier.isFile()) {
						String nomFichier = fichier.getName();
						String nomBase = nomFichier.substring(0, nomFichier.lastIndexOf('.'));
						String extension = nomFichier.substring(nomFichier.lastIndexOf('.'));
						if (!extension.equals(".mp3") && nomsFichiers.containsKey(nomBase)) {
							String cheminFichierExistant = fichier.getAbsolutePath();
							File fichierExistant = new File(cheminFichierExistant);

							if (fichierExistant.exists() && fichierExistant.isFile()) {
								if (fichierExistant.delete()) {
									System.out.println("Le fichier en double " + fichierExistant.getName() + " a été supprimé.");
								} else {
									System.out.println("Échec de suppression du fichier en double " + fichierExistant.getName());
								}
							}
						}
					}
				}
			} else {
				System.out.println("Le dossier est vide ou inaccessible.");
			}
		} else {
			System.out.println("Le chemin spécifié ne correspond pas à un dossier valide.");
		}
	}

	public static void deleteDuplicateMusic(){
		String cheminDossier = "C:\\Users\\ugovi\\OneDrive\\Bureau\\musiques";

		File dossier = new File(cheminDossier);

		if (dossier.isDirectory()) {
			File[] fichiers = dossier.listFiles();

			if (fichiers != null) {
				for (File fichier : fichiers) {
					String nomFichier = fichier.getName();
					if (nomFichier.contains("(1)") || nomFichier.contains("(2)") || nomFichier.contains("(3)")) {
						if (fichier.delete()) {
							System.out.println("Le fichier " + nomFichier + " a été supprimé.");
						} else {
							System.out.println("Échec de suppression du fichier " + nomFichier);
						}
					}
				}
			} else {
				System.out.println("Le dossier est vide ou inaccessible.");
			}
		} else {
			System.out.println("Le chemin spécifié ne correspond pas à un dossier valide.");
		}
	}

	public static void checkProcess(){
		List<classes.Item> spotifyItemsSong = getSpotifySongs("/songs.json");
		List<classes.VideoYT> youtubeItemsSong = getYoutubeSongs("/youtbe_songs.json");

		for (classes.Item spotifyItem : spotifyItemsSong){
			boolean find = false;
			for (classes.VideoYT youtubeItem : youtubeItemsSong){
				if(youtubeItem.snippet.title.equals(spotifyItem.track.name)){
					find = true;
					break;
				}
			}
			if (!find){
				log.info(spotifyItem.track.name);
			}
		}
	}

	public static void copyRestProcess() throws Exception {
		List<String> songs = new ArrayList<>();
		try {
			InputStream inputStream = SongsTextToYoutubePlaylistApplication.class.getResourceAsStream("/rest_songs.txt");

			if (inputStream != null) {
				InputStreamReader lecteurStreamReader = new InputStreamReader(inputStream);
				BufferedReader lecteurBufferise = new BufferedReader(lecteurStreamReader);

				String ligne;
				while ((ligne = lecteurBufferise.readLine()) != null) {
					songs.add(ligne);
				}

				lecteurBufferise.close();
				lecteurStreamReader.close();
			} else {
				System.out.println("Le fichier spécifié n'existe pas ou ne peut pas être lu.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		for(String song : songs){
			classes.SearchResult searchResult = searchRequest(song);
			addVideoToPlaylistRequest(searchResult.items.get(0));
		}
	}

	public static void copyProcess() throws Exception {
		List<classes.Item> itemsSong = getSpotifySongs("/songs.json");
		for(classes.Item item : itemsSong){
			StringBuilder artists = new StringBuilder();
			for(classes.Artist artist : item.track.artists){
				artists.append(String.format(" %s", artist.name));
			}
			String searchRequest = String.format("%s - %s", artists, item.track.name);
			classes.SearchResult searchResult = searchRequest(searchRequest);
			addVideoToPlaylistRequest(searchResult.items.get(0));
		}
	}

	public static List<classes.VideoYT> getYoutubeSongs(String uri){
		List<classes.VideoYT> result = null;

		try (InputStream inputStream = SongsTextToYoutubePlaylistApplication.class.getResourceAsStream(uri)) {
			if (inputStream != null) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

				classes.YoutubeJsonResult jsonResult = gson.fromJson(reader, classes.YoutubeJsonResult.class);
				result = Arrays.asList(jsonResult.items);

				reader.close();
			} else {
				System.out.println("Fichier introuvable : songs.json");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public static List<classes.Item> getSpotifySongs(String uri){
		List<classes.Item> itemList = null;

		try (InputStream inputStream = SongsTextToYoutubePlaylistApplication.class.getResourceAsStream(uri)) {
			if (inputStream != null) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

				classes.ItemResponse itemResponse = gson.fromJson(reader, classes.ItemResponse.class);
				itemList = Arrays.asList(itemResponse.items);

				reader.close();
			} else {
				System.out.println("Fichier introuvable : songs.json");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return itemList;
	}

	public static classes.SearchResult searchRequest(String searchText){
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(SEARCH_VIDEO_URI);
		log.info("Search for {}", searchText);
		uriBuilder.queryParam("key", API_KEY);
		uriBuilder.queryParam("part", "snippet");
		uriBuilder.queryParam("type", "video");
		uriBuilder.queryParam("maxResults", "1");
		uriBuilder.queryParam("q", searchText);

		ResponseEntity<String> response = client
				.get()
				.uri(uriBuilder.build().toUri())
				.retrieve()
				.toEntity(String.class)
				.block();
		classes.SearchResult searchResult = gson.fromJson(response.getBody(), classes.SearchResult.class);
		log.info("Search result {}", searchResult.items.get(0).snippet.title);
		return searchResult;
	}

	public static void addVideoToPlaylistRequest(classes.Video video) throws Exception {
		log.info("add video {} to playlist", video.snippet.title);
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(ADD_VIDEO_PLAYLIST_URI);
		uriBuilder.queryParam("part", "contentDetails,id,snippet,status");
		String body = String.format("{ \"snippet\": { \"playlistId\": \"%s\", \"resourceId\": { \"kind\": \"youtube#video\", \"videoId\": \"%s\" } } }",PLAYLIST_ID, video.id.videoId);

		try {
			ResponseEntity<String> response = client
					.post()
					.uri(uriBuilder.build().toUri())
					.header(HttpHeaders.AUTHORIZATION, "Bearer " + BearerToken)
					.bodyValue(body)
					.retrieve()
					.toEntity(String.class)
					.block();

		} catch (WebClientResponseException ex) {
			log.error("Erreur lors de la requête : {}", ex.getStatusCode());
			log.error("Corps de la réponse en erreur : {}", ex.getResponseBodyAsString());
			throw new Exception();
		}

	}
}
