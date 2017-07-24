package cf.connota.sunrinton.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Arrays;

import cf.connota.sunrinton.R;
import cf.connota.sunrinton.Util.NeyoAPI;
import cf.connota.sunrinton.Util.SharedManager;
import cf.connota.sunrinton.databinding.ActivitySplashBinding;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding binding;
    public static final String TAG = "SplashActivity";
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);

        SharedPreferences pref = getSharedPreferences("setting", MODE_PRIVATE);
        SharedManager.pref = pref;
        SharedManager.editor = pref.edit();
        SharedManager.editor.apply();

        String token = SharedManager.pref.getString("token", "");
        if (token.length() > 0) {
            NeyoAPI.server.userinit(token).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        String fcm_token = FirebaseInstanceId.getInstance().getToken();
                        Log.e(TAG, "token: " + fcm_token);
                        SharedManager.editor.putString("fcm_token", fcm_token);
                        SharedManager.save();
                    } else {
                        Toast.makeText(SplashActivity.this, "에러: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }


    }

    private void fbinit() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithPublishPermissions(this, Arrays.asList("publish_actions"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                final String fb_token = loginResult.getAccessToken().getToken();
                Log.e(TAG, "facebook token: " + fb_token);
                NeyoAPI.server.userinit(fb_token).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
//                                String raw = response.body().string();
//                                JSONObject obj = new JSONObject(raw);
                            SharedManager.editor.putString("token", fb_token);
                            String fcm_token = FirebaseInstanceId.getInstance().getToken();
                            Log.e(TAG, "token: " + fcm_token);
                            SharedManager.editor.putString("fcm_token", fcm_token);
                            SharedManager.save();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Toast.makeText(getApplicationContext(), "에러:" + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "취소", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                error.printStackTrace();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
