package sosexpress.com.br.sosexpres.activitys;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.codemybrainsout.onboarder.AhoyOnboarderActivity;
import com.codemybrainsout.onboarder.AhoyOnboarderCard;

import java.util.ArrayList;
import java.util.List;

import sosexpress.com.br.sosexpres.R;
import sosexpress.com.br.sosexpres.utils.Preferences;

public class IntroScreen extends AhoyOnboarderActivity {

    Preferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = new Preferences(this);

        AhoyOnboarderCard ahoyOnboarderCard1 = new AhoyOnboarderCard(getResources()
                .getString(R.string.slide_1_title),
                getResources().getString(R.string.slide_1_description),
                R.drawable.carro_quebrado);

        ahoyOnboarderCard1.setBackgroundColor(R.color.white);
        ahoyOnboarderCard1.setTitleColor(R.color.black);
        ahoyOnboarderCard1.setDescriptionColor(R.color.black);
        ahoyOnboarderCard1.setTitleTextSize(dpToPixels(10, this));
        ahoyOnboarderCard1.setDescriptionTextSize(dpToPixels(8, this));
        ahoyOnboarderCard1.setIconLayoutParams(300,300, 30, 10, 10,10);

        AhoyOnboarderCard ahoyOnboarderCard2 = new AhoyOnboarderCard(getResources().getString(R.string.slide_2_title), getResources().getString(R.string.slide_2_description), R.drawable.arte_mapa_intro);
        ahoyOnboarderCard2.setBackgroundColor(R.color.white);
        ahoyOnboarderCard2.setTitleColor(R.color.black);
        ahoyOnboarderCard2.setDescriptionColor(R.color.black);
        ahoyOnboarderCard2.setTitleTextSize(dpToPixels(10, this));
        ahoyOnboarderCard2.setDescriptionTextSize(dpToPixels(8, this));
        ahoyOnboarderCard2.setIconLayoutParams(350,280, 30, 10, 10,10);


        AhoyOnboarderCard ahoyOnboarderCard3 = new AhoyOnboarderCard(getResources().getString(R.string.slide_3_title), getResources().getString(R.string.slide_3_description), R.drawable.intro_cds_oficina);
        ahoyOnboarderCard3.setBackgroundColor(R.color.white);
        ahoyOnboarderCard3.setTitleColor(R.color.black);
        ahoyOnboarderCard3.setDescriptionColor(R.color.black);
        ahoyOnboarderCard3.setTitleTextSize(dpToPixels(10
                , this));
        ahoyOnboarderCard3.setDescriptionTextSize(dpToPixels(8, this));
        ahoyOnboarderCard3.setIconLayoutParams(350,400, 30, 10, 10,10);


        //mostrar/ocultar botoes de controle do slide
        showNavigationControls(true);

        //Set pager indicator colors
        setInactiveIndicatorColor(R.color.inactive_slide);
        setActiveIndicatorColor(R.color.active_slide);

        //texto botão
        setFinishButtonTitle("Finalizar");

        //layout do botão de finalizar slider
        setFinishButtonDrawableStyle(ContextCompat.getDrawable(this, R.drawable.rounded_button));
        List<AhoyOnboarderCard> listaSlides = new ArrayList<>();

        //add slide a lista
        listaSlides.add(ahoyOnboarderCard1);
        listaSlides.add(ahoyOnboarderCard2);
        listaSlides.add(ahoyOnboarderCard3);

        //seta lista com os slides
        setOnboardPages(listaSlides);

        //lista de cores para o background
        List<Integer> cores = new ArrayList<>();
        cores.add(R.color.solid_one);
        cores.add(R.color.solid_two);
        cores.add(R.color.solid_three);
        setColorBackground(cores);

    }

    //acao para o botao de finish
    @Override
    public void onFinishButtonPressed() {
        telaAcaoUsuario();
    }

    private void  telaAcaoUsuario(){
        //usuario viu a introducao e seta o valor para true para não exibir da proxima vez que abrir a aplicação
        preferences.setShowIntro(true);
        Intent intent = new Intent(IntroScreen.this, ActivityAcaoUsuario.class);
        startActivity( intent );
        finish();
    }


}
