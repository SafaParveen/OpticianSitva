package com.example.opticiansitwa.intro;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.opticiansitwa.R;
import com.example.opticiansitwa.databinding.ActIntroMainBinding;
import com.example.opticiansitwa.databinding.FragIntroBinding;
import com.example.opticiansitwa.login.Act_Login;

public class Act_IntroMain extends AppCompatActivity {

    private ViewsSliderAdapter mAdapter;
    private TextView[] dots;
    PrefManager preferenceManager;

    ActIntroMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActIntroMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        mAdapter = new ViewsSliderAdapter();
        binding.introVp.setAdapter(mAdapter);

//        preferenceManager = new PrefManager(this);
//        if (!preferenceManager.isFirstTimeLaunch()) {
//            launchHomeScreen();
//            finish();
//        }
//

        binding.skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent loginIntent = new Intent(Act_IntroMain.this, Act_Login.class);
                startActivity(loginIntent);
                finish();

            }
        });


        binding.introVp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position,positionOffset,positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);

                if(position==0)
                {

                    binding.nextBtn.setText("Next");
                    binding.nextBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            int current = 1;
                            binding.introVp.setCurrentItem(current);

                        }
                    });
                    binding.skipBtn.setVisibility(View.VISIBLE);

                }
                else
                {

                    binding.nextBtn.setText("Got it!");
                    binding.skipBtn.setVisibility(View.GONE);
                    binding.nextBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent loginIntent = new Intent(Act_IntroMain.this, Act_Login.class);
                            startActivity(loginIntent);
                            finish();


                        }
                    });

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        addBottomDots(0);

    }

    private void addBottomDots(int currentPage) {

        dots = new TextView[2];

        binding.layoutDots.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.dotInactive));
            binding.layoutDots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(getResources().getColor(R.color.dotActive));
    }

//    private void launchHomeScreen() {
//        preferenceManager.setFirstTimeLaunch(false);
//        startActivity(new Intent(Act_IntroMain.this, Act_Intro.class));
//        finish();
//    }


    public class ViewsSliderAdapter extends RecyclerView.Adapter<Act_IntroMain.IntroViewHolder> {


        @NonNull
        @Override
        public IntroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new IntroViewHolder(FragIntroBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull Act_IntroMain.IntroViewHolder holder, int position) {

            if(position==0)

            {
                holder.fragIntroBinding.mainText1.setText(R.string.main_text);
                holder.fragIntroBinding.subText1.setText(R.string.sub_text);
                holder.fragIntroBinding.imgIntro1.setImageResource(R.drawable.os_intro_img2);


            }

            else
            {
                holder.fragIntroBinding.mainText1.setText(R.string.main_text);
                holder.fragIntroBinding.subText1.setText(R.string.sub_text);
                holder.fragIntroBinding.imgIntro1.setImageResource(R.drawable.os_intro_img3);



            }



        }


        @Override
        public int getItemCount() {
            return 2;
        }
    }

    public class IntroViewHolder extends RecyclerView.ViewHolder {

        FragIntroBinding fragIntroBinding;

        public IntroViewHolder(FragIntroBinding fragIntro1Binding) {
            super(fragIntro1Binding.getRoot());
            this.fragIntroBinding=fragIntro1Binding;

        }
    }
}


