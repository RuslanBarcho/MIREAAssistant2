package radonsoft.mireaassistant.utils;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import radonsoft.mireaassistant.database.TableSchedule;
import radonsoft.mireaassistant.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    private List<TableSchedule> tableScheduleList;

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
        Pair<String, String> time = LessonTimeGenerator.getTiming(position);
        holder.beginTime.setText(time.first);
        holder.endTime.setText(time.second);
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
        TextView teacher;
        TextView beginTime;
        TextView endTime;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_name);
            room = itemView.findViewById(R.id.item_class);
            teacher = itemView.findViewById(R.id.item_professor);
            beginTime = itemView.findViewById(R.id.item_begin_time);
            endTime = itemView.findViewById(R.id.item_end_time);
        }
    }
}
