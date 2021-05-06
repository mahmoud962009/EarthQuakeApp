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

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.Holder> {
    Context context;
    List<card> cardList;
    int highestIndex;
    int lowestIndex;
    int highestLatitudeId, lowestLatitudeId, highestLongitudeId, lowestLongitudeId;


    public FilterAdapter(Context context, List<card> cardList, int highestIndex, int lowestIndex, int highestLatitudeId, int lowestLatitudeId, int highestLongitudeId, int lowestLongitudeId) {
        this.context = context;
        this.cardList = cardList;
        this.highestIndex = highestIndex;
        this.lowestIndex = lowestIndex;
        this.highestLatitudeId = highestLatitudeId;
        this.lowestLatitudeId = lowestLatitudeId;
        this.highestLongitudeId = highestLongitudeId;
        this.lowestLongitudeId = lowestLongitudeId;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_filter, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {


        holder.magnitude.setText("" + cardList.get(position).getMagnitude());
        holder.area.setText("" + cardList.get(position).getLocation());


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


        if (position == lowestIndex) {
            holder.depth.setVisibility(View.VISIBLE);
            holder.depth.setText("Shallowest: "+ cardList.get(position).getDepth());
        } else if (position == highestIndex) {
            holder.depth.setVisibility(View.VISIBLE);
            holder.depth.setText("Deepest: "+ cardList.get(position).getDepth());
        } else {
            holder.depth.setVisibility(View.GONE);
        }


        if (cardList.get(position).getId() == highestLatitudeId) {
            holder.extremity.setVisibility(View.VISIBLE);
            holder.extremity.setText("Most Northerly");
        } else if (cardList.get(position).getId() == lowestLatitudeId) {
            holder.extremity.setVisibility(View.VISIBLE);
            holder.extremity.setText("Most Southerly");
        } else if (cardList.get(position).getId() == highestLongitudeId) {
            holder.extremity.setVisibility(View.VISIBLE);
            holder.extremity.setText("Most Easterly");
        } else if (cardList.get(position).getId() == lowestLongitudeId) {
            holder.extremity.setVisibility(View.VISIBLE);
            holder.extremity.setText("Most Westerly");
        } else {
            holder.extremity.setVisibility(View.GONE);
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
        TextView magnitude, depth, extremity, area;

        public Holder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            magnitude = itemView.findViewById(R.id.magnitude);
            depth = itemView.findViewById(R.id.depth);
            extremity = itemView.findViewById(R.id.extremity);
            area = itemView.findViewById(R.id.area);
        }
    }

    public void reloadData(int highestIndex, int lowestIndex, int highestLatitudeId, int lowestLatitudeId, int highestLongitudeId, int lowestLongitudeId) {
        this.highestIndex = highestIndex;
        this.lowestIndex = lowestIndex;
        this.highestLatitudeId = highestLatitudeId;
        this.lowestLatitudeId = lowestLatitudeId;
        this.highestLongitudeId = highestLongitudeId;
        this.lowestLongitudeId = lowestLongitudeId;
        notifyDataSetChanged();
    }
}
