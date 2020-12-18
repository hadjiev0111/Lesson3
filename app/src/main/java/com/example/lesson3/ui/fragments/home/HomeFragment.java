package com.example.lesson3.ui.fragments.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lesson3.data.models.WeatherModel;
import com.example.lesson3.data.network.WeatherService;
import com.example.lesson3.databinding.FragmentHomeBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View v = binding.getRoot();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setWeatherStats();
    }

    private void setWeatherStats() {
        WeatherService.getInstance().getCurrentWeather("bishkek","4bbf5a1ed98cd9f688ebb3651474cdd2").enqueue(new Callback<WeatherModel>() {
            @Override
            public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherModel weatherModel = response.body();
                    Log.d("AZIZ", "yaai" + weatherModel.getName());
                    binding.locationHomeFrag.setText("city: " + weatherModel.getName() + " " + weatherModel.getSys().getCountry());
                    binding.humidityPercent.setText(weatherModel.getMain().getHumidity() + "%");
                    binding.temperature.setText(String.valueOf(weatherModel.getMain().getTemp()));
                    binding.pressureMp.setText(weatherModel.getMain().getPressure() + "mp");
                    binding.mSWind.setText(weatherModel.getWind().getDeg() + " " + weatherModel.getWind().getSpeed().toString() + "m/s");
                    binding.percentCloudiness.setText(weatherModel.getClouds().getAll() + "%");
                    binding.temperatureMin.setText("min: " + weatherModel.getMain().getTempMin());
                    binding.temperatureMax.setText("max: " + weatherModel.getMain().getTempMax());
                    Date dateRise = new Date(weatherModel.getSys().getSunrise());
                    SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                    binding.timeSunrise.setText(df.format(dateRise));
                    Date dateSet = new Date(weatherModel.getSys().getSunset());
                    binding.timeSunset.setText(df.format(dateSet));
                }

                Log.d("AZIZ", "onResponse: "+"СРАБОТАЛО");


            }

            @Override
            public void onFailure(Call<WeatherModel> call, Throwable t) {
                Log.d("anime", t.getMessage());
               // Log.d("AZIZ", "onFailure: " + t.getLocalizedMessage() + " RRRRRR " + t.getMessage());
            }
        });

        Date currentTime = new Date(System.currentTimeMillis());
        DateFormat dfTime = new SimpleDateFormat("HH:mm");
        binding.timeHomeFrag.setText(dfTime.format(currentTime));
        Date currentDate = new Date(System.currentTimeMillis());
        DateFormat dfDate = new SimpleDateFormat("dd-MMM-yyyy");
        binding.dateHomeFrag.setText(dfDate.format(currentDate));
    }
}