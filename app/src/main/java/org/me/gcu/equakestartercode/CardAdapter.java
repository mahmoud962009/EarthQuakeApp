package org.me.gcu.equakestartercode;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.Holder> {
    Context context;
    List<card> cardList;

    public CardAdapter(Context context, List<card> cardList) {
        this.context = context;
        this.cardList = cardList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {


        holder.Title.setText(""+ cardList.get(position).getLocation());
        holder.Magnitude.setText(""+ cardList.get(position).getMagnitude());


        double magnitude = cardList.get(position).getMagnitude();

        if (magnitude >= 2.6) {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.strong));
        } else if (magnitude <= 2.5 && magnitude >= 1.6) {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.moderate));
        }  else if (magnitude <= 1.5 && magnitude >= 0.5) {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.light));
        }
        else {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.minor));
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("card", cardList.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView Title, Magnitude;

        public Holder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            Title = itemView.findViewById(R.id.title);
            Magnitude = itemView.findViewById(R.id.magnitude);

        }
    }


}
