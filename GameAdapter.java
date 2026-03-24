package com.example.gamehub;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {
    
    private List<Game> gameList;
    private Context context;
    
    public GameAdapter(List<Game> gameList, Context context) {
        this.gameList = gameList;
        this.context = context;
    }
    
    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_game, parent, false);
        return new GameViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        Game game = gameList.get(position);
        
        holder.tvGameName.setText(game.getAppName());
        holder.ivGameIcon.setImageDrawable(game.getAppIcon());
        
        // प्ले टाइम दिखाना (अगर 0 से ज्यादा हो)
        if (game.getTotalPlayTime() > 0) {
            long minutes = game.getTotalPlayTime() / 60000;
            holder.tvPlayTime.setText("खेला: " + minutes + " मिनट");
            holder.tvPlayTime.setVisibility(View.VISIBLE);
        } else {
            holder.tvPlayTime.setVisibility(View.GONE);
        }
        
        // गेम पर क्लिक करने पर लॉन्च करें
        holder.itemView.setOnClickListener(v -> {
            PackageManager pm = context.getPackageManager();
            Intent launchIntent = pm.getLaunchIntentForPackage(game.getPackageName());
            if (launchIntent != null) {
                context.startActivity(launchIntent);
            }
        });
        
        // लॉन्ग क्लिक पर डिटेल में जाएं
        holder.itemView.setOnLongClickListener(v -> {
            Intent intent = new Intent(context, GameDetailActivity.class);
            intent.putExtra("packageName", game.getPackageName());
            intent.putExtra("gameName", game.getAppName());
            context.startActivity(intent);
            return true;
        });
    }
    
    @Override
    public int getItemCount() {
        return gameList.size();
    }
    
    public static class GameViewHolder extends RecyclerView.ViewHolder {
        ImageView ivGameIcon;
        TextView tvGameName;
        TextView tvPlayTime;
        
        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            ivGameIcon = itemView.findViewById(R.id.ivGameIcon);
            tvGameName = itemView.findViewById(R.id.tvGameName);
            tvPlayTime = itemView.findViewById(R.id.tvPlayTime);
        }
    }
}
