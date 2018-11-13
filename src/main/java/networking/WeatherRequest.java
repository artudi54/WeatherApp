package main.java.networking;

import javafx.application.Platform;
import main.java.exception.WeatherRequestException;
import main.java.weather.types.UnitsFormat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeatherRequest {
    private static final Pattern UNITS_PATTERN = Pattern.compile("units=(\\w+)");

    public interface OnError {
        void onError(Exception exc);
    }
    public interface OnResponseReady {
        void onResponseReady(WeatherResponse response);
    }

    class ResponseReader implements Runnable {
        @Override
        public void run() {
            try {
                HttpURLConnection httpConnection = (HttpURLConnection)requestUrl.openConnection();
                httpConnection.setConnectTimeout(5000);
                httpConnection.setReadTimeout(10000);
                httpConnection.setInstanceFollowRedirects(false);
                InputStream inputStream = httpConnection.getInputStream();
                InputStreamReader streamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(streamReader);

                StringBuilder responseBuilder = new StringBuilder();
                String inputLine;
                while ((inputLine = bufferedReader.readLine()) != null)
                    responseBuilder.append(inputLine);

                bufferedReader.close();
                httpConnection.disconnect();


                if (onResponseReady != null) {
                    WeatherResponse weatherResponse = new WeatherResponse(responseBuilder.toString(),
                                                                          acquisitionType,
                                                                          unitsFormat);
                    Platform.runLater(() -> onResponseReady.onResponseReady(weatherResponse));
                }
            }
            catch (IOException exc) {
                if (onError != null)
                    Platform.runLater(() -> onError.onError(exc));
            }
        }
    }

    private static UnitsFormat getUnitsFormat(URL requestUrl) {
        UnitsFormat result = UnitsFormat.DEFAULT;
        Matcher matcher = UNITS_PATTERN.matcher(requestUrl.toString());
        if (matcher.find())
            result = UnitsFormat.fromString(matcher.group(1));
        return result;
    }

    private static AcquisitionType getAcquisitionType(URL requestUrl) {
        String requestStr = requestUrl.toString();
        if (requestStr.contains("id="))
            return AcquisitionType.BY_ID;
        if (requestStr.contains("q="))
            return AcquisitionType.BY_CITY;
        if (requestStr.contains("lat="))
            return AcquisitionType.BY_COORDINATES;
        return null;
    }

    private URL requestUrl;
    private AcquisitionType acquisitionType;
    private OnError onError;
    private OnResponseReady onResponseReady;
    private ExecutorService executorService;
    private UnitsFormat unitsFormat;


    public WeatherRequest(URL requestUrl, ExecutorService executorService) {
        this.requestUrl = Objects.requireNonNull(requestUrl, "requestUrl cannot be null");
        this.executorService = Objects.requireNonNull(executorService, "executorService cannot be null");
        this.acquisitionType = getAcquisitionType(requestUrl);
        this.unitsFormat = getUnitsFormat(requestUrl);
    }

    public void setOnError(OnError onError) {
        this.onError = onError;
    }

    public void setOnResponseReady(OnResponseReady onResponseReady) {
        this.onResponseReady = onResponseReady;
    }

    public void asyncRun() {
        executorService.execute(new ResponseReader());
    }
}
