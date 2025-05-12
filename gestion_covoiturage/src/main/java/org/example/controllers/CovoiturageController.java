package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.web.WebView;
import javafx.concurrent.Worker;
import org.json.JSONObject;
import org.json.JSONArray;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import org.example.entities.Covoiturage;
import org.example.services.CovoiturageService;
import org.example.services.ConducteurService;
import org.example.services.VoitureService;

public class CovoiturageController {
    @FXML private TextField lieu_departField;
    @FXML private TextField lieu_arriveeField;
    @FXML private TextField date_heureField;
    @FXML private TextField idConducteurCovoitField;
    @FXML private TextField idVoitureField;
    @FXML private TextField places_restantesField;
    @FXML private TextField statutField;
    @FXML private TextField searchLieu_departField;
    @FXML private TextField searchLieu_arriveeField;
    @FXML private TextField searchDate_heureField;
    @FXML private TableView<Covoiturage> covoiturageTable;
    @FXML private Label distanceLabel;
    @FXML private Label durationLabel;
    @FXML private Label errorLabel;
    @FXML private WebView mapView;

    private static final String GRAPHHOPPER_API_KEY = "f2e0d98c-7100-4bad-8b21-7d79c11f250e";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final ObservableList<Covoiturage> covoiturageData = FXCollections.observableArrayList();
    private int covoiturageIdToUpdate = -1;
    private boolean isMapLoaded = false;
    private final CovoiturageService covoiturageService;
    private final ConducteurService conducteurService;
    private final VoitureService voitureService;

    public CovoiturageController() {
        covoiturageService = new CovoiturageService();
        conducteurService = new ConducteurService();
        voitureService = new VoitureService();
    }

    @FXML
    public void initialize() {
        setupCovoiturageTable();
        showCovoiturages();
        initializeMap();
    }

    private void setupCovoiturageTable() {
        TableColumn<Covoiturage, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id_covoiturage"));

        TableColumn<Covoiturage, String> lieu_departCol = new TableColumn<>("Départ");
        lieu_departCol.setCellValueFactory(new PropertyValueFactory<>("lieu_depart"));

        TableColumn<Covoiturage, String> lieu_arriveeCol = new TableColumn<>("Arrivée");
        lieu_arriveeCol.setCellValueFactory(new PropertyValueFactory<>("lieu_arrivee"));

        TableColumn<Covoiturage, String> date_heureCol = new TableColumn<>("Date");
        date_heureCol.setCellValueFactory(new PropertyValueFactory<>("date_heure"));

        TableColumn<Covoiturage, Integer> idConducteurCol = new TableColumn<>("Conducteur ID");
        idConducteurCol.setCellValueFactory(new PropertyValueFactory<>("id_conducteur"));

        TableColumn<Covoiturage, Integer> idVoitureCol = new TableColumn<>("Voiture ID");
        idVoitureCol.setCellValueFactory(new PropertyValueFactory<>("id_voiture"));

        TableColumn<Covoiturage, Integer> places_restantesCol = new TableColumn<>("Places");
        places_restantesCol.setCellValueFactory(new PropertyValueFactory<>("places_restantes"));

        TableColumn<Covoiturage, String> statutCol = new TableColumn<>("Statut");
        statutCol.setCellValueFactory(new PropertyValueFactory<>("statut"));

        covoiturageTable.getColumns().setAll(idCol, lieu_departCol, lieu_arriveeCol, date_heureCol, idConducteurCol, idVoitureCol, places_restantesCol, statutCol);
        covoiturageTable.setItems(covoiturageData);
    }

    private void initializeMap() {
        mapView.getEngine().loadContent(
                "<html>" +
                        "<head>" +
                        "<meta charset='UTF-8'>" +
                        "<style>" +
                        "html, body, #map { height: 100%; width: 100%; margin: 0; padding: 0; overflow: hidden; position: absolute; top: 0; left: 0; }" +
                        "</style>" +
                        "<link rel='stylesheet' href='https://unpkg.com/leaflet@1.9.4/dist/leaflet.css' />" +
                        "<script src='https://unpkg.com/leaflet@1.9.4/dist/leaflet.js'></script>" +
                        "</head>" +
                        "<body>" +
                        "<div id='map'></div>" +
                        "<script>" +
                        "console.log('Starting map initialization...');" +
                        "var map;" +
                        "function initMap() {" +
                        "  try {" +
                        "    map = L.map('map').setView([0, 0], 2);" +
                        "    console.log('Map created');" +
                        "    var tileLayer = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {" +
                        "      maxZoom: 19," +
                        "      attribution: '© OpenStreetMap contributors'" +
                        "    });" +
                        "    tileLayer.on('tileload', function() { console.log('Tile loaded'); });" +
                        "    tileLayer.on('tileerror', function(e) { console.error('Tile error:', JSON.stringify(e)); alert('Erreur de chargement des tuiles: ' + JSON.stringify(e)); });" +
                        "    tileLayer.addTo(map);" +
                        "    console.log('Tile layer added');" +
                        "    setTimeout(function() { map.invalidateSize(); console.log('Map size corrected'); }, 1500);" +
                        "  } catch (e) {" +
                        "    console.error('Erreur initMap:', e);" +
                        "    alert('Erreur d\\'initialisation de la carte: ' + e.message);" +
                        "  }" +
                        "}" +
                        "var leafletLoaded = false;" +
                        "try {" +
                        "  initMap();" +
                        "  leafletLoaded = true;" +
                        "  console.log('Leaflet loaded successfully');" +
                        "} catch (e) {" +
                        "  console.error('Erreur de chargement Leaflet:', e);" +
                        "  alert('Erreur de chargement Leaflet: ' + e.message);" +
                        "}" +
                        "function drawRoute(points) {" +
                        "  try {" +
                        "    if (!Array.isArray(points)) throw new Error('Points n\\'est pas un tableau');" +
                        "    points = points.filter(p => p && typeof p.lat === 'number' && typeof p.lng === 'number');" +
                        "    if (points.length === 0) throw new Error('Points filtrés invalides');" +
                        "    var latlngs = points.map(p => [p.lat, p.lng]);" +
                        "    var polyline = L.polyline(latlngs, { color: 'blue', weight: 5 }).addTo(map);" +
                        "    L.marker(latlngs[0]).addTo(map).bindPopup('Départ').openPopup();" +
                        "    L.marker(latlngs[latlngs.length - 1]).addTo(map).bindPopup('Arrivée');" +
                        "    map.fitBounds(polyline.getBounds(), { padding: [50, 50] });" +
                        "    map.invalidateSize();" +
                        "  } catch (e) {" +
                        "    console.error('Erreur JS drawRoute:', e);" +
                        "    alert('Erreur drawRoute: ' + e.message);" +
                        "  }" +
                        "}" +
                        "</script>" +
                        "</body>" +
                        "</html>"
        );

        mapView.getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                isMapLoaded = true;
                System.out.println("WebView content loaded successfully");
                mapView.getEngine().executeScript(
                        "if (typeof leafletLoaded !== 'undefined' && leafletLoaded) {" +
                                "  console.log('Leaflet is confirmed loaded');" +
                                "} else {" +
                                "  console.log('Leaflet failed to load');" +
                                "}"
                );
                mapView.getEngine().executeScript("setTimeout(() => { if (typeof map !== 'undefined') map.invalidateSize(); }, 1500);");
            } else if (newValue == Worker.State.FAILED) {
                System.out.println("WebView content failed to load");
                errorLabel.setText("Erreur de Carte: Impossible de charger la carte (problème réseau ou Leaflet non chargé).");
            }
        });

        mapView.getEngine().setOnAlert(event -> System.out.println("WebView Console: " + event.getData()));
        mapView.getEngine().setOnError(event -> {
            System.out.println("JavaScript Error: " + event.getMessage());
            errorLabel.setText("Erreur JavaScript: " + event.getMessage());
        });
        mapView.widthProperty().addListener((obs, oldVal, newVal) -> {
            mapView.getEngine().executeScript("if (typeof map !== 'undefined') map.invalidateSize();");
        });
        mapView.heightProperty().addListener((obs, oldVal, newVal) -> {
            mapView.getEngine().executeScript("if (typeof map !== 'undefined') map.invalidateSize();");
        });

        mapView.getEngine().setUserDataDirectory(null);
    }

    @FXML
    private void showRoute() {
        String start = lieu_departField.getText().trim();
        String end = lieu_arriveeField.getText().trim();

        if (start.isEmpty() || end.isEmpty()) {
            errorLabel.setText("Entrée Invalide: Veuillez entrer un lieu de départ et d'arrivée.");
            return;
        }

        if (!isMapLoaded) {
            errorLabel.setText("Carte Non Chargée: La carte n'est pas encore chargée. Veuillez réessayer dans un instant.");
            return;
        }

        try {
            Boolean leafletLoaded = (Boolean) mapView.getEngine().executeScript("typeof leafletLoaded !== 'undefined' && leafletLoaded");
            if (!leafletLoaded) {
                throw new Exception("Leaflet n'est pas chargé. Vérifiez votre connexion internet ou réessayez.");
            }
        } catch (Exception e) {
            errorLabel.setText("Erreur de Chargement: " + e.getMessage());
            return;
        }

        try {
            double[] startCoords = geocode(start);
            double[] endCoords = geocode(end);

            if (startCoords == null || endCoords == null ||
                    Double.isNaN(startCoords[0]) || Double.isNaN(startCoords[1]) ||
                    Double.isNaN(endCoords[0]) || Double.isNaN(endCoords[1])) {
                throw new Exception("Coordonnées invalides pour le départ ou l'arrivée.");
            }

            JSONObject route = getRoute(startCoords, endCoords);
            JSONObject path = route.getJSONArray("paths").getJSONObject(0);
            double distance = path.getDouble("distance") / 1000;
            double duration = path.getDouble("time") / 1000 / 60;
            int durationHours = (int) Math.floor(duration / 60);
            int durationMinutes = (int) Math.floor(duration % 60);

            distanceLabel.setText(String.format("Distance: %.2f km", distance));
            durationLabel.setText(String.format("Durée: %dh %dmin", durationHours, durationMinutes));

            JSONArray coordinates;
            Object points = path.get("points");
            if (points instanceof JSONObject) {
                coordinates = ((JSONObject) points).getJSONArray("coordinates");
            } else if (points instanceof String) {
                coordinates = decodePolyline((String) points);
            } else {
                throw new Exception("Format des points inconnu.");
            }

            JSONArray pointArray = new JSONArray();
            if (coordinates.length() > 0) {
                JSONArray first = coordinates.getJSONArray(0);
                JSONObject p1 = new JSONObject();
                p1.put("lat", first.getDouble(1));
                p1.put("lng", first.getDouble(0));
                pointArray.put(p1);

                for (int i = 10; i < coordinates.length() - 1; i += 10) {
                    JSONArray coord = coordinates.getJSONArray(i);
                    if (coord.length() >= 2) {
                        JSONObject point = new JSONObject();
                        point.put("lat", coord.getDouble(1));
                        point.put("lng", coord.getDouble(0));
                        pointArray.put(point);
                    }
                }

                if (coordinates.length() > 1) {
                    JSONArray last = coordinates.getJSONArray(coordinates.length() - 1);
                    JSONObject p2 = new JSONObject();
                    p2.put("lat", last.getDouble(1));
                    p2.put("lng", last.getDouble(0));
                    pointArray.put(p2);
                }
            }

            String jsScript = String.format(
                    "try {" +
                            "console.log('Starting route drawing...');" +
                            "var points = %s;" +
                            "points = points.filter(t => t && typeof t.lat === 'number' && typeof t.lng === 'number');" +
                            "if (points.length === 0) throw new Error('Points filtrés invalides');" +
                            "var latlngs = points.map(t => [t.lat, t.lng]);" +
                            "var polyline = L.polyline(latlngs, {color: 'blue', weight: 5}).addTo(map);" +
                            "L.marker(latlngs[0]).addTo(map).bindPopup('Départ: %s').openPopup();" +
                            "L.marker(latlngs[latlngs.length - 1]).addTo(map).bindPopup('Arrivée: %s');" +
                            "map.fitBounds(polyline.getBounds(), {padding: [50, 50]});" +
                            "map.invalidateSize();" +
                            "} catch (e) {" +
                            "console.error('Erreur JS drawRoute:', e);" +
                            "alert('Erreur JS: ' + e.message);" +
                            "throw e;" +
                            "}",
                    pointArray.toString(), start.replace("'", "\\'"), end.replace("'", "\\'")
            );

            mapView.getEngine().executeScript(jsScript);

        } catch (Exception e) {
            String errorMessage = e.getMessage();
            errorLabel.setText("Erreur de Trajet: " + errorMessage);
            distanceLabel.setText("Distance: N/A");
            durationLabel.setText("Durée: N/A");
            mapView.getEngine().executeScript(
                    "if (typeof map !== 'undefined') {" +
                            "map.eachLayer(function(layer) { " +
                            "if (layer instanceof L.Polyline || layer instanceof L.Marker) map.removeLayer(layer); " +
                            "});" +
                            "map.setView([0, 0], 2);" +
                            "map.invalidateSize();" +
                            "}"
            );
        }
    }

    private double[] geocode(String address) throws Exception {
        System.out.println("Geocoding address: " + address);
        String encodedAddress = java.net.URLEncoder.encode(address, "UTF-8");
        String url = "https://api.openrouteservice.org/geocode/search?api_key=5b3ce3597851110001cf62488762521e3d71488c8e25a16853af1c04&text=" + encodedAddress + "&size=1";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Geocode response status: " + response.statusCode());
        System.out.println("Geocode response body: " + response.body());

        if (response.statusCode() != 200) {
            throw new Exception("Erreur HTTP " + response.statusCode() + ": " + response.body());
        }

        JSONObject json = new JSONObject(response.body());
        JSONArray features = json.getJSONArray("features");
        if (features.length() == 0) {
            throw new Exception("Lieu non trouvé: " + address);
        }
        JSONArray coords = features.getJSONObject(0).getJSONObject("geometry").getJSONArray("coordinates");
        double[] result = new double[]{coords.getDouble(1), coords.getDouble(0)}; // [lat, lng]
        System.out.println("Geocoded coordinates for " + address + ": lat=" + result[0] + ", lon=" + result[1]);
        return result;
    }

    private JSONObject getRoute(double[] start, double[] end) throws Exception {
        System.out.println("Requesting route from: lat=" + start[0] + ", lon=" + start[1] +
                " to: lat=" + end[0] + ", lon=" + end[1]);
        String url = "https://graphhopper.com/api/1/route";
        JSONObject bodyJson = new JSONObject();
        JSONArray points = new JSONArray();
        points.put(new JSONArray(new double[]{start[1], start[0]})); // [lng, lat]
        points.put(new JSONArray(new double[]{end[1], end[0]}));     // [lng, lat]
        bodyJson.put("points", points);
        bodyJson.put("profile", "car");
        bodyJson.put("instructions", false);
        bodyJson.put("points_encoded", false);
        String body = bodyJson.toString();
        System.out.println("GraphHopper request body: " + body);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "?key=" + GRAPHHOPPER_API_KEY))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("GraphHopper route response status: " + response.statusCode());
        System.out.println("GraphHopper route response body: " + response.body());

        if (response.statusCode() != 200) {
            throw new Exception("Erreur HTTP " + response.statusCode() + ": " + response.body());
        }
        JSONObject jsonResponse = new JSONObject(response.body());
        if (!jsonResponse.has("paths") || jsonResponse.getJSONArray("paths").isEmpty()) {
            throw new Exception("Aucun itinéraire trouvé dans la réponse.");
        }
        return jsonResponse;
    }

    private JSONArray decodePolyline(String encoded) {
        List<double[]> points = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            points.add(new double[]{lng * 1e-5, lat * 1e-5}); // [lon, lat]
        }

        JSONArray coordinates = new JSONArray();
        for (double[] point : points) {
            coordinates.put(new JSONArray(new double[]{point[0], point[1]}));
        }
        return coordinates;
    }

    @FXML
    private void addCovoiturage() {
        if (!isValidNumeric(idConducteurCovoitField.getText()) || !isValidNumeric(idVoitureField.getText()) || !isValidNumeric(places_restantesField.getText())) {
            errorLabel.setText("Entrée Invalide: Les champs ID Conducteur, ID Voiture et Places doivent être numériques.");
            return;
        }

        String lieu_depart = lieu_departField.getText().trim();
        String lieu_arrivee = lieu_arriveeField.getText().trim();
        String date_heure = date_heureField.getText().trim();
        String statut = statutField.getText().trim();

        if (lieu_depart.isEmpty() || lieu_arrivee.isEmpty() || date_heure.isEmpty() || statut.isEmpty()) {
            errorLabel.setText("Champs Vides: Tous les champs doivent être remplis.");
            return;
        }

        if (!isValidDateTime(date_heure)) {
            errorLabel.setText("Format de Date Invalide: La date doit être au format 'yyyy-MM-dd HH:mm:ss' (ex. 2025-05-06 14:30:00).");
            return;
        }

        int idConducteur = Integer.parseInt(idConducteurCovoitField.getText());
        int idVoiture = Integer.parseInt(idVoitureField.getText());
        int placesRestantes = Integer.parseInt(places_restantesField.getText());

        try {
            boolean conducteurExists = conducteurService.getAllConducteurs().stream()
                    .anyMatch(c -> c.getId_conducteur() == idConducteur);
            if (!conducteurExists) {
                errorLabel.setText("Conducteur Inexistant: ID Conducteur " + idConducteur + " n'existe pas.");
                return;
            }

            boolean voitureExists = voitureService.getAllVoitures().stream()
                    .anyMatch(v -> v.getId_voiture() == idVoiture);
            if (!voitureExists) {
                errorLabel.setText("Voiture Inexistante: ID Voiture " + idVoiture + " n'existe pas.");
                return;
            }

            Covoiturage covoiturage = new Covoiturage(
                    covoiturageIdToUpdate == -1 ? 0 : covoiturageIdToUpdate,
                    lieu_depart, lieu_arrivee, date_heure, idConducteur, idVoiture, placesRestantes, statut
            );

            if (covoiturageIdToUpdate == -1) {
                if (covoiturageService.ajouterCovoiturage(covoiturage)) {
                    errorLabel.setText("Succès: Covoiturage ajouté avec succès.");
                } else {
                    errorLabel.setText("Erreur: Échec de l'ajout du covoiturage.");
                }
            } else {
                if (covoiturageService.updateCovoiturage(covoiturage)) {
                    errorLabel.setText("Succès: Covoiturage mis à jour avec succès.");
                    covoiturageIdToUpdate = -1;
                } else {
                    errorLabel.setText("Erreur: Échec de la mise à jour du covoiturage.");
                }
            }
            showCovoiturages();
            clearCovoiturageFields();
        } catch (SQLException e) {
            errorLabel.setText("Erreur SQL: " + e.getMessage());
        }
    }

    @FXML
    private void showCovoiturages() {
        covoiturageData.clear();
        try {
            covoiturageData.addAll(covoiturageService.getAllCovoiturages());
        } catch (SQLException e) {
            errorLabel.setText("Erreur SQL: Erreur lors de la récupération des covoiturages : " + e.getMessage());
        }
    }

    @FXML
    private void supprimerCovoiturage() {
        Covoiturage selected = covoiturageTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            errorLabel.setText("Aucune Sélection: Veuillez sélectionner une ligne à supprimer.");
            return;
        }
        try {
            if (covoiturageService.deleteCovoiturage(selected.getId_covoiturage())) {
                errorLabel.setText("Succès: Covoiturage supprimé avec succès.");
                showCovoiturages();
            } else {
                errorLabel.setText("Erreur: Échec de la suppression du covoiturage.");
            }
        } catch (SQLException e) {
            errorLabel.setText("Erreur SQL: " + e.getMessage());
        }
    }

    @FXML
    private void modifierCovoiturage() {
        Covoiturage selected = covoiturageTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            errorLabel.setText("Aucune Sélection: Veuillez sélectionner une ligne à modifier.");
            return;
        }
        lieu_departField.setText(selected.getLieu_depart());
        lieu_arriveeField.setText(selected.getLieu_arrivee());
        date_heureField.setText(selected.getDate_heure());
        idConducteurCovoitField.setText(String.valueOf(selected.getId_conducteur()));
        idVoitureField.setText(String.valueOf(selected.getId_voiture()));
        places_restantesField.setText(String.valueOf(selected.getPlaces_restantes()));
        statutField.setText(selected.getStatut());
        covoiturageIdToUpdate = selected.getId_covoiturage();
    }

    @FXML
    private void searchCovoiturages() {
        covoiturageData.clear();
        String lieu_depart = searchLieu_departField.getText().trim();
        String lieu_arrivee = searchLieu_arriveeField.getText().trim();
        String date_heure = searchDate_heureField.getText().trim();

        try {
            List<Covoiturage> allCovoiturages = covoiturageService.getAllCovoiturages();
            allCovoiturages.stream()
                    .filter(c -> (lieu_depart.isEmpty() || c.getLieu_depart().toLowerCase().contains(lieu_depart.toLowerCase())) &&
                            (lieu_arrivee.isEmpty() || c.getLieu_arrivee().toLowerCase().contains(lieu_arrivee.toLowerCase())) &&
                            (date_heure.isEmpty() || c.getDate_heure().toLowerCase().contains(date_heure.toLowerCase())))
                    .forEach(covoiturageData::add);

            if (covoiturageData.isEmpty()) {
                errorLabel.setText("Aucun Résultat: Aucun covoiturage trouvé.");
            } else {
                errorLabel.setText("");
            }
        } catch (SQLException e) {
            errorLabel.setText("Erreur SQL: Erreur lors de la recherche des covoiturages : " + e.getMessage());
        }
    }

    private boolean isValidNumeric(String str) {
        if (str == null || str.isEmpty()) return false;
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidDateTime(String dateTime) {
        if (dateTime == null || dateTime.isEmpty()) return false;
        try {
            LocalDateTime.parse(dateTime, DATE_TIME_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private void clearCovoiturageFields() {
        lieu_departField.clear();
        lieu_arriveeField.clear();
        date_heureField.clear();
        idConducteurCovoitField.clear();
        idVoitureField.clear();
        places_restantesField.clear();
        statutField.clear();
        covoiturageIdToUpdate = -1;
        errorLabel.setText("");
    }
}
