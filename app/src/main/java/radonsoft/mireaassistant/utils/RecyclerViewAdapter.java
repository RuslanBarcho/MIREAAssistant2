package radonsoft.mireaassistant.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import radonsoft.mireaassistant.database.TableSchedule;
import radonsoft.mireaassistant.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    private List<TableSchedule> tableScheduleList = new ArrayList<TableSchedule>();

    public RecyclerViewAdapter(List<TableSchedule> tableScheduleList){
        this.tableScheduleList = tableScheduleList;
    }

    public void updateData(List<TableSchedule> newData){
        this.tableScheduleList = newData;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.schedule_item, parent, false);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, final int position) {
        switch(position){
            case 0: {
                holder.beginTime.setText("09:00");
                holder.endTime.setText("10:30");
            }
            break;
            case 1: {
                holder.beginTime.setText("10:40");
                holder.endTime.setText("12:10");
            }
            break;
            case 2: {
                holder.beginTime.setText("13:00");
                holder.endTime.setText("14:30");
            }
            break;
            case 3: {
                holder.beginTime.setText("14:40");
                holder.endTime.setText("16:10");
            }
            break;
            case 4: {
                holder.beginTime.setText("16:20");
                holder.endTime.setText("17:50");
            }
            break;
            case 5: {
                holder.beginTime.setText("18:00");
                holder.endTime.setText("19:30");
            }
            break;
        }
        String nameToSet;
        if (!tableScheduleList.get(position).getType().equals("")){
            nameToSet = tableScheduleList.get(position).getName() + ", " + tableScheduleList.get(position).getType();
        } else {
            nameToSet = tableScheduleList.get(position).getName();
        }

        holder.name.setText(nameToSet);
        holder.room.setText(tableScheduleList.get(position).getClassroom());
        holder.teacher.setText(tableScheduleList.get(position).getTeacher());
    }

    @Override
    public int getItemCount() {
        return tableScheduleList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView room;
        public TextView teacher;

        public TextView beginTime;
        public TextView endTime;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_name);
            room = itemView.findViewById(R.id.item_class);
            teacher = itemView.findViewById(R.id.item_professor);
            beginTime = itemView.findViewById(R.id.item_begin_time);
            endTime = itemView.findViewById(R.id.item_end_time);
        }
    }
}
