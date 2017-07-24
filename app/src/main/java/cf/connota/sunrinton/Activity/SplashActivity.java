package cf.connota.sunrinton.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
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

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(getApplicationContext(), "권한 허가", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(getApplicationContext(), "권한을 허가해야 사용할 수 있습니다.\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                finish();
            }
        };

        new TedPermission(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("이 앱을 사용하기 위해 다음과 같은 기능들이 필요합니다.")
                .setPermissions(Manifest.permission.CAMERA)
                .check();

        NeyoAPI.getServer();

        SharedPreferences pref = getSharedPreferences("setting", MODE_PRIVATE);
        SharedManager.pref = pref;
        SharedManager.editor = pref.edit();
        SharedManager.editor.apply();

        String fb_token = SharedManager.pref.getString("fb_token", "");
        Log.e(TAG, "onCreate: pref_token" + fb_token);

        if (fb_token.length() > 0) {
            NeyoAPI.server.userinit(fb_token).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            flag(response.body().string());
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
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
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fbinit();
            }
        });
    }

    private void fbinit() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithPublishPermissions(this, Arrays.asList("publish_actions"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                final String fb_token = loginResult.getAccessToken().getToken();
                Log.e(TAG, "facebook token: " + fb_token);
                SharedManager.editor.putString("fb_token", fb_token);
                NeyoAPI.server.userinit(fb_token).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                flag(response.body().string());
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
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

    private void flag(String s) throws JSONException {
        JSONObject obj = new JSONObject(s);
        String fcm_token = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "token: " + fcm_token);
        SharedManager.editor.putString("fcm_token", fcm_token);
        SharedManager.editor.putString("profile", obj.getString("thumbnail"));
        SharedManager.editor.putInt("_id", obj.getInt("_id"));
        SharedManager.save();
        NeyoAPI.server.updateFCMToken(obj.getInt("_id"), fcm_token);
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}
