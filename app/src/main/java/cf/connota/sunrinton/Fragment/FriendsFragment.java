package cf.connota.sunrinton.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.nitrico.lastadapter.Holder;
import com.github.nitrico.lastadapter.ItemType;
import com.github.nitrico.lastadapter.LastAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import cf.connota.sunrinton.Activity.OptionActivity;
import cf.connota.sunrinton.BR;
import cf.connota.sunrinton.Model.User;
import cf.connota.sunrinton.R;
import cf.connota.sunrinton.Util.NeyoAPI;
import cf.connota.sunrinton.Util.SharedManager;
import cf.connota.sunrinton.databinding.ItemFriendBinding;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Conota on 2017-07-25.
 */

public class FriendsFragment extends Fragment {
    RecyclerView rv;
    LastAdapter lastAdapter;
    ArrayList<User> userArray = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.friends_fragment, container, false);

        updateUser();

        rv = v.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        lastAdapter = new LastAdapter(userArray, BR.content)
                .map(User.class, new ItemType<ItemFriendBinding>(R.layout.item_friend) {
                    @Override
                    public void onBind(Holder<ItemFriendBinding> holder) {
                        super.onBind(holder);
                        final User u = userArray.get(holder.getLayoutPosition());
                        if (u.isActivate) {
                            holder.getBinding().isActivate.setText(R.string.active);
                            holder.getBinding().isActivate.setTextColor(Color.parseColor("#ff0000"));
                        } else {
                            holder.getBinding().isActivate.setText(R.string.inactive);
                            holder.getBinding().isActivate.setTextColor(Color.parseColor("#8C8D8D"));
                        }
                        holder.getBinding().itemNickname.setText(u.getNickname());
                        Glide.with(getContext()).load(u.getProfile()).into(holder.getBinding().itemProfile);
                        holder.getBinding().containerBox.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(getContext(), OptionActivity.class).putExtra("_id", u.getId()));
                            }
                        });
                    }
                })
                .into(rv);

        return v;
    }

    public void updateUser() {
        int id = SharedManager.pref.getInt("_id", 0);
        if (id == 0) {
            Toast.makeText(getContext(), "에러", Toast.LENGTH_SHORT).show();
            return;
        }
        NeyoAPI.server.getUserList(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray json = new JSONArray(response.body().string());
                        for (int i = 0; i < json.length(); i++) {
                            JSONObject data = json.getJSONObject(i);
                            userArray.add(new User(data.getString("name"), data.getString("thumbnail"), data.getBoolean("online"), data.getInt("_id")));
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getContext(), "에러: " + response.code(), Toast.LENGTH_SHORT).show();
                }

                lastAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


}
