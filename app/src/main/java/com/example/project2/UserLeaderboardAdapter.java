package com.example.project2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2.database.entities.User;

import java.util.ArrayList;
import java.util.List;

public class UserLeaderboardAdapter extends RecyclerView.Adapter<UserLeaderboardAdapter.LeaderboardViewHolder> {

    private final List<User> users = new ArrayList<>();

    public void setUsers(List<User> newUsers) {
        users.clear();
        if (newUsers != null) {
            users.addAll(newUsers);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_leaderboard_row, parent, false);
        return new LeaderboardViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardViewHolder holder, int position) {
        User user = users.get(position);
        int rank = position + 1;

        holder.rankTextView.setText("#" + rank);
        holder.usernameTextView.setText(user.getUsername());
        holder.pointsTextView.setText(user.getPoints() + " pts");
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class LeaderboardViewHolder extends RecyclerView.ViewHolder {

        TextView rankTextView;
        TextView usernameTextView;
        TextView pointsTextView;

        LeaderboardViewHolder(@NonNull View itemView) {
            super(itemView);
            rankTextView = itemView.findViewById(R.id.leaderboardRankTextView);
            usernameTextView = itemView.findViewById(R.id.leaderboardUsernameTextView);
            pointsTextView = itemView.findViewById(R.id.leaderboardPointsTextView);
        }
    }
}
