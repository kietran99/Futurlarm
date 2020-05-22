package com.se68.rraptor.futurlarm.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.se68.rraptor.futurlarm.Class.Futurlarm;
import com.se68.rraptor.futurlarm.Class.FuturlarmList;
import com.se68.rraptor.futurlarm.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FuturlarmAdapter extends RecyclerView.Adapter<FuturlarmAdapter.ViewHolder> implements Filterable {

    private Context context;
    private FuturlarmList futurlarms;
    private FuturlarmList futurlarmsFull;

    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public FuturlarmAdapter(Context context, FuturlarmList futurlarms) {
        this.context = context;
        this.futurlarms = futurlarms;
        this.futurlarmsFull = new FuturlarmList();
        this.futurlarmsFull.setList(new ArrayList<>(futurlarms.getList()));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.futurlarm_list_item, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final Futurlarm futurlarm = futurlarms.getList().get(i);
        Picasso.get().load(futurlarm.getEventIcon()).placeholder(R.drawable.ic_launcher_foreground).fit().into(viewHolder.imgEvent);
        viewHolder.txtName.setText(futurlarm.getName());
        viewHolder.txtTime.setText(futurlarm.getTime());
        viewHolder.txtDate.setText(futurlarm.getDate());
        viewHolder.layout.setLongClickable(true);
        if (futurlarm.isImportant())
            viewHolder.layout.setBackgroundResource(R.color.importantBackground);
        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //itemDetail.onItemSelected(futurlarm);
            }
        });
    }

    @Override
    public int getItemCount() {
        return futurlarms.getList().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout layout;
        ImageView imgEvent;
        TextView txtName, txtTime, txtDate;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.LayoutListItem);
            imgEvent = itemView.findViewById(R.id.ImageViewEvent);
            txtName = itemView.findViewById(R.id.TextViewListName);
            txtTime = itemView.findViewById(R.id.TextViewListTime);
            txtDate = itemView.findViewById(R.id.TextViewListDate);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                            mListener.onClick(position);
                    }
                }
            });
        }
    }

    @Override
    public Filter getFilter() {
        return futurlarmFilter;
    }

    private Filter futurlarmFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Futurlarm> filteredList = new ArrayList<>();

            if (constraint.length() == 0 || constraint.toString().trim().equals("")){
                filteredList.addAll(futurlarmsFull.getList());
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Futurlarm futurlarm : futurlarmsFull.getList()){
                    if (futurlarm.getName().toLowerCase().trim().contains(filterPattern) ||
                            futurlarm.getSender().toLowerCase().trim().contains(filterPattern)){
                        filteredList.add(futurlarm);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            futurlarms.getList().clear();
            futurlarms.getList().addAll((ArrayList<Futurlarm>) results.values);
            notifyDataSetChanged();
        }
    };

}
