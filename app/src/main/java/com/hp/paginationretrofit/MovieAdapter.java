package com.hp.paginationretrofit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hp.paginationretrofit.model.MovieList;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MVH> {
    MovieList movieLists;
    Context context;
    List<MovieList.ResultsBean>Items;

    public MovieAdapter(MovieList movieLists, Context context, List<MovieList.ResultsBean> items) {
        this.movieLists = movieLists;
        this.context = context;
        Items = items;
    }

    public MovieAdapter(Context context) {
        Items = new ArrayList<>();
        context = context;
    }

    @NonNull
    @Override
    public MVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movielistitem, parent, false);

        return new MVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MVH holder, int position) {
      //  holder.movieName.setText(movieLists.getResults().get(position).getTitle().trim());

        MovieList.ResultsBean model = Items.get(position);

        //Picasso.get().load(model.getAvatar()).into(holder.imgUserAvatar);
      //  holder.txtFirstName.setText("First Name: "+ model.getFirstName());
        holder.movieName.setText("Last Name: "+ model.getTitle());

    }

    @Override
    public int getItemCount() {
        return Items.size();
    }

    public class MVH extends RecyclerView.ViewHolder{

        ImageView poster;
        TextView movieName;
        public MVH(@NonNull View itemView) {
            super(itemView);
            poster=itemView.findViewById(R.id.poster);
            movieName=itemView.findViewById(R.id.moviename);



        }
    }
    public void setItems(List<MovieList.ResultsBean> items){
        Items = items;
        notifyDataSetChanged();
    }

    public void addItems(List<MovieList.ResultsBean> items){
        Items.addAll(items);
        notifyDataSetChanged();;
    }


}
